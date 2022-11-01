package com.example.wastemanagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainViewModel:ViewModel() {

    private val db = Firebase.firestore

    private var _items: MutableLiveData<List<Post>> = MutableLiveData(listOf())
    val items: LiveData<List<Post>> = _items

    fun loadData(){
//        var post = Post("","Customer Support from base", "Delaware, MCC", "2022/11/23", "Burgers and Fries", "2022/11/23","Hawthorn, Victoria",3122,505,"+61 239 829 584","11:30","Breakfast","Meet in front of Gate 4 of the MCG Stadium and Contact the given number. A person will come at the front gate to receive you. ")
//        db.collection("Posts").add(post)
//        db.collection("Posts").add(post)
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
                            _items.value = _items.value?.plus(post) ?: mutableListOf(post)
                        }
                        Log.d("Database Loading Data:", "${_items.value}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Database Error", "get failed with ", exception)
            }
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

    fun sortData(criteria:String){
        _items.value = emptyList<Post>()
        db.collection("Posts").orderBy(criteria)
            .get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    val documents = task.result
                    if (documents != null) {
                        for (document in documents) {
                            val post = document.toObject<Post>()
                            post.id = document.id
                            _items.value = _items.value?.plus(post) ?: mutableListOf(post)
                        }
                        Log.d("Database Sorting Data:", "${_items.value}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Database Error", "get failed with ", exception)
            }
    }

    fun filterData(criteria: String,value: String) {
        _items.value = emptyList<Post>()
        db.collection("Posts").whereEqualTo(criteria, value)
            .get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    val documents = task.result
                    if (documents != null) {
                        for (document in documents) {
                            val post = document.toObject<Post>()
                            post.id = document.id
                            _items.value = _items.value?.plus(post) ?: mutableListOf(post)
                        }
                        Log.d("Database Loading Data:", "${_items.value}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Database Error", "get failed with ", exception)
            }
    }

    fun deletePost(post: Post):String {
        var retText = ""
        db.collection("Posts").document(post.id)
            .delete()
            .addOnSuccessListener {
                retText="Post is successfully deleted!"
                Log.d("Database Deletion", "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                retText = "Error deleting the Post"
                Log.w("Database Deletion", "Error deleting document", e)
            }
        return retText
    }
}


