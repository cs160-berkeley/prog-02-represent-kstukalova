package com.cs160.joleary.catnip;

import java.util.HashMap;

/**
 * Created by kstukalova on 3/4/16.
 */
public class FakeData {
    public static final FakeData instance = new FakeData();
    String[] zipcodes;
    HashMap<String, String[]> names = new HashMap<String, String[]>();
    HashMap<String, Integer> id = new HashMap<String, Integer>();
    HashMap<Integer, Integer> picture = new HashMap<Integer, Integer>();
    HashMap<Integer, String> party = new HashMap<Integer, String>();
    HashMap<Integer, String> email = new HashMap<Integer, String>();
    HashMap<Integer, String> website = new HashMap<Integer, String>();
    HashMap<Integer, String> tweet = new HashMap<Integer, String>();
    HashMap<Integer, String> endDate = new HashMap<Integer, String>();
    HashMap<Integer, String> committee = new HashMap<Integer, String>();
    HashMap<Integer, String> bills = new HashMap<Integer, String>();

    public FakeData() {
        zipcodes = new String[] {"94301", "94403", "11040"};
        String[] temp1 = {"Senator Barbara Boxer", "Senator Dianne Feinstein", "Representative Anna G. Eshoo"};
        names.put("94301", temp1);


        String[] temp2 = {"Senator Barbara Boxer", "Senator Dianne Feinstein", "Representative Jackie Speier"};
        names.put("94403", temp2);

        String[] temp3 = {"Senator Kirsten E. Gillibrand", "Senator Charles E. Schumer", "Representative Kathleen M. Rice", "Representative Steve Israel"};
        names.put("11040", temp3);

        id.put("Senator Barbara Boxer", 0);
        id.put("Senator Dianne Feinstein", 1);
        id.put("Representative Anna G. Eshoo", 2);
        id.put("Representative Jackie Speier", 3);
        id.put("Senator Kirsten E. Gillibrand", 4);
        id.put("Senator Charles E. Schumer", 5);
        id.put("Representative Kathleen M. Rice", 6);
        id.put("Representative Steve Israel", 7);

        picture.put(0, R.drawable.zero);
        picture.put(1, R.drawable.one);
        picture.put(2, R.drawable.two);
        picture.put(3, R.drawable.three);
        picture.put(4, R.drawable.four);
        picture.put(5, R.drawable.five);
        picture.put(6, R.drawable.six);
        picture.put(7, R.drawable.seven);

        party.put(0, "Democrat");
        party.put(1, "Democrat");
        party.put(2, "Democrat");
        party.put(3, "Democrat");
        party.put(4, "Democrat");
        party.put(5, "Democrat");
        party.put(6, "Democrat");
        party.put(7, "Democrat");

        email.put(0, "Democrat");
        email.put(1, "Democrat");
        email.put(2, "Democrat");
        email.put(3, "Democrat");
        email.put(4, "Sen.Gillibrand@opencongress.org");
        email.put(5, "Sen.Schumer@opencongress.org");
        email.put(6, "Rep.Kathleenrice@opencongress.org");
        email.put(7, "Rep.Israel@opencongress.org");

        website.put(0, "Democrat");
        website.put(1, "Democrat");
        website.put(2, "Democrat");
        website.put(3, "Democrat");
        website.put(4, "http://www.gillibrand.senate.gov");
        website.put(5, "http://www.schumer.senate.gov");
        website.put(6, "NA");
        website.put(7, "http://israel.house.gov");

        tweet.put(0, "Democrat");
        tweet.put(1, "Democrat");
        tweet.put(2, "Democrat");
        tweet.put(3, "Democrat");
        tweet.put(4, "Instituting a pre-clearance customs process for trains coming from Canada is impt step twd increasing tourism to #NY");
        tweet.put(5, "Why not pass legislation putting Puerto Ricans on a level playing field, helps end their economic crisis & gives fair access to healthcare?");
        tweet.put(6, "CONGRATS @madelinesingas, next Nassau DA!!! We've accomplished a lot together - excited to see you keep making progress in the yrs ahead -kr");
        tweet.put(7, "Hosted a call with experts from @CDCgov to brief local medical professionals and community leaders on #Zika ");

        endDate.put(0, "Democrat");
        endDate.put(1, "Democrat");
        endDate.put(2, "Democrat");
        endDate.put(3, "Democrat");
        endDate.put(4, "2019");
        endDate.put(5, "2017");
        endDate.put(6, "2017");
        endDate.put(7, "2017");

        committee.put(0, "Democrat");
        committee.put(1, "Democrat");
        committee.put(2, "Democrat");
        committee.put(3, "Democrat");
        committee.put(4, "Agriculture, Nutrition and Forestry; Armed Services; Environment and Public Works; Aging");
        committee.put(5, "Rules and Administration; Library; Printing; Banking, Housing, and Urban Affairs; Finance; Judiciary");
        committee.put(6, "Homeland Security; Veterans' Affairs");
        committee.put(7, "Appropriations; Armed Services; Financial Services; Science");

        bills.put(0, "Democrat");
        bills.put(1, "Democrat");
        bills.put(2, "Democrat");
        bills.put(3, "Democrat");
        bills.put(4, "S.Res.379, 02/29/16");
        bills.put(5, "S.2614, 03/01/16");
        bills.put(6, "H.R.4484, 02/10/16");
        bills.put(7, "H.R.4608, 02/24/16");
    }
}
