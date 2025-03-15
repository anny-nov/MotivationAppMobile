package com.example.motivationapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.motivationapp.data.models.User
import com.example.motivationapp.databinding.FragmentRegisterBinding
import com.example.motivationapp.ui.viewmodels.UserViewModel
import androidx.navigation.fragment.findNavController
import com.example.motivationapp.HabitApp
import com.example.motivationapp.R
import com.example.motivationapp.data.repository.CharacterRepository
import com.example.motivationapp.ui.factories.UserViewModelFactory
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var characterRepository: CharacterRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // Получаем UserRepository из приложения
        val userRepository = (requireActivity().application as HabitApp).userRepository
        val factory = UserViewModelFactory(userRepository)
        val app = requireActivity().application as HabitApp
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        characterRepository = app.characterRepository


        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val characterName = binding.etCharacterName.text.toString()

            if (password == confirmPassword) {
                userViewModel.register(username, email, password) { newUser ->
                    if (newUser != null) {
                        saveUserData(newUser)
                        createCharacterForNewUser(newUser.id!!, characterName)
                        navigateToCharacter()
                    } else {
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return binding.root
    }

    private fun saveUserData(user: User) {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("user_id", user.id!!)
            putString("username", user.username)
            putString("email", user.email)
            apply()
        }
    }

    private fun navigateToCharacter() {
        findNavController().navigate(R.id.action_registerFragment_to_characterFragment)
    }

    private fun createCharacterForNewUser(userId: Int, characterName: String) {
        lifecycleScope.launch {
            val character = characterRepository.createCharacter(userId, characterName)
            if (character == null) {
                Toast.makeText(context, "Failed to create character", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("RegisterFragment", "Character created: ${character.name}")
            }
        }
    }
}
