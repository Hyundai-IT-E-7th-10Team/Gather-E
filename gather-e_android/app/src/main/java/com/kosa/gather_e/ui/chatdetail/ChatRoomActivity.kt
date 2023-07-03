package com.kosa.gather_e.ui.chatdetail

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.DBKey.Companion.CHILD_CHAT
import com.kosa.gather_e.DBKey.Companion.DB_CHATS
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.ActivityChatRoomBinding
import com.kosa.gather_e.model.entity.chat.ChatItem
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.notification.PushNotificationData
import com.kosa.gather_e.model.entity.notification.PushNotificationEntity
import com.kosa.gather_e.model.entity.notification.PushNotificationResponse
import com.kosa.gather_e.model.entity.user.CurrUser
import com.kosa.gather_e.model.repository.firebase.FCMRetrofitProvider
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    private var chatListItem = mutableListOf<ChatListItem>()
    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB: DatabaseReference? = null
    private var selectedImageUri: Uri? = null

    private var userName = CurrUser.getUserName()
    private var userImage = CurrUser.getProfileImgUrl()
    lateinit var resultLauncher: ActivityResultLauncher<Intent>


    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatKey = intent.getLongExtra("chatKey", -1)
        val gatherTitle = intent.getStringExtra("gatherTitle")
        val gatherDate = intent.getStringExtra("gatherDate")
        val gatherLimit = intent.getIntExtra("gatherLimit",-1)
        val gatherCategortSeq = intent.getIntExtra("gatherCategorySeq",-1)
        val participants: List<String> = intent.getStringArrayListExtra("gatherParticipants") ?: emptyList()
        val participantTokens: List<String> = intent.getStringArrayListExtra("gatherParticipantTokens") ?: emptyList()
        val gatherPlace = intent.getStringExtra("gatherPlace")
        val gatherCategory = intent.getStringExtra("gatherCategory")
        Log.d("gather","Recieved userTokens :: ${participantTokens}")




        binding.chatRoomTitleTextView.text = gatherTitle
        binding.dateTextView.text = gatherDate
        binding.participantsTextView.text = gatherLimit.toString()
        var categoryImage = R.drawable.ic_1_football
        when (gatherCategortSeq) {
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
        Glide.with(this)
            .load(categoryImage)
            .apply(RequestOptions().transform (CircleCrop()))
            .into(binding.circleImageView)


        chatDB = Firebase.database.reference.child(CHILD_CHAT).child("$chatKey")

        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                adapter.scrollToBottom()
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}


        })

        // toolbar의 X(취소) 버튼
        binding.imgbtnQuit.setOnClickListener {
            finish()
        }

        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.post {
            binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
        adapter.scrollToBottom()

        binding.btnSelectImage.setOnClickListener {
            Log.d("Gatherne", "${Build.VERSION.RELEASE}")

            // 안드로이드 버전 13 미만의 코드를 작성해야함
            // READ_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT < 33) {
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
            } else {
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
            // 이미지 O, 텍스트 X
            if (selectedImageUri != null) {
                val photoUri = selectedImageUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = { uri ->
                        val chatItem =
                            ChatItem(senderId = userName, senderImage = userImage, message = message, image = uri,sendTime = getCurrentTime(), viewType = 0)
                        Log.d("Gatherne", "photoUri")

                        chatDB?.push()?.setValue(chatItem)
                        selectedImageUri = null

//                        binding.progressBar.isVisible = false
                    },
                    errorHandler = {
                        Toast.makeText(this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
            // 이미지 X, 텍스트 O
            } else {
                val chatItem =
                    ChatItem(senderId = userName, senderImage = userImage, message = message, image = "", sendTime = getCurrentTime(), viewType = 0)
                if (message.isBlank()) {
                    // Show an error message or handle it as desired
                    Toast.makeText(this, "메시지를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                chatDB?.push()?.setValue(chatItem)
//                findViewById<TextView>(R.id.messageTextView).isVisible = false
                Log.d("Gatherne", "No photoUri")


            }
            binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            selectedImageUri = null
            messageEditText.text.clear()
            binding.selectedImage.visibility = View.GONE

            // 검색 버튼 눌리면 키보드 숨기도록
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.sendButton.windowToken, 0)


            // ChatRoomList에 사용자의 토큰(token) 값을 저장하는 작업
            // 예시: ChatRoomList에 있는 모든 사용자의 토큰을 저장하는 리스트를 생성합니다.
            val userTokens = participantTokens
            Log.d("gather","userTokens :: ${userTokens}")

            // 각 사용자에게 알림을 보내는 작업
            for (token in userTokens) {
                val pushNotificationData = PushNotificationData(
                    type = "NORMAL",
                    title = gatherTitle!!,
                    message = message
                )

                val pushNotificationEntity = PushNotificationEntity(
                    to = token,
                    priority = "high",
                    data = pushNotificationData
                )

                val callPushNotification: Call<PushNotificationResponse> = FCMRetrofitProvider.getRetrofit().sendPushNotification(pushNotificationEntity)
                callPushNotification.enqueue(object : Callback<PushNotificationResponse> {
                    override fun onResponse(
                        call: Call<PushNotificationResponse>,
                        response: Response<PushNotificationResponse>
                    ) {
                        Log.d("gather", "성공 $call, $response")
                        Log.d("gather", "$pushNotificationEntity")
                    }

                    override fun onFailure(call: Call<PushNotificationResponse>, t: Throwable) {
                        Log.d("gather", "실패 $t")
                        Log.d("gather", "실패 $call")
                    }
                })
            }
//
//
//            val myToken = CurrUser.getToken()
//            val pushNotificationData = PushNotificationData(
//                type = "NORMAL",
//                title = gatherTitle!!,
//                message = message
//            )
//
//            val pushNotificationEntity = PushNotificationEntity(
//                to = myToken,
//                priority = "high",
//                data = pushNotificationData
//            )
//
//            val callPushNotification: Call<PushNotificationResponse> = FCMRetrofitProvider.getRetrofit().sendPushNotification(pushNotificationEntity)
//            callPushNotification.enqueue(object : Callback<PushNotificationResponse> {
//
//
//                override fun onResponse(
//                    call: Call<PushNotificationResponse>,
//                    response: Response<PushNotificationResponse>
//                ) {
//                    Log.d("gather", "성공 $call, $response")
//                    Log.d("gather", "${pushNotificationEntity}")
//                }
//
//                override fun onFailure(call: Call<PushNotificationResponse>, t: Throwable) {
//                    Log.d("gather", "실패 $t")
//                    Log.d("gather", "실패 $call")                }
//            })
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
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            2020 -> {
                val uri = data?.data
                if (uri != null) {
                    Log.d("gather","사진 가져와서 띄워주기 ${uri}")
                    selectedImageUri = uri
                    Glide.with(this)
                        .load(uri)
                        .into(binding.selectedImage)
                    binding.selectedImage.visibility = View.VISIBLE
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

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }


}