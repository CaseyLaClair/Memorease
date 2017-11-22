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

        /**
         * Put all of this in onResume too! Incase user never closes app
         * and comes back to it a day later!!!!!!!!!
         */
        boolean flag;
        Calendar current = Calendar.getInstance();
        daysCurrent = (current.getTimeInMillis() / (24 * 60 * 60 * 1000));

        SQLiteDatabase dbReader = cardsDbHelper.getReadableDatabase();
        SQLiteDatabase dbWriter = cardsDbHelper.getWritableDatabase();

        String query = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + ";";
        cursor = dbReader.rawQuery(query, null);

        if (cursor ==null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID));
                startDate = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.STUDY_DATE));
                correctAnswered = cursor.getInt(cursor.getColumnIndex("correctanswered"));

                SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date myDate = currentFormat.parse(startDate);
                    milliStart = myDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                daysStart = (milliStart / (24 * 60 * 60 * 1000));

                int days = (int) (daysCurrent - daysStart);
                flag = studyCheck(days, correctAnswered);

                if (flag == true) {
                    String dateCreated = currentFormat.format(current.getTime());
                    String updateDate = "UPDATE " + CardContract.CardEntry.TABLE_NAME + " SET " + CardContract.CardEntry.STUDY_DATE +
                            " = '" + dateCreated + "' WHERE " + CardContract.CardEntry._ID + " = " + id;
                    String updateStudy = "UPDATE " + CardContract.CardEntry.TABLE_NAME + " SET " + CardContract.CardEntry.STUDY_TODAY +
                            " = 1 WHERE " + CardContract.CardEntry._ID + " = " + id;

                    dbWriter.execSQL(updateDate);
                    dbWriter.execSQL(updateStudy);
                }

            } else if (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID));
                startDate = cursor.getString(cursor.getColumnIndex("studydate"));
                correctAnswered = cursor.getInt(cursor.getColumnIndex("correctanswered"));

                SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date myDate = currentFormat.parse(startDate);
                    milliStart = myDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                daysStart = (milliStart / (24 * 60 * 60 * 1000));

                int days = (int) (daysCurrent - daysStart);
                flag = studyCheck(days, correctAnswered);

                if (flag == true) {
                    String dateCreated = currentFormat.format(current.getTime());
                    String updateDate = "UPDATE " + CardContract.CardEntry.TABLE_NAME + " SET " + CardContract.CardEntry.STUDY_DATE +
                            " = '" + dateCreated + "' WHERE " + CardContract.CardEntry._ID + " = " + id;
                    String updateStudy = "UPDATE " + CardContract.CardEntry.TABLE_NAME + " SET " + CardContract.CardEntry.STUDY_TODAY +
                            " = 1 WHERE " + CardContract.CardEntry._ID + " = " + id;

                    dbWriter.execSQL(updateDate);
                    dbWriter.execSQL(updateStudy);
                }
            }
        }
        dbReader.close();
        dbWriter.close();
    }

    public boolean studyCheck(int days, int correct) {
        boolean value = false;

        if (correct == 0 && days != 0) {
            value = true;
        } else if (correct == 1 && days > 1) {
            value = true;
        } else if (correct == 2 && days > 4) {
            value = true;
        } else if (correct == 3 && days > 11) {
            value = true;
        } else if (correct == 4 && days % 27 == 0) {
            value = true;
        }

        return value;
    }

    // Sends user to CreateCardActivity.
    public void sendToCreateCardActivity(View view) {
        Intent intent = new Intent(this, CreateCardActivity.class);
        startActivity(intent);
    }

    // Sends user to AllTopicsActivity.
    public void sendToAllTopicsActivity(View view) {
        Intent intent = new Intent(this, AllTopicsActivity.class);
        startActivity(intent);
    }

    // Sends user to TodaysStudyActivity.
    public void sendToTodaysStudyActivity(View view) {
        Intent intent = new Intent(this, TodaysStudyActivity.class);
        startActivity(intent);
    }

    // Sends user to MyScoresActivity.
    public void sendToMyScoresActivity(View view) {
        Intent intent = new Intent(this, MyScoresActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cardsDbHelper.close();
        cursor.close();
    }
}

