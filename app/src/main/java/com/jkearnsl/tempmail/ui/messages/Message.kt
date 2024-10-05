package com.jkearnsl.tempmail.ui.messages
import java.util.Date

data class Message(
    val id: Int,
    val subject: String,
    val email: String,
    val date: Date
)