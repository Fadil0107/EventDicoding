package com.dicoding.eventdicoding.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.databinding.EventHorizontalBinding
import com.dicoding.eventdicoding.ui.DetailActivity

class HorizontalListAdapter(private val context : Context, private val onItemClicked : (DetailData) -> Unit) : ListAdapter<DetailData,
        HorizontalListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventHorizontalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        val detailData = getItem(position)
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("EXTRA_ID",position)
        holder.bind(detailData,onItemClicked)
    }
    inner class MyViewHolder(private val binding : EventHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detail : DetailData,onItemClicked: (DetailData) -> Unit) {
            Glide.with(context)
                .load(detail.imageLogo)
                .into(binding.headerImage)
            binding.title.text = detail.name
            binding.root.setOnClickListener{
                onItemClicked(detail)
            }
        }
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailData>() {
            override fun areItemsTheSame(oldItem: DetailData, newItem: DetailData): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DetailData, newItem: DetailData): Boolean {
                return oldItem == newItem
            }
        }
    }
}