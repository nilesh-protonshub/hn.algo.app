package com.example.myapplication;

import android.app.Activity;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = MainActivity.class.getSimpleName();

    private String URL_POST = "https://hn.algolia.com/api/v1/search_by_date?tags=story&page=";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private PostAdapter adapter;
    private List<Post> postList;
    private TextView totalSelectedPost;
    // initially page will be 1, later will be updated while parsing the json
    private int page = 0;

    //initially total Number of selected Post
    private int totalNoPost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalSelectedPost = (TextView) findViewById(R.id.totalSelectedPost);
        totalSelectedPost.setText("Total Selected Post :"+totalNoPost);
        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                // Get user selected item.

                Toast.makeText(MainActivity.this, "clickkkkk", Toast.LENGTH_SHORT).show();

                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                Post post = (Post)itemObject;

                // Get the checkbox.
                Switch selectUnselect = (Switch) view.findViewById(R.id.selectUnselect);

                // Reverse the checkbox and clicked item check state.
                if(selectUnselect.isChecked())
                {
                    totalNoPost = totalNoPost + 1;
                    post.selectUnselect = false;
                }else
                {
                    totalNoPost = totalNoPost - 1;
                    post.selectUnselect = true;
                }
                totalSelectedPost.setText("Total Selected Post :"+totalNoPost);
                adapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchPosts();
                                    }
                                }
        );

    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        fetchPosts();
    }
    /**
     * Fetching movies json by making http call
     */
    private void fetchPosts() {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        // appending offset to url
        page = page + 1;
        String url = URL_POST + page;

        // Volley's json array request object
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("hits");
                        }catch(Exception e)
                        {

                        }
                        if (jsonArray.length() > 0) {

                            // looping through json and adding to movies list
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject postObj = jsonArray.getJSONObject(i);

                                    String created_at = postObj.getString("created_at");
                                    String title = postObj.getString("title");

                                    Post m = new Post(created_at, title,false);

                                    postList.add(0, m);


                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyVolley.getInstance().addToRequestQueue(req);
    }

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