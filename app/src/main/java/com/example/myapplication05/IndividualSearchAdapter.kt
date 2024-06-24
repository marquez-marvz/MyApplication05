


package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.testpaper.TestCapture
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.grade_computation.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.individual_student_list.view.btnStudentList
import kotlinx.android.synthetic.main.individusl_student.btnActivityOrder
import kotlinx.android.synthetic.main.individusl_student.imgStudentPicture
import kotlinx.android.synthetic.main.individusl_student.listIndividualStudent
import kotlinx.android.synthetic.main.individusl_student.txtIndividualStudentNumber
import kotlinx.android.synthetic.main.individusl_student.txtStudentNanme
import kotlinx.android.synthetic.main.score_csv.view.btnCsvRemark
import kotlinx.android.synthetic.main.score_csv.view.txtCsvName
import kotlinx.android.synthetic.main.score_summary_studentrow.view.Score2
import kotlinx.android.synthetic.main.scoresummary.btnActivity1
import kotlinx.android.synthetic.main.scoresummary_activityrow.view.btnActivityDesc
import kotlinx.android.synthetic.main.student_import.view.*
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_row.view.*

import kotlinx.android.synthetic.main.test_capture.view.imgStudent
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class IndividualSearchAdapter(val context: Context, val student: List<EnrolleModel>) :
    RecyclerView.Adapter<IndividualSearchAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView =
            LayoutInflater.from(context).inflate(R.layout.individual_student_list, parent, false)
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

            itemView.btnStudentList.setOnClickListener { //                    val intent = Intent(this,  IndividualStudenht::class.java)
                val db: DatabaseHandler =
                    DatabaseHandler(context) //                ShowIndividualRecord( db.GetCurrentSection(), listALL[0].studentno)
                var e = currentStudenht
                Util.GRADE_NAME = currentStudenht!!.lastname + "," + currentStudenht!!.firstname
                Util.GRADE_STUD_NO = currentStudenht!!.studentno
                IndividualStudenht.vartxtName!!.text = Util.GRADE_NAME
                IndividualStudenht.vartxtSection!!.text = e!!.Section
                IndividualStudenht.vartxtStudNum!!.text = e!!.studentno
                val db2: TableActivity = TableActivity(context)
                val orderActivity = "CODE"
                IndividualStudenht.UpdateListIndividualContent(context, e!!.Section, e!!.studentno)

                ShowPicture(e!!.lastname + "," + e!!.firstname, e!!.studentno)
                IndividualStudenht.individualAdapter!!.notifyDataSetChanged()


                //0618
                IndividualStudenht.UpdateListContentIndividualAttendance(context, "MONTH",  e!!.studentno )
                IndividualStudenht.adapterIndividual!!.notifyDataSetChanged()

                //            cboStudent.setSelection(1)
            }


        } //init

        fun setData(myactivity: EnrolleModel?, pos: Int) {
            itemView.btnStudentList.text = myactivity!!.lastname + "," + myactivity!!.firstname
            this.currentStudenht = myactivity
            this.currentPosition = pos

        }

    }


    //0617
    fun ShowPicture(studName: String, studNumber: String) {

        try {

            val db2: DatabaseHandler = DatabaseHandler(context)
            var origSection = db2.GetStudentOriginalSection(studNumber) //
            Log.e("5685", origSection) //      var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
            val sdCard =
                Environment.getExternalStorageDirectory() //        val section = TestCapture.viewcapture!!.cboSectionPic.getSelectedItem().toString();
            val path = sdCard.absolutePath + "/Quiz/StudentPicture/" + origSection

            val f: File = File(path, studName + ".jpg")

            if (f.exists()) {
                val f: File = File(path, studName + ".jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                IndividualStudenht.varPicture!!.setImageBitmap(b)
            } else { // TestCapture.viewcapture!!.imgStudent.setImageBitmap(null)
                val f: File = File(sdCard.absolutePath + "/Quiz/StudentPicture/", "nopic.png")
                val b = BitmapFactory.decodeStream(FileInputStream(f))

                IndividualStudenht.varPicture!!.setImageBitmap(b)
            }


            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}