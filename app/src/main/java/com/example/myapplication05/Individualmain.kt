package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.attendance_main.btnCountAbsent
import kotlinx.android.synthetic.main.attendance_main.btnCountExcuse
import kotlinx.android.synthetic.main.attendance_main.btnCountLate
import kotlinx.android.synthetic.main.attendance_main.btnCountPresent
import kotlinx.android.synthetic.main.individual.*
import kotlinx.android.synthetic.main.individual.view.*
import kotlinx.android.synthetic.main.individual.view.listIndividual

import kotlinx.android.synthetic.main.sched_main.*
import kotlinx.android.synthetic.main.summary_main.*



class Individualmain : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val db: DatabaseHandler = DatabaseHandler(this)
        setContentView(R.layout.individual)
        var individualList: List<IndividualModel> = db.GetIndividuaList("APR", "1688")

        var idx= arrayListOf<IndividualModel>()
        for (att in individualList) {
            idx.add(IndividualModel(att.ampm, att.myDate,  att.attendanceStatus, att.remark))
            Log.e("LAOG", att.ampm + " " + att.myDate + " " +  att.attendanceStatus + " "  +  att.remark)
        }

        Util.Msgbox(this, idx.size.toString() + " Hello" )





        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listIndividual.layoutManager = layoutmanager
        var adapter = IndividualAdapter(this, idx)
        listIndividual.adapter = adapter
    }
}
