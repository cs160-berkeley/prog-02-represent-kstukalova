package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> namesOut;
    private final List<Integer> pictureOut;
    private final List<String> partyOut;
    private final List<String> emailOut;
    private final List<String> websiteOut;
    private final List<String> tweetOut;

    public CustomList(Activity context,
                      List<String> namesOut, List<Integer> pictureOut, List<String> partyOut, List<String> emailOut,
                      List<String> websiteOut, List<String> tweetOut) {
        super(context, R.layout.list_single, namesOut);


        this.context = context;
        this.namesOut = namesOut;
        this.pictureOut = pictureOut;
        this.partyOut = partyOut;
        this.emailOut = emailOut;
        this.websiteOut = websiteOut;
        this.tweetOut = tweetOut;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView party = (TextView) rowView.findViewById(R.id.party);
        TextView email = (TextView) rowView.findViewById(R.id.email);
        TextView website = (TextView) rowView.findViewById(R.id.website);
        TextView tweet = (TextView) rowView.findViewById(R.id.tweet);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
//        Log.e("position", position + "");

        name.setText("Name: " + namesOut.get(position));
        party.setText("Party: " + partyOut.get(position));
        email.setText("Email: " + emailOut.get(position));
        website.setText("Website: " + websiteOut.get(position));
        tweet.setText("Latest Tweet: '" + tweetOut.get(position) + "'");

        imageView.setImageResource(pictureOut.get(position));

        Button more = (Button) rowView.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailView.class).putExtra("name", namesOut.get(position));
                v.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}