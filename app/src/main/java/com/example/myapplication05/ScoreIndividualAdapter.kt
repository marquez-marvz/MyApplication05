package com.example.myapplication05


import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.individusl_student.view.*
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual.view.txtSection
import kotlinx.android.synthetic.main.score_individual.view.txtStudentNo
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.btnSubmissionStatus
import kotlinx.android.synthetic.main.score_individual_row.view.rowBtnEdit
import kotlinx.android.synthetic.main.score_individual_row.view.rowBtnSave
import kotlinx.android.synthetic.main.score_individual_row.view.rowtxtScore
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.search_row.view.*
import java.io.File
import java.io.FileOutputStream

public var bar: View? = null;

class ScoreIndividualAdapter(val context: Context, val individualScore: List<ScoreIndividualModel>) :
    RecyclerView.Adapter<ScoreIndividualAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView =
            LayoutInflater.from(context).inflate(R.layout.score_individual_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return individualScore.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myIndividualScore = individualScore[position]
        holder.setData(myIndividualScore, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),  PopupMenu.OnMenuItemClickListener  {

        var currentIndividualScore: ScoreIndividualModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnIndividualAdjusted.setOnClickListener {
                var num = itemView.btnIndividualAdjusted.text.toString().toInt()
                var score = currentIndividualScore!!.score
                Log.e("xxx", currentIndividualScore!!.item.toString() + "  " + (score + num))
                Log.e("II22", "Hello22")
                if (score + num <= currentIndividualScore!!.item) {
                    num = num + 1
                    itemView.btnIndividualAdjusted.setText(num.toString())
                    SaveDataIndividual()
                    Log.e("II22", "Hello23") //AdjustedScore(num,  dlgStudent)
                }

            }

            itemView.btnIndividualAdjusted.setOnLongClickListener {
                var num = itemView.btnIndividualAdjusted.text.toString().toInt()
                itemView.btnIndividualAdjusted.setText("0")
                SaveDataIndividual()
                true
            }




            itemView.btnRemark.setOnLongClickListener {
                val db: DatabaseHandler = DatabaseHandler(context)
                db.UpdateStudentRemark(currentIndividualScore!!.sectioncode, currentIndividualScore!!.activitycode, currentIndividualScore!!.studentNo, "-")
                individualScore[currentPosition].remark = "-"
                itemView.btnRemark.text = "-"
                true
            }


            //0622
            itemView.btnRemark.setOnClickListener {
                showPopRemarkScore(itemView, currentIndividualScore!!.sectioncode)
//                val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
//                val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
//                val inputDialog = mBuilder.show()
//                inputDialog.setCanceledOnTouchOutside(false);
//                RemarkMain.btnRemark = itemView.btnRemark
//                RemarkMain.alert = inputDialog
//                RemarkMain.remark_section = currentIndividualScore!!.sectioncode
//                var ppp: RemarkMain = RemarkMain()
//                ppp.SetButtonArray(dlgremark)
//                ppp.DisplayRenark(dlgremark, context, currentIndividualScore!!.studentNo, currentIndividualScore!!.activitycode)
//
//                for (i in 0..9) {
//                    RemarkMain.buttons[i].setOnClickListener {
//                        val rem = RemarkMain.buttons[i].text.toString()
//                        itemView.btnRemark.text = rem
//                        inputDialog.dismiss() //UpdateStudentRemark()
//                        val db: DatabaseHandler = DatabaseHandler(context)
//                        db.UpdateStudentRemark(currentIndividualScore!!.sectioncode, currentIndividualScore!!.activitycode, currentIndividualScore!!.studentNo, rem)
//                       //ScoreIndividualMain.individualList[currentPosition].remark = rem
//                    }
//                }
            }


            itemView.rowBtnEdit.setOnClickListener {
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                itemView.rowBtnSave.setVisibility(View.VISIBLE);
                itemView.rowtxtScore.isEnabled = true
            }

            itemView.rowtxtScore.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    Log.e("@@11", "hhhh")
                    if (itemView.rowtxtScore.text.toString() == "0") itemView.rowtxtScore.setText("")
                } else if (!hasFocus) {
                    Log.e("444", "444")
                    SaveDataIndividual()
                }
            })

            //            itemView.rowtxtAdjusted.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            //                if (hasFocus) {
            //                    Log.e("@@22", "hhhh")
            //                    if (itemView.rowtxtAdjusted.text.toString() == "0") itemView.rowtxtAdjusted.setText("")
            //                }
            //            })


            itemView.btnSubmissionStatus.setOnClickListener {
                var txt = itemView.btnSubmissionStatus.text
                if (txt == "NO") {
                    itemView.btnSubmissionStatus.text = "YES"
                    itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#64B5F6"))

                } else if (txt == "YES") {
                    itemView.btnSubmissionStatus.text = "LATE"
                    itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
                } else if (txt == "LATE") {
                    itemView.btnSubmissionStatus.text = "DR"
                    itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#FFFF00"))

                } else if (txt == "DR") {
                    itemView.btnSubmissionStatus.text = "NO"
                    itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#D3D3D3"))
                }
                val e = currentIndividualScore
                val db: TableActivity = TableActivity(context)
                var submissionStatus = itemView.btnSubmissionStatus.text.toString()
                db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNo, e.score, e.remark, submissionStatus)
            }


            itemView.rowBtnSave.setOnClickListener {
                Log.e("qqq", "Hello123")
                SaveDataIndividual()
            } //init


        } //imit

        fun setData(myatt: ScoreIndividualModel?, pos: Int) {
            val e = myatt
            val db: TableActivity = TableActivity(context)
            itemView.rowtxtActivity.text =
                e!!.activitycode + " (" + e!!.item + " )" // var gr = myGrades.GetStudentGrades(section, studentNumber, "FIRST")


            itemView.btnRecitation.setVisibility(View.INVISIBLE);
            itemView.btnTask.setVisibility(View.INVISIBLE);
            itemView.rowtxtDate.setVisibility(View.INVISIBLE);

            itemView.rowtxtScore.setText(e!!.score.toString())

            if (e!!.score == 0) itemView.rowtxtScore.setTextColor(Color.BLUE);
            else itemView.rowtxtScore.setTextColor(Color.BLACK);


            itemView.btnIndividualAdjusted.setText(e!!.adjustedScore.toString())
            itemView.rowtxtDescription.setText(e!!.description)
            itemView.btnSubmissionStatus.setText(e!!.submissionStatus)
            itemView.btnCategory.setText(e!!.category)
            itemView.btnRemark.setText(e!!.remark)

            //itemView.rowtxtItem.text = "ITEM-" + e.item

            //            if (e!!.category=="Quiz")
            //              itemView.rowtxtDescription.setTextColor(Color.MAGENTA );
            //           else if (e!!.category=="PT")
            //                itemView.rowtxtDescription.setTextColor(Color.BLUE);
            //            else
            //                itemView.rowtxtDescription.setTextColor(Color.BLACK);


            if (e!!.submissionStatus == "NO") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#D3D3D3"))
            else if (e!!.submissionStatus == "YES") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#64B5F6"))
            else if (e!!.submissionStatus == "LATE") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
            else if (e!!.submissionStatus == "DR") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#FFFF00"))



            if (e!!.status == "CLOSED") {
                itemView.rowtxtScore.isEnabled = false
                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                itemView.rowBtnSave.setVisibility(View.INVISIBLE);

            } else {
                itemView.rowtxtScore.isEnabled =
                    true // itemView.btnIndividualAdjusted.isEnabled = true
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                itemView.rowBtnSave.setVisibility(View.VISIBLE);
            }





            this.currentIndividualScore = myatt
            this.currentPosition = pos
        }

        //0622
        fun showPopRemarkScore(v: View, section: String) {
            val popup = PopupMenu(context, itemView.btnRemark, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_remark)
            val db2: DatabaseHandler = DatabaseHandler(context)
            var grp = db2.GetRemark(section)
            var x = 0;
            popup.getMenu().add("ADD REMARK");
            while (x < grp.size) {
                popup.getMenu().add(grp[x]);
                x++
            }
            popup.show()
        }


        override fun onMenuItemClick(item: MenuItem): Boolean {
                val selected = item.toString() // //0621t

                if (selected == "ADD REMARK") {
                    val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
                    val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
                    val inputDialog = mBuilder.show()
                    inputDialog.setCanceledOnTouchOutside(false); //
                    RemarkMain.alert = inputDialog
                    RemarkMain.remark_section = currentIndividualScore!!.sectioncode
                    var ppp: RemarkMain = RemarkMain() //    ppp.SetButtonArray(dlgremark)
                    ppp.DisplayRenark(dlgremark, context, currentIndividualScore!!.studentNo, currentIndividualScore!!.activitycode)

                } else {
                    itemView.btnRemark.setText(selected)
                    val db: DatabaseHandler = DatabaseHandler(context)
                    db.UpdateStudentRemark(currentIndividualScore!!.sectioncode, currentIndividualScore!!.activitycode, currentIndividualScore!!.studentNo, selected)

                }


            return true
        }


        fun SaveDataIndividual() {
            Log.e("II22", "Hello24")
            val db: TableActivity = TableActivity(context)
            if (itemView.rowtxtScore.text.toString() == "") return;
            if (itemView.btnIndividualAdjusted.text.toString() == "") return;

            var score = itemView.rowtxtScore.text.toString().toInt()
            var adjusted = itemView.btnIndividualAdjusted.text.toString().toInt()
            Log.e("ADJUSTED", itemView.btnIndividualAdjusted.text.toString())
            Log.e("SCORE", score.toString())
            val e = currentIndividualScore //  Util.Msgbox(context, "Hello" + score)
            var oldscore = e!!.score
            var stat = ""
            if (score > 0) stat = "YES"
            else stat = "NO"

            if (score.toInt() + adjusted > e!!.item) {
                Util.Msgbox(context, "The Score is invalid")
            } else {
//                try {
                    Log.e("II22", "Hello25")
                    itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                    itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                    Log.e("qqq", "Hello127")

                    itemView.rowtxtScore.isEnabled = false
                    Log.e("qqq", "Hello128")
                    individualScore[currentPosition].status = "CLOSED"
                    individualScore[currentPosition].score = score.toInt()
                    individualScore[currentPosition].adjustedScore = adjusted.toInt()



                    db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNo, score, e.remark, stat, adjusted)
                    Log.e("II22", "Hello27")
                    val db2: Grades = Grades(context)
                    val gradingPeriod = Util.GetCurrentGradingPeriod(context)
                    db2.GradeComputation(e!!.sectioncode, e.studentNo, gradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")

//
//                    if (db.GetAverageStatus() == "TRUE") {
//                        val section = e!!.sectioncode
//                        val studentNumber = e!!.studentNo
//
//                        val first =
//                            db2.GetStudentTermGrade(section, studentNumber, "FIRST", "ADJUSTED")
//                                .toString()
//                        val second =
//                            db2.GetStudentTermGrade(section, studentNumber, "SECOND", "ADJUSTED")
//                                .toString()
//                        val cumulative =
//                            db2.GetStudentTermGrade(section, studentNumber, "CUMULATIVE", "-")
//                                .toString()
//                        IndividualStudenht.varTxtGrade!!.setText("GR: " + first)
//                        IndividualStudenht.varTxtOriginalGrade!!.setText("ORIG:  " + second)
//                        IndividualStudenht.varTxtAdjustedGrade!!.setText("ADJ:  " + cumulative)
//                    } else {
//
//                        var grades =
//                            db2.GetStudentTermGrade(e!!.sectioncode, e!!.studentNo, gradingPeriod, "DECIMAL")
//                                .toString()
//                        var originalGrades =
//                            db2.GetStudentTermGrade(e!!.sectioncode, e!!.studentNo, gradingPeriod, "ORIGINAL")
//                                .toString()
//                        var adjustedGrades =
//                            db2.GetStudentTermGrade(e!!.sectioncode, e!!.studentNo, gradingPeriod, "ADJUSTED")
//                                .toString()
//
//                        Log.e("3333", "Hello27")
//
//                        IndividualStudenht.varTxtGrade!!.setText("GR: " + grades)
//                        IndividualStudenht.varTxtOriginalGrade!!.setText("ORIG:  " + originalGrades)
//                        IndividualStudenht.varTxtAdjustedGrade!!.setText("ADJ:  " + adjustedGrades)
//
//                    }
                IndividualStudenht.ShowGrades(context)
            }


        }

        //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
        //        TODO("Not yet implemented")
        //    }
    }


}