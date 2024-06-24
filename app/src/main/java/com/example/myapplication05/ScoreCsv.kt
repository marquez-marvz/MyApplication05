//package com.example.myapplication05
//
//class ScoreCsv {}
//

package com.example.myapplication05


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.score_csv.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.btnSubmissionStatus
import kotlinx.android.synthetic.main.score_individual_row.view.rowtxtScore
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_row.view.* //import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.score_row.view.btnGroup
import kotlinx.android.synthetic.main.score_row.view.rowtxtRemark //import kotlinx.android.synthetic.main.score_row.view.btnSubmissionStatus //import kotlinx.android.synthetic.main.score_row.view.rowBtnEdit
//import kotlinx.android.synthetic.main.score_row.view.rowBtnSave
//import kotlinx.android.synthetic.main.score_row.view.rowtxtScore
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.lang.Integer.parseInt


class ScoreCsvAdapter(val context: Context, val score: List<ScoreCsvModel>) :
    RecyclerView.Adapter<ScoreCsvAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.score_csv, parent, false)

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

        var currentScore: ScoreCsvModel? = null
        var currentPosition: Int = 0


        init {
            itemView.btnCsvSave.setOnClickListener {
                try {
                    var e = currentScore
                    val db: TableActivity = TableActivity(context)
                    db.UpdateStudentRecord(Util.ACT_CODE, Util.ACT_CURRENT_SECTION, e!!.studentNo, e!!.score.toInt(), e!!.remark, "YES", 0)
                    itemView.btnCsvSave.setVisibility(View.INVISIBLE);
                    itemView.btnCsvCheck.setVisibility(View.VISIBLE);
                    ScoreMain.scoreCsvList[currentPosition].status = "YES"
                }
                catch (e:Exception){
                    Util.Msgbox(context, "Error in Saving")
                }
            }

        }

        fun setData(myscore: ScoreCsvModel?, pos: Int) {
            itemView.txtCsvName.text = myscore!!.completeName
            itemView.btnCsvRemark.text = myscore!!.remark
            itemView.txtCsvStudentNo.text = myscore!!.studentNo
            itemView.txtCsvScore.text = myscore!!.score.toString()
            if (myscore!!.remark != "-")
                itemView.btnCsvRemark.setBackgroundColor(Color.parseColor("#64B5F6"))
            else
                itemView.btnCsvRemark.setBackgroundResource(android.R.drawable.btn_default);
            if (myscore!!.status=="NO"){
                itemView.btnCsvSave.setVisibility(View.VISIBLE);
                itemView.btnCsvCheck.setVisibility(View.INVISIBLE);
            }
            else{
                itemView.btnCsvSave.setVisibility(View.INVISIBLE);
                itemView.btnCsvCheck.setVisibility(View.VISIBLE);
            }
            this.currentScore = myscore
            this.currentPosition = pos
        }


    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}



