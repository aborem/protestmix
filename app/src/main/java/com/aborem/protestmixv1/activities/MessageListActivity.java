package com.aborem.protestmixv1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.adapters.ChatAdapter;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.util.ProtestMixUtil;
import com.aborem.protestmixv1.view_models.MessageListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private MessageListViewModel messageListViewModel;

    /**
     * Starter method for activity that loads intent
     * @param context
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, MessageListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // Adds action to + button
        FloatingActionButton buttonAddNote = findViewById(R.id.add_conversation_button);
        buttonAddNote.setOnClickListener(view -> showAddItemDialog(view.getContext()));

        // Observes for changes of contacts
        messageListViewModel = new ViewModelProvider(this).get(MessageListViewModel.class);
        messageListViewModel.getContacts().observe(this, this::updateUIWithChats);
        List<ContactModel> contacts = messageListViewModel.getContacts().getValue();
        if (contacts != null) {
            updateUIWithChats(contacts);
        } else {
            updateUIWithChats(new ArrayList<>());
        }
    }

    /**
     * Updates recycler view with new contact cards from database
     * @param contactModels list of updated contacts
     */
    protected void updateUIWithChats(List<ContactModel> contactModels) {
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter chatAdapter = new ChatAdapter(contactModels, this);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    /**
     * Creates view for and displays dialog box for new phone number
     * @param c the context in which the dialog should be displayed
     */
    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        taskEditText.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN
                && i == KeyEvent.KEYCODE_ENTER) {
                positiveButtonDialogAction(taskEditText.getText());
                return true;
            }
            return false;
        });
        taskEditText.setMaxLines(1);
        new MaterialAlertDialogBuilder(c)
                .setTitle("Start a conversation")
                .setView(taskEditText)
                .setPositiveButton("Create", ((dialogInterface, i) ->
                        positiveButtonDialogAction(taskEditText.getText()))
                )
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Function called when positive/confirm button is clicked in dialog box to create new
     * conversation
     * @param editTextContent the editable text in the edit text element
     */
    private void positiveButtonDialogAction(Editable editTextContent) {
        String phoneNumber = ProtestMixUtil.formatPhoneNumber(String.valueOf(editTextContent));
        Log.d("matching phonenumber", phoneNumber);
        if (messageListViewModel.contactExists(phoneNumber)) {
            Toast.makeText(this, "Conversation already exists", Toast.LENGTH_SHORT)
                    .show();
        } else {
            ContactModel newContact = new ContactModel(phoneNumber);
            messageListViewModel.insert(newContact);
            ConversationActivity.start(this, newContact.getContactId(), phoneNumber);
        }
    }
}