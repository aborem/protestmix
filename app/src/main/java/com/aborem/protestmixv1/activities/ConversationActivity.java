package com.aborem.protestmixv1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

public class ConversationActivity extends AppCompatActivity {
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private String phoneNumber;
    private MessageListAdapter messagesListAdapter;
    private ConversationViewModel conversationViewModel;

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        }

        Intent intent = getIntent();
        if (intent != null) {
            phoneNumber = intent.getStringExtra("phone_number");
        }

        List<MessageWrapper> baseList = new ArrayList<>();
        baseList.add(new MessageWrapper(new MessageModel(phoneNumber, "kjfehkfsw", new Date().getTime())));

        messagesListAdapter = new MessageListAdapter(this, baseList);
        conversationViewModel = new ViewModelProvider(
                this,
                new ConversationViewModelFactory(getApplication(), phoneNumber)
        ).get(ConversationViewModel.class);

        conversationViewModel.getMessages().observe(this, messageModels -> {
            // todo figure out how not to repeat messages
            for (MessageModel messageModel : messageModels) {
                addMessages(messageModel);
            }
        });

        RecyclerView messageRecyclerView = findViewById(R.id.recycler_message_list);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messagesListAdapter);

        EditText textChatBox = findViewById(R.id.edit_text_chatbox);
        Button sendButton = findViewById(R.id.button_chatbox_send);

        sendButton.setOnClickListener(view -> {
            sendMessage(new MessageModel(phoneNumber, textChatBox.getText().toString(), new Date().getTime()));
            Log.d("HFKFLSKDFKSJHKFJHDSKHSK", "hello clicked and I felt it!");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    private void sendMessage(MessageModel toSend) {
        SmsManager manager = SmsManager.getDefault();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            manager.sendTextMessage(phoneNumber, null, toSend.getMessageContent(), null, null);
            Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
            conversationViewModel.insertAll(toSend);
            addMessages(toSend);
        }
    }

    private void addMessages(MessageModel message) {
        messagesListAdapter.addItem(new MessageWrapper(message));
    }
}