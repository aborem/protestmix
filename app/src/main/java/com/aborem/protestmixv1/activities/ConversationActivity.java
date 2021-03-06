package com.aborem.protestmixv1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aborem.protestmixv1.Constants;
import com.aborem.protestmixv1.MainActivity;
import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.adapters.MessageListAdapter;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.models.MessageModelWrapper;
import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.util.ContactUpdateAction;
import com.aborem.protestmixv1.util.ProtestMixUtil;
import com.aborem.protestmixv1.view_models.ConversationViewModel;
import com.aborem.protestmixv1.view_models.ConversationViewModelFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements LifecycleOwner {
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 1;
    private String phoneNumber;
    private MessageListAdapter messagesListAdapter;
    private ConversationViewModel conversationViewModel;
    private RecyclerView messageRecyclerView;
    private ContactRepository contactRepository;

    public static void start(Context context, String chatId, String phoneNumber) {
        Intent starter = new Intent(context, ConversationActivity.class);
        starter.putExtra("chat_id", chatId);
        starter.putExtra("phone_number", phoneNumber);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        getPermissionToReadWriteSMS();

        Intent intent = getIntent();
        if (intent != null) {
            phoneNumber = intent.getStringExtra("phone_number");
        }

        messageRecyclerView = findViewById(R.id.recycler_message_list);

        ConversationViewModelFactory factory =
                new ConversationViewModelFactory(getApplication(), phoneNumber);
        conversationViewModel = new ViewModelProvider(this, factory).get(ConversationViewModel.class);
        conversationViewModel.getMessages().observe(this, messageModels ->
                observeMessageLiveData(this, messageModels)
        );

        // Clear out unread messages
        contactRepository = new ContactRepository(getApplication());
        contactRepository.insertOrUpdateUnreadMessageCount(new ContactModel(phoneNumber, 0), ContactUpdateAction.CLEAR);

        EditText textChatBox = findViewById(R.id.edit_text_chatbox);
        Button sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(view -> {
            sendMessage(new MessageModel(phoneNumber, textChatBox.getText().toString(), new Date().getTime(), true));
            textChatBox.setText("");
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (!(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Checks if permissions were enabled by user and sends sms, adds message to view model and
     * to messageListAdapter
     * @param toSend the message to send
     */
    private void sendMessage(MessageModel toSend) {
        SmsManager manager = SmsManager.getDefault();
        if (!hasReadWriteSMSPermissions()) {
            Log.d("sendMessage", "no permissions!");
            getPermissionToReadWriteSMS();
        } else {
            sendSMSMessage(manager, phoneNumber, toSend.getMessageContent());
            Log.d("sendMessage", "message being sent!");
            conversationViewModel.insert(toSend);
            contactRepository.updateLastMessageTimeMs(phoneNumber, System.currentTimeMillis());
        }
    }

    /**
     * Method called when changes are made to conversationViewModel with new messages
     *
     * @param context the context gleaned by the instance of ConversationActivity
     * @param messageModels the updated list of MessageModel objects (database)
     */
    private void observeMessageLiveData(Context context, List<MessageModel> messageModels) {
        Log.d("OBSERVED, new length", String.valueOf(messageModels.size()));
        List<MessageModelWrapper> messageModelWrapperList = new ArrayList<>();
        for (MessageModel messageModel : messageModels) {
            messageModelWrapperList.add(new MessageModelWrapper(messageModel));
        }
        messagesListAdapter = new MessageListAdapter(context, messageModelWrapperList);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        messageRecyclerView.setAdapter(messagesListAdapter);
        if (messageModels.size() > 0) {
            messageRecyclerView.smoothScrollToPosition(messageModels.size()-1);
        }
    }

    /**
     * Sends an SMS message using SmsManager
     * @param manager the SmsManager, passed in to avoid duplicate instances
     * @param phoneNumber phone number as String to send message to
     * @param messageContent content of message to send, with ForwardIndicator so receiving
     *                       number can forward to appropriate number
     */
    private void sendSMSMessage(SmsManager manager, String phoneNumber, String messageContent) {
        manager.sendTextMessage(
                phoneNumber,
                null,
                Constants.FORWARD_INDICATOR + messageContent,
                null,
                null
        );
    }

    /**
     * Checks if application has permissions to read SMS by checking ContextCompat
     * @return true if has permissions, false otherwise
     */
    private boolean hasReadWriteSMSPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Requests read, receive, and send sms permissions from user
     */
    private void getPermissionToReadWriteSMS() {
        if (!hasReadWriteSMSPermissions()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, READ_SMS_PERMISSIONS_REQUEST);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSIONS_REQUEST);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSIONS_REQUEST);
        }
    }
}