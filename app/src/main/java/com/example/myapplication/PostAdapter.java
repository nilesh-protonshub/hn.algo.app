package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postList;
    private String[] bgColors;

    public PostAdapter(Activity activity, List<Post> movieList) {
        this.activity = activity;
        this.postList = movieList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int location) {
        return postList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_title, null);

        TextView created_at = (TextView) convertView.findViewById(R.id.created_at);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        Switch selectUnselect = (Switch) convertView.findViewById(R.id.selectUnselect);

        created_at.setText(String.valueOf(postList.get(position).created_at));
        title.setText(postList.get(position).title);
        selectUnselect.setChecked(postList.get(position).selectUnselect);
        String color = bgColors[position % bgColors.length];
        created_at.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

}
