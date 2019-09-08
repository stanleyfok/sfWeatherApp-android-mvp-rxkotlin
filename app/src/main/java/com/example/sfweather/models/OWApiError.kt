package com.example.sfweather.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

data class OWApiError(
    @SerializedName("cod") var code: String,
    @SerializedName("message") var message: String
) {
    companion object Factory {
        fun createFromResponse(responeBody: ResponseBody): OWApiError {
            val gson = Gson()
            val apiError = gson.fromJson(responeBody.charStream(), OWApiError::class.java)

            return apiError
        }
    }
}