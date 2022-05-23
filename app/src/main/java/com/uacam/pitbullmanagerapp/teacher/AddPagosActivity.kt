package com.uacam.pitbullmanagerapp.teacher

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uacam.pitbullmanagerapp.databinding.ActivityAddClassesBinding
import com.uacam.pitbullmanagerapp.databinding.ActivityAddPagosBinding

class AddPagosActivity : AppCompatActivity() {

    private lateinit var bindingActivityAddPagos:  ActivityAddPagosBinding
    private val database = Firebase.database
    private val myRef = database.getReference("pagos")
    private val file = 1
    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityAddPagos = ActivityAddPagosBinding.inflate(layoutInflater)
        val view = bindingActivityAddPagos.root
        setContentView(view)


        bindingActivityAddPagos.saveButton.setOnClickListener {
            val namePagos :   String = bindingActivityAddPagos.namePagosEditText  .text.toString()
            val precio:       String = bindingActivityAddPagos.precioEditText     .text.toString()
            val date:         String = bindingActivityAddPagos.fechaEditText      .text.toString()
            val nameStudent:  String = bindingActivityAddPagos.teacherEditText    .text.toString()
            val nameTeacher:  String = bindingActivityAddPagos.studentEditText    .text.toString()
            val key: String = myRef.push().key.toString()


            if(fileUri==null){
                val mPagos = Pagos(namePagos, precio, date, nameStudent,nameTeacher)
                myRef.child(key).setValue(mPagos)
            }
            finish()
        }

    }
}