package com.uacam.pitbullmanagerapp.teacher.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.uacam.pitbullmanagerapp.R
import com.uacam.pitbullmanagerapp.databinding.FragmentStudentUsersBinding
import com.uacam.pitbullmanagerapp.teacher.StudentList

class StudentUsersFragment : Fragment() {

    private lateinit var bindingFragmentStudentUsers: FragmentStudentUsersBinding
    private lateinit var messagesListener: ValueEventListener
    private val database = Firebase.database
    private val listStudents: MutableList<StudentList> = ArrayList()
    private val myRef = database.getReference("estudiantes")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        bindingFragmentStudentUsers = FragmentStudentUsersBinding.inflate(layoutInflater)

        /** bindingFragmentStudentUsers.addImageView.setOnClickListener {
        val intent =Intent(activity,AddStudentActivity::class.java)
        startActivity(intent)
        }**/

        listStudents.clear()
        setupRecyclerView(bindingFragmentStudentUsers.recyclerView)
        return bindingFragmentStudentUsers.root
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {

        messagesListener = object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listStudents.clear()
                dataSnapshot.children.forEach { resp ->
                    val mClassesTraining =
                        StudentList(resp.child("nameStudent").value as String?,
                            resp.child("nameTeacher").value as String?,
                            resp.child("date").value as String?,
                            resp.child("description").value as String?,
                            resp.child("url").value as String?,
                            resp.key)
                    mClassesTraining.let { listStudents.add(it) }
                }
                recyclerView.adapter = StudentViewAdapter(listStudents)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)
        deleteSwipe(recyclerView)
    }

    class StudentViewAdapter(private val values: List<StudentList>):RecyclerView.Adapter<StudentViewAdapter.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.student_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val mStudentlist = values[position]
            holder.mNameTeacherTextView.text = mStudentlist.nameTeacher
            holder.mNameStudentTextView.text = mStudentlist.nameStudent
            holder.mBeltTextView.text = mStudentlist.belt
            holder.mPosterImgeView.let {
                Glide.with(holder.itemView.context)
                    .load(mStudentlist.url)
                    .into(it)
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mNameTeacherTextView: TextView = view.findViewById(R.id.nameTeacherTextView) as TextView
            val mNameStudentTextView: TextView = view.findViewById(R.id.nameStudentTextView) as TextView
            val mBeltTextView: TextView = view.findViewById(R.id.beltTextView) as TextView
            val mPosterImgeView: ImageView = view.findViewById(R.id.posterImgeView) as ImageView
        }
    }
    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("estudiantes/img"+listStudents[viewHolder.adapterPosition].key)
                imageFirebaseStorage.delete()

                listStudents[viewHolder.adapterPosition].key?.let { myRef.child(it).setValue(null) }
                listStudents.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
