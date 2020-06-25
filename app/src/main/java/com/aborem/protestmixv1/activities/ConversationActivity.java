package com.aborem.protestmixv1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.aborem.protestmixv1.Constants;
import com.aborem.protestmixv1.R;
import com.aborem.protestmixv1.models.MessageWrapper;
import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.view_models.ConversationViewModel;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class ConversationActivity extends AppCompatActivity {
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private String phoneNumber;
    private MessagesListAdapter<MessageWrapper> messagesListAdapter;
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

        conversationViewModel = new ViewModelProvider(this).get(ConversationViewModel.class);
        conversationViewModel.getMessages().observe(this, messageModels -> {
            // todo figure out how not to repeat messages
            messagesListAdapter.clear();
            for (MessageModel messageModel : messageModels) {
                addMessages(messageModel);
            }
        });

        initViews();
    }

    private void initViews() {
        MessageInput inputView = findViewById(R.id.input);
        MessagesList messagesList = findViewById(R.id.messagesList);
        inputView.setInputListener(input -> {
            sendMessage(new MessageModel(phoneNumber, input.toString(), (int) new Date().getTime()));
            return true;
        });
        ImageLoader imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
        messagesListAdapter = new MessagesListAdapter<>(Constants.authorId, imageLoader);
        messagesList.setAdapter(messagesListAdapter);
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
            addMessages(toSend);
        }
    }

    private void getPreviousMessages() {

    }

    private void addMessages(MessageModel message) {
        messagesListAdapter.addToStart(new MessageWrapper(message), true);
    }
}