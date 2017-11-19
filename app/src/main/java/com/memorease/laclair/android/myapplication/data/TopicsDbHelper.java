package com.memorease.laclair.android.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.memorease.laclair.android.myapplication.data.CardContract.*;

public class TopicsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Topics.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CardEntry.TABLE_NAME_2 + " (" +
                    CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CardEntry.TOPIC + " TEXT NOT NULL);";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME_2;

    public TopicsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
