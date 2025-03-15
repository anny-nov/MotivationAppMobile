package com.example.motivationapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.motivationapp.HabitApp
import com.example.motivationapp.data.repository.CharacterRepository
import com.example.motivationapp.databinding.FragmentCharacterBinding
import kotlinx.coroutines.launch

class CharacterFragment : Fragment() {
    private lateinit var characterRepository: CharacterRepository
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharacterBinding.inflate(inflater, container, false)

        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", -1)
        Log.d("CharacterFragment", "Loaded user_id from SharedPreferences: $userId")

        characterRepository = (requireActivity().application as HabitApp).characterRepository

        lifecycleScope.launch {
            val character = characterRepository.getCharacterByUserId(userId)
            if (character != null) {
                Log.d("CharacterFragment", "Character loaded: $character")
                binding.tvCharacterName.text = character.name
                binding.tvCharacterLevel.text = "Level: ${character.level}"
                binding.tvCharacterExperience.text = "EXP: ${character.experience}/${character.maxExperience}"
                binding.tvCharacterHealth.text = "HP: ${character.currentHealth}/${character.maxHealth}"
            } else {
                Toast.makeText(context, "Character not found", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
