package com.zireddinismayilov.chattapp

import java.io.Serializable


data class User(
    var username: String,
    var profilePhotoUrl: String,
    var userId: String
) : Serializable
