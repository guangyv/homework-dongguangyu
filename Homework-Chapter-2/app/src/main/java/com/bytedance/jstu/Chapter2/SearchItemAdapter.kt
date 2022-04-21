package com.bytedance.jstu.Chapter2

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by shenjun on 2/21/22.
 */
class SearchItemAdapter : RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>() {

    private val contentList = mutableListOf<String>()
    private val filteredList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val v = View.inflate(parent.context, R.layout.search_item_layout, null)
        val view = SearchItemViewHolder(v)
        view.itemView.setOnClickListener {
            val intent = Intent(v.context, QuestionShowActivity::class.java)
            intent.putExtra("questionNum",view.adapterPosition)
            v.context.startActivity(intent)
        }

        return view
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun setContentList(list: List<String>) {
        contentList.clear()
        contentList.addAll(list)
        filteredList.clear()
        filteredList.addAll(list)
        notifyDataSetChanged()
    }

    fun setFilter(keyword: String?) {
        filteredList.clear()
        if (keyword?.isNotEmpty() == true) {
            filteredList.addAll(contentList.filter { it.contains(keyword) })
        } else {
            filteredList.addAll(contentList)
        }
        notifyDataSetChanged()
    }

    class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tv = view.findViewById<TextView>(R.id.search_item_tv)

        fun bind(content: String) {
            tv.text = content
        }
    }

}