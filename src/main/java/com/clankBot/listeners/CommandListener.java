package com.clankBot.listeners;

import com.clankBot.Main;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.clankBot.Main.*;

public class CommandListener extends ListenerAdapter {

    private Main main;
    public CommandListener(Main main) {
        this.main = main;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        ArrayList<Object> rowValues = new ArrayList<>();
        rowValues.add(event.getGuild().getId());
        rowValues.add(Main.prefix);
        try {
            Main.serverDataManager.insert("prefixes", rowValues);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void onReady(@NotNull ReadyEvent e) {
        System.out.println(e.getJDA().getAccountType() + ": {} is ready".replace("{}", e.getJDA().getSelfUser().getName()));
        if (!serverDataManager.doesTableExist("prefixes")) {
            serverDataManager.createTable("prefixes", "id text PRIMARY KEY, prefix text NOT NULL");
            ArrayList<Object> objects = new ArrayList<>();
            for (int i = 0; i < e.getJDA().getGuilds().size(); i++) {
                objects.clear();
                objects.add(e.getJDA().getGuilds().get(i).getId());
                objects.add(prefix);
                try {
                    serverDataManager.insert("prefixes", objects);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        try {
            if (message.startsWith((String) serverDataManager.selectRowWithID(event.getGuild().getId(), "prefixes").get(1))) {
                message = message.substring(((String) (serverDataManager.selectRowWithID(event.getGuild().getId(), "prefixes").get(1))).length());
                String[] args = message.split(" ");
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
                for (int i = 0; i < Main.commandList.size(); i++) {
                    if (Main.commandList.get(i).getName().equals(args[0])) {
                        Main.commandList.get(i).run(realArgs, event);
                        break;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

