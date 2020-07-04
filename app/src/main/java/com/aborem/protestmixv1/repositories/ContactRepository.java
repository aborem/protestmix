package com.aborem.protestmixv1.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.dao.ContactDao;
import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.util.ContactUpdateAction;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<ContactModel>> allContacts;

    public ContactRepository(Application application) {
        AppDatabase appDb = AppDatabase.getInstance(application);
        this.contactDao = appDb.contactDao();
        this.allContacts = contactDao.getAll();
    }

    public LiveData<List<ContactModel>> getAllContacts() {
        return this.allContacts;
    }

    /**
     * Checks if contact with phone number already exists. If it does, it performs the action in
     * `updateAction` but does not modify any other fields. If it does not exist, it creates an
     * instance and ignores updateAction.
     * @param contact the ContactModel to update
     * @param updateAction the action to perform on unreadMessageCount
     */
    public void insertOrUpdateUnreadMessageCount(ContactModel contact, ContactUpdateAction updateAction) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ContactModel> retrievedContactModels = contactDao.retrieveEntry(contact.getPhoneNumber());
            if (retrievedContactModels.isEmpty()) {
                contactDao.insert(contact);
            } else {
                ContactModel retrieved = retrievedContactModels.get(0);
                switch (updateAction) {
                    case CLEAR:
                        retrieved.setUnreadMessageCount(0);
                        break;
                    case INCREMENT:
                        retrieved.incrementUnreadMessageCount();
                        break;
                    case DECREMENT:
                        retrieved.decrementUnreadMessageCount();
                        break;
                }
                contactDao.update(retrieved);
            }
        });
    }

    /**
     * Updates the `lastMessageTimeMs` field of the contact entry corresponding to `phoneNumber`
     * @param phoneNumber the phoneNumber of the entry to update
     * @param lastMessageTimeMs the value to update column with
     */
    public void updateLastMessageTimeMs(String phoneNumber, long lastMessageTimeMs) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<ContactModel> retrievedContactModels = contactDao.retrieveEntry(phoneNumber);
            ContactModel retrieved = retrievedContactModels.get(0);
            retrieved.setLastMessageTimeMs(lastMessageTimeMs);
            contactDao.update(retrieved);
        });
    }

    public void delete(ContactModel contact) {
        AppDatabase.databaseWriteExecutor.execute(() -> contactDao.delete(contact));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> contactDao.deleteAll());
    }
}
