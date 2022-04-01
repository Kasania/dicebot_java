/*
 * Author : Kasania
 * Filename : DiceResult
 * Desc :
 */
package com.kasania.dicebot.v1;

import java.util.List;

public class DiceResult {

    public final String query;

    public final List<DiceBunch> diceBunches;
    public final List<String> judgements;

    public String result;

    public DiceResult(String query, List<DiceBunch> diceBunches, List<String> judgements) {
        this.query = query.replace("#","<=")
                .replace("$",">=");

        this.diceBunches = diceBunches;
        this.judgements = judgements;
    }

    @Override
    public String toString() {
        return "DiceResult{" +
                "query='" + query + '\'' +
                ", diceBunches=" + diceBunches +
                ", judgements=" + judgements +
                ", result='" + result + '\'' +
                '}';
    }
}
