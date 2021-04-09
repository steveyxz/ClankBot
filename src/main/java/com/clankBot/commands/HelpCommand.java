package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class HelpCommand extends Command{
    public HelpCommand(String name, Category category, ArrayList<Permission> requiredPermissions) {
        super(name, category, requiredPermissions);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        EmbedBuilder builder = new EmbedBuilder();
        if (args.length == 0) {

        }
    }
}
