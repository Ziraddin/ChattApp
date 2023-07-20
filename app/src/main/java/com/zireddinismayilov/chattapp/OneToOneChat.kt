package com.zireddinismayilov.chattapp

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.zireddinismayilov.chattapp.databinding.ActivityOneToOneChatBinding
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class OneToOneChat : AppCompatActivity() {
    private lateinit var binding: ActivityOneToOneChatBinding
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var messages: MutableList<Message>
    private lateinit var userData: User
    private lateinit var oneToOneChatRecyclerView: RecyclerView
    private lateinit var adapter: OneToOneChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOneToOneChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userdata") as User
        binding.usernameOneToOne.text = userData.username
        Picasso.get().load(userData.profilePhotoUrl).transform(CropCircleTransformation())
            .into(binding.profilePhotoOnetoOne)

        oneToOneChatRecyclerView = binding.oneToOneChatRecyclerView
        messages = mutableListOf()
        adapter = OneToOneChatAdapter(messages)
        oneToOneChatRecyclerView.adapter = adapter
        oneToOneChatRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference

        recieveInComingMessage()
        recieveSendedMessage()

        binding.sendMessageEditText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = (view as EditText).compoundDrawablesRelative[2]
                if (event.rawX >= (view.right - drawableEnd.bounds.width())) {
                    sendMessage()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }


    }


    fun recieveInComingMessage() {
        db.child("${userData.userId}-${auth.currentUser?.uid}")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java) as Message
                    messages.add(message)
                    oneToOneChatRecyclerView.adapter?.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun recieveSendedMessage() {
        db.child("${auth.currentUser?.uid}-${userData.userId}")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java) as Message
                    messages.add(message)
                    oneToOneChatRecyclerView.adapter?.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun sendMessage() {
        val messageText = binding.sendMessageEditText.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val message = Message(auth.currentUser?.uid.toString(), messageText)
            db.child("${auth.currentUser?.uid}-${userData.userId}").push().setValue(message)
            binding.sendMessageEditText.text.clear()
        } else {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show()
        }
    }

}
