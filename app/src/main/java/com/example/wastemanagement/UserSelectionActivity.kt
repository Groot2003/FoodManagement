package com.example.wastemanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.wastemanagement.databinding.UserSelectionAcitivityBinding

class UserSelectionActivity : AppCompatActivity() {

    private lateinit var binding: UserSelectionAcitivityBinding
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserSelectionAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val consumerBtn = binding.Consumer
        val providerBtn = binding.Provider
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        consumerBtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java).apply {
                putExtra("type","consumer")
            }
            startActivity(intent)
        }

        providerBtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java).apply {
                putExtra("type","provider")
            }
            startActivity(intent)
        }
    }
}