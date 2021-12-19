/*
 * Author : Kasania
 * Filename : EmbedBuilder
 * Desc :
 */
package com.kasania.dicebot.common;

import com.kasania.dicebot.v1.DiceResult;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class SimpleEmbedMessage {

    public static MessageEmbed titleDescEmbed(String title, String desc){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(desc);
        return embed.build();
    }

    public static MessageEmbed diceEmbed(String result, String queryMessage, List<DiceResult> diceResults, List<String> judgements){
        EmbedBuilder embed = new EmbedBuilder();
        if(result.length()>240){
            embed.setTitle(":game_die: 결과");
            embed.addField("결과", result, true);
        }else{
            embed.setTitle(":game_die: 결과 : " + result);
        }
        if(queryMessage.length()<1024){
            embed.addField("명령","`"+queryMessage+"`",false);
        }

        if(!judgements.isEmpty()){
            StringBuilder judgementValue = new StringBuilder();
            for (String judgement : judgements) {
                judgementValue.append("[")
                        .append(judgement)
                        .append("]\n");
            }
            embed.addField("판정 목록",judgementValue.toString(),false);
        }
        if(!diceResults.isEmpty()){
            StringBuilder diceValue = new StringBuilder();
            for (DiceResult diceResult : diceResults) {
                diceValue.append("[");
                for (String dice : diceResult.dices) {
                    diceValue.append(dice).append(", ");
                }
                diceValue.append(diceResult.query).append("]\n");
            }

            if(diceValue.length()>1024){
                diceValue.setLength(0);
                for (DiceResult diceResult : diceResults) {
                    diceValue.append("[");
                    diceValue.append(diceResult.query).append("]\n");
                }
            }

            if(diceValue.length() <= 1024){
                embed.addField("주사위 목록",diceValue.toString(),false);
            }
        }

        return embed.build();
    }
}
