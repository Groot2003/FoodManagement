package com.example.wastemanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.wastemanagement.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileBS : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.logout.setOnClickListener{
            userViewModel.logout()
            Log.d("Auth", "User Logged Out")
            val intent = Intent(activity , UserSelectionActivity::class.java).apply {
            }
            startActivity(intent)
        }
        val switch = binding.display
        val sharedPref = activity?.getSharedPreferences("DayNight",Context.MODE_PRIVATE)
        if (sharedPref != null) {
            val nightMode = sharedPref.getBoolean("NightMode", false)
            switch.isChecked = nightMode
        }
        switch.setOnCheckedChangeListener{ _ , isChecked ->
            Log.d("Theme", "Changing Theme")
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            val sharedPref = activity?.getSharedPreferences("DayNight",Context.MODE_PRIVATE)
            if (sharedPref != null) {
                Log.d("Written to Shared Pref", "onCreateView: ")
                with (sharedPref.edit()) {
                    putBoolean("NightMode",isChecked)
                    apply()
                }
            }
        }
        return binding.root
    }
}