package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // Sends user to CreateCardActivity.
    public void sendToCreateCardActivity(View view){
        Intent intent = new Intent(this, CreateCardActivity.class);
        startActivity(intent);
    }

    // Sends user to AllTopicsActivity.
    public void sendToAllTopicsActivity(View view){
        Intent intent = new Intent(this, AllTopicsActivity.class);
        startActivity(intent);
    }

    // Sends user to TodaysStudyActivity.
    public void sendToTodaysStudyActivity(View view){
        Intent intent = new Intent(this, TodaysStudyActivity.class);
        startActivity(intent);
    }

    // Sends user to MyScoresActivity.
    public void sendToMyScoresActivity(View view){
        Intent intent = new Intent(this, MyScoresActivity.class);
        startActivity(intent);
    }
}

