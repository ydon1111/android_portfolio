package com.example.navermovieapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.navermovieapp.model.Keyword


@Database(
    entities = [Keyword::class],
    version = 3
)
abstract class HistoryDatabase : RoomDatabase(){
    abstract fun getHistoryDao(): HistoryDao
}