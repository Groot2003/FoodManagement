package com.example.wastemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileBS(private val bottom_sheet:String) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        if(bottom_sheet == "profile")
            return inflater.inflate(R.layout.fragment_profile, container, false)
        else
            return inflater.inflate(R.layout.fragment_sort, container, false)
    }
}