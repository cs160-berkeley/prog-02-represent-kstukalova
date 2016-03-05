//package com.cs160.joleary.catnip;
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.support.wearable.view.CardFragment;
//import android.support.wearable.view.FragmentGridPagerAdapter;
//import android.support.wearable.view.GridPagerAdapter;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by kstukalova on 3/3/16.
// */
//public class MyGridAdapter extends GridPagerAdapter {
//    HashMap<String, String[]> names = new HashMap<String, String[]>();
//    HashMap<String, Integer> id = new HashMap<String, Integer>();
//    HashMap<Integer, String> party = new HashMap<Integer, String>();
//    private static Context mContext;
////    private final String zipcode;
//
//    private void createFakeData() {
//        String[] temp1 = {"Senator Barbara Boxer", "Senator Dianne Feinstein", "Representative Anna G. Eshoo"};
//        names.put("94301", temp1);
//
//
//        String[] temp2 = {"Senator Barbara Boxer", "Senator Dianne Feinstein", "Representative Jackie Speier"};
//        names.put("94403", temp2);
//
//        String[] temp3 = {"Senator Kirsten E. Gillibrand", "Senator Charles E. Schumer",
//                "Representative Kathleen M. Rice", "Representative Steve Israel"};
//        names.put("11040", temp3);
//
//        id.put("Senator Barbara Boxer", 0);
//        id.put("Senator Dianne Feinstein", 1);
//        id.put("Representative Anna G. Eshoo", 2);
//        id.put("Representative Jackie Speier", 3);
//        id.put("Senator Kirsten E. Gillibrand", 4);
//        id.put("Senator Charles E. Schumer", 5);
//        id.put("Representative Kathleen M. Rice", 6);
//        id.put("Representative Steve Israel", 7);
//
//        party.put(0, "Democrat");
//        party.put(1, "Democrat");
//        party.put(2, "Democrat");
//        party.put(3, "Democrat");
//        party.put(4, "Democrat");
//        party.put(5, "Democrat");
//        party.put(6, "Democrat");
//        party.put(7, "Democrat");
//    }
//
//    public MyGridAdapter (Context ctx, String zipcode) {
//        super(fm);
//
//    }
//
//    private void createPAGES() {
//        for (int k = 0; k < names.get(zipcode).length; k++) {
//            String name = names.get(zipcode)[k];
//            int idCurr = id.get(name);
//            String partyCurr = party.get(id);
//            PAGES.add(new Page(name, partyCurr));
//        }
//    }
//
//    private static class Page {
//        String flightNumber;
//        String flightDetails;
//        int cardGravity = Gravity.CENTER;
//        boolean expansionEnabled = false;
//        float expansionFactor = 1.0f;
//        int expansionDirection = CardFragment.EXPAND_DOWN;
//
//        public Page(String flightNum, String details) {
//            this.flightNumber = flightNum;
//            this.flightDetails = details;
//        }
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int row, int col) {
//        final View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main, container, false);
//        final TextView textView = (TextView) view.findViewById(R.id.textView);
//        textView.setText(String.format("Page:\n%1$s, %2$s", row, col));
//        container.addView(view);
//        return view;
//    }
//
//
//
////    @Override
////    public Fragment getFragment (int row, int col) {
////        Log.e("col: ", col + "");
////        Page page = PAGES.get(col);
////
////        String name = page.name;
////        String party = page.party;
////        CardFragment fragment = CardFragment.create(name, party);
////        // Advanced settings
////        fragment.setCardGravity(page.cardGravity);
////        fragment.setExpansionEnabled(page.expansionEnabled);
////        fragment.setExpansionDirection(page.expansionDirection);
////        fragment.setExpansionFactor(page.expansionFactor);
////        return fragment;
////    }
//
//    @Override
//    public int getRowCount() {
//        return 1;
//    }
//
//    @Override
//    public int getColumnCount(int rowNum) {
//        return PAGES.size();
//    }
//
//    @Override
//    protected void destroyItem(ViewGroup container, int row, int col, Object view) {
//        container.removeView((View)view);
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view==object;
//    }
//}