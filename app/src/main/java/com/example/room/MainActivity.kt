package com.example.room

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        val btnSave: Button = findViewById(R.id.btnSave)
        val etLastName: EditText = findViewById(R.id.etLastName)
        val etInitials: EditText = findViewById(R.id.etInitials)
        val etPhoneNumber: EditText = findViewById(R.id.etPhoneNumber)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        adapter = ContactAdapter { contact ->
            contactViewModel.delete(contact)
            Toast.makeText(this, getString(R.string.contact_delete), Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnSave.setOnClickListener {
            val lastName = etLastName.text.toString().trim()
            val initials = etInitials.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()

            if (lastName.isEmpty() || !lastName.all { it.isLetter() }) {
                Toast.makeText(this, getString(R.string.input_lastname), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (initials.isEmpty() || !initials.all { it.isLetter() }) {
                Toast.makeText(this, getString(R.string.input_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty() || !phoneNumber.all { it.isDigit() }) {
                Toast.makeText(this, getString(R.string.input_phone), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val contact = Contact(lastName = lastName, initials = initials, phoneNumber = phoneNumber)
            contactViewModel.insert(contact)

            etLastName.text.clear()
            etInitials.text.clear()
            etPhoneNumber.text.clear()

            Toast.makeText(this, getString(R.string.contact_save), Toast.LENGTH_SHORT).show()
        }

        contactViewModel.allContacts.observe(this, Observer { contacts ->
            contacts?.let {
                adapter.setContacts(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}