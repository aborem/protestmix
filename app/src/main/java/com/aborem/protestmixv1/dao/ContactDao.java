package com.aborem.protestmixv1.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.aborem.protestmixv1.models.ContactModel;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts")
    LiveData<List<ContactModel>> getAll();

    @Delete
    void delete(ContactModel contact);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ContactModel contact);

    @Query("DELETE FROM contacts")
    void deleteAll();

    @Query("SELECT * FROM contacts WHERE phone_number = (:phoneNumber)")
    List<ContactModel> retrieveEntry(String phoneNumber);

    @Update
    void update(ContactModel contactModel);

    @Query("UPDATE contacts SET unread_message_count = (:unreadMessageCount) WHERE phone_number = (:phoneNumber)")
    void updateMessageCount(int unreadMessageCount, String phoneNumber);
}
