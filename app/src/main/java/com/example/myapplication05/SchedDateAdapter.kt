//package com.example.myapplication05
//
//class SchedDateAdapter {}


package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.answer_row.view.*
import kotlinx.android.synthetic.main.att_attendance.view.*
import kotlinx.android.synthetic.main.sched_date_row.view.btnDateNew
import kotlinx.android.synthetic.main.sched_main.btnAmPm
import kotlinx.android.synthetic.main.sched_main.view.*
import kotlinx.android.synthetic.main.sched_row.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.text.SimpleDateFormat
import java.util.*

class SchedDateAdapter(val context: Context, val date: List<ScheduleDateModel>) :
    RecyclerView.Adapter<SchedDateAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.sched_date_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return date.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val schedule = date[position]
        holder.setData(schedule, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        var currentSched: ScheduleDateModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnDateNew.setOnClickListener {
                SchedMain.vartxtDate!!.text = currentSched!!.date
            }

        }//init

        fun setData(mysched: ScheduleDateModel?, pos: Int) {
            itemView.btnDateNew.text = mysched!!.date + "\n" +  mysched!!.day
            this.currentSched = mysched;
            this.currentPosition = pos

        }


    }//inner


}//class




