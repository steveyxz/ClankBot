package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MuteCommand extends GuildCommand {

    public MuteCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(1, usage, args, requiredPermissions, e)) {
            return;
        }

        boolean isUnMute = true;

        StringBuilder times = new StringBuilder();
        if (args.length > 1) {
            isUnMute = false;
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }
                times.append(args[i]);
                times.append(" ");
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        try {
            User user = User.fromId(args[0].substring(3, args[0].length() - 1));

            if (PermissionUtil.canInteract(Objects.requireNonNull(e.getMember()),
                    Objects.requireNonNull(e.getGuild().getMember(user)))) {

                Member userMentioned = e.getGuild().getMember(user);

                GlobalMethods.addRoleToGuild(e.getGuild(), "MUTED", Color.GRAY, Permission.EMPTY_PERMISSIONS);

                for (net.dv8tion.jda.api.entities.Category c : e.getGuild().getCategories()) {
                    c.putPermissionOverride(e.getGuild().getRolesByName("MUTED", true).get(0)).setDeny(8589933567L).setAllow(1024L).queue();
                }

                assert userMentioned != null;
                if (userMentioned.getRoles().contains(e.getGuild().getRolesByName("MUTED", false).get(0))) {
                    e.getGuild().removeRoleFromMember(userMentioned, e.getGuild().getRolesByName("MUTED", false).get(0)).queue();
                    e.getChannel().sendMessage(EmbedCreator.createSuccessEmbed(embedBuilder, "SUCCESSFUL UNMUTE", "Unmuted " + args[0]).build()).queue();
                } else {
                    if (isUnMute) {
                        e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "Correct usage is: `" + usage + "`").build()).queue();
                        return;
                    }
                    e.getGuild().addRoleToMember(userMentioned, e.getGuild().getRolesByName("MUTED", false).get(0)).queue();
                    System.out.println(GlobalMethods.parseStringToMilli(new String(times)));

                    Timer timer = new Timer();
                    if (GlobalMethods.parseStringToMilli(new String(times)) == null) {
                        e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "Invalid time: see `help mute` for info").build()).queue();
                        return;
                    }
                    assert GlobalMethods.parseStringToMilli(new String(times)) != null;
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    e.getGuild().removeRoleFromMember(userMentioned, e.getGuild().getRolesByName("MUTED", false).get(0)).queue();
                                }
                            },
                            GlobalMethods.parseStringToMilli(new String(times))
                    );

                    e.getChannel().sendMessage(EmbedCreator.createSuccessEmbed(embedBuilder, "SUCCESSFUL MUTE", "Muted " + args[0] + " for " + new String(times)).build()).queue();
                }
            }
        } catch (
                HierarchyException error) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "Bot cannot mute this member: INSUFFICIENT PERMISSIONS").build()).queue();
        } catch (
                IllegalArgumentException error) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "User is non-existent").build()).queue();
        }
    }
}
