package com.moonsoft.noteit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.moonsoft.noteit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private var recyclerViewAdapter: NoteAdapter? = null

    var postListesi = ArrayList<Note>()
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        pullData()

        binding.btnTodoAdd.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = NoteAdapter(postListesi)
        binding.recyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter!!.onClick = {
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra("note", it)
            startActivity(intent)
        }
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

    private fun pullData() {
        database.collection("NOTE").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            val documents = snapshot.documents
                            postListesi.clear()
                            for (document in documents) {
                                val todoTask = document.get("Note") as String
                                val date = document.get("date")
                                val user = document.get("user")
                                if (auth.currentUser?.email == user) {
                                    val indirilenPost =
                                        Note(
                                            todoTask,
                                            date.toString()
                                        )
                                    postListesi.add(indirilenPost)
                                }
                            }
                        }
                        recyclerViewAdapter?.notifyDataSetChanged()
                    }
                }
            }
    }
}