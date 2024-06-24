
package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.att_attendance.view.*
import kotlinx.android.synthetic.main.sched_main.view.*
import kotlinx.android.synthetic.main.sched_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.text.SimpleDateFormat
import java.util.*

class SchedNewAdapter(val context: Context, val sched: List<ScheduleModel>) :
    RecyclerView.Adapter<SchedNewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.sched_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return sched.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val schedule = sched[position]
        holder.setData(schedule, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSched: ScheduleModel? = null
        var currentPosition: Int = 0

        init {

            itemView.setOnClickListener {
                           Util.ATT_CURRENT_DATE = currentSched!!.myDate
                Util.ATT_CURRENT_SECTION = Util.CURRENT_SECTION
                Util.ATT_CURRENT_AMPM=currentSched!!.ampm
//                AttendanceFragment.viewAttendance!!.txtAttendanceDate.setText(Util.ATT_CURRENT_DATE)
//                AttendanceFragment.viewAttendance!!.txtAttendanceSection.setText(Util.ATT_CURRENT_SECTION)
//                AttendanceFragment.viewAttendance!!.btnAttendanceAmPm.setText(Util.ATT_CURRENT_AMPM)
//                AttendanceFragment.ShowCountAttendance(context)
//                AttendanceFragment.currentSearch = "ALL"
//                AttendanceFragment.UpdateListContent(context)
//                AttendanceFragment.attendanceAdapter!!.notifyDataSetChanged()

            }

            itemView.btnTerm.setOnClickListener {
                var term = itemView.btnTerm.text
                var newTern = ""
                if (term == "-")
                    newTern = "FIRST"
                else if  (term == "FIRST")
                    newTern = "SECOND"
                else
                    newTern = "-"
                itemView.btnTerm.text = newTern
                val db: DatabaseHandler = DatabaseHandler(context)

                db.UpdateSchedTern(currentSched!!.myDate, currentSched!!.sectioncode, newTern)

            }
            //            val intent = Intent(this, NextActivity::class.java)
            //            (this@CurrentClassNam

//            itemView.rowBtnDelete.setOnClickListener {
//                val mymain = LayoutInflater.from(context).inflate(R.layout.sched_main, null)
//                // LayoutInflater layoutInflater = getLayoutInflater(null);
//                //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);
//
//                val myDate = currentSched!!.myDate
//                val ampm = currentSched!!.ampm
//                val section = Util.CURRENT_SECTION
//
//                // val thedate  = txtDate.text;
//
//
//                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
//                val mBuilder = AlertDialog.Builder(context)
//                    .setView(dlgconfirm)
//                    .setTitle("Do you like to delete $myDate  $ampm  in $section ")
//                val confirmDialog = mBuilder.show()
//                confirmDialog.setCanceledOnTouchOutside(false);
//
//                dlgconfirm.btnYes.setOnClickListener {
//
//
//                    val db: TableAttendance = TableAttendance(context)
//                    var status = db.ManageSched("DELETE", ampm, myDate, section, "", "")
////                    SchedFragment.UpdateListContent(context)
//                    notifyDataSetChanged()
//                    confirmDialog.dismiss()
//                }
//                //
//                dlgconfirm.btnNo.setOnClickListener {
//                    confirmDialog.dismiss()
//                }
//                //
//            }


            itemView.setOnLongClickListener {
                val myDate = currentSched!!.myDate
                val ampm = currentSched!!.ampm
                val schedID = currentSched!!.schedId

                val section = Util.CURRENT_SECTION

                val dlgInputBox = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgInputBox)
                    .setTitle("Input remark for  $myDate $ampm?")
                val inputBoxDialog = mBuilder.show()
                inputBoxDialog.setCanceledOnTouchOutside(false);
                dlgInputBox.txtdata.setText(currentSched!!.renark)


                dlgInputBox.btnOK.setOnClickListener {
                    val remark = dlgInputBox.txtdata.text.toString()
                    val db: TableAttendance = TableAttendance(context)
                    var status = db.ManageSched("EDIT", ampm, myDate, section, schedID,  remark)
//                    SchedFragment.UpdateListContent(context)
                    notifyDataSetChanged()
                    inputBoxDialog.dismiss()

                }

                dlgInputBox.btnCancel.setOnClickListener {
                    inputBoxDialog.dismiss()
                }



                true
            }

        }//init

        fun setData(mysched: ScheduleModel?, pos: Int) {
            itemView.rowDate.text = mysched!!.myDate
            itemView.rowSchedID.text = mysched!!.schedId
            itemView.rowAmPm.text =mysched!!.ampm
            itemView.rowremark.text = mysched!!.renark
            itemView.btnTerm.text = mysched!!.term
            itemView.txtDay.text = mysched!!.day
            this.currentSched = mysched;
            this.currentPosition = pos
        }
    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}






fun DatabaseHandler.UpdateSchedIDandDay(schedDate:String, section:String) {
    val db = this.writableDatabase

    var sss = schedDate.split(" ")
    Log.e("766", sss[0])
    Log.e("223", sss[1])
    var month =""
    if (sss[0] == "JAN"){
        month = "01"
    }
    if (sss[0] == "FEB"){
        month = "02"
    }
    if (sss[0] == "MAR"){
        month = "03"
    }
    if (sss[0] == "APR"){
        month = "04"
    }
    if (sss[0] == "MAY"){
        month = "05"
    }
    if (sss[0] == "JUN"){
        month = "06"
    }
    if (sss[0] == "JUL"){
        month = "07"
    }
    if (sss[0] == "AUG"){
        month = "08"
    }
    if (sss[0] == "SEP"){
        month = "09"
    }
    if (sss[0] == "OCT"){
        month = "10"
    }
    if (sss[0] == "NOV"){
        month = "11"
    }
    if (sss[0] == "DEC"){
        month = "12"
    }
    if (sss[0] == "JAN"){
        month = "01"
    }
    if (sss[0] == "JAN"){
        month = "01"
    }

    val cal = Calendar.getInstance()
    val formatter = SimpleDateFormat("MM/dd/yyyy")
    var p = formatter.parse(month + "/" + sss[1]   + "/2023")
    cal.setTime(p);
    val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

    Log.e("223", dayOfWeek.toString())
    val day =
        arrayOf<String>("", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    //  Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year)
    var schedDay = day[dayOfWeek]
    var schedID = month + sss[1]


    var sql = """
             update tbsched  set SchedID='$schedID', day ="$schedDay"
                          where SectionCode   ='$section'
                          and   SchedDate  ='$schedDate'
        """.trimIndent()


    //     sql = """
    //             update tbsched  set  day ="-"
    //        """.trimIndent()
    //    SchedTime	SchedDate		Remark	SchedTerm
    Log.e("123", sql)
    db.execSQL(sql)
}


fun DatabaseHandler.UpdateSchedTern(schedDate:String, section:String, term:String  ) {
    val db = this.writableDatabase
    var sql = """
             update tbsched  set SchedTerm='$term'
                          where SectionCode   ='$section'
                          and   SchedDate  ='$schedDate'
        """.trimIndent()
    //    SchedTime	SchedDate		Remark	SchedTerm

    db.execSQL(sql)
}

