package com.sinkwater.chicken;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.Activity;
import java.util.List;
import com.parse.*;
import com.sinkwater.chicken.db_handler.ParseDataHandler;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UserOrgSearchActivity extends Activity {

    String orgQuery;
    ListView listview;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_org_search);

        //get the organization name that was passed into the activity

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgQuery = extras.getString("query");
        }

        setParseData(orgQuery);

    }

    private void setParseData(String searchQuery) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.whereContains("generalId", searchQuery);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queryList, ParseException e) {
                if (e == null) {
                    //success
                    //System.out.println("bs");
                    //Bind results here

                    listview = (ListView)findViewById(R.id.classList);
                    adapter = new ArrayAdapter<String>(UserOrgSearchActivity.this,
                            R.layout.user_org_item);
                    if(queryList.isEmpty()) {
                        adapter.add("No class is found");
                    } else {
                        for (ParseObject orbj : queryList) {
                            String item = orbj.getString("name");
                            adapter.add(item);
                        }
                    }
                    listview.setAdapter(adapter);

                    listview.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(getApplicationContext(),
                                    "Class added", Toast.LENGTH_LONG)
                                    .show();
                            onItemClicked(parent, view, position, id);

                        }
                    });

                } else {
                    //failed
                    System.out.println("fucked up");
                }
            }
        });
    }

    public void onItemClicked(AdapterView<?> l, View v, int position, long id) {
        Log.i("clicking thing", "clicked the item: " + id + " at pos: " + position);

        String orgName = (String)l.getItemAtPosition(position);

        /* Create a new org that the user is associated with. TODO need to do something to check for
        making sure that the item doesnt already exist in the DB--except i really dont care to implement it right now
         */
        System.out.println("LOOOOLLL");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.whereContains("name", orgName);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queryList, ParseException e) {
                if (e == null) {
                    String orgId;

                    //get the associated value for organization
                    orgId = queryList.get(0).getString("generalId");

                    //get the associated value for user ID
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String username = (String)currentUser.get("username");

                    //get the user's display name
                    JSONObject userProfile = currentUser.getJSONObject("profile");

                    String displayName;
                    try {
                        displayName = userProfile.getString("name");
                    }
                    catch (JSONException d) {
                        //you fucked up TODO error catching
                        displayName = "broken";
                    }


                    ParseDataHandler userOrg = new ParseDataHandler();
                    //push the data
                    userOrg.addUserOrg(username, orgId, displayName);

                } else {
                    //failed
                    System.out.println("fuceked up");
                }
            }
        });


        Intent intent = new Intent(this, UserMenuActivity.class);
        //intent.putExtra("theClass", theClass);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_org_search, menu);
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
