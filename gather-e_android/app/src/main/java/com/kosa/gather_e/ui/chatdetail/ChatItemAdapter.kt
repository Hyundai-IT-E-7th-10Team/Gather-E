package com.kosa.gather_e.ui.chatdetail

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.ItemChatMineBinding
import com.kosa.gather_e.databinding.ItemChatYourBinding
import com.kosa.gather_e.model.entity.chat.ChatItem
import com.kosa.gather_e.util.CurrUser
import java.io.IOException

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
//                .override(30, 40) // 원하는 크기로 조정
                .apply(RequestOptions().transform(CircleCrop()))
                .into(binding.userImage)

            if (!chatItem.image.isBlank()) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.messageTextView.visibility = View.GONE
                binding.imageDownload.visibility = View.VISIBLE
                Glide.with(binding.imagePreview)
                    .load(chatItem.image)
                    .placeholder(R.drawable.loading_spinner)
                    .thumbnail(0.1f)
                    .override(300, 400) // 원하는 크기로 조정
                    .transform(CenterCrop(),RoundedCorners(30))
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 모든 이미지를 캐시
                    .centerCrop()
                    .into(binding.imagePreview)

            } else {
                binding.imagePreview.visibility = View.GONE
                binding.messageTextView.visibility = View.VISIBLE
                binding.imageDownload.visibility = View.GONE

            }

            binding.imageDownload.setOnClickListener {
                // 이미지 다운로드
                saveImageToGallery(itemView.context,chatItem.image,chatItem.image)
            }
        }

        init {
            binding.imageDownload.visibility = View.GONE
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
//                .override(90, 120) // 원하는 크기로 조정
                .apply(RequestOptions().transform(CircleCrop()))
                .into(binding.userImage)

            if (!chatItem.image.isBlank()) {
                binding.imagePreview.visibility = View.VISIBLE
                binding.messageTextView.visibility = View.GONE
                binding.imageDownload.visibility = View.VISIBLE
                Glide.with(binding.imagePreview)
                    .load(chatItem.image)
                    .placeholder(R.drawable.loading_spinner)
                    .thumbnail(0.1f)
                    .override(300, 400) // 원하는 크기로 조정
                    .transform(CenterCrop(),RoundedCorners(30))
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 모든 이미지를 캐시
                    .centerCrop()
                    .into(binding.imagePreview)
            } else {
                binding.imagePreview.visibility = View.GONE
                binding.messageTextView.visibility = View.VISIBLE
                binding.imageDownload.visibility = View.GONE

            }
            binding.imageDownload.setOnClickListener {
                // 이미지 다운로드
                saveImageToGallery(itemView.context,chatItem.image,chatItem.image)
            }
        }
        init {
            binding.imageDownload.visibility = View.GONE
            binding.imagePreview.visibility = View.GONE
            binding.messageTextView.visibility = View.VISIBLE
        }
    }

    private fun saveImageToGallery(context: Context, imageUrl: String, title: String) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Bitmap 이미지가 준비되면 갤러리에 저장
                    saveBitmapToGallery(context, resource, title)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    // Bitmap 이미지를 갤러리에 저장하는 함수
    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String) {
        val displayName = "$title.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let { imageUri ->
            try {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
                Toast.makeText(context, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}



