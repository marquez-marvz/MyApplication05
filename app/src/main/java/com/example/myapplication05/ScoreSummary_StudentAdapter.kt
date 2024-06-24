//package com.example.myapplication05
//
//class ScoreSummary_StudentAdapter {}


//package com.example.myapplication05
//
//class ScoreSummary_ActivityAdapter {}


package com.example.myapplication05


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.pdf_row.view.btnScore
import kotlinx.android.synthetic.main.score_csv.view.btnCsvRemark
import kotlinx.android.synthetic.main.score_csv.view.txtCsvName
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score1
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score10
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score2
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score3
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score4
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score5
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score6
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score7
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score8
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score9
import kotlinx.android.synthetic.main.score_summary_studentrow.view.txtSummaryFirst
import kotlinx.android.synthetic.main.score_summary_studentrow.view.txtSummaryLast
import kotlinx.android.synthetic.main.scoresummary_activityrow.view.btnActivityDesc
import kotlinx.android.synthetic.main.student_import.view.*
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_row.view.*

import kotlinx.android.synthetic.main.util_confirm.view.*
import java.util.ArrayList


class ScoreSummary_StudentAdapter(val context: Context, val student: List<EnrolleModel>) :
    RecyclerView.Adapter<ScoreSummary_StudentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView =
            LayoutInflater.from(context).inflate(R.layout.score_summary_studentrow, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return student.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = student[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentStudenht: EnrolleModel? = null
        var currentPosition: Int = 0

        init {

            itemView.rowBtnScoreIndividual.setOnClickListener { //                    val intent = Intent(this,  IndividualStudenht::class.java)
                val intent = Intent(context, IndividualStudenht::class.java)
                Util.GRADE_INDIVIDUAL = "GRADES"
                Util.GRADE_STUD_NO = currentStudenht!!.studentno
                Util.GRADE_NAME = currentStudenht!!.lastname + "," + currentStudenht!!.firstname
                Util.GRADE_SECTION = currentStudenht!!.Section
                Log.e("2294", "'")
                context.startActivity(intent)
            }
        } //init

        fun setData(myactivity: EnrolleModel?, pos: Int) {
            itemView.txtSummaryLast.text = myactivity!!.lastname
            itemView.txtSummaryFirst.text = myactivity!!.firstname

            val db: DatabaseHandler = DatabaseHandler(context)

            val dbglobal: TableActivity = TableActivity(context)

            var section = db.GetCurrentSection();
            if (ScoreSummary.VIEWMODE == "SCORE"){
                ViewScore(myactivity)
            }
           else if (ScoreSummary.VIEWMODE == "REMARK"){
                ViewRemark(myactivity)
            }

            //ViewRemark(myactivity)
            //            var currentTerm = Util.GetCurrentGradingPeriod(context)
            //            var absentCount =
            //                db.GetTermCountAttendance(section, myactivity!!.studentno, currentTerm, "A")
            //            var lateCount =
            //                db.GetTermCountAttendance(section, myactivity!!.studentno, currentTerm, "L") //            var lateCount =
            //
            //            itemView.Score9.text = absentCount.toString()
            //            itemView.Score10.text = lateCount.toString()
            //            itemView.Score9.isVisible = true
            //            itemView.Score10.isVisible = true
            //
            this.currentStudenht = myactivity
            this.currentPosition = pos
        }


        fun ViewScore(myactivity: EnrolleModel?) {
            val activity: List<ActivityModel>
            val db: DatabaseHandler = DatabaseHandler(context)
            activity =
                db.GetActivityList(myactivity!!.Section, Util.GetCurrentGradingPeriod(context))

            var x = 1;


            for (e in activity) {
                var score =
                    db.GetActivityScore(e.sectionCode, myactivity!!.studentno, e.activityCode)
                var remark =
                    db.GetActivityRemark(e.sectionCode, myactivity!!.studentno, e.activityCode)



                if (x == 1) {
                    itemView.Score1.text = score.toString()
                    itemView.Score1.isVisible = true
                    if (score == 0) {
                        itemView.Score1.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score1.setBackgroundColor(Color.parseColor("#64B5F6"))
                    }
                }

                if (x == 2) {
                    if (score == 0) {
                        itemView.Score2.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score2.setBackgroundColor(Color.parseColor("#FFB74D"))
                    }
                    itemView.Score2.text = score.toString()
                    itemView.Score2.isVisible = true
                }

                if (x == 3) {
                    if (score == 0) {
                        itemView.Score3.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score3.setBackgroundColor(Color.parseColor("#69F0AE"))
                    }
                    itemView.Score3.text = score.toString()
                    itemView.Score3.isVisible = true
                }




                if (x == 4) {
                    if (score == 0) {
                        itemView.Score4.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score4.setBackgroundColor(Color.parseColor("#eccbac"))
                    }
                    itemView.Score4.text = score.toString()
                    itemView.Score4.isVisible = true
                }

                if (x == 5) {
                    if (score == 0) {
                        itemView.Score5.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score5.setBackgroundColor(Color.parseColor("#ff7f50"))
                    }
                    itemView.Score5.text = score.toString()
                    itemView.Score5.isVisible = true
                }

                if (x == 6) {
                    if (score == 0) {
                        itemView.Score6.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score6.setBackgroundColor(Color.parseColor("#ee6aa7"))
                    }
                    itemView.Score6.text = score.toString()
                    itemView.Score6.isVisible = true
                }

                if (x == 7) {
                    if (score == 0) {
                        itemView.Score7.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score7.setBackgroundColor(Color.parseColor("#7fffd4"))
                    }
                    itemView.Score7.text = score.toString()
                    itemView.Score7.isVisible = true
                }

                if (x == 8) {
                    if (score == 0) {
                        itemView.Score8.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score8.setBackgroundColor(Color.parseColor("#B2BEB5"))
                    }
                    itemView.Score8.text = score.toString()
                    itemView.Score8.isVisible = true
                }

                if (x == 9) {
                    if (score == 0) {
                        itemView.Score9.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score9.setBackgroundColor(Color.parseColor("#fffbbf"))
                    }
                    itemView.Score9.text = score.toString()
                    itemView.Score9.isVisible = true
                }

                if (x == 10) {
                    if (score == 0) {
                        itemView.Score10.setBackgroundResource(android.R.drawable.btn_default);
                    } else {
                        itemView.Score10.setBackgroundColor(Color.parseColor("#7fffd4"))
                    }
                    itemView.Score10.text = score.toString()
                    itemView.Score10.isVisible = true
                }


                x++;


            }

        }

        fun ViewRemark(myactivity: EnrolleModel?) {
            val activity: List<ActivityModel>
            val db: DatabaseHandler = DatabaseHandler(context)
            activity =
                db.GetActivityList(myactivity!!.Section, Util.GetCurrentGradingPeriod(context))

            var x = 1;


            for (e in activity) {

                var remark =
                    db.GetActivityRemark(e.sectionCode, myactivity!!.studentno, e.activityCode)



                if (x == 1) {
                    itemView.Score1.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score1.text = remark.toString()
                        itemView.Score1.setBackgroundColor(Color.parseColor("#64B5F6"))
                    } else {
                        itemView.Score1.text ="'"
                        itemView.Score1.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 2) {
                    itemView.Score2.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score2.text =remark.toString()
                        itemView.Score2.setBackgroundColor(Color.parseColor("#FFB74D"))
                    } else {
                        itemView.Score2.text = ""
                        itemView.Score2.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 3) {
                    itemView.Score3.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score3.text =remark.toString()
                        itemView.Score3.setBackgroundColor(Color.parseColor("#69F0AE"))
                    } else {
                        itemView.Score3.text = ""
                        itemView.Score3.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 4) {
                    itemView.Score4.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score4.text =remark.toString()
                        itemView.Score4.setBackgroundColor(Color.parseColor("#eccbac"))
                    } else {
                        itemView.Score4.text = ""
                        itemView.Score4.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 5) {
                    itemView.Score5.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score5.text =remark.toString()
                        itemView.Score5.setBackgroundColor(Color.parseColor("#ff7f50"))
                    } else {
                        itemView.Score5.text = ""
                        itemView.Score5.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 6) {
                    itemView.Score6.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score6.text =remark.toString()
                        itemView.Score6.setBackgroundColor(Color.parseColor("#ee6aa7"))
                    } else {
                        itemView.Score6.text = ""
                        itemView.Score6.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }


                if (x == 7) {
                    itemView.Score7.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score7.text =remark.toString()
                        itemView.Score7.setBackgroundColor(Color.parseColor("#7fffd4"))
                    } else {
                        itemView.Score7.text = ""
                        itemView.Score7.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 8) {
                    itemView.Score8.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score8.text =remark.toString()
                        itemView.Score8.setBackgroundColor(Color.parseColor("#B2BEB5"))
                    } else {
                        itemView.Score8.text = ""
                        itemView.Score8.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }

                if (x == 9) {
                    itemView.Score9.isVisible = true
                    if (remark == "OK" || remark =="YES") {
                        itemView.Score9.text =remark.toString()
                        itemView.Score9.setBackgroundColor(Color.parseColor("#fffbbf"))
                    } else {
                        itemView.Score9.text = ""
                        itemView.Score9.setBackgroundResource(android.R.drawable.btn_default);

                    }
                }




                x++;




            }

        }
    }
}


fun DatabaseHandler.GetActivityScore(section: String, studentNumber: String, activityCode: String): Int {
    val db2 = this.readableDatabase
    var sql = """
            select *from  tbscore 
              where StudentNo='$studentNumber' 
            and ActivityCode ='$activityCode'
            and SectionCode  ='$section'
                    """


    var cursor: Cursor? = null
    cursor = db2.rawQuery(sql, null)


    if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex("Score")).toInt()
    else return 0;
}


fun DatabaseHandler.GetActivityRemark(section: String, studentNumber: String, activityCode: String): String {
    val db2 = this.readableDatabase
    var sql = """
            select *from  tbscore 
              where StudentNo='$studentNumber' 
            and ActivityCode ='$activityCode'
            and SectionCode  ='$section'
                    """


    var cursor: Cursor? = null
    cursor = db2.rawQuery(sql, null)


    if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex("Remark"))
    else return "-";
}



@SuppressLint("Range")
fun DatabaseHandler.GetActivityList(sectioncode: String, gradingPeriod: String = "", category: String = ""): ArrayList<ActivityModel> {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    val sql: String //ActivityCode	SectionCode	Description	Item	Status	Category	GradingPeriod	QuizID	RubricCode	KeyWord	DefaultActivity
    sql = """
                SELECT * FROM TBACTIVITY 
                WHERE SectionCode='$sectioncode'    
                AND GradingPeriod='$gradingPeriod' 
                ORDER BY ActivityCode
                """

    Log.e("PPP100", sql)
    Log.e("PPP100", gradingPeriod)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    try {
        cursor = db.rawQuery(sql, null)
    } catch (e: SQLiteException) {
        db.execSQL(sql)
        return ArrayList()
    }

    if (cursor.moveToFirst()) {
        do {
            var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
            var description = cursor.getString(cursor.getColumnIndex("Description"))
            var item = cursor.getString(cursor.getColumnIndex("Item")).toInt()
            var status = ""
            var category = cursor.getString(cursor.getColumnIndex("Category"))
            val att =
                ActivityModel(activityCode, sectionCode, description, item, status, category, "HIDE")
            activityList.add(att)
        } while (cursor.moveToNext())
    }
    return activityList
}


@SuppressLint("Range")
fun DatabaseHandler.GetTermCountAttendance(sectioncode: String, studNumber: String, term: String = "", status: String): Int {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    val sql: String //ActivityCode	SectionCode	Description	Item	Status	Category	GradingPeriod	QuizID	RubricCode	KeyWord	DefaultActivity
    sql = """
                SELECT * FROM TBATTENDANCE_QUERY
                WHERE SectionCode='$sectioncode'    
                AND StudentNumber='$studNumber' 
                AND SchedTerm='$term' 
                AND AttendanceStatus='$status' 
                """
    var cursor = SQLSelect(sql)
    return cursor!!.count
}



