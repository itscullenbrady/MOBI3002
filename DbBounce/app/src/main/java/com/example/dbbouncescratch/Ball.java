
package com.example.dbbouncescratch;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
    private int color;
    private int x;
    private int y;
    private int ax;
    private int ay;
    private int radius = 20; // Radius of the ball

    public Ball(int color, int x, int y, int ax, int ay) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.ax = ax;
        this.ay = ay;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint); // Draw a circle with radius 20
    }

    public void updatePosition(int viewWidth, int viewHeight) {
        x += ax;
        y += ay;

        // Check for collision with the left or right boundary
        if (x - radius < 0 || x + radius > viewWidth) {
            ax = -ax; // Reverse direction
        }

        // Check for collision with the top or bottom boundary
        if (y - radius < 0 || y + radius > viewHeight) {
            ay = -ay; // Reverse direction
        }
    }

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAx() {
        return ax;
    }

    public int getAy() {
        return ay;
    }
}