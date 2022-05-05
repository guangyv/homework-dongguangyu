package com.bytedance.jstu.chapter7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.image).setOnClickListener {
            val intent = Intent().setClass(this,GlideActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.video).setOnClickListener {
            val intent = Intent().setClass(this, VideoActivity::class.java)
            startActivity(intent)
        }
    }
}