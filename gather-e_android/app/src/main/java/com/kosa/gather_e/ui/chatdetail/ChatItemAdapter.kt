package com.kosa.gather_e.ui.chatdetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosa.gather_e.model.entity.chat.ChatItem
import com.kosa.gather_e.databinding.ItemChatMineBinding
import com.kosa.gather_e.databinding.ItemChatYourBinding

class ChatItemAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(diffUtil) {

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
        return if (chatItem.viewType == MY_CHAT_ITEM_VIEW_TYPE) {
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
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message

            Glide.with(binding.imagePreview)
                .load(chatItem.image)
                .into(binding.imagePreview)
        }
    }

    inner class YourChatViewHolder(private val binding: ItemChatYourBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message

            Glide.with(binding.imagePreview)
                .load(chatItem.image)
                .into(binding.imagePreview)
        }
    }
}

//
//class ChatItemAdapter : ListAdapter<ChatItem, ChatItemAdapter.ViewHolder>(diffUtil) {
//
//    inner class ViewHolder(private val binding: ItemChatMineBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(chatItem: ChatItem) {
//            binding.senderTextView.text = chatItem.senderId
//            binding.messageTextView.text = chatItem.message
//
//            Glide.with(itemView)
//                .load(chatItem.image)
//                .into(binding.imagePreview)
//        }
//    }
//    inner class ViewHolder(private val binding: ItemChatYourBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(chatItem: ChatItem) {
//            binding.senderTextView.text = chatItem.senderId
//            binding.messageTextView.text = chatItem.message
//
//            Glide.with(itemView)
//                .load(chatItem.image)
//                .into(binding.imagePreview)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(ItemChatMineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(currentList[position])
//    }
//
//    companion object {
//        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
//            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
//                return oldItem == newItem
//            }
//
//
//        }
//    }
//
//}