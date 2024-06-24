package com.example.myapplication05

//class RecitationAdapter {}


//package com.example.myapplication05
//
//class EnrolleAdapter {}
//
//package com.example.myapplication05

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.btnAttendandeStatus
import kotlinx.android.synthetic.main.attendance_row.view.btnName
import kotlinx.android.synthetic.main.attendance_row.view.txtAttendanceStud
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.rowBtnMenu
import kotlinx.android.synthetic.main.recitation_row.view.btnRecitationName
import kotlinx.android.synthetic.main.recitation_row.view.btnRecitationStatus
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.student_row.view.btnIndividual
import kotlinx.android.synthetic.main.student_row.view.txtFirstName
import kotlinx.android.synthetic.main.student_row.view.txtGender
import kotlinx.android.synthetic.main.student_row.view.txtLastName
import kotlinx.android.synthetic.main.student_row.view.txtOrderNum
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class RecitationAdapter(val context: Context, val recitation: List<EnrolleModel>) :
    RecyclerView.Adapter<RecitationAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context)
    var NUM = 1;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.recitation_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return recitation.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val person = recitation[position]
        holder.setData(person, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                                               PopupMenu.OnMenuItemClickListener {
        var currentStudent: EnrolleModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnRecitationStatus.setOnClickListener {
                showPopMenu(itemView) //                var p

            }

            itemView.btnRecitationName.setOnClickListener {
                Log.e("3489", "adapter")
                ShowPicture(currentStudent!!.lastname + "," + currentStudent!!.firstname, currentStudent!!.studentno)
                RecitationMain.varTxtFirstName!!.text = currentStudent!!.firstname
                RecitationMain.varTxtLasttName!!.text = currentStudent!!.lastname

                RecitationMain.btn_RecAbsent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))
                RecitationMain.btn_RecCorrect!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))
                RecitationMain.btn_RecEffort!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))
                RecitationMain.btn_RecNoAns!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))

                val dhandler: DatabaseHandler = DatabaseHandler(context);


                var theDate = RecitationMain.varTxtDate!!.text.toString()
                var rem =
                    dhandler.GetRecordRecitation(currentStudent!!.studentno, theDate, currentStudent!!.Section)
                if (rem == "CORRECT") {
                    RecitationMain.btn_RecCorrect!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6"))) // itemView.btnRecitationName.setBackgroundColor(Color.parseColor("#FFB74D"))
                } else if (rem == "EFFORT") {
                    RecitationMain.btn_RecEffort!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))

                } else if (rem == "NO ANS") {
                    RecitationMain.btn_RecNoAns!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))

                } else if (rem == "ABSENT") {
                    RecitationMain.btn_RecAbsent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FCF55F")))

                } //
                Log.e("3489", "adapter")
                RecitationMain.txt_StudentNo!!.setText(currentStudent!!.studentno)
                RecitationMain.GetIndivualRecitationRecord(context, currentStudent!!.studentno)
                RecitationMain.adapterRecitationIndividual!!.notifyDataSetChanged()

                RecitationMain.btn_RecAbsent!!.isVisible = true
                RecitationMain.btn_RecCorrect!!.isVisible = true
                RecitationMain.btn_RecEffort!!.isVisible = true
                RecitationMain.btn_RecNoAns!!.isVisible = true
                RecitationMain.varImgRecitationPicture!!.isVisible = true

            }
        }


        fun setData(pp: EnrolleModel?, pos: Int) {

            itemView.btnRecitationName.text =
                pp!!.lastname.toUpperCase() + " " + pp!!.firstname.toUpperCase()
            this.currentStudent = pp;
            this.currentPosition = pos
            val dhandler: DatabaseHandler = DatabaseHandler(context);
            var theDate = RecitationMain.varTxtDate!!.text.toString()
            var rem = dhandler.GetRecordRecitation(pp!!.studentno, theDate, pp!!.Section)
            Log.e("@@@", rem)
            if (rem == "CORRECT") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6"))) // itemView.btnRecitationName.setBackgroundColor(Color.parseColor("#FFB74D"))
            } else if (rem == "EFFORT") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))

            } else if (rem == "NO ANS") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))

            } else if (rem == "ABSENT") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FCF55F")))
            } else if (rem == "-") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d3d3d3")))
            }


            //            if (myatt!!.attendanceStatus == "P") {
            //                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6")))
            //            } else if (myatt!!.attendanceStatus == "A") {
            //                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))
            //            } else if (myatt!!.attendanceStatus == "L") {
            //                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE"))) //itemView.btnName.setBackgroundColor(Color.parseColor("#"))
            //            } else if (myatt!!.attendanceStatus == "E") {
            //                itemView.btnName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BA68C8"))) //


        }

        fun showPopMenu(v: View) {
            val popup = PopupMenu(context, itemView.btnRecitationName, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_recitation)
            popup.show()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            val selected = item.toString() //

            if (selected == "CORRECT") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6"))) // itemView.btnRecitationName.setBackgroundColor(Color.parseColor("#FFB74D"))
            } else if (selected == "EFFORT") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))

            } else if (selected == "NO ANS") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))

            } else if (selected == "ABSENT") {
                itemView.btnRecitationName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BA68C8")))

            }


            val dhandler: DatabaseHandler = DatabaseHandler(context);
            var theDate = RecitationMain.varTxtDate!!.text.toString()

            dhandler.RecordRecitation(currentStudent!!.studentno, theDate, selected, currentStudent!!.Section, context)
            RecitationMain.ShowChart(context)
            RecitationMain.btn_RecAbsent!!.isVisible = true
            RecitationMain.btn_RecCorrect!!.isVisible = true
            RecitationMain.btn_RecEffort!!.isVisible = true
            RecitationMain.btn_RecNoAns!!.isVisible = true
            RecitationMain.varImgRecitationPicture!!.isVisible = true
            return true
        }


        fun ShowPicture(studName: String, studNumber: String) {

            try {

                val db2: DatabaseHandler = DatabaseHandler(context)
                var origSection = db2.GetStudentOriginalSection(studNumber) //
                Log.e("5685", origSection) //      var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
                val sdCard =
                    Environment.getExternalStorageDirectory() //        val section = TestCapture.viewcapture!!.cboSectionPic.getSelectedItem().toString();
                val path = sdCard.absolutePath + "/Quiz/StudentPicture/" + origSection

                val f: File = File(path, studName + ".jpg")
                Log.e("5685", studName + ".jpg") //
                if (f.exists()) {
                    val f: File = File(path, studName + ".jpg")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    RecitationMain.varImgRecitationPicture!!.setImageBitmap(b)
                } else { // TestCapture.viewcapture!!.imgStudent.setImageBitmap(null)
                    val f: File = File(sdCard.absolutePath + "/Quiz/StudentPicture/", "nopic.png")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    RecitationMain.varImgRecitationPicture!!.setImageBitmap(b)
                }


                // val img = findViewById<View>(R.id.imgStudent) as ImageView

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }


    }
} // // //fun DatabaseHandler.CheckEnrollment2233 (studentID:String, sectionName:String) :Boolean { //    val sectionList: ArrayList<String> = ArrayList<String>() //    var sql =""
//    sql = """
//            SELECT *
//            FROM tbenroll WHERE Section= '$sectionName'
//            AND  StudentID= '$studentID'
//    """.trimIndent()
//    Log.e("SQLSS", sql)
//
//    val db = this.readableDatabase
//    var cursor: Cursor? = null
//    cursor = db.rawQuery(sql, null)
//
//    if (cursor.moveToFirst()) {
//        return true
//    }
//    else
//        return false
//
//}
//
//
//
//fun DatabaseHandler.UpdateLink (studentID:String, sectionName:String, link:String){
//    var  sql = """
//            Update tbenroll
//            SET  FolderLink= '$link'
//            WHERE Section= '$sectionName'
//            AND  StudentNo= '$studentID'
//    """.trimIndent()
//
//    Log.e("SQLSS", sql)
//    //    sql = "Update tbenroll SET  FolderLink= '-'"
//    //    Log.e("SQLSS200", sql)
//    val db = this.writableDatabase
//    db.execSQL(sql)
//
//}
//
//
//
//fun DatabaseHandler.GetEmail (studentID:String):String {
//    var  sql = """
//            select * from  tbstudent_info
//            where StudentID= '$studentID'
//    """.trimIndent()
//
//    val db = this.readableDatabase
//    var cursor: Cursor? = null
//    cursor = db.rawQuery(sql, null)
//    if (cursor.moveToFirst()) {
//        return  cursor.getString(cursor.getColumnIndex("emailAddress"))
//    }
//    else {
//        return ""
//    }
//
//}







