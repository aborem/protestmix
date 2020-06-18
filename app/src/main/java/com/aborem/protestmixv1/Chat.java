package com.aborem.protestmixv1;

public class Chat {
    private String name;
    private String guid;

    public Chat(String name, String guid) {
        this.name = name;
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }
}