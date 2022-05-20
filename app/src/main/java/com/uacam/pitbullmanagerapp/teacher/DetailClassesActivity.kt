package com.uacam.pitbullmanagerapp.teacher

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uacam.pitbullmanagerapp.databinding.ActivityDetailClassesBinding

class DetailClassesActivity : AppCompatActivity() {
    private lateinit var bindingActivityDetailClasses: ActivityDetailClassesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityDetailClasses = ActivityDetailClassesBinding.inflate(layoutInflater)
        val view = bindingActivityDetailClasses.root
        setContentView(view)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("clases").child(
            key.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mClassesList:ClassesList? = dataSnapshot.getValue(ClassesList::class.java)
                if (mClassesList != null) {
                    bindingActivityDetailClasses.nameClassesTextView.text = mClassesList.nameClasses.toString()
                    bindingActivityDetailClasses.nameTeacherTextView.text = mClassesList.nameTeacher.toString()
                    bindingActivityDetailClasses.dateTextView.text = mClassesList.date.toString()
                    bindingActivityDetailClasses.descriptionTextView.text = mClassesList.description.toString()
                    Glide.with(view).load(mClassesList.url.toString()).into(bindingActivityDetailClasses.posterImgeView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }
}