package com.aborem.protestmixv1.models;

import android.os.Message;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.StringWriter;
import java.util.UUID;

@Entity(tableName = "messages")
public class MessageModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "message_id")
    private String messageId;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "message_content")
    private String messageContent;

    @ColumnInfo(name = "sent_at_ms")
    private long sentAtMs;

    @Ignore
    public MessageModel(@NonNull String messageId, String phoneNumber, String messageContent, long sentAtMs) {
        this.messageContent = messageContent;
        this.messageId = messageId;
        this.phoneNumber = phoneNumber;
        this.sentAtMs = sentAtMs;
    }

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

    public long getSentAtMs() {
        return sentAtMs;
    }

    public void setMessageId(@NonNull String messageId) {
        this.messageId = messageId;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSentAtMs(long sentAtMs) {
        this.sentAtMs = sentAtMs;
    }
}
