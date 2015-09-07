package com.haowei.haowei.myriddle;

import android.provider.BaseColumns;

/**
 * Created by haowei on 8/30/15.
 */
public class RiddleDBContract {

    private RiddleDBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class RiddleStatus implements BaseColumns {
        public static final String TABLE_NAME = "riddle_status";
        public static final String COLUMN_NAME_ID = "rowid";
        public static final String COLUMN_NAME_RIDDLE_ID = "riddleId";
        public static final String COLUMN_NAME_SOLVED = "solved";
        public static final String COLUMN_NAME_REQUEST_HINT = "request_hint";
        public static final String COLUMN_NAME_REQUEST_HINT_AT = "request_hint_at";
        public static final String COLUMN_NAME_FRIENDS_INVITED = "friends_invited";
    }

    public static final String CREATE_RIDDLE_STATUS_TABLE = String.format(
            "CREATE TABLE %s (%s INTEGER, %s BOOLEAN, %s BOOLEAN, %s DATETIME, %s INTEGER)",
            RiddleStatus.TABLE_NAME,
            RiddleStatus.COLUMN_NAME_RIDDLE_ID,
            RiddleStatus.COLUMN_NAME_SOLVED,
            RiddleStatus.COLUMN_NAME_REQUEST_HINT,
            RiddleStatus.COLUMN_NAME_REQUEST_HINT_AT,
            RiddleStatus.COLUMN_NAME_FRIENDS_INVITED
    );
}
