package com.memorease.laclair.android.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;

public class StudyCards extends AppCompatActivity {

    TextView topicTextView;
    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
    Cursor cursor;
    TextView qaTextView;
    String question;
    String answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_cards);

        topicTextView = findViewById(R.id.topicStudy);
        qaTextView = findViewById(R.id.textView2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topicPassed = extras.getString("topic");

            topicTextView.setText(topicPassed);
        }

        String topic = (String) topicTextView.getText();
        String query = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + " WHERE topic LIKE \"%" + topic + "%\";";
        cursor = cardsDbHelper.getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            question = cursor.getString(cursor.getColumnIndex("question"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            qaTextView.setText(question);
            //cursor.moveToNext();
        } else {
            qaTextView.setText("No Cards Available");
        }
    }

    public void flipCard(View view) {

        if (qaTextView.getText().equals(question)) {
            qaTextView.setText(answer);
        } else {
            qaTextView.setText(question);
        }

    }

    public void nextCard(View view) {

        //Do something with radio buttons here to increment correct answered
        //and open writeable version of db to update with increment... maybe
        //read current value, increment or decrement and then write new value
        //in it's place.
        if (cursor.moveToNext()) {
            question = cursor.getString(cursor.getColumnIndex("question"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            qaTextView.setText(question);
            //cursor.moveToNext();
        } else {
            cursor.moveToFirst();
            question = cursor.getString(cursor.getColumnIndex("question"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            qaTextView.setText(question);
            //cursor.moveToNext();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cardsDbHelper.close();
        cursor.close();
    }
}
