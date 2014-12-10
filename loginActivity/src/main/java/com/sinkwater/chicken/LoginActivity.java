package com.sinkwater.chicken;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import com.google.android.gms.maps.model.LatLng;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.sinkwater.chicken.R;
import com.sinkwater.chicken.db_handler.ParseDataHandler;

import com.sinkwater.chicken.db_handler.ParseDataHandler;

public class LoginActivity extends Activity {

	private Button loginButton;
	private Dialog progressDialog;

    private Button adminMapButton;

    GPSManager gpsM;
    GPSData data = new GPSData(0.0,0.0);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
        adminMapButton = (Button) findViewById(R.id.AdminMapButton);
        adminMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdminMapClicked();
            }
        });

		// Check if there is a currently logged in user
		// and it's linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
            boolean isUserAdmin = currentUser.getBoolean("admin");
            //if admin w/ or w/out organization
            if(isUserAdmin){
                String orgId = currentUser.getString("organization");
                if(orgId.length()>0){
                    loadAdminMenu();
                }else{
                    loadAdminSetData();
                }
            }else{
                //if user
                loadUserMenu();
            }
		}

      //  ParseDataHandler test =  new ParseDataHandler();



        // This makes a Toast that displays GPS coordinates.
        //
       // gpsM = new GPSManager(LoginActivity.this);

       // if(gpsM.canGetLocation()) {

       //     double latitude = gpsM.getLatitude();
       //     double longitude = gpsM.getLongitude();

       //     Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
       // }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

    private void onAdminMapClicked() {
        Intent intent = new Intent(this, AdminMapActivity.class);
        startActivity(intent);
    }


	private void onLoginButtonClicked() {
		LoginActivity.this.progressDialog = 
				ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);
		
		List<String> permissions = Arrays.asList("public_profile", "email");
		// NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
		// (https://developers.facebook.com/docs/facebook-login/permissions/)

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();

                //Fetch from Parse if this user is admin or not
                Boolean isUserAdmin = (Boolean)user.get("admin");
                String userOrg = (String)user.get("organization");

				if (user == null) {
					Log.d(FacebookHandler.TAG, "User cancelled Facebook login");
				} else if (user.isNew()) {
					Log.d(FacebookHandler.TAG, "User signed up and logged in through Facebook");
                    if (isUserAdmin) {
                        //If admin isn't associated with an org, they need to set their data
                        if (userOrg.length() > 0 ) {
                            loadAdminMenu();
                        }
                        else {
                            loadAdminSetData();
                        }
                    }
                    else {
                        loadUserMenu();
                    }

				} else {
					Log.d(FacebookHandler.TAG, "User logged in through Facebook");
                    if (isUserAdmin) {
                        //If admin isn't associated with an org, they need to set their data
                        if (userOrg.length() > 0) {
                            loadAdminMenu();
                        }
                        else {
                            loadAdminSetData();
                        }
                    }
                    else {
                        loadUserMenu();
                    }
				}
			}
		});
	}

    //Load Activity for Admin
	private void loadAdminMenu() {
		Intent intent = new Intent(this, AdminMenuActivity.class);
		startActivity(intent);
	}

    //Load Activity for Admin to set settings
    private void loadAdminSetData() {
        Intent intent = new Intent(this, AdminSetDataActivity.class);
        startActivity(intent);
    }

    //Load the Activity for User
    private void loadUserMenu() {
        Intent intent = new Intent(this, UserMenuActivity.class);
        startActivity(intent);
    }
}
