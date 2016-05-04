package com.purpleorchestra.spinder.spindertec.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.Templates.Reservation;
import com.purpleorchestra.spinder.spindertec.adapter.HistorialAdapter;
import com.purpleorchestra.spinder.spindertec.app.AppConfig;
import com.purpleorchestra.spinder.spindertec.app.AppController;
import com.purpleorchestra.spinder.spindertec.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamesHistorial extends Activity {

    private static final String TAG = GamesHistorial.class.getSimpleName();


    private ListView listViewHistorial;
    private ArrayList<Reservation> alGames;

    //Adapter of views for ListView
    private HistorialAdapter historialAdapter;
    private SQLiteHandler db;
    private Button btnReturnHome;

    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        alGames = new ArrayList<Reservation>();

        listViewHistorial = (ListView) findViewById(R.id.listHistory);
        historialAdapter = new HistorialAdapter(this, alGames);
        listViewHistorial.setAdapter(historialAdapter);
        btnReturnHome = (Button) findViewById(R.id.btnReturnHome);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        String usid = db.getUserID();


        loadGames(usid);
        //loadReservations("3"); //Cargar las reservaciones de un usuario
        Log.d(TAG, "ALI");

        btnReturnHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        home.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void loadGames(final String userID) {

        // Tag used to cancel the request
        String tag_string_req = "load sports";

        pDialog.setMessage("Loading All Games ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETGAMESHISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get all games: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        JSONObject reservations = jObj.getJSONObject("reservations");


                        String reservationSport;
                        String reservationFacility;
                        String reservationScheduleDate;
                        String reservationScheduleTime;
                        String reservationOpponentFirstName;
                        String reservationOpponentLastName;
                        String reservationOopponent;

                        Log.d(TAG, "Get Reservation size: " + reservations.length());
                        hideDialog();


                        for (int i = 1; i < reservations.length(); i++) {
                            reservationSport = reservations.getJSONObject(Integer.toString(i)).getString("sp_sports.name");
                            reservationFacility = reservations.getJSONObject(Integer.toString(i)).getString("sp_facilities.name");
                            reservationScheduleDate = reservations.getJSONObject(Integer.toString(i)).getString("sp_schedules.date");
                            reservationScheduleTime = reservations.getJSONObject(Integer.toString(i)).getString("sp_schedules.time");
                            reservationOpponentFirstName = reservations.getJSONObject(Integer.toString(i)).getString("sp_user.first_name");
                            reservationOpponentLastName = reservations.getJSONObject(Integer.toString(i)).getString("sp_user.last_name");


                            reservationOopponent = reservationOpponentFirstName +" " +reservationOpponentLastName;

                            alGames.add(
                                    new Reservation(reservationSport,reservationFacility,reservationScheduleDate,reservationScheduleTime,
                                            reservationOopponent)
                            );

                        }

                        listViewHistorial.setAdapter(historialAdapter);

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
                params.put("userID", userID);

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
