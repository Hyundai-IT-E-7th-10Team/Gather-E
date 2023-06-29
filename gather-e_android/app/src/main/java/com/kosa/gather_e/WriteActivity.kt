package com.kosa.gather_e


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.getCategory
import androidx.recyclerview.widget.LinearLayoutManager
import com.kosa.gather_e.model.entity.location.SearchLocationEntity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.databinding.ActivityWriteBinding
import com.kosa.gather_e.model.entity.category.CategoryEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.ui.searchlocation.SearchLocationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WriteActivity : AppCompatActivity() {

    lateinit var binding : ActivityWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // toolbar의 X(취소) 버튼
        binding.cancelBtn.setOnClickListener {
            finish()

        }
        // toolbar의 작성 완료 버튼

        binding.completeBtn.setOnClickListener {
            // 완료 버튼 누르면 채팅방 생성
            val chatRoom = ChatListItem(
                userId = "user01",
                itemTitle = "chatRoom02",
                key = System.currentTimeMillis()
            )
            val chatDB = Firebase.database.reference.child("Chats")
            val newChatRoomRef = chatDB.push()

            newChatRoomRef.setValue(chatRoom)
                .addOnSuccessListener {
                }
                .addOnFailureListener { error ->
                }

            // 완료 버튼 누르면 글 작성 완료
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val selectedLocation = binding.placeText.text.toString()
            val selectedDate = binding.dateText.text.toString()
            val selectedTime = binding.timeText.text.toString()
            val selectedPersonnel = binding.personnelNumberPicker.value

            // 데이터 유효성 검사 (예: 제목과 내용은 비어있으면 안됨)
            if (title.isBlank() || content.isBlank()) {
                // 유효성 검사에 실패하면 사용자에게 알림
                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }


        // 카테고리 선택 버튼 동적으로 생성
        val callCategoryList: Call<List<CategoryEntity>> = SpringRetrofitProvider.getRetrofit().getCategory()
        callCategoryList.enqueue(object : Callback<List<CategoryEntity>> {
            override fun onResponse(call: Call<List<CategoryEntity>>, response: Response<List<CategoryEntity>>
            ) {
                Log.d("gather", "$call, $response") // 403 에러 떠서 retrofitProvider에 bearear 토큰 추가
                if(response.isSuccessful) {
                    val categoryList = response.body()
                    categoryList?.let { categories ->
                        val buttonContainer: LinearLayout = findViewById(R.id.buttonContainer)

                        for (category in categories){
                            val button = Button(this@WriteActivity)
                            button.text = category.categoryName
                            button.id = category.categorySeq

                            buttonContainer.addView(button)
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
            val datePickerDialog = DatePickerDialog(this,
                { _, year, month, day -> binding.dateText.text = "$year / ${month + 1} / ${day}"},
                year, month, day)

            // 오늘 날짜 이후로 가져오도록 함, 근데 왜 어제 날짜부터 가져오는지..?
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }

        // 만나는 시간 - clock
        // 시, 분
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.clockBtn.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this,
                { _, hour, minute -> binding.timeText.text = "${hour}시 ${minute}분"},
                hour, minute, true)

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

    // LocationDetailActivity에서 선택한 장소를 받아옴
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedLocation = result.data?.getSerializableExtra("selectedLocation") as? SearchLocationEntity
            Log.d("gather", "writeActivity에서 ${selectedLocation.toString()}")
            if (selectedLocation != null) {
                binding.placeText.text = selectedLocation.place_name
            }
        }
    }

}