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
    }

}
