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

import java.lang.Math;
import java.util.Calendar;



public class UserCheckInActivity extends Activity {

    String orgQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_in);

        //get the organization name that was passed into the activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgQuery = extras.getString("query");
        }

        checkingOrgRange(orgQuery);
    }



    private void checkingOrgRange(String attendingOrg){
        //ARBITRARY RANGE ATM
        final double range = 3.0; //radius, for now in coordinate degrees

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
                    //Get organization id and gps coordinates
                    String orgId = org.getString("generalId");
                    double orgLat = org.getDouble("latitude");
                    double orgLong = org.getDouble("longitude");

                    //Calculating distance
                    double latComp = Math.pow(orgLat - userLat,2);
                    double longComp = Math.pow(orgLong - userLong,2);
                    double distance = Math.sqrt(latComp+longComp);

                    if(distance <= range){
                        Toast.makeText(getApplicationContext(),
                                "In range for " + orgId, Toast.LENGTH_LONG).show();
                        updateAttendance(orgId);
                    } else{
                        Toast.makeText(getApplicationContext(),
                                "Not in range for " + orgId + "\nChange location and try again.", Toast.LENGTH_LONG).show();
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
                //Get current timestamp
                Calendar c = Calendar.getInstance();
                String timestamp = c.toString();
                if(e == null) {
                    //increase attendance by one
                    int attendCount = userOrg.getInt("attendance");
                    userOrg.put("attendance", ++attendCount);
                    userOrg.put("lastCheckin", timestamp );
                    userOrg.saveInBackground();
                    Toast.makeText(getApplicationContext(),
                            "Attendance updated", Toast.LENGTH_LONG).show();
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
