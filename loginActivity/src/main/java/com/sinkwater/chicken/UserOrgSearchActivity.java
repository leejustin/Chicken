package com.sinkwater.chicken;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.Activity;
import java.util.List;
import com.parse.*;

import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        System.out.println(orgQuery);
    }

    private void setParseData(String searchQuery) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.whereContains("generalId", searchQuery);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queryList, ParseException e) {
                if (e == null) {
                    //success
                    System.out.println("bs");
                    //Bind results here

                    listview = (ListView)findViewById(R.id.classList);
                    adapter = new ArrayAdapter<String>(UserOrgSearchActivity.this,
                            R.layout.user_org_item);
                    for (ParseObject orbj : queryList) {
                        String item = orbj.getString("name");
                        adapter.add(item);
                    }
                    listview.setAdapter(adapter);
                } else {
                    //failed
                    System.out.println("fuceked up");
                }
            }
        });
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
