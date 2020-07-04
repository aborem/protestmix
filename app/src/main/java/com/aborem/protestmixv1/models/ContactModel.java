package com.aborem.protestmixv1.models;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.aborem.protestmixv1.repositories.ContactRepository;

import java.util.UUID;

@Entity(tableName = "contacts")
public class ContactModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "contact_id")
    private String contactId;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "unread_message_count")
    private int unreadMessageCount;

    @ColumnInfo(name = "last_messsage_time_ms")
    private long lastMessageTimeMs;

    @Ignore
    public ContactModel(@NonNull String contactId, String phoneNumber, int unreadMessageCount, long lastMessageTimeMs) {
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.unreadMessageCount = unreadMessageCount;
        this.lastMessageTimeMs = lastMessageTimeMs;
    }

    @Ignore
    public ContactModel(String phoneNumber, int unreadMessageCount, long lastMessageTimeMs) {
        this.contactId = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
        this.unreadMessageCount = unreadMessageCount;
        this.lastMessageTimeMs = lastMessageTimeMs;
    }

    public ContactModel(String phoneNumber, int unreadMessageCount) {
        this.contactId = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
        this.unreadMessageCount = unreadMessageCount;
        this.lastMessageTimeMs = 0;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NonNull String getContactId() {
        return contactId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setContactId(@NonNull String contactId) {
        this.contactId = contactId;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public void incrementUnreadMessageCount() {
        this.unreadMessageCount++;
    }

    public void decrementUnreadMessageCount() {
        this.unreadMessageCount++;
    }

    public long getLastMessageTimeMs() {
        return lastMessageTimeMs;
    }

    public void setLastMessageTimeMs(long lastMessageTimeMs) {
        this.lastMessageTimeMs = lastMessageTimeMs;
    }
}
