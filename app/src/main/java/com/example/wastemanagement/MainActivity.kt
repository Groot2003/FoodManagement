package com.example.wastemanagement
//waste-management-project-88134
//project-WasteManagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marcinmoskala.arcseekbar.ArcSeekBar


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
        //TODO DELETE THIS LATE IN THE PROJECT
        val loginBTN = binding.login
        loginBTN.setOnClickListener{
            val intent = Intent(this , SignUpActivity::class.java)
            startActivity(intent)
        }

        val Homebtn : View = findViewById(R.id.navigation_home)
        val Dashbtn : View = findViewById(R.id.navigation_sort)
        val SortBTN : View = findViewById(R.id.navigation_profile_pic)

        SortBTN.setOnClickListener{
            val bottomDialog = SortBS()
            bottomDialog.show(
                supportFragmentManager,
                "sort_dialog_fragment"
            )
        }

//            Dashbtn.setOnClickListener{
//                val bottomDialog = BottomDialog("Dash")
//                bottomDialog.show(
//                    supportFragmentManager,
//                    "country_dialog_fragment"
//                )
//            }
//
//            Notifbtn.setOnClickListener{
//                val bottomDialog = BottomDialog("Notif")
//                bottomDialog.show(
//                    supportFragmentManager,
//                    "country_dialog_fragment"
//                )
//            }

        val searchView : SearchView = findViewById<SearchView>(R.id.search_bar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Override onQueryTextSubmit method which is call when submit query is searched
            override fun onQueryTextSubmit(query: String): Boolean {
                // If the list contains the search query than filter the adapter
                // using the filter method with the query as its argument
//                if (mainViewModel.items.value.contains(query)) {
//                    adapter.filter.filter(query)
//                } else {
//                    // Search query not found in List View
//                    Toast.makeText(this@MainActivity, "Not found", Toast.LENGTH_LONG).show()
//                }
//                return false
                Log.d("SEARCHING", "onQueryTextChange: ")
                return false
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            override fun onQueryTextChange(newText: String): Boolean {
//                adapter.filter.filter(newText)
                Log.d("SEARCHING", "onQueryTextChange: ")
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.loadData()
        if (mainViewModel.items.value!!.isEmpty())
        {
            loadingPB!!.visibility = View.VISIBLE
        }

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

//      Updating Adapter
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