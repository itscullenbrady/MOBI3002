package com.example.sensorassignment

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val selectedIngredientIds = mutableListOf<Int>()
    private lateinit var flowersTextView: TextView
    private lateinit var mixtureTextView: TextView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var isShakeDetected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flowersTextView = findViewById(R.id.flowers)
        mixtureTextView = findViewById(R.id.mixture)
        dbHelper = DatabaseHelper(this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val rosemaryButton = findViewById<ImageButton>(R.id.rosemaryButton)
        rosemaryButton.setOnClickListener(IngredientClickListener(this, "rosemary", flowersTextView, ::onIngredientSelected))

        val belladonnaButton = findViewById<ImageButton>(R.id.belladonnaButton)
        belladonnaButton.setOnClickListener(IngredientClickListener(this, "belladonna (deadly nightshade)", flowersTextView, ::onIngredientSelected))

        val yarrowButton = findViewById<ImageButton>(R.id.yarrowButton)
        yarrowButton.setOnClickListener(IngredientClickListener(this, "yarrow", flowersTextView, ::onIngredientSelected))

        val valerianButton = findViewById<ImageButton>(R.id.valerianButton)
        valerianButton.setOnClickListener(IngredientClickListener(this, "valerian root", flowersTextView, ::onIngredientSelected))

        val mixButton = findViewById<Button>(R.id.mixButton)
        mixButton.setOnClickListener {
            if (selectedIngredientIds.size == 2) {
                Toast.makeText(this, "Shake the device", Toast.LENGTH_SHORT).show()
                isShakeDetected = false
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                mixtureTextView.text = "Please select two ingredients."
            }
        }

        val clearButton = findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            clearSelectedIngredients()
        }
    }

    private fun onIngredientSelected(ingredientId: Int) {
        if (selectedIngredientIds.size >= 2) {
            selectedIngredientIds.clear()
        }
        selectedIngredientIds.add(ingredientId)
    }

    private fun checkForMixture() {
        val id1 = selectedIngredientIds[0]
        val id2 = selectedIngredientIds[1]
        val mixtureDescription = dbHelper.getMixtureDescription(id1, id2)
        mixtureTextView.text = mixtureDescription ?: "No mixture found for selected ingredients."
    }

    private fun clearSelectedIngredients() {
        selectedIngredientIds.clear()
        mixtureTextView.text = ""
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            when {
                acceleration > 12 -> {
                    if (!isShakeDetected) {
                        isShakeDetected = true
                        sensorManager.unregisterListener(this)
                        checkForMixture()
                        Toast.makeText(this, "Mixture Complete", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }
}