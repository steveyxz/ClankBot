package com.clankBot.util;

import com.clankBot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
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

}
