package com.bytedance.jstu.chapter8

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {

    companion object{
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var surfaceView: SurfaceView
    private  var camera: Camera? = null
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var holder: SurfaceHolder
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var recordButton: Button
    private var isRecording = false
    private var mp4Path = ""
    private var isLighting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        surfaceView = findViewById(R.id.surfaceview)
        imageView = findViewById(R.id.iv_img)
        videoView = findViewById(R.id.videoview)
        recordButton = findViewById(R.id.bt_record)
        holder = surfaceView.holder
        initCamera()
        holder.addCallback(this)
    }

    private fun initCamera() {
        camera = Camera.open()

        camera?.let {
            val parameters = it.parameters
            parameters.pictureFormat = ImageFormat.JPEG
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
            parameters["orientation"] = "portrait"
            parameters["rotation"] = 90
            it.parameters = parameters
            it.setDisplayOrientation(90)
        }
    }

    private fun requestPermission() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (hasCameraPermission && hasAudioPermission) {
            return
        } else {
            val permission: MutableList<String> = ArrayList()
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA)
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO)
            }
            ActivityCompat.requestPermissions(this, permission.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermission = true
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false
                break
            }
        }
        if (hasPermission) {
            return
        } else {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        try {
            camera?.let {
                it.setPreviewDisplay(holder)
                it.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        if (holder.surface == null) {
            return
        }
        //停止预览效果
        camera?.stopPreview()
        //重新设置预览效果
        try {
            camera?.let {
                it.setPreviewDisplay(holder)
                it.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        camera?.let {
            it.stopPreview()
            it.release()
        }
    }

    override fun onResume() {
        super.onResume()
        if (camera == null) {
            initCamera()
        }
        camera?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        camera?.stopPreview()
    }

    fun takePhoto(view: View) {
        camera?.takePicture(null, null, pictureCallback)
    }

    //获取照片中的接口回调
    var pictureCallback = Camera.PictureCallback { data, camera ->
        var fos: FileOutputStream? = null
        val filePath =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + File.separator + "1.jpg"
        Log.d("TAGnima", "pictureCallback:$filePath")
        val file = File(filePath)
        try {
            fos = FileOutputStream(file)
            fos.write(data)
            fos.flush()
            val bitmap = BitmapFactory.decodeFile(filePath)
            val rotateBitmap = PathUtils.rotateImage(bitmap, filePath)
            imageView.visibility = View.VISIBLE
            videoView.visibility = View.GONE
            imageView.setImageBitmap(rotateBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            this.camera?.startPreview()
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun record(view: View) {
        if (isRecording && mediaRecorder != null) {
            recordButton.text = "录制"
            val mediaRecorder = this.mediaRecorder ?:return

            mediaRecorder.setOnErrorListener(null)
            mediaRecorder.setOnInfoListener(null)
            mediaRecorder.setPreviewDisplay(null)
            try {
                mediaRecorder.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mediaRecorder.reset()
            mediaRecorder.release()
            this.mediaRecorder = null
            camera?.lock()
            videoView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            videoView.setVideoPath(mp4Path)
            videoView.start()
        } else {
            if (prepareVideoRecorder()) {
                recordButton.text = "暂停"
                mediaRecorder!!.start()
            }
        }
        isRecording = !isRecording
    }

    private val outputMediaPath: String
        private get() {
            val mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            Log.d("TAGnima", ": $mediaStorageDir")
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val mediaFile = File(mediaStorageDir, "IMG_$timeStamp.mp4")
            if (!mediaFile.exists()) {
                mediaFile.parentFile.mkdirs()
            }
            return mediaFile.absolutePath
        }

    private fun prepareVideoRecorder(): Boolean {
        val mediaRecorder = MediaRecorder()
        this.mediaRecorder = mediaRecorder

        // Step 1: Unlock and set camera to MediaRecorder
        camera?.unlock()
        mediaRecorder.setCamera(camera)

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))

        // Step 4: Set output file
        mp4Path = outputMediaPath
        mediaRecorder.setOutputFile(mp4Path)

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(holder.surface)
        mediaRecorder.setOrientationHint(90)

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare()
        } catch (e: IllegalStateException) {
            releaseMediaRecorder()
            return false
        } catch (e: IOException) {
            releaseMediaRecorder()
            return false
        }
        return true
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.let { mediaRecorder->
            mediaRecorder.reset() // clear recorder configuration
            mediaRecorder.release() // release the recorder object
            this.mediaRecorder = null
            camera?.lock() // lock camera for later use
        }
    }

    fun light(view: View) {
        camera?.let {
            val parameters = it.parameters
            if (isLighting) {
                parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                isLighting = false
            } else {
                parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                isLighting = true
            }
            it.parameters = parameters
        }
    }
}