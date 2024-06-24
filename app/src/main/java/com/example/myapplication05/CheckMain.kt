//package com.example.myapplication05
//
//class CheckMain {}

//package com.example.myapplication05
//
//class Answer_Main {}

package com.example.myapplication05

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.example.myapplication05.testpaper.CheckAdapter
import com.example.myapplication05.testpaper.GetAnswerList
import com.example.myapplication05.testpaper.TestCheck
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.answer_main.*
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.check_main.*
import kotlinx.android.synthetic.main.check_main.cboQuizCode
import kotlinx.android.synthetic.main.check_main.cboSubjectAnswer
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.paperpic.*
import kotlinx.android.synthetic.main.speak_row.*
import kotlinx.android.synthetic.main.test_check.view.*
import kotlinx.android.synthetic.main.util_folder_shared.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckMain : AppCompatActivity() {
    val db: DatabaseHandler = DatabaseHandler(this);
    val db1: TableActivity = TableActivity(this);
    var adapterAnswer: CheckAdapter? = null;
    var adapterRecordScore: CheckRecordAdapter? = null;
    var studentAdapter: ArrayAdapter<String>? = null;
    var list = arrayListOf<AnswerNewModel>()
    var scorelist = arrayListOf<ScoreModel>()
    var studentlist = arrayListOf<EnrolleModel>()
    var context = this
    var STUD_INDEX = 0;


    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    val myContext: Context = this;

    companion object {
        var cboStudent1: Spinner? = null;
        var vartxtCheckSection: TextView? = null;
        var vartxtLongAnswer: TextView? = null;
        var varbtnTotalScore: Button? = null;
        var varcboQuizCode: Spinner? = null
        var activityList = arrayListOf<ActivityModel>()
        var activityDesc = ""

        fun GetActivityCode(): String {

            var activityCode = ""
            for (e in activityList) {
                if (activityDesc == e.description) {
                    activityCode = e.activityCode
                    break;
                }
            }
            return activityCode
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_main)
        SetSpinnerAdapter()
        var subject =
            cboSubjectAnswer.getSelectedItem().toString(); //        UpdateListContent("ALL")
        //        ViewRecord()
        var currentSection = db.GetCurrentSection();
        txtCheckSection.setText(currentSection)
        LoadActivity(currentSection)
        imgStudent.setMaxZoom(10.0f)
        cboStudent1 = findViewById(R.id.cboStudentNane) as Spinner
        vartxtCheckSection = findViewById(R.id.txtCheckSection) as TextView
        vartxtLongAnswer = findViewById(R.id.txtLongAnswer) as TextView
        varbtnTotalScore = findViewById(R.id.btnTotalScore) as Button
        varcboQuizCode = findViewById(R.id.cboQuizCode) as Spinner



        LoadStudents("LAST_ORDER")


        //**************************************************************************

        cboSubjectAnswer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var subject = cboSubjectAnswer.getSelectedItem().toString();
                var quizList = db.GetSubjectQuizlist(subject)
                var sectionAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(context, R.layout.util_spinner, quizList)
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cboQuizCode.setAdapter(sectionAdapter);
            }
        }

        cboQuizCode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ShowListView()
                ViewRecord()

            }
        }

        cboActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activityDesc = cboActivity.getSelectedItem().toString();
                GetScoreRecord()
                SetUpAdapter()
            }
        }


        cboStudentNane.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("POS", position.toString())
                STUD_INDEX = position
                var name = cboStudentNane.getSelectedItem().toString()
                if (name != "Select") {
                    var subject = cboSubjectAnswer.getSelectedItem().toString();
                    var quizcode = cboQuizCode.getSelectedItem().toString();
                    var section = txtCheckSection.text.toString()
                    var keyword = btnKeyWord.text.toString()
                    var set = btnSet.text.toString()
                    ShowPicture(name, section, keyword)
                    db.SaveStudentAnswer(name, quizcode, subject, section, context, set)
                    adapterAnswer!!.notifyDataSetChanged()
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var studNum = db2.GetStudentNumber(name, section)
                    var num = db2.SumofScoreQuiz(quizcode, studNum)
                    btnTotalScore.setText(num.toString())

                    var name = cboStudentNane.getSelectedItem().toString();
                    btnCheckPage.setText("PAGE 1")
                    ShowPicture(name, section, keyword)
                    CheckScoreRecorded(studNum, section)
                }
            }
        }

        btnNextStudent.setOnClickListener {
            Log.e("III", STUD_INDEX.toString())
            Log.e("III", cboStudentNane.adapter.count.toString())
            Log.e("III", cboStudentNane.getSelectedItem().toString())
            if (STUD_INDEX < cboStudentNane.adapter.count - 1) {
                STUD_INDEX++
                cboStudentNane.setSelection(STUD_INDEX++)
                var name = cboStudentNane.getSelectedItem().toString();
                btnCheckPage.setText("PAGE 1")
                var section = txtCheckSection.text.toString()
                var keyword = btnKeyWord.text.toString()
                ShowPicture(name, section, keyword)
            }
        }

        btnPrevStudent.setOnClickListener {
            if (STUD_INDEX > 0) {
                STUD_INDEX--;
                cboStudentNane.setSelection(STUD_INDEX++)
                var name = cboStudentNane.getSelectedItem().toString();
                btnCheckPage.setText("PAGE 1")
                var section = txtCheckSection.text.toString()
                var keyword = btnKeyWord.text.toString()
                ShowPicture(name, section, keyword)
            }
        }

        btnCheckPage.setOnLongClickListener {
            var subject = cboSubjectAnswer.getSelectedItem().toString();
            var quizcode = cboQuizCode.getSelectedItem().toString();
            var section = txtCheckSection.text.toString()
            var keyword = btnKeyWord.text.toString()
            var name = cboStudentNane.getSelectedItem().toString();
            btnCheckPage.setText("PAGE 1")
            ShowPicture(name, section, keyword)
            true
        }
        btnCheckPage.setOnClickListener {
            var subject = cboSubjectAnswer.getSelectedItem().toString();
            var quizcode = cboQuizCode.getSelectedItem().toString();
            var section = txtCheckSection.text.toString()
            var keyword = btnKeyWord.text.toString()
            var name = cboStudentNane.getSelectedItem().toString();
            if (btnCheckPage.text.toString() == "PAGE 1") {
                btnCheckPage.setText("PAGE 2")
                ShowPicture(name, section, keyword, "P2")
            } else if (btnCheckPage.text.toString() == "PAGE 2") {
                btnCheckPage.setText("PAGE 3")
                ShowPicture(name, section, keyword, "P3")
            } else if (btnCheckPage.text.toString() == "PAGE 3") {
                btnCheckPage.setText("PAGE 4")
                ShowPicture(name, section, keyword, "P4")
            }
        }

        btnSet.setOnClickListener {
            var set = btnSet.text.toString()
            var newSet = "'"

            if (set == "A") newSet = "B"
            else if (set == "B") newSet = "C"
            else if (set == "C") newSet = "D"
            else if (set == "D") newSet = "A"

            btnSet.setText(newSet)
            var quizcode = cboQuizCode.getSelectedItem()
                .toString(); //  UpdateListContent(quizcode, context, newSet)
            adapterAnswer!!.notifyDataSetChanged()
            ShowListView()
            var subject = cboSubjectAnswer.getSelectedItem().toString();

            var section = txtCheckSection.text.toString()
            var keyword = btnKeyWord.text.toString()
            var name = cboStudentNane.getSelectedItem().toString()
            ShowPicture(name, section, keyword)
            db.SaveStudentAnswer(name, quizcode, subject, section, context, newSet)

        }

        btnKeyWord.setOnLongClickListener() {
            btnKeyWord.setText("QUIZ1")
            db.UpdateDefaultKeyword(btnKeyWord.text.toString())
            true
        }
        btnKeyWord.setOnClickListener() {
            var keyword = btnKeyWord.text.toString()
            if (keyword == "QUIZ1") {
                btnKeyWord.setText("QUIZ2")
            } else if (keyword == "QUIZ2") {
                btnKeyWord.setText("QUIZ3")
            } else if (keyword == "QUIZ3") {
                btnKeyWord.setText("QUIZ4")
            } else if (keyword == "QUIZ4") {
                btnKeyWord.setText("QUIZ5")
            }
            if (keyword == "QUIZ5") {
                btnKeyWord.setText("MIDTERM")
            }
            db.UpdateDefaultKeyword(btnKeyWord.text.toString())
        }
        btnTotalScore.setOnClickListener() {
            var activitycode = GetActivityCode()
            val db2: DatabaseHandler = DatabaseHandler(this)
            var quizScore = btnTotalScore.text.toString().toInt()
            var section = txtCheckSection.text.toString()
            var name = cboStudentNane.getSelectedItem().toString()
            var studNum = db2.GetStudentNumber(name, section)
            var rem = "-"
            if (quizScore > 0) rem = "OK"

            db2.DriveUpdateStudentRecord(section, activitycode, studNum, quizScore)
            CheckScoreRecorded(studNum, section)
        }
    }


    private fun SetSpinnerAdapter() { //val arrSection: Array<String> =
        val arrSection = arrayOf("Java", "Camhi-Web", "Data-Struct", "UNC-Web")
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSubjectAnswer.setAdapter(sectionAdapter);
    }


    fun UpdateListContent(category: String = "ALL") {
        val db2: DatabaseHandler = DatabaseHandler(this)
        val student: List<AnswerNewModel>
        var subject = cboSubjectAnswer.getSelectedItem().toString();
        student = db2.GetAnswerList(subject, "ALL")
        list.clear()
        for (e in student) {
            list.add(AnswerNewModel(e.Subject, e.QuizCode, e.OrderNum, e.AnswerID, e.Number, e.Answer, e.Points, e.QuizSet))

        }
    }


    fun LoadActivity(section: String) {
        val db: TableActivity = TableActivity(this)
        activityList = db.GetActivityList(section, db.GetDefaultGradingPeriod())
        var arrayActivityList = arrayListOf<String>()


        for (e in activityList) {

            arrayActivityList.add(e.description)

        }
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrayActivityList)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboActivity.setAdapter(sectionAdapter);

    }

    fun LoadStudents(category: String = "ALL") {
        Log.e("B201", category)

        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = txtCheckSection.text.toString()

        student = db2.GetEnrolleList(category, section)
        studentlist.clear()
        for (e in student) {
            studentlist.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }


        //    var scoreListArray = Array(list.size+1) { "" }
        val studName: ArrayList<String> = ArrayList<String>()
        var x = 1
        studName.add("Select")
        for (e in studentlist) {
            studName.add(e.lastname + "," + e.firstname)
            x++;
        }
        studentAdapter = ArrayAdapter<String>(myContext, R.layout.util_spinner, studName)
        studentAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboStudentNane.setAdapter(studentAdapter);
    }

    fun ShowPicture(studName: String, section: String, keyword: String, page: String = "") {

        Log.e("section111", section)
        try {
            val path = "/storage/emulated/0/Quiz/" + section + "/" + studName
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")
            var f: File? = null
            if (page == "") {
                f = File(path, keyword + ".jpg")
            } else if (page == "P2") {
                f = File(path, keyword + "-2" + ".jpg")

            } else if (page == "P3") {
                f = File(path, keyword + "-3" + ".jpg")

            } else if (page == "P4") {
                f = File(path, keyword + "-4" + ".jpg")

            } else if (page == "P5") {
                f = File(path, keyword + "-5" + ".jpg")
            }

            if (f!!.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imgStudent.setImageBitmap(b)
            } else {
                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imgStudent!!.setImageBitmap(b)
            }
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")

            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


    }

    fun ShowListView() {
        val db2: DatabaseHandler = DatabaseHandler(this)
        val answer: List<AnswerNewModel>
        var subject = cboSubjectAnswer.getSelectedItem().toString();
        var set =
            btnSet.text.toString() // var quizcode =  cboQuizCode.getSelectedItem().toString();
        var quizcode = cboQuizCode.getSelectedItem().toString();
        answer = db2.GetAnswerList(subject, quizcode, set)
        list.clear()
        Log.e("CCC", answer.size.toString())
        for (e in answer) {
            list.add(AnswerNewModel(e.Subject, e.QuizCode, e.OrderNum, e.AnswerID, e.Number, e.Answer, e.Points, e.QuizSet))
        }
    }

    fun GetScoreRecord() {
        val dbactivity: TableActivity = TableActivity(context)

        val db2: DatabaseHandler = DatabaseHandler(this)
        val activity: List<ScoreModel>
        val section = txtCheckSection.text.toString()
        val activityCode = GetActivityCode()
        activity = dbactivity.GetScoreList(section, activityCode, "SORT", "LASTNAME")
        scorelist.clear()

        for (e in activity) {
            scorelist.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, e.checkBoxStatus, e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }
    }


    fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listCheckAnswer.layoutManager = layoutmanager
        adapterAnswer = CheckAdapter(this, list)
        listCheckAnswer.adapter = adapterAnswer
    }

    fun SetUpAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listCheckAnswer.layoutManager = layoutmanager
        adapterRecordScore = CheckRecordAdapter(this, scorelist)
        listCheckAnswer.adapter = adapterRecordScore
    }

    fun CheckScoreRecorded(studNum: String, section: String) {
        var activitycode = GetActivityCode()
        val db2: DatabaseHandler = DatabaseHandler(this)
        var stud = db2.GetActivityRecord(activitycode, section, studNum)
        var quizScore = btnTotalScore.text.toString().toInt()
        Log.e("BBB", quizScore.toString())
        Log.e("QQQ", stud.score.toString())
        if (stud.score == quizScore) {
            btnTotalScore.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else {

            btnTotalScore.setBackgroundResource(android.R.drawable.btn_default);
        }
    }


}

fun DatabaseHandler.ManageAnswer22(jsonObject: JSONObject, arrHeader: ArrayList<String>, subject: String) {
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


fun DatabaseHandler.SaveStudentAnswer(name: String, quizcode: String, subject: String, section: String, context: Context, set: String = "A") {
    var sql: String = ""
    Log.e("555", name)
    Log.e("555", quizcode)
    Log.e("555", subject)
    Log.e("555", section)
    Log.e("555", set)
    if (name == "Select") {
        Util.Msgbox(context, "Select a Name")
        return
    }

    if (quizcode == "Select") {
        Util.Msgbox(context, "Select a Quiz")
        return
    }

    var studNum = GetStudentNumber(name, section)


    sql = """ SELECT  * FROM tbanswer
              where quizCode='$quizcode'  
              and subject='$subject'  
              and quizset='$set'  
            order by answerID """.trimMargin()
    Log.e("SQL", sql)
    var cursor2 = SQLSelect(sql, 112)
    Log.e("AAA", cursor2!!.count.toString())



    if (cursor2!!.moveToFirst()) {
        do { // Subject	QuizCode	OrderNum	AnswerID	Answer	Points
            var answerID = cursor2!!.getString(cursor2.getColumnIndex("AnswerID"))
            sql = """ SELECT * FROM tbAnswer_Student 
              where StudentNumber='$studNum'
              and AnswerID='$answerID'
           """
            var cursor3 = SQLSelect(sql, 567, 189)

            if (cursor3!!.count == 0) {
                var sql2 = """
                        insert into tbAnswer_Student(StudentNumber,	QuizCode,	AnswerID,	Score)
                        values ('$studNum', '$quizcode', '$answerID', -1)
                    """.trimIndent()
                SQLManage(sql2, 100) //                val db = this.writableDatabase
                //                db.execSQL(sql2)
            }
        } while (cursor2!!.moveToNext())
    }
}


fun DatabaseHandler.GetSubjectQuizlist(subject: String): ArrayList<String> {

    val quizList: ArrayList<String> = ArrayList<String>()

    var sql: String = ""

    quizList.add("Select")

    sql = """ SELECT * from tbquiz
              where Subject='$subject'
            order by QuizCode """.trimMargin()

    Log.e("SQL", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    Log.e("CCC", cursor.count.toString())
    var num = 1;
    if (cursor.moveToFirst()) {
        do {
            var quizCode = cursor.getString(cursor.getColumnIndex("QuizCode"))
            quizList.add(quizCode)
        } while (cursor.moveToNext())
    }
    return quizList
} //var subject = TestCheck.viewcheck!!.txtCheckSubject.text.toString();
//var quizcode = TestCheck.viewcheck!!.txtCheQuizCode.text.toString();

fun DatabaseHandler.SumofScoreQuiz(quizCode: String, studentNo: String): Int {

    val quizList: ArrayList<String> = ArrayList<String>()

    var sql: String = ""

    quizList.add("Select")

    sql = """ SELECT  Sum(Score) as total FROM tbanswer_Student 
              where StudentNumber='$studentNo'
              and QuizCode='$quizCode'
              and Score >=0
           """.trimMargin()

    var cursor = SQLSelect(sql)
    Log.e("CCC", cursor!!.count.toString())
    if (cursor!!.moveToFirst()) {

        val count = cursor!!.getString(cursor.getColumnIndex("total"))
        if (count === null) {
            return 0
        } else {
            return count.toInt()
        } //    StudentNumber	QuizCode	AnswerID	Score
    }
    return 0; //    Log.e("SQL", sql)
    //    val db = this.readableDatabase
    //    var cursor: Cursor? = null
    //    cursor = db.rawQuery(sql, null)
    //    Log.e("CCC", cursor.count.toString())
    //    var num = 1;
    //    if (cursor.moveToFirst()) {
    //        do {
    //            var quizCode = cursor.getString(cursor.getColumnIndex("QuizCode"))
    //            quizList.add(quizCode)
    //        } while (cursor.moveToNext())
    //    }
    //    return quizList
}

fun DatabaseHandler.SQLSelect(sql: String, num: Int = 0, count: Int = 0): Cursor? {
    if (num > 0) {
        Log.e("SQL" + num.toString(), sql)
    }
    try {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (count > 0) {
            Log.e("COUNT" + count.toString(), cursor.count.toString())
            var i = 0
            var f = ""
            while (i <cursor.getColumnCount()) {
                 f = f +  cursor.getColumnName(i) + " "
                i++;
            }
            Log.e("FIELD" + count.toString(), f)
        }

        return cursor
    } catch (e: Exception) {
        Log.e("SQLERROR", "SQL Error in " + sql)
        return null
    }
}


fun DatabaseHandler.SQLManage(sql: String, num: Int = 0) {
    if (num > 0) {
        Log.e("SQL" + num.toString(), sql)
    }
    try {
        val db = this.writableDatabase
        db.execSQL(sql)
    } catch (e: Exception) {
        Log.e("SQLERROR", "SQL Error in " + sql)

    }
}


fun DatabaseHandler.GetStudentNumber(name: String, section: String): String {
    var nameSplit = name.split(",")
    var lastname = nameSplit[0]
    var firstname = nameSplit[1]


    var sql = """ SELECT * FROM tbstudent_query 
              where FirstName='$firstname'
              and LastName='$lastname'
              and Section='$section'
"""

    var studNum = ""
    var cursor = SQLSelect(sql)
    if (cursor!!.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("StudentNo"))
    }
    return ""
}

fun DatabaseHandler.GetActivityScore(name: String, section: String): String {
    var nameSplit = name.split(",")
    var lastname = nameSplit[0]
    var firstname = nameSplit[1]


    var sql = """ SELECT * FROM tbstudent_query 
              where FirstName='$firstname'
              and LastName='$lastname'
              and Section='$section'
"""

    var studNum = ""
    var cursor = SQLSelect(sql)
    if (cursor!!.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("StudentNo"))
    }
    return ""
}




