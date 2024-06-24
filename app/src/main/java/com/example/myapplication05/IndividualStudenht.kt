package com.example.myapplication05

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.individusl_student.btnAverage
import kotlinx.android.synthetic.main.individusl_student.btnSearchFifth
import kotlinx.android.synthetic.main.individusl_student.btnSearchFirst
import kotlinx.android.synthetic.main.individusl_student.btnSearchFourth
import kotlinx.android.synthetic.main.individusl_student.btnSearchSecond
import kotlinx.android.synthetic.main.individusl_student.btnSearchThird
import kotlinx.android.synthetic.main.individusl_student.view.*
import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


class IndividualStudenht : AppCompatActivity() {


    var attendanceindividualList = arrayListOf<AttendanceModel>()

    var individualAttendanceAdapter: AttendanceIndividualAdapter? = null;
    val myContext = this;
    var list = arrayListOf<EnrolleModel>()
    var listALL = arrayListOf<EnrolleModel>()
    var studentNumber = ""
    var strGrade = ""
    var mode = "GRADES"
    var studentAdapter: ArrayAdapter<String>? = null;

    var studentSearchAdapter: IndividualSearchAdapter? = null;
    var studentSearchList = arrayListOf<EnrolleModel>()

    companion object {
        var adapterIndividual: AttendanceIndividualAdapter? = null;
        var attendanceIndividualList = arrayListOf<AttendanceModel>()
        var individualList = arrayListOf<ScoreIndividualModel>()
        var individualAdapter: ScoreIndividualAdapter? = null;
        var varTxtGrade: TextView? = null;
        var varTxtOriginalGrade: TextView? = null;
        var varTxtAdjustedGrade: TextView? = null;
        var varbtnPresent: Button? = null
        var varbtnAbsent: Button? = null
        var varbtnLate: Button? = null
        var varbtnRecitation: Button? = null
        var varbtnTask: Button? = null
        var vartxtName: TextView? = null
        var vartxtSection: TextView? = null
        var vartxtStudNum: TextView? = null
        var vartxtGrade: TextView? = null
        var vartxtOriginalEquivalwnt: TextView? = null
        var vartxtAdjustedEquivalent: TextView? = null
        var varPicture: TouchImageView? = null      //0617

        var vartxtGrade2: TextView? = null
        var vartxtOriginalEquivalwnt2: TextView? = null
        var vartxtAdjustedEquivalent2: TextView? = null
        var vartxtAverage: TextView? = null


        fun UpdateListIndividualContent(context: Context, section: String, studNum: String) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<ScoreIndividualModel>

            val db: TableActivity = TableActivity(context)
            student = db.GetIndividualActivity(section, studNum, "CODE")


            individualList.clear()
            for (e in student) {
                individualList.add(ScoreIndividualModel(e.activitycode, e.sectioncode, e.studentNo, e.description, e.score, e.remark, e.status, e.item, e.gradingperiod, e.category, e.adjustedScore, e.submissionStatus))
            }

            ShowGrades(context)
        }

        //0619
        fun UpdateListContentIndividualAttendance(context: Context, category: String = "MONTH", studNum: String = "") {

            val db1: TableAttendance = TableAttendance(context);
            val db2: DatabaseHandler = DatabaseHandler(context);

            var pp: AttendanceModel = AttendanceModel()

            val individuallist: List<AttendanceModel>

            individuallist = db2.GetIndividualAttendance(Util.GetCurrentGradingPeriod(context), studNum)
            Log.e("213456", individuallist.size.toString())
            attendanceIndividualList.clear()
            var x = 0;
            var len = attendanceIndividualList.size
            for (att in individuallist) {

                attendanceIndividualList.add(AttendanceModel(att.ampm, att.myDate, att.sectionCode, att.studentNo, att.completeName, att.groupNumber, att.attendanceStatus, att.remark, att.recitationPoint, att.TaskPoints, att.randomNumber, att.firstName, att.lastName))
                x++;
            }

            for (att in attendanceIndividualList) {
                Log.e("213000", att.myDate)

            }

            Log.e("213000", attendanceIndividualList.size.toString())
        }

//0619
        fun ShowGrades(context: Context) {
            val db4: TableAttendance = TableAttendance(context)
            Log.e("II22", "Hello")
            var currentGradingPeriod = Util.GetCurrentGradingPeriod(context)
            var section = vartxtSection!!.text.toString()
            var studentNumber = vartxtStudNum!!.text.toString()

            val db2: Grades = Grades(context)
            val db3: TableAttendance = TableAttendance(context)
            val db: TableActivity = TableActivity(context)
//            if (db.GetAverageStatus() == "TRUE") {
//                val first =
//                    db2.GetStudentTermGrade(section, studentNumber, "FIRST", "ADJUSTED").toString()
//                val second =
//                    db2.GetStudentTermGrade(section, studentNumber, "SECOND", "ADJUSTED").toString()
//                val cumulative =
//                    db2.GetStudentTermGrade(section, studentNumber, "CUMULATIVE", "-").toString()
//                vartxtGrade!!.setText("1ST: " + first)
//                vartxtOriginalEquivalwnt!!.setText("2ND:  " + second)
//                vartxtAdjustedEquivalent!!.setText("AVG:  " + cumulative)
//            } else if (currentGradingPeriod == "FIRST" || currentGradingPeriod == "SECOND") {

                var grades =
                    db2.GetStudentTermGrade(section, studentNumber, "FIRST", "DECIMAL").toString()
                var originalGrades =
                    db2.GetStudentTermGrade(section, studentNumber, "FIRST", "ORIGINAL").toString()
                var adjustedGrades =
                    db2.GetStudentTermGrade(section, studentNumber, "FIRST", "ADJUSTED").toString()


                var grades2 =
                    db2.GetStudentTermGrade(section, studentNumber, "SECOND", "DECIMAL").toString()
                var originalGrades2 =
                    db2.GetStudentTermGrade(section, studentNumber, "SECOND", "ORIGINAL").toString()
                var adjustedGrades2 =
                    db2.GetStudentTermGrade(section, studentNumber, "SECOND", "ADJUSTED").toString()


                val cumulative =
                    db2.GetStudentTermGrade(section, studentNumber, "CUMULATIVE", "-").toString()




                vartxtGrade!!.setText("GR: " + grades)
                vartxtOriginalEquivalwnt!!.setText("ORIG:  " + originalGrades)
                vartxtAdjustedEquivalent!!.setText("ADJ:  " + adjustedGrades)

                vartxtGrade2!!.setText("GR: " + grades2)
                vartxtOriginalEquivalwnt2!!.setText("ORIG:  " + originalGrades2)
                vartxtAdjustedEquivalent2!!.setText("ADJ:  " + adjustedGrades2)
                vartxtAverage!!.setText("AVG: "+ cumulative)


                var num = originalGrades.toDouble().toInt()
                vartxtGrade!!.setTextColor(Color.BLACK);
                vartxtOriginalEquivalwnt!!.setTextColor(Color.BLACK);


                if (adjustedGrades.toDouble() < 75) {
                    vartxtAdjustedEquivalent!!.setTextColor(Color.RED)
                };
                else {
                    vartxtAdjustedEquivalent!!.setTextColor(Color.BLACK)
                }
        }
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individusl_student) //]

        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        txtIndividualSection.text = currentSection


        varTxtGrade = findViewById(R.id.txtGrade) as TextView
        varTxtOriginalGrade = findViewById(R.id.txtOriginalEquivalwnt) as TextView
        varTxtAdjustedGrade = findViewById(R.id.txtAdjustedEquivalent) as TextView
        vartxtName = findViewById(R.id.txtStudentNanme) as TextView
        vartxtSection = findViewById(R.id.txtIndividualSection) as TextView
        vartxtStudNum = findViewById(R.id.txtIndividualStudentNumber) as TextView
        vartxtGrade = findViewById(R.id.txtGrade) as TextView
        vartxtOriginalEquivalwnt = findViewById(R.id.txtOriginalEquivalwnt) as TextView
        vartxtAdjustedEquivalent = findViewById(R.id.txtAdjustedEquivalent) as TextView

        vartxtGrade2 = findViewById(R.id.txtGradeSecond) as TextView
        vartxtOriginalEquivalwnt2 = findViewById(R.id.txtOriginalEquivalwntSecond) as TextView
        vartxtAdjustedEquivalent2 = findViewById(R.id.txtAdjustedEquivalentSecond) as TextView
        vartxtAverage = findViewById(R.id.txtAverage) as TextView
        varPicture = findViewById(R.id.imgStudentPicture) as TouchImageView   //0617




        Log.e("6789", "100")

        Log.e("6789", "200")

        UpdateListContent(myContext)
        Log.e("6789", "300")


        Log.e("6789", "400")

        GetAllStudent(myContext);
        Log.e("6789", "500")

        var currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
        Log.e("6789", "600")

        ChangeColorQuarter(currentGradingPeriod); //
        Log.e("6789", "700")

        Log.e("12322245", Util.GRADE_INDIVIDUAL)
        Log.e("12345", Util.GRADE_STUD_NO)
        Log.e("123455", listALL[0].studentno)
        Log.e("123455", Util.GRADE_INDIVIDUAL)
        val db2: DatabaseHandler = DatabaseHandler(this)

        UpdateSearchContent(this, "SECTION", "A-C", db2.GetCurrentSection())
        SetUpStudentAdapter()

        //0618
        UpdateListContentIndividualAttendance(this, "", "")
        SetUpListViewIndividualAttendanceAdapter()






        if (Util.GRADE_INDIVIDUAL == "GRADES") { //            var currentSection = Util.GRADE_SECTION
            //            val db: DatabaseHandler = DatabaseHandler(this)
            //            UpdateListIndividualContent(this, currentSection, Util.GRADE_STUD_NO)
            //            txtStudentNanme.text = Util.GRADE_NAME
            //            SetUpIndividualAdapter()
            //
            //            txtIndividualSection.text = currentSection
            //            txtIndividualStudentNumber.text =  Util.GRADE_STUD_NO
            //ShowGrades(this)
            //


            vartxtName!!.text = Util.GRADE_NAME
            vartxtSection!!.text = Util.GRADE_SECTION
            vartxtStudNum!!.text = Util.GRADE_STUD_NO

            val orderActivity = "CODE"

            SetUpIndividualAdapter()
            UpdateListIndividualContent(this, Util.GRADE_SECTION, Util.GRADE_STUD_NO)
            individualAdapter!!.notifyDataSetChanged()

        } else {
            val db: DatabaseHandler = DatabaseHandler(this)
            UpdateListIndividualContent(this, db.GetCurrentSection(), listALL[0].studentno)
            ShowIndividualRecord(db.GetCurrentSection(), listALL[0].studentno)
            Util.GRADE_NAME = listALL[0].lastname + "," + listALL[0].lastname
            txtStudentNanme.text = Util.GRADE_NAME
            SetUpIndividualAdapter()
            var currentSection = db.GetCurrentSection();
            txtIndividualSection.text = currentSection
            txtIndividualStudentNumber.text = listALL[0].studentno
        }


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        btnSearchFirst.setOnClickListener {
            UpdateSearchContent(this, "SECTION", "A-C", db2.GetCurrentSection())
            studentSearchAdapter!!.notifyDataSetChanged()
        }

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        btnSearchSecond.setOnClickListener {

            UpdateSearchContent(this, "SECTION", "D-J", db2.GetCurrentSection())

            studentSearchAdapter!!.notifyDataSetChanged()
        }


        //0617
        btnCapture.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var out = Environment.getExternalStorageDirectory()

            out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)

            val photoURI =
                FileProvider.getUriForFile(this, "com.ortiz.touchview.provider", out) // FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", out)
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(i, 100)
        }

        //0618
        btnUpload.setOnClickListener {
//            var  chooseFile: Intent
//            chooseFile = Intent(Intent.ACTION_GET_CONTENT)
//            chooseFile.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
//            chooseFile.setType("*/*")
//            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
//            startActivityForResult(chooseFile, 200)

            //Toast.makeText(this, "Select PDF File", Toast.LENGTH_SHORT).show()
            val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
            browseStorage.type = "image/*"
            //browseStorage.type = "application/pdf"
            //browseStorage.setType("image/*");
            //browseStorage.setType("image/*");
            browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(browseStorage, "Select Pdf"), 201)
        }


        btnSearchType.setOnClickListener {
            if (btnSearchType.text.toString() == "SECTION") {
                btnSearchType.text = "ALL"
                studentSearchList.clear()
                studentSearchAdapter!!.notifyDataSetChanged()
                UpdateSearchContent(this, "ALL", "ME")
                btnSearchFifth.isVisible = false
                btnSearchFirst.isVisible = false
                btnSearchFourth.isVisible = false
                btnSearchSecond.isVisible = false
                btnSearchThird.isVisible = false
                txtSearchAll.isVisible = true
            } else {
                btnSearchType.text = "SECTION"
                UpdateSearchContent(this, "SECTION", "A-C", db2.GetCurrentSection())
                studentSearchAdapter!!.notifyDataSetChanged()
                btnSearchFifth.isVisible = true
                btnSearchFirst.isVisible = true
                btnSearchFourth.isVisible = true
                btnSearchSecond.isVisible = true
                btnSearchThird.isVisible = true
                txtSearchAll.isVisible = false
            }

        }


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        btnSearchThird.setOnClickListener { //LoadStudents("K-O")
            UpdateSearchContent(this, "SECTION", "K-O", db2.GetCurrentSection())

            studentSearchAdapter!!.notifyDataSetChanged()
        }

        txtSearchAll.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { //here is your code
                UpdateSearchContent(myContext, "ALL", txtSearchAll.text.toString())
                studentSearchAdapter!!.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // TODO Auto-generated method stub
            }
        })


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        btnSearchFourth.setOnClickListener {
            UpdateSearchContent(this, "SECTION", "P-R", db2.GetCurrentSection())

            studentSearchAdapter!!.notifyDataSetChanged()
        }

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        btnSearchFifth.setOnClickListener {
            UpdateSearchContent(this, "SECTION", "S-Z", db2.GetCurrentSection())

            studentSearchAdapter!!.notifyDataSetChanged()
        }

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //        btnFirst.setOnClickListener {
        //            val section = cboSection.getSelectedItem().toString();
        //            val db: TableActivity = TableActivity(this)
        //            ChangeColorQuarter("FIRST")
        //            db.SetDefaultGradingPeriod("FIRST")
        //            kkk(section,  studentNumber)
        //            ShowGrades(myContext)
        //        }
        //
        //        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //        btnSecond.setOnClickListener {
        //            val db: TableActivity = TableActivity(this)
        //            db.SetDefaultGradingPeriod("SECOND")
        //            ChangeColorQuarter("SECOND")
        //            val section = cboSection.getSelectedItem().toString();
        //            ShowIndividuhhhualRecord(section,  studentNumber)
        //            ShowGrades(myContext)
        //
        //        }
        //btnAverage.set

        btnAverage.setOnClickListener {
            Log.e("VVV", btnAverage.backgroundTintList.toString()) ///
            val db: TableActivity = TableActivity(this)
            if (db.GetAverageStatus() == "TRUE") db.SetAverageStatus("FALSE")
            else db.SetAverageStatus("TRUE")
            var currentGradingPeriod = Util.GetCurrentGradingPeriod(myContext)
            ChangeColorQuarter(currentGradingPeriod)
            ShowGrades(myContext)
        }

        btnGradingPeriod.setOnClickListener {
            if (btnGradingPeriod.text == "FIRST") {
                btnGradingPeriod.setText("SECOND")
            }
            else if (btnGradingPeriod.text == "SECOND") {
                btnGradingPeriod.setText("FIRST")
            }

            var studNum = txtIndividualStudentNumber.text.toString()
            val db: TableActivity = TableActivity(this)
            db.SetDefaultGradingPeriod(btnGradingPeriod.text.toString())
            //0619
            UpdateListContentIndividualAttendance(this, "MONTH",  studNum )
            adapterIndividual!!.notifyDataSetChanged()
            val section = txtIndividualSection.text.toString()
            UpdateListIndividualContent(this, section, studNum)
            individualAdapter!!.notifyDataSetChanged()
            ShowGrades(myContext)
            ChangeColorQuarter(btnGradingPeriod.text.toString())
        }

        btnActivityOrder.setOnClickListener {
            if (btnActivityOrder.text == "CODE") btnActivityOrder.setText("TYPE")
            else if (btnActivityOrder.text == "TYPE") btnActivityOrder.setText("MISSING")
            else if (btnActivityOrder.text == "MISSING") btnActivityOrder.setText("CODE")


            val db: TableActivity = TableActivity(this)
            db.SetDefaultGradingPeriod(btnGradingPeriod.text.toString())
            val section = txtIndividualSection.text.toString()
            ShowIndividualRecord(section, studentNumber, 111)
            ShowGrades(myContext)
        }


        //        btnCoppyNoIndividual.setOnClickListener {
        //            Util.CopyText(myContext, strGrade)
        //        }

        btnOpenFolder.setOnClickListener {
            GetAllStudent(myContext)
            var lastname = ""
            var ctr = 0
            for (e in listALL) {
                var completeName = e.lastname + "," + e.firstname
                lastname = e.lastname
                var selectedName = txtStudentNanme.text.toString();
                if (completeName == selectedName) {
                    break;
                }
                ctr++;
            }


            var section = txtIndividualSection.text.toString()
            studentNumber = listALL[ctr].studentno
            val db: DatabaseHandler = DatabaseHandler(this)
            Util.FOLDER_LINK = db.GetLink(studentNumber, section)
            Util.FOLDER_NAME = lastname
            Util.FOLDER_SECTION = section
            Util.FOLDER_STUDNUM = studentNumber


            val intent = Intent(this, Gdrive::class.java)
            startActivity(intent)
        }


        btnSendGrade.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            val db: DatabaseHandler = DatabaseHandler(this)
            var studentNumber = txtIndividualStudentNumber.text.toString()
            val email = db.GetStudentEmail(studentNumber)

            var section = txtIndividualSection.text.toString()
            val db2: Grades = Grades(myContext)
            val db3: TableAttendance = TableAttendance(myContext)
            val db1: TableActivity = TableActivity(this)


            val currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
            var emailBody = txtStudentNanme.text.toString() + "\n"
            if (db1.GetAverageStatus() == "TRUE") {
                emailBody =
                    emailBody + "MIDTERM GRADE: " + db2.GetStudentTermGrade(section, studentNumber, "FIRST", "DECIMAL")
                        .toString() + "\n"
                emailBody =
                    emailBody + "FINAL GRADE: " + db2.GetStudentTermGrade(section, studentNumber, "SECOND", "DECIMAL")
                        .toString() + "\n"
                emailBody =
                    emailBody + "CUMULATIVE GRADE: " + db2.GetStudentTermGrade(section, studentNumber, "CUMULATIVE", "-")
                        .toString() + "\n"
            } else if (currentGradingPeriod == "FIRST") emailBody =
                emailBody + "MIDTERM GRADE: " + db2.GetStudentTermGrade(section, studentNumber, "FIRST", "ADJUSTED")
                    .toString()
            else if (currentGradingPeriod == "SECOND") emailBody =
                emailBody + "FINAL GRADE: " + db2.GetStudentTermGrade(section, studentNumber, "SECOND", "ADJUSTED")
                    .toString()






            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            i.putExtra(Intent.EXTRA_SUBJECT, "FINAL GRADE")
            i.putExtra(Intent.EXTRA_TEXT, emailBody)
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))

            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        //btnSendGrade
    } //overide fun



    //0618
    fun SetUpListViewIndividualAttendanceAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listIndividualAttendance.layoutManager = layoutmanager
        Log.e("213555", attendanceIndividualList.size.toString())
        adapterIndividual = AttendanceIndividualAdapter(this, attendanceIndividualList)
        listIndividualAttendance.adapter = adapterIndividual
    }

    fun SetUpStudentAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listStudent.layoutManager = layoutmanager
        studentSearchAdapter = IndividualSearchAdapter(this, studentSearchList)
        listStudent.adapter = studentSearchAdapter //
    }

    fun SetUpIndividualAdapter() {
        val layoutmanager = LinearLayoutManager(myContext)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listIndividualStudent.layoutManager = layoutmanager
        individualAdapter = ScoreIndividualAdapter(myContext, individualList)
        listIndividualStudent.adapter = individualAdapter

    }

    //0618
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("4510", requestCode.toString())
        Log.e("4510", resultCode.toString())
        Log.e("4510", Activity.RESULT_OK.toString())
        Log.e("4510", data.toString())
      if (requestCode == 100) {
          val path = "/storage/emulated/0"
          val f: File = File(path, "temp.jpg")
          val b = BitmapFactory.decodeStream(FileInputStream(f))
          imgStudentPicture.setImageBitmap(b)
          val db2: DatabaseHandler = DatabaseHandler(this)
          var origSection = db2.GetStudentOriginalSection(txtIndividualStudentNumber.text.toString()) //
          Log.e("5685", origSection)
          val sdCard = Environment.getExternalStorageDirectory()
          val dir = File(sdCard.absolutePath + "/Quiz/StudentPicture/" + origSection)
          dir.mkdirs() //
          var out = Environment.getExternalStorageDirectory()

          var newFileName = txtStudentNanme.text.toString()
          val file1 = File(out, "temp.jpg")
          val file2 = File(dir, newFileName + ".jpg")
          val src = FileInputStream(file1).channel
          val dst = FileOutputStream(file2).channel
          dst.transferFrom(src, 0, src.size())
          src.close()
          dst.close()
      }

        if (requestCode == 200) {
            if (data==null)
                Log.e("4510", "data is null")

//            val bitmap: Bitmap = data!!.extras?.get("data") as Bitmap
//            imgStudentPicture.setImageBitmap(bitmap)


            val uri: Uri? = data!!.data
            var sss = getRealPathFromURI(uri)
            val myFile = File(uri.toString());

            //            val uri: Uri? = data.getData()
//            val src = uri.path
            Log.e("4510", uri!!.getPath().toString())
            Log.e("4512",myFile.toString())
//            val content_describer: Uri? = data?.getData()
//            val src = content_describer?.path
//            var source = File(src)
//            Log.d("src is ", source.toString())
//            val filename = content_describer?.lastPathSegment
//          //  text.setText(filename)
//            Log.d("FileName is ", filename!!)
////            destination =
////                File(Environment.getExternalStorageDirectory().absolutePath + "/Test/TestTest/" + filename)
//            Log.d("Destination is ", destination.toString())
//            SetToFolder.setEnabled(true)
        }

        if(requestCode==201){
                val selectedPdf = data!!.data
                Log.e("PDF", selectedPdf.toString())
                var path1 = selectedPdf!!.path.toString().split(":")

                var path2 = "/storage/emulated/0/" + path1[1]
            Log.e("777", path2)
            val f: File = File(path2)
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            imgStudentPicture.setImageBitmap(b)

            val db2: DatabaseHandler = DatabaseHandler(this)
            var origSection = db2.GetStudentOriginalSection(txtIndividualStudentNumber.text.toString()) //
            val sdCard = Environment.getExternalStorageDirectory()
            val dir = File(sdCard.absolutePath + "/Quiz/StudentPicture/" + origSection)
            dir.mkdirs() //
            var out = Environment.getExternalStorageDirectory()
            var newFileName = txtStudentNanme.text.toString()
            val file2 = File(dir, newFileName + ".jpg")
            val src = FileInputStream(f).channel
            val dst = FileOutputStream(file2).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
            }
        }




    fun getRealPathFromURI(contentUri: Uri?): String? {
        // arrayOf(MediaStore.Images.Media._ID)
//        val proj = arrayOf(MediaStore.Video.Media.DATA)
//        val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
//
//        //val cursor = managedQuery(contentUri, proj, null, null, null)
//        Log.e("4511", cursor!!.count.toString())
//
//        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
//        Log.e("4511",column_index.toString())
//        cursor.moveToFirst()
//        Log.e("4511", cursor.getString(column_index).toString())
//        return cursor.getString(column_index)
        Log.e("711", contentUri!!.getScheme().toString())
        if ("content" == contentUri!!.getScheme()) {
            var path = ""
            val docId = DocumentsContract.getDocumentId(contentUri!!)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                path = "file://" + Environment.getExternalStorageDirectory() + "/" + split[1]
            }
            Log.e("711" , path)

        } else {

        }
        return ""
    }

    @SuppressLint("Range")
    fun getImageFilePath(uri: Uri?): String? {
        var path: String? = null
        var image_id: String? = null
        var cursor = contentResolver.query(uri!!, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            image_id = cursor.getString(0)
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1)
            cursor.close()
        }
        cursor =
            contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf(image_id), null)
        if (cursor != null) {
            cursor.moveToFirst()
            Log.e("7511", cursor.getColumnName(0).toString())
            Log.e("7511", cursor.getColumnName(1).toString())
            Log.e("7511", cursor.getColumnName(2).toString())
            Log.e("7511", cursor.getColumnName(3).toString())
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()
        }
        return path
    }


    fun UpdateSearchContent(context: Context, category: String, serchString: String, section: String = "") {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section1 = ""
        if (section == "") section1 = db2.GetCurrentSection()
        else section1 = section
        Log.e("111", category)
        studentSearchList.clear()

        student = db2.SearchEnrolleList(category, serchString, section)


        for (e in student) {
            studentSearchList.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }

    }

    //#################################################################################################################################
    fun ShowIndividualRecord(section: String, studNum: String, num: Int = 0) {
        Log.e("NUMNUM", num.toString())
        val orderActivity = btnActivityOrder.text.toString()
        if (mode == "GRADES") {
            val db: TableActivity = TableActivity(myContext)
            individualList =
                db.GetIndividualActivity(section, studNum, orderActivity) //            Log.e("A100", individualList.size.toString())
            //            val layoutmanager = LinearLayoutManager(myContext)
            //            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            //            listIndividualStudent.layoutManager = layoutmanager
            //            individualAdapter = ScoreIndividualAdapter(myContext, individualList)
            //            listIndividualStudent.adapter = individualAdapter
        } else {
            val db: TableActivity = TableActivity(myContext)
            val databaseHandler: TableAttendance = TableAttendance(myContext)

            attendanceindividualList = databaseHandler.GetAttendanceList("INDIVIDUAL", studNum)
            Log.e("A100", "12345")
            val layoutmanager = LinearLayoutManager(myContext)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            listIndividualStudent.layoutManager = layoutmanager
            individualAttendanceAdapter =
                AttendanceIndividualAdapter(myContext, attendanceindividualList)
            listIndividualStudent.adapter = individualAttendanceAdapter

        }
    }


    fun UpdateListContent(context: Context, category: String = "ALL") {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = txtIndividualSection.text.toString()

        student = db2.GetEnrolleList(category, section)
        list.clear()
        for (e in student) {
            list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }
    }

    fun GetAllStudent(context: Context) {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = txtIndividualSection.text.toString()



        student = db2.GetEnrolleList("LAST_ORDER", section)

        listALL.clear()
        for (e in student) {
            listALL.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }
    }

    //######################################################################################

    //###############################################################################
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ChangeColorQuarter(gradingPeriod: String) { //        btnFirst.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        //        btnSecond.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        //        btnAverage.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        //        //        btnFourth.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
        val db: TableActivity = TableActivity(this)
        Log.e("VVV", gradingPeriod)
//        if (db.GetAverageStatus() == "TRUE") btnAverage.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
//        else btnAverage.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));

        if (gradingPeriod == "FIRST") {
            btnGradingPeriod.text = "FIRST" // .setBackgroundColor(Color.parseColor("#64B5F6"))

            btnGradingPeriod.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(100, 181, 246)))
            txtGrade.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(100, 181, 246)))
            txtOriginalEquivalwnt.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(100, 181, 246)))
            txtAdjustedEquivalent.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(100, 181, 246))) //            txtOriginalEquivalwnt.setBackgroundTintList(Color.parseColor("#64B5F6"))
            //            txtAdjustedEquivalent.setBackgroundTintList(Color.parseColor("#64B5F6"))


            txtGradeSecond.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(211, 211, 211)))
            txtOriginalEquivalwntSecond.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(211, 211, 211)))
            txtAdjustedEquivalentSecond.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(211, 211, 211)))


        } else if (gradingPeriod == "SECOND") {
            btnGradingPeriod.text = "SECOND"


            btnGradingPeriod.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 183, 77)))

            txtGradeSecond.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 183, 77)))
            txtOriginalEquivalwntSecond.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 183, 77)))
            txtAdjustedEquivalentSecond.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 183, 77)))

            txtGrade.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(211, 211, 211)))
            txtOriginalEquivalwnt.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(211, 211, 211)))
            txtAdjustedEquivalent.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(211, 211, 211)))

        }


    }


    //
    //        btnCountPresentNew.text =
    //            "P=" + db3.GetIndividualCouunt(studentNumber, currentGradingPeriod, "P")
    //        btnCountLateNew.text =
    //            "L=" + db3.GetIndividualCouunt(studentNumber, currentGradingPeriod, "L")
    //        btnCountAbsentNew.text =
    //            "A=" + db3.GetIndividualCouunt(studentNumber, currentGradingPeriod, "A")
    //        btnTaskScore.text =
    //            "T=" + db3.GetIndividualCouunt(studentNumber, currentGradingPeriod, "", "TASK")
    //        btnRecitationScore.text =
    //            "R=" + db3.GetIndividualCouunt(studentNumber, currentGradingPeriod, "", "RECITATION")
    //
    //        //         strGrade =    cboStudent!!.getSelectedItem().toString() + "\n"
    //         strGrade =  strGrade +  "Third Quarter: " + db2.GetStudentTermGrade(section, studentNumber, "FIRST", "ADJUSTED") + "\n"
    //         strGrade =  strGrade + "Fourth Quarter: " + db2.GetStudentTermGrade(section, studentNumber, "SECOND", "ADJUSTED") + "\n"
    //         Log.e("A21", strGrade)


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

    fun GetNameOrder(): String {
        return "LAST_ORDER"
    }


    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.captureActivity = CaptureAct::class.java

        barLaucher.launch(options)

    }

    var barLaucher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result ->
            if (result.getContents() != null) {
                Util.Msgbox(this, result.getContents())
                val str = result.getContents()
                val rec = str.split(",").toTypedArray()
                val studID = rec[0]
                val db: DatabaseHandler = DatabaseHandler(this)
                var section = txtIndividualSection.text.toString()
                val studNum = db.GetSubjectStudentNo(studID, section)

                var studName = ""
                var ctr = 0

                for (e in listALL) {
                    if (studNum == e.studentno) {
                        studName = e.lastname + "," + e.firstname
                        break;
                    }
                }


                val i = studentAdapter!!.getPosition(studName) // cboStudent.setSelection(i)

                studentNumber = studNum
                ShowIndividualRecord(section, studentNumber, 469)
            }
        }


}


fun DatabaseHandler.GetStudentEmail(studentNo: String): String { //  StudentNo	FirstName	LastName	GrpNumber	Section	Gender	ContactNumber	Extension
    //  ParentContact	EnrollmentStatus	Address	emailAddress	SchoolStudentNumber	MIddleName
    var sql = """
                SELECT * FROM TBENROLL
                WHERE StudentNo = '$studentNo'
                  """
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        val StudentID = cursor.getString(cursor.getColumnIndex("StudentID"))

        sql = """
                SELECT * FROM tbstudent_info
                  WHERE StudentID = '$StudentID'
                """

        val cursor2 = db.rawQuery(sql, null)

        if (cursor2.moveToFirst()) {
            return cursor2.getString(cursor2.getColumnIndex("emailAddress"))
        } else {
            return "none@none.com"
        }

    } else {
        return "none@none.com"
    }
}


//0617
fun DatabaseHandler.GetStudentOriginalSection(studentNo: String): String {
    var sql = """
                SELECT * FROM tbstudent_query
                WHERE StudentNo = '$studentNo'
                  """
    val db = this.readableDatabase
    Log.e("5685", sql)
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        val origSection = cursor.getString(cursor.getColumnIndex("OriginalSection"))
        return origSection
    }
    return ""
}

fun DatabaseHandler.GetLink(studentNo: String, section: String): String {
    var sql = """
                SELECT * FROM TBENROLL
               WHERE Section= '$section'
            AND  StudentNo= '$studentNo'
                  """
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("FolderLink"))
    } else {
        return "-"
    }

}


@SuppressLint("Range")


fun DatabaseHandler.SearchEnrolleList(category: String, searchString: String, section: String = ""): ArrayList<EnrolleModel> {

    val studentList: ArrayList<EnrolleModel> = ArrayList<EnrolleModel>()
    var sql = ""

    if (category == "SECTION") {

        sql = """ SELECT  * FROM tbstudent_query
                                where Section='$section'  """
        if (searchString == "A-C") {
            sql =
                sql + "and (LASTNAME LIKE 'A%' OR LASTNAME LIKE 'B%' OR LASTNAME LIKE 'C%' ) order by lastname"
        } else if (searchString == "D-J") {
            sql =
                sql + "and (LASTNAME LIKE 'F%' OR LASTNAME LIKE 'G%' OR LASTNAME LIKE 'H%' OR LASTNAME LIKE 'I%'  OR LASTNAME LIKE 'J%' OR LASTNAME LIKE 'D%'  OR LASTNAME LIKE 'E%') order by lastname"
        } else if (searchString == "K-O") {
            sql =
                sql + "and (LASTNAME LIKE 'K%' OR LASTNAME LIKE 'L%' OR LASTNAME LIKE 'M%' OR LASTNAME LIKE 'N%'  OR LASTNAME LIKE 'O%') order by lastname"
        } else if (searchString == "P-R") {
            sql =
                sql + "and (LASTNAME LIKE 'P%' OR LASTNAME LIKE 'Q%' OR LASTNAME LIKE 'R%' ) order by lastname"
        } else if (searchString == "S-Z") {
            sql =
                sql + "and (LASTNAME LIKE 'U%' OR LASTNAME LIKE 'V%' OR LASTNAME LIKE 'W%' OR LASTNAME LIKE 'X%'  OR LASTNAME LIKE 'Y%' OR LASTNAME LIKE 'Z%' OR LASTNAME LIKE 'S%'  OR LASTNAME LIKE 'T%') order by lastname"
        }
    } else {   //ELECT * FROM tbsection_query where schoolYear='$schoolYear' and semester ='$semester'
        sql = """ SELECT  * FROM tbstudent_query
                                where LASTNAME LIKE '$searchString%'
                                and section in (select SectionName from tbsection where schoolYear='${Util.CURRENT_SCHOOLYEAR}' and semester ='${Util.CURRENT_SEMESTER}')  order by lastname 
                                """


    }



    Log.e("SQL2516", sql + category)

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    Log.e("B250", cursor.count.toString())
    var num = 1;
    if (cursor.moveToFirst()) {
        do {

            var sn = cursor.getString(cursor.getColumnIndex("StudentNo"))
            var fname = cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_FIRST))
            var lname = cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_LAST))
            var section = cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_SECTION))
            var gender = cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_GENDER))
            var enrollmentStatus = cursor.getString(cursor.getColumnIndex("EnrollmentStatus"))
            var studentID = cursor.getString(cursor.getColumnIndex("StudentID"))
            var grpNumber = cursor.getString(cursor.getColumnIndex("GrpNumber"))
            var link = cursor.getString(cursor.getColumnIndex("FolderLink"))
            var rnd = cursor.getString(cursor.getColumnIndex("Number")).toInt()
            val emp =
                EnrolleModel(num, sn, fname, lname, section, gender, enrollmentStatus, studentID, grpNumber, link, rnd)
            num++; //val emp = StudentModel(sn, fname, lname, grp, section, gender, extension, contactNumber)
            studentList.add(emp)
            Log.e("SQL", fname + " " + link)
        } while (cursor.moveToNext())
    }
    return studentList
}







