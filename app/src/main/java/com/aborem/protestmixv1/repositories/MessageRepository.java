package com.aborem.protestmixv1.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.dao.MessageDao;
import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.MessageModel;

import java.util.List;

public class MessageRepository {
    private MessageDao messageDao;
    private LiveData<List<MessageModel>> messagesForConversation;

    public MessageRepository(Application application, String phoneNumber) {
        AppDatabase appDb = AppDatabase.getInstance(application);
        this.messageDao = appDb.messageDao();
        this.messagesForConversation = messageDao.getMessagesFrom(phoneNumber);
    }

    public LiveData<List<MessageModel>> getMessagesForConversation() {
        return this.messagesForConversation;
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
