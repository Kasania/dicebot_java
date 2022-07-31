/*
 * Author : Kasania
 * Filename : DiceResult
 * Desc :
 */
package com.kasania.dicebot.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DiceBunch {

    public List<String> dices;

    public BigDecimal sum;

    public String query;

    public DiceBunch(){
        dices = new ArrayList<>();
    }

    public BigDecimal getSum(){
        BigDecimal sum = BigDecimal.ZERO;
        for (String dice : dices) {
            if(dice.equals("+")){
                sum = sum.add(BigDecimal.ONE);
            }else if(dice.equals("-")){
                sum = sum.subtract(BigDecimal.ONE);
            }
            else{
                sum = sum.add(BigDecimal.valueOf(Long.parseLong(dice)));
            }
        }
        return sum;
    }

    public void addDice(long value){
        dices.add(String.valueOf(value));
    }

    public void addDice(String value){
        dices.add(value);
    }

    public void keepHigh(long num){
        int size = dices.size();
        for (long i = 0; i < size-num; i++) {
            dropLow();
        }
    }

    public void keepLow(long num){
        int size = dices.size();
        for (long i = 0; i < size-num; i++) {
            dropHigh();
        }
    }

    public void dropHigh(long num){
        for (long i = 0; i < num; i++) {
            dropHigh();
        }
    }

    public void dropLow(long num){
        for (long i = 0; i < num; i++) {
            dropLow();
        }
    }

    public void dropHigh(){
        long value = Long.parseLong(dices.get(0));
        int pos = 0;
        for (int i = 1; i < dices.size(); i++) {
            long currentValue = Long.parseLong(dices.get(i));
            if(value < currentValue){
                value = currentValue;
                pos = i;
            }

        }
        dices.remove(pos);
    }

    public void dropLow(){
        long value = Long.parseLong(dices.get(0));
        int pos = 0;
        for (int i = 1; i < dices.size(); i++) {
            long currentValue = Long.parseLong(dices.get(i));
            if(value > currentValue){
                value = currentValue;
                pos = i;
            }

        }
        dices.remove(pos);
    }

    @Override
    public String toString() {
        return "DiceResult{" +
                "dices=" + dices +
                ", sum=" + getSum() +
                ", query='" + query + '\'' +
                '}';
    }
}
