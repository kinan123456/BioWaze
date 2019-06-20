/***
 * @author Kinan & Luzeen
 * UserHistoryScreen is accessed after clicking 'My History' button from MedicalCardScreen
 * Here we visualize the lifestyle history that the user has for selected time for period
 * from specific category. He can choose to display his data by a Graph View or a Table View
 */

package com.example.myapplication.UserHistory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MedicalCard.MedicalCardScreen;
import com.example.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UserHistoryScreen extends AppCompatActivity {
    private RadioButton weight ,height,blood,pulse,waistCircu;
    private RadioGroup categoryHistoryGroup, visualizeByGroup ,anthroGroupChoices;
    private TextView visualizeHistoryBy, startDateTextView, endDateTextView;
    private Button pickDate, submitButtonHistory;
    private int selectedStartYear, selectedStartMonth, selectedStartDay;
    private String firstDateString, secondDateString;
    private int selectedEndYear, selectedEndMonth, selectedEndDay;
    private Calendar startCalendarDate, endCalendarDate;
    private DatePickerDialog datePickerFirstPickerDialog, datePickerSecondPickerDialog;


    //clicked anthropometric data --> show weight , height , .....
    public void AnthropoButtonClicked(View view) {

        weight.setVisibility(View.VISIBLE);
        height.setVisibility(View.VISIBLE);
        blood.setVisibility(View.VISIBLE);
        waistCircu.setVisibility(View.VISIBLE);
        pulse.setVisibility(View.VISIBLE);
    }
    /***
     * Submit button has been pressed - handle the given info
     * Get the category selected: 'Feelings history' , 'Food history' , etc...
     * then check the display choice: graph or table view
     * @param view
     */
    public void submitHistoryClick(View view) {
        int selectedCategory = categoryHistoryGroup.getCheckedRadioButtonId();
        RadioButton selectedCategoryRadioButton = (RadioButton) findViewById(selectedCategory);
        String selectedCategoryText = selectedCategoryRadioButton.getText().toString().replaceAll("\\s", "");
        checkDisplayChoice(selectedCategoryText);
    }


    /**
     * Check the display choice (either graph view or table view)
     * then open a new screen for data visualization
     * @param selectedCategoryText
     */
    public void checkDisplayChoice(String selectedCategoryText) {
        int selectedDisplay = visualizeByGroup.getCheckedRadioButtonId();
        if (startDateTextView.getText().equals("") || endDateTextView.getText().equals("")) {
            Toast.makeText(UserHistoryScreen.this, "You have to pick a Date.", Toast.LENGTH_LONG).show();

        } else {
            if (selectedDisplay == -1) {
                Toast.makeText(UserHistoryScreen.this, "Please Choose Graph View or Table View then try again.", Toast.LENGTH_LONG).show();
            } else {

                RadioButton selectedDisplayRadioButton = findViewById(selectedDisplay);
                String selectedDisplayText = selectedDisplayRadioButton.getText().toString();
                //if display by is Graph option
                if (selectedDisplayText.startsWith("Graph")) {
                    int selectedColumn = anthroGroupChoices.getCheckedRadioButtonId();
                    RadioButton selectedCategoryRadioButton = (RadioButton) findViewById(selectedColumn);
                    String selectedColumnText = selectedCategoryRadioButton.getText().toString().replaceAll("\\s", "");

                    Intent intent = new Intent(getApplicationContext(), GraphHistoryView.class);
                    intent.putExtra("selectedAnthroData",selectedColumnText);
                    intent.putExtra("startDate", startCalendarDate.getTimeInMillis());
                    intent.putExtra("endDate", endCalendarDate.getTimeInMillis());
                    startActivity(intent);
                }  else{
                    //if display by is Table View option
                    Intent intent = new Intent(getApplicationContext(), TableHistoryView.class);
                    intent.putExtra("listName", selectedCategoryText);
                    intent.putExtra("startDate", startCalendarDate.getTimeInMillis());
                    intent.putExtra("endDate", endCalendarDate.getTimeInMillis());


                    startActivity(intent);
                }
            }
        }

    }
    /***
     * Method to initialize the Variables of this Activity
     * In addition, hide few elements that is not needed at first
     * and initialize listeners for Radio Groups
     *
     */
    public void initScreenFields() {
        //Initialize variables
        categoryHistoryGroup = findViewById(R.id.categoryHistoryGroup);
        visualizeByGroup = findViewById(R.id.visualizeByGroup);
        anthroGroupChoices = findViewById(R.id.anthroGroupChoices);
        pickDate = findViewById(R.id.pickDateButton);
        visualizeHistoryBy = findViewById(R.id.visualizeHistoryBy);
        submitButtonHistory =  findViewById(R.id.submitButtonHistory);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        weight = findViewById(R.id.Weight);
        height = findViewById(R.id.Height);
        blood = findViewById(R.id.BloodPres);
        waistCircu = findViewById(R.id.WaistCircumference);
        pulse = findViewById(R.id.Pulse);


        //hide few elements at first
        pickDate.setVisibility(View.GONE);
        visualizeHistoryBy.setVisibility(View.GONE);
        visualizeByGroup.setVisibility(View.GONE);
        submitButtonHistory.setVisibility(View.GONE);
        startDateTextView.setVisibility(View.GONE);
        endDateTextView.setVisibility(View.GONE);


        //set listeners for Radio Groups
        categoryHistoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //show hidden elements
                pickDate.setVisibility(View.VISIBLE);
                visualizeHistoryBy.setVisibility(View.VISIBLE);
                visualizeByGroup.setVisibility(View.VISIBLE);
                submitButtonHistory.setVisibility(View.VISIBLE);
                weight.setVisibility(View.GONE);
                height.setVisibility(View.GONE);
                blood.setVisibility(View.GONE);
                waistCircu.setVisibility(View.GONE);
                pulse.setVisibility(View.GONE);
            }
        });


    }

    /***
     * Date Range Picker: user chooses Start Date and End Date
     * @param view
     */
    public void datePickerOnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int yearFirstPicker = calendar.get(Calendar.YEAR);
        int monthFirstPicker = calendar.get(Calendar.MONTH);
        int dayOnMonthFirstPicker = calendar.get(Calendar.DAY_OF_MONTH);
        Toast.makeText(UserHistoryScreen.this, "Please select Start Date", Toast.LENGTH_LONG).show();

        datePickerFirstPickerDialog = new DatePickerDialog(UserHistoryScreen.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedStartYear = year;
                        selectedStartMonth = month + 1;
                        selectedStartDay = dayOfMonth;

                        firstDateString = selectedStartDay + "/" + selectedStartMonth + "/" + selectedStartYear;

                        //set start calendar date which will be sent to the next activity
                        startCalendarDate = Calendar.getInstance();
                        selectedStartMonth--;
                        startCalendarDate.set(selectedStartYear,selectedStartMonth,selectedStartDay);

                        Toast.makeText(UserHistoryScreen.this, "First Date: " + firstDateString + "\nNow Select 'End Date'", Toast.LENGTH_LONG).show();

                        startDateTextView.setVisibility(View.VISIBLE);

                        startDateTextView.setText("Start Date = " + firstDateString);
                        getSecondDate();
                    }
                }, yearFirstPicker, monthFirstPicker, dayOnMonthFirstPicker);
        datePickerFirstPickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerFirstPickerDialog.show();


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MedicalCardScreen.class));
    }

    public void getSecondDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedStartYear,selectedStartMonth,selectedStartDay);
        datePickerSecondPickerDialog = new DatePickerDialog(UserHistoryScreen.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedEndYear = year;
                        selectedEndMonth = month + 1;
                        selectedEndDay = dayOfMonth;

                        secondDateString = selectedEndDay + "/" + selectedEndMonth + "/" + selectedEndYear;

                        //set end calendar date which will be sent to the next activity
                        endCalendarDate = Calendar.getInstance();
                        selectedEndMonth--;
                        endCalendarDate.set(selectedEndYear,selectedEndMonth,selectedEndDay);

                        endDateTextView.setVisibility(View.VISIBLE);
                        endDateTextView.setText("End Date = " + secondDateString);
                        Toast.makeText(UserHistoryScreen.this, "Second Date: " + secondDateString, Toast.LENGTH_LONG).show();

                    }
                }, selectedStartYear, selectedStartMonth, selectedStartDay);

        datePickerSecondPickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerSecondPickerDialog.show();


        //yyyy-MM-dd HH:mm:ss.SSS

    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_screen);
        setTitle("History Information");
        initScreenFields();
    }
}