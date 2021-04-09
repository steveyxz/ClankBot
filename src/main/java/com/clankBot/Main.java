package com.clankBot;

import com.clankBot.commands.Command;
import com.clankBot.commands.HelpCommand;
import com.clankBot.commands.PingCommand;
import com.clankBot.data.DataManager;
import com.clankBot.enums.util.Category;
import com.clankBot.listeners.CommandListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static JDABuilder builder;
    public static String prefix = "c-";

    public static ArrayList<Command> commandList = new ArrayList<>();

    public static void main(String[] args) throws MalformedURLException, SQLException {
        new Main();
    }

    public static DataManager userDataManager;
    public static DataManager serverDataManager;

    public Main() {
        builder = JDABuilder.create(GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_WEBHOOKS);
        builder.setToken("ODI5NjQ4MTIwOTAzMjM3NjMy.YG7MBg.9VM252X_JY701_JXSF_D-Pmy03k");
        builder.addEventListeners(new CommandListener(this));
        builder.setActivity(Activity.listening(prefix + "help"));
        userDataManager = new DataManager("/data/userData.db");
        serverDataManager = new DataManager("/data/serverData.db");
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        initCommands();
        initDatabase();
    }

    private void initDatabase() {

    }

    private void initCommands() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MESSAGE_WRITE);
        commandList.add(new PingCommand("ping", Category.MiscCommand, permissions));
        commandList.add(new HelpCommand("help", Category.MiscCommand, permissions));
    }

    public static ArrayList<Command> getCommandsForCategory(Category category) {
        ArrayList<Command> returnList = new ArrayList<>();
        for (int i = 0; i < commandList.size(); i++) {
            if (commandList.get(i).getCategory() == category) {
                returnList.add(commandList.get(i));
            }
        }
        return returnList;
    }
}
