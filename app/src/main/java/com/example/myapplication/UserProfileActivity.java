/***
 * @author Kinan & Luzeen
 * This Activity for the User Profile.
 * He can view his profile account information or perform Logout if he wants to
 */

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class UserProfileActivity extends AppCompatActivity {

    /***
     * Variables Initialization
     */
    private static final int LOGIN_REQUEST = 0;

    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;

    private ParseUser currentUser;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile Account");

        setContentView(R.layout.activity_user_profile);
        titleTextView = findViewById(R.id.profile_title);
        emailTextView =  findViewById(R.id.profile_email);
        nameTextView = findViewById(R.id.profile_name);
        loginOrLogoutButton = findViewById(R.id.login_or_logout_button);
        titleTextView.setText(R.string.profile_title_logged_in);

        loginOrLogoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // User clicked to log out.
                    ParseUser.logOut();
                    currentUser = null;
                    Intent intent = new Intent(getApplicationContext(), SampleDispatchActivity.class);
                    startActivity(intent);
                    //showProfileLoggedOut();
                } /*else {
                    // User clicked to log in.
                    ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                            UserProfileActivity.this);
                    startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
                }*/
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            showProfileLoggedIn();
        } /*else {
            showProfileLoggedOut();
        }*/
    }

    /**
     * Shows the profile of the given user.
     */
    private void showProfileLoggedIn() {
        titleTextView.setText(R.string.profile_title_logged_in);
        emailTextView.setText(currentUser.getEmail());
        String fullName = currentUser.getString("name");
        if (fullName != null) {
            nameTextView.setText(fullName);
        }
        loginOrLogoutButton.setText(R.string.profile_logout_button_label);
    }

    /**
     * Show a message asking the user to log in, toggle login/logout button text.
     */
    private void showProfileLoggedOut() {
        titleTextView.setText(R.string.profile_title_logged_out);
        emailTextView.setText("");
        nameTextView.setText("");
        loginOrLogoutButton.setText(R.string.profile_login_button_label);
    }
}
