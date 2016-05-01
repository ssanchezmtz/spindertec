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
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.Templates.Deportes;
import com.purpleorchestra.spinder.spindertec.adapter.SportsAdapter;
import com.purpleorchestra.spinder.spindertec.app.AppConfig;
import com.purpleorchestra.spinder.spindertec.app.AppController;
import com.purpleorchestra.spinder.spindertec.helper.SQLiteHandler;
import com.purpleorchestra.spinder.spindertec.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class home extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtAge;
    private GridView gvSports;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private Button btnLogout;

    //sports
    private Deportes[] arrDepSports;
    private SportsAdapter sportAdapter;
    private ArrayList<Deportes> alDeportes;

    private SQLiteHandler db;
    private SessionManager session;

    private ProgressDialog pDialog;

    //SPORT STATIC INFORMATION
    public static String ID_ACTUAL_SPORT ="-1";
    public static String SPORT_NAME = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        txtName = (TextView) findViewById(R.id.username);
        txtEmail = (TextView) findViewById(R.id.email);
        txtAge = (TextView) findViewById(R.id.homAge);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        gvSports = (GridView) findViewById(R.id.home_GridSports);

        //loadSports("3");
        list = new ArrayList<String>();
        alDeportes = new ArrayList<Deportes>();

        //Gridview
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        sportAdapter = new SportsAdapter(this, alDeportes);
        gvSports.setAdapter(sportAdapter);
        //gvSports.setAdapter(adapter);



        gvSports.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                /*Toast.makeText(getApplicationContext(),
                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();*/

                /*Toast.makeText(getApplicationContext(),
                        alDeportes.get(position).id, Toast.LENGTH_SHORT).show();*/

                ID_ACTUAL_SPORT = alDeportes.get(position).id;
                SPORT_NAME = alDeportes.get(position).name;


                Intent i = new Intent(getApplicationContext(), ProfileSportInformation.class);
                startActivity(i);
                finish();
            }
        });


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());



        /* en caso de que no se hizo login volver
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        */

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String first_name = user.get("first_name");
        String last_name = user.get("last_name");
        String name = first_name + " " + last_name;
        String email = user.get("email");
        String age = user.toString();

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        txtAge.setText(age);

        loadSports("3");


        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
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

    private void loadSports(final String id){

        // Tag used to cancel the request
        String tag_string_req = "load sports";

        pDialog.setMessage("Loading sports ...");
        showDialog();


        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_GETSPORTSBYID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                //txtName.setText("HI");

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                       // String uid = jObj.getString("uid");
                        //txtName.setText("ALI");

                       JSONObject sports = jObj.getJSONObject("sports");
                        //JSONArray sports = jObj.getJSONArray("sports");
                        String sport_id;
                        String sport_name;
                        //txtName.setText(sports.length());


                        arrDepSports= new Deportes[sports.length()];
                       // namesSports = new String[sports.length()-1];

                        for(int i=1; i<sports.length(); i++){
                            sport_id = sports.getJSONObject(Integer.toString(i)).getString("id");
                            sport_name = sports.getJSONObject(Integer.toString(i)).getString("name");

                            list.add(i-1, sport_name);
                            arrDepSports[i-1] = new Deportes(sport_id, sport_name);
                            alDeportes.add(new Deportes(sport_id, sport_name));
                        }



                        txtName.setText(arrDepSports[1].name);

                        gvSports.setAdapter(sportAdapter);



                        // Inserting row in users table
                       // db.addUser(first_name, last_name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();


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

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}