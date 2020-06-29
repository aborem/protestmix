package com.aborem.protestmixv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.aborem.protestmixv1.activities.MessageListActivity;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.models.MessageModel;
import com.aborem.protestmixv1.repositories.ContactRepository;
import com.aborem.protestmixv1.repositories.MessageRepository;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO change to authentication
        Button enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(view -> MessageListActivity.start(this));
    }
}