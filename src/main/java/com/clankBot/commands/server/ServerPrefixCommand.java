package com.clankBot.commands.server;

import com.clankBot.commands.GuildCommand;
import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.clankBot.Main.serverDataManagerMongo;

public class ServerPrefixCommand extends GuildCommand {


    public ServerPrefixCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {

        if (GlobalMethods.doAllTheChecksForCommand(this, 1, usage, args, requiredPermissions, e)) {
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (args.length < 1) {
            EmbedCreator.createErrorEmbed(embedBuilder, "Correct usage is: `" + usage + "`");
            e.getChannel().sendMessage(embedBuilder.build()).queue();
            resetCooldown(Objects.requireNonNull(e.getMember()));
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
