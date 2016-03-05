package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class Presidential extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presidential);
        Intent intent = getIntent();
        String zip = intent.getStringExtra("zipcode");
        TextView county = (TextView) findViewById(R.id.county);
        TextView state = (TextView) findViewById(R.id.state);
        TextView obama = (TextView) findViewById(R.id.obama);
        TextView romney = (TextView) findViewById(R.id.romney);
        String setCounty;
        String setState;
        String setObama;
        String setRomney;

        if (zip == "94301") {
            setCounty = "Santa Clara";
            setState = "CA";
            setObama = "59%";
            setRomney = "40%";
        } else if (zip == "94403") {
            setCounty = "San Mateo";
            setState = "CA";
            setObama = "39%";
            setRomney = "50%";
        } else {
            setCounty = "New Hyde Park";
            setState = "New York";
            setObama = "70%";
            setRomney = "20%";
        }

        county.append(setCounty);
        state.append(setState);
        obama.append(setObama);
        romney.append(setRomney);
    }
}