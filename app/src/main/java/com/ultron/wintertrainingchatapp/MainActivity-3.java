package com.ultron.wintertrainingchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    public static final String JSON_URL = "http://synergywebdesigners.com/synergywebdesigners.com/ashish/nima/contact_list.php";
    private ListView listView;
    TextView username,ucompany;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView  = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageView = (ImageView) view.findViewById(R.id.imageDownloaded);
                username = (TextView) view.findViewById(R.id.user_name);
                ucompany = (TextView) view.findViewById(R.id.c_name);
                Intent i = new Intent(MainActivity.this ,Message.class);
                String name = username.getText().toString();
                String company = ucompany.getText().toString();
                i.putExtra("name",name);
                i.putExtra("company",company);
                startActivity(i);
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        sendRequest();
    }
    private void sendRequest() {
        StringRequest stringRequest = new StringRequest(JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showJSON(String json) {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();
        MemberList cl = new MemberList(MainActivity.this, ParseJSON.ids, ParseJSON.uname,ParseJSON.ucompany,ParseJSON.upicture);
        listView.setAdapter(cl);
    }

}
