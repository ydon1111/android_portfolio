package com.example.navermovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.navermovieapp.data.MovieRepository
import com.example.navermovieapp.model.Keyword
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MovieViewModel @Inject constructor(
     private val repository: MovieRepository,
) : ViewModel() {

    fun addSearchData(keyword: Keyword) = viewModelScope.launch {
        repository.insert(keyword)
    }
    fun getSavedKeywords() = repository.getKeywords()

    fun deleteKeyword(keyword: Keyword) = viewModelScope.launch {
        repository.deleteKeyword(keyword)
    }
    private val _searchQuery:MutableStateFlow<String> = MutableStateFlow("인생은아름다워")

    val searchQuery get(): StateFlow<String> = _searchQuery

    fun postKeyword(searchQuery: String){
        _searchQuery.value = searchQuery
    }
    val result = searchQuery.flatMapLatest {
        Log.d("검색어@@@", it)
        repository.letMovieList(it).cachedIn(viewModelScope)
    }
}