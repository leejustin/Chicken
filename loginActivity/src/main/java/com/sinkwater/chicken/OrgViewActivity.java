package com.sinkwater.chicken;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.parse.ParseUser;
import com.sinkwater.chicken.db_handler.ParseDataHandler;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;

/**
 * Created by dongjin on 12/10/2014.
 */
public class OrgViewActivity extends Activity {
    private GoogleMap googleMap;
    private MarkerOptions searchMarkerOptions;
    private MarkerOptions userMarker;

    private String orgId;
    private TextView orgNameView;
    private TextView orgIdView;
    private TextView orgTimeView;
    private Button checkinButton;

    private ParseDataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_view);

        // createMapView();
        googleMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.orgMapView))
                .getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //get intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgId = extras.getString("query");
        } else {
            orgId = null;
        }

        //hook up views and buttons
        orgNameView = (TextView) findViewById(R.id.orgOrgName);
        orgIdView = (TextView) findViewById(R.id.orgOrgId);
        orgTimeView = (TextView) findViewById(R.id.orgOrgTime);
        checkinButton = (Button) findViewById(R.id.orgCheckinButton);
        dataHandler = new ParseDataHandler();

        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIn(orgId);
            }
        });

        setOrgView();
    }

    String parseTime(String timeStr) {
        String result = "";
        for(int i=0; i<timeStr.length(); i++) {
            if(timeStr.charAt(i)>='A' && timeStr.charAt(i)<='Z')
                if(i>0 && timeStr.charAt(i-1)>='0' && timeStr.charAt(i-1)<='9')
                    result += '\n';
            result += timeStr.charAt(i);
        }
        return result;
    }


    void setOrgView() {
        WrapperObject<String> orgNameWrapper = new WrapperObject(),
                              orgTimeWrapper = new WrapperObject();
        WrapperObject<LatLng> orgLocWrapper =  new WrapperObject();

        dataHandler.getOrgName(orgId, orgNameWrapper);
        dataHandler.getOrgTime(orgId, orgTimeWrapper);
        dataHandler.getOrgLocation(orgId, orgLocWrapper);

        LatLng latlngObj = null, userLatlngObj = null;



        if(orgId!=null)
            orgIdView.setText(orgId);
        if(orgNameWrapper.getVal()!=null)
            orgNameView.setText(orgNameWrapper.getVal());
        if(orgTimeWrapper.getVal()!=null)
            orgTimeView.setText(parseTime(orgTimeWrapper.getVal()));

        latlngObj = orgLocWrapper.getVal();

        //get the user's location
        GPSManager gps = new GPSManager(OrgViewActivity.this);
        double userLat = gps.getLatitude();
        double userLong = gps.getLongitude();
        userLatlngObj = new LatLng(userLat, userLong);

        //focus the google map to the org location with a marker
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngObj, 14));
        searchMarkerOptions = new MarkerOptions();
        userMarker = new MarkerOptions();
        googleMap.addMarker(searchMarkerOptions.position(latlngObj));
        googleMap.addMarker(userMarker
                .position(userLatlngObj)
                .title("My location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) );
    }

    private void checkIn(String query){
        //Make sure the string passed in is valid
        if (query != null || query.length() > 0) {

            //Passes query to the activity that will use it to query search results
            Intent intent = new Intent(this, UserCheckInActivity.class);
            intent.putExtra("query", query);
            startActivity(intent);
        }
    }

}
