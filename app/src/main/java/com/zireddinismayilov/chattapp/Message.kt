package com.zireddinismayilov.chattapp

import java.io.Serializable

data class Message(
    var senderId: String? = "",
    var message: String? = ""
) : Serializable
