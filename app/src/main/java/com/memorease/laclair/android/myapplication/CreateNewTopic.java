package com.memorease.laclair.android.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.TopicsDbHelper;

import java.util.ArrayList;

public class CreateNewTopic extends AppCompatActivity {

    private AutoCompleteTextView topicTextView;

    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);
    SQLiteDatabase tDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_topic);

        tDb = topicsDbHelper.getWritableDatabase();

        topicTextView = findViewById(R.id.autoCompleteTopics);
    }

    public void createWithCards(View view) {

        String topic = topicTextView.getText().toString().trim();

        //If topic and subtopic typed, match the topic and subtopic, ignoring case, display error
        if (checkExistance()) {
            Toast.makeText(this, "Topic Already Exists", Toast.LENGTH_LONG).show();
        } else {
            createTopicInDb(topic);

            //Send to create cards activity and pass in topic and subtopic
            Intent intent = new Intent(this, CreateCardActivity.class);
            intent.putExtra("topicName", topic);
            startActivity(intent);
        }

    }

    public boolean checkExistance() {

        String topic = topicTextView.getText().toString().trim();

        boolean flag = false;

        //Loop through each value and compare to db, create flag variable and set to false.
        String query = "SELECT topic FROM topics";
        Cursor cursor = topicsDbHelper.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String sqlTopic = cursor.getString(cursor.getColumnIndex("topic"));

                if (topic.equalsIgnoreCase(sqlTopic.trim())) {
                    flag = true;
                }

            } while (cursor.moveToNext());
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return flag;
    }

    public void createCardsLater(View view) {

        String topic = topicTextView.getText().toString().trim();

        if (checkExistance()) {
            Toast.makeText(this, "Topic Already Exists", Toast.LENGTH_LONG).show();
        } else {
            createTopicInDb(topic);
        }
    }

    public void createTopicInDb(String topic) {

        ContentValues cv = new ContentValues();
        if (TextUtils.isEmpty(topic)) {
            Toast.makeText(this, "No Topic Entered", Toast.LENGTH_LONG).show();
        } else {
            cv.put(CardContract.CardEntry.TOPIC, topic);
            tDb.insert(CardContract.CardEntry.TABLE_NAME_2, null, cv);
        }

        topicTextView.getText().clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(tDb.isOpen()){
            tDb.close();
        }
        topicsDbHelper.close();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
