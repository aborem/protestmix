package com.aborem.protestmixv1.models;

import java.util.Date;

/**
 * Wrapper class for MessageModel so we're not accessing the database object directly
 * TODO may be superfluous?
 */
public class MessageModelWrapper {
    private MessageModel message;

    public MessageModelWrapper(MessageModel message) {
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
