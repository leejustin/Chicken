package com.sinkwater.chicken;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import android.content.Context;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_data);

        //Get Android intent data if any will be needed later...
        //Intent intent = getIntent();

        //Sets up the editText fields
        orgName = (EditText)findViewById(R.id.orgName);
        orgId = (EditText)findViewById(R.id.orgId);

        //Sets up the button and its methods
        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked();
            }
        });
    }

    //If text fields aren't null, then we can pass the intent to the next activity
    private void onNextButtonClicked() {

        String orgNameText = orgName.getText().toString();
        String orgIdText = orgId.getText().toString();

        if (orgNameText.length() > 0 && orgIdText.length() > 0) {
            // Loads up the next activity and also passes data on
            Intent loadGPS = new Intent(this, AdminMapActivity.class);

            loadGPS.putExtra("orgName", orgNameText);
            loadGPS.putExtra("orgId", orgIdText);

            //Load up the Activity that has the GPS settings           //TODO. Note that the next activity should change DB table
            startActivity(loadGPS);
        }

        else {
            //Load up an error
            Context context = getApplicationContext();
            CharSequence text = "Error: Both parameters need to be filled";
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
