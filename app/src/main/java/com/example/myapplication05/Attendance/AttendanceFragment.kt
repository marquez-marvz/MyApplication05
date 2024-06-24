//package com.example.myapplication05
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Color
//import android.graphics.drawable.BitmapDrawable
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Spinner
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.myapplication05.R
//import com.example.myapplication05.Student.GetOriginalSection
//import com.github.mikephil.charting.charts.PieChart
//import com.github.mikephil.charting.data.PieEntry
//import kotlinx.android.synthetic.main.att_attendance.*
//import kotlinx.android.synthetic.main.att_attendance.view.*
//import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
//import kotlinx.android.synthetic.main.attendance_new_dialog.view.btnFirst
//import kotlinx.android.synthetic.main.attendance_new_dialog.view.btnLast
//import kotlinx.android.synthetic.main.attendance_new_dialog.view.btnNext
//import kotlinx.android.synthetic.main.attendance_new_dialog.view.btnPrev
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileNotFoundException
//import java.io.FileOutputStream
//import java.util.ArrayList
//
//class AttendanceFragment : Fragment() {
//    var SECTION_NAME = ""
//    var STUD_NAME = ""
//    var dlgAttendance: View? = null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
//        viewAttendance =  inflater.inflate(R.layout.att_attendance, container, false)
//        containerGlobal  = container
//        var context = containerGlobal!!.context
//
//
//
//        viewAttendance!!.txtAttendanceDate.setText(Util.ATT_CURRENT_DATE)
//        viewAttendance!!.txtAttendanceSection.setText(Util.ATT_CURRENT_SECTION)
//        viewAttendance!!.btnAttendanceAmPm.setText(Util.ATT_CURRENT_AMPM)
//        AttendanceViewRecord()
//
//        val db: DatabaseHandler = DatabaseHandler(context)
//        currentSearch = "ALL"
//        ShowCountAttendance(context)
//
//        AttenceCount(context)
//        UpdateListContent(context); // UpdateScanListContent(this);
//        //ScanViewRecord()
//
//
//       viewAttendance!!.btnShowGraph.setBackgroundResource(android.R.drawable.btn_default);
//
//
//
//        viewAttendance!!.btnSetAllPresent.setOnClickListener {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            databaseHandler.UpdateStudentAttendance("P", "ALL", 0, 0)
//            UpdateListContent(context)
//            attendanceAdapter!!.notifyDataSetChanged()
//
//        }
//
//        viewAttendance!!.btnCopyAttendance.setOnClickListener {
//            var str = "Attendance " + txtAttendanceDate.text.toString() + "\n\n"
//            var x = 0; Log.e("XXXYYY", attendanceList.count().toString())
//            while (x < attendanceList.count()) {
//                var name = ""
//                var stat = attendanceList[x].attendanceStatus
//                if (stat == "E" || stat == "L" || stat == "A") {
//
//                    if (attendanceList[x].firstName.length != 0) {
//                        name =
//                            attendanceList[x].lastName + "," + attendanceList[x].firstName.substring(0, 1)
//                    } else {
//                        name = attendanceList[x].lastName
//                    }
//                    Log.e("XXX", x.toString())
//                    Log.e("XXX", attendanceList[x].lastName)
//
//                    str = str + name + "-" + attendanceList[x].attendanceStatus + "\n"
//                }
//                x++;
//            }
//            Util.CopyText(context, str, str)
//        }
//
//
//
//        viewAttendance!!.btnShowDialog.setOnClickListener {
//            var myDate: String = Util.ATT_CURRENT_DATE
//            var ampm: String = Util.ATT_CURRENT_AMPM
//            var section: String = Util.ATT_CURRENT_SECTION
//            val mydb: DatabaseHandler = DatabaseHandler(context)
//            dlgAttendance = LayoutInflater.from(context).inflate(R.layout.attendance_new_dialog, null)
//            val mBuilder = AlertDialog.Builder(context).setView(dlgAttendance).setTitle("Attendance")
//            val recitatioonDialog = mBuilder.show()
//            recitatioonDialog.setCanceledOnTouchOutside(false);
//            val arrSection: Array<String> = Util.GetSectionList(context)
//
//            var sectionAdapter1: ArrayAdapter<String> =
//                ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
//            sectionAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            UpdateListContent(context, "", "LASTNAME")
//            //  dlgstudent.cbosection.setAdapter(sectionAdapter1);
//            INDEX = 0;
//            val arrGroup: Array<String> = Util.GetArrayGroup()
//            val db2: DatabaseHandler = DatabaseHandler(context)
//            val db3: TableAttendance = TableAttendance(context)
//            val arrSection2: ArrayList<String> = db2.GetOriginalSection()
//            var sectionAdapter: ArrayAdapter<String> =
//                ArrayAdapter<String>(context, R.layout.util_spinner, arrSection2)
//            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //            dlgRecite.txtLastNameRecite.setText("
//            dlgAttendance!!.txtFirstNameAtt.setText(attendanceList[INDEX].firstName)
//            dlgAttendance!!.txtLastNameAtt.setText(attendanceList[INDEX].lastName)
//            ShowAttendandeCount(dlgAttendance)
//            val dbAttendance: TableAttendance = TableAttendance(context);
//
//
//
//            STUD_NAME =
//                attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //ShowRemark(dlgRecite)
//            ShowPicture(STUD_NAME)
//
//            dlgAttendance!!.btnPrev.setOnClickListener {
//                if (INDEX > 0) {
//                    INDEX--
//                    dlgAttendance!!.txtFirstNameAtt.setText(attendanceList[INDEX].firstName)
//                    dlgAttendance!!.txtLastNameAtt.setText(attendanceList[INDEX].lastName)
//                    ShowRemark(dlgAttendance)
//                    dlgAttendance!!.txtNum.setText((INDEX + 1).toString() + " / " + totalStudent)
//                    var studName =
//                        attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //ShowRemark(dlgRecite)
//                    ShowPicture(studName)
//                    STUD_NAME =
//                        attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //S
//                    ShowAttendandeCount(dlgAttendance)
//
//                }
//
//            }
//
//            dlgAttendance!!.btnNext.setOnClickListener {
//                if (INDEX < totalStudent - 1) {
//                    INDEX++
//                    dlgAttendance!!.txtFirstNameAtt.setText(attendanceList[INDEX].firstName)
//                    dlgAttendance!!.txtLastNameAtt.setText(attendanceList[INDEX].lastName) //                    ShowRemark(dlgRecite)
//                    ShowRemark(dlgAttendance)
//                    dlgAttendance!!.txtNum.setText((INDEX + 1).toString() + " / " + totalStudent)
//                    var studName =
//                        attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //ShowRemark(dlgRecite)
//                    ShowPicture(studName)
//                    STUD_NAME =
//                        attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //S
//                    ShowAttendandeCount(dlgAttendance)
//                }
//            }
//
//            dlgAttendance!!.btnLast.setOnClickListener {
//                INDEX = totalStudent - 1
//                dlgAttendance!!.txtFirstNameAtt.setText(attendanceList[INDEX].firstName)
//                dlgAttendance!!.txtLastNameAtt.setText(attendanceList[INDEX].lastName) //                ShowRemark(dlgRecite)
//                ShowRemark(dlgAttendance)
//                dlgAttendance!!.txtNum.setText((INDEX + 1).toString() + " / " + totalStudent)
//                var studName =
//                    attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //ShowRemark(dlgRecite)
//                ShowPicture(studName)
//                STUD_NAME =
//                    attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName //S
//                ShowAttendandeCount(dlgAttendance)
//            }
//
//            dlgAttendance!!.btnFirst.setOnClickListener {
//                INDEX = 0
//                dlgAttendance!!.txtFirstNameAtt.setText(attendanceList[INDEX].firstName)
//                dlgAttendance!!.txtLastNameAtt.setText(attendanceList[INDEX].lastName) //                ShowRemark(dlgRecite)
//                ShowRemark(dlgAttendance)
//                dlgAttendance!!.txtNum.setText((INDEX + 1).toString() + " / " + totalStudent)
//                var studName =
//                    attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName
//                ShowPicture(studName)
//                STUD_NAME = attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName
//                ShowAttendandeCount(dlgAttendance)
//            }
//
//            dlgAttendance!!.btnChangePicture.setOnClickListener { //
//                //       val intent = Intent(Intent.ACTION_GET_CONTENT)
//                //                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                //                intent.setType("image/*"); // startActivitinyForResult(intent, 100)
//                //                startActivityForResult(intent, PICK_FILE)
//
//                val cameraImgIntent =
//                    Intent(MediaStore.ACTION_IMAGE_CAPTURE) //            val dirName ="/storage/emulated/0/Picture/one.png" //            cameraImgIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", File(dirName)))
//                startActivityForResult(cameraImgIntent, 100)
//            }
//
//
//
//
//
//
//            dlgAttendance!!.btnLateDialog.setOnClickListener {
//                DefaultColor(dlgAttendance)
//                dlgAttendance!!.btnLateDialog.setBackgroundColor(Color.parseColor("#69F0AE"))
//                var studNum = attendanceList[INDEX].studentNo
//                dbAttendance.UpdateStudentAttendance("L", studNum, 0, 0, "") //
//                ShowCountAttendance(context)
//
//                UpdateListContent(context, "", "LASTNAME")
//                attendanceAdapter!!.notifyDataSetChanged()
//                //               DefaultColor(dlgRecite)
//            }
//
//            dlgAttendance!!.btnPresentDialog.setOnClickListener { ////
//                DefaultColor(dlgAttendance)
//                dlgAttendance!!.btnPresentDialog.setBackgroundColor(Color.parseColor("#64B5F6"))
//                var studNum = attendanceList[INDEX].studentNo
//                dbAttendance.UpdateStudentAttendance("P", studNum, 0, 0, "")
//                ShowCountAttendance(context)
//                UpdateListContent(context, "", "LASTNAME")
//                attendanceAdapter!!.notifyDataSetChanged()
//            }
//
//            dlgAttendance!!.btnAbsentDialog.setOnClickListener {
//                DefaultColor(dlgAttendance)
//                dlgAttendance!!.btnAbsentDialog.setBackgroundColor(Color.parseColor("#FFB74D"))
//                var studNum = attendanceList[INDEX].studentNo
//                dbAttendance.UpdateStudentAttendance("A", studNum, 0, 0, "")
//                ShowCountAttendance(context)
//                UpdateListContent(context, "", "LASTNAME")
//                attendanceAdapter!!.notifyDataSetChanged()
//            }
//
//            dlgAttendance!!.btnExcuseDialog.setOnClickListener { //                DefaultColor(dlgRecite)
//                DefaultColor(dlgAttendance)
//                dlgAttendance!!.btnExcuseDialog.setBackgroundColor(Color.parseColor("#BA68C8"))
//                var studNum = attendanceList[INDEX].studentNo
//                dbAttendance.UpdateStudentAttendance("E", studNum, 0, 0, "")
//                ShowCountAttendance(context)
//                UpdateListContent(context, "", "LASTNAME")
//                attendanceAdapter!!.notifyDataSetChanged()
//            }
//
//            dlgAttendance!!.btnSavePicture.setOnClickListener { //
//                var context = containerGlobal!!.context
//                val dbhandler: DatabaseHandler = DatabaseHandler(context);
//                var section =
//                    dbhandler.GetRealSection(Util.ATT_CURRENT_SECTION) //              DefaultColor(dlgRecite)
//                val draw = dlgAttendance!!.imgStudentAttendance.drawable as BitmapDrawable
//                val bitmap = draw.bitmap
//                Log.e("DDD", "123")
//                var outStream: FileOutputStream? = null
//                val sdCard = Environment.getExternalStorageDirectory()
//                val dir = File(sdCard.absolutePath + "/Picture/" + section)
//                dir.mkdirs()
//                Log.e("DDD", dir.toString())
//                STUD_NAME = attendanceList[INDEX].lastName + "," + attendanceList[INDEX].firstName
//                val fileName = STUD_NAME + ".jpg"
//                val outFile = File(dir, fileName)
//                outStream = FileOutputStream(outFile)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
//                outStream.flush()
//                outStream.close()
//            }
//
//
//
//            dlgAttendance!!.btnSetAllPresentDialog.setOnClickListener {
//                val dbhandler: DatabaseHandler = DatabaseHandler(context);
//                val databaseHandler: TableAttendance = TableAttendance(context)
//                databaseHandler.UpdateStudentAttendance("P", "ALL", 0, 0)
//                UpdateListContent(context)
//                attendanceAdapter!!.notifyDataSetChanged()
//                ShowCountAttendance(context)
//                UpdateListContent(context, "", "LASTNAME")
//                attendanceAdapter!!.notifyDataSetChanged()
//            }
//        }
//
//
//
//
//
//
//
//
//        viewAttendance!!.btnSearchFirst.setOnClickListener {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            UpdateListContent(context,  "A-C", "SEARCHLETTER")
//            attendanceAdapter!!.notifyDataSetChanged()
//        }
//        viewAttendance!!.btnSearchSecond.setOnClickListener {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            UpdateListContent(context, "D-J", "SEARCHLETTER")
//            attendanceAdapter!!.notifyDataSetChanged()
//        }
//        viewAttendance!!.btnSearchThird.setOnClickListener {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            UpdateListContent(context, "K-O", "SEARCHLETTER")
//            attendanceAdapter!!.notifyDataSetChanged()
//        }
//        viewAttendance!!.btnSearchFourth.setOnClickListener {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            UpdateListContent(context, "P-R", "SEARCHLETTER")
//            attendanceAdapter!!.notifyDataSetChanged()
//        }
//        viewAttendance!!.btnSearchFifth.setOnClickListener {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            UpdateListContent(context, "S-Z", "SEARCHLETTER")
//            attendanceAdapter!!.notifyDataSetChanged()
//        }
//
//
//        viewAttendance!!.btnSortData.setOnClickListener {
//
//            TXT_STATUS = false
//
//            7
//            var txt = btnSortData!!.text.toString()
//            if (txt == "GE") {
//                btnSortData.setText("LN")
//                UpdateListContent(context, "", "LASTNAME")
//            }
//            if (txt == "LN") {
//                btnSortData.setText("FN")
//                UpdateListContent(context, "", "FIRSTNAME")
//            }
//            if (txt == "FN") {
//                btnSortData.setText("RD")
//                UpdateListContent(context, "", "RANDOM")
//
//            }
//
//            if (txt == "RD") {
//                btnSortData.setText("GE")
//                UpdateListContent(context)
//            }
//
//
//            attendanceAdapter!!.notifyDataSetChanged()
//
//
//        }
//
//        viewAttendance!!.btnShowGraph.setOnClickListener {
//            val db: DatabaseHandler = DatabaseHandler(context)
//            val dlgactivity = LayoutInflater.from(context).inflate(R.layout.piechart, null)
//            val mBuilder =
//                AlertDialog.Builder(context).setView(dlgactivity).setTitle("Input Group Score")
//            val activityDialog = mBuilder.show()
//            activityDialog.setCanceledOnTouchOutside(false); //
//            var pie: PieChart =
//                dlgactivity.findViewById(R.id.piechart) as PieChart //  remarkList = db.GetActivityRecord(db.GetCurrentSection(), e.activityCode)
//            var attendanceCountList = arrayListOf<PieEntry>()
//            val db3: TableAttendance = TableAttendance(context)
//            var absent = db3.CountAttendance("A").toFloat()
//            if (absent > 0) {
//                attendanceCountList.add(PieEntry(absent, "ABSENT"))
//            }
//
//            var present = db3.CountAttendance("P").toFloat()
//            if (present > 0) {
//                attendanceCountList.add(PieEntry(present, "PRESENT"))
//            }
//
//
//            var late = db3.CountAttendance("L").toFloat()
//            if (late > 0) {
//                attendanceCountList.add(PieEntry(late, "LATE"))
//            }
//
//            var excuse = db3.CountAttendance("E").toFloat()
//            if (excuse > 0) {
//                attendanceCountList.add(PieEntry(absent, "EXCUSE"))
//            }
//
//            var none = db3.CountAttendance("-").toFloat()
//            if (none > 0) {
//                attendanceCountList.add(PieEntry(none, "NONE"))
//            }
//            Log.e("-", absent.toString())
//            Log.e("A", present.toString())
//            Log.e("-", none.toString())
//
//            Chart.SetUpPie(pie, attendanceCountList, Util.ACT_DESCRIPTION)
//        }
//
//
//
//
//        viewAttendance!!.btnCountPresent.setOnClickListener {
//            ShowAttendanceStatus("P")
//        }
//
//        viewAttendance!!.btnCountLate.setOnClickListener {
//            ShowAttendanceStatus("L")
//        }
//
//        viewAttendance!!.btnCountAbsent.setOnClickListener {
//            ShowAttendanceStatus("A")
//        }
//
//        viewAttendance!!.btnCountExcuse.setOnClickListener {
//            ShowAttendanceStatus("E")
//        }
//
//
//
//        return viewAttendance
//    }
//
//    companion object {
//        var containerGlobal: ViewGroup? = null
//        var viewAttendance: View? = null
//        //var attendanceAdapter: AttendandeNewAdapter? = null;
//        var attendanceScanAdapter: AttendanceScanAdapter? = null;
//        var attendanceCsvAdapter: AttendanceCSVAdapter? = null;
//        var attendanceList = arrayListOf<AttendanceModel>()
//        var attendanceScanList = arrayListOf<AttendanceScanModel>()
//        var attendanceCsvList = arrayListOf<AttendanceCSVModel>()
//        var PICK_FILE = 100;
//        var btnpresent: Button? = null;
//        var varBtnCountLate: Button? = null;
//        var varBtnCountAbsent: Button? = null;
//        var varBtnCountPresent: Button? = null;
//        var varBtnCountExcuse: Button? = null;
//
//
//
//
//        var btnabsent: Button? = null;
//        var btnexcuse: Button? = null;
//        var btn_none: Button? = null;
//        var cboGroup: Spinner? = null;
//        var txtSearch: EditText? = null;
//        var GRP_STATUS: Boolean = false;
//        var TXT_STATUS: Boolean = false;
//        var INDEX = 0;
//        var totalStudent = 0;
//
//        var currentSearch = ""
//
//
//        fun AttenceCount(context: Context) {
//            val db: TableAttendance =
//                TableAttendance(context) //            btnabsent!!.setText("A" + db.CountAttendance("A")) //            btnlate!!.setText("L " + db.CountAttendance("L")) //            btnpresent!!.setText("P " +db.CountAttendance("P")) //            btnexcuse!!.setText("E " + db.CountAttendance("E")) //            btn_none!!.setText("-    " + db.CountAttendance("-"))
//        }
//
//        fun UpdateScanListContent(context: Context, num: String, studNum: String, studName: String, status: String) {
//
//            for (att in attendanceScanList) {
//                if (att.studNumber == studNum) return
//            }
//            attendanceScanList.add(AttendanceScanModel(num, studNum, studName, status))
//        }
//
//        fun UpdateListContent(context: Context, search_attenndance: String = "", sortOrder: String = "") {
//            val databaseHandler: TableAttendance = TableAttendance(context)
//            var grp = ""
//            val att_list: List<AttendanceModel>
//            var txt = ""
//            Log.e("ATTX", sortOrder) //            if (grp != "ALL") {
//            //                Util.Msgbox(context, "Hello22")
//            //                att_list = databaseHandler.GetAttendanceList("GROUP", grp)
//            //            }
//            //            else if (txt != "") att_list = databaseHandler.GetAttendanceList("NAME", txt)
//            if (sortOrder == "ATT_STATUS") {
//                att_list = databaseHandler.GetAttendanceList(sortOrder, search_attenndance)
//            } else if (sortOrder == "SEARCHLETTER") {
//                att_list = databaseHandler.GetAttendanceList(sortOrder, search_attenndance)
//            } else if (search_attenndance != "" && sortOrder == "") {
//                att_list = databaseHandler.GetAttendanceList("ATTENDANCE", search_attenndance)
//
//            } else if (search_attenndance == "" && sortOrder == "LASTNAME") {
//                att_list = databaseHandler.GetAttendanceList("LASTNAME")
//            } else if (search_attenndance == "" && sortOrder == "RANDOM") {
//                att_list = databaseHandler.GetAttendanceList("RANDOM")
//            } else if (search_attenndance == "" && sortOrder == "FIRSTNAME") {
//                att_list = databaseHandler.GetAttendanceList("FIRSTNAME")
//            } else {
//                att_list = databaseHandler.GetAttendanceList()
//            }
//            attendanceList.clear()
//            totalStudent = att_list.count()
//            for (att in att_list) {
//                attendanceList.add(AttendanceModel(att.ampm, att.myDate, att.sectionCode, att.studentNo, att.completeName, att.groupNumber, att.attendanceStatus, att.remark, att.recitationPoint, att.TaskPoints, att.randomNumber, att.firstName, att.lastName))
//            }
//
//        }
//
//        fun ShowCountAttendance(context: Context){
//            val db3: TableAttendance = TableAttendance(context)
//            viewAttendance!!.btnCountPresent.setText("P=" + db3.CountAttendance("P"))
//            viewAttendance!!.btnCountAbsent.setText("A=" + db3.CountAttendance("A"))
//            viewAttendance!!.btnCountLate.setText("L=" + db3.CountAttendance("L"))
//            viewAttendance!!.btnCountExcuse.setText("E=" + db3.CountAttendance("E"))
//        }
//    }
//
//    fun AttendanceViewRecord() {
//        var context = containerGlobal!!.context
//        val layoutmanager = LinearLayoutManager(context)
//        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
//        viewAttendance!!.listAttendance.layoutManager = layoutmanager
//       // attendanceAdapter = AttendandeNewAdapter(context, attendanceList)
//        viewAttendance!!.listAttendance.adapter = attendanceAdapter
//    }
//
//
//
//
//    fun ShowAttendandeCount(dlgAttendance: View?) {
//        var context = containerGlobal!!.context
//        val db3: TableAttendance = TableAttendance(context)
//        var absentCount = db3.GetIndividualCouunt(attendanceList[INDEX].studentNo, "ALL", "A")
//        dlgAttendance!!.txtAbsentCount.setText("A- " + absentCount.toString())
//        var lateCount = db3.GetIndividualCouunt(attendanceList[INDEX].studentNo, "ALL", "L")
//        dlgAttendance!!.txtLateCount.setText("L- " + lateCount.toString())
//        var excuseCount = db3.GetIndividualCouunt(attendanceList[INDEX].studentNo, "ALL", "E")
//        dlgAttendance!!.txtExcuseCount.setText("E- " + excuseCount.toString())
//
//    }
//
//    fun ShowPicture(studName: String) {
//        var context = containerGlobal!!.context
//        val dbhandler: DatabaseHandler = DatabaseHandler(context);
//        var section = dbhandler.GetRealSection(Util.ATT_CURRENT_SECTION)
//        Log.e("section111", section)
//        try {
//            val path = "/storage/emulated/0/Picture/" + section
//            Log.e("sss", path)
//            Log.e("sss", studName + ".jpg")
//
//            val f: File = File(path, studName + ".jpg")
//            if (f.exists()) {
//                val b = BitmapFactory.decodeStream(FileInputStream(f))
//                dlgAttendance!!.imgStudentAttendance.setImageBitmap(b)
//            } else {
//                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
//                val b = BitmapFactory.decodeStream(FileInputStream(f))
//                dlgAttendance!!.imgStudentAttendance.setImageBitmap(b)
//            }
//            Log.e("sss", path)
//            Log.e("sss", studName + ".jpg")
//
//            // val img = findViewById<View>(R.id.imgStudent) as ImageView
//
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//    }
//
//    fun ShowRemark(dlgAttendance: View?) {
//        DefaultColor(dlgAttendance)
//        var context = containerGlobal!!.context
//        val dbhandler: DatabaseHandler = DatabaseHandler(context);
//
//        var studNum = attendanceList[INDEX].studentNo
//
//        var myDate: String = Util.ATT_CURRENT_DATE
//        var ampm: String = Util.ATT_CURRENT_AMPM
//        var section: String =
//            Util.ATT_CURRENT_SECTION //(studentno: String, theDate: String, theTime:String , section: String, ): String
//        var att = dbhandler.GetIndividualAttendance(studNum, myDate, ampm, section)
//        Log.e("studNum", studNum)
//        Log.e("myDate", myDate)
//        Log.e("ampm", ampm)
//        Log.e("sectio ", section)
//        Log.e("att ", att)
//
//        if (att == "P") {
//            dlgAttendance!!.btnPresentDialog.setBackgroundColor(Color.parseColor("#64B5F6"))
//        }
//
//        if (att == "L") {
//            dlgAttendance!!.btnLateDialog.setBackgroundColor(Color.parseColor("#69F0AE"))
//        }
//
//        if (att == "A") {
//            dlgAttendance!!.btnAbsentDialog.setBackgroundColor(Color.parseColor("#FFB74D"))
//        }
//
//        if (att == "E") {
//            dlgAttendance!!.btnExcuseDialog.setBackgroundColor(Color.parseColor("#BA68C8"))
//
//        }
//
//    }
//
//    fun DefaultColor(dlgAttendance: View?) {
//        dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
//        dlgAttendance!!.btnAbsentDialog.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
//        dlgAttendance!!.btnLateDialog.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
//        dlgAttendance!!.btnExcuseDialog.setBackgroundResource(android.R.drawable.btn_default);
//    }
//
//
//    fun ShowAttendanceStatus(status: String = "") {
//        var context = containerGlobal!!.context
//        TXT_STATUS = false
//        UpdateListContent(context, status, "ATT_STATUS")
//        attendanceAdapter!!.notifyDataSetChanged()
//    }
//
//
//
//}
