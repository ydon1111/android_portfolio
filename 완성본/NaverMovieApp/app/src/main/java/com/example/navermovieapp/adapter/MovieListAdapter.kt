package com.example.navermovieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navermovieapp.R
import com.example.navermovieapp.databinding.ItemMovieBinding
import com.example.navermovieapp.model.MovieItem
import com.example.navermovieapp.util.DataParseUtil

class MovieListAdapter :
    PagingDataAdapter<MovieItem, MovieListAdapter.MovieViewHolder>(differCallback) {

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem?) {
            binding.apply {
                Glide.with(root)
                    .load(item?.image)
                    .centerCrop()
                    .placeholder(R.drawable.img_not_found)
                    .into(ivMovieImage)
                itemView.setOnClickListener {
                    onItemClickListener?.let { it(item!!) }
                }
                tvMovieTitle.text = "제목 : ${DataParseUtil.removeTags(item?.title)}"
                tvMoviePubDate.text = "출시 : ${item?.pubDate}"
                tvMovieRating.text = "평점 : ${item?.userRating.toString()}"
            }
        }
    }

    private var onItemClickListener: ((MovieItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (MovieItem) -> Unit) {
        onItemClickListener = listener
    }
}