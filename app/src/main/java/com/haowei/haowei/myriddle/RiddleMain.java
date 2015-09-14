package com.haowei.haowei.myriddle;

import android.content.Intent;
import android.content.IntentSender;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.GregorianCalendar;


public class RiddleMain extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private Button mGetHintButton;
    private int riddle_id;
    private String riddle_string;
    private String riddle_answer;
    private TextView mCountDownText;
    private EditText mAnswerInput;
    private Button mSubmitAnswerButton;
    private int MILLI_SECONDS = 1000;
    private RiddleDAO riddleDAO;
    private RiddleItem riddleItem;
    private ShareActionProvider mShareActionProvider;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private static final String TAG = "RiddleMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle_main);

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar(); //getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        riddle_string = bundle.getString("riddle");
        riddle_id = bundle.getInt("riddle_id");
        riddle_answer = bundle.getString("riddle_answer");
        Toast.makeText(this, "riddle_id: "+riddle_id, Toast.LENGTH_LONG).show();
        TextView v = (TextView) findViewById(R.id.riddle_main_text_view);
        v.setText(riddle_string);
        riddleDAO = new RiddleDAO(RiddleMain.this);
        riddleItem = riddleDAO.getRiddleByRiddleId(riddle_id);

        mGetHintButton = (Button) findViewById(R.id.get_hint_button);
        mCountDownText = (TextView) findViewById(R.id.get_hint_text);
        mAnswerInput = (EditText) findViewById(R.id.answerInputText);
        mSubmitAnswerButton = (Button) findViewById(R.id.submit_answer);
        mSubmitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerInput = mAnswerInput.getText().toString();
                if(riddle_answer.equals(answerInput)){
                    Toast.makeText(RiddleMain.this,
                            "Correct: " + answerInput, Toast.LENGTH_LONG).show();
                    mAnswerInput.setText("");
                } else {
                    Toast.makeText(RiddleMain.this,
                            "Wrong: " + answerInput + ", correct: " + riddle_answer,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        mGetHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riddleDAO.requestHint(riddle_id);
                riddleItem = riddleDAO.getRiddleByRiddleId(riddle_id);

                setCountDown();
            }
        });

        setCountDown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_riddle_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }

    /** Defines a default (dummy) share intent to initialize the action provider.
     * However, as soon as the actual content to be used in the intent
     * is known or changes, you must update the share intent by again calling
     * mShareActionProvider.setShareIntent()
     */
    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Let's solve this riddle together:\n" + riddle_string);
        intent.setType("text/plain");
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // return super.onOptionsItemSelected(item);
        return false;
    }*/

    private void setCountDown() {
        mCountDownText.setText("");
        if (riddleItem == null)
            return;

        long count_down = countDownMillis();
        if(count_down > 0) {
            new CountDownTimer(count_down, 1 * MILLI_SECONDS) {

                public void onTick(long millisUntilFinished) {
                    MyDateTime delta = new MyDateTime(millisUntilFinished/MILLI_SECONDS);
                    String count_down_string = String.format(
                            "Time to Hint: %d days, %d:%d:%d",
                            delta.days, delta.hours, delta.minutes, delta.seconds
                    );
                    mCountDownText.setText(count_down_string);
                }

                public void onFinish() {
                    mCountDownText.setText("This is the Hint");
                    Toast.makeText(RiddleMain.this, "Count Down Finish", Toast.LENGTH_LONG).show();
                }
            }.start();
        } else if (count_down == 0){
            mCountDownText.setText("This is the Hint");
        } else {
            mCountDownText.setText("");
        }
    }

    private long countDownMillis(){
        if(riddleItem.isRequestHint()){
            long hint_show_at = riddleItem.getRequestHintAt().getTimeInMillis() + 1 * 86400 * 1000;
            long count_down = hint_show_at - GregorianCalendar.getInstance().getTimeInMillis();
            if(count_down > 0)
                return count_down;
            else
                return 0;
        } else
            return -1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        // Show the signed-in UI
        showSignedInUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        showSignedInUI();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            showSignedOutUI();
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult){
        Toast.makeText(this,
                "Connection Error" + connectionResult,
                Toast.LENGTH_LONG
        ).show();
    }

    private void showSignedOutUI(){
        Toast.makeText(this, "Signed Out, bye...", Toast.LENGTH_LONG).show();
    }

    private void showSignedInUI(){
        Toast.makeText(this, "Signed In, Congrats!", Toast.LENGTH_LONG).show();
    }

    public class MyDateTime {
        public int days;
        public int hours;
        public int minutes;
        public int seconds;

        public MyDateTime(long seconds) {
            days = (int)seconds / 86400;
            seconds -= days * 86400;
            hours = (int)seconds / 3600;
            seconds -= hours * 3600;
            minutes = (int)seconds / 60;
            seconds -= minutes * 60;
            this.seconds = (int) seconds;
        }
    }
}
