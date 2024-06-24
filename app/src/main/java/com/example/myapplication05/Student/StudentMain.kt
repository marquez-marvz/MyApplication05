package com.example.myapplication05.Student

import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.example.myapplication05.*
import com.example.myapplication05.testpaper.TestCapture
import com.ortiz.touchview.BuildConfig
import kotlinx.android.synthetic.main.enrolle_main.btnSearchFifth
import kotlinx.android.synthetic.main.enrolle_main.btnSearchFirst
import kotlinx.android.synthetic.main.enrolle_main.btnSearchFourth
import kotlinx.android.synthetic.main.enrolle_main.btnSearchSecond
import kotlinx.android.synthetic.main.enrolle_main.btnSearchThird
import kotlinx.android.synthetic.main.enrolle_main.cboSectionEnrolle
import kotlinx.android.synthetic.main.individusl_student.txtSearchAll
import kotlinx.android.synthetic.main.openpdf.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.student_dialog.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.student_import.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.student_main.btnAdd
import kotlinx.android.synthetic.main.student_main.btnImport
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.task_dialog_import.view.*
import kotlinx.android.synthetic.main.task_dialog_import.view.cboSheetList
import kotlinx.android.synthetic.main.task_dialog_import.view.txtActivityCode
import kotlinx.android.synthetic.main.task_dialog_import.view.txtDescription
import kotlinx.android.synthetic.main.task_main.cboSectionActivity
import kotlinx.android.synthetic.main.test_capture.view.btnPaperKeyWord
import kotlinx.android.synthetic.main.test_capture.view.cboSectionPic
import kotlinx.android.synthetic.main.test_capture.view.imgStudent
import kotlinx.android.synthetic.main.test_capture.view.txtStudentName
import kotlinx.android.synthetic.main.util_confirm.view.btnNo
import kotlinx.android.synthetic.main.util_confirm.view.btnYes
import kotlinx.android.synthetic.main.util_inputbox.view.*
import org.json.JSONArray
import java.io.*


class StudentMain : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    val c: Context = this;
    var fileIntent: Intent? = null
    var PATH: String = ""
    val db: DatabaseHandler = DatabaseHandler(this)
    val Utilities: Util = Util(this)


    companion object {
        var adapter: StudentAdapter? = null;
        var list = arrayListOf<StudentInfoModel>()

        var adapterEnrolle: EnrolleAdapter? = null;
        var listenrolle = arrayListOf<EnrolleModel>() //var cbo


        var cbosection: Spinner? = null;
        var cbogroup: Spinner? = null;
        var txtsearch: EditText? = null

        var subjectSection = ""

        fun UpdateListContent(context: Context, category: String = "ALL", section: String = "", searchString: String = "") {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<StudentInfoModel>
            var section = db2.GetCurrentOriginalSection();
            Log.e("111", category)
            list.clear()

            Log.e("AAA", category)
            if (category == "BYLETTER") {
                student = db2.GetStudentList(category, section, searchString)
            } else if (category == "ALL") {
                student = db2.GetStudentList(category, section)
            } else if (category == "NAME") {
                var lastname = txtsearch!!.text.toString();
                student = db2.GetStudentList("NAME_SEARCH", section, lastname)
            } else {
                student = db2.GetStudentList(category, section)
            }


            for (e in student) {
                list.add(StudentInfoModel(e.studentID, e.firstname, e.lastname, e.originalSection, e.gender, e.extension, e.contactNumber, e.parentcontact, e.address, e.emailAddress, e.schoolStudentNumber, e.middleName, e.ctr))
            }
        }

        fun UpdateEnrolleListContent(context: Context, category: String = "ALL") {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<EnrolleModel>
            val section = db2.GetCurrentSection()
            listenrolle.clear()

            if (category == "ALL") {
                student = db2.GetEnrolleList(category, section)

            } else {
                student = db2.GetEnrolleList(category, section)
            }


            for (e in student) {
                listenrolle.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink, 0, e.studentStatus))
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_main)
        val db2: DatabaseHandler = DatabaseHandler(c)
        var currentSection = db.GetCurrentOriginalSection();

        Util.ENROLLE_FLAG = "STUDENT"
        Log.e("5678", "100")


        //SetSpinnerAdapter()
        SetSpinnerAdapterEnrolle()
        SetDefaultSectionEnrolle()
        Log.e("5678", "200")




        SetDefaultSection()
        Log.e("5678", "300")

        UpdateListContent(this, "GENDER_ORDER");
        UpdateEnrolleListContent(this, "LAST_ORDER");
        Log.e("5678", "400")

        Log.e("ARR10", subjectSection)

        SetupStudentListViewAdapter() //
        Log.e("5678", "501")
        SetupEnrolleListViewAdapter()
        Log.e("5678", "5100")

        val s = ""
        val section = db.GetCurrentOriginalSection()
        val arrGroup: Array<String> = Util.GetArrayGroup() // //       var bbtnAdd = ""
        //        Log.e("5678", "5300")

        fun btnAdd() {}
        btnAdd.setOnClickListener {
            var section = cboSectionSearch.getSelectedItem().toString()
            Log.e("@@@128", section)
            ShowDialog("ADD", this, section)
        }





        btnSetAllInactive.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            val db2: DatabaseHandler = DatabaseHandler(c)
            db2.UpdateStudentStatus(section, "INACTIVE")

        }



        btnExport.setOnClickListener() {
            val db2: DatabaseHandler = DatabaseHandler(this)

            val db1: Grades = Grades(this)
            val student: List<StudentInfoModel>
            var section = cboSectionSearch.getSelectedItem().toString();

            val school = db1.GetSchool(section)
            Log.e("school", school)

            if (school == "DEPED") {
                student = db2.GetStudentList("GENDER_ORDER", section)
            } else {
                student = db2.GetStudentList("LAST_ORDER", section)
            }
            var studentData = ""
            for (e in student) {
                studentData =
                    studentData + e.studentID + "\t" + e.firstname + "\t" + e.lastname + "\t" + e.middleName + "\t" + e.extension + "\t"
                studentData =
                    studentData + e.gender + "\t" + e.contactNumber + "\t" + e.parentcontact + "\t" + e.address + "\t"
                studentData = studentData + e.emailAddress + "\t" + e.schoolStudentNumber + "\n"
            }
            Util.ExportToGoogleSheet(this, section, studentData, "Student List", "ExportStudentInfo")
            Util.FOLDER_LINK =
                "https://docs.google.com/spreadsheets/d/1_yrv_yGBrbCSwOblBhZsrTGdw_nwT23TbWL7t6_zmwA/edit#gid=559923255"
            val intent = Intent(this, Link::class.java)
            this.startActivity(intent)
        }

//0617
        btnImport2.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var out = Environment.getExternalStorageDirectory()

            out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)

            val photoURI =
                FileProvider.getUriForFile(this, "com.ortiz.touchview.provider", out) // FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", out)
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(i, 100)
        }
            btnImport.setOnClickListener {
            val loading = ProgressDialog.show(this, "Importing Score", "Please wait")
            val section = cboSectionSearch.getSelectedItem()
                .toString(); //var WithDashsectionCode = section.replace(" ", "-");
            var url =
                "https://script.google.com/macros/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec?"
            Log.e("@@@", "1111")
            url = url + "action=" + "GetClassList"
            url = url + "&&section=" + section

            Log.e("@@@", url)
            var msg = "The following  student number is taken"
            var notSave = false
            var stringRequest = StringRequest(Request.Method.GET, url, { response ->
                Log.e("@@@", response)
                Log.e("@@@", "Hwllo po")


                val str: String = response.toString()
                val check: Boolean = "ERROR" in str
                Log.e("124", str + "  " + check)
                Log.e("125", check.toString())

                if (check) {
                    Util.Msgbox(this, str);
                    loading.dismiss()

                } else {

                    var obj = JSONArray(response);

                    var i = 0;
                    var ctr = 0;
                    var x = 0;
                    var myScore = 0
                    var status = ""
                    val arr = Array<String>(100) { "" }
                    val ppp = obj.getJSONObject(0)
                    val iterator: Iterator<String> = ppp.keys();

                    while (iterator.hasNext()) {
                        arr.set(ctr, iterator.next())
                        ctr++;
                    }

                    Log.e("126", obj.length().toString())
                    while (i < obj.length()) {
                        val jsonObject = obj.getJSONObject(i)
                        var studnum = jsonObject.getString(arr[0])
                        var firstName = jsonObject.getString(arr[1])
                        var lastName = jsonObject.getString(arr[2])
                        var middleName = jsonObject.getString(arr[3])
                        var extension = jsonObject.getString(arr[4])
                        var section = cboSectionSearch.getSelectedItem().toString();
                        var gender = jsonObject.getString(arr[5])
                        var contactNumber = jsonObject.getString(arr[6])
                        var parentContcact = jsonObject.getString(arr[7])
                        var address = jsonObject.getString(arr[8])
                        var emailAddress = jsonObject.getString(arr[9])
                        var schoolStudentNumber =
                            jsonObject.getString(arr[10]) //          StudNumber	FirstName	LastName	MiddleName	Extension	Gender	ContactNumber	ParentContcact	Address	Email	SchoolStudentNumber //          0             1          2            3           4           5       6                      7       8      9         10
                        Log.e("126", i.toString() + "  " + studnum)
                        val db: DatabaseHandler = DatabaseHandler(this)
                        try {
                            val num = studnum
                            Log.e("##", num.toString() + "  " + db.CheckStudent(num, section) + " " + db.CheckStudenNumber(num))
                            if (db.CheckStudenNumber(num) == "EXIST") {
                                db.ManageStudentRecord(num, firstName, lastName, middleName, extension, section, gender, contactNumber, parentContcact, address, emailAddress, schoolStudentNumber, "EDIT")
                            } else {
                                db.ManageStudentRecord(num, firstName, lastName, middleName, extension, section, gender, contactNumber, parentContcact, address, emailAddress, schoolStudentNumber, "ADD")

                            }
                        } catch (e: NumberFormatException) {

                        }



                        i++;
                    } //while
                    if (notSave == false) Util.Msgbox(this, "All student Record  is successfully imported");
                    else Util.Msgbox(this, msg);
                }
            }) { }

            val queue: RequestQueue = Volley.newRequestQueue(this)
            queue.add(stringRequest)
            loading.dismiss()

        }

        cboSectionSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val section = cboSectionSearch.getSelectedItem().toString();
                Log.e("3453", section)
                Utilities.SetOriginalSection(section)
                UpdateListContent(c, "GENDER_ORDER", section)
                val db2: DatabaseHandler = DatabaseHandler(c)


                adapter!!.notifyDataSetChanged()


                ShowStudentCount()
                val arrGroup: Array<String> = Util.GetArrayGroup()


            }

        }

        cboSectionEnrolle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


                val section = cboSectionEnrolle.getSelectedItem().toString();
                val mydb: DatabaseHandler = DatabaseHandler(c)
                mydb.SetCurrentSection(section)
                UpdateEnrolleListContent(c, "ALL")
                adapterEnrolle!!.notifyDataSetChanged() //adapter!!.notifyDataSetChanged()

            }

        }


        btnSearchFirst.setOnClickListener() { //            val section = cboSectionEnrolle.getSelectedItem().toString();

            val section = UpdateEnrolleListContent(c, "A-C")
            adapterEnrolle!!.notifyDataSetChanged()
        }

        btnSearchSecond.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateEnrolleListContent(c, "D-J")
            adapterEnrolle!!.notifyDataSetChanged()
        }
        btnSearchThird.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateEnrolleListContent(c, "K-O")
            adapterEnrolle!!.notifyDataSetChanged()
        }
        btnSearchFourth.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateEnrolleListContent(c, "P-R")
            adapterEnrolle!!.notifyDataSetChanged()
        }
        btnSearchFifth.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateEnrolleListContent(c, "S-Z")
            adapterEnrolle!!.notifyDataSetChanged()
        }


        fun txtSearchAll() {}
        txtSearchAll.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { //here is your code
                // fun (context: Context, category: String = "ALL", section: String = "", searchString: String="") {
                UpdateListContent(c, "BYLETTER", "", txtSearchAll.text.toString())
                adapter!!.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // TODO Auto-generated method stub
            }
        })


    }

    private fun SetSpinnerAdapter() {
        val arrGroup: Array<String> = Util.GetArrayGroup()
        val db2: DatabaseHandler = DatabaseHandler(this)
        val arrSection: ArrayList<String> = db2.GetOriginalSection()
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionSearch.setAdapter(sectionAdapter);
    }

    private fun SetSpinnerAdapterEnrolle() {

        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionEnrolle.setAdapter(sectionAdapter);

    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun SetDefaultSectionEnrolle() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionEnrolle.setSelection(index)
    }

    fun showPopMenu(v: View) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.student_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val selected = item.toString()
        if (selected == "Sort by FirstName") {
            UpdateListContent(this, "FIRST_ORDER")
        } else if (selected == "Sort by LastName") {
            Log.e("menu", "last")
            UpdateListContent(this, "LAST_ORDER")
        } else if (selected == "Sort by FirstName") {
            UpdateListContent(this, "GENDER_ORDER")
        } else if (selected == "Sort by StudNo") {
            UpdateListContent(this, "ID_ORDER")
        } //
        //    } else if (selected == "Sort by Student ID") {
        //        UpdateListContent(this, "ID_ORDER")
        //    }

        //  Sort by StudNo

        adapter!!.notifyDataSetChanged()
        return true;
    }

    private fun SetDefaultSection() {
        var mycontext = this;
        var currentSection = db.GetCurrentOriginalSection();

        var index = GetNewSectionIndex(currentSection, this)
        cboSectionSearch.setSelection(index)

    }

    fun ShowStudentCount() {
        var section = cboSectionSearch.getSelectedItem().toString();
        val db: DatabaseHandler = DatabaseHandler(this)
        Log.e("COUNT", section)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }


    fun ImportExportFile(type: String, msg: String, mypath: String = "", filename: String = "") {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.util_inputbox, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle(msg)
        val mAlertDialog = mBuilder.show()

        mDialogView.txtdata.setText(filename);
        mDialogView.btnOK.setOnClickListener {

            val filename = mDialogView.txtdata.text.toString()
            mAlertDialog.dismiss()
            if (type == "exported") {

                Log.e("Write", "12344567890")
                WriteCSV(filename)
            } else if (type == "imported") { // Util.Msgbox(this, "Hello" )\
                Log.e("Heloo", mypath)
                Log.e("Heloo", filename)
                ReadCSV(mypath, filename)
            }
        }

        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }


    }

    fun WriteCSV(filename: String) {
        try {
            val FILENAME = filename + ".txt"
            val heading = "SN,FirstNane,LastName,Group,SectionCode,Gender"

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 23)

            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myFile = File(folder, FILENAME)
            val fstream = FileOutputStream(myFile)
            Log.e("Write", folder.toString())
            Log.e("Write", filename)

            fstream.write(heading.toByteArray())
            fstream.write("\n".toByteArray()) //            for (mylist in list) {
            //                var myData = mylist.studentno + "," + mylist.firstname + "," + mylist.lastname
            //                myData = myData + "," + mylist.grp + "," + mylist.sectioncode + "," + mylist.gender
            //                fstream.write(myData.toByteArray())
            //                fstream.write("\n".toByteArray())
            //            }
            fstream.close()
            Log.e("Write", "12344567891110")
        } catch (e: Exception) {
            Log.e("err", e.toString())
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun vhin() {
        val myPdfDocument = PdfDocument()
        val myPaint = Paint()

        val myPageInfo1 =
            PdfDocument.PageInfo.Builder((8.5 * 72).toInt(), (13 * 72).toInt(), 1).create()
        val myPage1 = myPdfDocument.startPage(myPageInfo1)
        val canvas = myPage1.canvas
        myPaint.textAlign = Paint.Align.CENTER
        myPaint.textSize = 22f
        canvas.drawText("Final Semestral Grades", (myPageInfo1.pageWidth / 2).toFloat(), 50F, myPaint);

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 14f
        var x = 100F
        var y = 100F
        canvas.drawText("REGION", x + 5, y, myPaint);
        canvas.drawText("SCHOOL NAME", x - 20, y + 20, myPaint);
        canvas.drawText("DIVISION", x + 150, y, myPaint);
        canvas.drawText("SCHOOL ID", 425F, y, myPaint);
        canvas.drawText("SCHOOL NAME", 400F, y + 20, myPaint);

        canvas.drawText("V", 210f, y, myPaint);
        myPaint.textSize = 10F
        canvas.drawText("CAMARINES SUR NATIONAl HIGH SCHOOL ", 190F, y + 20, myPaint);
        myPaint.textSize = 14f
        canvas.drawText("NAGA CITY", 320F, y, myPaint);
        canvas.drawText("302264", 510F, y, myPaint);
        canvas.drawText("2021-2022", 510F, y + 20, myPaint);


        for (i in 10..600 step 10) {
            myPaint.textSize = 6f
            canvas.drawText(i.toString(), i.toFloat(), 150F, myPaint);
        }

        for (i in 10..500 step 10) {
            myPaint.textSize = 6f
            canvas.drawText(i.toString(), 100f, i.toFloat(), myPaint);
        }
        myPaint.setStrokeWidth(0.5f);
        canvas.drawLine(180f, 87f, 245f, 87f, myPaint);
        canvas.drawLine(180f, 105f, 395f, 105f, myPaint);
        canvas.drawLine(180f, 123f, 395f, 123f, myPaint);
        canvas.drawLine(310f, 87f, 395f, 87f, myPaint);

        canvas.drawLine(500f, 87f, 580f, 87f, myPaint);
        canvas.drawLine(500f, 105f, 580f, 105f, myPaint);
        canvas.drawLine(500f, 123f, 580f, 123f, myPaint);

        Drawline(canvas, myPaint, 180, 87, 180, 123, Color.RED)
        Drawline(canvas, myPaint, 245, 87, 245, 105, Color.BLUE)
        Drawline(canvas, myPaint, 395, 87, 395, 123, Color.GREEN)
        Drawline(canvas, myPaint, 310, 87, 310, 105, Color.YELLOW)
        Drawline(canvas, myPaint, 500, 87, 500, 123, Color.RED)
        Drawline(canvas, myPaint, 580, 87, 580, 123, Color.BLUE)

        myPaint.textSize = 10f
        canvas.drawText("LEARNERS' NAMES", 30f, 175f, myPaint);
        canvas.drawText("GRADE AND SECTION: 12-PROG-1", 165f, 160f, myPaint);
        myPaint.textSize = 8f
        canvas.drawText("TEACHER: MARVIN MARQUEZ", 163f, 180f, myPaint);
        canvas.drawText("SEMESTER", 350f, 150f, myPaint);
        canvas.drawText("SUBJECT", 350f, 165f, myPaint);
        canvas.drawText("TRACK:TVL", 350f, 180f, myPaint);

        canvas.drawText("FIRST QUARTER", 165f, 195f, myPaint);
        canvas.drawText("SECOND QUARTER", 255f, 195f, myPaint);
        canvas.drawText("FIRST SEMESTER FINAL", 350f, 195f, myPaint);
        canvas.drawText("REMARK", 500f, 195f, myPaint);


        Drawline(canvas, myPaint, 10, 140, 590, 140, Color.RED)
        Drawline(canvas, myPaint, 10, 200, 590, 200, Color.RED)
        Drawline(canvas, myPaint, 345, 155, 590, 155, Color.BLUE)
        Drawline(canvas, myPaint, 160, 185, 590, 185, Color.BLUE)
        Drawline(canvas, myPaint, 160, 200, 590, 200, Color.BLUE)
        Drawline(canvas, myPaint, 160, 165, 590, 170, Color.GREEN)

        //VERTICAL
        Drawline(canvas, myPaint, 160, 140, 160, 200, Color.RED)
        Drawline(canvas, myPaint, 345, 140, 345, 200, Color.RED)
        Drawline(canvas, myPaint, 590, 140, 590, 200, Color.RED)

        var yy = 210

        for (i in 1..50 step 1) { //  canvas.drawText(i.toString(),10F, (yy-4).toFloat(), myPaint);
            Drawline(canvas, myPaint, 10, yy, 590, yy, Color.BLACK)
            yy = yy + 12
        }

        Drawline(canvas, myPaint, 10, 210, 10, yy - 12, Color.GREEN)
        Drawline(canvas, myPaint, 27, 210, 27, yy - 12, Color.GREEN)
        Drawline(canvas, myPaint, 160, 210, 160, yy - 12, Color.RED)
        Drawline(canvas, myPaint, 345, 210, 345, yy - 12, Color.RED)
        Drawline(canvas, myPaint, 590, 210, 590, yy - 12, Color.RED)


        val dbGrade: Grades = Grades(this)
        val theGrade: List<GradeModel>
        val sql = """ select * from tbgrade_query
                    where sectioncode = '12-PROG1-A'
                   order by gender desc, lastname
                    """.trimIndent()

        theGrade = dbGrade.GetGradeList(sql)
        yy = 210

        var ctr = 1;
        for (e in theGrade) {
            canvas.drawText(ctr.toString(), 20F, (yy - 3).toFloat(), myPaint);
            canvas.drawText(e.lastname + ", " + e.firstname, 50F, (yy - 3).toFloat(), myPaint);
            yy = yy + 12
            ctr++

        }


        //        Drawline(canvas, myPaint,130, 155,  590, 155, Color.BLUE)
        //        Drawline(canvas, myPaint,10, 200,  590, 200, Color.BLUE)
        //        Drawline(canvas, myPaint,10, 200,  590, 200, Color.BLUE)
        //        Drawline(canvas, myPaint,10, 200,  590, 200, Color.BLUE)
        //        Drawline(canvas, myPaint,130, 165,  590, 170, Color.GREEN)
        //        Drawline(canvas, myPaint,130, 180 , 590, 180, Color.GREEN)
        //        Drawline(canvas, myPaint,130, 190,  590, 190, Color.GREEN)
        //        Drawline(canvas, myPaint,130, 215  ,590, 190, Color.GREEN)


        myPdfDocument.finishPage(myPage1); //   val m =  getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        // = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Hello22.pdf")
        //   val file = File(this.getExternalFilesDir("/storage/emulated/0/"), "Hello22.pdf")
        //val file = File("/storage/emulated/0/" , "Hello.pdf")
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, "Hello230.pdf")

        //  try {
        myPdfDocument.writeTo(FileOutputStream(GetPath())) //  } catch (e: IOException) {
        //       e.printStackTrace()
        //   }

        myPdfDocument.close()
        Log.e("kkk", this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString())

    }

    fun Drawline(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        paint.color = color

        canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint);

    }

    fun ReadCSV(mypath: String, filename: String) {

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, filename)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)


        try {
            Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();

            var fileInputStream = FileInputStream(myFile)


            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            var text: String = ""
            var line = bufferedReader.readLine()
            var errMessage = ""
            while (line != null) {
                text = line.toString()
                line = bufferedReader.readLine()
                var token = text.split(",").toTypedArray()
                var saveStatus = true



                if (token.size < 7) {
                    saveStatus = false;
                    errMessage = errMessage + "$line-Missing Token\n"
                } else if (token[0] == "") {
                    saveStatus = false
                    errMessage = errMessage + "$line- Student No is Blank\n"
                } else if (token[1] == "") {
                    saveStatus = false
                    errMessage = errMessage + "$line- Firstnname is Blank\n"
                } else if (token[2] == "") {
                    saveStatus = false
                    errMessage = errMessage + "$line- Lastnname is Blank\n"
                } else if (GetGroupIndex(token[3], this) < 0) {
                    saveStatus = false
                    errMessage = errMessage + "$line-Grp  is Invaid"

                } else if (GetSectionIndex(token[4], this) < 0) {
                    saveStatus = false
                    errMessage = errMessage + "$line-Section  is Invaid"
                } else if (GetGenderIndex(token[5], this) < 0) {
                    saveStatus = false
                    errMessage = errMessage + "$line-Gender  is Invaid"
                }
                if (saveStatus == true) {
                    var status =
                        db.ManageStudent("ADD", token[0], token[1], token[2], token[3], token[4], token[5], token[6])
                    UpdateListContent(this)
                    adapter!!.notifyDataSetChanged()

                    //if (status == true)
                    //  list.add(StudentModel(token[0], token[1], token[2], token[3], token[4]))


                }

                //            //ViewRecord()
            }
            fileInputStream.close()
            adapter!!.notifyDataSetChanged()

            Toast.makeText(this, errMessage, Toast.LENGTH_SHORT).show();
        } catch (exception: FileNotFoundException) {
            Util.Msgbox(this, "The $filename cannot found200")
        }


    }


    fun GetPath(): String {
        val con: ContextWrapper = ContextWrapper(getApplicationContext())
        val dir: File? = con.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file: File = File(dir, "Hello123.pdf")
        return file.getPath()
    }


    fun SetupStudentListViewAdapter() {

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listViewStudent.layoutManager = layoutmanager
        Log.e("5678", "502")
        adapter = StudentAdapter(this, list)
        Log.e("5678", "503")
        listViewStudent.adapter = adapter
    }

    fun SetupEnrolleListViewAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listViewEnrolle.layoutManager = layoutmanager
        adapterEnrolle = EnrolleAdapter(this, listenrolle)
        listViewEnrolle.adapter = adapterEnrolle

    }


    fun ShowDialog(status: String, context: Context, section: String = "", studID: String = "", position: Int = -1) {
        val mydb: DatabaseHandler = DatabaseHandler(context)
        val student = mydb.GetOneStudentRecord(studID)
        val dlgstudent = LayoutInflater.from(context).inflate(R.layout.student_dialog, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgstudent).setTitle("Manage Student")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);
        val arrSection: Array<String> = Util.GetSectionList(context)

        var sectionAdapter1: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
        sectionAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //  dlgstudent.cbosection.setAdapter(sectionAdapter1);

        val arrGroup: Array<String> = Util.GetArrayGroup()
        val db2: DatabaseHandler = DatabaseHandler(context)
        val arrSection2: ArrayList<String> = db2.GetOriginalSection()
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection2)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        var p = """FirstName:
LastName:
 MiddleName:
 Gender:
 ContactNumber:
 ParentContact:
 Address:
 EmailAddress:
 SchoolStudentNumber:
            """
        dlgstudent.txtStudentData.setText(p)



        if (status == "ADD") {


            var studID = db.GetNewStudentID()
            dlgstudent.txtstudentnumber.setText(studID)
            ViewTextBox(false, dlgstudent)
            dlgstudent.btnSaveRecord.setText("SAVE RECORD")

        } else if (status == "VIEW") {

            dlgstudent.txtstudentnumber.setText(student!!.studentID)
            ViewTextBox(false, dlgstudent)

            var p = "FirstName:" + student!!.firstname + "\n"
            p = p + "LastName:" + student!!.lastname + "\n"
            p = p + "MiddleName:" + student!!.middleName + "\n"
            p = p + "Gender:" + student!!.gender + "\n"
            p = p + "ContactNumber:" + student!!.contactNumber + "\n"
            p = p + "ParentContact:" + student!!.parentcontact + "\n"
            p = p + "Address:" + student!!.address + "\n"
            p = p + "EmailAddress:" + student!!.emailAddress + "\n"
            p = p + "SchoolStudentNumber:" + student!!.schoolStudentNumber

            //            FirstName: Mario
            //            LastName:San Buenaventura
            //            MiddleName:	Castor
            //            Gender:	Male
            //            ContactNumber:	09276042323
            //            ParentContact:	123456
            //            Address: Pangpang Canaman Camarines Sur
            //            EmailAddress:marquez.marvz@gmail.com
            //            SchoolStudentNumber:234567889
            //            9:16 AM
            dlgstudent.txtStudentData.setText(p)
            dlgstudent.btnSaveRecord.setText("EDIT")


            var i = Util.GetOriginalSectionIndex(section, context)
            dlgstudent.cboSection.setSelection(i)
        }

        dlgstudent.btnPutInTextBox.setOnClickListener {
            var data = dlgstudent.txtStudentData.text


            var f = data.indexOf("FirstName")
            var l = data.indexOf("LastName")
            var m = data.indexOf("MiddleName")
            var g = data.indexOf("Gender")
            var ex = data.indexOf("Extension")
            var c = data.indexOf("ContactNumber")
            var p = data.indexOf("ParentContact")
            var a = data.indexOf("Address")
            var e = data.indexOf("EmailAddress")
            var s = data.indexOf("SchoolStudentNumber")

            if (f == -1) Util.Msgbox(context, "There is a problem on First Name")
            else if (l == -1) Util.Msgbox(context, "There is a problem on last Name")
            else if (m == -1) Util.Msgbox(context, "There is a problem on middle Name")
            else if (g == -1) Util.Msgbox(context, "There is a problem on gender")
            else if (c == -1) Util.Msgbox(context, "There is a problem on studebnt contact")
            else if (p == -1) Util.Msgbox(context, "There is a problem on parent contact")
            else if (a == -1) Util.Msgbox(context, "There is a problem on address")
            else if (e == -1) Util.Msgbox(context, "There is a problem on email")
            else if (s == -1) Util.Msgbox(context, "There is a problem on Student Number")
            else { //            val firstname = data.substring(f, l-1)
                //            val lastname = data.substring(l, m-1)

                //            var p = "FirstName:" + student!!.firstname + "\n"
                //            p = p + "LastName:"  + student!!.lastname + "\n"
                //            p = p + "MiddleName:"  + student!!.middleName + "\n"
                //            p = p + "Gender:"  + student!!.gender + "\n"
                //            p = p + "ContactNumber:"  + student!!.contactNumber + "\n"
                //            p = p + "ParentContact:"  + student!!.parentcontact + "\n"
                //            p = p + "ParentContact:"  + student!!.address + "\n"
                //            p = p + "EmailAddress:"  + student!!.emailAddress + "\n"
                //            p = p + "SchoolStudentNumber:"  + student!!.schoolStudentNumber

                Log.e("IndexOF", "Value of i:" + f);
                Log.e("IndexOF", "Value of f:" + l);
                Log.e("IndexOF", "Value of l:" + m);
                Log.e("IndexOF", "Value of g:" + g);

                Log.e("IndexOF", "Value of c:" + c);
                Log.e("IndexOF", "Value of p:" + p);
                Log.e("IndexOF", "Value of a:" + a);
                Log.e("IndexOF", "Value of e:" + e);
                Log.e("IndexOF", "Value of s:" + s);


                var arrFirstName = data.substring(f, l - 1).split(":")
                var arrLastName = data.substring(l, m - 1).split(":")
                var arrMiddleName = data.substring(m, g - 1).split(":")
                var arrGender = data.substring(g, c - 1).split(":")

                var arrContact = listOf<String>()
                if (ex > -1) {
                    arrContact = data.substring(c, ex - 1)
                        .split(":") //  extension = data.substring(ex, c - 1).split(":")
                } else {
                    arrContact = data.substring(c, p - 1).split(":")

                } //var arrContact = data.substring(c ,p-1).split(":")
                var arrParent = data.substring(p, a - 1).split(":")
                var arrAddress = data.substring(a, e - 1).split(":")
                var arrEmail = data.substring(e, s - 1).split(":")
                var arrStudentNo = data.substring(s).split(":")
                Log.e("FirtName", arrFirstName[1].trim());
                Log.e("arrLastName", arrLastName[1].trim());
                Log.e("arrMiddleName", arrMiddleName[1].trim());
                Log.e("arrGender", arrGender[1].trim());

                Log.e("arrContact", arrContact[1].trim());
                Log.e("arrParent", arrParent[1].trim());
                Log.e("arrAddress", arrAddress[1].trim());
                Log.e("arrEmail", arrEmail[1].trim());
                Log.e("FirtName", arrStudentNo[1].trim()); //            val list = data.split("\n")
                //            Log.e("@@@", list.count().toString())
                //            var ctr = 1;
                //            for (item in list) {
                //                Log.e("@@@", item)
                //                val token = item.split(":")
                //                if (token.count() == 2) {
                //                    Log.e("@@@", token[1] + "   "+ ctr )
                //                    if (ctr == 1)
                dlgstudent.txtfirstname.setText(CapitalizeFirstLetter(arrFirstName[1].trim()))
                dlgstudent.txtlastname.setText(CapitalizeFirstLetter(arrLastName[1].trim()))
                dlgstudent.txtMiddleName.setText(CapitalizeFirstLetter(arrMiddleName[1].trim()))


                var gender = GetGenderIndex(arrGender[1].trim().toUpperCase(), context)
                dlgstudent.cboGender.setSelection(gender)

                dlgstudent.txtContact.setText(arrContact[1].trim())
                dlgstudent.txtParent.setText(arrParent[1].trim())
                dlgstudent.txtAddress.setText(arrAddress[1].trim())
                dlgstudent.txtEmailAddress.setText(arrEmail[1].trim())
                dlgstudent.txtSchoolStudentNo.setText(arrStudentNo[1].trim()) //                }
                //                ctr++
                //            }
                //
                //
                var i = GetNewSectionIndex(section, context)
                dlgstudent.cboSection.setSelection(i)
                dlgstudent.cboSection.isVisible = true
                ViewTextBox(true, dlgstudent)
                dlgstudent.txtStudentData.isVisible = false
            }
        }

//0617
        dlgstudent.btnCapture.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var out = Environment.getExternalStorageDirectory()
//            var section = TestCapture.viewcapture!!.cboSectionPic.getSelectedItem().toString();
//            var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
            out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
//            TestCapture.CURRENT_PAGE =
//                TestCapture.GetCurrentPage(TestCapture.PAPER_FILENAME, section, keyword) // val photoURI =
            val photoURI =
                FileProvider.getUriForFile(this, "com.ortiz.touchview.provider", out) // FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", out)
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(i, 100)
        }


            dlgstudent.btnSaveRecord.setOnClickListener {
            val buttonText: String = dlgstudent.btnSaveRecord.getText().toString()
            val studentNumber = dlgstudent.txtstudentnumber.text.toString()
            val firstName = dlgstudent.txtfirstname.text.toString()
            val lastName = dlgstudent.txtlastname.text.toString()
            val gender = dlgstudent.cboGender.getSelectedItem().toString();
            val middle = dlgstudent.txtMiddleName.text.toString()
            val contact = dlgstudent.txtContact.text.toString()
            val parentContact = dlgstudent.txtParent.text.toString()
            val address = dlgstudent.txtAddress.text.toString()
            val emailAddress = dlgstudent.txtEmailAddress.text.toString()
            val schoolStudentNo = dlgstudent.txtSchoolStudentNo.text.toString()
            val section = dlgstudent.cboSection.getSelectedItem().toString()


            val db: DatabaseHandler = DatabaseHandler(context)
            when (buttonText) {
                "SAVE RECORD" -> {
                    var status =
                        db.ManageStudent("ADD", studentNumber, firstName, lastName, middle, section, gender, contact, parentContact, address, emailAddress, schoolStudentNo)
                    studentDialog.dismiss()
                    if (status == true) { // list.add(StudentInfoModel(studentNumber, firstName, lastName, section, gender, contact, extension, parentContact, address, emailAddress, 0, ""))
                        adapter!!.notifyDataSetChanged()
                    }

                    UpdateListContent(this, "ID_ORDER")
                    adapter!!.notifyDataSetChanged()
                }

                "EDIT" -> {
                    StatusTextBox(true, dlgstudent)
                    dlgstudent.btnSaveRecord.setText("SAVE CHANGES")
                    dlgstudent.txtstudentnumber.isEnabled = false;
                }

                "SAVE CHANGES" -> {
                    var status =
                    // Log.e("EDIT", "$studentNumber, #firstName, $lastName, $grpNumber, $section, $gender")
                        // db.ManageStudent("ADD", studentNumber, firstName, lastName, grpNumber, section, gender)
                        db.ManageStudent("EDIT", studentNumber, firstName, lastName, middle, section, gender, contact, parentContact, address, emailAddress, schoolStudentNo)
                    Toast.makeText(context, position.toString() + list.size.toString() + "", Toast.LENGTH_SHORT)
                        .show(); //                    list[position].studentID = studentNumber
                    //                    list[position].firstname = firstName
                    //                    list[position].lastname = lastName
                    //                    list[position].gender = gender
                    //                    list[position].extension = middle
                    //                    adapter!!.notifyItemChanged(position)

                    //UpdateListContent(context, "ID_ORDER")
                    //                    adapter!!.notifyDataSetChanged()
                    studentDialog.dismiss()

                }
            } //end when
        }
        dlgstudent.btnCLearTextBox.setOnClickListener {
            dlgstudent.txtStudentData.setText("")
        }

        dlgstudent.btnCopyStudentData.setOnClickListener {
            val data = dlgstudent.txtStudentData.text.toString()
            Util.CopyText(context, data, "COPY")
        }

    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
//
//    }


    fun CapitalizeFirstLetter(str: String): String {

        var newS = str.toLowerCase()
        val words = newS.split(" ")

        var newStr = ""

        words.forEach {
            newStr += it.capitalize() + " "
        }
        Log.e("LET", newStr.trimEnd())
        return newStr.trimEnd()
    }


    fun ShowImportDialog(context: Context, arr: Array<String>) {
        val mydb: DatabaseHandler = DatabaseHandler(context)
        val dlgstudent = LayoutInflater.from(context).inflate(R.layout.student_import, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgstudent).setTitle("Import Score")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);
        val arrSection: Array<String> = Util.GetSectionList(context)
        Log.e("@@@", "2222")
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arr)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dlgstudent.cboSheetList.setAdapter(sectionAdapter);
        dlgstudent.cboSheetList.setSelection(sectionAdapter.getPosition("Select")) //        dlgstudent.btnExportGrades.isEnabled = false


        //            dlgstudent.rowBtnImportStudent.setOnClickListener {
        //                val section =  dlgstudent.cboSheetList.getSelectedItem().toString();
        //                val loading = ProgressDialog.show(context,"Importing Score", "Please wait")
        //                            var url = "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?action=getClassList"
        //                url = url + "&&sheet=" + section
        //                            Log.e("" , url)
        //                            var stringRequest =
        //                                StringRequest (Request.Method.GET, url, { response ->
        //                                    Log.e("@@@", response)
        //                                    Log.e("@@@", "Hwllo po")
        //
        //                                    var t = "\"" + "Section Not Found" + "\""
        //                                    var q= "\"" +  "Sheet is Missing" + "\""
        //                                    "Sheet is Missing"
        //
        //                                    Log.e("123", response + "  "+ t)
        //
        //                                    if (response.toString()==t.toString()){
        //                                        Util.Msgbox(this, "Section Not Found");
        //                                    }
        //                                    else  if (response.toString()==q.toString()){
        //                                        Util.Msgbox(this, "Sheet is Missing");
        //                                    }
        //
        //
        //                                    else {
        //                                        var obj = JSONArray(response);
        //
        //                                        var i = 0;
        //                                        var ctr = 0;
        //                                        var x = 0;
        //                                        var myScore = 0
        //                                        var status = ""
        //                                        val arr = Array<String>(100) { "" }
        //                                        val ppp = obj.getJSONObject(0)
        //                                        val iterator: Iterator<String> = ppp.keys();
        //
        //                                        while (iterator.hasNext()) {
        //                                            arr.set(ctr, iterator.next())
        //                                            ctr++;
        //                                        }
        //
        //                                        var firstName = ""
        //                                        var lastName = ""
        //                                        var extension= ""
        //                                        var section = ""
        //                                        var grpNumber = ""
        //                                        var gender = ""
        //                                        var contactNumber = ""
        //                                        var parentContcact = ""
        //                                        var  email = ""
        //                                       var  studentNumber = ""
        //                                        var address = ""
        //
        //                                        //FirstName	LastName	Extensiom	Section	GrpNumber	Gender	ContactNumber	ParentContcact	Email	LastName	Extensiom	Section	GrpNumber	Gender	ContactNumber	ParentContcact	Email
        ////                                        if (arr.indexOf( "StudNumber") == -1) {
        ////                                            Util.Msgbox(this, "Mo StudNumber Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////
        ////                                        }
        ////                                        else
        ////                                            studentNumber =arr[arr.indexOf( "StudNumber")]
        ////
        ////                                        if (arr.indexOf( "FirstName") == -1) {
        ////                                            Util.Msgbox(this, "Mo Fi Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////                                        }
        ////                                        else
        ////                                            firstName =arr[arr.indexOf( "FirstName")]
        ////
        ////                                        if (arr.indexOf( "LastName") == -1) {
        ////                                            Util.Msgbox(this, "No LastName Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////                                        }
        ////                                        else
        ////                                            lastName =arr[arr.indexOf( "LastName")]
        ////
        ////
        ////                                        if (arr.indexOf( "Extension") == -1) {
        ////                                            Util.Msgbox(this, "No Extension Column");
        ////                                            return@StringRequest
        ////                                            loading.dismiss()
        ////
        ////                                        }
        ////                                        else
        ////                                            extension =arr[arr.indexOf( "Extension")]
        ////
        ////
        ////
        ////                                        if (arr.indexOf( "GrpNumber") == -1) {
        ////                                            Util.Msgbox(this, "No GrpNumber Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////
        ////                                        }
        ////                                        else
        ////                                            grpNumber =arr[arr.indexOf( "GrpNumber")]
        ////
        ////
        ////                                        if (arr.indexOf( "Gender") == -1) {
        ////                                            Util.Msgbox(this, "No Gender Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////                                        }
        ////                                        else
        ////                                            gender =arr[arr.indexOf( "Gender")]
        ////
        ////
        ////                                        if (arr.indexOf( "ContactNumber") == -1) {
        ////                                            Util.Msgbox(this, "No ContactNumber Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////
        ////                                        }
        ////                                        else
        ////                                            contactNumber =arr[arr.indexOf( "ContactNumber")]
        ////
        ////
        ////                                        if (arr.indexOf( "ParentContcact") == -1) {
        ////                                            Util.Msgbox(this, "No ParentContcact Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////                                        }
        ////                                        else
        ////                                            parentContcact =arr[arr.indexOf( "ParentContcact")]
        ////
        ////
        ////                                        if (arr.indexOf( "Email") == -1) {
        ////                                            Util.Msgbox(this, "No Email Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////                                        }
        ////                                        else
        ////                                           email =arr[arr.indexOf( "Email")]
        ////
        ////                                        if (arr.indexOf( "Address") == -1) {
        ////                                            Util.Msgbox(this, "No Address Column");
        ////                                            loading.dismiss()
        ////                                            return@StringRequest
        ////                                        }
        ////                                        else
        ////                                            address =arr[arr.indexOf( "Address")]
        //
        //
        //
        //
        //
        //
        //                                        while (i < obj.length()) {
        //                                            val jsonObject = obj.getJSONObject(i)
        //                                            var studnum = jsonObject.getString(arr[0])
        //                                            var firstName = jsonObject.getString(arr[1])
        //                                            var lastName = jsonObject.getString(arr[2])
        //                                            var extension = jsonObject.getString(arr[3])
        //                                            var section = cboSectionSearch.getSelectedItem().toString();
        //                                            var grpNumber = jsonObject.getString(arr[4])
        //                                            var gender = jsonObject.getString(arr[5])
        //                                            var contactNumber = jsonObject.getString(arr[6])
        //                                            var parentContcact = jsonObject.getString(arr[7])
        //                                            var address = jsonObject.getString(arr[8])
        //                                            var emailAddress =  jsonObject.getString(arr[9])
        //
        //                                            val db: DatabaseHandler = DatabaseHandler(this)
        //                                            try {
        //                                                val num = studnum.toInt()
        //                                                if (db.CheckStudent(num) == "OLD") {
        //                                                    db.ManageStudentRecord(num, firstName, lastName, extension, section, grpNumber, gender, contactNumber, parentContcact, address, emailAddress, "EDIT")
        //                                                } else {
        //
        //                                                    db.ManageStudentRecord(num, firstName, lastName, extension, section, grpNumber, gender, contactNumber, parentContcact, address, emailAddress, "ADD")
        //
        //                                                }
        //                                            } catch (e: NumberFormatException) {
        //
        //                                            }
        //
        //
        //
        //                                            i++;
        //                                        } //while
        //                                        Util.Msgbox(this, "Score is successfully imported@@");
        //                                    }
        //                                }) { }
        //
        //                            val queue: RequestQueue = Volley.newRequestQueue(this)
        //                            queue.add(stringRequest)
        //                            loading.dismiss()
        //
        //                }


    }


    fun StatusTextBox(stat: Boolean, dlgstudent: View) {
        dlgstudent.txtstudentnumber.isEnabled = stat
        dlgstudent.txtfirstname.isEnabled = stat
        dlgstudent.txtlastname.isEnabled = stat
        dlgstudent.txtMiddleName.isEnabled = stat
        dlgstudent.txtContact.isEnabled = stat
        dlgstudent.cboGender.isClickable = stat
        dlgstudent.txtParent.isEnabled = stat

        dlgstudent.txtAddress.isEnabled = stat
        dlgstudent.txtEmailAddress.isEnabled = stat
        dlgstudent.txtSchoolStudentNo.isEnabled = stat
        dlgstudent.cboSection.isClickable = stat


    }

    fun ViewTextBox(stat: Boolean, dlgstudent: View) {
        dlgstudent.txtfirstname.isVisible = stat
        dlgstudent.txtlastname.isVisible = stat
        dlgstudent.txtMiddleName.isVisible = stat
        dlgstudent.txtContact.isVisible = stat
        dlgstudent.txtParent.isVisible = stat
        dlgstudent.btnCoppyParent.isVisible = stat
        dlgstudent.btnCoppyContact.isVisible = stat
        dlgstudent.cboGender.isVisible = stat
        dlgstudent.txtAddress.isVisible = stat
        dlgstudent.txtEmailAddress.isVisible = stat
        dlgstudent.cboSection.isVisible = stat
        dlgstudent.txtSchoolStudentNo.isVisible = stat
    }


    fun GetGroupIndex(search: String, context: Context): Int { // val res: Resources = resources
        val arrGroup: Array<String> = context.getResources().getStringArray(R.array.grpNumber)
        val index =
            arrGroup.indexOf(search) // Msgbox(p.toString() + "") //getResources().getStringArray(R.array.your_string_array)
        return index

    }

    fun GetSectionIndex(search: String, context: Context): Int {
        val arrSection: Array<String> = Util.GetSectionList(context)
        val index = arrSection.indexOf(search)
        return index
    }

    fun GetGenderIndex(search: String, context: Context): Int {
        val arrGender: Array<String> = context.getResources().getStringArray(R.array.gender_choice)
        val index = arrGender.indexOf(search)
        return index
    }

    fun GetNewSectionIndex(search: String, context: Context): Int {
        val arr: Array<String> = context.getResources().getStringArray(R.array.year_choice)
        val index = arr.indexOf(search)
        return index
    }


    fun Msgbox(msg: String) {
        Toast.makeText(this, "$msg", Toast.LENGTH_SHORT).show();

    }


} //class


fun DatabaseHandler.GetOriginalSection(): ArrayList<String> {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql = ""
    sql = "SELECT DISTINCT OriginalSection FROM tbstudent_info  ORDER BY OriginalSection"

    val db2 = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db2.rawQuery(sql, null)

    if (cursor.moveToFirst()) {
        do {
            var sectionCode = cursor.getString(cursor.getColumnIndex("OriginalSection"))
            sectionList.add(sectionCode)
        } while (cursor.moveToNext())
    }
    return sectionList
}


fun DatabaseHandler.GetSubjectSection(realSectionName: String): ArrayList<String> {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql = ""
    sql = """
            SELECT *
            FROM TBSECTION WHERE STATUS ='SHOW'  
            AND  RealSectionName= '$realSectionName'
    """.trimIndent()
    Log.e("SQLSS", sql)

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    Log.e("SQLSS", cursor.count.toString())
    if (cursor.moveToFirst()) {
        do {
            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionName"))
            sectionList.add(sectionCode)
        } while (cursor.moveToNext())
    }
    Log.e("SQL200", sectionList.size.toString())
    return sectionList
}

fun DatabaseHandler.GetNewStudentID(): String {
    var sql = ""
    sql = """
            SELECT *
            FROM tbstudent_info  WHERE StudentID like '2023%'  
            ORDER BY StudentID DESC 
    """.trimIndent()
    Log.e("SQL333", "@@@!!!!!")


    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    Log.e("SQL333", cursor.count.toString())
    Log.e("SQL333", sql)
    if (cursor.count == 0) {

        return "2023-001"

    } else {
        cursor.moveToFirst()
        var studID = cursor.getString(cursor.getColumnIndex("StudentID"))
        var num = studID.split("-")

        var newStudentID = num[0] + "-" + Util.ZeroPad((num[1].toInt() + 1), 3)
        return newStudentID

    }
}


fun DatabaseHandler.UpdateOriginalSection() {
    var sql = ""
    sql = """
            UPDATE 
            tbstudent_info 
             SET OriginalSection = 'CAMHI-2023'
             WHERE OriginalSection  IN ('12-PROG--2','12-PROG1-2023')
    """.trimIndent()
    SQLManage(sql, 345)


}


fun DatabaseHandler.UpdateStudentStatus(section: String, status: String) {
    var sql = ""
    sql = """
            UPDATE 
            tbstudent_info 
             SET Status = '$status'
             WHERE StudentID IN (select StudentID from tbenroll where Section='$section')
    """.trimIndent()
    SQLManage(sql, 3690)


}
