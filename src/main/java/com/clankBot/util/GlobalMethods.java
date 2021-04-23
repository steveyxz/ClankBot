package com.clankBot.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static com.clankBot.Main.serverDataManagerMongo;

public class GlobalMethods {

    public static boolean hasPermissions(Member target, ArrayList<Permission> permissions) {
        for (Permission permission :
                permissions) {
            if (!PermissionUtil.checkPermission(target)) {
                return true;
            }
        }
        return false;
    }

    public static String getPrefixForGuild(Guild guild) {
        return (String) serverDataManagerMongo.getValueOfKey("serverData", "prefixes", guild.getId());
    }

    public static Color generateRandomColor() {
        Random r = new Random();
        return new Color(r.nextInt(224), r.nextInt(224), r.nextInt(224));
    }

}
