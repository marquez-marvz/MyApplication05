package com.example.myapplication05

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*




class GradeAverageAdapter(val context: Context, val myGrade: List<GradeModel>) :
    RecyclerView.Adapter<GradeAverageAdapter.MyViewHolder>() {

    var myIndividualAdapter: ScoreIndividualAdapter? = null;


    companion object{
        var individualList = arrayListOf<ScoreIndividualModel>()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.grade_average, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return myGrade.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = myGrade[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentGrade: GradeModel? = null
        var currentPosition: Int = 0

        init {
            itemView.rowBtnScoreIndividual.setOnClickListener{
                try {
                    val studentNumber = currentGrade!!.studentNo
                    val firstName =  currentGrade!!.firstname
                    val lastName = currentGrade!!.lastname
                    val section = currentGrade!!.sectioncode

                    // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();

                    val dlgStudent =
                        LayoutInflater.from(context).inflate(R.layout.score_individual, null)
                    val mBuilder =
                        AlertDialog.Builder(context).setView(dlgStudent).setTitle(lastName + "  "  + firstName)

                    val confirmDialog = mBuilder.show()
                    confirmDialog.setCanceledOnTouchOutside(false);
                    confirmDialog.getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                    confirmDialog.setCanceledOnTouchOutside(false);
                    dlgStudent.txtStudentNo.setText(studentNumber)
                    dlgStudent.txtSection.setText(section)
                    val f = currentGrade
                    val db2: Grades = Grades(context)
                    dlgStudent.txtOriginslGrade.setText(db2.GetStudentTermGrade(f!!.sectioncode,  f!!.studentNo, "FIRST", "ORIGINAL").toString())
                    dlgStudent.txtAdjustedGrade.setText(db2.GetStudentTermGrade(f!!.sectioncode,  f!!.studentNo, "FIRST", "ADJUSTED").toString())
                    dlgStudent.txtAdjustedGradeDecimal.setText(db2.GetStudentTermGrade(f!!.sectioncode,  f!!.studentNo, "FIRST", "DECIMAL").toString())

                    bar = dlgStudent

                    //                val myGrades: Grades = Grades(context)
                    //                var gr = myGrades.GetStudentGrades(section, studentNumber, "FIRST")
                    //                dlgStudent.txtGrade.setText(gr.toString())
                    //                Util.txtgrade = dlgStudent.findViewById(R.id.txtGrade) as EditText

                    UpdateListContent(context,studentNumber, section )
                    val layoutmanager = LinearLayoutManager(context)
                    layoutmanager.orientation = LinearLayoutManager.VERTICAL;
                    dlgStudent.listIndividualScore.layoutManager = layoutmanager
                    myIndividualAdapter = ScoreIndividualAdapter(context, individualList)
                    dlgStudent.listIndividualScore.adapter = myIndividualAdapter
                    //                dlgStudent.txtFirstName.setText(firstName)
                    Log.e("hello124", "OK")
                }
                catch(e:Exception)
                {
                    Log.e("hello123", e.toString())
                }
            }






        } //init


        fun setData(grade: GradeModel?, pos: Int) {
            val db: TableRandom= TableRandom(context)
            itemView.rowtxtLastName.text = grade!!.lastname
            itemView.rowtxtFirstName.text =  grade!!.firstname
            val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
            if (currentGradingPeriod=="FIRST") {
                itemView.rowtxtGrade.text = grade!!.firstGrade.toString()
                itemView.rowtxtEquivalentGrade.text = grade!!.firstEquivalent.toString()
                itemView.rowOriginalGrade.text = grade!!.firstOriginalGrade.toString()
            }
            else if (currentGradingPeriod=="SECOND") {
                itemView.rowtxtGrade.text = grade!!.secondGrade.toString()
                itemView.rowtxtEquivalentGrade.text = grade!!.secondEquivalent.toString()
                itemView.rowOriginalGrade.text = grade!!.secondOriginalGrade.toString()
            }
            this.currentGrade = grade;
            this.currentPosition = pos
        }



    }


    private fun UpdateListContent(context: Context, studentNumber:String, section:String) {
        Log.e("zzz", "Hello po")
        val dbactivity: TableActivity = TableActivity(context)
        val theinidividual: List<ScoreIndividualModel>
        individualList.clear()
        theinidividual = dbactivity.GetIndividualActivity(section, studentNumber)
        Log.e("zzz", theinidividual.size.toString())

        for (e in theinidividual) {
            individualList.add(ScoreIndividualModel(e.activitycode, e.sectioncode, e.studentNo, e.description, e.score, e.remark, e.status, e.item, e.gradingperiod, e.category, e.adjustedScore , e.submissionStatus  ))
            //            var activitycode: String,
            //            var sectioncode: String,
            //            var studentNo:String,
            //            var description: String,
            //            var score: Int,
            //            var remark: String,
            //            var status: String,
            //            var item:Int
            //            )
        }

    }




}