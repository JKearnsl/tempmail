package com.jkearnsl.tempmail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class Api {

    private val client = OkHttpClient()
    private val baseUrl = "https://www.1secmail.com/api/v1/"

    suspend fun generateRandomEmail(): String {
        return withContext(Dispatchers.IO) {
            val url = "$baseUrl?action=genRandomMailbox"
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonArray = JSONArray(response.body?.string())
                jsonArray.getString(0)
            }
        }
    }

    suspend fun getMessages(email: String): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            val (login, domain) = email.split("@")
            val url = "$baseUrl?action=getMessages&login=$login&domain=$domain"
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonArray = JSONArray(response.body?.string())
                val messages = mutableListOf<Map<String, Any>>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val message = mapOf(
                        "id" to jsonObject.getInt("id"),
                        "from" to jsonObject.getString("from"),
                        "subject" to jsonObject.getString("subject"),
                        "date" to jsonObject.getString("date")
                    )
                    messages.add(message)
                }
                messages
            }
        }
    }

    suspend fun fetchMessage(email: String, id: Int): Map<String, Any> {
        return withContext(Dispatchers.IO) {
            val (login, domain) = email.split("@")
            val url = "$baseUrl?action=readMessage&login=$login&domain=$domain&id=$id"
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonObject = JSONObject(response.body?.string())
                val attachments = jsonObject.getJSONArray("attachments").let { array ->
                    List(array.length()) { i ->
                        val attachment = array.getJSONObject(i)
                        mapOf(
                            "filename" to attachment.getString("filename"),
                            "contentType" to attachment.getString("contentType"),
                            "size" to attachment.getInt("size")
                        )
                    }
                }
                mapOf(
                    "id" to jsonObject.getInt("id"),
                    "from" to jsonObject.getString("from"),
                    "subject" to jsonObject.getString("subject"),
                    "date" to jsonObject.getString("date"),
                    "attachments" to attachments,
                    "body" to jsonObject.getString("body"),
                    "textBody" to jsonObject.getString("textBody"),
                    "htmlBody" to jsonObject.getString("htmlBody")
                )
            }
        }
    }

    fun downloadAttachment(email: String, id: Int, fileName: String): ByteArray {
        val (login, domain) = email.split("@")
        val url = "$baseUrl?action=download&login=$login&domain=$domain&id=$id&file=$fileName"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.bytes() ?: throw IOException("Empty response body")
        }
    }
}