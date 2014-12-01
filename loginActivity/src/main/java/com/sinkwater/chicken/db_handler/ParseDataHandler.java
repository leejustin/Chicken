package com.sinkwater.chicken.db_handler;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by justin on 11/16/14.
 */
public class ParseDataHandler {




    //Need function to get data about an organization.




    //Set Parse organization information
  //  ParseQuery<ParseObject> query = ParseQuery.getQuery("")
    public void createOrg(String orgName, String generalId, double latitude, double longitude) {

        //Set up data on organization
        ParseObject orgData = new ParseObject("Organization");
        orgData.put("name", orgName);
        orgData.put("generalId", generalId);
        orgData.put("latitude", latitude);
        orgData.put("longitude", longitude);

        orgData.saveInBackground();
    }

}
