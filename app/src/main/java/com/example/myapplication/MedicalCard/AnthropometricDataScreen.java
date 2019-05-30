package com.example.myapplication.MedicalCard;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AnthropometricDataScreen extends AppCompatActivity {

    private EditText weight, height, bloodPressure, WaistCircumference, pulse;

    public void SubmitClick(View view){

    }

    public void initVariables(){
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
        initVariables();
    }
}
