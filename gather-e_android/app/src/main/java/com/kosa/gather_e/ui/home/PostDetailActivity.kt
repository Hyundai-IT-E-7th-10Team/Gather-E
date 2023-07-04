package com.kosa.gather_e.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kosa.gather_e.DBKey
import com.kosa.gather_e.R
import com.kosa.gather_e.databinding.ActivityPostDetailBinding
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.util.CurrUser
import com.kosa.gather_e.model.entity.user.UserEntity
import com.kosa.gather_e.model.entity.user_gather.UserGather
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.GatherUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var gatherEntity: GatherEntity
    val chatDB = Firebase.database.reference.child(DBKey.DB_CHATS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        gatherEntity = intent.getSerializableExtra("gather") as GatherEntity
        val isGathering = GatherUtil.isGathering(gatherEntity)
        binding.backBtn.setOnClickListener { finish() }
        with(gatherEntity) {
            binding.categoryText.text = categoryName
            binding.statusText.text = if (isGathering) {
                "모집중"
            } else {
                "모집 종료"
            }
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
                var isJoined = false
                response.body()?.forEach {
                    if (CurrUser.getSeq() == it.userSeq) {
                        isJoined = true
                    }
                }
                if(isJoined && isGathering) setBtnCancle()
                else if(!isJoined && isGathering) setBtnJoin()
                else if(isJoined && !isGathering) setBtnGone()
                else setBtnDone()
            }
            override fun onFailure(call: Call<List<UserEntity>>, t: Throwable) {
            }
        })
        if(isGathering) {
            binding.joinBtn.setOnClickListener{

            }
        }

        binding.shareSNS.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("image/*")
                intent.putExtra(Intent.EXTRA_STREAM, R.drawable.logo)
                val chooser = Intent.createChooser(intent, "친구에게 공유하기")
                startActivity(chooser)
        }

        setContentView(binding.root)
    }

    fun setBtnJoin() {
        binding.joinBtn.setOnClickListener(null)
        binding.joinBtn.visibility = View.VISIBLE
        binding.joinBtn.text = "참가 하기"
        binding.joinBtn.setOnClickListener{

            val call = SpringRetrofitProvider.getRetrofit().joinGather(UserGather(gatherEntity.gatherSeq!!.toLong(), null))
            call.enqueue(object: Callback<UserGather> {
                override fun onResponse(call: Call<UserGather>, response: Response<UserGather>) {
                    Toast.makeText(this@PostDetailActivity, "모임에 참여하였습니다", Toast.LENGTH_SHORT).show()
                    setBtnCancle()
                }
                override fun onFailure(call: Call<UserGather>, t: Throwable) {
                    t.printStackTrace()
                }
            })
            // 석현 작업
            // 채팅방 참여하기
            val query = chatDB.orderByChild("gatherTitle").equalTo(gatherEntity.gatherTitle)
            Log.d("gather","detail 채팅방 가져오기 : ${query}")
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val chatListItem = snapshot.getValue(ChatListItem::class.java)
                        Log.d("gather","detail 채팅방 가져오기 each : ${chatListItem}")

                        chatListItem?.participants?.add(CurrUser.getUserName())
                        chatListItem?.participantTokens?.add(CurrUser.getToken())

                        snapshot.ref.setValue(chatListItem)

                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }
    fun setBtnGone() {
        binding.joinBtn.visibility = View.GONE
    }

    fun setBtnCancle() {
        binding.joinBtn.setOnClickListener(null)
        binding.joinBtn.visibility = View.VISIBLE
        binding.joinBtn.text = "참가 취소"
        binding.joinBtn.backgroundTintList = ContextCompat.getColorStateList(
            this@PostDetailActivity,
            R.color.light_purple_cancled
        )
        binding.joinBtn.setOnClickListener{

            val call = SpringRetrofitProvider.getRetrofit().leaveGather(gatherEntity.gatherSeq!!.toLong())
            call.enqueue(object: Callback<UserGather> {
                override fun onResponse(call: Call<UserGather>, response: Response<UserGather>) {
                    Toast.makeText(this@PostDetailActivity, "모임을 취소했습니다.", Toast.LENGTH_SHORT).show()
                    setBtnCancle()
                }
                override fun onFailure(call: Call<UserGather>, t: Throwable) {
                    t.printStackTrace()
                }
            })
            // 채팅방 나가기
            val query = chatDB.orderByChild("gatherTitle").equalTo(gatherEntity.gatherTitle)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        // 원하는 작업 수행
                        val chatListItem = snapshot.getValue(ChatListItem::class.java)
                        Log.d("gather","detail 채팅방 가져오기 each : ${chatListItem}")

                        chatListItem?.participants?.remove(CurrUser.getUserName())
                        chatListItem?.participantTokens?.remove(CurrUser.getToken())

                        snapshot.ref.setValue(chatListItem)

                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }

    fun setBtnDone() {
        binding.joinBtn.visibility = View.VISIBLE
        binding.joinBtn.text = "모집 종료"
        binding.joinBtn.isEnabled = false
    }
}