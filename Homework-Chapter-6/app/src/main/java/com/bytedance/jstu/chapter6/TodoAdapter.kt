package com.bytedance.jstu.chapter6

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by shenjun on 2/21/22.
 */
class TodoAdapter(private var db: SQLiteDatabase?) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private val TAG = "TodoAdapter"

    private val contentList = mutableListOf<TodoBean>()
    private val filteredList = mutableListOf<TodoBean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val v = View.inflate(parent.context, R.layout.todo_layout, null)
        val todoViewHolder = TodoViewHolder(v)
        todoViewHolder.checkBox.setOnCheckedChangeListener { _, b ->
            val id: Int = todoViewHolder.adapterPosition + 1
            if (b) {
                val values = ContentValues().apply {
                    put("status", 1)
                }
                db?.update("todoList", values, "id = ?",
                    arrayOf(id.toString())
                )
                Log.d(TAG, "onCreateViewHolder: $id")
            } else {
                Log.d(TAG, "onCreateViewHolder: 2")
                val values = ContentValues().apply {
                    put("status", 0)
                }
                db?.update("todoList", values, "id = ?",
                    arrayOf(id.toString())
                )
            }
        }
        return todoViewHolder
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun setContentList(list: List<TodoBean>) {
        contentList.clear()
        contentList.addAll(list)
        filteredList.clear()
        filteredList.addAll(list)
        notifyDataSetChanged()
    }

    fun setFilter(keyword: String?) {
        filteredList.clear()
        if (keyword?.isNotEmpty() == true) {
            filteredList.addAll(contentList.filter { it.task.contains(keyword) })
        } else {
            filteredList.addAll(contentList)
        }
        notifyDataSetChanged()
    }

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val checkBox: CheckBox = view.findViewById(R.id.todoCheckBox)

        fun bind(content: TodoBean) {
            checkBox.text = content.task
            checkBox.isChecked = content.status == 1
        }
    }

}
