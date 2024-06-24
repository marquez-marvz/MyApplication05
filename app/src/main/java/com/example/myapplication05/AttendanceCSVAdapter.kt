//package com.example.myapplication05
//
//class  {}

package com.example.myapplication05


import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.att_individual_main.view.*
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnEdit
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnSave
import kotlinx.android.synthetic.main.attendance_csv.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.score_csv.view.*
import kotlinx.android.synthetic.main.score_csv.view.txtCsvName
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlinx.android.synthetic.main.score_individual.view.btnNext as btnNext1


class AttendanceCSVAdapter(val context: Context, val attendance: List<AttendanceCSVModel>) :
    RecyclerView.Adapter<AttendanceCSVAdapter.MyViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.attendance_csv, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return attendance.size;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myattendance = attendance[position]
        holder.setData(myattendance, position) //        Util.Msgbox(context, "Hello123" )
        //
        //        when (myatt!!.attendanceStatus) {
        //            "P" -> itemView.rowBtnPresent.setBackgroundColor(Color.parseColor("#64B5F6"))
        //            "L" -> itemView.rowBtnLate.setBackgroundColor(Color.parseColor("#69F0AE"))
        //            "A" -> itemView.rowBtnAbsent.setBackgroundColor(Color.parseColor("#FFB74D"))
        //            "E" -> itemView.rowBtnExcuse.setBackgroundColor(Color.parseColor("#BA68C8"))
        //        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @RequiresApi(Build.VERSION_CODES.O)
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentAttendance: AttendanceCSVModel? = null
        var currentPosition: Int = 0

        init {
            var att = ""
            var e = currentAttendance
            itemView.btnCsvPress.setOnClickListener {
                if (itemView.btnCsvPress.text == "-") {
                    att = "P"
                } else if (itemView.btnCsvPress.text == "P") {
                    att = "A"
                } else if (itemView.btnCsvPress.text == "A") {
                    att = "L"
                } else if (itemView.btnCsvPress.text == "L") {
                    att = "E"
                } else if (itemView.btnCsvPress.text == "E") {
                    att = "-"
                }

                itemView.btnCsvPress.text = att
                itemView.txtCsvAttendance.text = att
                itemView.btnCsvAttendanceSave.setVisibility(View.INVISIBLE);
                itemView.btnCsvAttendanceCheck.setVisibility(View.VISIBLE);
                val databaseHandler: TableAttendance = TableAttendance(context)
                Log.e("3545", currentAttendance!!.studentNo)
                Log.e("3545", currentAttendance!!.TaskPoints.toString())
                Log.e("3545", currentAttendance!!.Recitation.toString())
                //databaseHandler.UpdateStudentAttendance(att, currentAttendance!!.studentNo, currentAttendance!!.TaskPoints,currentAttendance!!.Recitation)
            }

            itemView.btnCsvAttendanceSave.setOnClickListener {
                itemView.btnCsvAttendanceSave.setVisibility(View.INVISIBLE);
                itemView.btnCsvAttendanceCheck.setVisibility(View.VISIBLE);
                val databaseHandler: TableAttendance = TableAttendance(context)
               // databaseHandler.UpdateStudentAttendance(currentAttendance!!.attendanceStatus, currentAttendance!!.studentNo, currentAttendance!!.TaskPoints, currentAttendance!!.Recitation)
            }


        }


        fun setData(myatt: AttendanceCSVModel?, pos: Int) {

            itemView.txtCsvName.text = myatt!!.completeName
            itemView.btnCsvTaskNew.text = myatt!!.TaskPoints.toString()
            itemView.txtCsvAttendance.text = myatt!!.attendanceStatus
            itemView.txtCsvTime.text = myatt!!.theTime.substring(0, 5);
            this.currentAttendance = myatt;
            this.currentPosition = pos
        }
    }
}











