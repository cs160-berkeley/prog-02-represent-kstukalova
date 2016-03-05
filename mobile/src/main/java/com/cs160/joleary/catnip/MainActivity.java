package com.cs160.joleary.catnip;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.
    final Context context = this;
    private Button mFredButton;
    private Button mLexyButton;
    private Button mSearchButton; // button to press once user enters in zip-code
    private CheckBox mCurrentLocation;
    private EditText zipcode;
    public Boolean checked = false;
    public Boolean valid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mFredButton = (Button) findViewById(R.id.fred_btn);
//        mLexyButton = (Button) findViewById(R.id.lexy_btn);
        mSearchButton = (Button) findViewById(R.id.search_btn);
        mCurrentLocation = (CheckBox) findViewById(R.id.mCurrentLocation);
//        mFredButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("CAT_NAME", "Fred");
//                startService(sendIntent);
//            }
//        });
//
//        mLexyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("CAT_NAME", "Lexy");
//                startService(sendIntent);
//            }
//        });

        // borrowed from stackExchange
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff6666")));
        bar.setTitle(Html.fromHtml("<font color='#ffffff'>Represent! </font>"));

        // validates zip code
        zipcode = (EditText) findViewById(R.id.mZipCodeInput);
        zipcode.addTextChangedListener(new TextValidator(zipcode) {
            @Override
            public void validate(TextView textView, String text) {
                String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(zipcode.getText().toString());
                if (!matcher.matches() && zipcode.getText().toString().length() == 5)
                {
                    valid = false;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Invalid zip code");
                    alertDialogBuilder
                            .setMessage("Click OK to try another zip code")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if (zipcode.getText().toString().length() < 5) {
                    valid = false;
                }
                else {
                    valid = true;
                }
            }
        });

        // check if current location button is pressed
        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checked = !checked;
            }
        });
        // search button click
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (valid || checked) {
                    String zip;
                    if (checked) {
                        zip = "94301";
                    }
                    else {
                        zip = zipcode.getText().toString();
                    }
                    Intent intent = new Intent(v.getContext(), MobileResults.class).putExtra("zipcode", zip);
                    startActivity(intent);
                    Intent watchIntent = new Intent(v.getContext(), PhoneToWatchService.class);
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

                    v.getContext().startService(watchIntent);
                }
                else {
                    Toast toast = Toast.makeText(context, "Invalid input", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
