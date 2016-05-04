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
public class friendsAdapter extends BaseAdapter {
    private ArrayList<Friends> alFriendsByUser;
    private static LayoutInflater inflater=null;
    private Activity activity;


    public friendsAdapter(Activity c, ArrayList<Friends> alFriends){
        //mContext = c;
        activity = c;
        alFriendsByUser = alFriends;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return alFriendsByUser.size();
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
            vi = inflater.inflate(R.layout.friends_display, null);
            TextView friendsName = (TextView)vi.findViewById(R.id.txtFriendsName);
            TextView friendEmail = (TextView)vi.findViewById(R.id.txtFriendsMail);

            String myFirstName = alFriendsByUser.get(i).friendsFirstName;
            String lastName = alFriendsByUser.get(i).friendsLastName;
            String friendComplete = myFirstName + " " + lastName;



            friendsName.setText(friendComplete);
            friendEmail.setText(alFriendsByUser.get(i).friendsEmail);
        }
        return vi;
    }
}