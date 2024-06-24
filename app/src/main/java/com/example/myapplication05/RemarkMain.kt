//package com.example.myapplication05
//
//class ma {}

package com.example.myapplication05

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.RemarkMain.Companion.btnRemark
import kotlinx.android.synthetic.main.att_individual_main.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.group_main.*
import kotlinx.android.synthetic.main.group_main.cboActivity
import kotlinx.android.synthetic.main.group_main.cboStudent
import kotlinx.android.synthetic.main.group_row.view.*
import kotlinx.android.synthetic.main.openpdf.*
import kotlinx.android.synthetic.main.openpdf.cboSectionActivity
import kotlinx.android.synthetic.main.remark_main.*
import kotlinx.android.synthetic.main.remark_main.view.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*

//class Grouping {}


class RemarkMain : AppCompatActivity() {
    var studentList = arrayListOf<EnrolleModel>()

    var con = this
    val db2: DatabaseHandler = DatabaseHandler(this)

    companion object {
        var btnRemark: Button? = null
        var alert: AlertDialog? = null
        var remark_section = ""

        // var buttons: Array<Button> = Array(10)
        var buttons: ArrayList<Button> = arrayListOf()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remark_main) //        SetSpinnerAdapter()


        //        buttons.add(findViewById(R.id.btnRemark3) as Button)
        //        buttons.add(findViewById(R.id.btnRemark4) as Button)
        //        buttons.add(findViewById(R.id.btnRemark5) as Button)
        //        buttons.add(findViewById(R.id.btnRemark6) as Button)
        //        buttons.add(findViewById(R.id.btnRemark7) as Button)
        //        buttons.add(findViewById(R.id.btnRemark8) as Button)
        //        buttons.add(findViewById(R.id.btnRemark9) as Button)
        ////        buttons[1] = findViewById(R.id.btnRemark2) as Button
        //        buttons[2] = findViewById(R.id.btnRemark3) as Button

        //        for (i in 0..2) {
        //            buttons[i].setOnClickListener {
        //                  Log.e("123", "SSS   " + i)
        //            }
        //        }
    }

    fun SetButtonArray(v: View) {
        Companion.buttons.clear()
        Companion.buttons.add(v.findViewById(R.id.btnRemark1) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark2) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark3) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark4) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark5) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark6) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark7) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark8) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark9) as Button)
        Companion.buttons.add(v.findViewById(R.id.btnRemark10) as Button)
    }

    fun DisplayRenark(v: View, context: Context, studentNo: String, activityCode: String) {
        SpinnerRemarkAdapter(v, context)
        ShowRemark(v, context)



        v.txtAddRemark.isEnabled = false


        //        v.btnRemark1.setOnClickListener {
        //            SetRemark(v.btnRemark1.text.toString(),context, studentNo, activityCode)
        //        }
        //
        //        v.btnRemark2.setOnClickListener {
        //            SetRemark(v.btnRemark2.text.toString(),context, studentNo, activityCode)
        //        }

        //        v.btnRemark3.setOnClickListener {
        //            SetRemark(v.btnRemark3.text.toString(),context, studentNo, activityCode)
        //        }
        //
        //        v.btnRemark4.setOnClickListener {
        //            SetRemark(v.btnRemark5.text.toString() ,context, studentNo, activityCode)
        //        }
        //
        //
        //        v.btnRemark5.setOnClickListener {
        //            SetRemark(v.btnRemark5.text.toString() ,context, studentNo, activityCode)
        //        }
        //
        //
        //        v.btnRemark6.setOnClickListener {
        //            SetRemark(v.btnRemark6.text.toString() ,context, studentNo, activityCode)
        //        }
        //
        //        v.btnRemark7.setOnClickListener {
        //            SetRemark(v.btnRemark7.text.toString() ,context, studentNo, activityCode)
        //        }
        //
        //
        //        v.btnRemark8.setOnClickListener {
        //            SetRemark(v.btnRemark8.text.toString(), context, studentNo, activityCode)
        //        }
        //
        //        v.btnRemark9.setOnClickListener {
        //            SetRemark(v.btnRemark9.text.toString() , context, studentNo, activityCode)
        //        }


        v.btnRemark10.setOnClickListener {
            SetRemark(v.btnRemark10.text.toString(), context, studentNo, activityCode)
        }


        v.btnRemark1.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark1.text.toString())
            true
        }

        v.btnRemark2.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark2.text.toString())
            true
        }

        v.btnRemark3.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark3.text.toString())
            true
        }

        v.btnRemark4.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark4.text.toString())
            true
        }

        v.btnRemark5.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark5.text.toString())
            true
        }

        v.btnRemark6.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark6.text.toString())
            true
        }

        v.btnRemark7.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark7.text.toString())
            true
        }

        v.btnRemark8.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark8.text.toString())
            true
        }

        v.btnRemark9.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark9.text.toString())
            true
        }

        v.btnRemark10.setOnLongClickListener {
            DelRenark(v, context, v.btnRemark10.text.toString())
            true
        }





        v.btnAddRemark.setOnClickListener {
            v.txtAddRemark.setText("")
            v.btnAddRemark.setVisibility(View.INVISIBLE);
            v.btnSaveRemark.setVisibility(View.VISIBLE);
            v.txtAddRemark.isEnabled = true
        }

        v.btnSaveRemark.setOnClickListener {
            val db: DatabaseHandler = DatabaseHandler(context)
            db.AddRemark(remark_section, v.txtAddRemark.text.toString())
            v.txtAddRemark.isEnabled = false
            v.txtAddRemark.setText("")
            v.btnAddRemark.setVisibility(View.VISIBLE);
            v.btnSaveRemark.setVisibility(View.INVISIBLE);
            ShowRemark(v, context)
        }

        v.cboRemark.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var rem = v.cboRemark.getSelectedItem().toString(); // v.txtAddRemark.setText(rem)
                if (rem != "") {
                    val db: DatabaseHandler = DatabaseHandler(context)
                    db.AddRemark(remark_section, rem) //                v.txtAddRemark.isEnabled = false
                    ShowRemark(v, context)
                }
            }
        }

    }

    fun DelRenark(v: View, context: Context, txt: String) {
        val db: DatabaseHandler = DatabaseHandler(context)


        val dlgsection = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgsection)
            .setTitle("Do you want to delete " + txt)
        val inputDialog = mBuilder.show()
        inputDialog.setCanceledOnTouchOutside(false); //                dlgsection.txtSectionCode.setText(db.GetNewSectionCode())
        //                dlgsection.txtSectionCode.isEnabled= false

        dlgsection.btnYes.setOnClickListener { //
            db.DeleteRemark(remark_section, txt)
            ShowRemark(v, context)
            inputDialog.dismiss()
        }

        dlgsection.btnNo.setOnClickListener { //
            inputDialog.dismiss()
        }
    }


    fun SpinnerRemarkAdapter(v: View, context: Context) { //   val arrSection: Array<String> = Util.GetSectionList(this)
        val db: DatabaseHandler = DatabaseHandler(context)
        val arrRemark: ArrayList<String> = db.GetDistinctRemark(context)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrRemark)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        v.cboRemark.setAdapter(sectionAdapter);

    }

    fun ShowRemark(v: View, context: Context) {
        val db: DatabaseHandler = DatabaseHandler(context)
        var remarkList: ArrayList<String> = ArrayList<String>()
        remarkList = db.GetRemark(remark_section)
        v.btnRemark1.setVisibility(View.INVISIBLE);
        v.btnRemark2.setVisibility(View.INVISIBLE);
        v.btnRemark3.setVisibility(View.INVISIBLE);
        v.btnRemark4.setVisibility(View.INVISIBLE);
        v.btnRemark5.setVisibility(View.INVISIBLE);
        v.btnRemark6.setVisibility(View.INVISIBLE);
        v.btnRemark7.setVisibility(View.INVISIBLE);
        v.btnRemark8.setVisibility(View.INVISIBLE);
        v.btnRemark9.setVisibility(View.INVISIBLE);
        v.btnRemark10.setVisibility(View.INVISIBLE);



        for (i in 0..remarkList.size - 1) {
            if (i == 0) {
                v.btnRemark1.text = remarkList[0]
                v.btnRemark1.setVisibility(View.VISIBLE);
            }

            if (i == 1) {
                v.btnRemark2.text = remarkList[1]
                v.btnRemark2.setVisibility(View.VISIBLE);
            }

            if (i == 3) {
                v.btnRemark3.text = remarkList[2]
                v.btnRemark3.setVisibility(View.VISIBLE);
            }

            if (i == 4) {
                v.btnRemark4.text = remarkList[3]
                v.btnRemark4.setVisibility(View.VISIBLE);
            }

            if (i == 5) {
                v.btnRemark5.text = remarkList[4]
                v.btnRemark5.setVisibility(View.VISIBLE);
            }

            if (i == 6) {
                v.btnRemark6.text = remarkList[5]
                v.btnRemark6.setVisibility(View.VISIBLE);
            }

            if (i == 7) {
                v.btnRemark7.text = remarkList[6]
                v.btnRemark7.setVisibility(View.VISIBLE);
            }

            if (i == 8) {
                v.btnRemark8.text = remarkList[7]
                v.btnRemark8.setVisibility(View.VISIBLE);
            }

            if (i == 9) {
                v.btnRemark9.text = remarkList[8]
                v.btnRemark9.setVisibility(View.VISIBLE);
            }

            if (i == 10) {
                v.btnRemark10.text = remarkList[9]
                v.btnRemark10.setVisibility(View.VISIBLE);
            }
        }

    }

    fun SetRemark(text: String, context: Context, studentNo: String, activityCode: String) {

    }
}

fun DatabaseHandler.AddRemark(sectionName: String, remark: String) {
    var sql = """
             insert into tbremark  (Remark,	SectionCode)
             values('$remark', '$sectionName')
            """ //Remark	SectionCode
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}


fun DatabaseHandler.DeleteRemark(sectionName: String, remark: String) {
    var sql = """
             DELETE FROM tbremark 
            WHERE SectionCode= '$sectionName'
            AND Remark= '$remark'
    """.trimIndent()
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}


fun DatabaseHandler.GetRemark(sectionName: String): ArrayList<String> {
    val remarkList: ArrayList<String> = ArrayList<String>()
    var sql = """
            SELECT *
            FROM tbremark WHERE SectionCode= '$sectionName' order by Remark 
    """.trimIndent()
    val db2 = this.readableDatabase


    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    var x = 0;
    if (cursor.moveToFirst()) {
        do {
            var rem = cursor.getString(cursor.getColumnIndex("Remark"))
            remarkList.add(rem)
        } while (cursor.moveToNext())
    }
    return remarkList
}


fun DatabaseHandler.GetDistinctRemark(context: Context): ArrayList<String> {
    val remarkList: ArrayList<String> = ArrayList<String>()
    var sql = """
            SELECT DISTINCT(REMARK)
            FROM tbremark  order by Remark 
    """.trimIndent()
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    var x = 0;
    if (cursor.moveToFirst()) {
        do {
            var rem = cursor.getString(cursor.getColumnIndex("Remark"))
            remarkList.add(rem)
        } while (cursor.moveToNext())
    }
    return remarkList
}


fun DatabaseHandler.UpdateStudentRemark(sectionCode: String, activityCode: String, studentNo: String, remark: String) {
    var sql = """ UPDATE TBSCORE
                    SET   Remark='$remark'
                    WHERE SectionCode ='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND StudentNo='$studentNo'  """
    Log.e("REM", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}


