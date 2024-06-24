package com.example.myapplication05


import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_main.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.individual_row.view.*
import kotlinx.android.synthetic.main.summary_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random


class IndividualAdapter (val context: Context, val individual: List<IndividualModel>) :
    RecyclerView.Adapter<IndividualAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.individual_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return individual.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myIndividual = individual[position]
        holder.setData(myIndividual, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentIndividual: IndividualModel? = null
        var currentPosition: Int = 0

        init {

        }//init

        fun setData(myindividual: IndividualModel?, pos: Int) {
            itemView.rowTxtDate.text = myindividual!!.myDate
            itemView.rowTxtAmPm.text = myindividual!!.ampm
            itemView.rowTxtRemark.text =myindividual!!.remark
            itemView.rowTxtStatus.text =myindividual!!.attendanceStatus
            this.currentIndividual = myindividual;
            this.currentPosition = pos
        }
    }
}