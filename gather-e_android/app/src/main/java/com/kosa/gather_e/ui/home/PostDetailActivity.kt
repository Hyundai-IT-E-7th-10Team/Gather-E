package com.kosa.gather_e.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.ActivityPostDetailBinding
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.user.CurrUser
import com.kosa.gather_e.model.entity.user.UserEntity
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gatherEntity = intent.getSerializableExtra("gather") as GatherEntity
        binding.backBtn.setOnClickListener {finish()}
        with(gatherEntity) {
            binding.categoryText.text = categoryName
            binding.statusText.text = "일단 보류"
            binding.titleText.text = gatherTitle
            binding.calendarText.text = gatherDate
            binding.locationText.text = gatherLocationName
            binding.contentText.text = gatherDescription
            binding.cntText.text = "참여한 이웃 %d/%d".format(gatherUserCnt, gatherLimit)
            Glide.with(this@PostDetailActivity).load(creatorImgUrl).into(binding.ownerProfileImg)
            binding.ownerName.text = creatorName
        }
        val call = SpringRetrofitProvider.getRetrofit()
            .getGatherUsers(gatherEntity.gatherSeq!!)
        call.enqueue(object : Callback<List<UserEntity>> {
            override fun onResponse(
                call: Call<List<UserEntity>>,
                response: Response<List<UserEntity>>
            ) {
                response.body()?.forEach {
                    if(CurrUser.getSeq() == it.userSeq) {
                        binding.joinBtn.text ="참가 취소"
                    }
                }
            }

            override fun onFailure(call: Call<List<UserEntity>>, t: Throwable) {
            }
        })

    }
}