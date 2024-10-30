package com.example.sensorassignment

import android.content.Context
import android.view.View
import android.widget.TextView

class IngredientClickListener(
    private val context: Context,
    private val ingredientName: String,
    private val flowersTextView: TextView,
    private val onIngredientSelected: (Int) -> Unit
) : View.OnClickListener {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    override fun onClick(v: View?) {
        val description = dbHelper.getIngredientDescription(ingredientName)
        flowersTextView.text = description
        val ingredientId = dbHelper.getIngredientId(ingredientName)
        onIngredientSelected(ingredientId)
    }
}