package com.purpleorchestra.spinder.spindertec.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.purpleorchestra.spinder.spindertec.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PopFriendsOrRandom extends Activity {

    private Button btnFriends;
    private Button btnRandom;
    private Spinner tmSchedule;
    private TextView txtCurrentDate;


    public static String selectScheduledTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_friends_or_random);

        btnFriends = (Button) findViewById(R.id.btnFriends);
        btnRandom = (Button) findViewById(R.id.btnRandom);
        tmSchedule = (Spinner) findViewById(R.id.scheduleTime);
        txtCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);

        //Show date
        Calendar cal = Calendar.getInstance();


        Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(currentDate.getTime());

        txtCurrentDate.setText(formattedDate);

        String[] valores = {"07:00","08:00","09:00","10:00","11:00","12:00", "13:00", "14:00","15:00","16:00",
                "17:00","18:00","19:00","20:00","21:00"};
        tmSchedule.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));


        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        tmSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                selectScheduledTime = (String) adapterView.getItemAtPosition(position)+":00";
                txtCurrentDate.setText(selectScheduledTime);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectScheduledTime = (String) parent.getItemAtPosition(1) +":00";
            }
        });


        // btn Return profile

        // Link to Register Screen
        btnRandom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RandomRequestReservation.class);
                startActivity(i);
                finish();
            }
        });
    }
}
