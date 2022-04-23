package com.bytedance.jstu.chapter3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindActivity(R.id.btn_ValueAnimator, ValueAnimatorActivity::class.java)
        bindActivity(R.id.btn_ObjectAnimator, ObjectAnimatorActivity::class.java)
    }

    private fun bindActivity(btnId: Int, activityClass: Class<*>) {
        findViewById<View>(btnId).setOnClickListener {
            startActivity(Intent(this, activityClass))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}