package com.clankBot.util;

import com.clankBot.commands.Command;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.clankBot.Main.serverDataManagerMongo;
import static com.clankBot.Main.userDataManagerMongo;

public class GlobalMethods {

    public static boolean doesNotHavePermission(Member target, ArrayList<Permission> permissions) {
        for (Permission permission :
                permissions) {
            if (!PermissionUtil.checkPermission(target, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Does all the checks for a command before runtime
     *
     * @param usage               The usage of the command so it can return it.
     * @param args                The arguments of the command.
     * @param requiredPermissions The required permissions for the command.
     * @param e                   The GuildMessageReceivedEvent of the command
     * @return True if the command failed, false on success.
     */
    public static boolean doAllTheChecksForCommand(Command command, int argNo, String usage, String[] args, ArrayList<Permission> requiredPermissions, GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            command.resetCooldown(Objects.requireNonNull(e.getMember()));
            return true;
        }
        EmbedBuilder builder = new EmbedBuilder();
        if (GlobalMethods.doesNotHavePermission(e.getMember(), requiredPermissions)) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(builder, "Insufficient permissions").build()).queue();
            command.resetCooldown(Objects.requireNonNull(e.getMember()));
            return true;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (args.length < argNo) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "Correct usage is: `" + usage + "`").build()).queue();
            command.resetCooldown(Objects.requireNonNull(e.getMember()));
            return true;
        }

        return false;
    }

    public static Long parseStringToMilli(String parser) {
        String[] times = parser.split(" ");
        long returned = 0L;
        for (String s : times) {
            try {
                long number = Long.parseLong(s.substring(0, s.length() - 1));
                if (s.endsWith("s")) {
                    returned += number * 1000;
                } else if (s.endsWith("m")) {
                    returned += number * 1000 * 60;
                } else if (s.endsWith("h")) {
                    returned += number * 1000 * 60 * 60;
                } else if (s.endsWith("d")) {
                    returned += number * 1000 * 60 * 60 * 24;
                }
            } catch (NumberFormatException error) {
                return null;
            }
        }
        return returned;
    }

    public static String getPrefixForGuild(Guild guild) {
        return (String) serverDataManagerMongo.getValueOfKey("serverData", "prefixes", guild.getId());
    }

    public static Color generateRandomColor() {
        Random r = new Random();
        return new Color(r.nextInt(224), r.nextInt(224), r.nextInt(224));
    }

    public static void addRoleToGuild(Guild guild, String roleName, Color color, Permission... permissions) {
        try {

            for (int i = 0; i < guild.getRoles().size(); i++) {
                if (guild.getRoles().get(i).getName().equals(roleName)) {
                    return;
                }
            }
            guild.createRole()
                    .setColor(color)
                    .setPermissions(permissions)
                    .setMentionable(false)
                    .setName(roleName)
                    .queue();
        } catch (HierarchyException error) {

        }
    }

    public static boolean checkIfCooldownFinished(Cooldown cooldown, User user, Guild guild) {
        return cooldown.checkCompletion(user.getId(), guild);
    }

    public static Object getValueOfSetting(String settingName, String userID, String type) {
        JsonParser parser = new JsonParser();
        String json = (String) userDataManagerMongo.getValueOfKey("userData", "settings", userID);
        JsonObject object = parser.parse(json).getAsJsonObject();
        switch (type) {
            case "string" -> {
                return object.get(settingName).getAsString();
            }
            case "int" -> {
                return object.get(settingName).getAsInt();
            }
            case "double" -> {
                return object.get(settingName).getAsDouble();
            }
            case "long" -> {
                return object.get(settingName).getAsLong();
            }
        }
        return object.get(settingName).getAsString();
    }

    public static String prettifyWholeNumber(long number) {
        String stringValue = Long.toString(number);
        stringValue = new String(new StringBuilder(stringValue).reverse());
        for (int i = 0; i < stringValue.toCharArray().length; i++) {
            if ((i + 1) % 4 == 0) {
                stringValue = new String(new StringBuilder(stringValue).insert(i, ','));
            }
        }
        return new String(new StringBuilder(stringValue).reverse());
    }

    public static String convMillisecondsToString(double millis) {
        if (millis < 500) {
            return millis + "ms";
        }
        if (millis < 1000) {
            return millis / 1000D + "s";
        }
        long milliseconds = (long) (millis % 1000);
        long secs = (long) (millis / 1000);
        if (secs < 60) {
            return secs + "s " + milliseconds + "ms";
        }
        long mins = secs / 60;
        secs = secs % 60;
        if (mins < 60) {
            return mins + "m " + secs + "s " + milliseconds + "ms";
        }
        long hours = mins / 60;
        mins = mins % 60;
        if (hours < 24) {
            return hours + "h " + mins + "m " + secs + "s";
        }
        long days = hours / 24;
        hours = hours % 24;
        return days + "d " + hours + "h " + mins + "m " + secs + "s";
    }

}
