package gte.com.itextmosimayor.chat_components

import androidx.annotation.Keep
import java.util.*

@Keep
data class ImageMessage(val imagePath: String,
                        override val time: Date,
                        override val senderId: String,
                        override val recipientId: String,
                        override val senderName: String,
                        override val type: String = MessageType.IMAGE)
    : Message {
    constructor() : this("", Date(0), "", "", "")
}