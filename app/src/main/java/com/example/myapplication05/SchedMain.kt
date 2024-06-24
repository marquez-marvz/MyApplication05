package com.example.myapplication05

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.answer_main.*
import kotlinx.android.synthetic.main.enrolle_main.*
import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_folder_shared.view.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class SchedMain : AppCompatActivity() {
    val db: TableAttendance = TableAttendance(this);
    val db1: TableActivity = TableActivity(this);


    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    val myContext: Context = this;

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sched_main)
        SetSpinnerAdapter()

        Util.ATT_FLAG = "SCHEDULE"
        var mycontext = this; //mDisplayDate = findViewById(R.id.txtdate) as TextView
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionSched.setSelection(index)

        val today = LocalDate.now()
        val monthNum = today.monthValue
//        vartxtDate = findViewById(R.id.txtDate) as TextView

        varcboMonth = findViewById(R.id.cboMonthName) as Spinner
        btnScheduleTerm.setText(Util.GetCurrentGradingPeriod(this))



        Log.e("5670", monthNum.toString())
        cboMonthName.setSelection(monthNum - 1)
        SetDateSpinnerAdapter()

        SetDate()
        SetUpListViewSchedAdapter()
        UpdateListContentDate(this)

        TableAttendance
        var globalSearch = ""

        globalCategory = "MONTH"
        globalMonth = GetMonth()
        val db2: DatabaseHandler = DatabaseHandler(this)
        db2.SF2Data()


//        txtDate.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View) {
//                val cal = Calendar.getInstance()
//                val year = cal.get(Calendar.YEAR)
//                val month = cal.get(Calendar.MONTH)
//                val day = cal.get(Calendar.DAY_OF_MONTH)
//                val dialog =
//                    DatePickerDialog(mycontext, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day) //dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.show()
//            }
//        })

        mDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
                SetDate(month, day)
                adapter!!.notifyDataSetChanged()
            }
        }


        btnAmPm.setOnClickListener {
            if (btnAmPm.getText() == "AM") btnAmPm.setText("PM")
            else btnAmPm.setText("AM")
        }

        //1400
        cboMonthName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { //                val section = cboSectionSearch.getSelectedItem().toString();

                //                UpdateListContentDate(this, i + 1)
                //                btnMonthName.text = monthName[i + 1]
                //                adapterDate!!.notifyDataSetChanged()
                Log.e("444", "HelloWorld ")
                UpdateListContent(mycontext, "MONTH", "FEB");
                adapter!!.notifyDataSetChanged()
                SetDateSpinnerAdapter()


            }

        }



//




        btnExportAttendance.setOnClickListener {

            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgquiz).setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {
                val db2: DatabaseHandler = DatabaseHandler(this)
                val db1: Grades = Grades(this)
                val student: List<EnrolleModel>
                var section = currentSection
                val school = db1.GetSchool(section)
                Log.e("school", school)

                if (school == "DEPED") {
                    student = db2.GetEnrolleList("GENDER_ORDER", section)
                } else {
                    student = db2.GetEnrolleList("LAST_ORDER", section)
                }
                var studentData = ""
                for (e in student) {
                    var complete = e.lastname + "," + e.firstname
                    studentData = studentData + e.studentno + "\t" + complete + "\n"
                }

                val loading = ProgressDialog.show(this, "ATTENDANCE", "Please wait")
                var url =
                    "https://script.google.com/macros/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec"

                val stringRequest: StringRequest = object :

                    StringRequest(Method.POST, url, Response.Listener { response ->
                        loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()

                    }, Response.ErrorListener { }) {
                    override fun getParams(): Map<String, String> {
                        val parmas: MutableMap<String, String> = HashMap()
                        parmas["action"] = "ExportAttendance"
                        parmas["data"] = studentData
                        parmas["sheetName"] = currentSection
                        return parmas
                    }
                }

                val socketTimeOut = 50000 // u can change this .. here it is 50 seconds


                val retryPolicy: RetryPolicy =
                    DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                stringRequest.retryPolicy = retryPolicy

                val queue = Volley.newRequestQueue(this)

                queue.add(stringRequest)
            }
            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }
        }

        //1400

        btnAdd.setOnClickListener {
            val myDate = cboMonthDate.getSelectedItem().toString();
            val ampm = btnAmPm.getText().toString();
            val section = cboSectionSched.getSelectedItem().toString();
            val id = db1.GetNewSchedCode(section)
            AddNewSched(this, myDate, ampm, section, id)
        }

        //1400
        btnScheduleTerm.setOnClickListener {
            var globalCategory = ""
            var txt = btnScheduleTerm.text.toString()
            globalCategory = "FIRST"
            if (txt == "FIRST") {
                btnScheduleTerm.setText("SECOND")
                globalCategory = "SECOND"
            } else if (txt == "SECOND") {
                btnScheduleTerm.setText("ALL")
                globalCategory = "ALL"

            } else if (txt == "ALL") {
                btnScheduleTerm.setText("FIRST")
                globalCategory = "FIRST"
            }
            UpdateListContent(this, globalCategory, globalMonth);
            adapter!!.notifyDataSetChanged()
        }

        btnImport.setOnClickListener {

            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgquiz).setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {
                val loading = ProgressDialog.show(this, "", "Please wait")
                var url =
                    "https://script.google.com/a/macros/deped.gov.ph/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec";
                val stringRequest: StringRequest = @RequiresApi(Build.VERSION_CODES.O) object :
                    StringRequest(Method.POST, url, Response.Listener { response ->
                        loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()

                        var obj = JSONArray(response);

                        var i = 0;
                        var ctr = 0;
                        var x = 0;
                        var myScore = 0
                        var status = ""
                        val arr = ArrayList<String>()
                        val ppp = obj.getJSONObject(0)
                        val iterator: Iterator<String> = ppp.keys();

                        while (iterator.hasNext()) {
                            var date = iterator.next()
                            val section = cboSectionSched.getSelectedItem().toString();
                            val id = db1.GetNewSchedCode(section)
                            arr.add(date)
                            Log.e("SSS", ctr.toString() + "   " + date)

                            val db: DatabaseHandler = DatabaseHandler(this)

                            if (ctr > 1) { //  AddNewSched(context ,  date, "PM", section, id)
                            }
                            ctr++


                        }

                        //    fun UpdateStudentAttendance(attStatus: String, studentNo: String = "", taskpoint: Int, recite: Int, remark: String = "-") {


                        while (i < obj.length()) {


                            val jsonObject = obj.getJSONObject(i)
                            var studNumber = jsonObject.getString(arr[0])
                            var name = jsonObject.getString(arr[1])
                            var m = 2
                            Log.e("PPP", studNumber + "   " + name + "  ")
                            Log.e("PPP", arr.size.toString() + "   " + name + "  ")

                            while (m < arr.size) {
                                Util.ATT_CURRENT_DATE = arr[m]
                                Util.ATT_CURRENT_AMPM = "PM"
                                Util.ATT_CURRENT_SECTION =
                                    cboSectionSched.getSelectedItem().toString();
                                var day2 = ""
                                var day1 = jsonObject.getString(arr[m])
                                if (day1.toString() == "true") day2 = "P"
                                else if (day1 == "false") day2 = "A"
                                else day2 = day1
                                Log.e("PPP", studNumber + "   " + name + "  " + day1 + "   " + Util.ATT_CURRENT_DATE + "   " + day2) //                            val dbAttendance:TableAttendance = TableAttendance(this);
                                val dbAttendance: TableAttendance = TableAttendance(this)
                                dbAttendance.UpdateStudentAttendance(day2, studNumber, "", "", "")
                                m++;
                            }
                            i++;
                        } //w

                    }, Response.ErrorListener { }) {
                    override fun getParams(): Map<String, String> {
                        val parmas: MutableMap<String, String> = HashMap()
                        parmas["action"] = "ImportAttendance"
                        parmas["section"] = cboSectionSched.getSelectedItem().toString();
                        return parmas
                    }
                }

                val socketTimeOut = 50000 // u can change this .. here it is 50 seconds
                val retryPolicy: RetryPolicy =
                    DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                stringRequest.retryPolicy = retryPolicy
                val queue = Volley.newRequestQueue(this)
                queue.add(stringRequest)

            }
            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }

        }

//        cboSectionSched.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val section = cboSectionSched.getSelectedItem().toString();
//                Util.CURRENT_SECTION = section;
//                val myDate = txtDate.text.toString()
//                UpdateListContent(mycontext, "MONTH", GetMonth());
//                adapter!!.notifyDataSetChanged()
//                val mydb: DatabaseHandler = DatabaseHandler(myContext)
//                mydb.SetCurrentSection(section)
//            }
//        }
    }

    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionSched.setAdapter(sectionAdapter);
    }

    //1400
    @RequiresApi(Build.VERSION_CODES.O)
    private fun SetDateSpinnerAdapter() {
        val arrSection: Array<String> = GetDayList()
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboMonthDate.setAdapter(sectionAdapter);

        val today = LocalDate.now()
        val monthNum = today.monthValue
        var monthName = cboMonthName!!.getSelectedItem().toString();
        Log.e("7811", monthName)
        val nameMonth =
            arrayOf<String>("", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        if (nameMonth[monthNum] == monthName) {
            val cal = Calendar.getInstance()
            val myMonth = cal.get(Calendar.MONTH)
            val myDay = cal.get(Calendar.DAY_OF_MONTH)
            cboMonthDate.setSelection(myDay)
        } else {
            cboMonthDate.setSelection(1)
        } //

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun GetDayList(): Array<String> {
        val today = LocalDate.now()
        val monthNum = today.monthValue
        var monthName = cboMonthName!!.getSelectedItem().toString();
        Log.e("7890", monthName)
        val numberOfDays = arrayOf<Int>(0, 31, 29, 31, 30, 31, 30, 31, 30, 31, 30, 31)

        var monthday = numberOfDays[monthNum]
        var i = 1;

        var sectionArray = Array(monthday + 1) { "" }
        while (i <= monthday) {
            sectionArray[i] = monthName + " " + Util.ZeroPad(i, 2)
            i++
        }

        return sectionArray
    }


    companion object {
        var adapter: ScheduleAdapter? = null;
        var adapterDate: SchedDateAdapter? = null;
        var vartxtDate: TextView? = null;
        var varbtnmonthName: Button? = null;
        var varcboMonth: Spinner? = null;
        val monthName = arrayOf<String>("", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL")

        var scheduleList = arrayListOf<ScheduleModel>()
        var scheduleDate = arrayListOf<ScheduleDateModel>()
        var globalCategory = ""
        var globalMonth = ""

        fun UpdateListContent(context: Context, category: String = "MONTH", monthName: String = "") {
            var section: String = Util.CURRENT_SECTION
            var myDate: String = Util.CURRENT_DATE
            val db1: TableAttendance = TableAttendance(context);
            var monthName = varcboMonth!!.getSelectedItem().toString();
            val schedlist: List<ScheduleModel>

            Log.e("4598", monthName)
            schedlist = db1.GetScheduleList(section, category, monthName)

            scheduleList.clear()

            for (sched in schedlist) {
                scheduleList.add(ScheduleModel(sched.ampm, sched.myDate, sched.sectioncode, sched.renark, sched.term, sched.schedId, sched.day))
            }
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun UpdateListContentDate(context: Context, monthNumbber: Int = 0) {

            //            val numberOfDays = arrayOf<Int>(0, 31, 29, 31, 30, 31, 30, 31, 30, 31, 30, 31)
            //            var mmm = Util.ZeroPad(month, 2)
            //            val cal = Calendar.getInstance()
            //            var month = 0
            //            if (monthNumbber == 0) {
            //                val today = LocalDate.now()
            //                month = today.monthValue
            //            } else {
            //                month = monthNumbber
            //            }
            //            Log.e("2342", month.toString())
            //            Log.e("2342", monthNumbber.toString())
            //            val year: Int = cal.get(Calendar.YEAR)
            //
            //            val ld = LocalDate.parse("2020-07-07")
            //
            //            var x = 1;
            //            scheduleDate.clear()
            //            while (x <= numberOfDays[month]) {
            //                var mmm = Util.ZeroPad(month, 2)
            //                var ddd = Util.ZeroPad(x, 2)
            //                var dte = year.toString() + "-" + mmm + "-" + ddd
            //                val ld = LocalDate.parse(dte)
            //                val nnn = monthName[month] + " " + ddd
            //
            //                scheduleDate.add(ScheduleDateModel(nnn, ld.dayOfWeek.toString()))
            //                x++;
            //
            //            }
            //
            //
            //            //            scheduleDate.clear()
            //
            //            for (sched in scheduleDate) {
            //            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun AddNewSched(context: Context, myDate: String, ampm: String, section: String, id: String) {
            Log.e("myDate", myDate)
            var sss = myDate.split(" ")
            var month = ""
            var monthCode = ""
            if (sss[0] == "JAN") {
                monthCode = "08"
                month = "01"

            }
            if (sss[0] == "FEB") {
                monthCode = "09"
                month = "02"
            }
            if (sss[0] == "MAR") {
                monthCode = "10"
                month = "03"
            }
            if (sss[0] == "APR") {
                monthCode = "11"
                month = "04"
            }
            if (sss[0] == "MAY") {
                monthCode = "12"
                month = "05"
            }
            if (sss[0] == "JUN") {
                monthCode = "01"
                month = "06"
            }
            if (sss[0] == "JUL") {
                monthCode = "02"
                month = "07"
            }
            if (sss[0] == "AUG") {
                monthCode = "03"
                month = "08"
            }
            if (sss[0] == "SEP") {
                monthCode = "04"
                month = "09"
            }
            if (sss[0] == "OCT") {
                monthCode = "05"
                month = "10"
            }
            if (sss[0] == "NOV") {
                monthCode = "06"
                month = "11"
            }
            if (sss[0] == "DEC") {
                monthCode = "07"
                month = "12"
            }

            var schedDay = ""
            var schedID = ""
            try {
                val cal = Calendar.getInstance()
                val formatter = SimpleDateFormat("MM/dd/yyyy")
                Log.e("DATEEEE", sss[1])
                val year: Int = cal.get(Calendar.YEAR)
                Log.e("5632", year.toString())
                var p = formatter.parse(month + "/" + sss[1] + "/" + year.toString())

                cal.setTime(p);
                val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
                Log.e("56325", dayOfWeek.toString())


                val f= SimpleDateFormat("u")
                val strDate: String = f.format(p)
                Log.e("56300", strDate)


                val day = arrayOf<String>("", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
                var schedDay = day[dayOfWeek]
                var schedID = monthCode + sss[1]
                Log.e("56325", schedID.toString())


                val db: TableAttendance = TableAttendance(context);
                val db1: TableActivity = TableActivity(context);
                val db2: DatabaseHandler = DatabaseHandler(context);
                if (db2.CheckAttendance(ampm, myDate, section) == 0) {
                    var status = db.ManageSched("ADD", ampm, myDate, section, schedID, schedDay)
                    db.AddStudetAttendance(myDate, ampm, section)
                    var monthName = varcboMonth!!.getSelectedItem().toString();
                    UpdateListContent(context, globalCategory, monthName);
                    adapter!!.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                var schedDay = ""
                var schedID = myDate
            }


        }
    }

    fun SetUpListViewSchedAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listViewSched.layoutManager = layoutmanager
        adapter = ScheduleAdapter(this, scheduleList)
        listViewSched.adapter = adapter
    }


    //    fun showPopMenuSchedule(v: View) { //  showPopMenu
    //        Log.e("433", "hi")
    //        val popup = PopupMenu(this, v)
    //        popup.setOnMenuItemClickListener(this)
    //        popup.inflate(R.menu.menu_month)
    //        popup.show()
    //    }

    //    override fun onMenuItemClick(item: MenuItem): Boolean {
    //        val selected = item.toString()
    //        if (selected == "Nov") {
    //            btnShow.setText("NOV")
    //            UpdateListContent(this, "MONTH", "NOV");
    //            adapter!!.notifyDataSetChanged()
    //            globalCategory = "MONTH"
    //            globalMonth = "NOV"
    //        }
    //
    //        //        } else if (selected == "Show Rubric") {
    //        //            Util.ACT_OPEN_FOLDER=false
    //        //            Util.ACT_RUBRIC = true
    //        //            ScoreMain.scoreAdapter!!.notifyDataSetChanged()
    //        //
    //        //        } else if (selected == "Hide All") {
    //        //            Util.ACT_OPEN_FOLDER=false
    //        //            Util.ACT_RUBRIC = false
    //        //            ScoreMain.scoreAdapter!!.notifyDataSetChanged()
    //
    //        return true
    //    }


    fun SetDate(month: Int = 0, day: Int = 0) {
        var myMonth: Int
        var myDay: Int
        val cal = Calendar.getInstance()
        if (month == 0 && day == 0) {
            myMonth = cal.get(Calendar.MONTH)
            myDay = cal.get(Calendar.DAY_OF_MONTH)

        } else {
            myMonth = month
            myDay = day
        }
        var ampm = cal.get(Calendar.AM_PM)
        val monthName =
            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC") //  Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year)
        val date = monthName[myMonth] + " " + Util.ZeroPad(myDay, 2)


        btnAmPm
        if (ampm == 1) btnAmPm.setText("PM")
        else btnAmPm.setText("AM")

        Util.CURRENT_DATE = cboMonthDate.getSelectedItem().toString()
        Util.CURRENT_SECTION = cboSectionSched.getSelectedItem().toString();
        Log.e("OKOK", "123456")
        UpdateListContent(this, "MONTH", monthName[myMonth])

    }

    fun GetMonth(): String {
        var myMonth: Int
        val cal = Calendar.getInstance()

        myMonth = cal.get(Calendar.MONTH)


        val monthName =
            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

        return monthName[myMonth]

    }


}


fun DatabaseHandler.CheckAttendance(time: String, date: String, section: String): Int {

    var sql = """
            SELECT * from TBSCHED 
            WHERE SchedTime ='$time'  
            AND SchedDate ='$date'  
            AND SectionCode ='$section'  
    """.trimIndent()
    Log.e("SQLSS", sql)

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    return cursor.count

}


fun DatabaseHandler.SF2Data() {
    var month = "JAN"
    var sql = """
            SELECT * from TBSCHED 
            WHERE SchedDate  like '$month%'  
            AND sectioncode ='ADVISER' 
    """.trimIndent()
    var cursor = SQLSelect(sql, 340)
    Log.e("HHH", cursor!!.count.toString())
    var daycount = cursor!!.count

    sql = """
            SELECT * from TBATTENDANCE_query 
            WHERE SchedDate  like '$month%'  
            AND AttendanceStatus ='A' 
            AND sectioncode ='ADVISER' 
            AND gender ='MALE' 
    """.trimIndent()
    var cursor1 = SQLSelect(sql, 341)
    Log.e("HHH", cursor1!!.count.toString())
    var absentMale = cursor1!!.count

    sql = """
            SELECT * from TBATTENDANCE_query 
            WHERE SchedDate  like '$month%'  
            AND AttendanceStatus ='A' 
            AND sectioncode ='ADVISER' 
            AND gender ='FEMALE' 
    """.trimIndent()
    var cursor2 = SQLSelect(sql, 341)
    Log.e("HHH", cursor2!!.count.toString())
    var absentFemale = cursor2!!.count

    sql = """
            SELECT * from TBSTUDENT_query
            WHERE section ='ADVISER' 
            AND gender ='MALE' 
    """.trimIndent()
    var cursor3 = SQLSelect(sql, 341)
    Log.e("HHH", cursor3!!.count.toString())
    var enrollMale = cursor3!!.count

    sql = """
            SELECT * from TBSTUDENT_query
            WHERE section ='ADVISER' 
            AND gender ='FEMALE' 
    """.trimIndent()
    var cursor4 = SQLSelect(sql, 341)
    Log.e("HHH", cursor4!!.count.toString())
    var enrollFemale = cursor4!!.count

    var averageMale = (daycount * enrollMale - absentMale).toDouble() / daycount
    var averageFemale = (daycount * enrollFemale - absentFemale).toDouble() / daycount
    var averageSum = averageFemale + averageMale

    Log.e("AVGM", averageMale.toString())
    Log.e("AVGF", averageFemale.toString())
    Log.e("AVGF", averageSum.toString())


    var percentageMale = averageMale / enrollMale * 100
    var percentageFemale = averageFemale / enrollFemale * 100
    var percentageSum = (percentageMale + percentageFemale) / 2


    Log.e("pM", percentageMale.toString())
    Log.e("pf", percentageFemale.toString())
    Log.e("ps", percentageSum.toString())
}
