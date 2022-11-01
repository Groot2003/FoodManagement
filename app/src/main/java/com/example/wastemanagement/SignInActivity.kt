package com.example.wastemanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.wastemanagement.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var userViewModel : UserViewModel
    private lateinit var type : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val tp = intent.getStringExtra("type")
        type = intent.getStringExtra("type")?:"consumer"
        Log.d("auth", "Got Extra in SignIn: $tp")
        firebaseAuth = FirebaseAuth.getInstance()
        binding.link2signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        binding.gSignInBtn.setOnClickListener {
            signInGoogle()
        }

        binding.SignInBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val authenticated = userViewModel.getRealUser(type)
                        if (authenticated){
                            val intent = Intent(this , MainActivity::class.java)
                            startActivity(intent)
                        } else{
                            Toast.makeText(this, "Sign In Using the Appropriate Account" , Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun signInGoogle(){
        Log.d("Auth", "signInGoogle: ")
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            Log.d("Auth", "Got Results: ")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                Log.d("Auth", "Fetched Account and Stored: nd trying to update ")
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        Log.d("Auth", "Update UI")
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("Auth", "Signed in with google")
                userViewModel.updateUser(type)
                val authenticated = userViewModel.getRealUser(type)
                if (authenticated){
                    val intent = Intent(this , MainActivity::class.java)
                    startActivity(intent)
                } else{
                    Toast.makeText(this, "Sign In Using the Appropriate Account" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }

//    private fun updateUser() {
//        val user = firebaseAuth.currentUser
//        Log.d("Auth", "Trying to Update User profile")
//        if (user != null) {
//            if(user.displayName?.contains("|")==false)
//            {
//                val profileUpdates = userProfileChangeRequest {
//                    if (user != null) {
//                        displayName = user.displayName+"|consumer"
////                        displayName = user.displayName+"|provider"
//                    }
//                }
//                user!!.updateProfile(profileUpdates)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d("authenticate", "User profile updated.")
//                        }
//                    }
//            }
//        }
//    }

//    private fun getRealUser(){
//        val user = firebaseAuth.currentUser
//        Log.d("Auth", "group: $user")
//        if (user != null) {
//            val usrType = user.displayName?.split("|")
//            val group = usrType?.get(usrType.size - 1)
//            Log.d("Auth", "group: $group")
//        }
//    }

    override fun onStart() {
        super.onStart()
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
        val authenticated = userViewModel.getRealUser(type)
        if (authenticated){
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        userViewModel.getRealUser(type)
    }
}