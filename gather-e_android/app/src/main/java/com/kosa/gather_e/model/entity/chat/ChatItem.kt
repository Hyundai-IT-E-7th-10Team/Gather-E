package com.kosa.gather_e.model.entity.chat

data class ChatItem(
    val senderId: String,
    val message: String,
    val image: String
) {

    constructor(): this("", "","")
}
