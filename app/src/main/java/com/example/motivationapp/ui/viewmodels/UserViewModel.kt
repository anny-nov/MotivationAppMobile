package com.example.motivationapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.motivationapp.data.models.User
import com.example.motivationapp.data.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userLiveData = MutableLiveData<User?>()
    val errorLiveData = MutableLiveData<String?>()

    fun register(username: String, email: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                // Регистрируем пользователя, создавая объект User из строковых данных
                val user = userRepository.registerUser(User(username = username, email = email, password = password))
                userLiveData.postValue(user)
                callback(user) // Передаем user в callback
                if (user == null) {
                    errorLiveData.postValue("Registration failed")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }

    fun login(email: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser(email, password)
                userLiveData.postValue(user)
                callback(user)
                if (user == null) {
                    errorLiveData.postValue("Login failed")
                }
            } catch (e: Exception) {
                errorLiveData.postValue(e.message)
            }
        }
    }
}


