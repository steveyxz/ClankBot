package com.clankBot.commands;

import com.clankBot.Main;
import com.clankBot.enums.util.Category;
import com.clankBot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.clankBot.Main.*;

public class ServerPrefixCommand extends Command{


    public ServerPrefixCommand(String name, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {

        if (e.getAuthor().isBot()) {
            return;
        }
        for (Permission perm:
                requiredPermissions) {
            if (Objects.requireNonNull(e.getMember()).getPermissions().contains(Permission.ADMINISTRATOR)) {
                break;
            }
            if (!Objects.requireNonNull(e.getMember()).getPermissions().contains(perm)) {
                return;
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (args.length < 1) {
            EmbedCreator.createErrorEmbed(embedBuilder, "Correct usage is: `" + usage + "`");
            e.getChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }
        try {
            serverDataManager.deleteValue("prefixes", e.getGuild().getId());
            ArrayList<Object> values = new ArrayList<>();
            values.add(e.getGuild().getId());
            values.add(args[0]);
            serverDataManager.insert("prefixes", values);
            embedBuilder.setColor(Main.generateRandomColor());
            embedBuilder.addField("Success!", "Set the new server prefix to `" + args[0] + "`", false);
        } catch (SQLException throwables) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.addField("ERROR", "An error occurred while running this command. Please contact the developers.", false);
            throwables.printStackTrace();
        }
        e.getChannel().sendMessage(embedBuilder.build()).queue();


    }
}
