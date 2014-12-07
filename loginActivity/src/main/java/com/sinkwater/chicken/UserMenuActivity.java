package com.sinkwater.chicken;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.*;


public class UserMenuActivity extends Activity {

    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    private ProfilePictureView userProfilePictureView;
    private TextView userNameView;
    //private TextView userGenderView;
    //private TextView userEmailView;
    private Button logoutButton;
    private Button searchButton;
    private EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermenu);

        searchQuery = (EditText)findViewById(R.id.searchQuery);
       // setParseData();

        userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        userNameView = (TextView) findViewById(R.id.userName);
        //userGenderView = (TextView) findViewById(R.id.userGender);
        //userEmailView = (TextView) findViewById(R.id.userEmail);

        searchButton = (Button) findViewById(R.id.searchOrgs);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passValue = searchQuery.getText().toString();
                searchOrg(passValue);
            }
        });

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutButtonClicked();
            }
        });

        // Fetch Facebook user info if the session is active
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            makeMeRequest();
        }
    }

    private void setParseData() {
        //nothing lol
    }




    @Override
    public void onResume() {
        super.onResume();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // Check if the user is currently logged
            // and show any cached content
            updateViewsWithProfileInfo();
        } else {
            // If the user is not logged in, go to the
            // activity showing the login view.
            startLoginActivity();
        }
    }

    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {
                                // Populate the JSON object
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender", (String) user.getProperty("gender"));
                                }
                                if (user.getProperty("email") != null) {
                                    userProfile.put("email", (String) user.getProperty("email"));
                                }

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                                // Show the user info
                                updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d(FacebookHandler.TAG, "Error parsing returned user data. " + e);
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                    (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d(FacebookHandler.TAG, "The facebook session was invalidated." + response.getError());
                                onLogoutButtonClicked();
                            } else {
                                Log.d(FacebookHandler.TAG,
                                        "Some other error: " + response.getError());
                            }
                        }
                    }
                }
        );
        request.executeAsync();
    }


    private void updateViewsWithProfileInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser.has("profile")) {
            JSONObject userProfile = currentUser.getJSONObject("profile");
            try {

                if (userProfile.has("facebookId")) {
                    userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
                } else {
                    // Show the default, blank user profile picture
                    userProfilePictureView.setProfileId(null);
                }

                if (userProfile.has("name")) {
                    userNameView.setText(userProfile.getString("name"));
                } else {
                    userNameView.setText("");
                }
				/*
				if (userProfile.has("gender")) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}

				if (userProfile.has("email")) {
					userEmailView.setText(userProfile.getString("email"));
				} else {
					userEmailView.setText("");
				}
				*/
            } catch (JSONException e) {
                Log.d(FacebookHandler.TAG, "Error parsing saved user data.");
            }
        }
    }

    //Takes in the search parameter and passes it to the next activity
    private void searchOrg(String query) {
        //Make sure the string passed in is valid
        if (query != null || query.length() > 0) {

            //Passes query to the activity that will use it to query search results
            Intent intent = new Intent(this, UserOrgSearchActivity.class);
            intent.putExtra("query", query);
            startActivity(intent);
        }
    }


    private void onLogoutButtonClicked() {
        // Log the user out
        ParseUser.logOut();

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
