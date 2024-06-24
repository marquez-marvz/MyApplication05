//package com.example.myapplication05
//
//class CheckAdapter {}

//package com.example.myapplication05
//
//class Answer_Main_Adapter {}


package com.example.myapplication05.testpaper

import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.*
import com.example.myapplication05.testpaper.TestCheck
import kotlinx.android.synthetic.main.chek_row.*
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*

import kotlinx.android.synthetic.main.chek_row.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*
import kotlinx.android.synthetic.main.test_check.view.*


class CheckAdapter(val context: Context, val picture: List<AnswerNewModel>) :

    RecyclerView.Adapter<CheckAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context);
    var flag = "SMALL-LIST"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.chek_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return picture.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val mySummary = picture[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentAnswer: AnswerNewModel? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener { //                TestCheck.viewcheck!!.txtLongAnswer.setText(currentAnswer!!.Answer)
            }
            itemView.btnPointCheck.setOnLongClickListener {
                itemView.btnPointCheck.setText("0") //                TestCheck.viewcheck!!.txtLongAnswer.setText(currentAnswer!!.Answer)
                var name = TestCheck.currentStudName
                var section = TestCheck.currentSection
                var quizcode = TestCheck.currentQuizCode


                if (name != "Select") {
                    var studNum = db.GetStudentNumber(name, section)
                    db.UpdatePoint(studNum, currentAnswer!!.AnswerID, 0)
                    itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#64B5F6"))
                    notifyItemChanged(currentPosition)
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var num = db2.SumofScoreQuiz(quizcode, studNum)
                    TestCheck.viewcheck!!.btnTotalScore.setText(num.toString())
                }
                true
            }

            itemView.txtCorrectAnswer.setOnClickListener {
                var num = itemView.btnPointCorrect.text.toString()
                itemView.btnPointCheck.setText(num)
                itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#f0c569"))
                var score = num.toInt()
                var name = TestCheck.currentStudName
                var section = TestCheck.currentSection
                if (name != "Select") {
                    var studNum = db.GetStudentNumber(name, section)
                    db.UpdatePoint(studNum, currentAnswer!!.AnswerID, score) // notifyItemChanged(currentPosition)
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var quizcode = TestCheck.currentQuizCode
                    var num = db2.SumofScoreQuiz(quizcode, studNum)
                    TestCheck.viewcheck!!.btnTotalScore.setText(num.toString())
                }
            }

            itemView.txtCorrectAnswer.setOnLongClickListener {
//                if (flag == "SMALL-LIST") {
//                    val params: ViewGroup.LayoutParams =
//                        TestCheck.viewcheck!!.listCheckAnswer.getLayoutParams()
//                    params.height = 2000
//                    TestCheck.viewcheck!!.listCheckAnswer.setLayoutParams(params)
//                    TestCheck.viewcheck!!.imgStudent.isVisible = false
//                    flag = "BIG-LIST"
//                } else {
//                    val params: ViewGroup.LayoutParams =
//                        TestCheck.viewcheck!!.listCheckAnswer.getLayoutParams()
//                    params.height = 700
//                    TestCheck.viewcheck!!.listCheckAnswer.setLayoutParams(params)
//                    TestCheck.viewcheck!!.imgStudent.isVisible = true
//                    flag = "SMALL-LIST"
//                }
                var num = "0"
                itemView.btnPointCheck.setText(num)
                itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#64B5F6"))
                var score = num.toInt()
                var name = TestCheck.currentStudName
                var section = TestCheck.currentSection
                if (name != "Select") {
                    var studNum = db.GetStudentNumber(name, section)
                    db.UpdatePoint(studNum, currentAnswer!!.AnswerID, score) // notifyItemChanged(currentPosition)
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var quizcode = TestCheck.currentQuizCode
                    var num = db2.SumofScoreQuiz(quizcode, studNum)
                    TestCheck.viewcheck!!.btnTotalScore.setText(num.toString())
                }
                            true
            }
            itemView.btnPointCorrect.setOnClickListener {
                var num = itemView.btnPointCorrect.text.toString()
                itemView.btnPointCheck.setText(num)
                itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#f0c569"))
                var score = num.toInt()
                var name = TestCheck.currentStudName
                var section = TestCheck.currentSection
                if (name != "Select") {
                    var studNum = db.GetStudentNumber(name, section)
                    db.UpdatePoint(studNum, currentAnswer!!.AnswerID, score) // notifyItemChanged(currentPosition)
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var quizcode = TestCheck.currentQuizCode
                    var num = db2.SumofScoreQuiz(quizcode, studNum)
                    TestCheck.viewcheck!!.btnTotalScore.setText(num.toString())
                }
            }

            itemView.btnPointCheck.setOnClickListener { //                TestCheck.viewcheck!!.txtLongAnswer.setText(currentAnswer!!.Answer)
                var num = itemView.btnPointCheck.text.toString()
                var score = 0;
                if (num == "-") {
                    itemView.btnPointCheck.setText("0")
                    score = 0
                    itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#64B5F6"))

                } else {

                    var b = num.toInt()
                    Log.e("bbnn", b.toString())
                    if (b < currentAnswer!!.Points.toInt()) {
                        score = b + 1
                        Log.e("bbb", score.toString())

                        itemView.btnPointCheck.setText(score.toString())
                        Log.e("SC", score.toString() + "" + currentAnswer!!.Points.toInt())
                        if (score == currentAnswer!!.Points.toInt()) {
                            itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#f0c569"))
                        } else {
                            itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#69F0AE"))
                        }
                    } else {
                        score = num.toInt()
                        itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#f0c569"))
                    }

                }
                var name = TestCheck.currentStudName
                var section = TestCheck.currentSection
                if (name != "Select") {
                    var studNum = db.GetStudentNumber(name, section)
                    db.UpdatePoint(studNum, currentAnswer!!.AnswerID, score) // notifyItemChanged(currentPosition)
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var quizcode = TestCheck.currentQuizCode
                    var num = db2.SumofScoreQuiz(quizcode, studNum)
                    TestCheck.viewcheck!!.btnTotalScore.setText(num.toString())
                }

            }
        }

        fun setData(myatt: AnswerNewModel?, pos: Int) {
            var name = TestCheck.viewcheck!!.cboStudentNane.getSelectedItem().toString();
            itemView.txtCorrectAnswer.setText(myatt!!.Number + "  " + myatt!!.Answer)
            itemView.btnPointCorrect.setText(myatt!!.Points)
            itemView.btnPointCheck.isVisible = false
            itemView.btnPart.isVisible = false

            if (name != "Select") {
                if (myatt!!.Answer.contains("PART")) {
                    itemView.btnPart.setText(myatt!!.Answer)
                    itemView.btnPart.isVisible = true
                    itemView.btnPointCheck.isVisible = false
                    itemView.txtCorrectAnswer.setText("")
                } else {

                    itemView.btnPointCheck.isVisible = true

                    var section = TestCheck.currentSection
                    var studNum = db.GetStudentNumber(name, section)
                    var point = db.GetStudentPoint(studNum, myatt!!.AnswerID)
                    itemView.btnPointCheck.setText(point)

                    if (point == "-") {
                        itemView.btnPointCheck.setBackgroundResource(android.R.drawable.btn_default)
                    } //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
                    else if (point.toInt() == 0) {
                        itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#64B5F6"))
                    } else if (point.toInt() == myatt!!.Points.toInt()) {

                        itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#f0c569"))
                    } else {
                        itemView.btnPointCheck.setBackgroundColor(Color.parseColor("#69F0AE"))
                    }
                }
            }





            this.currentAnswer = myatt;
            this.currentPosition = pos
        }

    }


}


fun DatabaseHandler.GetStudentPoint(studNum: String, answerID: String): String {
    var sql = """ SELECT StudentNumber,	QuizCode,	AnswerID,	Score FROM tbAnswer_Student 
              where StudentNumber='$studNum'
              and AnswerID='$answerID'
           """
    var cursor5 = SQLSelect(sql, 77)
    Log.e("BBB", sql + cursor5!!.columnNames.toString())
    if (cursor5!!.moveToFirst()) {
        var point = cursor5!!.getString(cursor5!!.getColumnIndex("Score")).toInt()
        if (point == -1) {
            return "-"
        } else {
            return point.toString()
        }
    } else {
        return "0"
    }
}


fun DatabaseHandler.UpdatePoint(studNum: String, answerID: String, point: Int) {
    var sql = """ UPDATE tbAnswer_Student
              set  Score='$point'
              where StudentNumber='$studNum'
              and AnswerID='$answerID'
           """
    val db = this.writableDatabase
    db.execSQL(sql)
}
