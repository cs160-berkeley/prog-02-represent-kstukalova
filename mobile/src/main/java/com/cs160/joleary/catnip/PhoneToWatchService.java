package com.cs160.joleary.catnip;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by joleary on 2/19/16.
 */
public class PhoneToWatchService extends Service {

    private static final String TAG = "PhoneToWatchService";

    private GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    private String createWearData(String zip, ArrayList<String> name, ArrayList<String> id,
                                  ArrayList<String> picture_url, ArrayList<String> party,
                                  ArrayList<String> term) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("zip", zip);
        JSONArray representatives = new JSONArray();
        for (int i = 0; i < name.size(); i++) {
            JSONObject repr = new JSONObject();

            repr.put("name", name.get(i));
            repr.put("id", id.get(i));
            repr.put("picture_url", picture_url.get(i));
            repr.put("party", party.get(i));
            repr.put("term", term.get(i));

            representatives.put(i, repr);
        }
        data.put("repr", representatives);

        return data.toString();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        // crashing here because intent is nothing
        Bundle extras = intent.getExtras();
        final String zip = extras.getString("zipcode");
        final ArrayList<String> name = extras.getStringArrayList("name");
        final ArrayList<String> id = extras.getStringArrayList("id");
        final ArrayList<String> picture_url = extras.getStringArrayList("picture_url");
        final ArrayList<String> party = extras.getStringArrayList("party");
        final ArrayList<String> term = extras.getStringArrayList("term");

        // Send the message with the cat name
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                mApiClient.connect();
                //now that you're connected, send a massage with the cat name
                try {
                    String wearData = createWearData(zip, name, id, picture_url, party,
                            term);
                    Log.i(TAG, wearData);
                    sendMessage("/data", wearData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
    }

}
