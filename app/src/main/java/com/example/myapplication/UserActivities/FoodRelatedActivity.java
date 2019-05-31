/***
 * @author Kinan & Luzeen
 * FoodRelatedActivity is an activity for users to share what they eat or drink
 * The application will gather the input data and store it in the Cloud Database
 */
package com.example.myapplication.UserActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FoodRelatedActivity extends AppCompatActivity {

    /**
     * Initialize Variables
     */
    private EditText noteContent, whatEattenContent;
    private ImageView foodImage;
    private RadioGroup whyEatRadioGroup,howWasRadioGroup,whereEatRadioGroup,makeYouFeelRadioGroup;
    private RadioButton whyEatRadioButtonSelected, howWasRadioButtonSelected, whereEatRadioButtonSelected,makeYouFeelRadioButtonSelected;
    private String whyEatRadioValue, howWasRadioValue, whereEatRadioValue, makeYouFeelRadioValue;
    private byte[] byteArray = null;
    private ProgressDialog progressDialog;

    /***
     *
     * @param view
     */
    public void shareFoodOnClick(View view) {
        String note_content_string = ((EditText)findViewById(R.id.NoteContent)).getText().toString();
        String what_eatten_string = ((EditText)findViewById(R.id.whatEattenContent)).getText().toString();

        if (whyEatRadioGroup.getCheckedRadioButtonId() == -1 ||
                howWasRadioGroup.getCheckedRadioButtonId() == -1 ||
                whereEatRadioGroup.getCheckedRadioButtonId() == -1 ||
                makeYouFeelRadioGroup.getCheckedRadioButtonId() == -1 ||
                note_content_string.matches("") || what_eatten_string.matches("")) {
            Toast.makeText(FoodRelatedActivity.this, "One or more missing fields. Try again.", Toast.LENGTH_LONG).show();

        } else {
            progressDialog=ProgressDialog.show(this,null,null,true,false);
            progressDialog.setContentView(new ProgressBar(this));
            ParseObject foodHistory = new ParseObject("FoodHistory");
            foodHistory.put("user", ParseUser.getCurrentUser().getUsername());
            // Add a relation between the FoodHistory row with user id
            foodHistory.put("parent", ParseObject.createWithoutData("User", ParseUser.getCurrentUser().getObjectId()));
            foodHistory.put("whatEat", whatEattenContent.getText().toString());
            foodHistory.put("noteEat", noteContent.getText().toString());
            foodHistory.put("whyEat", whyEatRadioValue);
            foodHistory.put("howWasEat", howWasRadioValue);
            foodHistory.put("whereEat", whereEatRadioValue);
            foodHistory.put("feelAfterEat", makeYouFeelRadioValue);
            //Image has been selected. save it too

            if (this.byteArray != null) {
                ParseFile file = new ParseFile("image.png", this.byteArray);
                file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                            }
                        }, new ProgressCallback() {
                            @Override
                            public void done(Integer percentDone) {
                                progressDialog.setProgress(percentDone);
                            }
                        }
                );
                foodHistory.put("image", file);
            }

            foodHistory.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(FoodRelatedActivity.this, "Successfully, your food-related data is shared.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), LifestyleActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(FoodRelatedActivity.this, "Error occurred. Please try again later", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }

    /**
     * Get photo from Android Gallery / Camera
     */
    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                this.byteArray = stream.toByteArray();
                foodImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Upload Picture clicked --- handle it -- ask permission
     * @param view
     */
    public void uploadFoodPicClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                getPhoto();
            }
        } else {
            getPhoto();
        }
    }

    /**
     * Set listeners (handlers) for each radio group defined
     */
    public void setListenersRadioGroups() {
        whyEatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                whyEatRadioButtonSelected = (RadioButton) findViewById(checkedId);
                whyEatRadioValue = whyEatRadioButtonSelected.getText().toString();
            }
        });
        howWasRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                howWasRadioButtonSelected = (RadioButton) findViewById(checkedId);
                howWasRadioValue = howWasRadioButtonSelected.getText().toString();
            }
        });
        whereEatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                whereEatRadioButtonSelected = (RadioButton) findViewById(checkedId);
                whereEatRadioValue = whereEatRadioButtonSelected.getText().toString();
            }
        });
        makeYouFeelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                makeYouFeelRadioButtonSelected = (RadioButton) findViewById(checkedId);
                makeYouFeelRadioValue = makeYouFeelRadioButtonSelected.getText().toString();
            }
        });
    }

    /**
     * Initialize FoodRelatedActivity variables declared above
     */
    public void initVariables() {

        noteContent = (EditText) findViewById(R.id.NoteContent);
        foodImage = (ImageView) findViewById(R.id.foodImage);
        whatEattenContent = (EditText) findViewById(R.id.whatEattenContent);
        whyEatRadioGroup = (RadioGroup) findViewById(R.id.whyEatRadioGroup);
        howWasRadioGroup = (RadioGroup) findViewById(R.id.howWasRadioGroup);
        whereEatRadioGroup = (RadioGroup) findViewById(R.id.whereEatRadioGroup);
        makeYouFeelRadioGroup = (RadioGroup) findViewById(R.id.makeYouFeelRadioGroup);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_related);
        setTitle("Food-Related");
        initVariables();
        setListenersRadioGroups();

    }
}
