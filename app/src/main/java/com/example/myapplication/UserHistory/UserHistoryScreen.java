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

import com.example.myapplication.R;

import java.util.Calendar;

public class UserHistoryScreen extends AppCompatActivity {

    private RadioButton weight ,height,blood,pulse,waistCircu;
    private RadioGroup categoryHistoryGroup, visualizeByGroup, anthroGroupChoices;
    private TextView visualizeHistoryBy, startDateTextView, endDateTextView;
    private Button pickDate, submitButtonHistory;
    private int selectedStartYear, selectedStartMonth, selectedStartDay;
    private int selectedEndYear, selectedEndMonth, selectedEndDay;
    private DatePickerDialog datePickerFirstPickerDialog, datePickerSecondPickerDialog;
    /***
     * TODO: Time period : XML GUI , Handle selected dates when click Submit Button
     */
    /***
     * Submit button has been pressed - handle the given info
     * Get the category selected: 'Feelings history' , 'Food history' , etc...
     * then check the display choice: graph or table view
     * @param view
     */
        //clicked anthropometric data --> show weight , height , .....
    public void AnthropoButtonClicked(View view) {

        weight.setVisibility(View.VISIBLE);
        height.setVisibility(View.VISIBLE);
        blood.setVisibility(View.VISIBLE);
        waistCircu.setVisibility(View.VISIBLE);
        pulse.setVisibility(View.VISIBLE);
    }


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

        if (selectedDisplay == -1) {
            Toast.makeText(UserHistoryScreen.this, "One or more fields are missing. Fill in everything.", Toast.LENGTH_LONG).show();
        } else {

            RadioButton selectedDisplayRadioButton = findViewById(selectedDisplay);
            String selectedDisplayText = selectedDisplayRadioButton.getText().toString();
            //if display by is Graph option
            if (selectedDisplayText.startsWith("Graph")) {
                Intent intent = new Intent(getApplicationContext(), GraphHistoryView.class);
                intent.putExtra("selectedAnthroData", "weight");
                startActivity(intent);
            }  else{
                //if display by is Table View option
                Intent intent = new Intent(getApplicationContext(), TableHistoryView.class);
                intent.putExtra("listName", selectedCategoryText);
                startActivity(intent);
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
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        pickDate = findViewById(R.id.pickDateButton);
        visualizeHistoryBy = findViewById(R.id.visualizeHistoryBy);
        submitButtonHistory =  findViewById(R.id.submitButtonHistory);
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
                        selectedStartMonth = month;
                        selectedStartMonth++;
                        selectedStartDay = dayOfMonth;
                        String firstDateString = selectedStartDay + "/" + selectedStartMonth + "/" + selectedStartYear;
                        Toast.makeText(UserHistoryScreen.this, "First Date: " + firstDateString + "\nNow Select 'End Date'", Toast.LENGTH_LONG).show();
                        startDateTextView.setText(firstDateString);
                        getSecondDate();
                    }
                }, yearFirstPicker, monthFirstPicker, dayOnMonthFirstPicker);
        datePickerFirstPickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerFirstPickerDialog.show();

    }

    public void getSecondDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedStartYear,selectedStartMonth,selectedStartDay);
        datePickerSecondPickerDialog = new DatePickerDialog(UserHistoryScreen.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedEndYear = year;
                        selectedEndMonth = month;
                        selectedEndMonth++;
                        selectedEndDay = dayOfMonth;
                        String secondDateString = selectedEndDay + "/" + selectedEndMonth + "/" + selectedEndYear;
                        Toast.makeText(UserHistoryScreen.this, "Second Date: " + secondDateString, Toast.LENGTH_LONG).show();
                        endDateTextView.setText(secondDateString);

                    }
                }, selectedStartYear, selectedStartMonth, selectedStartDay);

        datePickerSecondPickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerSecondPickerDialog.show();
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
