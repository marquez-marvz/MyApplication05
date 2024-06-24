//package com.example.myapplication05
//
//class CheckRecordAdapter {}


//package com.example.myapplication05
//
//class CheckAdapter {}

//package com.example.myapplication05
//
//class Answer_Main_Adapter {}


package com.example.myapplication05

import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R


import kotlinx.android.synthetic.main.answer_row.view.*

import kotlinx.android.synthetic.main.check_rrecord_row.view.*
import kotlinx.android.synthetic.main.check_rrecord_row.view.btnEdit
import kotlinx.android.synthetic.main.check_rrecord_row.view.btnSave
import kotlinx.android.synthetic.main.chek_row.view.*
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.test_check.view.*
import kotlinx.android.synthetic.main.test_score.view.*


class CheckRecordAdapter(val context: Context, val scoreList: List<ScoreModel>) :
    RecyclerView.Adapter<CheckRecordAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context);
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.check_rrecord_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return scoreList.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val mySummary = scoreList[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentAnswer: ScoreModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnEdit.setOnClickListener {
//                itemView.btnEdit.setVisibility(View.INVISIBLE);
//                itemView.btnSave.setVisibility(View.VISIBLE);
//                itemView.txtScore.isEnabled = true
//                if (itemView.txtScore.text.toString() == "0") itemView.txtScore.setText("")
//                itemView.txtScore.requestFocus()
//                var studName = (currentAnswer!!.lastnanme + "," + currentAnswer!!.firstname)
//                TestScore.viewscore!!.txtStudentName.setText(currentAnswer!!.lastnanme + "," + currentAnswer!!.firstname)
//                var keyword =  TestScore.viewscore!!.btnScoreQuizCode.text.toString()
//                TestCheck.ShowPicture(studName, TestScore.currentSectionScore, keyword  , TestScore.viewscore!!.imgStudentScore)
            }


            itemView.btnQuizScore.setOnClickListener() {
//                var activitycode =TestScore.currentActivityCode
//                val db2: DatabaseHandler = DatabaseHandler(context)
//                var quizScore =  itemView.btnQuizScore.text.toString().toInt()
//                var section = TestCheck.currentSection
//                var name = currentAnswer!!.lastnanme +  "," +  currentAnswer!!.firstname
//                var studNum = db2.GetStudentNumber(name, section)
//                var rem = "-"
//                if (quizScore > 0) {
//                    rem = "OK"
//                    itemView.btnRemark.setBackgroundColor(Color.parseColor("#69F0AE"))
//                    itemView.btnQuizScore.setBackgroundColor(Color.parseColor("#64B5F6"))
//                }
//                itemView.txtScore.setText(quizScore.toString())
//                db2.DriveUpdateStudentRecord(section, activitycode, studNum, quizScore)

            }

            itemView.btnRemark.setOnClickListener {
                val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
                val inputDialog = mBuilder.show()
                inputDialog.setCanceledOnTouchOutside(false); //
                RemarkMain.btnRemark = itemView.btnRemark
                RemarkMain.alert = inputDialog
                RemarkMain.remark_section = currentAnswer!!.sectioncode
                var ppp: RemarkMain = RemarkMain()
                ppp.SetButtonArray(dlgremark)
                ppp.DisplayRenark(dlgremark, context, currentAnswer!!.studentNo, currentAnswer!!.activitycode)

                for (i in 0..9) {
                    RemarkMain.buttons[i].setOnClickListener {
                        val rem = RemarkMain.buttons[i].text.toString()
                        itemView.btnRemark.text = rem
                        inputDialog.dismiss() //UpdateStudentRemark()
                        val db: DatabaseHandler = DatabaseHandler(context)
                        db.UpdateStudentRemark(RemarkMain.remark_section, currentAnswer!!.activitycode, currentAnswer!!.studentNo, rem)
                        scoreList[currentPosition].remark = rem
                    }
                }

            }
            itemView.btnSave.setOnClickListener {
                itemView.btnEdit.setVisibility(View.VISIBLE);
                itemView.btnSave.setVisibility(View.INVISIBLE);
                itemView.txtScore.isEnabled = false


                var activitycode = currentAnswer!!.activitycode
                val db2: DatabaseHandler = DatabaseHandler(context)
               var scoreText =  itemView.txtScore.text.toString()
                if (scoreText=="") {
                    scoreText = "0"
                }
                var score = scoreText.toInt()
                var section = currentAnswer!!.sectioncode
                var studNum = currentAnswer!!.studentNo
                var rem = "-"
                if (score > 0) {
                    rem = "OK"
                    itemView.btnRemark.setBackgroundColor(Color.parseColor("#69F0AE"))
                    itemView.btnRemark.setText("OK")
                } else {
                    itemView.btnRemark.setBackgroundResource(android.R.drawable.btn_default);
                }

                db2.DriveUpdateStudentRecord(section, activitycode, studNum, score)
                scoreList[currentPosition].remark = rem
                scoreList[currentPosition].score = score

            }
        }

        fun setData(myatt: ScoreModel?, pos: Int) {
            itemView.txtLastName.setText(myatt!!.lastnanme + "," + myatt.firstname.substring(0, 1))
            itemView.txtScore.setText(myatt!!.score.toString())
            itemView.btnRemark.setText(myatt!!.remark)
            this.currentAnswer = myatt;
            this.currentPosition = pos
            if (myatt!!.remark == "OK") {
                itemView.btnRemark.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else {
                itemView.btnRemark.setBackgroundResource(android.R.drawable.btn_default);
            }
            val db2: DatabaseHandler = DatabaseHandler(context)
            var quizCode = "" //TestScore.viewscore!!.btnScoreQuizCode.text.toString()
            var num = db2.SumofScoreQuiz(quizCode, myatt!!.studentNo)
            itemView.btnQuizScore.setText(num.toString())
            if (myatt!!.score == num) {
                itemView.btnQuizScore.setBackgroundColor(Color.parseColor("#64B5F6"))
            }
            else{
                itemView.btnQuizScore.setBackgroundResource(android.R.drawable.btn_default);
            }
//            Log.e("@@@", TestScore.viewscore!!.btnRemarkStatus.text.toString())
//            if (TestScore.viewscore!!.btnRemarkStatus.text.toString() == "HIDE REMARK") {
//                Log.e("@@@", "Hello")
//                itemView.btnRemark.isVisible = false
//            } else {
//                itemView.btnRemark.isVisible = true
//            }
        }

    }


}

