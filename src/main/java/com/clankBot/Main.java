package com.clankBot;

import com.clankBot.commands.*;
import com.clankBot.data.DataManagerMongoDB;
import com.clankBot.data.DataManagerSQLite;
import com.clankBot.enums.util.Category;
import com.clankBot.listeners.CommandListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static JDABuilder builder;
    public static String prefix = "c-";

    public static ArrayList<GuildCommand> guildCommandList = new ArrayList<>();
    public static ArrayList<GuildCommand> dmCommandList = new ArrayList<>();
    public static DataManagerSQLite userDataManagerSQLite;
    public static DataManagerSQLite serverDataManagerSQLite;

    public static DataManagerMongoDB userDataManagerMongo;
    public static DataManagerMongoDB serverDataManagerMongo;

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
        try {
            byte[] tokenBytes = Objects.requireNonNull(getClass().getResourceAsStream("/token.txt")).readAllBytes();
            builder.setToken(new String(tokenBytes))
                    .addEventListeners(new CommandListener(this))
                    .setActivity(Activity.listening(prefix + "help"))
                    .setStatus(OnlineStatus.IDLE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        //userDataManagerSQLite = new DataManagerSQLite("/data/userData.db");
        //serverDataManagerSQLite = new DataManagerSQLite("/data/serverData.db");

        userDataManagerMongo = new DataManagerMongoDB("userData");
        serverDataManagerMongo = new DataManagerMongoDB("serverData");

        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        initPrefix();
        initCommands();
        initDatabase();
    }

    public static void main(String[] args) throws MalformedURLException, SQLException {
        new Main();
    }

    public static ArrayList<GuildCommand> getCommandsForCategory(Category category) {
        ArrayList<GuildCommand> returnList = new ArrayList<>();
        for (int i = 0; i < guildCommandList.size(); i++) {
            if (guildCommandList.get(i).getCategory() == category) {
                returnList.add(guildCommandList.get(i));
            }
        }
        for (int i = 0; i < dmCommandList.size(); i++) {
            if (dmCommandList.get(i).getCategory() == category) {
                returnList.add(dmCommandList.get(i));
            }
        }
        return returnList;
    }

    private void initPrefix() {

    }

    private void initDatabase() {

    }

    private void initCommands() {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MESSAGE_WRITE);
        guildCommandList.add(new PingCommand("ping", "Returns pong: for testing purposes", new String[] {"ping", "pingg", "bing"}, Category.MiscCommand, permissions, "ping"));
        guildCommandList.add(new HelpCommand("help", "Shows this command", new String[] {"help", "halp", "commands", "commandlist"}, Category.MiscCommand, permissions, "help [category or command]"));
        permissions.add(Permission.MANAGE_SERVER);
        guildCommandList.add(new ServerPrefixCommand("setprefix", "Sets the prefix for this server", new String[] {"setPrefix", "makePrefix", "createPrefix", "prefix", "sp"}, Category.ServerCommand, permissions, "setprefix [prefix]"));
        permissions.add(Permission.KICK_MEMBERS);
        guildCommandList.add(new KickCommand("kick", "Kicks a user", new String[] {"kick", "boot", "k"}, Category.ModerationCommand, permissions, "kick [user] {reason}"));
        permissions.remove(Permission.KICK_MEMBERS);
        permissions.add(Permission.BAN_MEMBERS);
        guildCommandList.add(new BanCommand("ban", "Bans a user",  new String[] {"ban", "hammer", "hardkick", "b"}, Category.ModerationCommand, permissions, "ban [user] {reason}"));
        permissions.remove(Permission.BAN_MEMBERS);
        permissions.add(Permission.MANAGE_ROLES);
        guildCommandList.add(new MuteCommand("mute", "Mutes a user for a certain time. If user is already muted, it unmutes that user.", new String[] {"mute", "youcanttalk", "m", "moot"}, Category.ModerationCommand, permissions, "mute [user] {time}"));
    }
}
