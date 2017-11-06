package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AllTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);
    }

    public void sendToCreateNewTopic(View view){
        Intent intent = new Intent(this, CreateNewTopic.class);
        startActivity(intent);
    }
}
