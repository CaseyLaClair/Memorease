package com.memorease.laclair.android.myapplication.data;

import android.provider.BaseColumns;


public final class CardContract {

    private CardContract() {
    }

    public static abstract class CardEntry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "cards";
        public static final String TABLE_NAME_2 = "topics";
        public static final String TOPIC = "topic";
        public static final String QUESTION = "question";
        public static final String ANSWER = "answer";
        public static final String STUDY_DATE = "studydate";
        //public static final String LEARN_BY_DATE = "learnby";
        public static final String CORRECT_ANSWERED = "correctanswered";
        //public static final String DAYS_COUNTER = "dayscounter";
        //This is actually a boolean, but for purpose of putting
        //in a sqlite db, using 1 and 0 for true or false.
        public static final String STUDY_TODAY = "studytoday";
    }
}

