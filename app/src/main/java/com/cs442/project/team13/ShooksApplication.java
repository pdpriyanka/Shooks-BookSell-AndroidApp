package com.cs442.project.team13;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

/**
 * Created by chen on 2015/11/11.
 */
public class ShooksApplication extends Application {

    private static final String TAG = "RegIntentService";
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Userdata.class);
        ParseObject.registerSubclass(Book.class);
        ParseObject.registerSubclass(WishListTable.class);
        ParseObject.registerSubclass(Installation.class);
		// Put parse application id and client key
        Parse.initialize(this, "xxxx", "xxxx");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.saveInBackground();
    }
}
