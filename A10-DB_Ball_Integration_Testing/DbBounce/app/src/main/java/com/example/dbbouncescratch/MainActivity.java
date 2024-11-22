package com.example.dbbouncescratch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.dbbouncescratch.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    // Configuration for the app bar
    private AppBarConfiguration appBarConfiguration;
    // Binding object to access the views in the layout
    private ActivityMainBinding binding;
    // Spinner to select the database
    private Spinner databaseSpinner;

    // Method called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Inflate the layout using data binding
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            // Set the content view to the root of the binding
            setContentView(binding.getRoot());

            // Set up the toolbar
            setSupportActionBar(binding.toolbar);

            // Set up navigation controller
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            // Set up the floating action button
            binding.fab.setOnClickListener(view -> {
                try {
                    // Show a Snackbar message when the FAB is clicked
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAnchorView(R.id.fab)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    // Log the exception and print the stack trace
                    e.printStackTrace();
                    // Show a Snackbar message indicating an error
                    Snackbar.make(view, "Error performing action", Snackbar.LENGTH_LONG)
                            .setAnchorView(R.id.fab)
                            .setAction("Action", null).show();
                }
            });

            // Find the spinner in the layout
            databaseSpinner = findViewById(R.id.database_spinner);
            // Find the select database button in the layout
            Button selectDatabaseButton = findViewById(R.id.select_database_button);

            // Populate the spinner with database options
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.database_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            databaseSpinner.setAdapter(adapter);

            // Set an OnClickListener for the select database button
            selectDatabaseButton.setOnClickListener(v -> {
                try {
                    // Get the selected database position
                    int selectedDatabase = databaseSpinner.getSelectedItemPosition();
                    // Determine the database file based on the selected position
                    int databaseResId;
                    switch (selectedDatabase) {
                        case 0:
                            databaseResId = R.raw.example_test_1;
                            break;
                        case 1:
                            databaseResId = R.raw.example_test_2;
                            break;
                        case 2:
                            databaseResId = R.raw.example_test_3;
                            break;
                        default:
                            databaseResId = R.raw.example_test_1;
                    }

                    // Start TestActivity with the selected database resource ID
                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                    intent.putExtra("DATABASE_RES_ID", databaseResId);
                    startActivity(intent);
                } catch (Exception e) {
                    // Log the exception and print the stack trace
                    e.printStackTrace();
                    // Show a Snackbar message indicating an error
                    Snackbar.make(v, "Error selecting database", Snackbar.LENGTH_LONG)
                            .setAnchorView(R.id.select_database_button)
                            .setAction("Action", null).show();
                }
            });
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            // Show a Snackbar message indicating an error
            Snackbar.make(findViewById(android.R.id.content), "Error initializing activity", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show();
        }
    }

    // Method to create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // Inflate the menu; this adds items to the action bar if it is present
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return false;
        }
    }

    // Method to handle item selections in the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            // Get the ID of the selected item
            int id = item.getItemId();
            // Handle action bar item clicks here
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return false;
        }
    }

    // Method to handle navigation up actions
    @Override
    public boolean onSupportNavigateUp() {
        try {
            // Get the navigation controller
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            // Handle the navigation up action
            return NavigationUI.navigateUp(navController, appBarConfiguration)
                    || super.onSupportNavigateUp();
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return false;
        }
    }
}