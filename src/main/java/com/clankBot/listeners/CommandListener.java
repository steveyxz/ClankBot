package com.clankBot.listeners;

import com.clankBot.Main;
import com.clankBot.util.GlobalMethods;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import static com.clankBot.Main.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.clankBot.Main.prefix;
import static com.clankBot.Main.serverDataManagerMongo;

public class CommandListener extends ListenerAdapter {

    private final Main main;

    public CommandListener(Main main) {
        this.main = main;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        HashMap<String, Object> rowValues = new HashMap<>();
        rowValues.put(event.getGuild().getId(), prefix);

        serverDataManagerMongo.insertToDocument("serverData", "prefixes", rowValues);

    }

    public void onReady(@NotNull ReadyEvent e) {
        System.out.println(e.getJDA().getAccountType() + ": {} is ready".replace("{}", e.getJDA().getSelfUser().getName()));
        //if (!serverDataManagerSQLite.doesTableExist("prefixes")) {
        //serverDataManagerSQLite.createTable("prefixes", "id text PRIMARY KEY, prefix text NOT NULL");
        if (!serverDataManagerMongo.doesDocumentExist("prefixes", "serverData")) {
            serverDataManagerMongo.createDocument("prefixes", "serverData");
            HashMap<String, Object> objects = new HashMap<>();
            for (int i = 0; i < e.getJDA().getGuilds().size(); i++) {
                objects.clear();
                objects.put(e.getJDA().getGuilds().get(i).getId(), prefix);

                serverDataManagerMongo.insertToDocument("serverData", "prefixes", objects);

            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        //if (message.startsWith((String) serverDataManagerSQLite.selectRowWithID(event.getGuild().getId(), "prefixes").get(1))) {
        if (message.startsWith((String) serverDataManagerMongo.getValueOfKey("serverData", "prefixes", event.getGuild().getId()))) {
            message = message.substring(((String) (serverDataManagerMongo.getValueOfKey("serverData", "prefixes", event.getGuild().getId()))).length());
            String[] args = message.split("\\s+");
            if (args.length < 1) {
                return;
            }
            String[] realArgs = new String[args.length - 1];
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }
                realArgs[i - 1] = args[i];
            }
            for (int i = 0; i < Main.guildCommandList.size(); i++) {
                if (Arrays.asList(guildCommandList.get(i).getAliases()).contains(args[0])) {
                    Main.guildCommandList.get(i).run(realArgs, event);
                    break;
                }
            }
        } else if (message.startsWith("<@!829648120903237632>")) {
            event.getChannel().sendMessage("The prefix for this server is `" + GlobalMethods.getPrefixForGuild(event.getGuild()) + "`").queue();
        }
    }
}

