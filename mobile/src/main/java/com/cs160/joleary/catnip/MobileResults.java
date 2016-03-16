package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.fabric.sdk.android.Fabric;
import retrofit.http.GET;
import retrofit.http.Query;

public class MobileResults extends Activity {
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_EMAIL = "oc_email";
    private static final String TAG_PARTY = "party";
    private static final String TAG_TERM_END = "term_end";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TWITTER_ID = "twitter_id";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_UNIQUE_ID = "bioguide_id"; // use this to search for bills/committees on details page

    ListView list;
    Context context;
    String zip;

    ArrayList<String> last_name = new ArrayList<>();
    ArrayList<String> first_name = new ArrayList<>();
    ArrayList<String> full_name = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> term_end = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> twitter = new ArrayList<>();
    ArrayList<String> website = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> party = new ArrayList<>();
    ArrayList<String> image_url = new ArrayList<>();

    final ArrayList<String> final_tweets = new ArrayList<String>();
    ArrayList<Bitmap> images_bitmap = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_results);
        Intent intent = getIntent();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extras = intent.getExtras();
        zip = extras.getString("zip");
        if (zip == null) {
            zip = extras.getString("random");
        }
        TextView zipField = (TextView) findViewById(R.id.topInfoBar);
        zipField.append(zip);

        new RetrieveFeedTask().execute();
    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public UsersService getUsersService() {
            return getService(UsersService.class);
        }
    }

    interface UsersService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long userId,
                  @Query("screen_name") String screenName,
                  @Query("include_entities") Boolean includeEntities,
                          Callback<User> cb);
    }


    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private String TAG = "RetrieveFeedTask";
        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://congress.api.sunlightfoundation.com/legislators/locate?zip="
                        + zip + "&apikey=c54cf68b071040898a0b5165b29c2bf4");
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
            //Log.e(TAG, response);
            Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

            ArrayList<String> full_name_watch = new ArrayList<>(); // watch
            ArrayList<String> party_watch = new ArrayList<>(); // watch
            ArrayList<String> term_watch = new ArrayList<>();
            ArrayList<String> id_watch = new ArrayList<>();
            ArrayList<String> picture_url_watch = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(response);
                JSONArray resultsArray = jsonData.getJSONArray("results");
                int count_representatives = 0;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject repr = resultsArray.getJSONObject(i);

                    String current_id = repr.getString(TAG_UNIQUE_ID);
                    first_name.add(repr.getString(TAG_FIRST_NAME));
                    last_name.add(repr.getString(TAG_LAST_NAME));
                    email.add(repr.getString(TAG_EMAIL));
                    party.add(repr.getString(TAG_PARTY));
                    term_end.add(repr.getString(TAG_TERM_END));
                    title.add(repr.getString(TAG_TITLE));
                    twitter.add(repr.getString(TAG_TWITTER_ID));
                    website.add(repr.getString(TAG_WEBSITE));
                    id.add(repr.getString(TAG_UNIQUE_ID));
                    full_name.add(repr.getString(TAG_TITLE) + ". "
                            + repr.getString(TAG_FIRST_NAME) + " "
                            + repr.getString(TAG_LAST_NAME));

                    if (repr.getString(TAG_TITLE).equals("Rep")) {
                        count_representatives++;
                        if (count_representatives < 2) {
                            full_name_watch.add(full_name.get(full_name.size() - 1));
                            String current_picture_url = "https://theunitedstates.io/images/congress/225x275/";
                            current_picture_url = current_picture_url.concat(current_id + ".jpg");
                            Log.e("omg what is id: ", current_id);
                            picture_url_watch.add(current_picture_url);
                            Log.e("urls: ", current_picture_url);
                            party_watch.add(repr.getString(TAG_PARTY));
                            term_watch.add(repr.getString(TAG_TERM_END));
                            id_watch.add(repr.getString(TAG_UNIQUE_ID));
                        }
                    }
                    else {
                        full_name_watch.add(full_name.get(full_name.size() - 1));
                        String current_picture_url = "https://theunitedstates.io/images/congress/225x275/";
                        current_picture_url = current_picture_url.concat(current_id + ".jpg");
                        Log.e("omg what is id: ", current_id);
                        picture_url_watch.add(current_picture_url);
                        Log.e("urls: ", current_picture_url);
                        party_watch.add(repr.getString(TAG_PARTY));
                        term_watch.add(repr.getString(TAG_TERM_END));
                        id_watch.add(repr.getString(TAG_UNIQUE_ID));
                    }


                    Log.e("size: ", full_name_watch.size() + "");
                    if (full_name_watch.size() == 3) {
                        watchIntent.putExtra("zipcode", zip);
                        watchIntent.putStringArrayListExtra("name", full_name_watch);
                        watchIntent.putStringArrayListExtra("picture_url", picture_url_watch);
                        watchIntent.putStringArrayListExtra("party", party_watch);
                        watchIntent.putStringArrayListExtra("term", term_watch);
                        watchIntent.putStringArrayListExtra("id", id_watch);
                        getBaseContext().startService(watchIntent);
                    }

                    if (i == resultsArray.length() - 1) {
                        TwitterAuthConfig authConfig = new TwitterAuthConfig("9M7lJneL14xe9bXJmlZKo2CSR", "x9TDB9jNCGCV87heXJEDkOvf35qkEp4D6okVOYre6DaKNzAVqp");
                        Fabric.with(getBaseContext(), new Twitter(authConfig));
                        final String image_url_start = "https://theunitedstates.io/images/congress/225x275/";

                        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                            @Override
                            public void success(Result<AppSession> appSessionResult) {
                                AppSession session = appSessionResult.data;
                                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);

                                for (int i = 0; i < twitter.size(); i++) {
                                    String current_twitter_handle = twitter.get(i);
                                    twitterApiClient.getStatusesService().userTimeline(null, current_twitter_handle,
                                            1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                                                @Override
                                                public void success(Result<List<Tweet>> listResult) {
                                                    for(Tweet tweet: listResult.data) {
                                                        Tweet first = listResult.data.get(0);
                                                        String test = first.text;
                                                        final_tweets.add(test);
//
                                                        String current_image_url = image_url_start + id.get(final_tweets.size() - 1) + ".jpg";
                                                        image_url.add(current_image_url);
                                                        try {
                                                            URL url = new URL(current_image_url);
                                                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                                            connection.setDoInput(true);
                                                            connection.connect();
                                                            InputStream input = connection.getInputStream();
                                                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                                            images_bitmap.add(myBitmap);
                                                        } catch (MalformedURLException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    // filtrate the screen only when all tweets loaded
                                                    if (final_tweets.size() == twitter.size()) {
                                                        final CustomList adapter = new
                                                                CustomList(MobileResults.this, zip, full_name, images_bitmap, party, email, website, final_tweets, term_end, id, image_url);
                                                        list=(ListView)findViewById(R.id.list);
                                                        list.setAdapter(adapter);
                                                    }
                                                }
                                                @Override
                                                public void failure(TwitterException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                }
                            }
                            @Override
                            public void failure(TwitterException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
