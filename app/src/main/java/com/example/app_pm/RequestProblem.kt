package com.example.app_pm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.request_problem.*

private lateinit var noteText: EditText



class RequestProblem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_problem)

        noteText = findViewById(R.id.insert_problem_box)

        val button1 = findViewById<Button>(R.id.button_confirm)
        button1.setOnClickListener {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(insert_problem_box.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY, insert_problem_box.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

        companion object {
            const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
            const val EXTRA_REPLY_ID = "com.example.android.id"
        }
}



