package com.zireddinismayilov.chattapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class OneToOneChatAdapter(private val messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var auth = FirebaseAuth.getInstance().currentUser

    private val SENDER_MESSAGE = 1
    private val RECEIVER_MESSAGE = 2


    inner class SenderMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessageText: TextView = itemView.findViewById(R.id.senderMessageText)
    }

    inner class ReceiverMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverMessageText: TextView = itemView.findViewById(R.id.receiverMessageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENDER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sender_message_item, parent, false)
                SenderMessageViewHolder(view)
            }

            RECEIVER_MESSAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.receiver_message_item, parent, false)
                ReceiverMessageViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SenderMessageViewHolder -> {
                holder.senderMessageText.text = message.message
            }

            is ReceiverMessageViewHolder -> {
                holder.receiverMessageText.text = message.message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == auth!!.uid) {
            SENDER_MESSAGE
        } else {
            RECEIVER_MESSAGE
        }
    }
}
