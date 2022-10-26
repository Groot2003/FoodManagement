package com.example.wastemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBS : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_sort,container,false)
        return view
    }
}