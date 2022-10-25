package com.example.wastemanagement
//waste-management-project-88134
//project-WasteManagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.marcinmoskala.arcseekbar.ArcSeekBar
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TheAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mainViewModel: MainViewModel
    val progress: MutableLiveData<Int> = MutableLiveData(0)
    var loadingPB: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        loadingPB = binding.idProgressBar
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.loadData()
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_change_page,R.id.navigation_sort,R.id.navigation_home,R.id.navigation_search,R.id.navigation_profile_pic
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Recycler View
        val listView = findViewById<RecyclerView>(R.id.PostList)
        linearLayoutManager = LinearLayoutManager(this)
        listView.layoutManager = linearLayoutManager
        adapter = TheAdapter(mainViewModel.items.value!!) { showDetail(it) }
        listView.adapter = adapter
        Log.d("View Model MA", "$mainViewModel")

        //Sorting Seek Bar
        val seek = findViewById<ArcSeekBar>(R.id.seekBar)
        if(seek!=null){
            Log.d("seek", seek.toString())
            progress.value = seek.progress
        }
        else {
            Log.d("seek", "is null")
        }
        seek.onProgressChangedListener?.invoke(seek.progress)
        progress.observe(this) { value ->
            run {
                Log.d("seek", value.toString())
                when(progress.value){
                    in 2..25 -> Log.d("seek", "Breakfast: ")
                    in 26..50 -> Log.d("seek", "Lunch: ")
                    in 51..75 -> Log.d("seek", "Snacks: ")
                    in 76..100 -> Log.d("seek", "Dinner: ")
            } }
        }
        val sortBTN = findViewById<TextView>(R.id.easySort)
        sortBTN.setOnClickListener{
            Log.d("seek", "Seek Value on Click of SortBTN: "+seek.progress.toString())
            mainViewModel.sortData("date")
        }

//        Updating Adapter
        mainViewModel.items.observe(this) { list: List<Post> ->
            run {
                Log.d("Database", "onResume: $list")
                adapter.setData(list)
                if (mainViewModel.items.value!!.isNotEmpty())
                {
                    loadingPB!!.visibility = View.GONE

                }
            }
        }
    }

    private fun showDetail(item: Post) {
        val intent = Intent(this , DetailActivity::class.java).apply {
            putExtra("Post",item)
        }
        startActivity(intent)
    }
}