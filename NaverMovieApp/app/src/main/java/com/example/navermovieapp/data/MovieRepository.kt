package com.example.navermovieapp.data

import androidx.paging.PagingData
import com.example.navermovieapp.model.Keyword
import com.example.navermovieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow



interface MovieRepository {
    fun letMovieList(searchQuery: String): Flow<PagingData<MovieItem>>
    suspend fun insert(keyword: Keyword): Long
    fun getKeywords(): Flow<List<Keyword>>
    suspend fun deleteKeyword(keyword: Keyword)
}