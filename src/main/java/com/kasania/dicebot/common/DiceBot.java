/*
 * Author : Kasania
 * Filename : Dicebot
 * Desc :
 */
package com.kasania.dicebot.common;

import com.kasania.dicebot.v1.Commands;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class DiceBot extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;

        System.out.println(event.getMessage().getGuild().getId());
        //DM
        if (event.isFromType(ChannelType.PRIVATE))
        {
            log.info("{}, {}, {}", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay(),  event.getAuthor().getAsMention());
        }
        //NON-DM
        else
        {
            handleMessage(event);
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        System.out.println(event.getName());
    }

    public void handleMessage(@NotNull MessageReceivedEvent event){
        String message = event.getMessage().getContentDisplay();

        if(!message.startsWith("!")) return;

        String[] commands = message.split(" ");
        String commandName = commands[0].replace("!","");

        for (Commands value : Commands.values()) {
            if(value.name().equals(commandName)){
                value.execute(event);
                return;
            }
        }
    }

    public void handleEvent(){

    }

}
