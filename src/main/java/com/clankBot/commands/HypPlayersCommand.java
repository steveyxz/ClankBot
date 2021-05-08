package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.hypixel.api.reply.GameCountsReply;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.clankBot.Main.api;
import static com.clankBot.util.GlobalMethods.doAllTheChecksForCommand;
import static com.clankBot.util.GlobalMethods.generateRandomColor;

public class HypPlayersCommand extends GuildCommand {
    public HypPlayersCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (doAllTheChecksForCommand(0, usage, args, requiredPermissions, e)) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(generateRandomColor());
        embedBuilder.setTitle("**HYPIXEL PLAYERS**");

        try {
            GameCountsReply object = api.getGameCounts().get();
            builder.append("**Total players**: ")
                    .append(object.getPlayerCount());

            for (String game : object.getGames().keySet()) {
                builder.append("\n**Players in ")
                        .append(game)
                        .append("**: ")
                        .append(object.getGames().get(game).getPlayers());
                if (builder.length() > 400) {
                    embedBuilder.addField("", new String(builder), true);
                    builder.delete(0, builder.length());
                }
            }
        } catch (InterruptedException | ExecutionException interruptedException) {
            interruptedException.printStackTrace();
        }
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
