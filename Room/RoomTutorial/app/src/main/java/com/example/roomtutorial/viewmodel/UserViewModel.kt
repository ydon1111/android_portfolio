package com.example.roomtutorial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomtutorial.data.UserDatabase
import com.example.roomtutorial.repository.UserRepository
import com.example.roomtutorial.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val UserDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao = UserDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
             repository.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun deleteAllUser(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllUser()
        }
    }



}