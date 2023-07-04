package com.kosa.gather_e.util

import com.kosa.gather_e.model.entity.gather.GatherEntity
import java.text.SimpleDateFormat
import java.util.Date

object GatherUtil {

    fun isGathering(gather: GatherEntity): Boolean {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val dateTime = format.parse(gather.gatherDate)
        val now = Date()
        return now.before(dateTime)
    }
    fun isFull(gather: GatherEntity): Boolean {
        return gather.gatherUserCnt!! >= gather.gatherLimit
    }
}