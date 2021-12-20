/*
 * Author : Kasania
 * Filename : StackDice
 * Desc :
 */
package com.kasania.dicebot.v1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class StackDice {
    private final static Map<String,Integer> prec = new HashMap<>();
    static {
        prec.put("f", 7);
        prec.put("d", 6);
        prec.put("h", 5);
        prec.put("l", 5);
        prec.put("*", 4);
        prec.put("/", 4);
        prec.put("+", 3);
        prec.put("-", 3);
        prec.put("#", 2);
        prec.put("$", 2);
        prec.put("<", 2);
        prec.put(">", 2);
        prec.put("(", 1);
    }


    public DiceResult calcExpr(String query){
        DiceResult diceResult = new DiceResult(query,new ArrayList<>(), new ArrayList<>());

        postfixEval(convertExpr(splitTokens(query)), diceResult);

        return diceResult;
    }

    private List<String> splitTokens(String expr){

        List<String> tokens = new ArrayList<>();
        BigDecimal val = BigDecimal.ZERO;
        boolean valProc = false;

        for (char c : expr.toCharArray()) {

            if(c == ' ') continue;

            if("0123456789".chars().anyMatch(value -> value == c)){
                val = val.multiply(BigDecimal.TEN).add(BigDecimal.valueOf(Character.getNumericValue(c)));
                valProc = true;
            }
            else {
                if (valProc) {
                    tokens.add(String.valueOf(val));
                    val = BigDecimal.ZERO;
                }
                valProc = false;
                tokens.add(String.valueOf(c));
            }
        }

        if(valProc){
            tokens.add(String.valueOf(val));
        }

        return tokens;
    }

    private List<String> convertExpr(List<String> tokens){

        Stack<String> opStack = new Stack<>();
        List<String> postfixList = new ArrayList<>();

        for (String token : tokens) {

            try{
                postfixList.add(new BigDecimal(token).toPlainString());
                continue;
            }catch (NumberFormatException ignored){}

            if(token.equals("(")){
                opStack.push(token);
            }
            else if(token.equals(")")){
                while(!opStack.peek().equals("(")){
                    postfixList.add(opStack.pop());
                }
                opStack.pop();
            }
            else {
                if (!opStack.isEmpty()) {
                    while (opStack.size() > 0) {
                        if (prec.get(opStack.peek()) >= prec.get(token)) {
                            postfixList.add(opStack.pop());
                        } else {
                            break;
                        }
                    }
                }
                opStack.push(token);
            }

        }
        while(!opStack.isEmpty()){
            postfixList.add(opStack.pop());
        }
        return postfixList;
    }

    private void postfixEval(List<String> tokens, DiceResult diceResult){
        Stack<String> valStack = new Stack<>();

        boolean isJudgementResult = false;

        for (String token : tokens) {
            try{
                valStack.add(new BigDecimal(token).toPlainString());
                continue;
            }catch (NumberFormatException ignored){}

            if(token.equals("f")){
                valStack.push(token);
            }
            else if(token.equals("d")){
                String n1 = valStack.pop();
                BigDecimal n2 = BigDecimal.ONE;
                if(!valStack.isEmpty()){
                    n2 = new BigDecimal(valStack.pop());
                }
                DiceBunch result = rollDice(n2,n1);
                diceResult.diceBunches.add(result);
                valStack.push(String.valueOf(result.sum));
                isJudgementResult = false;
            }

            else{
                String n1 = valStack.pop();
                String n2 = valStack.pop();

                final int judge = new BigDecimal(n2).compareTo(new BigDecimal(n1));

                switch (token) {
                    case "+" -> {
                        valStack.push(new BigDecimal(n2).add(new BigDecimal(n1)).toPlainString());
                        isJudgementResult = false;
                    }
                    case "-" -> {
                        valStack.push(new BigDecimal(n2).subtract(new BigDecimal(n1)).toPlainString());
                        isJudgementResult = false;
                    }
                    case "*" -> {
                        valStack.push(new BigDecimal(n2).multiply(new BigDecimal(n1)).toPlainString());
                        isJudgementResult = false;
                    }
                    case "/" -> {
                        valStack.push(new BigDecimal(n2).divide(new BigDecimal(n1), RoundingMode.DOWN).toPlainString());
                        isJudgementResult = false;
                    }
                    case "#" -> {

                        boolean result = judge <= 0;
                        diceResult.judgements.add(
                                n2 + "<=" + n1 + ", " + (result ? "성공" : "실패")
                        );

                        valStack.push(String.valueOf(result ? 1 : 0));
                        isJudgementResult = true;
                    }
                    case "$" -> {
                        boolean result = judge >= 0;
                        diceResult.judgements.add(
                                n2 + ">=" + n1 + ", " + (result ? "성공" : "실패")
                        );
                        valStack.push(String.valueOf(result ? 1 : 0));
                        isJudgementResult = true;
                    }
                    case "<" -> {
                        boolean result = judge < 0;
                        diceResult.judgements.add(
                                n2 + "<" + n1 + ", " + (result ? "성공" : "실패")
                        );
                        valStack.push(String.valueOf(result ? 1 : 0));
                        isJudgementResult = true;
                    }
                    case ">" -> {
                        boolean result = judge > 0;
                        diceResult.judgements.add(
                                n2 + ">" + n1 + ", " + (result ? "성공" : "실패")
                        );
                        valStack.push(String.valueOf(result ? 1 : 0));
                        isJudgementResult = true;
                    }
                }
            }


        }
        String result = valStack.pop();
        diceResult.result = isJudgementResult? result.equals("1") ? "성공" : "실패" : result;
    }

    private DiceBunch rollDice(BigDecimal x, String y){

        DiceBunch diceBunch = new DiceBunch();

        if(x.compareTo(BigDecimal.valueOf(1000)) > 0){
            throw new AssertionError("num of dice is too large");
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if(y.equals("f")) {
            for (int i = 0; i < x.intValue(); i++) {
                long rand = random.nextLong(0,2)-1;
                if(rand == -1){
                    diceBunch.addDice("-");
                }
                else if(rand == 1){
                    diceBunch.addDice("+");
                }
                else{
                    diceBunch.addDice(rand);
                }
            }

        }
        else if(y.equals("0")){
            throw new AssertionError("num of die is 0");
        }
        else if(new BigDecimal(y).compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) > 0){
            throw new AssertionError("num of die value is too large");
        }
        else{
            for (int i = 0; i < x.intValue(); i++) {

                long rand = random.nextLong(1, Long.parseLong(y));
                diceBunch.addDice(rand);
            }
        }

        diceBunch.query = x + "d"+ y + "=" + diceBunch.sum;
        return diceBunch;
    }

}
