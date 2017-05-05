package com.ultron.wintertrainingchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Message extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private EditText txtmessage;
    private Button btnsend;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        setTitle(name);//set Title Of Navigation Bar
        txtmessage = (EditText) findViewById(R.id.txtmessage);
        btnsend = (Button) findViewById(R.id.buttonSend);
        btnsend.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        //View message
        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        getJSON();
                                    }
                                }
        );
    }
    @Override
    public void onRefresh() {
        getJSON();
    }
    @Override
    public void onClick(View v) {
        if (v == btnsend) {
            addMessage();
            txtmessage.setText("");
            getJSON();
        }

    }
    private void addMessage(){
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String company = i.getStringExtra("company");
        final String sname = new String("Neeraj Sharma");
        final String rname = new String(name);
        final String  ucompany = new String(company);
        final String date = new String(getDateTime());
        final String message = txtmessage.getText().toString().trim();
        class AddMessage extends AsyncTask<Void,Void,String> {

            // ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Toast.makeText(ChatActivity.this,"Your Message sending..",Toast.LENGTH_LONG).show();
                // loading = ProgressDialog.show(ChatActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                Toast.makeText(Message.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_SENDER,sname);
                params.put(Config.KEY_RECIVER,rname);
                params.put(Config.KEY_MESSAGE,message);
                params.put(Config.KEY_COMPANY,ucompany);
                params.put(Config.KEY_DAY,date);
                ReuestHandler rh = new ReuestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }
        AddMessage ae = new AddMessage();
        ae.execute();
    }
    ///showing message
    private void showMesage(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String message = jo.getString(Config.TAG_MESSAGE);
                String created = jo.getString(Config.TAG_CREATED);
                String sender = jo.getString(Config.TAG_SENDER);
                HashMap<String,String> employees = new HashMap<>();
                employees.put(Config.TAG_MESSAGE,message);
                employees.put(Config.TAG_CREATED,created);
                employees.put(Config.TAG_SENDER,sender);
                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(Message.this, list, R.layout.layout_message,
                new String[]{Config.TAG_MESSAGE,Config.TAG_CREATED,Config.TAG_SENDER},
                new int[]{R.id.message,R.id.created,R.id.user_name});
        listView.setAdapter(adapter);
    }
    private void getJSON(){
        swipeRefreshLayout.setRefreshing(true);
        class GetJSON extends AsyncTask<Void,Void,String>{

           // ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // Toast.makeText(Message.this,"Please Wait..",Toast.LENGTH_LONG).show();
               // loading = ProgressDialog.show(Message.this,"Showing Message","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // loading.dismiss();
                JSON_STRING = s;
                showMesage();
            }

            @Override
            protected String doInBackground(Void... params) {
                ReuestHandler rh = new ReuestHandler();
                String sname = new String("Neeraj Sharma");
                Intent i = getIntent();
                String name = i.getStringExtra("name");
                String reciver = (name.replaceAll(" ", "%20"));
                String sender = (sname.replaceAll(" ", "%20"));
                String s = rh.sendGetRequest(Config.URL_GET_ALL+sender+Config.URL_GET_DUMMY+reciver);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
        swipeRefreshLayout.setRefreshing(false);
    }
    private String getDateTime() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
                "dd MMM hh :mm aa", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
