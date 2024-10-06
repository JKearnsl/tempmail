package com.jkearnsl.tempmail.ui.messages

data class Attachment(
    val filename: String,
    val size: Int,
    val contentType: String
)