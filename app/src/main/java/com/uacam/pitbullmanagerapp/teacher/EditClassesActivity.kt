package com.uacam.pitbullmanagerapp.teacher

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uacam.pitbullmanagerapp.databinding.ActivityEditClassesBinding

class EditClassesActivity : AppCompatActivity() {

    private lateinit var bindingActivityEditClasses: ActivityEditClassesBinding
    private val file = 1
    private var fileUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityEditClasses = ActivityEditClassesBinding.inflate(layoutInflater)
        val view = bindingActivityEditClasses.root
        setContentView(view)
        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("clases").child(
            key.toString()
        )
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mClassesList: ClassesList? = dataSnapshot.getValue(ClassesList::class.java)
                if (mClassesList != null) {

                    bindingActivityEditClasses.nameClassesEditText.text = Editable.Factory.getInstance().newEditable(mClassesList.nameClasses)
                    bindingActivityEditClasses.dateEditText.text = Editable.Factory.getInstance().newEditable(mClassesList.date)
                    bindingActivityEditClasses.studentEditText.text = Editable.Factory.getInstance().newEditable(mClassesList.nameStudent)
                    bindingActivityEditClasses.teacherEditText.text = Editable.Factory.getInstance().newEditable(mClassesList.nameTeacher)
                    bindingActivityEditClasses.descriptionEditText.text = Editable.Factory.getInstance().newEditable(mClassesList.description)

                    imageUrl = mClassesList.url.toString()
                    if (fileUri==null){
                        Glide.with(view).load(imageUrl).into(bindingActivityEditClasses.posterImageView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())            }
        })
        bindingActivityEditClasses.saveButton.setOnClickListener {
            /**
            val nameClasses: String? = null,
            val nameTeacher: String? = null,
            val nameStudent: String? = null,
            val date: String? = null,
            val description: String? = null,
            val url: String? = null,
             **/
            val nameClasses : String = bindingActivityEditClasses.nameClassesEditText.text.toString()
            val date : String = bindingActivityEditClasses.dateEditText.text.toString()
            val nameStudent : String = bindingActivityEditClasses.studentEditText.text.toString()
            val nameTeacher : String = bindingActivityEditClasses.teacherEditText.text.toString()
            val descriptor : String = bindingActivityEditClasses.descriptionEditText.text.toString()

            val folder : StorageReference = FirebaseStorage.getInstance().reference.child("clases")
            val classesReference : StorageReference = folder.child("img$key")

            if (fileUri==null){
                val mClassesList = ClassesList (nameClasses,nameTeacher,nameStudent,date,descriptor,imageUrl)
                myRef.setValue(mClassesList)
            }else {
                classesReference.putFile(fileUri!!).addOnSuccessListener {
                    classesReference.downloadUrl.addOnSuccessListener { uri->
                        val mClasses = ClassesList(nameClasses,nameTeacher,nameStudent,date,descriptor, uri.toString())
                        myRef.setValue(mClasses)
                    }
                }
            }
            finish()
        }
        bindingActivityEditClasses.posterImageView.setOnClickListener {
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
                bindingActivityEditClasses.posterImageView.setImageURI(fileUri)
            }
        }
    }
}