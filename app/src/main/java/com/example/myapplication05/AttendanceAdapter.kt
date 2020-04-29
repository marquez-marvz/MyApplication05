package com.example.myapplication05


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*


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
//        Util.Msgbox(context, "Hello123" )
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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentAttendance: AttendanceModel? = null
        var currentPosition: Int = 0

        init {

            itemView.setOnLongClickListener {
                val completeName = currentAttendance!!.completeName
                val attendance = currentAttendance!!.attendanceStatus
                val studentNo = currentAttendance!!.studentNo
                val dlginput = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlginput)
                    .setTitle("Input Remark for $completeName")
                val inputDialog = mBuilder.show()
                inputDialog.setCanceledOnTouchOutside(false);


                dlginput.btnOK.setOnClickListener {
                    val remark = dlginput.txtdata.text.toString()
                    val databaseHandler: DatabaseHandler = DatabaseHandler(context)
                    databaseHandler.UpdateStudentAttendance(attendance, studentNo, remark)
                    var a:AttendanceMain = AttendanceMain()
                    AttendanceMain.UpdateListContent(context)
                    notifyDataSetChanged()
                    inputDialog.dismiss()
                }

                true


            }

            itemView.rowBtnPresent.setOnClickListener {
                DefaultColor();
                itemView.rowBtnPresent.setBackgroundColor(Color.parseColor("#64B5F6"))
                AttendanceData("P")
                AttendanceMain.AttenceCount(context)
            }

            itemView.rowBtnLate.setOnClickListener {
                DefaultColor();
                itemView.rowBtnLate.setBackgroundColor(Color.parseColor("#69F0AE"))
                AttendanceData("L")
                AttendanceMain.AttenceCount(context)
            }

            itemView.rowBtnAbsent.setOnClickListener {
                DefaultColor();
                itemView.rowBtnAbsent.setBackgroundColor(Color.parseColor("#FFB74D"))
                AttendanceData("A")
                AttendanceMain.AttenceCount(context)
            }


            itemView.rowBtnExcuse.setOnClickListener {
                DefaultColor();
                itemView.rowBtnExcuse.setBackgroundColor(Color.parseColor("#BA68C8"))
                AttendanceData("E")
                AttendanceMain.AttenceCount(context)
            }


        }

        fun DefaultColor() {
            itemView.rowBtnPresent.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowBtnExcuse.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowBtnLate.setBackgroundResource(android.R.drawable.btn_default);
        }

        fun AttendanceData(attStatus: String) {
            var sectionCode = currentAttendance!!.sectionCode
            var studentNo = currentAttendance!!.studentNo
            var ampm = currentAttendance!!.ampm
            var myDate = currentAttendance!!.myDate
            AttendanceMain.attendanceList[currentPosition].attendanceStatus = attStatus


            val databaseHandler: DatabaseHandler = DatabaseHandler(context)
            databaseHandler.UpdateStudentAttendance(attStatus, studentNo)

        }


        fun setData(myatt: AttendanceModel?, pos: Int) {
            itemView.rowtxtName.text = myatt!!.completeName
            itemView.rowtxtRemark.text = myatt!!.remark

            DefaultColor()
            when (myatt!!.attendanceStatus) {
                "P" -> itemView.rowBtnPresent.setBackgroundColor(Color.parseColor("#64B5F6"))
                "L" -> itemView.rowBtnLate.setBackgroundColor(Color.parseColor("#69F0AE"))
                "A" -> itemView.rowBtnAbsent.setBackgroundColor(Color.parseColor("#FFB74D"))
                "E" -> itemView.rowBtnExcuse.setBackgroundColor(Color.parseColor("#BA68C8"))
            }
            this.currentAttendance = myatt;
            this.currentPosition = pos
        }
    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}