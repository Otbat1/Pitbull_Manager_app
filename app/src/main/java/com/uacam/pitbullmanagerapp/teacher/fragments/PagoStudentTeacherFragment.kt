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
import com.uacam.pitbullmanagerapp.databinding.FragmentPagoStudentTeacherBinding
import com.uacam.pitbullmanagerapp.teacher.*


class PagoStudentTeacherFragment : Fragment() {

    private lateinit var bindingPagoStudentTeacherFragment: FragmentPagoStudentTeacherBinding
    private lateinit var messagesListener: ValueEventListener
    private val database = Firebase.database
    private val Listpagos: MutableList<Pagos> = ArrayList()
    private val myRef = database.getReference("pagos")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        bindingPagoStudentTeacherFragment.addImageView.setOnClickListener {
            val intent = Intent(activity, AddPagosActivity::class.java)

            startActivity(intent)
        }
        Listpagos.clear()
        setupRecyclerView(bindingPagoStudentTeacherFragment.recyclerView)
        return bindingPagoStudentTeacherFragment.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        messagesListener = object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Listpagos.clear()
                dataSnapshot.children.forEach { resp ->
                    val mPagos =
                        Pagos(resp.child("namePagos")        .value as String?,
                            resp.child("date")               .value as String?,
                            resp.child("precio")             .value as String?,
                            resp.child("nameStudent")        .value as String?,
                            resp.child("nameTeacher")        .value as String?,
                            resp.key)
                    mPagos.let { Listpagos.add(it) }
                }
                recyclerView.adapter=PagosViewAdapter(Listpagos)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)
        deleteSwipe(recyclerView)
    }
    class PagosViewAdapter(private val values: List<Pagos>): RecyclerView.Adapter<PagosViewAdapter.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pagos_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val mPagos = values[position]
            holder.mNameClassesTextView?.text = mPagos.namePagos

            holder.mNameTeacherTextView?.text = mPagos.nameTeacher
            holder.mDateTextView?.text = mPagos.date

            /**
            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, DetailClassesActivity::class.java).apply {
                    putExtra("key", mPagos.key)
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditClassesActivity::class.java).apply {
                    putExtra("key", mPagos.key)
                }
                v.context.startActivity(intent)
                true
            }**/

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


            val mNameClassesTextView = view.findViewById(R.id.nameClassesTextView) as? TextView
            val mNameTeacherTextView= view.findViewById(R.id.nameStudentTextView) as? TextView
            val mDateTextView = view.findViewById(R.id.dateTextView) as? TextView

        }
    }
    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Listpagos[viewHolder.adapterPosition].key?.let { myRef.child(it).setValue(null) }
                Listpagos.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}

