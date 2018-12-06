package com.patrickmolvikjr.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameLobbyActivity extends BaseActivity {

    TextView txtUsername;
    ImageView imgAvatar;
    Button btnCallengeComputer;
    private ListView lstViewPlayers;
    Gson gson;

    static String[] playersList;


    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        lstViewPlayers = findViewById(R.id.lstViewPlayers);
        getPlayers();

        btnCallengeComputer = findViewById(R.id.btnCallengeComputer);
        txtUsername = findViewById(R.id.txtUsername);
        imgAvatar = findViewById(R.id.imgAvatar);

        txtUsername.setText(userPrefs.getAvatarName());
        Picasso.with(getApplicationContext()).load("http://www.battlegameserver.com/" + userPrefs.getAvatarImage()).into(imgAvatar);

    }

    public void challengeComputerOnClick(View v) {
        String url = BATTLE_SERVER_URL + "api/v1/challenge_computer.json";

        final JsonObjectRequest request = new JsonObjectRequest(
                url, null,
                //Call backs
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("INTERNET", response.toString());
                        try {
                            gameId = response.getInt("game_id");
                            Intent intent = new Intent(getApplicationContext(), BoardSetupActivity.class);
                            startActivity(intent);
                            toastIt("Game Created: " + gameId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            toastIt("Game Creation Failed");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("INTERNET", error.toString());
                        toastIt("Internet Failure" + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String credentials = username + ":" + password;
                Log.d("AUTH", "Login Info: " + credentials);
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        //Fixes Time out error for volley
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    public void getPlayers() {
        String url = "http://www.battlegameserver.com/api/v1/all_users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url,

                //Call Backs
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("INTERNET", response);
                        players = gson.fromJson(response, Player[].class);

                        adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.activity_listview, players);
                        lstViewPlayers.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Do Something With The Error
                        Log.d("INTERNET", error.toString());
                        toastIt("Internet Failure: " + error.toString());
                    }
                }

        );
        requestQueue.add(request);
    }
}
