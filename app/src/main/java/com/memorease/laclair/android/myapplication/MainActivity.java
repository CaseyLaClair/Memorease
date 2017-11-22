package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;
import com.memorease.laclair.android.myapplication.data.TopicsDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
    Cursor cursor;
    int correctAnswered;
    String startDate;
    long daysStart;
    long daysCurrent;
    long milliStart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar current = Calendar.getInstance();
        daysCurrent = (current.getTimeInMillis()/(24*60*60*1000));

        SQLiteDatabase dbReader = cardsDbHelper.getReadableDatabase();
        SQLiteDatabase dbWriter = cardsDbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + ";";
        cursor = dbReader.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            startDate = cursor.getString(cursor.getColumnIndex("studydate"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date myDate = format.parse(startDate);
                milliStart = myDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            daysStart = (milliStart/(24*60*60*1000));
            

        }




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

