/***
 * @author Kinan & Luzeen
 * MainActivity is the first screen that launched (after
 * the LoginActivity from Parse. MainActivity displays to user options to choose,
 * each option is implemented by a button that opens a new screen.
 * Logout button is implemented, navigation drawer, etc.
 */


package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MedicalCard.MedicalCardScreen;
import com.example.myapplication.UserActivities.UserActivitiesScreen;
import com.parse.ParseAnalytics;
import com.parse.ui.login.ParseLoginBuilder;

public class MainActivity extends AppCompatActivity {


    /**
     * Leave empty if you want nothing to happen on back press.
     */
    @Override
    public void onBackPressed()
    { }

    /**
     * Medical Card button has been pressed - Open new screen
     * @param view
     */
    public void medicalCardClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MedicalCardScreen.class);
        startActivity(intent);
    }


    /**
     * More Activities button has been pressed - Open new screen
     * @param view
     */
    public void moreActivitiesClick(View view) {
        Toast.makeText(MainActivity.this, "Not available yet", Toast.LENGTH_LONG).show();

    }
    /**
     * Share Activity button has been pressed - Open new screen
     * @param view
     */
    public void shareActivityClick(View view) {
        Intent intent = new Intent(getApplicationContext(), UserActivitiesScreen.class);
        startActivity(intent);
    }

    /**
     * My Network button has been pressed - Open new screen
     * @param view
     */
    public void myNetworkClick(View view) {
        Toast.makeText(MainActivity.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    /**
     * Create options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle selected items from the options menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.myAccount) {
            Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * First executed method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Homepage");
        /*ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
        startActivityForResult(builder.build(), 0);*/
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}

//@TODO Toast messages in black color