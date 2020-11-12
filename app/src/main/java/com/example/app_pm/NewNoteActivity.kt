package com.example.app_pm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.example.app_pm.dao.NoteDao
import com.example.app_pm.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files.delete

class NewNoteActivity : AppCompatActivity() {

    private lateinit var noteText: EditText
    private lateinit var priorityText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)



        noteText = findViewById(R.id.edit_note)
        priorityText = findViewById(R.id.edit_priority)

        val extras = intent.extras
        var id = -1
        // Verify if there's nothing coming on intent. If not starts the EditText empty
        if(extras != null){
            val note = intent.getStringExtra("Note").toString()
            id = intent.getIntExtra("ID",-1)
            val priority = intent.getStringExtra("Priority").toString()
            if(note.isNotEmpty()){
                priorityText.setText(priority)
                noteText.setText(note)
                noteText.setSelection(note.length)
                noteText.requestFocus()
            }
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(noteText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_NOTE, noteText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_PRIORITY, priorityText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_ID, id)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY_NOTE = "com.example.android.note"
        const val EXTRA_REPLY_PRIORITY = "com.example.android.priority"
        const val EXTRA_REPLY_ID = "com.example.android.id"
    }


}
