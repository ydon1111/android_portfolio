package com.example.navermovieapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.navermovieapp.model.Keyword
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(keyword: Keyword): Long

    @Query("SELECT * FROM keywords ORDER BY id DESC LIMIT 10")
    fun getAllKeywords(): Flow<List<Keyword>>

    @Delete
    suspend fun deleteKeyword(keyword: Keyword)

}