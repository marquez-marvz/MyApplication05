//package com.example.myapplication05
//
//class ScoreSummary {}


package com.example.myapplication05

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_NAME
import com.example.myapplication05.R.layout.section_dialog
import com.example.myapplication05.R.layout.util_inputbox
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score1
import kotlinx.android.synthetic.main.scoresummary.btnActivity1
import kotlinx.android.synthetic.main.scoresummary.btnActivity2
import kotlinx.android.synthetic.main.scoresummary.btnActivity3
import kotlinx.android.synthetic.main.scoresummary.btnActivity4
import kotlinx.android.synthetic.main.scoresummary.btnActivity5
import kotlinx.android.synthetic.main.scoresummary.btnActivity6
import kotlinx.android.synthetic.main.scoresummary.btnActivity7
import kotlinx.android.synthetic.main.scoresummary.btnActivity8
import kotlinx.android.synthetic.main.scoresummary.btnActivity9
import kotlinx.android.synthetic.main.scoresummary.btnFilter
import kotlinx.android.synthetic.main.scoresummary.btnMode
import kotlinx.android.synthetic.main.scoresummary.btnRefresh
import kotlinx.android.synthetic.main.scoresummary.listActivityScore
import kotlinx.android.synthetic.main.scoresummary.listStudentScore
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_dialog.view.txtSection
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_main.btnSecond
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlinx.android.synthetic.main.task_dialog.view.btnSaveRecord as btnSaveRecord1

class ScoreSummary : AppCompatActivity() {

    var activityAdapter: ScoreSummary_ActivityAdapter? = null;
    var activityList = arrayListOf<ActivityModel>()

    var scoreAdapter: ScoreSummary_StudentAdapter? = null;
    var scoreList = arrayListOf<EnrolleModel>()

    companion object {
        var VIEWMODE = "REMARK"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val db: DatabaseHandler = DatabaseHandler(this)
        var section = db.GetCurrentSection();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoresummary)
        ActivityUpdateListContent(this)
        UpdateListContent(this, "LAST_ORDER", section, "ALL");
        SetUpStudentAdapter()


        btnRefresh.setOnClickListener {
            scoreAdapter!!.notifyDataSetChanged()
        }
            btnMode.setOnClickListener {
            Log.e("1344", "")
            if (btnMode.text == "SCORE") {
                btnMode.text ="REMARK"
                VIEWMODE="REMARK"
                scoreAdapter!!.notifyDataSetChanged()
            } else {
                btnMode.setText("SCORE")
                VIEWMODE="SCORE"
                scoreAdapter!!.notifyDataSetChanged()

            }
        }



        btnRefresh.setOnClickListener {
            UpdateListContent(this, "LAST_ORDER", section,  btnFilter.text.toString());
            scoreAdapter!!.notifyDataSetChanged()
        }
        btnFilter.setOnClickListener {
            Log.e("1344", "")
            if (btnFilter.text == "ALL") {
                btnFilter.text ="MISSING"
                UpdateListContent(this, "LAST_ORDER", section,  btnFilter.text.toString());
                scoreAdapter!!.notifyDataSetChanged()
            } else {
                btnFilter.setText("ALL")
                UpdateListContent(this, "LAST_ORDER", section,  btnFilter.text.toString());
                scoreAdapter!!.notifyDataSetChanged()

            }
        }






//        ActivityListUpdate()
    }

    fun SetUpActivityAdapter(){
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listActivityScore.layoutManager = layoutmanager
        Log.e("111122", activityList.size.toString())
        activityAdapter = ScoreSummary_ActivityAdapter(this, activityList)
        listActivityScore.adapter = activityAdapter
        //
    }

    fun SetUpStudentAdapter(){
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listStudentScore.layoutManager = layoutmanager
        Log.e("11555", scoreList.size.toString())
        scoreAdapter = ScoreSummary_StudentAdapter(this, scoreList)
        listStudentScore.adapter = scoreAdapter
        //
    }

    fun ActivityUpdateListContent(context: Context) {

        val db: DatabaseHandler = DatabaseHandler(this)
        var section = db.GetCurrentSection();
        val activity = db.GetActivityList(section, Util.GetCurrentGradingPeriod(this))


      var x = 1
        for (e in activity) {
            activityList.add(ActivityModel(e.activityCode, e.sectionCode, e.description, e.item.toInt(), e.gradingPeriod, e.category, ""))
            if (x==1){
                btnActivity1.text = e.description
                btnActivity1.isVisible = true
                btnActivity1.setBackgroundColor(Color.parseColor("#64B5F6"))
            }

            if (x==2){
                btnActivity2.text = e.description
                btnActivity2.isVisible = true
                btnActivity2.setBackgroundColor(Color.parseColor("#FFB74D"))

            }
            if (x==3){
                btnActivity3.text = e.description
                btnActivity3.isVisible = true
                btnActivity3.setBackgroundColor(Color.parseColor("#69F0AE"))

            }
            if (x==4){
                btnActivity4.text = e.description
                btnActivity4.isVisible = true
                btnActivity4.setBackgroundColor(Color.parseColor("#eccbac"))

            }
            if (x==5){
                btnActivity5.text = e.description
                btnActivity5.isVisible = true
                btnActivity5.setBackgroundColor(Color.parseColor("#ff7f50"))

            }
            if (x==6){
                btnActivity6.text = e.description
                btnActivity6.isVisible = true
                btnActivity6.setBackgroundColor(Color.parseColor("#ee6aa7"))

            }
            if (x==7){
                btnActivity7.text = e.description
                btnActivity7.isVisible = true
                btnActivity7.setBackgroundColor(Color.parseColor("#7fffd4"))

            }

            if (x==8){
                btnActivity8.text = e.description
                btnActivity8.isVisible = true
                btnActivity8.setBackgroundColor(Color.parseColor("#7fffd4"))
            }

            if (x==9){
                btnActivity9.text = e.description
                btnActivity9.isVisible = true
                btnActivity9.setBackgroundColor(Color.parseColor("#B2BEB5"))
            }

            x++;

        }

        Log.e("234", activityList.size.toString())
    }

    fun UpdateListContent(context: Context, category: String = "ALL", section: String = "", filter:String) {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section1 = ""
        if (section == "") section1 = db2.GetCurrentSection()
        else section1 = section
        scoreList.clear()
        Log.e("5467", filter)
        if (filter == "ALL") {
            student = db2.GetEnrolleList(category, section)
            for (e in student) {
                scoreList.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
            }

        } else {
            student = db2.GetEnrolleList(category, section)
            for (e in student) {
                if (db2.GetAllActivitiStatus(e.studentno)=="WITH-MISSING")
                scoreList.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
            }
        }



    }
}

//        SectionUpdateListContent(this, "SHOW")
//        SectionViewRecord()
//
//
//
//
//        btnAdd.setOnClickListener {
//            val db: DatabaseHandler = DatabaseHandler(this)
//            val sectionCode = db.GetNewSectionCode()
//            val dlgsection = LayoutInflater.from(this).inflate(section_dialog, null)
//            val mBuilder = AlertDialog.Builder(this).setView(dlgsection)
//                .setTitle("Input Section Name for $sectionCode")
//            val inputDialog = mBuilder.show()
//            inputDialog.setCanceledOnTouchOutside(false);
//            dlgsection.txtSectionCode.setText(db.GetNewSectionCode())
//            dlgsection.txtSectionCode.isEnabled= false
//
//            val arrSection: ArrayList<String> = db.GetSubjectDescription()
//            var sectionAdapter: ArrayAdapter<String> =
//                ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
//            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            dlgsection.cboSubjectDescription.setAdapter(sectionAdapter);
//
//
//
//            dlgsection.btnSaveRecord.setOnClickListener {
//                val sectionName = dlgsection.txtSection.text.toString()
//                val originalSection = dlgsection.txtOriginalSection.text.toString()
//                val school =   dlgsection.cboSchool.getSelectedItem().toString();
//                val status =    dlgsection.cboVisibility.getSelectedItem().toString();
//                val desc =    dlgsection.cboSubjectDescription.getSelectedItem().toString();
//                val db: DatabaseHandler = DatabaseHandler(this)
//                db.SaveSection(sectionCode, sectionName, school, status, originalSection, desc)
//                inputDialog.dismiss()
//                SectionUpdateListContent(this, "SHOW")
//                SectionViewRecord()
//            }
//
//            //  inputDialog.setOnCancelListener(object : DialogInterface.OnCancelListener {
//            //       override fun onCancel(dialog: DialogInterface) { // Your code ...
//            //                    val e = currentGrade
//            //                    val studentNo = dlgStudent.txtStudentNo.text.toString()
//            //                    e!!.firstGrade= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "DECIMAL")
//            //                    e!!.firstEquivalent= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "ADJUSTED")
//            //                    e!!.firstOriginalGrade= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "ORIGINAL")
//            //                    Util.Msgbox(contezt, "Hello")
//            //GradeMain.myGradeAdapter!!.notifyDataSetChanged()
//
//
//
//
//            //       );
//            // }
//            // })
//
//        }
//
//        btnShow.setOnClickListener {
//            var txt = btnShow!!.text.toString()
//
//            if (txt == "ALL") {
//                btnShow.setText("SHOW")
//                SectionUpdateListContent(this, "SHOW")
//                SectionViewRecord()
//            } else if (txt == "SHOW") {
//                btnShow.setText("HIDE")
//                SectionUpdateListContent(this, "HIDE")
//                SectionViewRecord()
//            } else if (txt == "HIDE") {
//                btnShow.setText("ALL")
//                SectionUpdateListContent(this, "ALL")
//                SectionViewRecord()
//            }
//        }
//    }
//
//    companion object{
//        var sectionAdapter: SectionAdapter? = null;
//        var sectionList = arrayListOf<SectionModel>()
//
//        fun SectionUpdateListContent(context:Context, status:String) {
//            val db: DatabaseHandler = DatabaseHandler(context)
//            val myList: List<SectionModel> = db.GetSectionList(status)
//            sectionList.clear()
//
//            for (e in myList) {
//                sectionList.add(SectionModel(e.sectionCode, e.sectionName, e.school, e.status, e.Message, e.originalSection, e.subjectDescription,e.folderLink))
//
//            }
//
//        }
//    }
//
//
//    private fun SectionViewRecord() {
//        val layoutmanager = LinearLayoutManager(this)
//        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
//        listSection.layoutManager = layoutmanager
//        sectionAdapter = SectionAdapter(this, sectionList)
//        listSection.adapter = sectionAdapter
//    }
//
//
//}
//
//private fun DatabaseHandler.SaveSection(sectionCode: String, sectionName: String, school: String, status: String,  origSection:String, desc:String) {
//    val db = this.writableDatabase
//    var sql1 = """
//        SELECT * FROM  TBSUBJECT
//         WHERE  SubjectDescription='$desc'
//    """.trimIndent()
//    Log.e("AAA", sql1)
//    val db2 = this.readableDatabase
//    val cursor = db2.rawQuery(sql1, null)
//    cursor.moveToFirst()
//    var scode = cursor.getString(cursor.getColumnIndex("SubjectCode"))
//    cursor.close()
//
//
//    val contentValues = ContentValues()
//    contentValues.put(TBSECTION_CODE, sectionCode)
//    contentValues.put(TBSECTION_NAME, sectionName) // EmpModelClass Name
//    contentValues.put("School", school) // EmpModelClass Name
//    contentValues.put("School", school) // EmpModelClass Name
//    contentValues.put("Status ", status) // EmpModelClass Name
//    contentValues.put("RealSectionName ", origSection) // EmpModelClass Name
//    contentValues.put("SubjectCode ", scode) // EmpModelClass Name
//    val success = db.insert("tbsection", null, contentValues)
//}
//
//fun DatabaseHandler.GetSectionList(Visibility:String) :List<SectionModel> {
//    val sectionList: ArrayList<SectionModel> = ArrayList<SectionModel>()
//
//
//    var sql =""
//    if (Visibility =="SHOW")
//        sql = "SELECT * FROM tbsection_query where status='SHOW' ORDER BY School, $TBSECTION_NAME "
//    else if (Visibility =="HIDE")
//        sql = "SELECT * FROM tbsection_query where status='HIDE' ORDER BY SCHO0L,  $TBSECTION_NAME "
//    else
//        sql = "SELECT * FROM tbsection_query ORDER BY SCHO0L, $TBSECTION_NAME "
//
//
//
//    var cursor2=  SQLSelect(sql, 40, 50)
//    val db = this.readableDatabase
//    var cursor: Cursor? = null
//    cursor = db.rawQuery(sql, null)
//
//    if (cursor.moveToFirst()) {
//        do {
//
//            var sectionCode = cursor.getString(cursor.getColumnIndex(TBSECTION_CODE))
//            var sectionName = cursor.getString(cursor.getColumnIndex(TBSECTION_NAME))
//            var school = cursor.getString(cursor.getColumnIndex("School"))
//            var status = cursor.getString(cursor.getColumnIndex("Status"))
//            var message = cursor.getString(cursor.getColumnIndex("Message"))
//            var origSection = cursor.getString(cursor.getColumnIndex("RealSectionName"))
//            var description = cursor.getString(cursor.getColumnIndex("SubjectDescription"))
//            var folderLink = cursor.getString(cursor.getColumnIndex("SectionFolderLInk"))
//            //            SubjectDescription	PT	Quiz	Exam	Project	ClassStanding
//            //                    Participation	ClassStandingCoverage
//            //                    RealSectionName	SectionCode	SectionName
//            val myList = SectionModel(sectionCode, sectionName, school, status, message, origSection,description, folderLink)
//            sectionList.add(myList)
//        } while (cursor.moveToNext())
//    }
//    return sectionList
//}
//
//
//
//
fun DatabaseHandler.GetAllActivitiStatus(studNumber:String):String {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var  sql = """ SELECT  * FROM tbscore
             where StudentNo='$studNumber' 
            and (Remark <>'YES'
            AND  Remark <>'OK')
                    """



    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.count==0) {
        return "NO-MISSING"
    } else{
        return "WITH-MISSING"
    }
}



//fun DatabaseHandler.GetSectionActivityList(section:String, period:String) :List<ActivityModel> {
//    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
//    var sql =""
//
//    sql = "SELECT * FROM tbactivity where sectionCode='$section' and gradingPeriod='$period'"
//
//    val db = this.readableDatabase
//    var cursor: Cursor? = null
//    cursor = db.rawQuery(sql, null)
//    /*
//           CREATE TABLE tbactivity  (ActivityCode  TEXT, SectionCode  text, Description  text, Item  INTEGER,
//           Status  text, Category text, GradingPeriod text, ScoreColumn TEXT, NameColumn TEXT, SheetName TEXT,
//            */
//    if (cursor.moveToFirst()) {
//        do {
//
//            var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
//            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
//            var description = cursor.getString(cursor.getColumnIndex("Description"))
//            var item = cursor.getString(cursor.getColumnIndex("Item"))
//            var gradingPeriod = cursor.getString(cursor.getColumnIndex("GradingPeriod"))
//            var category = cursor.getString(cursor.getColumnIndex("Category"))
//
//            val myList = ActivityModel(activityCode, sectionCode, description, item.toInt(), gradingPeriod, category, "")
//            activityList.add(myList)
//        } while (cursor.moveToNext())
//    }
//    return activityList
//}
//
//
//fun DatabaseHandler.GetSubjectDescription(): ArrayList<String> {
//
//    val subjectList: ArrayList<String> = ArrayList<String>()
//
//    var sql: String = "SELECT  * FROM tbsubject "
//    val db = this.readableDatabase
//    var cursor: Cursor? = null
//    cursor = db.rawQuery(sql, null)
//    var num = 1;
//    if (cursor.moveToFirst()) {
//        do {
//            var sn = cursor.getString(cursor.getColumnIndex("SubjectDescription"))
//            subjectList.add(sn)
//        } while (cursor.moveToNext())
//    }
//    return subjectList
//}









