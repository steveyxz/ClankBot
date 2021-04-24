package com.clankBot.commands;

import com.clankBot.Main;
import com.clankBot.enums.util.Category;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class HelpCommand extends GuildCommand {
    public HelpCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, description, aliases, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(0, usage, args, requiredPermissions, e)) {
            return;
        }
        EmbedBuilder builder = new EmbedBuilder();
        if (args.length == 0) {
            StringBuilder categories = new StringBuilder();
            for (Category cat :
                    Category.values()) {
                categories.append("`");
                categories.append(GlobalMethods.getPrefixForGuild(e.getGuild()));

                categories.append("help ");
                categories.append(Category.convCatToCommand(cat));
                categories.append("`");
                categories.append("\n");
            }
            builder.setColor(GlobalMethods.generateRandomColor());
            builder.addField("HELP CATEGORIES", String.valueOf(categories), false);
        } else if (args.length == 1) {
            Category selectedCategory = Category.convCatToCommand(args[0]);
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Add `");

            stringBuilder.append(GlobalMethods.getPrefixForGuild(e.getGuild()));
            stringBuilder.append("` in front of these commands.\n\n");

            for (GuildCommand command : Main.getCommandsForCategory(selectedCategory)) {
                stringBuilder.append("`");
                stringBuilder.append(command.name);
                stringBuilder.append("`\n");
            }

            builder.setColor(GlobalMethods.generateRandomColor());
            builder.addField(selectedCategory.toString(), String.valueOf(stringBuilder), false);
        }
        e.getChannel().sendMessage(builder.build()).queue();
    }
}
