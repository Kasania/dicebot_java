/*
 * Author : Kasania
 * Filename : Dicebot
 * Desc :
 */
package com.kasania.dicebot.common;

import com.kasania.dicebot.v1.Commands;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class DiceBot extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getUser().isBot() || event.getUser().isSystem()) return;

        if(event.isFromGuild()){
            for (Commands value : Commands.values()) {
                if(value.name().equals(event.getName())){
                    value.execute(event);
                    return;
                }
            }
        }
    }
}
