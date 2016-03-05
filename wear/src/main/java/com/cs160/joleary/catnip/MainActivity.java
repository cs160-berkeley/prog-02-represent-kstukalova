package com.cs160.joleary.catnip;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private TextView mTextView;
    private Button mFeedBtn;
    private Button past;
    private String zipcode;
    private ArrayList<String> names;
    private ArrayList<String> parties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("RUNNING ONCREATE", "yes");
        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
//        mFeedBtn = (Button) findViewById(R.id.feed_btn);


//        past.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), Presidential.class);
//                sendIntent.putExtra("zipcode", zipcode);
//                startService(sendIntent);
//            }
//        });
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        names = new ArrayList<>();
        parties = new ArrayList<>();
        if (extras != null) {
            String wearData = extras.getString("wearData");
            try {
                JSONObject jsonData = new JSONObject(wearData);
                zipcode = jsonData.getString("zip");
                JSONArray reprs = jsonData.getJSONArray("repr");
                for (int i = 0; i < 3; i++) {
                    JSONObject repr = reprs.getJSONObject(i);
                    names.add(repr.getString("name"));
                    parties.add(repr.getString("party"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            zipcode = "94301";
            names.add("");
            parties.add("");
        }

        //Log.e("zipcod before adapter: ", "null" + zipcode);
            final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            pager.setAdapter(new RepresentativesAdapter(this, getFragmentManager(), zipcode, names, parties));

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
            Log.i("xxx", "accel:" + mAccel);
            if (mAccel >= 800) { // detect shake
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
