/*
 * Author : Kasania
 * Filename : BasicDice
 * Desc :
 */
package com.kasania.dicebot.v1;

import com.kasania.dicebot.common.SimpleEmbedMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BasicDice {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<Player,Map<String,String>> diceAliases = new HashMap<>();


    public void command_r(@NotNull MessageReceivedEvent event){
        String message = event.getMessage().getContentDisplay();
        String queryString = message.split(" ")[1];

        event.getMessage().replyEmbeds(rollDice(queryString)).queue();
    }

    public void command_ra(@NotNull MessageReceivedEvent event){

        Player player = Player.fromEvent(event);
        Map<String,String> aliases = diceAliases.get(player);

        if(Objects.isNull(aliases)){
            aliases = new HashMap<>();
            diceAliases.put(player,aliases);
        }

        String[] messages = event.getMessage().getContentDisplay().split(" ");

        String name = messages[1];
        String expr = messages[2];

        aliases.put(name,expr);


        event.getMessage().replyEmbeds(SimpleEmbedMessage.titleDescEmbed("주사위 별명이 등록되었습니다.",
                name+" -> "+expr)).queue();

    }

    public void command_rt(@NotNull MessageReceivedEvent event){

        Player player = Player.fromEvent(event);
        Map<String,String> aliases = diceAliases.get(player);

        if(Objects.isNull(aliases)){
            event.getMessage().replyEmbeds(SimpleEmbedMessage.titleDescEmbed(":x: 등록된 주사위 별명이 없습니다.",
                    "!ra 명령을 사용하여 별명을 등록하세요.")).queue();
            return;
        }

        String[] messages = event.getMessage().getContentDisplay().split(" ");

        String name = messages[1];
        String queryString = aliases.get(name);
        if(messages.length>2){
            String expr = messages[2];
            queryString = aliases.get(name)+expr;
        }

        event.getMessage().replyEmbeds(rollDice(queryString)).queue();

    }

    public void command_rl(@NotNull MessageReceivedEvent event){

    }

    public void command_rd(@NotNull MessageReceivedEvent event){

    }


    public MessageEmbed rollDice(String queryMessage){

        MessageEmbed embed;

        try{
            DiceResult result = new StackDice()
                    .calcExpr(queryMessage
                            .replace("<=","#")
                            .replace(">=","$"));
            embed = SimpleEmbedMessage.diceEmbed(result);

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
