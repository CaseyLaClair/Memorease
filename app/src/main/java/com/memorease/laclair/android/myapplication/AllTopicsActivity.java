package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.memorease.laclair.android.myapplication.data.TopicsDbHelper;

import java.util.ArrayList;

public class AllTopicsActivity extends AppCompatActivity {

    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);

    //Find a way to update database and reload other activities as soon as it happens.
    //Because there's an issue with changing activities and newly created items not showing up.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);

        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);

        ListView listView = findViewById(R.id.listViewAllTopics);
        listView.setAdapter(listAdapter);

        //Set click listener for items in listview.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Method write here for get item position on click and send to topic
                //activity with the topic chosen.
            }
        });
    }

    public void sendToCreateNewTopic(View view){
        Intent intent = new Intent(this, CreateNewTopic.class);
        startActivity(intent);
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

    protected void onResume(){

        super.onResume();
        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);

        ListView listView = findViewById(R.id.listViewAllTopics);
        listView.setAdapter(listAdapter);

        //Set click listener for items in listview.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Method write here for get item position on click and send to topic
                //activity with the topic chosen.
            }
        });

    }
}
