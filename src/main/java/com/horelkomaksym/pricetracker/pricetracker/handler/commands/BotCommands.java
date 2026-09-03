package com.horelkomaksym.pricetracker.pricetracker.handler.commands;

import java.util.Objects;

public enum BotCommands {
    START("/start"), ADD("/add"), LIST("/list"), SHOPS("/shops"), UNKNOWN("UNKNOWN");

    private final String s;

    BotCommands(String s) {
        this.s = s;
    }

    public static BotCommands valueOfOption(String text) {
        for (BotCommands e : values()) {
            if (Objects.equals(e.s, text)) {
                return e;
            }
        }
        return BotCommands.UNKNOWN;
    }
    @Override
    public String toString() {
        return s;
    }
}
