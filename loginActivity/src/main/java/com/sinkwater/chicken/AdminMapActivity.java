package com.sinkwater.chicken;

import android.app.Activity;
import android.content.DialogInterface;
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

import java.io.IOException;
import java.util.List;

public class AdminMapActivity extends Activity implements OnMapLongClickListener, OnMapClickListener, OnMarkerClickListener {

    GoogleMap googleMap;
    MarkerOptions searchMarkerOptions;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_map);
        // createMapView();
        googleMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView))
                .getMap();
        
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);

        // Get Reference to Find Button
        Button button_find = (Button) findViewById(R.id.string_button_find);

        // Define button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText enteredLocation = (EditText) findViewById(R.id.enter_location);

                String location = enteredLocation.getText().toString();

                if(location!= null && !location.equals("")) {
                    new GeocoderTask().execute(location);
                }
            }
        };
        button_find.setOnClickListener(findClickListener);
    }

    // Not used for now
    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                //googleMap.setOnMapClickListener(this);

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    //error
                }
            }
        } catch (NullPointerException exception){
            //error
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_map, menu);
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.clear();
        searchMarkerOptions = new MarkerOptions();
        googleMap.addMarker(searchMarkerOptions
                .position(latLng)
                .title(latLng.toString()));
        Toast.makeText(getBaseContext(), "Marker added at: " + latLng.toString(), Toast.LENGTH_SHORT).show();

    }
    // Click on map to move
    // TODO: Maybe we do not need this functionality
    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO: Add functionality so that marker can pass LatLng object
        Log.w("Click", "test");
        return false;
    }

    // Used for Searching for the address
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationNames) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try{
                addresses = geocoder.getFromLocationName(locationNames[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if(addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location Found", Toast.LENGTH_SHORT).show();
            }
            googleMap.clear();

            assert addresses != null;
            for(int i = 0;  i < addresses.size(); i++) {
                Address address;
                address = addresses.get(i);
                latLng = new LatLng(address.getLatitude(),address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                searchMarkerOptions = new MarkerOptions();
                searchMarkerOptions.position(latLng);
                searchMarkerOptions.title(addressText);

                googleMap.addMarker(searchMarkerOptions);
                if(i==0) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
