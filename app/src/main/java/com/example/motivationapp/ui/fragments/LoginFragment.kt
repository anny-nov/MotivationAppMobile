package com.example.motivationapp.ui.fragments
import com.example.motivationapp.data.models.User

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.motivationapp.ui.viewmodels.UserViewModel
import androidx.navigation.fragment.findNavController
import com.example.motivationapp.HabitApp
import com.example.motivationapp.R
import com.example.motivationapp.databinding.FragmentLoginBinding
import com.example.motivationapp.ui.factories.UserViewModelFactory


class LoginFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Получаем UserRepository из приложения
        val userRepository = (requireActivity().application as HabitApp).userRepository
        val factory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.login(email, password) { user ->
                    if (user != null) {
                        saveUserData(user)
                        navigateToCharacter()
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
        findNavController().navigate(R.id.action_loginFragment_to_characterFragment)
    }
}