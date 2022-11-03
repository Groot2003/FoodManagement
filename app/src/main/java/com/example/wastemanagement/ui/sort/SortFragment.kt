package com.example.wastemanagement.ui.sort

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wastemanagement.MainViewModel
import com.example.wastemanagement.databinding.FragmentSortBinding


class SortFragment : Fragment() {

    private var _binding: FragmentSortBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val view =  inflater.inflate(R.layout.bottom_sheet,container,false)
        _binding = FragmentSortBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.distribution.setOnClickListener{
            Log.d("Trying to Sort", "date: ")
            mainViewModel.sortData("date")
        }
        binding.posted.setOnClickListener{
            Log.d("Trying to Sort", "postedDate: ")
            mainViewModel.sortData("postedDate")
        }
        binding.people.setOnClickListener{
            Log.d("Trying to Sort", "people: ")
            mainViewModel.sortData("people")
        }
        binding.distributor.setOnClickListener{
            Log.d("Trying to Sort", "company: ")
            mainViewModel.sortData("company")
        }
        binding.name.setOnClickListener{
            Log.d("Trying to Sort", "postHeader: ")
            mainViewModel.sortData("postHeader")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}