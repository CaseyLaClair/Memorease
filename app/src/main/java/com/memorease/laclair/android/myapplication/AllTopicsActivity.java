package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AllTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);

        ListView list = findViewById(R.id.listViewAllTopics);
    }

    public void sendToCreateNewTopic(View view){
        Intent intent = new Intent(this, CreateNewTopic.class);
        startActivity(intent);
    }
}
