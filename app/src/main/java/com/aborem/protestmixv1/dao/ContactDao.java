package com.aborem.protestmixv1.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aborem.protestmixv1.models.ContactModel;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts")
    LiveData<List<ContactModel>> getAll();

    @Delete
    void delete(ContactModel contact);

    @Insert
    void insertAll(ContactModel ... contacts);
}
