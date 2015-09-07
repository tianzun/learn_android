package com.haowei.haowei.myriddle;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by haowei on 8/30/15.
 */
public class RiddleDBTask extends AsyncTask<String, String, String> {

    Activity mActivity = null;

    public RiddleDBTask(Activity activity){
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        RiddleDBHelper dbHelper = RiddleDBHelper.getInstance(mActivity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return null;
    }
}
