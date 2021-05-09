package com.clankBot.enums.util;

public enum Category {

    DMCommand {
        @Override
        public String toString() {
            return "DM Commands";
        }
    },
    ModerationCommand {
        @Override
        public String toString() {
            return "Moderation Commands";
        }
    },
    CurrencyCommand {
        @Override
        public String toString() {
            return "Currency Commands";
        }
    },
    MiscCommand {
        @Override
        public String toString() {
            return "Misc Commands";
        }
    },
    ServerCommand {
        @Override
        public String toString() {
            return "Server Commands";
        }
    },
    HypixelCommand {
        @Override
        public String toString() {
            return "Hypixel Commands";
        }
    },
    SkyblockCommand {
        @Override
        public String toString() {
            return "Skyblock Commands";
        }
    };

    public static String convCatToCommand(Category category) {
        switch (category) {
            case DMCommand -> {
                return "dm";
            }
            case MiscCommand -> {
                return "misc";
            }
            case CurrencyCommand -> {
                return "currency";
            }
            case ModerationCommand -> {
                return "mod";
            }
            case ServerCommand -> {
                return "server";
            }
            case HypixelCommand -> {
                return "hyp";
            }
            case SkyblockCommand -> {
                return "skyblock";
            }
        }
        return "";
    }

    public static Category convCatToCommand(String command) {
        switch (command) {
            case "dm" -> {
                return Category.DMCommand;
            }
            case "currency" -> {
                return Category.CurrencyCommand;
            }
            case "mod" -> {
                return Category.ModerationCommand;
            }
            case "misc" -> {
                return Category.MiscCommand;
            }
            case "server" -> {
                return Category.ServerCommand;
            }
            case "hyp" -> {
                return Category.HypixelCommand;
            }
            case "skyblock" -> {
                return Category.SkyblockCommand;
            }
        }
        return null;
    }

}
