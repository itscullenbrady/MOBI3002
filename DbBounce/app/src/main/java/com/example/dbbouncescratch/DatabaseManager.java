package com.example.dbbouncescratch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long insertData(String colour, int x, int y, int ax, int ay) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COLOUR, colour);
        values.put(DatabaseHelper.COLUMN_X, x);
        values.put(DatabaseHelper.COLUMN_Y, y);
        values.put(DatabaseHelper.COLUMN_AX, ax);
        values.put(DatabaseHelper.COLUMN_AY, ay);
        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public Cursor getAllData() {
        return database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
    }



}