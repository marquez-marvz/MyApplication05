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
import kotlinx.android.synthetic.main.logmain.*
import kotlinx.android.synthetic.main.random_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.util.*
import java.util.Calendar.*

class LogActivity : AppCompatActivity() {
    var studentlist = arrayListOf<StudentModel>()
    val myContext = this;
    var mylogAdapter: LogAdapter? = null;

    var sectionStatus = false;

    companion object{
        var logList = arrayListOf<LogModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logmain)
        val section = "11-PROG-1"
        UpdateListContent(section)
        ViewRecord()

    }


    private fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listLog.layoutManager = layoutmanager
        mylogAdapter = LogAdapter(this, logList)
        listLog.adapter = mylogAdapter
    }

    private fun UpdateListContent(section: String, searchString:String="") {
        val dbactivity: TableActivity = TableActivity(this)
        val thelog: List<LogModel>
        Log.e("xxx", "Hi Hello22")
        logList.clear()
        thelog = dbactivity.GetLogList()
        Log.e("11LOG", thelog.size.toString())
        for (e in thelog) {
            logList.add(LogModel(e.LogID, e.DateTime, e.Description))
        }

    }







}


