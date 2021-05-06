package com.clankBot.commands;

import com.clankBot.enums.util.Category;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.hypixel.api.reply.skyblock.BazaarReply;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static com.clankBot.Main.api;

public class HypixelCommand extends GuildCommand {

    public HypixelCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage) {
        super(name, description, aliases, category, requiredPermissions, usage);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (GlobalMethods.doAllTheChecksForCommand(0, usage, args, requiredPermissions, e)) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();

        StringBuilder stringBuilder = new StringBuilder();

        try {
            final BazaarReply.Product[] bestProduct = {null};
            api.getBazaar().get().getProducts().forEach(
                    (s, product) -> {
                        if (bestProduct[0] == null) {
                            bestProduct[0] = product;
                            return;
                        }
                        if (product.getQuickStatus().getBuyVolume() > bestProduct[0].getQuickStatus().getBuyVolume()) {
                            bestProduct[0] = product;
                        }
                    });
            stringBuilder.append("**Players**: ")
                    .append(api.getGameCounts().get().getPlayerCount())
                    .append(" (use the `hypplayers` command to get more detail)")
                    .append("\n**Total bans**: ")
                    .append(api.getWatchdogStats().get().getWatchdogTotal() + api.getWatchdogStats().get().getStaffTotal())
                    .append(" (use `hypbans` for more info)")
                    .append("\n**Most bought item in bazaar**: ")
                    .append(bestProduct[0].getProductId())
                    .append(" with ")
                    .append(bestProduct[0].getQuickStatus().getBuyVolume())
                    .append(" currently in buy orders.");
        } catch (ExecutionException | InterruptedException executionException) {
            executionException.printStackTrace();
        }
        builder.addField("**HYPIXEL STATS**", new String(stringBuilder), false);
        builder.setColor(GlobalMethods.generateRandomColor());

        e.getChannel().sendMessage(builder.build()).queue();

    }
}
