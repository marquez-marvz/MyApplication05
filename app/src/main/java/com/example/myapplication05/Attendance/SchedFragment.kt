///*
//package com.example.myapplication05
//
//import android.app.DatePickerDialog
//import android.app.ProgressDialog
//import android.content.Context
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.DatePicker
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.volley.DefaultRetryPolicy
//import com.android.volley.Response
//import com.android.volley.RetryPolicy
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.example.myapplication05.R
//import kotlinx.android.synthetic.main.att_sched.view.*
//import kotlinx.android.synthetic.main.sched_main.*
//import kotlinx.android.synthetic.main.util_confirm.view.*
//import org.json.JSONArray
//import java.text.SimpleDateFormat
//import java.util.*
//
//class SchedFragment : Fragment() {
//    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
//        viewSched=  inflater.inflate(R.layout.att_sched, container, false)
//        containerGlobal = container
//        var context = containerGlobal!!.context
//        SetSpinnerAdapter()
//        val db: TableAttendance = TableAttendance(context);
//        //mDisplayDate = findViewById(R.id.txtdate) as TextView
//        var currentSection = db.GetCurrentSection();
//        Util.ATT_CURRENT_DATE = "NOV 21"
//        Util.ATT_CURRENT_SECTION = currentSection
//        Util.ATT_CURRENT_AMPM="AM"
//
//
//        var index = Util.GetSectionIndex(currentSection,  context)
//        viewSched!!.cboSectionSched.setSelection(index)
//        SetDate()
//        ViewRecord()
//        TableAttendance
//
//        viewSched!!.txtDate.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View) {
//                val cal = Calendar.getInstance()
//                val year = cal.get(Calendar.YEAR)
//                val month = cal.get(Calendar.MONTH)
//                val day = cal.get(Calendar.DAY_OF_MONTH)
//                val dialog =
//                    DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day) //dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.show()
//            }
//        })
//
//        mDateSetListener = object : DatePickerDialog.OnDateSetListener {
//            override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
//                SetDate(month, day)
//                adapter!!.notifyDataSetChanged()
//            }
//        }
//
//
//        viewSched!!.btnAmPm.setOnClickListener {
//            if (viewSched!!.btnAmPm.getText() == "AM") {
//                viewSched!!.btnAmPm.setText("PM")
//            }
//            else {
//                viewSched!!.btnAmPm.setText("AM")
//            }
//        }
//
//
//
//        viewSched!!.btnExportAttendance.setOnClickListener {
//
//            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
//            val mBuilder =
//                AlertDialog.Builder(context).setView(dlgquiz).setTitle("Do you want to import answer?")
//            val inputDialog = mBuilder.show()
//            inputDialog.setCanceledOnTouchOutside(false); //
//
//            dlgquiz.btnYes.setOnClickListener {
//                val db2: DatabaseHandler = DatabaseHandler(context)
//                val db1: Grades = Grades(context)
//                val student: List<EnrolleModel>
//                var section = currentSection
//                val school = db1.GetSchool(section)
//                Log.e("school", school)
//
//                if (school == "DEPED") {
//                    student = db2.GetEnrolleList("GENDER_ORDER", section)
//                } else {
//                    student = db2.GetEnrolleList("LAST_ORDER", section)
//                }
//                var studentData = ""
//                for (e in student) {
//                    var complete = e.lastname + "," + e.firstname
//                    studentData = studentData + e.studentno + "\t" + complete + "\n"
//                }
//
//                val loading = ProgressDialog.show(context, "ATTENDANCE", "Please wait")
//                var url =
//                    "https://script.google.com/macros/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec"
//
//                val stringRequest: StringRequest = object :
//
//                    StringRequest(Method.POST, url, Response.Listener { response ->
//                        loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
//
//                    }, Response.ErrorListener { }) {
//                    override fun getParams(): Map<String, String> {
//                        val parmas: MutableMap<String, String> = HashMap()
//                        parmas["action"] = "ExportAttendance"
//                        parmas["data"] = studentData
//                        parmas["sheetName"] = currentSection
//                        return parmas
//                    }
//                }
//
//                val socketTimeOut = 50000 // u can change this .. here it is 50 seconds
//
//
//                val retryPolicy: RetryPolicy =
//                    DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//                stringRequest.retryPolicy = retryPolicy
//
//                val queue = Volley.newRequestQueue(context)
//
//                queue.add(stringRequest)
//            }
//            dlgquiz.btnNo.setOnClickListener {
//                inputDialog.dismiss()
//            }
//        }
//
//        viewSched!!.btnAdd.setOnClickListener {
//            val myDate = viewSched!!.txtDate.text.toString()
//            val ampm = viewSched!!.btnAmPm.getText().toString();
//            val section = viewSched!!.cboSectionSched.getSelectedItem().toString();
//            val db1: TableActivity = TableActivity(context);
//            val id = db1.GetNewSchedCode(section)
//            AddNewSched(myDate, ampm, section, id)
//        }
//
////        viewSched!!.btnShow.setOnClickListener {
////            var txt = btnShow.text
////            if (txt == "MONTH") btnShow.text = "FIRST"
////            else if (txt == "FIRST") btnShow.text = "SECOND"
////            else if (txt == "SECOND") btnShow.text = "ALL"
////            else if (txt == "ALL") btnShow.text = "MONTH"
////
////
////            UpdateListContent(context, btnShow.text.toString(), GetMonth());
////            adapter!!.notifyDataSetChanged()
////        }
//
//        viewSched!!.btnImport.setOnClickListener {
//
//            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
//            val mBuilder =
//                AlertDialog.Builder(context).setView(dlgquiz).setTitle("Do you want to import answer?")
//            val inputDialog = mBuilder.show()
//            inputDialog.setCanceledOnTouchOutside(false); //
//
//            dlgquiz.btnYes.setOnClickListener {
//                val loading = ProgressDialog.show(context, "", "Please wait")
//                var url =
//                    "https://script.google.com/a/macros/deped.gov.ph/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec";
//                val stringRequest: StringRequest = @RequiresApi(Build.VERSION_CODES.O) object :
//                    StringRequest(Method.POST, url, Response.Listener { response ->
//                        loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
//
//                        var obj = JSONArray(response);
//
//                        var i = 0;
//                        var ctr = 0;
//                        var x = 0;
//                        var myScore = 0
//                        var status = ""
//                        val arr = ArrayList<String>()
//                        val ppp = obj.getJSONObject(0)
//                        val iterator: Iterator<String> = ppp.keys();
//
//                        while (iterator.hasNext()) {
//                            var date = iterator.next()
//                            val section = cboSectionSched.getSelectedItem().toString();
//                            val db1: TableActivity = TableActivity(context);
//                            val id = db1.GetNewSchedCode(section)
//                            arr.add(date)
//                            Log.e("SSS", ctr.toString() + "   " + date)
//
//                            val db: DatabaseHandler = DatabaseHandler(context)
//
//                            if (ctr > 1) {
//                                AddNewSched(date, "PM", section, id)
//                            }
//                            ctr++
//
//
//                        }
//
//                        //    fun UpdateStudentAttendance(attStatus: String, studentNo: String = "", taskpoint: Int, recite: Int, remark: String = "-") {
//
//
//                        while (i < obj.length()) {
//
//
//                            val jsonObject = obj.getJSONObject(i)
//                            var studNumber = jsonObject.getString(arr[0])
//                            var name = jsonObject.getString(arr[1])
//                            var m = 2
//                            Log.e("PPP", studNumber + "   " + name + "  ")
//                            Log.e("PPP", arr.size.toString() + "   " + name + "  ")
//
//                            while (m < arr.size) {
//                                Util.ATT_CURRENT_DATE = arr[m]
//                                Util.ATT_CURRENT_AMPM = "PM"
//                                Util.ATT_CURRENT_SECTION =
//                                    cboSectionSched.getSelectedItem().toString();
//                                var day2 = ""
//                                var day1 = jsonObject.getString(arr[m])
//                                if (day1.toString() == "true") day2 = "P"
//                                else if (day1 == "false") day2 = "A"
//                                else day2 = day1
//                                Log.e("PPP", studNumber + "   " + name + "  " + day1 + "   " + Util.ATT_CURRENT_DATE + "   " + day2) //                            val dbAttendance:TableAttendance = TableAttendance(this);
//                                val dbAttendance: TableAttendance = TableAttendance(context)
//                                dbAttendance.UpdateStudentAttendance(day2, studNumber, "", "", "")
//                                m++;
//                            }
//                            i++;
//                        } //w
//
//                    }, Response.ErrorListener { }) {
//                    override fun getParams(): Map<String, String> {
//                        val parmas: MutableMap<String, String> = HashMap()
//                        parmas["action"] = "ImportAttendance"
//                        parmas["section"] = cboSectionSched.getSelectedItem().toString();
//                        return parmas
//                    }
//                }
//
//                val socketTimeOut = 50000 // u can change this .. here it is 50 seconds
//                val retryPolicy: RetryPolicy =
//                    DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//                stringRequest.retryPolicy = retryPolicy
//                val queue = Volley.newRequestQueue(context)
//                queue.add(stringRequest)
//
//            }
//            dlgquiz.btnNo.setOnClickListener {
//                inputDialog.dismiss()
//            }
//
//        }
//
//        viewSched!!.cboSectionSched.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val section = cboSectionSched.getSelectedItem().toString();
//                Util.CURRENT_SECTION = section;
//                val myDate = txtDate.text.toString()
//                UpdateListContent(context, "MONTH", GetMonth());
//                adapter!!.notifyDataSetChanged()
//                val mydb: DatabaseHandler = DatabaseHandler(context)
//                mydb.SetCurrentSection(section)
//            }
//        }
//        return viewSched
//    }
//
//    companion object {
//        var adapter: SchedNewAdapter? = null;
//        var scheduleList = arrayListOf<ScheduleModel>()
//        var containerGlobal: ViewGroup? = null
//        var viewSched: View? = null
//        fun UpdateListContent(context: Context, category: String = "MONTH", monthName: String = "") {
//            var section: String = Util.CURRENT_SECTION
//            var myDate: String = Util.CURRENT_DATE
//            val db1: TableAttendance = TableAttendance(context);
//            val schedlist: List<ScheduleModel>
//            Log.e("@@@", "123456789")
//            schedlist = db1.GetScheduleList(section, category, monthName)
//
//            scheduleList.clear()
//
//            for (sched in schedlist) {
//                scheduleList.add(ScheduleModel(sched.ampm, sched.myDate, sched.sectioncode, sched.renark, sched.term, sched.schedId, sched.day))
//            }
//        }
//    }//companion
//
//    private fun SetSpinnerAdapter() {
//        var context = containerGlobal!!.context
//
//        val arrSection: Array<String> = Util.GetSectionList(context)
//        var sectionAdapter: ArrayAdapter<String> =
//            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
//        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        viewSched!!.cboSectionSched.setAdapter(sectionAdapter);
//    }
//
//    fun SetDate(month: Int = 0, day: Int = 0) {
//        var context = containerGlobal!!.context
//        var myMonth: Int
//        var myDay: Int
//        val cal = Calendar.getInstance()
//        if (month == 0 && day == 0) {
//            myMonth = cal.get(Calendar.MONTH)
//            myDay = cal.get(Calendar.DAY_OF_MONTH)
//
//        } else {
//            myMonth = month
//            myDay = day
//        }
//        var ampm = cal.get(Calendar.AM_PM)
//        val monthName =
//            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC") //  Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year)
//        val date = monthName[myMonth] + " " + Util.ZeroPad(myDay, 2)
//        viewSched!!.txtDate.setText(date)
//
//        if (ampm == 1)  {
//            viewSched!!.btnAmPm.setText("PM")
//        }
//        else {
//            viewSched!!.btnAmPm.setText("AM")
//        }
//
//        Util.CURRENT_DATE =  viewSched!!.txtDate.text.toString()
//        Util.CURRENT_SECTION = viewSched!!.cboSectionSched.getSelectedItem().toString();
//        Log.e("OKOK", "123456")
//        UpdateListContent(context, "MONTH", monthName[myMonth])
//
//    }
//
//    fun ViewRecord() {
//        var context = containerGlobal!!.context
//        val layoutmanager = LinearLayoutManager(context)
//        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
//        viewSched!!.listSched.layoutManager = layoutmanager
//        adapter = SchedNewAdapter(context, scheduleList)
//        viewSched!!.listSched.adapter = adapter
//    }
//
//    fun AddNewSched(myDate: String, ampm: String, section: String, id: String) {
//        var context = containerGlobal!!.context
//        Log.e("myDate", myDate)
//        var sss = myDate.split(" ")
//        var month = ""
//        if (sss[0] == "JAN") {
//            month = "01"
//        }
//        if (sss[0] == "FEB") {
//            month = "02"
//        }
//        if (sss[0] == "MAR") {
//            month = "03"
//        }
//        if (sss[0] == "APR") {
//            month = "04"
//        }
//        if (sss[0] == "MAY") {
//            month = "05"
//        }
//        if (sss[0] == "JUN") {
//            month = "06"
//        }
//        if (sss[0] == "JUL") {
//            month = "07"
//        }
//        if (sss[0] == "AUG") {
//            month = "08"
//        }
//        if (sss[0] == "SEP") {
//            month = "09"
//        }
//        if (sss[0] == "OCT") {
//            month = "10"
//        }
//        if (sss[0] == "NOV") {
//            month = "11"
//        }
//        if (sss[0] == "DEC") {
//            month = "12"
//        }
//        if (sss[0] == "JAN") {
//            month = "01"
//        }
//        if (sss[0] == "JAN") {
//            month = "01"
//        }
//        var schedDay = ""
//        var schedID = ""
//        try {
//            val cal = Calendar.getInstance()
//            val formatter = SimpleDateFormat("MM/dd/yyyy")
//            Log.e("DATEEEE", sss[1])
//            var p = formatter.parse(month + "/" + sss[1] + "/2023")
//            cal.setTime(p);
//            val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
//
//
//            val day = arrayOf<String>("", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
//            var schedDay = day[dayOfWeek]
//            var schedID = month + sss[1]
//        } catch (e: Exception) {
//            var schedDay = ""
//            var schedID = myDate
//        }
//
//
//        val db1: TableActivity = TableActivity(context);
//        val db2: DatabaseHandler = DatabaseHandler(context);
//        if (db2.CheckAttendance(ampm, myDate, section) == 0) {
//            val db: TableAttendance = TableAttendance(context);
//            var status = db.ManageSched("ADD", ampm, myDate, section, schedID, schedDay)
//            db.AddStudetAttendance(myDate, ampm, section)
//            UpdateListContent(context, "MONTH", GetMonth());
//            adapter!!.notifyDataSetChanged()
//        }
//
//    }
//
//    fun GetMonth(): String {
//        var myMonth: Int
//        val cal = Calendar.getInstance()
//
//        myMonth = cal.get(Calendar.MONTH)
//
//
//        val monthName =
//            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
//
//        return monthName[myMonth]
//
//    }
//
//
//
//}
//*/
