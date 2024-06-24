////package com.example.myapplication05.Attendance
////
////class AttendandeNewAdapter {}
//
//package com.example.myapplication05
//
//
//import android.content.Context
//import android.graphics.Color
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.att_individual_main.view.*
//import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnEdit
//import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnSave
//import kotlinx.android.synthetic.main.attendance_row.view.*
//import kotlinx.android.synthetic.main.score_individual.view.*
//import kotlinx.android.synthetic.main.score_individual_row.view.*
//import kotlinx.android.synthetic.main.util_inputbox.view.*
//import kotlinx.android.synthetic.main.score_individual.view.btnNext as btnNext1
//
//
//class AttendandeNewAdapter(val context: Context, val attendance: List<AttendanceModel>) :
//    RecyclerView.Adapter<AttendandeNewAdapter.MyViewHolder>() {
//
//
//    var attendanceIndividualList = arrayListOf<AttendanceModel>()
//    var attendanceIndividualAdapter: AttendabceIndividualAdapter? = null;
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//
//        val myView = LayoutInflater.from(context).inflate(R.layout.attendance_row, parent, false)
//
//        return MyViewHolder((myView))
//
//    }
//
//    override fun getItemCount(): Int {
//        return attendance.size;
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        // holder.removeAllViews();
//        val myattendance = attendance[position]
//        holder.setData(myattendance, position) //        Util.Msgbox(context, "Hello123" )
//        //
//        //        when (myatt!!.attendanceStatus) {
//        //            "P" -> itemView.rowBtnPresent.setBackgroundColor(Color.parseColor("#64B5F6"))
//        //            "L" -> itemView.rowBtnLate.setBackgroundColor(Color.parseColor("#69F0AE"))
//        //            "A" -> itemView.rowBtnAbsent.setBackgroundColor(Color.parseColor("#FFB74D"))
//        //            "E" -> itemView.rowBtnExcuse.setBackgroundColor(Color.parseColor("#BA68C8"))
//        //        }
//
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        var currentAttendance: AttendanceModel? = null
//        var currentPosition: Int = 0
//
//        init {
//
//
//            itemView.setOnLongClickListener { //                val completeName = currentAttendance!!.completeName
//                //                val attendance = currentAttendance!!.attendanceStatus
//                //                val studentNo = currentAttendance!!.studentNo
//                //                val dlginput = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
//                //                val mBuilder = AlertDialog.Builder(context).setView(dlginput)
//                //                    .setTitle("Input Remark for $completeName")
//                //                val inputDialog = mBuilder.show()
//                //                inputDialog.setCanceledOnTouchOutside(false);
//                //
//                //
//                //                dlginput.btnOK.setOnClickListener {
//                //                    val remark = dlginput.txtdata.text.toString()
//                //                    val databaseHandler: TableAttendance = TableAttendance(context)
//                //                    databaseHandler.UpdateStudentAttendance(attendance, studentNo, remark)
//                //                    var a:AttendanceMain = AttendanceMain()
//                //                    AttendanceMain.UpdateListContent(context)
//                //                    notifyDataSetChanged()
//                //                    inputDialog.dismiss()
//                //                }
//
//
//                val attendance1 = currentAttendance!!.attendanceStatus
//                val studentNo = currentAttendance!!.studentNo
//
//                val dlginput =
//                    LayoutInflater.from(context).inflate(R.layout.att_individual_main, null)
//                val mBuilder = AlertDialog.Builder(context).setView(dlginput).setTitle("")
//                val inputDialog = mBuilder.show()
//                inputDialog.setCanceledOnTouchOutside(false);
//                ShowAttendance(dlginput, currentAttendance!!, inputDialog)
//                true
//
//
//                dlginput.rowBtnEdit.setOnClickListener {
//                    dlginput.rowBtnEdit.setVisibility(View.INVISIBLE);
//                    dlginput.rowBtnSave.setVisibility(View.VISIBLE);
//                    dlginput.txtParticipationScore.isEnabled = true
//
//                }
//
//                dlginput.btnNext.setOnClickListener {
//                    currentPosition = currentPosition + 1
//                    currentAttendance = attendance[currentPosition]
//                    ShowAttendance(dlginput, currentAttendance!!, inputDialog)
//                }
//
//                dlginput.btnPrev.setOnClickListener {
//                    currentPosition = currentPosition - 1
//                    currentAttendance = attendance[currentPosition]
//                    ShowAttendance(dlginput, currentAttendance!!, inputDialog)
//                }
//
//
//
//                dlginput.rowBtnSave.setOnClickListener {
//                    var e = currentAttendance
//                    dlginput.rowBtnEdit.setVisibility(View.VISIBLE);
//                    dlginput.rowBtnSave.setVisibility(View.INVISIBLE);
//                    dlginput.txtParticipationScore.isEnabled = false
//                    val score = dlginput.txtParticipationScore.text.toString().toInt()
//                    val db: TableActivity = TableActivity(context)
//                    val participationCode = db.GetParticipationCode(currentAttendance!!.sectionCode)
//                    db.UpdateStudentRecord(participationCode, e!!.sectionCode, e.studentNo, score, e.remark, "OK", 0)
//
//                }
//
//                dlginput.txtParticipationScore.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
//                    if (hasFocus) {
//                        Log.e("@@11", "hhhh")
//                        if (dlginput.txtParticipationScore.text.toString() == "0") dlginput.txtParticipationScore.setText("")
//                    }
//                })
//                true
//            }
//
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
//
//
//            itemView.rowBtnAttendanceStatus.setOnClickListener { // DefaultColor();
//                var p = itemView.rowBtnAttendanceStatus.text
//                if (p == "-") {
//                    itemView.rowBtnAttendanceStatus.text = "P"
//                    itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#64B5F6"))
//                    AttendanceFragment.AttenceCount(context)
//                    AttendanceData("P")
//                } else if (p == "P") {
//                    itemView.rowBtnAttendanceStatus.text = "L"
//                    itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
//                    AttendanceFragment.AttenceCount(context)
//                    AttendanceData("L")
//                } else if (p == "L") {
//                    itemView.rowBtnAttendanceStatus.text = "A"
//                    itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#FFB74D"))
//                    AttendanceFragment.AttenceCount(context)
//                    AttendanceData("A")
//                } else if (p == "A") {
//                    itemView.rowBtnAttendanceStatus.text = "E"
//                    itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#BA68C8"))
//                    AttendanceFragment.AttenceCount(context)
//                    AttendanceData("E")
//                }
//                notifyDataSetChanged()
//                AttendanceFragment.ShowCountAttendance(context)
//            }
//
//            itemView.rowBtnAttendanceStatus.setOnLongClickListener() {
//                itemView.rowBtnAttendanceStatus.text = "-"
//                DefaultColor()
//                AttendanceFragment.AttenceCount(context)
//                AttendanceData("-")
//                true
//            }
//
//        }
//
//
//        fun DefaultColor() {
//            itemView.rowBtnAttendanceStatus.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
//            //            itemView.rowBtnExcuse.setBackgroundResource(android.R.drawable.btn_default);
//            //            itemView.rowBtnLate.setBackgroundResource(android.R.drawable.btn_default);
//        }
//
//        fun ShowAttendance(dlginput: View, f: AttendanceModel, individualDialog: AlertDialog) {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            attendanceIndividualList =
//                databaseHandler.GetAttendanceList("INDIVIDUAL", currentAttendance!!.studentNo)
//            val layoutmanager = LinearLayoutManager(context)
//            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
//            dlginput.listAttendanceIndividual.layoutManager = layoutmanager
//            attendanceIndividualAdapter =
//                AttendabceIndividualAdapter(context, attendanceIndividualList)
//            dlginput.listAttendanceIndividual.adapter = attendanceIndividualAdapter
//            val db: DatabaseHandler = DatabaseHandler(context)
//            val participationCode = db.GetParticipationCode(f!!.sectionCode)
//            val score = db.GetParticipationScore(f!!.sectionCode, participationCode, f!!.studentNo)
//            dlginput.txtParticipationScore.setText(score.toString())
//            val completeName = f.completeName
//            dlginput.txtStudentName.setText(completeName)
//
//        }
//
//        fun AttendanceData(attStatus: String) {
//            var sectionCode = currentAttendance!!.sectionCode
//            var studentNo = currentAttendance!!.studentNo
//            var ampm = currentAttendance!!.ampm
//            var myDate = currentAttendance!!.myDate
//            AttendanceFragment.attendanceList[currentPosition].attendanceStatus = attStatus
//
//
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            databaseHandler.UpdateStudentAttendance(attStatus, studentNo, currentAttendance!!.TaskPoints, currentAttendance!!.recitationPoint)
//
//        }
//
//        fun UpdateRecitationScore(score: Int) {
//            var sectionCode = currentAttendance!!.sectionCode
//            var studentNo = currentAttendance!!.studentNo
//            var ampm = currentAttendance!!.ampm
//            var myDate = currentAttendance!!.myDate
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            databaseHandler.UpdateRecitation(score, studentNo)
//            AttendanceFragment.attendanceList[currentPosition].recitationPoint = score
//        }
//
//
//        fun setData(myatt: AttendanceModel?, pos: Int) {
//            itemView.rowtxtLastName.text = myatt!!.lastName
//            itemView.rowtxtFirstName.text = myatt!!.firstName
//
//            val db: DatabaseHandler = DatabaseHandler(context)
//            itemView.rowBtnRemark.text = myatt!!.remark.toString()
//            val db2: TableAttendance = TableAttendance(context)
//            var absentCount = db2.GetIndividualCouunt(myatt!!.studentNo, "ALL", "A")
//            if (absentCount <= 1) {
//                itemView.rowBtnAbsentCount.setBackgroundResource(android.R.drawable.btn_default);
//            } else if (absentCount <= 3) {
//                itemView.rowBtnAbsentCount.setBackgroundColor(Color.parseColor("#64B5F6"))
//            } else if (absentCount <= 5) {
//                itemView.rowBtnAbsentCount.setBackgroundColor(Color.parseColor("#69F0AE"))
//            } else if (absentCount <= 7) {
//                itemView.rowBtnAbsentCount.setBackgroundColor(Color.parseColor("#ED2939"))
//            }
//
//
//
//
//            itemView.rowBtnAbsentCount.setText(db2.GetIndividualCouunt(myatt!!.studentNo, "ALL", "A")
//                                                   .toString())
//
//
//            //                "A-" + db.CountTermAttendance("FIRST", myatt!!.studentNo, "A")
//            //            itemView.btnLateCount.text =
//            //                "L-" + db.CountTermAttendance("FIRST", myatt!!.studentNo, "L")
//            //            itemView.btnExcuseCount.text =
//            //                "E-" + db.CountTermAttendance("FIRST", myatt!!.studentNo, "E")
//            //
//
//
//            itemView.rowBtnAttendanceStatus.text = myatt!!.attendanceStatus
//            if (myatt!!.attendanceStatus == "P") {
//                itemView.rowBtnAttendanceStatus.text = "P"
//                itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#64B5F6"))
//            } else if (myatt!!.attendanceStatus == "A") {
//                itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#FFB74D"))
//            } else if (myatt!!.attendanceStatus == "L") {
//                itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
//            } else if (myatt!!.attendanceStatus == "E") {
//                itemView.rowBtnAttendanceStatus.setBackgroundColor(Color.parseColor("#BA68C8"))
//                val db: DatabaseHandler = DatabaseHandler(context)
//
//            } else if (myatt!!.attendanceStatus == "-") {
//                itemView.rowBtnAttendanceStatus.setBackgroundResource(android.R.drawable.btn_default);
//                val db: DatabaseHandler = DatabaseHandler(context)
//            } //            itemView.rowBtnPresent.text = "P" + "-" +  db.CountTermAttendance("FIRST" , myatt!!.studentNo, "P")
//            //            itemView.rowBtnAbsent.text = "A" + "-" +  db.CountTermAttendance("FIRST" , myatt!!.studentNo, "A")
//            //            itemView.rowBtnLate.text = "L" + "-" +  db.CountTermAttendance("FIRST" , myatt!!.studentNo, "L")
//            //            itemView.rowBtnExcuse.text = "E" + "-" +  db.CountTermAttendance("FIRST" , myatt!!.studentNo, "E")
//            //                "L" -> itemView.rowBtnLate.setBackgroundColor(Color.parseColor("#69F0AE"))
//            //                "A" -> itemView.rowBtnAbsent.setBackgroundColor(Color.parseColor("#FFB74D"))
//            //                "E" -> itemView.rowBtnExcuse.setBackgroundColor(Color.parseColor("#BA68C8"))
//
//            itemView.rowtxtNum.setVisibility(View.INVISIBLE);
//            itemView.rowtxtStudentNumber.setVisibility(View.INVISIBLE);
//            itemView.rowtxtCompleteName.setVisibility(View.INVISIBLE);
//            itemView.rowBtnRemark.setVisibility(View.INVISIBLE);
//
//            this.currentAttendance = myatt;
//            this.currentPosition = pos
//        }
//    }
//
//
//}
//
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
//
//
//
//
//
//
//
//
//
//
