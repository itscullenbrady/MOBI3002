package com.codelab.basics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper implements DB_Interface {

    private static final String DATABASE_NAME = "database.db";
    private static final String DATABASE_PATH = "/data/data/com.codelab.basics/databases/";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase database;

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabase();
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("DBClass", "Error copying database", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables as we are using a pre-existing database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public int count() {
        String countQuery = "SELECT * FROM Items";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        Log.v("DBClass", "getCount=" + cnt);
        return cnt;
    }

    @Override
    public int save(DataModel dataModel) {
        // Implement save method if needed
        return 0;
    }

    @Override
    public int update(DataModel dataModel) {
        // Implement update method if needed
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        // Implement delete method if needed
        return 0;
    }

    @Override
    public List<DataModel> findAll() {
        List<DataModel> temp = new ArrayList<>();
        String query = "SELECT * FROM Items";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                DataModel item = new DataModel(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5) // access_count
                );
                temp.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.v("DBClass", "findAll=> " + temp.toString());
        return temp;
    }

    @Override
    public String getNameById(Long id) {
        // Implement getNameById method if needed
        return null;
    }

    public void incrementAccessCount(long item_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("UPDATE Items SET access_count = access_count + 1 WHERE item_id = ?", new Object[]{item_id});
            Log.d("DBClass", "Access count incremented for item_id: " + item_id);
        } catch (Exception e) {
            Log.e("DBClass", "Error incrementing access count", e);
        } finally {
            db.close();
        }
    }

    public DataModel getFavoriteItem() {
        DataModel favoriteItem = null;
        String query = "SELECT * FROM Items ORDER BY access_count DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            favoriteItem = new DataModel(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getString(4),
                cursor.getInt(5) // access_count
            );
        }

        cursor.close();
        return favoriteItem;
    }
}