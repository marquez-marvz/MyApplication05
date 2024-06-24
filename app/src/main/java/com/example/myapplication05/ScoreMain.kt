package com.example.myapplication05


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.fragment.GroupAdapter
import com.example.myapplication05.fragment.GroupFragment
import com.example.myapplication05.fragment.ScoreFragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.fragment_score.view.listGroup
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.group_row.view.btnGroupNumber
import kotlinx.android.synthetic.main.import_score.view.listImportScore
import kotlinx.android.synthetic.main.import_score.view.txtActivityCodeImport
import kotlinx.android.synthetic.main.import_score.view.txtDescriptionImport
import kotlinx.android.synthetic.main.import_score.view.txtSectionImport
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.remark_main.view.*
import kotlinx.android.synthetic.main.score_dialog.view.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_main.btnAscending
import kotlinx.android.synthetic.main.score_main.btnDescending
import kotlinx.android.synthetic.main.score_main.btnSearchFifth
import kotlinx.android.synthetic.main.score_main.btnSearchFirst
import kotlinx.android.synthetic.main.score_main.btnSearchFourth
import kotlinx.android.synthetic.main.score_main.btnSearchSecond
import kotlinx.android.synthetic.main.score_main.btnSearchThird
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_dialog.view.txtActivityCode
import kotlinx.android.synthetic.main.task_dialog.view.txtDescription
import kotlinx.android.synthetic.main.task_dialog_import.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_copy.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader


class ScoreMain : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    val myContext = this;
    var activityAdapter: ScoreSummary_ActivityAdapter? = null;
    var importAdapter: ScoreImportAdapter? = null;
    var activityList = arrayListOf<ActivityModel>()
    var importScoreList = arrayListOf<ScoreImportModel>()


    companion object {
        var scoreList = arrayListOf<ScoreModel>()
        var scoreCsvList = arrayListOf<ScoreCsvModel>()
        var activityList = arrayListOf<ActivityModel>()
        //0621
        var grpList = arrayListOf<GrpModel>()
        var grpScoreAdapter: GroupAdapter? = null;

        //0619
        var scoreAdapter: ScoreAdapter? = null;
        var scoreCardAdapter: ScoreAdapter? = null;
        var txtsection: TextView? = null;
        var txtactivitycode: TextView? = null;
        var txtactivitydesc: TextView? = null;
        var pie: PieChart? = null;

        var csvAdapter: ScoreCsvAdapter? = null;
        var GROUP_SEARCH: String = ""
        var NAME_SEARCH: String = ""
        var GRP_STATUS: Boolean = false
        var TXT_STATUS: Boolean = false
        var list = arrayListOf<EnrolleModel>()

        var SECTION = ""
        var TITLE = ""
        var ACT_CODE = ""
        var globalCategory = ""
        var globalsortOrder = ""
        var varBtnStatusData: Button? = null;
        var remarkList = arrayListOf<String>()


        fun ScoreUpdateListContent(context: Context, category: String = "", sortOrder: String = "", zeroOnly: Boolean = false) {
            val dbactivity: TableActivity = TableActivity(context)
            val activity: List<ScoreModel>
            Log.e("5621a", sortOrder)
            scoreList.clear()
            var section = txtsection!!.text.toString()
            var activityCode = txtactivitycode!!.text.toString()
            if (category == "REMARK") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "GROUP") {

                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "NAME") {
                activity = dbactivity.GetScoreList(section, activityCode, category, NAME_SEARCH)
            } else if (category == "SORT") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "SORT" && sortOrder == "FIRSTNAME") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "SORT" && sortOrder == "LASTTNAME") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "SORT" && sortOrder == "GENDER") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "SORT" && sortOrder == "GROUP") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "SEARCHLETTER" && sortOrder == "A-C") {
                activity =
                    dbactivity.GetScoreList(section, activityCode, category, sortOrder, zeroOnly)
            } else if (category == "SEARCHLETTER" && sortOrder == "D-J") {
                activity =
                    dbactivity.GetScoreList(section, activityCode, category, sortOrder, zeroOnly)
            } else if (category == "SEARCHLETTER" && sortOrder == "K-O") {
                activity =
                    dbactivity.GetScoreList(section, activityCode, category, sortOrder, zeroOnly)
            } else if (category == "SEARCHLETTER" && sortOrder == "P-R") {
                activity =
                    dbactivity.GetScoreList(section, activityCode, category, sortOrder, zeroOnly)
            } else if (category == "SEARCHLETTER" && sortOrder == "S-Z") {
                activity =
                    dbactivity.GetScoreList(section, activityCode, category, sortOrder, zeroOnly)
            } else if (category == "SORT" && sortOrder == "SUBMIT") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "STATUS") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else if (category == "SORT") {
                activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
            } else {
                activity = dbactivity.GetScoreList(section, activityCode)
            }


            for (e in activity) {
                scoreList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
            }
            Util.Msgbox(context, scoreList.size.toString()) //            var completeName: String,
        }

        fun ShowChart(context: Context) {
            val db: DatabaseHandler = DatabaseHandler(context)
            var remarkList = arrayListOf<PieEntry>()
            Log.e("4532", Util.ACT_CURRENT_SECTION + "    " + Util.ACT_CODE)
            remarkList = db.GetActivityRecordRemark(Util.ACT_CURRENT_SECTION, Util.ACT_CODE)
            Chart.SetUpPie(pie, remarkList, Util.ACT_CODE + "\n" + Util.ACT_DESCRIPTION)
            pie!!.notifyDataSetChanged();
            pie!!.invalidate();
        }

    }


    @SuppressLint("MissingInflatedId") //OC123
    override fun onCreate(savedInstanceState: Bundle?) {
        Util.ACT_OPEN_FOLDER = false
        Util.ACT_RUBRIC = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_main)
        txtSectionScore.setText(Util.ACT_CURRENT_SECTION)
        txtScoreActivityCode.setText(Util.ACT_CODE)
        txtSectionActivityDesc.setText(Util.ACT_DESCRIPTION)
        btnScoreItem.setText(Util.ACT_ITEM.toString())


        //        txtDescription.setText(Util.ACT_DESCRIPTION)
       // listActiviity.isVisible = true

        ActivityUpdateListContent(this)
        SetUpActivityAdapter()


        //  .toString()) //       final  scrollView = (HorizontalScrollView)findViewById    (R.id.horizontalScrollView);
        //0620
        var hview: HorizontalScrollView? = null;
        hview = findViewById(R.id.hview) as HorizontalScrollView
        txtsection = findViewById(R.id.txtSectionScore) as TextView
        txtactivitycode = findViewById(R.id.txtScoreActivityCode) as TextView
        txtactivitydesc = findViewById(R.id.txtSectionActivityDesc) as TextView
        pie = findViewById(R.id.pieChartActivity) as PieChart
        var db: DatabaseHandler = DatabaseHandler(this)
        remarkList = db.GetActivityRemark(Util.ACT_CURRENT_SECTION, Util.ACT_CODE)
        ShowChart(this)
        SetSpinnerAdapter()
        ScoreUpdateListContent(this)
        ScoreViewRecord()
        Util.CHART_STATUS = false
        txtSectionActivityDesc.isEnabled = false
        btnScoreItem.isEnabled = false

        //0621
        ShowGroupList(this,Util.ACT_CURRENT_SECTION )
        SetUpGroupAdapter()
        Util.HVIEW = true
        hview!!.setOnTouchListener(OnTouch())

        //        btnRemark.setOnClickListener {
        //            val dlgstudent = LayoutInflater.from(this).inflate(R.layout.score_import, null)
        //            val mBuilder = AlertDialog.Builder(this).setView(dlgstudent).setTitle("Import Score")
        //            val studentDialog = mBuilder.show()
        //            studentDialog.setCanceledOnTouchOutside(false);
        //            var dataText = ReadCSV()
        //            Log.e("4356", dataText)
        //            dlgstudent.txtFileData.setText(dataText)
        //
        //
        //            dlgstudent.btnSaveImportRecord.setOnClickListener {
        //                var sss = dlgstudent.txtFileData.text.toString()
        //                var token = sss.split("\n").toTypedArray()
        //                var x = 0;
        //
        //                while (x < token.size - 1) {
        //                    Log.e("1157--" + x, token[x])
        //                    var tokenNew = token[x].split("\t").toTypedArray()
        //                    Log.e("1157" + x, "SN" + tokenNew[0])
        //                    Log.e("1157" + x, "NAME" + tokenNew[1])
        //                    Log.e("1157" + x, "SCORE" + tokenNew[2])
        //                    Log.e("1157" + x, "SCORE" + (tokenNew[2] != "-").toString())
        //                    var studentNumber = tokenNew[0]
        //                    var score = 0
        //                    var SubmitStatus = "-"
        //                    if (tokenNew[2] != "-") {
        //                        score = tokenNew[2].toInt()
        //                        SubmitStatus = "OK"
        //
        //                    }
        //
        //                    Log.e("1157" + x, score.toString() + "    " + SubmitStatus)
        //                    txtSectionScore.setText(Util.ACT_CURRENT_SECTION)
        //                    txtActivityCode.setText(Util.ACT_CODE)
        //                    val db: TableActivity = TableActivity(this)
        //                    db.UpdateStudentRecord(Util.ACT_CODE, Util.ACT_CURRENT_SECTION, studentNumber, score, "", SubmitStatus)
        //                    x++;
        //                }
        //
        //
        //            }

        //            val mydb: DatabaseHandler = DatabaseHandler(context)
        //            val student = mydb.GetOneStudentRecord(studID)
        //            val dlgstudent = LayoutInflater.from(context).inflate(R.layout.student_dialog, null)
        //            val mBuilder = AlertDialog.Builder(context).setView(dlgstudent).setTitle("Manage Student")
        //            val studentDialog = mBuilder.show()
        //            studentDialog.setCanceledOnTouchOutside(false);
        //            val arrSection: Array<String> = Util.GetSectionList(context)
        //   }

        //0621
        btnScoreEdit.setOnClickListener {
            btnScoreEdit.isVisible = false
            btnScoreSave.isVisible = true
            txtSectionActivityDesc.isEnabled = true
            btnScoreItem.isEnabled = true
        }

        //0622


        btnActivityOrGroup.setOnClickListener {
            if (btnActivityOrGroup.text.toString() == "ACTIVITY") {
                btnActivityOrGroup.text = "GROUP"
                listGrouping.isVisible = true
                listActiviity.isVisible = false
                btnAddGroup.isVisible = true
            }
            else{
                btnActivityOrGroup.text = "ACTIVITY"
                listGrouping.isVisible = false
                listActiviity.isVisible = true
                btnAddGroup.isVisible =false
            }

        }


        btnScoreItem.setOnClickListener {
            var num = btnScoreItem.text.toString().toInt() + 5
            btnScoreItem.setText(num.toString())
            UpdateactivityRecord()
        }

        btnScoreItem.setOnLongClickListener {
            btnScoreItem.setText("0")
            UpdateactivityRecord()
            true
        }

//        0621
        btnAddGroup.setOnClickListener {
//            val dlgremark = LayoutInflater.from(this).inflate(R.layout.rubrics, null)
//            val mBuilder = AlertDialog.Builder(this).setView(dlgremark).setTitle("")

            val dlgsection =
                LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgsection)
                .setTitle("Do you want to add group d ")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //                dlgsection.txtSectionCode.setText(db.GetNewSectionCode())
            //                dlgsection.txtSectionCode.isEnabled= false

            dlgsection.btnYes.setOnClickListener { //
                val db2: DatabaseHandler = DatabaseHandler(this)
                db2.AddGroup(db2.GetCurrentSection())
                ShowGroupList(this, Util.ACT_CURRENT_SECTION)
                grpScoreAdapter!!.notifyDataSetChanged()
                inputDialog.dismiss()
            }

            dlgsection.btnNo.setOnClickListener { //
                inputDialog.dismiss()
            }
        }

        btnScoreSave.setOnClickListener {
            UpdateactivityRecord()
            btnScoreEdit.isVisible = true
            btnScoreSave.isVisible = false
            txtSectionActivityDesc.isEnabled = false
            btnScoreItem.isEnabled = false
        }






        btnRubric.setOnClickListener {
            val dlgremark = LayoutInflater.from(this).inflate(R.layout.rubrics, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgremark).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            Rubric.alert = inputDialog
            var ppp: Rubric = Rubric()
            val section = txtSectionScore.text.toString()
            val actCode = txtScoreActivityCode.text.toString()
            ppp.DisplayRenark(dlgremark, this, section, actCode)


        }



        btnCopyNoActivity.setOnClickListener {
            val dbactivity: TableActivity = TableActivity(myContext)
            val activity: List<ScoreModel>
            var section = txtsection!!.text.toString()

            var activityCode = txtactivitycode!!.text.toString()
            var activityDesc = txtSectionActivityDesc!!.text.toString()
            activity = dbactivity.GetScoreList(section, activityCode, "SORT", "LASTNAME")
            var noActivity =
                activityDesc + " Status" + "\n" // noActivity = txtDescription!!.text.toString() + " STATUS  \n\n" //= "SORT" && search_string == "LASTNAME"
            for (e in scoreList) {
                noActivity = noActivity + e.lastnanme + "-" + e.remark + "\n"
            }

            CopyText(noActivity)

        }

        btnAscending.setOnClickListener {
            ScoreUpdateListContent(myContext, "SORT", "DESC")
            btnAscending.setVisibility(View.INVISIBLE);
            btnDescending.setVisibility(View.VISIBLE);
            scoreAdapter!!.notifyDataSetChanged()
            globalCategory = "SORT"
        }

        //0619
        btnViewListView.setOnClickListener {
            if (btnViewListView.text.toString() == "LIST") {
                listScore.isVisible = false
                listScoreCard.isVisible = true
                listActiviity.isVisible = false
                btnViewListView.text = "CARD"
                ScoreUpdateListContent(this)
                scoreCardAdapter!!.notifyDataSetChanged()
            } else {
                listScore.isVisible = true
                listScoreCard.isVisible = false
                listActiviity.isVisible = true
                btnViewListView.text = "LIST"
                ScoreUpdateListContent(this)
                scoreAdapter!!.notifyDataSetChanged()
            }
        }





        btnDescending.setOnClickListener {
            ScoreUpdateListContent(myContext, "SORT", "ASC")
            btnAscending.setVisibility(View.VISIBLE);
            btnDescending.setVisibility(View.INVISIBLE);
            scoreAdapter!!.notifyDataSetChanged()
            globalCategory = "SORT"
        }

        //1200
        btnImportCsv.setOnClickListener {

            val loading = ProgressDialog.show(this, "", "Please wait")
            var url =
                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec";
            Log.e("4563", url)
            val stringRequest: StringRequest = object :

                StringRequest(Method.POST, url, Response.Listener { response ->
                    loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
                    //                    val intent = Intent(applicationContext, MainActivity::class.java)
                    //                    startActivity(intent)

                    var obj = JSONArray(response);
                    Log.e("5643", obj.length().toString())
                    var i = 0;
                    importScoreList.clear()
                    while (i < obj.length()) {

                        val jsonObject =
                            obj.getJSONObject(i) //                        var studnum = jsonObject.getString("Name") //                        Log.e("4563", studnum)

                        val scoreOne = ScoreImportModel();

                        scoreOne.score = 0
                        if (jsonObject.getString("Score") != "-") scoreOne.score =
                            jsonObject.getString("Score").toInt()

                        scoreOne.activitycode = jsonObject.getString("ActivityCode")
                        scoreOne.sectioncode = jsonObject.getString("Section")
                        scoreOne.completeName = jsonObject.getString("Name")
                        scoreOne.studentNumber = jsonObject.getString("StudNum")
                        scoreOne.quizTitle = jsonObject.getString("Quiz")
                        importScoreList.add(scoreOne)
                        i++
                    }

                    Log.e("5643", importScoreList.size.toString())

                    //                    var i = 0;
                    //                    var ctr = 0;
                    //                    var x = 0;
                    //                    var myScore = 0
                    //                    var status = ""
                    //                    val arr = ArrayList<String>()
                    //                    val ppp = obj.getJSONObject(0)
                    //                    val iterator: Iterator<String> = ppp.keys();
                    //
                    //                    while (iterator.hasNext()) {
                    //                        var date = iterator.next()
                    //                        val section = cboSectionSched.getSelectedItem().toString();
                    //                        val db1: TableActivity = TableActivity(context);
                    //                        val id = db1.GetNewSchedCode(section)
                    //                        arr.add(date)
                    //                        Log.e("SSS", ctr.toString() + "   " + date)
                    //
                    //                        val db: DatabaseHandler = DatabaseHandler(context)
                    //
                    //                        if (ctr > 1) {
                    //                            AddNewSched(date, "PM", section, id)
                    //                        }
                    //                        ctr++
                    //
                    //
                    //                    }
                    ShowDialogImport()

                }, Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    val parmas: MutableMap<String, String> =
                        HashMap() //                    var folderName = e!!.lastname + "," + e!!.firstname

                    parmas["Action"] = "ImportScore"
                    parmas["SectionName"] = Util.ACT_CURRENT_SECTION
                    parmas["ActivityCode"] =
                        Util.ACT_CODE //                    parmas["sectionFolder"]\\

                    //                      //  db.GetSectionFolderLink(e!!.Section) //   parmas["sectionFolder"] ="https://drive.google.com/drive/folders/1s0NmdPGCHeF2pKNrB9GaB95RzvHDpVq8"
                    //                    parmas["emailAddress"] = email
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


        /*   btnStatusData.setOnClickListener {
               var txt = btnStatusData!!.text.toString()
               if (txt == "ALL") {
                   btnStatusData.setText("DR")
                   ScoreUpdateListContent(myContext, "STATUS", "YES")
               } else if (txt == "DR") {
                   btnStatusData.setText("ZERO")
                   ScoreUpdateListContent(myContext, "STATUS", "ZERO")
               } else if (txt == "ZERO") {
                   btnStatusData.setText("ALL")
                   ScoreUpdateListContent(myContext, "STATUS", "ALL")
               }
               scoreAdapter!!.notifyDataSetChanged()
           }*/

        //        btnSortData.setOnClickListener {
        //            var txt = btnSortData!!.text.toString()
        //            if (txt == "GE") {
        //                btnSortData.setText("LN")
        //                ScoreUpdateListContent(myContext, "SORT", "LASTNAME")
        //                scoreAdapter!!.notifyDataSetChanged()
        //            }
        //            if (txt == "LN") {
        //                btnSortData.setText("FN")
        //                ScoreUpdateListContent(myContext, "SORT", "FIRSTNAME")
        //                scoreAdapter!!.notifyDataSetChanged()
        //            }
        //
        //
        //            if (txt == "FN") {
        //                btnSortData.setText("GRP")
        //                ScoreUpdateListContent(myContext, "SORT", "GROUP")
        //                scoreAdapter!!.notifyDataSetChanged()
        //            }
        //
        //
        //            if (txt == "GRP") {
        //                btnSortData.setText("GE")
        //                ScoreUpdateListContent(myContext, "SORT", "DB")
        //                scoreAdapter!!.notifyDataSetChanged()
        //            }
        //
        //            //            ScoreUpdateListContent(myContext, "SORT","DESC")
        //            //            scoreAdapter!!.notifyDataSetChanged()
        //        }

        //0620


        btnHorizontalView.setOnClickListener {
            if (Util.HVIEW == true) {
                Util.HVIEW = false
            } else {
                Util.HVIEW = true
            }
            hview!!.setOnTouchListener(OnTouch()) //
        }


        btnSearchFirst.setOnClickListener {

            ScoreUpdateListContent(myContext, "SEARCHLETTER", "A-C")
            globalCategory = "SEARCHLETTER"
            globalsortOrder = "A-C"
            scoreAdapter!!.notifyDataSetChanged()
            scoreCardAdapter!!.notifyDataSetChanged()

        }

        btnSearchSecond.setOnClickListener {

            ScoreUpdateListContent(myContext, "SEARCHLETTER", "D-J")
            globalCategory = "SEARCHLETTER"
            globalsortOrder = "D-J"
            scoreAdapter!!.notifyDataSetChanged()
            scoreCardAdapter!!.notifyDataSetChanged()
        }

        btnSearchThird.setOnClickListener {

            ScoreUpdateListContent(myContext, "SEARCHLETTER", "K-O")
            globalCategory = "SEARCHLETTER"
            globalsortOrder = "K-O"
            scoreAdapter!!.notifyDataSetChanged()
            scoreCardAdapter!!.notifyDataSetChanged()
        }

        btnSearchFourth.setOnClickListener {

            ScoreUpdateListContent(myContext, "SEARCHLETTER", "P-R")
            globalCategory = "SEARCHLETTER"
            globalsortOrder = "P-R"
            scoreAdapter!!.notifyDataSetChanged()
            scoreCardAdapter!!.notifyDataSetChanged()
        }

        btnSearchFifth.setOnClickListener {

            ScoreUpdateListContent(myContext, "SEARCHLETTER", "S-Z")
            globalCategory = "SEARCHLETTER"
            globalsortOrder = "S-Z"
            scoreAdapter!!.notifyDataSetChanged()
            scoreCardAdapter!!.notifyDataSetChanged()
        }

        pieChartActivity!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                var title = pieChartActivity!!.getCenterText().toString()
                var ppp =
                    title.split("\n") //                Util.ACT_DESCRIPTION = ppp[0] //                Util.ACT_CODE = ppp[1]
                var remark = (e as PieEntry).label
                ScoreUpdateListContent(myContext, "REMARK", remark)
                scoreAdapter!!.notifyDataSetChanged()
                scoreCardAdapter!!.notifyDataSetChanged()
            }

            override fun onNothingSelected() {}
        })


    }



    //0621
    fun UpdateactivityRecord(){
        val db: TableActivity = TableActivity(this)
        var activityCode  = txtScoreActivityCode.text.toString()
        var activityDescription  = txtSectionActivityDesc.text.toString()
        var section  =   txtSectionScore.text.toString()
        var item =  btnScoreItem.text.toString()
        var gradingPeriod = Util.GetCurrentGradingPeriod(this)
        db.ManageActivity("EDIT", activityCode, section, activityDescription, item, Util.ACT_CATEGORY, gradingPeriod)
    }

//    override fun onBackPressed() {
//        Log.d("4356", "onBackPressed Called")
//        val intent = Intent(this, ActivityMain::class.java)
//        this.startActivity(intent)
//    }


    //   1200
    fun ShowDialogImport() {

        val dlgimport = LayoutInflater.from(this).inflate(R.layout.import_score, null)
        val mBuilder = AlertDialog.Builder(this).setView(dlgimport).setTitle("Manage Student")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);
        val arrSection: Array<String> = Util.GetSectionList(this)

        dlgimport.txtSectionImport.text = Util.ACT_CURRENT_SECTION
        dlgimport.txtActivityCodeImport.text = Util.ACT_CODE
        dlgimport.txtDescriptionImport.text = Util.ACT_DESCRIPTION

        var sectionAdapter1: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        dlgimport.listImportScore.layoutManager = layoutmanager
        importAdapter = ScoreImportAdapter(this, importScoreList)
        dlgimport.listImportScore.adapter = importAdapter //

        //        //  dlgstudent.cbosection.setAdapter(sectionAdapter1);
        //
        //        val arrGroup: Array<String> = Util.GetArrayGroup()
        //        val db2: DatabaseHandler = DatabaseHandler(context)
        //        val arrSection2: ArrayList<String> = db2.GetOriginalSection()
        //        var sectionAdapter: ArrayAdapter<String> =
        //            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection2)
        //        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    }


    fun ReadCSV(): String {

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, "SAMPLE.TXT")
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)


        try { //            Toast.makeText(this, , Toast.LENGTH_SHORT).show();

            var fileInputStream = FileInputStream(myFile)


            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            var text: String = ""
            var line = bufferedReader.readLine()
            var dataText = ""
            while (line != null) {
                text = line.toString()
                line = bufferedReader.readLine()
                Log.e("1122", text)

                dataText = dataText + text + "\n" //if (status == true)
                //  list.add(StudentModel(token[0], token[1], token[2], token[3], token[4]))

            }

            //            //ViewRecord()

            fileInputStream.close()
            return dataText
        } catch (exception: FileNotFoundException) {
            Util.Msgbox(this, "The cannot found200")
        }

        return ""
    }


    fun showPopMenu(v: View) { //  showPopMenu
        Log.e("433", "hi")
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.menu_score)
        popup.show()
    }

        //0621
        fun ShowGroupList(context: Context, section: String) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            grpList.clear()
            Log.e("TTT", section)
            var grp: List<GrpModel>
            grp = db2.GetGroupList(section)
            Log.e("TTT", grp.size.toString())
            for (e in grp) {
                grpList.add(GrpModel(e.grpnumber, e.grpnumber, e.num))
            }
        }

    fun SetUpGroupAdapter() {

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listGrouping.layoutManager = layoutmanager
        grpScoreAdapter = GroupAdapter(this, grpList)
        listGrouping.adapter = grpScoreAdapter
    }


    fun SetUpActivityAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listActiviity.layoutManager = layoutmanager
        activityAdapter = ScoreSummary_ActivityAdapter(this, activityList)
        listActiviity.adapter = activityAdapter //
    }

    fun ActivityUpdateListContent(context: Context) {
        val dbglobal: TableActivity = TableActivity(context)
        val activity: List<ActivityModel>

        activityList.clear()
        val section = ActivityMain.cboSectionAct!!.getSelectedItem().toString();
        activity = dbglobal.GetActivityList(section, dbglobal.GetDefaultGradingPeriod())


        for (e in activity) {
            activityList.add(ActivityModel(e.activityCode, e.sectionCode, e.description, e.item.toInt(), e.gradingPeriod, e.category, ""))
        }
    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        val selected = item.toString()
        if (selected == "Show Open Folder") {
            Util.ACT_OPEN_FOLDER = true
            Util.ACT_RUBRIC = false
            scoreAdapter!!.notifyDataSetChanged()

        } else if (selected == "Show Rubric") {
            Util.ACT_OPEN_FOLDER = false
            Util.ACT_RUBRIC = true
            scoreAdapter!!.notifyDataSetChanged()

        } else if (selected == "Hide All") {
            Util.ACT_OPEN_FOLDER = false
            Util.ACT_RUBRIC = false
            scoreAdapter!!.notifyDataSetChanged()

        } //
        return true
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
                Util.Msgbox(this, result.getContents())
                val str = result.getContents()
                val rec = str.split("\n").toTypedArray()
                for (item in rec) {
                    val newrec = item.replace("\n", ",")
                    val token = str.split(",").toTypedArray()
                    val db: DatabaseHandler = DatabaseHandler(this)
                    var section = txtSectionScore.text.toString()
                    val studentNumber = db.GetSubjectStudentNo(token[0], section)

                }
                Log.e("Copy Record", rec.size.toString())
            }
        }


    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultcode, data)
        if (requestCode == AttendanceMain.PICK_FILE && resultcode == Activity.RESULT_OK && data != null) {

            val uri: Uri? = data.data
            val myFile = File(uri.toString());

            val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var mLine: String?


            scoreCsvList.clear() //            for (e in activity) {
            //            }


            var line = bufferedReader.readLine() //            while (r.readLine()!= null) {
            while (line != null) {
                var text = line.toString()
                Log.e("123456", line)
                val list = line.split(",")
                val db: DatabaseHandler = DatabaseHandler(this)
                val section = txtsection!!.text.toString()
                var studentNo = ""

                if (list[0].contains("-") && list[0].length == 5) {
                    studentNo = db.GetSubjectStudentNo(list[0], section)
                    scoreCsvList.add(ScoreCsvModel(studentNo, list[1], list[2], list[3], list[4]))
                } else if (list[0].length == 4) {
                    studentNo = list[0]
                    scoreCsvList.add(ScoreCsvModel(studentNo, list[1], list[2].trim(), list[3].trim(), list[4]))
                }

                line = bufferedReader.readLine()
            } //            Log.e("PDF200", selectedPdf.toString())

        }


        val layoutmanager = LinearLayoutManager(myContext)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listScore.layoutManager = layoutmanager
        csvAdapter = ScoreCsvAdapter(myContext, scoreCsvList)
        listScore.adapter = csvAdapter
    }


    private fun SetSpinnerAdapter() {
        val arrGroup: Array<String> = this.getResources().getStringArray(R.array.grpNumber)
        var grpAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrGroup)
        grpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    fun ScoreViewRecord() {

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listScore.layoutManager = layoutmanager
        scoreAdapter = ScoreAdapter(this, scoreList)
        listScore.adapter = scoreAdapter

        //0619
        val layoutmanager2 = GridLayoutManager(this, 2)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listScoreCard.layoutManager = layoutmanager2
        scoreCardAdapter = ScoreAdapter(this, scoreList)
        listScoreCard.adapter = scoreCardAdapter
    }

    fun ShowDialog(status: String, context: Context) {
        val mydb: DatabaseHandler = DatabaseHandler(context)
        val dlgstudent = LayoutInflater.from(context).inflate(R.layout.util_copy, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgstudent).setTitle("Copy Record")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);
        val arrSection: Array<String> = Util.GetSectionList(context)


        //        dlgstudent.btnSaveRecord.setOnClickListener {
        //            val buttonText: String = dlgstudent.btnSaveRecord.getText().toString()
        //            val studentNumber = dlgstudent.txtstudentnumber.text.toString()
        //            val firstName = dlgstudent.txtfirstname.text.toString()
        //            val lastName = dlgstudent.txtlastname.text.toString()
        //            val grpNumber = dlgstudent.cbogroup.getSelectedItem().toString();
        //            val section = dlgstudent.cbosection.getSelectedItem().toString();
        //            val gender = dlgstudent.cboGender.getSelectedItem().toString();
        //
        //            // val db: DatabaseHandler = DatabaseHandler(context)
        //            when (buttonText) {
        //                "SAVE RECORD" -> {
        //                    var status =
        //                        db.ManageStudent("ADD", studentNumber, firstName, lastName, grpNumber, section, gender)
        //                    studentDialog.dismiss()
        //                    if (status == true) {
        //                        StudentMain.list.add(StudentModel(studentNumber, firstName, lastName, grpNumber, section, gender))
        //                        StudentMain.adapter!!.notifyDataSetChanged()
        //                    }
        //                }
        //
        //                "EDIT" -> {
        //                    StatusTextBox(true, dlgstudent)
        //                    dlgstudent.btnSaveRecord.setText("SAVE CHANGES")
        //                    dlgstudent.txtstudentnumber.isEnabled = false;
        //                }
        //
        //                "SAVE CHANGES" -> {
        //                    var status =
        //                    // Log.e("EDIT", "$studentNumber, #firstName, $lastName, $grpNumber, $section, $gender")
        //                        // db.ManageStudent("ADD", studentNumber, firstName, lastName, grpNumber, section, gender)
        //
        //                        mydb.ManageStudent("EDIT", studentNumber, firstName, lastName, grpNumber, section, gender)
        //                    Toast.makeText(context, position.toString() + StudentMain.list.size.toString() + "", Toast.LENGTH_SHORT)
        //                        .show();
        //                    StudentMain.list[position].studentno = studentNumber
        //                    StudentMain.list[position].firstname = firstName
        //                    StudentMain.list[position].lastname = lastName
        //                    StudentMain.list[position].grp = grpNumber
        //                    StudentMain.list[position].sectioncode = section
        //                    StudentMain.list[position].gender = gender
        //                    StudentMain.adapter!!.notifyItemChanged(position)
        //                    studentDialog.dismiss()
        //
        //                    // ViewRecord(context)
        //
        //                }
        //            } //end when

        dlgstudent.btnCopyNo.setOnClickListener {
            var textToCopy = "NO " + Util.ACT_DESCRIPTION + "\n" + "\n"
            var x = 1;
            for (e in scoreList) {
                if (e.SubmissionStatus == "NO") {
                    textToCopy =
                        textToCopy + x + ". " + e.lastnanme + ", " + e.firstname.substring(0, 1) + "\n"
                    x++;
                }
            }
            CopyText(textToCopy)
            studentDialog.dismiss()
        }

        dlgstudent.btnCopyYes.setOnClickListener {
            var textToCopy = "WITH " + Util.ACT_DESCRIPTION + "\n" + "\n"
            var x = 1;
            for (e in scoreList) {
                if (e.SubmissionStatus == "YES") {
                    textToCopy =
                        textToCopy + x + ". " + e.lastnanme + ", " + e.firstname.substring(0, 1) + "\n"
                    x++;
                }
            }
            CopyText(textToCopy)
            studentDialog.dismiss()
        }




        dlgstudent.btnNameOnly.setOnClickListener {
            var textToCopy = Util.ACT_DESCRIPTION + "\n" + "\n"
            var x = 0;
            for (e in scoreList) {
                if (e.checkBoxStatus == "ON") textToCopy =
                    textToCopy + e.lastnanme + ", " + e.firstname.substring(0, 1) + "\n"
            }
            CopyText(textToCopy)
            studentDialog.dismiss()
        }

        dlgstudent.btnNameOnly.setOnClickListener {
            var textToCopy = Util.ACT_DESCRIPTION + "\n" + "\n"
            for (e in scoreList) {
                if (e.checkBoxStatus == "ON") textToCopy =
                    textToCopy + e.lastnanme + ", " + e.firstname.substring(0, 1) + "\n"
            }
            CopyText(textToCopy)
            studentDialog.dismiss()
        }

        dlgstudent.btnNameStudentNumber.setOnClickListener {
            var textToCopy = Util.ACT_DESCRIPTION + "\n" + "\n"
            for (e in scoreList) {
                if (e.checkBoxStatus == "ON") textToCopy =
                    textToCopy + e.studentNo + "\t" + e.lastnanme + "\t" + e.firstname + "\n"
            }
            CopyText(textToCopy)
            studentDialog.dismiss()
        }

        dlgstudent.btnNameStudentScore.setOnClickListener {
            var textToCopy = Util.ACT_DESCRIPTION + "\n" + "\n"
            for (e in scoreList) {
                textToCopy =
                    textToCopy + e.studentNo + "\t" + e.lastnanme + "\t" + e.firstname + "\t" + e.score + "\n"
            }
            CopyText(textToCopy)
            studentDialog.dismiss()
        }


    }


    fun CopyText(copyString: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", copyString)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

    fun ShowImport(context: Context, activityCode: String, description: String, section: String) {
        val db: TableActivity = TableActivity(context)
        val dlgactivity = LayoutInflater.from(context).inflate(R.layout.task_dialog_import, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgactivity).setTitle("Import Score22")
        val activityDialog = mBuilder.show()
        activityDialog.setCanceledOnTouchOutside(false);
        dlgactivity.txtActivityCode.setText(activityCode)
        dlgactivity.txtDescription.setText(description)

        var cursor: Cursor? = null
        cursor = db.GetActivitYInfo(section, activityCode);
        dlgactivity.txtSheetName.setText(cursor.getString(cursor.getColumnIndex("SheetName")))


        var scoreStudent: List<ScoreModel> ////////////////////////////////////////////////////

        var WithDashsectionCode = section.replace(" ", "-");
        var url =
            "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"

        url = url + "&&section=" + "WORKBOOK";
        url = url + "&&sheet=" + "SectionList";
        Log.e("sss", url)

        var stringRequest =
            StringRequest(Request.Method.GET, url, { response -> // Util.Msgbox(this, response.toString())
                // loading.dismiss()
                var response = response.replace("\"", "");
                val arrNew = response.split("/").toTypedArray()
                var sheetList = Array(arrNew.size) { "" }
                for (x in 0..arrNew.size - 1) {
                    sheetList[x] = arrNew[x]
                }

                //            var sectionAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.util_spinner, sheetList)
                //            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //            dlgactivity.cboSheetList.setAdapter(sectionAdapter);
            }) { }
        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)


        // //////////////////////////////////////////
        dlgactivity.rowBtnGeneral.setOnClickListener {
            val loading = ProgressDialog.show(context, "Importing Score", "Please wait")

            var WithDashsectionCode = section.replace(" ", "-");
            var url =
                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"

            url = url + "&&section=" + "GENERAL";
            url = url + "&&sheet=" + "GETSHEETNAME";
            Log.e("sss", url)
            var stringRequest = StringRequest(Request.Method.GET, url, { response ->
                Util.Msgbox(this, response.toString())
                loading.dismiss()
                var response = response.replace("\"", "");
                val arrNew = response.split("/").toTypedArray()
                var sheetList = Array(arrNew.size) { "" }
                for (x in 0..arrNew.size - 1) {
                    sheetList[x] = arrNew[x]
                }

                var sectionAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(this, R.layout.util_spinner, sheetList)
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dlgactivity.cboSheetList.setAdapter(sectionAdapter);
            }) { }
            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
            loading.dismiss()
        }


        dlgactivity.rowBtnGeneral.setOnClickListener {
            val loading = ProgressDialog.show(context, "Importing Score", "Please wait")

            var WithDashsectionCode = section.replace(" ", "-");
            var url =
                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"

            url = url + "&&section=" + "GENERAL";
            url = url + "&&sheet=" + "GETSHEETNAME";
            Log.e("sss", url)
            var stringRequest = StringRequest(Request.Method.GET, url, { response ->
                Util.Msgbox(this, response.toString())
                loading.dismiss()
                var response = response.replace("\"", "");
                val arrNew = response.split("/").toTypedArray()
                var sheetList = Array(arrNew.size) { "" }
                for (x in 0..arrNew.size - 1) {
                    sheetList[x] = arrNew[x]
                }

                var sectionAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(this, R.layout.util_spinner, sheetList)
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dlgactivity.cboSheetList.setAdapter(sectionAdapter);
            }) { }
            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
            loading.dismiss()
        }

        dlgactivity.cboSheetList.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val sheet = dlgactivity.cboSheetList.getSelectedItem().toString();
                    dlgactivity.txtSheetName.setText(sheet)
                }
            }






        dlgactivity.rowBtnSection.setOnClickListener {
            val loading = ProgressDialog.show(context, "Importing Score", "Please wait")

            var WithDashsectionCode = section.replace(" ", "-");
            var url =
                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"

            url = url + "&&section=" + WithDashsectionCode
            url = url + "&&sheet=" + "GETSHEETNAME";
            Log.e("sss", url)
            var stringRequest = StringRequest(Request.Method.GET, url, { response ->
                Util.Msgbox(this, response.toString())
                var response = response.replace("\"", "");
                val arrNew = response.split("/").toTypedArray()

                var sheetList = Array(arrNew.size) { "" }
                for (x in 0..arrNew.size - 1) {
                    sheetList[x] = arrNew[x]
                }


                var sectionAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(this, R.layout.util_spinner, sheetList)
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dlgactivity.cboSheetList.setAdapter(sectionAdapter);
            }) { }
            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
            loading.dismiss()
        }


        dlgactivity.rowBtnSaveSheet.setOnClickListener {
            if (dlgactivity.rowBtnSaveSheet.text == "EDIT DATA") {
                dlgactivity.rowBtnSaveSheet.setText("SAVE CHHANGES22")

                dlgactivity.txtSheetName.isEnabled = true
            } else {
                dlgactivity.rowBtnSaveSheet.setText("EDIT DATA")
                dlgactivity.txtSheetName.isEnabled = false

                db.UpdateSheetData(section, activityCode, dlgactivity.txtSheetName.text.toString())

            }
        }


        dlgactivity.rowBtnImportScore.setOnClickListener {
            val sheet = dlgactivity.txtSheetName.text.toString()

            val loading = ProgressDialog.show(context, "Importing Score20", "Please wait")
            var activityCode = dlgactivity.txtActivityCode.text.toString()

            ImportDataActivity("SCORE", sheet, section, activityCode, loading)
        }

        dlgactivity.rowBtnImportStatus.setOnClickListener {
            Log.e("STATUS", "aaa")
            val sheet = dlgactivity.txtSheetName.text.toString()
            val loading = ProgressDialog.show(context, "Importing Score40", "Please wait")
            var activityCode = dlgactivity.txtActivityCode.text.toString()

            ImportDataActivity("STATUS", sheet, section, activityCode, loading)
        }


        dlgactivity.rowBtnCSPCImport.setOnClickListener {
            val sheet = dlgactivity.txtSheetName.text.toString()
            val loading = ProgressDialog.show(context, "Importing Score", "Please wait")
            var actCode = dlgactivity.txtActivityCode.text.toString()

            var WithDashsectionCode = section.replace(" ", "-");
            var url =
                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"

            url = url + "&&section=" + "GENERAL"
            url = url + "&&sheet=" + sheet;

            var stringRequest = StringRequest(Request.Method.GET, url, { response ->


                var t = "\"" + "Section Not Found" + "\""
                var q = "\"" + "Sheet is Missing" + "\""
                "Sheet is Missing"

                Log.e("123", response + "  " + t)

                if (response.toString() == t.toString()) {
                    Util.Msgbox(this, "Section Not Found");
                    return@StringRequest

                }

                if (response.toString() == q.toString()) {
                    Util.Msgbox(this, "Sheet is Missing");
                    return@StringRequest
                }


                var obj = JSONArray(response);


                var i = 0;
                var ctr = 0;
                var x = 0;
                var myScore = 0
                var status = ""
                val arr = Array<String>(20) { "" }
                val ppp = obj.getJSONObject(0)
                val iterator: Iterator<String> = ppp.keys();
                val studentNameArray = Array<String>(100) { "" }
                val scoreArray = Array<String>(100) { "" }
                while (iterator.hasNext()) {
                    arr.set(ctr, iterator.next())
                    ctr++;
                }
                Log.e("index", arr.indexOf("Score").toString())
                var studCount = 0
                var index = 0
                var scoreIndex = arr.indexOf("Score")
                var emailIndex = arr.indexOf("Email")

                if (scoreIndex == -1) {
                    Util.Msgbox(this, "Mo Score Column");
                    return@StringRequest
                }


                if (emailIndex == -1) {
                    Util.Msgbox(this, "Mo Email Column");
                    return@StringRequest
                }

                var foundName = false




                while (iterator.hasNext()) {
                    arr.set(ctr, iterator.next())
                    ctr++;
                }



                while (i < obj.length()) {
                    val jsonObject = obj.getJSONObject(i)
                    var email = jsonObject.getString(arr[2])
                    var score = jsonObject.getString(arr[3])
                    val db: DatabaseHandler = DatabaseHandler(this)
                    val db3: TableAttendance = TableAttendance(this)
                    val studnum = db.GetStudentNumberViaEmail(section, email)

                    var submissionStatus =
                        db3.GetStatusStudent(studnum.toString(), section, actCode)
                    if (score == "-") {
                        myScore = 0
                        status = "NO"
                    } else {
                        myScore = score.toInt();
                        status = "YES"
                    }
                    if (submissionStatus == "DR") status = submissionStatus
                    val db2: TableAttendance = TableAttendance(this)
                    db2.UpdateStudentActivityScore(status, myScore, studnum.toString(), section, activityCode) //
                    i++;
                }
                Util.Msgbox(this, "Score is successfully imported@@");
                loading.dismiss()
            }) { }

            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)

        }


    } //ShowDialog

    fun ImportDataActivity(category: String, sheet: String, section: String, activityCode: String, loading: ProgressDialog) {

        var WithDashsectionCode = section.replace(" ", "-");
        var url =
            "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getItems"
        val db: TableActivity = TableActivity(myContext)
        url = url + "&&section=" + WithDashsectionCode;
        url = url + "&&sheet=" + sheet;
        Log.e("CHECK", url)
        var stringRequest = StringRequest(Request.Method.GET, url, { response ->
            db.UpdateSheetData(section, activityCode, sheet)


            var t = "\"" + "Section Not Found" + "\""
            var q = "\"" + "Sheet is Missing" + "\""
            "Sheet is Missing"

            Log.e("123", response + "  " + t)

            if (response.toString() == t.toString()) {
                Util.Msgbox(this, "Section Not Found");
                loading.dismiss()
                return@StringRequest
            }

            if (response.toString() == q.toString()) {
                Util.Msgbox(this, "Sheet is Missing");
                loading.dismiss()
                return@StringRequest
            }


            var obj = JSONArray(response);


            var i = 0;
            var ctr = 0;
            var x = 0;
            var myScore = 0
            var status = ""
            val arr = Array<String>(100) { "" }
            val ppp = obj.getJSONObject(0)
            val iterator: Iterator<String> = ppp.keys();
            val studentNameArray = Array<String>(100) { "" }
            val scoreArray = Array<String>(100) { "" }
            while (iterator.hasNext()) {
                arr.set(ctr, iterator.next())
                ctr++;
            }


            Log.e("index", arr.indexOf("Score").toString())
            var studCount = 0
            var index = 0
            var scoreIndex = arr.indexOf("Score")

            if (scoreIndex == -1) {
                Util.Msgbox(this, "Mo Score Column");
                loading.dismiss()
                return@StringRequest

            }

            var foundName = false


            for (element in arr) {
                Log.e("index22", element.toString())
                if (element.toString().contains("StudentName")) {
                    i = 0;
                    foundName = true
                    Log.e("index", arr[index])
                    while (i < obj.length()) {
                        val jsonObject = obj.getJSONObject(i)
                        Log.e("index", jsonObject.getString(arr[index]))
                        if (jsonObject.getString(arr[index]) != "") {
                            studentNameArray[studCount] = jsonObject.getString(arr[index])
                            scoreArray[studCount] = jsonObject.getString(arr[scoreIndex])
                            studCount++;
                        }
                        i++;
                    }
                }
                index++

            }

            if (foundName == false) {
                Util.Msgbox(this, "Mo Name Column");
                return@StringRequest
            }


            for (i in 0..studCount - 1) {

                Log.e("aaa", studentNameArray[i] + "  " + scoreArray[i])
                val db: TableAttendance = TableAttendance(this)
                val arrNew = studentNameArray[i].split("-").toTypedArray()
                var studnum = arrNew[0]
                var score = scoreArray[i]
                if (category == "SCORE") {
                    var submissionStatus = db.GetStatusStudent(studnum, section, activityCode)
                    if (score == "-") {
                        myScore = 0
                        status = "NO"
                    } else {
                        myScore = score.toInt();
                        status = "YES"
                    }
                    if (submissionStatus == "DR") status = submissionStatus

                } else {
                    myScore = 0
                    status = "YES"
                }

                val db2: TableAttendance = TableAttendance(this)
                Log.e("PPP", "$status, $myScore, $studnum, $section, $activityCode") //
                db2.UpdateStudentActivityScore(status, myScore, studnum, section, activityCode) //
                //                            }
            }
            Util.Msgbox(this, "Score is successfully imported@@");
            loading.dismiss()
        }) { }

        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)

    }

}


fun DatabaseHandler.UpdateGroupScore(section: String, activityCode: String, group: String, score: Int) {
    val sql = """
                    update tbscore
                        set score  ='$score'
                        where  activityCode  ='$activityCode'
                        and      sectionCode  ='$section'
                        and StudentNo IN (select StudentNo from tbstudent_query
                        where GrpNumber = '$group');
                    """
    Log.e("SQL", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}

fun DatabaseHandler.GetActivityRemark(sectionName: String, activitycode: String): ArrayList<String> {
    var remarkList = arrayListOf<String>()


    val grpMember: ArrayList<GrpMemberModel> = ArrayList<GrpMemberModel>()
    var sql = ""
    sql = """ select distinct (remark) as remarkList  from tbscore_query	
             where sectionCode = '$sectionName' 
            and activityCode = '$activitycode' 
            """
    Log.e("AAA", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        do {
            var rem = cursor.getString(cursor.getColumnIndex("remarkList"))
            Log.e("12345", rem)
            var sql2 = """ select *   from tbscore_query	
                         where sectionCode = '$sectionName' 
                        and activityCode = '$activitycode' 
                        and remark = '$rem' 
                        """
            var cursor2: Cursor? = null
            cursor2 = db.rawQuery(sql2, null)
            Log.e("12345", sql2)
            Log.e("12345", cursor2.count.toString())
            remarkList.add(rem)
        } while (cursor.moveToNext())
    }
    return remarkList
}


