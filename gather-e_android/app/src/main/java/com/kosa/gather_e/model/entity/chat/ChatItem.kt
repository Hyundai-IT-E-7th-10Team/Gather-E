package com.kosa.gather_e.model.entity.chat

data class ChatItem(
    val senderId: String,
    val senderImage: String,
    val message: String,
    val image: String,
    val sendTime: String,
    val viewType: Int
) {

    constructor(): this("","", "","","",0)
}
