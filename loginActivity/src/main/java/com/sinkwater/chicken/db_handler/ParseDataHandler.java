package com.sinkwater.chicken.db_handler;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by justin on 11/16/14.
 */
public class ParseDataHandler {

    public ParseDataHandler() {
        //
    }


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

    public void addUserOrg(String username, String generalId) {

        //Set up data on user's organization. User should be Parse username key
        ParseObject userOrg = new ParseObject("UserOrg");
        userOrg.put("username", username);
        userOrg.put("generalId", generalId);

        userOrg.saveInBackground();
    }

}
