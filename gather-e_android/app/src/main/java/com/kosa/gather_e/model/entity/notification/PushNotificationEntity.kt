package com.kosa.gather_e.model.entity.notification

import com.google.gson.annotations.SerializedName


data class PushNotificationEntity(
    @SerializedName("registration_ids")
    val registration_ids: List<String>,
    @SerializedName("priority")
    val priority: String,
    @SerializedName("data")
    val data: PushNotificationData
)

data class PushNotificationData(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String
)

data class PushNotificationResponse(
    val multicastId: Long,
    val success: Int,
    val failure: Int,
    val canonicalIds: Int,
    val results: List<PushNotificationResult>
)

data class PushNotificationResult(
    val messageId: String
)