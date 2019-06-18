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
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;
import com.parse.GetDataCallback;

import java.util.ArrayList;
import java.util.List;

public class GraphHistoryView extends AppCompatActivity {


    private String receivedListName;
    private List<Integer> tempList1 = new ArrayList<>();
    private List<Integer> tempList2 = new ArrayList<>();
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
                    tempList1.clear();
                    tempList2.clear();
                    for (ParseObject obj : list){
                        l = obj.getInt(receivedListName);
                        tempList1.add(l);
                }
                    displayDataOnGraph(tempList1);   //display the data in graph view

                } else {
                    Toast.makeText(GraphHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void displayDataOnGraph(List list){
        int size = list.size();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();

        for(int i=0 ; i<size; i++){

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_history_view);
        setTitle("History Graph View");

        Intent intent = getIntent();
        receivedListName = intent.getStringExtra("listName");
        getDataFromCloud();
    }
}
