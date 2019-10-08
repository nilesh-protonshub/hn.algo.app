package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postList;
    private String[] bgColors;
    private CheckBoxListener checkBoxListener;

    public PostAdapter(Activity activity, List<Post> movieList,CheckBoxListener checkBoxListener) {
        this.activity = activity;
        this.postList = movieList;
        this.checkBoxListener = checkBoxListener;
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
    final  int pos = position;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_title, null);

        TextView created_at = (TextView) convertView.findViewById(R.id.created_at);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        Switch selectUnselect = (Switch) convertView.findViewById(R.id.selectUnselect);
        selectUnselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("TAG Test", "checked "+isChecked);
                postList.get(pos).selectUnselect = isChecked;
                if(isChecked)
                    checkBoxListener.addPost();
                else
                    checkBoxListener.removePost();
            }
        });
        created_at.setText(String.valueOf(postList.get(position).created_at));
        title.setText(postList.get(position).title);
        selectUnselect.setChecked(postList.get(position).selectUnselect);
        String color = bgColors[position % bgColors.length];
        created_at.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }
    public interface CheckBoxListener
    {
        void addPost();
        void removePost();
    }
}
