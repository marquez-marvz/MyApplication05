package com.example.myapplication05.testpaper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.*
import kotlinx.android.synthetic.main.answer_main.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.keyword_main.view.*
import kotlinx.android.synthetic.main.paperpic.*
import kotlinx.android.synthetic.main.test_check.view.*
import kotlinx.android.synthetic.main.test_check.view.imgStudent
import kotlinx.android.synthetic.main.test_check.view.listCheckAnswer
import kotlinx.android.synthetic.main.test_check.view.txtCheckSection
import kotlinx.android.synthetic.main.test_score.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream


class TestCheck : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        viewcheck = inflater.inflate(R.layout.test_check, container, false)
        containerGlobal = container
        var context = container!!.context
        val db: DatabaseHandler = DatabaseHandler(context);
        val db1: TableActivity = TableActivity(context);
        viewcheck!!.imgStudent.setMaxZoom(10.0f)

//        var subject = viewcheck!!.cboSubjectAnswer.getSelectedItem().toString();
        currentSection = db.GetCurrentSection();

        viewcheck!!.txtCheckSection.setText(currentSection)
        LoadStudents("LAST_ORDER")

        val db2: DatabaseHandler = DatabaseHandler(context)


        viewcheck!!.cboStudentNane.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.e("POS", position.toString())
                    STUD_INDEX = position
                    var name = viewcheck!!.cboStudentNane.getSelectedItem().toString()
                    currentStudName = name
                    if (name != "Select") {
//                        var subject = viewcheck!!.cboSubjectAnswer.getSelectedItem().toString();
//                        var quizcode = viewcheck!!.cboQuizCode.getSelectedItem().toString();
                        //var subject = viewcheck!!.cboSubjectAnswer.getSelectedItem().toString();
                        var subject = TestCheck.viewcheck!!.txtCheckSubject.text.toString();
                        var quizcode = TestCheck.viewcheck!!.txtCheQuizCode.text.toString()
                        var section = viewcheck!!.txtCheckSection.text.toString()
                        var keyword = viewcheck!!.btnKeyWord.text.toString()
                        var set = viewcheck!!.btnSet.text.toString()
                        ShowPicture(name, section, keyword, viewcheck!!.imgStudent)
                        db.SaveStudentAnswer(name, quizcode, subject, section, context, set)
                        adapterAnswer!!.notifyDataSetChanged()
                        val db2: DatabaseHandler = DatabaseHandler(context)
                        var studNum = db2.GetStudentNumber(name, section)
                        var num = db2.SumofScoreQuiz(quizcode, studNum)
                        viewcheck!!.btnTotalScore.setText(num.toString())

                        var name = viewcheck!!.cboStudentNane.getSelectedItem().toString();
                        viewcheck!!.btnCheckPage.setText("PAGE 1")
                        ShowPicture(name, section, keyword, viewcheck!!.imgStudent)
                        CheckScoreRecorded(studNum, section)
                    }
                }
            }


        viewcheck!!.btnNextStudent.setOnClickListener {
            if (STUD_INDEX < viewcheck!!.cboStudentNane.adapter.count - 1) {
                STUD_INDEX++
                viewcheck!!.cboStudentNane.setSelection(STUD_INDEX++)
                var name = viewcheck!!.cboStudentNane.getSelectedItem().toString();
                viewcheck!!.btnCheckPage.setText("PAGE 1")
                var section = viewcheck!!.txtCheckSection.text.toString()
                var keyword = viewcheck!!.btnKeyWord.text.toString()
                ShowPicture(name, section, keyword, viewcheck!!.imgStudent)
            }
        }

        viewcheck!!.btnPrevStudent.setOnClickListener {
            if (STUD_INDEX > 0) {
                STUD_INDEX--;
                viewcheck!!.cboStudentNane.setSelection(STUD_INDEX++)
                var name = viewcheck!!.cboStudentNane.getSelectedItem().toString();
                viewcheck!!.btnCheckPage.setText("PAGE 1")
                var section = viewcheck!!.txtCheckSection.text.toString()
                var keyword = viewcheck!!.btnKeyWord.text.toString()
                ShowPicture(name, section, keyword, viewcheck!!.imgStudent)
            }
        }


        viewcheck!!.btnCheckPage.setOnLongClickListener {
            var subject = viewcheck!!.txtCheckSubject.text.toString();
            var quizcode = viewcheck!!.txtCheQuizCode.text.toString();
            var section = viewcheck!!.txtCheckSection.text.toString()
            var keyword = viewcheck!!.btnKeyWord.text.toString()
            var name = viewcheck!!.cboStudentNane.getSelectedItem().toString();
            viewcheck!!.btnCheckPage.setText("PAGE 1")
            ShowPicture(name, section, keyword, viewcheck!!.imgStudent)
            true
        }
        viewcheck!!.btnCheckPage.setOnClickListener {
           // PrintMe()
            var subject = viewcheck!!.txtCheckSubject.text.toString();
            var quizcode = viewcheck!!.txtCheQuizCode.text.toString();

            var section = viewcheck!!.txtCheckSection.text.toString()
            var keyword = viewcheck!!.btnKeyWord.text.toString()
            var name = viewcheck!!.cboStudentNane.getSelectedItem().toString();
            if (viewcheck!!.btnCheckPage.text.toString() == "PAGE 1") {
                viewcheck!!.btnCheckPage.setText("PAGE 2")
                ShowPicture(name, section, keyword, viewcheck!!.imgStudent, "P2")
            } else if (viewcheck!!.btnCheckPage.text.toString() == "PAGE 2") {
                viewcheck!!.btnCheckPage.setText("PAGE 3")
                ShowPicture(name, section, keyword, viewcheck!!.imgStudent, "P3")
            } else if (viewcheck!!.btnCheckPage.text.toString() == "PAGE 3") {
                viewcheck!!.btnCheckPage.setText("PAGE 4")
                ShowPicture(name, section, keyword, viewcheck!!.imgStudent, "P4")
            }
        }

        viewcheck!!.btnSet.setOnClickListener {
            var set = viewcheck!!.btnSet.text.toString()
            var newSet = "'"

            if (set == "A") newSet = "B"
            else if (set == "B") newSet = "C"
            else if (set == "C") newSet = "D"
            else if (set == "D") newSet = "A"


            viewcheck!!.btnSet.setText(newSet)
            var quizcode = viewcheck!!.txtCheQuizCode.text.toString();
                //.toString(); //  UpdateListContent(quizcode, context, newSet)
            adapterAnswer!!.notifyDataSetChanged()
            ShowListView()
            var subject = viewcheck!!.txtCheckSubject.text.toString();



            var section = viewcheck!!.txtCheckSection.text.toString()
            var keyword = viewcheck!!.btnKeyWord.text.toString()
            var name = viewcheck!!.cboStudentNane.getSelectedItem().toString()
            ShowPicture(name, section, keyword, viewcheck!!.imgStudent)
            db.SaveStudentAnswer(name, quizcode, subject, section, context, newSet)

        }

        viewcheck!!.btnKeyWord.setOnLongClickListener() {
            viewcheck!!.btnKeyWord.setText("QUIZ1")
            db.UpdateDefaultKeyword(viewcheck!!.btnKeyWord.text.toString())
            true
        }
        viewcheck!!.btnKeyWord.setOnClickListener() {
            var section = currentSection
            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.keyword_main, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz).setTitle("")
            inputDialogKeyword = mBuilder.show()
            inputDialogKeyword!!.setCanceledOnTouchOutside(false); //

            val layoutmanager = LinearLayoutManager(context)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlgquiz.lstKeyWord.layoutManager = layoutmanager
            keyWordAdapter = KeyWordAdapter(context, listKeyWord)
            dlgquiz.lstKeyWord.adapter = keyWordAdapter
            var period = Util.GetCurrentGradingPeriod(context)
            dlgquiz.btnPeriod.setText(period)
            var category = "IND"
            UpdateListKeyWord(category, period, section, context)

            val arrSection: ArrayList<String> = db2.GetAllKeyWord()
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dlgquiz.cboKeyWord.setAdapter(sectionAdapter);
            dlgquiz.txtKeyWord.setText("")



            dlgquiz.btnSavekeyWord.setOnClickListener {
                var keyword = dlgquiz.txtKeyWord.text.toString()
                val newPeriod = dlgquiz.btnPeriod.text.toString()
                var category = "IND"
                db2.SaveNewKeyword(keyword, currentSection, category, newPeriod)
                UpdateListKeyWord("IND", newPeriod, section, context)
                keyWordAdapter!!.notifyDataSetChanged()
            }

            dlgquiz.btnPeriod.setOnClickListener {
                var newPeriod = dlgquiz.btnPeriod.text.toString()
                if (newPeriod == "FIRST") {
                    dlgquiz.btnPeriod.setText("SECOND")
                } else {
                    dlgquiz.btnPeriod.setText("FIRST")
                }
                newPeriod = dlgquiz.btnPeriod.text.toString()
                var category = "IND"
                UpdateListKeyWord(category, newPeriod, section, context)
                keyWordAdapter!!.notifyDataSetChanged()

            }

            dlgquiz.cboKeyWord.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        var sss = dlgquiz.cboKeyWord.getSelectedItem().toString();
                        if (sss == "Select") {
                            dlgquiz.txtKeyWord.setText("")
                        } else {
                            dlgquiz.txtKeyWord.setText(sss)
                        }
                    }
                }

        }
        viewcheck!!.btnTotalScore.setOnClickListener() {
            var activitycode = CheckMain.GetActivityCode()
            val db2: DatabaseHandler = DatabaseHandler(context)
            var quizScore = viewcheck!!.btnTotalScore.text.toString().toInt()
            var section = viewcheck!!.txtCheckSection.text.toString()
            var name = viewcheck!!.cboStudentNane.getSelectedItem().toString()
            var studNum = db2.GetStudentNumber(name, section)
            var rem = "-"
            if (quizScore > 0) rem = "OK"

            db2.DriveUpdateStudentRecord(section, activitycode, studNum, quizScore)
            CheckScoreRecorded(studNum, section)
        }

        return viewcheck
    }


    fun CheckScoreRecorded(studNum: String, section: String) {
        var activitycode = CheckMain.GetActivityCode()
        var context = containerGlobal!!.context
        val db2: DatabaseHandler = DatabaseHandler(context)
        var stud = db2.GetActivityRecord(activitycode, section, studNum)
        var quizScore = viewcheck!!.btnTotalScore.text.toString().toInt()
        Log.e("BBB", quizScore.toString())
        Log.e("QQQ", stud.score.toString())
        if (stud.score == quizScore) {
            viewcheck!!.btnTotalScore.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else {

            viewcheck!!.btnTotalScore.setBackgroundResource(android.R.drawable.btn_default);
        }
    }


//    private fun SetSpinnerAdapter() { //val arrSection: Array<String> =
//        var context = containerGlobal!!.context
//        val arrSection = arrayOf("Java", "Camhi-Web", "Data-Struct", "UNC-Web")
//        var sectionAdapter: ArrayAdapter<String> =
//            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
//        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        viewcheck!!.cboSubjectAnswer.setAdapter(sectionAdapter);
//    }




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

    fun PrintMe(){
        var context = containerGlobal!!.context
        var semester = "SECOND"
        val myPdfDocument = PdfDocument()
        val myPaint = Paint()



        val myPageInfo1 =
            PdfDocument.PageInfo.Builder((25 * 72).toInt(), (25 * 72).toInt(), 1).create()
        val myPage1 = myPdfDocument.startPage(myPageInfo1)
        val canvas =
            myPage1.canvas //    canvas.drawText("Version " + version , 10F, 10F, myPaint);

        val image1: Bitmap
        val image2: Bitmap
        val image3: Bitmap
        val image4: Bitmap

       // image1 = BitmapFactory.decodeResource(getResources(), R.drawable.deped1)

        val f: File = File("/storage/emulated/0", "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
//        image2 = Bitmap.createScaledBitmap(b, 700, 700, true)
        canvas.drawBitmap(b, 50F, 10F, myPaint)



        myPdfDocument.finishPage(myPage1);

       // ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)
        //btnPDFGrade
        val folder = "/storage/emulated/0/"

        //           // val folder =
        //              //  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var myFile: File

            myFile = File(folder, "Hello23.pdf")


        requestPermission()
        Log.e("PERMISSION", checkPermission().toString())
        myPdfDocument.writeTo(FileOutputStream(myFile)) //  } catch (e: IOException) {
        //       e.printStackTrace()
        //   }

        myPdfDocument.close()

     //   Util.Msgbox(this, "Grade was sucessfully printed")


    }


    fun requestPermission() {
        var context = containerGlobal!!.context
        if (Build.VERSION.SDK_INT >= 30) {
            if (Environment.isExternalStorageManager()) {
            } else {
                val intent = Intent()
                intent.action =
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION //intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }


    fun checkPermission(): Boolean {
        var context = containerGlobal!!.context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) return Environment.isExternalStorageManager()
        else {

            val readcheck =
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            val writecheck =
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readcheck == PackageManager.PERMISSION_GRANTED && writecheck == PackageManager.PERMISSION_GRANTED
        }
    }

    fun LoadStudents(category: String = "ALL") {
        Log.e("B201", category)
        var context = containerGlobal!!.context
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = viewcheck!!.txtCheckSection.text.toString()

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
        studentAdapter = ArrayAdapter<String>(context, R.layout.util_spinner, studName)
        studentAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewcheck!!.cboStudentNane.setAdapter(studentAdapter);
    }


    companion object {
        var currentSection = ""
        var currentQuizCode = ""
        var currentSubject = ""
        var currentStudName = ""
        var viewcheck: View? = null
        var containerGlobal: ViewGroup? = null
        var cboStudent1: Spinner? = null;
        var vartxtCheckSection: TextView? = null;
        var vartxtLongAnswer: TextView? = null;
        var varbtnTotalScore: Button? = null;
        var varcboQuizCode: Spinner? = null
        var activityList = arrayListOf<ActivityModel>()
        var activityDesc = ""
        var inputDialogKeyword: AlertDialog? = null
        var keyWordAdapter: KeyWordAdapter? = null;

        var adapterAnswer: CheckAdapter? = null;
        var adapterRecordScore: CheckRecordAdapter? = null;
        var studentAdapter: ArrayAdapter<String>? = null;
        var list = arrayListOf<AnswerNewModel>()
        var scorelist = arrayListOf<ScoreModel>()
        var studentlist = arrayListOf<EnrolleModel>()
        var context = this
        var STUD_INDEX = 0;
        var listKeyWord = arrayListOf<KeywordModel>()

        fun GetActivityCode(): String {

            var activityCode = ""
            for (e in CheckMain.activityList) {
                if (CheckMain.activityDesc == e.description) {
                    activityCode = e.activityCode
                    break;
                }
            }
            return activityCode
        }

        fun ShowPicture(studName: String, section: String, keyword: String, imageView: ImageView, page: String = "") {

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
                    imageView.setImageBitmap(b)
                } else {
                    val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    imageView.setImageBitmap(b)
                }
                Log.e("sss", path)
                Log.e("sss", studName + ".jpg")

                // val img = findViewById<View>(R.id.imgStudent) as ImageView

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }


        }

        fun ShowListView() {
            var context = containerGlobal!!.context
            val db2: DatabaseHandler = DatabaseHandler(context)
            val answer: List<AnswerNewModel>
            var subject = viewcheck!!.txtCheckSubject.text.toString();
            var quizcode = viewcheck!!.txtCheQuizCode.text.toString();
            var set = viewcheck!!.btnSet.text.toString()
            currentQuizCode = quizcode
            answer = db2.GetAnswerList(subject, quizcode, set)
            list.clear()
            Log.e("CCC", answer.size.toString())
            for (e in answer) {
                list.add(AnswerNewModel(e.Subject, e.QuizCode, e.OrderNum, e.AnswerID, e.Number, e.Answer, e.Points, e.QuizSet))
            }
        }


        fun SetupQuizCodeAdapter() {
            var context = containerGlobal!!.context
            val layoutmanager = LinearLayoutManager(context)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            viewcheck!!.listCheckAnswer.layoutManager = layoutmanager
            adapterAnswer = CheckAdapter(context, list)
            viewcheck!!.listCheckAnswer.adapter = adapterAnswer
        }
    }

    fun UpdateListKeyWord(category: String, period: String, section: String, context: Context) {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val keyword: List<KeywordModel>


        keyword = db2.GetKeywordList(category, section, period)
        listKeyWord.clear()
        for (e in keyword) {
            listKeyWord.add(KeywordModel(e.Section, e.Keyword, e.GradingPeriod, e.Category, e.DefautKeyword))
        }
    } //


}

fun DatabaseHandler.SetQuizKeyWord(subject: String, quizCode: String, keyword: String) {
    var sql = ""
    sql = """ UPDATE  tbquiz
              set  KeyWord='$keyword'  
                where quizCode='$quizCode'  
              and subject='$subject' 
              """
    SQLManage(sql, 5)
}


fun DatabaseHandler.GetQuizKeyWord(subject: String, quizCode: String): String {
    var sql = ""
    sql = """ SELECT * from  tbquiz
                where quizCode='$quizCode'  
              and subject='$subject' 
              """

    var cursor = SQLSelect(sql, 5)

    if (cursor!!.moveToFirst()) {
        return cursor!!.getString(cursor.getColumnIndex("Keyword"))
    } else {
        return "-"
    }
}