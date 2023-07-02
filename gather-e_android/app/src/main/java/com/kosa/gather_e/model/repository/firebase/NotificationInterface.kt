package com.kosa.gather_e.model.repository.firebase

import com.kosa.gather_e.model.entity.notification.PushNotificationEntity
import com.kosa.gather_e.model.entity.notification.PushNotificationResponse
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST




interface NotificationInterface {
    @POST("fcm/send")
    fun sendPushNotification(@Body pushNotificationEntity: PushNotificationEntity): Call<PushNotificationResponse>
}