package com.example.myapplication05

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.speak.*
import kotlinx.android.synthetic.main.student_import.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.task_dialog_import.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*


//import android.widget.Toast;


class GradeMain : AppCompatActivity() {

    val myContext = this;
    var t1: TextToSpeech? = null
    var lineNumberPrint = false
    var RULER_VIEW = false
    var DEV_FILENANME = false


    companion object {
        var gradeList = arrayListOf<GradeModel>()
        var sectionList = arrayListOf<SectionModel>()
        var myGradeAdapter: GradeAdapter? = null;
        var gradeSectionAdapter: GradeSectionAdapter? = null;
        var varbtn60to69: Button? = null;
        var varbtn70to74: Button? = null;
        var varbtn75to79: Button? = null;
        var varbtn80to89: Button? = null;
        var varbtn90to94: Button? = null;
        var varbtn95to99: Button? = null;
        var varcbosection: Spinner? = null;
        var piechartko: PieChart? = null;
        var vartxtScoreVersion: TextView? = null;



        fun ShowGrades(context: Context, section: String) {
            val sql = "select * from tbgrade_query where sectioncode = '$section' order by lastname"
            UpdateListContent(context, sql)
        }


        fun UpdateListContent(context: Context, sql: String) {
            try {
                val dbGrade: Grades = Grades(context)
                val theGrade: List<GradeModel>
                gradeList.clear()

                theGrade = dbGrade.GetGradeList(sql)
                for (e in theGrade) {
                    gradeList.add(GradeModel(e.sectioncode, e.studentNo, e.firstname, e.lastname, e.firstGrade, e.firstEquivalent, e.firstOriginalGrade, e.secondGrade, e.secondEquivalent, e.secondOriginalGrade, e.CumulativeGrade, e.CumulativeGradeEquivalent, e.remark, e.MidtermStatus, e.FinalStatus, e.gender))
                }

                myGradeAdapter!!.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("err", e.toString())
            }
        }

        fun UpdateCount(context: Context, sectionCode: String) {
            try {

                val myGrades: Grades = Grades(context)
                val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
                var grade6069 = myGrades.GradeCount(currentGradingPeriod, 50, 69.99, sectionCode)
                varbtn60to69!!.setText("60-69\n" + grade6069)
                var grade7074 = myGrades.GradeCount(currentGradingPeriod, 70, 74.99, sectionCode)
                varbtn70to74!!.setText("70-74\n" + grade7074)
                var grade7579 = myGrades.GradeCount(currentGradingPeriod, 75, 79.99, sectionCode)
                varbtn75to79!!.setText("75-79\n" + grade7579)
                var grade8089 = myGrades.GradeCount(currentGradingPeriod, 80, 89.99, sectionCode)
                varbtn80to89!!.setText("81-89\n" + grade8089)
                var grade9094 = myGrades.GradeCount(currentGradingPeriod, 90, 94.99, sectionCode)
                varbtn90to94!!.setText("90-94\n" + grade9094)
                var grade9699 = myGrades.GradeCount(currentGradingPeriod, 95, 99.99, sectionCode)
                varbtn95to99!!.setText("95-99\n" + grade9699)
            } catch (e: Exception) {
                Log.e("hello", e.toString())
            }
        }


        fun ShowChartGrades(context: Context, sectionCode: String) {
            var gradeCount = arrayListOf<PieEntry>()

            val myGrades: Grades = Grades(context)
            val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
            var grade6069 = myGrades.GradeCount(currentGradingPeriod, 50, 69.99, sectionCode)
            Log.e("!!!", grade6069.toString() + " " + sectionCode)

            if (grade6069 > 0) {
                gradeCount.add(PieEntry(grade6069.toFloat(), "60-69"))
            }
            var grade7074 = myGrades.GradeCount(currentGradingPeriod, 70, 74.99, sectionCode)
            if (grade7074 > 0) {
                gradeCount.add(PieEntry(grade7074.toFloat(), "70-74"))
            }

            var grade7579 = myGrades.GradeCount(currentGradingPeriod, 75, 79.99, sectionCode)
            if (grade7579 > 0) {
                gradeCount.add(PieEntry(grade7579.toFloat(), "75-79"))
            }
            var grade8089 = myGrades.GradeCount(currentGradingPeriod, 80, 89.99, sectionCode)
            if (grade8089 > 0) {
                gradeCount.add(PieEntry(grade8089.toFloat(), "80-89"))
            }
            var grade9094 = myGrades.GradeCount(currentGradingPeriod, 90, 94.99, sectionCode)
            if (grade9094 > 0) {
                gradeCount.add(PieEntry(grade9094.toFloat(), "90-94"))
            }

            var grade9699 = myGrades.GradeCount(currentGradingPeriod, 95, 99.99, sectionCode)
            if (grade9699 > 0) {
                gradeCount.add(PieEntry(grade9699.toFloat(), "95-99"))
            }
            Chart.SetUpGradePie(piechartko, gradeCount, Util.ACT_DESCRIPTION)
            piechartko!!.notifyDataSetChanged();
            piechartko!!.invalidate();
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grade_main)
        SetSpinnerAdapter()
        SetDefaultSection(Util.GRADE_SECTION)
        val section = cboSection.getSelectedItem().toString();
        var currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
        ChangeColorQuarter(currentGradingPeriod);
        val myGrades: Grades = Grades(myContext)
        Log.e("xxx", "Hello12345")
        var ppp = "Hello World"
        var strSpeak = ""
        ViewRecord()
        if (Util.GRADE_CHART == true) {
            var gr = Util.GRADE_SEARCH.split("-")
            ShowBracketGrades(gr[0].toInt(), gr[1].toInt() + 0.99)
        } else {
            ShowGrades(this, section)
        }



        varbtn60to69 = findViewById(R.id.btn60to69) as Button
        varbtn70to74 = findViewById(R.id.btn70to74) as Button
        varbtn75to79 = findViewById(R.id.btn75to79) as Button
        varbtn80to89 = findViewById(R.id.btn80to89) as Button
        varbtn90to94 = findViewById(R.id.btn90to94) as Button
        varbtn95to99 = findViewById(R.id.btn95to99) as Button
        varcbosection = findViewById(R.id.cboSection) as Spinner
        piechartko = findViewById(R.id.pieChartGrade) as PieChart
        vartxtScoreVersion = findViewById(R.id.txtScoreVersion) as TextView







        UpdateCount(this, Util.GRADE_SECTION)
        myGrades.UpdateVersion(section)
        txtScoreVersion.setText(myGrades.GetVersion(section).toString())
        ShowChartGrades(this, Util.GRADE_SECTION)
        SectionUpdateListContent()
        SetUpSectionGradeAdapter()




        t1 = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                t1!!.setLanguage(Locale.US)
                val db: TableActivity =
                    TableActivity(myContext) //                Log.e("SPP", db.GetSpeedSpeak().toFloat().toString()) //                t1!!.setSpeechRate(db.GetSpeedSpeak().toFloat())
            }
        }


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        cboSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                val section = cboSection.getSelectedItem().toString();

                myGradeAdapter!!.notifyDataSetChanged()
                db.SetCurrentSection(section)
            }
        }


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=
        btnRefresh.setOnClickListener {
            ShowGrades(this, Util.GRADE_SECTION)
            UpdateCount(this, Util.GRADE_SECTION)
            ShowChartGrades(this, Util.GRADE_SECTION)
        }

        btnComputeGrades.setOnClickListener {
            try {
                val sectionCode = cboSection!!.getSelectedItem().toString();
                val db: DatabaseHandler = DatabaseHandler(this)
                val db2: Grades = Grades(this)
                val db1: TableActivity = TableActivity(this)
                val activity: List<ActivityModel>


                val student: List<EnrolleModel>
                student = db.GetEnrolleList("SECTION", sectionCode)
                var currentGradingPeriod = db1.GetDefaultGradingPeriod();
                Log.e("334455", currentGradingPeriod)
                for (e in student) {
                    val myGrades: Grades = Grades(this)
                    val school = db2.GetSchool(sectionCode)
                    if (school == "CSPC") {
                        var gr =
                            myGrades.CSPCComputeGrades(sectionCode, e.studentno, currentGradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
                    } else if (school == "BISCAST") {
                        var gr=

                            myGrades.BISCASATComputeGrades(sectionCode, e.studentno, currentGradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
                    } else {
                        Log.e("gr", school)
                        var gr =
                            myGrades.DEPEDComputeGrades(sectionCode, e.studentno, currentGradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
                    }
                }
                ShowGrades(this, sectionCode)
                ViewRecord() // Util.Msgbox(this, "Grade are Updated")
            } catch (e: Exception) {
            }
        }


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        btnSpeak.setOnClickListener {
            if (btnSpeak.text == "SPEAK") {
                Log.e("PPP", "HI") //            val  sql = "select * from tbgrade_query where sectioncode = '$section' order by lastname"
                val dbGrade: Grades =
                    Grades(this) //            val theGrade: List<GradeModel> //            theGrade = dbGrade.GetGradeList(sql)

                //            val currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
                val db2: TableActivity = TableActivity(myContext)
                val averageStatus = db2.GetAverageStatus()
                val school = dbGrade.GetSchool(section)
                Log.e("school200", school)

                btnSpeak.text = "STOP"
                for (e in gradeList) {
                    if (averageStatus == "TRUE" && school == "DEPED") strSpeak =
                        strSpeak + e.lastname.toLowerCase() + "," + e.firstname.toLowerCase() + ".   " + e.firstGrade + "  " + e.secondGrade.toInt() + " " + "Final Grade:  ," + e.CumulativeGrade.toInt() + "\n"
                    else if (averageStatus == "TRUE") strSpeak =
                        strSpeak + e.lastname + "," + e.firstname + ".   " + e.firstGrade + "  " + e.secondGrade + " " + "Final Grade:  ," + e.CumulativeGrade + " , " + e.CumulativeGradeEquivalent + "\n"
                    else if (currentGradingPeriod == "FIRST") strSpeak =
                        strSpeak + e.lastname.toLowerCase() + "," + e.firstname + ".   " + e.firstGrade + "," + e.firstEquivalent + ".  ."
                    else if (currentGradingPeriod == "SECOND") strSpeak =
                        strSpeak + e.lastname.toLowerCase() + "," + e.firstname + ".   " + +e.secondGrade + "," + e.secondEquivalent + ".  ."
                }
                Log.e("PPP", strSpeak)
                t1!!.speak(strSpeak, TextToSpeech.QUEUE_FLUSH, null);

            } else {
                t1!!.stop()
                btnSpeak.text = "SPEAK"
            }

            //                gradeList.add(GradeModel(e.sectioncode,
            //                                         e.studentNo,
            //                                         e.firstname,
            //                                         e.lastname,
            //                                         e.firstGrade,
            //                                         e.firstEquivalent,
            //                                         e.firstOriginalGrade,
            //                                         e.secondGrade,
            //                                         e.secondEquivalent,
            //                                         e.secondOriginalGrade,
            //                                         e.CumulativeGrade,
            //                                         e.CumulativeGradeEquivalent,
            //                                         e.remark,
            //                                         e.MidtermStatus,
            //                                         e.FinalStatus
            //                ))


        }


        btnExportGrades.setOnClickListener {


            val sheetName =
                cboSection.getSelectedItem().toString() + "-" + Util.GetCurrentGradingPeriod(this)

            val myGrades: Grades = Grades(this)
            val db: TableActivity = TableActivity(this)
            val currentGradingPeriod = db.GetDefaultGradingPeriod()
             var data = myGrades.GradeExported(section);
            Util.ExportToGoogleSheet(myContext, sheetName, data, "Export Grades", "ExportGrades")




            //Log.e("PPP", data)

            //            val loading = ProgressDialog.show(this, "Importing Score", "Please wait")
            //            var url =
            //                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getSheetExport"
            //
            //            var stringRequest = StringRequest(Request.Method.GET, url, { response ->
            //                Util.Msgbox(this, response.toString())
            //                loading.dismiss()
            //                Log.e("@@@", response)
            //                var response = response.replace("\"", "");
            //                val arrNew = response.split("/").toTypedArray()
            //                var sheetList = Array(arrNew.size) { "" }
            //
            //                sheetList[0] = "Select"
            //                for (x in 1..arrNew.size - 1) {
            //                    sheetList[x] = arrNew[x]
            //                }
            //
            //                ShowImportDialog(this, sheetList)
            //            }) { }
            //            val queue: RequestQueue = Volley.newRequestQueue(this)
            //            queue.add(stringRequest)
            //            loading.dismiss()
            //
            //
            //            //            try {
            //            //                val sectionCode = cboSection!!.getSelectedItem()
            //            //                    .toString(); //                val db: DatabaseHandler = DatabaseHandler(this)
            //            //
            //            // val myGrades: Grades = Grades(this)
            //            //                val db: TableActivity = TableActivity(this)
            //            //                val currentGradingPeriod =db.GetDefaultGradingPeriod()
            //            //                val averageStatus = db.GetAverageStatus()
            //            //                var data=""
            //            //
            //            //                if (averageStatus=="TRUE"){
            //            //                    Log.e("xxx", "aaaabbbb")
            //            //
            //            //data =myGrades.GetGradeListforCopy(sectionCode)
            //            //               Log.e("ddd", data)
            //            //                } else {
            //            //                    Log.e("xxx", "sssyyyy")
            //            //                    data = myGrades.FileTransfer(sectionCode, currentGradingPeriod);
            //            //                }
            //            //                    CopyText(data); //                for (e in student) {
            //            //                    val myGrades: Grades = Grades(this)
            //            //                    var gr = myGrades.CollegeComputeGrades(sectionCode, e.studentno, "FIRST")
            //            //                    //myGrades.DisplayLogGrade(sectionCode, "FIRST")
            //            //                }
            //            //                ShowGrades(sectionCode)
            //            //                ViewRecord()
            //            // Util.Msgbox(this, "Grade are Updated")
            //
            //            //            } catch (e: Exception) {
            //            //            }
        }



        btnAlphabet.setOnClickListener {
            btnAscending.setVisibility(View.INVISIBLE);
            btnDescending.setVisibility(View.INVISIBLE);
            btnAlphabet.setVisibility(View.INVISIBLE);
            btnGender.setVisibility(View.VISIBLE);

            val currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
            val section = cboSection.getSelectedItem().toString();
            val db: TableActivity = TableActivity(this)

            var gradingPeriodField = currentGradingPeriod + "Grade"
            val averageStatus = db.GetAverageStatus()
            var sql = ""
            if (averageStatus == "TRUE") {
                sql = """ select * from tbgrade_query
                    where sectioncode = '$section'
                   order by gender desc, lastname
                    """.trimIndent()
            } else {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    order by gender desc, lastname
                    """.trimIndent()
            }
            UpdateListContent(this, sql)


        }

        btnGender.setOnClickListener {
            btnAscending.setVisibility(View.VISIBLE);
            btnDescending.setVisibility(View.INVISIBLE);
            btnAlphabet.setVisibility(View.INVISIBLE);
            btnGender.setVisibility(View.INVISIBLE);

            val currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
            val section = cboSection.getSelectedItem().toString();
            val db: TableActivity = TableActivity(this)

            var gradingPeriodField = currentGradingPeriod + "Grade"
            val averageStatus = db.GetAverageStatus()
            var sql = ""
            if (averageStatus == "TRUE") {
                sql = """ select * from tbgrade_query
                    where sectioncode = '$section'
                    order by CumulativeGrade ASC
                    """.trimIndent()
            } else {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    order by $gradingPeriodField ASC
                    """.trimIndent()
            }
            UpdateListContent(this, sql)


        }


        btnAscending.setOnClickListener {
            btnAscending.setVisibility(View.INVISIBLE);
            btnDescending.setVisibility(View.VISIBLE);
            btnAlphabet.setVisibility(View.INVISIBLE);


            val currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
            val section = cboSection.getSelectedItem().toString();
            val db: TableActivity = TableActivity(this)

            var gradingPeriodField = currentGradingPeriod + "Grade"
            val averageStatus = db.GetAverageStatus()
            var sql = ""
            if (averageStatus == "TRUE") {
                sql = """ select * from tbgrade_query
                    where sectioncode = '$section'
                    order by CumulativeGrade DESC
                    """.trimIndent()
            } else {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    order by $gradingPeriodField DESC
                    """.trimIndent()
            }
            UpdateListContent(this, sql)


        }
        btnDescending.setOnClickListener {
            btnAscending.setVisibility(View.INVISIBLE);
            btnDescending.setVisibility(View.INVISIBLE);
            btnAlphabet.setVisibility(View.VISIBLE);
            val section = cboSection.getSelectedItem().toString();
            ShowGrades(this, section)

            //            try {
            //                val currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
            //                val section = cboSection.getSelectedItem().toString();
            //                val sql = """
            //                    select * from tbgrade_query
            //                    where sectioncode = '$section'
            //                     order by $currentGradingPeriod  desc, lastname,firstname
            //                """.trimIndent()
            //                Log.e("err", sql)
            //                UpdateListContent(sql)
            //                myGradeAdapter!!.notifyDataSetChanged()
            //            } catch (e: Exception) {
            //            }
        }

        btn60to69.setOnClickListener {
            ShowBracketGrades(50, 69.99)
        }

        btn70to74.setOnClickListener {
            ShowBracketGrades(70, 74.99)
        }

        btn75to79.setOnClickListener {
            ShowBracketGrades(75, 79.99)
        }

        btn80to89.setOnClickListener {
            ShowBracketGrades(80, 89.99)
        }

        btn90to94.setOnClickListener {
            ShowBracketGrades(90, 94.99)
        }

        btn95to99.setOnClickListener {
            ShowBracketGrades(95, 99.99)
        }




        pieChartGrade.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChartGrade, e)
            }

            override fun onNothingSelected() {}
        })


        btnPDFGrade.setOnClickListener {
            val section = cboSection.getSelectedItem().toString();
            var semester = "SECOND"
            val myPdfDocument = PdfDocument()
            val myPaint = Paint()
            val version = txtScoreVersion.text

            val myPageInfo1 =
                PdfDocument.PageInfo.Builder((8.5 * 72).toInt(), (13 * 72).toInt(), 1).create()
            val myPage1 = myPdfDocument.startPage(myPageInfo1)
            val canvas =
                myPage1.canvas //    canvas.drawText("Version " + version , 10F, 10F, myPaint);

            PdfGradeSHeet(section, canvas, myPaint, myPageInfo1.pageWidth.toFloat(), Util.CURRENT_SEMESTER)
            myPdfDocument.finishPage(myPage1);


            val currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
            val db3: TableActivity = TableActivity(this)
            val activity: List<ActivityModel>
            activity = db3.GetActivityList(section, currentGradingPeriod, "ON")


            val myPageInfo2 =
                PdfDocument.PageInfo.Builder((13 * 72).toInt(), (8.5 * 72).toInt(), 1).create()
            val myPage2 = myPdfDocument.startPage(myPageInfo2)
            val canvas2 = myPage2.canvas


            PdfClassRecord(section, canvas2, myPaint, myPageInfo2.pageWidth.toFloat(), Util.CURRENT_SEMESTER, activity)
            myPdfDocument.finishPage(myPage2);

            val myPageInfo3 =
                PdfDocument.PageInfo.Builder((13 * 72).toInt(), (8.5 * 72).toInt(), 1).create()
            val myPage3 = myPdfDocument.startPage(myPageInfo3)
            val canvas3 = myPage3.canvas


            PdfClassRecordSecond(section, canvas3, myPaint, myPageInfo3.pageWidth.toFloat(), "FIRST", activity)
            myPdfDocument.finishPage(myPage3);


            //   val m =  getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)


            // = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Hello22.pdf")
            //   val file = File(this.getExternalFilesDir("/storage/emulated/0/"), "Hello22.pdf")
            //val file = File("/storage/emulated/0/" , "Hello.pdf")
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23) //btnPDFGrade
            val folder = "/storage/emulated/0/DriveSyncFiles/PDF GRADES"

            //           // val folder =
            //              //  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            var myFile: File
            if (DEV_FILENANME == true) {
                myFile = File(folder, "Hello23.pdf")
            } else {
                myFile = File(folder, section + "-v" + version + ".pdf")
            }
            Log.e("MMM", myFile.toString()) //  try {
            requestPermission()
            Log.e("PERMISSION", checkPermission().toString())
            myPdfDocument.writeTo(FileOutputStream(myFile)) //  } catch (e: IOException) {
            //       e.printStackTrace()
            //   }

            myPdfDocument.close()
            Log.e("kkk", GetPath())
            Util.Msgbox(this, "Grade was sucessfully printed")


        }

        btnFirst.setOnClickListener {
            val section = cboSection.getSelectedItem().toString();
            val db: TableActivity = TableActivity(this)
            ChangeColorQuarter("FIRST")
            db.SetDefaultGradingPeriod("FIRST")
            db.SetAverageStatus("FALSE")
            ShowGrades(this, Util.GRADE_SECTION)
            UpdateCount(this, Util.GRADE_SECTION)
        }

        btnSecond.setOnClickListener {
            val db: TableActivity = TableActivity(this)
            db.SetDefaultGradingPeriod("SECOND")
            db.SetAverageStatus("FALSE")
            ChangeColorQuarter("SECOND")
            val section = cboSection.getSelectedItem().toString();
            ShowGrades(this, Util.GRADE_SECTION)
            UpdateCount(this, Util.GRADE_SECTION)
        }

        btnViewChart.setOnClickListener {

            val db: DatabaseHandler = DatabaseHandler(this)
            val dlgactivity = LayoutInflater.from(this).inflate(R.layout.piechart, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgactivity).setTitle(db.GetCurrentSection())
            val activityDialog = mBuilder.show()
            activityDialog.setCanceledOnTouchOutside(false); //
            var pie: PieChart =
                dlgactivity.findViewById(R.id.piechart) as PieChart //  remarkList = db.GetActivityRecord(db.GetCurrentSection(), e.activityCode)
            var gradeCount = arrayListOf<PieEntry>()
            val sectionCode = cboSection!!.getSelectedItem().toString();
            val myGrades: Grades = Grades(this)
            val currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
            var grade6069 = myGrades.GradeCount(currentGradingPeriod, 50, 69.99, sectionCode)
            Log.e("!!!", grade6069.toString() + " " + sectionCode)

            if (grade6069 > 0) {
                gradeCount.add(PieEntry(grade6069.toFloat(), "60-69"))
            }
            var grade7074 = myGrades.GradeCount(currentGradingPeriod, 70, 74.99, sectionCode)
            if (grade7074 > 0) {
                gradeCount.add(PieEntry(grade7074.toFloat(), "70-74"))
            }

            var grade7579 = myGrades.GradeCount(currentGradingPeriod, 75, 79.99, sectionCode)
            if (grade7579 > 0) {
                gradeCount.add(PieEntry(grade7579.toFloat(), "75-79"))
            }
            var grade8089 = myGrades.GradeCount(currentGradingPeriod, 80, 89.99, sectionCode)
            if (grade8089 > 0) {
                gradeCount.add(PieEntry(grade8089.toFloat(), "80-89"))
            }
            var grade9094 = myGrades.GradeCount(currentGradingPeriod, 90, 94.99, sectionCode)
            if (grade9094 > 0) {
                gradeCount.add(PieEntry(grade9094.toFloat(), "90-94"))
            }

            var grade9699 = myGrades.GradeCount(currentGradingPeriod, 95, 99.99, sectionCode)
            if (grade9699 > 0) {
                gradeCount.add(PieEntry(grade9699.toFloat(), "95-99"))
            }
            Chart.SetUpPie(pie, gradeCount, Util.ACT_DESCRIPTION)

        }


        btnAverage.setOnClickListener {
            val db2: Grades = Grades(this)
            val db3: DatabaseHandler = DatabaseHandler(this)
            val section = cboSection.getSelectedItem().toString();
            val school = db2.GetSchool(section)
            Log.e("school200", school)

            val student: List<EnrolleModel>
            student = db3.GetEnrolleList("SECTION", section)
            val myGrades: Grades = Grades(this)
            for (e in student) {
                db2.ComputeAverage(section, e.studentno)

            }


            val db: TableActivity = TableActivity(this)
            db.SetAverageStatus()
            ChangeColorQuarter("AVG")
            ShowGrades(this, section)

        }

        cboSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                val section = cboSection.getSelectedItem().toString();
                if (Util.GRADE_CHART == true) {
                    var gr = Util.GRADE_SEARCH.split("-")
                    ShowBracketGrades(gr[0].toInt(), gr[1].toInt() + 0.99)
                } else {
                    ShowGrades(myContext, section)
                }
                val mydb: DatabaseHandler = DatabaseHandler(myContext)
                mydb.SetCurrentSection(section)
            }
        }




    }

    override fun onBackPressed() {
        t1!!.shutdown()
        super.finish()
    }

    fun PieClick(pie: PieChart?, e: Entry?) {
        var bracketGrades = (e as PieEntry).label
        var gr = bracketGrades.split("-")
        ShowBracketGrades(gr[0].toInt(), gr[1].toInt() + 0.99)
    }



fun ShowImportDialog(context: Context, arr: Array<String>) {
    val mydb: DatabaseHandler = DatabaseHandler(context)
    val dlgstudent = LayoutInflater.from(context).inflate(R.layout.student_import, null)
    val mBuilder = AlertDialog.Builder(context).setView(dlgstudent).setTitle("Manage Student")
    val studentDialog = mBuilder.show()
    studentDialog.setCanceledOnTouchOutside(false);
    val arrSection: Array<String> = Util.GetSectionList(context)
    Log.e("@@@", "2222")
    var sectionAdapter: ArrayAdapter<String> =
        ArrayAdapter<String>(context, R.layout.util_spinner, arr)
    sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    dlgstudent.cboSheetList.setAdapter(sectionAdapter);
//    dlgstudent.rowBtnImportStudent.isEnabled = false
    dlgstudent.cboSheetList.setSelection(sectionAdapter.getPosition("Select"))

//    dlgstudent.btnExportGrades.setOnClickListener {
//        val sheetName = dlgstudent.cboSheetList.getSelectedItem().toString();
//        val section = cboSection!!.getSelectedItem().toString();
//        val myGrades: Grades = Grades(this)
//        val db: TableActivity = TableActivity(this)
//        val currentGradingPeriod = db.GetDefaultGradingPeriod()
//        var data = myGrades.FileTransfer(section, currentGradingPeriod);
//        Log.e("PPP", data)
//        Util.ExportToGoogleSheet(myContext, sheetName, data, "Export Grades", "ExportGrades")
//    }


}


private fun ViewRecord() {
    try {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listGrade.layoutManager = layoutmanager
        myGradeAdapter = GradeAdapter(this, gradeList)
        listGrade.adapter = myGradeAdapter
    } catch (e: Exception) {
        Log.e("err", e.toString())
    }
}

fun SetUpSectionGradeAdapter() {

    val layoutmanager = LinearLayoutManager(this)
    layoutmanager.orientation = LinearLayoutManager.VERTICAL;
    listGradeSection.layoutManager = layoutmanager
    gradeSectionAdapter = GradeSectionAdapter(this, sectionList)
    listGradeSection.adapter = gradeSectionAdapter

}


fun SectionUpdateListContent() {
    val db: DatabaseHandler = DatabaseHandler(this)
    val myList: List<SectionModel> = db.GetSectionPerSchool("BISCAST")
    sectionList.clear()



    for (e in myList) {
        sectionList.add(SectionModel(e.sectionCode, e.sectionName, e.school, e.status, e.Message, e.originalSection, e.subjectDescription, e.folderLink))

    }
    Log.e("4455", sectionList.size.toString())
}

private fun SetSpinnerAdapter() {
    val arrSection: Array<String> = Util.GetSectionList(this)
    var sectionAdapter: ArrayAdapter<String> =
        ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
    sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    cboSection.setAdapter(sectionAdapter); //   ActivityMain.cboSectionAct = findViewById(R.id.cboSectionActivity) as Spinner
}

private fun SetDefaultSection(currentSection: String) {
    var mycontext = this;
    val db: DatabaseHandler = DatabaseHandler(this) // var currentSection = db.GetCurrentSection();
    var index = Util.GetSectionIndex(currentSection, this)
    cboSection.setSelection(index)
}


fun ShowBracketGrades(min: Int, max: Double) {
    try {
        val db: TableActivity = TableActivity(this)
        val db1: Grades = Grades(this)
        val currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
        val section = Util.GRADE_SECTION
        var gradingPeriodEquivalent = currentGradingPeriod + "Equivalent"

        var gradingPeriodField = currentGradingPeriod + "Grade"
        val averageStatus = db.GetAverageStatus()
        var sql = ""
        if (averageStatus == "TRUE") {
            sql = """ select * from tbgrade_query
                    where CumulativeGrade >= $min
                    and CumulativeGrade <= $max
                    and sectioncode = '$section'
                    """.trimIndent()
        } else {
            val school = db1.GetSchool(section)
            if (school == "DEPED") {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    and   $gradingPeriodEquivalent >= $min
                    and   $gradingPeriodEquivalent <=$max
                    order by $gradingPeriodField
                    """.trimIndent()
            } else {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    and   $gradingPeriodField >= $min
                    and   $gradingPeriodField <=$max
                    order by $gradingPeriodField
                    """.trimIndent()
            }


        }
        Log.e("123432", sql)
        UpdateListContent(this, sql)
    } catch (e: Exception) {
        Log.e("helloerr", e.toString())
    }


}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun ChangeColorQuarter(gradingPeriod: String) {
    btnFirst.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
    btnSecond.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
    btnAverage.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY)); //        btnFourth.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
    val db: TableActivity = TableActivity(this)

    if (db.GetAverageStatus() == "TRUE") btnAverage.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
    else if (gradingPeriod == "FIRST") {
        btnFirst.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));

    } else if (gradingPeriod == "SECOND") {
        btnSecond.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
    }

}

fun CopyText(copyString: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", copyString)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
}


@RequiresApi(Build.VERSION_CODES.KITKAT)
fun PdfGradeSHeet(section: String, canvas: Canvas, myPaint: Paint, width: Float, semester: String) {


    myPaint.textSize = 12f
    canvas.drawText("Version ", 10F, 10F, myPaint);

    myPaint.textAlign = Paint.Align.CENTER
    myPaint.textSize = 22f
    canvas.drawText("Final Semestral Grades", (width / 2).toFloat(), 50F, myPaint);

    myPaint.textAlign = Paint.Align.LEFT
    myPaint.textSize = 14f
    var x = 100F
    var y = 100F


    val image1: Bitmap
    val image2: Bitmap
    val image3: Bitmap
    val image4: Bitmap

    image1 = BitmapFactory.decodeResource(getResources(), R.drawable.deped1)
    image2 = Bitmap.createScaledBitmap(image1, 80, 80, false)
    canvas.drawBitmap(image2, 20F, 10F, myPaint)

    image3 = BitmapFactory.decodeResource(getResources(), R.drawable.deped2)
    image4 = Bitmap.createScaledBitmap(image3, 80, 80, false)
    canvas.drawBitmap(image4, 450F, 0F, myPaint)


    canvas.drawText("REGION", x + 5, y, myPaint);
    canvas.drawText("SCHOOL NAME", x - 20, y + 20, myPaint);
    canvas.drawText("DIVISION", x + 150, y, myPaint);
    canvas.drawText("SCHOOL ID", 425F, y, myPaint);
    canvas.drawText("SCHOOL NAME", 400F, y + 20, myPaint);

    canvas.drawText("V", 210f, y, myPaint);
    myPaint.textSize = 10F
    canvas.drawText("CAMARINES SUR NATIONAl HIGH SCHOOL ", 190F, y + 20, myPaint);
    myPaint.textSize = 14f
    canvas.drawText("NAGA CITY", 320F, y, myPaint);
    canvas.drawText("302264", 510F, y, myPaint);
    canvas.drawText(Util.CURRENT_SCHOOLYEAR,  510F, y + 20, myPaint);



    myPaint.setStrokeWidth(0.5f);
    canvas.drawLine(180f, 87f, 245f, 87f, myPaint);
    canvas.drawLine(180f, 105f, 395f, 105f, myPaint);
    canvas.drawLine(180f, 123f, 395f, 123f, myPaint);
    canvas.drawLine(310f, 87f, 395f, 87f, myPaint);

    canvas.drawLine(500f, 87f, 580f, 87f, myPaint);
    canvas.drawLine(500f, 105f, 580f, 105f, myPaint);
    canvas.drawLine(500f, 123f, 580f, 123f, myPaint);

    Drawline(canvas, myPaint, 180, 87, 180, 123, Color.BLACK)
    Drawline(canvas, myPaint, 245, 87, 245, 105, Color.BLACK)
    Drawline(canvas, myPaint, 395, 87, 395, 123, Color.BLACK)
    Drawline(canvas, myPaint, 310, 87, 310, 105, Color.BLACK)
    Drawline(canvas, myPaint, 500, 87, 500, 123, Color.BLACK)
    Drawline(canvas, myPaint, 580, 87, 580, 123, Color.BLACK)
    val db: DatabaseHandler = DatabaseHandler(this)
    myPaint.textSize = 10f
    canvas.drawText("LEARNERS' NAMES", 30f, 175f, myPaint);
    canvas.drawText("GRADE AND SECTION: " + db.GetSectionDetail(section, "SECTION_NAME"), 165f, 160f, myPaint);
    myPaint.textSize = 8f
    canvas.drawText("TEACHER: MARVIN MARQUEZ", 163f, 180f, myPaint);
    canvas.drawText("SEMESTER:" + semester, 350f, 150f, myPaint);
    canvas.drawText("SUBJECT:" + db.GetSectionDetail(section, "SUBJECT_DESC"), 350f, 165f, myPaint);
    canvas.drawText("TRACK:TVL", 350f, 180f, myPaint);


    if (semester == "FIRST") {
        canvas.drawText("FIRST QUARTER", 165f, 195f, myPaint);
        canvas.drawText("SECOND QUARTER", 255f, 195f, myPaint);
        canvas.drawText("FIRST SEMESTER FINAL", 350f, 195f, myPaint);
    } else if (semester == "SECOND") {
        canvas.drawText("THIRD QUARTER", 165f, 195f, myPaint);
        canvas.drawText("FOURTH QUARTER", 255f, 195f, myPaint);
        canvas.drawText("SECOND SEMESTER FINAL", 350f, 195f, myPaint);
    }


    canvas.drawText("REMARK", 500f, 195f, myPaint);


    Drawline(canvas, myPaint, 10, 140, 590, 140, Color.BLACK)
    Drawline(canvas, myPaint, 10, 200, 590, 200, Color.BLACK)
    Drawline(canvas, myPaint, 345, 155, 590, 155, Color.BLACK)
    Drawline(canvas, myPaint, 160, 185, 590, 185, Color.RED)
    Drawline(canvas, myPaint, 160, 200, 590, 200, Color.GREEN)
    Drawline(canvas, myPaint, 160, 170, 590, 170, Color.BLUE)

    //VERTICAL
    Drawline(canvas, myPaint, 10, 140, 10, 200, Color.RED)
    Drawline(canvas, myPaint, 160, 140, 160, 200, Color.RED)
    Drawline(canvas, myPaint, 345, 140, 345, 200, Color.GREEN)
    Drawline(canvas, myPaint, 590, 140, 590, 200, Color.BLUE)

    var yy = 210

    for (i in 1..50 step 1) { //  canvas.drawText(i.toString(),10F, (yy-4).toFloat(), myPaint);
        Drawline(canvas, myPaint, 10, yy, 590, yy, Color.BLACK)
        yy = yy + 12
    }

    Drawline(canvas, myPaint, 10, 200, 10, yy - 12, Color.BLACK)
    Drawline(canvas, myPaint, 35, 200, 35, yy - 12, Color.RED)
    Drawline(canvas, myPaint, 160, 200, 160, yy - 12, Color.BLACK)
    Drawline(canvas, myPaint, 345, 200, 345, yy - 12, Color.BLACK)
    Drawline(canvas, myPaint, 240, 200, 240, yy - 12, Color.BLACK)
    Drawline(canvas, myPaint, 240, 185, 240, yy - 12, Color.BLACK)
    Drawline(canvas, myPaint, 470, 185, 470, yy - 12, Color.BLACK)
    Drawline(canvas, myPaint, 590, 200, 590, yy - 12, Color.BLACK)


    val dbGrade: Grades = Grades(myContext)
    val theGrade: List<GradeModel>
    val sql = """ select * from tbgrade_query
                    where sectioncode = '$section'
                   order by gender desc, lastname
                    """.trimIndent()

    theGrade = dbGrade.GetGradeList(sql)
    yy = 210
    canvas.drawText("MALE", 40F, (yy - 3).toFloat(), myPaint);
    var ctr = 1;
    yy = 222 //var hen
    var gender = "MALE"
    var gradingPeriod = Util.GetCurrentGradingPeriod(myContext)

    for (e in theGrade) {
        if (gender != e.gender) {
            ctr = 1
            canvas.drawText("FEMALE", 40F, (yy - 3).toFloat(), myPaint);
            yy = yy + 12
        }
        canvas.drawText(ctr.toString(), 20F, (yy - 3).toFloat(), myPaint);
        canvas.drawText(e.lastname + ", " + e.firstname, 40F, (yy - 3).toFloat(), myPaint);
        if (gradingPeriod == "FIRST") {
            canvas.drawText(e.firstEquivalent.toInt()
                                .toString(), 190F, (yy - 3).toFloat(), myPaint);
        }

        if (gradingPeriod == "SECOND") {
            canvas.drawText(e.firstEquivalent.toInt()
                                .toString(), 190F, (yy - 3).toFloat(), myPaint);
            canvas.drawText(e.secondEquivalent.toInt()
                                .toString(), 280F, (yy - 3).toFloat(), myPaint);
            canvas.drawText(e.CumulativeGrade.toInt()
                                .toString(), 400F, (yy - 3).toFloat(), myPaint);
            canvas.drawText(e.remark.toString(), 500F, (yy - 3).toFloat(), myPaint);
        }

        yy = yy + 12
        gender = e.gender
        ctr++
    }
    myPaint.textSize = 10f
    yy = 755
    canvas.drawText("Prepared:", 70F, (yy + 75).toFloat(), myPaint);
    canvas.drawText("MARVIN C. MARQUEZ", 40F, (yy + 100).toFloat(), myPaint);
    canvas.drawText("Teacher III", 70F, (yy + 112).toFloat(), myPaint);

    canvas.drawText("Checked:", 220F, (yy + 75).toFloat(), myPaint);
    canvas.drawText("MELISSA B. BOBOS", 210F, (yy + 100).toFloat(), myPaint);
    canvas.drawText("Assistant Principal II, SHS", 200F, (yy + 112).toFloat(), myPaint);

    canvas.drawText("Approved:", 440F, (yy + 75).toFloat(), myPaint);
    canvas.drawText("SULPICIO C. ALFEREZ III, Ph,D.", 400F, (yy + 100).toFloat(), myPaint);
    canvas.drawText("Principal IV:", 440F, (yy + 112).toFloat(), myPaint);


    //        Drawline(canvas, myPaint,130, 155,  590, 155, Color.BLUE)
    //        Drawline(canvas, myPaint,10, 200,  590, 200, Color.BLUE)
    //        Drawline(canvas, myPaint,10, 200,  590, 200, Color.BLUE)
    //        Drawline(canvas, myPaint,10, 200,  590, 200, Color.BLUE)
    //        Drawline(canvas, myPaint,1  bvg30, 165,  590, 170, Color.GREEN)
    //        Drawline(canvas, myPaint,130, 180 , 590, 180, Color.GREEN)
    //        Drawline(canvas, myPaint,130, 190,  590, 190, Color.GREEN)
    //        Drawline(canvas, myPaint,130, 215  ,590, 190, Color.GREEN)


}

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun PdfClassRecord(section: String, canvas: Canvas, myPaint: Paint, width: Float, semester: String, activity: List<ActivityModel>) {


    myPaint.textSize = 12f

    myPaint.textAlign = Paint.Align.CENTER
    myPaint.textSize = 22f
    canvas.drawText("Senior High School Class Record ", (width / 2).toFloat(), 50F, myPaint);

    myPaint.textAlign = Paint.Align.LEFT
    myPaint.textSize = 14f
    var x = 180F
    var y = 100F
    val image1: Bitmap
    val image2: Bitmap
    val image3: Bitmap
    val image4: Bitmap

    image1 = BitmapFactory.decodeResource(getResources(), R.drawable.deped1)
    image2 = Bitmap.createScaledBitmap(image1, 100, 100, false)
    canvas.drawBitmap(image2, 50F, 10F, myPaint)

    image3 = BitmapFactory.decodeResource(getResources(), R.drawable.deped2)
    image4 = Bitmap.createScaledBitmap(image3, 100, 100, false)
    canvas.drawBitmap(image4, 810F, 10F, myPaint)

    canvas.drawText("REGION", x + 5, y, myPaint);
    canvas.drawText("SCHOOL NAME", x - 20, y + 20, myPaint);
    canvas.drawText("DIVISION", x + 150, y, myPaint);
    canvas.drawText("SCHOOL ID", 585F, y, myPaint);
    canvas.drawText("SCHOOL NAME", 585F, y + 20, myPaint);

    canvas.drawText("V", 270f, y, myPaint);
    myPaint.textSize = 10F
    canvas.drawText("CAMARINES SUR NATIONAl HIGH SCHOOL ", 263F, y + 20, myPaint);
    myPaint.textSize = 14f
    canvas.drawText("NAGA CITY", 400F, y, myPaint);
    canvas.drawText("302264", 695F, y, myPaint);
    canvas.drawText(Util.CURRENT_SCHOOLYEAR, 695F, y + 20, myPaint);


    //                for (i in 10..900 step 10) {
    //                    myPaint.textSize = 6f
    //                    canvas.drawText(i.toString(), i.toFloat(), 75F, myPaint);
    //                }
    //
    //                for (i in 10..950 step 10) {
    //                    myPaint.textSize = 6f
    //                    canvas.drawText(i.toString(), 100f, i.toFloat(), myPaint);
    //                }
    myPaint.setStrokeWidth(0.5f); //        canvas.drawLine(180f, 87f, 245f, 87f, myPaint);
    //        canvas.drawLine(180f, 105f, 395f, 105f, myPaint);
    //        canvas.drawLine(180f, 123f, 395f, 123f, myPaint);
    //        canvas.drawLine(310f, 87f, 395f, 87f, myPaint);
    //
    //        canvas.drawLine(500f, 87f, 580f, 87f, myPaint);
    //        canvas.drawLine(500f, 105f, 580f, 105f, myPaint);
    //        canvas.drawLine(500f, 123f, 580f, 123f, myPaint);

    Drawline(canvas, myPaint, 260, 87, 305, 87, Color.BLACK, "A")
    Drawline(canvas, myPaint, 260, 123, 480, 123, Color.BLACK, "A1")
    Drawline(canvas, myPaint, 260, 105, 480, 105, Color.BLACK, "B")
    Drawline(canvas, myPaint, 305, 87, 305, 105, Color.BLACK, "B1") //  Drawline(canvas, myPaint, 180, 105, 395, 105, Color.BLACK, "C")


    Drawline(canvas, myPaint, 260, 87, 260, 123, Color.BLACK, "D")
    Drawline(canvas, myPaint, 395, 87, 395, 105, Color.BLACK, "F")
    Drawline(canvas, myPaint, 480, 87, 480, 123, Color.BLACK, "F1")
    Drawline(canvas, myPaint, 395, 87, 480, 87, Color.BLACK, "F2") //Drawline(canvas, myPaint, 310, 87, 310, 105, Color.BLACK, "G")
    Drawline(canvas, myPaint, 580, 87, 580, 123, Color.BLACK, "IV")
    Drawline(canvas, myPaint, 775, 87, 775, 123, Color.BLACK, "JV")
    Drawline(canvas, myPaint, 690, 87, 690, 123, Color.BLACK, "KV")
    Drawline(canvas, myPaint, 580, 87, 775, 87, Color.BLACK, "IH")
    Drawline(canvas, myPaint, 580, 123, 775, 123, Color.BLACK, "JH")
    Drawline(canvas, myPaint, 580, 105, 775, 105, Color.BLACK, "KH")

    val db: DatabaseHandler = DatabaseHandler(this)
    myPaint.textSize = 10f
    canvas.drawText("LEARNERS' NAMES", 30f, 195f, myPaint);
    canvas.drawText("WRITTEN WORKS(20%)", 260f, 195f, myPaint);
    canvas.drawText("PERFORMANCE TASKS(60%)", 520f, 195f, myPaint);
    canvas.drawText("EXAM(20%)", 740f, 195f, myPaint);
    myPaint.textSize = 9f
    canvas.drawText("Initial", 815f, 195f, myPaint);
    canvas.drawText("Grades", 815f, 205f, myPaint);

    canvas.drawText("Quartertly", 855f, 195f, myPaint);
    canvas.drawText("Grades", 860f, 205f, myPaint)


    canvas.drawText("FIRST SEMESTER ", 30f, 160f, myPaint);
    canvas.drawText("GRADE AND SECTION: " + db.GetSectionDetail(section, "SECTION_NAME"), 165f, 160f, myPaint);
    canvas.drawText("TEACHER: MARVIN C. MARQUEZ", 450f, 150f, myPaint);
    canvas.drawText("SEMESTER: $semester" ,  450f, 165f, myPaint);
    canvas.drawText("SUBJECT:" + db.GetSectionDetail(section, "SUBJECT_DESC"), 725f, 150f, myPaint);
    canvas.drawText("TRACK: Technical & Vocational Livelihood", 725f, 165f, myPaint);




    myPaint.textSize = 8f







    Drawline(canvas, myPaint, 10, 140, 900, 140, Color.BLACK)
    Drawline(canvas, myPaint, 440, 155, 900, 155, Color.BLACK)
    Drawline(canvas, myPaint, 10, 170, 900, 170, Color.BLUE, "H3")

    //VERTICAL
    Drawline(canvas, myPaint, 10, 140, 10, 573, Color.RED)
    Drawline(canvas, myPaint, 160, 140, 160, 573, Color.RED)

    Drawline(canvas, myPaint, 440, 140, 440, 210, Color.RED)
    Drawline(canvas, myPaint, 720, 140, 720, 210, Color.RED)
    Drawline(canvas, myPaint, 800, 170, 800, 210, Color.RED)
    Drawline(canvas, myPaint, 850, 170, 850, 210, Color.RED)
    Drawline(canvas, myPaint, 900, 140, 900, 210, Color.RED)

    Drawline(canvas, myPaint, 160, 210, 800, 210, Color.BLACK)
    Drawline(canvas, myPaint, 10, 222, 800, 222, Color.BLACK)
    var yy = 235
    for (i in 1..27 step 1) { //  canvas.drawText(i.toString(),10F, (yy-4).toFloat(), myPaint);
        Drawline(canvas, myPaint, 10, yy, 900, yy, Color.BLACK)
        yy = yy + 13
    }

    var xx = 180
    for (i in 1..31 step 1) { //  canvas.drawText(i.toString(),10F, (yy-4).toFloat(), myPaint);
        Drawline(canvas, myPaint, xx, 210, xx, 573, Color.BLACK)



        if (i >= 1 && i <= 10) canvas.drawText(i.toString(), xx - 15.toFloat(), 220F, myPaint);
        else if (i >= 14 && i <= 23) canvas.drawText((i - 13).toString(), xx - 15.toFloat(), 220F, myPaint);
        else if (i == 11 || i == 24) canvas.drawText("TOTAL", xx - 28.toFloat(), 220F, myPaint);
        else if (i == 12 || i == 25 || i == 28) canvas.drawText("PS", xx - 20.toFloat(), 220F, myPaint);
        else if (i == 13 || i == 26 || i == 29) canvas.drawText("WS", xx - 20.toFloat(), 220F, myPaint);
        else if (i == 27) canvas.drawText("EXAM", xx - 28.toFloat(), 220F, myPaint); // PrintText(xx,i)

        if (i == 10 || i == 23) xx = xx + 30
        else if (i == 11 || i == 12) xx = xx + 25
        else if (i == 24 || i == 25) xx = xx + 25
        else if (i == 26) xx = xx + 30
        else if (i == 27 || i == 28) xx = xx + 25
        else if (i == 31 || i == 32) xx = xx + 25
        else if (i == 29 || i == 30) xx = xx + 50
        else xx = xx + 20

    }

    //        Drawline(canvas, myPaint, 10, 200, 10, yy - 12, Color.BLACK)
    //        Drawline(canvas, myPaint, 35, 200, 35, yy - 12, Color.RED)
    //        Drawline(canvas, myPaint, 160, 200, 160, yy - 12, Color.BLACK)
    //        Drawline(canvas, myPaint, 345, 200, 345, yy - 12, Color.BLACK)
    //        Drawline(canvas, myPaint, 240, 200, 240, yy - 12, Color.BLACK)
    //        Drawline(canvas, myPaint, 240, 185, 240, yy - 12, Color.BLACK)
    //        Drawline(canvas, myPaint, 470, 185, 470, yy - 12, Color.BLACK)
    //        Drawline(canvas, myPaint, 590, 200, 590, yy - 12, Color.BLACK)


    var totalquiz = 0
    var totalactivity = 0
    var activityLocation = 445 //165
    var quizLocation = 165 //445
    Log.e("AAA", "activityLocation")
    for (e in activity) {
        Log.e("AAA", e.category.toString())
        if (e.category == "PT") {
            canvas.drawText(e.item.toString(), activityLocation.toFloat(), 230F, myPaint);
            activityLocation = activityLocation + 20
            Log.e("AAA", e.item.toString())
            totalactivity = totalactivity + e.item
        } else if (e.category == "QUIZ") {
            canvas.drawText(e.item.toString(), quizLocation.toFloat(), 230F, myPaint);
            quizLocation = quizLocation + 20
            totalquiz = totalquiz + e.item
        } else if (e.category == "EXAM") {
            canvas.drawText(e.item.toString(), 725f, 230F, myPaint);
            Log.e("AAA", e.item.toString())
        }
    }

    canvas.drawText(totalquiz.toString(), 370f, 230F, myPaint);
    canvas.drawText("100", 395f, 230F, myPaint);
    canvas.drawText("20%", 420f, 230F, myPaint);

    canvas.drawText(totalactivity.toString(), 650f, 230F, myPaint);
    canvas.drawText("100", 675f, 230F, myPaint);
    canvas.drawText("60%", 700f, 230F, myPaint);

    canvas.drawText("100", 755f, 230F, myPaint);
    canvas.drawText("20%", 780f, 230F, myPaint);


    PrintRecordStudent(canvas, myPaint, 260, activity, section, 1);
}

fun PdfClassRecordSecond(section: String, canvas: Canvas, myPaint: Paint, width: Float, semester: String, activity: List<ActivityModel>) {


    var yy = 50
    for (i in 1..28 step 1) { //  canvas.drawText(i.toString(),10F, (yy-4).toFloat(), myPaint);
        Drawline(canvas, myPaint, 10, yy, 900, yy, Color.BLACK)
        yy = yy + 13
    }

    var xx = 180
    Drawline(canvas, myPaint, 10, 50, 10, 401, Color.BLACK)
    Drawline(canvas, myPaint, 160, 50, 160, 401, Color.BLACK)
    for (i in 1..31 step 1) { //  canvas.drawText(i.toString(),10F, (yy-4).toFloat(), myPaint);
        Drawline(canvas, myPaint, xx, 50, xx, 401, Color.BLACK)




        if (i == 10 || i == 23) xx = xx + 30
        else if (i == 11 || i == 12) xx = xx + 25
        else if (i == 24 || i == 25) xx = xx + 25
        else if (i == 26) xx = xx + 30
        else if (i == 27 || i == 28) xx = xx + 25
        else if (i == 31 || i == 32) xx = xx + 25
        else if (i == 29 || i == 30) xx = xx + 50
        else xx = xx + 20

    }
    PrintRecordStudent(canvas, myPaint, 62, activity, section, 2);
    Log.e("kkk", GetPath())

    canvas.drawText("Prepared:", 200F, 420F, myPaint);
    canvas.drawText("MARVIN C. MARQUEZ", 180F, 445F, myPaint);
    canvas.drawText("Teacher III", 200F, 457F, myPaint);

    //        canvas.drawText("Checked:" ,220F, 420F, myPaint);
    //        canvas.drawText("MELISSA B. BOBOS" ,210F, 445F, myPaint);
    //        canvas.drawText("Assistant Principal II, SHS" ,200F, 457, myPaint);

    canvas.drawText("Checked:", 440F, 420F, myPaint);
    canvas.drawText("MELISSA B. BOBOS", 430F, 445F, myPaint);
    canvas.drawText("Assistant Principal II, SHS", 420F, 457F, myPaint);

    //        canvas.drawText("Approved:" ,440F, (yy+75).toFloat(), myPaint);
    //        canvas.drawText("SULPICIO C. ALFEREZ III, Ph,D." ,400F, (yy+100).toFloat(), myPaint);
    //        canvas.drawText("Principal IV:" ,440F, (yy+112).toFloat(), myPaint);

    canvas.drawText("Approved:", 660F, 420F, myPaint);
    canvas.drawText("SULPICIO C. ALFEREZ III, Ph,D.", 620F, 445F, myPaint);
    canvas.drawText("Principal IV:", 660F, 457F, myPaint);

    Ruler(canvas, myPaint)

}

fun Ruler(canvas: Canvas, myPaint: Paint) {
    if (RULER_VIEW == true) {
        for (i in 10..600 step 10) {
            myPaint.textSize = 6f
            canvas.drawText(i.toString(), i.toFloat(), 400F, myPaint);
        }

        for (i in 10..950 step 10) {
            myPaint.textSize = 6f
            canvas.drawText(i.toString(), 100f, i.toFloat(), myPaint);
        }
    }
}


fun PrintRecordStudent(canvas: Canvas, myPaint: Paint, y: Int, activity: List<ActivityModel>, section: String, page: Int) {
    val dbGrade: Grades = Grades(myContext)
    val theGrade: List<GradeModel>
    val sql = """ select * from tbgrade_query
                    where sectioncode = '$section'
                   order by gender desc, lastname
                    """.trimIndent()
    myPaint.textSize = 8f
    theGrade = dbGrade.GetGradeList(sql)
    if (page == 1) {
        canvas.drawText("HIGHEST POSSIBLE SCORE", 30F, 230F, myPaint);
        canvas.drawText("MALE", 30F, 243F, myPaint);
    }
    var ctr = 1;
    var yy = y
    var gender = "MALE"
    var gradingPeriod = Util.GetCurrentGradingPeriod(myContext)
    var studCount = 0
    val db: DatabaseHandler = DatabaseHandler(this)


    for (f in theGrade) {

        studCount++
        if (studCount < 26 && page == 2) continue;
        if (studCount == 26 && f.gender == "MALE" && page == 2) ctr = 26

        if (gender != f.gender) {
            ctr = 1
            canvas.drawText("FEMALE", 40F, (yy - 3).toFloat(), myPaint);
            yy = yy + 13
        }
        canvas.drawText(ctr.toString(), 20F, (yy - 3).toFloat(), myPaint);
        canvas.drawText(f.lastname + ", " + f.firstname, 40F, (yy - 3).toFloat(), myPaint);

        var totalQuiz = 0
        var totalactivity = 0

        var activityLocation = 445 //165
        var quizLocation = 165 //445
        var totalQuizScore = 0
        var totalActivityScore = 0
        var score = 0;
        var examItem = 0
        var examScore = 0
        for (e in activity) {
            Log.e("AAA", e.category.toString())
            if (e.category == "PT") {
                score = db.GetIndividualActivityScore(section, f.studentNo, e.activityCode)
                canvas.drawText(score.toString(), activityLocation.toFloat(), yy.toFloat(), myPaint);
                activityLocation = activityLocation + 20
                totalactivity = totalactivity + e.item
                totalActivityScore = totalActivityScore + score
            } else if (e.category == "QUIZ") {
                score = db.GetIndividualActivityScore(section, f.studentNo, e.activityCode)
                canvas.drawText(score.toString(), quizLocation.toFloat(), yy.toFloat(), myPaint);
                quizLocation = quizLocation + 20
                totalQuiz = totalQuiz + e.item
                totalQuizScore = totalQuizScore + score

            } else if (e.category == "EXAM") {
                examScore = db.GetIndividualActivityScore(section, f.studentNo, e.activityCode)
                examItem = e.item
                canvas.drawText(examScore.toString(), 730f, yy.toFloat(), myPaint);
                Log.e("AAA", e.item.toString())
            }
        }


        var psActivity = totalActivityScore.toDouble() / totalactivity.toDouble() * 100
        var wsActivity = psActivity * .6

        var psQuiz = totalQuizScore.toDouble() / totalQuiz.toDouble() * 100
        var wsQuiz = psQuiz * .2


        var psExam = examScore.toDouble() / examItem.toDouble() * 100
        var wsExam = psExam * .2

        canvas.drawText(totalQuizScore.toString(), 370f, yy.toFloat(), myPaint);
        canvas.drawText(totalQuizScore.toString(), 370f, yy.toFloat(), myPaint);
        canvas.drawText(String.format("%.2f", psQuiz), 391f, yy.toFloat(), myPaint);
        canvas.drawText(String.format("%.2f", wsQuiz), 416f, yy.toFloat(), myPaint);

        canvas.drawText(totalActivityScore.toString(), 650f, yy.toFloat(), myPaint);
        canvas.drawText(String.format("%.2f", psActivity), 671f, yy.toFloat(), myPaint);
        canvas.drawText(String.format("%.2f", wsActivity), 696f, yy.toFloat(), myPaint);


        canvas.drawText(String.format("%.2f", psExam), 751f, yy.toFloat(), myPaint);
        canvas.drawText(String.format("%.2f", wsExam), 776f, yy.toFloat(), myPaint);


        var gradingPeriod = Util.GetCurrentGradingPeriod(myContext)
        if (gradingPeriod == "FIRST") {
            canvas.drawText(String.format("%.2f", f.firstGrade), 820f, yy.toFloat(), myPaint);
            canvas.drawText(String.format("%.2f", f.firstEquivalent), 870f, yy.toFloat(), myPaint);
        } else {
            canvas.drawText(String.format("%.2f", f.secondGrade), 820f, yy.toFloat(), myPaint);
            canvas.drawText(String.format("%.2f", f.secondEquivalent), 870f, yy.toFloat(), myPaint);

        }


        yy = yy + 13
        gender = f.gender
        ctr++


        if (studCount == 25 && page == 1) break
    }
}


fun Drawline(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int, color: Int, lineName: String = "") {
    paint.color = color
    val myPaint = Paint()
    myPaint.textSize = 6f
    if (lineNumberPrint == true) {
        canvas.drawText("(" + x1.toString() + "-" + y1.toString() + ")-" + lineName, x1.toFloat(), y1.toFloat(), myPaint);
    }
    canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint);

}

fun GetPath(): String {
    val con: ContextWrapper = ContextWrapper(getApplicationContext())
    Log.e("LLL", con.toString())
    val dir: File? = con.getExternalFilesDir("")
    val file: File = File(dir, "sf2.pdf")
    return file.getPath()
}

fun checkPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) return Environment.isExternalStorageManager()
    else {

        val readcheck =
            ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        val writecheck =
            ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return readcheck == PackageManager.PERMISSION_GRANTED && writecheck == PackageManager.PERMISSION_GRANTED
    }
}


fun requestPermission() {

    if (Build.VERSION.SDK_INT >= 30) {
        if (Environment.isExternalStorageManager()) {
        } else {
            val intent = Intent()
            intent.action =
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION //intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }
}

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
//            try {
//                val intent:Intent= Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//            }
//          //             intent.addCategory("android.intent.category.DEFAULT");
//               // intent.setData(Uri.parse(String.format("package:%s", new Object(HgetApplicationContextO.getPackageName()})) activityResultLauncher.launch(intent);
//              }catch (e:Exception)
//     {
//
//            val intent:Intent  = Intent()
//
//            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//            activityResultLauncher.launch(intent)
//
//        }
//
//    }
//
//    else
//
//    {
//
//        ActivityCompat.requestpermissions( activity: HainActivity.this,permission, requestCodc 30);
//
//    }

//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)


}


fun DatabaseHandler.GetSectionDetail(section: String, detail: String): String {
    var sql = """
                SELECT * FROM TBSECTION_QUERY
                WHERE SectionName= '$section'
                  """ //  SectionCode	SectionName	Status	School	Subject	Version	Message	SubjectCode	RealSectionName	SubjectDescription	PT	Quiz	Exam	Project	ClassStanding //  Participation	ClassStandingCoverage	RealSectionName:1

    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        if (detail == "SECTION_NAME") return cursor.getString(cursor.getColumnIndex("RealSectionName"))
        else if (detail == "SUBJECT_DESC") return cursor.getString(cursor.getColumnIndex("SubjectDescription"))
    }
    return ""
}


fun DatabaseHandler.GetIndividualActivityScore(sectionCode: String, studentNo: String, activityCode: String): Int {
    var sql = """   SELECT * FROM TBSCORE_QUERY 
                        WHERE StudentNo ='$studentNo' 
                        AND ActivityCode ='$activityCode' 
                        AND SectionCode ='$sectionCode' 
                 """
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("Score")).toInt()
    }
    return 0;

}


@SuppressLint("Range")
fun DatabaseHandler.GetSectionPerSchool(school: String): List<SectionModel> {
    val sectionList: ArrayList<SectionModel> = ArrayList<SectionModel>()


    var sql = ""
    sql =
        "SELECT * FROM tbsection_query where status='SHOW' and school = '$school' ORDER BY SectionName"


    var cursor = SQLSelect(sql, 401, 501)


    if (cursor!!.moveToFirst()) {
        do {

            var sectionCode =
                cursor!!.getString(cursor!!.getColumnIndex(DatabaseHandler.TBSECTION_CODE))
            var sectionName =
                cursor!!.getString(cursor!!.getColumnIndex(DatabaseHandler.TBSECTION_NAME))
            var school = cursor!!.getString(cursor!!.getColumnIndex("School"))
            var status = cursor!!.getString(cursor!!.getColumnIndex("Status"))
            var message = cursor!!.getString(cursor!!.getColumnIndex("Message"))
            var origSection = cursor!!.getString(cursor!!.getColumnIndex("RealSectionName"))
            var description = cursor!!.getString(cursor!!.getColumnIndex("SubjectDescription"))
            var folderLink =
                cursor!!.getString(cursor!!.getColumnIndex("SectionFolderLInk")) //            SubjectDescription	PT	Quiz	Exam	Project	ClassStanding
            //                    Participation	ClassStandingCoverage
            //                    RealSectionName	SectionCode	SectionName
            val myList =
                SectionModel(sectionCode, sectionName, school, status, message, origSection, description, folderLink)
            sectionList.add(myList)
        } while (cursor.moveToNext())
    }
    return sectionList
}








