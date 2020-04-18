package com.example.firebasechat

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechat.FirebaseHelper.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        mAuth = FirebaseAuth.getInstance()
        button_daftar.setOnClickListener{
            daftar()
        }
    }

    private fun daftar(){
        val name = daftar_name.text.toString().trim()
        val email = daftar_email.text.toString().trim()
        val password = daftar_password.text.toString().trim()
        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length > 6){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(){
                if(it.isSuccessful){
                    val me = mAuth.currentUser
                    if(me != null){
                        FirebaseHelper.pushUserData(name,email,me.uid)
                    }
                    finish()
                    Toast.makeText(this@RegisterActivity,"Register success", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@RegisterActivity,it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this@RegisterActivity,"Please, fill in the blank lines", Toast.LENGTH_SHORT).show()
        }
    }
}
