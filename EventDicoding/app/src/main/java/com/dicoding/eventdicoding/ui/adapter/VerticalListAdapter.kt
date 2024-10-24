package com.dicoding.eventdicoding.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.eventdicoding.data.response.DetailData
import com.dicoding.eventdicoding.databinding.EventVerticalBinding

class VerticalListAdapter(private val context : Context, private val onItemClicked: (DetailData) -> Unit) : ListAdapter<DetailData,
        VerticalListAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventVerticalBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        val detailData = getItem(position)
        holder.bind(detailData)

        holder.itemView.setOnClickListener {
            onItemClicked(detailData)
        }
    }
    inner class MyViewHolder(private val binding : EventVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detail : DetailData) {
            Glide.with(context)
                .load(detail.imageLogo)
                .into(binding.image)
            binding.judul.text = "${detail.name}"
            binding.penyelenggara.text = "${detail.ownerName}"
            binding.waktu.text = getWaktu(detail)

            binding.root.setOnClickListener{
                onItemClicked(detail)
            }
        }
        private fun getWaktu(detail: DetailData) : String {
            return "Waktu Mulai : ${detail.beginTime}\nWaktu Selesai : ${detail.endTime}"
        }
    }
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetailData>() {
            override fun areContentsTheSame(oldItem: DetailData, newItem: DetailData): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: DetailData, newItem: DetailData): Boolean {
                return oldItem == newItem
            }
        }
    }
}