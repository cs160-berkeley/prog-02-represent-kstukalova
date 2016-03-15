package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DetailView extends Activity {
//    ListView list;
    Context context;
    String nameArg = "";
    String picture_url = "";
    String party = "";
    String term = "";
    String id = "";
    TextView partyField;
    TextView dateField;
    TextView committeeField;
    TextView billsField;
    ImageView pictureField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        TextView name = (TextView) findViewById(R.id.name);
        Intent intent = getIntent();
        nameArg = intent.getStringExtra("name");
        picture_url = intent.getStringExtra("picture_url");
        party = intent.getStringExtra("party");
        term = intent.getStringExtra("term");
        id = intent.getStringExtra("id");

        partyField = (TextView) findViewById(R.id.party);
        dateField = (TextView) findViewById(R.id.end);
        committeeField = (TextView) findViewById(R.id.committee);
        billsField = (TextView) findViewById(R.id.bills);
        pictureField = (ImageView) findViewById(R.id.imageView);

        name.append(nameArg);

        try {
            URL url = new URL(picture_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            pictureField.setImageBitmap(myBitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (party.equals("D")) {
            partyField.setText("Party: Democrat");
        }
        else if (party.equals("R")) {
            partyField.setText("Party: Republican");
        }
        else  {
            partyField.setText("Party: " + party);
        }
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try
        {
            date = form.parse(term);
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String month_string = new DateFormatSymbols().getMonths()[month];
        dateField.setText("End of Term: " + month_string + " " + day + ", " + year + "\n");

        new RetrieveCommittees().execute();
        new RetrieveBills().execute();
    }

    class RetrieveCommittees extends AsyncTask<Void, Void, String> {
        private String TAG = "RetrieveFeedTask";
        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://congress.api.sunlightfoundation.com/committees?member_ids="
                        + id + "&apikey=c54cf68b071040898a0b5165b29c2bf4");
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
                try {
                    jsonData = new JSONObject(response);
                    JSONArray resultsArray = jsonData.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject repr = resultsArray.getJSONObject(i);
                        committeeField.append(repr.getString("name") + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class RetrieveBills extends AsyncTask<Void, Void, String> {
        private String TAG = "RetrieveFeedTask";
        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://congress.api.sunlightfoundation.com/bills?sponsor_id="
                        + id + "&apikey=c54cf68b071040898a0b5165b29c2bf4");
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
                try {
                    jsonData = new JSONObject(response);
                    JSONArray resultsArray = jsonData.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length() && i < 10; i++) {
                        JSONObject repr = resultsArray.getJSONObject(i);
                        String date_unformatted = repr.getString("introduced_on");
                        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date date = null;
                        try
                        {
                            date = form.parse(term);
                        }
                        catch (ParseException e)
                        {

                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();

                        cal.setTime(date);
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        String month_string = new DateFormatSymbols().getMonths()[month];
                        String date_formatted = month_string + " " + day + ", " + year;
                        billsField.append(date_formatted + ": " + repr.getString("official_title") + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
