//package com.example.myapplication05
//
//class GradeSectionAdapter {}]
//
//

//package com.example.myapplication05
//
//class ScoreSummary_ActivityAdapter {}


package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.activiy_main_row.view.btnSectionMain
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.grade_section_row.view.btnSectionName
import kotlinx.android.synthetic.main.score_csv.view.btnCsvRemark
import kotlinx.android.synthetic.main.score_csv.view.txtCsvName
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score2
import kotlinx.android.synthetic.main.scoresummary.btnActivity1
import kotlinx.android.synthetic.main.scoresummary_activityrow.view.btnActivityDesc
import kotlinx.android.synthetic.main.student_import.view.*
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_row.view.*

import kotlinx.android.synthetic.main.util_confirm.view.*


class GradeSectionAdapter(val context: Context, val section: List<SectionModel>) :
    RecyclerView.Adapter<GradeSectionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView =
            LayoutInflater.from(context).inflate(R.layout.grade_section_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return section.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = section[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var currentActivity: SectionModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnSectionName.setOnClickListener {
                val myGrades: Grades = Grades(context)

                Util.GRADE_SECTION = currentActivity!!.sectionName
                GradeMain.ShowGrades(context,  currentActivity!!.sectionName)
                GradeMain.UpdateCount(context,currentActivity!!.sectionName)
                GradeMain.ShowChartGrades(context,currentActivity!!.sectionName)
                GradeMain.vartxtScoreVersion!!.setText(myGrades.GetVersion(Util.GRADE_SECTION ).toString())
                val db: DatabaseHandler = DatabaseHandler(context)
                Log.e("4456", "")
                var ss = itemView.btnSectionName.text.toString()
                db.SetCurrentSection(ss)
                myGrades.UpdateVersion(Util.GRADE_SECTION)
                    notifyDataSetChanged()
            }

        } //init

        fun setData(myactivity: SectionModel?, pos: Int) {
          itemView.btnSectionName.text = myactivity!!.sectionName
//            Log.e("POS", pos.toString())
//            Log.e("POS", activity.size.toString())
            val db: DatabaseHandler = DatabaseHandler(context)
           if (myactivity!!.sectionName== db.GetCurrentSection()) {
               itemView.btnSectionName.setBackgroundColor(Color.parseColor("#69F0AE"))
           }
            else{
               itemView.btnSectionName.setBackgroundResource(android.R.drawable.btn_default); //
            }
            this.currentActivity = myactivity
            this.currentPosition = pos
//
//            if (Util.ACT_DESCRIPTION ==  itemView.btnActivityDesc.text.toString()){
//                itemView.btnActivityDesc.setBackgroundColor(Color.parseColor("#64B5F6"))
//            }
//            else{
//                itemView.btnActivityDesc.setBackgroundResource(android.R.drawable.btn_default);
//            }

        }
    }
}