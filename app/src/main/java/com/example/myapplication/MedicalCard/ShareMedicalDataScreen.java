/***
 * ShareMedicalDataScreen activity has few choices as buttons
 * It is for the medical data for a user e.g. Anthropometric data, medical test, doctor visits, etc
 * Each button click opens a new screen
 * @author Kinan & Luzeen
 */

package com.example.myapplication.MedicalCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ShareMedicalDataScreen extends AppCompatActivity {

    public void AnthropometricDataClick(View view){
        Intent intent = new Intent(getApplicationContext(), AnthropometricDataScreen.class);
        startActivity(intent);
    }

    public void MedicalTestClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void DoctorVisitClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void MoreClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_medical_data_screen);
        setTitle("Medical Data");
    }
}
