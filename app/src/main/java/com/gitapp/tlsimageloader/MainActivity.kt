package com.gitapp.tlsimageloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this)
            .load("https://sdo.gsfc.nasa.gov/assets/img/latest/latest_2048_HMIIC.jpg")
            .placeholder(android.R.drawable.sym_contact_card)
            .error(android.R.drawable.sym_def_app_icon)
            .into(img_pics);
    }

}
