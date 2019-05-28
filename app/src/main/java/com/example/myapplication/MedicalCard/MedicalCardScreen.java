/***
 * @author Kinan & Luzeen
 * This Activity is for Medical Card Screen that includes few options that the user
 * can click. Here we handle the clicked button and open an appropiate screens.
 */

package com.example.myapplication.MedicalCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.UserHistory.UserHistoryScreen;

public class MedicalCardScreen extends AppCompatActivity {


    /**
     * My History button has been pressed - Open new screen
     * @param view
     */
    public void myHistoryClick(View view) {
        Intent intent = new Intent(getApplicationContext(), UserHistoryScreen.class);
        startActivity(intent);

    }

    /**
     * Share Medical Data button has been pressed - Open new screen
     * @param view
     */
    public void shareMedicalDataClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ShareMedicalDataScreen.class);
        startActivity(intent);

    }

    /**
     * Share Card Data button has been pressed - Open new screen
     * @param view
     */
    public void shareCardClick(View view) {
        Toast.makeText(MedicalCardScreen.this, "Not available yet", Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_card_screen);
        setTitle("Medical Card");
    }
}
