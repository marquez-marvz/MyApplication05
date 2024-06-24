package com.example.myapplication05.testpaper

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.example.myapplication05.*
import kotlinx.android.synthetic.main.quiz_set.view.*
import kotlinx.android.synthetic.main.test_answer.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.HashMap

class TestAnswer : Fragment() {
    val PDF_SELECTION_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        viewanswer = inflater.inflate(R.layout.test_answer, container, false)
        containerGlobal = container

        var context = containerGlobal!!.context
        SetSpinnerAdapter()
        var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();

        UpdateListContent("ALL", subject, context)
        ViewRecord()
        viewanswer!!.pdfview.isVisible = false
        mode = "ANSWER MODE"


        viewanswer!!.pdfview.setMaxZoom(10.0f)


        val db2: DatabaseHandler = DatabaseHandler(context)
        var arr = db2.GetDefaultQuiz()
        if (arr.size == 0) {
            viewanswer!!.cboSubjectAnswer.setSelection(0)
        } else {
            Log.e("PPP", arr[0] + "     " + arr[1])
            var i = GetComboSelection(viewanswer!!.cboSubjectAnswer, arr[0])
            viewanswer!!.cboSubjectAnswer.setSelection(i)
        }




        viewanswer!!.cboSubjectAnswer.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    SetQuizCodeAdapter()
                    if (arr.size == 0) {
                        viewanswer!!.cboQuizCode.setSelection(0)
                    } else {
                        SetQuizCodeAdapter()
                        var i = GetComboSelection(viewanswer!!.cboQuizCode, arr[1])
                        viewanswer!!.cboQuizCode.setSelection(i)

                    }
                }
            }


        viewanswer!!.cboQuizCode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    var quizcode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
                    var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
                    UpdateListContent(quizcode, subject, context)
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    db2.SetDefaultValu(subject, quizcode)
                    Log.e("203", "Hello")
                    LoadPDF()
                    adapterAnswer!!.notifyDataSetChanged()
                }
            }

        viewanswer!!.btnOpenPDF.setOnClickListener {
            val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
            browseStorage.type = "application/pdf"
            browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(browseStorage, "Select Pdf"), PDF_SELECTION_CODE)
        }

        viewanswer!!.btnAddQuiz.setOnClickListener {
            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //


            dlgquiz.btnOK.setOnClickListener {
                val db2: DatabaseHandler = DatabaseHandler(context)
                var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
                var quizCode = dlgquiz.txtdata.text.toString()
                db2.AddNewQuiz(subject, quizCode)
                inputDialog.dismiss()
                SetQuizCodeAdapter()
            }


            dlgquiz.btnCancel.setOnClickListener {
                inputDialog.dismiss()
            }

        }

        viewanswer!!.btnQuizSet.setOnClickListener {
            var set = viewanswer!!.btnQuizSet.text.toString()
            var newSet = "'"

            if (set == "A") newSet = "B"
            else if (set == "B") newSet = "C"
            else if (set == "C") newSet = "D"
            else if (set == "D") newSet = "A"

            viewanswer!!.btnQuizSet.setText(newSet)
            var quizcode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
            var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
            UpdateListContent(quizcode, subject, context, newSet)
            adapterAnswer!!.notifyDataSetChanged()
        }

        viewanswer!!.btnAddNewSet.setOnClickListener {
            val dlgquizSet = LayoutInflater.from(context).inflate(R.layout.quiz_set, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquizSet).setTitle("")
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

                var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
                var quizCode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
                var oldset = viewanswer!!.cboQuizCode.getSelectedItem().toString();
                db2.AddNewSetAnswer(subject, quizCode, quizSet)

            }

            //
            //
            //            dlgquiz.btnCancel.setOnClickListener {
            //                inputDialog.dismiss()
            //            }
            true
        }

        viewanswer!!.btnAddAnswer.setOnClickListener {
            val db2: DatabaseHandler = DatabaseHandler(context)
            var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
            var quizCode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
            var quizSet = viewanswer!!.btnQuizSet.text.toString();
            db2.AddAnswer(subject, quizCode, quizSet)
            var quizcode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
            UpdateListContent(quizcode, subject, context)
            adapterAnswer!!.notifyDataSetChanged()
        }

        viewanswer!!.btnDeleteAllAnswer.setOnClickListener {
            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz)
                .setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {

                var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
                var quizCode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
                val db2: DatabaseHandler = DatabaseHandler(context)
                db2.DeleteAnswer(subject, quizCode)
                inputDialog.dismiss()
                SetQuizCodeAdapter()
            }
            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }
        }

        viewanswer!!.btnImportAnswer.setOnClickListener {

            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz)
                .setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //


            dlgquiz.btnYes.setOnClickListener {


                var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();

                val loading = ProgressDialog.show(context, "", "Please wait")
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
                            val db: DatabaseHandler = DatabaseHandler(context)
                        }

                        //    fun UpdateStudentAttendance(attStatus: String, studentNo: String = "", taskpoint: Int, recite: Int, remark: String = "-") {


                        while (i < obj.length()) {

                            val db2: DatabaseHandler = DatabaseHandler(context)

                            val jsonObject = obj.getJSONObject(i)

                            db2.ManageAnswer(jsonObject, arrHeader, subject)
                            i++;
                        } //w

                    }, Response.ErrorListener { }) {
                    override fun getParams(): Map<String, String> {
                        val parmas: MutableMap<String, String> = HashMap()
                        parmas["action"] = "ImportAnswer"
                        parmas["subject"] =
                            viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
                        return parmas
                    }
                }

                val socketTimeOut = 50000 // u can change this .. here it is 50 seconds
                val retryPolicy: RetryPolicy =
                    DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                stringRequest.retryPolicy = retryPolicy
                val queue = Volley.newRequestQueue(context)
                queue.add(stringRequest)
                inputDialog.dismiss()
            }

            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }

        }

        return viewanswer
    }


    private fun SetSpinnerAdapter() { //val arrSection: Array<String> =
        var context = containerGlobal!!.context
        val arrSection = arrayOf("Java", "Camhi-Web", "Data-Struct", "UNC-Web")
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewanswer!!.cboSubjectAnswer.setAdapter(sectionAdapter);
    }

    fun ViewRecord() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewanswer!!.listAnswer.layoutManager = layoutmanager
        adapterAnswer = AnswerAdapter(context, list)
        viewanswer!!.listAnswer.adapter = adapterAnswer
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

    fun SetQuizCodeAdapter() {
        var context = containerGlobal!!.context
        val db: DatabaseHandler = DatabaseHandler(context);
        var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
        var quizList = db.GetSubjectQuizlist(subject)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, quizList)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewanswer!!.cboQuizCode.setAdapter(sectionAdapter);

        UpdateListContent("ALL", subject, context)
        adapterAnswer!!.notifyDataSetChanged()
    }


    fun LoadPDF() {
        var context = containerGlobal!!.context
        var subject = viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
        var quizCode = viewanswer!!.cboQuizCode.getSelectedItem().toString();
        val db2: DatabaseHandler = DatabaseHandler(context)
        var path = db2.GetPDFPath(subject, quizCode)
        Log.e("346", path)
        var file = File(path)
        if (file.exists()) {
            viewanswer!!.pdfview.fromFile(file).load()
            Log.e("XXX", "YESSS")
        } else {
            var file = File("")
            viewanswer!!.pdfview.fromFile(file).load()
            Log.e("XXX", "NONO")

        }
    }


    fun DefaultColor(dlgquizSet: View?) {
        dlgquizSet!!.btnFirstSet.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgquizSet!!.btnSecondSet.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgquizSet!!.btnThirdSet.setBackgroundResource(android.R.drawable.btn_default); //            itemView.rowBtnAbsent.setBackgroundResource(android.R.drawable.btn_default);
        dlgquizSet!!.btnFourthSet.setBackgroundResource(android.R.drawable.btn_default);
    }

    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
        var context = containerGlobal!!.context
        super.onActivityResult(requestCode, resultcode, data)
        if (requestCode == PDF_SELECTION_CODE && resultcode == Activity.RESULT_OK && data != null) {
            val selectedPdf = data.data
            Log.e("PDF", selectedPdf.toString())
            var path1 = selectedPdf!!.path.toString().split(":")
            var path2 = "/storage/emulated/0/" + path1[1]
            Log.e("PaTH", path2)
            showPDF(selectedPdf)
            var subject =  viewanswer!!.cboSubjectAnswer.getSelectedItem().toString();
            var quizCode =  viewanswer!!.cboQuizCode.getSelectedItem().toString();
            val db2: DatabaseHandler = DatabaseHandler(context)
            db2.UpdatePDFPath(subject, quizCode, path2)
        }
    }

    fun showPDF(uri: Uri?) { //  val mypath = uri?.let { getPathFromURI(myContext, it) }
        val myFile = File(uri.toString());
        Log.e("XX", uri.toString())
        viewanswer!!.pdfview.fromUri(uri)
            .load()
    }

    companion object {
        var containerGlobal: ViewGroup? = null
        var mode = ""
        var viewanswer: View? = null
        var varlistAnswer: RecyclerView? = null
        var list = arrayListOf<AnswerNewModel>()
        var adapterAnswer: AnswerAdapter? = null;
        var studentAdapter: ArrayAdapter<String>? = null;


        fun UpdateListContent(category: String = "ALL", subject: String, context: Context, quizSet: String = "A") {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<AnswerNewModel>
            student = db2.GetAnswerList(subject, category, quizSet)
            list.clear()
            for (e in student) {
                list.add(AnswerNewModel(e.Subject, e.QuizCode, e.OrderNum, e.AnswerID, e.Number, e.Answer, e.Points, e.QuizSet))
            }
        }


    }


}


fun DatabaseHandler.AddAnswer(subject: String, QuizCode: String, quizSet: String) {
    var sql = """ SELECT  * FROM tbanswer
              where quizCode='$QuizCode'  
              and subject='$subject'  
            order by OrderNum DESC"""
    var cursor = SQLSelect(sql, 200)

    var quizID = ""

    sql = """ SELECT  * FROM tbquiz
              where quizCode='$QuizCode'  
              and subject='$subject'  
            """

    var cursor1 = SQLSelect(sql, 220, 13)
    if (cursor1!!.moveToFirst()) {
        Log.e("IDDD", quizID)
        quizID = cursor1!!.getString(cursor1!!.getColumnIndex("QuizID"))
    }
    Log.e("IDDD", quizID)

    if (cursor!!.count == 0) {
        var answerID = GetNewAnswerID(subject, QuizCode)
        sql = """
                insert into tbAnswer (Subject,	QuizCode,	OrderNum,	AnswerID,	Number, Answer,	Points, QuizSet) 
                values('$subject', '$QuizCode', '001', '$answerID',  '-', '-', '1', '$quizSet')
                  """
    } else {
        cursor!!.moveToFirst()
        var sss = cursor.getString(cursor.getColumnIndex("OrderNum"))
        Log.e("SSSNUM", sss)
        var newOrderNum =
            Util.ZeroPad(sss.toInt() + 1, 3) //    Data-Struct	QUIZ03A	37	DATS-QUIZ03A-37	3	AB+  CD% * EG*+	1/
        var answerID = GetNewAnswerID(subject, QuizCode)

        sql = """
                insert into tbAnswer (Subject,	QuizCode,	OrderNum,	AnswerID,	Number, Answer,	Points, quizSet) 
                values('$subject', '$QuizCode', '$newOrderNum', '$answerID',  '-', '-', '1', '$quizSet')
                  """
    }

    SQLManage(sql, 11)
}


fun DatabaseHandler.AddNewSetAnswer(subject: String, QuizCode: String, quizSet: String, oldset: String = "A") {


    var sql = """ SELECT  * FROM tbanswer
              where quizCode='$QuizCode'  
              and subject='$subject'  
              and QuizSet='$oldset'  
            order by OrderNum DESC"""

    var cursor = SQLSelect(sql, 245, 211)

    if (cursor!!.moveToFirst()) {
        do {

            var orderNum = cursor!!.getString(cursor!!.getColumnIndex("OrderNum"))
            var number = cursor!!.getString(cursor!!.getColumnIndex("Number"))
            var answer = cursor!!.getString(cursor!!.getColumnIndex("Answer"))
            var points = cursor!!.getString(cursor!!.getColumnIndex("Points"))
            var answerID = GetNewAnswerID(subject, QuizCode)
            Log.e("AAA", orderNum)
            sql = """
                insert into tbAnswer (Subject,	QuizCode,	OrderNum,	AnswerID,	Number, Answer,	Points, quizSet) 
                values('$subject', '$QuizCode', '$orderNum', '$answerID',  '$number', '$answer', '$points', '$quizSet')
                  """
            SQLManage(sql)
        } while (cursor!!.moveToNext())
    }

    //
    //        cursor!!.moveToFirst()
    //        var sss = cursor.getString(cursor.getColumnIndex("OrderNum"))
    //        var newOrderNum =
    //            Util.ZeroPad(sss.toInt() + 1, 3) //    Data-Struct	QUIZ03A	37	DATS-QUIZ03A-37	3	AB+  CD% * EG*+	1/
    //        var answerID = quizID + "-" + newOrderNum
    //
}


fun DatabaseHandler.GetNewAnswerID(subject: String, quizCode: String): String {
    var sql2 = """ SELECT  * FROM tbquiz
              where quizCode='$quizCode'  
              and subject='$subject'  
            """
    var quizID = ""
    var answerIDNew = ""
    var cursor1 = SQLSelect(sql2, 220, 13)
    if (cursor1!!.moveToFirst()) {
        Log.e("IDDD", quizID)
        quizID = cursor1!!.getString(cursor1!!.getColumnIndex("QuizID"))
    }
    Log.e("IDDD", quizID)

    var sql = """ SELECT  * FROM tbAnswer
              where quizCode='$quizCode'  
              and subject='$subject'  
              order by AnswerID DESC
            """
    var answerID = ""
    var cursor2 = SQLSelect(sql, 225, 110)
    if (cursor2!!.count == 0) {
        return quizID + "-" + "001"
    }
    if (cursor2!!.moveToFirst()) {
        answerID = cursor2!!.getString(cursor2!!.getColumnIndex("AnswerID"))
        var num = answerID.split("-")
        Log.e("NUUUU222", answerID + "-" + num[2])
        var newNum = num[2].toInt() + 1
        answerIDNew = Util.ZeroPad(newNum, 3)
        return quizID + "-" + answerIDNew
    }
    return ""
}


fun DatabaseHandler.AddNewQuiz(subject: String, QuizCode: String) {

    var sql = """ SELECT  * FROM tbquiz
              order by quizID Desc"""
    var quizID = ""
    var cursor1 = SQLSelect(sql, 200)
    if (cursor1!!.moveToFirst()) {
        var sss = cursor1!!.getString(cursor1!!.getColumnIndex("QuizID"))
        var num = sss.split("-")
        Log.e("NUUUU", sss + "-" + num[0])
        var newNum = num[1].toInt() + 1
        quizID = "Q-" + Util.ZeroPad(newNum, 3)
    }


    sql = """ SELECT  * FROM tbquiz
              where quizCode='$QuizCode'  
              and subject='$subject' 
              """
    var cursor = SQLSelect(sql, 200)
    Log.e("SSS11", cursor!!.count.toString()) //  Log.e("SSs", answerID.toString())
    if (cursor!!.count == 0) {
        sql = """
                insert into tbQuiz (Subject,	QuizCode,	Path	,DefaultQuiz, QuizID) 
                values('$subject', '$QuizCode',"-", "NO", '$quizID')
                  """
        val db = this.writableDatabase
        db.execSQL(sql)
    }
}

fun DatabaseHandler.ManageAnswer(jsonObject: JSONObject, arrHeader: ArrayList<String>, subject: String) {
    var quizCode = jsonObject.getString(arrHeader[0])
    var orderNum = jsonObject.getString(arrHeader[1])
    var answerID = jsonObject.getString(arrHeader[2])
    var number = jsonObject.getString(arrHeader[3])
    var answer = jsonObject.getString(arrHeader[4])
    var points = jsonObject.getString(arrHeader[5])




    Log.e("ANS", quizCode + "   " + answer)

    // Subject	QuizCode	OrderNum	AnswerID	Answer	Points
    var sql = """
            SELECT * from tbanswer
            WHERE AnswerID ='$answerID'
    """.trimIndent()
    Log.e("SQLSS", sql)

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.count == 0) {
        var sql = """
                insert into tbAnswer (Subject,	QuizCode,	OrderNum,	AnswerID,	Number, Answer,	Points) 
                values('$subject', '$quizCode', '$orderNum', '$answerID',  '$number', '$answer', '$points')
                  """
        val db = this.writableDatabase
        db.execSQL(sql)
    } else {

        var sql = """
        update tbAnswer 
        set answer ='$answer'
        , Points ='$points'
        where AnswerID ='$answerID'
        """.trimIndent()
        val db = this.writableDatabase
        db.execSQL(sql)
        Log.e("SQLSS", sql)

    }


}

fun DatabaseHandler.GetAnswerList(subject: String, quizCode: String = "ALL", quizSet: String = "A"): ArrayList<AnswerNewModel> {

    val answerList: ArrayList<AnswerNewModel> = ArrayList<AnswerNewModel>()

    var sql: String = ""



    if (quizCode == "ALL") {
        sql = """ SELECT  * FROM tbanswer    where  subject='$subject'    order by answerID """
    } else {
        sql = """ SELECT  * FROM tbanswer
              where quizCode='$quizCode'  
              and subject='$subject'  
                and quizset='$quizSet' 
            order by answerID """.trimMargin()
        var ppp = "" //        //var sql: String = """ SELECT  * FROM tbstudent_query //
    }
    var cursor = SQLSelect(sql, 453, 786)

    var num = 1;
    if (cursor!!.moveToFirst()) {
        do { // Subject	QuizCode	OrderNum	AnswerID	Answer	Points
            var subject = cursor!!.getString(cursor!!.getColumnIndex("Subject"))
            var quizCode = cursor!!.getString(cursor!!.getColumnIndex("QuizCode"))
            var orderNum = cursor!!.getString(cursor!!.getColumnIndex("OrderNum"))
            var answerID = cursor!!.getString(cursor!!.getColumnIndex("AnswerID"))
            var number = cursor!!.getString(cursor!!.getColumnIndex("Number"))
            var answer = cursor!!.getString(cursor!!.getColumnIndex("Answer"))
            var points = cursor!!.getString(cursor!!.getColumnIndex("Points"))
            var set = cursor!!.getString(cursor!!.getColumnIndex("QuizSet"))
            val emp =
                AnswerNewModel(subject, quizCode, orderNum, answerID, number, answer, points, set)
            num++;
            answerList.add(emp)
        } while (cursor.moveToNext())
    }
    return answerList
}


fun DatabaseHandler.DeleteAnswer(subject: String, quizCode: String) {
    var sql = """ DELETE FROM tbanswer
              where quizCode='$quizCode'  
              and subject='$subject'  
              """
    SQLManage(sql)

    sql = """ DELETE FROM tbquiz
              where quizCode='$quizCode'  
              and subject='$subject'  
              """
    SQLManage(sql)
}


fun DatabaseHandler.SetDefaultValu(subject: String, quizCode: String) {
    var sql = ""
    if (quizCode != "Select") {
        sql = """ UPDATE tbquiz
              set  DefaultQuiz='NO'  
              """
        SQLManage(sql)

        sql = """ UPDATE  tbquiz
              set  DefaultQuiz='YES'  
                where quizCode='$quizCode'  
              and subject='$subject' 
              """

        SQLManage(sql, 5)
    }
    var quiz = arrayListOf<String>()
    sql = """ SELECT * from  tbquiz
              where DefaultQuiz='YES'  
              """
    var cursor = SQLSelect(sql, 5)
    Log.e("SSS", cursor!!.count.toString())
}


fun DatabaseHandler.GetDefaultQuiz(): ArrayList<String> {

    var quiz = arrayListOf<String>()
    var sql = """ SELECT * from  tbquiz
              where DefaultQuiz='YES'  
              """
    var cursor = SQLSelect(sql, 5)
    Log.e("SSS200", cursor!!.count.toString())
    if (cursor!!.moveToFirst()) {
        Log.e("SSS230", cursor!!.count.toString())
        var subject = cursor.getString(cursor.getColumnIndex("Subject"))
        var quizCode = cursor.getString(cursor.getColumnIndex("QuizCode"))
        quiz.add(subject)
        quiz.add(quizCode)
    }
    return quiz
}


fun DatabaseHandler.UpdatePDFPath(subject: String, quizCode: String, path: String) {
    var sql = ""
    if (quizCode != "Select") {
        sql = """ UPDATE  tbquiz
              set  Path='$path'  
                where quizCode='$quizCode'  
              and subject='$subject' 
              """
        SQLManage(sql, 5)
    }

}


fun DatabaseHandler.GetPDFPath(subject: String, quizCode: String): String {
    var quiz = arrayListOf<String>()
    var sql = """ SELECT * from  tbquiz
                where quizCode='$quizCode'  
              and subject='$subject' 
              """
    var cursor = SQLSelect(sql, 5)
    if (cursor!!.moveToFirst()) {
        var path = cursor.getString(cursor.getColumnIndex("Path"))
        return path
    }
    return "-"

}




