package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private EditText textN1, textN2, textANS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "App started");

        textN1 = (EditText) findViewById(R.id.inputNumber1);
        textN2 = (EditText) findViewById(R.id.inputNumber2);
        textANS = (EditText) findViewById(R.id.editTextNumAns);

        // Add button
        ImageButton buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> performOperation((d1, d2) -> d1 + d2, "+"));

        // Subtract button
        ImageButton buttonSubtract = (ImageButton) findViewById(R.id.buttonSubtract);
        buttonSubtract.setOnClickListener(v -> performOperation((d1, d2) -> d1 - d2, "-"));

        // Multiply button
        ImageButton buttonMultiply = (ImageButton) findViewById(R.id.buttonMultiply);
        buttonMultiply.setOnClickListener(v -> performOperation((d1, d2) -> d1 * d2, "*"));

        // Divide button
        ImageButton buttonDivide = (ImageButton) findViewById(R.id.buttonDivide);
        buttonDivide.setOnClickListener(v -> performOperation((d1, d2) -> {
            if (d2 != 0) {
                return d1 / d2;
            } else {
                return Double.NaN; // Or display an error message
            }
        }, "/"));
    }

    private void performOperation(Operation operation, String operatorSymbol) {
        Double d1 = 0.0;
        Double d2 = 0.0;
        Double answer = 0.0;

        try {
            d1 = Double.parseDouble(textN1.getText().toString());
            d2 = Double.parseDouble(textN2.getText().toString());
            answer = operation.calculate(d1, d2);
        } catch (Exception e) {
            Log.w("M01_Calculator", "Operation Selected with no inputs ... " + answer);
        }

        textANS.setText(answer.toString());
        Log.w("M01_Calculator", "Operation Selected with => " + d1 + " " + operatorSymbol + " " + d2 + "=" + answer);
    }

    interface Operation {
        double calculate(double d1, double d2);
    }
}