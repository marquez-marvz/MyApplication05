//package com.example.myapplication05
//
//class MissingMain {}

package com.example.myapplication05

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_NAME
import kotlinx.android.synthetic.main.missingmain.*
import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.task_main.*

//import kotlinx.android.synthetic.main.score_main.*
//import kotlinx.android.synthetic.main.section_main.*
//import kotlinx.android.synthetic.main.task_main.*

//import com.example.myapplication05.R.layout.section_dialog
//import com.example.myapplication05.R.layout.util_inputbox
//import kotlinx.android.synthetic.main.grade_row.view.*
//import kotlinx.android.synthetic.main.score_individual.view.*
//import kotlinx.android.synthetic.main.score_main.*
//import kotlinx.android.synthetic.main.section_dialog.view.*
//import kotlinx.android.synthetic.main.section_dialog.view.txtSection
//import kotlinx.android.synthetic.main.section_main.*
//import kotlinx.android.synthetic.main.student_dialog.view.*
//import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
//import kotlinx.android.synthetic.main.task_dialog.view.*
//import kotlinx.android.synthetic.main.task_main.*
//import kotlinx.android.synthetic.main.util_inputbox.view.*
//import kotlinx.android.synthetic.main.task_dialog.view.btnSaveRecord as btnSaveRecord1

class MissingMain : AppCompatActivity() {
    val myContext: Context = this;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missingmain) //        SectionUpdateListContent(this, "SHOW")
        SetSpinnerAdapter()
        SetDefaultSection()
        val sectioncode = cboSectionMissing.getSelectedItem().toString();
        MissingUpdateListContent(this,"ACTIVITYCODE" , sectioncode)
        SectionViewRecord()


        btnSortData.setOnClickListener {
            val sectioncode = cboSectionMissing.getSelectedItem().toString();
            val category = btnCategory.text.toString()
            if (btnSortData.text == "ACTIVITY CODE") {
                btnSortData.text = "LAST NAME"
                MissingUpdateListContent(this, "LASTNAME", sectioncode, category)
                missingAdapter!!.notifyDataSetChanged()

            }
            else
            {
                btnSortData.text ="ACTIVITY CODE"
                MissingUpdateListContent(this, "ACTIVITYCODE", sectioncode, category)
                missingAdapter!!.notifyDataSetChanged()


            }
        }


        btnCategory.setOnClickListener {
            val category = cboSectionMissing.getSelectedItem().toString();
            if (btnCategory.text == "NO") {
                btnCategory.text = "DRP"
                val sortCategory = btnSortData.text.toString()
                MissingUpdateListContent(this,sortCategory , sectioncode, "DRP")
                missingAdapter!!.notifyDataSetChanged()

            }

            else if (btnCategory.text == "DRP") {
                btnCategory.text = "ZERO"
                val sortCategory = btnSortData.text.toString()
                MissingUpdateListContent(this,sortCategory , sectioncode, "ZERO")
                missingAdapter!!.notifyDataSetChanged()

            }

            else if (btnCategory.text == "ZERO") {
                btnCategory.text = "NO"
                val sortCategory = btnSortData.text.toString()
                MissingUpdateListContent(this,sortCategory , sectioncode, "NO")
                missingAdapter!!.notifyDataSetChanged()
            }

        }

        cboSectionMissing.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                val section = cboSectionMissing.getSelectedItem().toString();
                MissingUpdateListContent(myContext, "ACTIVITYCODE", section)
                missingAdapter!!.notifyDataSetChanged()
//                ActivityMain.ActivityUpdateListContent(myContext);
//                ActivityMain.activityAdapter!!.notifyDataSetChanged()
                val mydb: DatabaseHandler = DatabaseHandler(myContext)
                mydb.SetCurrentSection(section)
            }
        }

    }



    companion object {
        var missingAdapter: MissingAdapter? = null;
        var missingList = arrayListOf<ScoreModel>()
       var cboSectionMiss: Spinner? = null;

        fun MissingUpdateListContent(context: Context, sortData:String, sectioncode:String, category:String="NO") {
            val db: DatabaseHandler = DatabaseHandler(context)
            val term = Util.GetCurrentGradingPeriod(context)

            val myList: List<ScoreModel> = db.GetMissingActivity(sectioncode, sortData, term, category)
            missingList.clear()
            for (e in myList) {
                missingList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
            }

        }
    }




    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionMissing.setAdapter(sectionAdapter);
        cboSectionMiss = findViewById(R.id.cboSectionMissing) as Spinner
    }


    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionMissing.setSelection(index)
    }


    private fun SectionViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listMissing.layoutManager = layoutmanager
        missingAdapter = MissingAdapter(this, missingList)
        listMissing.adapter = missingAdapter
    }



}


fun DatabaseHandler.GetMissingActivity(sectioncode: String,  sortData: String = "ACTIVITYCODE", term:String = "FIRST", category:String="NO"): ArrayList<ScoreModel> {
    val scoreList: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
    var sql: String
    sql = """   SELECT * FROM TBSCORE_QUERY 
                    WHERE SectionCode ='$sectioncode'  
                    AND GradingPeriod = '$term'
                    and REMARK NOT IN ('ABSENT', 'DROPPED', 'N/A')
                    and SubmissionStatus ='NO'
                    """
//    if (remark<> )
//        sql = sql +  " AND Score =0 AND SubmissionStatus <> 'DRP' ORDER BY $sortData"
//    else if (category =="ZERO")
//        sql = sql +  " AND Score =0 ORDER BY $sortData"
//    else
//        sql = sql +  " AND  SubmissionStatus ='DRP' ORDER BY $sortData"

    Log.e("SSS", sql)

   // FirstName	LastName		SectionCode	StudentNo	Score	Remark
//   SubmissionStatus	AdjustedScore	Gender	Section	GrpNumber	GradingPeriod
 //  Description	Item


    val db = this.readableDatabase
    var cursor: Cursor? = null
    try {
        cursor = db.rawQuery(sql, null)
    } catch (e: SQLiteException) {
        db.execSQL(sql)
        return ArrayList()
    }

    Log.e("YYY", cursor.count.toString())
    var completeName = ""
    if (cursor.moveToFirst()) {
        do {
            //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
            var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
            var studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))
            var firstName = cursor.getString(cursor.getColumnIndex("FirstName"))
            var lastName = cursor.getString(cursor.getColumnIndex("LastName"))
            var score = cursor.getString(cursor.getColumnIndex("Score")).toInt()
           var adjustedScore = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
            var remark = cursor.getString(cursor.getColumnIndex("Remark"))
            var submissionStatus =  cursor.getString(cursor.getColumnIndex("SubmissionStatus"))
            var grp =  cursor.getString(cursor.getColumnIndex("GrpNumber"))
            var desc =  cursor.getString(cursor.getColumnIndex("Description"))

            // FirstName	LastName		SectionCode	StudentNo	Score	Remark
            //   SubmissionStatus	AdjustedScore	Gender	Section	GrpNumber	GradingPeriod
            //  Description	Item


              val att  =ScoreModel(activityCode, sectioncode, firstName , lastName,studentNo, completeName, score, remark, "CLOSED", "OFF",submissionStatus, adjustedScore, grp, desc)
            scoreList.add(att)
        } while (cursor.moveToNext())
    }
    return scoreList
}

