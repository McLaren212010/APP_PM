package com.example.app_pm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.example.app_pm.entities.Note

class NewNoteActivity : AppCompatActivity() {

    private lateinit var noteText: EditText
    private lateinit var priorityText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        noteText = findViewById(R.id.edit_note)
        priorityText = findViewById(R.id.edit_priority)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(noteText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_NOTE, noteText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_PRIORITY, priorityText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY_NOTE = "com.example.android.note"
        const val EXTRA_REPLY_PRIORITY = "com.example.android.priority"
    }
}
