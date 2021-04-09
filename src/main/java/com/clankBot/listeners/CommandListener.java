package com.clankBot.listeners;

import com.clankBot.Main;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    private Main main;
    public CommandListener(Main main) {
        this.main = main;
    }

    public void onReady(@NotNull ReadyEvent e) {
        System.out.println(e.getJDA().getAccountType() + ": {} is ready".replace("{}", e.getJDA().getSelfUser().getName()));
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.startsWith(Main.prefix)) {
            message = message.substring(Main.prefix.length());
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
                }
            }
        }
    }
}
