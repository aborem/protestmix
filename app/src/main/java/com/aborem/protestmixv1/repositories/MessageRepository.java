package com.aborem.protestmixv1.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.dao.MessageDao;
import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.MessageModel;

import java.util.List;

public class MessageRepository {
    private MessageDao messageDao;
    private LiveData<List<MessageModel>> messagesForConversation;

    public MessageRepository(Application application) {
        AppDatabase appDb = AppDatabase.getInstance(application);
        this.messageDao = appDb.messageDao();
    }

    public LiveData<List<MessageModel>> getMessagesForConversation(String phoneNumber) {
        messagesForConversation = messageDao.getMessagesFrom(phoneNumber);
        return messagesForConversation;
    }

    public void delete(MessageModel message) {
        AppDatabase.databaseWriteExecutor.execute(() -> messageDao.delete(message));
    }

    public void insert(MessageModel message) {
        AppDatabase.databaseWriteExecutor.execute(() -> messageDao.insertAll(message));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> messageDao.deleteAll());
    }
}
