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
        sum = BigDecimal.ZERO;
    }

    public void addDice(long value){
        dices.add(String.valueOf(value));
        sum = sum.add(BigDecimal.valueOf(value));
    }

    public void addDice(String value){
        dices.add(value);
        if(value.equals("-")){
            sum = sum.subtract(BigDecimal.ONE);
        }
        else{
            sum = sum.add(BigDecimal.ONE);
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
                ", sum=" + sum +
                ", query='" + query + '\'' +
                '}';
    }
}
