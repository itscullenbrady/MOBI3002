package com.example.dbbouncescratch;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dbbouncescratch.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private DatabaseManager databaseManager;
    private BallView ballView;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        databaseManager = new DatabaseManager(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        return binding.getRoot();
    }

@Override
public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ballView = binding.getRoot().findViewById(R.id.ballView);

    binding.savedb.setOnClickListener(v -> {
        int progress = binding.seekBar1.getProgress();
        int color = Color.HSVToColor(new float[]{progress * 360 / 100, 1, 1}); // Map progress to a color
        String colorHex = String.format("#%06X", (0xFFFFFF & color)); // Convert to hex color
        int x = binding.seekBar2.getProgress();
        int y = binding.seekBar3.getProgress();
        int ax = binding.seekBar4.getProgress();
        int ay = binding.seekBar5.getProgress();
        long id = databaseManager.insertData(colorHex, x, y, ax, ay);
        Toast.makeText(getContext(), "Data inserted with ID: " + id, Toast.LENGTH_SHORT).show();
    });

    binding.create.setOnClickListener(v -> {
        Cursor cursor = databaseManager.getAllData();
        if (cursor.moveToFirst()) {
            Toast.makeText(getContext(), "Data found in the database", Toast.LENGTH_SHORT).show();
            do {
                int colourIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_COLOUR);
                int xIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_X);
                int yIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_Y);
                int axIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AX);
                int ayIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AY);

                if (colourIndex != -1 && xIndex != -1 && yIndex != -1 && axIndex != -1 && ayIndex != -1) {
                    String colourHex = cursor.getString(colourIndex);
                    int x = cursor.getInt(xIndex);
                    int y = cursor.getInt(yIndex);
                    int ax = cursor.getInt(axIndex);
                    int ay = cursor.getInt(ayIndex);
                    try {
                        int color = Color.parseColor(colourHex);
                        Ball ball = new Ball(color, x, y, ax, ay);
                        ballView.addBall(ball);
                        Toast.makeText(getContext(), "Ball created", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getContext(), "Invalid color format: " + colourHex, Toast.LENGTH_SHORT).show();
                        e.printStackTrace(); // Log the exception for debugging
                    }
                } else {
                    Toast.makeText(getContext(), "Error retrieving data from the database", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getContext(), "No data found in the database", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    });

    binding.Clean.setOnClickListener(v -> {
        databaseHelper.clearDatabase();
        ballView.clearBalls();
        Toast.makeText(getContext(), "Database and screen cleared", Toast.LENGTH_SHORT).show();
    });
}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}