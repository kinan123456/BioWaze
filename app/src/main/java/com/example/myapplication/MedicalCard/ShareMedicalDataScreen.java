package com.example.myapplication.MedicalCard;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import com.example.myapplication.R;
import android.content.Intent;

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

    public void DescribeYourPastClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void DiscribeYourEnvironmentClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void UploadDataFromDevicesClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void UplaodPicturesClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void MoreClick(View view){
        Toast.makeText(ShareMedicalDataScreen.this, "Not available yet", Toast.LENGTH_LONG).show();
    }

    public void SubmitClick(View view){
        String note_content_string = ((EditText)findViewById(R.id.NoteContent)).getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_medical_data_screen);
    }


}
