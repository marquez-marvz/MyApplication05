package com.example.myapplication05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        btnStudent.setOnClickListener {
            val intent = Intent(this, StudentMain::class.java)
            startActivity(intent)
        }


        btnAttendance.setOnClickListener {
            val intent = Intent(this, SchedMain::class.java)
            startActivity(intent)
        }





    } //oncreate
} //class
