package com.example.wastemanagement

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest


class UserViewModel: ViewModel() {

    private lateinit var user: FirebaseUser
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userType: String = ""

    fun updateUser(type:String) {
        user = firebaseAuth.currentUser!!
        Log.d("Auth", "Trying to Update User profile ${user.displayName}")
        if(user.displayName?.contains("|")==false)
        {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(user.displayName+"|$type")
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("authenticate", "User profile updated.")
                    }
                }
                .addOnFailureListener {
                    Log.d("authenticate", "Could Not Update User profile updated.")
                }
        } else if(user.displayName == null){
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("xyz|$type")
                .build()
            Log.d("Auth", "Profile Update Sent When Display Null")

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("authenticate", "User profile updated.")
                    }
                }
                .addOnFailureListener {
                    Log.d("authenticate", "Could Not Update User profile updated.")
                }
        }
    }

    fun getRealUser(type:String):Boolean{
        val user = firebaseAuth.currentUser
        if (user != null) {
            userType = getUserType()
            val authenticated = authenticateType(type)
            Log.d("auth", "userIs: ${user.displayName} ReqIs:$type")
            Log.d("Auth", "?: $authenticated")
            return authenticated
        }
        return false
    }

    private fun authenticateType(type:String):Boolean { return userType == type }

    fun getUserType():String{
        val user = firebaseAuth.currentUser
        if (user != null) {
            val usrType = user.displayName?.split("|")
            userType = usrType?.get(usrType.size - 1).toString()
        }
        return userType
    }

    fun logout(){
        firebaseAuth.signOut()
    }
}