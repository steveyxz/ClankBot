package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;

public abstract class GuildCommand extends Command {

    public GuildCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

}
