package gte.com.itextmosimayor.chat_components

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_client_sender.*
import kotlinx.android.synthetic.main.item_department_sender.*
import kotlinx.android.synthetic.main.item_image_client_sender.*
import kotlinx.android.synthetic.main.item_image_department_sender.*
import java.text.SimpleDateFormat


abstract class MessageItem(private val message: Message)
    : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTimeText(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        when {
            viewHolder.txtDateClient != null -> viewHolder.txtDateClient.text = dateFormat.format(message.time)
            viewHolder.txtDateClientImage != null -> viewHolder.txtDateClientImage.text = dateFormat.format(message.time)
            viewHolder.txtDateDept != null -> viewHolder.txtDateDept.text = dateFormat.format(message.time)
            viewHolder.txtDateDeptImage != null -> viewHolder.txtDateDeptImage.text = dateFormat.format(message.time)
        }
    }
}