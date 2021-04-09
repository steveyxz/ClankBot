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
        public String toString() { return "Misc Commands"; }
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
        }
        return Category.MiscCommand;
    }

}
