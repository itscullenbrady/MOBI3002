package com.example.dbbouncescratch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class BallView extends View {
    // Tag for logging
    private static final String TAG = "BallView";
    // List to hold all the balls
    private List<Ball> balls = new ArrayList<>();
    // Paint object for drawing
    private Paint paint = new Paint();
    // Handler to schedule updates
    private Handler handler = new Handler();
    // Runnable to update ball positions periodically
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                // Update the position of each ball
                for (Ball ball : balls) {
                    ball.updatePosition(getWidth(), getHeight());
                    Log.d(TAG, "Ball updated: " + ball.toString());
                }
                // Schedule the next update (approx. 60 FPS)
                handler.postDelayed(this, 16);
            } catch (Exception e) {
                // Log the exception and print the stack trace
                Log.e(TAG, "Error updating ball positions", e);
                e.printStackTrace();
            }
            invalidate(); // Trigger a redraw
        }
    };

    // Constructor used when creating the view programmatically
    public BallView(Context context) {
        super(context);
        try {
            startUpdating(); // Start the periodic updates
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error starting updates", e);
            e.printStackTrace();
        }
    }

    // Constructor used when inflating the view from XML
    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            startUpdating(); // Start the periodic updates
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error starting updates", e);
            e.printStackTrace();
        }
    }

    // Method to add a ball to the view
    public void addBall(Ball ball) {
        try {
            balls.add(ball);
            Log.d(TAG, "Ball added: " + ball.toString());
            invalidate(); // Trigger a redraw
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error adding ball", e);
            e.printStackTrace();
        }
    }

    // Method to clear all balls from the view
    public void clearBalls() {
        try {
            balls.clear();
            Log.d(TAG, "All balls cleared");
            invalidate(); // Trigger a redraw
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error clearing balls", e);
            e.printStackTrace();
        }
    }

    // Method to start the periodic updates
    private void startUpdating() {
        try {
            handler.post(runnable); // Post the runnable to the handler
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error posting runnable", e);
            e.printStackTrace();
        }
    }

    // Method to draw the balls on the canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            for (Ball ball : balls) {
                ball.draw(canvas, paint); // Draw each ball
            }
        } catch (Exception e) {
            // Log the exception and print the stack trace
            Log.e(TAG, "Error drawing balls", e);
            e.printStackTrace();
        }
    }
}