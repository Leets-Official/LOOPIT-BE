package com.example.loopitbe.enums;

public enum ImageDomain {
    PRODUCT("products"),
    CHAT("chats"),
    PROFILE("profiles");

    private final String prefix;

    ImageDomain(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
