package com.aborem.protestmixv1.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "sent_by_user")
    private boolean sentByUser;

    @Ignore
    public MessageModel(@NonNull String messageId, String phoneNumber, String messageContent, long sentAtMs, boolean sentByUser) {
        this.messageContent = messageContent;
        this.messageId = messageId;
        this.phoneNumber = phoneNumber;
        this.sentAtMs = sentAtMs;
        this.sentByUser = sentByUser;
    }

    public MessageModel(String phoneNumber, String messageContent, long sentAtMs, boolean sentByUser) {
        this.messageContent = messageContent;
        this.phoneNumber = phoneNumber;
        this.messageId = UUID.randomUUID().toString();
        this.sentAtMs = sentAtMs;
        this.sentByUser = sentByUser;
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

    public boolean isSentByUser() {
        return sentByUser;
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

    public void setSentByUser(boolean sentByUser) {
        this.sentByUser = sentByUser;
    }
}
