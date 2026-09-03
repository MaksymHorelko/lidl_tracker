package com.horelkomaksym.pricetracker.pricetracker.parser;

public enum SupportedShops {
    LIDL("Lidl");

    private final String s;

    SupportedShops(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
