//package com.example.myapplication05

//class ScoreSummary_RemarkAdapter {}

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
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
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


class ScoreSummary_RemarkAdapter(val context: Context, val activity: List<ActivityModel>) :
    RecyclerView.Adapter<ScoreSummary_RemarkAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView =
            LayoutInflater.from(context).inflate(R.layout.scoresummary_activityrow, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return activity.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = activity[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var currentActivity: ActivityModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnActivityDesc.setOnClickListener {
                Util.ACT_CODE = currentActivity!!.activityCode
                Util.ACT_CURRENT_SECTION = currentActivity!!.sectionCode
                Util.ACT_DESCRIPTION = currentActivity!!.description
                ScoreMain.txtactivitycode!!.text  = currentActivity!!.activityCode
                ScoreMain.txtactivitydesc!!.text =currentActivity!!.description
                ScoreMain.ScoreUpdateListContent(context)
                ScoreMain.scoreAdapter!!.notifyDataSetChanged()
                notifyDataSetChanged()

            }

        } //init

        fun setData(myactivity: ActivityModel?, pos: Int) {
            itemView.btnActivityDesc.text = myactivity!!.description
            Log.e("POS", pos.toString())
            Log.e("POS", activity.size.toString())
            this.currentActivity = myactivity
            this.currentPosition = pos

            if (Util.ACT_DESCRIPTION ==  itemView.btnActivityDesc.text.toString()){
                itemView.btnActivityDesc.setBackgroundColor(Color.parseColor("#64B5F6"))
            }
            else{
                itemView.btnActivityDesc.setBackgroundResource(android.R.drawable.btn_default);
            }

        }
    }
}