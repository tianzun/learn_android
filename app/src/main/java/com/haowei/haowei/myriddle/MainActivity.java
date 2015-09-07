package com.haowei.haowei.myriddle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManger;
    private RiddleListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManger = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManger);

        mAdapter = new RiddleListAdapter(this, mRecycleView);
        mRecycleView.setAdapter(mAdapter);

        Log.i("OnCreate", "Creating...");
        RiddleDBTask a_task = new RiddleDBTask(this);
        a_task.execute("Initial");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
