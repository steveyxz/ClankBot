package com.clankBot.commands;

import com.clankBot.Main;
import com.clankBot.enums.util.Category;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;

import static com.clankBot.Main.serverDataManagerMongo;

public class ServerPrefixCommand extends Command {


    public ServerPrefixCommand(String name, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) {
            return;
        }
        if (GlobalMethods.hasPermissions(e.getMember(), requiredPermissions)) {
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (args.length < 1) {
            EmbedCreator.createErrorEmbed(embedBuilder, "Correct usage is: `" + usage + "`");
            e.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        HashMap<String, Object> values = new HashMap<>();
        values.put(e.getGuild().getId(), args[0]);
        serverDataManagerMongo.insertToDocument("serverData", "prefixes", values);
        embedBuilder.setColor(GlobalMethods.generateRandomColor());
        embedBuilder.addField("Success!", "Set the new server prefix to `" + args[0] + "`", false);

        e.getChannel().sendMessage(embedBuilder.build()).queue();


    }
}
