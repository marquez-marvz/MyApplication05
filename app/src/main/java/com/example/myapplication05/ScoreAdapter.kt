package com.example.myapplication05


import android.content.*
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.rubric_student.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual.view.txtName
import kotlinx.android.synthetic.main.score_individual.view.txtStudentNo
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.score_row.view.rowBtnEdit
import kotlinx.android.synthetic.main.score_row.view.rowBtnSave
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.task_row.view.rowBtnMenu
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.lang.Integer.parseInt


class ScoreAdapter(val context: Context, val score: List<ScoreModel>) :
    RecyclerView.Adapter<ScoreAdapter.MyViewHolder>(), DialogInterface.OnCancelListener {
    var previousSelectedScoreButton: Button? = null
    var previousSelectedByFiveButton: Button? = null

    // var individualList: List<ScoreIndividualModel>
    var individualList = arrayListOf<ScoreIndividualModel>()
    var individualAdapter: ScoreIndividualAdapter? = null;
    var prev_position = -1

    var POPOP_MENU = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.score_row, parent, false)

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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
                                               PopupMenu.OnMenuItemClickListener {

        var currentScore: ScoreModel? = null
        var currentPosition: Int = 0

        init {

            //            itemView.setOnLongClickListener {
            //                DisplayInputRemark()
            //                true
            //            } //init


            //            itemView.rowBtnScoreIndividual.setOnClickListener {
            //                val dlgindividual =
            //                    LayoutInflater.from(context).inflate(R.layout.score_individual, null)
            //                val completeName = currentScore!!.completeName
            //                val mBuilder =
            //                    AlertDialog.Builder(context).setView(dlgindividual).setTitle("")
            //                val individualDialog = mBuilder.show()
            //                individualDialog.setCanceledOnTouchOutside(false);
            //                individualDialog.getWindow()!!
            //                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            //
            //
            //
            //
            //                ShowIndividualRecord(dlgindividual, currentScore!!, individualDialog )
            //                dlgindividual.btnNext.setOnClickListener {
            //                    currentPosition = currentPosition + 1
            //                    currentScore= score[currentPosition]
            //                    ShowIndividualRecord(dlgindividual, currentScore!!, individualDialog)
            //
            //                }
            //
            //                dlgindividual.btnEditAll.setOnClickListener {
            //                    Log.e("SSS", "123")
            //
            //                    for (e in individualList) {
            //                        e.status = "OPEN"
            //                    }
            //                    individualAdapter!!.notifyDataSetChanged()
            //                }
            //
            //
            //
            //
            //                dlgindividual.btnCopyText.setOnClickListener {
            //                    val db2: Grades = Grades(context)
            //                    val db: TableActivity = TableActivity(context)
            //                    var currrentGradingPeriod =db.GetDefaultGradingPeriod();
            //                    var individualList: List<ScoreIndividualModel>
            //                    val sectionCode =  currentScore!!.sectioncode
            //                    val studentNo =dlgindividual.txtStudentNo.text.toString()
            //                    Log.e("yyy", sectionCode)
            //                    Log.e("yyy", studentNo)
            //                    var s = completeName + "\n"
            //                    individualList=    db.GetIndividualActivity(sectionCode, studentNo)
            //                    for (e in individualList) {
            //                        Log.e("Helo", "ssss")
            //                        s = s  + e.description + "   " + e.submissionStatus + "\n"
            //                    }
            //                    val   str = db2.GetStudentTermGrade(sectionCode,  studentNo, currrentGradingPeriod, "DECIMAL").toString()
            //
            //                    if(currrentGradingPeriod=="FIRST")
            //                          s = s  + "Midterm Grade" + "   " + str  + "\n"
            //                    else if(currrentGradingPeriod=="SECOND")
            //                        s = s  + "Final Grade" + "   " + str  + "\n"
            //                    CopyText(s)
            //                    Log.e("yyy", s)
            //
            //                }
            //
            ////                dlgindividual.rowBtnSaveAdjust.setOnClickListener {
            ////                    Log.e("rowBtnSaveAdjust", "12234566")
            ////                    val db: TableActivity = TableActivity(context)
            ////                    var currrentGradingPeriod =db.GetDefaultGradingPeriod();
            ////                    val adjustedScore= dlgindividual.rowtxtScoreAdjust.getText().toString()
            ////                    val sectionCode =  currentScore!!.sectioncode
            ////                    val studentNo =dlgindividual.txtStudentNo.text.toString()
            ////                    val db2: Grades = Grades(context)
            ////                    db2.UpdateAdjustedScore(sectionCode, studentNo, currrentGradingPeriod, adjustedScore.toInt())
            ////                    val e = currentScore
            ////                    var individualList: List<ScoreIndividualModel>
            ////                    individualList = db.GetIndividualActivity(e!!.sectioncode, studentNo)
            ////                    var adapter = ScoreIndividualAdapter(context, individualList)
            ////                    dlgindividual.listIndividualScore.adapter = adapter
            ////                    notifyDataSetChanged()
            ////
            ////                    db2.GradeComputation(e!!.sectioncode, studentNo, currrentGradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
            ////                    dlgindividual.txtOriginslGrade.setText(db2.GetStudentTermGrade(e!!.sectioncode,  studentNo, currrentGradingPeriod, "ORIGINAL").toString())
            ////                    dlgindividual.txtAdjustedGrade.setText(db2.GetStudentTermGrade(e!!.sectioncode,  studentNo, currrentGradingPeriod, "ADJUSTED").toString())
            ////                    dlgindividual.txtAdjustedGradeDecimal.setText(db2.GetStudentTermGrade(e!!.sectioncode,  studentNo, currrentGradingPeriod, "DECIMAL").toString())
            ////val section = e!!.sectioncode;
            ////
            ////                    val  sql = "select * from tbgrade_query where sectioncode = '$section' order by lastname"
            ////                    val gr: GradeMain = GradeMain()
            ////                      gr.UpdateListContent(sql)
            ////                    gr.UpdateCount();
            ////                    Log.e("rowBtnSaveAdjust", "@@@@@@")
            ////
            ////                } //rowBtnSaveAdjust
            //
            //
            //
            //            }


            itemView.btnRubricsScore.setOnClickListener {
                itemView.btnActivityScore.text = itemView.btnRubricsScore.text
                var score = itemView.btnActivityScore.text.toString().toInt()
                var e = currentScore
                val db2: DatabaseHandler = DatabaseHandler(context)
                db2.DriveUpdateStudentRecord(e!!.sectioncode, e!!.activitycode, e!!.studentNo, score)
                ScoreMain.ScoreUpdateListContent(context, ScoreMain.globalCategory, ScoreMain.globalsortOrder)
                notifyDataSetChanged()
            } //init


            itemView.rowBtnEdit.setOnClickListener {
                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                Log.e("173", prev_position.toString() + " " + currentPosition)
                var stat = itemView.btnByFive.isVisible
                if (stat == true) {
                    var num = itemView.btnActivityScore.text.toString().toInt() - 1
                    itemView.btnActivityScore.setText(num.toString())
                    SaveData()
                }


                if (previousSelectedScoreButton == null) {
                    Log.e("123", "hello")
                    previousSelectedScoreButton = itemView.btnActivityScore
                    previousSelectedByFiveButton = itemView.btnByFive
                    itemView.btnActivityScore.isEnabled = true
                    itemView.btnByFive.isVisible = true
                    prev_position = currentPosition
                } else if (prev_position != currentPosition) {
                    Log.e("123", "hello22")
                    previousSelectedScoreButton!!.isEnabled = false
                    previousSelectedByFiveButton!!.isVisible = false
                    previousSelectedScoreButton = itemView.btnActivityScore
                    previousSelectedByFiveButton = itemView.btnByFive
                    prev_position = currentPosition
                    itemView.btnActivityScore.isEnabled = true
                    itemView.btnByFive.isVisible = true
                }

            } //init

            itemView.rowBtnEdit.setOnLongClickListener { //                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                //                itemView.rowBtnSave.setVisibility(View.VISIBLE);
                itemView.btnActivityScore.isEnabled = true
                true
            } //init

            itemView.btnByFive.setOnLongClickListener {
                var num = itemView.btnByFive.text.toString().toInt() - 5

                val db: TableActivity = TableActivity(context)
                val e = currentScore
                val activityItem = db.GetActivityDetail(e!!.sectioncode, e!!.activitycode, "ITEM")
                if (num >= 0) {
                    itemView.btnByFive.setText(num.toString())
                    itemView.btnActivityScore.setText(num.toString())
                    SaveData()
                }
                SaveData()
                true
            }

            itemView.btnActivityScore.setOnLongClickListener {
                var num = itemView.btnActivityScore.text.toString().toInt() - 1
                itemView.btnActivityScore.setText(num.toString())
                SaveData()
                true
            }

            itemView.btnByFive.setOnClickListener {
                var num = itemView.btnByFive.text.toString().toInt() + 5

                val db: TableActivity = TableActivity(context)
                val e = currentScore
                val activityItem = db.GetActivityDetail(e!!.sectioncode, e!!.activitycode, "ITEM")
                if (num <= activityItem.toInt()) {
                    itemView.btnByFive.setText(num.toString())
                    itemView.btnActivityScore.setText(num.toString())
                    SaveData()
                    ScoreMain.ShowChart(context)
                    if (num > 0) {
                        itemView.btnActivityScore.setBackgroundColor(Color.parseColor("#64B5F6"))
                    } else {
                        itemView.btnActivityScore.setBackgroundResource(android.R.drawable.btn_default); //
                    }
                }
            } //init

            itemView.btnActivityScore.setOnClickListener {

                var num = itemView.btnActivityScore.text.toString().toInt() + 1
                val db: TableActivity = TableActivity(context)
                val e = currentScore
                val activityItem = db.GetActivityDetail(e!!.sectioncode, e!!.activitycode, "ITEM")
                if (num <= activityItem.toInt()) {
                    itemView.btnActivityScore.setText(num.toString())
                    SaveData()
                    ScoreMain.ShowChart(context)
                    if (num > 0) {
                        itemView.btnActivityScore.setBackgroundColor(Color.parseColor("#64B5F6"))
                    } else {
                        itemView.btnActivityScore.setBackgroundResource(android.R.drawable.btn_default); //
                    }
                }


            } //init

            itemView.rowBtnSave.setOnClickListener {
                SaveData()
                ScoreMain.ShowChart(context)
                Log.e("@@", ScoreMain.globalsortOrder)
                if (Util.CHART_STATUS == false) {
                    Log.e("@@", ScoreMain.globalsortOrder)
                    ScoreMain.ScoreUpdateListContent(context, ScoreMain.globalCategory, ScoreMain.globalsortOrder)
                    notifyDataSetChanged()
                }

            } //init

            itemView.btnGroup.setOnClickListener {
                if (itemView.btnGroup.text == "NONE") itemView.btnGroup.text = "G-A"
                else if (itemView.btnGroup.text == "G-A") itemView.btnGroup.text = "G-B"
                else if (itemView.btnGroup.text == "G-B") itemView.btnGroup.text = "G-C"
                else if (itemView.btnGroup.text == "G-C") itemView.btnGroup.text = "G-D"
                else if (itemView.btnGroup.text == "G-D") itemView.btnGroup.text = "G-E"
                else if (itemView.btnGroup.text == "G-E") itemView.btnGroup.text = "G-F"
                else if (itemView.btnGroup.text == "G-F") itemView.btnGroup.text = "G-G"
                else if (itemView.btnGroup.text == "G-G") itemView.btnGroup.text = "G-H"
                else if (itemView.btnGroup.text == "G-H") itemView.btnGroup.text = "G-I"
                else if (itemView.btnGroup.text == "G-I") itemView.btnGroup.text = "G-J"
                else if (itemView.btnGroup.text == "G-J") itemView.btnGroup.text = "G-K"

                val grp = itemView.btnGroup.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context)
                db.UpdateGroupNumber(currentScore!!.studentNo, grp)


            } //init

            itemView.btnGroup.setOnLongClickListener {
                itemView.btnGroup.text = "NONE"
                val grp = itemView.btnGroup.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context)
                db.UpdateGroupNumber(currentScore!!.studentNo, grp)

                true
            }

            itemView.btnScoreRemark.setOnLongClickListener {
                itemView.btnScoreRemark.text = "-"
                val db: TableActivity = TableActivity(context)
                val e = currentScore
                db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNo, e.score, "-", e!!.SubmissionStatus, e!!.AdjustedScore)

                ScoreMain.scoreList[currentPosition].remark = "-"
                true
            }

            itemView.btnGrr.setOnClickListener {
                POPOP_MENU = "GROUP"
                showPopMenuScore(itemView, currentScore!!.sectioncode)
            }


            //0622

            itemView.btnScoreRemark.setOnClickListener {
                showPopRemarkScore(itemView, currentScore!!.sectioncode)
                POPOP_MENU =
                    "REMARK" //                val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
                //                val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
                //                val inputDialog = mBuilder.show()
                //                inputDialog.setCanceledOnTouchOutside(false); //
                //                RemarkMain.btnRemark = itemView.btnScoreRemark
                //                RemarkMain.alert = inputDialog
                //                RemarkMain.remark_section = currentScore!!.sectioncode
                var ppp: RemarkMain =
                    RemarkMain() //    ppp.SetButtonArray(dlgremark) //                ppp.DisplayRenark(dlgremark, context, currentScore!!.studentNo, currentScore!!.activitycode)
                //
                //                for (i in 0..9) {
                //                    RemarkMain.buttons[i].setOnClickListener {
                //                        val rem = RemarkMain.buttons[i].text.toString()
                //                        itemView.btnScoreRemark.text = rem
                //                        inputDialog.dismiss() //UpdateStudentRemark()
                //                        val db: DatabaseHandler = DatabaseHandler(context)
                //                        db.UpdateStudentRemark(RemarkMain.remark_section, currentScore!!.activitycode, currentScore!!.studentNo, rem)
                //                        ScoreMain.ShowChart(context)
                //                        ScoreMain.scoreList[currentPosition].remark = rem
                //                    }
                //                }
                //
                //                Util.Msgbox(context, "Hello")
                //                Log.e("354", "hehehe")
                //
            }


            //            itemView.rowBtnSave.setOnClickListener {
            //                SaveData()
            //            } //init
            itemView.btnRubrics.setOnClickListener {

                var e = currentScore
                val dlgremark =
                    android.view.LayoutInflater.from(context).inflate(R.layout.rubric_student, null)
                val mBuilder =
                    androidx.appcompat.app.AlertDialog.Builder(context).setView(dlgremark)
                        .setTitle("")
                val inputDialog = mBuilder.show()
                inputDialog.setCanceledOnTouchOutside(false); //
                val db2: DatabaseHandler = DatabaseHandler(context)
                var actRubricCode = db2.GetSectionActivityRubric(e!!.sectioncode, e!!.activitycode);


                var stud = db2.GetActivityRecord(e!!.activitycode, e!!.sectioncode, e!!.studentNo)

                var first = db2.GetActivityRubricCriteria(actRubricCode, e!!.activitycode, 1)
                dlgremark.btnRubricfirst.setText(first)
                dlgremark.btnPointFirst.setText(stud.first.toString())
                dlgremark.txtRubricStudetName.setText(stud.lastName + "," + stud.firstName)
                dlgremark.txtRubricStudetName.setText(stud.lastName + "," + stud.firstName)
                dlgremark.btnRubricRemark.setText(stud.remark)

                var criteria = db2.GetActivityRubricCriteria(actRubricCode, e!!.activitycode, 2)
                dlgremark.btnRubricSecond.setText(criteria)
                dlgremark.btnPointSecond.setText(stud.second.toString())

                criteria = db2.GetActivityRubricCriteria(actRubricCode, e!!.activitycode, 3)
                dlgremark.btnRubricThird.setText(criteria)
                dlgremark.btnPointThird.setText(stud.third.toString())


                criteria = db2.GetActivityRubricCriteria(actRubricCode, e!!.activitycode, 4)
                dlgremark.btnRubricFourth.setText(criteria)
                dlgremark.btnPointFourth.setText(stud.fourth.toString())


                criteria = db2.GetActivityRubricCriteria(actRubricCode, e!!.activitycode, 5)
                dlgremark.btnRubricFifth.setText(criteria)
                dlgremark.btnPointFifth.setText(stud.fifth.toString())

                GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)


                dlgremark.btnPointFirst.setOnClickListener {
                    var num = dlgremark.btnPointFirst.text.toString()
                        .toInt() // fun Updatecriteria(num: Int, buttonNum: Int, criteria: String, dlgRemark: View, actCode: String, section:String): Int {
                    var result =
                        Updatecriteria(num, 1, "Criteria1", dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    dlgremark.btnPointFirst.setText(result.toString())
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                }

                dlgremark.btnPointFirst.setOnLongClickListener {
                    dlgremark.btnPointFirst.setText("0")
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria1", 0)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)

                    true
                }

                dlgremark.btnPointSecond.setOnClickListener {
                    var num = dlgremark.btnPointSecond.text.toString().toInt()
                    var result =
                        Updatecriteria(num, 1, "Criteria1", dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    dlgremark.btnPointSecond.setText(result.toString())
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                }

                dlgremark.btnPointSecond.setOnLongClickListener {
                    dlgremark.btnPointSecond.setText("0")
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria2", 0)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)

                    true
                }


                dlgremark.btnPointThird.setOnClickListener {
                    var num = dlgremark.btnPointThird.text.toString().toInt()
                    var result =
                        Updatecriteria(num, 3, "Criteria3", dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)


                    dlgremark.btnPointThird.setText(result.toString())
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)

                }

                dlgremark.btnPointThird.setOnLongClickListener {
                    dlgremark.btnPointThird.setText("0")
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria3", 0)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)

                    true
                }

                dlgremark.btnPointFourth.setOnClickListener {
                    var num = dlgremark.btnPointFourth.text.toString().toInt()
                    var result =
                        Updatecriteria(num, 3, "Criteria4", dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    dlgremark.btnPointFourth.setText(result.toString())
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                }

                dlgremark.btnPointFourth.setOnLongClickListener {
                    dlgremark.btnPointFourth.setText("0")
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria4", 0)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)

                    true
                }

                dlgremark.btnPointFifth.setOnClickListener {
                    var num = dlgremark.btnPointFifth.text.toString().toInt()
                    var result =
                        Updatecriteria(num, 5, "Criteria5", dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    dlgremark.btnPointFifth.setText(result.toString())
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true
                }



                dlgremark.btnPointFifth.setOnLongClickListener {
                    dlgremark.btnPointFifth.setText("0")
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria5", 0)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true

                }


                //RubricFirst
                dlgremark.btnRubricfirst.setOnLongClickListener {
                    var point = db2.GetActivityCriteria(e!!.sectioncode, e!!.activitycode, 1)
                    dlgremark.btnPointFirst.setText(point.toString())
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria1", point)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true
                }

                dlgremark.btnRubricfirst.setOnClickListener {
                    var num = dlgremark.btnPointFirst.text.toString().toInt()
                    if (num > 0) {
                        num--;
                        dlgremark.btnPointFirst.setText(num.toString())
                        db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria1", num)
                        GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    }
                }

                //RubricSecond
                dlgremark.btnRubricSecond.setOnLongClickListener {
                    var point = db2.GetActivityCriteria(e!!.sectioncode, e!!.activitycode, 2)
                    dlgremark.btnPointSecond.setText(point.toString())
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria2", point)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true
                }

                dlgremark.btnRubricSecond.setOnClickListener {
                    var num = dlgremark.btnPointSecond.text.toString().toInt()
                    if (num > 0) {
                        num--;
                        dlgremark.btnPointSecond.setText(num.toString())
                        db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria2", num)
                        GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    }
                }

                dlgremark.btnRubricThird.setOnLongClickListener {
                    var point = db2.GetActivityCriteria(e!!.sectioncode, e!!.activitycode, 3)
                    dlgremark.btnPointThird.setText(point.toString())
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria3", point)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true
                }

                dlgremark.btnRubricThird.setOnClickListener {
                    var num = dlgremark.btnPointThird.text.toString().toInt()
                    if (num > 0) {
                        num--;
                        dlgremark.btnPointThird.setText(num.toString())
                        db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria3", num)
                        GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    }
                }

                dlgremark.btnRubricFourth.setOnLongClickListener {
                    var point = db2.GetActivityCriteria(e!!.sectioncode, e!!.activitycode, 4)
                    dlgremark.btnPointFourth.setText(point.toString())
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria4", point)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true
                }

                dlgremark.btnRubricFourth.setOnClickListener {
                    var num = dlgremark.btnPointFourth.text.toString().toInt()
                    if (num > 0) {
                        num--;
                        dlgremark.btnPointFourth.setText(num.toString())

                        db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria4", num)
                        GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    }
                }

                dlgremark.btnRubricFifth.setOnLongClickListener {
                    var point = db2.GetActivityCriteria(e!!.sectioncode, e!!.activitycode, 5)
                    dlgremark.btnPointFifth.setText(point.toString())
                    db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria5", point)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    true
                }

                dlgremark.btnRubricFifth.setOnClickListener {
                    var num = dlgremark.btnPointFifth.text.toString().toInt()
                    if (num > 0) {
                        num--;
                        dlgremark.btnPointFifth.setText(num.toString())
                        db2.UpdateCriteriaScore(e!!.sectioncode, e!!.activitycode, e!!.studentNo, "Criteria5", num)
                        GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    }
                }







                dlgremark.btnTotalScore.setOnClickListener {
                    var score = dlgremark.btnTotalScore.text.toString().toInt()

                    db2.DriveUpdateStudentRecord(e!!.sectioncode, e!!.activitycode, e!!.studentNo, score)
                    GetRubricTotalScore(dlgremark, e!!.activitycode, e!!.sectioncode, e!!.studentNo)
                    ScoreMain.ScoreUpdateListContent(context, ScoreMain.globalCategory, ScoreMain.globalsortOrder)
                    notifyDataSetChanged()

                }


            }



            itemView.rowBtnOpenFolder.setOnClickListener {
                var section = currentScore!!.sectioncode
                var studentNumber = currentScore!!.studentNo
                val db: DatabaseHandler = DatabaseHandler(context)
                Util.FOLDER_LINK = db.GetLink(studentNumber, section)
                Util.FOLDER_NAME = currentScore!!.lastnanme
                Util.FOLDER_SECTION = currentScore!!.sectioncode
                Util.FOLDER_STUDNUM = currentScore!!.studentNo
                Util.FOLDER_ACT_DESCRIPTION = currentScore!!.description
                val intent = Intent(context, Gdrive::class.java)
                context.startActivity(intent)
            }


            //                itemView.btnSubmissionStatus.setBackgroundResource(android.R.drawable.btn_default);
            //                itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor(a"#64B5F6"))
            //                itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#69F0AE"))


            //            itemView.chkName.setOnClickListener {
            //                if (itemView.chkName!!.isChecked==true)
            //                    ScoreMain.scoreList[currentPosition].checkBoxStatus = "ON"
            //                else
            //                    ScoreMain.scoreList[currentPosition].checkBoxStatus ="OFF"
            //                notifyDataSetChanged()
            //            } //init


        }

        fun setData(myscore: ScoreModel?, pos: Int) {
            itemView.rowtxtName.text = myscore!!.completeName
            itemView.rowtxtRemark.text = myscore!!.remark
            itemView.btnByFive.isVisible = false


            itemView.rowBtnEdit.isVisible = !Util.ACT_RUBRIC
            itemView.btnActivityScore.setTextColor(Color.BLACK)
            if (myscore!!.score > 0) {
                itemView.btnActivityScore.setBackgroundColor(Color.parseColor("#64B5F6"))
            } else {
                itemView.btnActivityScore.setBackgroundResource(android.R.drawable.btn_default); //
            }


            itemView.btnByFive.setText("0")
            itemView.btnActivityScore.setText(myscore!!.score.toString())
            itemView.btnGroup.setText(myscore!!.grp.toString()) //itemView.cboIndividualGroup.setSelection(GetGroupIndex(myscore!!.grp, context))

            val db: TableActivity = TableActivity(context)
            val count =
                db.GetIndividualNoSubmissionCount(myscore!!.sectioncode, myscore!!.studentNo)

            val db2: DatabaseHandler = DatabaseHandler(context)
            itemView.btnRubricsScore.isVisible = Util.ACT_RUBRIC


            var stud =
                db2.GetActivityRecord(myscore!!.activitycode, myscore!!.sectioncode, myscore!!.studentNo)
            var totalScore = stud.first + stud.second + stud.third + stud.fourth + stud.fifth

            itemView.btnRubricsScore.text = totalScore.toString()
            itemView.btnGrr.text = myscore!!.grp


            //            if (count == 0) itemView.btnIndividualCount.setBackgroundColor(Color.parseColor("#D3D3D3"))
            //            else itemView.btnIndividualCount.setBackgroundColor(Color.parseColor("#69F0AE"))


            //
            //            if (myscore!!.SubmissionStatus == "NO") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#D3D3D3"))
            //            else if (myscore!!.SubmissionStatus == "YES") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#64B5F6"))
            //            else if (myscore!!.SubmissionStatus == "LATE") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
            //            else if (myscore!!.SubmissionStatus == "DRP") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#FFFF00"))
            //            else if (myscore!!.SubmissionStatus == "OK") itemView.btnSubmissionStatus.setBackgroundColor(Color.parseColor("#FFA500"))


            itemView.btnScoreRemark.setText(myscore!!.remark)




            if (myscore!!.status == "CLOSED") {

                itemView.btnActivityScore.isEnabled = false

                itemView.btnActivityScore.isEnabled = false


            } else {

                itemView.btnActivityScore.isEnabled = true

                itemView.btnActivityScore.isEnabled = true


            } //
            //                      if (myscore!!.checkBoxStatus == "OFF") {
            //                                itemView.chkName!!.isChecked=false
            //                            } else {
            //                                    itemView.chkName!!.isChecked=true
            //                            }
            previousSelectedScoreButton == null
            prev_position = -1
            this.currentScore = myscore
            this.currentPosition = pos
        }


        fun GetGroupIndex(search: String, context: Context): Int {
            val arrGender: Array<String> = context.getResources().getStringArray(R.array.grpNumber)
            val index = arrGender.indexOf(search)
            return index
        }


        fun Updatecriteria(num: Int, buttonNum: Int, criteria: String, dlgRemark: View, actCode: String, section: String, studNum: String): Int {
            val db2: DatabaseHandler = DatabaseHandler(context)
            var point = db2.GetActivityCriteria(section, actCode, buttonNum)
            var num1 = num
            if (num1 < point) {
                num1++;
                var total = GetTotalScore(dlgRemark) + 1
                db2.UpdateCriteriaScore(section, actCode, studNum, criteria, num1)
            }

            return num1;


        }

        fun GetTotalScore(dlgRemark: View): Int {
            var n1 = dlgRemark.btnPointFirst.text.toString().toInt();
            var n2 = dlgRemark.btnPointSecond.text.toString().toInt();
            var n3 = dlgRemark.btnPointThird.text.toString().toInt();
            var n4 = dlgRemark.btnPointFourth.text.toString().toInt();
            var n5 = dlgRemark.btnPointFifth.text.toString().toInt();
            var total = n1 + n2 + n3 + n4 + n5

            return total
        }


        fun GetRubricTotalScore(dlgremark: View, actCode: String, section: String, studNum: String) {

            val db2: DatabaseHandler = DatabaseHandler(context)
            val grades: Grades = Grades(context)

            var stud = db2.GetActivityRecord(actCode, section, studNum)
            var totalScore = stud.first + stud.second + stud.third + stud.fourth + stud.fifth
            var point = GetRubricTotalPoint(actCode, section)
            var dec = ((totalScore.toDouble() / point.toDouble()) * 100).toDouble()
            var grade = grades.ConvertDepedGrade(dec)
            dlgremark.btnTotalScore.setText(totalScore.toString())
            dlgremark.btnEquivalentGrade.setText(grade.toString())
            var recordScore = db2.GetActivutyStudentScore(section, actCode, studNum)
            Log.e("798SCORE", recordScore.toString())
            Log.e("798SCORE", totalScore.toString())
            if (recordScore == totalScore) {
                dlgremark.btnTotalScore.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else {

                dlgremark.btnTotalScore.setBackgroundResource(android.R.drawable.btn_default);
            }

        }

        fun GetRubricTotalPoint(actCode: String, section: String): Int {
            var point = 0

            val db2: DatabaseHandler = DatabaseHandler(context)

            point = point + db2.GetActivityCriteria(section, actCode, 1)
            point = point + db2.GetActivityCriteria(section, actCode, 2)
            point = point + db2.GetActivityCriteria(section, actCode, 3)
            point = point + db2.GetActivityCriteria(section, actCode, 4)
            point = point + db2.GetActivityCriteria(section, actCode, 5)
            return point
        }


        fun ShowIndividualRecord(dlgindividual: View, f: ScoreModel, individualDialog: AlertDialog) {

            val completeName = f!!.completeName
            dlgindividual.txtName.setText(f!!.completeName)

            dlgindividual.txtStudentNo.setText(currentScore!!.studentNo)
            val f = currentScore
            val db2: Grades = Grades(context)
            val db: TableActivity = TableActivity(context)
            var currrentGradingPeriod = db.GetDefaultGradingPeriod();
            dlgindividual.txtOriginslGrade.setText(db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currrentGradingPeriod, "ORIGINAL")
                                                       .toString())
            dlgindividual.txtAdjustedGrade.setText(db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currrentGradingPeriod, "ADJUSTED")
                                                       .toString())
            dlgindividual.txtAdjustedGradeDecimal.setText(db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currrentGradingPeriod, "DECIMAL")
                                                              .toString())

            bar = dlgindividual


            val e = currentScore

            individualList = db.GetIndividualActivity(e!!.sectioncode, e!!.studentNo)

            val layoutmanager = LinearLayoutManager(context)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlgindividual.listIndividualScore.layoutManager = layoutmanager
            individualAdapter = ScoreIndividualAdapter(context, individualList)
            dlgindividual.listIndividualScore.adapter = individualAdapter

        }

        //0622
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun showPopMenuScore(v: View, section: String) {
            val popup = PopupMenu(context, itemView.btnGrr, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_group)
            val db2: DatabaseHandler = DatabaseHandler(context)
            var grp = db2.GetListGroupNumber(section)
            var x = 0;
            while (x < grp.size) {
                popup.getMenu().add(grp[x]);
                x++
            }
            popup.show()


        }

        //0622
        fun showPopRemarkScore(v: View, section: String) {
            val popup = PopupMenu(context, itemView.btnScoreRemark, Gravity.RIGHT)
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

        /*    var remarkList: ArrayList<String> = ArrayList<String>()
            remarkList = db.GetRemark(remark_section)
    */
        override fun onMenuItemClick(item: MenuItem): Boolean {
            if (POPOP_MENU == "GROUP") {
                val selected = item.toString() // //0621
                val db2: DatabaseHandler = DatabaseHandler(context)
                var grp = db2.SetStudentGroup(currentScore!!.studentNo, selected)
                itemView.btnGrr.setText(selected)
            }

            if (POPOP_MENU == "REMARK") {
                val selected = item.toString() // //0621t

                if (selected == "ADD REMARK") {
                    val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
                    val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
                    val inputDialog = mBuilder.show()
                    inputDialog.setCanceledOnTouchOutside(false); //
                    RemarkMain.alert = inputDialog
                    RemarkMain.remark_section = currentScore!!.sectioncode
                    var ppp: RemarkMain = RemarkMain() //    ppp.SetButtonArray(dlgremark)
                    ppp.DisplayRenark(dlgremark, context, currentScore!!.studentNo, currentScore!!.activitycode)

                } else {
                    itemView.btnScoreRemark.setText(selected)
                    val db: DatabaseHandler = DatabaseHandler(context)
                    db.UpdateStudentRemark(currentScore!!.sectioncode, currentScore!!.activitycode, currentScore!!.studentNo, selected)
                    ScoreMain.ShowChart(context)
                }
            }

            return true
        }

        fun SaveData() {
            val db: TableActivity = TableActivity(context)
            var myscore = itemView.btnActivityScore.text.toString()
            val e = currentScore
            val activityItem = db.GetActivityDetail(e!!.sectioncode, e!!.activitycode, "ITEM")
            var score = 0;
            var oldscore = e!!.score
            try {
                score = parseInt(myscore)
            } catch (e: NumberFormatException) {
                score = 0
            }

            if (Util.CHART_STATUS == false) {
                if (score == 0) {
                    ScoreMain.scoreList[currentPosition].SubmissionStatus = "NO"
                } else {
                    ScoreMain.scoreList[currentPosition].SubmissionStatus = "OK"
                }
            } else {
                if (score == 0) {
                    ChartList.scoreList[currentPosition].SubmissionStatus = "NO"
                } else {
                    ChartList.scoreList[currentPosition].SubmissionStatus = "OK"
                }

            }
            if (score > activityItem.toInt()) {
                Util.Msgbox(context, "The Score is invalid")
            } else {
                itemView.rowBtnEdit.setVisibility(View.VISIBLE); //                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                //                itemView.btnActivityScore.isEnabled = false
                if (Util.CHART_STATUS == false) {
                    ScoreMain.scoreList[currentPosition].status = "CLOSED"
                    ScoreMain.scoreList[currentPosition].score = score
                } else {
                    ChartList.scoreList[currentPosition].status = "CLOSED"
                    ChartList.scoreList[currentPosition].score = score
                }
                db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNo, e.score, e.remark, e.SubmissionStatus, e.AdjustedScore) // db.SaveLog(e.studentNo, e.sectioncode, e.activitycode, oldscore, e.score)
            }
        }


        fun DisplayInputRemark() {
            val dlgRemark = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
            val completeName = currentScore!!.completeName
            val mBuilder = AlertDialog.Builder(context).setView(dlgRemark).setTitle("$completeName")
            dlgRemark.txtdata.hint = "Activity Remark"
            val remarkDialog = mBuilder.show()
            remarkDialog.setCanceledOnTouchOutside(false);
            remarkDialog.getWindow()!!
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

            dlgRemark.btnOK.setOnClickListener {
                val db: TableActivity = TableActivity(context)
                val remark = dlgRemark.txtdata.text.toString()
                val e = currentScore
                db.UpdateStudentRecord(e!!.activitycode, e!!.sectioncode, e.studentNo, e.score, remark, e.SubmissionStatus)
                remarkDialog.dismiss()
                ScoreMain.scoreList[currentPosition].remark = remark
                notifyDataSetChanged()
            }

            dlgRemark.btnCancel.setOnClickListener {
                remarkDialog.dismiss()
            }
        }


        fun CopyText(copyString: String) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", copyString)
            clipboardManager.setPrimaryClip(clipData)

        }


    }


    override fun onCancel(p0: DialogInterface?) {
        Log.e("Hello", "1111")
    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}

//0621
fun DatabaseHandler.GetListGroupNumber(sectionName: String): Array<String> {
    val grpList: ArrayList<GrpModel> = ArrayList<GrpModel>()
    var sql = """
            SELECT *
            FROM tbgroup WHERE SectionCode= '$sectionName' order by GroupNumber 
    """.trimIndent()
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)

    var x = 0

    var grpArray = Array(cursor.count + 1) { "" }
    grpArray[x] = "NONE"
    x++
    if (cursor.moveToFirst()) {
        do { //            , 	SectionCode
            var grpnumber = cursor.getString(cursor.getColumnIndex("GroupNumber"))
            grpArray[x] = grpnumber
            x++
        } while (cursor.moveToNext())
    }
    return grpArray
}


//0621
fun DatabaseHandler.SetStudentGroup(studentNumber: String, grpNumber: String) {
    val db = this.writableDatabase

    var sql = """
             update tbenroll set GrpNumber='$grpNumber'
                          where  StudentNo ='$studentNumber'
        """.trimIndent()


    Log.e("@@@", sql)
    db.execSQL(sql)
}
