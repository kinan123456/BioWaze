/***
 * @author Kinan & Luzeen
 * UserHistoryScreen is accessed after clicking 'My History' button from MedicalCardScreen
 * Here we visualize the lifestyle history that the user has for selected time for period
 * from specific category. He can choose to display his data by a Graph View or a Table View
 */

package com.example.myapplication.UserHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class UserHistoryScreen extends AppCompatActivity {
    private RadioGroup categoryHistoryGroup, visualizeByGroup;
    private TextView timePeriodHistoryLabel, visualizeHistoryBy;
    private Button submitButtonHistory;


    /***
     * TODO: Time period : XML GUI , Handle selected dates when click Submit Button
     */
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
     * Checkt the display choice (either graph view or table view)
     * then open a new screen for data visualization
     * @param selectedCategoryText
     */
    public void checkDisplayChoice(String selectedCategoryText) {
        int selectedDisplay = visualizeByGroup.getCheckedRadioButtonId();

        if (selectedDisplay == -1) {
            Toast.makeText(UserHistoryScreen.this, "One or more fields are missing. Fill in everything.", Toast.LENGTH_LONG).show();
        } else {
            RadioButton selectedDisplayRadioButton = (RadioButton) findViewById(selectedDisplay);
            String selectedDisplayText = selectedDisplayRadioButton.getText().toString();
            //if display by is Graph option
            if (selectedDisplayText.startsWith("Graph")) {
                Toast.makeText(UserHistoryScreen.this, "Not available yet", Toast.LENGTH_LONG).show();

            } else {
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
        categoryHistoryGroup = (RadioGroup) findViewById(R.id.categoryHistoryGroup);
        visualizeByGroup = (RadioGroup) findViewById(R.id.visualizeByGroup);
        timePeriodHistoryLabel = (TextView) findViewById(R.id.timePeriodHistoryLabel);
        visualizeHistoryBy = (TextView) findViewById(R.id.visualizeHistoryBy);
        submitButtonHistory = (Button) findViewById(R.id.submitButtonHistory);

        //hide few elements at first
        timePeriodHistoryLabel.setVisibility(View.GONE);
        visualizeHistoryBy.setVisibility(View.GONE);
        visualizeByGroup.setVisibility(View.GONE);
        submitButtonHistory.setVisibility(View.GONE);
        //set listeners for Radio Groups
        categoryHistoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //show hidden elements
                timePeriodHistoryLabel.setVisibility(View.VISIBLE);
                visualizeHistoryBy.setVisibility(View.VISIBLE);
                visualizeByGroup.setVisibility(View.VISIBLE);
                submitButtonHistory.setVisibility(View.VISIBLE);
            }
        });

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
