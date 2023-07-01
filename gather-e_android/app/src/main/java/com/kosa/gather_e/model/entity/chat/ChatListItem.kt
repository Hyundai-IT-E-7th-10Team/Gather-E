package com.kosa.gather_e.model.entity.chat

data class ChatListItem(
    var userId: String,
    var gatherTitle: String,
    var gatherDate: String,
    var gatherLimit: Int,
    var gatherCategory: String,
    var gatherPlace: String,
    var key: Long
) {

    constructor(): this("", "","",0,"","",0)
}