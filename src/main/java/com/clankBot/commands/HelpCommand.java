package com.clankBot.commands;

import com.clankBot.Main;
import com.clankBot.enums.util.Category;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class HelpCommand extends Command{
    public HelpCommand(String name, Category category, ArrayList<Permission> requiredPermissions) {
        super(name, category, requiredPermissions);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        EmbedBuilder builder = new EmbedBuilder();
        if (args.length == 0) {
            StringBuilder categories = new StringBuilder();
            for (Category cat:
                 Category.values()) {
                categories.append("`");
                categories.append(Main.prefix);
                categories.append("help ");
                categories.append(Category.convCatToCommand(cat));
                categories.append("`");
                categories.append("\n");
            }
            Random r = new Random();
            builder.setColor(new Color(r.nextInt(254), r.nextInt(254), r.nextInt(254)));
            builder.addField("HELP CATEGORIES", String.valueOf(categories), false);
        } else if (args.length == 1) {
            Category selectedCategory = Category.convCatToCommand(args[0]);
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Add `");
            stringBuilder.append(Main.prefix);
            stringBuilder.append("` in front of these commands.\n\n");

            for (Command command: Main.getCommandsForCategory(selectedCategory)) {
                stringBuilder.append("`");
                stringBuilder.append(command.name);
                stringBuilder.append("`\n");
            }

            Random r = new Random();
            builder.setColor(new Color(r.nextInt(254), r.nextInt(254), r.nextInt(254)));
            builder.addField(selectedCategory.toString(), String.valueOf(stringBuilder), false);
        }
        e.getChannel().sendMessage(builder.build()).queue();
    }
}
