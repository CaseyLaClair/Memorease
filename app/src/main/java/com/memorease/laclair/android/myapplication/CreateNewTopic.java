package com.memorease.laclair.android.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private AutoCompleteTextView subTopicTextView;

    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_topic);

        topicTextView = findViewById(R.id.autoCompleteTopics);
        subTopicTextView = findViewById(R.id.autoCompleteSubTopic);

        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);
        topicTextView = findViewById(R.id.autoCompleteTopics);
        topicTextView.setAdapter(adapter);
    }

    public ArrayList<String> getTopicFromDB() {

        ArrayList<String> topics = new ArrayList<>();
        String query = "SELECT topic FROM topics";
        Cursor cursor = topicsDbHelper.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //If first value found @ cursor.getColumnIndex(etc) is equal to
                //any value in topics already, dont add. Else add.
                if (topics.contains(cursor.getString(cursor.getColumnIndex("topic")))) {
                    break;
                } else {
                    topics.add(cursor.getString(cursor.getColumnIndex("topic")));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return topics;
    }


    public void createWithCards(View view){

        //topicTextView = findViewById(R.id.autoCompleteTopics);
        //subTopicTextView = findViewById(R.id.autoCompleteSubTopic);

        String topic = topicTextView.getText().toString().trim();
        String subTopic = subTopicTextView.getText().toString().trim();

        //If topic and subtopic typed, match the topic and subtopic, ignoring case, display error
        if(checkExistance()){
            Toast.makeText(this, "Topic Already Exists", Toast.LENGTH_LONG).show();
        }
        else{
            createTopicInDb(topic, subTopic);

            //Send to create cards activity and pass in topic and subtopic
            Intent intent = new Intent(this, CreateCardActivity.class);
            intent.putExtra("topicName", topic);
            intent.putExtra("subTopicName", subTopic);
            startActivity(intent);
        }

    }

    public boolean checkExistance(){

        String topic = topicTextView.getText().toString().trim();
        String subTopic = subTopicTextView.getText().toString().trim();

        boolean flag = false;

        //Loop through each value and compare to db, create flag variable and set to false.
        String query = "SELECT topic, subtopic FROM topics";
        Cursor cursor = topicsDbHelper.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String sqlTopic = cursor.getString(cursor.getColumnIndex("topic"));
                String sqlSubTopic = cursor.getString(cursor.getColumnIndex("subtopic"));

                if(topic.equalsIgnoreCase(sqlTopic.trim())&&subTopic.equalsIgnoreCase(sqlSubTopic.trim())){
                    flag=true;
                }

            } while (cursor.moveToNext());

            cursor.close();
        }

        return flag;
    }

    public void createCardsLater(View view){

        String topic = topicTextView.getText().toString().trim();
        String subTopic = subTopicTextView.getText().toString().trim();

        if(checkExistance()){
            Toast.makeText(this, "Topic Already Exists", Toast.LENGTH_LONG).show();
        }
        else{
            createTopicInDb(topic, subTopic);
        }
    }

    public void createTopicInDb(String topic, String subTopic){

        SQLiteDatabase tDb = topicsDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CardContract.CardEntry.TOPIC, topic);
        cv.put(CardContract.CardEntry.SUB_TOPIC, subTopic);

        tDb.insert(CardContract.CardEntry.TABLE_NAME_2, null, cv);

        topicTextView.getText().clear();
        subTopicTextView.getText().clear();
    }

    @Override
    protected void onDestroy() {

        topicsDbHelper.close();
        super.onDestroy();
    }
}
