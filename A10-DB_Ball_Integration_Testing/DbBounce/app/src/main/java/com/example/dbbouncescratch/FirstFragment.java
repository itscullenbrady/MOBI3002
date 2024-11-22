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

    // Binding object to access the views in the layout
    private FragmentFirstBinding binding;
    // DatabaseManager instance to perform database operations
    private DatabaseManager databaseManager;
    // BallView instance to display the balls
    private BallView ballView;
    // DatabaseHelper instance to manage database creation and version management
    private DatabaseHelper databaseHelper;

    // Method called to create the view hierarchy associated with the fragment
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        try {
            // Inflate the layout for this fragment using data binding
            binding = FragmentFirstBinding.inflate(inflater, container, false);
            // Initialize DatabaseManager with the context
            databaseManager = new DatabaseManager(getContext());
            // Initialize DatabaseHelper with the context
            databaseHelper = new DatabaseHelper(getContext());
            // Return the root view of the binding
            return binding.getRoot();
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            // Return a default view in case of an exception
            return inflater.inflate(R.layout.fragment_first, container, false);
        }
    }

    // Method called immediately after onCreateView() has returned
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Get the BallView from the layout
            ballView = binding.getRoot().findViewById(R.id.ballView);

            // Set an OnClickListener for the save button
            binding.savedb.setOnClickListener(v -> {
                try {
                    // Get the progress from the seek bars and map to a color
                    int progress = binding.seekBar1.getProgress();
                    int color = Color.HSVToColor(new float[]{(float) (progress * 360) / 100, 1, 1}); // Map progress to a color
                    String colorHex = String.format("#%06X", (0xFFFFFF & color)); // Convert to hex color
                    int x = binding.seekBar2.getProgress();
                    int y = binding.seekBar3.getProgress();
                    int ax = binding.seekBar4.getProgress();
                    int ay = binding.seekBar5.getProgress();
                    // Insert the data into the database and get the row ID
                    long id = databaseManager.insertData(colorHex, x, y, ax, ay);
                    // Show a toast message with the row ID
                    Toast.makeText(getContext(), "Data inserted with ID: " + id, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Log the exception and print the stack trace
                    e.printStackTrace();
                    // Show a toast message indicating an error
                    Toast.makeText(getContext(), "Error inserting data", Toast.LENGTH_SHORT).show();
                }
            });

            // Set an OnClickListener for the create button
            binding.create.setOnClickListener(v -> {
                try (Cursor cursor = databaseManager.getAllData()) {
                    // Get all data from the database
                    if (cursor.moveToFirst()) {
                        // Show a toast message if data is found
                        Toast.makeText(getContext(), "Data found in the database", Toast.LENGTH_SHORT).show();
                        do {
                            // Get the column indices
                            int colourIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_COLOUR);
                            int xIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_X);
                            int yIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_Y);
                            int axIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AX);
                            int ayIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_AY);

                            // Check if the column indices are valid
                            if (colourIndex != -1 && xIndex != -1 && yIndex != -1 && axIndex != -1 && ayIndex != -1) {
                                // Get the data from the cursor
                                String colourHex = cursor.getString(colourIndex);
                                int x = cursor.getInt(xIndex);
                                int y = cursor.getInt(yIndex);
                                int ax = cursor.getInt(axIndex);
                                int ay = cursor.getInt(ayIndex);
                                try {
                                    // Parse the color and create a new Ball object
                                    int color = Color.parseColor(colourHex);
                                    Ball ball = new Ball(color, x, y, ax, ay);
                                    // Add the ball to the BallView
                                    ballView.addBall(ball);
                                    // Show a toast message indicating the ball was created
                                    Toast.makeText(getContext(), "Ball created", Toast.LENGTH_SHORT).show();
                                } catch (IllegalArgumentException e) {
                                    // Show a toast message if the color format is invalid
                                    Toast.makeText(getContext(), "Invalid color format: " + colourHex, Toast.LENGTH_SHORT).show();
                                    e.printStackTrace(); // Log the exception for debugging
                                }
                            } else {
                                // Show a toast message if there is an error retrieving data
                                Toast.makeText(getContext(), "Error retrieving data from the database", Toast.LENGTH_SHORT).show();
                            }
                        } while (cursor.moveToNext());
                    } else {
                        // Show a toast message if no data is found
                        Toast.makeText(getContext(), "No data found in the database", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Log the exception and print the stack trace
                    e.printStackTrace();
                    // Show a toast message indicating an error
                    Toast.makeText(getContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
                // Close the cursor to release resources
            });

            // Set an OnClickListener for the clean button
            binding.Clean.setOnClickListener(v -> {
                try {
                    // Clear the database and the BallView
                    databaseHelper.clearDatabase();
                    ballView.clearBalls();
                    // Show a toast message indicating the database and screen were cleared
                    Toast.makeText(getContext(), "Database and screen cleared", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Log the exception and print the stack trace
                    e.printStackTrace();
                    // Show a toast message indicating an error
                    Toast.makeText(getContext(), "Error clearing database and screen", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            // Show a toast message indicating an error
            Toast.makeText(getContext(), "Error initializing view", Toast.LENGTH_SHORT).show();
        }
    }

    // Method called when the view hierarchy associated with the fragment is being removed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            // Set the binding to null to avoid memory leaks
            binding = null;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        }
    }
}