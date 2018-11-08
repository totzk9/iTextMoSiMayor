package gte.com.itextmosimayor.SMS

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gte.com.itextmosimayor.R
import kotlinx.android.synthetic.main.item_conversation_text_message.view.*

abstract class MessageAdapter: RecyclerView.Adapter<MessageViewHolder>() {
//    var mDataset: String[]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForThread = layoutInflater.inflate(R.layout.item_conversation_text_message, parent, false )
        return MessageViewHolder(cellForThread)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.view.txtNumber.text = ""
    }
}

class MessageViewHolder(val view: View): RecyclerView.ViewHolder(view){

}