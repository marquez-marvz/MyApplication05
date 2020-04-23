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
import kotlinx.android.synthetic.main.dialog_student.view.*
import kotlinx.android.synthetic.main.myrecycle.*
import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.sched_main.btnAdd
import java.util.*
import kotlin.text.Typography.section


class SchedMain : AppCompatActivity() {
    companion object {
        var adapter: ScheduleAdapter? = null;
        var list = arrayListOf<ScheduleModel>()

        fun UpdateListContent(section: String, myDate:String,  context: Context) {
            val databaseHandler: DatabaseHandler = DatabaseHandler(context)
            val monthname =  myDate.substring(0, 3)
            val schedlist: List<ScheduleModel>
            schedlist = databaseHandler.GetScheduleList(section,  monthname)

            list.clear()

            for (sched in schedlist) {
                list.add(ScheduleModel(sched.ampm, sched.myDate, sched.sectioncode, sched.renark))
            }
        }

    }

    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    val myContext:Context = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sched_main)

        //Util.DatabaseUpGrade(this)
        //Util.ShowTableField(this, "tbsched")
        SetDate()

        ViewRecord()


        //DatabaseUtility(this)
        val arrSection: Array<String> = this.getResources().getStringArray(R.array.section_choice)

        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.spinner_choice, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionSched.setAdapter(sectionAdapter);

        var mycontext = this;
        //mDisplayDate = findViewById(R.id.txtdate) as TextView
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
            val db: DatabaseHandler = DatabaseHandler(this);
            var status = db.ManageSched("ADD", ampm, myDate, section)
            UpdateListContent(section,myDate, this);
            adapter!!.notifyDataSetChanged()

        }


        cboSectionSched.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val section = cboSectionSched.getSelectedItem().toString();
                val myDate = txtDate.text.toString()
                UpdateListContent(section,myDate, mycontext);
                adapter!!.notifyDataSetChanged()
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

        val section = cboSectionSched.getSelectedItem().toString();
        val myDate = txtDate.text.toString()
        UpdateListContent(section, myDate, this)

    }


    fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSched.layoutManager = layoutmanager

        adapter = ScheduleAdapter(this, list)
        listSched.adapter = adapter
    }


}
