package com.bytedance.jstu.chapter7

import android.graphics.PixelFormat
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var buttonPlay: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonRePlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_video)

        videoView = findViewById(R.id.videoView)
        buttonPause = findViewById(R.id.buttonPause)
        buttonPlay = findViewById(R.id.buttonPlay)
        buttonRePlay = findViewById(R.id.buttonRePlay)

        buttonPause.setOnClickListener { videoView.pause() }
        buttonPlay.setOnClickListener { videoView.start() }
        buttonRePlay.setOnClickListener { videoView.resume() }
        videoView.holder.setFormat(PixelFormat.TRANSPARENT)
        videoView.setZOrderOnTop(true)
        videoView.setVideoPath(getVideoPath(R.raw.big_buck_bunny))
        videoView.setMediaController(MediaController(this))
    }

    private fun getVideoPath(resId: Int): String {
        return "android.resource://" + this.packageName + "/" + resId
    }
}