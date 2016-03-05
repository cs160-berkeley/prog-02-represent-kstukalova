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
    private ArrayList<String> names;
    private ArrayList<String> parties;

    /*
    private void createFakeData() {
        String[] temp1 = {"Senator Barbara Boxer", "Senator Dianne Feinstein", "Representative Anna G. Eshoo"};
        names.put("94301", temp1);


        String[] temp2 = {"Senator Barbara Boxer", "Senator Dianne Feinstein", "Representative Jackie Speier"};
        names.put("94403", temp2);

        String[] temp3 = {"Senator Kirsten E. Gillibrand", "Senator Charles E. Schumer",
                "Representative Kathleen M. Rice", "Representative Steve Israel"};
        names.put("11040", temp3);

        id.put("Senator Barbara Boxer", 0);
        id.put("Senator Dianne Feinstein", 1);
        id.put("Representative Anna G. Eshoo", 2);
        id.put("Representative Jackie Speier", 3);
        id.put("Senator Kirsten E. Gillibrand", 4);
        id.put("Senator Charles E. Schumer", 5);
        id.put("Representative Kathleen M. Rice", 6);
        id.put("Representative Steve Israel", 7);

        party.put(0, "Democrat");
        party.put(1, "Democrat");
        party.put(2, "Democrat");
        party.put(3, "Democrat");
        party.put(4, "Democrat");
        party.put(5, "Democrat");
        party.put(6, "Democrat");
        party.put(7, "Democrat");
        Log.e("zipcode fakedata: ", zipcode + "OH NO NOTHING");
        for (int k = 0; k < names.get(zipcode).length; k++) {
//            Log.e("k", k + "");
            String name = names.get(zipcode)[k];
            int idCurr = id.get(name);
            String partyCurr = party.get(idCurr);
//            Log.e("id", idCurr + "");
//            Log.e("party: ", partyCurr);
            PAGES.add(new Page(name, partyCurr));

        }
    }
    */

    RepresentativesAdapter(Context ctx, FragmentManager fm, String zipcode,
                           ArrayList<String> names, ArrayList<String> parties
    ) {
        super(fm);
        this.zipcode = zipcode;
        this.parties = parties;
        this.names = names;
//        Log.e("ZIPCODE: ", zipcode);
        mContext = ctx;
        //createFakeData();
//        Log.e("here", "here");
//        Log.e("people: ", PAGES.size() + "");
    }


    private static class Page {
        String name;
        String party;
        int cardGravity = Gravity.CENTER;
        boolean expansionEnabled = false;
        float expansionFactor = 2.0f;
        int expansionDirection = CardFragment.EXPAND_UP;

        Page(String name, String party) {
            this.name = name;
            this.party = party;
        }
    }

//    public static class ExampleFragment extends CardFragment {
//        @Override
//        public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // Inflate the layout for this fragment
//            return inflater.inflate(R.layout.fragment_mine, container, false);
//        }
//    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        //Page page = PAGES.get(col);
        String name = names.get(col);
        String party = parties.get(col);
//        Log.e("party", name);
        ExampleFragment fragment = new ExampleFragment();
        Bundle args = new Bundle();

        args.putCharSequence("name", name);
        args.putCharSequence("party", party);
        args.putCharSequence("zipcode", zipcode);
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
        return names.size();
    }
}