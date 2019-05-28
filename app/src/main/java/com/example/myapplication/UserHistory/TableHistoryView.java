/***
 * @author Kinan & Luzeen
 * TableHistoryView Activity is for data visualization
 * We get the selected choice from the previous activity (UserHistoryScreen) and handle it in
 * this intent. We display the data in a TableLayout Android resource
 */

package com.example.myapplication.UserHistory;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableHistoryView extends AppCompatActivity {

    /***
     * Variables Initialization
     */
    private String receivedListName, lifestyleActivityName;
    private TableLayout tableLayout;
    private ProgressBar progressBar;
    /**
     * Query from Parse Dashboard to get all rows for specific user
     * This is his Lifestyle history
     */
    public void getDataFromCloud() {
        ParseQuery<ParseObject> query;
        query = ParseQuery.getQuery(receivedListName);
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    displayDataOnTable(list);   //display the data in table view

                } else {
                    Toast.makeText(TableHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Refresh TableLayout after each change we make
     */
    public void refreshTable() {
        tableLayout.invalidate();
        tableLayout.refreshDrawableState();
    }

    /**
     * @param list
     * Put the List that we got from Database Cloud in the TableLayout
     */
    public void setTableRows(List<ParseObject> list) {
        ArrayList<String> databaseColumns = new ArrayList<>();
        ArrayList<String> commonColumns = new ArrayList<>();
        //depends on the lifeStyleActivity name we query from Cloud
        switch(lifestyleActivityName) {
            case "Food":
                databaseColumns.clear();
                databaseColumns.add("whatEat");
                databaseColumns.add("whyEat");
                databaseColumns.add("howWasEat");
                databaseColumns.add("whereEat");
                databaseColumns.add("feelAfterEat");
                databaseColumns.add("noteEat");
                databaseColumns.add("image");
                break;
            case "Feelings":
                databaseColumns.clear();
                databaseColumns.add("bodyPart");
                databaseColumns.add("feeling");
                databaseColumns.add("startedDate");
                databaseColumns.add("stoppedDate");
                databaseColumns.add("possibleReason");

                break;
            default:
                break;
        }

        //scan the list of contents that we got from Cloud and insert into our tableLayout
        for (ParseObject obj : list) {
            final TableRow tableRow = new TableRow(this); // Create a new table row.
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            Date date = obj.getCreatedAt();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateCreatedText = formatter.format(date);
            commonColumns.add(ParseUser.getCurrentUser().getString("name"));
            commonColumns.add(lifestyleActivityName);
            commonColumns.add(dateCreatedText);

            //put common columns for all lifeStyleActivities
            for (String commonColumn : commonColumns) {
                TextView tcView = new TextView(this);
                tcView.setTextColor(Color.BLACK);
                tcView.setPadding(0,0,30,0);
                tcView.setText(commonColumn);

                tableRow.addView(tcView);   //add textView to the row
            }
            commonColumns.clear();

            //put appropriate columns (depends on lifeStyle activity name)
            for (String columnName : databaseColumns) {
                TextView tcView = new TextView(this);
                tcView.setTextColor(Color.BLACK);
                tcView.setPadding(0,0,30,0);
                switch(columnName) {
                    case "startedDate": //Feelings History column
                    case "stoppedDate": //Feelings History column
                        Date startStopDate = obj.getDate(columnName);
                        String toPut = formatter.format(startStopDate);
                        tcView.setText(toPut);
                        break;

                    case "image":   //Food History column
                        ParseFile imageFile = (ParseFile) obj.get("image");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    final ImageView imageButton = new ImageView(TableHistoryView.this);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    imageButton.setImageBitmap(bitmap);
                                    imageButton.setPadding(0,0,0,0);
                                    imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                                    tableRow.addView(imageButton,300, 350);
                                } else {
                                    Toast.makeText(TableHistoryView.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new ProgressCallback() {
                            public void done(Integer percentDone) {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(percentDone);
                            }
                        });
                        break;

                    default:
                        tcView.setText(obj.getString(columnName));
                        break;
                }

                tableRow.addView(tcView);   //add textView to the row

            }

            tableLayout.addView(tableRow);
            //refreshTable(); //refresh table
        }

    }

    /***
     * @param list
     * Set the Table Row Header
     * Handle different row header.
     * For example: Food Table row header isn't equal to Feelings Table row header
     */
    public void setTableHeader(List<ParseObject> list) {
        TableRow tr = findViewById(R.id.table_row_header);  //Row Header

        String delString = "History";
        lifestyleActivityName = receivedListName.replace(delString, "");
        ArrayList<String> headerStrings = new ArrayList<String>();

        //table column names (row header)
        switch(lifestyleActivityName) {
            case "Food":
                headerStrings.clear();
                headerStrings.add("You Ate");
                headerStrings.add("Reason");
                headerStrings.add("It Was");
                headerStrings.add("Place");
                headerStrings.add("Felt After");
                headerStrings.add("Your Note");
                headerStrings.add("Photo");
                break;
            case "Feelings":
                headerStrings.clear();
                headerStrings.add("Body Part");
                headerStrings.add("Feeling");
                headerStrings.add("Started At");
                headerStrings.add("Stopped At");
                headerStrings.add("Possible Reason");
                break;
            default:
                break;
        }

        //put them in the table (add all TextViews to the first row that is the header)
        for (String header : headerStrings) {
            TextView tcView = new TextView(this);
            tcView.setPadding(0,0,50,0);
            tcView.setText(header);
            tcView.setTextColor(Color.BLACK);
            tr.addView(tcView);
        }

        refreshTable(); //refresh table after performing changes
    }

    /***
     * Display the data on the TableLayout
     * Set Header Row
     * Set all rows content (set the list we got from Cloud database)
     */
    public void displayDataOnTable(List<ParseObject> list) {
        if (list.isEmpty()) {
            Toast.makeText(TableHistoryView.this, "There's not enough data for your selection." + "\n" +
                    "Please choose other category and try-again!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), UserHistoryScreen.class));
        } else {
            tableLayout = findViewById(R.id.table_history_layout);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            setTableHeader(list);   //Set Table Header
            setTableRows(list);   //Set Table Content in rows
        }

    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_history_view);
        setTitle("History Table View");

        Intent intent = getIntent();        // Get the transferred data from source activity.
        receivedListName = intent.getStringExtra("listName");
        getDataFromCloud();
    }
}
