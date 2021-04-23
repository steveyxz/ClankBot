package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Objects;

public class BanCommand extends Command{
    public BanCommand(String name, Category category, ArrayList<Permission> requiredPermissions, String usage) {
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

        StringBuilder reason = new StringBuilder();
        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }
                reason.append(args[i]);
                reason.append(" ");
            }
        }

        try {
            User user = User.fromId(args[0].substring(3, args[0].length() - 1));

            if (PermissionUtil.canInteract(Objects.requireNonNull(e.getMember()),
                    Objects.requireNonNull(e.getGuild().getMember(user)))) {
                if (args.length < 2) {
                    e.getGuild().ban(Objects.requireNonNull(e.getGuild().getMember(user)), 1).queue();
                } else {
                    e.getGuild().ban(Objects.requireNonNull(e.getGuild().getMember(user)), 1, new String(reason)).queue();
                }
            } else {
                e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "You cannot ban this member because (s)he is of higher authority than you").build()).queue();
                return;
            }

            if (args.length < 2) {
                e.getChannel().sendMessage(EmbedCreator.createSuccessEmbed(embedBuilder, "SUCCESSFUL BAN", "Banned member " + args[0]).build()).queue();
            } else {
                e.getChannel().sendMessage(EmbedCreator.createSuccessEmbed(embedBuilder, "SUCCESSFUL BAN", "Banned member " + args[0] + " for `" + new String(reason) + "`").build()).queue();
            }

        } catch (HierarchyException error) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "Bot cannot ban this member: INSUFFICIENT PERMISSIONS").build()).queue();
        } catch (IllegalArgumentException error) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "User is non-existent").build()).queue();
        }
    }
}
