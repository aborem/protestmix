package com.aborem.protestmixv1.models;

public class Chat {
    private String phoneNumber;
    private String guid;

    public Chat(String phoneNumber, String guid) {
        this.phoneNumber = phoneNumber;
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}