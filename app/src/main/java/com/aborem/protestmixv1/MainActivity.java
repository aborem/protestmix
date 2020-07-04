package com.aborem.protestmixv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aborem.protestmixv1.activities.ConversationActivity;
import com.aborem.protestmixv1.activities.MessageListActivity;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.models.ForwardInfo;
import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.repositories.ForwardInfoRepository;
import com.aborem.protestmixv1.util.ProtestMixUtil;
import com.aborem.protestmixv1.view_models.ContactViewModel;
import com.aborem.protestmixv1.view_models.ConversationViewModel;
import com.aborem.protestmixv1.view_models.ConversationViewModelFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText newContactEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adds contact info
        Button buttonNewContact = findViewById(R.id.new_contact_phone_number_enter);
        buttonNewContact.setOnClickListener(view -> createNewContact(view.getContext()));
        newContactEditText = findViewById(R.id.new_contact_phone_number);
        newContactEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // TODO change to authentication
        Button enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(view -> MessageListActivity.start(this));
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
            return true;
        }
        return false;
    }

    private void createNewContact(Context context) {
        if (newContactEditText.getText() != null) {
            String phoneNumber = ProtestMixUtil.formatPhoneNumber(newContactEditText.getText().toString());
            ContactViewModel contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
            if (contactViewModel.contactExists(phoneNumber)) {
                Toast.makeText(this, "Contact already added", Toast.LENGTH_SHORT)
                        .show();
            } else {
                ContactModel newContact = new ContactModel(phoneNumber, 0);
                contactViewModel.insert(newContact);

                ForwardInfoRepository forwardInfoRepository = new ForwardInfoRepository(getApplication());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                forwardInfoRepository.insert(new ForwardInfo(phoneNumber, calendar.getTimeInMillis()));
            }
        }
    }
}