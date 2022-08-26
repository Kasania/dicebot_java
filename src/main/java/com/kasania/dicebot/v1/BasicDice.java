/*
 * Author : Kasania
 * Filename : BasicDice
 * Desc :
 */
package com.kasania.dicebot.v1;

import com.kasania.dicebot.common.SimpleEmbedMessage;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BasicDice {

    private final Map<Player,Map<String,String>> diceAliases = new HashMap<>();

    public MessageEmbed command_r(@NotNull SlashCommandInteractionEvent event){
        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        return rollDice(args.get(0));
    }

    public MessageEmbed command_ra(@NotNull SlashCommandInteractionEvent event){

        Player player = Player.fromEvent(event);
        Map<String,String> aliases = diceAliases.get(player);

        if(Objects.isNull(aliases)){
            aliases = new HashMap<>();
            diceAliases.put(player,aliases);
        }

        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        String name = args.get(0);
        String expr = args.get(1);

        aliases.put(name,expr);

        return SimpleEmbedMessage.titleDescEmbed("주사위 별명이 등록되었습니다.", name+" -> "+expr);
    }

    public MessageEmbed command_rw(@NotNull SlashCommandInteractionEvent event){

        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        int elementSize = args.size();

        String query = "1d"+elementSize;

        DiceResult result = new StackDice().calcExpr(query);
        return SimpleEmbedMessage.titleDescEmbed(":game_die: 결과 : " + args.get(Integer.parseInt(result.result)-1),
                result.query + "=" + result.result);

    }

    public MessageEmbed command_rt(@NotNull SlashCommandInteractionEvent event){

        Player player = Player.fromEvent(event);
        Map<String,String> aliases = diceAliases.get(player);

        if(Objects.isNull(aliases) || aliases.size() == 0){
            return SimpleEmbedMessage.titleDescEmbed(":x: 등록된 주사위 별명이 없습니다.",
                    "/ra 명령을 사용하여 별명을 등록하세요.");
        }

        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        String name = args.get(0);
        String queryString = aliases.get(name);

        if(Objects.isNull(queryString)){
            return SimpleEmbedMessage.titleDescEmbed(":x: 해당 별명으로 등록된 주사위가 없습니다.",
                    "/ra 명령을 사용하여 별명을 등록하세요.");
        }

        if(args.size()>1){
            String expr = args.get(1);
            queryString = aliases.get(name)+expr;
        }

        return rollDice(queryString);
    }

    public MessageEmbed command_rl(@NotNull SlashCommandInteractionEvent event){

        Player player = Player.fromEvent(event);
        Map<String,String> aliases = diceAliases.get(player);

        if(Objects.isNull(aliases) || aliases.size() == 0){
            return SimpleEmbedMessage.titleDescEmbed(":x: 등록된 주사위 별명이 없습니다.",
                    "/ra 명령을 사용하여 별명을 등록하세요.");
        }
        StringBuilder registeredDice = new StringBuilder();
        for (Map.Entry<String, String> alias : aliases.entrySet()) {
            registeredDice.append(alias.getKey())
                    .append(" -> ")
                    .append(alias.getValue())
                    .append("\n");
        }

        return SimpleEmbedMessage.titleDescEmbed("등록된 주사위 별명 목록",
                registeredDice.toString());
    }

    public MessageEmbed command_rd(@NotNull SlashCommandInteractionEvent event){

        Player player = Player.fromEvent(event);
        Map<String,String> aliases = diceAliases.get(player);

        if(Objects.isNull(aliases) || aliases.size() == 0){
            return SimpleEmbedMessage.titleDescEmbed(":x: 등록된 주사위 별명이 없습니다.",
                    "/ra 명령을 사용하여 별명을 등록하세요.");
        }

        List<OptionMapping> message = event.getOptions();
        List<String> args = message.stream().map(OptionMapping::getAsString).collect(Collectors.toList());

        String name = args.get(0);
        String expr = aliases.get(name);

        if(Objects.isNull(expr)){
            return SimpleEmbedMessage.titleDescEmbed(":x: 해당 별명으로 등록된 주사위가 없습니다.",
                    "/ra 명령을 사용하여 별명을 등록하세요.");
        }

        aliases.remove(name);

        return SimpleEmbedMessage.titleDescEmbed("주사위 별명이 제거되었습니다.",name+" -> "+expr);
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
            log.error("{}", assertionError.getMessage(),assertionError);
            embed = SimpleEmbedMessage.titleDescEmbed(":warning: 주사위 값이 잘못되었습니다.",
                    "주사위의 개수나 값을 조절해 주세요.");
        }
        catch (Exception e){
            log.error("{}", e.getMessage(),e);
            embed = SimpleEmbedMessage.titleDescEmbed(":x: 지원하지 않는 명령어입니다.",
                    "명령어를 확인해 주세요.");
        }

        return embed;
    }

}
