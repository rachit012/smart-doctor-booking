package com.example.frontened.utils


import android.util.Base64
import org.json.JSONObject
import java.util.Date

object JwtUtils {

    fun isTokenExpired(token: String): Boolean {
        return try {
            val payload = token.split(".")[1]
            val decodeBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodePayload = String(decodeBytes)

            val jsonObject = JSONObject(decodePayload)
            val exp = jsonObject.getLong("exp")

            val expiryDate = Date(exp * 1000)
            expiryDate.before(Date())
        }catch (e: Exception) {
            true
        }
    }
}