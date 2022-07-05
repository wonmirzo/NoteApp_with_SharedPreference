package com.wonrmizo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.wonrmizo.R
import com.wonrmizo.adapter.NoteAdapter
import com.wonrmizo.manager.PrefsManager
import com.wonrmizo.model.Note
import com.wonrmizo.util.Time

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNotesStatus: TextView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        prefsManager = PrefsManager(this)

        val ivTrash: ImageView = findViewById(R.id.ivTrash)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        noteAdapter = NoteAdapter(getAllNotes())
        recyclerView.adapter = noteAdapter
        tvNotesStatus = findViewById(R.id.tvNotesStatus)

        val fabAddNote: ExtendedFloatingActionButton = findViewById(R.id.fabAddNote)

        fabAddNote.setOnClickListener {
            openDialog()
        }

        tvNotesStatus.isVisible = noteAdapter.notes.isEmpty()

        ivTrash.setOnClickListener {
            prefsManager.clearAllNote()
            Toast.makeText(this, "Notes has cleared!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun getAllNotes(): ArrayList<Note> {
        if (prefsManager.getNote().isEmpty()) {
            return arrayListOf()
        }
        return prefsManager.getNote()
    }

    @SuppressLint("InflateParams")
    private fun openDialog() {
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.custom_dialog_view, null)
        val builder = AlertDialog.Builder(this).create()
        builder.setView(view)
        val etNote = view.findViewById<EditText>(R.id.etNote)
        val btnCancel = view.findViewById<MaterialButton>(R.id.btnCancel)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)

        btnCancel.setOnClickListener {
            builder.dismiss()
        }

        btnSave.setOnClickListener {
            val note = etNote.text.toString().trim()
            if (note.isEmpty()) {
                Toast.makeText(this, "Please write some notes", Toast.LENGTH_SHORT)
                    .show()
            } else {
                noteAdapter.addNote(Note(Time.timeStamp(), note))
                prefsManager.saveNote(noteAdapter.notes)
                tvNotesStatus.isVisible = noteAdapter.notes.isEmpty()
                builder.dismiss()
            }
        }
        builder.show()
    }

}