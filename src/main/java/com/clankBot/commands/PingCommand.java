package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class PingCommand extends GuildCommand {
    public PingCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, description, aliases, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(0, usage, args, requiredPermissions, e)) {
            return;
        }
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.CYAN);
            builder.setTitle("\uD83C\uDFD3 Pong! \uD83C\uDFD3");
            builder.addField("You sent a ping message so here's your pong!", "", true);
            builder.setFooter("Requested by: " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator());
            e.getChannel().sendMessage(builder.build()).queue();
        } catch (Exception exception) {
            e.getChannel().sendMessage("An error occurred.").queue();
        }
    }
}
