package com.example.motivationapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.motivationapp.data.models.Event
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.data.models.User
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получаем доступ к Repositories через приложение
        val app = application as HabitApp
        val userRepository = app.userRepository
        val habitRepository = app.habitRepository
        val eventRepository = app.eventRepository

        // Тестируем взаимодействие с сервером
        lifecycleScope.launch {
            try {
                // Тест 1: Регистрация нового пользователя
                val newUser = User(
                    username = "test_user2",
                    email = "test_user2@example.com",
                    password = "password123"
                )
                val registeredUser = userRepository.registerUser(newUser)
                Log.d("API_TEST", "User registered: $registeredUser")

                // Проверяем, что пользователь зарегистрирован
                if (registeredUser != null && registeredUser.id != null) {
                    val userId = registeredUser.id

                    // Тест 2: Создаем новую привычку для зарегистрированного пользователя
                    val newHabit = Habit(
                        userId = userId,
                        name = "Drink Water",
                        description = "Drink 8 glasses of water daily",
                        repeat = "Daily"
                    )
                    val createdHabit = habitRepository.createHabit(newHabit)
                    Log.d("API_TEST", "Habit created: $createdHabit")

                    // Проверяем, что привычка создана
                    if (createdHabit != null && createdHabit.id != null) {
                        val habitId = createdHabit.id

                        // Тест 3: Создаем событие для привычки
                        val newEvent = Event(
                            habitId = habitId,
                            executionTime = Date() // Передаем текущую дату
                        )
                        val createdEvent = eventRepository.createEvent(newEvent)
                        Log.d("API_TEST", "Event created: $createdEvent")

                        // Тест 4: Получаем все события для привычки
                        val events = eventRepository.getEventsByHabitId(habitId)
                        Log.d("API_TEST", "Events for habit: $events")
                    }

                    // Тест 5: Получаем все привычки для пользователя
                    val habits = habitRepository.getHabitsByUserId(userId)
                    Log.d("API_TEST", "Habits for user: $habits")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Error: ${e.message}", e)
            }
        }
    }
}



