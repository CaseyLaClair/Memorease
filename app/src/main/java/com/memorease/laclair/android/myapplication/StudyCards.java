package com.memorease.laclair.android.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.CardContract;
import com.memorease.laclair.android.myapplication.data.CardsDbHelper;

/**
 * This class manages the study cards activity
 */
public class StudyCards extends AppCompatActivity {

    TextView topicTextView;
    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
    SQLiteDatabase db;
    SQLiteDatabase cardWriter;
    Cursor cursor;
    TextView qaTextView;
    String question;
    String answer;
    RadioButton incorrect;
    RadioButton correct;
    CheckBox delete;
    int rightOrWrong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_cards);

        db = cardsDbHelper.getReadableDatabase();
        cardWriter = cardsDbHelper.getWritableDatabase();

        incorrect = findViewById(R.id.incorrectButton);
        correct = findViewById(R.id.correctButton);
        topicTextView = findViewById(R.id.topicStudy);
        qaTextView = findViewById(R.id.textView2);
        delete = findViewById(R.id.checkBox);

        //Pull in topic passed in from other activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String topicPassed = extras.getString("topic");

            topicTextView.setText(topicPassed);
        }

        String topic = (String) topicTextView.getText();
        String query = "SELECT * FROM " + CardContract.CardEntry.TABLE_NAME + " WHERE topic LIKE \"%" + topic + "%\";";
        cursor = db.rawQuery(query, null);

        //Get the first question and answer from the cursor
        //If none, display that none exist
        if (cursor.moveToFirst()) {
            question = cursor.getString(cursor.getColumnIndex("question"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            qaTextView.setText(question);
        } else {
            question = "No Cards Available";
            answer = "Nothing On This Side Either";
            qaTextView.setText(question);
        }
    }

    /**
     * This method flips the card to display the answer or question
     * depending on which is already showing
     *
     * @param view
     */
    public void flipCard(View view) {

        if (qaTextView.getText().equals(question)) {
            qaTextView.setText(answer);
        } else {
            qaTextView.setText(question);
        }

    }

    /**
     * This method pulls the next card to be studied
     * and also updates the previous card whether the user got it right or wrong.
     * @param view
     * @throws SQLException
     */
    public void nextCard(View view) throws SQLException {

        if (qaTextView.getText().equals("No Cards Available"))
            return;

        if (delete.isChecked()) {
            cardWriter.delete(CardContract.CardEntry.TABLE_NAME, "question=? and answer=?", new String[]{question, answer});
            delete.setChecked(false);
            checkMoveToNext();
        } else {
            rightOrWrong = cursor.getInt(cursor.getColumnIndex("correctanswered"));
            if (incorrect.isChecked() && rightOrWrong != 0) {
                rightOrWrong--;
            } else if (correct.isChecked() && rightOrWrong < 4) {
                rightOrWrong++;
            }

            ContentValues values = new ContentValues();
            values.put(CardContract.CardEntry.CORRECT_ANSWERED, rightOrWrong);
            cardWriter.update(CardContract.CardEntry.TABLE_NAME, values, CardContract.CardEntry.QUESTION + " = ?", new String[]{question});

            checkMoveToNext();
        }
    }

    /**
     * This method checks if there are other cards to study.
     * If not, it cycles back to the beginning
     */
    public void checkMoveToNext() {
        if (cursor.moveToNext()) {
            question = cursor.getString(cursor.getColumnIndex("question"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            qaTextView.setText(question);
            incorrect.setChecked(true);
        } else {
            onPause();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db.isOpen()) {
            db.close();
        }
        if (cardWriter.isOpen()) {
            cardWriter.close();
        }
        if (!cursor.isClosed()) {
            cursor.close();
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
