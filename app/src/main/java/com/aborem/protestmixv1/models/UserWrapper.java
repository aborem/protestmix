package com.aborem.protestmixv1.models;

import com.stfalcon.chatkit.commons.models.IUser;
import java.util.UUID;

public class UserWrapper implements IUser {
    private String phoneNumber;
    private String userId;

    public UserWrapper(String phoneNumber) {
        this.userId = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getName() {
        return phoneNumber;
    }

    @Override
    public String getAvatar() {
        return null;
    }
}
