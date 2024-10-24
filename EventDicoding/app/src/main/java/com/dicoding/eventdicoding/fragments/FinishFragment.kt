package com.dicoding.eventdicoding.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventdicoding.R
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.databinding.FragmentFinishBinding
import com.dicoding.eventdicoding.ui.DetailActivity
import com.dicoding.eventdicoding.ui.adapter.SpaceItemDecor
import com.dicoding.eventdicoding.ui.adapter.VerticalListAdapter
import com.dicoding.eventdicoding.viewmodel.MainViewModelFinished
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class FinishFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentFinishBinding
    private lateinit var searchBar: SearchBar
    private lateinit var searchView: SearchView
    private lateinit var rvVerticalAdapter: VerticalListAdapter
    private var allEvents: List<DetailData> = listOf() // Menyimpan daftar event asli

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = binding.searchView
        searchBar = binding.searchBar
        searchBar.setOnClickListener(this)

        setupRecyclerView()

        val vertical = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModelFinished::class.java]
        vertical.listEvent.observe(viewLifecycleOwner) { value ->
            setEventDataVertical(value)
        }
        vertical.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        // Menambahkan listener untuk pencarian
        searchView.editText.addTextChangedListener { text ->
            filterEvent(text.toString())
        }
    }

    // Simpan event asli dan tampilkan
    private fun setEventDataVertical(event: List<DetailData>) {
        allEvents = event // Simpan semua event
        rvVerticalAdapter.submitList(allEvents) // Tampilkan semua event awalnya
    }

    // Filter event berdasarkan query pencarian
    private fun filterEvent(query: String) {
        val filteredList = allEvents.filter {
            it.name?.contains(query, ignoreCase = true) ?: false // Mengatasi kemungkinan null
        }
        rvVerticalAdapter.submitList(filteredList)
    }

    private fun setupRecyclerView() {
        rvVerticalAdapter = VerticalListAdapter(requireContext()) { detailData ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("EXTRA_DETAIL", detailData)
            startActivity(intent)
        }

        binding.verticalOnly.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.verticalOnly.adapter = rvVerticalAdapter
        binding.verticalOnly.addItemDecoration(SpaceItemDecor(24))
        binding.verticalOnly.clipToPadding = false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.searchBar -> {
                searchView.visibility = VISIBLE
                with(binding) {
                    searchView.setupWithSearchBar(searchBar)
                    searchView
                        .editText
                        .setOnEditorActionListener { _, _, _ ->
                            searchBar.setText(searchView.text)
                            searchView.hide()
                            Toast.makeText(this@FinishFragment.context, searchView.text, Toast.LENGTH_SHORT).show()
                            false
                        }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) VISIBLE else View.GONE
    }
}