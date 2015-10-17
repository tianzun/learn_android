package com.haowei.haowei.myriddle;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;

import java.io.IOException;

/**
 * Created by haowei on 10/5/15.
 */

public class GetUsernameTask extends AsyncTask<String, String, String> {
    Activity mActivity;
    String mScope;
    String mEmail;
    final String tag = "GetUerNameTask";
    public AsyncResponse delegate = null;

    GetUsernameTask(Activity activity, String name) {
        this.mActivity = activity;
        this.mScope = String.format("oauth2:%s", Scopes.PROFILE);
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            String token = fetchToken();
            if (token != null) {
                // **Insert the good stuff here.**
                // Use the token to access the user's Google data.
                return token;
            }
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
            Log.d(tag, "IOException");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        /*Toast.makeText(
                mActivity, "Fetched Token is " + result, Toast.LENGTH_SHORT
        ).show();*/
        Intent sendTokenIntent = new Intent(mActivity, RiddleMain.class);
        sendTokenIntent.putExtra("token", result);
        delegate.processFinish(result);
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException userRecoverableException) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            Log.d(tag, "user Recoverable Exception");
        } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
            Log.d(tag, "GoogleAuth Fatal Exception");
        }
        return null;
    }
}
