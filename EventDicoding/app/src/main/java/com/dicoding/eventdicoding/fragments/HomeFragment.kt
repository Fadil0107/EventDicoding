package com.dicoding.eventdicoding.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.databinding.FragmentHomeBinding
import com.dicoding.eventdicoding.ui.DetailActivity
import com.dicoding.eventdicoding.ui.adapter.HorizontalListAdapter
import com.dicoding.eventdicoding.ui.adapter.SpaceItemDecor
import com.dicoding.eventdicoding.ui.adapter.VerticalListAdapter
import com.dicoding.eventdicoding.viewmodel.MainViewModel
import com.dicoding.eventdicoding.viewmodel.MainViewModelFinished

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvHorizontalAdapter: HorizontalListAdapter
    private lateinit var rvVerticalAdapter: VerticalListAdapter
    private var position: Int = 0

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        arguments?.let {
            position = it.getInt("EXTRA_ID", 0)
            Log.d(TAG, "Position received: $position")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        val mainViewVertical = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModelFinished::class.java]

        mainViewModel.listEvent.observe(viewLifecycleOwner) { value ->
            setEventData(value)
        }
        mainViewVertical.listEvent.observe(viewLifecycleOwner) { value ->
            setEventDataVertical(value)
        }
        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setEventData(event: List<DetailData>) {
        rvHorizontalAdapter.submitList(event)
    }

    private fun setEventDataVertical(event: List<DetailData>) {
        // Batasi jumlah event yang ditampilkan menjadi 5
        val limitedEvents = if (event.size > 5) event.subList(0, 5) else event
        rvVerticalAdapter.submitList(limitedEvents)
    }

    private fun setupRecyclerView() {
        rvHorizontalAdapter = HorizontalListAdapter(requireContext()) { detailData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_DETAIL", detailData)
            startActivity(intent)
        }

        binding.horizontalOnly.layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
        binding.horizontalOnly.adapter = rvHorizontalAdapter

        rvVerticalAdapter = VerticalListAdapter(requireContext()) { detailData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_DETAIL", detailData)
            startActivity(intent)
        }

        binding.verticalOnly.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.verticalOnly.adapter = rvVerticalAdapter
        binding.verticalOnly.addItemDecoration(SpaceItemDecor(24))
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
