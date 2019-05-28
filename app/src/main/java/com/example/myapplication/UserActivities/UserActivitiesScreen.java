/***
 * @author Kinan & Luzeen
 * UserActivitiesScreen to display the lifestyle activities that user does and can share it
 * as input data to store in cloud.
 * For example: "I did" button, "I see" button... The user clicks a category which then will
 * open a new screen for the chosen button
 */


package com.example.myapplication.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class UserActivitiesScreen extends AppCompatActivity {

    /**
     * Initialize list items that will be displayed in User Activities Screen
     * e.g. "I feel..", "I did..", ...
     */
    public void initList() {
        final ArrayList<String> activities = new ArrayList<String>();

        final ListView shareActivitiesList = (ListView) findViewById(R.id.shareActivitiesList);

        shareActivitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    //"I feel" button
                    case 0:
                        intent = new Intent(getApplicationContext(), ShareFeelingsActivity.class);
                        startActivity(intent);
                        break;

                    //"I did" button
                    case 1:
                        intent = new Intent(getApplicationContext(), LifestyleActivity.class);
                        startActivity(intent);
                        break;

                    //"I see" button
                    case 2:
                        Toast.makeText(UserActivitiesScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
                        break;

                    //"In my Environment" button
                    case 3:
                        Toast.makeText(UserActivitiesScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }
        });
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, activities);
        activities.add("I feel..");
        activities.add("I did..");
        activities.add("I see..");
        activities.add("In My Environment..");

        shareActivitiesList.setAdapter(arrayAdapter);

    }

    /**
     * Back Android button has been pressed - go back to previous screen
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities_screen);
        setTitle("Life-style Activities");
        initList();
        initActionBar();
    }
}
