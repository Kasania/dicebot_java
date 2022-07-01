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
    public final String authorID;

    public synchronized static Player fromEvent(@NotNull MessageReceivedEvent event) {
        return new Player(event.getGuild().getId(), event.getAuthor().getId());
    }

    public Player(String guildID, String authorID) {
        GuildID = guildID;
        this.authorID = authorID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(GuildID, player.GuildID) && Objects.equals(authorID, player.authorID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GuildID, authorID);
    }

    @Override
    public String toString() {
        return "Player{" +
                "GuildID='" + GuildID + '\'' +
                ", authorID='" + authorID + '\'' +
                '}';
    }
}
