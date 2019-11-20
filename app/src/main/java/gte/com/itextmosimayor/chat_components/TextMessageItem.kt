package gte.com.itextmosimayor.chat_components

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gte.com.itextmosimayor.R
import kotlinx.android.synthetic.main.item_client_sender.*
import kotlinx.android.synthetic.main.item_department_sender.*

class TextMessageItem(val message: TextMessage,
                      val context: Context)
    : MessageItem(message) {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        if (viewHolder.textView_message_text != null) {
            viewHolder.textView_message_text.text = message.text
            val dataReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
            dataReference.child(message.senderId).child("img").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Picasso.get().load(dataSnapshot.value.toString()).fit().centerInside().into(viewHolder.imgPP)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        } else
            viewHolder.txtMessage.text = message.text
        super.bind(viewHolder, position)
    }

    override fun getLayout() =
            if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid)
                R.layout.item_department_sender
            else
                R.layout.item_client_sender

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}