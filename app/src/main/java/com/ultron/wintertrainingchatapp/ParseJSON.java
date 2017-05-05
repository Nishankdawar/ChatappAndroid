package com.ultron.wintertrainingchatapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Synergy on 25-03-2017.
 */

public class ParseJSON {
    public static String[] ids;
    public static String[] uname;
    public static String[] ucompany;
    public static String[] upicture;
    public static Bitmap[] bitmaps;
    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_Name = "uname";
    public static final String KEY_Company = "ucompany";
    public static final String KEY_Picture = "upicture";
    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(KEY_Picture));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }
    protected void parseJSON(){
        JSONObject jsonObject=null;

        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            ids = new String[users.length()];
            uname = new String[users.length()];
            bitmaps = new Bitmap[users.length()];
            upicture = new String[users.length()];
            ucompany = new String[users.length()];
            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                uname[i] = jo.getString(KEY_Name);
                // picture[i] = users.getJSONObject(i).getString(KEY_IMAGE);
                upicture[i] =jo.getString(KEY_Picture);
                ucompany[i] = jo.getString(KEY_Company);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
