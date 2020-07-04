package com.aborem.protestmixv1.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.repositories.MessageRepository;

import java.util.List;

public class ConversationViewModel extends AndroidViewModel {
    private MessageRepository repository;
    private LiveData<List<MessageModel>> messages;

    public ConversationViewModel(@NonNull Application application, String phoneNumber) {
        super(application);
        this.repository = new MessageRepository(application);
        this.messages = repository.getMessagesForConversation(phoneNumber);
    }

    public void delete(MessageModel messageModel) {
        repository.delete(messageModel);
    }

    public void insert(MessageModel messageModel) {
        repository.insert(messageModel);
    }

    public LiveData<List<MessageModel>> getMessages() {
        return messages;
    }
}
