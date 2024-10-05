package com.jkearnsl.tempmail

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import com.jkearnsl.tempmail.ui.messages.Message
import okio.IOException
import org.json.JSONObject
import java.util.Locale

class Core() {

    private lateinit var sharedPreferences: SharedPreferences
    private val api = Api()
    public val messages = mutableListOf<Message>()

    constructor(sharedPreferences: SharedPreferences): this() {
        this.sharedPreferences = sharedPreferences
        sharedPreferences.getStringSet("last_messages", emptySet())?.mapNotNull { messageString ->
            val message = JSONObject(messageString)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val date = dateFormat.parse(message.getString("date"))

            Message(
                message.getInt("id"),
                message.getString("subject"),
                message.getString("from"),
                date
            )
        }?.sortedBy { it.date }?.reversed()?.let { messages.addAll(it) }
    }

    suspend fun currentEmail(): String {
        val email = sharedPreferences.getString("current_email", "null")?: "null"
        return if (email == "null") {
            try {
                api.generateRandomEmail()
            } catch (e: IOException) {
                "null@null.com"
            }
        } else {
            email
        }
    }

    suspend fun generateRandomEmail(): String {
        val email = api.generateRandomEmail()
        sharedPreferences.edit().putString("current_email", email).apply()
        sharedPreferences.edit().putStringSet("last_messages", emptySet()).apply()
        return email
    }

    suspend fun refreshMessages() {
        val email = sharedPreferences.getString("current_email", "null")?: "null"
        if (email == "null") {
            return
        }

        try {
            val newMessages = api.getMessages(email)
            sharedPreferences.edit().putStringSet(
                "last_messages",
                newMessages.map { message ->
                    JSONObject(message).toString()
                }.toSet()
            ).apply()
            this.messages.clear()
            this.messages.addAll(newMessages.map { message ->
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                val date = dateFormat.parse(message["date"] as String)

                Message(
                    message["id"] as Int,
                    message["subject"] as String,
                    message["from"] as String,
                    date
                )
            })
            this.messages.sortByDescending { it.date }
        } catch (e: IOException) {
            // Do nothing
        }
    }

    fun fetchMessage(email: String, id: Int): Map<String, Any> {
        return emptyMap()
    }

    fun fetchAttachment(email: String, id: Int, attachmentId: Int): ByteArray {
        return ByteArray(0)
    }

    fun deleteAttachment(email: String, id: Int, attachmentId: Int): Boolean {
        return false
    }
}