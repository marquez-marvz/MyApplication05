package com.example.myapplication05

//class PdfRecordAdapter {}
//
//package com.example.myapplication05

//class OpenPdf_Adapter {}


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.group_main.*
import kotlinx.android.synthetic.main.group_student.view.*
import kotlinx.android.synthetic.main.group_student.view.btnScoreRemark
import kotlinx.android.synthetic.main.openpdf.*
import kotlinx.android.synthetic.main.openpdf.view.*
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.pdf_row.view.*
import kotlinx.android.synthetic.main.remark_main.view.*
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.remark_main.*
import kotlinx.android.synthetic.main.remark_main.view.*
import kotlinx.android.synthetic.main.score_row.view.*


class PdfRecordAdapter(val context: Context, val scorePdf: List<ScoreIndividualModel>) :
    RecyclerView.Adapter<PdfRecordAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.pdf_record, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return scorePdf.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = scorePdf[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentActivity: ScoreIndividualModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnPdfEditScore.setOnClickListener {
                itemView.btnPdfEditScore.setVisibility(View.INVISIBLE);
                itemView.btnPdfSaveScore.setVisibility(View.VISIBLE);
                itemView.txtPdfScore.isEnabled = true

                var score = itemView.txtPdfScore.text.toString().toInt()
                if (score == 0) {
                    itemView.txtPdfScore.setText("")
                }

            }

            itemView.btnPdfEditScore.setOnClickListener {
                itemView.btnPdfEditScore.setVisibility(View.INVISIBLE);
                itemView.btnPdfSaveScore.setVisibility(View.VISIBLE);
                itemView.txtPdfScore.isEnabled = true

                var score = itemView.txtPdfScore.text.toString().toInt()
                if (score == 0) {
                    itemView.txtPdfScore.setText("")
                }

            }


            itemView.btnPdfRemark.setOnClickListener {
                val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
                val inputDialog = mBuilder.show()
                inputDialog.setCanceledOnTouchOutside(false); //
                RemarkMain.btnRemark = itemView.btnPdfRemark
                RemarkMain.alert = inputDialog
                RemarkMain.remark_section =currentActivity!!.sectioncode
                var ppp: RemarkMain = RemarkMain()
                ppp.SetButtonArray(dlgremark)
                ppp.DisplayRenark(dlgremark, context, currentActivity!!.studentNo, currentActivity!!.activitycode)

                for (i in 0..9) {
                    RemarkMain.buttons[i].setOnClickListener {
                        val rem = RemarkMain.buttons[i].text.toString()
                        itemView.btnPdfRemark.text= rem
                        inputDialog.dismiss() //UpdateStudentRemark()
                        val db: DatabaseHandler = DatabaseHandler(context)
                        db.UpdateStudentRemark(RemarkMain.remark_section, currentActivity!!.activitycode, currentActivity!!.studentNo,rem)
                        OpenNewPDF.individualList[currentPosition].remark = rem
                    }
                }
                //  ppp.DisplayRenark(dlgremark, context)

//                //                var e = currentActivity
//                //                var score = itemView.txtPdfScore.text.toString().toInt()
//                //                var remark = itemView.btnPdfRemark.text.toString()
//                                SaveScore(e, score, remark)
            }

            itemView.btnPdfRemark.setOnLongClickListener {
                itemView.btnPdfRemark.text = "-"
                val db: DatabaseHandler = DatabaseHandler(context)
                db.UpdateStudentRemark(RemarkMain.remark_section, currentActivity!!.activitycode, currentActivity!!.studentNo,"-")

                true
            }



            itemView.btnPdfSaveScore.setOnClickListener {
                var e = currentActivity
                var score = itemView.txtPdfScore.text.toString().toInt()
                var remark = itemView.btnPdfRemark.text.toString()
                SaveScore(e, score, remark)

            }


        } //init

        fun setData(pdfScores: ScoreIndividualModel?, pos: Int) {

            itemView.txtStudentNo.setText(pdfScores!!.studentNo.toString())
            itemView.txtActivityCode.setText(pdfScores!!.studentNo.toString())
            itemView.txtName.setText(pdfScores!!.lastname.toString())
            itemView.txtActivityDescription.setText(pdfScores!!.description.toString())
            itemView.txtPdfScore.setText(pdfScores!!.score.toString())
            itemView.btnPdfRemark.setText(pdfScores!!.remark)

            itemView.txtPdfScore.isEnabled = false

            if (OpenNewPDF.PDF_STATUS == "IND") itemView.txtActivityDescription.setVisibility(View.VISIBLE)
            if (OpenNewPDF.PDF_STATUS == "ACT") itemView.txtActivityDescription.setVisibility(View.INVISIBLE)
            this.currentActivity = pdfScores
            this.currentPosition = pos
        }

        fun SaveScore(e: ScoreIndividualModel?, score: Int, remark: String) {
            Log.e("123", remark)
            val section = e!!.sectioncode

            var studNumber = e!!.studentNo
            var activityCode = e!!.activitycode

            val activityItem = e.item
            if (score > activityItem.toInt()) Util.Msgbox(context, "The Score is invalid")
            else {
                var submissionStatus = ""
                if (score == 0) submissionStatus = "NO"
                else submissionStatus = "OK"
                val db: TableActivity = TableActivity(context)
                db.UpdateStudentRecord(activityCode, section, studNumber, score, remark, submissionStatus)
                itemView.btnPdfEditScore.setVisibility(View.VISIBLE);
                itemView.btnPdfSaveScore.setVisibility(View.INVISIBLE);
                itemView.txtPdfScore.isEnabled = false

                //OpenNewPDF.individualList = db.GetActivityRecord(section, activityCode, OpenNewPDF.sss_string)
                OpenNewPDF.individualList[currentPosition].score = score
                OpenNewPDF.individualList[currentPosition].remark = remark
            }
        }


    }

    fun xxx(sss: String) {

    }
}