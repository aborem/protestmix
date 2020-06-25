package com.aborem.protestmixv1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aborem.protestmixv1.Constants;
import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.adapters.ChatAdapter;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.repositories.MessageRepository;
import com.aborem.protestmixv1.view_models.MessageListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        FloatingActionButton buttonAddNote = findViewById(R.id.add_conversation_button);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(view.getContext());
            }
        });

        messageListViewModel = new ViewModelProvider(this).get(MessageListViewModel.class);
        messageListViewModel.getContacts().observe(this, contactModels -> {
            // todo is this right? is reference to `this` correct?
            updateUIWithChats(contactModels);
        });
        List<ContactModel> contacts = messageListViewModel.getContacts().getValue();
        if (contacts != null) {
            updateUIWithChats(contacts);
        } else {
            updateUIWithChats(new ArrayList<>());
        }
    }

    protected void updateUIWithChats(List<ContactModel> contactModels) {
        // todo maybe find better way of doing this that doesn't involve setting the adapter each time?
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter chatAdapter = new ChatAdapter(contactModels, this);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private String cleanPhoneNumber(String rawPhoneNumber) {
        Matcher m = Pattern.compile(Constants.REGEX_CLEAN_NUMBER).matcher(rawPhoneNumber);
        List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group());
        }
        return TextUtils.join("", matches);
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setMaxLines(1);
        new MaterialAlertDialogBuilder(c)
                .setTitle("Start a conversation")
                .setView(taskEditText)
                .setPositiveButton("Create", ((dialogInterface, i) -> {
                    String phoneNumber = String.valueOf(taskEditText.getText());
                    if (!Pattern.compile(Constants.REGEX_PHONE_NUMBER).matcher(phoneNumber).matches()) {
                        Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    String cleanPhoneNumber = cleanPhoneNumber(phoneNumber);
                    Log.d("matching phonenumber", cleanPhoneNumber);
                    if (messageListViewModel.contactExists(cleanPhoneNumber)) {
                        Toast.makeText(
                                this,
                                "Conversation already exists",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        ContactModel newContact = new ContactModel(cleanPhoneNumber);
                        messageListViewModel.insert(newContact);
                        ConversationActivity.start(this, newContact.getContactId(), phoneNumber);
                    }
                }))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}