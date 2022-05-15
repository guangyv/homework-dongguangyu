package com.bytedance.jstu.homework

import android.content.Context
import android.os.Build
import androidx.core.content.FileProvider
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import java.io.IOException

internal object PathUtils {
    fun getUriForFile(context: Context, path: String): Uri {
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context.applicationContext, context.applicationContext.packageName + ".fileprovider", File(path))
        } else {
            Uri.fromFile(File(path))
        }
    }
}