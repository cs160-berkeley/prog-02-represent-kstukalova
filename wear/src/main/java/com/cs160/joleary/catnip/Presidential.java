package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Presidential extends Activity {
    private Context context;
    private Double lat1;
    private Double lng1;

    TextView county_xml;
    TextView state_xml;
    TextView obama_xml;
    TextView romney_xml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential);
        Intent intent = getIntent();
        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        String zip = intent.getStringExtra("zipcode");

        county_xml = (TextView) findViewById(R.id.county);
        state_xml = (TextView) findViewById(R.id.state);
        obama_xml = (TextView) findViewById(R.id.obama);
        romney_xml = (TextView) findViewById(R.id.romney);

        Log.e("hello", zip);
        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                for (int k = 0; k < address.getMaxAddressLineIndex(); k++) {
                    Log.e("k", k + " " + address.getAddressLine(k));
                }
                // Use the address as needed
                lat1 = address.getLatitude();
                lng1 = address.getLongitude();
                String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                new RetrieveCounty().execute();
            }
            else {
                // Display appropriate message when Geocoder services are not available
            }
        } catch (IOException e) {
            // handle exception
        }
    }

    class RetrieveCounty extends AsyncTask<Void, Void, String> {
        private String TAG = "RetrieveCounty";
        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            try {
                String key = "AIzaSyCzKNunkd6K5D5I4rAYM7jgwP2B41hCYgc";
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat1+
                        ","+lng1+"&sensor=true_or_false");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            else {
                JSONObject jsonData = null;
                String county = "";
                try {
                    jsonData = new JSONObject(response);
                    JSONArray resultsArray = jsonData.getJSONArray("results");
                    JSONObject address_components_object = resultsArray.getJSONObject(0);
                    JSONArray address_components_list = address_components_object.getJSONArray("address_components");
                    for (int i = 0; i < address_components_list.length(); i++) {
                        JSONObject current_address_cluster = address_components_list.getJSONObject(i);
                        JSONArray list_of_types = current_address_cluster.getJSONArray("types");
                        for (int k = 0; k < list_of_types.length(); k++) {
                            if (list_of_types.get(k).equals("administrative_area_level_2")) {
                                county = current_address_cluster.getString("long_name");
                                county = county.substring(0, county.length() - 7);
                                try {
                                    JSONArray all_counties = new JSONArray(loadJSONFromAsset());
                                    for (int j = 0; j < all_counties.length(); j++) {
                                        JSONObject current_county = all_counties.getJSONObject(j);

                                        if (current_county.getString("county-name").equals(county)) {
                                            county_xml.append(county);
                                            state_xml.append(current_county.getString("state-postal"));
                                            obama_xml.append(current_county.getString("obama-percentage") + "%");
                                            romney_xml.append(current_county.getString("romney-percentage") + "%");
                                            break;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("election-county-2012.json");
            Log.e("what the hell: ", this.getAssets().getLocales()[0]);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            // Log.i("xxx", "accel:" + mAccel);
            if (mAccel >= 10) { // detect shake
                Log.i("xxx", "accel:" + mAccel);
                Intent watchIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                watchIntent.putExtra("random", true);
                getBaseContext().startService(watchIntent); // i think this is wrong
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


}