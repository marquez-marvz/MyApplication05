//package com.example.myapplication05
//
//class AttendanceSchedAdapter {}


package com.example.myapplication05


import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.att_individual_main.view.*
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnEdit
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnSave
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlinx.android.synthetic.main.score_individual.view.btnNext as btnNext1


class AttendanceSchedAdapter(val context: Context, val schedule: List<ScheduleModel>) :
    RecyclerView.Adapter<AttendanceSchedAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.attendance_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return schedule.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val sched = schedule[position]
        holder.setData(sched, position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSched: ScheduleModel? = null
        var currentPosition: Int = 0

        init{

        }












        fun setData(myatt: ScheduleModel?, pos: Int) {
            this.currentSched = myatt;
            this.currentPosition = pos


        }
    }


}











