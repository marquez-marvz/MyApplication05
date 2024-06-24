package com.example.myapplication05


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.Student.GetOriginalSection
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.attendance_dialog.view.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.attendance_main.btnSearchFifth
import kotlinx.android.synthetic.main.attendance_main.btnSearchFirst
import kotlinx.android.synthetic.main.attendance_main.btnSearchFourth
import kotlinx.android.synthetic.main.attendance_main.btnSearchSecond
import kotlinx.android.synthetic.main.attendance_main.btnSearchThird
import kotlinx.android.synthetic.main.attendance_new_dialog.*
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.qrcode.*
import kotlinx.android.synthetic.main.recitation_main.*
import kotlinx.android.synthetic.main.sched_main.btnAmPm
import kotlinx.android.synthetic.main.sched_main.cboMonthDate
import kotlinx.android.synthetic.main.sched_main.cboMonthName
import kotlinx.android.synthetic.main.sched_main.cboSectionSched
import kotlinx.android.synthetic.main.student_dialog.view.*
import java.io.*
import java.time.LocalDate
import java.util.*


class AttendanceMain : AppCompatActivity() {
    val context = this;
    val dbhandler: DatabaseHandler = DatabaseHandler(this);
    val dbAttendance: TableAttendance = TableAttendance(this);
    var SECTION_NAME = ""
    var STUD_NAME = ""


    var dlgAttendance: View? = null

    companion object {
        var currentAttendance: String = ""
        var adapterDate: ScheduleAdapter? = null;
        var adapterIndividual: AttendanceIndividualAdapter? = null;
        var scheduleList = arrayListOf<ScheduleModel>()
        var attendanceIndividualList = arrayListOf<AttendanceModel>()
        var attendanceAdapter: AttendanceAdapter? = null;
        var attendanceScanAdapter: AttendanceScanAdapter? = null;
        var attendanceCsvAdapter: AttendanceCSVAdapter? = null;
        var attendanceList = arrayListOf<AttendanceModel>()
        var attendanceScanList = arrayListOf<AttendanceScanModel>()
        var attendanceCsvList = arrayListOf<AttendanceCSVModel>()
        var PICK_FILE = 100;
        var btnpresent: Button? = null;
        var varBtnCountLate: Button? = null;
        var varBtnCountAbsent: Button? = null;
        var varBtnCountPresent: Button? = null;
        var varBtnCountExcuse: Button? = null;
        var vartxtAttendanceDate: TextView? = null;
        var vartxtAttendanceRemark: EditText? = null;
        var vartxtAttendanceDay: TextView? = null;
        var varbtnCurrentStatus: Button? = null;
        var varbtnAttendanceTime: TextView? = null;
        var vartxtStudentName: TextView? = null;
        var varbtnViewStatus: TextView? = null;
        var varlistViewIndividual: RecyclerView? = null;
        var varlistViewDate: RecyclerView? = null;
        var varcboAttandanceSesrch: Spinner? = null;
        var varImgAttendancePicture: TouchImageView? = null;




        var btnlate: Button? = null;
        var btnabsent: Button? = null;
        var btnexcuse: Button? = null;
        var btn_none: Button? = null;
        var cboGroup: Spinner? = null;
        var txtSearch: EditText? = null;
        var varpieChart: PieChart? = null;


        var globalSearch = ""
        var GRP_STATUS: Boolean = false;
        var TXT_STATUS: Boolean = false;
        var INDEX = 0;
        var totalStudent = 0;

        var currentSearch = ""

        fun SS() {

        }

        //1200
        fun UpdateListContentIndividualAttendance(context: Context, category: String = "MONTH", studNum: String = "") {

            val db1: TableAttendance = TableAttendance(context);
            val db2: DatabaseHandler = DatabaseHandler(context);

            var pp: AttendanceModel = AttendanceModel()

            val individuallist: List<AttendanceModel>

            individuallist = db2.GetIndividualAttendance("", studNum)
            Log.e("213456", individuallist.size.toString())
            attendanceIndividualList.clear()
            var x = 0;
            var len = attendanceIndividualList.size
            for (att in individuallist) {

                attendanceIndividualList.add(AttendanceModel(att.ampm, att.myDate, att.sectionCode, att.studentNo, att.completeName, att.groupNumber, att.attendanceStatus, att.remark, att.recitationPoint, att.TaskPoints, att.randomNumber, att.firstName, att.lastName))
                x++;
            }

            for (att in attendanceIndividualList) {
                Log.e("213000", att.myDate)

            }

            Log.e("213000", attendanceIndividualList.size.toString())
        }


        fun UpdateScanListContent(context: Context, num: String, studNum: String, studName: String, status: String) {

            for (att in attendanceScanList) {
                if (att.studNumber == studNum) return
            }
            attendanceScanList.add(AttendanceScanModel(num, studNum, studName, status))
        }

        fun UpdateListContent(context: Context, mydate: String, ampm: String, search_attenndance: String = "", sortOrder: String = "") {
            val databaseHandler: TableAttendance = TableAttendance(context)
            var grp = ""
            val att_list: List<AttendanceModel>
            var txt = ""
            Log.e("3612-3", Util.ATT_CURRENT_DATE)
            Log.e("ATTX", sortOrder) //            if (grp != "ALL") {
            //                Util.Msgbox(context, "Hello22")
            //                att_list = databaseHandler.GetAttendanceList("GROUP", grp)
            //            }
            //            else if (txt != "") att_list = databaseHandler.GetAttendanceList("NAME", txt)
            if (sortOrder == "ATT_STATUS") {
                att_list =
                    databaseHandler.GetAttendanceList(mydate, ampm, sortOrder, search_attenndance)
            } else if (sortOrder == "SEARCHLETTER") {
                att_list =
                    databaseHandler.GetAttendanceList(mydate, ampm, sortOrder, search_attenndance)
            } else if (search_attenndance != "" && sortOrder == "") {
                att_list =
                    databaseHandler.GetAttendanceList(mydate, ampm, "ATTENDANCE", search_attenndance)

            } else if (search_attenndance == "" && sortOrder == "LASTNAME") {
                att_list = databaseHandler.GetAttendanceList(mydate, ampm, "LASTNAME")
            } else if (search_attenndance == "" && sortOrder == "RANDOM") {
                att_list = databaseHandler.GetAttendanceList(mydate, ampm, "RANDOM")
            } else if (search_attenndance == "" && sortOrder == "FIRSTNAME") {
                att_list = databaseHandler.GetAttendanceList(mydate, ampm, "FIRSTNAME")
            } else {
                att_list = databaseHandler.GetAttendanceList(mydate, ampm)
            }
            attendanceList.clear()
            totalStudent = att_list.count()
            Log.e("4578", att_list.toString())
            Log.e("3612-3", att_list.count().toString())
            for (att in att_list) {
                attendanceList.add(AttendanceModel(att.ampm, att.myDate, att.sectionCode, att.studentNo, att.completeName, att.groupNumber, att.attendanceStatus, att.remark, att.recitationPoint, att.TaskPoints, att.randomNumber, att.firstName, att.lastName))
            }

        }


        fun ShowGraph(context: Context) {
            Log.e("12356A", "LLL")

            var date = vartxtAttendanceDate!!.text.toString()
            var ampm = varbtnAttendanceTime!!.text.toString()
            var section = Util.ATT_CURRENT_SECTION

            Log.e("12490", date)
            Log.e("12490", ampm)
            Log.e("12490", section)

            val db: DatabaseHandler = DatabaseHandler(context)
            var attendanceCountList = arrayListOf<PieEntry>()
            val db3: TableAttendance = TableAttendance(context)
            var absent = db3.CountAttendance("A", date, ampm, section).toFloat()
            if (absent > 0) {
                attendanceCountList.add(PieEntry(absent, "ABSENT"))
            }

            var present = db3.CountAttendance("P", date, ampm, section).toFloat()
            if (present > 0) {
                attendanceCountList.add(PieEntry(present, "PRESENT"))
            }


            var late = db3.CountAttendance("L", date, ampm, section).toFloat()
            if (late > 0) {
                attendanceCountList.add(PieEntry(late, "LATE"))
            }

            var excuse = db3.CountAttendance("E", date, ampm, section).toFloat()
            if (excuse > 0) {
                attendanceCountList.add(PieEntry(excuse, "EXCUSE"))
            }

            var none = db3.CountAttendance("-", date, ampm, section).toFloat()
            if (none > 0) {
                attendanceCountList.add(PieEntry(none, "NONE"))
            }
            Log.e("12356A", absent.toString())
            Log.e("12356P", present.toString())
            Log.e("12356-", none.toString())

            Chart.SetUpPieAttendance(varpieChart, attendanceCountList, Util.ACT_DESCRIPTION)

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance_main)
        Log.e("12113", "")
        txtAttendanceDate.setText(Util.ATT_CURRENT_DATE)
        val db3: TableAttendance = TableAttendance(this);
        txtAttendanceSection.setText(db3.GetCurrentSection())
        btnAttendanceTime.setText(Util.ATT_CURRENT_AMPM)
        UpdateListContent(this, "ALL", "LASTNAME")
        AttendanceViewRecord()
        Util.ATT_FLAG = "ATTENDANCE"

        val db: DatabaseHandler = DatabaseHandler(this)
        val context = this
        currentSearch = "ALL"
        currentAttendance = ""


        vartxtStudentName = findViewById(R.id.txtNewStudName) as TextView
        vartxtAttendanceDate = findViewById(R.id.txtAttendanceDate) as TextView
        vartxtAttendanceDay = findViewById(R.id.txtAttendanceDay) as TextView
        vartxtAttendanceDay = findViewById(R.id.txtAttendanceDay) as TextView
        vartxtAttendanceRemark = findViewById(R.id.txtAttendanceRemark) as EditText
        varbtnAttendanceTime = findViewById(R.id.btnAttendanceTime) as TextView
        varbtnViewStatus = findViewById(R.id.btnViewStatus) as TextView
        varlistViewIndividual = findViewById(R.id.listViewIndividual) as RecyclerView
        varlistViewDate = findViewById(R.id.listViewDate) as RecyclerView
        varcboAttandanceSesrch = findViewById(R.id.cboAttandanceSesrch) as Spinner
        varImgAttendancePicture = findViewById(R.id.imgAttendancePicture) as TouchImageView





        varpieChart = findViewById(R.id.piechart) as PieChart



        UpdateListContent(context, "ALL", "LASTNAME")
        attendanceAdapter!!.notifyDataSetChanged()
//        ShowCountAttendance(context)
        vartxtAttendanceDate!!.text = Util.ATT_CURRENT_DATE
        vartxtAttendanceRemark!!.setText(Util.ATT_CURRENT_REMARK.toString())
        vartxtAttendanceDay!!.text = Util.ATT_CURRENT_DAY




        //ShowCountAttendance(this)
        ShowGraph(this)

        //1400
        val today = LocalDate.now()
        val monthNum = today.monthValue
        cboAttandanceSesrch.setSelection(monthNum + 1)
        var month = cboAttandanceSesrch.getSelectedItem().toString();

        //1200
        UpdateListContentSched(this, "MONTH", month);
        SetUpListViewSchedAdapter()

        UpdateListContentIndividualAttendance(this, "MAY", "")
        SetUpListViewIndividualAttendanceAdapter()


        UpdateListContent(this, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM); // UpdateScanListContent(this);
        //ScanViewRecord()
        Log.e("3612-8", Util.ATT_CURRENT_DATE)



//1500
        btnViewStatus.setOnClickListener {
            AttendanceMain.vartxtStudentName!!.isVisible = false
            AttendanceMain.varbtnViewStatus!!.text = "SCHED"
            AttendanceMain.varlistViewIndividual!!.isVisible = false
            AttendanceMain.varlistViewDate!!.isVisible = true
            val today = LocalDate.now()
            val monthNum = today.monthValue
            cboAttandanceSesrch.setSelection(monthNum + 1)
           var month = cboAttandanceSesrch.getSelectedItem().toString();
        }


            btnAddNewsched.setOnClickListener {
            val cal = Calendar.getInstance()
            var myMonth = cal.get(Calendar.MONTH)
            var myDay = cal.get(Calendar.DAY_OF_MONTH)
            var ampm1 = cal.get(Calendar.AM_PM)
            val monthName =
                arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC") //  Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year)
            val date = monthName[myMonth] + " " + Util.ZeroPad(myDay, 2)
            val myDate = date
            var ampm = ""

            if (ampm1 == 1) {
                ampm = "PM"
            } else {
                ampm = "PM"
            }


            val section = Util.ATT_CURRENT_SECTION
            val db1: TableActivity = TableActivity(this);
            val id = db1.GetNewSchedCode(section)
            SchedMain.AddNewSched(this, myDate, ampm, section, id)

            Log.e("4678", "@@@")
            Util.ATT_CURRENT_DATE = date
            Util.ATT_CURRENT_AMPM = ampm


            var ATT_CURRENT_REMARK: String = ""
            UpdateListContent(context, date, ampm, "ALL", "LASTNAME")
            attendanceAdapter!!.notifyDataSetChanged()
            vartxtAttendanceRemark!!.setText("-")
            vartxtAttendanceDay!!.text = ""
            vartxtAttendanceDate!!.text = date
            varbtnAttendanceTime!!.text = ampm
            ShowGraph(context)
            UpdateListContentSched(this, "MONTH", GetMonth());
            adapterDate!!.notifyDataSetChanged()


        } //        btnCurrentStatus.setOnClickListener {
        //            var p = btnCurrentStatus.text
        //            if (p == "-") {
        //                btnCurrentStatus.text =
        //                    "PRESENT" // btnCurrentStatus.setBackgroundColor(Color.parseColor("#64B5F6"))
        //                btnCurrentStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
        //            } else if (p == "PRESENT") {
        //                btnCurrentStatus.text = "LATE"
        //                btnCurrentStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))
        //            } else if (p == "LATE") {
        //                btnCurrentStatus.text = "ABSENT"
        //                btnCurrentStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))
        //            } else if (p == "ABSENT") {
        //                btnCurrentStatus.text = "EXCUSE"
        //                btnCurrentStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BA68C8")))
        //            }
        //        }

        //1400
        cboAttandanceSesrch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { //
                if (btnViewStatus.text.toString() == "SCHED") {
                    var category = cboAttandanceSesrch.getSelectedItem().toString();

                    if (category == "FIRST" || category == "SECOND" || category == "ALL") {
                        UpdateListContentSched(context, category);
                        adapterDate!!.notifyDataSetChanged()

                    } else {
                        var month = category
                        UpdateListContentSched(context, "MONTH", month);
                        adapterDate!!.notifyDataSetChanged()
                    }
                }
            }
        }

        btnCurrentPresent.setOnClickListener {
            Log.e("3390", currentAttendance)
            if (currentAttendance == "PRESENT") {
                btnCurrentPresent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                currentAttendance = ""
            } else {
                btnCurrentPresent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
                currentAttendance = "PRESENT"
                btnCurrentExcuse.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentAbsent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentlate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
            }
        }

        btnCurrentlate.setOnClickListener {
            if (currentAttendance == "LATE") {
                btnCurrentlate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                currentAttendance = ""
            } else {
                currentAttendance = "LATE"
                btnCurrentlate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))
                btnCurrentExcuse.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentAbsent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentPresent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
            }
        }



        btnCurrentAbsent.setOnClickListener {
            Log.e("3390", currentAttendance)
            if (currentAttendance == "ABSENT") {
                btnCurrentAbsent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                currentAttendance = ""
            } else {
                btnCurrentAbsent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))
                currentAttendance = "ABSENT"
                btnCurrentExcuse.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentPresent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentlate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
            }
        }


        btnCurrentExcuse.setOnClickListener {
            Log.e("3390", currentAttendance)
            if (currentAttendance == "EXCUSE") {
                btnCurrentAbsent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                currentAttendance = ""
            } else {
                btnCurrentExcuse.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FAFAD2")))
                currentAttendance = "EXCUSE"
                btnCurrentAbsent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentPresent.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
                btnCurrentlate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
            }
        }


        //            var p = btnCurrentStatus.text
        //            if (p == "LATE") {
        //                btnCurrentStatus.text = "PRESENT"
        //
        //            } else if (p == "ABSENT") {
        //                btnCurrentStatus.text = "LATE"
        //                btnCurrentStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))
        //            } else if (p == "EXCUSE") {
        //                btnCurrentStatus.text = "ABSENT"
        //                btnCurrentStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))
        //            }
        //            true
        //        }


        btnSetAllPresent.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            var date = txtAttendanceDate.text.toString()
            var ampm = btnAttendanceTime.text.toString()


            databaseHandler.UpdateStudentAttendance("P", "ALL", date, ampm)

            UpdateListContent(this, date, ampm)
            attendanceAdapter!!.notifyDataSetChanged()

        }

        fun piechart() {}
        piechart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                var remark = (e as PieEntry).label
                Log.e("3612", Util.ATT_CURRENT_DATE)
                if (remark == "PRESENT") {
                    ShowAttendanceStatus("P")
                }

                if (remark == "ABSENT") {
                    ShowAttendanceStatus("A")
                }

                if (remark == "LATE") {
                    ShowAttendanceStatus("L")
                }


                if (remark == "EXCUSE") {
                    ShowAttendanceStatus("E")
                }


                if (remark == "NONE") {
                    ShowAttendanceStatus("-")
                }


            }

            override fun onNothingSelected() {}
        })


        //        btnScanQR.setOnClickListener {
        //            ScanViewRecord()
        //
        //            ScanViewRecord()
        //            scanCode()
        //        }


        btnCopyAttendance.setOnClickListener {
            var str = "Attendance " + txtAttendanceDate.text.toString() + "\n\n"
            var x = 0; Log.e("XXXYYY", attendanceList.count().toString())
            while (x < attendanceList.count()) {
                var name = ""
                var stat = attendanceList[x].attendanceStatus
                if (stat == "E" || stat == "L" || stat == "A") {

                    if (attendanceList[x].firstName.length != 0) {
                        name =
                            attendanceList[x].lastName + "," + attendanceList[x].firstName.substring(0, 1)
                    } else {
                        name = attendanceList[x].lastName
                    }
                    Log.e("XXX", x.toString())
                    Log.e("XXX", attendanceList[x].lastName)

                    str = str + name + "-" + attendanceList[x].attendanceStatus + "\n"
                }
                x++;
            }
            Util.CopyText(this, str, str)

        }











        btnSearchFirst.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            UpdateListContent(this, "A-C", "SEARCHLETTER")
            attendanceAdapter!!.notifyDataSetChanged()
        }
        btnSearchSecond.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            UpdateListContent(this, "D-J", "SEARCHLETTER")
            attendanceAdapter!!.notifyDataSetChanged()
        }
        btnSearchThird.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            UpdateListContent(this, "K-O", "SEARCHLETTER")
            attendanceAdapter!!.notifyDataSetChanged()
        }
        btnSearchFourth.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            UpdateListContent(this, "P-R", "SEARCHLETTER")
            attendanceAdapter!!.notifyDataSetChanged()
        }
        btnSearchFifth.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            UpdateListContent(this, "S-Z", "SEARCHLETTER")
            attendanceAdapter!!.notifyDataSetChanged()
        }


        //
        //        btnCountPresent.setOnClickListener {
        //            Log.e("ATTX", "present")
        //            ShowAttendanceStatus("P")
        //        }

        //        btnClearList.setOnClickListener {
        //            attendanceScanList.clear()
        //            attendanceScanAdapter!!.notifyDataSetChanged()
        //        }
        //
        //        btnCountNone.setOnClickListener {
        //            Log.e("ATTX", "present")
        //            ShowAttendanceStatus("-")
        //        }

        //        btnSortData.setOnClickListener {
        //
        //            TXT_STATUS = false
        //
        //            var date = txtAttendanceDate.text.toString()
        //            var ampm = btnAttendanceTime.text.toString()
        //            var txt = btnSortData!!.text.toString()
        //            if (txt == "GE") {
        //                btnSortData.setText("LN")
        //                UpdateListContent(this, date, ampm, "", "LASTNAME")
        //            }
        //            if (txt == "LN") {
        //                btnSortData.setText("FN")
        //                UpdateListContent(this, date, ampm, "", "FIRSTNAME")
        //            }
        //            if (txt == "FN") {
        //                btnSortData.setText("RD")
        //                UpdateListContent(this, date, ampm, "", "RANDOM")
        //
        //            }
        //
        //            if (txt == "RD") {
        //                btnSortData.setText("GE")
        //                UpdateListContent(this, date, ampm)
        //            }
        //
        //
        //            attendanceAdapter!!.notifyDataSetChanged()
        //
        //
        //        }


    }

    fun SetUpListViewSchedAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listViewDate.layoutManager = layoutmanager
        adapterDate = ScheduleAdapter(this, scheduleList)
        listViewDate.adapter = adapterDate
    }

    fun SetUpListViewIndividualAttendanceAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listViewIndividual.layoutManager = layoutmanager
        Log.e("213555", attendanceIndividualList.size.toString())
        adapterIndividual = AttendanceIndividualAdapter(this, attendanceIndividualList)
        listViewIndividual.adapter = adapterIndividual
    }


    fun UpdateListContentSched(context: Context, category: String = "MONTH", monthName: String = "") {
        var myDate: String = Util.CURRENT_DATE
        val db1: TableAttendance = TableAttendance(context);
        val db2: DatabaseHandler = DatabaseHandler(context);
        var section = db2.GetCurrentSection()
        val schedlist: List<ScheduleModel>
        Log.e("@@@", "123456789")
        schedlist = db1.GetScheduleList(section, category, monthName, "ASC")

        scheduleList.clear()
        var x = 0;
        var len = schedlist.size
        for (sched in schedlist) {
            scheduleList.add(ScheduleModel(sched.ampm, sched.myDate, sched.sectioncode, sched.renark, sched.term, sched.schedId, sched.day))
            if (x == len - 1 && Util.CURRENT_DATE == "") {
                txtAttendanceDate.text =
                    sched.myDate //         txtAttendanceDate.text = sched.myDate

            }
            x++;
        }

    }

    fun UpdateListContentAttendance(context: Context, category: String = "MONTH", monthName: String = "") {
        var myDate: String = Util.CURRENT_DATE
        val db1: TableAttendance = TableAttendance(context);
        val db2: DatabaseHandler = DatabaseHandler(context);
        var section = db2.GetCurrentSection()
        val schedlist: List<ScheduleModel>
        Log.e("@@@", "123456789")
        schedlist = db1.GetScheduleList(section, category, monthName, "ASC")

        scheduleList.clear()
        var x = 0;
        var len = schedlist.size
        for (sched in schedlist) {
            scheduleList.add(ScheduleModel(sched.ampm, sched.myDate, sched.sectioncode, sched.renark, sched.term, sched.schedId, sched.day))
            if (x == len - 1 && Util.CURRENT_DATE == "") {
                txtAttendanceDate.text =
                    sched.myDate //         txtAttendanceDate.text = sched.myDate

            }
            x++;
        }

    }


    fun GetMonth(): String {
        var myMonth: Int
        val cal = Calendar.getInstance()

        myMonth = cal.get(Calendar.MONTH)


        val monthName =
            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

        return monthName[myMonth]

    }

    override fun onBackPressed() {

        super.onBackPressed()
        Log.e("6790", "")
    }


    fun ShowPicture(studName: String) {
        var section = dbhandler.GetRealSection(Util.ATT_CURRENT_SECTION)
        Log.e("section111", section)
        try {
            val path = "/storage/emulated/0/Picture/" + section
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")

            val f: File = File(path, studName + ".jpg")
            if (f.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                dlgAttendance!!.imgStudentAttendance.setImageBitmap(b)
            } else {
                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                dlgAttendance!!.imgStudentAttendance.setImageBitmap(b)
            }
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")

            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun ShowRemark(dlgAttendance: View?) {
        DefaultColor(dlgAttendance)

        var studNum = attendanceList[INDEX].studentNo

        var myDate: String = Util.ATT_CURRENT_DATE
        var ampm: String = Util.ATT_CURRENT_AMPM
        var section: String =
            Util.ATT_CURRENT_SECTION //(studentno: String, theDate: String, theTime:String , section: String, ): String
        var att = dbhandler.GetIndividualAttendance(studNum, myDate, ampm, section)
        Log.e("studNum", studNum)
        Log.e("myDate", myDate)
        Log.e("ampm", ampm)
        Log.e("sectio ", section)
        Log.e("att ", att)

        if (att == "P") {
            dlgAttendance!!.btnPresentDialog.setBackgroundColor(Color.parseColor("#64B5F6"))
        }

        if (att == "L") {
            dlgAttendance!!.btnLateDialog.setBackgroundColor(Color.parseColor("#69F0AE"))
        }

        if (att == "A") {
            dlgAttendance!!.btnAbsentDialog.setBackgroundColor(Color.parseColor("#FFB74D"))
        }

        if (att == "E") {
            dlgAttendance!!.btnExcuseDialog.setBackgroundColor(Color.parseColor("#BA68C8"))

        }

    }

    fun DefaultColor(dlgAttendance: View?) {
        dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgAttendance!!.btnAbsentDialog.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgAttendance!!.btnLateDialog.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgAttendance!!.btnExcuseDialog.setBackgroundResource(android.R.drawable.btn_default);
    }

    fun ShowAttendandeCount(dlgAttendance: View?) {
        val db3: TableAttendance = TableAttendance(context)
        var absentCount = db3.GetIndividualCouunt(attendanceList[INDEX].studentNo, "ALL", "A")
        dlgAttendance!!.txtAbsentCount.setText("A- " + absentCount.toString())
        var lateCount = db3.GetIndividualCouunt(attendanceList[INDEX].studentNo, "ALL", "L")
        dlgAttendance!!.txtLateCount.setText("L- " + lateCount.toString())
        var excuseCount = db3.GetIndividualCouunt(attendanceList[INDEX].studentNo, "ALL", "E")
        dlgAttendance!!.txtExcuseCount.setText("E- " + excuseCount.toString())

    }


    //    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
    //        super.onActivityResult(requestCode, resultcode, data)
    //        if (requestCode == PICK_FILE && resultcode == Activity.RESULT_OK && data != null) {
    //
    //            val uri: Uri? = data.data
    //            val myFile = File(uri.toString());
    //
    //            val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
    //            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    //            var mLine: String?
    //            var line = bufferedReader.readLine()
    //            attendanceCsvList.clear()
    //            while (line != null) {
    //                var text = line.toString()
    //                Log.e("123456", line)
    //                val list = line.split(",")
    //                val db: DatabaseHandler = DatabaseHandler(context)
    //                val section = txtAttendanceSection.text.toString()
    //                var studentNo = db.GetSubjectStudentNo(list[0], section) // attendanceCsvList
    //                val name = list[1]
    //                val attendanceStatus = list[2]
    //                val task = list[3]
    //                val recitation = list[4]
    //                val myTime = list[5]
    //                if (list[0].contains("-") && list[0].length == 5) {
    //                    studentNo = db.GetSubjectStudentNo(list[0], section)
    //                    attendanceCsvList.add(AttendanceCSVModel(studentNo, name, attendanceStatus, task.toInt(), recitation.toInt(), myTime))
    //                } else if (list[0].length == 4) {
    //                    studentNo = list[0]
    //                    attendanceCsvList.add(AttendanceCSVModel(studentNo, name, attendanceStatus, task.toInt(), recitation.toInt(), myTime))
    //                }
    //
    //                line = bufferedReader.readLine()
    //
    //            }
    //        }
    //
    //
    //        val layoutmanager = LinearLayoutManager(this)
    //        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
    //        listAttendance.layoutManager = layoutmanager
    //        attendanceCsvAdapter = AttendanceCSVAdapter(this, attendanceCsvList)
    //        listAttendance.adapter = attendanceCsvAdapter
    //
    //    }

    fun ShowAttendanceStatus(status: String = "") {
        TXT_STATUS = false
        Log.e("3612", Util.ATT_CURRENT_DATE)
        var date = txtAttendanceDate.text.toString()
        var ampm = btnAttendanceTime.text.toString()
        UpdateListContent(this, date, ampm, status, "ATT_STATUS")
        attendanceAdapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultcode, data)
        if (requestCode == 100) {
            val bitmap: Bitmap = data!!.extras?.get("data") as Bitmap
            dlgAttendance!!.imgStudentAttendance.setImageBitmap(bitmap)
        }

        //        else if (requestCode == AttendanceMain.PICK_FILE && resultcode == Activity.RESULT_OK && data != null) { //            STUD_NAME= attendanceList[INDEX].lastName + ","  + attendanceList[INDEX].firstName//ShowRemark(dlgRecite)
        //            //            SECTION_NAME = "12-PROG1-2023"
        //
        //            val uri: Uri? = data.data
        //            val myFile = File(uri.toString());
        //
        //            val selectedFile = data.data
        //            val path = selectedFile!!.path
        //            Log.e("AAA", uri.toString())
        //            Log.e("AAA", path.toString())
        //            var p = path!!.split(":")
        //            Log.e("AAA", p[1]) //                try {
        //            SECTION_NAME = dbhandler.GetRealSection(Util.ATT_CURRENT_SECTION)
        //            val folderDest = "/storage/emulated/0/PICTURE/" + SECTION_NAME
        //            val fileDest = File(folderDest)
        //            if (!fileDest.exists()) fileDest.mkdir()
        //
        //            /*   val data1 = Environment.getDataDirectory()
        //           Log.e("AAA", data1.toString())*/
        //            val file = File("/storage/emulated/0/", p[1])
        //            val file2 =
        //                File("/storage/emulated/0/Picture/" + SECTION_NAME, STUD_NAME + ".jpg") //                        val sourceImagePath = "/path/to/source/file.jpg" //                        val destinationImagePath = "/path/to/destination/file.jpg"
        //            //                        val source = File(data, sourceImagePath)
        //            //                        val destination = File(sd, destinationImagePath)
        //            /*if (source.exists()) {*/
        //            val src = FileInputStream(file).channel
        //            val dst = FileOutputStream(file2).channel
        //            dst.transferFrom(src, 0, src.size())
        //            src.close()
        //            dst.close() //                        }
        //            //                    }
        //            //                } catch (e: Exception) {
        //            //                }
        //            //               val mSelectedImagePath = getPath(uri);
        //            //                Log.e("AAA", mSelectedImagePath.toString())
        //            //                val f = File("/storage/emulated/0/Picture/Sample2.jpg")
        //            //                if (!f.exists()) {
        //            //                    try {
        //            //                        f.createNewFile()
        //            //                        copyFile(File(data.data?.let { getRealPathFromURI(it) }), f)
        //            //                    } catch (e: IOException) { // TODO Auto-generated catch block
        //            //                        e.printStackTrace()
        //            //                    }
        //            //                }
        //
        //
        //        }

    }


    fun AttendanceViewRecord() {

        val layoutmanager =
            GridLayoutManager(this, 4) // layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listAttendance.layoutManager = layoutmanager
        attendanceAdapter = AttendanceAdapter(this, attendanceList)
        listAttendance.adapter = attendanceAdapter
    }


    fun ScanViewRecord() { //val layoutmanager = LinearLayoutManager(this)
        val layoutmanager =
            GridLayoutManager(this, 2) // layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listAttendance.layoutManager = layoutmanager
        attendanceScanAdapter = AttendanceScanAdapter(this, attendanceScanList)
        listAttendance.adapter = attendanceScanAdapter
    }


    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.captureActivity = CaptureAct::class.java
        barLaucher.launch(options)

    }

    var barLaucher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result ->
            if (result.getContents() != null) {

                val str = result.getContents()
                Log.e("SSS", str)
                val rec = str.split(",").toTypedArray()
                val studNum = rec[0]
                val studName = rec[1]
                val db: DatabaseHandler = DatabaseHandler(context)
                val section = txtAttendanceSection.text.toString()
                val studentNo = db.GetSubjectStudentNo(studNum, section)
                if (studentNo == "NONE") {
                    Util.Msgbox(context, "Student Not Foun")
                    return@registerForActivityResult
                }
                Log.e("sss", studentNo)
                val databaseHandler: DatabaseHandler =
                    DatabaseHandler(context) //                val txt= btnScanOption.text //                var stat = "" //                if (txt=="SCAN FOR ABSENT") { //                    db.UpdatwIndividualAttendanceviaQR("A", studentNo, section, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM) //                 stat = "A" //                } //                else if (txt=="SCAN FOR LATE") { //                    db.UpdatwIndividualAttendanceviaQR("L", studentNo, section, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM) //                    stat = "L" //                } else  if (txt=="SCAN FOR EXCUSE") { //                    db.UpdatwIndividualAttendanceviaQR("E", studentNo, section, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM) //                    stat = "E" //                } //                else  if (txt=="SCAN FOR PRESENT") { //                    db.UpdatwIndividualAttendanceviaQR("P", studentNo, section, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM) //                    stat = "P" //                }else  if (txt=="SCAN FOR TASK") { //                    db.UpdateIndividualTask(studentNo, section, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM, 3) //                    stat = "1" //                } //               else  if (txt=="SCAN FOR RECITATION") { //                    Log.e("Heklo", "Hi") //                    val db2:TableAttendance = TableAttendance(context) //                    db2.UpdateRecitation(1, studentNo, Util.ATT_CURRENT_DATE, Util.ATT_CURRENT_AMPM, section) //                    stat = "3"
                //                }
                //                val length = attendanceScanList.size + 1
                //
                //                UpdateScanListContent(this,length.toString(), studentNo, studName, stat)
                //
                //                attendanceScanAdapter!!.notifyDataSetChanged()
                //                scanCode()
                //            }
            }
        }


    fun ShowDialog(status: String, context: Context, student: StudentModel? = null, position: Int = -1) { //val mydb: DatabaseHandler = DatabaseHandler(context)

        val dlgstudent = LayoutInflater.from(context).inflate(R.layout.attendance_dialog, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgstudent).setTitle("Manage Student")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);
        val arrSection: Array<String> = Util.GetSectionList(context)


        //        dlgstudent.btnSetAttendance.setOnClickListener {
        //            val databaseHandler: TableAttendance = TableAttendance(context)
        //            var grp = AttendanceMain.cboGroup!!.getSelectedItem().toString();
        //            val att_list: List<AttendanceModel>
        //            var txt = AttendanceMain.txtSearch!!.text.toString()
        //
        //            att_list = databaseHandler.GetAttendanceList()
        //
        //
        //            var studentData = dlgstudent.txtData.text.toString().toUpperCase()
        //            studentData = studentData.replace('Ñ', 'N');
        //            var studentDataSplit= studentData.split("---")
        //            Log.e("Attendance", studentData);
        //            for (att in att_list) {
        //                var completeName = att.completeName
        //                val myName: List<String> = completeName.split(",")
        //                var firstNameStatus = studentDataSplit[0].contains(myName[1], ignoreCase = true)
        //                var lastNameStatus = studentDataSplit[0].contains(myName[0], ignoreCase = true)
        //
        //                if (firstNameStatus && lastNameStatus) {
        //                    val databaseHandler: TableAttendance = TableAttendance(context)
        //                    databaseHandler.UpdateStudentAttendance("P", att.studentNo, currentIndividualAttendance)
        //                    Log.e("Found", myName[0]);
        //                }
        //            }
        //
        //            Log.e("XXX", studentDataSplit[1]);
        //            for (att in att_list) {
        //                var completeName = att.completeName
        //                val myName: List<String> = completeName.split(",")
        //                var firstNameStatus = studentDataSplit[1].contains(myName[1], ignoreCase = true)
        //                var lastNameStatus = studentDataSplit[1].contains(myName[0], ignoreCase = true)
        //
        //                if (firstNameStatus && lastNameStatus) {
        //                    val databaseHandler: TableAttendance = TableAttendance(context)
        //                    databaseHandler.UpdateStudentAttendance("L", att.studentNo)
        //                    Log.e("Found", myName[0]);
        //                }
        //                else
        //                    Log.e("not Foundxxx", myName[0]);
        //            }


        dlgstudent.btnCloseAttendance.setOnClickListener {
            ShowAttendanceStatus();
            studentDialog.dismiss() //AttenceCount(context)
        }

        dlgstudent.btnCopyStat.setOnClickListener {
            val stat: String = dlgstudent.btnCopyStat.getText().toString()
            if (stat == "ALL") dlgstudent.btnCopyStat.setText("PRESENT")
            else if (stat == "PRESENT") dlgstudent.btnCopyStat.setText("LATE")
            else if (stat == "LATE") dlgstudent.btnCopyStat.setText("ABSENT")
            else if (stat == "ABSENT") dlgstudent.btnCopyStat.setText("NONE")
            else if (stat == "NONE") dlgstudent.btnCopyStat.setText("ALL")
        }


        dlgstudent.btnCopyAttendance.setOnClickListener {
            val databaseHandler: TableAttendance = TableAttendance(context)
            val att_list: List<AttendanceModel>
            var date = txtAttendanceDate.text.toString()
            var ampm = btnAttendanceTime.text.toString()

            att_list = databaseHandler.GetAttendanceList(date, ampm)


            val stat: String = dlgstudent.btnCopyStat.getText().toString()
            if (stat == "ALL") {
                var completeName = "Attendance for " + Util.ATT_CURRENT_DATE + "\n\n"

                for (att in att_list) {
                    completeName =
                        completeName + att.completeName + "-" + att.attendanceStatus + "\n"
                }
                completeName = "\n\n" + completeName + "P- Present in Online Class " + "\n"
                completeName = completeName + "L- Attendance in GC " + "\n"
                CopyText(completeName)
            } else {
                var letterStat = ""
                if (stat == "PRESENT") letterStat = "P";
                else if (stat == "LATE") letterStat = "L";
                else if (stat == "ABSENT") letterStat = "A";
                else if (stat == "NONE") letterStat = "-";

                var completeName = stat + "(" + Util.ATT_CURRENT_DATE + " )" + "\n\n"
                for (att in att_list) {
                    if (att.attendanceStatus == letterStat) completeName =
                        completeName + att.completeName + "-" + att.attendanceStatus + "\n"
                }
                Log.e("Attedance", completeName);
                CopyText(completeName)
            }

        }

        //        dlgstudent.btnCopyAbsent.setOnClickListener {
        //            val databaseHandler: TableAttendance = TableAttendance(context)
        //            val att_list: List<AttendanceModel>
        //            att_list = databaseHandler.GetAttendanceList()
        //
        //            var completeName = "Absent  (" + Util.ATT_CURRENT_DATE + " )" + "\n\n"
        //            for (att in att_list) {
        //                if (att.attendanceStatus=="A")
        //                    completeName = completeName + att.completeName + "-" + att.attendanceStatus + "\n"
        //            }
        //            Log.e("Attedance", completeName);
        //            CopyText(completeName)
        //        }
        //
        //        dlgstudent.btnCopyPresent.setOnClickListener {
        //            val databaseHandler: TableAttendance = TableAttendance(context)
        //            val att_list: List<AttendanceModel>
        //            att_list = databaseHandler.GetAttendanceList()
        //
        //            var completeName = "Prsent  (" + Util.ATT_CURRENT_DATE + " )" + "\n\n"
        //            for (att in att_list) {
        //                if (att.attendanceStatus=="P")
        //                    completeName = completeName + att.completeName + "-" + att.attendanceStatus + "\n"
        //            }
        //            Log.e("Attedance", completeName);
        //            CopyText(completeName)
        //        }
        //
        //        dlgstudent.btnCopyLate.setOnClickListener {
        //            val databaseHandler: TableAttendance = TableAttendance(context)
        //            val att_list: List<AttendanceModel>
        //            att_list = databaseHandler.GetAttendanceList()
        //
        //            var completeName = "Attendance in Group Chat  (" + Util.ATT_CURRENT_DATE + " )" + "\n\n"
        //            for (att in att_list) {
        //                if (att.attendanceStatus=="A")
        //                    completeName = completeName + att.completeName + "-" + att.attendanceStatus + "\n"
        //            }
        //            Log.e("Attedance", completeName);
        //            CopyText(completeName)
        //        }


    }

    fun CopyText(copyString: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", copyString)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }
}


fun DatabaseHandler.UpdatwIndividualAttendanceviaQR(attStatus: String, studentNo: String, sectionCode: String, myDate: String, ampm: String) {
    val sql = """
              update tbattendance 
              set AttendanceStatus = '$attStatus',
              Remark = '-'
              where SchedDate ='$myDate' 
              and SchedTime='$ampm' 
              and SectionCode ='$sectionCode'
              and StudentNumber ='$studentNo'
              """
    val db = this.writableDatabase
    db.execSQL(sql)

}


fun DatabaseHandler.UpdateIndividualTask(studentNo: String, sectionCode: String, myDate: String, ampm: String, score: Int = 0) {
    val sql = """
              update tbattendance 
              set TaskPoints = $score
              where SchedDate ='$myDate' 
              and SchedTime='$ampm' 
              and SectionCode ='$sectionCode'
              and StudentNumber ='$studentNo'
              """
    val db = this.writableDatabase
    Log.e("TAK", sql)
    db.execSQL(sql)

}


@SuppressLint("Range")
fun DatabaseHandler.GetIndividualAttendance(monthName: String, studentNo: String): ArrayList<AttendanceModel> { //    SchedTime	SchedID	SchedDate	SectionCode	Remark	SchedTerm	Day
    //    SchedDate	SchedTime	SectionCode	StudentNumber	Remark	AttendanceStatus	RandomNumber	RecitationPoints	TaskPoints
var sql = ""
    if (monthName == "FIRST" || monthName == "SECOND"){
         sql = """
              select * from tbattendance_query
              where  StudentNumber = '$studentNo'
              and   SchedTerm = '$monthName'
              order by SchedDate
              """
    }
    else {
         sql = """
              select * from tbattendance
              where  StudentNumber = '$studentNo'
              order by SchedDate
              """
    }
    var cursor = SQLSelect(sql)

    val attendanceIndividual: ArrayList<AttendanceModel> = ArrayList<AttendanceModel>()
    if (cursor!!.moveToFirst()) {

        do {
            var att: AttendanceModel = AttendanceModel()
            att.myDate = cursor!!.getString(cursor!!.getColumnIndex("SchedDate"))
            att.attendanceStatus = cursor!!.getString(cursor!!.getColumnIndex("AttendanceStatus"))
            att.remark = cursor!!.getString(cursor!!.getColumnIndex("Remark"))

            attendanceIndividual.add(att)
        } while (cursor.moveToNext())
    }
    return attendanceIndividual
}

fun DatabaseHandler.GetIndividualAttendance(studentno: String, theDate: String, theTime: String, section: String): String {
    var sql = """
                          select * from tbattendance
                          where  StudentNumber='$studentno'
                          and SectionCode='$section'
                          and SchedDate='$theDate'
                          and SchedTime='$theTime'
                          """

    Log.e("sql", sql)    //	SectionCode		Remark	AttendanceStatus	RandomNumber	RecitationPoints	TaskPoints
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    val columnNames: Array<String> = cursor.getColumnNames()
    Log.e("AAA", Arrays.toString(columnNames))
    if (cursor.count == 0) {
        Log.e("REMMM", "-")
        return "-"
    } else {
        cursor.moveToFirst()
        val rem = cursor.getString(cursor.getColumnIndex("AttendanceStatus"))
        Log.e("REMMM", rem)
        return rem
    }
}


fun DatabaseHandler.GetRealSection(section: String): String {
    var sql = """
                          select * from tbsection
                          where  SectionName='$section'
                          """

    Log.e("sql", sql)
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    val columnNames: Array<String> = cursor.getColumnNames()
    Log.e("AAA", Arrays.toString(columnNames))
    Log.e("AAA", cursor.count.toString())
    cursor.moveToFirst()
    val realSection = cursor.getString(cursor.getColumnIndex("RealSectionName"))
    Log.e("REMMM", realSection)
    return realSection
}












