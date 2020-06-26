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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.adapters.MessageListAdapter;
import com.aborem.protestmixv1.models.MessageWrapper;
import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.view_models.ConversationViewModel;
import com.aborem.protestmixv1.view_models.ConversationViewModelFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements LifecycleOwner {
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private String phoneNumber;
    private MessageListAdapter messagesListAdapter;
    private ConversationViewModel conversationViewModel;
    private RecyclerView messageRecyclerView;

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

        getPermissionToReadSMS();

        Intent intent = getIntent();
        if (intent != null) {
            phoneNumber = intent.getStringExtra("phone_number");
        }

        messageRecyclerView = findViewById(R.id.recycler_message_list);

        ConversationViewModelFactory factory = new ConversationViewModelFactory(getApplication(), phoneNumber);
        conversationViewModel = new ViewModelProvider(this, factory).get(ConversationViewModel.class);
        conversationViewModel.getMessages().observe(this, messageModels ->
                observeMessageLiveData(this, messageModels)
        );

        // call for first time to prepopulate todo figure out if necessary
//        List<MessageModel> prePopulatedMessages = conversationViewModel.getMessages().getValue();
//        if (prePopulatedMessages != null) {
//            observeMessageLiveData(this, prePopulatedMessages);
//        }

        EditText textChatBox = findViewById(R.id.edit_text_chatbox);
        Button sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(view -> {
            sendMessage(new MessageModel(phoneNumber, textChatBox.getText().toString(), new Date().getTime(), true));
            textChatBox.setText("");
        });
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
     * Requests read sms permissions from user and displays Toast
     */
    private void getPermissionToReadSMS() {
        if (!hasReadSMSPermissions()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    /**
     * Checks if application has permissions to read SMS by checking ContextCompat
     * @return true if has permissions, false otherwise
     */
    private boolean hasReadSMSPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks if permissions were enabled by user and sends sms, adds message to view model and
     * to messageListAdapter
     * @param toSend the message to send
     */
    private void sendMessage(MessageModel toSend) {
        SmsManager manager = SmsManager.getDefault();
        if (!hasReadSMSPermissions()) {
            Log.d("sendMessage", "no permissions!");
            getPermissionToReadSMS();
        } else {
            manager.sendTextMessage(phoneNumber, null, toSend.getMessageContent(), null, null);
            Log.d("sendMessage", "message being sent!");
            conversationViewModel.insertAll(toSend);
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
        List<MessageWrapper> messageWrapperList = new ArrayList<>();
        for (MessageModel messageModel : messageModels) {
            messageWrapperList.add(new MessageWrapper(messageModel));
        }
        messagesListAdapter = new MessageListAdapter(context, messageWrapperList);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        messageRecyclerView.setAdapter(messagesListAdapter);
        if (messageModels.size() > 0) {
            messageRecyclerView.smoothScrollToPosition(messageModels.size()-1);
        }
    }
}