package com.example.myapplication05


import android.app.AlertDialog
import android.content.*
import android.database.Cursor
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.log_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.task_row.view.*


class GradeAdapter(val context: Context, val myGrade: List<GradeModel>) :
    RecyclerView.Adapter<GradeAdapter.MyViewHolder>() {


    companion object {
        var individualList = arrayListOf<ScoreIndividualModel>()
        var myIndividualAdapter: ScoreIndividualAdapter? = null;
        fun UpdateListContent(context: Context, studentNumber: String, section: String) {
            val dbactivity: TableActivity = TableActivity(context)
            val theinidividual: List<ScoreIndividualModel>
            individualList.clear()
            theinidividual = dbactivity.GetIndividualActivity(section, studentNumber)

            for (e in theinidividual) {
                individualList.add(ScoreIndividualModel(e.activitycode, e.sectioncode, e.studentNo, e.description, e.score, e.remark, e.status, e.item, e.gradingperiod, e.category, e.adjustedScore, e.submissionStatus))
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.grade_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return myGrade.size;
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = myGrade[position]
        holder.setData(mySummary, position)

        Log.e("`1234", "KKK")
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    inner class MyViewHolder(itemView1: View) : RecyclerView.ViewHolder(itemView1) {

        var currentGrade: GradeModel? = null
        var currentPosition: Int = 0

        init {


            itemView1.btnEnrolled.setOnClickListener {
                itemView1.btnEnrolled.setVisibility(View.INVISIBLE);
                itemView1.btnDropped.setVisibility(View.VISIBLE);
                val db2: Grades = Grades(context)
                val gradingPeriod = Util.GetCurrentGradingPeriod(context)
                db2.UpdateEnrollmentStatus(currentGrade!!.sectioncode, currentGrade!!.studentNo, "DRP", gradingPeriod)
                if (gradingPeriod == "FIRST") {
                    currentGrade!!.MidtermStatus = "DRP"
                }

                if (gradingPeriod == "SECOND") {
                    currentGrade!!.FinalStatus = "DRP"
                }
            }

            itemView1.btnDropped.setOnClickListener {
                itemView1.btnEnrolled.setVisibility(View.VISIBLE);
                itemView1.btnDropped.setVisibility(View.INVISIBLE);
                val db2: Grades = Grades(context)
                val gradingPeriod = Util.GetCurrentGradingPeriod(context)
                db2.UpdateEnrollmentStatus(currentGrade!!.sectioncode, currentGrade!!.studentNo, "OK", gradingPeriod)
                if (gradingPeriod == "FIRST") {
                    currentGrade!!.MidtermStatus = "OK"
                }

                if (gradingPeriod == "SECOND") {
                    currentGrade!!.FinalStatus = "OK"
                }

            }

            itemView1.rowBtnComputation.setOnClickListener {
                val studentNumber = currentGrade!!.studentNo
                val firstName = currentGrade!!.firstname
                val lastName = currentGrade!!.lastname
                val section = currentGrade!!.sectioncode

                val dlgStudent =
                    LayoutInflater.from(context).inflate(R.layout.grade_computation, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgStudent)
                    .setTitle(lastName + "  " + firstName)

                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);
                confirmDialog.getWindow()!!
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                confirmDialog.setCanceledOnTouchOutside(false);
                val gradingPeriod = Util.GetCurrentGradingPeriod(context)
                val db2: Grades = Grades(context)
                var gr = ""
                val school = db2.GetSchool(section)
                Log.e("SSS", school + "   " + currentGrade!!.studentNo)
                if (school == "CSPC") {
                    gr =
                        db2.CSPCComputeGrades(section, currentGrade!!.studentNo, gradingPeriod, "COMPUTATION")
                } else if (school == "BISCAST") {
                    gr =
                        db2.BISCASATComputeGrades(section, currentGrade!!.studentNo, gradingPeriod, "COMPUTATION")
                } else {
                    Log.e("gr", school)
                    gr =
                        db2.DEPEDComputeGrades(section, currentGrade!!.studentNo, gradingPeriod, "COMPUTATION")

                }


                //                val c = db2.CollegeComputeGrades(section, currentGrade!!.studentNo,gradingPeriod,"COMPUTATION")
                dlgStudent.txtComputation.setText(gr)

                dlgStudent.btnCopy.setOnClickListener {
                    val msg = "" + "\n" + dlgStudent.txtComputation.text.toString()
                    Util.CopyText(context, msg, "COPY")
                }


            }
            itemView1.rowBtnScoreIndividual.setOnClickListener { //                    val intent = Intent(this,  IndividualStudenht::class.java)
                //                    startActivity(intent)

                val intent = Intent(context, IndividualStudenht::class.java)
                Util.GRADE_INDIVIDUAL = "GRADES"
                Util.GRADE_STUD_NO = currentGrade!!.studentNo
                Util.GRADE_NAME = currentGrade!!.lastname + "," + currentGrade!!.firstname
              Util.GRADE_SECTION =  currentGrade!!.sectioncode
                context.startActivity(intent)
            } //                val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
            //                try {
            //                    val studentNumber = currentGrade!!.studentNo
            //                    val firstName = currentGrade!!.firstname
            //                    val lastName = currentGrade!!.lastname
            //                    val section = currentGrade!!.sectioncode
            //
            //                    // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();
            //
            //                    val dlgStudent =
            //                        LayoutInflater.from(context).inflate(R.layout.score_individual, null)
            //                    val mBuilder = AlertDialog.Builder(context).setView(dlgStudent)
            //                        .setTitle(lastName + "  " + firstName)
            //
            //                    val confirmDialog = mBuilder.show()
            //                    confirmDialog.setCanceledOnTouchOutside(false);
            //                    confirmDialog.getWindow()!!
            //                        .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            //                    confirmDialog.setCanceledOnTouchOutside(false);
            //                    dlgStudent.txtStudentNo.setText(studentNumber)
            //                    dlgStudent.txtSection.setText(section)
            //                    val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
            //                    val f = currentGrade
            //                    val db2: Grades = Grades(context)
            //                    dlgStudent.txtOriginslGrade.setText(db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currentGradingPeriod, "ORIGINAL")
            //                                                            .toString())
            //                    dlgStudent.txtAdjustedGrade.setText(db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currentGradingPeriod, "ADJUSTED")
            //                                                            .toString())
            //                    dlgStudent.txtAdjustedGradeDecimal.setText(db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currentGradingPeriod, "DECIMAL")
            //                                                                   .toString())
            //
            //                    var origGrade = db2.GetStudentTermGrade(f!!.sectioncode, f!!.studentNo, currentGradingPeriod, "ORIGINAL")
            //
            //                    dlgStudent.btnTarget.text =  GetTarget(origGrade.toInt()).toString()
            //
            //
            //                    bar = dlgStudent
            //
            //                    //                val myGrades: Grades = Grades(context)
            //                    //                var gr = myGrades.GetStudentGrades(section, studentNumber, "FIRST")
            //                    //                dlgStudent.txtGrade.setText(gr.toString())
            //                    //                Util.txtgrade = dlgStudent.findViewById(R.id.txtGrade) as EditText
            //
            //                    UpdateListContent(context, studentNumber, section)
            //                    val layoutmanager = LinearLayoutManager(context)
            //                    layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            //                    dlgStudent.listIndividualScore.layoutManager = layoutmanager
            //                    myIndividualAdapter = ScoreIndividualAdapter(context, individualList)
            //                    dlgStudent.listIndividualScore.adapter =
            //                        myIndividualAdapter //                dlgStudent.txtFirstName.setText(firstName)
            //                    Log.e("hello124", "OK")
            //
            //
            //                    dlgStudent.btnMessage.setOnClickListener {
            //                        val db: DatabaseHandler = DatabaseHandler(context)
            //                        var msg = db.GetMessage(currentGrade!!.sectioncode)
            //                       var msgNew = msg.replace("<Student>", firstName + "  " + lastName)
            //Log.e("ppp", msgNew)
            //                        Util.CopyText(context, msgNew)
            //                    }
            //
            //                    dlgStudent.btnPlus.setOnLongClickListener {
            //                        var num = dlgStudent.btnPlus.text.toString().toInt()
            //                        if (num > 0) {
            //                            num = num - 1
            //                            dlgStudent.btnPlus.setText(num.toString()) //
            //                            AdjustedScore(num,  dlgStudent)
            //
            //                        //                   if ( == "-") num = -1
            //                        }
            //                        true
            //                    }
            //
            //
            //
            //                    dlgStudent.btnPlus.setOnClickListener {
            //                        var num =dlgStudent.btnPlus.text.toString().toInt()
            //                        if (num<10) {
            //                            num = num + 1
            //                            dlgStudent.btnPlus.setText(num.toString())
            //                            AdjustedScore(num,  dlgStudent)
            //                        }
            //                    }
            //
            //
            //
            //                        dlgStudent.btnCopyText.setOnClickListener {
            //                        val db2: Grades = Grades(context)
            //                        val db: TableActivity = TableActivity(context)
            //                        var currrentGradingPeriod = db.GetDefaultGradingPeriod();
            //                        var individualList: List<ScoreIndividualModel>
            //                        val sectionCode = currentGrade!!.sectioncode
            //                        val studentNo = dlgStudent.txtStudentNo.text.toString()
            //                        Log.e("yyy", sectionCode)
            //                        Log.e("yyy", studentNo)
            //                        var s = lastName + "  " + firstName + "\n"
            //                        individualList = db.GetIndividualActivity(sectionCode, studentNo)
            //                        for (e in individualList) {
            //                            Log.e("Helo", "ssss")
            //                            s = s + e.description + "   " + e.submissionStatus + "\n"
            //                        }
            //                        val str =
            //                            db2.GetStudentTermGrade(sectionCode, studentNo, currrentGradingPeriod, "DECIMAL")
            //                                .toString()
            //
            //                        if (currrentGradingPeriod == "FIRST") s =
            //                            s + "Midterm Grade" + "   " + str + "\n"
            //                        else if (currrentGradingPeriod == "SECOND") s =
            //                            s + "Final Grade" + "   " + str + "\n"
            //                        CopyText(s)
            //                        Log.e("yyy", s)
            //
            //                    }
            //
            //
            //
            //                    confirmDialog.setOnCancelListener(object : DialogInterface.OnCancelListener {
            //                        override fun onCancel(dialog: DialogInterface) { // Your code ...
            //                            val e = currentGrade
            //                            val studentNo = dlgStudent.txtStudentNo.text.toString()
            //                            e!!.firstGrade= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "DECIMAL")
            //                            e!!.firstEquivalent= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "ADJUSTED")
            //                            e!!.firstOriginalGrade= db2.GetStudentTermGrade(e!!.sectioncode, studentNo , currentGradingPeriod, "ORIGINAL")
            //                            Util.Msgbox(context, e!!.sectioncode.toString() + "  "  +  e!!.firstGrade +   "  "   +  itemView.rowtxtLastName.text )
            //                            GradeMain.myGradeAdapter!!.notifyDataSetChanged()
            //
            //
            //
            //
            //                        //       );
            //                        }
            //                    })
            //
            //
            //
            //
            //                }
            //                catch(e:Exception)
            //                {
            //                    Log.e("hello123", e.toString())
            //                }
            //
            //
            //
            //
            //
            //            }

            //   notifyDataSetChanged()

        } //init

        fun AdjustedScore(adjustedScore: Int, dlgStudent: View) {
            val db: TableActivity = TableActivity(context)
            val currentGradingPeriod =
                Util.GetCurrentGradingPeriod(context) // val adjustedScore = i + 1
            val sectionCode = currentGrade!!.sectioncode
            val studentNo = dlgStudent.txtStudentNo.text.toString()
            val db2: Grades = Grades(context)
            db2.UpdateAdjustedScore(sectionCode, studentNo, currentGradingPeriod, adjustedScore)

            val e = currentGrade
            var individualList: List<ScoreIndividualModel>
            individualList = db.GetIndividualActivity(e!!.sectioncode, e!!.studentNo)
            var adapter = ScoreIndividualAdapter(context, individualList)
            dlgStudent.listIndividualScore.adapter = adapter
            notifyDataSetChanged()

            db2.GradeComputation(e!!.sectioncode, studentNo, currentGradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
            dlgStudent.txtOriginslGrade.setText(db2.GetStudentTermGrade(e!!.sectioncode, studentNo, currentGradingPeriod, "ORIGINAL")
                                                    .toString())
            dlgStudent.txtAdjustedGrade.setText(db2.GetStudentTermGrade(e!!.sectioncode, studentNo, currentGradingPeriod, "ADJUSTED")
                                                    .toString())
            dlgStudent.txtAdjustedGradeDecimal.setText(db2.GetStudentTermGrade(e!!.sectioncode, studentNo, currentGradingPeriod, "DECIMAL")
                                                           .toString())
        }


        fun GetTarget(grade: Int): Int {
            if (grade == 60) return 70;
            else if (grade == 61) return 71;
            else if (grade == 62) return 72;
            else if (grade == 63) return 73;
            else if (grade == 64) return 74
            else if (grade == 65) return 75;
            else if (grade == 67) return 77;
            else if (grade == 68) return 78;
            else if (grade == 69) return 79;
            else if (grade == 70) return 80;
            else if (grade == 71 || grade == 72) return 81;
            else if (grade == 73 || grade == 74) return 82;
            else if (grade == 75 || grade == 76) return 83;
            else if (grade == 77 || grade == 78) return 84;
            else if (grade == 79 || grade == 80) return 85;
            else if (grade == 81 || grade == 82) return 86;
            else if (grade == 83 || grade == 84) return 87;
            else if (grade == 85 || grade == 86) return 88;
            else if (grade == 87 || grade == 88) return 89;
            else if (grade > 89) return grade + 1
            else return 0

        }

        fun setData(grade: GradeModel?, pos: Int) {
            val db: TableRandom = TableRandom(context)

            itemView.rowtxtLastName.text = grade!!.lastname
            itemView.rowtxtFirstName.text = grade!!.firstname
            val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
            val db2: TableActivity = TableActivity(context)
            val averageStatus = db2.GetAverageStatus()
            val db3: Grades = Grades(context)
            val school = db3.GetSchool(grade!!.sectioncode)


            if (averageStatus == "TRUE") {
                itemView.rowtxtGrade.setVisibility(View.INVISIBLE);
                itemView.rowtxtEquivalentGrade.setVisibility(View.INVISIBLE);
                itemView.rowOriginalGrade.setVisibility(View.INVISIBLE);
                itemView.rowFirstGrade.setVisibility(View.VISIBLE);
                itemView.rowSecondGrade.setVisibility(View.VISIBLE);
                itemView.rowtxtAverageEquivalent.setVisibility(View.VISIBLE);
                itemView.rowtxtAverageGrade.setVisibility(View.VISIBLE);


                if (grade!!.FinalStatus == "OK") {
                    if (school == "DEPED") {
                        itemView.rowFirstGrade.text = grade!!.firstEquivalent.toString()
                        itemView.rowSecondGrade.text = grade!!.secondEquivalent.toString()
                        itemView.rowtxtAverageEquivalent.text =
                            grade!!.CumulativeGradeEquivalent.toString()
                        itemView.rowtxtAverageGrade.text = grade!!.CumulativeGrade.toString()   //
                    } else {
                        itemView.rowFirstGrade.text = grade!!.firstGrade.toString()
                        itemView.rowSecondGrade.text = grade!!.secondGrade.toString()
                        itemView.rowtxtAverageEquivalent.text =
                            grade!!.CumulativeGradeEquivalent.toString()
                        itemView.rowtxtAverageGrade.text = grade!!.CumulativeGrade.toString()   //
                    }



                    if (grade!!.CumulativeGrade < 75) itemView.rowtxtAverageGrade.setTextColor(Color.RED);
                    else itemView.rowtxtAverageGrade.setTextColor(Color.BLACK);



                    itemView.btnEnrolled.setVisibility(View.VISIBLE);
                    itemView.btnDropped.setVisibility(View.INVISIBLE);

                } else if (grade!!.FinalStatus == "DRP") {
                    if (grade!!.MidtermStatus == "DRP") itemView.rowFirstGrade.text = "DRP"
                    else itemView.rowFirstGrade.text = grade!!.firstEquivalent.toString()

                    itemView.rowSecondGrade.text = "DRP"
                    itemView.rowtxtAverageEquivalent.text = "DRP"
                    itemView.rowtxtAverageGrade.text = "DRP"


                    itemView.rowtxtAverageGrade.setTextColor(Color.BLUE);
                    itemView.btnEnrolled.setVisibility(View.INVISIBLE);
                    itemView.btnDropped.setVisibility(View.VISIBLE);
                }

            } else if (currentGradingPeriod == "FIRST") {
                itemView.rowtxtGrade.setVisibility(View.VISIBLE);
                itemView.rowtxtEquivalentGrade.setVisibility(View.VISIBLE);
                itemView.rowOriginalGrade.setVisibility(View.VISIBLE);
                itemView.rowFirstGrade.setVisibility(View.INVISIBLE);
                itemView.rowSecondGrade.setVisibility(View.INVISIBLE);
                itemView.rowtxtAverageEquivalent.setVisibility(View.INVISIBLE);
                itemView.rowtxtAverageGrade.setVisibility(View.INVISIBLE);

                if (grade!!.MidtermStatus != "DRP") {
                    itemView.rowtxtGrade.text = grade!!.firstGrade.toString()
                    itemView.rowtxtEquivalentGrade.text = grade!!.firstEquivalent.toString()
                    itemView.rowOriginalGrade.text = grade!!.firstOriginalGrade.toString()

                    if (grade!!.firstEquivalent > 3.0 && school != "DEPED") itemView.rowtxtEquivalentGrade.setTextColor(Color.RED);
                    else if (grade!!.firstEquivalent < 75 && school == "DEPED") itemView.rowtxtEquivalentGrade.setTextColor(Color.RED);
                    else itemView.rowtxtEquivalentGrade.setTextColor(Color.BLACK);

                } else {
                    itemView.rowtxtGrade.text = "DRP"
                    itemView.rowtxtEquivalentGrade.text = "DRP"
                    itemView.rowOriginalGrade.text = "DRP"
                }

                Log.e("bbb", grade!!.firstname + "  " + grade!!.MidtermStatus)
                if (grade!!.MidtermStatus == "OK") {
                    itemView.btnEnrolled.setVisibility(View.VISIBLE);
                    Log.e("bbb", grade!!.firstname + " 4444 " + grade!!.MidtermStatus)
                    itemView.btnDropped.setVisibility(View.INVISIBLE);
                } else {
                    itemView.btnEnrolled.setVisibility(View.INVISIBLE);
                    itemView.btnDropped.setVisibility(View.VISIBLE);
                    Log.e("bbb", grade!!.firstname + " 333 " + grade!!.MidtermStatus)
                }

            } else if (currentGradingPeriod == "SECOND") {
                itemView.rowtxtGrade.setVisibility(View.VISIBLE);
                itemView.rowtxtEquivalentGrade.setVisibility(View.VISIBLE);
                itemView.rowOriginalGrade.setVisibility(View.VISIBLE);
                itemView.rowFirstGrade.setVisibility(View.INVISIBLE);
                itemView.rowSecondGrade.setVisibility(View.INVISIBLE);
                itemView.rowtxtAverageEquivalent.setVisibility(View.INVISIBLE);
                itemView.rowtxtAverageGrade.setVisibility(View.INVISIBLE);

                if (grade!!.FinalStatus != "DRP") {
                    itemView.rowtxtGrade.text = grade!!.secondGrade.toString()
                    itemView.rowtxtEquivalentGrade.text = grade!!.secondEquivalent.toString()
                    itemView.rowOriginalGrade.text = grade!!.secondOriginalGrade.toString()

                    if (grade!!.secondEquivalent > 3.0 && school != "DEPED") itemView.rowtxtEquivalentGrade.setTextColor(Color.RED);
                    else if (grade!!.secondEquivalent < 75 && school == "DEPED") itemView.rowtxtEquivalentGrade.setTextColor(Color.RED);
                    else itemView.rowtxtEquivalentGrade.setTextColor(Color.BLACK);

                } else {
                    itemView.rowtxtGrade.text = "DRP"
                    itemView.rowtxtEquivalentGrade.text = "DRP"
                    itemView.rowOriginalGrade.text = "DRP"
                }


                if (grade!!.FinalStatus == "OK") {
                    itemView.btnEnrolled.setVisibility(View.VISIBLE);
                    itemView.btnDropped.setVisibility(View.INVISIBLE);
                } else {
                    itemView.btnEnrolled.setVisibility(View.INVISIBLE);
                    itemView.btnDropped.setVisibility(View.VISIBLE);
                }
            }

            this.currentGrade = grade;
            this.currentPosition = pos
        }


    }


    fun CopyText(copyString: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", copyString)
        clipboardManager.setPrimaryClip(clipData)

    }


    private fun DatabaseHandler.GetMessage(sectioncode: String): String {
        var sql = """
        SELECT * FROM  TBSECTION 
         WHERE  SectionName='$sectioncode'        
    """.trimIndent()
        Log.e("ppp", sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)


        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("Message"))
        }
        return ""
    }


}