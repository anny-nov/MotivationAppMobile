package com.example.motivationapp.ui.fragments

import com.example.motivationapp.R
import android.app.Dialog
import com.example.motivationapp.HabitApp
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.motivationapp.data.models.Habit
import com.example.motivationapp.data.repository.HabitRepository
import com.example.motivationapp.databinding.FragmentCreateHabitBinding
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.widget.ArrayAdapter


class CreateHabitDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentCreateHabitBinding
    private lateinit var habitRepository: HabitRepository
    private var selectedDifficulty = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = FragmentCreateHabitBinding.inflate(LayoutInflater.from(context))

        val defaultName = arguments?.getString(ARG_DEFAULT_NAME)
        defaultName?.let {
            binding.habitNameEditText.setText(it)
        }

        setupStars()
        dialog.setContentView(binding.root)

        habitRepository = (requireActivity().application as HabitApp).habitRepository

        val repeatOptions = listOf(
            "1 раз в день",
            "2 раза в день",
            "3 раза в неделю",
            "2 раза в неделю",
            "1 раз в неделю",
            "1 раз в месяц"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, repeatOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.repeatSpinner.adapter = adapter

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.createButton.setOnClickListener {
            val name = binding.habitNameEditText.text.toString().trim()
            val description = binding.habitDescriptionEditText.text.toString().trim()
            val repeat = binding.repeatSpinner.selectedItem.toString()

            if (name.isEmpty() || repeat.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, заполните обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDifficulty == 0) {
                Toast.makeText(context, "Пожалуйста, выберите сложность", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPrefs.getInt("user_id", -1)

            if (userId == -1) {
                Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newHabit = Habit(
                name = name,
                description = description,
                repeat = repeat,
                userId = userId,
                difficulty = selectedDifficulty
            )

            Log.d("CreateHabit", "Создание привычки: name=$name, desc=$description, repeat=$repeat, difficulty=$selectedDifficulty, userId=$userId")

            lifecycleScope.launch {
                val created = habitRepository.createHabit(newHabit)
                if (created != null) {
                    Toast.makeText(context, "Привычка создана", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, "Ошибка при создании привычки", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return dialog
    }

    private fun setupStars() {
        val stars = listOf(binding.star1, binding.star2, binding.star3)

        fun updateStars(level: Int) {
            selectedDifficulty = level
            stars.forEachIndexed { index, imageView ->
                imageView.setImageResource(
                    if (index < level) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                )
            }
        }

        stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                updateStars(index + 1)
            }
        }
        updateStars(1)
    }

    companion object {
        private const val ARG_DEFAULT_NAME = "default_name"

        fun newInstance(defaultName: String): CreateHabitDialogFragment {
            val fragment = CreateHabitDialogFragment()
            val args = Bundle()
            args.putString(ARG_DEFAULT_NAME, defaultName)
            fragment.arguments = args
            return fragment
        }
    }
}
