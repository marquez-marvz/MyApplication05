package com.example.myapplication05


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.chart.btnChartTerm
import kotlinx.android.synthetic.main.chart.listActivityChart
import kotlinx.android.synthetic.main.chart.listGradebracket
import kotlinx.android.synthetic.main.chart.txtInfo
import kotlinx.android.synthetic.main.chart.txtRemarkChart
import kotlinx.android.synthetic.main.chart.txtSection
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.missing_activity.view.listMissingActivity
import kotlinx.android.synthetic.main.missing_activity.view.txtMissingActivityCode
import kotlinx.android.synthetic.main.missing_activity.view.txtMissingDescription
import kotlinx.android.synthetic.main.missing_activity.view.txtMissingRemark
import kotlinx.android.synthetic.main.score_dialog.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.task_dialog.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_dialog.view.txtActivityCode
import kotlinx.android.synthetic.main.task_dialog.view.txtDescription
import kotlinx.android.synthetic.main.task_dialog_import.view.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.task_main.view.*
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.Calendar as Calendar1


class ActivityMain : AppCompatActivity() {
    val myContext: Context = this;
    var taskInfo = arrayListOf<TaskInfoModel>()

    var scoreList = arrayListOf<ScoreModel>()
    var scoreAdapter: ScoreAdapter? = null;



    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_main)
        SetSpinnerAdapter()
        ActivityUpdateListContent(this)
        ActivityViewRecord()
        SetDefaultSection()
        var currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
        DefaultColor(currentGradingPeriod)

        pieChart1 = findViewById(R.id.pieChartActivity) as PieChart
        pieChart2 = findViewById(R.id.pieChartActivity2) as PieChart
        pieChart3 = findViewById(R.id.pieChartActivity3) as PieChart
        pieChart4= findViewById(R.id.pieChartActivity4) as PieChart
        pieChart5 = findViewById(R.id.pieChartActivity5) as PieChart
        pieChart6 = findViewById(R.id.pieChartActivity6) as PieChart
        pieChart7 = findViewById(R.id.pieChartActivity7) as PieChart
        pieChart8 = findViewById(R.id.pieChartActivity8) as PieChart
        pieChart9 = findViewById(R.id.pieChartActivity9) as PieChart
        pieChart10 = findViewById(R.id.pieChartActivity10) as PieChart
        pieChart11 = findViewById(R.id.pieChartActivity11) as PieChart
        pieChart12 = findViewById(R.id.pieChartActivity12) as PieChart
//        pieChart3 = findViewById(R.id.activityPiechart1) as PieChart
        SetUpActivityChart(currentGradingPeriod)
//        pieChart4 = findViewById(R.id.piechart4) as PieChart
//        pieChart5 = findViewById(R.id.piechart5) as PieChart
//        pieChart6 = findViewById(R.id.piechart6) as PieChart
//        pieChart7 = findViewById(R.id.piechart7) as PieChart
//        pieChart8 = findViewById(R.id.piechart8) as PieChart
//        pieChart9 = findViewById(R.id.piechart9) as PieChart
//        pieChart10 = findViewById(R.id.piechart10) as PieChart


        val db: TableActivity = TableActivity(myContext)
        val db2: DatabaseHandler = DatabaseHandler(this)
        var section = db2.GetCurrentSection()

//        taskInfo = db.GetSubjectTask(section)
//        Log.e("TTT", taskInfo.count().toString())




       // GetSubjectTask()


        btnAddActivity.setOnClickListener {


            ShowDialog("ADD", this)
        }

        //222
        btnExportData.setOnClickListener {
            val myGrades: Grades = Grades(this)
            var data = myGrades.ActivityExported(section, currentGradingPeriod);
           Util.ExportToGoogleSheet(myContext, "", data, "ExportClassRecord", "ExportClassRecord")

        }


        btnExportData

        btnExport2.setOnClickListener {

            try {
                Log.e("pp", "3333")
                Util.Msgbox(this, "2222")
                val db: TableActivity = TableActivity(this)
                val sectionCode = cboSectionAct!!.getSelectedItem().toString();
                var scoreStudent: List<ScoreModel>
                val db2: DatabaseHandler = DatabaseHandler(this)
                val activity: List<ActivityModel>
                var activityCode = ""
                var description = ""
                var item = ""

                activity = db.GetActivityList(sectionCode, currentGradingPeriod, "ON")

                for (e in activity) {
                    activityCode = activityCode + e.activityCode + ","
                    description = description + e.description + ","
                    item = item + e.item + ","
                }

                var data = ""
                data = data + ",," + description + "\n"
                data = data + ",," + activityCode + "\n"
                data = data + ",," + item + "\n"


                val student: List<EnrolleModel>
                student = db2.GetEnrolleList("SECTION", sectionCode)
                for (e in student) {
                    Log.e("pp", e.studentno + " " + e.firstname + "," + e.lastname)
                    var studentData = e.studentno + "," + e.lastname + " " + e.firstname + ","
                    for (f in activity) {
                        scoreStudent = db.GetStudentScore(sectionCode, f.activityCode, e.studentno)
                        studentData = studentData + " " + scoreStudent[0].score + ", "
                    }
                    data = data + studentData + "\n"
                }
                val c = Calendar1.getInstance()

                val year = c.get(Calendar1.YEAR)
                val month = c.get(Calendar1.MONTH)
                val day = c.get(Calendar1.DAY_OF_MONTH)
                val monthName = arrayOf("JAN", "FEB", "MAR")
                val hour = c.get(Calendar1.HOUR_OF_DAY)
                val minute = c.get(Calendar1.MINUTE)
                val filename =
                    sectionCode + " " + monthName[month] + " " + day + "_" + hour + " " + minute + ".csv"
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)
                var folder =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

                val myFile = File(folder, filename)
                val fstream = FileOutputStream(myFile)
                Log.e("Write", folder.toString())
                Log.e("Write", "EXPORT01.CSV")

                fstream.write(data.toByteArray())
                fstream.close()
                Log.e("Write", "12344567891110")
            } catch (e: Exception) {
                Log.e("err", e.toString())
            }
        }



        btnFirst.setOnClickListener {
            Log.e("111", "yes")
            ViewCurrentGradingActivity("FIRST")
        }

        btnSecond.setOnClickListener {
            ViewCurrentGradingActivity("SECOND")

        }




        btnThird.setOnClickListener {
            ViewCurrentGradingActivity("THIRD")
        }


        btnChart.setOnClickListener {
            val intent = Intent(this,  Chart::class.java)
            startActivity(intent)
        }

        btnImportCopy.setOnClickListener {
            var sectionCode = cboSectionAct!!.getSelectedItem().toString();
            ShowDialog2(sectionCode, this)
        }//btnImportCopy





//        val keys: Iterator<String> = jsonObject.keys()
//
//        while (keys.hasNext()) {
//            val key = keys.next()
//            if (jsonObject.get(key) is JSONObject) { // do something with jsonObject here
//            }
//        }



        btnCoppyNoSubmission.setOnClickListener {

            try {
                Log.e("pp", "3333")
                Util.Msgbox(this, "2222")
                val db: TableActivity = TableActivity(this)
                val sectionCode = cboSectionAct!!.getSelectedItem().toString();
                var scoreStudent: List<ScoreModel>
                val db2: DatabaseHandler = DatabaseHandler(this)
                val activity: List<ActivityModel>
                var activityCode = ""
                var description = ""
                var item = ""

                activity = db.GetActivityList(sectionCode, currentGradingPeriod)
                val student: List<EnrolleModel>
                student = db2.GetEnrolleList("SECTION", sectionCode)
                var noSubmission = ""
                var studentList = ""
                for (f in activity) {
                    studentList = db.GetNoSubmission(sectionCode, f.activityCode)
                    noSubmission =
                        noSubmission + "NO " + f.description + "\n" + studentList + "\n\n"
                }
               // Log.e("ppp", noSubmission)
                Log.e("ppp", "sample22")

                val dlgStudent =
                    LayoutInflater.from(myContext).inflate(R.layout.grade_computation, null)
                // val title = "No " + currentActivity!!.description
                val mBuilder = android.app.AlertDialog.Builder(myContext).setView(dlgStudent)
                    .setTitle("No")
                Log.e("123456", "sample22")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);
                confirmDialog.getWindow()!!
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                confirmDialog.setCanceledOnTouchOutside(false);
                dlgStudent.txtComputation.setText(noSubmission)
                dlgStudent.txtComputation.setMovementMethod(ScrollingMovementMethod())

                dlgStudent.btnCopy.setOnClickListener {
                    val msg = "" + "\n" + dlgStudent.txtComputation.text.toString()
                    Util.CopyText(myContext, msg, "COPY")
                    Log.e("123456", "sample")
                }




            } catch (e: Exception) {
                Log.e("err", e.toString())
            }


        }




        pieChart1!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart1, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart2!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart2, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart3!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart3, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart4!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart4, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart5!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart5, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart6!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart6, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart7!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart7, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart8!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart8, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart9!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart9, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart10!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart10, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart11!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart11, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart12!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart12, e)
            }

            override fun onNothingSelected() {}
        })


        btnCoppyNoIndividual.setOnClickListener {

            try {
                Log.e("pp", "3333")
                Util.Msgbox(this, "2222")
                val db: TableActivity = TableActivity(this)
                val sectionCode = cboSectionAct!!.getSelectedItem().toString();
                var scoreStudent: List<ScoreModel>
                val db2: DatabaseHandler = DatabaseHandler(this)
                val activity: List<ActivityModel>
                var activityCode = ""
                var description = ""
                var item = ""

             //   activity = db.GetActivityList(sectionCode, currentGradingPeriod)
                 val student: List<EnrolleModel>
                student = db2.GetEnrolleList("SECTION", sectionCode)
                Log.e("@@@", "123")
                var noSubmission = ""
                var studentList = ""
                for (st in student) {
                    Log.e("@@@", "1234")

                    studentList = db.GetIndividualNoSubmission(sectionCode, st.studentno, st.lastname + " " + st.firstname)
                     Log.e ("@@@" , st.studentno + "  " + st.lastname + " " + st.firstname)
                    noSubmission = noSubmission + studentList + "\n\n"
                }
                Log.e("ppp", noSubmission)
                Log.e("ppp", noSubmission)

                val dlgStudent =
                    LayoutInflater.from(myContext).inflate(R.layout.grade_computation, null)
               // val title = "No " + currentActivity!!.description
                val mBuilder = android.app.AlertDialog.Builder(myContext).setView(dlgStudent)
                    .setTitle("No")

                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);
                confirmDialog.getWindow()!!
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                confirmDialog.setCanceledOnTouchOutside(false);
                dlgStudent.txtComputation.setText(noSubmission)
                dlgStudent.txtComputation.setMovementMethod(ScrollingMovementMethod())

                dlgStudent.btnCopy.setOnClickListener {
                    val msg = "" + "\n" + dlgStudent.txtComputation.text.toString()
                    Util.CopyText(myContext, msg, "COPY")
                    Log.e("123456", "sample")
                }


                // Util.ExportToGoogleSheet(this,sectionCode, noSubmission, "Missing-Individual List")
            } catch (e: Exception) {
                Log.e("err", e.toString())
            }
        }






        cboSectionActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                val section = cboSectionActivity.getSelectedItem().toString();
                ActivityUpdateListContent(myContext);
                activityAdapter!!.notifyDataSetChanged()
                val mydb: DatabaseHandler = DatabaseHandler(myContext)
                mydb.SetCurrentSection(section)
            }
        }

    }

    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionActivity.setAdapter(sectionAdapter);
        cboSectionAct = findViewById(R.id.cboSectionActivity) as Spinner
    }

//1200
    fun PieClick(pie: PieChart?, e: Entry?) {
            var title = pie!!.getCenterText().toString()
            var ppp = title.split("\n")
            Util.CHART_TITLE = ppp[0]
            var actCode = ppp[1]
            var remark = (e as PieEntry).label
Log.e("3489", remark)
    ShowMissingDialog(actCode ,  Util.CHART_TITLE,remark )

    }

    //1200
    fun ShowMissingDialog(activityCode:String,desc:String, remark:String){

        val dlgMissing = LayoutInflater.from(this).inflate(R.layout.missing_activity, null)
        val mBuilder =
            AlertDialog.Builder(this).setView(dlgMissing).setTitle("Manage Activity")
        val activityDialog = mBuilder.show()
        activityDialog.setCanceledOnTouchOutside(false);

        dlgMissing.txtMissingActivityCode.text = activityCode
        dlgMissing.txtMissingDescription.text = desc
        dlgMissing.txtMissingRemark.text = remark


        ScoreUpdateListContent(this, "REMARK", remark, activityCode)
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        dlgMissing.listMissingActivity.layoutManager = layoutmanager
        scoreAdapter = ScoreAdapter(this, scoreList)
        dlgMissing.listMissingActivity.adapter = scoreAdapter


        //scoreAdapter!!.notifyDataSetChanged()



    }

////1200
//    fun SetUpScoreAdapter() {
//
//    }



//    fun SetUpScoreAdapter() {
//        val layoutmanager = LinearLayoutManager(this)
//        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
//        listActivityChart.layoutManager = layoutmanager
//        Chart.scoreAdapter = ScoreAdapter(this, Chart.scoreList)
//        listActivityChart.adapter = Chart.scoreAdapter
//    }

    fun ScoreUpdateListContent(context: Context, category: String = "", remark: String = "", actCode: String) {
        val dbactivity: TableActivity = TableActivity(context)
        val db2: DatabaseHandler = DatabaseHandler(context)
        val activity: List<ScoreModel>
        scoreList.clear()
        var section = db2.GetCurrentSection()

        activity = dbactivity.GetScoreList(section, actCode, category, remark)

        for (e in activity) {
            scoreList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }

        Log.e("4396", Chart.scoreList.size.toString())

    }






    fun SetUpActivityChart(gradingPeriod: String) {

        val db3: TableActivity = TableActivity(this)
        val db: DatabaseHandler = DatabaseHandler(this)
        val activity: List<ActivityModel>
        activity = db3.GetActivityList(db.GetCurrentSection(), gradingPeriod)
        Log.e("2468", activity.size.toString())
        var ctr = 1;
        HideAllChart()
        for (e in activity) {
            var remarkList = arrayListOf<PieEntry>()
            Log.e("3456", ctr.toString() + "   " + e.activityCode)
            remarkList = db.GetActivityRecordRemark(db.GetCurrentSection(), e.activityCode)

            if (ctr == 1) {
                pieChart1!!.isVisible = true
                Chart.SetUpPie(pieChart1, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 2) {
                Log.e("3456", ctr.toString() + "  #### " + e.activityCode)
                pieChart2!!.isVisible = true
                Chart.SetUpPie(pieChart2, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 3) {
                pieChart3!!.isVisible = true
                Chart.SetUpPie(pieChart3, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 4) {
                pieChart4!!.isVisible = true
                Chart.SetUpPie(pieChart4, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 5) {
                pieChart5!!.isVisible = true
                Chart.SetUpPie(pieChart5, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 6) {
                pieChart6!!.isVisible = true
                Chart.SetUpPie(pieChart6, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 7) {
                pieChart7!!.isVisible = true
                Chart.SetUpPie(pieChart7, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 8) {
                pieChart8!!.isVisible = true
                Chart.SetUpPie(pieChart8, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 9) {
                pieChart9!!.isVisible = true
                Chart.SetUpPie(pieChart9, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 10) {
                pieChart10!!.isVisible = true
                Chart.SetUpPie(pieChart10, remarkList, e.description + "\n" + e.activityCode)
            }


            if (ctr == 11) {
                pieChart11!!.isVisible = true
                Chart.SetUpPie(pieChart11, remarkList, e.description + "\n" + e.activityCode)
            }


            if (ctr == 12) {
                pieChart12!!.isVisible = true
                Chart.SetUpPie(pieChart12, remarkList, e.description + "\n" + e.activityCode)
            }
            ctr++
        }

    }




    fun CopyText(copyString: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", copyString)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }


    companion object {
        var activityList = arrayListOf<ActivityModel>()
        var cboSectionAct: Spinner? = null;
        var activityAdapter: ActivityAdapter? = null;
        var currentGradingPeriod= ""

        var pieChart1: PieChart? = null
        var pieChart2: PieChart? = null
        var pieChart3: PieChart? = null
        var pieChart4: PieChart? = null
        var pieChart5: PieChart? = null
        var pieChart6: PieChart? = null
        var pieChart7: PieChart? = null
        var pieChart8: PieChart? = null
        var pieChart9: PieChart? = null
        var pieChart10: PieChart? = null
        var pieChart11: PieChart? = null
        var pieChart12: PieChart? = null


        fun ActivityUpdateListContent(context: Context) {
            val dbglobal: TableActivity = TableActivity(context)
            val activity: List<ActivityModel>

            activityList.clear()
            val section = cboSectionAct!!.getSelectedItem().toString();
            activity = dbglobal.GetActivityList(section, dbglobal.GetDefaultGradingPeriod())


            for (e in activity) {
                activityList.add(ActivityModel(e.activityCode, e.sectionCode, e.description, e.item.toInt(), e.gradingPeriod, e.category, ""))
            }
        }

        fun GetSubject(): String {
            return cboSectionAct!!.getSelectedItem().toString();

        }

        fun HideAllChart() {

            pieChart1!!.isVisible = false
            pieChart2!!.isVisible = false
            pieChart3!!.isVisible = false
            pieChart4!!.isVisible = false
            pieChart5!!.isVisible = false
            pieChart6!!.isVisible = false
            pieChart7!!.isVisible = false
            pieChart8!!.isVisible = false
            pieChart9!!.isVisible = false
            pieChart10!!.isVisible = false
            pieChart11!!.isVisible = false
            pieChart12!!.isVisible = false

        }




        fun ShowDialog(status: String, context: Context, activity: ActivityModel? = null, position: Int = -1 ) {
            val db: TableActivity = TableActivity(context)
            val dlgactivity = LayoutInflater.from(context).inflate(R.layout.task_dialog, null)
            val mBuilder =
                AlertDialog.Builder(context).setView(dlgactivity).setTitle("Manage Activity")
            val activityDialog = mBuilder.show()
            activityDialog.setCanceledOnTouchOutside(false);

            val arrSection: Array<String> = Util.GetSectionList(context)
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dlgactivity.cboSectionCopy.setAdapter(sectionAdapter);
            val section= cboSectionAct!!.getSelectedItem().toString();



//            var taskInfo = arrayListOf<TaskInfoModel>()
//            taskInfo = db.GetSubjectTask(section)



//            val task = ArrayList<String>()
//            Log.e("sss", taskInfo.count().toString())
//            task.add("-")
//            for (e in taskInfo ) {
//                task.add(e.TaskCode)
//                Log.e("sss", e.TaskCode)
//            }
//            Log.e("sss10", task.count().toString())

//            var sectionAdapter2: ArrayAdapter<String> = ArrayAdapter<String>(context, R.layout.util_spinner, task)
//            sectionAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//           dlgactivity.cboTaskCode.setAdapter(sectionAdapter2);

            if (status == "ADD") {
                ClearTextBox(dlgactivity)
                StatusTextBox(true, dlgactivity)
                dlgactivity.txtActivityCode.setText(db.GetNewActivityCode(GetSubject()))
                dlgactivity.cboGradingPeriod.setSelection(GetGradingPeriodIndex(db.GetDefaultGradingPeriod(), context))
                dlgactivity.cboGradingPeriod.isEnabled = false
            } else if (status == "EDIT") {
                dlgactivity.txtActivityCode.setText(activity!!.activityCode)
                dlgactivity.txtDescription.setText(activity!!.description) //dlgactivity.cboCategory.setText(activity!!.category)
                dlgactivity.cboCategory.setSelection(GetCategoryIndex(activity!!.category, context))
                dlgactivity.cboGradingPeriod.setSelection(GetGradingPeriodIndex(activity!!.gradingPeriod, context))
                dlgactivity.txtItem.setText(activity.item.toString())
                dlgactivity.btnSaveRecord.setText("EDIT")
                StatusTextBox(false, dlgactivity)
            }


            dlgactivity.btnSaveRecord.setOnClickListener {
                val buttonText: String = dlgactivity.btnSaveRecord.getText().toString()
                val activityCode = dlgactivity.txtActivityCode.text.toString()
                val description = dlgactivity.txtDescription.text.toString()
                val item = dlgactivity.txtItem.text.toString()
                val sectionCode = cboSectionAct!!.getSelectedItem().toString();
                val activityCategory = dlgactivity.cboCategory!!.getSelectedItem().toString();
                val gradingPeriod = dlgactivity.cboGradingPeriod!!.getSelectedItem().toString();

                if (buttonText == "SAVE RECORD") {
                    var status =
                        db.ManageActivity("ADD", activityCode, sectionCode, description, item, activityCategory, gradingPeriod)
                    ActivityUpdateListContent(context)
                    activityAdapter!!.notifyDataSetChanged()
                    activityDialog.dismiss()
                    db.AddStudeScore(sectionCode, activityCode)

                } else if (buttonText == "EDIT") {
                    StatusTextBox(true, dlgactivity)
                    dlgactivity.btnSaveRecord.setText("SAVE CHANGES")

                    dlgactivity.txtActivityCode.isEnabled = false;
                } else if (buttonText == "SAVE CHANGES") {
                    db.ManageActivity("EDIT", activityCode, sectionCode, description, item, activityCategory, gradingPeriod)
                    ActivityUpdateListContent(context)
                    activityAdapter!!.notifyDataSetChanged()
                    activityDialog.dismiss()
                }

            }

            dlgactivity.btnClose.setOnClickListener {
                activityDialog.dismiss()
            }

            dlgactivity.btnCopyActivity.setOnClickListener {
                val gradingPeriod = Util.GetCurrentGradingPeriod(context);
                val  section =  dlgactivity.cboSectionCopy!!.getSelectedItem().toString();
                val  description =  dlgactivity.cboActivityCopy!!.getSelectedItem().toString();
                val myList: List<ActivityModel> = db.GetSectionActivityList(section, gradingPeriod)
                for (x in 0..myList.size - 1) {
                    if (description == myList[x].description){
                        dlgactivity.txtDescription.setText( myList[x].description) //dlgactivity.cboCategory.setText(activity!!.category)
                        dlgactivity.cboCategory.setSelection(GetCategoryIndex(myList[x].category, context))
                        dlgactivity.cboGradingPeriod.setSelection(GetGradingPeriodIndex(myList[x].gradingPeriod, context))
                        dlgactivity.txtItem.setText(myList[x].item.toString())
                    }
                }
            }

            dlgactivity.cboSectionCopy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                   var section =  dlgactivity.cboSectionCopy!!.getSelectedItem().toString();
                    val arrSection: Array<String> = Util.GetSectionListActivity(context, section)
                    var sectionAdapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
                    sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dlgactivity.cboActivityCopy.setAdapter(sectionAdapter);
                }
            }

            dlgactivity.cboActivityCopy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                }
            }

        } //ShowDialog

        private fun ClearTextBox(dlgactivity: View) {
            dlgactivity.txtActivityCode.setText("")
            dlgactivity.txtDescription.setText("")
            dlgactivity.txtItem.setText("")
        }

        fun StatusTextBox(stat: Boolean, dlgactivity: View) {

            dlgactivity.txtDescription.isEnabled = stat
            dlgactivity.txtItem.isEnabled = stat
            dlgactivity.cboCategory.isEnabled = stat
            dlgactivity.cboGradingPeriod.isEnabled = stat

        }

        fun GetCategoryIndex(search: String, context: Context): Int {
            val arrGroup: Array<String> =
                context.getResources().getStringArray(R.array.category_choice)
            val index = arrGroup.indexOf(search)
            return index
        }

        fun GetGradingPeriodIndex(search: String, context: Context): Int {
            val arrGroup: Array<String> =
                context.getResources().getStringArray(R.array.gradingperiod_choice)
            val index = arrGroup.indexOf(search)
            return index
        }


    }


    fun ActivityViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listActivity.layoutManager = layoutmanager
        activityAdapter = ActivityAdapter(this, activityList)
        listActivity.adapter = activityAdapter
    }

    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionActivity.setSelection(index)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun DefaultColor(gradingPeriod: String) {

        btnFirst.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        btnSecond.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        btnThird.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        val db: TableActivity = TableActivity(this)
        db.SetDefaultGradingPeriod(gradingPeriod)

        if (gradingPeriod == "FIRST") {
            btnFirst.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else if (gradingPeriod == "SECOND")
            btnSecond.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        else if (gradingPeriod == "THIRD") //.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            btnThird.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ViewCurrentGradingActivity(gradingPeriod: String) {
        DefaultColor(gradingPeriod)
        currentGradingPeriod = gradingPeriod
        ActivityUpdateListContent(this)
        activityAdapter!!.notifyDataSetChanged()
    }






    fun ShowDialog2(section: String, context: Context, student: StudentModel? = null, position: Int = -1) { //val mydb: DatabaseHandler = DatabaseHandler(context)

        val dlgscore = LayoutInflater.from(context).inflate(R.layout.score_dialog, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgscore).setTitle("Manage Student")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);
        val arrSection: Array<String> = Util.GetSectionList(context)
        val db: TableAttendance = TableAttendance(context)

        dlgscore.btnSetScore.setOnClickListener {
            var WithDashsectionCode= section.replace(" ", "-");
            var url = "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems&&sheet=Quiz"
            url = url + "&&section=" + WithDashsectionCode;
            Log.e("" , url)
            var stringRequest =
                StringRequest (Request.Method.GET,url, { response ->
                    Log.e("", response)
                    var obj = JSONArray(response);
                    Log.e("", obj.length().toString())
                    var i = 0;
                    var ctr =0;
                    var x = 0;
                    var myScore = 0
                    var status  = ""
                    val arr  = Array<String>(20) {""}
                    val ppp = obj.getJSONObject(0)
                    val iterator: Iterator<String> = ppp.keys();

                    while (iterator.hasNext()) {
                        arr.set(ctr, iterator.next())
                        ctr++;
                    }
                    while (i< obj.length()){

                        val jsonObject = obj.getJSONObject(i)
                        var studnum = jsonObject.getString(arr[0])

                        var name  = jsonObject.getString(arr[1])
                        for (x in 2..jsonObject.length()-1) {
                            var score  = jsonObject.getString(arr[x])
                            var  submissionStatus =   db.GetStatusStudent(studnum, section, arr[x])
                            if (score == "-") {
                                myScore = 0
                                status = "NO"
                            } else {
                                myScore = score.toInt();
                                status = "YES"
                            }
                            if (submissionStatus =="DR")
                                  status = submissionStatus
                            val db: TableAttendance = TableAttendance(this)
                            db.UpdateStudentActivityScore(status, myScore, studnum, section, arr[x])

                        }
                        i++;
                    }
                    Util.Msgbox(this, "Score is successfully imported@@");
                }) { }

            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)

        }


        dlgscore.btnSetStatus.setOnClickListener {
            var WithDashsectionCode= section.replace(" ", "-");
            var url = "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems&&sheet=Status"
            url = url + "&&section=" + WithDashsectionCode;
            Log.e("" , url)
            var stringRequest =
                StringRequest (Request.Method.GET,url, { response ->
                    Log.e("", response)
                    var obj = JSONArray(response);
                    Log.e("", obj.length().toString())
                    var i = 0;
                    var ctr =0;
                    var x = 0;
                    var myScore = 0
                    var status  = ""
                    val arr  = Array<String>(20) {""}
                    val ppp = obj.getJSONObject(0)
                    val iterator: Iterator<String> = ppp.keys();

                    while (iterator.hasNext()) {
                        arr.set(ctr, iterator.next())
                        ctr++;
                    }
                    while (i< obj.length()){
                        val jsonObject = obj.getJSONObject(i)
                        var studnum = jsonObject.getString(arr[0])
                        var name  = jsonObject.getString(arr[1])
                        for (x in 3..jsonObject.length()-1) {
                            var status  = jsonObject.getString(arr[x])
                            if (status == "YES") {
                                val db: TableAttendance = TableAttendance(this)
                                Log.e("", status+ "  " + studnum + " " + section + " " +  arr[x])
                                db.UpdateStudentActivityStatusNew(status, studnum, section, arr[x])
                            }
                        }
                        i++;
                    }
                    Util.Msgbox(this, "Score is successfully imported@@");
                }) { }

            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)

        }
    }
}


