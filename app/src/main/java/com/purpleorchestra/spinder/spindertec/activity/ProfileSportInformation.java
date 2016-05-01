package com.purpleorchestra.spinder.spindertec.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.app.AppConfig;
import com.purpleorchestra.spinder.spindertec.app.AppController;
import com.purpleorchestra.spinder.spindertec.helper.SQLiteHandler;
import com.purpleorchestra.spinder.spindertec.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileSportInformation extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextView txtSportName;
    private TextView txtSportScore;
    private TextView txtSportLevel;
    private Button btnCrearPartido;
    private Button btnReturnHome;


    private SQLiteHandler db;
    private SessionManager session;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sport_information);

        txtSportName = (TextView) findViewById(R.id.detailSportName);
        txtSportScore = (TextView) findViewById(R.id.detailSportScore);
        txtSportLevel = (TextView) findViewById(R.id.detailSportLevel);
        btnCrearPartido = (Button) findViewById(R.id.btn_iniciarPartida);
        btnReturnHome = (Button) findViewById(R.id.btnReturnHome);




        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        txtSportName.setText(home.SPORT_NAME);
        loadSportDetails("3", home.ID_ACTUAL_SPORT);

        // btn Return profile
        btnReturnHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        home.class);
                startActivity(i);
                finish();
            }
        });

    }


    private void loadSportDetails(final String userID, final String sportID){

        // Tag used to cancel the request
        String tag_string_req = "load sports";

        pDialog.setMessage("Loading sports ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETPROFILESPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();


                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    txtSportLevel.setText("ALI");


                    if (!error) {


                        JSONObject sports = jObj.getJSONObject("profileSport");

                        String sportScore;
                        String sportLevel;

                        sportScore = sports.getString("score");
                        sportLevel = sports.getString("level");

                        txtSportScore.setText(sportScore);
                        txtSportLevel.setText(sportLevel);
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();


                    } else {

                        txtSportLevel.setText("MAL");

                        // Error occurred in registration. Get the error
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
                params.put("sportID", sportID);


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
