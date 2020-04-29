package com.example.myapplication05

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.telephony.MbmsGroupCallSession
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.sched_main.btnAdd
import java.util.*
import kotlin.text.Typography.section


class SchedMain : AppCompatActivity() {
    val db: DatabaseHandler = DatabaseHandler(this);
    companion object {
        var adapter: ScheduleAdapter? = null;
        var  scheduleList = arrayListOf<ScheduleModel>()

        fun UpdateListContent(context: Context) {
            var section: String = Util.CURRENT_SECTION
            var myDate: String = Util.CURRENT_DATE
            val monthname = myDate.substring(0, 3)
            val schedlist: List<ScheduleModel>
            val db1: DatabaseHandler = DatabaseHandler(context);
            schedlist = db1.GetScheduleList(section, monthname)

            scheduleList.clear()

            for (sched in schedlist) {
                scheduleList.add(ScheduleModel(sched.ampm, sched.myDate, sched.sectioncode, sched.renark))
            }
        }

    }

    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    val myContext: Context = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sched_main)


        SetDate()

        ViewRecord()
        db.ShowAllRecord("TBATTENDANCE_QUERY")


        //DatabaseUtility(this)
        val arrSection: Array<String> = this.getResources().getStringArray(R.array.section_choice)
        var sectionAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionSched.setAdapter(sectionAdapter);

        var mycontext = this;
        //mDisplayDate = findViewById(R.id.txtdate) as TextView
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
       cboSectionSched.setSelection(index)
        Util.Msgbox(this, index.toString() +  " " + currentSection)



        txtDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val dialog = DatePickerDialog(
                    mycontext,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day
                )
                //dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        })

        mDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
                SetDate(month, day)
                adapter!!.notifyDataSetChanged()
            }
        }


        btnAmPm.setOnClickListener {
            if (btnAmPm.getText() == "AM")
                btnAmPm.setText("PM")
            else
                btnAmPm.setText("AM")
        }


        btnAdd.setOnClickListener {
            val myDate = txtDate.text.toString()
            val ampm = btnAmPm.getText().toString();
            val section = cboSectionSched.getSelectedItem().toString();

            var status = db.ManageSched("ADD", ampm, myDate, section)
            db.AddStudetAttendance(myDate, ampm, section)
            UpdateListContent( this);
            adapter!!.notifyDataSetChanged()


        }


        cboSectionSched.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val section = cboSectionSched.getSelectedItem().toString();
                Util.CURRENT_SECTION = section;
                val myDate = txtDate.text.toString()
                UpdateListContent(mycontext);
                adapter!!.notifyDataSetChanged()
                db.SetCurrentSection(section)



            }
        }


    }

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
        val monthName = arrayOf<String>(
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV", "DEC"
        )
        //  Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year)
        val date = monthName[myMonth] + " " + Util.ZeroPad(myDay, 2)


        txtDate.setText(date)
        btnAmPm
        if (ampm == 1)
            btnAmPm.setText("PM")
        else
            btnAmPm.setText("AM")

        Util.CURRENT_DATE = txtDate.text.toString()
        Util.CURRENT_SECTION = cboSectionSched.getSelectedItem().toString();

        UpdateListContent(this)

    }


    fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSched.layoutManager = layoutmanager

        adapter = ScheduleAdapter(this, scheduleList)
        listSched.adapter = adapter
    }


}
