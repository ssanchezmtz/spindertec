package com.purpleorchestra.spinder.spindertec.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RandomRequestReservation extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private TextView txtStatus;
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_request_reservation);

        txtStatus = (TextView) findViewById(R.id.txtStatus);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new SQLiteHandler(getApplicationContext());
        final String usid = db.getUserID();

        processReservation(usid, "1", PopFriendsOrRandom.selectScheduledTime);
    }



    private void processReservation(final String userID, final String sportID, final String selectedHour){
        // Tag used to cancel the request
        String tag_string_req = "load reservation";

        pDialog.setMessage("Looking for a reservation ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROCESSRESERVATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();


                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext(), "User successfully make something. Try login now!", Toast.LENGTH_LONG);
                        String errorMsg = jObj.getString("error_msg");
                        asignarMensaje(jObj.getString("result"));


                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();


                    } else {

                        String hourID = jObj.getString("hourID");
                        txtStatus.setText(hourID);
                        asignarMensaje(jObj.getString("result"));


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
                params.put("selectedHour", selectedHour);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void asignarMensaje(String x){
        String sResult="ALITZELMENDEZ";
        txtStatus.setText(sResult);

        switch (x){
            case "1":
                sResult = "Reservación exitosa";
                break;

            case "2":
                sResult = "Reservación fallida";

                break;

            case "3":
                sResult = "Actualmente tienes una reservación en ese horario";

                break;

            case "4":
                sResult = "No se encontró a un contrincante pero se seguirá buscando a un contrincante";

                break;

            case "5":
                sResult = "No se pudo realizar la reservación, intenta más tarde";

                break;

            case "6":
                sResult = "Todo mal";
                break;

            default:
                sResult = "Ni el switch! vaya!";

                break;
        }
        txtStatus.setText(sResult);
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