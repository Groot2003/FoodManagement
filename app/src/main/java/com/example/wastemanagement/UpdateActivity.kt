package com.example.wastemanagement

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.TimePicker.OnTimeChangedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.wastemanagement.databinding.PostEditingBinding
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class UpdateActivity: AppCompatActivity(),AdapterView.OnItemSelectedListener {

    private lateinit var binding: PostEditingBinding
    private lateinit var meal: String
    private lateinit var mainViewModel: MainViewModel
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = PostEditingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = intent.getParcelableExtra<Post>("Post")!!

        val titleInput: TextView = binding.TitleInput // No Checking Possible
        val companyInput: TextView = binding.CompanyInput // No Checking Possible
        val peopleInput: TextView = binding.PeopleInput // No Checking Possible
        val foodItemInput: TextView = binding.FoodItemInput // No Check Possible
        val locationInput: TextView = binding.LocationInput // No Check Possible
        val postInput: TextView = binding.PostInput //Postcode Check
        val contactInput: TextView = binding.ContactInput // Phone Number Check
        val mealTypeInput = binding.MealTypeInput // Spinner Use Options
        val timeInput = binding.TimeInput // Time Checking Possible
        val submitBtn: Button = binding.Post //Submit Button
        val backBtn: Button = binding.Back
        val dateInput:TextView = binding.DateInput // Date Checking Possible
        val page = binding.PageHeader
        val description: TextView = binding.DescriptionInput
        var time = ""//To Store Time

        //Initializing the Values for editing
        post.let {
            titleInput.text = it.postHeader
            companyInput.text = it.company
            peopleInput.text = it.people.toString()
            foodItemInput.text = it.foodItem
            locationInput.text = it.location
            postInput.text = it.postcode.toString()
            contactInput.text = it.contact.toString()
//            mealTypeInput =
            dateInput.text = it.date
            page.text = "Updating Post"
            timeInput.hour = Integer.parseInt(it.time.substring(0,2))
            timeInput.minute=Integer.parseInt(it.time.substring(3));
            time = it.time
            description.text = it.description
        }

        //Getting Time
        timeInput.setIs24HourView(true)
        timeInput.setOnTimeChangedListener(OnTimeChangedListener { _, hourOfDay, minute ->
            // Store Time
            time = if (hourOfDay<10) "0$hourOfDay:$minute" else "$hourOfDay:$minute"
            Log.d("Time", time) // set the current time in text view
        })

        //Setting Meal Type Options
        ArrayAdapter.createFromResource(
            this,
            R.array.meals_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mealTypeInput.adapter = adapter
        }

        //Getting Meal Type
        mealTypeInput.onItemSelectedListener = this@UpdateActivity

        backBtn.setOnClickListener{
            finish()
        }
        submitBtn.setOnClickListener{
            Log.d("View Model", "$mainViewModel")
            val postcode = postcodeIsValid(postInput) //if(postcodeIsValid(postInput)) Integer.parseInt(postInput.text.toString()) else 0
            val title = titleInput.text.toString()
            val company = companyInput.text.toString()
            val people =  if(peopleInput.text.toString()!="")Integer.parseInt(peopleInput.text.toString()) else 0
            val foodItem = foodItemInput.text.toString()
            val location = locationInput.text.toString()
            val contact = numberIsValid(contactInput) //if(numberIsValid(contactInput)) contactInput.text.toString() else ""
            val date = dateIsValid(dateInput.text.toString(),dateInput)//if(dateIsValid(dateInput.text.toString(),dateInput)) dateInput.text.toString() else ""
            val desc = description.text.toString()
//            meal and time from above

//            Enter to Database
            if(date && postcode && contact) {
                Log.d("Database Sending", "Database Sending")
                val p = Post(
                    post.id,
                    title,
                    company,
                    dateInput.text.toString(),
                    foodItem,
                    post.postedDate,
                    location,
                    Integer.parseInt(postInput.text.toString()),
                    people,
                    contactInput.text.toString(),
                    time,
                    meal,
                    desc
                )
//                val p = Post(title,company, dateInput.text.toString() ,foodItem, post.postedDate , location ,Integer.parseInt(postInput.text.toString()) ,people , contactInput.text.toString() ,time, meal, desc)
                if(mainViewModel.updatePost(p))
                {
                    mainViewModel.loadData()
                    Log.d("Database", "")
                    val mainAct = Intent().putExtra("Post",p)
                    setResult(RESULT_OK,mainAct)
                    finish()
//                    val listView = findViewById<RecyclerView>(R.id.PostList)
//                    listView.adapter?.notifyDataSetChanged()
                }
                else
                {
                    var toast = Toast.makeText(this,"Could not update the Post",Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }

    override fun onBackPressed() {
        val mainAct = Intent().putExtra("Post",post)
        setResult(RESULT_OK,mainAct)
        super.onBackPressed()
    }

    private fun postcodeIsValid(postInput:TextView): Boolean{
        val postcode = postInput.text.toString()
        if(!postcode.matches(Regex("[0-9]{4}")))
        {
            Log.d("Postcode", "${postcode.matches(Regex("[0-9]{4}"))}")
            postInput.error = "Please input a valid Post Code"
            return false
        }
        var geocoder1 = Geocoder(this)
        return try {
            val addresses1: List<Address> = geocoder1.getFromLocationName(postcode, 1)
            if (addresses1 != null && !addresses1.isEmpty()) {
                true
            } else {
                postInput.error = "Please input a valid Post Code"
                Toast.makeText(this, "Unable to Process Location. Postcode You entered doesn't map anywhere", Toast.LENGTH_LONG).show()
                false
            }
        } catch (e: IOException) {
            // handle exception
            postInput.error = "Please input a valid Post Code"
            Toast.makeText(this, "Unable to Process Location.", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun numberIsValid(phNumber: TextView):Boolean {
        //Phone Number Check
        val number = phNumber.text.toString()
        if(!number.matches(Regex("\\+61 [0-9]{3} [0-9]{3} [0-9]{3}")))
        {
            Log.d("Phone Number", "${number.matches(Regex("\\+61 [0-9]{3} [0-9]{3} [0-9]{3}"))}")
            phNumber.error = "The number should start with +61 and have 9 digits"
        }
        return true
    }

    private fun dateIsValid( value: String,view: TextView): Boolean {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH)
        try {
            val ld = LocalDate.parse(value, formatter)
            val today = LocalDate.now()
            val result = ld.format(formatter)
            Log.d("Date4", result.toString())
            if(result == value){
                val year = Integer.parseInt(value.substring(0,4))
                val month = Integer.parseInt(value.substring(5,7))
                val day = Integer.parseInt(value.substring(8))
                return if((year<today.year) || (day<today.dayOfMonth && month<= today.monthValue && year<=today.year) || (month<today.monthValue && year<=today.year)){
                    Log.d("Date Check", "${((year<today.year) || (day<today.dayOfMonth && month<= today.monthValue && year<=today.year) || (month<today.monthValue && year<=today.year))}")
                    view.error = "You Cannot have a Post for the Past"
                    false
                } else {
                    true
                }
            }
            view.error = "Invalid Date"
            return false
        } catch (e2: DateTimeParseException) {
            view.error = "Invalid Date"
            return false
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        meal = p0?.getItemAtPosition(p2).toString()
        Log.d("Selected", meal)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        meal = "Snacks"
        Log.d("Selected", meal)
    }
}