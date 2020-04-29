package com.example.myapplication05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val db: DatabaseHandler = DatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //     Util.DatabaseUpGrade(this)
      Util.ShowTableField(this, "tbactivity")
        ///db.SetCurrentSection("RIVERA")
       // db.CountTableRecord("tbinfo");
        // db.GetCurrentSection()
        //db.ShowAllRecord("tbinfo");
      //db.DatabaseUtil("Update tbstudent set gender='Male'")

        btnStudent.setOnClickListener {
            val intent = Intent(this, StudentMain::class.java)
            startActivity(intent)
        }


        btnAttendance.setOnClickListener {
            val intent = Intent(this, SchedMain::class.java)
            startActivity(intent)
        }


        btnSummary.setOnClickListener {
            val intent = Intent(this, SummaryMain::class.java)
            startActivity(intent)
        }

        btnActivity.setOnClickListener {
            val intent = Intent(this,  ActivityMain::class.java)
            startActivity(intent)
        }






    } //oncreate
} //class
