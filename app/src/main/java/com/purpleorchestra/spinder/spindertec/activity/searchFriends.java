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
import com.purpleorchestra.spinder.spindertec.Templates.Friends;
import com.purpleorchestra.spinder.spindertec.adapter.friendsAdapter;
import com.purpleorchestra.spinder.spindertec.app.AppConfig;
import com.purpleorchestra.spinder.spindertec.app.AppController;
import com.purpleorchestra.spinder.spindertec.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spinder on 03/05/16.
 */
public class searchFriends extends Activity {

    private static final String TAG = searchFriends.class.getSimpleName();
    private Button btnAdFriend;
    private ListView listViewFriends;
    private ArrayList<Friends> alFriends;
    private friendsAdapter myFriendsAdapter;
    private SQLiteHandler db;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

       alFriends = new ArrayList<Friends>();
        listViewFriends = (ListView) findViewById(R.id.listFriends);
        myFriendsAdapter = new friendsAdapter(this,alFriends);
        listViewFriends.setAdapter(myFriendsAdapter);

        btnAdFriend = (Button) findViewById(R.id.btnAdFriend);



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        final String usid = db.getUserID();

        findPeople(usid);



        btnAdFriend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        AddFriends.class);
                startActivity(i);
                finish();
            }
        });

//

//        loadReservations(usid);
        //loadReservations("3"); //Cargar las reservaciones de un usuario


    }

    private void findPeople(final String userID) {

        // Tag used to cancel the request
        String tag_string_req = "load people";

        pDialog.setMessage("Loading people ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETFRIENDS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get PEOPLE: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        JSONObject user = jObj.getJSONObject("user");


                        String userID;
                        String userFirstName;
                        String userLastName;
                        String userEmail;

                        Log.d(TAG, "Get users size: " + user.length());
                        hideDialog();
                        int size = user.length();

                        for (int i = 0; i < user.length()-1; i++) {
                            Log.d(TAG, i+"");
                            hideDialog();
                            userID = user.getJSONObject(Integer.toString(i)).getString("id");
                            userFirstName = user.getJSONObject(Integer.toString(i)).getString("first_name");
                            userLastName = user.getJSONObject(Integer.toString(i)).getString("last_name");
                            userEmail = user.getJSONObject(Integer.toString(i)).getString("email");

                            Log.d(TAG, "i = " + i + " " + size +" Adding: " + userID + " " + userFirstName + " " + userLastName + " " + userEmail);
                            hideDialog();

                            alFriends.add(new Friends(userID,userFirstName,userLastName,userEmail));


                            Log.d(TAG,  "i = " + i + " " + size + " Adding 2: " + alFriends.get(i).friendUserId + " " + alFriends.get(i).friendsFirstName  + " " + alFriends.get(i).friendsLastName + " " + alFriends.get(i).friendsEmail);
                            hideDialog();

                        }

                        listViewFriends.setAdapter(myFriendsAdapter);

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
                params.put("id", userID);

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
