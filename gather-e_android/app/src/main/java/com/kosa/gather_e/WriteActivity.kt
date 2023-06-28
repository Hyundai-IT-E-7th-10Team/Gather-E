package com.kosa.gather_e


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kosa.gather_e.data.model.SearchLocationEntity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kosa.gather_e.data.model.chat.ChatListItem
import com.kosa.gather_e.databinding.ActivityWriteBinding
import com.kosa.gather_e.ui.searchlocation.SearchLocationActivity


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

        }

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


        binding.completeBtn.setOnClickListener {
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
        }




    }
}