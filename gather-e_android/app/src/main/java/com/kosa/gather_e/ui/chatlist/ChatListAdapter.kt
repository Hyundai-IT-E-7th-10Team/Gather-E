package com.kosa.gather_e.ui.chatlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kosa.gather_e.R
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.databinding.ItemChatListBinding
import com.naver.maps.map.overlay.OverlayImage

class ChatListAdapter(val onItemClicked: (ChatListItem) -> Unit) : ListAdapter<ChatListItem, ChatListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatListItem: ChatListItem) {
            binding.root.setOnClickListener {
                onItemClicked(chatListItem)
            }

            binding.chatRoomTitleTextView.text = chatListItem.gatherTitle
            binding.dateTextView.text = chatListItem.gatherPlace
            binding.participants.text = chatListItem.gatherLimit.toString()
            binding.currentParticipants.text = chatListItem.participants.size.toString()

            Log.d("gather","카테고리 ${chatListItem.gatherCategory}")
            Log.d("gather","카테고리 Seq : ${chatListItem.gatherCategorySeq}")
            var categoryImage = R.drawable.ic_1_football
            when (chatListItem.gatherCategorySeq) {
                1 -> categoryImage = R.drawable.ic_1_football
                2 -> categoryImage = R.drawable.ic_2_tennis
                3 -> categoryImage = R.drawable.ic_3_golf
                4 -> categoryImage = R.drawable.ic_4_basketball
                5 -> categoryImage = R.drawable.ic_5_hiking
                6 -> categoryImage = R.drawable.ic_6_shuttlecock
                7 -> categoryImage = R.drawable.ic_7_volleyball
                8 -> categoryImage = R.drawable.ic_8_bowling
                9 -> categoryImage = R.drawable.ic_9_squash
                10 -> categoryImage = R.drawable.ic_10_pingpong
                11 -> categoryImage = R.drawable.ic_11_swimmig
                12 -> categoryImage = R.drawable.ic_12_riding
                13 -> categoryImage = R.drawable.ic_13_skate
                14 -> categoryImage = R.drawable.ic_14_cycling
                15 -> categoryImage = R.drawable.ic_15_yoga
                16 -> categoryImage = R.drawable.ic_16_pilates
                17 -> categoryImage = R.drawable.ic_17_climbing
                18 -> categoryImage = R.drawable.ic_18_billiard
                19 -> categoryImage = R.drawable.ic_19_dancing
                20 -> categoryImage = R.drawable.ic_20_boxing
            }

            Glide.with(itemView)
                .load(categoryImage)
                .apply(RequestOptions().transform(CircleCrop()))
                .into(binding.circleImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatListItem>() {
            override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem == newItem
            }


        }
    }

}