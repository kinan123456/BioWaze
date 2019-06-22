
package com.example.myapplication.UserHistory;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphHistoryView extends AppCompatActivity {


    private String receivedListName;
    private ArrayList<Date> tempDatesList = new ArrayList<>();
    private ArrayList<Integer> tempIntList = new ArrayList<>();
    private LineGraphSeries<DataPoint> series;
    private Date startDate, endDate;

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
                    if(list.isEmpty()){
                        Toast.makeText(GraphHistoryView.this, "There's not enough data for your selection." + "\n" +
                                "Please choose other category and try-again!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), UserHistoryScreen.class));

                    }
                    else {
                        int l;
                        Date date;
                        for (ParseObject obj : list) {
                            l = obj.getInt(receivedListName);
                            tempIntList.add(l);
                            date = obj.getCreatedAt();
                            tempDatesList.add(date);
                        }
                        displayDataOnGraph(tempDatesList, tempIntList);   //display the data in graph view
                    }
                } else {
                    Toast.makeText(GraphHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void displayDataOnGraph(ArrayList listOfDates, ArrayList listOfInts) {
        int size = listOfInts.size();
        int y;
        Date x;
        String x1;

            GraphView graph = (GraphView) findViewById(R.id.graph);
            series = new LineGraphSeries<>();
            final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            for (int i = 0; i < size; i++) {
                x = (Date) listOfDates.get(i);
                y = (int) listOfInts.get(i);

                series.appendData(new DataPoint(x, y), true, size);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_history_view);
        setTitle("History Graph View");

        Intent intent = getIntent();
        receivedListName = intent.getStringExtra("selectedAnthroData");
        startDate = new Date(intent.getLongExtra("startDate",-1));
        endDate = new Date(intent.getLongExtra("endDate",-1));
        Toast.makeText(GraphHistoryView.this, receivedListName, Toast.LENGTH_LONG).show();
        getDataFromCloud();
    }
}
