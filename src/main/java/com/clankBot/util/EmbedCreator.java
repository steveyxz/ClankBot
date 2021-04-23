package com.clankBot.util;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

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

}
