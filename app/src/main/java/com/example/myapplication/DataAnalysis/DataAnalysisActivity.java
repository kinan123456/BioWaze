/***
 * @author Kinan & Luzeen
 * DataAnalysisActivity is to analyze the lifestyle input data
 * To be more specific, it analyzes and relationship between food and feelings
 */

package com.example.myapplication.DataAnalysis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static HashMap<String, HashSet<String>> DBFeeLingList, DBFoodList;
    private HashSet<String> allDates, allFoodKinds, allFeelingKinds;
    private TextView otherLabel, dataAnalysisLabel;
    private ProgressBar progressBar, progressBar2;
    private ChiSquareParams chiSquareParams;
    private Spinner foodFeelingSpinner;
    private Button startAnalyzeButton, analyzeMoreButton;
    private String[] seperated;
    private ArrayList<String> listOfItems, spinnerItems;
    private ArrayList<CountFood> countFoodList;

    /***
     *  Method to analyze lifestyle input data
     *  first we query the Database to get all food kinds and all feelings kinds for each input
     *  with its date then we save them, after that we send them to server to analyze the data
     * @param view
     */
    public void startAnalyzeClick(View view) {

        Handler handler = new Handler();
        analyzeMoreButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override

            public void run() {

                String selectedRowString = (String) listOfItems.get(foodFeelingSpinner.getSelectedItemPosition());
                seperated = selectedRowString.split(",");
                chiSquareTest(seperated[0], seperated[1]);
                HashMap<String, Object> params = new HashMap<>();

                /***
                 * call the algorithm that is in the server (function called chiSquareTest)
                 * using ParseCloud codes. we send a map which contains all the n1-n4 parameters
                 * for the desired food&feelings variables and display results on screen
                 */
                params.put("n1", String.valueOf(chiSquareParams.n1));
                params.put("n2", String.valueOf(chiSquareParams.n2));
                params.put("n3", String.valueOf(chiSquareParams.n3));
                params.put("n4", String.valueOf(chiSquareParams.n4));
                Toast.makeText(DataAnalysisActivity.this, "," + chiSquareParams.n1 + "," +
                        chiSquareParams.n2 + "," + chiSquareParams.n3
                        + "," + chiSquareParams.n4, Toast.LENGTH_LONG).show();


                ParseCloud.callFunctionInBackground("chiSquareTest", params, new FunctionCallback<HashMap<String, String>>() {

                    @Override
                    public void done(HashMap<String, String> object, ParseException e) {
                        if (e == null) {
                            String answer = object.get("answer");
                            String depIndep = null;
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(answer);
                                depIndep = (String) jsonObject.get("firstresult");

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            Toast.makeText(DataAnalysisActivity.this, "\nFood: " + seperated[0] + ", Feeling: " + seperated[1]
                                    + "\nBased on your data they're " + depIndep, Toast.LENGTH_LONG).show();


                        }
                    }
                });
                params.clear();
                progressBar.setVisibility(View.GONE);
                analyzeMoreButton.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    public void analyzeMoreButtonClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MonthWeekAverageAnalysis.class);
        startActivity(intent);
    }

    /***
     * Initialize 4 parameters for the Hypothesis method that is in server
     * (represent the chi-square 2x2 matrix)
     */
    public void initializeParametersForChiSquare() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Map.Entry<String,HashSet<String>> entryFood : DBFoodList.entrySet()) {
            String foodEattenDateString =  entryFood.getKey();

            Date foodEattenDate = null;
            try {
                foodEattenDate = simpleDateFormat.parse(foodEattenDateString);
            } catch (java.text.ParseException e) {
                Toast.makeText(DataAnalysisActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            for (Map.Entry<String,HashSet<String>> entryFeelings : DBFeeLingList.entrySet()) {
                String feelingsSharedDateString = entryFeelings.getKey();
                Date feelingsSharedDate = null;
                try {
                    feelingsSharedDate = simpleDateFormat.parse(feelingsSharedDateString);
                } catch (java.text.ParseException e) {
                    Toast.makeText(DataAnalysisActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                if (feelingsSharedDate.after(foodEattenDate)) {
                    //we are willing to compare dates that: feeling date is after maximum 2 days than eatten date
                    long daysDiff = (feelingsSharedDate.getTime() - foodEattenDate.getTime()) / (24*60*60*1000);

                    if (daysDiff <= 2) {

                        //now we have to scan the values of these two keys and get 2 variables which we'll send to server to check
                        //if they're independent

                        for (String foodSetString : entryFood.getValue()) {
                            for (String feelingsSetString : entryFeelings.getValue()) {
                                String temp = foodSetString + "," + feelingsSetString;
                                String temp2 = foodSetString + " on " + foodEattenDateString + ", " + feelingsSetString + " on " + feelingsSharedDateString;
                                listOfItems.add(temp);
                                spinnerItems.add(temp2);
                            }
                        }
                    }
                }
            }
        }
    }

    /***
     * calc number of: food YES & feelings after-2-days YES which represents N1 parameter for chi-test
     * calc number of: food YES & feelings after-2-days NO which represents N2 parameter for chi-test
     * calc number of: food NO & feelings after-2-days YES which represents N3 parameter for chi-test
     * calc number of: food NO & feelings after-2-days NO which represents N4 parameter for chi-test
     *
     * @param foodSetString
     * @param feelingsSetString
     */
    public void chiSquareTest(String foodSetString, String feelingsSetString) {
        int n1, n2, n3, n4;
        n1 = n2 = n3 = n4 = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Map.Entry<String, HashSet<String>> entryFood : DBFoodList.entrySet()) {
            if (entryFood.getValue().contains(foodSetString)) { //if this row in map contains this food we're checking
                String dateOfThisFoodString = entryFood.getKey();  //get date when user eat this food
                Date dateOfThisFood;
                long daysDiff;
                try {
                    dateOfThisFood = simpleDateFormat.parse(dateOfThisFoodString);
                    for (Map.Entry<String, HashSet<String>> entryFeelings : DBFeeLingList.entrySet()) {
                        String feelingsDateString = entryFeelings.getKey();
                        Date feelingsDate = simpleDateFormat.parse(feelingsDateString);

                        if (feelingsDate.after(dateOfThisFood)) {
                            daysDiff = (feelingsDate.getTime() - dateOfThisFood.getTime()) / (24 * 60 * 60 * 1000);
                            //end if feelingDate after foodDate
                            if (daysDiff <= 2) {
                                if (entryFeelings.getValue().contains(feelingsSetString)) {
                                    n1++;//food YES & feelings after-2-days YES
                                }   //end if row has 'feelingString'
                                else {
                                    n2++;   //food YES & feelings after-2-days
                                }
                            }
                        }
                    }   //end foreach DBFeelingList

                } catch (java.text.ParseException e) {
                    Toast.makeText(DataAnalysisActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }   //end catch
            }   //end if row has 'foodString'

            else {  //row doesn't have 'foodString'
                String dateOfNotThisFoodString = entryFood.getKey();  //get date when user didn't eat this food
                Date dateOfNotThisFood;
                long daysDiff;
                try {
                    dateOfNotThisFood = simpleDateFormat.parse(dateOfNotThisFoodString);
                    for (Map.Entry<String, HashSet<String>> entryFeelings : DBFeeLingList.entrySet()) {
                        if (entryFeelings.getValue().contains(feelingsSetString)) {
                            String feelingsDateString = entryFeelings.getKey();
                            Date feelingsDate = simpleDateFormat.parse(feelingsDateString);
                            if (feelingsDate.after(dateOfNotThisFood)) {
                                daysDiff = (feelingsDate.getTime() - dateOfNotThisFood.getTime()) / (24 * 60 * 60 * 1000);
                                if (daysDiff <= 2) {
                                    if (entryFeelings.getValue().contains(feelingsSetString)) {
                                        n3++;   //food NO & feelings after-2-days YES
                                    }
                                    else {
                                        n4++;   //food NO & feelings after-2-days NO
                                    }   //end else
                                }

                            }   //end if feelingDate after foodDate
                        }   //end if row has 'feelingString'

                    }   //end foreach DBFeelingList
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }   //end else

        }//end foreach DBFoodList
        chiSquareParams = new ChiSquareParams(foodSetString, feelingsSetString, n1, n2, n3, n4);

    }   //end function

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
        listOfItems = new ArrayList<>();
        spinnerItems = new ArrayList<>();
        countFoodList = new ArrayList<>();
        foodFeelingSpinner = findViewById(R.id.foodFeelingSpinner);
        otherLabel = findViewById(R.id.otherLabel);
        startAnalyzeButton = findViewById(R.id.startAnalyzeButton);
        analyzeMoreButton = findViewById(R.id.analyzeMoreButton);
        dataAnalysisLabel = findViewById(R.id.dataAnalysisLabel);
        foodFeelingSpinner.setVisibility(View.GONE);
        otherLabel.setVisibility(View.GONE);
        startAnalyzeButton.setVisibility(View.GONE);
        analyzeMoreButton.setVisibility(View.GONE);

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
        progressBar2 = findViewById(R.id.progressBarDataAnalysis2);
        progressBar2.setVisibility(View.GONE);
        setTitle("Data Analysis");
        initializeVariables();
        initData();
    }

    private void initData() {
        getDataToAnalyze("FeelingsHistory", "feeling");
        allDates.clear();
        getDataToAnalyze("FoodHistory", "whatEat");

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


        Handler handler = new Handler();
        progressBar2.setVisibility(View.VISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeParametersForChiSquare();
                if (listOfItems.isEmpty() || listOfItems == null) {

                    Toast.makeText(DataAnalysisActivity.this, "No Food-Feelings pairs found for analyze.\n" , Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                    dataAnalysisLabel.setText("No Food-Feelings pairs found for analyze");
                    dataAnalysisLabel.setTextColor(Color.RED);
                    analyzeMoreButton.setVisibility(View.VISIBLE);
                } else {
                    otherLabel.setVisibility(View.VISIBLE);
                    foodFeelingSpinner.setVisibility(View.VISIBLE);
                    startAnalyzeButton.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.GONE);
                    analyzeMoreButton.setVisibility(View.VISIBLE);
                    dataAnalysisLabel.setText("Provided pairs are food that you ate on Date X and feeling you felt" +
                            " on maximum 2 days after");
                    dataAnalysisLabel.setTextColor(Color.RED);

                    initializeSpinnerFoodFeeling();
                }
            }
        }, 3000);
    }

    /**
     * Initialize spinner for 'select body part'
     */
    public void initializeSpinnerFoodFeeling() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodFeelingSpinner.setAdapter(adapter);
    }


    /**
     * class for chi-square params that contains 4 parameters (n1---n4)
     */
    private class ChiSquareParams {
        String food, feelings;
        int n1, n2, n3, n4;

        public ChiSquareParams(String food, String feelings, int n1, int n2, int n3, int n4) {
            this.food = food;
            this.feelings = feelings;
            this.n1 = n1;
            this.n2 = n2;
            this.n3 = n3;
            this.n4 = n4;
        }

        public String toString() {
            return "food: " + this.food + " feelings: " + this.feelings +
                    " \nn1: " + this.n1 + ", n2: " + this.n2 + ", n3: " + this.n3 + ", n4: " + this.n4 + "\n";
        }

    }

    private class CountFood {
        private int count;
        private String foodName;

        public CountFood(int count, String foodName) {
            this.count = count;
            this.foodName = foodName;
        }
    }

}