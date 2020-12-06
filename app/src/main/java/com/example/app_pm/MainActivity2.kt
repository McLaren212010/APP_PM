package com.example.app_pm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_pm.adapter.UserAdapter
import com.example.app_pm.api.EndPoints
import com.example.app_pm.api.OutputPost
import com.example.app_pm.api.ServiceBuilder
import com.example.app_pm.api.User
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Response
import retrofit2.Callback


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()

        call.enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(call: retrofit2.Call<List<User>>, response: retrofit2.Response<List<User>>){
                if(response.isSuccessful){
                    recyclerview.apply{
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity2)
                        adapter = UserAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<List<User>>, t:Throwable){
                Toast.makeText(this@MainActivity2, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun getSingle(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUserById(2) // estaticamente o valor 2. deverá depois passar a ser dinamico

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: retrofit2.Call<User>, response: retrofit2.Response<User>) {
                if (response.isSuccessful){
                    val c: User = response.body()!!
                    Toast.makeText(this@MainActivity2, c.address.zipcode, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
                Toast.makeText(this@MainActivity2, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun post(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
       // val call = request.postTest("teste")

        /*call.enqueue(object : Callback<OutputPost>{
            override fun onResponse(call: retrofit2.Call<OutputPost>, response: retrofit2.Response<OutputPost>) {
                if (response.isSuccessful){
                    val c: OutputPost = response.body()!!
                   // Toast.makeText(this@MainActivity2, c.id.toString() + "-" + c.title, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@MainActivity2, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    fun map(view: View) {
        val intent = Intent(this, MapsActivity::class.java).apply {
        }
        startActivity(intent)

    }
}