package com.aborem.protestmixv1.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.dao.ContactDao;
import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.ContactModel;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<ContactModel>> allContacts;
    private LiveData<Boolean> entryExists;

    public ContactRepository(Application application) {
        AppDatabase appDb = AppDatabase.getInstance(application);
        this.contactDao = appDb.contactDao();
        this.allContacts = contactDao.getAll();
    }

    public LiveData<List<ContactModel>> getAllContacts() {
        return this.allContacts;
    }

    public LiveData<Boolean> entryExists(String phoneNumber) {
        return contactDao.entryExists(phoneNumber);
    }

    public void insert(ContactModel contact) {
        AppDatabase.databaseWriteExecutor.execute(() -> contactDao.insertAll(contact));
    }

    public void delete(ContactModel contact) {
        AppDatabase.databaseWriteExecutor.execute(() -> contactDao.delete(contact));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> contactDao.deleteAll());
    }
}
