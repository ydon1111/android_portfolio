package com.example.navermovieapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.navermovieapp.databinding.ItemSearchBinding
import com.example.navermovieapp.model.Keyword

class SearchKeywordAdapter : ListAdapter<Keyword, SearchKeywordAdapter.SearchViewHolder>(diffUtil) {

    inner class SearchViewHolder(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Keyword) {
            binding.apply {
                searchTermText.text = item.keyword
                constraintSearchItem.setOnClickListener {
                    onSearchKeywordClickListener?.let { it(item) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(holder.bindingAdapterPosition))
    }

    private var onSearchKeywordClickListener: ((Keyword) -> Unit)? = null
    fun setOnSearchKeywordClickListener(listener: (Keyword) -> Unit) {
        onSearchKeywordClickListener = listener
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Keyword>() {
            override fun areItemsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
                return oldItem == newItem
            }

        }
    }
}