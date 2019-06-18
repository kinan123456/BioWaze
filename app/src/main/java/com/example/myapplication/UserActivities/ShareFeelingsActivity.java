/***
 * @author Kinan & Luzeen
 * ShareFeelingsActivity is an activity for users to share what they feel
 * The application will gather the input data and store it in the Cloud Database
 */

package com.example.myapplication.UserActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShareFeelingsActivity extends AppCompatActivity {
    /**
     * Variables Initialization
     */

    //@TODO 'good' choices for Body Part & Feeling

    DatePickerDialog startedPicker, stoppedPicker;
    EditText startedFeelingDatePicker, stoppedFeelingDatePicker;
    Spinner bodyPartSpinner, feelingSpinner;

    /**
     * Share Feelings button has been pressed - open new screen
     * @param view
     */
    public void shareFeelingClick(View view) {
        String bodyPartString = bodyPartSpinner.getSelectedItem().toString();
        String feelingString = feelingSpinner.getSelectedItem().toString();
        String startedFeelingString = startedFeelingDatePicker.getText().toString();
        String stoppedFeelingString = stoppedFeelingDatePicker.getText().toString();
        String reason = ((EditText)findViewById(R.id.possibleReasonContent)).getText().toString();

        if (startedFeelingString.matches("")|| stoppedFeelingString.matches("") || reason.matches("")) {
            Toast.makeText(ShareFeelingsActivity.this, "One or more missing fields. Try again.", Toast.LENGTH_LONG).show();
        } else {
            try {
                ParseObject feelingsHistory = new ParseObject("FeelingsHistory");


                Date startedFeelingDate = new SimpleDateFormat("dd/MM/yyyy").parse(startedFeelingString);
                Date stoppedFeelingDate = new SimpleDateFormat("dd/MM/yyyy").parse(stoppedFeelingString);

                feelingsHistory.put("bodyPart", bodyPartString);
                feelingsHistory.put("feeling", feelingString);
                feelingsHistory.put("startedDate", startedFeelingDate);
                feelingsHistory.put("stoppedDate", stoppedFeelingDate);
                feelingsHistory.put("possibleReason", reason);
                feelingsHistory.put("user", ParseUser.getCurrentUser().getUsername());
                feelingsHistory.put("parent", ParseObject.createWithoutData("User", ParseUser.getCurrentUser().getObjectId()));

                feelingsHistory.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(ShareFeelingsActivity.this, "Your Feelings are submitted", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), UserActivitiesScreen.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ShareFeelingsActivity.this, "Error occurred - Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initialize spinner for 'select body part'
     */
    public void initBodyPartSpinner() {

        bodyPartSpinner = (Spinner) findViewById(R.id.bodyPartSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bodyPartSpinnerFeeling, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bodyPartSpinner.setAdapter(adapter);
    }

    /**
     * Initialize spinner for 'select feeling'
     */
    public void initFeelingSpinner() {
        feelingSpinner = (Spinner) findViewById(R.id.feelingSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.feelingSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feelingSpinner.setAdapter(adapter);
    }

    /**
     * Handle date picker listener
     */
    public void initDatePickerListener() {
        startedFeelingDatePicker = (EditText) findViewById(R.id.startedFeelingDate);
        startedFeelingDatePicker.setInputType(InputType.TYPE_NULL);
        startedFeelingDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                startedPicker = new DatePickerDialog(ShareFeelingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startedFeelingDatePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                startedPicker.show();
            }
        });



        stoppedFeelingDatePicker = (EditText) findViewById(R.id.stoppedFeelingDate);
        stoppedFeelingDatePicker.setInputType(InputType.TYPE_NULL);
        stoppedFeelingDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                stoppedPicker = new DatePickerDialog(ShareFeelingsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                stoppedFeelingDatePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                stoppedPicker.show();
            }
        });
    }


    /**
     * Back Android button has been pressed - go back to previous screen
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), UserActivitiesScreen.class);
        startActivity(intent);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_feelings);
        setTitle("Share Feelings");

        initBodyPartSpinner();
        initFeelingSpinner();
        initDatePickerListener();
    }
}
