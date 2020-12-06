package com.example.app_pm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_pm.adapter.NoteAdapter
import com.example.app_pm.adapter.OnNoteItemClickListener
import com.example.app_pm.adapter.UserAdapter
import com.example.app_pm.api.EndPoints
import com.example.app_pm.api.ServiceBuilder
import com.example.app_pm.api.User
import com.example.app_pm.entities.Note
import com.example.app_pm.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Response
import java.nio.file.Files.delete
import javax.security.auth.callback.Callback


class MainActivity : AppCompatActivity(), OnNoteItemClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private val newNoteActivityRequestCode = 1
    private val updateNoteActivityRequestCode = 2




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            // Update the cached copy of the words in the adapter.
            notes?.let { adapter.setNotes(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewModel: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewModel.layoutPosition))
                Toast.makeText(this@MainActivity, "Note Deleted.", Toast.LENGTH_SHORT).show()

            }
        }).attachToRecyclerView(recyclerView)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            var pnote : String?
            var ppriority : String?
        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            pnote = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_NOTE)
            ppriority = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_PRIORITY)

            if (pnote != null && ppriority != null) {
                val note = Note(note = pnote, priority = ppriority)
                noteViewModel.insert(note)
            }

        } else if (requestCode == updateNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            pnote = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_NOTE)
            ppriority = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_PRIORITY)
            val pid = data?.getIntExtra(NewNoteActivity.EXTRA_REPLY_ID,-1)
            if (pnote != null && ppriority != null && pid != -1) {
                val note = Note(id = pid, note = pnote, priority = ppriority )
                Toast.makeText(this, note.priority, Toast.LENGTH_SHORT).show()
                noteViewModel.update(note)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.deletebutton -> {
                noteViewModel.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    override fun onItemClick(note: Note, priority: Int) {
        Toast.makeText(this, note.id.toString(), Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, NewNoteActivity::class.java)
        intent.putExtra("Note", note.note)
        intent.putExtra("Priority", note.priority)
        intent.putExtra("ID", note.id)
        startActivityForResult(intent, updateNoteActivityRequestCode)

    }
}