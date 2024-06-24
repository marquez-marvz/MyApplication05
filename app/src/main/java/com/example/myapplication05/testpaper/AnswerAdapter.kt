//package com.example.myapplication05.testpaper
//
//import com.example.myapplication05.*
//
//class AnswerAdapter {}

package com.example.myapplication05

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.testpaper.TestAnswer
import kotlinx.android.synthetic.main.answer_row.view.*
import kotlinx.android.synthetic.main.answer_row.view.btnEdit
import kotlinx.android.synthetic.main.answer_row.view.btnSave
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.paperpic_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.test_answer.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class AnswerAdapter(val context: Context, val picture: List<AnswerNewModel>) :
    RecyclerView.Adapter<AnswerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.answer_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return picture.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = picture[position]
        holder.setData(mySummary, position) //        if ( ( previousSelectedButton == null ) || ( previousSelectedButton == this.aButton ) ) {
        //            this.aButton!!.setBackgroundColor(Color.parseColor("#64B5F6"))
        //        } else {
        //
        //            previousSelectedButton!!.setBackgroundResource(android.R.drawable.btn_default);
        //            this.aButton!!.setBackgroundColor(Color.parseColor("#64B5F6"))
        //
        //            // !!.setBackgroundColor( ContextCompat.getColor( context, R.color.grey) )
        //            //this.aButton!!.setBackgroundColor( ContextCompat.getColor( context, R.color.orange) )
        //        }
        //

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                                               PopupMenu.OnMenuItemClickListener {

        var currentAnswer: AnswerNewModel? = null
        var currentPosition: Int = 0

        init {


            itemView.btnEdit.setOnClickListener {
                itemView.btnEdit.setVisibility(View.INVISIBLE);
                itemView.btnSave.setVisibility(View.VISIBLE);
                itemView.txtAnswer.isEnabled = true
                if (itemView.txtAnswer.text.toString() == "-") itemView.txtAnswer.setText("")
                itemView.txtAnswer.requestFocus()

            }

            itemView.setOnLongClickListener {
                Util.Msgbox(context, "Hwo")
                true
            }

            itemView.btnEdit.setOnLongClickListener {
                itemView.txtAnswer.setText("-")
                true
            }


            itemView.txtAnswer.setOnClickListener {
                val sss = itemView.txtAnswer.text.toString()
                Util.CopyText(context, sss)
                Log.e("SSS", sss)

            }

            itemView.btnSave.setOnClickListener {
                itemView.btnEdit.setVisibility(View.VISIBLE);
                itemView.btnSave.setVisibility(View.INVISIBLE);

                itemView.txtAnswer.isEnabled = false
                var ans = itemView.txtAnswer.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context);
                db.UpdateQuizAnswer(ans, currentAnswer!!.Points.toInt(), currentAnswer!!.Number, currentAnswer!!.AnswerID)
                picture[currentPosition].Answer = ans
            }

            //            itemView.btnPart.setOnLongClickListener {
            //
            //                true
            //
            //            }

            itemView.btnMenu.setOnClickListener {
                showPopMenu(itemView)
            }


            itemView.btnPoint.setOnClickListener {
                var pp = itemView.btnPoint.text.toString().toInt()


                pp++
                itemView.btnPoint.setText(pp.toString())
                var ans = itemView.txtAnswer.text.toString()
                var num = itemView.btnAnswerNumber.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context);
                db.UpdateQuizAnswer(ans, pp, num, currentAnswer!!.AnswerID)
                picture[currentPosition].Points = pp.toString()
            }


            itemView.btnPoint.setOnLongClickListener {
                itemView.btnPoint.setText("1")
                var ans = itemView.txtAnswer.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context);
                db.UpdateQuizAnswer(ans, 1, currentAnswer!!.Number, currentAnswer!!.AnswerID)
                picture[currentPosition].Points = "1"
                true
            }

            itemView.btnAnswerNumber.setOnLongClickListener {

                var num = 0
                Log.e("SSS", currentPosition.toString())
                if (currentPosition == 0) {
                    num = 1
                } else if (picture[currentPosition - 1].Number == "-") {
                    num = 1
                } else {
                    num = picture[currentPosition - 1].Number.toInt() + 1
                }
                itemView.btnAnswerNumber.setText(num.toString())
                var ans = itemView.txtAnswer.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context);
                db.UpdateQuizAnswer(ans, 1, num.toString(), currentAnswer!!.AnswerID)
                picture[currentPosition].Number = num.toString()


                true
            }

            itemView.btnAnswerNumber.setOnClickListener {

                var b = TestAnswer.mode == "ANSWER MODE"
                if (TestAnswer.mode == "ANSWER MODE") { //itemView.txtAnswer.isVisible = false
                    val params: ViewGroup.LayoutParams =
                        TestAnswer!!.viewanswer!!.listAnswer.getLayoutParams()
                    params.width = 180
                    TestAnswer!!.viewanswer!!.listAnswer.setLayoutParams(params)
                    TestAnswer.mode = "ANSWER MODE"
                    Log.e("MODE", "222")
                    TestAnswer!!.viewanswer!!.pdfview.isVisible = true
                } else { //   itemView.txtAnswer.isVisible = true
                    val params: ViewGroup.LayoutParams =
                        TestAnswer!!.viewanswer!!.listAnswer.getLayoutParams()
                    params.width = 1500
                    TestAnswer!!.viewanswer!!.listAnswer.setLayoutParams(params)
                    TestAnswer.mode = "ANSWER MODE"
                    Log.e("MODE", "222")
                    TestAnswer!!.viewanswer!!.pdfview.isVisible = false


                }
            }

        }

        //  dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default);
        fun setData(myatt: AnswerNewModel?, pos: Int) { // itemView.btnLastName.setText(myatt!!.lastname.toString())
            //itemView.btnLastName.setBackgroundResource(android.R.drawable.btn_default);
            itemView.txtAnswerQuizCode.setText(myatt!!.AnswerID)
            itemView.btnAnswerNumber.setText(myatt!!.Number)
            itemView.txtAnswer.setText(myatt!!.Answer)
            itemView.btnPoint.setText(myatt!!.Points)

            this.currentAnswer = myatt;
            this.currentPosition = pos
            itemView.txtAnswer.isEnabled = false

            if (myatt!!.Answer.contains("PART")) {
                itemView.btnAnswerNumber.isVisible = false
                itemView.btnPoint.isVisible = false
            } else {
                itemView.btnAnswerNumber.isVisible = true
                itemView.btnPoint.isVisible = true
            }
        }


        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun showPopMenu(v: View) {
            val popup = PopupMenu(context, v, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_answer)
            popup.show()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            val selected = item.toString() //

            if (selected == "Make Part") {
                itemView.txtAnswer.setText("PART XX")
                val db: DatabaseHandler = DatabaseHandler(context);
                db.UpdateQuizAnswer("PART XX", 0, "-", currentAnswer!!.AnswerID)
                picture[currentPosition].Answer = "PART XX"
                itemView.btnPoint.isVisible = false
                itemView.btnAnswerNumber.isVisible = false
                picture[currentPosition].Number = "-"
            }

            if (selected == "Move Up Answer") {
                var ans = currentAnswer!!.Answer
                var ansID = currentAnswer!!.AnswerID
                var position = currentPosition
                val db: DatabaseHandler = DatabaseHandler(context);
                val e = picture[position - 1]
                db.UpdateQuizAnswer(e!!.Answer, e!!.Points.toInt(), currentAnswer!!.Number, currentAnswer!!.AnswerID)
                db.UpdateQuizAnswer(currentAnswer!!.Answer, currentAnswer!!.Points.toInt(), e!!.Number, e!!.AnswerID)
                TestAnswer.UpdateListContent(currentAnswer!!.QuizCode, currentAnswer!!.Subject, context, currentAnswer!!.QuizSet)
                notifyDataSetChanged()

            }

            if (selected == "Move Down Answer") {
                var ans = currentAnswer!!.Answer
                var ansID = currentAnswer!!.AnswerID
                var position = currentPosition
                val db: DatabaseHandler = DatabaseHandler(context);
                val e = picture[position + 1]
                db.UpdateQuizAnswer(e!!.Answer, e!!.Points.toInt(), currentAnswer!!.Number, currentAnswer!!.AnswerID)
                db.UpdateQuizAnswer(currentAnswer!!.Answer, currentAnswer!!.Points.toInt(), e!!.Number, e!!.AnswerID)
                TestAnswer.UpdateListContent(currentAnswer!!.QuizCode, currentAnswer!!.Subject, context, currentAnswer!!.QuizSet)
                notifyDataSetChanged()
            }
            return true
        }
    }

}


fun DatabaseHandler.UpdateQuizAnswer(answer: String, point: Int, number: String, answerID: String) {
    var sql = """
        update tbAnswer 
        set answer ='$answer'
         ,  number ='$number'
        , Points ='$point'
        where AnswerID ='$answerID'
        """.trimIndent()
    val db = this.writableDatabase
    db.execSQL(sql)
    Log.e("SQLSS", sql)

}





