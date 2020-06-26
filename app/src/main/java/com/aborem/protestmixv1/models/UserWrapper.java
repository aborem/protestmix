package com.aborem.protestmixv1.models;

import java.util.UUID;

public class UserWrapper {
    private String phoneNumber;
    private String userId;

    public UserWrapper(String phoneNumber) {
        this.userId = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return userId;
    }

    public String getName() {
        return phoneNumber;
    }
}
