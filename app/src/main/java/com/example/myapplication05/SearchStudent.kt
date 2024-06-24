package com.example.myapplication05

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.logmain.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.search_main.*


class SearchStudent : AppCompatActivity() {
    var studentlist = arrayListOf<StudentModel>()
    val myContext = this;
    var mySearchStudentAdapter: SearchAdapter? = null;

    var sectionStatus = false;

    companion object{
        var searchStudentAdapterList = arrayListOf<StudentModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_main)

        UpdateListContent("")
        ViewRecord()

        txtStudentSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                   var searchString = txtStudentSearch.text.toString()
                UpdateListContent(searchString)
                mySearchStudentAdapter!!.notifyDataSetChanged()


            }
        })

        txtStudentSearch.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                txtStudentSearch.setText("")
            } else if (!hasFocus) {

            }
        })


    }


    private fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSearchStudent.layoutManager = layoutmanager
        mySearchStudentAdapter = SearchAdapter(this, searchStudentAdapterList)
        listSearchStudent.adapter = mySearchStudentAdapter
    }

    private fun UpdateListContent(searchString:String) {
        val dbactivity: DatabaseHandler = DatabaseHandler(this)
        val searchStudentList: List<StudentModel>
        Log.e("xxx", "Hi Hello500")
        searchStudentAdapterList.clear()
        //var searchString = ""
        searchStudentList = dbactivity.SearchStudent(searchString)
        Log.e("xxx", searchStudentList.size.toString())
        var num= 0;
        for (e in searchStudentList) {
            searchStudentAdapterList.add(StudentModel(e.studentno, e.firstname, e.lastname, e.grp, e.sectioncode, e.gender, e.extension, e.contactNumber,e.parentcontact, e.enrollmentStatus, e.address, e.emailAddress, num, e.schoolStudentNumber,  e.expand, e.middleName))
           num++;
        }


    }







}


