package com.uacam.pitbullmanagerapp.teacher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uacam.pitbullmanagerapp.R
import com.uacam.pitbullmanagerapp.databinding.ActivityAccountRecoveryTeacherBinding

class AccountRecoveryTeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountRecoveryTeacherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountRecoveryTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.senEmailAppCompatButton.setOnClickListener {
            val emailAddress = binding.emailEditText.text.toString()
            Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent (this,SignInTeacherActivity::class.java)
                    this.startActivity(intent)
                } else {
                    Toast.makeText(baseContext,"Ingrese un email de una cuenta valida.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}