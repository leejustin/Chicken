package com.sinkwater.chicken;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.sinkwater.chicken.R;

public class FacebookHandler extends Application {

	static final String TAG = "MyApp";

	@Override
	public void onCreate() {
		super.onCreate();

        //Initialize Parse
        Parse.initialize(this,
                "PtbyMcZFz4nahip8pNMPITcPluoBpvJeWjATZDpB",
                "JaHQqlRCSYlzANTMb2fz4CF5E5WbAL49hU5UPcDM"
        );

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));
	}
}
