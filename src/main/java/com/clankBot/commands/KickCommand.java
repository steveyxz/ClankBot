package com.clankBot.commands;

import com.clankBot.Main;
import com.clankBot.enums.util.Category;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class KickCommand extends Command {


    public KickCommand(String name, Category category, ArrayList<Permission> requiredPermissions, String usage) {
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

        User user = User.fromId(args[0].substring(3, args[0].length() - 1));
        e.getGuild().kick(Objects.requireNonNull(e.getGuild().getMember(user))).queue();

        embedBuilder.setColor(GlobalMethods.generateRandomColor());
    }

}
