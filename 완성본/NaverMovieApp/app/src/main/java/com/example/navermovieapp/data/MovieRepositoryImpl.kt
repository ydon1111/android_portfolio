package com.example.navermovieapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.navermovieapp.api.MovieApi
import com.example.navermovieapp.db.HistoryDatabase
import com.example.navermovieapp.model.Keyword
import com.example.navermovieapp.model.MovieItem
import com.example.navermovieapp.util.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl(
    private val api: MovieApi,
    private val db : HistoryDatabase
) : MovieRepository{
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false )
    }

    override fun letMovieList(searchQuery: String): Flow<PagingData<MovieItem>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = {MovieListPagingSource(api,searchQuery)}
        ).flow
    }

    override suspend fun insert(keyword: Keyword) = db.getHistoryDao().insert(keyword)
    override fun getKeywords(): Flow<List<Keyword>> = db.getHistoryDao().getAllKeywords()
    override suspend fun deleteKeyword(keyword: Keyword) = db.getHistoryDao().deleteKeyword(keyword)
}
