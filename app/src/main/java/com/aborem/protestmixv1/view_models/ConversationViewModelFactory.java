package com.aborem.protestmixv1.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ConversationViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private String phoneNumber;

    public ConversationViewModelFactory(Application application, String phoneNumber) {
        this.application = application;
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConversationViewModel(application, phoneNumber);
    }
}
