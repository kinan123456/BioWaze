/***
 * @author Kinan & Luzeen
 */

package com.example.myapplication;

import android.widget.Toast;

import com.example.myapplication.AdminPanel.AdminHomeActivity;
import com.parse.ui.login.ParseLoginDispatchActivity;

public class SampleDispatchActivity extends ParseLoginDispatchActivity {


    /**
     * Display LoginParseUI if ParseUser isn't connected, otherwise display MainActivity screen
     * @return
     */
    @Override
    protected Class<?> getTargetClass() {

        return MainActivity.class;

    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(SampleDispatchActivity.this, "Nothing happens.", Toast.LENGTH_LONG).show();

    }
}
