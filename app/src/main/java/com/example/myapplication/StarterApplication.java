/**
 * @author Kinan & Luzeen
 * An Application that executed at first.
 * Its goal to initialize the Parse Configurations and the connection to the Parse Server
 */
package com.example.myapplication;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.facebook.ParseFacebookUtils;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("920191d294d02426baa6fbc7cba191951f1cbdab")
                .clientKey("6b459d634eeacd8009b7a92f5d67585f7284789a")
                .server("http://13.59.185.64:80/parse/")
                .build()
        );

        //ParseFacebookUtils.initialize(this);

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

    }
}
