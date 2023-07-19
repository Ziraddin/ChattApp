package com.zireddinismayilov.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.zireddinismayilov.chattapp.databinding.ActivityMainBinding
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.usernameTv.text = auth.currentUser?.displayName
        Picasso.get()
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .transform(CropCircleTransformation())
            .into(binding.profilePhoto)
    }
}