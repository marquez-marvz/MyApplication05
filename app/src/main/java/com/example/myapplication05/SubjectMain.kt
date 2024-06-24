//package com.example.myapplication05
//
//class  {}


package com.example.myapplication05

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_NAME

import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_dialog.view.txtSection
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.subject_main.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlinx.android.synthetic.main.task_dialog.view.btnSaveRecord as btnSaveRecord1

class SubjectMain : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_main)
        SubjecctUpdateListContent(this)
        SubjectViewRecord()


         btnAddNewSubject.setOnClickListener {
             val db: DatabaseHandler = DatabaseHandler(this)
             db.AddNewSubject()
         }
    }

    companion object {
        var subjectAdapter: SubjectAdapter? = null;
        var subjectList = arrayListOf<SubjectModel>()

        fun SubjecctUpdateListContent(context: Context) {
            val db: DatabaseHandler = DatabaseHandler(context)
            val myList: List<SubjectModel> = db.GetSubjectList()
            subjectList.clear()

            for (e in myList) {
                subjectList.add(SubjectModel(e.subjectCode, e.subjectDescription, e.pt, e.quiz, e.exam, e.project, e.classStanding, e.participation, e.classStandingCoverage))
            }

        }


    }


    private fun SubjectViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSubject.layoutManager = layoutmanager
        subjectAdapter = SubjectAdapter(this, subjectList)
        listSubject.adapter = subjectAdapter //    }
    }



// //private fun DatabaseHandler.SaveSection(sectionCode: String, sectionName: String, school: String, status: String ) {
    //    val db = this.writableDatabase
    //    val contentValues = ContentValues()
    //    contentValues.put(TBSECTION_CODE, sectionCode)
    //    contentValues.put(TBSECTION_NAME, sectionName) // EmpModelClass Name
    //    contentValues.put("School", school) // EmpModelClass Name
    //    contentValues.put("Status ", status) // EmpModelClass Name
    //    val success = db.insert("tbsection", null, contentValues)
    //}
    //
    //fun DatabaseHandler.GetSectionList(Visibility:String) :List<SectionModel> {
    //    val sectionList: ArrayList<SectionModel> = ArrayList<SectionModel>()
    //    var sql =""
    //    if (Visibility =="SHOW")
    //        sql = "SELECT * FROM TBSECTION where status='SHOW' ORDER BY $TBSECTION_NAME "
    //    else if (Visibility =="HIDE")
    //        sql = "SELECT * FROM TBSECTION where status='HIDE' ORDER BY $TBSECTION_NAME "
    //    else
    //        sql = "SELECT * FROM TBSECTION ORDER BY $TBSECTION_NAME "
    //
    //
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
    //
    //            val myList = SectionModel(sectionCode, sectionName, school, status, message)
    //            sectionList.add(myList)
    //        } while (cursor.moveToNext())
    //    }
    //    return sectionList
    //}
    //
    //
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
}
fun DatabaseHandler.AddNewSubject(){
    var sql = ""

    sql = """
                SELECT * FROM tbsubject 
                ORDER BY SubjectCode DESC
                """

 var scode = ""
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        var actCode = cursor.getString(cursor.getColumnIndex("SubjectCode"))
        var num = actCode.takeLast(2).toInt() + 1 // Grade>>>
        Util.Msgbox(context, num.toString())
        scode = "SUB-" + Util.ZeroPad(num, 2)
    } else { // Util.Msgbox(context, "ACT-01" )
        scode = "SUB-01"
    }

    sql = """ insert into tbsubject  
             (SubjectCode, SubjectDescription, PT, Quiz, Exam, Project, ClassStanding, Participation, ClassStandingCoverage) 
             values('$scode', '-', 0, 0, 0, 0, 0, 0,  '-')
             """


    val db2 = this.writableDatabase
    db2.execSQL(sql);



    //SubjectCode	SubjectDescription	PT	Quiz	Exam	Project	ClassStanding	Participation	ClassStandingCoverage
}
fun DatabaseHandler.GetSubjectList(): List<SubjectModel> {
        val sectionList: ArrayList<SubjectModel> = ArrayList<SubjectModel>()
        var sql = ""
        sql = "SELECT * FROM TBSUBJECT "


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            do {

                var subjectCode = cursor.getString(cursor.getColumnIndex("SubjectCode"))
                var subjectDescription =
                    cursor.getString(cursor.getColumnIndex("SubjectDescription"))
                var pt = cursor.getString(cursor.getColumnIndex("PT"))
                var quiz = cursor.getString(cursor.getColumnIndex("Quiz"))
                var exam = cursor.getString(cursor.getColumnIndex("Exam"))
                var proj = cursor.getString(cursor.getColumnIndex("Project"))
                var standing = cursor.getString(cursor.getColumnIndex("ClassStanding"))
                var part = cursor.getString(cursor.getColumnIndex("Participation"))
                var coverage = cursor.getString(cursor.getColumnIndex("ClassStandingCoverage"))


                // SubjectCode	SubjectDescription	PT	Quiz	Exam	Project	ClassStanding	Participation	ClassStandingCoverage

                val myList =
                    SubjectModel(subjectCode, subjectDescription, pt, quiz, exam, proj, standing, part, coverage)
                sectionList.add(myList)
            } while (cursor.moveToNext())
        }
        return sectionList
    }









