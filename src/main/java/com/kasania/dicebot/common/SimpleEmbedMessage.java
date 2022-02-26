/*
 * Author : Kasania
 * Filename : EmbedBuilder
 * Desc :
 */
package com.kasania.dicebot.common;

import com.kasania.dicebot.v1.DiceBunch;
import com.kasania.dicebot.v1.DiceResult;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class SimpleEmbedMessage {

    public static void replyTitleDesc(@NotNull MessageReceivedEvent event,String title, String desc){
        event.getMessage().replyEmbeds(SimpleEmbedMessage.titleDescEmbed(title,desc)).queue();
    }

    public static MessageEmbed titleDescEmbed(String title, String... desc){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(String.join("\n",desc));
        return embed.build();
    }

    public static MessageEmbed titleDescEmbed(String title, String desc){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(desc);
        return embed.build();
    }

    public static MessageEmbed diceEmbed(DiceResult result){
        EmbedBuilder embed = new EmbedBuilder();
        if(result.result.length()>240){
            embed.setTitle(":game_die: 결과");
            embed.addField("결과", result.result, true);
        }else{
            embed.setTitle(":game_die: 결과 : " + result.result);
        }
        if(result.query.length()<1024){
            embed.addField("명령","`"+result.query+"`",false);
        }

        if(!result.judgements.isEmpty()){
            StringBuilder judgementValue = new StringBuilder();
            for (String judgement : result.judgements) {
                judgementValue.append("[")
                        .append(judgement)
                        .append("]\n");
            }
            embed.addField("판정 목록",judgementValue.toString(),false);
        }
        if(!result.diceBunches.isEmpty()){
            StringBuilder diceValue = new StringBuilder();
            for (DiceBunch diceBunch : result.diceBunches) {
                diceValue.append("[");
                for (String dice : diceBunch.dices) {
                    diceValue.append(dice).append(", ");
                }
                diceValue.append(diceBunch.query).append("]\n");
            }

            if(diceValue.length()>1024){
                diceValue.setLength(0);
                for (DiceBunch diceBunch : result.diceBunches) {
                    diceValue.append("[");
                    diceValue.append(diceBunch.query).append("]\n");
                }
            }

            if(diceValue.length() <= 1024){
                embed.addField("주사위 목록",diceValue.toString(),false);
            }
        }

        return embed.build();
    }
}
