package com.bytedance.jstu.chapter6

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private val dbHelper = MyDBHelper(this, "Todo.db", 1)
    private var db: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = dbHelper.writableDatabase

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)

        val adapter = TodoAdapter(db)
        adapter.setContentList(getAllTasks())
        rv.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val dialog = layoutInflater.inflate(R.layout.dialog_layout, findViewById(R.id.edit))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(this)
                    .setTitle("新建事项")
                    .setView(dialog)
                    .setPositiveButton("ok") { _: DialogInterface, _: Int ->
                        val task = dialog.findViewById<EditText>(R.id.edit).text.toString()
                        val values = ContentValues().apply {
                            put("task", task)
                            put("status", "0")
                        }
                        db?.insert("todoList", null, values)
                        adapter.setContentList(getAllTasks())
                    }
                    .setNegativeButton("cancel") { _: DialogInterface, _: Int ->

                    }
                    .show()
            }
        }

        val et = findViewById<EditText>(R.id.words_et)
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                adapter.setFilter(p0.toString())
            }
        })
    }

    @SuppressLint("Range")
    fun getAllTasks(): List<TodoBean> {
        val cursor = (db?: dbHelper.writableDatabase).query("todoList", null, null, null, null, null, null, null)
        val todoList = ArrayList<TodoBean>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val task = cursor.getString(cursor.getColumnIndex("task"))
                val status = cursor.getInt(cursor.getColumnIndex("status"))
                todoList.add(TodoBean(id, task, status))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return todoList
    }
}