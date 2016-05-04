package com.purpleorchestra.spinder.spindertec.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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

        btnFriends.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        searchFriends.class);
                startActivity(i);
                finish();
            }
        });


    }
}
