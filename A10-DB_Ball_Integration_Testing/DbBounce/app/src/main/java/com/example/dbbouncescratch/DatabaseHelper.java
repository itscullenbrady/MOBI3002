package com.example.dbbouncescratch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version constants
    private static final String DATABASE_NAME = "example.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH;
    private Context context;

    // Table and column name constants
    public static final String TABLE_NAME = "example_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COLOUR = "colour";
    public static final String COLUMN_X = "x";
    public static final String COLUMN_Y = "y";
    public static final String COLUMN_AX = "ax";
    public static final String COLUMN_AY = "ay";

    // SQL statement to create the table
    private static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COLOUR + " TEXT, " +
            COLUMN_X + " INTEGER, " +
            COLUMN_Y + " INTEGER, " +
            COLUMN_AX + " INTEGER, " +
            COLUMN_AY + " INTEGER);";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        copyDatabase(); // Copy the default database file to the internal storage
    }

    // Method to copy the default database file from the res/raw folder to the internal storage
    private void copyDatabase() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // Open the default database file from the res/raw folder
            inputStream = context.getResources().openRawResource(R.raw.example_test_1); // this is the default database file
            // Create an output stream to the internal storage database path
            outputStream = Files.newOutputStream(Paths.get(DATABASE_PATH));

            // Buffer to hold data during the copy process
            byte[] buffer = new byte[1024];
            int length;
            // Read from the input stream and write to the output stream
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        } finally {
            // Ensure the streams are closed
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // Log the exception and print the stack trace
                e.printStackTrace();
            }
        }
    }

    // Method to set a different database file from the res/raw folder
    public void setDatabaseFile(int resId) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // Open the specified database file from the res/raw folder
            inputStream = context.getResources().openRawResource(resId);
            // Create an output stream to the internal storage database path
            outputStream = Files.newOutputStream(Paths.get(DATABASE_PATH));

            // Buffer to hold data during the copy process
            byte[] buffer = new byte[1024];
            int length;
            // Read from the input stream and write to the output stream
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        } finally {
            // Ensure the streams are closed
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // Log the exception and print the stack trace
                e.printStackTrace();
            }
        }
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_CREATE); // Execute the SQL statement to create the table
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        }
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Drop the existing table
            onCreate(db); // Create the table again
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        }
    }

    // Method to clear all data from the table
    public void clearDatabase() {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Get a writable database instance
            db.execSQL("DELETE FROM " + TABLE_NAME); // Execute the SQL statement to delete all rows
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        }
        // Close the database
    }
}