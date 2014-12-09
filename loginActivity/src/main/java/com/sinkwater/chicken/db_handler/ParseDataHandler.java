package com.sinkwater.chicken.db_handler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.*;
import com.sinkwater.chicken.R;


import java.util.List;

/**
 * Created by justin on 11/16/14.
 */
public class ParseDataHandler {

    public ParseDataHandler() {
        //
    }

/* FUCK
    //Get information on organization
    public ParseObject getOrg(String objectId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //object is value
                    //result = object;

                } else {
                    //error
                }
            }
        });

        return null;
    }
*/
    //Set Parse organization information
    public static void addOrg(String orgName, String generalId, double latitude, double longitude) {

        //Set up data on organization
        ParseObject orgData = new ParseObject("Organization");
        orgData.put("name", orgName);
        orgData.put("generalId", generalId);
        orgData.put("latitude", latitude);
        orgData.put("longitude", longitude);

        orgData.saveInBackground();
    }

    public static void addUserOrg(String username, String generalId, String displayName) {

        //Set up data on user's organization. User should be Parse username key
        ParseObject userOrg = new ParseObject("UserOrg");
        userOrg.put("username", username);
        userOrg.put("generalId", generalId);
        userOrg.put("attendance", 0);
        userOrg.put("displayName", displayName);

        userOrg.saveInBackground();
    }








}
