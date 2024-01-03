package com.example.madcamp1stweek.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.madcamp1stweek.R

class ReviewAdapter(private val onItemClick: (ItemData) -> Unit) : ListAdapter<ItemData, ReviewAdapter.ViewHolder>(RestaurantDiffCallback()) {
    // ViewHolder 및 기타 필요한 메서드 구현
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        fun bind(item: ItemData, onItemClick: (ItemData) -> Unit) {
            nameTextView.text = item.name
            ratingTextView.text = item.rating
            // 이미지 로딩 등

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.nameTextView.text = item.name
        holder.ratingTextView.text = item.rating
        holder.bind(item, onItemClick)
        val options = RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(16)))

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .apply(options)
            .into(holder.imageView)
    }

    fun setReviews(list: List<ItemData>){
        submitList(list)
    }


    class RestaurantDiffCallback : DiffUtil.ItemCallback<ItemData>() {
        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem == newItem
        }
    }
}
