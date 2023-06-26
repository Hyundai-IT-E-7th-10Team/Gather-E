package com.kosa.gather_e

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kosa.gather_e.databinding.ActivityWriteBinding
import java.security.MessageDigest


class WriteActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // toolbar의 X(취소) 버튼
        binding.cancelBtn.setOnClickListener {
            finish()

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

        }
    }
}