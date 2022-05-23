package com.uacam.pitbullmanagerapp.teacher.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.uacam.pitbullmanagerapp.databinding.FragmentStudentClassesBinding
import com.uacam.pitbullmanagerapp.teacher.AddClassesActivity
import com.uacam.pitbullmanagerapp.teacher.ClassesList
import com.uacam.pitbullmanagerapp.teacher.DetailClassesActivity
import com.uacam.pitbullmanagerapp.teacher.EditClassesActivity


class StudentClassesFragment : Fragment() {
    private lateinit var bindingFragmentStudentClasses: FragmentStudentClassesBinding
    private lateinit var messagesListener: ValueEventListener
    private val database = Firebase.database
    private val listClasses: MutableList<ClassesList> = ArrayList()
    private val myRef = database.getReference("clases")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        bindingFragmentStudentClasses = FragmentStudentClassesBinding.inflate(layoutInflater)

        bindingFragmentStudentClasses.addImageView.setOnClickListener {
            val intent = Intent(activity, AddClassesActivity::class.java)
            startActivity(intent)
        }
        listClasses.clear()
        setupRecyclerView(bindingFragmentStudentClasses.recyclerView)
        return bindingFragmentStudentClasses.root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView) {

        messagesListener = object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listClasses.clear()
                dataSnapshot.children.forEach { resp ->
                    val mClasses =
                        ClassesList(resp.child("nameClasses").value as String?,
                            resp.child("date")               .value as String?,
                            resp.child("nameStudent")        .value as String?,
                            resp.child("nameTeacher")        .value as String?,
                            resp.child("description")        .value as String?,
                            resp.child("url")                .value as String?,
                            resp.key)
                    mClasses.let { listClasses.add(it) }
                }
                recyclerView.adapter = StudentViewAdapter(listClasses)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)
        deleteSwipe(recyclerView)
    }

    class StudentViewAdapter(private val values: List<ClassesList>): RecyclerView.Adapter<StudentViewAdapter.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.classes_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val mClassless = values[position]
            holder.mNameClassesTextView?.text = mClassless.nameClasses
            holder.mNameTeacherTextView?.text = mClassless.nameTeacher
            holder.mDateTextView?.text = mClassless.date
            holder.mPosterImageView?.let {
                Glide.with(holder.itemView.context)
                    .load(mClassless.url)
                    .into(it)
            }
            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, DetailClassesActivity::class.java).apply {
                    putExtra("key", mClassless.key)
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditClassesActivity::class.java).apply {
                    putExtra("key", mClassless.key)
                }
                v.context.startActivity(intent)
                true
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mNameClassesTextView = view.findViewById(R.id.nameClassesTextView) as? TextView
            val mNameTeacherTextView= view.findViewById(R.id.nameStudentTextView) as? TextView
            val mDateTextView = view.findViewById(R.id.dateTextView) as? TextView
            val mPosterImageView = view.findViewById(R.id.posterImgeView) as? ImageView
        }
    }
    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("clases/img"+listClasses[viewHolder.adapterPosition].key)
                imageFirebaseStorage.delete()

                listClasses[viewHolder.adapterPosition].key?.let { myRef.child(it).setValue(null) }
                listClasses.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}