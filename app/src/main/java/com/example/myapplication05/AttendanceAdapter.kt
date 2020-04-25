package com.example.myapplication05


import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlin.random.Random


class AttendanceAdapter(val context: Context, val attendance: List<AttendanceModel>) :
    RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.attendance_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return attendance.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myattendance = attendance[position]
        holder.setData(myattendance, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSched: AttendanceModel? = null
        var currentPosition: Int = 0

        init {
            itemView.rowBtnPresent.setOnClickListener {
                DefaultColor();
                itemView.rowBtnPresent.setBackgroundColor(Color.parseColor("#64B5F6"))
            }

            itemView.rowBtnLate.setOnClickListener {
                DefaultColor();
                itemView.rowBtnLate.setBackgroundColor(Color.parseColor("#69F0AE"))
            }

            itemView.rowBtnAbsent.setOnClickListener {
                DefaultColor();
                itemView.rowBtnAbsent.setBackgroundColor(Color.parseColor("#FFB74D"))
            }


            itemView.rowBtnExcuse.setOnClickListener {

                DefaultColor();
                itemView.rowBtnExcuse.setBackgroundColor(Color.parseColor("#BA68C8"))
            }






        }

        fun DefaultColor(){
            itemView.rowBtnPresent.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowBtnExcuse.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowBtnPresent.setBackgroundResource(android.R.drawable.btn_default);
        }


        fun setData(myatt: AttendanceModel?, pos: Int) {
            itemView.rowtxtName.text = myatt!!.completeName
            //itemView.rowremark.text = myatt!!.remark
//            itemView.rowremark.text = mysched!!.renark
            this.currentSched = myatt;
            this.currentPosition = pos
        }
    }

//    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
}