package com.example.myapplication.MedicalCard;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AnthropometricDataScreen extends AppCompatActivity {

    private EditText weight, height, bloodPressure, WaistCircumference, pulse;

    public void SubmitClick(View view){

        getVariables();
        ParseObject AnthropometricData = new ParseObject("AnthropometricData");
        AnthropometricData.put("parent", ParseObject.createWithoutData("User", ParseUser.getCurrentUser().getObjectId()));
        AnthropometricData.put("weight", Integer.parseInt(weight.getText().toString()));
        AnthropometricData.put("height",Integer.parseInt(height.getText().toString()));
        AnthropometricData.put("waistCircuference",WaistCircumference);
        AnthropometricData.put("pulse",pulse);
        AnthropometricData.put("bloodPressure",Integer.parseInt(bloodPressure.getText().toString()));
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
