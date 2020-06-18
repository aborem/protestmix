package com.aborem.protestmixv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aborem.protestmixv1.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, MessageListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        ArrayList<Chat> listy = new ArrayList<Chat>();
        listy.add(new Chat("Buddypal", "1"));

        updateUIWithChats(listy);
    }

    protected void updateUIWithChats(List<Chat> chats) {
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter chatAdapter = new ChatAdapter(chats, this);
        chatRecyclerView.setAdapter(chatAdapter);
    }
}