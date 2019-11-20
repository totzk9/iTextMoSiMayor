package gte.com.itextmosimayor.chat_components

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gte.com.itextmosimayor.R
import kotlinx.android.synthetic.main.item_client_sender.*
import kotlinx.android.synthetic.main.item_image_department_sender.*


class ImageMessageItem(val message: ImageMessage,
                       val context: Context)
    : MessageItem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)

        val storageRef = FirebaseStorage.getInstance().reference.child(StorageUtil.pathToReference(message.imagePath))
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get()
                .load(uri).fit().centerInside()
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(viewHolder.imageView_message_image)
        }
        val dataReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        if (viewHolder.imgPP != null) {
            dataReference.child(message.senderId).child("img").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Picasso.get().load(dataSnapshot.value.toString()).fit().centerInside().into(viewHolder.imgPP)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }
    }

    override fun getLayout() =
            if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid)
                R.layout.item_image_department_sender
            else
                R.layout.item_image_client_sender

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}
