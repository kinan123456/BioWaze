/***
 * @author Kinan & Luzeen
 * LifeStyleActivity is an activity for users and has multiple choices
 * Actions that he does: Sport/Movement , Medical Action, etc.
 */

package com.example.myapplication.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class LifestyleActivity extends AppCompatActivity {

    Intent intent;

    /**
     * Food-Related button has been pressed - open new screen
     * @param view
     */
    public void foodRelatedClick(View view) {
        intent = new Intent(getApplicationContext(), FoodRelatedActivity.class);
        startActivity(intent);
    }

    /**
     * Sport/Movement button has been pressed - open new screen
     * @param view
     */
    public void sportMovementClick(View view) {
        Toast.makeText(LifestyleActivity.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    /**
     * Medical Action / Procedure button has been pressed - open new screen
     * @param view
     */
    public void medicalProcedureClick(View view) {
        Toast.makeText(LifestyleActivity.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    /**
     * Good/Bad Deed button has been pressed - open new screen
     * @param view
     */
    public void goodBadDeedClick(View view) {
        Toast.makeText(LifestyleActivity.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    /**
     * Work button has been pressed - open new screen
     * @param view
     */
    public void workClick(View view) {
        Toast.makeText(LifestyleActivity.this, "Not available yet", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserActivitiesScreen.class));
    }
    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle);
        setTitle("Actions");

    }
}