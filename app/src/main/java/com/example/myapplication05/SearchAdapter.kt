package com.example.myapplication05



import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.LogActivity.Companion.logList
import kotlinx.android.synthetic.main.openpdf.*
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.search_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.lang.Exception


class SearchAdapter(val context: Context, val student: List<StudentModel>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    var myIndividualAdapter: ScoreIndividualAdapter? = null;


    companion object{
        var individualList = arrayListOf<ScoreIndividualModel>()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.search_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return student.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = student[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentStudent: StudentModel? = null
        var currentPosition: Int = 0
        val myGrades: Grades = Grades(context)

        init {
            itemView.btnIndividual.setOnClickListener {
                Log.e("XXX123", "Hello")
                val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
                try {
                    val studentNumber = currentStudent!!.studentno
                    val completeName   = currentStudent!!.lastname
                    val section =currentStudent!!.sectioncode

                    //                    val firstName = currentGrade!!.firstname
                    //                    val lastName = currentGrade!!.lastname
                    //                    val section = currentGrade!!.sectioncode

                    // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();

                    val dlgStudent =
                        LayoutInflater.from(context).inflate(R.layout.score_individual, null)
                    val mBuilder = android.app.AlertDialog.Builder(context).setView(dlgStudent)
                        .setTitle(completeName)

                    val confirmDialog = mBuilder.show()
                    confirmDialog.setCanceledOnTouchOutside(false);
                    confirmDialog.getWindow()!!
                        .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                    confirmDialog.setCanceledOnTouchOutside(false);
//                    dlgStudent.txtStudentNo.setText(studentNumber)
//                    dlgStudent.txtSection.setText(section)
                    val currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
                    val f = context
                    // val db2: Grades = Grades(context)
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


                    bar = dlgStudent

                    //                val myGrades: Grades = Grades(context)
                    //                var gr = myGrades.GetStudentGrades(section, studentNumber, "FIRST")
                    //                dlgStudent.txtGrade.setText(gr.toString())
                    //                Util.txtgrade = dlgStudent.findViewById(R.id.txtGrade) as EditText


                    GradeAdapter.UpdateListContent(context, studentNumber, section)
                    val layoutmanager = LinearLayoutManager(context)
                    layoutmanager.orientation = LinearLayoutManager.VERTICAL;
                    dlgStudent.listIndividualScore.layoutManager = layoutmanager
                    GradeAdapter.myIndividualAdapter = ScoreIndividualAdapter(context, GradeAdapter.individualList)
                    dlgStudent.listIndividualScore.adapter = GradeAdapter.myIndividualAdapter //                dlgStudent.txtFirstName.setText(firstName)
                    Log.e("hello124", "OK")






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
                    //                            //       );
                    //                        }
                    //                    })




                }
                catch(e:Exception)
                {
                    Log.e("hello123", e.toString())
                }

            }










        }


            fun setData(student: StudentModel?, pos: Int) {
            val db: TableRandom= TableRandom(context)
//            itemView.txtStudentNo.text = student!!.studentno
//            itemView.txtFirstName.text = student!!.firstname
//            itemView.txtLastName.text = student!!.lastname
//            itemView.txtSection.text = student!!.sectioncode
//            this.currentStudent = student;
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
            individualList.add(ScoreIndividualModel(e.activitycode, e.sectioncode, e.studentNo, e.description, e.score, e.remark,  e.status,e.item,e.gradingperiod, e.category , e.adjustedScore, e.submissionStatus))
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