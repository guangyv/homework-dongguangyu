package com.bytedance.jstu.Chapter2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class QuestionShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_show)

        val tvTxt = findViewById<TextView>(R.id.text_question)
        val questionNum = intent.extras?.getInt("questionNum")
        val questionContent = getTxtContent("questionContent.txt").split("~")
        tvTxt.text = questionNum?.let { questionContent[it] }
    }

    private fun getTxtContent(fileName: String): String {
        var str = ""
        try {
            val ins = application.assets.open(fileName)
            val len = ins.available()
            val buffer = ByteArray(len)
            ins.read(buffer)
            str = String(buffer)
            ins.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return str
    }
}