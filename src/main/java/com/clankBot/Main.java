package com.clankBot;

import com.clankBot.commands.*;
import com.clankBot.data.DataManagerMongoDB;
import com.clankBot.data.DataManagerSQLite;
import com.clankBot.enums.util.Category;
import com.clankBot.listeners.CommandListener;
import com.clankBot.util.Cooldown;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.hypixel.api.HypixelAPI;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Main {

    public static JDABuilder builder;
    public static String prefix = "c-";

    public static ArrayList<GuildCommand> guildCommandList = new ArrayList<>();
    public static ArrayList<GuildCommand> dmCommandList = new ArrayList<>();
    public static DataManagerSQLite userDataManagerSQLite;
    public static DataManagerSQLite serverDataManagerSQLite;

    public static DataManagerMongoDB userDataManagerMongo;
    public static DataManagerMongoDB serverDataManagerMongo;

    public static HypixelAPI api;

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

        initCommands();
        initDatabase();
        initHypixelAPI();
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

    private void initHypixelAPI() {
        String pword = "";
        try {
            pword = new String(Objects.requireNonNull(this.getClass().getResourceAsStream("/apiKey.txt")).readAllBytes());
        } catch (IOException event) {
            event.printStackTrace();
        }
        api = new HypixelAPI(UUID.fromString(pword));
    }

    private void initDatabase() {

    }

    private void initCommands() {
        ArrayList<Permission> permissions = new ArrayList<>();
        guildCommandList.add(new HypixelCommand("hypixel", "Gives general info about the status of the hypixel server in minecraft", new String[]{"hypixel", "hy"}, Category.HypixelCommand, permissions, "hypixel", new Cooldown("5s")));
        guildCommandList.add(new HypLoginCommand("hyplogin", "Logs into you hypixel account", new String[]{"hyplogin", "hlogin", "hl"}, Category.HypixelCommand, permissions, "hyplogin [name]", new Cooldown("30s")));
        guildCommandList.add(new HypPlayersCommand("hypplayers", "Gives info about the players in Hypixel", new String[]{"hypplayers", "hplayers", "hypp"}, Category.HypixelCommand, permissions, "hypplayers", new Cooldown("5s")));
        guildCommandList.add((new HypBansCommand("hypbans", "Gives info about the bans in hypixel", new String[]{"hypbans", "hybans", "hbans", "hbs"}, Category.HypixelCommand, permissions, "hypbans", new Cooldown("5s"))));
        guildCommandList.add(new ProfileCommand("profile", "Gives your profile inside ClankBot.", new String[]{"profile", "p", "mydata"}, Category.CurrencyCommand, permissions, "profile", new Cooldown("3s")));
        permissions.add(Permission.MESSAGE_WRITE);
        guildCommandList.add(new PingCommand("ping", "Returns pong: for testing purposes", new String[]{"ping", "pingg", "bing"}, Category.MiscCommand, permissions, "ping", new Cooldown("2s")));
        guildCommandList.add(new HelpCommand("help", "Shows this command", new String[]{"help", "halp", "commands", "commandlist"}, Category.MiscCommand, permissions, "help [category or command]", new Cooldown("4s")));
        permissions.add(Permission.MANAGE_SERVER);
        guildCommandList.add(new ServerPrefixCommand("setprefix", "Sets the prefix for this server", new String[]{"setprefix", "makeprefix", "createprefix", "prefix", "sp"}, Category.ServerCommand, permissions, "setprefix [prefix]", new Cooldown("5m")));
        permissions.add(Permission.KICK_MEMBERS);
        guildCommandList.add(new KickCommand("kick", "Kicks a user", new String[]{"kick", "boot", "k"}, Category.ModerationCommand, permissions, "kick [user] {reason}", new Cooldown("10s")));
        permissions.remove(Permission.KICK_MEMBERS);
        permissions.add(Permission.BAN_MEMBERS);
        guildCommandList.add(new BanCommand("ban", "Bans a user", new String[]{"ban", "hammer", "hardkick", "b"}, Category.ModerationCommand, permissions, "ban [user] {reason}", new Cooldown("10s")));
        permissions.remove(Permission.BAN_MEMBERS);
        permissions.add(Permission.MANAGE_ROLES);
        guildCommandList.add(new MuteCommand("mute", "Mutes a user for a certain time. If user is already muted, it unmutes that user.", new String[]{"mute", "youcanttalk", "m", "moot"}, Category.ModerationCommand, permissions, "mute [user] {time}", new Cooldown("10s")));


        for (GuildCommand command: guildCommandList) {
            command.getCooldown().setTimestamp(0);
        }
    }
}
