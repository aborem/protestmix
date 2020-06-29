package com.aborem.protestmixv1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(view.getContext());
            }
        });

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
     * UNUSED Cleans phone number using regex
     * @param rawPhoneNumber the phone number pre formatted
     * @return the phone number formatted
     */
    private String cleanPhoneNumber(String rawPhoneNumber) {
        Matcher m = Pattern.compile(Constants.REGEX_CLEAN_NUMBER).matcher(rawPhoneNumber);
        List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group());
        }
        return TextUtils.join("", matches);
    }

    /**
     * Creates view for and displays dialog box for new phone number
     * @param c the context in which the dialog should be displayed
     */
    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        taskEditText.setMaxLines(1);
        new MaterialAlertDialogBuilder(c)
                .setTitle("Start a conversation")
                .setView(taskEditText)
                .setPositiveButton("Create", ((dialogInterface, i) ->
                        positiveButtonDialogAction(taskEditText)))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Function called when positive/confirm button is clicked in dialog box to create new
     * conversation
     * @param taskEditText the edit text entry for the phone number
     */
    private void positiveButtonDialogAction(EditText taskEditText) {
        String phoneNumber = String.valueOf(taskEditText.getText());
        if (!Pattern.compile(Constants.REGEX_PHONE_NUMBER).matcher(phoneNumber).matches()) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
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