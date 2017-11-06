package com.memorease.laclair.android.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.memorease.laclair.android.myapplication.data.*;

import java.util.Calendar;

public class CreateCardActivity extends AppCompatActivity {

    private Button dateButton;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener datePickerListener;

    CardsDbHelper cardsDbHelper = new CardsDbHelper(this);
    TopicsDbHelper topicsDbHelper = new TopicsDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        SQLiteDatabase db2 = topicsDbHelper.getReadableDatabase();
        String[] projection = {
                CardContract.CardEntry.TOPIC,
                CardContract.CardEntry.SUB_TOPIC
        };

        showDialogOnClick();

    }

    public void showDialogOnClick() {

        dateButton = (Button) findViewById(R.id.datePicker);


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(CreateCardActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog, datePickerListener, year, month, day);
                dateDialog.show();
            }
        });

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year_x, int monthOfYear, int dayOfMonth) {
                year = year_x;
                month = monthOfYear + 1;
                day = dayOfMonth;

                String date = month + " / " + day + " / " + year;
                Toast.makeText(CreateCardActivity.this, date, Toast.LENGTH_LONG).show();
            }
        };
    }

    public void createCardOnDone(View view) {

        SQLiteDatabase db = cardsDbHelper.getWritableDatabase();
        SQLiteDatabase tDb = topicsDbHelper.getWritableDatabase();

        EditText acTopicText = (EditText)findViewById(R.id.autoCompleteTextViewTopics);
        String topic = acTopicText.getText().toString().trim();

        EditText acSubTopicText = (EditText)findViewById(R.id.autoCompleteTextViewSubTopics);
        String subTopic = acSubTopicText.getText().toString().trim();

        EditText questionText = (EditText)findViewById(R.id.questionCardText);
        String question = questionText.getText().toString().trim();

        EditText answerText = (EditText)findViewById(R.id.answerCardText);
        String answer = answerText.getText().toString().trim();

        Calendar current = Calendar.getInstance();
        int dateCreated = (int) current.getTimeInMillis();

        Calendar learnBy = Calendar.getInstance();
        learnBy.set(year,month,day);
        int learnByDate = (int) learnBy.getTimeInMillis();


        ContentValues cv = new ContentValues();
        cv.put(CardContract.CardEntry.TOPIC, topic);
        cv.put(CardContract.CardEntry.SUB_TOPIC, subTopic);
        cv.put(CardContract.CardEntry.QUESTION, question);
        cv.put(CardContract.CardEntry.ANSWER, answer);
        cv.put(CardContract.CardEntry.DATE_CREATED, dateCreated);
        cv.put(CardContract.CardEntry.LEARN_BY_DATE, learnByDate);
        cv.put(CardContract.CardEntry.CORRECT_ANSWERED, 0);
        cv.put(CardContract.CardEntry.DAYS_COUNTER, 0);
        cv.put(CardContract.CardEntry.STUDY_TODAY, 0);

        db.insert(CardContract.CardEntry.TABLE_NAME, null, cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(CardContract.CardEntry.TOPIC, topic);
        cv2.put(CardContract.CardEntry.SUB_TOPIC, subTopic);

        tDb.insert(CardContract.CardEntry.TABLE_NAME_2,null, cv2);

        acTopicText.getText().clear();
        acSubTopicText.getText().clear();
        questionText.getText().clear();
        answerText.getText().clear();

        Toast.makeText(CreateCardActivity.this, "Card Created", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        cardsDbHelper.close();
        topicsDbHelper.close();
        super.onDestroy();
    }
}

