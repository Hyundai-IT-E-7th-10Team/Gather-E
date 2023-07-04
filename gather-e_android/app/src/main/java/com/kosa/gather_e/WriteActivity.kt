package com.kosa.gather_e


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import com.kosa.gather_e.DBKey.Companion.DB_CHATS
import com.kosa.gather_e.databinding.ActivityWriteBinding
import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.location.SearchLocationEntity
import com.kosa.gather_e.util.CurrUser
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.ui.searchlocation.SearchLocationActivity
import com.naver.maps.map.overlay.OverlayImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WriteActivity : AppCompatActivity() {

    lateinit var binding : ActivityWriteBinding

    lateinit var gather : GatherEntity
    lateinit var chatRoom : ChatListItem
    lateinit var userName : String
    lateinit var userImage : String
    lateinit var myToken : String

    var date: String? = null
    var time: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.me { user, error ->
            if (user != null) {
                userName = user.kakaoAccount?.profile?.nickname.toString()
            }
        }
        UserApiClient.instance.me { user, error ->
            if (user != null) {
                userImage = user.kakaoAccount?.profile?.thumbnailImageUrl.toString()
            }
        }

        gather = GatherEntity(
            categorySeq = 0, // 선택된 카테고리 ID를 설정해야 함
            gatherTitle = "",
            gatherDate = "",
            gatherDescription = "",
            gatherLimit = 0,
            gatherLatitude = 0.0,
            gatherLongitude = 0.0,
            gatherLocationName = "",
            gatherSeq = 0,
            creatorImgUrl = "",
            creatorName = "",
            gatherCreator = 0,
            gatherUserCnt = 0,
            categoryName = ""
        )
        chatRoom = ChatListItem()
        myToken = CurrUser.getToken()
        // toolbar의 X(취소) 버튼
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        // toolbar의 작성 완료 버튼
        binding.completeBtn.setOnClickListener {
            // 완료 버튼 누르면 글 작성 완료
            gather.gatherTitle = binding.titleEditText.text.toString()
            gather.gatherDescription = binding.contentEditText.text.toString()
            gather.gatherLimit = binding.personnelNumberPicker.value
            gather.gatherDate = "$date $time"



//            // 데이터 유효성 검사 (예: 제목과 내용은 비어있으면 안됨)
//            if (title.isBlank() || gatherDescription.isBlank()) {
//                // 유효성 검사에 실패하면 사용자에게 알림
//                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            // 완료 버튼 누르면 채팅방 생성

            chatRoom.userId = userName
            chatRoom.gatherTitle = binding.titleEditText.text.toString()
            chatRoom.gatherDate = binding.dateText.text.toString() + " " + binding.timeText.text.toString()
            chatRoom.gatherLimit = binding.personnelNumberPicker.value
            chatRoom.key = System.currentTimeMillis()
            chatRoom.participants = mutableListOf(userName)
            chatRoom.participantTokens = mutableListOf(myToken)


            val chatDB = Firebase.database.reference.child(DB_CHATS)
            val newChatRoomRef = chatDB.push()

            newChatRoomRef.setValue(chatRoom)
                .addOnSuccessListener {
                }
                .addOnFailureListener { error ->
                }

            val callCreateGather: Call<GatherEntity> = SpringRetrofitProvider.getRetrofit().createGather(gather = gather)
            callCreateGather.enqueue(object : Callback<GatherEntity> {
                override fun onResponse(
                    call: Call<GatherEntity>,
                    response: Response<GatherEntity>
                ) {
                    Toast.makeText(this@WriteActivity, "${gather.gatherTitle} 모임이 생성되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("gather", "성공 $call, $response")
                    Log.d("gather", "$gather")
                }

                override fun onFailure(call: Call<GatherEntity>, t: Throwable) {
                    Toast.makeText(this@WriteActivity, "${gather.gatherTitle} 모임에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("gather", "실패 $t")
                    Log.d("gather", "실패 $call")
                }
            })
            finish()
        }


        // 카테고리 선택 버튼 동적으로 생성
        val callCategoryList: Call<List<CategoryEntity>> =
            SpringRetrofitProvider.getRetrofit().getCategory()
        callCategoryList.enqueue(object : Callback<List<CategoryEntity>> {
            override fun onResponse(
                call: Call<List<CategoryEntity>>, response: Response<List<CategoryEntity>>
            ) {
                if (response.isSuccessful) {
                    val categoryList = response.body()
                    categoryList?.let { categories ->
                        val buttonContainer: LinearLayout = findViewById(R.id.buttonContainer)

                        for (category in categories) {
                            val layout = LinearLayout(this@WriteActivity)
                            layout.orientation = LinearLayout.VERTICAL
                            layout.id = category.categorySeq
                            layout.gravity = Gravity.CENTER_HORIZONTAL

                            val imageView = ImageView(this@WriteActivity)
                            val imageLayoutParams = LinearLayout.LayoutParams(150, 150)  // Adjust width and height
                            imageView.layoutParams = imageLayoutParams
                            when (category.categorySeq) {
                                1 -> imageView.setImageResource(R.drawable.ic_1_football)
                                2 -> imageView.setImageResource(R.drawable.ic_2_tennis)
                                3 -> imageView.setImageResource(R.drawable.ic_3_golf)
                                4 -> imageView.setImageResource(R.drawable.ic_4_basketball)
                                5 -> imageView.setImageResource(R.drawable.ic_5_hiking)
                                6 -> imageView.setImageResource(R.drawable.ic_6_shuttlecock)
                                7 -> imageView.setImageResource(R.drawable.ic_7_volleyball)
                                8 -> imageView.setImageResource(R.drawable.ic_8_bowling)
                                9 -> imageView.setImageResource(R.drawable.ic_9_squash)
                                10 -> imageView.setImageResource(R.drawable.ic_10_pingpong)
                                11 -> imageView.setImageResource(R.drawable.ic_11_swimmig)
                                12 -> imageView.setImageResource(R.drawable.ic_12_riding)
                                13 -> imageView.setImageResource(R.drawable.ic_13_skate)
                                14 -> imageView.setImageResource(R.drawable.ic_14_cycling)
                                15 -> imageView.setImageResource(R.drawable.ic_15_yoga)
                                16 -> imageView.setImageResource(R.drawable.ic_16_pilates)
                                17 -> imageView.setImageResource(R.drawable.ic_17_climbing)
                                18 -> imageView.setImageResource(R.drawable.ic_18_billiard)
                                19 -> imageView.setImageResource(R.drawable.ic_19_dancing)
                                20 -> imageView.setImageResource(R.drawable.ic_20_boxing)

                            }

                            val textView = TextView(this@WriteActivity)
                            textView.text = category.categoryName
                            textView.setTextColor(Color.GRAY)
                            textView.textSize = 13f
                            textView.gravity = Gravity.CENTER

                            layout.addView(imageView)
                            layout.addView(textView)

                            buttonContainer.addView(layout)

                            layout.setOnClickListener {
                                gather.categorySeq = category.categorySeq
                                chatRoom.gatherCategory = category.categoryName
                                chatRoom.gatherCategorySeq = category.categorySeq
                                Log.d("Button Clicked", "Category ID: ${category.categoryName}")
                                Log.d("Button Clicked", "Category ID: ${category.categorySeq}")
                                layout.setBackgroundColor(Color.GREEN)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<CategoryEntity>>, t: Throwable) {
                Log.d("gather", "$t")
            }
        })


        // 만나는 날짜 - Calendar
        val calendar = Calendar.getInstance()
        // 년, 월, 일
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        binding.calendarBtn.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    binding.dateText.text = "$year/${month + 1}/${day}"
                    // DB로 넣기 위해 포맷팅
                    val formattedMonth = String.format("%02d", month + 1)
                    val formattedDay = String.format("%02d", day)
                    date = "$year/$formattedMonth/$formattedDay"
                },
                year, month, day
            )

            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }

        // 만나는 시간 - clock
        // 시, 분
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.clockBtn.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hour, minute ->
                    binding.timeText.text = "${hour}시 ${minute}분"
                    // DB로 넣기 위해 포맷팅
                    val formattedHour = String.format("%02d", hour)
                    val formattedMinute = String.format("%02d", minute)
                    time = "$formattedHour:$formattedMinute"
                },
                hour, minute, true
            )

            timePickerDialog.show()
        }

        // 장소
        binding.placeBtn.setOnClickListener {
            val intent = Intent(this, SearchLocationActivity::class.java)
            startForResult.launch(intent)
        }

        // 모집 인원
        binding.personnelNumberPicker.minValue = 2
        binding.personnelNumberPicker.maxValue = 22

        binding.personnelNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d("gather", "선택된 인원: $newVal")
        }
    }

    // LocationDetailActivity에서 선택한 장소를 받아옴
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedLocation = result.data?.getSerializableExtra("selectedLocation") as? SearchLocationEntity
            Log.d("gather", "writeActivity에서 ${selectedLocation.toString()}")
            if (selectedLocation != null) {
                binding.placeText.text = selectedLocation.place_name
                chatRoom.gatherPlace = selectedLocation.place_name

                gather.gatherLongitude = selectedLocation.x.toDouble()
                gather.gatherLatitude = selectedLocation.y.toDouble()
                gather.gatherLocationName = selectedLocation.place_name

            }
        }
    }

}