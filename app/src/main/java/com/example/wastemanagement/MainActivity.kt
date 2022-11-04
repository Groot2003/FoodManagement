package com.example.wastemanagement
//waste-management-project-88134
//project-WasteManagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wastemanagement.databinding.ActivityMainBinding
import com.example.wastemanagement.databinding.FragmentProfileBinding
import com.marcinmoskala.arcseekbar.ArcSeekBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TheAdapter
    private lateinit var mainViewModel: MainViewModel
    var loadingPB: ProgressBar? = null
    private var nightMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        loadingPB = binding.idProgressBar
        setContentView(binding.root)
        val sharedPref = applicationContext?.getSharedPreferences("DayNight",Context.MODE_PRIVATE)
        if (sharedPref != null) {
            nightMode = sharedPref.getBoolean("NightMode", false)
            Log.d("Shared Pref", "Not null: ")
        }
        setTheme(nightMode)

        val sortBTN : View = findViewById(R.id.navigation_sort)
        val profileBTN : View = findViewById(R.id.navigation_profile_pic)

        sortBTN.setOnClickListener{
            val bottomDialog = SortBS(mainViewModel)
            bottomDialog.show(
                supportFragmentManager,
                "sort_dialog_fragment"
            )
        }
        profileBTN.setOnClickListener{
            val bottomDialog = ProfileBS()
            bottomDialog.show(
                supportFragmentManager,
                "country_dialog_fragment"
            )
        }

        val newPostBTN =  binding.newPost
        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        if(userViewModel.getUserType()!="provider"){
            newPostBTN.visibility = GONE
        }
        newPostBTN.setOnClickListener{
            val intent = Intent(this , PostActivity::class.java)
            startActivity(intent)
        }

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
        val linearLayoutManager = LinearLayoutManager(this)
        listView.layoutManager = linearLayoutManager
        adapter = TheAdapter(mainViewModel.items.value!!) { showDetail(it) }
        listView.adapter = adapter
        Log.d("View Model MA", "$mainViewModel")

//        Sorting Seek Bar
        val seek = findViewById<ArcSeekBar>(R.id.seekBar)
        val filterBTN = findViewById<TextView>(R.id.easySort)
        filterBTN.setOnClickListener{
            Log.d("seek", "Seek Value on Click of SortBTN: "+seek.progress.toString())
            val value :String = when(seek.progress){
                in 2..25 -> "Breakfast"
                in 26..50 -> "Lunch"
                in 51..75 -> "Snacks"
                in 76..100 -> "Dinner"
                else -> "Lunch"
            }
            mainViewModel.filterData("meal",value)
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

    private fun setTheme(nightMode: Boolean){
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}