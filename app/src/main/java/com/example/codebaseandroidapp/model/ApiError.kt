package com.example.codebaseandroidapp.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ApiError (

    @SerializedName("status_code"    ) var statusCode    : Int?     = null,
    @SerializedName("status_message" ) var statusMessage : String  = "Something went wrong",
    @SerializedName("success"        ) var success       : Boolean = false,
    @SerialName("Message")
    val messageError: MessageErrorModel = MessageErrorModel(),
    var message: String = ""
) {
    fun getMessageError(): String {
        return if (message.isEmpty()) {
            "Kết nói máy chủ không thành công.\n"
        } else {
            message
        }
    }
}

@Serializable
data class MessageErrorModel(
    var message: String = "",
    var messageEn: String = "",
    val errorCode: Int = 0
)