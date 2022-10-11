package com.example.wastemanagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainViewModel:ViewModel() {

    lateinit var posts:MutableList<Post>
    private val db = Firebase.firestore

    private var _items: MutableLiveData<List<Post>> = MutableLiveData(listOf())
    val items: LiveData<List<Post>> = _items

    fun loadData(){
//        var post = Post("Customer Support from base", "Delaware, MCC", "11-02-2023", "Burgers and Fries", "10-02-2023","Hawthorn, Victoria",3122,505,9023982984,"11:30","Breakfast","Meet in front of Gate 4 of the MCG Stadium and Contact the given number. A person will come at the front gate to receive you. ")
        posts= mutableListOf()
        _items.value = emptyList<Post>()
        db.collection("Posts")
            .get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    val documents = task.result
                    if (documents != null) {
                        for (document in documents) {
                            val post = document.toObject<Post>()
                            post.id = document.id
                            posts.add(post)
                            _items.value = _items.value?.plus(post) ?: mutableListOf(post)
                        }
                        Log.d("Database Loading Data:", "${_items.value}")
                        Log.d("Database Loading Data Posts:", "${posts}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Database Error", "get failed with ", exception)
            }
        Log.d("Database Error",  "$posts")
    }

    fun updatePost(post:Post):Boolean{
        var execution = true
        Log.d("Database Updated", "Trying to Update")
        if(post.id != "") {
            db.collection("Posts").document(post.id)
                .set(post)
                .addOnSuccessListener { _ ->
                    Log.d("Database Updated", "DocumentSnapshot written with ID: ${post.id}")
                    execution = true
                }
                .addOnFailureListener { e ->
                    Log.w("Database Updated", "Error adding document", e)
                    execution = false
                }
        }
        return execution
    }

    fun postData(post:Post){
        db.collection("Posts").add(post)
            .addOnSuccessListener { documentReference ->
                Log.d("Database Adding", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Database Adding", "Error adding document", e)
            }
    }
}


