package com.example.motivationapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationapp.data.models.Event
import com.example.motivationapp.data.models.ForecastResponse
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.data.repository.EventRepository
import com.example.motivationapp.data.repository.HabitRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HabitViewModel(
    private val habitRepository: HabitRepository,
    private val eventRepository: EventRepository,
    private val userId: Int
) : ViewModel() {

    private val _habits = MutableLiveData<List<Pair<Habit, List<Event>>>>()
    val habits: LiveData<List<Pair<Habit, List<Event>>>> = _habits

    fun loadHabits() {
        viewModelScope.launch {
            val habits = habitRepository.getHabitsByUserId(userId)
            val habitsWithEvents = habits.map { habit ->
                val events = eventRepository.getEventsByHabitId(habit.id ?: -1)
                habit to events
            }
            _habits.value = habitsWithEvents
        }
    }

    fun markHabitAsDone(habit: Habit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val now = Date()
            val event = Event(habitId = habit.id!!, executionTime = now)
            val created = eventRepository.createEvent(event)
            if (created == null) {
                onFailure("Невозможно выполнить привычку сейчас.")
            } else {
                loadHabits()
            }
        }
    }

    fun getForecast(habitId: Int, callback: (ForecastResponse?) -> Unit) {
        viewModelScope.launch {
            try {
                val forecast = habitRepository.getForecast(habitId)
                callback(forecast)
            } catch (e: Exception) {
                callback(null)
            }
        }
    }

    fun deleteHabit(habitId: Int, callback: () -> Unit) {
        viewModelScope.launch {
            try {
                habitRepository.deleteHabit(habitId)
                loadHabits()
                callback()
            } catch (e: Exception) {
                callback()
            }
        }
    }
}