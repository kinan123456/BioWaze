package com.example.myapplication.UserHistory;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphHistoryView extends AppCompatActivity {


    private String receivedListName;
    private Map mapList = new HashMap<>();
    private LineGraphSeries<DataPoint> series;

    public void getDataFromCloud() {
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery("AnthropometricData");
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    int l;
                    Date date;
                    for (ParseObject obj : list){
                        l = obj.getInt(receivedListName);
                        Double d = Double.valueOf(l);
                        date = obj.getCreatedAt();
                        mapList.put(date,d);
                    }
                    displayDataOnGraph(mapList);   //display the data in graph view

                } else {
                    Toast.makeText(GraphHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void displayDataOnGraph(Map list){
        int size = list.size();
        double y;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        Set key;
        key = list.keySet();

        for (Object s : key) {
            y = (double) list.get(s);

            series.appendData(new DataPoint((Date) s,y),true,size);
        }

        graph.addSeries(series);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_history_view);
        setTitle("History Graph View");

        Intent intent = getIntent();
        receivedListName = intent.getStringExtra("selectedAnthroData");
        Toast.makeText(GraphHistoryView.this, receivedListName, Toast.LENGTH_LONG).show();

        getDataFromCloud();
    }
}
