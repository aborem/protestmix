package com.aborem.protestmixv1.activities;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.aborem.protestmixv1.MainActivity;
import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.adapters.ChatAdapter;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.util.ProtestMixUtil;
import com.aborem.protestmixv1.view_models.ContactViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;

    /**
     * Starter method for activity that loads intent
     * @param context the context where the activity should start
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
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getContacts().observe(this, this::updateUIWithChats);
        List<ContactModel> contacts = contactViewModel.getContacts().getValue();
        if (contacts != null) {
            updateUIWithChats(contacts);
        } else {
            updateUIWithChats(new ArrayList<>());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all_data) {
            ProtestMixUtil.clearAllData(getApplication());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Updates recycler view with new contact cards from database. Only shows non-empty chats
     * @param contactModels list of updated contacts
     */
    protected void updateUIWithChats(List<ContactModel> contactModels) {
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(contactModels, (cM1, cM2) -> (int) (cM2.getLastMessageTimeMs() - cM1.getLastMessageTimeMs()));
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
        if (contactViewModel.contactExists(phoneNumber)) {
            Toast.makeText(this, "Conversation already exists", Toast.LENGTH_SHORT)
                    .show();
        } else {
            ContactModel newContact = new ContactModel(phoneNumber, 0);
            contactViewModel.insert(newContact);
            ConversationActivity.start(this, newContact.getContactId(), phoneNumber);
        }
    }
}