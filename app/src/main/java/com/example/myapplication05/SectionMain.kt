package com.example.myapplication05

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_NAME
import com.example.myapplication05.R.layout.section_dialog
import com.example.myapplication05.R.layout.util_inputbox
import kotlinx.android.synthetic.main.activity_main.btnSemester
import kotlinx.android.synthetic.main.activity_main.cboSchoolYear
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_dialog.view.txtSection
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlinx.android.synthetic.main.task_dialog.view.btnSaveRecord as btnSaveRecord1

class SectionMain : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var context = this
        setContentView(R.layout.section_main)
        SectionUpdateListContent(this, Util.CURRENT_SCHOOLYEAR, Util.CURRENT_SEMESTER)
        SectionViewRecord()
        SetDefaultSchoolYea(Util.CURRENT_SCHOOLYEAR)
        btnSemesterSection.text = Util.CURRENT_SEMESTER


        btnSemesterSection.setOnClickListener {
            if (btnSemesterSection.text.toString() == "FIRST") {
                btnSemesterSection.text = "SECOND"

            } else if (btnSemesterSection.text.toString() == "SECOND") {
                btnSemesterSection.text = "SUMMER"
            } else if (btnSemesterSection.text.toString() == "SUMMER") {
                btnSemesterSection.text = "FIRST"

            }

            var schoolyear = cboSchoolYearSection.getSelectedItem().toString();
            var semester = btnSemesterSection.text.toString()
            SectionUpdateListContent(this, schoolyear, semester)
            sectionAdapter!!.notifyDataSetChanged()
            sectionAdapter!!.notifyDataSetChanged()
        }


        cboSchoolYearSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val schoolyear = cboSchoolYearSection.getSelectedItem().toString();
                var semester = btnSemesterSection.text.toString()
                SectionUpdateListContent(context, schoolyear, semester)
                sectionAdapter!!.notifyDataSetChanged()

            }
        }





        btnAdd.setOnClickListener {
            val db: DatabaseHandler = DatabaseHandler(this)
            val sectionCode = db.GetNewSectionCode()
            val dlgsection = LayoutInflater.from(this).inflate(section_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgsection)
                .setTitle("Input Section Name for $sectionCode")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false);
            dlgsection.txtSectionCode.setText(db.GetNewSectionCode())
            dlgsection.cboAddSchoolYear.setSelection(GetSchoolYearIndex(Util.CURRENT_SCHOOLYEAR))
            dlgsection.cboAddSemester.setSelection(GetSemesterIndex(Util.CURRENT_SEMESTER))
            dlgsection.txtSectionCode.isEnabled = false


            val arrSection: ArrayList<String> = db.GetSubjectDescription()
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dlgsection.cboSubjectDescription.setAdapter(sectionAdapter);



            dlgsection.btnSaveRecord.setOnClickListener {
                val sectionName =
                    dlgsection.txtSection.text.toString() //val originalSection = dlgsection.txtOriginalSection.text.toString()
                val school = dlgsection.cboSchool.getSelectedItem().toString();
                val schoolyear = dlgsection.cboAddSchoolYear.getSelectedItem().toString();
                val semester = dlgsection.cboAddSemester.getSelectedItem()
                    .toString(); // val status =    dlgsection.cboVisibility.getSelectedItem().toString();
                val desc = dlgsection.cboSubjectDescription.getSelectedItem().toString();
                val db: DatabaseHandler = DatabaseHandler(this)
                if (db.CheckSectionExistence(sectionName) == 0) {
                    db.SaveSection(sectionCode, sectionName, school, schoolyear, semester, desc)
                    inputDialog.dismiss()
                    SectionUpdateListContent(this, Util.CURRENT_SCHOOLYEAR, Util.CURRENT_SEMESTER)
                    SectionViewRecord()
                } else {
                    Util.Msgbox(this, "Section Already Exist")
                }
            }

            //  inputDialog.setOnCancelListener(object : DialogInterface.OnCancelListener {
            //       override fun onCancel(dialog: DialogInterface) { // Your code ...
            //                    val e = currentGrade
            //                    val studentNo = dlgStudent.txtStudentNo.text.toString()
            //                    e!!.firstGrade= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "DECIMAL")
            //                    e!!.firstEquivalent= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "ADJUSTED")
            //                    e!!.firstOriginalGrade= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "ORIGINAL")
            //                    Util.Msgbox(contezt, "Hello")
            //GradeMain.myGradeAdapter!!.notifyDataSetChanged()


            //       );
            // }
            // })

        }


    }

    companion object {
        var sectionAdapter: SectionAdapter? = null;
        var sectionList = arrayListOf<SectionModel>()

        fun SectionUpdateListContent(context: Context, schoolyear: String, semester: String) {
            val db: DatabaseHandler = DatabaseHandler(context)
            val myList: List<SectionModel> = db.GetSectionList(schoolyear, semester)
            sectionList.clear()

            for (e in myList) {
                sectionList.add(SectionModel(e.sectionCode, e.sectionName, e.school, e.status, e.Message, e.originalSection, e.subjectDescription, e.folderLink, e.schhoolYear, e.semester))

            }

        }

        fun GetSchoolYearIndex(search: String): Int {
            val arrSchoolYear = arrayOf("2022-2023", "2023-2024", "2024-2025")
            val index = arrSchoolYear.indexOf(search)
            return index
        }

        fun GetSemesterIndex(search: String): Int {
            val arrSchoolYear = arrayOf("FIRST", "SECOND", "SUMMER")
            val index = arrSchoolYear.indexOf(search)
            return index
        }

        fun GetOriginalSectionIndex(search: String): Int {
            val arrSection = arrayOf("11-PROG-1", "11-PROG-2", "12-PROG-1", "12-PROG-2")
            val index = arrSection.indexOf(search)
            return index
        }


        fun GetSubjectDescriptionIndex(context:Context,  search: String): Int {
            val db: DatabaseHandler = DatabaseHandler(context)
            val arrDesc: ArrayList<String> = db.GetSubjectDescription()
            val index = arrDesc.indexOf(search)
            return index
        }
    }


    private fun SectionViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSection.layoutManager = layoutmanager
        sectionAdapter = SectionAdapter(this, sectionList)
        listSection.adapter = sectionAdapter
    }

    fun SetDefaultSchoolYea(search: String) {
        val db: DatabaseHandler = DatabaseHandler(this)
        val arrSchoolYear = arrayOf("2022-2023", "2023-2024", "2024-2025")
        val index = arrSchoolYear.indexOf(search)
        cboSchoolYearSection.setSelection(index)
    }


}


@SuppressLint("Range")
private fun DatabaseHandler.CheckSectionExistence(sectionName: String): Int {
    val db = this.writableDatabase
    var sql1 = """
        SELECT * FROM  TBSECTION
         WHERE SectionName ='$sectionName'        
    """.trimIndent()
    Log.e("AAA", sql1)
    val db2 = this.readableDatabase
    val cursor = db2.rawQuery(sql1, null)
    return cursor.count
}

private fun DatabaseHandler.SaveSection(sectionCode: String, sectionName: String, school: String, schoolYear: String, semester: String, desc: String) {
    val db = this.writableDatabase
    var sql1 = """
        SELECT * FROM  TBSUBJECT 
         WHERE  SubjectDescription='$desc'        
    """.trimIndent()
    Log.e("AAA", sql1)
    val db2 = this.readableDatabase
    val cursor = db2.rawQuery(sql1, null)
    cursor.moveToFirst()
    var scode = cursor.getString(cursor.getColumnIndex("SubjectCode"))
    cursor.close()


    val contentValues = ContentValues()
    contentValues.put(TBSECTION_CODE, sectionCode)
    contentValues.put(TBSECTION_NAME, sectionName) // EmpModelClass Name
    contentValues.put("School", school) // EmpModelClass Name
    contentValues.put("School", school) // EmpModelClass Name
    contentValues.put("SubjectCode ", scode) // EmpModelClass Name
    contentValues.put("Semester ", semester) // EmpModelClass Name
    contentValues.put("SchoolYear ", schoolYear) // EmpModelClass Name
    val success = db.insert("tbsection", null, contentValues)
}

fun DatabaseHandler.GetSectionList(schoolYear: String, semester: String): List<SectionModel> {
    val sectionList: ArrayList<SectionModel> = ArrayList<SectionModel>()


    var sql = ""
    sql =
        "SELECT * FROM tbsection_query where schoolYear='$schoolYear' and semester ='$semester' ORDER BY School, $TBSECTION_NAME " //    else if (Visibility =="HIDE")
    //        sql = "SELECT * FROM tbsection_query where status='HIDE' ORDER BY School,  $TBSECTION_NAME "
    //    else
    //        sql = "SELECT * FROM tbsection_query ORDER BY SCHO0L, $TBSECTION_NAME "


    var cursor2 = SQLSelect(sql, 40, 50)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)

    if (cursor.moveToFirst()) {
        do {

            var sectionCode = cursor.getString(cursor.getColumnIndex(TBSECTION_CODE))
            var sectionName = cursor.getString(cursor.getColumnIndex(TBSECTION_NAME))
            var school = cursor.getString(cursor.getColumnIndex("School"))
            var message = cursor.getString(cursor.getColumnIndex("Message"))
            var origSection = cursor.getString(cursor.getColumnIndex("RealSectionName"))
            var description = cursor.getString(cursor.getColumnIndex("SubjectDescription"))
            var folderLink = cursor.getString(cursor.getColumnIndex("SectionFolderLInk"))
            var schoolYear = cursor.getString(cursor.getColumnIndex("SchoolYear"))
            var semester =
                cursor.getString(cursor.getColumnIndex("Semester")) //            SubjectDescription	PT	Quiz	Exam	Project	ClassStanding //                    Participation	ClassStandingCoverage
            //                    RealSectionName	SectionCode	SectionName
            val myList =
                SectionModel(sectionCode, sectionName, school, "", message, origSection, description, folderLink, schoolYear, semester)
            sectionList.add(myList)
        } while (cursor.moveToNext())
    }
    return sectionList
}


fun DatabaseHandler.GetSectionNoSubjecgt(Visibility: String): List<String> {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql = "SELECT  FROM TBSECTION where status="


    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)

    if (cursor.moveToFirst()) {
        do {

            var sectionCode = cursor.getString(cursor.getColumnIndex(TBSECTION_CODE))



            sectionList.add(sectionCode)
        } while (cursor.moveToNext())
    }
    return sectionList
}


fun DatabaseHandler.GetSectionActivityList(section: String, period: String): List<ActivityModel> {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    var sql = ""

    sql = "SELECT * FROM tbactivity where sectionCode='$section' and gradingPeriod='$period'"

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)/*
           CREATE TABLE tbactivity  (ActivityCode  TEXT, SectionCode  text, Description  text, Item  INTEGER,
           Status  text, Category text, GradingPeriod text, ScoreColumn TEXT, NameColumn TEXT, SheetName TEXT,
            */
    if (cursor.moveToFirst()) {
        do {

            var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
            var description = cursor.getString(cursor.getColumnIndex("Description"))
            var item = cursor.getString(cursor.getColumnIndex("Item"))
            var gradingPeriod = cursor.getString(cursor.getColumnIndex("GradingPeriod"))
            var category = cursor.getString(cursor.getColumnIndex("Category"))

            val myList =
                ActivityModel(activityCode, sectionCode, description, item.toInt(), gradingPeriod, category, "")
            activityList.add(myList)
        } while (cursor.moveToNext())
    }
    return activityList
}


fun DatabaseHandler.GetSubjectDescription(): ArrayList<String> {

    val subjectList: ArrayList<String> = ArrayList<String>()

    var sql: String = "SELECT  * FROM tbsubject "
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    var num = 1;
    if (cursor.moveToFirst()) {
        do {
            var sn = cursor.getString(cursor.getColumnIndex("SubjectDescription"))
            subjectList.add(sn)
        } while (cursor.moveToNext())
    }
    return subjectList
}









