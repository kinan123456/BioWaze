package com.example.myapplication.UserHistory;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class GraphHistoryView extends AppCompatActivity {




    public void getDataFromCloud() {
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery(receivedListName);
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    displayDataOnGraph(list);   //display the data in table view

                } else {
                    Toast.makeText(GraphHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void displayDataOnGraph(List list){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_history_view);
        setTitle("History Graph View");

        GetDataFromCloud();
    }
}
