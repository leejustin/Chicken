package com.sinkwater.chicken.db_handler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.*;
import com.sinkwater.chicken.R;
import com.sinkwater.chicken.WrapperObject;

import java.sql.Wrapper;
import java.util.List;

/**
 * Created by justin on 11/16/14.
 */
public class ParseDataHandler {

    public ParseDataHandler() {
    }




    //Set Parse organization information
    public void addOrUpdateOrg(ParseUser current_user, String orgName, String generalId, String time, LatLng latlng) {

        // Prevent duplicate generalIds for organizations
        ParseQuery<ParseObject> orgQuery = ParseQuery.getQuery("Organization");
        orgQuery.whereContains("generalId", generalId);

        List<ParseObject> results = null;
        try {
            results = orgQuery.find();
        } catch(ParseException e) {
            return;
        }

        String usersOrg = current_user.getString("organization");

        if(results.size()>0 && usersOrg.equals(generalId)==true) {
            updateOrg(generalId, orgName, time, latlng);
        } else if(results.size()==0) {
            addOrg(orgName, generalId, time, latlng.latitude, latlng.longitude);
            current_user.put("organization", generalId); //update the user's organization
            current_user.saveInBackground();
        }

    }

    private void addOrg(String orgName, String generalId, String time, double latitude, double longitude) {
        //Set up data on organization
        ParseObject orgData = new ParseObject("Organization");
        orgData.put("name", orgName);
        orgData.put("time", time);
        orgData.put("generalId", generalId);
        orgData.put("latitude", latitude);
        orgData.put("longitude", longitude);
        orgData.saveInBackground();
    }

    public void updateOrg(String generalId, final String name, final String time, final LatLng latlng) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        WrapperObject<String> result = new WrapperObject();
        getOrgObjId(generalId, result);

        //asynchronously process the update
        query.getInBackground(result.getVal(), new GetCallback<ParseObject>() {
            public void done(ParseObject orgObj, ParseException e) {
                if (e == null) {
                    if(name!=null)
                        orgObj.put("name", name);
                    if(time!=null)
                        orgObj.put("time", time);
                    if(latlng!=null) {
                        orgObj.put("latitude", latlng.latitude);
                        orgObj.put("longitude", latlng.longitude);
                    }
                    if(name!=null || time!=null || latlng!=null)
                        orgObj.saveInBackground();
                }
            }
        });
    }

    public void getOrgObjId(String generalId, WrapperObject<String> orgObjIdWrapper) {
        ParseQuery<ParseObject> orgQuery = ParseQuery.getQuery("Organization");
        orgQuery.whereContains("generalId", generalId);

        List<ParseObject> results = null;
        try {
            results = orgQuery.find();
        } catch(ParseException e) {
            return;
        }
        if(results!=null && results.get(0)!=null)
            orgObjIdWrapper.setVal(results.get(0).getObjectId());
    }

    public void getOrgName(String generalId, WrapperObject<String> orgNameWrapper) {

        ParseQuery<ParseObject> orgQuery = ParseQuery.getQuery("Organization");
        orgQuery.whereContains("generalId",generalId);

        List<ParseObject> results = null;
        try {
            results = orgQuery.find();
        } catch(ParseException e) {
            return;
        }
        if(results!=null && results.get(0)!=null)
            orgNameWrapper.setVal(results.get(0).getString("name"));
    }

    public void getOrgTime(String generalId, WrapperObject<String> orgNameWrapper) {

        ParseQuery<ParseObject> orgQuery = ParseQuery.getQuery("Organization");
        orgQuery.whereContains("generalId",generalId);

        List<ParseObject> results = null;
        try {
            results = orgQuery.find();
        } catch(ParseException e) {
            return;
        }
        if(results!=null && results.get(0)!=null)
            orgNameWrapper.setVal(results.get(0).getString("time"));
    }

    public void getOrgLocation(String generalId, WrapperObject<LatLng> orgNameWrapper) {

        ParseQuery<ParseObject> orgQuery = ParseQuery.getQuery("Organization");
        orgQuery.whereContains("generalId",generalId);

        List<ParseObject> results = null;
        try {
            results = orgQuery.find();
        } catch(ParseException e) {
            return;
        }

        if(results!=null && results.get(0)!=null) {
            double lat=results.get(0).getDouble("latitude");
            double lon=results.get(0).getDouble("longitude");
            LatLng latlngObj = new LatLng(lat, lon);
            orgNameWrapper.setVal(latlngObj);
        }
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
