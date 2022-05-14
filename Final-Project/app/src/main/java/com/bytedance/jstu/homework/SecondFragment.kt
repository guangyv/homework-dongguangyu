package com.bytedance.jstu.homework

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bytedance.jstu.homework.databinding.FragmentSecondBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SecondFragment : Fragment() {
    private val TAG = "SecondFragment"

    private val PERMISSION_REQUEST_CODE = 1001
    private val REQUEST_CODE_RECORD = 1002
    private val REQUEST_CODE_TAKE_PHOTO_PATH = 1002

    private var mp4Path = ""
    private var takeImagePath: String = ""
    private lateinit var videoView: VideoView
    private lateinit var record: Button
    private lateinit var takePhoto: Button
    private lateinit var publish: Button

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val root: View = binding.root

        videoView = binding.videoview
        record = binding.record
        takePhoto = binding.photo
        publish = binding.publish

        if (!ThirdFragment.isLogined) {
            Toast.makeText(activity, "请先登录", Toast.LENGTH_SHORT).show()
            record.isEnabled = false
            takePhoto.isEnabled = false
            publish.isEnabled = false
        } else {
            record.isEnabled = true
            takePhoto.isEnabled = true
            publish.isEnabled = true
        }

        record.setOnClickListener {
            record()
        }
        takePhoto.setOnClickListener {
            takePhoto()
        }
        publish.setOnClickListener {
            publish()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun record() {
        requestPermission()
    }

    fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takeImagePath = outputMediaPath()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PathUtils.getUriForFile(binding.root.context, takeImagePath))
        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO_PATH)
        }
    }

    private fun outputMediaPath():String{
        val mediaStorageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile = File(mediaStorageDir, "IMG_$timeStamp.jpg")
        if (!mediaFile.exists()) {
            mediaFile.parentFile.mkdirs()
        }
        return mediaFile.absolutePath
    }

    private fun requestPermission() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(binding.root.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val hasAudioPermission = ContextCompat.checkSelfPermission(binding.root.context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (hasCameraPermission && hasAudioPermission) {
            recordVideo()
        } else {
            val permission: MutableList<String> = ArrayList()
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA)
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO)
            }
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, permission.toTypedArray(),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun recordVideo() {
        Log.d(TAG, "recordVideo: ")
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        mp4Path = outputMediaPath
        Log.d(TAG, "recordVideo: $outputMediaPath")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PathUtils.getUriForFile(binding.root.context, mp4Path))
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivityForResult(intent, REQUEST_CODE_RECORD)
        }
    }

    private val outputMediaPath: String
        private get() {
            val mediaStorageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val mediaFile = File(mediaStorageDir, "IMG_$timeStamp.mp4")
            if (!mediaFile.exists()) {
                mediaFile.parentFile.mkdirs()
            }
            return mediaFile.absolutePath
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermission = true
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false
                break
            }
        }
        if (hasPermission) {
            recordVideo()
        } else {
            Toast.makeText(binding.root.context, "权限获取失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_RECORD && resultCode == AppCompatActivity.RESULT_OK) {
            play()
        }
    }

    private fun play() {
        videoView.setVideoPath(mp4Path)
        videoView.start()
    }

    fun publish() {
        postVideo("admin", "extraValue", takeImagePath, mp4Path)
    }

    private fun postVideo(userName: String, extraValue: String, imgPath: String, videoPath: String) {
        val imageFile = File(imgPath)
        val imageBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("cover_image", imageFile.name, imageBody)
        val videoFile = File(videoPath)
        val videoBody = videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())
        val videoPart = MultipartBody.Part.createFormData("video", videoFile.name, videoBody)
        getRetrofit().create(VideoService::class.java)
            .postVideo("518051910008_post", userName, extraValue, imagePart, videoPart)
            .enqueue(object : Callback<PostVideoBean> {
                override fun onResponse(call: Call<PostVideoBean>, response: Response<PostVideoBean>) {
                    if (response.body()?.success == true) {
                        Log.d(TAG, "onResponse: ")
                        Toast.makeText(binding.root.context, "上传成功", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<PostVideoBean>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }
            })
    }
}