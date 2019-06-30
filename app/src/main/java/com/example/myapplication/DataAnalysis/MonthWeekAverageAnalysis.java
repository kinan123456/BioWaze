package com.example.myapplication.DataAnalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.myapplication.DataAnalysis.DataAnalysisActivity.DBFeeLingList;
import static com.example.myapplication.DataAnalysis.DataAnalysisActivity.DBFoodList;

public class MonthWeekAverageAnalysis extends AppCompatActivity {

    private HashSet<String> foodKinds = new HashSet<>();
    private HashSet<String> feelingKinds = new HashSet<>();
    private final ArrayList<String> foodKindsList = new ArrayList<>();
    private final ArrayList<String> feelingsKindsList = new ArrayList<>();
    private RadioGroup categoryToAnalyzeGroup;
    private String[] items;
    private ListView listView;
    private boolean flag;
    private void initListView() {

        if (flag) {
            final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, feelingsKindsList);
            listView.setAdapter(arrayAdapter);
        } else {
            final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, foodKindsList);
            listView.setAdapter(arrayAdapter);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_week_average_analysis);
        setTitle("Average of X in 1 month");
        categoryToAnalyzeGroup = findViewById(R.id.categoryToAnalyzeGroup);
        listView = findViewById(R.id.listViewTypes);
        listView.setVisibility(View.GONE);
        items = new String[256];
        setListener();
        initSets();
        initLists();
    }

    private void initLists() {
        for (String s : foodKinds) {
            foodKindsList.add(s);
        }
        for (String s : feelingKinds) {
            feelingsKindsList.add(s);
        }
    }

    private void setListener() {

        categoryToAnalyzeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String x = ((RadioButton)findViewById(checkedId)).getText().toString();
                listView.setVisibility(View.VISIBLE);
                if (x.equals("Feelings")) {
                    flag = true;
                    initListView();
                } else {
                    flag = false;
                    initListView();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    //feelings list view
                    calcAvg(feelingsKindsList.get(position));
                } else {
                    //food list view
                    calcAvg(foodKindsList.get(position));
                }

            }
        });
    }

    private void initSets() {
        for (Map.Entry<String, HashSet<String>> entryFood : DBFoodList.entrySet()) {
            for (String food : entryFood.getValue()) {
                foodKinds.add(food);
            }
        }

        for (Map.Entry<String, HashSet<String>> entryFeeling : DBFeeLingList.entrySet()) {
            for (String feeling : entryFeeling.getValue()) {
                feelingKinds.add(feeling);
            }
        }
    }
    private void calcAvg(String type) {
        int counter = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar startMonthCal = Calendar.getInstance();
        Calendar todayCal = Calendar.getInstance();
        startMonthCal.set(startMonthCal.getTime().getYear(), startMonthCal.getTime().getMonth(), 1);
        if (flag) {
            //feelings
            for (Map.Entry<String, HashSet<String>> entryFeeling : DBFeeLingList.entrySet()) {
                if (entryFeeling.getValue().contains(type)) {
                    String feelingDateString = entryFeeling.getKey();
                    try {
                        Date feelingDate = simpleDateFormat.parse(feelingDateString);
                        if (feelingDate.after(startMonthCal.getTime()) && feelingDate.before(todayCal.getTime())) {
                            counter++;
                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //food
            for (Map.Entry<String, HashSet<String>> entryFood : DBFoodList.entrySet()) {
                if (entryFood.getValue().contains(type)) {
                    String foodDateString = entryFood.getKey();
                    try {
                        Date foodDate = simpleDateFormat.parse(foodDateString);
                        if (foodDate.after(startMonthCal.getTime()) && foodDate.before(todayCal.getTime())) {
                            counter++;
                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (flag) {
            //feelings
            Toast.makeText(MonthWeekAverageAnalysis.this, "Based on your Stored data:\nYou feel " + type + " in average each " + (30 / counter) + " day.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MonthWeekAverageAnalysis.this, "Based on your Stored Data:\nYou eat " + type + " in average each " + (30 / counter) + " day.", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * back button pressed, go back to previous screen
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DataAnalysisActivity.class));
    }
}
