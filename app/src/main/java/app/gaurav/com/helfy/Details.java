package app.gaurav.com.helfy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arvindbedi.helfy.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Details extends AppCompatActivity {

    CircleImageView pic;

    TextView name, status, address, description, type, mobile, date;

    RelativeLayout addressBox, descriptionBox, typeBox, mobileBox;

    LinearLayout statusBox;

    Button accept, reject;

    String id, orgid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Donation Info");
        setSupportActionBar(toolbar);toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backpress_white_icon);

        pic = findViewById(R.id.image);
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        type = findViewById(R.id.type);
        mobile = findViewById(R.id.mobile);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        statusBox = findViewById(R.id.statusBox);

        statusBox.setVisibility(View.GONE);

        Picasso.with(this)
                .load("https://scontent-bom1-1.xx.fbcdn.net/v/t1.0-9/30743463_1678653912214695_4925833537208188928_o.jpg?_nc_cat=0&oh=fe2e628513cb27dd92d705e6bdd90cfd&oe=5C276BDC")
                .error(R.drawable.user_icon)
                .placeholder(R.drawable.user_icon)
                .into(pic);

        final Bundle bundle = getIntent().getExtras();

        name.setText(bundle.getString("name"));
        description.setText(bundle.getString("description"));
        status.setText(bundle.getString("status"));
        address.setText(bundle.getString("address"));
        type.setText(bundle.getString("type"));
        id = bundle.getString("id");
        orgid = bundle.getString("orgid");

        if (status.getText().equals("New")){
            statusBox.setVisibility(View.VISIBLE);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus("Accepted");
                status.setText("Accepted");
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus("Rejected");
                status.setText("Rejected");
            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Intent i = new Intent(Details.this, MainActivity.class);

        startActivity(i);

        finish();

    }

    private void updateStatus(String status) {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url;
        url = "http://www.freestuffs.co/helfy/updatestatus.php?id="+id+"&status="+status+"&orgid="+orgid;
        String goodurl = url.replaceAll(" ", "%20");
        StringRequest postRequest = new StringRequest(Request.Method.GET, goodurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                print("Status updated successfully");
                statusBox.setVisibility(View.GONE);

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","error"+error.toString());
                    }
                }

        );

        queue.add(postRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void print(String s) {
        Toast.makeText(Details.this, s, Toast.LENGTH_SHORT).show();
    }


}
