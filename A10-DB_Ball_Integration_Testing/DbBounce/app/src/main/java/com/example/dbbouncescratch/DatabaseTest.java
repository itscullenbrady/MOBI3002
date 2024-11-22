package com.example.dbbouncescratch;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DatabaseTest {
    // Tag for logging
    private static final String TAG = "DatabaseTest";
    // Instance of DatabaseManager to perform database operations
    private DatabaseManager databaseManager;

    // Constructor to initialize DatabaseManager with the context and selected database file
    public DatabaseTest(Context context, int databaseResId) {
        try {
            databaseManager = new DatabaseManager(context); // Initialize DatabaseManager
            databaseManager.setDatabaseFile(databaseResId); // Set the database file from the resources
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error initializing DatabaseTest", e);
            e.printStackTrace();
        }
    }

    // Method to run database tests
    public void runTests() {
        Log.d(TAG, "Running database tests...");
        try (Cursor cursor = databaseManager.getAllData()) {
            // Retrieve and log all data from the database
            // Get a cursor over all data in the table
            if (cursor.moveToFirst()) { // Move to the first row in the result set
                do {
                    // Retrieve data from the current row
                    String colour = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COLOUR)); // Get the colour value
                    int x = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_X)); // Get the x value
                    int y = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_Y)); // Get the y value
                    int ax = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AX)); // Get the ax value
                    int ay = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AY)); // Get the ay value
                    // Log the retrieved data
                    Log.d(TAG, "Retrieved data: colour=" + colour + ", x=" + x + ", y=" + y + ", ax=" + ax + ", ay=" + ay);
                } while (cursor.moveToNext()); // Move to the next row in the result set
            }
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error running database tests", e);
            e.printStackTrace();
        }
        // Close the cursor
        Log.d(TAG, "Database tests completed.");
    }
}