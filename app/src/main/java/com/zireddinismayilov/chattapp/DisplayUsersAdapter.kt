package com.zireddinismayilov.chattapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class DisplayUsersAdapter(
    var users: MutableList<User>,
    val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DisplayUsersAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePhoto: ImageView = itemView.findViewById<ImageView>(R.id.photo)
        val username: TextView = itemView.findViewById<TextView>(R.id.username)
        val lastMessage: TextView = itemView.findViewById<TextView>(R.id.lastmsg)

        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(users[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.people_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(users[position].profilePhotoUrl)
            .transform(CropCircleTransformation())
            .into(holder.profilePhoto)

        holder.username.text = users[position].username
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }
}