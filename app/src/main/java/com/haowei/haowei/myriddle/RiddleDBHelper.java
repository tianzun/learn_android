package com.haowei.haowei.myriddle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haowei on 8/30/15.
 */
public class RiddleDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "riddle.db";
    private static RiddleDBHelper instance = null;

    public static RiddleDBHelper getInstance(Context context) {
        if(instance == null) {
            instance = new RiddleDBHelper(context);
        }
        return instance;
    }

    private RiddleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RiddleDBContract.CREATE_RIDDLE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
