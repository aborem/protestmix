package com.aborem.protestmixv1.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aborem.protestmixv1.models.MessageModel;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages WHERE phone_number = (:userPhoneNumber)")
    LiveData<List<MessageModel>> getMessagesFrom(String userPhoneNumber);

    @Delete
    void delete(MessageModel message);

    @Query("DELETE FROM messages")
    void deleteAll();

    @Insert
    void insertAll(MessageModel ... messages);
}
