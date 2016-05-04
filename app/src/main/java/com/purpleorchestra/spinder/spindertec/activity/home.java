package com.purpleorchestra.spinder.spindertec.activity;

/**
 * Created by Spinder on 24/04/16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.helper.SQLiteHandler;
import com.purpleorchestra.spinder.spindertec.helper.SessionManager;

import java.util.HashMap;

public class home extends Activity {
    private static final String TAG = home.class.getSimpleName();
    private TextView txtName;
    private TextView txtEmail;
//    private TextView txtAge;
//    private GridView gvSports;
    private Button btnLogout;
    private Button btnCheckReservations;
    private Button btnStartGame;
    private Button btnHistory;

    //sports
//    private SportsAdapter sportAdapter;
//    private ArrayList<Deportes> alDeportes;

    private SQLiteHandler db;
    private SessionManager session;

    private ProgressDialog pDialog;

    //SPORT STATIC INFORMATION
//    public static String ID_ACTUAL_SPORT ="-1";
//    public static String SPORT_NAME = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        txtName = (TextView) findViewById(R.id.home_username);
        txtEmail = (TextView) findViewById(R.id.home_email);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnHistory = (Button) findViewById(R.id.btn_History);
        btnStartGame = (Button) findViewById(R.id.btn_BeginPartida);
        //gvSports = (GridView) findViewById(R.id.home_GridSports);
        btnCheckReservations = (Button) findViewById(R.id.btn_CheckReservations);

//        alDeportes = new ArrayList<Deportes>();

  //     //TEMP
        Log.d(TAG, "HOLA");


        //Gridview
    //    sportAdapter = new SportsAdapter(this, alDeportes);
     //   gvSports.setAdapter(sportAdapter);


/*        gvSports.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                ID_ACTUAL_SPORT = alDeportes.get(position).id;
                SPORT_NAME = alDeportes.get(position).name;


                Intent i = new Intent(getApplicationContext(), ProfileSportInformation.class);
                startActivity(i);
                finish();
            }
        });*/


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());



        if (!session.isLoggedIn()) {
            logoutUser();
        }


        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String usid = user.get("sid");
        String first_name = user.get("first_name");
        String last_name = user.get("last_name");
        String name = first_name + " " + last_name;
        String email = user.get("email");
        //String age = "22"; //user.toString();

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        //txtAge.setText(age);

        //loadSports(usid);
        loadSports("3");
        //loadSports("3");

        btnStartGame.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(home.this, PopFriendsOrRandom.class);
                startActivity(i);
            }
        });

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnCheckReservations.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Reservations.class);
                startActivity(i);
                finish();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GamesHistorial.class);
                startActivity(i);
                finish();
            }
        });


    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(home.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

   /* private void loadSports(final String id){

        // Tag used to cancel the request
        String tag_string_req = "load sports";

        pDialog.setMessage("Loading sports ...");
        showDialog();


        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_GETSPORTSBYID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Sports: " + response.toString());
                hideDialog();


                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                       // String uid = jObj.getString("uid");
                        //txtName.setText("ALI");

                       JSONObject sports = jObj.getJSONObject("sports");
                        String sport_id;
                        String sport_name;

                        Log.d(TAG, "Get Sports size: " + sports.length());
                        hideDialog();


                        for(int i=1; i<sports.length(); i++){
                            sport_id = sports.getJSONObject(Integer.toString(i)).getString("id");
                            sport_name = sports.getJSONObject(Integer.toString(i)).getString("name");

                            alDeportes.add(new Deportes(sport_id, sport_name));
                        }

                        gvSports.setAdapter(sportAdapter);

                        // Inserting row in users table
                         Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }*/

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}