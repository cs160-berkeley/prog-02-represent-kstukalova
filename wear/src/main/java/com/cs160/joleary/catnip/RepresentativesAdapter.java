package com.cs160.joleary.catnip;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs160.joleary.catnip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepresentativesAdapter extends FragmentGridPagerAdapter {
    //HashMap<String, String[]> names = new HashMap<String, String[]>();
    //HashMap<String, Integer> id = new HashMap<String, Integer>();
    //HashMap<Integer, String> party = new HashMap<Integer, String>();
    private String zipcode;
    private final Context mContext;
    //private List mRows;
    //private final ArrayList<Page> PAGES = new ArrayList<Page>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> id = new ArrayList<>();
    private ArrayList<String> picture_url = new ArrayList<>();
    private ArrayList<String> term = new ArrayList<>();
    private ArrayList<String> party = new ArrayList<>();

    RepresentativesAdapter(Context ctx, FragmentManager fm, String zipcode,
                           ArrayList<String> name, ArrayList<String> id,
                           ArrayList<String> picture_url, ArrayList<String> term,
                           ArrayList<String> party
    ) {
        super(fm);
        this.zipcode = zipcode;
        this.name = name;
        this.id = id;
        this.picture_url = picture_url;
        this.term = term;
        this.party = party;

        mContext = ctx;
    }


    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        //Page page = PAGES.get(col);
        String current_name = name.get(col);
        String current_id = id.get(col);
        String current_picture_url = picture_url.get(col);
        String current_term = term.get(col);
        String current_party = party.get(col);
        String zip = zipcode;

        Log.e("party: ", current_party);
        if (current_party.equals("D")) {
            current_party = "Democrat";
        }
        else if (current_party.equals("R")) {
            current_party = "Republican";
        }

//        Log.e("party", name);
        ExampleFragment fragment = new ExampleFragment();
        Bundle args = new Bundle();

        args.putCharSequence("name", current_name);
        args.putCharSequence("id", current_id);
        args.putCharSequence("picture_url", current_picture_url);
        args.putCharSequence("term", current_term);
        args.putCharSequence("party", current_party);
        args.putCharSequence("zip", zipcode);
    ;
        fragment.setArguments(args);

        // Advanced settings (card gravity, card expansion/scrolling)
//        fragment.setCardGravity(Gravity.BOTTOM);
//        fragment.setExpansionEnabled(true);
//        fragment.setExpansionDirection(CardFragment.EXPAND_UP);
//        fragment.setExpansionFactor(page.expansionFactor);
        return fragment;
    }

    // Obtain the background image for the row
//    @Override
//    public Drawable getBackgroundForRow(int row) {
//        return mContext.getResources().getDrawable(
//                (BG_IMAGES[row % BG_IMAGES.length]), null);
//    }



    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return name.size();
    }
}