package com.clankBot.commands.moderation;

import com.clankBot.commands.GuildCommand;
import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
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

public class BanCommand extends GuildCommand {
    public BanCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(this, 1, usage, args, requiredPermissions, e)) {
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

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
                this.resetCooldown(Objects.requireNonNull(e.getMember()));
                return;
            }

            if (args.length < 2) {
                e.getChannel().sendMessage(EmbedCreator.createSuccessEmbed(embedBuilder, "SUCCESSFUL BAN", "Banned member " + args[0]).build()).queue();
            } else {
                e.getChannel().sendMessage(EmbedCreator.createSuccessEmbed(embedBuilder, "SUCCESSFUL BAN", "Banned member " + args[0] + " for `" + new String(reason) + "`").build()).queue();
            }

        } catch (HierarchyException error) {
            this.resetCooldown(Objects.requireNonNull(e.getMember()));
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "Bot cannot ban this member: INSUFFICIENT PERMISSIONS").build()).queue();
        } catch (IllegalArgumentException error) {
            this.resetCooldown(Objects.requireNonNull(e.getMember()));
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "User is non-existent").build()).queue();
        }
    }
}
