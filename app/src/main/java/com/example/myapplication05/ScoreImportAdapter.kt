//package com.example.myapplication05
//
//class ScoreImportAdapter {}

package com.example.myapplication05


import android.content.*
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.import_score_row.view.btnEditImport
import kotlinx.android.synthetic.main.import_score_row.view.btnSaveImport
import kotlinx.android.synthetic.main.import_score_row.view.txtActivityCodeRowImport
import kotlinx.android.synthetic.main.import_score_row.view.txtNameImport
import kotlinx.android.synthetic.main.import_score_row.view.txtQuizImport
import kotlinx.android.synthetic.main.import_score_row.view.txtScoreImport
import kotlinx.android.synthetic.main.import_score_row.view.txtStudentNumberImport
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.rubric_student.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual.view.txtName
import kotlinx.android.synthetic.main.score_individual.view.txtStudentNo
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.score_row.view.rowBtnEdit
import kotlinx.android.synthetic.main.score_row.view.rowBtnSave
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.lang.Integer.parseInt


class ScoreImportAdapter(val context: Context, val score: List<ScoreImportModel>) :
    RecyclerView.Adapter<ScoreImportAdapter.MyViewHolder>() {
    var previousSelectedScoreButton: Button? = null
    var previousSelectedByFiveButton: Button? = null

    // var individualList: List<ScoreIndividualModel>
    var individualList = arrayListOf<ScoreIndividualModel>()
    var individualAdapter: ScoreIndividualAdapter? = null;
    var prev_position = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.import_score_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return score.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myScore = score[position]
        holder.setData(myScore, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentScore: ScoreImportModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnSaveImport.setOnClickListener {
                val db: TableActivity = TableActivity(context)
                val e = currentScore
                e!!.status = "OK"
                db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNumber, e.score, "OK", "OK", 0) // db.SaveLog(e.studentNo, e.sectioncode, e.activitycode, oldscore, e.score)
                itemView.btnSaveImport.isVisible = false
                itemView.btnEditImport.isVisible = true
            }


        } //init


        fun setData(myscore: ScoreImportModel?, pos: Int) { //            itemView.rowtxtName.text = myscore!!.completeName
            //            itemView.rowtxtRemark.text = myscore!!.remark
            itemView.txtNameImport.text = myscore!!.completeName
            itemView.txtActivityCodeRowImport.text = myscore!!.activitycode
            itemView.txtScoreImport.text = myscore!!.score.toString()
            itemView.txtStudentNumberImport.text = myscore!!.studentNumber
            itemView.txtQuizImport.text = myscore!!.quizTitle

            if (myscore!!.score == 0 ){
                    itemView.btnSaveImport.isVisible = false
                }
            else if (myscore.status == "-") {
                itemView.btnSaveImport.isVisible = true
                itemView.btnEditImport.isVisible = false
            } else {
                itemView.btnSaveImport.isVisible = false
                itemView.btnEditImport.isVisible = true
            }


            this.currentScore = myscore
            this.currentPosition = pos

        }

    }
}