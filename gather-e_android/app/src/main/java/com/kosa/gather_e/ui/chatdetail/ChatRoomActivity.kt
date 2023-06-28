package com.kosa.gather_e.ui.chatdetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kosa.gather_e.DBKey.Companion.DB_CHATS
import com.kosa.gather_e.R
import com.kosa.gather_e.data.model.chat.ChatItem
import com.kosa.gather_e.databinding.ActivityChatRoomBinding
import com.kosa.gather_e.databinding.ItemChatBinding

class ChatRoomActivity : AppCompatActivity() {

    private val user: String = "user02"


    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB: DatabaseReference? = null
    private var selectedImageUri: Uri? = null
    companion object {
        private const val REQUEST_IMAGE_GALLERY = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var itemBinding = ItemChatBinding.inflate(layoutInflater)
        // Notification
//        FirebaseMessaging.getInstance().token
//            .addOnCompleteListener{task ->
//                if(task.isSuccessful){
//                    val token = task.result
//
//
//                }
//            }

        val chatKey = intent.getLongExtra("chatKey", -1)

        chatDB = Firebase.database.reference.child(DB_CHATS).child("$chatKey")

        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}


        })

        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnSelectImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
        }

        fun isImageSelected(): Boolean {
            val imagePreview = itemBinding.imagePreview
            return imagePreview.visibility == View.VISIBLE
        }


        binding.sendButton.setOnClickListener {
            val messageEditText = binding.messageEditText
            val message = messageEditText.text.toString()

            if (isImageSelected()) {
//                val chatItem = ChatItem(senderId = user, message = selectedImageUri.toString())
//                chatDB?.push()?.setValue(chatItem)
            } else {
                val chatItem = ChatItem(senderId = user, message = message)
                chatDB?.push()?.setValue(chatItem)
            }

            messageEditText.text.clear()
        }
    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
//            // Image selected from gallery
//            selectedImageUri = data.data
//            if (selectedImageUri != null) {
//                displaySelectedImage(selectedImageUri!!)
//            }
//        }
//    }
//
//    private fun displaySelectedImage(imageUri: Uri) {
//        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
//        val messageEditText = findViewById<EditText>(R.id.messageEditText)
//
//        // Show the ImageView and hide the TextView
//        imagePreview.visibility = View.VISIBLE
//        messageEditText.visibility = View.GONE
//
//        // Set the selected image to the ImageView
//        imagePreview.setImageURI(imageUri)
//    }
}