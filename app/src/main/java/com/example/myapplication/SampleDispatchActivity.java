/***
 * @author Kinan & Luzeen
 */

package com.example.myapplication;

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
}
