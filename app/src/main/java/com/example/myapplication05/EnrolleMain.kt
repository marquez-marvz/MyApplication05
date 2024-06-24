package com.example.myapplication05

//import kotlinx.android.synthetic.main.openpdf.*
//import kotlinx.android.synthetic.main.score_main.*
//import kotlinx.android.synthetic.main.student_dialog.view.*
//import kotlinx.android.synthetic.main.student_import.view.*
//import kotlinx.android.synthetic.main.student_main.*
//import kotlinx.android.synthetic.main.student_main.btnAdd
//import kotlinx.android.synthetic.main.student_main.btnImport
//import kotlinx.android.synthetic.main.task_dialog_import.view.*
//import kotlinx.android.synthetic.main.task_dialog_import.view.cboSheetList
//import kotlinx.android.synthetic.main.util_inputbox.view.*
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.enrolle_main.*

import kotlinx.android.synthetic.main.util_inputbox.view.*
import org.json.JSONArray
import java.io.*
import java.nio.channels.FileChannel


class EnrolleMain : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    val c: Context = this;
    var fileIntent: Intent? = null
    var PATH: String = ""
    val db: DatabaseHandler = DatabaseHandler(this)
    val Utilities: Util = Util(this)

    companion object {
        var adapterEnrolle: EnrolleAdapter? = null;
        var list =
            arrayListOf<EnrolleModel>() //var cbosection: Spinner? = null; //        var cbogroup: Spinner? = null;
        //        var txtsearch: EditText? = null
        //        var myBtnSubjectSection: Button? = null


        fun UpdateListContent(context: Context, category: String = "ALL", section: String = "") {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<EnrolleModel>
            var section1 = ""
            if (section == "") section1 = db2.GetCurrentSection()
            else section1 = section
            Log.e("111", category)
            list.clear()

            Log.e("AAA", category)

            if (category == "ALL") {
                student = db2.GetEnrolleList(category, section)

            } else {
                student = db2.GetEnrolleList(category, section)
            }


            for (e in student) {
                list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enrolle_main) //cbosection = findViewById(R.id.cboSectionEnrolle) as Spinner
        SetSpinnerAdapter()
        SetDefaultSection()


        val section = db.GetCurrentSection();
        val s = ""
        val db1: Grades = Grades(this)
        val school = db1.GetSchool(section)
        Log.e("School", school)
        if (school == "DEPED") {
            UpdateListContent(this, "GENDER_ORDER", section);
        } else {
            UpdateListContent(this, "LAST_ORDER", section);
        }
        ViewRecord()
        ShowStudentCount()


        var bbtnAdd = ""


        btnSearchFirst.setOnClickListener() { //            val section = cboSectionEnrolle.getSelectedItem().toString();
            //            UpdateListContent(c, "A-C", section)
            //            adapterEnrolle!!.notifyDataSetChanged()
//            Util.FOLDER_LINK =
//                "https://docs.google.com/spreadsheets/d/1OtQChfir_H4wi1UJpVANKwvil6oYZK-h2ekKldpzBSE/edit#gid=0"
//            val intent = Intent(this, Gdrive::class.java)
//            this.startActivity(intent)

            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateListContent(c, "A-C", section)
            adapterEnrolle!!.notifyDataSetChanged()

        }

        btnSearchSecond.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateListContent(c, "D-J", section)
            adapterEnrolle!!.notifyDataSetChanged()
        }
        btnSearchThird.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateListContent(c, "K-O", section)
            adapterEnrolle!!.notifyDataSetChanged()
        }
        btnSearchFourth.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateListContent(c, "P-R", section)
            adapterEnrolle!!.notifyDataSetChanged()
        }
        btnSearchFifth.setOnClickListener() {
            val section = cboSectionEnrolle.getSelectedItem().toString();
            UpdateListContent(c, "S-Z", section)
            adapterEnrolle!!.notifyDataSetChanged()
        }




        btnCheckFolderStatus.setOnClickListener() {
            val loading = ProgressDialog.show(this, "", "Please wait")
            var url =
                "https://script.google.com/a/macros/deped.gov.ph/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec";
            val section = cboSectionEnrolle.getSelectedItem().toString();
            val stringRequest: StringRequest = object :

                StringRequest(Method.POST, url, Response.Listener { response ->
                    loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
                    //                    val intent = Intent(applicationContext, MainActivity::class.java)
                    //                    startActivity(intent)

                    Log.e("@@@", response)
                    Log.e("@@@", "Hwllo po")
                    //  dlgconfirm.txtFolderLink.setText(response)
                    if (response != "Invalid Email") {
//                        db.UpdateLink(e!!.studentno, e!!.Section, response)
//                        e!!.folderLink = response


                    }


                }, Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                  val parmas: MutableMap<String, String> = HashMap()
//                    var folderName = e!!.lastname + "," + e!!.firstname
                    parmas["action"] = "GetAllFolder"
                    parmas["sectionFolder"] = db.GetSectionFolderLink(section) //   parmas["sectionFolder"] ="https://drive.google.com/drive/folders/1s0NmdPGCHeF2pKNrB9GaB95RzvHDpVq8"
                    return parmas
                }
            }

            val socketTimeOut = 50000 // u can change this .. here it is 50 seconds
            val retryPolicy: RetryPolicy =
                DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringRequest.retryPolicy = retryPolicy
            val queue = Volley.newRequestQueue(this)
            queue.add(stringRequest)

        }




        btnExport.setOnClickListener() {
            val db2: DatabaseHandler = DatabaseHandler(this)

            val db1: Grades = Grades(this)
            val student: List<EnrolleModel>
            var section = cboSectionEnrolle!!.getSelectedItem().toString();

            val school = db1.GetSchool(section)
            Log.e("school", school)

            if (school == "DEPED") student = db2.GetEnrolleList("GENDER_ORDER", section)
            else student = db2.GetEnrolleList("LAST_ORDER", section)
            var studentData = ""
            for (e in student) {
                var complete = e.lastname + "," + e.firstname
                studentData =
                    studentData + e.studentno + "\t" + e.studentID + "\t" + e.firstname + "\t" + e.lastname + "\t" + complete + "\t" + e.grpNumber + "\t" + e.gender + "\t"
                studentData = studentData + e.enrollmentStatus + "\n"
            }
            Util.ExportToGoogleSheet(this, section, studentData, "Student List", "ExportEnrolle")

            Util.FOLDER_LINK =
                "https://docs.google.com/spreadsheets/d/15ZCCyqSdlFixsa-uoYeVPB4o7gTHC_hraca1-DBUJVo/edit#gid=995341414"
            val intent = Intent(this, Link::class.java)
            this.startActivity(intent)

        }

//        btnExportLink.setOnClickListener() {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/*"); // startActivitinyForResult(intent, 100)
//            startActivityForResult(intent, AttendanceMain.PICK_FILE)
////            val db2: DatabaseHandler = DatabaseHandler(this)
////
////            val db1: Grades = Grades(this)
////            val student: List<EnrolleModel>
////            var section = cboSectionEnrolle!!.getSelectedItem().toString();
////
////            val school = db1.GetSchool(section)
////            Log.e("school", school)
////
////            if (school == "DEPED") student = db2.GetEnrolleList("GENDER_ORDER", section)
////            else student = db2.GetEnrolleList("LAST_ORDER", section)
////            var studentData = ""
////            for (e in student) {
////
////                var email= db.GetEmail (e.studentID)
////                var complete = e.lastname + "," + e.firstname
////                studentData = studentData + e.studentno + "\t" + complete + "\t" + email + "\n"
////
////            }
////            Util.ExportToGoogleSheet(this, section, studentData, "Student List", "ExportLink")
////
////            Util.FOLDER_LINK =
////                "https://docs.google.com/spreadsheets/d/1jA_OrAn4R60AZhshdQcWEmcJFMN3SO3fD38Abcaut44/edit#gid=957316462"
////            val intent = Intent(this, Link::class.java)
////            this.startActivity(intent)
//
//        }

//        btnImportLink.setOnClickListener {
//            val loading = ProgressDialog.show(this, "Importing Score", "Please wait")
//            val section = cboSectionEnrolle.getSelectedItem()
//                .toString(); //var WithDashsectionCode = section.replace(" ", "-");
//            var url =
//                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec?"
//            Log.e("@@@", "1111")
//            url = url + "action=" + "GetFolderLinK"
//            url = url + "&&section=" + section
//
//            Log.e("@@@", url)
//            var msg = "The following  student number is taken"
//            var notSave = false
//            var stringRequest = StringRequest(Request.Method.GET, url, { response ->
//                Log.e("@@@", response)
//                Log.e("@@@", "Hwllo po")
//
//
//                val str: String = response.toString()
//                val check: Boolean = "ERROR" in str
//                Log.e("124", str + "  " + check)
//                Log.e("125", check.toString())
//
//                if (check) {
//                    Util.Msgbox(this, str);
//                    loading.dismiss()
//
//                } else {
//
//                    var obj = JSONArray(response);
//
//                    var i = 0;
//                    var ctr = 0;
//                    var x = 0;
//                    var myScore = 0
//                    var status = ""
//                    val arr = Array<String>(100) { "" }
//                    val ppp = obj.getJSONObject(0)
//                    val iterator: Iterator<String> = ppp.keys();
//
//                    while (iterator.hasNext()) {
//                        arr.set(ctr, iterator.next())
//                        ctr++;
//                    }
//
//                    Log.e("126", obj.length().toString())
//                    while (i < obj.length()) {
//                        val jsonObject = obj.getJSONObject(i)
//                        var studNumber = jsonObject.getString(arr[1])
//                        var link = jsonObject.getString(arr[4])
//                        Log.e("PPP", studNumber + "   " + link)
//
//
//                        val db: DatabaseHandler = DatabaseHandler(this)
//                        try {
//
//                            db.UpdateLink(studNumber, section,  link)
//
//
//                        } catch (e: NumberFormatException) {
//
//                        }
//
//
//
//                        i++;
//                    } //while
//                    if (notSave == false) Util.Msgbox(this, "All student Record  is successfully imported");
//                    else Util.Msgbox(this, msg);
//                }
//            }) { }
//
//            val queue: RequestQueue = Volley.newRequestQueue(this)
//            queue.add(stringRequest)
//            loading.dismiss()
//
//        }



        cboSectionEnrolle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val section = cboSectionEnrolle.getSelectedItem().toString();
                UpdateListContent(c, "GENDER_ORDER", section)
                adapterEnrolle!!.notifyDataSetChanged()
                Log.e("EEE", "hi")
                Utilities.SetCurrentSection(section)
                ShowStudentCount()
            }

        }
    }


    private fun SetSpinnerAdapter() {
        val arrGroup: Array<String> = Util.GetArrayGroup()
        val db2: DatabaseHandler = DatabaseHandler(this)
        val arrSection: ArrayList<String> = db2.GetSubjectSection()
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionEnrolle.setAdapter(sectionAdapter);
    }


    fun showPopMenuMainEnrolle(v: View) {
        Log.e("POP", "HIHI")
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.enrollment_main)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val selected = item.toString()
        val section = cboSectionEnrolle.getSelectedItem().toString();
        Log.e("SSS", selected)
        if (selected == "Sort by FirstName") {
            UpdateListContent(this, "FIRST_ORDER", section)
            adapterEnrolle!!.notifyDataSetChanged()
        } else if (selected == "Sort by LastName") {
            UpdateListContent(this, "LAST_ORDER", section)
            adapterEnrolle!!.notifyDataSetChanged()
        } else if (selected == "Sort by Gender") {
            UpdateListContent(this, "GENDER_ORDER", section)
            adapterEnrolle!!.notifyDataSetChanged()
        } else if (selected == "Sort by Group") {
            UpdateListContent(this, "GRP_ORDER", section)
            adapterEnrolle!!.notifyDataSetChanged()
        }

        //adapterEnrolle!!.notifyDataSetChanged()
        return true;
    }

    fun SetDefaultSection() {
        var mycontext = this;
        var currentSection = db.GetCurrentSection();
        val arrSection: ArrayList<String> = db.GetSubjectSection()
        var index = arrSection.indexOf(currentSection)
        cboSectionEnrolle.setSelection(index)
    }

    fun ShowStudentCount() {
        var section = cboSectionEnrolle!!.getSelectedItem().toString();
        val db: DatabaseHandler = DatabaseHandler(this)
        Log.e("COUNT", section)
        txtMaleCount.setText("M=" + db.CountStudentRecord(section, "MALE"))
        txtFemaleCount.setText("F=" + db.CountStudentRecord(section, "FEMALE"))
        txtTotalCount.setText("T=" + db.CountStudentRecord(section, "TOTAL"))
        txtEnrolledCount.setText("EN=" + db.CountStudentRecord(section, "ENROLLED"))
        txtDroppedCount.setText("DR=" + db.CountStudentRecord(section, "DROPPED"))
    }



        override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultcode, data)
            if (requestCode == AttendanceMain.PICK_FILE && resultcode == Activity.RESULT_OK && data != null) {

                val uri: Uri? = data.data
                val myFile = File(uri.toString());

                val selectedFile = data.data
                val path = selectedFile!!.path
                Log.e("AAA", uri.toString())
                Log.e("AAA", path.toString())
var p = path!!.split(":")
                Log.e("AAA",p[1])
//                try {
                 /*   val data1 = Environment.getDataDirectory()
                Log.e("AAA", data1.toString())*/
                val file = File("/storage/emulated/0/" , p[1])
                val file2 = File("/storage/emulated/0/Picture" , "one2.jpg")
//                        val sourceImagePath = "/path/to/source/file.jpg"
//                        val destinationImagePath = "/path/to/destination/file.jpg"
//                        val source = File(data, sourceImagePath)
//                        val destination = File(sd, destinationImagePath)
                        /*if (source.exists()) {*/
                            val src = FileInputStream(file).channel
                            val dst = FileOutputStream(file2).channel
                            dst.transferFrom(src, 0, src.size())
                            src.close()
                            dst.close()
//                        }
//                    }
//                } catch (e: Exception) {
//                }
//               val mSelectedImagePath = getPath(uri);
//                Log.e("AAA", mSelectedImagePath.toString())
//                val f = File("/storage/emulated/0/Picture/Sample2.jpg")
//                if (!f.exists()) {
//                    try {
//                        f.createNewFile()
//                        copyFile(File(data.data?.let { getRealPathFromURI(it) }), f)
//                    } catch (e: IOException) { // TODO Auto-generated catch block
//                        e.printStackTrace()
//                    }
//                }






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

    @Throws(IOException::class)
    private fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        source = FileInputStream(sourceFile).channel
        destination = FileOutputStream(destFile).channel
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        if (source != null) {
            source.close()
        }
        if (destination != null) {
            destination.close()
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        startManagingCursor(cursor)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


    fun Drawline(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        paint.color = color

        canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint);

    }


    fun GetPath(): String {
        val con: ContextWrapper = ContextWrapper(getApplicationContext())
        val dir: File? = con.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file: File = File(dir, "Hello123.pdf")
        return file.getPath()
    }


    fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listViewStudent.layoutManager = layoutmanager
        adapterEnrolle = EnrolleAdapter(this, list)
        listViewStudent.adapter = adapterEnrolle
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


    fun Msgbox(msg: String) {
        Toast.makeText(this, "$msg", Toast.LENGTH_SHORT).show();
    }


} //class


fun DatabaseHandler.GetOriginalSection22(): ArrayList<String> {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql = ""
    sql =
        "SELECT DISTINCT  RealSectionName FROM TBSECTION WHERE STATUS ='SHOW' ORDER BY RealSectionName"

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)

    if (cursor.moveToFirst()) {
        do {
            var sectionCode = cursor.getString(cursor.getColumnIndex("RealSectionName"))
            sectionList.add(sectionCode)
        } while (cursor.moveToNext())
    }
    return sectionList
}


fun DatabaseHandler.GetSubjectSection(): ArrayList<String> {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql = ""
    sql = """
            SELECT *
            FROM TBSECTION  where schoolYear='${Util.CURRENT_SCHOOLYEAR}' and semester ='${Util.CURRENT_SEMESTER}'
       
    """.trimIndent()
    Log.e("SQLSS", sql)

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)

    if (cursor.moveToFirst()) {
        do {
            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionName"))
            sectionList.add(sectionCode)
        } while (cursor.moveToNext())
    }
    return sectionList
}





