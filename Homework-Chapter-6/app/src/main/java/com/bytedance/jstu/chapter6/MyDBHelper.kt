package com.bytedance.jstu.chapter6

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDBHelper(val context: Context, name: String, version: Int): SQLiteOpenHelper(context, name, null, version) {

    private val createTodoList = "create table todoList(" +
            "id integer primary key autoincrement," +
            "task text," +
            "status integer)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTodoList)
//        Toast.makeText(context, "create todoList db success", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        if (oldVersion <= 1) {
//            db?.execSQL(createMessageList)
//        }
//        if (oldVersion <= 2) {
//            db?.execSQL(createUserList)
//        }
    }
}