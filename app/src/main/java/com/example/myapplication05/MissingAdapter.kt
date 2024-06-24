//package com.example.myapplication05
//
//class MissingAdapter {}]]

//package com.example.myapplication05
//
//class MissingRow {}


package com.example.myapplication05



import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
import kotlinx.android.synthetic.main.attendance_main.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.dialog_message.view.*
import kotlinx.android.synthetic.main.missingrow.view.*
import kotlinx.android.synthetic.main.missingrow.view.rowBtnEdit
import kotlinx.android.synthetic.main.missingrow.view.rowBtnSave
import kotlinx.android.synthetic.main.missingrow.view.rowtxtScore
import kotlinx.android.synthetic.main.random_student.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.section_row.view.*
import kotlinx.android.synthetic.main.summary_main.*
import kotlinx.android.synthetic.main.summary_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.section_main.view.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord as btnSaveRecord1


class MissingAdapter(val context: Context, val missing: List<ScoreModel>) :
    RecyclerView.Adapter<MissingAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.missingrow, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return missing.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = missing[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentMissing: ScoreModel? = null
        var currentPosition: Int = 0

        init {
            itemView.rowBtnEdit.setOnClickListener {
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                itemView.rowBtnSave.setVisibility(View.VISIBLE);
                itemView.rowtxtScore.isEnabled = true
            } //init

            itemView.btnRemark.setOnClickListener {
                          } //init



            itemView.rowBtnSave.setOnClickListener {
                val db: TableActivity = TableActivity(context)
                var myscore = itemView.rowtxtScore.text.toString()
                val e = currentMissing
                val activityItem = db.GetActivityDetail( e!!.sectioncode, e!!.activitycode,  "ITEM")
                var score= 0;
                var oldscore = e!!.score
                try {
                    score = Integer.parseInt(myscore)
                } catch (e: NumberFormatException) {
                    score= 0
                }

                if (score == 0)
                    MissingMain.missingList[currentPosition].SubmissionStatus = "NO"
                else
                    MissingMain.missingList[currentPosition].SubmissionStatus = "OK"

                if (score> activityItem.toInt()) Util.Msgbox(context, "The Score is invalid")
                else {
                    itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                    itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                    itemView.rowtxtScore.isEnabled = false
                    MissingMain.missingList[currentPosition].status = "CLOSED"
                    MissingMain.missingList[currentPosition].score = score
                    db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNo, e.score, e.remark, e.SubmissionStatus)
                } //init
            }

        } //init


        fun setData(myatt: ScoreModel?, pos: Int) {
            itemView.rowtxtName.text = myatt!!.lastnanme + "," + myatt!!.firstname
            itemView.txtDescription.text = myatt!!.description
            itemView.rowtxtScore.isEnabled = false
            itemView.rowBtnEdit.setVisibility(View.VISIBLE);
            itemView.rowBtnSave.setVisibility(View.INVISIBLE);
            itemView.rowtxtScore.setText(myatt!!.score.toString())
            //itemView.btnR.setText(myatt!!.remark.toString())
            this.currentMissing = myatt;
            this.currentPosition = pos
        }


    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}



 fun DatabaseHandler.EditSection(sectionName:String, sectioncode:String, school:String, status:String, origSection:String, desc:String, folderLink:String )  {
     //SubjectCode	SubjectDescription	PT	Quiz	Exam	Project	ClassStanding	Participation	ClassStandingCoverage
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

    var sql = """
        UPDATE TBSECTION 
         SET  SectionName='$sectionName'        
         ,  Status='$status'        
         ,  School='$school'        
         ,  RealSectionName='$origSection'        
         ,  SubjectCode='$scode'  
         ,  SectionFolderLInk='$folderLink'  
         WHERE  SectionCode='$sectioncode'        
    """.trimIndent()
    Log.e("ppp765", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}



private fun DatabaseHandler.UpdateMessage(sectioncode:String, msg:String)  {
    var sql = """
        UPDATE TBSECTION 
         SET  Message='$msg'        
         WHERE  SectionCode='$sectioncode'        
    """.trimIndent()
    val db = this.writableDatabase
    db.execSQL(sql)
}


private fun DatabaseHandler.UpdateSectionStatus(sectioncode:String, status:String)  {
    var sql = """
        UPDATE TBSECTION 
         SET  Status='$status'        
         WHERE  SectionCode='$sectioncode'        
    """.trimIndent()
    val db = this.writableDatabase
    db.execSQL(sql)
}





private fun DatabaseHandler.DeleteSection(sectioncode:String)  {

    var sql = "DELETE FROM TBSECTION " +
            " where  $TBSECTION_CODE='$sectioncode'"
    val db = this.writableDatabase
    db.execSQL(sql)
}
//
//
//fun GetSchoolIndex(search: String, context: Context): Int {
//    val arrGroup: Array<String> = context.getResources().getStringArray(R.array.school)
//    val index = arrGroup.indexOf(search)
//    Log.e("xx", index.toString() + "   " + search)
//    return index
//}
//
//
//fun GetVisibilityIndex(search: String, context: Context): Int {
//    val arrGroup: Array<String> = context.getResources().getStringArray(R.array.visibilityMode)
//    val index = arrGroup.indexOf(search)
//    Log.e("uyy", index.toString() + "   " + search)
//
//    return index
//}
//







