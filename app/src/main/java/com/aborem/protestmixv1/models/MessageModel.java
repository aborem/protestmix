package com.aborem.protestmixv1.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "messages")
public class MessageModel {
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    private String messageId;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "message_content")
    private String messageContent;

    @ColumnInfo(name = "sent_at_ms")
    private long sentAtMs;

    public MessageModel(String phoneNumber, String messageContent, long sentAtMs) {
        this.messageContent = messageContent;
        this.phoneNumber = phoneNumber;
        this.messageId = UUID.randomUUID().toString();
        this.sentAtMs = sentAtMs;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getSentAt() {
        return sentAtMs;
    }

}
