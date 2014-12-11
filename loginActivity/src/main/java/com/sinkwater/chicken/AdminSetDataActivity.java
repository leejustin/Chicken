package com.sinkwater.chicken;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import android.content.Context;

import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

/*
 * allows the user to set data on the Group and then passes the data with an intent to the map for GPS
 */
public class AdminSetDataActivity extends Activity {

    private Button nextButton;
    private EditText orgName;
    private EditText orgId;
    private EditText orgTime;

    private CheckBox sunday, monday, tuesday, wednesday, thursday, friday, saturday;

    private TimePicker tpStartTime, tpEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_data);

        //Get Android intent data if any will be needed later...
        //Intent intent = getIntent();

        //Sets up the editText fields
        orgName = (EditText)findViewById(R.id.orgName);
        orgId = (EditText)findViewById(R.id.orgId);
        orgTime = (EditText)findViewById(R.id.orgTime);

        //Sets up TimePickers
        tpStartTime = (TimePicker) findViewById(R.id.tpStartTime);
        tpEndTime = (TimePicker) findViewById(R.id.tpEndTime);

        addListenerOnChkDays();

        //Sets up the button and its methods
        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked();
            }
        });
    }

    public void addListenerOnChkDays() {
        sunday = (CheckBox) findViewById(R.id.sunday);
        monday = (CheckBox) findViewById(R.id.monday);
        tuesday = (CheckBox) findViewById(R.id.tuesday);
        wednesday = (CheckBox) findViewById(R.id.wednesday);
        thursday = (CheckBox) findViewById(R.id.thursday);
        friday = (CheckBox) findViewById(R.id.friday);
        saturday = (CheckBox) findViewById(R.id.saturday);

    }

    //If text fields aren't null, then we can pass the intent to the next activity
    private void onNextButtonClicked() {
        String orgNameText = orgName.getText().toString();
        String orgIdText = orgId.getText().toString();
        String orgTimeText = "";

        String startTimeText = null;
        String endTimeText = null;
        String timeText = null;

        String startTimeTextHour = null;
        String startTimeTextMinute = null;
        startTimeTextHour = tpStartTime.getCurrentHour().toString();
        if(tpStartTime.getCurrentHour() < 10)
            startTimeTextHour = "0"+startTimeTextHour;
        startTimeTextMinute = tpStartTime.getCurrentMinute().toString();
        if(tpStartTime.getCurrentMinute() < 10)
            startTimeTextMinute = "0" + startTimeTextMinute;
        startTimeText = startTimeTextHour + "." + startTimeTextMinute;

        String endTimeTextHour = null;
        String endTimeTextMinute = null;
        endTimeTextHour = tpEndTime.getCurrentHour().toString();
        if(tpEndTime.getCurrentHour() < 10)
            endTimeTextHour = "0" + endTimeTextHour;
        endTimeTextMinute = tpEndTime.getCurrentMinute().toString();
        if(tpEndTime.getCurrentMinute() < 10)
            endTimeTextMinute = "0" + endTimeTextMinute;
        endTimeText = endTimeTextHour + "." + endTimeTextMinute;

        timeText = startTimeText + "-" + endTimeText;

        if(sunday.isChecked()) {
            orgTimeText = orgTimeText + "SU" + timeText;
        }
        if(monday.isChecked()) {
            orgTimeText = orgTimeText + "MO" + timeText;
        }
        if(tuesday.isChecked()) {
            orgTimeText = orgTimeText + "TU" + timeText;
        }
        if(wednesday.isChecked()) {
            orgTimeText = orgTimeText + "WE" + timeText;
        }
        if(thursday.isChecked()) {
            orgTimeText = orgTimeText + "TH" + timeText;
        }
        if(friday.isChecked()) {
            orgTimeText = orgTimeText + "FR" + timeText;
        }
        if(saturday.isChecked()) {
            orgTimeText = orgTimeText + "SA" + timeText;
        }

        if (orgNameText.length() > 0 &&
                orgIdText.length() > 0 &&
                orgTimeText.length() > 0 &&
                (tpStartTime.getCurrentHour() < tpEndTime.getCurrentHour() ||
                        ((tpStartTime.getCurrentHour() == tpEndTime.getCurrentHour()) && tpStartTime.getCurrentMinute()<tpEndTime.getCurrentMinute()))) {
            // Loads up the next activity and also passes data on
            Intent loadGPS = new Intent(this, AdminMapActivity.class);

            loadGPS.putExtra("orgName", orgNameText);
            loadGPS.putExtra("orgId", orgIdText);
            loadGPS.putExtra("orgTime", orgTimeText);

            //Load up the Activity that has the GPS settings
            startActivity(loadGPS);
        }

        else {
            //Load up an error
            Context context = getApplicationContext();
            CharSequence text = "Error: All parameters need to be filled";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_set_data, menu);
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
