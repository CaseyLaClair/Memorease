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
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.TopicsDbHelper;

import java.util.ArrayList;

public class AllTopicsActivity extends AppCompatActivity {

    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);

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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(AllTopicsActivity.this, SingleTopic.class);
                i.putExtra("topic", String.valueOf(adapterView.getItemAtPosition(position)));
                startActivity(i);
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
        

    }

    @Override
    protected void onDestroy() {

        topicsDbHelper.close();
        super.onDestroy();
    }
}
