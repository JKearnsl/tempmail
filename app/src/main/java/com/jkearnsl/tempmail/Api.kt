package com.jkearnsl.tempmail

import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class Api {

    private val client = OkHttpClient()
    private val baseUrl = "https://www.1secmail.com/api/v1/"

    // Method to generate a random email address
    fun generateRandomEmail(): String {
        val url = "$baseUrl?action=genRandomMailbox"
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val jsonArray = JSONArray(response.body?.string())
            return jsonArray.getString(0)
        }
    }

    // Method to get messages from an email
    fun getMessages(email: String): List<Map<String, Any>> {
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
            return messages
        }
    }

    // Method to fetch a single message
    fun fetchMessage(email: String, id: Int): Map<String, Any> {
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
            return mapOf(
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

    // Method to download an attachment
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