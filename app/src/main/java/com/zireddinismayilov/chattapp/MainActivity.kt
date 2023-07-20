package com.zireddinismayilov.chattapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.zireddinismayilov.chattapp.databinding.ActivityMainBinding
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var usersList: MutableList<User> = mutableListOf()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        setUpProfileInfo()
        setUpOneToOneChatRecyclerView()

    }

    private fun setUpOneToOneChatRecyclerView() {
        val peopleRecyclerView = binding.peopleRecyclerView
        peopleRecyclerView.adapter =
            DisplayUsersAdapter(usersList, object : DisplayUsersAdapter.OnItemClickListener {
                override fun onItemClick(user: User) {
                    val intent = Intent(this@MainActivity, OneToOneChat::class.java)
                    intent.putExtra("userdata", user)
                    startActivity(intent)
                }

            })
        peopleRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                val userdata = User(
                    document.getString("username").toString(),
                    document.getString("profilePhotoUrl").toString(),
                    document.getString("userId").toString()
                )
                if (userdata.userId != auth.currentUser?.uid) {
                    usersList.add(userdata)
                    peopleRecyclerView.adapter?.notifyDataSetChanged()
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
        }
    }

    private fun setUpProfileInfo() {
        binding.usernameTv.text = auth.currentUser?.displayName
        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .transform(CropCircleTransformation()).into(binding.profilePhoto)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}