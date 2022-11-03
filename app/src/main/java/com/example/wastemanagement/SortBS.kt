package com.example.wastemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.wastemanagement.databinding.FragmentSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBS(private val mainViewModel:MainViewModel)  : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSortBinding
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        binding = FragmentSortBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.distribution.setOnClickListener{
            mainViewModel.sortData("date")
        }
        binding.posted.setOnClickListener{
            mainViewModel.sortData("postedDate")
        }
        binding.people.setOnClickListener{
            mainViewModel.sortData("people")
        }
        binding.distributor.setOnClickListener{
            mainViewModel.sortData("company")
        }
        binding.name.setOnClickListener{
            mainViewModel.sortData("postHeader")
        }
        return view
    }
}