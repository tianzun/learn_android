package com.haowei.haowei.myriddle;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;


public class RiddleMain extends ActionBarActivity {

    private Button mGetHintButton;
    private int riddle_id;
    private String riddle_string;
    private TextView mCountDownText;
    private int MILLI_SECONDS = 1000;
    private RiddleDAO riddleDAO;
    private RiddleItem riddleItem;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle_main);

        ActionBar actionBar = getSupportActionBar(); //getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        riddle_string = bundle.getString("riddle");
        riddle_id = bundle.getInt("riddle_id");
        Toast.makeText(this, "riddle_id: "+riddle_id, Toast.LENGTH_LONG).show();
        TextView v = (TextView) findViewById(R.id.riddle_main_text_view);
        v.setText(riddle_string);
        riddleDAO = new RiddleDAO(RiddleMain.this);
        riddleItem = riddleDAO.getRiddleByRiddleId(riddle_id);

        mGetHintButton = (Button) findViewById(R.id.get_hint_button);
        mCountDownText = (TextView) findViewById(R.id.get_hint_text);
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
        // TODO Auto-generated method stub if(requestCode == 0) {
        // You will get callback here when email activity is exited
        // perform you task here
        Log.i("Receiving Activity", String.valueOf(resultCode));
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
