package com.example.myapplication05

//class OpenPdf_Adapter {}




import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.openpdf.view.*
import kotlinx.android.synthetic.main.pdf_row.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*


class OpenPdf_Adapter(val context: Context, val taskNewScore: List<TaskScoreModel>) :
    RecyclerView.Adapter<OpenPdf_Adapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.pdf_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return taskNewScore.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = taskNewScore[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentActivity: TaskScoreModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnScore.setOnClickListener {
                 var itempoint = currentActivity!!.Points
                var currentPoint = 0
               itemView.btnScore.setBackgroundColor(Color.parseColor("#64B5F6"))
                if (itemView.btnScore.text != "-")
                   currentPoint= itemView.btnScore.text.toString().toInt()


                if (itemView.btnScore.text == "-")
                     itemView.btnScore.text ="0"
                else if (itemView.btnScore.text == "0") {
                    if (currentPoint < itempoint)
                           itemView.btnScore.text = "1"
                    else
                          itemView.btnScore.text = "-"
                }
                else if (itemView.btnScore.text == "1") {
                    if (currentPoint < itempoint)
                        itemView.btnScore.text = "2"
                    else
                        itemView.btnScore.text = "-"
                }
                else if (itemView.btnScore.text == "2") {
                    if (currentPoint < itempoint)
                        itemView.btnScore.text = "3"
                    else
                        itemView.btnScore.text = "-"
                }
                else if (itemView.btnScore.text == "3") {
                    if (currentPoint < itempoint)
                        itemView.btnScore.text = "4"
                    else
                        itemView.btnScore.text = "-"
                }
                else if (itemView.btnScore.text == "4") {
                    if (currentPoint < itempoint)
                        itemView.btnScore.text = "5"
                    else
                        itemView.btnScore.text = "-"
                }
                else if (itemView.btnScore.text == "5") {
                        itemView.btnScore.text = "-"

                }



                val db: TableActivity = TableActivity(context)
                val e = currentActivity
                var score = 0;
                if (itemView.btnScore.text == "-") {
                    itemView.btnScore.setBackgroundResource(android.R.drawable.btn_default);
                    score = 0
                } else {
                    score = itemView.btnScore.text.toString().toInt()
                    itemView.btnScore.setBackgroundColor(Color.parseColor("#64B5F6"))
                }
              db.SetTaskScore(e!!.TaskCode,  e!!.StudentNumber,  e.ItemCode,score )

                OpenNewPDF.myTxtAnswer!!.text = e.Answer
                OpenNewPDF.myTxtScore!!.text = db.GetTotalTaskScore(e.TaskCode,e.StudentNumber).toString()

            }
        } //init

        fun setData(taskScore: TaskScoreModel?, pos: Int) {
            val db: TableAttendance = TableAttendance(context)
            itemView.txtItemCode.text = taskScore!!.ItemCode
            itemView.txtAnswer.text = taskScore!!.Answer
            if (taskScore!!.Score == -1) {
                itemView.btnScore.setText("-")
                itemView.btnScore.setBackgroundResource(android.R.drawable.btn_default);

            }
            else {
                itemView.btnScore.setBackgroundColor(Color.parseColor("#64B5F6"))
                itemView.btnScore.setText(taskScore!!.Score.toString())
            }
//            itemView.rowDescription.text = myactivity.description
//            itemView.rowItem.setText(myactivity.item.toString())
//            var  countYes= db.GetYesSubmissionCount(myactivity!!.sectionCode  ,  myactivity!!.activityCode)
//            itemView.rowActivityCount.setText(countYes)
//            //itemView.rowBtnStatus.text = myactivity.gradingPeriod
//            itemView.rowActivityCategory.text = myactivity.category
       this.currentActivity = taskScore
            this.currentPosition = pos
        }


    }
}