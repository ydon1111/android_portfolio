package com.example.favoriteplacesapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.favoriteplacesapp.models.FavoritePlaceModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "FavoritePlacesDatabase"
        private const val TABLE_FAVORITE_PLACE = "FavoriteTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_FAVORITE_PLACE_TABLE = ("CREATE TABLE " + TABLE_FAVORITE_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_FAVORITE_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE_PLACE")
        onCreate(db)
    }

    fun addFavoritePlace(favoritePlace: FavoritePlaceModel): Long {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(KEY_TITLE, favoritePlace.title)
        contentValue.put(KEY_IMAGE, favoritePlace.image)
        contentValue.put(
            KEY_DESCRIPTION,
            favoritePlace.description
        )
        contentValue.put(KEY_DATE, favoritePlace.date)
        contentValue.put(KEY_LOCATION, favoritePlace.location)
        contentValue.put(KEY_LATITUDE, favoritePlace.latitude)
        contentValue.put(KEY_LONGITUDE, favoritePlace.longitude)


        val result = db.insert(TABLE_FAVORITE_PLACE, null, contentValue)

        db.close()
        return result
    }

    fun updateFavoritePlace(favoritePlace: FavoritePlaceModel): Int {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(KEY_TITLE, favoritePlace.title)
        contentValue.put(KEY_IMAGE, favoritePlace.image)
        contentValue.put(
            KEY_DESCRIPTION,
            favoritePlace.description
        )
        contentValue.put(KEY_DATE, favoritePlace.date)
        contentValue.put(KEY_LOCATION, favoritePlace.location)
        contentValue.put(KEY_LATITUDE, favoritePlace.latitude)
        contentValue.put(KEY_LONGITUDE, favoritePlace.longitude)


        val success =
            db.update(
                TABLE_FAVORITE_PLACE,
                contentValue,
                KEY_ID + "=" + favoritePlace.id,
                null
            )

        db.close()
        return success
    }

    fun deleteFavoritePlace(favoritePlace: FavoritePlaceModel): Int {
        val db = this.writableDatabase

        val success = db.delete(
            TABLE_FAVORITE_PLACE,
            KEY_ID + "=" + favoritePlace.id,
            null)
        db.close()
        return success
    }


    @SuppressLint("Range")
    fun getFavoritePlacesList(): ArrayList<FavoritePlaceModel> {
        val favoritePlaceList = ArrayList<FavoritePlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_FAVORITE_PLACE"
        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val place = FavoritePlaceModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )

                    favoritePlaceList.add(place)

                } while (cursor.moveToNext())
            }

            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        return favoritePlaceList
    }

}