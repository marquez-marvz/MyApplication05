package com.example.myapplication05

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.random_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.util.*
import java.util.Calendar.*

class RandomMain : AppCompatActivity() {
    var studentlist = arrayListOf<EnrolleModel>()
    val myContext = this;
    var randomAdapter: RandomAdapter? = null;

    var sectionStatus = false;

    companion object{
        var randomList = arrayListOf<RandomModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_main)
        SetSpinnerAdapter()
        SetDefaultSection()
        val section = GetCurrentSection()
        CheckRandom(section)
        GetRandomcode()


        txtSearch.addTextChangedListener(object : TextWatcher {
                  override fun afterTextChanged(p0: Editable?) {}

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        UpdateListContent(section, txtSearch.text.toString())
                        randomAdapter!!.notifyDataSetChanged()
                    }
                })



        btnReset.setOnClickListener {

            val dlgconfirm = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgconfirm)
                .setTitle("Do you like to  reset the random?")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {
                val db: TableRandom = TableRandom(this)
                var section = GetCurrentSection()
                db.DeleteRandom(section);
                RandomStudents(section, db, randomList.size)
                UpdateListContent(section)
                randomAdapter!!.notifyDataSetChanged()
                confirmDialog.dismiss()
            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()

            }
        }


        btnDelete.setOnClickListener {

            val dlgconfirm = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgconfirm)
                .setTitle("Do you like to  delete the recitation for the section?")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {
                val db: TableRandom = TableRandom(this)
                var section = GetCurrentSection()
                db.DeleteSectionRecitation(section);
                randomAdapter!!.notifyDataSetChanged()
                confirmDialog.dismiss()

            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()

            }
        }


        cboSectionRandom.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                           sectionStatus = true
                            return false
                    }
         })


        cboSectionRandom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if (sectionStatus == true) {
                   val db: TableRandom = TableRandom(myContext)
                   val section = GetCurrentSection()
                   GetStudentSectionList()
                   RandomStudents(section, db, studentlist.size)
                   UpdateListContent(section)
                   randomAdapter!!.notifyDataSetChanged()
                   sectionStatus = false
               }
            }
        }
    }

    fun CheckRandom(section: String) {
        val db: TableRandom = TableRandom(this)
        if (db.CountSectionRandom(section) == 0) {
            GetStudentSectionList()
            RandomStudents(section, db, studentlist.size)
            UpdateListContent(section)
            ViewRecord()

        } else {
            GetStudentSectionList();
            UpdateListContent(section)
            ViewRecord()
        }
    }

    private fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listRandom.layoutManager = layoutmanager
        randomAdapter = RandomAdapter(this, randomList)
        listRandom.adapter = randomAdapter
    }

    private fun UpdateListContent(section: String, searchString:String="") {
        val db: TableRandom = TableRandom(this)
        val rnd_list: List<RandomModel> = db.GetRandomList(section, searchString)
        randomList.clear()

        for (e in rnd_list) {
            randomList.add(RandomModel(e.sequenceNumber, e.sectioncode, e.studentNo, e.completeName, e.recitationCount, e.remark, e.randomCode))
    }
    }


    private fun GetStudentSectionList() {
        val db: TableRandom = TableRandom(this)
        val student: List<EnrolleModel>
        val section = GetCurrentSection()
        student = db.GetEnrolleList("SECTION", section)

        studentlist.clear()
        var num = 1;
        for (e in student) {
            studentlist.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus,
                          e.studentID, e.grpNumber, e.folderLink))
            num++;
        }

        Util.Msgbox(this, studentlist.size.toString())

    }

    private fun GetCurrentSection(): String {
        val section = cboSectionRandom.getSelectedItem().toString();
        return section
    }

    private fun RandomStudents(section: String, db: TableRandom, studentCount: Int) {

        var flag = Array(studentCount) { false }
        var randomNumber = Array(studentCount) { 0 }
        var num = 0;
        for (i in 0..studentCount - 1) {
            do {
                num = (0 until studentCount).random()

            } while (flag[num] != false);

            flag[num] = true;
            randomNumber[i] = num;
        }

        var randomCode = GetRandomcode()
        for (i in 0..studentCount - 1) {
            var ctr = randomNumber[i]
            val sequenceNumber = Util.ZeroPad(i + 1, 2)
            Log.e("RND", sequenceNumber + " " + section + " " + studentlist[ctr].studentno)
            db.ManageRandom(sequenceNumber, section, studentlist[ctr].studentno, "-", randomCode)

        }
    }

    private fun GetRandomcode(): String {
        val cal =  Calendar.getInstance()
        var myMonth = Util.ZeroPad(cal.get(MONTH) + 1, 2)
        var myDay =  Util.ZeroPad(cal.get(DAY_OF_MONTH), 2)
        val mySecond=  Util.ZeroPad(cal.get(Calendar.SECOND), 2);
        var randomCode =  myMonth.toString()  + ""  + myDay.toString() + "" + mySecond.toString()
        Log.e("XCAL", randomCode)
        return randomCode
    }

    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionRandom.setSelection(index)
    }

    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionRandom.setAdapter(sectionAdapter);

    }


}


