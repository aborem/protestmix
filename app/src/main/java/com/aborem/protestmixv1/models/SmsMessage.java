package com.aborem.protestmixv1.models;

import java.util.UUID;

public class SmsMessage {
    private String phoneNumber;
    private String messageId;
    private String content;
    private int sentAtMs;

    public SmsMessage(String phoneNumber, String content, int sentAtMs) {
        this.content = content;
        this.phoneNumber = phoneNumber;
        this.messageId = UUID.randomUUID().toString();
        this.sentAtMs = sentAtMs;
    }

    public String getContent() {
        return content;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSentAt() {
        return sentAtMs;
    }
}
