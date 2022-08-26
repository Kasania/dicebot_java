/*
 * Author : Kasania
 * Filename : Main
 * Desc :
 */
package com.kasania;

import com.kasania.dicebot.common.DiceBot;
import com.kasania.dicebot.v1.Commands;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
public class Main {
    public static void main(String[] args) {

        try {
            log.info("Dice bot is started");
            String TOKEN = Files.readAllLines(Path.of("keys.txt"), StandardCharsets.UTF_8).get(0);
            JDA jda = JDABuilder.createDefault(TOKEN).setActivity(Activity.listening("/rhelp")).addEventListeners(new DiceBot()).build();

            jda.awaitReady();
            for (Commands value : Commands.values()) {
                jda.upsertCommand(value.name(),value.simpleHelpMessage).addOptions(value.optionData).queue();
            }

            log.info("Dice bot is initialized");
        } catch (IOException | LoginException | InterruptedException e) {
            log.error("{}",e.getMessage(),e);
        }
    }
}
