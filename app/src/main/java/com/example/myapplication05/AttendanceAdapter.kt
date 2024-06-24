package com.example.myapplication05


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.att_individual_main.view.*
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnEdit
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnSave
import kotlinx.android.synthetic.main.attendance_main.cboAttandanceSesrch
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.individusl_student.btnGradingPeriod
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.task_row.view.rowBtnMenu
import kotlinx.android.synthetic.main.util_confirm.view.btnNo
import kotlinx.android.synthetic.main.util_confirm.view.btnYes
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import kotlinx.android.synthetic.main.score_individual.view.btnNext as btnNext1


class AttendanceAdapter(val context: Context, val attendance: List<AttendanceModel>) :
    RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>() {


    var attendanceIndividualList = arrayListOf<AttendanceModel>()
    var attendanceIndividualAdapter: AttendabceIndividualAdapter? = null;

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

    @RequiresApi(Build.VERSION_CODES.O) //    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                                               PopupMenu.OnMenuItemClickListener {

        var currentAttendance: AttendanceModel? = null
        var currentPosition: Int = 0

        init {


            itemView.setOnLongClickListener { //                val completeName = currentAttendance!!.completeName
                //                val attendance = currentAttendance!!.attendanceStatus
                //                val studentNo = currentAttendance!!.studentNo
                //                val dlginput = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
                //                val mBuilder = AlertDialog.Builder(context).setView(dlginput)
                //                    .setTitle("Input Remark for $completeName")
                //                val inputDialog = mBuilder.show()
                //                inputDialog.setCanceledOnTouchOutside(false);
                //
                //
                //                dlginput.btnOK.setOnClickListener {
                //                    val remark = dlginput.txtdata.text.toString()
                //                    val databaseHandler: TableAttendance = TableAttendance(context)
                //                    databaseHandler.UpdateStudentAttendance(attendance, studentNo, remark)
                //                    var a:AttendanceMain = AttendanceMain()
                //                    AttendanceMain.UpdateListContent(context)
                //                    notifyDataSetChanged()
                //                    inputDialog.dismiss()
                //                }


                val attendance1 = currentAttendance!!.attendanceStatus
                val studentNo = currentAttendance!!.studentNo

                val dlginput =
                    LayoutInflater.from(context).inflate(R.layout.att_individual_main, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlginput).setTitle("")
                val inputDialog = mBuilder.show()
                inputDialog.setCanceledOnTouchOutside(false);
                ShowAttendance(dlginput, currentAttendance!!, inputDialog)
                true


                dlginput.rowBtnEdit.setOnClickListener {
                    dlginput.rowBtnEdit.setVisibility(View.INVISIBLE);
                    dlginput.rowBtnSave.setVisibility(View.VISIBLE);
                    dlginput.txtParticipationScore.isEnabled = true

                }

                dlginput.btnNext.setOnClickListener {
                    currentPosition = currentPosition + 1
                    currentAttendance = attendance[currentPosition]
                    ShowAttendance(dlginput, currentAttendance!!, inputDialog)
                }

                dlginput.btnPrev.setOnClickListener {
                    currentPosition = currentPosition - 1
                    currentAttendance = attendance[currentPosition]
                    ShowAttendance(dlginput, currentAttendance!!, inputDialog)
                }



                dlginput.rowBtnSave.setOnClickListener {
                    var e = currentAttendance
                    dlginput.rowBtnEdit.setVisibility(View.VISIBLE);
                    dlginput.rowBtnSave.setVisibility(View.INVISIBLE);
                    dlginput.txtParticipationScore.isEnabled = false
                    val score = dlginput.txtParticipationScore.text.toString().toInt()
                    val db: TableActivity =
                        TableActivity(context) //val participationCode = db.GetParticipationCode(currentAttendance!!.sectionCode) //    db.UpdateStudentRecord(participationCode, e!!.sectionCode, e.studentNo, score, e.remark, "OK", 0)

                }

                dlginput.txtParticipationScore.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        Log.e("@@11", "hhhh")
                        if (dlginput.txtParticipationScore.text.toString() == "0") dlginput.txtParticipationScore.setText("")
                    }
                })
                true
            }

            //            itemView.rowBtnRemark.setOnClickListener {
            //                val arr = arrayOf("-", "SICK", "LATE", "FAMILY", "OTHER", "SCHOOL")
            //                var txt = itemView.rowBtnRemark.text
            //                for (i in 0..arr.size - 1) {
            //                    if (txt == arr[i] && i < arr.size - 1) {
            //                        itemView.rowBtnRemark.text = arr[i + 1] //   UpdateRemark
            //                    }
            //                }
            //            }
            //
            //            itemView.rowBtnRemark.setOnLongClickListener {
            //                itemView.rowBtnRemark.text = "-"
            //                true
            //            }


            //            itemView.btAttendandeStatus.setOnLongClickListener {
            //                true
            //            }
            itemView.btnAttendandeStatus.setOnClickListener {
                showPopMenu(itemView) //                var p = itemView.btnAttendandeStatus.text.toString()
                //
                //                if (p == "P") {
                //                    itemView.btnAttendandeStatus.text = "L"
                //                    itemView.txtAttendanceStud.text = "L"
                //                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))
                //                    AttendanceData("L")
                //                    AttendanceMain.ShowCountAttendance(context)
                //                } else if (p == "L") {
                //                    itemView.btnAttendandeStatus.text = "A"
                //                    itemView.txtAttendanceStud.text = "A"
                //                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D"))) // itemView.btnName.setBackgroundColor(Color.parseColor("#FFB74D"))
                //                    AttendanceData("A")
                //                    AttendanceMain.ShowCountAttendance(context)
                //                } else if (p == "A") {
                //                    itemView.txtAttendanceStud.text = "E"
                //                    itemView.btnAttendandeStatus.text =
                //                        "E" //  itemView.btnName.setBackgroundColor(Color.parseColor("#BA68C8"))
                //                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D2B55B")))
                //                    AttendanceData("E")
                //                    AttendanceMain.ShowCountAttendance(context)
                //                }


                //                    itemView.txtAttendanceStud.text = "L"
                //                    itemView.btnName.setBackgroundColor(Color.parseColor("#69F0AE"))
                //                    AttendanceData("L")
                //                    AttendanceMain.ShowCountAttendance(context)
                ////
                ////
                ////                } else if (p == "L") {
                ////                    itemView.txtAttendanceStud.text = "A"
                ////                    itemView.btnName.setBackgroundColor(Color.parseColor("#FFB74D"))
                ////                    AttendanceData("A")
                ////                    AttendanceMain.ShowCountAttendance(context)
                ////                } else if (p == "A") {
                ////                    itemView.txtAttendanceStud.text = "E"
                ////                    itemView.btnName.setBackgroundColor(Color.parseColor("#BA68C8"))
                ////                    AttendanceData("E")
                ////                    AttendanceMain.ShowCountAttendance(context)
                ////                } else if (p == "E" +
                ////                    "") {
                ////                    itemView.txtAttendanceStud.text = "P"
                ////                    itemView.btnName.setBackgroundColor(Color.parseColor("#64B5F6"))
                ////                    AttendanceData("P")
                ////                    AttendanceMain.ShowCountAttendance(context)
                ////                }
                //                true

            }


            itemView.btnName.setOnClickListener { // DefaultColor();\\
                var p = AttendanceMain.currentAttendance
                Log.e("12678", currentAttendance!!.myDate)
                Log.e("12678", "DDDD")
                if (p == "PRESENT") {
                    itemView.txtAttendanceStud.text = "P"
                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
                    AttendanceData("P")

                } else if (p == "LATE") {
                    itemView.txtAttendanceStud.text = "L"
                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE"))) //itemView.btnName.setBackgroundColor(Color.parseColor("#"))
                    AttendanceData("L")

                } else if (p == "ABSENT") {
                    itemView.txtAttendanceStud.text = "A"
                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D"))) // itemView.btnName.setBackgroundColor(Color.parseColor("#FFB74D"))
                    AttendanceData("A")

                } else if (p == "EXCUSE") {
                    itemView.txtAttendanceStud.text =
                        "E" //  itemView.btnName.setBackgroundColor(Color.parseColor("#BA68C8"))
                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D2B55B")))
                    AttendanceData("E")

                } else {
//                    itemView.txtAttendanceStud.text = "-"
//                    itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
//                    AttendanceData("-")

                }

                Log.e("12356", "")
                AttendanceMain.ShowGraph(context)
                AttendanceMain.UpdateListContentIndividualAttendance(context, "", currentAttendance!!.studentNo) //                AttendanceMain.adapterIndividual!!.notifyDataSetChanged()
                AttendanceMain.vartxtStudentName!!.text = currentAttendance!!.lastName
                AttendanceMain.vartxtStudentName!!.isVisible = true
                AttendanceMain.vartxtStudentName!!.isVisible = true
                AttendanceMain.varbtnViewStatus!!.text = "INDIVIDUAL"
                AttendanceMain.adapterIndividual!!.notifyDataSetChanged()
                AttendanceMain.varlistViewIndividual!!.isVisible = true
                AttendanceMain.varlistViewDate!!.isVisible = false
                AttendanceMain.varcboAttandanceSesrch!!.setSelection(0)
                ShowPicture(currentAttendance!!.completeName,currentAttendance!!.studentNo )
                AttendanceMain.varImgAttendancePicture!!.isVisible = true

            }


            //                AttendanceData("P")
            ////                itemView.btnPresentNew.setBackgroundColor(Color.parseColor("#64B5F6"))
            ////                AttendanceMain.AttenceCount(context)
            ////                notifyDataSetChanged()
            ////                AttendanceMain.ShowCountAttendance(context)

            // }

            //            itemView.btnAbsentNew.setOnClickListener {
            //                DefaultColor();
            //                itemView.btnAbsentNew.setBackgroundColor(Color.parseColor("#FFB74D"))
            //                AttendanceData("A")
            //                AttendanceMain.AttenceCount(context)
            //                notifyDataSetChanged()
            //                AttendanceMain.ShowCountAttendance(context)
            //            }

            //            itemView.btnLateNew.setOnClickListener {
            //                DefaultColor();
            //                itemView.btnLateNew.setBackgroundColor(Color.parseColor("#69F0AE"))
            //                AttendanceData("L")
            //                AttendanceMain.AttenceCount(context)
            //                notifyDataSetChanged()
            //                AttendanceMain.ShowCountAttendance(context)
            //            }

            //            itemView.btnExcuseNew.setOnClickListener {
            //                DefaultColor();
            //                itemView.btnExcuseNew.setBackgroundColor(Color.parseColor("#BA68C8"))
            //                AttendanceData("E")
            //                AttendanceMain.AttenceCount(context)
            //                notifyDataSetChanged()
            //                AttendanceMain.ShowCountAttendance(context)
            //

            //            itemView.rowBtnAttendanceStatus.setOnLongClickListener() {
            //                itemView.rowBtnAttendanceStatus.text = "-"
            //                DefaultColor()
            //                AttendanceMain.AttenceCount(context)
            //                AttendanceData("-")
            //                true
            //            }

        } //1400

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun showPopMenu(v: View) {
            val popup = PopupMenu(context, itemView.btnAttendandeStatus, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.attendance_menu)
            popup.show()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            val selected = item.toString() //

            if (selected == "Present") {
                itemView.txtAttendanceStud.text = "P"
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
                AttendanceData("P") //                AttendanceMain.ShowCountAttendance(context)
            }

            if (selected == "Late") {
                itemView.txtAttendanceStud.text = "L"
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))
                AttendanceData("L") //AttendanceMain.ShowCountAttendance(context)
            }

            if (selected == "Absent") {
                itemView.txtAttendanceStud.text = "A"
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D"))) // itemView.btnName.setBackgroundColor(Color.parseColor("#FFB74D"))
                AttendanceData("A") // AttendanceMain.ShowCountAttendance(context)
            }


            if (selected == "Excuse") {
                itemView.txtAttendanceStud.text = "E"
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BA68C8")))
                AttendanceData("E") // AttendanceMain.ShowCountAttendance(context)
            }

            AttendanceMain.ShowGraph(context)
            AttendanceMain.UpdateListContentIndividualAttendance(context, "", currentAttendance!!.studentNo) //                AttendanceMain.adapterIndividual!!.notifyDataSetChanged()
            AttendanceMain.vartxtStudentName!!.text = currentAttendance!!.lastName
            AttendanceMain.varImgAttendancePicture!!.isVisible = true
            AttendanceMain.vartxtStudentName!!.isVisible = true
            AttendanceMain.varcboAttandanceSesrch!!.isVisible = true
            AttendanceMain.varbtnViewStatus!!.text = "INDIVIDUAL"
            AttendanceMain.adapterIndividual!!.notifyDataSetChanged()
            AttendanceMain.varlistViewIndividual!!.isVisible = true
            AttendanceMain.varlistViewDate!!.isVisible = false
            AttendanceMain.varcboAttandanceSesrch!!.setSelection(0)
            ShowPicture(currentAttendance!!.completeName,currentAttendance!!.studentNo )
            AttendanceMain.varImgAttendancePicture!!.isVisible = true
            return true
        }


        fun DefaultColor() { //            itemView.btnPresentNew.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
            //            itemView.btnAbsentNew.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
            //            itemView.btnLateNew.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
            //            itemView.btnExcuseNew.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        }


        //0618
        fun ShowPicture(studName: String, studNumber: String) {

            try {

                val db2: DatabaseHandler = DatabaseHandler(context)
                var origSection = db2.GetStudentOriginalSection(studNumber) //
                Log.e("5685", origSection) //      var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
                val sdCard =
                    Environment.getExternalStorageDirectory() //        val section = TestCapture.viewcapture!!.cboSectionPic.getSelectedItem().toString();
                val path = sdCard.absolutePath + "/Quiz/StudentPicture/" + origSection

                val f: File = File(path, studName + ".jpg")
                Log.e("5685", studName + ".jpg") //
                if (f.exists()) {
                    val f: File = File(path, studName + ".jpg")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    AttendanceMain.varImgAttendancePicture!!.setImageBitmap(b)
                } else { // TestCapture.viewcapture!!.imgStudent.setImageBitmap(null)
                    val f: File = File(sdCard.absolutePath + "/Quiz/StudentPicture/", "nopic.png")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    AttendanceMain.varImgAttendancePicture!!.setImageBitmap(b)
                }


                // val img = findViewById<View>(R.id.imgStudent) as ImageView

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }


        fun ShowAttendance(dlginput: View, f: AttendanceModel, individualDialog: AlertDialog) {
            val databaseHandler: TableAttendance = TableAttendance(context)
            attendanceIndividualList =
                databaseHandler.GetAttendanceList("INDIVIDUAL", currentAttendance!!.studentNo)
            val layoutmanager = LinearLayoutManager(context)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlginput.listAttendanceIndividual.layoutManager = layoutmanager
            attendanceIndividualAdapter =
                AttendabceIndividualAdapter(context, attendanceIndividualList)
            dlginput.listAttendanceIndividual.adapter = attendanceIndividualAdapter
            val db: DatabaseHandler =
                DatabaseHandler(context) //    val participationCode = db.GetParticipationCode(f!!.sectionCode) //  val score = db.GetParticipationScore(f!!.sectionCode, participationCode, f!!.studentNo) //dlginput.txtParticipationScore.setText(score.toString())
            val completeName = f.completeName
            dlginput.txtStudentName.setText(completeName)

        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun AttendanceData(attStatus: String) {
            var sectionCode = currentAttendance!!.sectionCode
            var studentNo = currentAttendance!!.studentNo
            var ampm = currentAttendance!!.ampm
            var myDate = currentAttendance!!.myDate
            Log.e("3256", myDate)
            AttendanceMain.attendanceList[currentPosition].attendanceStatus = attStatus


            val databaseHandler: TableAttendance = TableAttendance(context)
            databaseHandler.UpdateStudentAttendance(attStatus, studentNo, myDate, ampm)

        }

        fun UpdateRecitationScore(score: Int) {
            var sectionCode = currentAttendance!!.sectionCode
            var studentNo = currentAttendance!!.studentNo
            var ampm = currentAttendance!!.ampm
            var myDate = currentAttendance!!.myDate
            val databaseHandler: TableAttendance = TableAttendance(context)
            databaseHandler.UpdateRecitation(score, studentNo)
            AttendanceMain.attendanceList[currentPosition].recitationPoint = score
        }


        fun setData(myatt: AttendanceModel?, pos: Int) {

            itemView.txtAttendanceStud.text = myatt!!.attendanceStatus
            itemView.btnName.text =
                myatt!!.lastName + "\n" + myatt!!.firstName //            itemView.rowtxtLastName.text = myatt!!.lastName
            //            itemView.rowtxtFirstName.text =
            DefaultColor()
            val db: DatabaseHandler =
                DatabaseHandler(context) //  itemView.rowBtnRemark.text = myatt!!.remark.toString()
            val db2: TableAttendance = TableAttendance(context)
            var absentCount =
                db2.GetIndividualCouunt(myatt!!.studentNo, "ALL", "A") //            if (absentCount <= 1) {


            if (myatt!!.attendanceStatus == "P") {
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
            } else if (myatt!!.attendanceStatus == "A") {
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))
            } else if (myatt!!.attendanceStatus == "L") {
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE"))) //itemView.btnName.setBackgroundColor(Color.parseColor("#"))
            } else if (myatt!!.attendanceStatus == "E") {
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BA68C8"))) //
                // itemView.btnName.setBackgroundColor(Color.parseColor("#"))
                // val db: DatabaseHandler = DatabaseHandler(context)

            } else if (myatt!!.attendanceStatus == "-") {
                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3"))) //itemView.btnName.setBackgroundResource(android.R.drawable.btn_default); //val db: DatabaseHandler = DatabaseHandler(context)
            }

            this.currentAttendance = myatt;
            this.currentPosition = pos
        }
    }


}

//
//fun DatabaseHandler.CountTermAttendance(schedTerm: String, studentNo: String, attendance: String): String {
//    var sql = """
//                SELECT *  FROM tbsched_query
//                where SchedTerm = '$schedTerm'
//                and StudentNumber = '$studentNo'
//                and AttendanceStatus = '$attendance'
//                  """
//    val db = this.readableDatabase
//    val cursor = db.rawQuery(sql, null)
//    val count = cursor.count
//    cursor.close()
//    Util.Msgbox(context, count.toString())
//    return count.toString()
//
//}
//
//
//fun DatabaseHandler.GetParticipationCode(section: String): String {
//    val term = context?.let { Util.GetCurrentGradingPeriod(it) }
//    var sql = """
//                SELECT *  FROM tbactivity
//                where GradingPeriod = '$term'
//                and SectionCode = '$section'
//                and category = 'Participation'
//                  """
//    val db = this.readableDatabase
//    val cursor = db.rawQuery(sql, null)
//
//    if (cursor.moveToFirst()) {
//
//        Log.e("PPP", cursor.getString(cursor.getColumnIndex("ActivityCode")))
//        return cursor.getString(cursor.getColumnIndex("ActivityCode"))
//
//    } else {
//        Util.Msgbox(context, "No Participation Activity")
//        return ""
//    }
//
//}
//
//
//fun DatabaseHandler.GetParticipationScore(section: String, activityCode: String, studentNo: String): Int {
//
//    var sql = """
//                SELECT *  FROM tbscore
//                where ActivityCode = '$activityCode'
//                and StudentNo = '$studentNo'
//                and SectionCode = '$section'
//                  """
//    val db = this.readableDatabase
//    val cursor = db.rawQuery(sql, null)
//
//    if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex("Score")).toInt()
//    else return 0
//}
//
//
////
////fun DatabaseHandler.UpdateParticipationScoreCode(section:String):String  {
////
////
////}










