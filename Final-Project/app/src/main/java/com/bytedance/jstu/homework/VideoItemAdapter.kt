package com.bytedance.jstu.homework

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class VideoItemAdapter(private val videoList: MutableList<VideoBean.Feeds>): RecyclerView.Adapter<VideoItemAdapter.ViewHolder>() {

    private val TAG = "VideoItemAdapter"
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  //绑定数据
        val userName = videoList[position].user_name
        val extraValue = videoList[position].extra_value
        val createdAt = videoList[position].createdAt
        val videoUrl = videoList[position].video_url
        val imageUrl = videoList[position].image_url
        val video = holder.videoView
        val videoCover = holder.videoCover

        holder.userName.text = "$userName"
        holder.extraValue.text = "$extraValue"
        holder.createdAt.text = "$createdAt"
        videoCover.setImageURI(Uri.parse(imageUrl))
        video.setVideoURI(Uri.parse(videoUrl))
        video.setOnPreparedListener {
            videoCover.visibility = View.GONE
            video.start()
        }
        video.setOnCompletionListener {
            video.start()
        }
        video.setOnTouchListener { view: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (video.isPlaying) video.pause() else video.start()
                }
                MotionEvent.ACTION_MOVE -> {

                }
                MotionEvent.ACTION_UP -> {

                }
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: VideoView = itemView.findViewById(R.id.videoView)
        val videoCover: ImageView = itemView.findViewById(R.id.videoCover)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val extraValue: TextView = itemView.findViewById(R.id.extraValue)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
    }
}