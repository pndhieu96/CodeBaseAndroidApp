package com.example.codebaseandroidapp.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseDataPostAPI {
    @SerialName("manufactureId")
    var manufactureId: String = ""

    @SerialName("timeStamp")
    var timeStamp: String = ""


    var delayTime: Long = 0L
    var keyword: String = ""

    init {
        manufactureId = ""
    }
}