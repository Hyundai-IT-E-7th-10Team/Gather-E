package com.kosa.gather_e.ui.chatdetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.R
import com.kosa.gather_e.model.entity.chat.ChatItem
import com.kosa.gather_e.databinding.ItemChatMineBinding
import com.kosa.gather_e.databinding.ItemChatYourBinding
import com.kosa.gather_e.model.entity.user.CurrUser

class ChatItemAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(diffUtil) {

    private var userName = CurrUser.getUserName()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == MY_CHAT_ITEM_VIEW_TYPE) {
            val binding = ItemChatMineBinding.inflate(inflater, parent, false)
            MyChatViewHolder(binding)
        } else {
            val binding = ItemChatYourBinding.inflate(inflater, parent, false)
            YourChatViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatItem = getItem(position)

        when (holder) {
            is MyChatViewHolder -> holder.bind(chatItem)
            is YourChatViewHolder -> holder.bind(chatItem)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val chatItem = getItem(position)
        return getItemViewTypeBySenderId(chatItem.senderId)
    }

    private fun getItemViewTypeBySenderId(senderId: String): Int {
        return if (senderId == userName) {
            MY_CHAT_ITEM_VIEW_TYPE
        } else {
            YOUR_CHAT_ITEM_VIEW_TYPE
        }
    }

    companion object {
        private const val MY_CHAT_ITEM_VIEW_TYPE = 0
        private const val YOUR_CHAT_ITEM_VIEW_TYPE = 1

        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyChatViewHolder(private val binding: ItemChatMineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            Log.d("gather",chatItem.image)
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message
            binding.txtDate.text = chatItem.sendTime

            Glide.with(itemView)
                .load(chatItem.senderImage)
                .apply(RequestOptions().transform(CircleCrop()))
                .into(binding.userImage)

            if (!chatItem.image.isBlank()) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.messageTextView.visibility = View.GONE
                Glide.with(binding.imagePreview)
                    .load(chatItem.image)
                    .placeholder(R.drawable.loading_spinner)
                    .thumbnail(0.1f)
                    .apply(RequestOptions().override(300, 300)) // 이미지 크기 제한
//                    .override(300, 300) // 원하는 크기로 조정
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 모든 이미지를 캐시
                    .centerCrop()
                    .into(binding.imagePreview)

            } else {
                binding.imagePreview.visibility = View.GONE
                binding.messageTextView.visibility = View.VISIBLE

            }
        }
        init {
            binding.imagePreview.visibility = View.GONE
            binding.messageTextView.visibility = View.VISIBLE
        }

    }


    inner class YourChatViewHolder(private val binding: ItemChatYourBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            Log.d("gather",chatItem.image)
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message
            binding.txtDate.text = chatItem.sendTime

            Glide.with(itemView)
                .load(chatItem.senderImage)
                .apply(RequestOptions().transform(CircleCrop()))
                .into(binding.userImage)

            if (!chatItem.image.isBlank()) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.messageTextView.visibility = View.GONE
                Glide.with(binding.imagePreview)
                    .load(chatItem.image)
                    .into(binding.imagePreview)
            } else {
                binding.imagePreview.visibility = View.GONE
                binding.messageTextView.visibility = View.VISIBLE

            }
        }
        init {
            binding.imagePreview.visibility = View.GONE
            binding.messageTextView.visibility = View.VISIBLE
        }
    }

}

