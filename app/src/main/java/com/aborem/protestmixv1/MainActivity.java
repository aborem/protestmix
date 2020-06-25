package com.aborem.protestmixv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aborem.protestmixv1.activities.MessageListActivity;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.repositories.ContactRepository;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        Button enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactRepository rep = new ContactRepository(getApplication());
                rep.insert(new ContactModel("+18325609681"));
                transitionMessageListScreen();
            }
        });
    }

    private void transitionMessageListScreen() {
        MessageListActivity.start(this);
    }

}