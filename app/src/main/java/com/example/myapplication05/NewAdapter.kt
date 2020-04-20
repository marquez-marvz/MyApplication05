package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_student.view.*
import kotlinx.android.synthetic.main.recycle_row.view.*

class NewAdapter (val context:Context, val person:List<Person>):RecyclerView.Adapter<NewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.recycle_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {

       return person.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val person= person[position]
        holder.setData(person, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentPerson: Person? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
            }

            itemView.rowBtnDelete.setOnClickListener {
                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            }
        }//init

        fun setData(pp: Person?, pos: Int) {
            itemView.txtfirstName.text = pp!!.lastname
             itemView.txtLastName.text =pp!!.firstname
            this.currentPerson= pp;
            this.currentPosition = pos
        }
    }
}