package com.aborem.protestmixv1;

import android.app.Application;

import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.repositories.MessageRepository;

// TODO I think this is very bad but at the moment I don't know how to get around it
// todo remove from AndroidManifest as well when found better approach
public class ApplicationWrapper extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationWrapper.application = this;

        MessageRepository messageRepository = new MessageRepository(getApplication(), "+18325609681");
        messageRepository.deleteAll();

        ContactRepository contactRepository = new ContactRepository(getApplication());
        contactRepository.deleteAll();
    }

    public static Application getApplication() {
        return application;
    }
}
