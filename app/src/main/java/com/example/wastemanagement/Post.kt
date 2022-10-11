package com.example.wastemanagement

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(var id: String, val postHeader: String, val company: String,
                val date: String, val foodItem: String,val postedDate: String,val location:String,
                val postcode: Int,val people: Int,val contact: String,val time:String,val meal:String,val description:String ) : Parcelable {
    public constructor() : this("","", "", "", "", "", "", 0, 0, "", "", "", "")
}
//    val postHeader: String
//    val company: String
//    val date: String
//    val foodItem: String
//    val postedDate: String
//    val location:String
//    val postcode: Int
//    val people: Int
//    val contact: Long
//    val time:String
//    val meal:String
//    val description:String

//    public constructor( postHeader: String,  company: String,
//                        date: String,  foodItem: String, postedDate: String, location:String,
//                        postcode: Int, people: Int, contact: Long, time:String, meal:String, description:String ) : this() {
//        this.postHeader = postHeader
//        this.company= company
//        this.date= date
//        this.foodItem= foodItem
//        this.postedDate= postedDate
//        this.location=location
//        this.postcode= postcode
//        this.people= people
//        this.contact= contact
//        this.time=time
//        this.meal=meal
//        this.description=description
//    }



//val title = titleInput.text
//            val company = companyInput.text
//            val people = peopleInput.text
//            val foodItem = foodItemInput.text
//            val location = locationInput.text
//            val postcode = postcodeIsValid(postInput.text.toString())
//            val contact = numberIsValid(contactInput.text.toString())
//            val date = dateIsValid(dateInput.text.toString(),dateInput)
////            meal and time from above