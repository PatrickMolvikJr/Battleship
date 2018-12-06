package com.patrickmolvikjr.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    Button btnLogin;
    EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

    }

    public void LoginOnClick(View v){
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        String url = "http://battlegameserver.com/api/v1/login.json";
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                //Call backs
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("INTERNET", response);
                        userPrefs = gson.fromJson(response, UserPreferences.class);
                        toastIt("You are now logged in");

                        Intent intent = new Intent(getApplicationContext(), GameLobbyActivity.class);
                        startActivity(intent);
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

    }
}
