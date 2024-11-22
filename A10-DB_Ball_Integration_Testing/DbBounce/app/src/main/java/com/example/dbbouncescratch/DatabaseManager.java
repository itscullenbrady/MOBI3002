package com.example.dbbouncescratch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {

    // Tag for logging
    private static final String TAG = "DatabaseManager";
    // Instance of DatabaseHelper to manage database creation and version management
    private DatabaseHelper dbHelper;
    // SQLiteDatabase instance to perform database operations
    private SQLiteDatabase database;

    // Constructor to initialize DatabaseHelper and get a writable database
    public DatabaseManager(Context context) {
        try {
            dbHelper = new DatabaseHelper(context); // Initialize DatabaseHelper with the context
            database = dbHelper.getWritableDatabase(); // Get a writable database
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error initializing DatabaseManager", e);
            e.printStackTrace();
        }
    }

    // Method to set a new database file from the resources
    public void setDatabaseFile(int resId) {
        try {
            dbHelper.setDatabaseFile(resId); // Set the new database file in DatabaseHelper
            database = dbHelper.getWritableDatabase(); // Get a writable database with the new file
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error setting database file", e);
            e.printStackTrace();
        }
    }

    // Method to insert data into the database
    public long insertData(String colour, int x, int y, int ax, int ay) {
        long rowId = -1;
        try {
            ContentValues values = new ContentValues(); // Create a new ContentValues object to hold the data
            values.put(DatabaseHelper.COLUMN_COLOUR, colour); // Put the colour value
            values.put(DatabaseHelper.COLUMN_X, x); // Put the x value
            values.put(DatabaseHelper.COLUMN_Y, y); // Put the y value
            values.put(DatabaseHelper.COLUMN_AX, ax); // Put the ax value
            values.put(DatabaseHelper.COLUMN_AY, ay); // Put the ay value
            rowId = database.insert(DatabaseHelper.TABLE_NAME, null, values); // Insert the data into the table and return the row ID
        } catch (SQLException e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error inserting data", e);
            e.printStackTrace();
        }
        return rowId;
    }

    // Method to retrieve all data from the database
    public Cursor getAllData() {
        Cursor cursor = null;
        try {
            cursor = database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null); // Query the table and return a Cursor over the result set
        } catch (SQLException e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error retrieving data", e);
            e.printStackTrace();
        }
        return cursor;
    }

    // Method to clear all data from the database
    public void clearDatabase() {
        try {
            database.execSQL("DELETE FROM " + DatabaseHelper.TABLE_NAME); // Execute the SQL statement to delete all rows from the table
        } catch (SQLException e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error clearing database", e);
            e.printStackTrace();
        }
    }
}