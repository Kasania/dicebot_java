/*
 * Author : Kasania
 * Filename : BasicDice
 * Desc :
 */
package com.kasania.dicebot.v1;

import com.kasania.dicebot.common.SimpleEmbedMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BasicDice {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void command_r(@NotNull MessageReceivedEvent event){
        String message = event.getMessage().getContentDisplay();
        String queryString = message.split(" ")[1];
        List<DiceResult> diceResults = new ArrayList<>();
        List<String> judgements = new ArrayList<>();

        event.getMessage().replyEmbeds(rollDice(queryString, diceResults, judgements)).queue();
    }

    public void command_ra(@NotNull MessageReceivedEvent event){



    }

    public void command_rt(@NotNull MessageReceivedEvent event){

    }

    public void command_rl(@NotNull MessageReceivedEvent event){

    }

    public void command_rd(@NotNull MessageReceivedEvent event){

    }


    public MessageEmbed rollDice(String queryMessage, List<DiceResult> diceResults, List<String> judgements){

        MessageEmbed embed;

        try{
            String result = new StackDice()
                    .calcExpr(queryMessage
                            .replace("<=","#")
                            .replace(">=","$"),diceResults, judgements);
            embed = SimpleEmbedMessage.diceEmbed(result, queryMessage, diceResults, judgements);

        }
        catch (AssertionError assertionError){
            assertionError.printStackTrace();
            embed = SimpleEmbedMessage.titleDescEmbed(":warning: 주사위 값이 잘못되었습니다.",
                    "주사위의 개수나 값을 조절해 주세요.");
        }
        catch (Exception e){
            e.printStackTrace();
            embed = SimpleEmbedMessage.titleDescEmbed(":x: 지원하지 않는 명령어입니다.",
                    "명령어를 확인해 주세요.");
        }

        return embed;
    }



}
