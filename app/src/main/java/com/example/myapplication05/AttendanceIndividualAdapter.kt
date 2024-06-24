package com.example.myapplication05

//class  {}





import android.R.attr.button
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_individual.view.btnDateIndividual
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.btnSubmissionStatus
import kotlinx.android.synthetic.main.score_individual_row.view.rowBtnEdit
import kotlinx.android.synthetic.main.score_individual_row.view.rowtxtScore
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.search_row.view.*
//import kotlinx.android.synthetic.main.task_row.view.*


class AttendanceIndividualAdapter(val context: Context, val individualScore: List<AttendanceModel>) :
    RecyclerView.Adapter<AttendanceIndividualAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView =
            LayoutInflater.from(context).inflate(R.layout.attendance_individual, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return individualScore.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myIndividualScore = individualScore[position]
        holder.setData(myIndividualScore, position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentIndividualAttendance: AttendanceModel? = null
        var currentPosition: Int = 0

        init {










        } //imit

        fun setData(myatt: AttendanceModel?, pos: Int) {
            itemView.btnDateIndividual.text= myatt!!.myDate
            if (myatt!!.attendanceStatus == "P") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
            } else if (myatt!!.attendanceStatus == "L") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))
            } else if (myatt!!.attendanceStatus == "A") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))
            } else if (myatt!!.attendanceStatus == "E") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D2B55B")))
            }
            else {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
            }

            this.currentIndividualAttendance = myatt
            this.currentPosition = pos
        }


    }

}