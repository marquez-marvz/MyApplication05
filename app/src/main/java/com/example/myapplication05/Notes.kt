//package com.example.myapplication05
//
//class Notes {}

package com.example.myapplication05

import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.group_main.*

import kotlinx.android.synthetic.main.section_notes.*
import kotlinx.android.synthetic.main.section_notes.btnCancel
import kotlinx.android.synthetic.main.section_notes.btnFirst
import kotlinx.android.synthetic.main.section_notes.btnLast
import kotlinx.android.synthetic.main.section_notes.btnNext
import kotlinx.android.synthetic.main.section_notes.btnPrev
import kotlinx.android.synthetic.main.section_notes.txtCounter
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*

//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.openpdf.*
//import kotlinx.android.synthetic.main.score_main.*
//import kotlinx.android.synthetic.main.student_dialog.view.*
//import kotlinx.android.synthetic.main.student_import.view.*
//import kotlinx.android.synthetic.main.student_main.*
//import kotlinx.android.synthetic.main.student_main.btnAdd
//import kotlinx.android.synthetic.main.student_main.btnImport
//import kotlinx.android.synthetic.main.task_dialog_import.view.*
//import kotlinx.android.synthetic.main.task_dialog_import.view.cboSheetList
//import kotlinx.android.synthetic.main.util_inputbox.view.*
import org.json.JSONArray
import java.io.*


class Notes : AppCompatActivity() {
    var sectionName = ""
    var noteList = arrayListOf<SectionNoteModel>()
    var noteCounter = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.section_notes)
        noteCounter = 0
        SetSpinnerAdapter()
        SetDefaultSection()
        GetNoteList(sectionName)
        Log.e("1234", noteList.size.toString())
        val db2: DatabaseHandler = DatabaseHandler(this)

        if (noteList.size > 0) {
            DisplayRecord()
        }
        txtNote.isEnabled = false
        txtNote.setTextColor(Color.BLACK);

        txtNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //  Log.d("LOGER", s.length.toString() + "") // show the count of the Text in the TextView
            }

            override fun afterTextChanged(s: Editable) {
                     Log.e("HEllo", "HHHH")

                    var remark = txtNote.text.toString()
                    var remarkID = txtNoteID.text.toString()
                    if (remark!="") {
                        db2.SaveSectionNote(remark, sectionName, remarkID)
                    }
                }

        })



        btnSave.setOnClickListener {
            val db2: DatabaseHandler = DatabaseHandler(this)
            var remark = txtNote.text.toString()
            var remarkID = txtNoteID.text.toString()
            db2.SaveSectionNote(remark, sectionName, remarkID)
            btnEdit.isVisible = true
            btnDelete.isVisible = true
            btnSave.isVisible = false
            btnAdd.isVisible = true
            btnCancel.isVisible = false
            txtNote.isEnabled = false
            GetNoteList(sectionName)

        }

        btnDelete.setOnClickListener {
            val db2: DatabaseHandler = DatabaseHandler(this)
            var remarkID = txtNoteID.text.toString()

            val dlgsection = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgsection)
                .setTitle("Do you want to delete this note ")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false);

            dlgsection.btnYes.setOnClickListener { //
                var remarkID = txtNoteID.text.toString()
                val db2: DatabaseHandler = DatabaseHandler(this)
                db2.SetNoteStatus(remarkID, "DELETED")
                GetNoteList(sectionName)
                inputDialog.dismiss()

            }

            dlgsection.btnNo.setOnClickListener { //
                inputDialog.dismiss()
            }

            //b2.SaveSectionNote(remark, sectionName, saveStatus, remarkID)


        }


        btnSetStatus.setOnClickListener{
            var remarkID = txtNoteID.text.toString()
            val db2: DatabaseHandler = DatabaseHandler(this)
            Log.e("123",btnSetStatus.text.toString())
            if (btnSetStatus.text =="ACTIVE") {
                btnSetStatus.text = "HIDE"
                db2.SetNoteStatus(remarkID, "HIDE")
            }
            else{
                btnSetStatus.text = "ACTIVE"
                db2.SetNoteStatus(remarkID, "ACTIVE")
            }

        }

        btnShowAll.setOnClickListener {

            GetNoteList(sectionName, "SHOW ALL")
            noteCounter = 0
            DisplayRecord()

        }
        btnAdd.setOnClickListener {
//            btnEdit.isVisible = false
//            btnDelete.isVisible = false
//            btnSave.isVisible = true
          //  saveStatus = "NEW RECORD"
            txtNote.setText("")
            txtNote.isEnabled = true
//            btnCancel.isVisible = true
//            btnAdd.isVisible = false
            txtNote.requestFocus()
            val db2: DatabaseHandler = DatabaseHandler(this)

            txtNoteID.setText(db2.GetNewNoteID(sectionName))
        }

        btnEdit.setOnClickListener {
//            btnEdit.isVisible = false
//            btnDelete.isVisible = false
//            btnSave.isVisible = true
//            saveStatus = "EDIT RECORD"
            txtNote.isEnabled = true
//            btnCancel.isVisible = true
//            btnAdd.isVisible = false
            txtNote.requestFocus()
        }

        btnNext.setOnClickListener {
            Log.e("12345", noteCounter.toString() + "     " + noteList.size)
            if (noteCounter < noteList.size - 1) {
                noteCounter++
                txtNote.setText(noteList[noteCounter].Remark)
                txtNoteID.setText(noteList[noteCounter].RemarkID)
                txtCounter.setText((noteCounter + 1).toString() + "/" + noteList.size.toString())
            }
        }

        btnPrev.setOnClickListener {
            Log.e("aa", noteCounter.toString())
            if (noteCounter > 0) {
                noteCounter--
                txtNote.setText(noteList[noteCounter].Remark)
                txtNoteID.setText(noteList[noteCounter].RemarkID)
                txtCounter.setText((noteCounter + 1).toString() + "/" + noteList.size.toString())
            }
        }

        btnLast.setOnClickListener {
            if (noteList.size > 0) {
                noteCounter = noteList.size - 1
                txtNote.setText(noteList[noteCounter].Remark)
                txtNoteID.setText(noteList[noteCounter].RemarkID)
                txtCounter.setText((noteCounter + 1).toString() + "/" + noteList.size.toString())
            }

        }

        btnFirst.setOnClickListener {
            if (noteList.size > 0) {
                noteCounter = 0
                DisplayRecord()

            }
        }

        cboSectionNote.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 sectionName = cboSectionNote.getSelectedItem().toString();
                GetNoteList(sectionName)
                noteCounter = 0
                if (noteList.size > 0) {
                    DisplayRecord()
                }
                else{
                    if (noteList.size>0) {
                        txtNote.setText("")
                        txtNoteID.setText("")
                        txtCounter.setText("0/" + noteList.size.toString())
                        btnSetStatus.text = ""
                    }
                }

            }
        }

    }

    fun DisabledNavigation(b: Boolean) {
        btnFirst.isEnabled = b
        btnLast.isEnabled = b
        btnPrev.isEnabled = b
        btnNext.isEnabled = b
    }

    fun DisplayRecord() {
        Log.e("1234", noteCounter.toString())
        txtNote.setText(noteList[noteCounter].Remark)
        txtNoteID.setText(noteList[noteCounter].RemarkID)

        txtCounter.setText((noteCounter + 1).toString() + "/" + noteList.size.toString())
        btnSetStatus.text = (noteList[noteCounter].RemarkStatus)
    }


    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionNote.setAdapter(sectionAdapter);

    }

    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionNote.setSelection(index)
        sectionName = db.GetCurrentSection();
    }

    fun GetNoteList(section: String, status:String="ACTIVE ONLY") {
        val db2: DatabaseHandler = DatabaseHandler(this)
        noteList.clear()

        var note: List<SectionNoteModel>
        note = db2.GetSectionNoteList(section, status)
        Log.e("1335", note.size.toString())
        for (e in note) {
            var note: SectionNoteModel = SectionNoteModel()
            note = e
            noteList.add(note)
        }
    }

} //class

//


fun DatabaseHandler.SaveSectionNote(remark: String, sectionName: String, remarkID: String = "") {


    var sql = ""

    sql = """
                   select * from  tbsection_note
                    WHERE RemarkID ='$remarkID'
            """

    val db = this.readableDatabase
    val cursor2 = db.rawQuery(sql, null)

    if (cursor2.count ==0 ) {
        sql = """
             insert into tbsection_note  (RemarkID,	SectionCode,	RemarkDate,	RemarkTime,	Remark,	RemarkStatus)
             values('$remarkID', '$sectionName', '-', '-','$remark','ACTIVE')
            """
    }
    else  {
        sql = """
                   UPDATE tbsection_note
                    SET   Remark='$remark'
                    WHERE RemarkID ='$remarkID'
            """
    }
    Log.e("123", sql)
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}

fun DatabaseHandler.GetNewNoteID(sectioncode: String): String {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    val sql: String //RemarkID	GroupNUmber	SectionCode	RemarkDate	RemarkTime	Remark	RemarkSTatus
    sql = """
                SELECT * FROM tbsection_note 
                WHERE SectionCode='$sectioncode' 
                ORDER BY  RemarkID DESC
                """
    val db = this.readableDatabase

    var sql2 = """
                SELECT * FROM TBSECTION
                WHERE SectionName='$sectioncode' 
                """
    val cursor2 = db.rawQuery(sql2, null)

    var sectioncode = ""
    Log.e("211", sql2)
    Log.e("211", cursor2.count.toString())

    if (cursor2.moveToFirst()) {
        sectioncode = cursor2.getString(cursor2.getColumnIndex("SectionCode"))
    }
    Log.e("211", sectioncode)


    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        var actCode = cursor.getString(cursor.getColumnIndex("RemarkID"))
        var num = actCode.takeLast(3).toInt() + 1 // Grade>>>
        Util.Msgbox(context, num.toString())

        Log.e("211", sectioncode + "-" + Util.ZeroPad(num, 3))
        return sectioncode + "-" + Util.ZeroPad(num, 3)
    } else { // Util.Msgbox(context, "ACT-01" )
        return sectioncode + "-" + "001"
    } //  return  "helo"
}


fun DatabaseHandler.GetSectionNoteList(sectionName: String, status:String="ACTIVE ONLY"): ArrayList<SectionNoteModel> {
    val noteList: ArrayList<SectionNoteModel> = ArrayList<SectionNoteModel>()
    var sql = """
            SELECT *
            FROM tbSection_Note 
            WHERE SectionCode= '$sectionName' 
            
           
    """.trimIndent()
    if (status =="ACTIVE ONLY")
        sql = sql +   " AND   RemarkStatus='ACTIVE' order by RemarkID"
    else if  (status =="SHOW ALL")
        sql = sql +   " AND   RemarkStatus='ACTIVE' or   RemarkStatus='HIDE' order by RemarkID"

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        do { //            , 	SectionCode
            var note: SectionNoteModel = SectionNoteModel()
            note.SectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
            note.Remark = cursor.getString(cursor.getColumnIndex("Remark"))
            note.RemarkDate = cursor.getString(cursor.getColumnIndex("RemarkDate"))
            note.RemarkTime = cursor.getString(cursor.getColumnIndex("RemarkTime"))
            note.RemarkStatus = cursor.getString(cursor.getColumnIndex("RemarkStatus"))
            note.RemarkID = cursor.getString(cursor.getColumnIndex("RemarkID"))
            noteList.add(note)
        } while (cursor.moveToNext())
    }
    return noteList
}


fun DatabaseHandler.SetNoteStatus(remarkID: String, stat:String) {
    var sql = """
                     UPDATE tbsection_note
                    SET   RemarkStatus='$stat'
                    WHERE RemarkID ='$remarkID'
            """
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}


