package com.cs160.joleary.catnip;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

//   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
private static final String TOAST = "/send_toast";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase("/name") ) {
            Intent intent = new Intent(getBaseContext(), DetailView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String wearData = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            try {
                JSONObject jsonData = new JSONObject(wearData);
                String id = jsonData.getString("id");
                String picture_url = jsonData.getString("picture_url");
                String term = jsonData.getString("term");
                String name = jsonData.getString("name");
                String party = jsonData.getString("party");

                intent.putExtra("id", id);
                intent.putExtra("picture_url", picture_url);
                intent.putExtra("term", term);
                intent.putExtra("name", name);
                intent.putExtra("party", party);

                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if( messageEvent.getPath().equalsIgnoreCase("/random") ) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, MobileResults.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("random", new String(messageEvent.getData(), StandardCharsets.UTF_8));
            startActivity(intent);
        }
        else {
            super.onMessageReceived( messageEvent );
        }

    }
}
