package com.example.motivationapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.motivationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем доступ к SharedPreferences для проверки сохраненных данных пользователя
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Проверяем, если у нас есть сохраненные данные о пользователе
        val userId = sharedPref.getInt("user_id", -1)
        val username = sharedPref.getString("username", null)
        val email = sharedPref.getString("email", null)

        if (userId != -1 && username != null && email != null) {
            // Если данные пользователя есть, отправляем его на экран персонажа
            val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
            navController.navigate(R.id.action_loginFragment_to_characterFragment)
        } else {
            // Если данных нет (пользователь не зарегистрирован), отправляем на экран регистрации
            val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}





