package com.example.sensorassignment

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_INGREDIENTS_TABLE)
        db.execSQL(CREATE_MIXTURES_TABLE)
        db.execSQL(INSERT_INGREDIENTS)
        db.execSQL(INSERT_MIXTURES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Ingredients")
        db.execSQL("DROP TABLE IF EXISTS Mixtures")
        onCreate(db)
    }

    fun getIngredientDescription(name: String): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT description FROM Ingredients WHERE name = ?", arrayOf(name))
        var description = ""
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        }
        cursor.close()
        return description
    }

    fun getIngredientId(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM Ingredients WHERE name = ?", arrayOf(name))
        var id = -1
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        return id
    }

    fun getMixtureDescription(id1: Int, id2: Int): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT description FROM Mixtures WHERE (ingredient1_id = ? AND ingredient2_id = ?) OR (ingredient1_id = ? AND ingredient2_id = ?)",
            arrayOf(id1.toString(), id2.toString(), id2.toString(), id1.toString())
        )
        var description: String? = null
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
        }
        cursor.close()
        return description
    }

    companion object {
        private const val DATABASE_NAME = "herbs.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_INGREDIENTS_TABLE = """
            CREATE TABLE Ingredients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT NOT NULL
            );
        """

        private const val CREATE_MIXTURES_TABLE = """
            CREATE TABLE Mixtures (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ingredient1_id INTEGER NOT NULL,
                ingredient2_id INTEGER NOT NULL,
                description TEXT NOT NULL,
                FOREIGN KEY (ingredient1_id) REFERENCES Ingredients(id),
                FOREIGN KEY (ingredient2_id) REFERENCES Ingredients(id)
            );
        """

        private const val INSERT_INGREDIENTS = """
            INSERT INTO Ingredients (name, description) VALUES
            ('valerian root', 'known historically for its sedative effects, valerian was often used to relieve anxiety and promote restful sleep. it’s a great ingredient for calming blends or sleep aids.'),
            ('belladonna (deadly nightshade)', 'although toxic in larger amounts, belladonna was once used in careful, minimal doses for pain relief and muscle relaxation. this could be used for mixtures that involve pain management or reducing tension.'),
            ('yarrow', 'a classic in herbal medicine, yarrow is said to aid wound healing and reduce inflammation. it would fit well in a blend for physical resilience, health tonics, or first aid salves.'),
            ('rosemary', 'beyond its culinary use, rosemary has been historically associated with memory enhancement and mental clarity. it could contribute to mixtures designed to support focus and cognitive function.');
        """

        private const val INSERT_MIXTURES = """
            INSERT INTO Mixtures (ingredient1_id, ingredient2_id, description) VALUES
            (1, 3, 'a soothing tincture, traditionally brewed to ease the mind and promote deep, restful sleep. said to help with relaxation and recovery from the day’s toils.'),
            (3, 1, 'a soothing tincture, traditionally brewed to ease the mind and promote deep, restful sleep. said to help with relaxation and recovery from the day’s toils.'),
    
            (2, 4, 'a precise and careful blend, used only sparingly, believed to aid in muscle relaxation and focus. historically, this mixture was applied by healers to relieve tension while keeping the mind alert.'),
            (4, 2, 'a precise and careful blend, used only sparingly, believed to aid in muscle relaxation and focus. historically, this mixture was applied by healers to relieve tension while keeping the mind alert.'),
            
            (3, 4, 'a bright, invigorating blend, thought to enhance clarity and support recovery. known among herbalists as a remedy for lingering fatigue, it’s also said to sharpen memory and concentration.'),
            (4, 3, 'a bright, invigorating blend, thought to enhance clarity and support recovery. known among herbalists as a remedy for lingering fatigue, it’s also said to sharpen memory and concentration.'),
    
            (1, 2, 'a rare combination, used in moderation to soothe pain and promote calmness. historically, it was prepared with caution, providing comfort in small doses when relief was needed most.'),
            (2, 1, 'a rare combination, used in moderation to soothe pain and promote calmness. historically, it was prepared with caution, providing comfort in small doses when relief was needed most.');

            """
    }
}