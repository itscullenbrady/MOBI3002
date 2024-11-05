package com.example.intents

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var path = Path()
    private var paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private var bitmap: Bitmap? = null
    private var canvasBitmap: Canvas? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("DrawingView", "onDraw called")
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
            Log.d("DrawingView", "Bitmap drawn")
        }
        canvas.drawPath(path, paint)
        Log.d("DrawingView", "Path drawn")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.d("DrawingView", "onTouchEvent: x=$x, y=$y, action=${event.action}")

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                Log.d("DrawingView", "Action down at x=$x, y=$y")
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                Log.d("DrawingView", "Action move to x=$x, y=$y")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("DrawingView", "Action up at x=$x, y=$y")
            }
        }
        invalidate()
        return true
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        canvasBitmap = Canvas(bitmap)
        Log.d("DrawingView", "Bitmap set")
        invalidate()
    }

    fun clearDrawing() {
        path.reset()
        Log.d("DrawingView", "Drawing cleared")
        invalidate()
    }
}