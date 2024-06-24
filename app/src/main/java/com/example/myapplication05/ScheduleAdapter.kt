package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.answer_row.view.*
import kotlinx.android.synthetic.main.att_attendance.view.*
import kotlinx.android.synthetic.main.sched_main.view.*
import kotlinx.android.synthetic.main.sched_row.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ScheduleAdapter(val context: Context, val sched: List<ScheduleModel>) :
    RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {


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

    @RequiresApi(Build.VERSION_CODES.O)
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                                               PopupMenu.OnMenuItemClickListener {

        var currentSched: ScheduleModel? = null
        var currentPosition: Int = 0

        init {


            itemView.btnSchedAdapterMenu.setOnClickListener {
                showPopMenu(itemView)
            }

            itemView.rowDate.setOnClickListener {
                Log.e("3612-00", "@@@@")
                if (Util.ATT_FLAG == "SCHEDULE") {
                    val intent = Intent(context, AttendanceMain::class.java)
                    context.startActivity(intent)
                    Util.ATT_CURRENT_DATE = currentSched!!.myDate
                    Util.ATT_CURRENT_SECTION = Util.CURRENT_SECTION
                    Util.ATT_CURRENT_AMPM = currentSched!!.ampm
                    Util.ATT_CURRENT_AMPM = currentSched!!.ampm
                    Util.ATT_CURRENT_DAY = currentSched!!.day
                    Util.ATT_CURRENT_REMARK = currentSched!!.renark
                    Log.e("3612-0", Util.ATT_CURRENT_DATE)
                }

                Log.e("46780", Util.ATT_FLAG)

                if (Util.ATT_FLAG == "ATTENDANCE") {
                    Log.e("4678", "@@@")
                    val today = LocalDate.now()
                    val monthNum = today.monthValue
                    val monthName =
                        arrayOf<String>("","JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC") //  Log.
                    Util.ATT_CURRENT_DATE = currentSched!!.myDate
                    Util.ATT_CURRENT_AMPM = currentSched!!.ampm
                    Util.ATT_CURRENT_SECTION = currentSched!!.sectioncode
                    Util.ATT_CURRENT_MONTH = monthName[monthNum]
                    Log.e("4678", "123")
                    var ATT_CURRENT_REMARK: String = ""
                    Log.e("4678", "124")
                    AttendanceMain.UpdateListContent(context,  currentSched!!.myDate, currentSched!!.ampm , "ALL", "LASTNAME")
                    AttendanceMain.attendanceAdapter!!.notifyDataSetChanged()
//                    AttendanceMain.ShowCountAttendance(context)
                    AttendanceMain.vartxtAttendanceDate!!.text = currentSched!!.myDate
                    AttendanceMain.vartxtAttendanceRemark!!.setText(currentSched!!.renark.toString())
                    AttendanceMain.vartxtAttendanceDay!!.text = currentSched!!.day
                    AttendanceMain.varbtnAttendanceTime!!.text = currentSched!!.ampm
                    AttendanceMain.ShowGraph(context)
                    Log.e("4611", "125")

                }
            }




            itemView.btnTerm.setOnClickListener {
                var term = itemView.btnTerm.text
                var newTern = ""
                if (term == "-") newTern = "FIRST"
                else if (term == "FIRST") newTern = "SECOND"
                else newTern = "-"
                itemView.btnTerm.text = newTern
                val db: DatabaseHandler = DatabaseHandler(context)

                db.UpdateSchedTern(currentSched!!.myDate, currentSched!!.sectioncode, newTern)

            } //            val intent = Intent(this, NextActivity::class.java)
            //            (this@CurrentClassNam

//            itemView.rowBtnDelete.setOnClickListener {
//                val mymain = LayoutInflater.from(context)
//                    .inflate(R.layout.sched_main, null) // LayoutInflater layoutInflater = getLayoutInflater(null);
//                //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);
//
//                val myDate = currentSched!!.myDate
//                val ampm = currentSched!!.ampm
//                val section = Util.CURRENT_SECTION
//
//                // val thedate  = txtDate.text;
//
//                val txtDate = ""
//                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
//                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
//                    .setTitle("Do you like to delete $myDate  $ampm  in $section ")
//                val confirmDialog = mBuilder.show()
//                confirmDialog.setCanceledOnTouchOutside(false);
//
//                dlgconfirm.btnYes.setOnClickListener {
//
//
//                    val db: TableAttendance = TableAttendance(context)
//                    var status = db.ManageSched("DELETE", ampm, myDate, section, "", "")
//                    SchedMain.UpdateListContent(context)
//                    notifyDataSetChanged()
//                    confirmDialog.dismiss()
//                } //
//                dlgconfirm.btnNo.setOnClickListener {
//                    confirmDialog.dismiss()
//                } //
//            }


            itemView.setOnLongClickListener {
                val myDate = currentSched!!.myDate
                val ampm = currentSched!!.ampm
                val schedID = currentSched!!.schedId

                val section = Util.CURRENT_SECTION

                val dlgInputBox = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgInputBox)
                    .setTitle("Input remark for  $myDate $ampm?")
                val inputBoxDialog = mBuilder.show()
                inputBoxDialog.setCanceledOnTouchOutside(false);
                dlgInputBox.txtdata.setText(currentSched!!.renark)


                dlgInputBox.btnOK.setOnClickListener {
                    val remark = dlgInputBox.txtdata.text.toString()
                    val db: TableAttendance = TableAttendance(context)
                    var status = db.ManageSched("EDIT", ampm, myDate, section, schedID, "", remark)
                    SchedMain.UpdateListContent(context)
                    notifyDataSetChanged()
                    inputBoxDialog.dismiss()

                }

                dlgInputBox.btnCancel.setOnClickListener {
                    inputBoxDialog.dismiss()
                }



                true
            }

        } //init

        fun setData(mysched: ScheduleModel?, pos: Int) {
            itemView.rowDate.text = mysched!!.myDate
            itemView.rowSchedID.text = mysched!!.schedId
            itemView.rowAmPm.text = mysched!!.ampm
            itemView.rowremark.text = mysched!!.renark
            itemView.btnTerm.text = mysched!!.term
            itemView.txtDay.text = mysched!!.day
            this.currentSched = mysched;
            this.currentPosition = pos
            val db3: TableAttendance = TableAttendance(context)
            Util.ATT_CURRENT_DATE = mysched!!.myDate
            Util.ATT_CURRENT_AMPM = mysched!!.ampm
            Util.ATT_CURRENT_SECTION = mysched!!.sectioncode
//            itemView.txtPresentCount.text = "P=" + db3.CountAttendance("P")
//            itemView.txtExcuseCount.text = "E=" + db3.CountAttendance("E")
//            itemView.txtAbsentCount.text = "A=" + db3.CountAttendance("A")
//            itemView.txtLateCount.text = "L=" + db3.CountAttendance("L")


            if (Util.ATT_FLAG == "ATTENDANCE") {
                itemView.rowSchedID.isVisible = false
                itemView.rowAmPm.isVisible = false
                itemView.rowremark.text = mysched!!.renark
                itemView.btnTerm.isVisible = false
                itemView.txtDay.isVisible = false

                if (mysched!!.renark == "-") {
                    itemView.rowremark.isVisible = false
                }
            } else {

            }
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun showPopMenu(v: View) {
            val popup = PopupMenu(context, v, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_sched_adapter)
            popup.show()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            val selected = item.toString() //

            if (selected == "Delete Sched") {
                DeleteSched()
            }

            if (selected == "Create Next Day") {
                var sched = currentSched!!.myDate
                var dateBroken = sched.split(" ")
                var month = dateBroken[0]
                var day = Util.ZeroPad(dateBroken[1].toInt() + 1, 2)
                var newDay = month + " " + day
                Log.e("1100", newDay)
                val db1: TableActivity = TableActivity(context);
                val id = db1.GetNewSchedCode(currentSched!!.sectioncode)

                SchedMain.AddNewSched(context, newDay, "AM", currentSched!!.sectioncode, id)
            }

            if (selected == "Create Prev Day") {
                var sched = currentSched!!.myDate
                var dateBroken = sched.split(" ")
                var month = dateBroken[0]
                var day = Util.ZeroPad(dateBroken[1].toInt() - 1, 2)
                var newDay = month + " " + day
                Log.e("1100", newDay)
                val db1: TableActivity = TableActivity(context);
                val id = db1.GetNewSchedCode(currentSched!!.sectioncode)

                SchedMain.AddNewSched(context, newDay, "AM", currentSched!!.sectioncode, id)
            }
            return true
        }

        fun DeleteSched() {

            val mymain = LayoutInflater.from(context)
                .inflate(R.layout.sched_main, null) // LayoutInflater layoutInflater = getLayoutInflater(null);
            //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);

            val myDate = currentSched!!.myDate
            val ampm = currentSched!!.ampm
            val section = Util.CURRENT_SECTION

            // val thedate  = txtDate.text;

            val txtDate =  ""
            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you like to delete $myDate  $ampm  in $section ")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {


                val db: TableAttendance = TableAttendance(context)
                var status = db.ManageSched("DELETE", ampm, myDate, section, "", "")
                SchedMain.UpdateListContent(context, SchedMain.globalCategory, SchedMain.globalMonth)
                notifyDataSetChanged()
                confirmDialog.dismiss()
            } //
            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }

        }

    } //inner


} //class




