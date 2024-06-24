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
import kotlinx.android.synthetic.main.answer_main.*
import kotlinx.android.synthetic.main.answer_row.view.*
import kotlinx.android.synthetic.main.answer_row.view.btnEdit
import kotlinx.android.synthetic.main.answer_row.view.btnSave
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.paperpic_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class Answer_Main_Adapter(val context: Context, val picture: List<AnswerNewModel>) :
    RecyclerView.Adapter<Answer_Main_Adapter.MyViewHolder>(){

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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , PopupMenu.OnMenuItemClickListener  {

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
                Log.e("MODE", Answer_Main.mode)
                var b = Answer_Main.mode == "ANSWER MODE"
                Log.e("MODE", b.toString())
                if (Answer_Main.mode == "ANSWER MODE") { //itemView.txtAnswer.isVisible = false
                    val params: ViewGroup.LayoutParams =
                        Answer_Main.varlistAnswer!!.getLayoutParams()
                    params.width = 180
                    Answer_Main.varlistAnswer!!.setLayoutParams(params)
                    Answer_Main.mode = "PDF MODE"
                    Log.e("MODE", "111")
                    Answer_Main.varpdfView!!.isVisible =
                        true //                    var pp = itemView.btnAnswerNumber.text.toString()
                    //                    if (pp == "-") {
                    //                        pp = "1"
                    //                    } else {
                    //                        pp = (pp.toInt() + 1).toString()
                    //                    }
                    //                    itemView.btnAnswerNumber.setText(pp.toString())
                    //                    var ans = itemView.txtAnswer.text.toString()
                    //                    var point = itemView.btnPoint.text.toString()
                    //                    val db: DatabaseHandler = DatabaseHandler(context);
                    //                    db.UpdateQuizAnswer(ans, point.toInt(), pp, currentAnswer!!.AnswerID)
                    //                    picture[currentPosition].Number = pp.toString()
                } else { //   itemView.txtAnswer.isVisible = true
                    val params: ViewGroup.LayoutParams =
                        Answer_Main.varlistAnswer!!.getLayoutParams()
                    params.width = 1500
                    Answer_Main.varlistAnswer!!.setLayoutParams(params)
                    Answer_Main.mode = "ANSWER MODE"
                    Log.e("MODE", "222")
                    Answer_Main.varpdfView!!.isVisible = false

                    //                    Answer_Main.varbtnAnswerMainNumber!!.setText(currentAnswer!!.Number)
                    //                    Answer_Main.vartxtAnswerMain!!.setText(currentAnswer!!.Answer)
                    //                    Answer_Main.varbtnAnswerPoint!!.setText(currentAnswer!!.Points)
                    //                    Answer_Main.vartxtAnswerID!!.setText(currentAnswer!!.AnswerID)
                    //                    Answer_Main.varbtnAnswerMainNumber!!.isEnabled = false
                    //                    Answer_Main.vartxtAnswerMain!!.isEnabled = false
                    //

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
            } //            if (Answer_Main.mode == "PDF MODE") {
            //                itemView.txtAnswer.isVisible = false
            //            } else {
            //                itemView.txtAnswer.isVisible = true
            //            }
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
                var position  = currentPosition
                val db: DatabaseHandler = DatabaseHandler(context);
                val e = picture[position-1]
                db.UpdateQuizAnswer(e!!.Answer, e!!.Points.toInt(), currentAnswer!!.Number, currentAnswer!!.AnswerID)
                db.UpdateQuizAnswer(currentAnswer!!.Answer, currentAnswer!!.Points.toInt(), e!!.Number, e!!.AnswerID)
                Answer_Main.UpdateListContent(currentAnswer!!.QuizCode, currentAnswer!!.Subject, context, currentAnswer!!.QuizSet)
                notifyDataSetChanged()

            }

            if (selected == "Move Down Answer") {
                var ans = currentAnswer!!.Answer
                var ansID = currentAnswer!!.AnswerID
                var position  = currentPosition
                val db: DatabaseHandler = DatabaseHandler(context);
                val e = picture[position+1]
                db.UpdateQuizAnswer(e!!.Answer, e!!.Points.toInt(), currentAnswer!!.Number, currentAnswer!!.AnswerID)
                db.UpdateQuizAnswer(currentAnswer!!.Answer, currentAnswer!!.Points.toInt(), e!!.Number, e!!.AnswerID)
                Answer_Main.UpdateListContent(currentAnswer!!.QuizCode, currentAnswer!!.Subject, context, currentAnswer!!.QuizSet)
                notifyDataSetChanged()
            }
            return true
        }
    }

}

