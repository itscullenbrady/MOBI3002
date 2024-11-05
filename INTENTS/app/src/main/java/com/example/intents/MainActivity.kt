package com.example.intents

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    private lateinit var importButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate called")

        drawingView = findViewById(R.id.drawingView)
        importButton = findViewById(R.id.importButton)
        clearButton = findViewById(R.id.clearButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            Log.d("MainActivity", "Window insets applied")
            insets
        }

        importButton.setOnClickListener {
            Log.d("MainActivity", "Import button clicked")
            val downloadIntent = Intent(Intent.ACTION_PICK)
            downloadIntent.setDataAndType(Uri.parse("content://com.android.providers.downloads.documents/document"), "image/*")
            startActivityForResult(downloadIntent, REQUEST_CODE_GALLERY)
        }

        clearButton.setOnClickListener {
            Log.d("MainActivity", "Clear button clicked")
            drawingView.clearDrawing()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("MainActivity", "Image selected: $uri")
                val bitmap = getBitmapFromUri(uri)
                val scaledBitmap = scaleBitmapToFitView(bitmap, drawingView.width, drawingView.height)
                drawingView.setBitmap(scaledBitmap)
            }
        }
    }

    private fun scaleBitmapToFitView(bitmap: Bitmap, viewWidth: Int, viewHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val scaledWidth: Int
        val scaledHeight: Int

        if (viewWidth / aspectRatio <= viewHeight) {
            scaledWidth = viewWidth
            scaledHeight = (viewWidth / aspectRatio).toInt()
        } else {
            scaledWidth = (viewHeight * aspectRatio).toInt()
            scaledHeight = viewHeight
        }

        Log.d("MainActivity", "Bitmap scaled to width=$scaledWidth, height=$scaledHeight")
        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val inputStream = contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        Log.d("MainActivity", "Bitmap retrieved from URI")
        return originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 1
    }
}