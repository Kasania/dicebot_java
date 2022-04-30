/*
 * Author : Kasania
 * Filename : Main
 * Desc :
 */
package com.kasania;

import com.kasania.dicebot.common.DiceBot;
import com.kasania.dicebot.v1.Commands;
import com.kasania.dicebot.v2.DiceBot2;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.dv8tion.jda.internal.interactions.command.CommandImpl;

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
            JDA jda = JDABuilder.createDefault(TOKEN).setActivity(Activity.listening("!rhelp")).build();
            jda.addEventListener(new DiceBot());
            log.info("Dice bot is initialized");

            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Guild guild : jda.getGuilds()) {
                    log.info("{} : {}", guild.getName(),guild.getIdLong());
                }
            }).start();

        } catch (IOException | LoginException e) {
            log.error("{}",e.getMessage(),e);
        }
    }
}
