/*
 * Author : Kasania
 * Filename : DnDDice
 * Desc :
 */
package com.kasania.dicebot.v1;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class DnDDice {

    public MessageEmbed command_rDnD(@NotNull MessageReceivedEvent event){

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":game_die: DnD 5th 캐릭터 메이킹");
        StackDice stackDice = new StackDice();

        DiceResult[] diceResults = new DiceResult[6];

        for (int i = 0; i < 6; i++) {
            diceResults[i] = stackDice.calcExpr("4d6");
        }

        StringBuilder printValue = new StringBuilder();
        StringBuilder diceValue = new StringBuilder();

        for (DiceResult diceResult : diceResults) {
            for (DiceBunch diceBunch : diceResult.diceBunches) {
                diceValue.append("[");
                for (String dice : diceBunch.dices) {
                    diceValue.append(dice).append(", ");
                }
                diceValue.append(diceBunch.query).append("]\n");
            }
        }
        long totalSum = 0L;
        StringBuilder desc = new StringBuilder();

        for (DiceResult diceResult : diceResults) {
            diceResult.diceBunches.get(0).dropLow();
            for (DiceBunch diceBunch : diceResult.diceBunches) {
                long sum = 0L;
                printValue.append("[");
                for (String dice : diceBunch.dices) {
                    sum += Long.parseLong(dice);
                    printValue.append(dice).append(", ");
                }
                desc.append(sum).append(", ");
                totalSum += sum;
                printValue.delete(printValue.length()-2,printValue.length()).append(" => ").append(sum).append("]\n");
            }
        }

        embed.setDescription("**" + desc.substring(0,desc.length()-2) + "**");

        embed.addField("능력치 목록", printValue.toString(),true);
        embed.addField("주사위 목록", diceValue.toString(),true);

        embed.addField("능력치 합", String.valueOf(totalSum),false);

        return embed.build();
    }
}
