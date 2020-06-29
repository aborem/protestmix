package com.aborem.protestmixv1.models;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "contacts")
public class ContactModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "contact_id")
    private String contactId;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    public ContactModel(String phoneNumber) {
        this.contactId = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NonNull String getContactId() {
        return contactId;
    }

    public void setContactId(@NonNull String contactId) {
        this.contactId = contactId;
    }
}
