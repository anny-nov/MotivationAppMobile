package com.example.motivationapp.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.motivationapp.HabitApp
import com.example.motivationapp.R
import com.example.motivationapp.data.models.ForecastResponse
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.databinding.FragmentHabitListBinding
import com.example.motivationapp.ui.viewmodels.HabitViewModel

class HabitListFragment : Fragment() {

    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        val app = requireActivity().application as HabitApp

        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HabitViewModel(app.habitRepository, app.eventRepository, userId) as T
                }
            }
        )[HabitViewModel::class.java]

        val adapter = HabitListAdapter { habit ->
            viewModel.markHabitAsDone(habit) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadHabits()

        binding.addHabitButton.setOnClickListener {
            CreateHabitDialogFragment().show(parentFragmentManager, "CreateHabitDialog")
        }

        // ПРОВЕРКА ПРОГНОЗА
        checkForecastsIfNeeded(sharedPref)
    }

    private fun checkForecastsIfNeeded(sharedPref: SharedPreferences) {
        Log.d("ForecastCheck", "checkForecastsIfNeeded called")
        val lastCheckMillis = sharedPref.getLong("last_forecast_check", 0L)
        val oneWeekMillis = 7 * 24 * 60 * 60 * 1000L
        val now = System.currentTimeMillis()

        // if (now - lastCheckMillis < oneWeekMillis) return

        // Обновим дату прогноза сразу, чтобы не повторять в случае сбоя
        sharedPref.edit().putLong("last_forecast_check", now).apply()

        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            processForecastsSequentially(habits.map { it.first })
        }
    }

    private fun processForecastsSequentially(habits: List<Habit>, index: Int = 0) {
        if (index >= habits.size) return

        val habit = habits[index]
        habit.id?.let {
            viewModel.getForecast(it) { forecast ->
                if (forecast != null && forecast.declining) {
                    showForecastDialog(habit, forecast) {
                        // После диалога переходим к следующей привычке
                        processForecastsSequentially(habits, index + 1)
                    }
                } else {
                    processForecastsSequentially(habits, index + 1)
                }
            }
        }
    }

    private fun showForecastDialog(habit: Habit, forecast: ForecastResponse, onDismiss: () -> Unit) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Мы заметили, что тебе сложно придерживаться привычки: ${habit.name}")
            .setMessage("Хочешь заменить её на что-то попроще?")
            .setCancelable(false)
            .setView(R.layout.dialog_forecast_alternatives)
            .create()

        dialog.setOnShowListener {
            val view = dialog.findViewById<View>(R.id.dialog_forecast_root)
            view?.findViewById<Button>(R.id.alternative1)?.apply {
                text = forecast.alternative1
                setOnClickListener {
                    replaceHabit(habit, forecast.alternative1)
                    dialog.dismiss()
                    onDismiss()
                }
            }
            view?.findViewById<Button>(R.id.alternative2)?.apply {
                text = forecast.alternative2
                setOnClickListener {
                    replaceHabit(habit, forecast.alternative2)
                    dialog.dismiss()
                    onDismiss()
                }
            }
            view?.findViewById<Button>(R.id.alternative3)?.apply {
                text = forecast.alternative3
                setOnClickListener {
                    replaceHabit(habit, forecast.alternative3)
                    dialog.dismiss()
                    onDismiss()
                }
            }
            view?.findViewById<Button>(R.id.cancel_button)?.setOnClickListener {
                dialog.dismiss()
                onDismiss()
            }
        }

        dialog.show()
    }

    private fun replaceHabit(habit: Habit, newHabitName: String) {
        habit.id?.let {
            viewModel.deleteHabit(it) {
                val dialog = CreateHabitDialogFragment.newInstance(newHabitName)
                dialog.show(parentFragmentManager, "CreateHabitDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
