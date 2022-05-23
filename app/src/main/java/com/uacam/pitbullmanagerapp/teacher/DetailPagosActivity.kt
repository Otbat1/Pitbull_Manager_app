package com.uacam.pitbullmanagerapp.teacher

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uacam.pitbullmanagerapp.R
import com.uacam.pitbullmanagerapp.databinding.ActivityDetailPagosBinding

class DetailPagosActivity : AppCompatActivity() {
    private lateinit var bindingActivityDetailPagos: ActivityDetailPagosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingActivityDetailPagos = ActivityDetailPagosBinding.inflate(layoutInflater)
        val view = bindingActivityDetailPagos.root
        setContentView(view)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database
            .getReference("pagos").child(key.toString())

        myRef.addValueEventListener(object : ValueEventListener{

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val mPagos:Pagos?= dataSnapshot.getValue(Pagos::class.java)

                if (mPagos != null){
                    bindingActivityDetailPagos.namePagosTextView.text = mPagos.namePagos.toString()
                    bindingActivityDetailPagos.precioTextView.text = mPagos.precio.toString()
                    bindingActivityDetailPagos.fechaTextView.text = mPagos.date.toString()
                    bindingActivityDetailPagos.studentTextView.text = mPagos.nameStudent.toString()
                    bindingActivityDetailPagos.teacherTextView.text = mPagos.nameTeacher.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }
}