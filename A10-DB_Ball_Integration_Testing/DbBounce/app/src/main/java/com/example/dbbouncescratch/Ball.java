package com.example.dbbouncescratch;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
    // Ball properties
    private int color; // Color of the ball
    private int x; // X-coordinate of the ball's position
    private int y; // Y-coordinate of the ball's position
    private int ax; // X-axis velocity of the ball
    private int ay; // Y-axis velocity of the ball
    private int radius = 20; // Radius of the ball

    // Constructor to initialize the ball's properties
    public Ball(int color, int x, int y, int ax, int ay) {
        this.color = color; // Set the color of the ball
        this.x = x; // Set the initial X-coordinate
        this.y = y; // Set the initial Y-coordinate
        this.ax = ax; // Set the initial X-axis velocity
        this.ay = ay; // Set the initial Y-axis velocity
    }

    // Method to draw the ball on the canvas
    public void draw(Canvas canvas, Paint paint) {
        try {
            paint.setColor(color); // Set the paint color to the ball's color
            canvas.drawCircle(x, y, radius, paint); // Draw a circle representing the ball
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        }
    }

    // Method to update the ball's position based on its velocity
    public void updatePosition(int viewWidth, int viewHeight) {
        try {
            x += ax; // Update the X-coordinate by adding the X-axis velocity
            y += ay; // Update the Y-coordinate by adding the Y-axis velocity

            // Check for collision with the left or right boundary
            if (x - radius < 0 || x + radius > viewWidth) {
                ax = -ax; // Reverse the X-axis direction if a collision occurs
            }

            // Check for collision with the top or bottom boundary
            if (y - radius < 0 || y + radius > viewHeight) {
                ay = -ay; // Reverse the Y-axis direction if a collision occurs
            }
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
        }
    }

    // Getter method for the ball's color
    public int getColor() {
        try {
            return color;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return -1; // Return a default value in case of an exception
        }
    }

    // Getter method for the ball's X-coordinate
    public int getX() {
        try {
            return x;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return -1; // Return a default value in case of an exception
        }
    }

    // Getter method for the ball's Y-coordinate
    public int getY() {
        try {
            return y;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return -1; // Return a default value in case of an exception
        }
    }

    // Getter method for the ball's X-axis velocity
    public int getAx() {
        try {
            return ax;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return -1; // Return a default value in case of an exception
        }
    }

    // Getter method for the ball's Y-axis velocity
    public int getAy() {
        try {
            return ay;
        } catch (Exception e) {
            // Log the exception and print the stack trace
            e.printStackTrace();
            return -1; // Return a default value in case of an exception
        }
    }
}