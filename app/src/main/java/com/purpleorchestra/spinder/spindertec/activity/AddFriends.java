package com.purpleorchestra.spinder.spindertec.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.purpleorchestra.spinder.spindertec.R;
import com.purpleorchestra.spinder.spindertec.Templates.Friends;
import com.purpleorchestra.spinder.spindertec.adapter.SearchPeopleAdapter;
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
public class AddFriends extends AppCompatActivity {

    private static final String TAG = AddFriends.class.getSimpleName();
    private Button btnSearch;
    private EditText inputName;
    private ListView listViewPeople;
    private ArrayList<Friends> alPeople;
    private SearchPeopleAdapter peopleAdapter;
    private SQLiteHandler db;
    private ProgressDialog pDialog;


    public static String ID_ACTUAL_FRIEND ="-1";
    public static String FRIEND_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Añadir amigos");
        setSupportActionBar(toolbar);

        inputName = (EditText) findViewById(R.id.ad_friend_name);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        alPeople = new ArrayList<Friends>();
        listViewPeople = (ListView) findViewById(R.id.listCoincidentsFriends);
        peopleAdapter = new SearchPeopleAdapter(this,alPeople);
        listViewPeople.setAdapter(peopleAdapter);



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        final String usid = db.getUserID();

        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name = inputName.getText().toString().trim();

                // Check for empty data in the form
                if (!name.isEmpty()) {
                    // login user

                    alPeople.clear();
                    listViewPeople.setAdapter(peopleAdapter);
                    findPeople(name);



                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the  name!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        listViewPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                ID_ACTUAL_FRIEND = alPeople.get(position).friendUserId;

                addNewFriend(ID_ACTUAL_FRIEND, usid);


            }
        });
//        loadReservations(usid);
        //loadReservations("3"); //Cargar las reservaciones de un usuario


    }

    private void findPeople(final String userName) {

        // Tag used to cancel the request
        String tag_string_req = "load people";

        pDialog.setMessage("Loading people ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETPEOPLE, new Response.Listener<String>() {

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
                            userID = user.getJSONObject(Integer.toString(i)).getString("id");
                            userFirstName = user.getJSONObject(Integer.toString(i)).getString("first_name");
                            userLastName = user.getJSONObject(Integer.toString(i)).getString("last_name");
                            userEmail = user.getJSONObject(Integer.toString(i)).getString("email");


                            alPeople.add(new Friends(userID,userFirstName,userLastName,userEmail));


                        }

                        listViewPeople.setAdapter(peopleAdapter);

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
                params.put("first_name", userName);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void addNewFriend(final String sUserID, final String fUserID) {

        // Tag used to cancel the request
        String tag_string_req = "load people";

        pDialog.setMessage("Adding friend ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADDFRIEND, new Response.Listener<String>() {

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


                        JSONObject message = jObj.getJSONObject("Message");

                        String serverMessage;

                        serverMessage = message.getJSONObject("1").getString("respuesta");

                        // Inserting row in users table
                        Toast.makeText(getApplicationContext(), serverMessage, Toast.LENGTH_LONG).show();


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
                params.put("fid", fUserID);
                params.put("sid", sUserID);

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
