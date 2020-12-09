package com.example.app_pm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.request_problem.*
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id



class Request_UserID : AppCompatActivity() {

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
                val intent = Intent(this@Request_UserID, MapsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request__user_i_d)




        val option1 = findViewById<Button>(R.id.option1)
        val option2 = findViewById<Button>(R.id.option2)


        option1.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EXTRA_REPLY, 1)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
        option2.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EXTRA_REPLY, 2)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

}
