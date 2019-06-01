package com.example.myapplication.AdminPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.UserHistory.TableHistoryView;
import com.example.myapplication.UserHistory.UserHistoryScreen;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private TableLayout usersTable;


    public void initTableHeaderRow() {
        TableRow tr_head = new TableRow(this);
        //tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView usernameHeader = new TextView(this);
        //username.setId(20);
        usernameHeader.setText("Username");
        usernameHeader.setTextColor(Color.WHITE);

        usernameHeader.setPadding(5, 5, 5, 5);
        tr_head.addView(usernameHeader);// add the column to the table row here

        TextView nameHeader = new TextView(this);
        //name.setId(21);// define id that must be unique
        nameHeader.setText("Name"); // set the text for the header
        nameHeader.setTextColor(Color.WHITE); // set the color
        nameHeader.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(nameHeader); // add the column to the table row here

        TextView passwordHeader = new TextView(this);
        //name.setId(21);// define id that must be unique
        passwordHeader.setText("Password"); // set the text for the header
        passwordHeader.setTextColor(Color.WHITE); // set the color
        passwordHeader.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(passwordHeader); // add the column to the table row here

        usersTable.addView(tr_head, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
    }

    public void putOnTable(List<ParseUser> list) {
        if (list.isEmpty()) {
            Toast.makeText(ManageUsersActivity.this, "No users registered" + "\n" +
                    "You can add users by yourself: Click 'Add User'.", Toast.LENGTH_LONG).show();
        } else {
            initTableHeaderRow();
            setTableRows(list);   //Set Table Content in rows
        }
    }

    public void setTableRows(List<ParseUser> list) {
        Integer count=0;
        for (ParseObject obj : list) {

            // Create the table row
            TableRow tr = new TableRow(this);
            if (count % 2 != 0)
                tr.setBackgroundColor(Color.GRAY);
            else
                tr.setBackgroundColor(Color.BLACK);
            //tr.setId(100 + count);

            //Create two columns to add as table data
            // Create a TextView to add date
            TextView username = new TextView(this);
            username.setId(200 + count);
            username.setText(obj.getString("username"));
            username.setPadding(2, 0, 5, 0);
            username.setTextColor(Color.WHITE);
            tr.addView(username);

            TextView name = new TextView(this);
            name.setId(200 + count);
            name.setText(obj.getString("name"));
            name.setTextColor(Color.WHITE);
            tr.addView(name);

            TextView password = new TextView(this);
            password.setId(300 + count);
            password.setText(obj.getString("password"));
            password.setTextColor(Color.WHITE);
            tr.addView(password);

            // finally add this to the table row
            usersTable.addView(tr);
            count++;
        }
    }

    public void getDatabaseContent() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    putOnTable(list);   //display the data in table view

                } else {
                    Toast.makeText(ManageUsersActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        setTitle("Manage Users");
        usersTable = findViewById(R.id.manageUsersTable);
        getDatabaseContent();

    }
}
