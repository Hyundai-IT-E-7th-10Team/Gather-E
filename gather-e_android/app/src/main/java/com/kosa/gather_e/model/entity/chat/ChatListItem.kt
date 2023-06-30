package com.kosa.gather_e.model.entity.chat

data class ChatListItem(
    val userId: String,
    val gatherTitle: String,
    val gatherDate: String,
    val gatherLimit: Int,
    val gatherCategory: String,
    val key: Long
) {

    constructor(): this("", "","",0,"",0)
}