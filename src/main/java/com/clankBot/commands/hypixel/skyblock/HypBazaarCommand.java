package com.clankBot.commands.hypixel.skyblock;

import com.clankBot.commands.GuildCommand;
import com.clankBot.enums.util.Category;
import com.clankBot.util.Cooldown;
import com.clankBot.util.EmbedCreator;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.hypixel.api.reply.skyblock.BazaarReply;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.clankBot.Main.api;
import static com.clankBot.util.GlobalMethods.doAllTheChecksForCommand;

public class HypBazaarCommand extends GuildCommand {
    public HypBazaarCommand(String name, String description, String[] aliases, Category category, ArrayList<Permission> requiredPermissions, String usage, Cooldown cooldown) {
        super(name, description, aliases, category, requiredPermissions, usage, cooldown);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (doAllTheChecksForCommand(this, 0, usage, args, requiredPermissions, e)) {
            return;
        }
        String type;
        String type1 = null;
        if (args.length == 0) {
            type1 = "modifiedtopbuy";
        } else if (args.length == 1) {
            type1 = "";
            if (args[0].equals("topbuy")) {
                type1 = "topbuy";
            }
            if (args[0].equals("topsell")) {
                type1 = "topsell";
            }
            if (args[0].equals("topbuyprice")) {
                type1 = "topbuyprice";
            }
            if (args[0].equals("lowbuyprice")) {
                type1 = "lowbuyprice";
            }
            if (args[0].equals("topsellprice")) {
                type1 = "topsellprice";
            }
            if (args[0].equals("lowsellprice")) {
                type1 = "lowsellprice";
            }
        } else {
            type1 = "search";
            if (!args[0].equals("search")) {
                e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(new EmbedBuilder(), "If you have two or more arguments they must be `hypbazaar search <item name>`").build()).queue();
                return;
            }
        }
        type = type1;
        if (type.equals("")) {
            e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(new EmbedBuilder(), "That is not a valid category!").build()).queue();
            return;
        }
        if (type.equals("topbuy") || type.equals("modifiedtopbuy") || type.equals("topsell") || type.equals("topbuyprice") || type.equals("lowbuyprice") || type.equals("topsellprice") || type.equals("lowsellprice")) {
            int noLines = (int) GlobalMethods.getValueOfSetting("displayCount", e.getAuthor().getId(), "int");

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setColor(GlobalMethods.generateRandomColor());
            embedBuilder.setFooter("Remember you can change the number of shown values in settings! -Sent from legit version");

            StringBuilder text = new StringBuilder();
            try {
                BazaarReply reply = api.getBazaar().get();

                final ArrayList<BazaarReply.Product> bestProduct = new ArrayList<>();
                boolean typeIsOrders = type.equals("topbuy") || type.equals("modifiedtopbuy") || type.equals("topsell");
                if (typeIsOrders) {
                    String finalType = type;
                    reply.getProducts().forEach(
                            (s, product) -> {
                                if (bestProduct.size() <= noLines) {
                                    bestProduct.add(product);
                                    return;
                                }
                                for (int i = 0; i < bestProduct.size(); i++) {
                                    if (getVolumeByType(finalType, product) > getVolumeByType(finalType, bestProduct.get(i))) {
                                        bestProduct.set(i, product);
                                        return;
                                    }
                                }

                            });
                } else {
                    String finalType1 = type;
                    reply.getProducts().forEach(
                            (s, product) -> {
                                if (bestProduct.size() <= noLines) {
                                    bestProduct.add(product);
                                    return;
                                }
                                if (finalType1.equals("topbuyprice") || finalType1.equals("topsellprice")) {
                                    for (int i = 0; i < bestProduct.size(); i++) {
                                        for (int j = i + 1; j < bestProduct.size(); j++) {
                                            BazaarReply.Product tmp;
                                            if (getPriceByType(finalType1, bestProduct.get(i)) > getPriceByType(finalType1, bestProduct.get(j))) {
                                                tmp = bestProduct.get(i);
                                                bestProduct.set(i, bestProduct.get(j));
                                                bestProduct.set(j, tmp);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < bestProduct.size(); i++) {
                                        for (int j = i + 1; j < bestProduct.size(); j++) {
                                            BazaarReply.Product tmp;
                                            if (getPriceByType(finalType1, bestProduct.get(i)) < getPriceByType(finalType1, bestProduct.get(j))) {
                                                tmp = bestProduct.get(i);
                                                bestProduct.set(i, bestProduct.get(j));
                                                bestProduct.set(j, tmp);
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < bestProduct.size(); i++) {
                                    if (finalType1.equals("topbuyprice") || finalType1.equals("topsellprice")) {
                                        if (getPriceByType(finalType1, product) > getPriceByType(finalType1, bestProduct.get(i))) {
                                            bestProduct.set(i, product);
                                            return;
                                        }
                                    } else {
                                        if (getPriceByType(finalType1, product) < getPriceByType(finalType1, bestProduct.get(i))) {
                                            bestProduct.set(i, product);
                                            return;
                                        }
                                    }
                                }

                            });
                    if (type.equals("topbuyprice") || type.equals("topsellprice")) {
                        for (int i = 0; i < bestProduct.size(); i++) {
                            for (int j = i + 1; j < bestProduct.size(); j++) {
                                BazaarReply.Product tmp;
                                if (getPriceByType(type, bestProduct.get(i)) < getPriceByType(type, bestProduct.get(j))) {
                                    tmp = bestProduct.get(i);
                                    bestProduct.set(i, bestProduct.get(j));
                                    bestProduct.set(j, tmp);
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < bestProduct.size(); i++) {
                            for (int j = i + 1; j < bestProduct.size(); j++) {
                                BazaarReply.Product tmp;
                                if (getPriceByType(type, bestProduct.get(i)) > getPriceByType(type, bestProduct.get(j))) {
                                    tmp = bestProduct.get(i);
                                    bestProduct.set(i, bestProduct.get(j));
                                    bestProduct.set(j, tmp);
                                }
                            }
                        }
                    }
                }

                if (typeIsOrders) {
                    for (int i = 0; i < bestProduct.size() - 1; i++) {
                        text.append(i + 1)
                                .append(". **")
                                .append(bestProduct.get(i).getQuickStatus().getProductId().replace("_", " "))
                                .append("** with `")
                                .append(GlobalMethods.prettifyWholeNumber(getVolumeByType(type, bestProduct.get(i))))
                                .append("` in ")
                                .append(type.equals("topsell") ? "sell" : "buy")
                                .append(" orders.")
                                .append("\n");
                    }
                } else {
                    for (int i = 0; i < bestProduct.size() - 1; i++) {
                        text.append(i + 1)
                                .append(". **")
                                .append(bestProduct.get(i).getQuickStatus().getProductId().replace("_", " "))
                                .append("** costing `")
                                .append(type.equals("lowsellprice") || type.equals("lowbuyprice") ? getPriceByType(type, bestProduct.get(i)) : GlobalMethods.prettifyWholeNumber(Math.round(getPriceByType(type, bestProduct.get(i)))))
                                .append("` gold.\n");
                    }
                }

                if (typeIsOrders) {
                    embedBuilder.addField("**Top " + noLines + " Bazaar " + (type.equals("topsell") ? "Sold" : "Bought") + " Items Currently**", new String(text), false);
                } else {
                    embedBuilder.addField("**" + (type.equals("topbuyprice") ? "Most Expensive" : (type.equals("topsellprice") ? "Most Valuable" : "Cheapest")) + " Bazaar Items Currently to " + (type.equals("topsellprice") || type.equals("lowsellprice") ? "Sell" : "Buy") + "**", new String(text), false);
                }
                if (type.equals("modifiedtopbuy")) {
                    embedBuilder.addField("You didn't add an argument for this command!", "This is why this command is showing you the bazaar most bought items as defualt. The possible arguments are:\n`topbuy` `topsellprice`\n`topsell` `lowsellprice`\n`topbuyprice` `search <item>`\n`lowbuyprice`", false);
                }
                e.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (ExecutionException | InterruptedException executionException) {
                this.resetCooldown(Objects.requireNonNull(e.getMember()));
                e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(embedBuilder, "An internal bot error occurred. Please contact the developer if this hinders usage.").build()).queue();
                executionException.printStackTrace();
            }
        } else {
            StringBuilder query = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }
                query.append(args[i]);
                if (i != args.length - 1) {
                    query.append(" ");
                }
            }
            String queryString = new String(query);
            queryString = queryString.toUpperCase();
            queryString = queryString.replace(" ", "_");
            BazaarReply reply = null;
            try {
                reply = api.getBazaar().get();
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
                this.resetCooldown(Objects.requireNonNull(e.getMember()));
                e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(new EmbedBuilder(), "An internal bot error occurred. Please contact the developer if this hinders usage.").build()).queue();
                return;
            }
            BazaarReply.Product productFound = reply.getProduct(queryString);
            if (productFound == null) {
                e.getChannel().sendMessage(EmbedCreator.createErrorEmbed(new EmbedBuilder(), "The product `" + queryString + "` was not found.").build()).queue();
                this.resetCooldown(Objects.requireNonNull(e.getMember()));
                return;
            }
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("**" + productFound.getProductId().replace("_", " ") + "**");
            StringBuilder text = new StringBuilder();

            text.append("**Name**: ")
                    .append(productFound.getProductId().toLowerCase().replace("_", " "))
                    .append("\n**Price**: ")
                    .append(getPriceByType("topbuyprice", productFound))
                    .append("\n**Worth (Sell price)**: ")
                    .append(getPriceByType("topsellprice", productFound))
                    .append("\n**Number of sell orders: **")
                    .append(productFound.getQuickStatus().getSellOrders())
                    .append("\n**Number of buy orders: **")
                    .append(productFound.getQuickStatus().getBuyOrders())
                    .append("\n**Number of items in sell orders: **")
                    .append(productFound.getQuickStatus().getSellVolume())
                    .append("\n**Number of items in buy orders: **")
                    .append(productFound.getQuickStatus().getBuyVolume());

            embedBuilder.addField("", new String(text), false);
            embedBuilder.setColor(GlobalMethods.generateRandomColor());

            e.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    private int getVolumeByType(String type, BazaarReply.Product product) {
        if (type.equals("topsell")) {
            return product.getQuickStatus().getSellVolume();
        } else if (type.equals("topbuy") || type.equals("modifiedtopbuy")) {
            return product.getQuickStatus().getBuyVolume();
        }
        return 0;
    }

    private double getPriceByType(String type, BazaarReply.Product product) {
        if (type.equals("topsellprice") || type.equals("lowsellprice")) {
            if (product.getSellSummary().size() < 1) {
                return 0;
            }
            return product.getSellSummary().get(0).getPricePerUnit();
        } else if (type.equals("topbuyprice") || type.equals("lowbuyprice")) {
            if (product.getBuySummary().size() < 1) {
                return 0;
            }
            return product.getBuySummary().get(0).getPricePerUnit();
        }
        return 0;
    }
}
