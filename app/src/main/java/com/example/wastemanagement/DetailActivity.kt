package com.example.wastemanagement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.wastemanagement.databinding.DetailActivityBinding
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint
import android.content.pm.PackageManager;
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import java.io.IOException
import kotlin.random.Random

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: DetailActivityBinding
    private lateinit var post:Post
    private var CALL_PERMISSION_REQUEST_CODE = 1
    private var phNum = ""
    private lateinit var mainViewModel: MainViewModel

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val data = result.data?.getParcelableExtra<Post>("Post")
        if (result.resultCode == RESULT_OK) {
            if(data!=null){
                post = data
                Toast.makeText(this,"${data.postHeader} Updated",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = intent.getParcelableExtra("Post")!!
    }

    override fun onResume() {
        super.onResume()
        val imgV : ImageView = binding.img
        val postHeader: TextView = binding.PostHeader
        val meal: TextView = binding.MealType
        val person: TextView = binding.person
        val date: TextView = binding.date
        val time: TextView = binding.time
        val location: TextView = binding.location
        val company: TextView = binding.company
        val foodItems = binding.food
        val contact = binding.contact
        val description: TextView = binding.description
//        val dateInput:TextView = binding.DateInput // POST Created DATE
        val updateBTN = binding.updateBtn
        val deleteBTN = binding.deleteBtn

        val img :Int = when (Random.nextInt(0, 3)) {
            1 -> R.drawable.dumplings
            2 -> R.drawable.hotdog
            3 -> R.drawable.pizza
            else -> R.drawable.drinks
        }
        imgV.setImageResource(img)

        //Putting in the Values
        post.let {
            postHeader.text = it.postHeader
            meal.text = it.meal
            date.text = it.date
            time.text = it.time
            person.text = it.people.toString()
            val locationText = "${it.location}. PostCode: ${it.postcode}"
            location.text = locationText
            company.text = it.company
            foodItems.text = it.foodItem
            contact.text = it.contact
            phNum = it.contact
            description.text = it.description
        }

        //Contect Pressing
        contact.setOnClickListener{
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phNum")
            if (ActivityCompat.checkSelfPermission(
                    this@DetailActivity,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askForCallPermission()
            }
            startActivity(callIntent)
        }

        //Location Pressing
        location.setOnClickListener {
            var geocoder1 = Geocoder(this)
            try {
                val addresses1: List<Address> = geocoder1.getFromLocationName(location.text.toString(), 1)
                if (addresses1 != null && addresses1.isNotEmpty()) {
                    val address1: Address = addresses1[0]
                    // Use the address as needed
                    val latitude_start = address1.getLatitude()
                    val longitude_start = address1.getLongitude()
                    val message = "Latitude: " + address1.getLatitude()
                        .toString() + ", Longitude: " + address1.getLongitude()
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    Log.d("Location", message)

                    val gmmIntentUri =   Uri.parse("geo:$latitude_start,$longitude_start")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    mapIntent.resolveActivity(packageManager)?.let {
                        startActivity(mapIntent)
                    }

                } else {
                    Toast.makeText(this, "Unable to Process Location. Please contact the distributor directly", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this, "Unable to Process Location. Please contact the distributor directly", Toast.LENGTH_LONG).show()
            }
        }

        //Update Button Button Click
        updateBTN.setOnClickListener{
            val intent = Intent(this , UpdateActivity::class.java).apply {
                putExtra("Post",post)
            }
            getContent.launch(intent)
        }

        deleteBTN.setOnClickListener{
            val displayText = mainViewModel.deletePost(post)
            Toast.makeText(this,displayText,Toast.LENGTH_SHORT)
            finish()
        }
    }

    fun askForCallPermission() {
        ActivityCompat.requestPermissions(
            this@DetailActivity, arrayOf(Manifest.permission.CALL_PHONE),
            CALL_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$phNum")
                startActivity(callIntent)
            } else {
                Toast.makeText(
                    this@DetailActivity,
                    "You cannot call without accepting this permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}