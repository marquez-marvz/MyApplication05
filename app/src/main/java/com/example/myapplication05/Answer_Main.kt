//package com.example.myapplication05
//
//class Answer_Main {}

package com.example.myapplication05

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.example.myapplication05.testpaper.*
import com.github.barteksc.pdfviewer.PDFView
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.addquiz.view.*
import kotlinx.android.synthetic.main.answer_main.*
import kotlinx.android.synthetic.main.answer_main.pdfview
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.openpdf.*
import kotlinx.android.synthetic.main.quiz_set.view.*
import kotlinx.android.synthetic.main.util_confirm.view.* //import kotlinx.android.synthetic.main.util_folder_shared.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class Answer_Main : AppCompatActivity() {
    val db: DatabaseHandler = DatabaseHandler(this);
    val db1: TableActivity = TableActivity(this);
    var adapterAnswer: Answer_Main_Adapter? = null;
    var studentAdapter: ArrayAdapter<String>? = null;


    var context = this
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    val myContext: Context = this;
    val PDF_SELECTION_CODE = 100

    companion object {
        var mode = ""

        var varlistAnswer: RecyclerView? = null
        var varpdfView: PDFView? = null
        var list = arrayListOf<AnswerNewModel>()

        fun UpdateListContent(category: String = "ALL", subject:String,  context: Context, quizSet: String = "A",) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<AnswerNewModel>
            student = db2.GetAnswerList(subject, category, quizSet)
            list.clear()
            for (e in student) {
                list.add(AnswerNewModel(e.Subject, e.QuizCode, e.OrderNum, e.AnswerID, e.Number, e.Answer, e.Points, e.QuizSet))
            }
        }

        //        btnAnswerMainNumber.isVisible =true
        //        txtAnswerMain.isVisible =true
        //        btnAnswerEdit.isVisible =true
        //        btnAnswerPoint.isVisible =true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.answer_main)
        SetSpinnerAdapter()
        var subject = cboSubjectAnswer.getSelectedItem().toString();
        UpdateListContent("ALL", subject, this)
        ViewRecord()
        pdfview.isVisible = false
        mode = "ANSWER MODE"


        pdfview.setMaxZoom(10.0f)

        varlistAnswer = findViewById(R.id.listAnswer) as RecyclerView
        varpdfView = findViewById(R.id.pdfview) as PDFView


        val db2: DatabaseHandler = DatabaseHandler(context)
        var arr = db2.GetDefaultQuiz()
        if (arr.size == 0) {
            cboSubjectAnswer.setSelection(0)
        } else {
            Log.e("PPP", arr[0] + "     " + arr[1])
            var i = GetComboSelection(cboSubjectAnswer, arr[0])
            cboSubjectAnswer.setSelection(i)
        }






        cboSubjectAnswer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SetQuizCodeAdapter()
                if (arr.size == 0) {
                    cboQuizCode.setSelection(0)
                } else {
                    SetQuizCodeAdapter()
                    var i = GetComboSelection(cboQuizCode, arr[1])
                    cboQuizCode.setSelection(i)

                }
            }
        }

        cboQuizCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var quizcode = cboQuizCode.getSelectedItem().toString();
                var subject = cboSubjectAnswer.getSelectedItem().toString();
                UpdateListContent(quizcode, subject, context)
                val db2: DatabaseHandler = DatabaseHandler(context)

                db2.SetDefaultValu(subject, quizcode)
                LoadPDF()
                adapterAnswer!!.notifyDataSetChanged()
            }
        }

        btnOpenPDF.setOnClickListener {
            Toast.makeText(this, "Select PDF File", Toast.LENGTH_SHORT).show()
            val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
            browseStorage.type = "application/pdf"
            browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(browseStorage, "Select Pdf"), PDF_SELECTION_CODE)
        }

        btnAddQuiz.setOnClickListener {
            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_inputbox, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgquiz).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //


            dlgquiz.btnOK.setOnClickListener {
                val db2: DatabaseHandler = DatabaseHandler(this)
                var subject = cboSubjectAnswer.getSelectedItem().toString();
                var quizCode = dlgquiz.txtdata.text.toString()
                db2.AddNewQuiz(subject, quizCode)
                inputDialog.dismiss()
                SetQuizCodeAdapter()
            }


            dlgquiz.btnCancel.setOnClickListener {
                inputDialog.dismiss()
            }

        }

        btnQuizSet.setOnClickListener {
            var set = btnQuizSet.text.toString()
            var newSet = "'"

            if (set == "A") newSet = "B"
            else if (set == "B") newSet = "C"
            else if (set == "C") newSet = "D"
            else if (set == "D") newSet = "A"

            btnQuizSet.setText(newSet)
            var quizcode = cboQuizCode.getSelectedItem().toString();
            var subject = cboSubjectAnswer.getSelectedItem().toString();
            UpdateListContent(quizcode,subject ,  context, newSet)
            adapterAnswer!!.notifyDataSetChanged()
        }

        btnAddNewSet.setOnClickListener {
            val dlgquizSet = LayoutInflater.from(this).inflate(R.layout.quiz_set, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgquizSet).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //
            var quizSet = ""


            dlgquizSet.btnFirstSet.setOnClickListener {
                DefaultColor(dlgquizSet)
                dlgquizSet.btnFirstSet.setBackgroundColor(Color.parseColor("#64B5F6"))
                quizSet = "B"

            }

            dlgquizSet.btnSecondSet.setOnClickListener {
                DefaultColor(dlgquizSet)
                dlgquizSet.btnSecondSet.setBackgroundColor(Color.parseColor("#64B5F6"))
                quizSet = "C"
            }

            dlgquizSet.btnCreateNewSet.setOnClickListener {

                    var subject = cboSubjectAnswer.getSelectedItem().toString();
                var quizCode = cboQuizCode.getSelectedItem().toString();
                var oldset  = cboQuizCode.getSelectedItem().toString();
                    db2.AddNewSetAnswer(subject, quizCode, quizSet)

            }

            //
            //
            //            dlgquiz.btnCancel.setOnClickListener {
            //                inputDialog.dismiss()
            //            }
            true
        }

        btnAddAnswer.setOnClickListener {
            val db2: DatabaseHandler = DatabaseHandler(this)
            var subject = cboSubjectAnswer.getSelectedItem().toString();
            var quizCode = cboQuizCode.getSelectedItem().toString();
            var quizSet = btnQuizSet.text.toString();
            db2.AddAnswer(subject, quizCode, quizSet)
            var quizcode = cboQuizCode.getSelectedItem().toString();

            UpdateListContent(quizcode, subject, this)
            adapterAnswer!!.notifyDataSetChanged()
        }




        btnDeleteAllAnswer.setOnClickListener {
            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgquiz).setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {

                var subject = cboSubjectAnswer.getSelectedItem().toString();
                var quizCode = cboQuizCode.getSelectedItem().toString();
                val db2: DatabaseHandler = DatabaseHandler(this)
                db2.DeleteAnswer(subject, quizCode)
                inputDialog.dismiss()
                SetQuizCodeAdapter()
            }
            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }
        }

        btnImportAnswer.setOnClickListener {

            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgquiz).setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //


            dlgquiz.btnYes.setOnClickListener {


                var subject = cboSubjectAnswer.getSelectedItem().toString();

                val loading = ProgressDialog.show(this, "", "Please wait")
                var url =
                    "https://script.google.com/a/macros/deped.gov.ph/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec";
                val stringRequest: StringRequest = @RequiresApi(Build.VERSION_CODES.O) object :
                    StringRequest(Method.POST, url, Response.Listener { response ->
                        loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()

                        var obj = JSONArray(response);

                        var i = 0;
                        var ctr = 0;
                        var x = 0;
                        var myScore = 0
                        var status = ""
                        val arrHeader = ArrayList<String>()
                        val ppp = obj.getJSONObject(0)
                        val iterator: Iterator<String> = ppp.keys();

                        while (iterator.hasNext()) {
                            var header = iterator.next()
                            arrHeader.add(header)
                            Log.e("SSS", ctr.toString() + "   " + header)
                            val db: DatabaseHandler = DatabaseHandler(this)
                        }

                        //    fun UpdateStudentAttendance(attStatus: String, studentNo: String = "", taskpoint: Int, recite: Int, remark: String = "-") {


                        while (i < obj.length()) {


                            val jsonObject = obj.getJSONObject(i)

                            db.ManageAnswer(jsonObject, arrHeader, subject)
                            i++;
                        } //w

                    }, Response.ErrorListener { }) {
                    override fun getParams(): Map<String, String> {
                        val parmas: MutableMap<String, String> = HashMap()
                        parmas["action"] = "ImportAnswer"
                        parmas["subject"] = cboSubjectAnswer.getSelectedItem().toString();
                        return parmas
                    }
                }

                val socketTimeOut = 50000 // u can change this .. here it is 50 seconds
                val retryPolicy: RetryPolicy =
                    DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                stringRequest.retryPolicy = retryPolicy
                val queue = Volley.newRequestQueue(this)
                queue.add(stringRequest)
                inputDialog.dismiss()
            }

            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }

        }
    }

    private fun SetSpinnerAdapter() { //val arrSection: Array<String> =
        val arrSection = arrayOf("Java", "Camhi-Web", "Data-Struct", "UNC-Web")
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSubjectAnswer.setAdapter(sectionAdapter);
    }

    fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listAnswer.layoutManager = layoutmanager
        adapterAnswer = Answer_Main_Adapter(this, list)
        listAnswer.adapter = adapterAnswer
    }



    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultcode, data)
        if (requestCode == PDF_SELECTION_CODE && resultcode == Activity.RESULT_OK && data != null) {
            val selectedPdf = data.data
            Log.e("PDF", selectedPdf.toString())
            var path1 = selectedPdf!!.path.toString().split(":")
            var path2 = "/storage/emulated/0/" + path1[1]
            Log.e("PaTH", path2)
            showPDF(selectedPdf)
            var subject = cboSubjectAnswer.getSelectedItem().toString();
            var quizCode = cboQuizCode.getSelectedItem().toString();
            val db2: DatabaseHandler = DatabaseHandler(this)
            db2.UpdatePDFPath(subject, quizCode, path2)


        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun showPDF(uri: Uri?) { //  val mypath = uri?.let { getPathFromURI(myContext, it) }
        val myFile = File(uri.toString());
        Log.e("XX", uri.toString())
        pdfview.fromUri(uri)
            .load()
    }

    fun SetQuizCodeAdapter() {
        var subject = cboSubjectAnswer.getSelectedItem().toString();
        var quizList = db.GetSubjectQuizlist(subject)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, quizList)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboQuizCode.setAdapter(sectionAdapter);

        UpdateListContent("ALL", subject, context)
        adapterAnswer!!.notifyDataSetChanged()
    }

    fun LoadPDF() {
        var subject = cboSubjectAnswer.getSelectedItem().toString();
        var quizCode = cboQuizCode.getSelectedItem().toString();
        val db2: DatabaseHandler = DatabaseHandler(this)
        var path = db2.GetPDFPath(subject, quizCode)

        var file = File(path)
        if (file.exists()) {
            pdfview.fromFile(file).load()
            Log.e("XXX", "YESSS")
        } else {
            var file = File("")
            pdfview.fromFile(file).load()
            Log.e("XXX", "NONO")

        }
    }


    fun GetComboSelection(cbo: Spinner, value: String): Int {
        var adapter = cbo.adapter
        val n = adapter.count
        var i = 0
        while (i < n) {
            Log.e("SSS", adapter.getItem(i).toString() + "   " + value)
            if (adapter.getItem(i).toString() == value) {
                Log.e("SSS100", adapter.getItem(i).toString() + "   " + value)
                return i
                break
            }
            i++;

        }
        return 0;
    }

    fun DefaultColor(dlgquizSet: View?) {
        dlgquizSet!!.btnFirstSet.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgquizSet!!.btnSecondSet.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgquizSet!!.btnThirdSet.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgquizSet!!.btnFourthSet.setBackgroundResource(android.R.drawable.btn_default);
    }


}
