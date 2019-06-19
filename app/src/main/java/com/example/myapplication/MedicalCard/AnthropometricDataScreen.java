package com.example.myapplication.MedicalCard;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.UserActivities.ShareFeelingsActivity;
import com.example.myapplication.UserActivities.UserActivitiesScreen;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AnthropometricDataScreen extends AppCompatActivity {

    //TODO validate that this works 100% and to validate input details
    private EditText weight, height, bloodPressure, WaistCircumference, pulse;

    public void SubmitClickAnthroData(View view){

        getVariables();
        ParseObject AnthropometricData = new ParseObject("AnthropometricData");
        //AnthropometricData.put("parent", ParseObject.createWithoutData("User", ParseUser.getCurrentUser().getObjectId()));
        AnthropometricData.put("user", ParseUser.getCurrentUser().getUsername());
        AnthropometricData.put("weight", Integer.parseInt(weight.getText().toString()));
        AnthropometricData.put("height",Integer.parseInt(height.getText().toString()));
        AnthropometricData.put("waistCircuference",Integer.parseInt(WaistCircumference.getText().toString()));
        AnthropometricData.put("pulse",Integer.parseInt(pulse.getText().toString()));
        AnthropometricData.put("bloodPressure",Integer.parseInt(bloodPressure.getText().toString()));

        AnthropometricData.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(AnthropometricDataScreen.this, "Data shared", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ShareMedicalDataScreen.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AnthropometricDataScreen.this, "Error occurred - Please try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getVariables(){
        weight = (EditText) findViewById(R.id.Weight);
        height = (EditText) findViewById(R.id.Height);
        WaistCircumference = (EditText) findViewById(R.id.WaistCircumference);
        pulse = (EditText) findViewById(R.id.Pulse);
        bloodPressure = (EditText) findViewById(R.id.BloodPressure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anthropometric_data_screen);
        setTitle("Anthropometric Data");
    }
}
