package com.example.app_pm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.app_pm.api.EndPoints
import com.example.app_pm.api.OutputPost
import com.example.app_pm.api.ServiceBuilder
import kotlinx.android.synthetic.main.recyclerview2_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var userEditTextView: EditText
private lateinit var passEditTextView: EditText
private lateinit var login_button: Button

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userEditTextView = findViewById(R.id.user)
        passEditTextView = findViewById(R.id.insert_password)
        login_button = findViewById(R.id.button_login)


        //Chama o sharedPref
        val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )

        //Vericia se o utilizador ainda está logado
        val automatic_login = sharedPref.getBoolean(getString(R.string.automatic_login), false)

        if( automatic_login ) {
            val intent = Intent(this@Login, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun clicklogin(view: View) {
        val name = userEditTextView.text.toString()
        val password = passEditTextView.text.toString()

        //ENCRIPTAR A PASSWORD
        val secretKey: String = "662ede816988e58fb6d057d9d85605e0"
        var encryptor = Encryption()
        val encryptedValue: String? = encryptor.encrypt(password, secretKey)
        println(encryptedValue)


        //Verifica se as caixas de texto têm conteudo
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.Name_Fail, Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.Pass_Fail, Toast.LENGTH_LONG).show()
            return
        } else {
            //fazer a ligaçao
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.login(name, encryptedValue)
            call.enqueue(object : Callback<OutputPost> {
                override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {

                    //Se a ligação ocorrer sem erros
                    if(response.isSuccessful) {
                        val p: OutputPost = response.body()!!
                       // Toast.makeText(this@Login, "No errors", Toast.LENGTH_SHORT).show()

                        //Se o valor error retornado for true
                        if (p.error) {
                            //Campo sucesso retornou com valor false
                            Toast.makeText(this@Login, R.string.Login_Failed, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(this@Login, R.string.Login_Done, Toast.LENGTH_SHORT).show()

                            //Envia os valores para a shared preferences para ocorrer o login automático
                            val sharedPref: SharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
                            with ( sharedPref.edit() ) {
                                putBoolean(getString(R.string.automatic_login), true)
                                putString(getString(R.string.username_login), name )
                                putInt(getString(R.string.id_login), p.data.id)
                                commit()
                                println(p.data.id)
                                println(R.string.id_login)
                            }

                            //Inicia a atividade dos mapas/main activity
                            val intent = Intent(this@Login, MapsActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }
                }

                override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                    Toast.makeText(this@Login, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    fun click_notas(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}