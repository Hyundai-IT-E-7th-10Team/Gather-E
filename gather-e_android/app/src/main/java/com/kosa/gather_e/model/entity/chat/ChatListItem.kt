package com.kosa.gather_e.model.entity.chat

data class ChatListItem(
    val userId: String,
    val itemTitle: String,
    val key: Long
) {

    constructor(): this("", "", 0)
}