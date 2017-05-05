package com.ultron.wintertrainingchatapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Synergy on 25-03-2017.
 */

public class MemberList extends ArrayAdapter<String> {
    private String[] ids;
    private String[] uname;
    private String[] ucompany;
    private String[] upicture;
    private Activity context;
    public MemberList(Activity context,String[] ids,String[] name,String[] company,String[] picture){
        super(context, R.layout.layout_member_list,picture);
        this.context = context;
        this.ids = ids;
        this.uname = name;
        this.ucompany = company;
        this.upicture = picture;
    }
    public View getView(int position , View ConvertView , ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_member_list , null , true);
        TextView name = (TextView) listViewItem.findViewById(R.id.user_name);
        TextView company = (TextView) listViewItem.findViewById(R.id.c_name);
        ImageView picture = (ImageView) listViewItem.findViewById(R.id.imageDownloaded);
        name.setText(uname[position]);
        company.setText(ucompany[position]);
        Picasso.with(getContext()).load("http://www.synergywebdesigners.com/synergywebdesigners.com/ashish/nimadesktop/admin/data/profile/xuser.png.pagespeed.ic.fYAVyOASUV.webp").transform(new CircleTransform()).into(picture);
        return listViewItem;
    }

}
