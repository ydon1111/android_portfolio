package com.example.navermovieapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.navermovieapp.api.MovieApi
import com.example.navermovieapp.model.MovieItem
import com.example.navermovieapp.util.Constants.NAVER_ID
import com.example.navermovieapp.util.Constants.NAVER_SECRET
import com.example.navermovieapp.util.Constants.PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class MovieListPagingSource(val movieApi: MovieApi, val searchQuery: String) :
    PagingSource<Int, MovieItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val page = params.key ?: 1
            val response = movieApi.getMovieList(
                NAVER_ID,
                NAVER_SECRET, searchQuery,
                PAGE_SIZE, page
            )

            Log.d("pageCount@@@", page.toString() + " " + response.total)

            LoadResult.Page(
                response.items,
                prevKey = null,
                nextKey = if (page + PAGE_SIZE >= response.total) null else page + PAGE_SIZE
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition
    }
}