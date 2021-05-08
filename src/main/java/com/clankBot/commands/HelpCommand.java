package com.clankBot.commands;

import com.clankBot.Main;
import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static com.clankBot.Main.dmCommandList;
import static com.clankBot.Main.guildCommandList;

public class HelpCommand extends GuildCommand {
    public HelpCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
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

            for (int i = 0; i < Main.guildCommandList.size(); i++) {
                if (Arrays.asList(guildCommandList.get(i).getAliases()).contains(args[0])) {
                    e.getChannel().sendMessage(EmbedCreator.createGuildCommandInfoEmbed(builder, guildCommandList.get(i)).build()).queue();
                    return;
                }
            }

            for (int i = 0; i < Main.dmCommandList.size(); i++) {
                if (Arrays.asList(dmCommandList.get(i).getAliases()).contains(args[0])) {
                    e.getChannel().sendMessage(EmbedCreator.createDMCommandInfoEmbed(builder, dmCommandList.get(i)).build()).queue();
                    return;
                }
            }

            Category selectedCategory = Category.convCatToCommand(args[0]);

            if (selectedCategory == null) {
                e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(builder, "Command or category not found.").build()).queue();
                return;
            }

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
