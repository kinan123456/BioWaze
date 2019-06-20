/***
 * @author Kinan & Luzeen
 * DataAnalysisActivity is to analyze the lifestyle input data
 * To be more specific, it analyzes and relationship between food and feelings
 */

package com.example.myapplication.DataAnalysis;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataAnalysisActivity extends AppCompatActivity {

    /***
     * Variables Deceleration
     */
    private HashMap<String, HashSet<String>> DBFeeLingList, DBFoodList;
    private HashSet<String> allDates, allFoodKinds, allFeelingKinds;
    ProgressBar progressBar;


    /***
     *  Method to analyze lifestyle input data
     *  first we query the Database to get all food kinds and all feelings kinds for each input
     *  with its date then we save them, after that we send them to server to analyze the data
     * @param view
     */
    public void startAnalyzeClick(View view) {
        getDataToAnalyze("FeelingsHistory", "feeling");
        allDates.clear();
        getDataToAnalyze("FoodHistory", "whatEat");


        Handler handler = new Handler();
        progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override

            public void run() {
                String s = "";
                Iterator<Map.Entry<String, HashSet<String>>> it;

                it = DBFeeLingList.entrySet().iterator();   //to remove empty values from map
                while (it.hasNext()) {
                    Map.Entry<String, HashSet<String>> e = it.next();
                    HashSet<String> value = e.getValue();
                    if (value.isEmpty() || e.getKey().isEmpty()) {
                        it.remove();
                    }
                }

                it = DBFoodList.entrySet().iterator();  //to remove empty values from map
                while (it.hasNext()) {
                    Map.Entry<String, HashSet<String>> e = it.next();
                    HashSet<String> value = e.getValue();
                    if (value.isEmpty() || e.getKey().isEmpty()) {
                        it.remove();
                    }
                }
                s += "Food:\n";
                for (Map.Entry e : DBFoodList.entrySet()) {
                    s += "key: " + e.getKey() + " value: " + e.getValue() + "\n";
                }


                s += "Feelings:\n";
                for (Map.Entry e : DBFeeLingList.entrySet()) {
                    s += "key: " + e.getKey() + " value: " + e.getValue() + "\n";
                }

                Toast.makeText(DataAnalysisActivity.this, s + "\n feelings size = " + DBFeeLingList.size(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        },4000);



    }

    /***
     * Query from Parse Dashboard to get all food kinds & feelings kinds input for a User
     * @param tableName
     * @param colName
     */
    public void getDataToAnalyze(final String tableName, final String colName) {

        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery(tableName); //query from table FoodHistory or FeelingsHistory
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        if (colName.equals("whatEat"))
            query.orderByAscending("createdAt");
        else
            query.orderByAscending("startedDate");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    //save all dates in a list
                    Date date;

                    for (ParseObject obj : list) {
                        if (colName.equals("whatEat")) { //foodHistory table
                            date = obj.getCreatedAt();

                        } else {    //feelingsHistory table
                            date = obj.getDate("startedDate");
                            date.setDate(date.getDate()-1);
                        }

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateCreatedText = formatter.format(date);

                        allDates.add(dateCreatedText);  //save all dates in a list
                    }

                    //scan this list: for each date get all food/feelings kinds from 'list' we got we DB
                    for (String dateString : allDates) {
                        HashSet<String> kinds = new HashSet<>();
                        Date d;
                        for (ParseObject obj : list) {
                            if (colName.equals("whatEat"))  //foodHistory
                            {
                                d = obj.getCreatedAt();
                            } else {
                                d = obj.getDate("startedDate");
                                //d.setDate(d.getDate()-1);
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dd = formatter.format(d);
                            if (dateString.equals(dd)) {
                                kinds.add(obj.getString(colName));  //add 'food kind' or 'feeling kind' to set
                                //distinct values
                                if (colName.equals("whatEat")) {
                                    allFoodKinds.add(obj.getString(colName));
                                } else {
                                    allFeelingKinds.add(obj.getString(colName));
                                }
                            }
                        }
                        if (colName.equals("whatEat"))  //foodHistory
                        {
                            DBFoodList.put(dateString, kinds);
                        } else {
                            DBFeeLingList.put(dateString, kinds);
                        }
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
        DBFeeLingList = new HashMap<>();
        DBFoodList = new HashMap<>();
        allDates = new HashSet<>();
        allFeelingKinds = new HashSet<>();
        allFoodKinds = new HashSet<>();
    }

    /***
     * Start method: initialize variables of this class and setTitle for this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_data_analysis);
        progressBar = findViewById(R.id.progressBarDataAnalysis);
        progressBar.setVisibility(View.GONE);
        setTitle("Data Analysis");
        initializeVariables();
    }

}





