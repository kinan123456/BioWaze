/**
 * @author Kinan & Luzeen
 * An Application that executed at first.
 * Its goal to initialize the Parse Configurations and the connection to the Parse Server
 */
package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRole;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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

        //readCSV();
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
        ParseRole role = new ParseRole("Administrator", defaultACL);
        role.saveInBackground();

    }


    private void readCSV() {

        InputStream is = getResources().openRawResource(R.raw.dataset_csv);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        try {
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                // Split the line into different tokens (using the comma as a separator).
                String[] nextLine = line.split(",");
                ParseObject heartDisease = new ParseObject("HeartDisease");
                heartDisease.put("age", Integer.parseInt(nextLine[0]));
                heartDisease.put("sex", Integer.parseInt(nextLine[1]));
                heartDisease.put("cp", Integer.parseInt(nextLine[2]));
                heartDisease.put("trestbps", Integer.parseInt(nextLine[3]));
                heartDisease.put("chol", Integer.parseInt(nextLine[4]));
                heartDisease.put("fbs", Integer.parseInt(nextLine[5]));
                heartDisease.put("restecg", Integer.parseInt(nextLine[6]));
                heartDisease.put("thalach", Integer.parseInt(nextLine[7]));
                heartDisease.put("exang", Integer.parseInt(nextLine[8]));
                heartDisease.put("oldpeak", Float.parseFloat(nextLine[9]));
                heartDisease.put("slope", Integer.parseInt(nextLine[10]));
                heartDisease.put("ca", Integer.parseInt(nextLine[11]));
                heartDisease.put("thal", Integer.parseInt(nextLine[12]));
                heartDisease.put("target", Integer.parseInt(nextLine[13]));
                heartDisease.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            Log.i("SaveInBackground", "Successful");

                        } else {


                            Log.i("SaveInBackground", "Failed. Error: " + e.toString());

                        }

                    }
                });

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
