package com.moonsoft.noteit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val note = intent.getParcelableExtra<Note>("note")
        auth = FirebaseAuth.getInstance()

        if (note != null) {
            val etNoteUpdate = findViewById<EditText>(R.id.tv_todo_update)
            val btnUpdate = findViewById<Button>(R.id.btn_update)
            etNoteUpdate.setText(note.todoTask)

            btnUpdate.setOnClickListener {
                val note = etNoteUpdate.text
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                val date = LocalDateTime.now().format(formatter)
                val user = auth.currentUser?.email

                val userId = auth.currentUser!!.uid
                val editMap = mapOf(
                    "Note" to note,
                    "date" to date,
                    "user" to user
                )

            }

        }
    }
}