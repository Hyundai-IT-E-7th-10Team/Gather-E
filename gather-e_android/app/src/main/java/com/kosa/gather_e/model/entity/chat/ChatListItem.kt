package com.kosa.gather_e.model.entity.chat

data class ChatListItem(
    var userId: String,
    var gatherTitle: String,
    var gatherDate: String,
    var gatherLimit: Int,
    var gatherCategory: String,
    var gatherCategorySeq: Int,
    var gatherPlace: String,
    var key: Long,
    var participants: List<String>, // 사용자 ID 리스트
    var participantTokens: List<String> // 사용자 토큰(token) 리스트

) {

    constructor(): this("", "","",0,"",0,"",0, emptyList(), emptyList())
}