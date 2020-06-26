package com.aborem.protestmixv1.models;

import java.util.Date;

public class MessageWrapper {
    private MessageModel message;

    public MessageWrapper(MessageModel message) {
        this.message = message;
    }

    public String getId() {
        return message.getMessageId();
    }

    public String getText() {
        return message.getMessageContent();
    }

    public UserWrapper getUser() {
        return new UserWrapper(message.getPhoneNumber());
    }

    public Date getCreatedAt() {
        return new Date(message.getSentAtMs() * 1000);
    }

    public boolean isSentByUser() {
        return message.isSentByUser();
    }
}
