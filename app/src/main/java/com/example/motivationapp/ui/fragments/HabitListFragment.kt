package com.example.motivationapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.motivationapp.databinding.FragmentHabitListBinding

class HabitListFragment : Fragment() {

    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addHabitButton.setOnClickListener {
            CreateHabitDialogFragment().show(parentFragmentManager, "CreateHabitDialog")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
