package com.sinkwater.chicken;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinkwater.chicken.validtime_handler.ValidTimeHandler;

import java.lang.Math;
import java.util.Date;



public class UserCheckInActivity extends Activity {

    String orgQuery;
    private ValidTimeHandler timeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_in);

        //get the organization name that was passed into the activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgQuery = extras.getString("query");
        }
        timeHandler = new ValidTimeHandler();
        checkingOrgRange(orgQuery);
    }



    private void checkingOrgRange(String attendingOrg){
        //ARBITRARY RANGE ATM
        final double range = 0.5; //radius, for now in coordinate degrees

        //Get user gps coordinates
        GPSManager gps = new GPSManager(UserCheckInActivity.this);
        final double userLat = gps.getLatitude();
        final double userLong = gps.getLongitude();

        //Get organization information
        ParseQuery<ParseObject> orgQuery = ParseQuery.getQuery("Organization");
        orgQuery.whereEqualTo("generalId", attendingOrg);
        orgQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject org, ParseException e) {
                if(e == null){
                    //Get organization id
                    //Get gps coordinates
                    //Get time information
                    String orgId = org.getString("generalId");
                    double orgLat = org.getDouble("latitude");
                    double orgLong = org.getDouble("longitude");
                    String orgTime = org.getString("time");

                    //For testing purposes, if we forgot to put the time in the database then
                    //have the org active at all time
                    if(orgTime == null || orgTime.length() < 1)
                        orgTime = "SU00.00-23.59MO00.00-23.59TU00.00-23.59WE00.00-23.59TH00.00-23.59FR00.00-23.59SA00.00-23.59";

                    //Calculating distance
                    double latComp = Math.pow(orgLat - userLat,2);
                    double longComp = Math.pow(orgLong - userLong,2);
                    double distance = Math.sqrt(latComp+longComp);
                    //Check if it is a valid time to sign in
                    boolean validTime = timeHandler.checkValidTime(orgTime);

                    if(!validTime) {
                        Toast.makeText(getApplicationContext(),
                                "Not during the active time for " + orgId + "\nCome back during:\n" + orgTime, Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (distance <= range) {
                            Toast.makeText(getApplicationContext(),
                                    "In range for " + orgId, Toast.LENGTH_LONG).show();
                            updateAttendance(orgId);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Not in range for " + orgId + "\nChange location and try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                    Intent intent = new Intent(UserCheckInActivity.this, UserMenuActivity.class);
                    startActivity(intent);

                }else{
                    //error
                }
            }
        });
    }

    public void updateAttendance(String orgId)
    {
        //get current user's username
        ParseUser currentUser = ParseUser.getCurrentUser();
        String username = currentUser.getUsername();



        //find UserOrg object for this user and organization
        ParseQuery<ParseObject> userOrgQuery = ParseQuery.getQuery("UserOrg");
        userOrgQuery.whereEqualTo("username",username);
        userOrgQuery.whereEqualTo("generalId",orgId);
        userOrgQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject userOrg, ParseException e) {

                if(e == null) {
                    //Check timestamp
                    Date currentDate = new Date();
                    Date lastUpdated = new Date();
                    lastUpdated = userOrg.getUpdatedAt();
                    String currentDateString = currentDate.toString();
                    String lastUpdatedString = lastUpdated.toString();
                    currentDateString = currentDateString.substring(0,10);
                    lastUpdatedString = lastUpdatedString.substring(0,10);
                    boolean sameDate = currentDateString.equals(lastUpdatedString);

                    if( !sameDate || userOrg.getInt("attendance") == 0  )
                    {
                        //increase attendance by one
                        int attendCount = userOrg.getInt("attendance");
                        userOrg.put("attendance", ++attendCount);
                        userOrg.saveInBackground();
                        Toast.makeText(getApplicationContext(),
                                "Attendance updated", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "You have already checked in!", Toast.LENGTH_LONG).show();
                    }




                } else{
                    //error
                }
            }
        });
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_check_in, menu);
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
