package com.kosa.gather_e.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.navigationInfoParameters
import com.google.firebase.ktx.Firebase
import com.kosa.gather_e.BottomNavigationVarActivity
import com.kosa.gather_e.DBKey
import com.kosa.gather_e.R
import com.kosa.gather_e.SplashActivity
import com.kosa.gather_e.databinding.ActivityPostDetailBinding
import com.kosa.gather_e.model.entity.chat.ChatListItem
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.notification.PushNotificationData
import com.kosa.gather_e.model.entity.notification.PushNotificationEntity
import com.kosa.gather_e.model.entity.notification.PushNotificationResponse
import com.kosa.gather_e.model.entity.user.UserEntity
import com.kosa.gather_e.model.entity.user_gather.UserGather
import com.kosa.gather_e.model.repository.firebase.FCMRetrofitProvider
import com.kosa.gather_e.model.repository.spring.SpringRetrofitProvider
import com.kosa.gather_e.util.CurrUser
import com.kosa.gather_e.util.GatherUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var gatherEntity: GatherEntity
    val chatDB = Firebase.database.reference.child(DBKey.DB_CHATS)
    private lateinit var registration_ids : MutableList<String>
    private lateinit var currentParticipants : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        gatherEntity = intent.getSerializableExtra("gather") as GatherEntity
        val isBefore = GatherUtil.isGathering(gatherEntity)
        val isFull = GatherUtil.isFull(gatherEntity)

        val createQuery = chatDB.orderByChild("gatherTitle").equalTo(gatherEntity.gatherTitle)
        Log.d("gather", "create 채팅방 가져오기 : ${createQuery}")
        createQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val chatListItem = snapshot.getValue(ChatListItem::class.java)
                    Log.d("gather", "토큰들들들 : ${chatListItem?.participantTokens}")
                    registration_ids = chatListItem?.participantTokens!!
                    currentParticipants = chatListItem?.participants!!
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


        binding.backBtn.setOnClickListener {
            finish()
        }

        SpringRetrofitProvider.getRetrofit().getGatherDetail(gatherEntity.gatherSeq!!)
            .enqueue(object : Callback<GatherEntity> {
                override fun onResponse(
                    call: Call<GatherEntity>, response: Response<GatherEntity>
                ) {
                    with(response.body()!!) {
                        binding.categoryText.text = categoryName
                        binding.statusText.text =
                            if (GatherUtil.isGathering(this) && !GatherUtil.isFull(this)) {
                                "모집중"
                            } else {

                                "모집 종료"
                            }
                        binding.titleText.text = gatherTitle
                        binding.calendarText.text = gatherDate
                        binding.locationText.text = gatherLocationName
                        binding.contentText.text = gatherDescription
                        binding.cntText.text = "참여한 이웃 %d/%d".format(gatherUserCnt, gatherLimit)
                        Glide.with(this@PostDetailActivity).load(creatorImgUrl)
                            .into(binding.ownerProfileImg)
                        binding.ownerName.text = creatorName
                    }
                }

                override fun onFailure(call: Call<GatherEntity>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

        val call = SpringRetrofitProvider.getRetrofit().getGatherUsers(gatherEntity.gatherSeq!!)
        call.enqueue(object : Callback<List<UserEntity>> {
            override fun onResponse(
                call: Call<List<UserEntity>>, response: Response<List<UserEntity>>
            ) {
                var isJoined = false
                response.body()?.forEach {
                    if (CurrUser.getSeq() == it.userSeq) {
                        isJoined = true
                    }
                }
                if (isJoined && isBefore) setBtnCancle()
                else if (!isJoined && isBefore && !isFull) setBtnJoin()
                else if (!isBefore) setBtnGone()
                else setBtnDone()
            }

            override fun onFailure(call: Call<List<UserEntity>>, t: Throwable) {
            }
        })


        binding.shareSNS.setOnClickListener {

            val dynamicLink = initDynamicLink().toString()
            Log.d("gather","dynamic Link : : ${dynamicLink}")

            val msg = Intent(Intent.ACTION_SEND)

            msg.addCategory(Intent.CATEGORY_DEFAULT)
            msg.putExtra(
                Intent.EXTRA_TEXT, dynamicLink)
            msg.type = "text/plain"
            startActivity(Intent.createChooser(msg, "앱을 선택해 주세요"))


            val chooser = Intent.createChooser(msg, "친구에게 공유하기")
            startActivity(chooser)
        }


        setContentView(binding.root)
    }

    private fun initDynamicLink(): Uri {
        val playStoreUri : Uri =Uri.parse("https://play.google.com")
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://kosa.page.link/rniX")
            domainUriPrefix = "https://kosa.page.link/rniX"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                androidParameters(SplashActivity::class.java.packageName) {
                    fallbackUrl = playStoreUri
                }
            }
            navigationInfoParameters {
                forcedRedirectEnabled = true
            }
        }
        val dynamicLinkUri = dynamicLink.uri
        return dynamicLinkUri
    }

    fun setBtnJoin() {
        binding.joinBtn.setOnClickListener(null)
        binding.joinBtn.visibility = View.VISIBLE
        binding.joinBtn.text = "참가 하기"
        binding.joinBtn.backgroundTintList = ContextCompat.getColorStateList(
            this@PostDetailActivity, R.color.light_purple
        )
        binding.joinBtn.setOnClickListener {

            val call = SpringRetrofitProvider.getRetrofit()
                .joinGather(UserGather(gatherEntity.gatherSeq!!.toLong(), null))
            call.enqueue(object : Callback<UserGather> {
                override fun onResponse(call: Call<UserGather>, response: Response<UserGather>) {
                    Toast.makeText(this@PostDetailActivity, "모임에 참여하였습니다", Toast.LENGTH_SHORT)
                        .show()
//                    setBtnCancle()
                    finish()
                }

                override fun onFailure(call: Call<UserGather>, t: Throwable) {
                    t.printStackTrace()
                }
            })
            // 석현 작업
            // 채팅방 참여하기
            val query = chatDB.orderByChild("gatherTitle").equalTo(gatherEntity.gatherTitle)
            Log.d("gather", "detail 채팅방 가져오기 : ${query}")
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val chatListItem = snapshot.getValue(ChatListItem::class.java)
                        Log.d("gather", "detail 채팅방 가져오기 each : ${chatListItem}")

                        chatListItem?.participants?.add(CurrUser.getUserName())
                        chatListItem?.participantTokens?.add(CurrUser.getToken())
                        snapshot.ref.setValue(chatListItem)

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

            if(gatherEntity.gatherLimit <= currentParticipants.size+1){
                Log.d("gather", "${gatherEntity.gatherLimit} <= ${currentParticipants.size+1}")
                val pushNotificationData = PushNotificationData(
                    type = "NORMAL",
                    title = gatherEntity.gatherTitle,
                    message = "모임 정원이 충족되었습니다."
                )

                val pushNotificationEntity = PushNotificationEntity(
                    registration_ids = registration_ids,
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

            // 알림
            Log.d("gather", "채팅방 입장 토큰 : ${registration_ids}")
            // 알림 데이터 생성
            val pushNotificationData = PushNotificationData(
                type = "NORMAL",
                title = gatherEntity.gatherTitle,
                message = "${CurrUser.getUserName()}님이 참여하셨습니다"
            )

            // 알림 요청 데이터 생성
            val pushNotificationEntity = PushNotificationEntity(
                registration_ids = registration_ids,
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

    fun setBtnCancle() {
        binding.joinBtn.setOnClickListener(null)
        binding.joinBtn.visibility = View.VISIBLE
        binding.joinBtn.text = "참가 취소"
        binding.joinBtn.backgroundTintList = ContextCompat.getColorStateList(
            this@PostDetailActivity, R.color.light_purple_cancled
        )
        binding.joinBtn.setOnClickListener {

            val call =
                SpringRetrofitProvider.getRetrofit().leaveGather(gatherEntity.gatherSeq!!.toLong())
            call.enqueue(object : Callback<UserGather> {
                override fun onResponse(call: Call<UserGather>, response: Response<UserGather>) {
                    Toast.makeText(this@PostDetailActivity, "모임을 취소했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }

                override fun onFailure(call: Call<UserGather>, t: Throwable) {
                }
            })
            // 채팅방 나가기
            val query = chatDB.orderByChild("gatherTitle").equalTo(gatherEntity.gatherTitle)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        // 원하는 작업 수행
                        val chatListItem = snapshot.getValue(ChatListItem::class.java)
                        Log.d("gather", "detail 채팅방 가져오기 each : ${chatListItem}")

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

    fun setBtnGone() {
        binding.joinBtn.visibility = View.GONE
    }

    fun setBtnDone() {
        binding.joinBtn.visibility = View.VISIBLE
        binding.joinBtn.text = "모집 종료"
        binding.joinBtn.isEnabled = false
    }
}
