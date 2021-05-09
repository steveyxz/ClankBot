package com.clankBot.commands.hypixel;

import com.clankBot.commands.GuildCommand;
import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.hypixel.api.reply.WatchdogStatsReply;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.clankBot.Main.api;
import static com.clankBot.util.GlobalMethods.doAllTheChecksForCommand;
import static com.clankBot.util.GlobalMethods.generateRandomColor;

public class HypBansCommand extends GuildCommand {
    public HypBansCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (doAllTheChecksForCommand(this, 0, usage, args, requiredPermissions, e)) {
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder text = new StringBuilder();

        embedBuilder.setColor(generateRandomColor());
        embedBuilder.setFooter("Make sure you don't get banned!");

        try {
            WatchdogStatsReply watchdogStatsReply = api.getWatchdogStats().get();
            int staffTotal = watchdogStatsReply.getStaffTotal();
            int watchdogTotal = watchdogStatsReply.getWatchdogTotal();
            int dailyStaff = watchdogStatsReply.getStaffRollingDaily();
            int dailyWatchdog = watchdogStatsReply.getWatchdogRollingDaily();
            text.append("**Total bans**: ")
                    .append(watchdogTotal + staffTotal)
                    .append(" (")
                    .append(staffTotal)
                    .append(" by staff and ")
                    .append(watchdogTotal)
                    .append(" by watchdog)\n")
                    .append("**Daily Bans**: ")
                    .append(dailyStaff + dailyWatchdog)
                    .append(" (")
                    .append(dailyStaff)
                    .append(" by staff and ")
                    .append(dailyWatchdog)
                    .append(" by watchdog.)");
            embedBuilder.addField("**BAN INFO**", new String(text), false);
            e.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (InterruptedException | ExecutionException interruptedException) {
            this.resetCooldown(Objects.requireNonNull(e.getMember()));
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "An internal bot error occurred. Please contact the developer if this hinders usage.").build()).queue();
            interruptedException.printStackTrace();
        }
    }
}
