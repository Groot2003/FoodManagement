package com.example.wastemanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import com.example.wastemanagement.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileBS(private val MAct: MainActivity) : BottomSheetDialogFragment() {
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
            val intent = Intent(MAct , UserSelectionActivity::class.java).apply {
            }
            startActivity(intent)
        }

        binding.display.setOnCheckedChangeListener{ _ , isChecked ->
            Log.d("Theme", "Changing Theme")
            MAct.changeTheme(isChecked)
        }
        return binding.root
    }
}