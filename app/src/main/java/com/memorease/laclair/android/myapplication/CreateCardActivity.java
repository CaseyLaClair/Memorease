package com.memorease.laclair.android.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class creates an instance to be used for activity_create_card
 */
public class CreateCardActivity extends AppCompatActivity {

    //Declare all vars and open Dbs
    private AutoCompleteTextView topicTextView;
    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);
    SQLiteDatabase db;
    SQLiteDatabase tDb;
    Cursor cursor;

    /**
     * onCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        db = cardsDbHelper.getWritableDatabase();
        tDb = topicsDbHelper.getWritableDatabase();

        //Init textview
        topicTextView = findViewById(R.id.autoCompleteTextViewTopics);

        //Get extras if info is being passed from a separate activity.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topicPassed = extras.getString("topicName");

            topicTextView.setText(topicPassed);
        }

        //Fill array list of all topics to display as autocomplete text
        ArrayList<String> topics = new ArrayList<>(getTopicFromDB());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);
        topicTextView.setAdapter(adapter);
    }

    /**
     * This method gets all topic values from the Db and fills an array
     * @return ArrayList of topics
     */
    public ArrayList<String> getTopicFromDB() {

        ArrayList<String> topics = new ArrayList<>();
        String query = "SELECT topic FROM topics";
        cursor = topicsDbHelper.getReadableDatabase().rawQuery(query, null);

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
        }
        return topics;
    }

    public void createCardOnDone(View view) {

        EditText acTopicText = findViewById(R.id.autoCompleteTextViewTopics);
        String topic = acTopicText.getText().toString().trim();

        EditText questionText = findViewById(R.id.questionCardText);
        String question = questionText.getText().toString().trim();

        EditText answerText = findViewById(R.id.answerCardText);
        String answer = answerText.getText().toString().trim();

        Calendar current = Calendar.getInstance();
        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateCreated = currentFormat.format(current.getTime());

        ContentValues cv = new ContentValues();
        cv.put(CardContract.CardEntry.TOPIC, topic);
        cv.put(CardContract.CardEntry.QUESTION, question);
        cv.put(CardContract.CardEntry.ANSWER, answer);
        cv.put(CardContract.CardEntry.STUDY_DATE, dateCreated);
        cv.put(CardContract.CardEntry.CORRECT_ANSWERED, 0);
        cv.put(CardContract.CardEntry.STUDY_TODAY, 0);

        db.insert(CardContract.CardEntry.TABLE_NAME, null, cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(CardContract.CardEntry.TOPIC, topic);

        tDb.insert(CardContract.CardEntry.TABLE_NAME_2, null, cv2);

        questionText.getText().clear();
        answerText.getText().clear();

        Toast.makeText(CreateCardActivity.this, "Card Created", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!cursor.isClosed()){
            cursor.close();
        }
        if (db.isOpen()){
            db.close();
        }
        if (tDb.isOpen()){
            tDb.close();
        }
        cardsDbHelper.close();
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

