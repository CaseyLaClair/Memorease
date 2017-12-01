package com.memorease.laclair.android.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;
import com.memorease.laclair.android.myapplication.data.TopicsDbHelper;

public class SingleTopic extends AppCompatActivity {

    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
    SQLiteDatabase cards;

    private TextView topicTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_topic);

        cards = cardsDbHelper.getWritableDatabase();

        topicTextView = findViewById(R.id.topicTop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topicPassed = extras.getString("topic");

            topicTextView.setText(topicPassed);
        }
    }

    public void takeToStudyCards(View view){
        Intent intent = new Intent(this, StudyCards.class);
        intent.putExtra("topic", topicTextView.getText());
        startActivity(intent);
    }

    public void takeToCreateCard(View view){
        Intent intent = new Intent(this, CreateCardActivity.class);
        intent.putExtra("topicName", topicTextView.getText());
        startActivity(intent);
    }

    public void deleteAllCards(View view){

        String topic = (String) topicTextView.getText();

        cards.execSQL("DELETE FROM "+CardContract.CardEntry.TABLE_NAME+" WHERE topic LIKE \"%"+topic+"%\";");
        cards.execSQL("DELETE FROM "+CardContract.CardEntry.TABLE_NAME_2+" WHERE topic LIKE \"%"+topic+"%\";");

        Intent intent = new Intent(this, AllTopicsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cards.isOpen()){
            cards.close();
        }
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
