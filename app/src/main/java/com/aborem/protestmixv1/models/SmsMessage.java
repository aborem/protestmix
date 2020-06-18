package com.aborem.protestmixv1.models;

import java.util.Date;

public class SmsMessage {
    private String phoneNumber;
    private String messageId;
    private String content;
    private Date sentAt;

    public SmsMessage(String phoneNumber, String messageId, String content, Date sentAt) {
        this.content = content;
        this.phoneNumber = phoneNumber;
        this.messageId = messageId;
        this.sentAt = sentAt;
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

    public Date getSentAt() {
        return sentAt;
    }
}
