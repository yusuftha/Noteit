package com.moonsoft.noteit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class NoteAdapter(private val postList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteAdapter.PostHolder>() {
    private lateinit var database: FirebaseFirestore

    class PostHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(todo: Note) {
            val tvTodoTask = view.findViewById<TextView>(R.id.tv_todo)
            val tvTodoDate = view.findViewById<TextView>(R.id.tv_todo_date)
            tvTodoTask.text = todo.todoTask
            tvTodoDate.text = todo.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_todo, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val product = postList[position]
        holder.bind(product)
    }
}