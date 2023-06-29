package com.kosa.gather_e.ui.chatdetail

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kosa.gather_e.DBKey.Companion.DB_CHATS
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.ActivityChatRoomBinding
import com.kosa.gather_e.model.entity.chat.ChatItem

class ChatRoomActivity : AppCompatActivity() {

    private val user: String = "user02"


    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB: DatabaseReference? = null
    private var selectedImageUri: Uri? = null

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val itemBinding = ItemChatBinding.inflate(layoutInflater)

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
            Log.d("Gatherne","${Build.VERSION.RELEASE}")

            // 안드로이드 버전 13 미만의 코드를 작성해야함
            // READ_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT < 33){
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        startContentProvider()
                    }

                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showPermissionContextPopup()
                    }

                    else -> {
                        requestPermissions(
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            1010
                        )
                    }
                }
            }else {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        startContentProvider()
                    }

                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES) -> {
                        showPermissionContextPopup()
                    }

                    else -> {
                        requestPermissions(
                            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                            1010
                        )
                    }

                }
            }

        }

        binding.sendButton.setOnClickListener {
            val messageEditText = binding.messageEditText
            val message = messageEditText.text.toString()
            if (selectedImageUri != null) {
                val photoUri = selectedImageUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = { uri ->
                        val chatItem = ChatItem(senderId = user, message = message, image = uri, viewType = 0)
                        Log.d("Gatherne", "photoUri, Mine")

                        chatDB?.push()?.setValue(chatItem)
                        selectedImageUri = null
//                        binding.progressBar.isVisible = false
                     },
                    errorHandler = {
                        Toast.makeText(this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                val chatItem = ChatItem(senderId = user, message = message, image = "", viewType = 1)
                chatDB?.push()?.setValue(chatItem)
                Log.d("Gatherne", "No photoUri, YOur")


            }
            selectedImageUri = null
            messageEditText.text.clear()
        }

    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
//        findViewById<ProgressBar>(R.id.progressBar).isVisible = true

        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("chat").child(fileName)
            .putFile(uri)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        successHandler(uri.toString())
                    }
                    .addOnFailureListener {
                        errorHandler()
                    }
            }
            .addOnFailureListener {
                errorHandler()
            }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1010 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var binding = ActivityChatRoomBinding.inflate(layoutInflater)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            2020 -> {
                val uri = data?.data
                if (uri != null) {
                    selectedImageUri = uri
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
            }
            .create()
            .show()

    }


}