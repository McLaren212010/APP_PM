package com.example.app_pm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_pm.adapter.NoteAdapter
import com.example.app_pm.adapter.OnNoteItemClickListener
import com.example.app_pm.entities.Note
import com.example.app_pm.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), OnNoteItemClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private val newNoteActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteAdapter(this)
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val pnote = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_NOTE)
            val ppriority = data?.getStringExtra(NewNoteActivity.EXTRA_REPLY_PRIORITY)

            if (pnote!= null && ppriority != null) {
                val note = Note(note = pnote, priority = ppriority)
                noteViewModel.insert(note)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }


/*

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagartudo -> {
                noteViewModel.deleteAll()
                true
            }

            R.id.cidadesPortugal -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = CityAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.getCitiesByCountry("Portugal").observe(this, Observer { cities ->
                    // Update the cached copy of the words in the adapter.
                    cities?.let { adapter.setCities(it) }
                })

                true
            }

            R.id.todasCidades -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = CityAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.allCities.observe(this, Observer { cities ->
                    // Update the cached copy of the words in the adapter.
                    cities?.let { adapter.setCities(it) }
                })


                true
            }

            R.id.getCountryFromAveiro -> {
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.getCountryFromCity("Aveiro").observe(this, Observer { city ->
                    Toast.makeText(this, city.country, Toast.LENGTH_SHORT).show()
                })
                true
            }

            R.id.apagarAveiro -> {
                cityViewModel.deleteByCity("Aveiro")
                true
            }

            R.id.alterar -> {
                val city = City(id = 1, city = "xxx", country = "xxx")
                cityViewModel.updateCity(city)
                true
            }

            R.id.alteraraveiro -> {
                cityViewModel.updateCountryFromCity("Aveiro", "Japão")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}*/

    override fun onItemClick(note: Note, id: Note) {
        TODO("Not yet implemented")
    }
}