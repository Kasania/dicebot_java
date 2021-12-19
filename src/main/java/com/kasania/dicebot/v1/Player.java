/*
 * Author : Kasania
 * Filename : Player
 * Desc :
 */
package com.kasania.dicebot.v1;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Player {

    public final String GuildID;
    public final String MentionID;

    public static Player fromEvent(@NotNull MessageReceivedEvent event){
        return new Player(event.getGuild().getId(),event.getMember().getAsMention());
    }

    public Player(String guildID, String mentionID) {
        GuildID = guildID;
        MentionID = mentionID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(GuildID, player.GuildID) && Objects.equals(MentionID, player.MentionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GuildID, MentionID);
    }

    @Override
    public String toString() {
        return "Player{" +
                "GuildID='" + GuildID + '\'' +
                ", MentionID='" + MentionID + '\'' +
                '}';
    }
}
