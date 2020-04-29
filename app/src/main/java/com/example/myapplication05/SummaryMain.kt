package com.example.myapplication05

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.summary_main.*

import java.util.*

class SummaryMain : AppCompatActivity() {
    val db: DatabaseHandler = DatabaseHandler(this)
    var GRP_STATUS: Boolean = false;
    var MONTH_STATUS: Boolean = false;

    companion object {
        var summaryAdapter: SummaryAdapter? = null;
        var summaryList = arrayListOf<SummaryModel>()
        var cboMonth: Spinner? = null;

        fun GetMonth():String  {
           return cboMonth!!.getSelectedItem().toString();
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.summary_main)
        SetSpinnerAdapter()
        SetCurrentMonth()
        SummaryUpdateListContent("SECTION")
        SummaryViewRecord()
        cboMonth = findViewById(R.id.cboMonthSummary) as Spinner


        cboSectionSummary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SummaryUpdateListContent("SECTION")
                var section = cboSectionSummary.getSelectedItem().toString();
                summaryAdapter!!.notifyDataSetChanged()
                db.SetCurrentSection(section)
                cboGroupSummary.setSelection(0)
                txtSummarySearch.setText("")
                SetCurrentMonth()
            }
        }

        cboGroupSummary.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                Toast.makeText(getBaseContext(), "CLICK", Toast.LENGTH_SHORT).show()
                GRP_STATUS = true;
                return false
            }
        })

        cboGroupSummary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (GRP_STATUS == true) {
                    SummaryUpdateListContent("GROUP")
                    summaryAdapter!!.notifyDataSetChanged()
                    GRP_STATUS == false
                }
            }
        }

        cboMonthSummary.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                MONTH_STATUS = true;
                return false
            }
        })

        cboMonthSummary.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (MONTH_STATUS == true) {
                    SummaryUpdateListContent("SECTION")
                    summaryAdapter!!.notifyDataSetChanged()
                    MONTH_STATUS == false
                    cboGroupSummary.setSelection(0)
                    txtSummarySearch.setText("")
                }
            }
        }

        txtSummarySearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                SummaryUpdateListContent("NAME")
                summaryAdapter!!.notifyDataSetChanged()
                cboGroupSummary.setSelection(0)
            }
        })


    } //oncreate

    fun SummaryViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSummary.layoutManager = layoutmanager
        summaryAdapter = SummaryAdapter(this, summaryList)
        listSummary.adapter = summaryAdapter
    }


    fun SummaryUpdateListContent(category: String) {

        var mySummaryList: List<SummaryModel>
        var section = cboSectionSummary.getSelectedItem().toString();
        var myMonth = cboMonthSummary.getSelectedItem().toString();

        when (category) {
            "GROUP" -> {
                var grp = cboGroupSummary.getSelectedItem().toString();
                if (grp == "ALL") grp = ""

                mySummaryList = db.GetCountAttendanceList(myMonth, section, "GROUP", grp)
            }

            "NAME" -> {
                var txt = txtSummarySearch.text.toString()
                mySummaryList = db.GetCountAttendanceList(myMonth, section, "NAME", txt)
            }

            else -> mySummaryList = db.GetCountAttendanceList(myMonth, section)
        }

        summaryList.clear()

        for (sm in mySummaryList) {
            summaryList.add(SummaryModel(sm.studentNo, sm.completeName, sm.prsentCount, sm.lateCount, sm.absentCount, sm.excuseCount))

        }

        //   Util.Msgbox(this, summaryList.size.toString())

    }


    fun SetSpinnerAdapter() {
        val arrSection: Array<String> = this.getResources().getStringArray(R.array.section_choice)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionSummary.setAdapter(sectionAdapter);

        val arrMonth =
            arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        var monthAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrMonth)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboMonthSummary.setAdapter(monthAdapter);


        val arrGroup = arrayOf("ALL", "G-1", "G-2", "G-3", "G-4", "G-5", "G-6", "G-7", "G-8", "G-9")
        var grpAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrGroup)
        grpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboGroupSummary.setAdapter(grpAdapter);

    }

    fun SetCurrentMonth() {
        val cal = Calendar.getInstance()
        var myMonth = cal.get(Calendar.MONTH)
        cboMonthSummary.setSelection(myMonth)
    }
} //class