package com.example.denero.classwork6

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toolbar

/**
 * Created by DENERO on 25.12.2017.
 */
class PhotoFullActivity:AppCompatActivity() {
    lateinit var imgV:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_full_image)
        imgV = findViewById(R.id.show_img)
        imgV.setImageURI(intent.getParcelableExtra("data"))
    }
}