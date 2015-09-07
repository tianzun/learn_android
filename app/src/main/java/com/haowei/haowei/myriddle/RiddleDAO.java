package com.haowei.haowei.myriddle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by haowei on 8/30/15.
 */
public class RiddleDAO {
    private Context context;
    private String[] proj_all = {
            RiddleDBContract.RiddleStatus.COLUMN_NAME_RIDDLE_ID,
            RiddleDBContract.RiddleStatus.COLUMN_NAME_SOLVED,
            RiddleDBContract.RiddleStatus.COLUMN_NAME_REQUEST_HINT,
            RiddleDBContract.RiddleStatus.COLUMN_NAME_REQUEST_HINT_AT,
            RiddleDBContract.RiddleStatus.COLUMN_NAME_FRIENDS_INVITED
    };
    private String table = RiddleDBContract.RiddleStatus.TABLE_NAME;
    private String selectId = RiddleDBContract.RiddleStatus.COLUMN_NAME_RIDDLE_ID + " = ?";
    private String[] selectIdArgs(int riddleId) {
        String[] strings = { Integer.toString(riddleId) };
        return strings;
    }

    public RiddleDAO(Context context) { this.context = context; }

    public void requestHint(int riddleId) {
        RiddleDBHelper dbHelper = RiddleDBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        RiddleItem riddleItem = getRiddleByRiddleId(riddleId);
        if(riddleItem == null){
            ContentValues values = objToContentVal(null, Boolean.TRUE, null);
            values.put(RiddleDBContract.RiddleStatus.COLUMN_NAME_RIDDLE_ID, riddleId);
            db.insert(table, null, values);
            Log.i("Database update", "riddleId: " + riddleId);
        }
    }

    public RiddleItem getRiddleByRiddleId(int riddleId) {
        RiddleDBHelper dbHelper = RiddleDBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(table, proj_all, selectId, selectIdArgs(riddleId), null, null, null, "1");
        if(c.getCount() > 0) {
            c.moveToFirst();
            int solved = c.getInt(c.getColumnIndex(
                    RiddleDBContract.RiddleStatus.COLUMN_NAME_SOLVED));
            int requestHint = c.getInt(c.getColumnIndex(
                    RiddleDBContract.RiddleStatus.COLUMN_NAME_REQUEST_HINT));
            Calendar date = new GregorianCalendar();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(
                    RiddleDBContract.RiddleStatus.COLUMN_NAME_REQUEST_HINT_AT)) * 1000);
            int invitedFriends = c.getInt(c.getColumnIndex(
                    RiddleDBContract.RiddleStatus.COLUMN_NAME_FRIENDS_INVITED));
            return new RiddleItem(riddleId, solved != 0, requestHint !=0, date, invitedFriends);
        } else
            return null;
    }

    public ContentValues objToContentVal(Boolean solved, Boolean requestHint, Integer invitedFriends){
        ContentValues values = new ContentValues();
        if(solved != null) {
            values.put(RiddleDBContract.RiddleStatus.COLUMN_NAME_SOLVED, solved);
        }
        if(requestHint != null) {
            values.put(RiddleDBContract.RiddleStatus.COLUMN_NAME_REQUEST_HINT, requestHint);
            values.put(RiddleDBContract.RiddleStatus.COLUMN_NAME_REQUEST_HINT_AT,
                    (Calendar.getInstance().getTimeInMillis()/1000));
        }
        if(invitedFriends != null) {
            values.put(RiddleDBContract.RiddleStatus.COLUMN_NAME_FRIENDS_INVITED, invitedFriends);
        }
        return values;
    }

    public void update(int riddleId, ContentValues value){

    }

}
