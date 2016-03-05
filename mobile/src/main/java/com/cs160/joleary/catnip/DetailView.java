package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

public class DetailView extends Activity {
//    ListView list;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        TextView name = (TextView) findViewById(R.id.name);
        Intent intent = getIntent();
        String nameArg = intent.getStringExtra("name");
        name.append(nameArg);
        int idCurr = FakeData.instance.id.get(nameArg);
        int pictureCurr = FakeData.instance.picture.get(idCurr);
        String partyCurr = FakeData.instance.party.get(idCurr);
        String endDateCurr = FakeData.instance.endDate.get(idCurr);
        String committeeCurr = FakeData.instance.committee.get(idCurr);
        String billsCurr = FakeData.instance.bills.get(idCurr);

        TextView partyField = (TextView) findViewById(R.id.party);
        TextView dateField = (TextView) findViewById(R.id.end);
        TextView committeeField = (TextView) findViewById(R.id.committee);
        TextView billsField = (TextView) findViewById(R.id.bills);
        ImageView pictureField = (ImageView) findViewById(R.id.imageView);

        partyField.setText("Party: " + partyCurr);
        dateField.setText("End of Term: " + endDateCurr);
        committeeField.setText("Committees: " + committeeCurr);
        billsField.setText("Bills: " + billsCurr);
        pictureField.setImageResource(pictureCurr);
    }
}
