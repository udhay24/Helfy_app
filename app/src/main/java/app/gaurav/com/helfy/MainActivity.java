package app.gaurav.com.helfy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arvindbedi.helfy.Model.Order;
import com.example.arvindbedi.helfy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gaurav.com.helfy.OrdersAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

   OrdersAdapter adapter;

    public ArrayList<String> type = new ArrayList<String>();
    public ArrayList<String> name = new ArrayList<String>();
    public ArrayList<String> description = new ArrayList<String>();
    public ArrayList<String> date = new ArrayList<String>();
    public ArrayList<String> org = new ArrayList<String>();
    public ArrayList<String> status = new ArrayList<String>();
    public ArrayList<String> address = new ArrayList<String>();
    public ArrayList<String> id = new ArrayList<String>();
    public ArrayList<String> orgid = new ArrayList<String>();
    private ArrayList<Order> listContentArr= new ArrayList<>();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        request();

    }

    public void request() {
        progressDialog = ProgressDialog.show(this, "Loading", "Adding your event... Please wait", true, false);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        final String url;
        url = "http://freestuffs.co/helfy/fetchorders.php";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray jsonArray = null;

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i<jsonArray.length(); i++) {

                        JSONObject post = jsonArray.getJSONObject(i);

                        type.add(post.getString("type"));
                        name.add(post.getString("name"));
                        description.add(post.getString("description"));
                        date.add(post.getString("date"));
                        org.add(post.getString("org"));
                        status.add(post.getString("status"));
                        address.add(post.getString("address"));
                        id.add(post.getString("id"));
                        orgid.add(post.getString("orgid"));

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                recyclerView = findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapter = new OrdersAdapter(MainActivity.this);

                for (int iter = 1; iter <= jsonArray.length(); iter++) {

                    Order pojoObject = new Order();

                    pojoObject.setType(type.get(iter-1));
                    pojoObject.setName(name.get(iter-1));
                    pojoObject.setAddress(address.get(iter-1));
                    pojoObject.setDescription(description.get(iter-1));
                    pojoObject.setDate(date.get(iter-1));
                    pojoObject.setOrg(org.get(iter-1));
                    pojoObject.setStatus(status.get(iter-1));

                    listContentArr.add(pojoObject);
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                adapter.setListContent(listContentArr);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();

                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);

                            Intent go = new Intent(MainActivity.this, Details.class);
                            go.putExtra("id", id.get(position));
                            go.putExtra("orgid",orgid.get(position));
                            go.putExtra("name", listContentArr.get(position).getName());
                            go.putExtra("description", listContentArr.get(position).getDescription());
                            go.putExtra("date", listContentArr.get(position).getDate());
                            go.putExtra("address", listContentArr.get(position).getAddress());
                            go.putExtra("status", listContentArr.get(position).getStatus());
                            go.putExtra("org", listContentArr.get(position).getOrg());
                            go.putExtra("type", listContentArr.get(position).getType());
                            startActivity(go);
                            finish();
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    }
                });

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        print(error.toString());
                        progressDialog.dismiss();
                    }
                }

        );

        queue.add(postRequest);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.social_media_item:
                intent = new Intent(this , ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.statistics_item:
                intent = new Intent(this , StatisticsActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void print(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }


}
