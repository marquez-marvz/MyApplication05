package com.example.myapplication05

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.myrecycle.*

class MyRecycle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myrecycle)
        ViewRecord()


    }

    fun ViewRecord(){
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        recPerson.layoutManager = layoutmanager
        var list = arrayListOf<Person>()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val student: List<Person> = databaseHandler.GetStudentList()


        for (e in student) {
            list.add(Person(e.studentno,  e.firstname, e.lastname, e.sectioncode, e.grp))

        }



        val adapter = NewAdapter(this, list)
        recPerson.adapter = adapter
    }
}