package com.bytedance.jstu.Chapter2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_demo)

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)

        val adapter = SearchItemAdapter()
//        val data = (1..100).map { "这是第${it}行" }
        val questionList = getTxtContent("questionList.txt").split("\n")
        adapter.setContentList(questionList)
        rv.adapter = adapter

        val et = findViewById<EditText>(R.id.words_et)
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                adapter.setFilter(p0.toString())
            }
        })
    }

    private fun getTxtContent(fileName: String): String {
        var str = ""
        try {
            val ins = assets.open(fileName)
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