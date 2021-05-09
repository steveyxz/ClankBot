package com.clankBot.commands.misc;

import com.clankBot.commands.GuildCommand;
import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class PingCommand extends GuildCommand {
    public PingCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(this, 0, usage, args, requiredPermissions, e)) {
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
            this.resetCooldown(Objects.requireNonNull(e.getMember()));
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(new EmbedBuilder(), "An internal bot error occurred. Please contact the developer if this hinders usage.").build()).queue();
        }
    }
}
