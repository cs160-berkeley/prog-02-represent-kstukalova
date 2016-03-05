package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MobileResults extends Activity {
    ListView list;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_results);
        Intent intent = getIntent();
        String zip;
        Boolean random = intent.getBooleanExtra("random", false);
        if (random) {
            zip = FakeData.instance.zipcodes[(new Random().nextInt(FakeData.instance.zipcodes.length))];
            Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            watchIntent.putExtra("zipcode", zip);

            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> parties = new ArrayList<>();
            String[] namesArray = FakeData.instance.names.get(zip);

            for (int k = 0; k < namesArray.length; k++) {
                String currName = namesArray[k];
                names.add(currName);
                Integer currId = FakeData.instance.id.get(currName);
                parties.add(FakeData.instance.party.get(currId));
            }
            watchIntent.putStringArrayListExtra("names", names);
            watchIntent.putStringArrayListExtra("parties", parties);

            getBaseContext().startService(watchIntent);
        }
        else {
            zip = intent.getStringExtra("zipcode");
        }
        TextView zipField = (TextView) findViewById(R.id.topInfoBar);
        zipField.append(zip);
        Log.i("xxx", "zip:" + zip);

        // get correct things
        final List<String> namesOut = new ArrayList<String>();
        List<Integer> pictureOut = new ArrayList<Integer>();
        List<String>  partyOut  = new ArrayList<String>();
        List<String> emailOut = new ArrayList<String>();
        List<String> websiteOut = new ArrayList<String>();
        List<String> tweetOut = new ArrayList<String>();
        //Log.e("names: ", names.size() + "");
        String[] namesArray = FakeData.instance.names.get(zip);
        //Log.e("namesArray: ", namesArray.length + "");
        if (namesArray == null) {
            moveTaskToBack(true);
        }
        for (int k = 0; k < namesArray.length; k++) {
            String currName = namesArray[k];
            namesOut.add(currName);
            Integer currId = FakeData.instance.id.get(currName);
            pictureOut.add(FakeData.instance.picture.get(currId));
            partyOut.add(FakeData.instance.party.get(currId));
            emailOut.add(FakeData.instance.email.get(currId));
            websiteOut.add(FakeData.instance.website.get(currId));
            tweetOut.add(FakeData.instance.tweet.get(currId));
        }

//        Log.e("name: ", namesOut.size() + "");
//        Log.e("picture: ", pictureOut.size() + "");
//        Log.e("party: ", partyOut.size() + "");
//        Log.e("email: ", emailOut.size() + "");
//        Log.e("website: ", websiteOut.size() + "");
//        Log.e("tweet: ", tweetOut.size() + "");
//        final CustomList adapter = new
//                CustomList(MobileResults.this, namesOut, pictureOut, partyOut, emailOut, websiteOut, tweetOut);
        final CustomList adapter = new
                CustomList(MobileResults.this, namesOut, pictureOut, partyOut, emailOut, websiteOut, tweetOut);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view,
////                                    int position, long id) {
////                Intent intent = new Intent(view.getContext(), DetailView.class).putExtra("name", namesOut.get(position));
////                startActivity(intent);
////            }
//        });
    }

}
