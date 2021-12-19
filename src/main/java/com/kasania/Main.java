/*
 * Author : Kasania
 * Filename : Main
 * Desc :
 */
package com.kasania;

import com.kasania.dicebot.common.DiceBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) {
        try {

            String TOKEN = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("keys.txt").toURI())).get(0);
            JDA jda = JDABuilder.createDefault(TOKEN).build();
            jda.addEventListener(new DiceBot());

        } catch (IOException | LoginException | URISyntaxException e) {
            e.printStackTrace();
        }
    }




}
