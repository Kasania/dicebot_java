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

    @Override
    public String toString() {
        return "DiceResult{" +
                "dices=" + dices +
                ", sum=" + sum +
                ", query='" + query + '\'' +
                '}';
    }
}
