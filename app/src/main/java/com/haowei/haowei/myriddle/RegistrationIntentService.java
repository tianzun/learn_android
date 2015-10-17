package com.haowei.haowei.myriddle;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by haowei on 10/14/15.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    private static final String[] TOPICS = {"global"};

    private String projectNumber = null;

    /* Creates an IntentService.  Invoked by your subclass's constructor.*/
    public RegistrationIntentService() {
        super(TAG);
    }


    private String getInstanceIDToken() {

        projectNumber = loadProjectNumber();

        String iid = InstanceID.getInstance(this).getId();
        Log.i(TAG, "iid is:" + iid);

        String authorizedEntity = projectNumber; // Project id from Google Developers Console
        String scope = "GCM"; // e.g. communicating using GCM, but you can use any
        // URL-safe characters up to a maximum of 1000, or
        // you can also leave it blank.
        String token = null;
        try {
            token = InstanceID.getInstance(this).getToken(
                    authorizedEntity, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "token is:" + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = getInstanceIDToken();

        if (token != null) {
            try {
                Log.i(TAG, "Now start register token");
                subscribeTopics(token);
                Log.i(TAG, "registration success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        Log.i(TAG, "created a GcmPubSub, " + TOPICS.toString());
        for (String topic : TOPICS) {
            Log.i(TAG, "subscribing topic, topic is " + topic);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

    private String loadProjectNumber() {
        String projectNumber = null;
        try {
            InputStream is = getAssets().open("google.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            projectNumber = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.i(TAG, "projectNumber is:" + projectNumber);
        return projectNumber;
    }
}
