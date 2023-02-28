package com.example.codebaseandroidapp.model

import com.google.gson.annotations.SerializedName

data class ApiError (

    @SerializedName("status_code"    ) var statusCode    : Int?     = null,
    @SerializedName("status_message" ) var statusMessage : String  = "Something went wrong",
    @SerializedName("success"        ) var success       : Boolean = false

)