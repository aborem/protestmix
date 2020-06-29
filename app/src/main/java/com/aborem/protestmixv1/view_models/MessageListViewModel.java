package com.aborem.protestmixv1.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.repositories.ContactRepository;

import java.util.List;

public class MessageListViewModel extends AndroidViewModel {
    private ContactRepository repository;
    private LiveData<List<ContactModel>> contacts;

    public MessageListViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContactRepository(application);
        this.contacts = repository.getAllContacts();
    }

    public void insert(ContactModel contact) {
        repository.insert(contact);
    }

    public void delete(ContactModel contact) {
        repository.delete(contact);
    }

    public LiveData<List<ContactModel>> getContacts() {
        return contacts;
    }

    /**
     * Checks if contact exists by searching through contacts in database
     * TODO find more efficient way of doing this with particular query? not sure
     * @param phoneNumber phone number to find
     * @return boolean indicating if contact exists (true) or not (false)
     */
    public boolean contactExists(String phoneNumber) {
        if (getContacts() != null && getContacts().getValue() != null) {
            for (ContactModel contact : getContacts().getValue()) {
                if (contact.getPhoneNumber().equals(phoneNumber)) {
                    return true;
                }
            }
        }
        return false;
    }
}
