package com.example.myapplication05


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.grade_row.view.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.openpdf.*
import kotlinx.android.synthetic.main.openpdf.btnSearchFifth
import kotlinx.android.synthetic.main.openpdf.btnSearchFirst
import kotlinx.android.synthetic.main.openpdf.btnSearchFourth
import kotlinx.android.synthetic.main.openpdf.btnSearchSecond
import kotlinx.android.synthetic.main.openpdf.btnSearchThird
import kotlinx.android.synthetic.main.openpdf.cboSectionActivity
import kotlinx.android.synthetic.main.openpdf.cboStudent
import kotlinx.android.synthetic.main.pdf_record.view.*
import kotlinx.android.synthetic.main.pdf_record.view.txtStudentNo
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.task_dialog_import.view.*
import kotlinx.android.synthetic.main.task_main.*
import org.json.JSONArray
import java.io.File


class OpenNewPDF : AppCompatActivity(), OnPageChangeListener {
    val PDF_SELECTION_CODE = 100
    val myContext: Context = this;
    var activity = arrayListOf<ActivityModel>()
    var scoreList = arrayListOf<ScoreModel>()
    var taskAnswer = arrayListOf<TaskModel>()
    var taskCode = ""
    var taskScoreList = arrayListOf<TaskScoreModel>()
    var taskScoreAdaopter: OpenPdf_Adapter? = null;

    var taskRecordList = arrayListOf<TaskRecordModel>()
    var taskRecordAdapter: PdfRecordAdapter? = null;

    var individualAdapter: PdfRecordAdapter? = null;
    var studentList = arrayListOf<EnrolleModel>()
    var SEARCH_STRING = ""

    companion object {
        var myTxtAnswer: TextView? = null;
        var myTxtScore: TextView? = null;
        var PDF_STATUS = ""
        var individualList = arrayListOf<ScoreIndividualModel>()
        var sss_string = ""
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.openpdf) //pdfview.fromAsset("aaa.pdf").load()
        SetSpinnerAdapter()
        SetDefaultSection()
        var section = cboSectionActivity.getSelectedItem().toString();
        PdfAdapterSetUp();

        LoadStudents("LAST_ORDER", section)
        val dbglobal: TableActivity = TableActivity(myContext)

        btnSearchSecond.setOnClickListener {
            LoadStudents("D-J")
            SEARCH_STRING = "D-J"
            ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
            individualAdapter!!.notifyDataSetChanged()

        }

        btnSearchFirst.setOnClickListener {
            LoadStudents("A-C")
            SEARCH_STRING = "A-C"
            ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
            individualAdapter!!.notifyDataSetChanged()

        }

        btnSearchThird.setOnClickListener {
            LoadStudents("K-O")
            SEARCH_STRING = "K-O"
            ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
            individualAdapter!!.notifyDataSetChanged()

        }

        btnSearchFourth.setOnClickListener {
            LoadStudents("P-R")
            SEARCH_STRING = "P-R"
            ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
            individualAdapter!!.notifyDataSetChanged()

        }

        btnSearchFifth.setOnClickListener {
            LoadStudents("S-Z")
            SEARCH_STRING = "S-Z"
            ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
            individualAdapter!!.notifyDataSetChanged()
        }

        btnSearchNo.setOnClickListener { // LoadNoStudents()
            Log.e("zz", btnSearchNo.text.toString())
            if (btnSearchNo.text == "ZERO") {
                btnSearchNo.text = "ALL"
                ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
                individualAdapter!!.notifyDataSetChanged()

            } else {
                btnSearchNo.text = "ZERO"
                ShowActivityRecord(section, cboActivity.getSelectedItem().toString());
                individualAdapter!!.notifyDataSetChanged()
            }
        }





        btnOpen.setOnClickListener {
            selectPdfFromStorage();
        } //init


        cboSectionActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                 section = cboSectionActivity.getSelectedItem().toString();
                val dbglobal: TableActivity = TableActivity(myContext)

                activity = dbglobal.GetActivityList(section, dbglobal.GetDefaultGradingPeriod())
                var activityList = Array(activity.size + 1) { "" }
                var x = 1;


                activityList[0] = "Select"
                for (e in activity) {
                    activityList[x] = e.description
                    x++;
                }
                var sectionAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(myContext, R.layout.util_spinner, activityList)
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cboActivity.setAdapter(sectionAdapter);
                cboActivity.setSelection(sectionAdapter.getPosition("Select"))
                LoadStudents("ALL")

                //                var desc = cboActivity.getSelectedItem().toString();
                //                if (desc= "Select") {
                //                    LoadStudents()
                //                }


                //  val description = cboActivity.getSelectedItem().toString();
                //OpenPDFfiles(section, description)
            }
        }

        cboActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val section = cboSectionActivity.getSelectedItem().toString();
                val description = cboActivity.getSelectedItem().toString();
                if (description != "Select") {
                    val student = cboStudent.getSelectedItem().toString()
                    SEARCH_STRING = "ALL"
                    ShowActivityRecord(section, description);
                    individualAdapter!!.notifyDataSetChanged()
                }
            }
        }

        cboStudent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                val studName = cboStudent.getSelectedItem().toString();
                var studNumber = ""
                var section = cboSectionActivity.getSelectedItem().toString();
                for (e in studentList) {
                    var completeName = e.lastname + "," + e.firstname
                    if (studName == completeName) {
                        studNumber = e.studentno
                        ShowIndividualRecord(section, studNumber)
                        break;
                    }
                }


            }

        }
    }


    fun ShowIndividualRecord(section: String, studNum: String) {
        val db: TableActivity = TableActivity(myContext)
        individualList = db.GetIndividualActivity(section, studNum, "TYPE")
        Log.e("A100", "12345")
        val layoutmanager = LinearLayoutManager(myContext)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        pdfStudentScore.layoutManager = layoutmanager
        individualAdapter = PdfRecordAdapter(myContext, individualList)
        pdfStudentScore.adapter = individualAdapter
        PDF_STATUS = "IND"
    }


    fun ShowActivityRecord(section: String, description: String) {

        var activityCode = ""
        for (e in activity) {
            if (description == e.description) {
                activityCode = e.activityCode
                break;
            }
        }

        var scoreStatus = ""
        if (btnSearchNo.text == "ZERO") {
            scoreStatus = "ZERO"
        } else {
            scoreStatus = "ALL"
        }
        Log.e("RRR", scoreStatus + "    " + SEARCH_STRING)
        val db: TableActivity = TableActivity(myContext)
        var individual: List<ScoreIndividualModel>
        individualList.clear()
        individual = db.GetActivityRecord(section, activityCode, SEARCH_STRING, scoreStatus)


        for (e in individual) {
            individualList.add(ScoreIndividualModel(e.activitycode, e.sectioncode, e.studentNo, e.description, e.score, e.remark, e.status, e.item, e.gradingperiod, e.category, e.adjustedScore, e.submissionStatus, e.lastname))
        }
        Log.e("ctr", individualList.size.toString())
        PDF_STATUS = "ACT"
    }

    fun PdfAdapterSetUp() {
        val layoutmanager = LinearLayoutManager(myContext)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        pdfStudentScore.layoutManager = layoutmanager
        individualAdapter = PdfRecordAdapter(myContext, individualList)
        pdfStudentScore.adapter = individualAdapter
    }

    fun ShowDialog(context: Context) {
        val db: TableActivity = TableActivity(context)
        val dlgactivity = LayoutInflater.from(context).inflate(R.layout.taskpdf, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgactivity).setTitle("Manage Student")
        val activityDialog = mBuilder.show()
        activityDialog.setCanceledOnTouchOutside(false);
        val section = cboSectionActivity!!.getSelectedItem().toString();
        var taskInfo = arrayListOf<TaskInfoModel>()
        taskInfo = db.GetSubjectTask(section)
        var activityCode = GetActivityCode()
        var activitTaskCode = db.GetActivityTaskCode(section, activityCode)
        Log.e("CCC", "ccc $activitTaskCode  $activityCode")

        val task = ArrayList<String>()
        Log.e("sss", taskInfo.count().toString())
        task.add("-")
        var title = "-"
        for (e in taskInfo) {
            task.add(e.TaskTitle)
            Log.e("sss", e.TaskTitle)
            if (e.TaskCode == activitTaskCode) title = e.TaskTitle

        }
        Log.e("sss10", task.count().toString())


        var sectionAdapter2: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, task)
        sectionAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dlgactivity.cboTaskCode.setAdapter(sectionAdapter2);
        dlgactivity.cboTaskCode.setSelection(sectionAdapter2.getPosition(title))




        dlgactivity.btnSaveRecord.setOnClickListener {
            val buttonText: String = dlgactivity.btnSaveRecord.getText().toString()
            if (buttonText == "EDIT RECORD") {

                dlgactivity.btnSaveRecord.setText("SAVE RECORD")
            } else {
                val description = dlgactivity.cboTaskCode.getSelectedItem().toString();
                var tsskCode = ""
                for (e in taskInfo) {
                    if (description == e.TaskTitle) {
                        taskCode = e.TaskCode
                    }
                }
                Log.e("XXX", taskCode)

                dlgactivity.btnSaveRecord.setText("EDIT RECORD")
                db.UpdateActivityTaskCode(taskCode, activityCode, section)
            } //            val activityCode = dlgactivity.txtActivityCode.text.toString()
            //            val description = dlgactivity.txtDescription.text.toString()
            //            val item = dlgactivity.txtItem.text.toString()
            //            val sectionCode = ActivityMain.cboSectionAct!!.getSelectedItem().toString();
            //            val activityCategory = dlgactivity.cboCategory!!.getSelectedItem().toString();
            //            val gradingPeriod = dlgactivity.cboGradingPeriod!!.getSelectedItem().toString();
        }

    }

    fun GetActivityCode(): String {
        val description = cboActivity.getSelectedItem().toString();
        var studNumber = ""
        var activityCode = ""

        for (e in activity) {
            if (description == e.description) {
                return e.activityCode

            }
        }
        return "";
    }


    fun GetStudentNumber(): String {
        val studName = cboStudent.getSelectedItem().toString();
        for (e in scoreList) {
            if (studName == e.completeName) {
                return e.studentNo
            }
        }
        return "";
    }

    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(myContext)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, mycontext)
        cboSectionActivity.setSelection(index)
    }

    fun OpenPDFfiles(section: String, description: String) {

        val db: TableActivity = TableActivity(myContext)


        var activityCode = ""
        for (e in activity) {
            if (description == e.description) {
                activityCode = e.activityCode
                break;
            }
        }
        val myPath = db.GetPDFPath(section, activityCode)

        var file = File(myPath)
        var uri = Uri.fromFile(file) // pdfview.fromFile(file).load()
        showPDF(uri);


    }

    private fun selectPdfFromStorage() {
        Toast.makeText(this, "Select PDF File", Toast.LENGTH_SHORT).show()
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(browseStorage, "Select Pdf"), PDF_SELECTION_CODE)
    }

    override fun onPageChanged(page: Int, pageCount: Int) { //pageNumber = page //do hat you want with the pageNumber
        Log.e("xxxx", "$page, $pageCount")
        txtPages.text = (page + 1).toString() + "/" + pageCount
    }


    fun LoadStudents(search_string: String = "A-C", name: String = "") {
        val section = cboSectionActivity.getSelectedItem().toString();
        val db2: DatabaseHandler = DatabaseHandler(myContext)

        studentList = db2.GetEnrolleList(search_string, section)

        //  scoreList = dbactivity.GetScoreList(section, activityCode, "SEARCHLETTER", search_string)
        //Log.e("2345", activityCode + "");
        var scoreListArray = Array(studentList.size + 1) { "" }
        var x = 1;
        scoreListArray[0] = "Select"
        for (e in studentList) {
            scoreListArray[x] = e.lastname + "," + e.firstname
            Log.e("2345", e.lastname + "," + e.firstname)
            x++;
        }
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(myContext, R.layout.util_spinner, scoreListArray)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboStudent.setAdapter(sectionAdapter);
        if (name != "") {
            cboStudent.setSelection(sectionAdapter.getPosition(name))
        }
    }


    fun LoadNoStudents() {
        val section = cboSectionActivity.getSelectedItem().toString();
        val description = cboActivity.getSelectedItem()
            .toString(); //val dbglobal: TableActivity = TableActivity(myContext)
        val dbactivity: TableActivity = TableActivity(myContext)
        var activityCode = ""
        for (e in activity) {
            if (description == e.description) {
                activityCode = e.activityCode
                break;
            }
        }


        scoreList = dbactivity.GetScoreList(section, activityCode, "STATUS", "NO")

        var scoreListArray = Array(scoreList.size) { "" }
        var x = 0;

        for (e in scoreList) {
            scoreListArray[x] = e.completeName
            x++;
        }
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(myContext, R.layout.util_spinner, scoreListArray)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboStudent.setAdapter(sectionAdapter);
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun showPDF(uri: Uri?) {
        val mypath = uri?.let { getPathFromURI(myContext, it) }

        val db: TableActivity = TableActivity(myContext)
        val section = cboSectionActivity.getSelectedItem().toString();
        val description = cboActivity.getSelectedItem().toString();
        var activityCode = ""
        for (e in activity) {
            if (description == e.description) {
                activityCode = e.activityCode
                break;
            }
        }

        Log.e("PDF100", "$section   $activityCode  $mypath")
        db.UpdatePdfFiles(section, activityCode, mypath)


        val myFile = File(uri.toString());

        pdfview.fromUri(uri) //  ri          .defaultPage(defaultPage: 0)
            //             .spacing(spacing 10)
            .enableAnnotationRendering(true)

            .onPageChange(this)

            .load()


    }

    override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultcode, data)
        if (requestCode == PDF_SELECTION_CODE && resultcode == Activity.RESULT_OK && data != null) {
            val selectedPdf = data.data
            Log.e("PDF", selectedPdf.toString())
            showPDF(selectedPdf)
        }
    }


    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionActivity.setAdapter(sectionAdapter);
        ActivityMain.cboSectionAct = findViewById(R.id.cboSectionActivity) as Spinner
    }


    fun Sampke() {

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromURI(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) { // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri: Uri? = null

                ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = uri?.let {
                context.getContentResolver().query(it, projection, selection, selectionArgs, null)
            }

            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }


    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


} //class
