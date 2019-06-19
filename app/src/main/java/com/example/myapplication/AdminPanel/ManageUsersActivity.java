/***
 * @author Kinan & Luzeen
 * This Activity is for the Administrator only
 * He can Manage the Cloud Databse Users Table
 * Either he deletes specific user or can add a new User to the database
 */

package com.example.myapplication.AdminPanel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    /***
     * Variables Initialization
     */
    private TableLayout usersTable;
    private TextView adminNameLabel;
    /**
     * Set row header of the TableLayout
     */
    public void initTableHeaderRow() {

        /*Button addUser = new Button(this);
        addUser.setText("Add New User");
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ManageUsersActivity.this);
                final EditText edittext = new EditText(ManageUsersActivity.this);
                alert.setMessage("Enter Your Message");
                alert.setTitle("Enter Your Title");

                alert.setView(edittext);

                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        Editable YouEditTextValue = edittext.getText();

                    }
                });

                alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });
        usersTable.addView(addUser);*/


        TableRow tr_head = new TableRow(this);
        //tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLUE);
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


        usersTable.addView(tr_head, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Put content on TableLayout
     * @param list
     */
    public void putOnTable(List<ParseUser> list) {
        if (list.isEmpty()) {
            Toast.makeText(ManageUsersActivity.this, "No users registered" + "\n" +
                    "You can add users by yourself: Click 'Add User'.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),AdminHomeActivity.class));
        } else {
            //addUserButton();
            initTableHeaderRow();
            setTableRows(list);   //Set Table Content in rows
        }
    }

    /**
     * Set all users on the TableLayout
     * @param list
     */
    public void setTableRows(List<ParseUser> list) {
        Integer count=0;
        for (final ParseUser obj : list) {

            // Create the table row
            final TableRow tr = new TableRow(this);

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

            final TextView name = new TextView(this);
            name.setId(200 + count);
            name.setText(obj.getString("name"));
            name.setTextColor(Color.WHITE);
            tr.addView(name);

            Button delete = new Button(this);
            delete.setText("Delete");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*HashMap<String,String> params = new HashMap<>();
                    params.put("objectId", ParseUser.getCurrentUser().getObjectId());
                    ParseCloud.callFunctionInBackground("deleteUserWithId", params, new FunctionCallback<Object>() {
                        @Override
                        public void done(Object object, ParseException e) {
                            if (e == null) {
                                //success
                            } else {
                                Toast.makeText(ManageUsersActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });*/
                    ParseUser user = ParseUser.getCurrentUser();
                    ParseRelation<ParseObject> relation = user.getRelation("FoodHistory");

                    usersTable.removeView(tr);
                    /***
                     * @TODO delete user: delete all relations for deleted user: e.g. delete rows of other tables that user made in past
                     */
                }
            });
            tr.addView(delete);
            // finally add this to the table row
            usersTable.addView(tr);
            count++;
        }
    }

    /**
     * Query from Cloud Database to get all the registered users
     */
    public void getDatabaseContent() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
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


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        setTitle("Manage Users");
        usersTable = findViewById(R.id.manageUsersTable);
        adminNameLabel = findViewById(R.id.adminLabelManageUsers);
        adminNameLabel.setText("You're logged in as: "+ ParseUser.getCurrentUser().getUsername() + "\n" +
                "All registered users are shown in the Table below.");
        getDatabaseContent();

    }
}
