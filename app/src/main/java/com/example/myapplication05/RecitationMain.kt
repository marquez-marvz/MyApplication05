//package com.example.myapplication05
//
//class RecitationMain {}


package com.example.myapplication05

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.Student.GetOriginalSection
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.attendance_main.listAttendance
import kotlinx.android.synthetic.main.attendance_new_dialog.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.recitation_main.*
import kotlinx.android.synthetic.main.recitation_row.view.btnRecitationName
import kotlinx.android.synthetic.main.sched_main.listViewSched
import kotlinx.android.synthetic.main.score_main.pieChartActivity
import kotlinx.android.synthetic.main.task_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*
import kotlin.random.Random


class RecitationMain : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    val db: TableAttendance = TableAttendance(this);
    val db1: TableActivity = TableActivity(this);
    val dhandler: DatabaseHandler =
        DatabaseHandler(this); //    var adapterRecitation: RecitationAdapter? = null; //    var list = arrayListOf<EnrolleModel>()
    var context = this

    companion object {
        var adapterRecitation: RecitationAdapter? = null;
        var adapterRecitationDate: RecitationDateAdapter? = null;
        var adapterRecitationIndividual: RecitationIndividualAdapter? = null;
        var list = arrayListOf<EnrolleModel>()
        var individualListRecitation = arrayListOf<RecitationIndividualModel>()

        var dateRecitation = arrayListOf<RecitationDateModel>()
        var totalStudent = 0;
        var currentSection = ""
        var INDEX = 0

        var pie: PieChart? = null;


        var varTxtDate: TextView? = null;
        var varImgRecitationPicture: TouchImageView? = null;
        var varTxtFirstName: TextView? = null;
        var varTxtLasttName: TextView? = null;
        var btn_RecAbsent: Button? = null;
        var btn_RecCorrect: Button? = null;
        var btn_RecEffort: Button? = null;
        var btn_RecNoAns: Button? = null;
        var txt_StudentNo: TextView? = null;


        fun GetIndivualRecitationRecord(context: Context, studentno: String) {
            val dhandler: DatabaseHandler = DatabaseHandler(context);
            val student: List<RecitationIndividualModel>
            Log.e("3489", "GetIndivualRecitationRecord")
            student = dhandler.GetIndivualRecitation(studentno)
            Log.e("3489", individualListRecitation.size.toString())
            individualListRecitation.clear()
            for (e in student) {
                individualListRecitation.add(e)
            }
        }

        //0623
        fun GetRecitationDateRecord(context: Context, section: String) {
            val dhandler: DatabaseHandler = DatabaseHandler(context);
            val dateRect: List<RecitationDateModel>
            Log.e("3489", "GetIndivualRecitationRecord")
            dateRect = dhandler.GetRecitationDate(section)
            Log.e("3489", dateRecitation.size.toString())
            dateRecitation.clear()
            for (e in dateRect) {
                dateRecitation.add(e)
            }
        }

        fun ShowChart(context: Context) {
            val db: DatabaseHandler = DatabaseHandler(context)
            var section = db.GetCurrentSection();
            var remarkList = arrayListOf<PieEntry>()
            Log.e("4532", Util.ACT_CURRENT_SECTION + "    " + Util.ACT_CODE)
            var theDate = varTxtDate!!.text.toString()
            remarkList = db.GetRecitationCount(section, theDate)
            Chart.SetUpPieRecitation(pie, remarkList, "RECITATION" + "\n" + "")
            pie!!.notifyDataSetChanged();
            pie!!.invalidate();

        }


        fun UpdateListContent(context: Context, category: String = "ALL", section: String = "") {
            val dhandler: DatabaseHandler = DatabaseHandler(context);
            val student: List<EnrolleModel>
            list.clear()
            student = dhandler.GetEnrolleList(category, section)
            totalStudent = student.count()

            for (e in student) {
                if (category == "ALL" || category == "RANDOM_ORDER" ||  category == "LAST_ORDER" ) {
                    list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink, e.randomNum))

                } else {
                    var theDate = varTxtDate!!.text.toString()
                    var rem = dhandler.GetRecordRecitation(e.studentno, theDate, e.Section)
                    if (rem == category) {
                        list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink, e.randomNum))
                    }
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recitation_main)
        SetSpinnerAdapter()

        var mycontext = this; //mDisplayDate = findViewById(R.id.txtdate) as TextView
        currentSection = dhandler.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSection.setSelection(index)
        SetDate()
        val db3: Grades = Grades(this)
        val school = db3.GetSchool(currentSection)


        //0623


        varTxtDate = findViewById(R.id.txtDate) as TextView
        varImgRecitationPicture = findViewById(R.id.imgRecitationPicture) as TouchImageView
        varTxtFirstName = findViewById(R.id.txtFirstName) as TextView
        varTxtLasttName = findViewById(R.id.txtLasttName) as TextView
        txt_StudentNo = findViewById(R.id.txtStudentNo) as TextView

        btn_RecAbsent = findViewById(R.id.btnRecAbsent) as Button
        btn_RecCorrect = findViewById(R.id.btnRecCorrect) as Button
        btn_RecEffort = findViewById(R.id.btnRecEffort) as Button
        btn_RecNoAns = findViewById(R.id.btnRecNoAns) as Button
        pie = findViewById(R.id.pieChartRecitation) as PieChart

        btn_RecAbsent!!.isVisible = false
        btn_RecCorrect!!.isVisible = false
        btn_RecEffort!!.isVisible = false
        btn_RecNoAns!!.isVisible = false
        varImgRecitationPicture!!.isVisible = false




        UpdateListContent(this, "LAST_ORDER", currentSection);
        GetRecitationDateRecord(this, currentSection)
        GetIndivualRecitationRecord(this, "")
        SetupAllAdapter()
        ShowChart(this)


        //0624
        btnRandom.setOnClickListener {

            showPopMenu(it)
        }

        btnCopy.setOnClickListener {
            var x = 1;
            var str = ""
            for (e in list) {
                str = str + x + " " + e.lastname + "," + e.firstname + "\n"
                x++;
            }
            Log.e("106", str)
            Util.CopyText(this, str, str)

        }


        //0622
        btnRecAbsent.setOnClickListener {
            UpdateRecitation("ABSENT")
            adapterRecitation!!.notifyDataSetChanged()
        }

        btnAllStudent.setOnClickListener {
            UpdateListContent(mycontext, "ALL", currentSection);
            adapterRecitation!!.notifyDataSetChanged() //
        }



        btnRecCorrect.setOnClickListener {
            UpdateRecitation("CORRECT")
            adapterRecitation!!.notifyDataSetChanged()
        }

        btnRecNoAns.setOnClickListener {
            UpdateRecitation("NO ANS")
            adapterRecitation!!.notifyDataSetChanged()
        }

        btnRecEffort.setOnClickListener {
            UpdateRecitation("EFFORT")
            adapterRecitation!!.notifyDataSetChanged()
        }

        //0623
        pieChartRecitation!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                var remark = (e as PieEntry).label
                if (remark == "NONE") remark = "-"
                UpdateListContent(mycontext, remark, currentSection);
                adapterRecitation!!.notifyDataSetChanged() //                ScoreUpdateListContent(myContext, "REMARK", remark)
            }

            override fun onNothingSelected() {}
        })


        //0623
        btnStudentorDate.setOnClickListener {
            var sss = btnStudentorDate.text.toString()
            if (sss == "STUDENT") {
                listRecitation.isVisible = false
                listRecitationDate.isVisible = true
                btnStudentorDate.text = "DATE"
            } else {
                listRecitation.isVisible = true
                listRecitationDate.isVisible = false
                btnStudentorDate.text = "STUDENT"
            }

        }

        cboSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val section = cboSection.getSelectedItem().toString()
                UpdateListContent(context, "ALL", section);
                adapterRecitation!!.notifyDataSetChanged()
                val mydb: DatabaseHandler = DatabaseHandler(context)
                mydb.SetCurrentSection(section)
            }
        }

    }

    fun showPopMenu(v: View) {
        val popup = PopupMenu(context, btnRandom, Gravity.RIGHT)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.menu_recitation_order)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val selected = item.toString() //

        if (selected == "RANDOM ORDER") {
            UpdateListContent(this, "RANDOM_Ogit RDER", currentSection);
            adapterRecitation!!.notifyDataSetChanged()
        }

        if (selected == "LAST NAME ORDER") {
            UpdateListContent(this, "LAST_ORDER", currentSection);
            adapterRecitation!!.notifyDataSetChanged()
        }

        if (selected == "NEW RANDOM") {
            RandomNumbers();
            UpdateListContent(this, "RANDOM_ORDER", currentSection);
            adapterRecitation!!.notifyDataSetChanged()
        }
        return true
    }

    fun UpdateRecitation(status: String) {
        val dhandler: DatabaseHandler = DatabaseHandler(context);
        var theDate = RecitationMain.varTxtDate!!.text.toString()
        var studentNO = RecitationMain.txt_StudentNo!!.text.toString()
        var section = dhandler.GetCurrentSection();
        dhandler.RecordRecitation(studentNO, theDate, status, section, this)

        btn_RecAbsent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))
        btn_RecCorrect!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))
        btn_RecEffort!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))
        btn_RecNoAns!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")))

        if (status == "CORRECT") {
            btn_RecCorrect!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6"))) // itemView.btnRecitationName.setBackgroundColor(Color.parseColor("#FFB74D"))
        } else if (status == "EFFORT") {
            btn_RecEffort!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))

        } else if (status == "NO ANS") {
            btn_RecNoAns!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))

        } else if (status == "ABSENT") {
            btn_RecAbsent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FCF55F")))

        }

        GetIndivualRecitationRecord(context, studentNO)
        adapterRecitationIndividual!!.notifyDataSetChanged()
        ShowChart(this)
    }


    //    fun ShowPicture(dlgRecite: View, studName: String) {
    //        val dbhandler: DatabaseHandler = DatabaseHandler(this);
    //        var section = dbhandler.GetRealSection(currentSection)
    //        try {
    //            val path = "/storage/emulated/0/Picture/" + section
    //            Log.e("sss", path)
    //            Log.e("sss", studName + ".jpg")
    //
    //            val f: File = File(path, studName + ".jpg")
    //            if (f.exists()) {
    //                val b = BitmapFactory.decodeStream(FileInputStream(f))
    //                dlgRecite.imgStudent.setImageBitmap(b)
    //            } else {
    //                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
    //                val b = BitmapFactory.decodeStream(FileInputStream(f))
    //                dlgRecite.imgStudent.setImageBitmap(b)
    //            }
    //            Log.e("sss", path)
    //            Log.e("sss", studName + ".jpg")
    //
    //            // val img = findViewById<View>(R.id.imgStudent) as ImageView
    //
    //        } catch (e: FileNotFoundException) {
    //            e.printStackTrace()
    //        }
    //    }


    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSection.setAdapter(sectionAdapter);

    }

    //0623-7


    fun SetDate(month: Int = 0, day: Int = 0) {
        var myMonth: Int
        var myDay: Int
        val cal = Calendar.getInstance()
        if (month == 0 && day == 0) {
            myMonth = cal.get(Calendar.MONTH)
            myDay = cal.get(Calendar.DAY_OF_MONTH)

        } else {
            myMonth = month
            myDay = day
        }
        var ampm = cal.get(Calendar.AM_PM)
        val monthName =
            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC") //  Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year)
        val date = monthName[myMonth] + " " + Util.ZeroPad(myDay, 2)


        txtDate.setText("JUN 24") //        btnAmPm
        //        if (ampm == 1) btnAmPm.setText("PM")
        //        else btnAmPm.setText("AM")

        Util.CURRENT_DATE = txtDate.text.toString()
        Util.CURRENT_SECTION = cboSection.getSelectedItem().toString();
        Log.e("OKOK", "123456") //  SchedMain.UpdateListContent(this, "MONTH", monthName[myMonth])

    }

    //0623
    fun SetupAllAdapter() {
        val layoutmanager = GridLayoutManager(this, 4)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listRecitation.layoutManager = layoutmanager
        adapterRecitation = RecitationAdapter(this, list)
        listRecitation.adapter = adapterRecitation

        val layoutmanager2 = LinearLayoutManager(this)
        layoutmanager2.orientation = LinearLayoutManager.VERTICAL;
        listIndividualRecitation.layoutManager = layoutmanager2
        adapterRecitationIndividual = RecitationIndividualAdapter(this, individualListRecitation)
        listIndividualRecitation.adapter = adapterRecitationIndividual

        val layoutmanager3 = GridLayoutManager(this, 4)
        layoutmanager3.orientation = LinearLayoutManager.VERTICAL;
        listRecitationDate.layoutManager = layoutmanager3
        adapterRecitationDate = RecitationDateAdapter(this, dateRecitation)
        listRecitationDate.adapter = adapterRecitationDate


    }


    fun RandomNumbers() {

        val numberArray = IntArray(totalStudent)
        val statusArray = BooleanArray(totalStudent)
        val studentNoArray = Array<String>(totalStudent) { "" }
        var randomNumber = 0





        Log.e("Total:", totalStudent.toString())
        for (i in 0..totalStudent - 1) {
            do {
                randomNumber = Random.nextInt(0, totalStudent)
            } while (statusArray[randomNumber] == true)
            statusArray[randomNumber] = true
            numberArray[i] = randomNumber
        }

        for (i in 0..totalStudent - 1) {
            dhandler.UpdateRandomNumber(list[i].studentno, numberArray[i] + 1, currentSection)
        }

    }
}


fun DatabaseHandler.UpdateRandomNumber(studentno: String, num: Int, section: String) {
    var sql = """
                          update tbenroll
                          set Number='$num'
                          where  StudentNo='$studentno'
                          and section='$section'
                         """.trimIndent()
    Log.e("AAA", sql)
    val db = this.writableDatabase
    db.execSQL(sql)

}


fun DatabaseHandler.RecordRecitation(studentno: String, theDate: String, remark: String, section: String, c: Context) { //StudentNo	Date	Points	SectionCode	Remark
    RecordRecitatioDate(section, theDate, c)
    var sql = """
                          select * from tbrecitation 
                          where  StudentNo='$studentno'
                          and SectionCode='$section'
                          and Date='$theDate'
                          """
    val db = this.readableDatabase
    val db2 = this.writableDatabase
    val cursor = db.rawQuery(sql, null)
    Util.Msgbox(context, cursor.count.toString()) //    GetRecordRecitation(studentno, theDate, section)
    if (cursor.count == 0) {
        sql = """ 
           insert into tbrecitation(StudentNo,	Date,	Points,	SectionCode,	Remark)
           values('$studentno', '$theDate', 0, '$section', '$remark')
       """.trimIndent()
        Log.e("sql", sql)
        db2.execSQL(sql)
    } else {
        var sql = """
                          update tbrecitation
                          set Remark='$remark'
                          where  StudentNo='$studentno'
                          and sectionCode='$section'
                          and Date='$theDate'
                         """.trimIndent()
        Log.e("AAA", sql)
        val db = this.writableDatabase
        db2.execSQL(sql)
    }
}


fun DatabaseHandler.RecordRecitatioDate(sectioo: String, theDate: String, c: Context) {
    var month = "JUN"
    var monthCode = ""
    var monthDate = "23"

    if (month == "JUN") monthCode = "01"
    if (month == "JUL") monthCode = "02"
    if (month == "AUG") monthCode = "03"
    if (month == "SEP") monthCode = "04"
    if (month == "OCT") monthCode = "05"
    if (month == "NOV") monthCode = "06"
    if (month == "DEC") monthCode = "07"
    if (month == "JAN") monthCode = "08"
    if (month == "FEB") monthCode = "09"
    if (month == "MAR") monthCode = "10"
    if (month == "APR") monthCode = "11"
    if (month == "MAY") monthCode = "12"

    var schedCode = monthCode + monthDate
    var gradingPeriod = Util.GetCurrentGradingPeriod(c)
    var sql = """
                          select * from tbrecitation_date
                          where  Section='$sectioo'
                          and RecitationDate='$theDate'
                         """
    val db = this.readableDatabase
    val db2 = this.writableDatabase
    val cursor = db.rawQuery(sql, null)

    if (cursor.count == 0) {
        sql = """ 
           insert into tbrecitation_date(SchedCode,	RecitationDate,	GradingPeriod,	Section)
           values('$schedCode', '$theDate', '$gradingPeriod', '$sectioo')
       """.trimIndent()
        Log.e("sql", sql)
        db2.execSQL(sql)
    }

}


fun DatabaseHandler.GetRecordRecitation(studentno: String, theDate: String, section: String): String {
    var sql = """
        select * from tbrecitation where StudentNo ='$studentno'
        and SectionCode ='$section'
        and Date ='$theDate'
        """
    val db = this.readableDatabase
    val cursor = db.rawQuery(sql, null)
    val columnNames: Array<String> = cursor.getColumnNames()
    Log.e("AAA", Arrays.toString(columnNames))
    if (cursor.count == 0) {
        Log.e("REMMM", "-")
        return "-"
    } else {
        cursor.moveToFirst()
        val rem = cursor.getString(cursor.getColumnIndex("Remark"))
        Log.e("REMMM", rem)
        return rem
    }
}

//0623
fun DatabaseHandler.GetIndivualRecitation(studentno: String): ArrayList<RecitationIndividualModel> {

    val studentList: ArrayList<RecitationIndividualModel> = ArrayList<RecitationIndividualModel>()
    var sql: String = """ SELECT  * FROM tbrecitation_query
                                where StudentNo='$studentno'  order by SchedCode """


    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    Log.e("3489", cursor.count.toString())
    Log.e("3489", sql)
    var num = 1;
    if (cursor.moveToFirst()) {
        do {
            var rec: RecitationIndividualModel = RecitationIndividualModel();
            rec.studentno = cursor.getString(cursor.getColumnIndex("StudentNo"))
            rec.date = cursor.getString(cursor.getColumnIndex("Date"))
            rec.sectioncode = cursor.getString(cursor.getColumnIndex("SectionCode"))
            rec.firstname = cursor.getString(cursor.getColumnIndex("FirstName"))
            rec.lastname = cursor.getString(cursor.getColumnIndex("LastName"))
            rec.renmark = cursor.getString(cursor.getColumnIndex("Remark"))
            rec.gradingPeriod = cursor.getString(cursor.getColumnIndex("GradingPeriod"))
            studentList.add(rec)
        } while (cursor.moveToNext())
    }
    return studentList
}


//0623
fun DatabaseHandler.GetRecitationDate(section: String): ArrayList<RecitationDateModel> {

    val dateList: ArrayList<RecitationDateModel> = ArrayList<RecitationDateModel>()
    var sql: String = """ SELECT  * FROM tbrecitation_date
                                where section='$section'  order by SchedCode """


    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    Log.e("3489", cursor.count.toString())
    Log.e("3489", sql)
    var num = 1;
    if (cursor.moveToFirst()) {
        do {
            var rec: RecitationDateModel = RecitationDateModel();
            rec.schedCode = cursor.getString(cursor.getColumnIndex("SchedCode"))
            rec.recitationDate = cursor.getString(cursor.getColumnIndex("RecitationDate"))
            rec.gradingPeriod = cursor.getString(cursor.getColumnIndex("GradingPeriod"))
            rec.section = cursor.getString(cursor.getColumnIndex("Section"))
            dateList.add(rec)
        } while (cursor.moveToNext())
    }
    return dateList
}




