
package com.example.myapplication.UserHistory;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphHistoryView extends AppCompatActivity {

    private String receivedListName;
    private ArrayList<Date> tempDatesList;
    private ArrayList<Double> tempIntList;
    private LineGraphSeries<DataPoint> series;
    private Date startDate, endDate;
    private GraphView graph;

    public void getDataFromCloud() {
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery("AnthropometricData");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.whereGreaterThanOrEqualTo("createdAt", startDate);
        query.whereLessThanOrEqualTo("createdAt",endDate);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if(list.isEmpty() || list == null || list.size() <= 0){
                        Toast.makeText(GraphHistoryView.this, "There's not enough data for your selection." + "\n" +
                                "Please choose other category and try-again!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), UserHistoryScreen.class));

                    }
                    else {
                        for (ParseObject obj : list) {
                            tempIntList.add(obj.getDouble(receivedListName));
                            tempDatesList.add(obj.getCreatedAt());
                        }

                        displayDataOnGraph();   //display the data in graph view
                    }
                } else {
                    Toast.makeText(GraphHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void displayDataOnGraph() {
        int size = tempIntList.size();
        double y;
        Date x;

        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        for (int i = 0; i < size; i++) {
            x = tempDatesList.get(i);
            y = tempIntList.get(i);
            series.appendData(new DataPoint(x, y), true, size,true);
        }

        graph.addSeries(series);
        // display Date format on x axis
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else
                    return super.formatLabel(value, isValueX);
            }
        });
    }


    private void initVariables() {
        tempIntList = new ArrayList<>();
        tempDatesList = new ArrayList<>();
        series = new LineGraphSeries<>();
        graph = findViewById(R.id.graph);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserHistoryScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_history_view);
        setTitle("History Graph View");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        receivedListName = intent.getStringExtra("selectedAnthroData");
        startDate = new Date(intent.getLongExtra("startDate",-1));
        endDate = new Date(intent.getLongExtra("endDate",-1));
        initVariables();

        getDataFromCloud();
    }

}
