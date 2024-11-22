package com.example.dbbouncescratch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

public class DatabaseUnitTest_01 {
    // Instance of DatabaseManager to perform database operations
    private DatabaseManager databaseManager;

    // Method to set up the test environment before each test
    @Before
    public void setUp() {
        try {
            // Get the application context
            Context context = ApplicationProvider.getApplicationContext();
            // Initialize DatabaseManager with the context
            databaseManager = new DatabaseManager(context);
            // Clear the database to ensure a clean state for each test
            databaseManager.clearDatabase();
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            fail("Exception in setUp: " + e.getMessage());
        }
    }

    // Test method to insert and retrieve a ball's data from the database
    @Test
    public void testInsertAndRetrieveBall() {
        Cursor cursor = null;
        try {
            // Define test data
            String colour = "#FF0000"; // Colour of the ball
            int x = 10; // X-coordinate of the ball
            int y = 20; // Y-coordinate of the ball
            int ax = 1; // X-axis velocity of the ball
            int ay = 1; // Y-axis velocity of the ball

            // Insert the test data into the database and get the row ID
            long id = databaseManager.insertData(colour, x, y, ax, ay);

            // Retrieve all data from the database
            cursor = databaseManager.getAllData();
            // Assert that the cursor is not empty and move to the first row
            assertTrue(cursor.moveToFirst());
            // Assert that the retrieved data matches the inserted data
            assertEquals(colour, cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COLOUR)));
            assertEquals(x, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_X)));
            assertEquals(y, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_Y)));
            assertEquals(ax, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AX)));
            assertEquals(ay, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AY)));
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            fail("Exception in testInsertAndRetrieveBall: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor to release resources
            }
        }
    }
}