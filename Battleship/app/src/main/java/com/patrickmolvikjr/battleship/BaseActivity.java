package com.patrickmolvikjr.battleship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    final String BATTLE_SERVER_URL = "http://www.battlegameserver.com/";
    static String username, password;
    RequestQueue requestQueue;
    Gson gson;
    static UserPreferences userPrefs;
    static Player[] players;
    static int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        gson = gsonBuilder.create();

        //Volley Library
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue( cache, network );
        requestQueue.start();

    }

    public void toastIt(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.menuLogout:
                String url = "http://battlegameserver.com/api/v1/logout.json";
                StringRequest request = new StringRequest(
                        Request.Method.GET, url,
                        //Call backs
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("INTERNET", response);
                                toastIt("You are now logged out");

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("INTERNET", error.toString());
                                toastIt("Internet Failure" + error.toString());
                            }
                        }  ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = username + ":" + password;
                        Log.d("AUTH", "Login Info: " + credentials);
                        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

                requestQueue.add(request);


                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default :
                return super.onOptionsItemSelected(item);

        }
    }

}
