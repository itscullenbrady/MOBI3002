package com.example.dbbouncescratch;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class TestActivity extends AppCompatActivity {

    // Tag for logging
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Set the content view to the layout defined in activity_test.xml
            setContentView(R.layout.activity_test);

            // Get the selected database resource ID from the intent
            int databaseResId = getIntent().getIntExtra("DATABASE_RES_ID", R.raw.example_test_1);

            // Create an instance of DatabaseTest with the selected database resource ID and run the tests
            DatabaseTest databaseTest = new DatabaseTest(this, databaseResId);
            databaseTest.runTests();
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error in onCreate", e);
            e.printStackTrace();
        }
    }
}