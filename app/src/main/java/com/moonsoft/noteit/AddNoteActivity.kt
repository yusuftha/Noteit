package com.moonsoft.noteit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.moonsoft.noteit.databinding.ActivityAddNoteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        backButton()
        binding.btnAddTodo.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val todoTask = binding.etTodoTask.text.toString()
            val date = LocalDateTime.now().format(formatter)
            val user = auth.currentUser?.email
            if (todoTask.isNotEmpty()) {
                val postHashMap = hashMapOf<String, Any>()
                postHashMap["Note"] = todoTask
                postHashMap["date"] = date
                postHashMap["user"] = user.toString()
                database.collection("NOTE").add(postHashMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, "Not Eklendi!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Lütfen boş bırakmayın!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun backButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                auth.signOut()
                Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.settings -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}