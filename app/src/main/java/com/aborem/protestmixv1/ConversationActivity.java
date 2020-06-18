package com.aborem.protestmixv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ConversationActivity extends AppCompatActivity {

    public static void start(Context context, String chatId) {
        Intent starter = new Intent(context, ConversationActivity.class);
        starter.putExtra("chat_id", chatId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
    }
}