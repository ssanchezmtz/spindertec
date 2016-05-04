package com.purpleorchestra.spinder.spindertec.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.Templates.Friends;

import java.util.ArrayList;

/**
 * Created by alichel_6288 on 30/04/16.
 */
public class SearchPeopleAdapter extends BaseAdapter {
    private ArrayList<Friends> alPeopleByUser;
    private static LayoutInflater inflater=null;
    private Activity activity;


    public SearchPeopleAdapter(Activity c, ArrayList<Friends> alPeople){
        //mContext = c;
        activity = c;
        alPeopleByUser = alPeople;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return alPeopleByUser.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView==null){
            vi = inflater.inflate(R.layout.search_users_display, null);
            TextView peopleName = (TextView)vi.findViewById(R.id.txtPeopleName);
            TextView peopleEmail = (TextView)vi.findViewById(R.id.txtPeopleMail);

            String peopleFirstName = alPeopleByUser.get(i).friendsFirstName;
            String peopleLastName = alPeopleByUser.get(i).friendsLastName;
            String peopleConpleteName = peopleFirstName + " " + peopleLastName;


            peopleName.setText(peopleConpleteName);
            peopleEmail.setText(alPeopleByUser.get(i).friendsEmail);
        }
        return vi;
    }
}