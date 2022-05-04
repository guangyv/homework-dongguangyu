package com.bytedance.jstu.chapter5

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.chapter5.api.Youdao
import com.bytedance.jstu.chapter5.api.YoudaoChar
import com.bytedance.jstu.chapter5.api.YoudaoRes
import com.bytedance.jstu.chapter5.api.YoudaoSentence
import com.bytedance.jstu.chapter5.interceptor.TimeConsumeInterceptor
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class OkHttpActivity : AppCompatActivity() {

    private var requestBtn: Button? = null
    private var showText: TextView? = null
    private var editText: EditText? = null

    private val okhttpListener = object : EventListener() {
        @SuppressLint("SetTextI18n")
        override fun dnsStart(call: Call, domainName: String) {
            super.dnsStart(call, domainName)
//            updateShowTextView("\nDns Search: $domainName")
        }

        @SuppressLint("SetTextI18n")
        override fun responseBodyStart(call: Call) {
            super.responseBodyStart(call)
//            updateShowTextView("\nResponse Start")
        }
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TimeConsumeInterceptor())
        .eventListener(okhttpListener)
        .build()

    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_okhttp)
        requestBtn = findViewById(R.id.send_request)
        showText = findViewById(R.id.show_text)
        editText = findViewById(R.id.edit)

        editText?.setOnKeyListener { view, i, keyEvent -> run {
            if(keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                translate()
                true
            } else {
                false
            }
        } }

        requestBtn?.setOnClickListener {
            translate()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateShowTextView(text: String, append: Boolean = false) {
        if (Looper.getMainLooper() !== Looper.myLooper()) {
            // 子线程，提交到主线程中去更新 UI.
            runOnUiThread {
                updateShowTextView(text, append)
            }
        } else {
            showText?.text = if (append) showText?.text.toString() + text else text
        }
    }

    private fun request(url: String, callback: Callback) {
        val request: Request = Request.Builder()
            .url(url)
            .header("User-Agent", "Sjtu-Android-OKHttp")
            .build()
        client.newCall(request).enqueue(callback)
    }

    private fun translate() {
//        val url = "https://movie.querydata.org/api?id=25845392"
        val url = "https://dict.youdao.com/jsonapi?q=${editText?.text}"
        request(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                updateShowTextView(e.message.toString(), false)
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string()
                val responseText = if (response.isSuccessful) {
                    var res: String = "No data"
                    val detect = gson.fromJson(string, Youdao::class.java)
                    when {
                        "fanyi" in detect.meta.dicts -> {
                            val format = gson.fromJson(string, YoudaoSentence::class.java)
                            res = format.fanyi.tran
                        }
                        "blng_sents_part" in detect.meta.dicts -> {
                            try {
                                val format = gson.fromJson(string, YoudaoChar::class.java)
                                res = format.blng_sents_part.trs[1].tr
                            } catch (e: Exception) {
                                res = "No data"
                            }
                        }
                        "web_trans" in detect.meta.dicts -> {
                            try {
                                val format = gson.fromJson(string, YoudaoRes::class.java)
                                res = format.webTrans.webTranslation[0].value
                            } catch (e: Exception) {
                                res = "No data"
                            }
                        }
                    }
                    res
                } else {
                    "Response fail: ${string}, http status code: ${response.code}."
                }
                updateShowTextView(responseText, false)
            }
        })
    }
}