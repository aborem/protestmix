package com.aborem.protestmixv1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.adapters.ChatAdapter;
import com.aborem.protestmixv1.models.Chat;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.view_models.MessageListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private MessageListViewModel messageListViewModel;

    public static void start(Context context) {
        Intent starter = new Intent(context, MessageListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageListViewModel = new ViewModelProvider(this).get(MessageListViewModel.class);
        messageListViewModel.getContacts().observe(this, new Observer<List<ContactModel>>() {
            @Override
            public void onChanged(List<ContactModel> contactModels) {
                // todo figure out how to call updateUIWithChats from here (access to `this` isn't working)
                updateUIWithChats(contactModels);
            }
        });
        updateUIWithChats(messageListViewModel.getContacts().getValue());
    }

    protected void updateUIWithChats(List<ContactModel> contactModels) {
//        AppDatabase messageDb = AppDatabase.getInstance(this);
//        LiveData<List<String>> activeConversations = messageDb.messageDao().getActiveConversations();
//        activeConversations.observe(this, );


        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter chatAdapter = new ChatAdapter(contactModels, this);
        chatRecyclerView.setAdapter(chatAdapter);
    }
}