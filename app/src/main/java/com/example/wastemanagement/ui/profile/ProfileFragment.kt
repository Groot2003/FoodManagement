package com.example.wastemanagement.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import com.example.wastemanagement.*
import com.example.wastemanagement.databinding.FragmentProfileBinding

class ProfileFragment() : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var userViewModel : UserViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val logoutBTN = binding.logout
        logoutBTN.setOnClickListener{
            userViewModel.logout()
            Log.d("Auth", "User Logged Out")
        }

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        val displayBTN = binding.display
        displayBTN.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("Clicked", "onDestroyView: Home")
    }
}