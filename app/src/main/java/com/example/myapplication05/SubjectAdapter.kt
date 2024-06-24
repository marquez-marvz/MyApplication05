//package com.example.myapplication05
//
//class SubjectAdapter {}

package com.example.myapplication05



import android.R.attr.button
import android.content.Context
import android.database.Cursor
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
import com.example.myapplication05.R.layout.section_dialog
import kotlinx.android.synthetic.main.attendance_main.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.dialog_message.view.*
import kotlinx.android.synthetic.main.random_student.view.*
import kotlinx.android.synthetic.main.section_row.view.*
import kotlinx.android.synthetic.main.summary_main.*
import kotlinx.android.synthetic.main.summary_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.section_main.view.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.subject_row.view.*
import kotlinx.android.synthetic.main.subject_row.view.btnEdit
import kotlinx.android.synthetic.main.subject_row.view.btnSave
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord as btnSaveRecord1


class SubjectAdapter(val context: Context, val section: List<SubjectModel>) :
    RecyclerView.Adapter<SubjectAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.subject_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return section.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = section[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSubject : SubjectModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnEdit.setOnClickListener {
                itemView.txtDescription.isEnabled = true
                itemView.txtPT.isEnabled = true
                itemView.txtQuiz.isEnabled = true
                itemView.txtExam.isEnabled = true
                itemView.txtProject.isEnabled = true
                itemView.txtClassStanding.isEnabled = true
                itemView.txtParticipation.isEnabled = true
                itemView.btnEdit.setVisibility(View.INVISIBLE);
                itemView.btnSave.setVisibility(View.VISIBLE);
            }

            itemView.btnSave.setOnClickListener {
                itemView.txtDescription.text
                itemView.txtPT.isEnabled = true
                itemView.txtQuiz.isEnabled = true
                itemView.txtExam.isEnabled = true
                itemView.txtProject.isEnabled = true
                itemView.txtClassStanding.isEnabled = true
                itemView.txtParticipation.isEnabled = true
                val db: DatabaseHandler = DatabaseHandler(context)
                db.UpdateSubjectPercentage(
                        itemView.txtSubjectCode.text.toString(),
                        itemView.txtDescription.text.toString(),
                    itemView.txtPT.text.toString().toInt(),
                    itemView.txtQuiz.text.toString().toInt(),
                    itemView.txtExam.text.toString().toInt(),
                    itemView.txtProject.text.toString().toInt(),
                    itemView.txtParticipation.text.toString().toInt(),
                    itemView.txtClassStanding.text.toString().toInt()
                )
                itemView.btnEdit.setVisibility(View.VISIBLE);
                itemView.btnSave.setVisibility(View.INVISIBLE);
            }
        } //init


        fun setData(myatt: SubjectModel?, pos: Int) {
//            itemView.rowSectionName.text = myatt!!.sectionName
//            itemView.rowSectionCode.text = myatt!!.sectionCode
            itemView.txtSubjectCode.text = myatt!!.subjectCode
            itemView.txtDescription.setText( myatt!!.subjectDescription)
            itemView.txtPT.setText(myatt!!.pt)
            itemView.txtQuiz.setText(myatt!!.quiz)
            itemView.txtExam.setText(myatt!!.exam)
            itemView.txtProject.setText(myatt!!.project)
            itemView.txtParticipation.setText(myatt!!.participation)
            itemView.txtClassStanding.setText(myatt!!.classStanding)


            this.currentSubject = myatt;
            this.currentPosition = pos
        }


    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}


fun DatabaseHandler.UpdateSubjectPercentage(subjcode:String, desc:String, pt:Int, quiz:Int, exam:Int, proj:Int, part:Int,cs:Int){
   Log.e("CS", cs.toString())
    var sql = """
             update tbsubject set SubjectDescription='$desc', 
             PT='$pt', 
             Quiz='$quiz', 
             Exam='$exam', 
             Project='$proj', 
             Participation='$part', 
             ClassStanding='$cs', 
             ClassStandingCoverage='QUIZ,PT,PARTICIPATION'
             where SubjectCode   ='$subjcode'
        """.trimIndent()
    val db = this.writableDatabase
    db.execSQL(sql)
}




