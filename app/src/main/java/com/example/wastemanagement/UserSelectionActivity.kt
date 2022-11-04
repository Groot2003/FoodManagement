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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class UserSelectionActivity : AppCompatActivity() {

    private lateinit var binding: UserSelectionAcitivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserSelectionAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val consumerBtn = binding.Consumer
        val providerBtn = binding.Provider
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }
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