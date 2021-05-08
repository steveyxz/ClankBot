package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Command {
    protected String name;
    protected String description;
    protected String[] aliases;
    protected Category category;
    protected ArrayList<Permission> requiredPermissions;
    protected String usage;
    protected Cooldown cooldown;

    public Command(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        this.name = name;
        this.category = category;
        this.aliases = aliases;
        this.description = description;
        this.requiredPermissions = requiredPermissions;
        this.usage = usage;
        this.cooldown = cooldown;
    }

    public Cooldown getCooldown() {
        return cooldown;
    }

    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public abstract void run(String[] args, GuildMessageReceivedEvent e);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<Permission> getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(ArrayList<Permission> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }
}
