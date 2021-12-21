/*
 * Author : Kasania
 * Filename : Main
 * Desc :
 */
package com.kasania;

import com.kasania.dicebot.common.DiceBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Dice bot is started");
            String TOKEN = Files.readAllLines(Path.of("keys.txt"), StandardCharsets.UTF_8).get(0);
            JDA jda = JDABuilder.createDefault(TOKEN).setActivity(Activity.listening("!rhelp")).build();
            jda.addEventListener(new DiceBot());
            System.out.println("Dice bot is initialized");
        } catch (IOException | LoginException e) {
            e.printStackTrace();
        }
    }
}
