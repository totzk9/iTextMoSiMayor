package gte.com.itextmosimayor.chat_components

import androidx.annotation.Keep

@Keep
data class User(val name: String,
                val bio: String,
                val profilePicturePath: String?,
                val registrationTokens: MutableList<String>) {
    constructor(): this("", "", null, mutableListOf())
}