package com.haowei.haowei.myriddle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by haowei on 8/25/15.
 */
public class RiddleListAdapter extends RecyclerView.Adapter<RiddleListAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ArrayList<String> list = new ArrayList<>();
    private String[] riddle_array;

    public RiddleListAdapter(Context context, RecyclerView recyclerView){
        mContext = context;
        mRecyclerView = recyclerView;
        Resources res = mContext.getResources();
        riddle_array = res.getStringArray(R.array.riddle_array);
        for(String riddle:riddle_array){
            /*list.add(Character.toString(c));*/
            list.add(riddle);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.riddle_list_item, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RiddleMain.class);
                int position = mRecyclerView.getChildAdapterPosition(v);
                intent.putExtra("riddle", list.get(position));
                intent.putExtra("riddle_id", position);
                mContext.startActivity(intent);
            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void setText(String s) { text.setText(s); }
        public String getText() { return text.getText().toString(); }
    }
}
