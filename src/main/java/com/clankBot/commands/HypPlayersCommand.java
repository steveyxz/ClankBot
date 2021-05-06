package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class HypPlayersCommand extends GuildCommand{
    public HypPlayersCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, description, aliases, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {

    }
}
