package com.kosa.gather_e.data.model.chat
data class ChatItem(
    val senderId: String,
    val message: String
) {

    constructor(): this("", "")
}
