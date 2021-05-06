package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.clankBot.Main.*;

import java.util.ArrayList;
import java.util.HashMap;

public class HypLoginCommand extends GuildCommand{
    public HypLoginCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, description, aliases, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(1, usage, args, requiredPermissions, e)) {
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        HashMap<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(e.getAuthor().getId(), args[0]);

        userDataManagerMongo.insertToDocument("userData", "login", valuesMap);

        embedBuilder.setColor(GlobalMethods.generateRandomColor());
        embedBuilder.setTitle("Logged in as " + args[0]);
        embedBuilder.setDescription("Now you will be known as this IGN until you login again.");
        embedBuilder.setFooter("**Note that Hypixel commands will not function if IGN is not valid!**");

        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
