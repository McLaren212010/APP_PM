package com.example.app_pm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

private lateinit var distance: EditText

class Request_Distance : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu3: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu3, menu3)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //logout
            R.id.btn1 -> {

                //volta a atividade login (Main)
                val intent = Intent(this@Request_Distance, MapsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request__distance)


        distance = findViewById(R.id.distance)

        val button1 = findViewById<Button>(R.id.button_confirm)
        button1.setOnClickListener {
            if (TextUtils.isEmpty(distance.text.toString())) {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_LONG).show()
            }
            else
            {
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_REPLY, distance.text.toString().toDouble())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}