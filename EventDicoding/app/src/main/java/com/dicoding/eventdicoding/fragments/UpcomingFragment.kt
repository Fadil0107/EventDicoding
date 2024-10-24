package com.dicoding.eventdicoding.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventdicoding.R
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.databinding.FragmentUpcomingBinding
import com.dicoding.eventdicoding.ui.DetailActivity
import com.dicoding.eventdicoding.ui.adapter.SpaceItemDecor
import com.dicoding.eventdicoding.ui.adapter.VerticalListAdapter
import com.dicoding.eventdicoding.viewmodel.MainViewModel

class UpcomingFragment : Fragment() {
    private lateinit var rvVerticalAdapter: VerticalListAdapter
    private lateinit var binding : FragmentUpcomingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val mainViewVertical = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        mainViewVertical.listEvent.observe(viewLifecycleOwner) { value ->
            setEventDataVertical(value)
        }
        mainViewVertical.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }
    private fun setEventDataVertical(event : List<DetailData>) {
        rvVerticalAdapter.submitList(event)
    }
    private fun setupRecyclerView() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        rvVerticalAdapter = VerticalListAdapter(requireContext()) { detailData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_DETAIL", detailData)
            startActivity(intent)
        }

        binding.verticalOnly.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.verticalOnly.adapter = rvVerticalAdapter

        // Tambahkan item decoration
        binding.verticalOnly.addItemDecoration(SpaceItemDecor(spacingInPixels))
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}