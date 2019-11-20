package gte.com.itextmosimayor.chat_components

import androidx.annotation.Keep

@Keep
data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}