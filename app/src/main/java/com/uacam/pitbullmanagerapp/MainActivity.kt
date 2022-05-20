package com.uacam.pitbullmanagerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.uacam.pitbullmanagerapp.student.SignInStudentActivity
import com.uacam.pitbullmanagerapp.teacher.SignInTeacherActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        studentAppLoginButton.setOnClickListener{
            val intent = Intent(this, SignInStudentActivity::class.java)
            startActivity(intent)
        }
        teacherAppLoginButton.setOnClickListener{
            val intent = Intent(this, SignInTeacherActivity::class.java)
            startActivity(intent)
        }
    }
}