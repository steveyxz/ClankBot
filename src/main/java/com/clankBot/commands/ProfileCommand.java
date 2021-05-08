package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class ProfileCommand extends GuildCommand {
    public ProfileCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(0, usage, args, requiredPermissions, e)) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();

        e.getChannel().sendMessage(EmbedCreator.createProfileEmbed(builder, e.getAuthor().getId(), e.getJDA()).build()).queue();
    }
}
