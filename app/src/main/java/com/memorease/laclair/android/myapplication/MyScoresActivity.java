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

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;
import com.memorease.laclair.android.myapplication.data.TopicsDbHelper;

import java.util.ArrayList;

public class MyScoresActivity extends AppCompatActivity {

    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scores);

        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);

        ListView listView = findViewById(R.id.myScoresListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(MyScoresActivity.this, TopicScoreActivity.class);
                i.putExtra("topic", String.valueOf(adapterView.getItemAtPosition(position)));
                startActivity(i);
            }
        });
    }

    public ArrayList<String> getTopicFromDB() {

        ArrayList<String> topics = new ArrayList<>();
        String query = "SELECT topic FROM "+ CardContract.CardEntry.TABLE_NAME_2;
        Cursor cursor = cardsDbHelper.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //If first value found @ cursor.getColumnIndex(etc) is equal to
                //any value in topics already, dont add. Else add.
                //if (topics.contains(cursor.getString(cursor.getColumnIndex("topic")))) {
                  //  break;
                //} else {
                    topics.add(cursor.getString(cursor.getColumnIndex("topic")));
                //}
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()){
            cursor.close();
        }
        return topics;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cardsDbHelper.close();
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
