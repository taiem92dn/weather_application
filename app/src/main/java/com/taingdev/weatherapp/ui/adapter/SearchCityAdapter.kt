package com.taingdev.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taingdev.weatherapp.databinding.ItemSearchCityBinding
import com.taingdev.weatherapp.model.CityItem

class SearchCityAdapter: ListAdapter<CityItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    var onItemClickListener: ((CityItem) -> Unit) = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityAdapter.ViewHolder {
        val binding = ItemSearchCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }


    inner class ViewHolder(val binding: ItemSearchCityBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener(it)
                }
            }
        }

        fun bind(item: CityItem?) {
            binding.also {
                it.cityItem = item
                it.executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<CityItem>() {
            override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
                return  oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}
