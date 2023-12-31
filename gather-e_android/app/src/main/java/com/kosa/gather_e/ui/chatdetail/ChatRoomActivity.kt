package com.kosa.gather_e.ui.chatdetail

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kosa.gather_e.DBKey.Companion.CHILD_CHAT
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.ActivityChatRoomBinding
import com.kosa.gather_e.model.entity.chat.ChatItem
import com.kosa.gather_e.model.entity.chat.HmallItem
import com.kosa.gather_e.model.entity.notification.PushNotificationData
import com.kosa.gather_e.model.entity.notification.PushNotificationEntity
import com.kosa.gather_e.model.entity.notification.PushNotificationResponse
import com.kosa.gather_e.model.repository.firebase.FCMRetrofitProvider
import com.kosa.gather_e.util.CurrUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var chatDB: DatabaseReference? = null
    private var selectedImageUri: Uri? = null

    private var userName = CurrUser.getUserName()
    private var userImage = CurrUser.getProfileImgUrl()
    private var hmallImageList = mutableListOf<HmallItem>()
    private lateinit var imageAdapter: ImageAdapter

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
        val participants: MutableList<String> = intent.getStringArrayListExtra("gatherParticipants") ?: mutableListOf()
        val participantTokens: MutableList<String> = intent.getStringArrayListExtra("gatherParticipantTokens") ?: mutableListOf()
        val gatherPlace = intent.getStringExtra("gatherPlace")
        val gatherCategory = intent.getStringExtra("gatherCategory")
        Log.d("gather","Recieved userTokens :: ${participantTokens}")
        var participantsCnt:Int = participants.size



        binding.chatRoomTitleTextView.text = gatherTitle
//        binding.dateTextView.text = gatherDate
        binding.dateTextView.text = gatherPlace
        binding.participants.text = gatherLimit.toString()
        binding.currentParticipants.text = participantsCnt.toString()

//        val keyword = "탁구"
        val hmall = "https://www.hmall.com/p/pde/search.do?searchTerm=${gatherCategory}&gnbSearchYn=Y&gaSearchType=1"

        Thread {
            val doc = Jsoup.connect(hmall).get()

            val wrapElement = doc.selectFirst("div.wrap.searchresult")
            val mainElement = wrapElement?.selectFirst("main.cmain.results")
            val div1Element = mainElement?.getElementById("reflashArea")
            val formElement = div1Element?.selectFirst("form[name=pdeSearchForm]")
            val div2Element = formElement?.selectFirst("div.container.gird-l2x")
            val div3Element = div2Element?.selectFirst("div.contents")
            val div4Element = div3Element?.selectFirst("div.product-area")
            val div5Element = div4Element?.selectFirst("div.pdlist-wrap")
            val ulElement = div5Element?.selectFirst("ul")
            val liList = ulElement?.select("li")

            for (liElement in liList!!) {
                var imageItem = HmallItem()
                var aElement = liElement.selectFirst("a")
                var divElement = aElement?.selectFirst("div.thumb")
                var imgElement = divElement?.selectFirst("img")
                var img = imgElement?.attr("src")

                // onclick 속성 값 추출
                val onclick: String = aElement.attr("onclick")

                val pattern = Pattern.compile("itemDetailLinkProc\\('([^']*)'")
                val matcher: Matcher = pattern.matcher(onclick)

                var firstArgument: String? = ""
                if (matcher.find()) {
                    firstArgument = matcher.group(1)
                }


                Log.d("crawling",firstArgument!!)


                imageItem.itemImage = img.toString()
                imageItem.itemUrl = "https://www.hmall.com${firstArgument}}"
                hmallImageList.add(imageItem)
            }

            runOnUiThread {
                Log.d("crawling",hmallImageList.toString())
                val recyclerView: RecyclerView = binding.hmallList
                recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

                imageAdapter = ImageAdapter(onItemClicked = { hmallItem ->
                    this?.let {
                        val url = hmallItem.itemUrl
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                })
                recyclerView.adapter = imageAdapter
                imageAdapter.submitList(hmallImageList)

            }
        }.start()


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
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
                binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount)

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
        binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount)

        binding.btnSelectImage.setOnClickListener {
            Log.d("Gatherne", "${Build.VERSION.RELEASE}")

            val permission =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                } else {
                    android.Manifest.permission.READ_MEDIA_IMAGES
                }

            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(permission) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    requestPermissions(arrayOf(permission), 1010)
                }
            }
        }


        binding.sendButton.setOnClickListener {
            val messageEditText = binding.messageEditText
            val message = messageEditText.text.toString()
            // 이미지 O, 텍스트 X
            if (selectedImageUri != null) {
                showProgressBar()
                val photoUri = selectedImageUri ?: return@setOnClickListener
                // 코루틴 시작
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val uri = withContext(Dispatchers.IO) {
                            Log.d("gather","보낼 때 사진 압축 후 일까? ${photoUri}")
                            uploadPhoto(photoUri)
                        }
                        sendMessageWithImage(uri, message)
                    } catch (e: Exception) {
                        Toast.makeText(this@ChatRoomActivity, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } finally {
                        hideProgressBar()
                    }
                }
            // 이미지 X, 텍스트 O
            } else {
                // 코루틴 시작
                CoroutineScope(Dispatchers.Main).launch {
                    sendMessageWithoutImage(message)
                    Log.d("Gatherne", "No photoUri")
                }

            }
            selectedImageUri = null
            messageEditText.text.clear()
            binding.selectedImage.visibility = View.GONE


            // 검색 버튼 눌리면 키보드 숨기도록
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.sendButton.windowToken, 0)

            binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount)

            var userTokens: MutableList<String> = participantTokens
            Log.d("gather","삭제 전 userTOkens : ${userTokens}")

            userTokens.remove(CurrUser.getToken())
            Log.d("gather","삭제 후 userTOkens : ${userTokens}")
            // 알림 데이터 생성
            val pushNotificationData = PushNotificationData(
                type = "NORMAL",
                title = gatherTitle!!,
                message = message
            )

            // 알림 요청 데이터 생성
            val pushNotificationEntity = PushNotificationEntity(
                registration_ids = userTokens,
                priority = "high",
                data = pushNotificationData
            )

            // 알림 요청 전송
            val callPushNotification: Call<PushNotificationResponse> = FCMRetrofitProvider.getRetrofit().sendPushNotification(pushNotificationEntity)
            callPushNotification.enqueue(object : Callback<PushNotificationResponse> {
                override fun onResponse(call: Call<PushNotificationResponse>, response: Response<PushNotificationResponse>) {
                    Log.d("gather", "성공 $call, $response")
                    Log.d("gather", "$pushNotificationEntity")
                }

                override fun onFailure(call: Call<PushNotificationResponse>, t: Throwable) {
                    Log.d("gather", "실패 $t")
                    Log.d("gather", "실패 $call")
                }
            })

        }

    }

    private suspend fun uploadPhoto(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val fileName = "${System.currentTimeMillis()}.png"
            storage.reference.child("chat").child(fileName)
                .putFile(uri)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()
        }
    }

    private fun convertResizeImage(imageUri: Uri): Uri {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

        val tempFile = File.createTempFile("resized_image", ".jpg", this.cacheDir)
        val fileOutputStream = FileOutputStream(tempFile)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.close()

        return Uri.fromFile(tempFile)
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
                    Log.d("gather","압축 전 ${uri}")
                    selectedImageUri = convertResizeImage(uri)
                    Log.d("gather","압축 후 ${selectedImageUri}")
                    Glide.with(this)
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(30))
                        .override(300, 450) // 원하는 크기로 조정
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // 모든 이미지를 캐시
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

    private suspend fun sendMessageWithImage(imageUri: String, message: String) {
        val chatItem = ChatItem(
            senderId = userName,
            senderImage = userImage,
            message = message,
            image = imageUri,
            sendTime = getCurrentTime(),
            viewType = 0
        )
        chatDB?.push()?.setValue(chatItem)
        selectedImageUri = null
    }

    private suspend fun sendMessageWithoutImage(message: String) {
        if (message.isBlank()) {
            // Show an error message or handle it as desired
            Toast.makeText(this@ChatRoomActivity, "메시지를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val chatItem = ChatItem(
            senderId = userName,
            senderImage = userImage,
            message = message,
            image = "",
            sendTime = getCurrentTime(),
            viewType = 0
        )
        chatDB?.push()?.setValue(chatItem)
    }



    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)

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

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}