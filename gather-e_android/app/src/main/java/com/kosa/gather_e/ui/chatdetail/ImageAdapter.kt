package com.kosa.gather_e.ui.chatdetail

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kosa.gather_e.databinding.ItemHmallBinding
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.model.entity.chat.HmallItem


class ImageAdapter(val onItemClicked: (HmallItem) -> Unit) : ListAdapter<HmallItem, ImageAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHmallBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hmallItem: HmallItem) {
            binding.root.setOnClickListener {
                onItemClicked(hmallItem)
         }
            Log.d("crawling","gogo ${hmallItem.itemImage}")
            Glide.with(itemView)
                .load(hmallItem.itemImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 모든 이미지를 캐시
                .into(binding.itemImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHmallBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HmallItem>() {
            override fun areItemsTheSame(oldItem: HmallItem, newItem: HmallItem): Boolean {
                return oldItem.itemImage == newItem.itemImage
            }

            override fun areContentsTheSame(oldItem: HmallItem, newItem: HmallItem): Boolean {
                return oldItem == newItem
            }


        }
    }

}
