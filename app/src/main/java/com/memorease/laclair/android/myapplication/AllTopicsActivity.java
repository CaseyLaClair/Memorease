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

/**
 * This class creates a list of all topics for activity_all_topics.
 */
public class AllTopicsActivity extends AppCompatActivity {

    //Create a DbHelper from premade class.
    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);

    /**
     * onCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);


        //Create an array list of all topics
        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());
        ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, topics);

        //Adapt array into a listview
        ListView listView = findViewById(R.id.listViewAllTopics);
        listView.setAdapter(listAdapter);

        //Set click listener for items in listview.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //Open new activity for whatever topic chosen.
                Intent i = new Intent(AllTopicsActivity.this, SingleTopic.class);
                i.putExtra("topic", String.valueOf(adapterView.getItemAtPosition(position)));
                startActivity(i);
            }
        });
    }

    /**
     * sendToCreateNewTopic takes the user to a CreateNewTopic activity
     * @param view
     */
    public void sendToCreateNewTopic(View view){
        Intent intent = new Intent(this, CreateNewTopic.class);
        startActivity(intent);

    }

    /**
     * This method queries the topics db and pulls all topics into an
     * array list without duplicates.
     * @return ArrayList of topics
     */
    public ArrayList<String> getTopicFromDB() {

        //Declare and init needed variables
        ArrayList<String> topics = new ArrayList<>();
        String query = "SELECT topic FROM topics";
        Cursor cursor = topicsDbHelper.getReadableDatabase().rawQuery(query, null);

        //Cycle through the cursor to find topics and add to array list
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

            //Close the cursor being used.
            cursor.close();
        }

        return topics;
    }

    /**
     * onResume fetches new list of topics. There was an issue with topics
     * not updating after creation and returning to this activity. This is needed
     * to recreate the arraylist incase of updates.
     */
    protected void onResume(){

        super.onResume();
        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());

        ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, topics);

        ListView listView = findViewById(R.id.listViewAllTopics);
        listView.setAdapter(listAdapter);
        

    }

    /**
     * What to do when the app is closed.
     */
    @Override
    protected void onDestroy() {

        topicsDbHelper.close();
        super.onDestroy();
    }
}
