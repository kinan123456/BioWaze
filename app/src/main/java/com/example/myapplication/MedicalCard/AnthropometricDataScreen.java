package com.example.myapplication.MedicalCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AnthropometricDataScreen extends AppCompatActivity {

    //TODO validate that this works 100% and to validate input details
    private EditText weight, height, bloodPressure, WaistCircumference, pulse;

    public void SubmitClickAnthroData(View view) {
        String tempWeight, tempHeight, tempBloodPres, tempWaistCircu, tempPulse;
        int tmpweight,tmpheight,tmpBloodPres,tmpWaistcircu,tmpPulse;
        getVariables();

        tempWeight = weight.getText().toString();
        tempHeight = height.getText().toString();
        tempBloodPres = bloodPressure.getText().toString();
        tempWaistCircu = WaistCircumference.getText().toString();
        tempPulse = pulse.getText().toString();

        tmpweight= Integer.valueOf(tempWeight);
        tmpheight=Integer.valueOf(tempHeight);
        tmpBloodPres=Integer.valueOf(tempBloodPres);
        tmpWaistcircu=Integer.valueOf(tempWaistCircu);
        tmpPulse=Integer.valueOf(tempPulse);

        //checking if there is any empty field  or if there are values <=0
        if (tempWeight.equals("") || tempHeight.equals("") ||
                tempBloodPres.equals("") || tempWaistCircu.equals("") ||
                tempPulse.equals("")|| tmpweight <=0 ||
                tmpheight<=0 || tmpBloodPres<=0 || tmpWaistcircu<=0 || tmpPulse<=0)
            Toast.makeText(AnthropometricDataScreen.this, "One or more missing fields. Try again.", Toast.LENGTH_LONG).show();

        else {

            ParseObject AnthropometricData = new ParseObject("AnthropometricData");
            AnthropometricData.put("parent", ParseObject.createWithoutData("User", ParseUser.getCurrentUser().getObjectId()));
            AnthropometricData.put("user", ParseUser.getCurrentUser().getUsername());
            AnthropometricData.put("weight", Integer.parseInt(weight.getText().toString()));
            AnthropometricData.put("height", Integer.parseInt(height.getText().toString()));
            AnthropometricData.put("waistCircuference", Integer.parseInt(WaistCircumference.getText().toString()));
            AnthropometricData.put("pulse", Integer.parseInt(pulse.getText().toString()));
            AnthropometricData.put("bloodPressure", Integer.parseInt(bloodPressure.getText().toString()));

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
    }
    public void getVariables(){
        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);
        WaistCircumference = (EditText) findViewById(R.id.waistCircumference);
        pulse = (EditText) findViewById(R.id.pulse);
        bloodPressure = (EditText) findViewById(R.id.BloodPressure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anthropometric_data_screen);
        setTitle("Anthropometric Data");
    }
}
