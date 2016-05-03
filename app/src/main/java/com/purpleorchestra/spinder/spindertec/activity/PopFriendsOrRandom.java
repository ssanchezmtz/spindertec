package com.purpleorchestra.spinder.spindertec.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.purpleorchestra.spinder.spindertec.R;

public class PopFriendsOrRandom extends Activity {

    private Button btnFriends;
    private Button btnRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_friends_or_random);

        btnFriends = (Button) findViewById(R.id.btnFriends);
        btnRandom = (Button) findViewById(R.id.btnRandom);


        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));


    }
}
