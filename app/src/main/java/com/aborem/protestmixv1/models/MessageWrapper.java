package com.aborem.protestmixv1.models;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class MessageWrapper implements IMessage {
    private MessageModel message;

    public MessageWrapper(MessageModel message) {
        this.message = message;
    }

    @Override
    public String getId() {
        return message.getMessageId();
    }

    @Override
    public String getText() {
        return message.getMessageContent();
    }

    @Override
    public IUser getUser() {
        return new UserWrapper(message.getPhoneNumber());
    }

    @Override
    public Date getCreatedAt() {
        return new Date(message.getSentAtMs() * 1000);
    }
}
