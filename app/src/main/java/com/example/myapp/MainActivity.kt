package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.random

class MainActivity : AppCompatActivity() {

    var cpt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            cpt++;
            textView.text = "${cpt}"
        }

        buttonReset.setOnClickListener {
            cpt = 0
            textView.text = "${cpt}"
        }
    }
}
