package com.patrickmolvikjr.battleship;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class BoardSetupActivity extends BaseActivity {

    Spinner shipSpinner;
    ArrayAdapter shipSpinnerArrayAdapter;
    String[] shipsArray;
    static TreeMap<String, Integer> shipsMap = new TreeMap<String, Integer>();

    Spinner directionSpinner;
    ArrayAdapter directionSpinnerArrayAdapter;
    String[] directionsArray;
    static TreeMap<String, Integer> directionsMap = new TreeMap<String, Integer>();

    Spinner rowSpinner;
    Spinner columnSpinner;

    ImageView imgDefensiveGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_setup);

        shipSpinner = findViewById(R.id.spinnerShips);
        directionSpinner = findViewById(R.id.spinnerDirections);
        rowSpinner = findViewById(R.id.rowSpinner);
        columnSpinner = findViewById(R.id.columnSpinner);
        imgDefensiveGrid = findViewById(R.id.imgDefensiveGrid);

        imgDefensiveGrid.setOnTouchListener(new View.OnTouchListener(){
           @Override
            public boolean onTouch( View v, MotionEvent event){
               if(event.getAction() == MotionEvent.ACTION_UP){
                   Log.i("SHIP", "onTouch: x( " + event.getX()/BoardView.cellWidth + " ) y(" + event.getY()/BoardView.cellWidth + " )");
               }
               return true;
            }
        });

        GetAvailableShips();
        GetAvailableDirections();

    }

    public void addShipOnClick(View v){

         String ship = shipSpinner.getSelectedItem().toString();
         String direction = directionSpinner.getSelectedItem().toString();
         String row = rowSpinner.getSelectedItem().toString();
         String col = columnSpinner.getSelectedItem().toString();

         int directionNumber = directionsMap.get(direction);


         String url = BATTLE_SERVER_URL + "api/v1/game/" + gameId + "/add_ship/" + ship + "/" + row + "/" + col + "/" + directionNumber + ".json";
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                //Call backs
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("INTERNET", response);
                        toastIt(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("INTERNET", error.toString());
                        toastIt(error.toString());
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

    private void GetAvailableShips() {
        String url = BATTLE_SERVER_URL + "api/v1/available_ships.json";

        final JsonObjectRequest request = new JsonObjectRequest(
                url, null,
                //Call backs
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("INTERNET", response.toString());
                        Iterator iter = response.keys();
                        while (iter.hasNext()) {
                            String key = (String) iter.next();
                            try {

                                Integer value = (Integer) response.get(key);
                                shipsMap.put(key, value);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //shipsMap convert to array that spinner can use
                        int size = shipsMap.keySet().size(); //How many elements are in the hash
                        shipsArray = new String[size];
                        shipsArray = shipsMap.keySet().toArray(new String[]{});

                        shipSpinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, shipsArray);
                        shipSpinner.setAdapter(shipSpinnerArrayAdapter);
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

        requestQueue.add(request);

    }

    private void GetAvailableDirections() {
        String url = BATTLE_SERVER_URL + "api/v1/available_directions.json";

        final JsonObjectRequest request = new JsonObjectRequest(
                url, null,
                //Call backs
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("INTERNET", response.toString());
                        Iterator iter = response.keys();
                        while (iter.hasNext()) {
                            String key = (String) iter.next();
                            try {

                                Integer value = (Integer) response.get(key);
                                directionsMap.put(key, value);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        int size = directionsMap.keySet().size(); //How many elements are in the hash
                        directionsArray = new String[size];
                        directionsArray = directionsMap.keySet().toArray(new String[]{});

                        directionSpinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, directionsArray);
                        directionSpinner.setAdapter(directionSpinnerArrayAdapter);
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

        requestQueue.add(request);

    }
}
