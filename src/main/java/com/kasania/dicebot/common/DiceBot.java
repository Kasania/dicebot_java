/*
 * Author : Kasania
 * Filename : Dicebot
 * Desc :
 */
package com.kasania.dicebot.common;

import com.kasania.dicebot.v1.Commands;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiceBot extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;

        //DM
        if (event.isFromType(ChannelType.PRIVATE))
        {
            logger.info("{}, {}", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        }
        //NON-DM
        else
        {
            handleMessage(event);
        }
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

}
