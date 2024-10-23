package com.example.dbbouncescratch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class BallView extends View {
    private List<Ball> balls = new ArrayList<>();
    private Paint paint = new Paint();
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (Ball ball : balls) {
                ball.updatePosition(getWidth(), getHeight());
            }
            invalidate(); // Trigger a redraw
            handler.postDelayed(this, 16); // Schedule next update (approx. 60 FPS)
        }
    };

    public BallView(Context context) {
        super(context);
        startUpdating();
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        startUpdating();
    }

    public void addBall(Ball ball) {
        balls.add(ball);
        invalidate(); // Trigger a redraw
    }

    public void clearBalls() {
        balls.clear();
        invalidate(); // Trigger a redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Ball ball : balls) {
            ball.draw(canvas, paint); // Draw each ball
        }
    }

    private void startUpdating() {
        handler.post(runnable);
    }
}