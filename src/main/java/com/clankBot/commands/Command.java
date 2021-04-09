package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public abstract class Command {

    protected String name;
    protected Category category;
    protected ArrayList<Permission> requiredPermissions;

    public Command(String name, Category category, ArrayList<Permission> requiredPermissions) {
        this.name = name;
        this.category = category;
        this.requiredPermissions = requiredPermissions;
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
