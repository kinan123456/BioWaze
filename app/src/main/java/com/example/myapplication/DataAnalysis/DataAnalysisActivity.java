package com.example.myapplication.DataAnalysis;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.collections.MultiMap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;

public class DataAnalysisActivity extends AppCompatActivity {

    private ArrayList<WhenWhatEat> whenWhatEatArrayList;
    private HashMap<String, HashSet<String>> foodKindsWithDates;

    /***
     *
     * @param view
     */
    public void startAnalyzeClick(View view) {
        getFoodKindsDataFromCloud("FoodHistory");
    }

    /**
     * Query from Parse Dashboard to get all food kinds for a User
     */
    public void getFoodKindsDataFromCloud(String tableName) {
        ParseQuery<ParseObject> query;
        ArrayList<String> columnsKeys = new ArrayList<>();
        final HashSet<String> allDates = new HashSet<>();
        columnsKeys.add("createdAt");
        columnsKeys.add("whatEat");
        query = ParseQuery.getQuery(tableName);
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.selectKeys(columnsKeys);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    //save all dates in a list
                    for (ParseObject obj : list) {
                        Date date = obj.getCreatedAt();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateCreatedText = formatter.format(date);
                        allDates.add(dateCreatedText);
                    }

                    //scan this list: for each date get all food kinds from 'list' we got we DB
                    for (String dateString : allDates) {
                        HashSet<String> foodKinds = new HashSet<>();
                        for (ParseObject obj : list) {
                            Date d = obj.getCreatedAt();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dd = formatter.format(d);
                            if (dateString.equals(dd)) {
                                foodKinds.add(obj.getString("whatEat"));
                            }
                        }
                        foodKindsWithDates.put(dateString, foodKinds);
                        foodKinds.clear();
                    }



                } else {
                    Toast.makeText(DataAnalysisActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /***
     * Method to initialize all variables that are in this class
     */
    public void initializeVariables() {
        foodKindsWithDates = new HashMap<>();
        whenWhatEatArrayList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data_analysis);
        initializeVariables();
    }

    private class WhenWhatEat {
        private Date whenEat;
        private HashSet<String> whatEat;

        public WhenWhatEat() {
            whenEat = new Date();
            whatEat = new HashSet<>();
        }

        public void setWhenEat(Date whenEat) {
            this.whenEat = whenEat;
        }

        public Date getWhenEat() {
            return this.whenEat;
        }

        public void setWhatEat(HashSet<String> whatEat) {

        }
    }
}
