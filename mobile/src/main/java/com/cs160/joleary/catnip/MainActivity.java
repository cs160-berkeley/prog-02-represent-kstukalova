package com.cs160.joleary.catnip;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "9M7lJneL14xe9bXJmlZKo2CSR";
    private static final String TWITTER_SECRET = "x9TDB9jNCGCV87heXJEDkOvf35qkEp4D6okVOYre6DaKNzAVqp";


    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.
    //final Context context = this;
    private Button mFredButton;
    private Button mLexyButton;
    private Button mSearchButton; // button to press once user enters in zip-code
    private CheckBox mCurrentLocation;
    private EditText zipcode;
    public Boolean checked = false;
    public Boolean valid = false;
    private String zip;
    private GoogleApiClient mApiClient;
    private Location current;
    private LocationRequest locationRequest;
//    private static final String TAG_FIRST_NAME = "first_name";
//    private static final String TAG_LAST_NAME = "last_name";
//    private static final String TAG_EMAIL = "oc_email";
//    private static final String TAG_PARTY = "party";
//    private static final String TAG_TERM_END = "term_end";
//    private static final String TAG_TITLE = "title";
//    private static final String TAG_TWITTER_ID = "twitter_id";
//    private static final String TAG_WEBSITE = "website";
//    private static final String TAG_UNIQUE_ID = "bioguide_id"; // use this to search for bills/committees on details page


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        Log.e("i go here", "yes");
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addApi(AppIndex.API).build();
        mApiClient.connect();

        setContentView(R.layout.activity_main);

        mSearchButton = (Button) findViewById(R.id.search_btn);
        mCurrentLocation = (CheckBox) findViewById(R.id.mCurrentLocation);

        // borrowed from stackExchange
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff6666")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Represent! </font>"));


        // validates zip code
        zipcode = (EditText) findViewById(R.id.mZipCodeInput);
        zipcode.addTextChangedListener(new TextValidator(zipcode) {
            @Override
            public void validate(TextView textView, String text) {
                String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(zipcode.getText().toString());
                if (!matcher.matches() && zipcode.getText().toString().length() == 5) {
                    valid = false;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getBaseContext());
                    alertDialogBuilder.setTitle("Invalid zip code");
                    alertDialogBuilder
                            .setMessage("Click OK to try another zip code")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (zipcode.getText().toString().length() < 5) {
                    valid = false;
                } else {
                    valid = true;
                }
            }
        });

        // check if current location button is pressed
        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checked = !checked;
            }
        });
        // search button click
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (valid || checked) {
                    if (checked) {

                        //zip = "94301";
                        // use current for location
                        if (current == null) {
                            return;
                        }
                        else {
                            Geocoder currentAddress = new Geocoder(MainActivity.this);
                            List<Address> addressList;
                            try {
                                addressList = currentAddress.getFromLocation(current.getLatitude(), current.getLongitude(), 1);
                                if (!addressList.isEmpty()) {
                                    zip = addressList.get(0).getPostalCode();
                                    Log.e("zip: ", zip);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        zip = zipcode.getText().toString();
                    }

                    Intent intent = new Intent(getBaseContext(), MobileResults.class);
                    intent.putExtra("zip", zip);
                    startActivity(intent);
                    // Get all congressional information and pass to phone and inte (details?)


//                    ArrayList<String> names = new ArrayList<>();
//                    ArrayList<String> parties = new ArrayList<>();
//                    String[] namesArray = FakeData.instance.names.get(zip);

//                    for (int k = 0; k < namesArray.length; k++) {
//                        String currName = namesArray[k];
//                        names.add(currName);
//                        Integer currId = FakeData.instance.id.get(currName);
//                        parties.add(FakeData.instance.party.get(currId));
//                    }
//                    watchIntent.putStringArrayListExtra("names", names);
//                    watchIntent.putStringArrayListExtra("parties", parties);
//
//                    v.getContext().startService(watchIntent);
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), "Invalid input", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        current = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
//        Log.e("location: ", "long: " + longitude + " lat: " + latitude);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        int permissionCheck = ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
//
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mApiClient, locationRequest, this);
        Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //Log.e("location: ", "long: " + longitude + " lat: " + latitude);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.cs160.joleary.catnip/http/host/path")
        );
        AppIndex.AppIndexApi.start(mApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.cs160.joleary.catnip/http/host/path")
        );
        AppIndex.AppIndexApi.end(mApiClient, viewAction);
        mApiClient.disconnect();
    }

    public abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


