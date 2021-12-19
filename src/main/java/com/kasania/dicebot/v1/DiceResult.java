/*
 * Author : Kasania
 * Filename : DiceResult
 * Desc :
 */
package com.kasania.dicebot.v1;

import java.util.ArrayList;
import java.util.List;

public class DiceResult {

    public List<String> dices;

    public int sum;

    public String query;


    public DiceResult(){

        dices = new ArrayList<>();
    }

    public void addDice(long value){
        dices.add(String.valueOf(value));
        sum += value;
    }

    public void addDice(String value){
        dices.add(value);
        if(value.equals("-")){
            sum -= 1;
        }
        else{
            sum += 1;
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
