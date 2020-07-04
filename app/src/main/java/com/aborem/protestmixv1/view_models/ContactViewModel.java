package com.aborem.protestmixv1.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.util.ContactUpdateAction;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository contactRepository;
    private LiveData<List<ContactModel>> contacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        this.contactRepository = new ContactRepository(application);
        this.contacts = contactRepository.getAllContacts();
    }

    public void insert(ContactModel contact) {
        contactRepository.insertOrUpdateUnreadMessageCount(contact, ContactUpdateAction.NOCHANGE);
    }

    public void delete(ContactModel contact) {
        contactRepository.delete(contact);
    }

    public void deleteAll() {
        contactRepository.deleteAll();
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
