package com.uacam.pitbullmanagerapp.teacher

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uacam.pitbullmanagerapp.databinding.ActivityAddClassesBinding

class AddClassesActivity : AppCompatActivity() {
    private lateinit var bindingActivityAddClasses: ActivityAddClassesBinding
    private val database = Firebase.database
    private val myRef = database.getReference("clases")
    private val file = 1
    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityAddClasses = ActivityAddClassesBinding.inflate(layoutInflater)
        val view = bindingActivityAddClasses.root
        setContentView(view)


        bindingActivityAddClasses.saveButton.setOnClickListener {
            val nameClasses : String = bindingActivityAddClasses.nameClassesEditText.text.toString()
            val date:         String = bindingActivityAddClasses.dateEditText       .text.toString()
            val nameStudent:  String = bindingActivityAddClasses.studentEditText    .text.toString()
            val nameTeacher:  String = bindingActivityAddClasses.teacherEditText    .text.toString()
            val description:  String = bindingActivityAddClasses.descriptionEditText.text.toString()
            val key: String = myRef.push().key.toString()
            val folder: StorageReference = FirebaseStorage.getInstance().reference.child("clases")
            val classesReference  : StorageReference = folder.child("img$key")

            if(fileUri==null){
                val mClasses = ClassesList(nameClasses, date, nameStudent, nameTeacher,description)
                myRef.child(key).setValue(mClasses)
            } else {
                classesReference.putFile(fileUri!!).addOnSuccessListener {
                    classesReference.downloadUrl.addOnSuccessListener { uri ->
                        val mClasses = ClassesList(nameClasses, date, nameStudent, nameTeacher,description ,uri.toString())
                        myRef.child(key).setValue(mClasses)
                    }
                }
            }
            finish()
        }
        bindingActivityAddClasses.posterImageView.setOnClickListener {
            fileUpload()
        }
    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, file)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == file) {
            if (resultCode == RESULT_OK) {
                fileUri = data!!.data
                bindingActivityAddClasses.posterImageView.setImageURI(fileUri)
            }
        }
    }
}