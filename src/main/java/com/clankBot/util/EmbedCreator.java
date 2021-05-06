package com.clankBot.util;

import com.clankBot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import static com.clankBot.Main.*;
import java.util.Arrays;

public class EmbedCreator {

    public static EmbedBuilder createErrorEmbed(EmbedBuilder builder, String message) {
        builder.addField("ERROR", message, false);
        builder.setColor(Color.red);
        return builder;
    }

    public static EmbedBuilder createSuccessEmbed(EmbedBuilder builder, String title, String message) {
        builder.addField(title, message, false);
        builder.setColor(Color.green);
        return builder;
    }

    public static EmbedBuilder createGuildCommandInfoEmbed(EmbedBuilder builder, Command command) {
        builder.setTitle("**" + command.getName().toUpperCase() + "**");
        builder.setColor(GlobalMethods.generateRandomColor());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("**Command name:** ")
                .append(command.getName())
                .append("\n**Description:** ")
                .append(command.getDescription())
                .append("\n**Aliases:** ")
                .append(Arrays.toString(command.getAliases()))
                .append("\n**Usage:** `")
                .append(command.getUsage())
                .append("`");

        builder.setDescription(new String(stringBuilder));

        return builder;
    }

    public static EmbedBuilder createDMCommandInfoEmbed(EmbedBuilder builder, Command command) {
        builder.setTitle("**" + command.getName().toUpperCase() + "**");
        builder.setColor(GlobalMethods.generateRandomColor());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("**Command name:** ")
                .append(command.getName())
                .append("\n**Description:** ")
                .append(command.getDescription())
                .append("\n**Aliases:** ")
                .append(Arrays.toString(command.getAliases()))
                .append("\n**Usage:** `")
                .append(command.getUsage())
                .append("`");

        builder.setDescription(new String(stringBuilder));

        return builder;
    }

    public static EmbedBuilder createProfileEmbed(EmbedBuilder builder, String userID, JDA jda) {
        builder.setColor(GlobalMethods.generateRandomColor());
        User user = jda.getUserById(userID);
        assert user != null;
        builder.setTitle(user.getName() + "'s profile");
        builder.setThumbnail(user.getAvatarUrl());
        StringBuilder generalText = new StringBuilder();
        generalText
                .append("Coins: ")
                .append(userDataManagerMongo.getValueOfKey("userData", "coins", userID))
                .append("\nLevel: ")
                .append(userDataManagerMongo.getValueOfKey("userData", "levels", userID))
                .append("\nProgress: ")
                .append(userDataManagerMongo.getValueOfKey("userData", "currentExp", userID))
                .append("/")
                .append(userDataManagerMongo.getValueOfKey("userData", "reqExp", userID));
        builder.addField("**GENERAL**", new String(generalText), true);
        return builder;
    }

}
