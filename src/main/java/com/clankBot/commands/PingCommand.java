package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class PingCommand extends Command {
    public PingCommand(String name, Category category, ArrayList<Permission> requiredPermissions) {
        super(name, category, requiredPermissions);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
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
