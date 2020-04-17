package com.example.myapplication05

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.database.*
import kotlinx.android.synthetic.main.listview.*

class ListView :  AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database)
        // val listview :ListView = findViewById(R.id.lvwsample)
//        var list = mutableListOf<Model>()
//        list.add(Model("facebook", "Hallow", R.drawable.facebook))
//        list.add(Model("Twitter", "Tweet Na!!", R.drawable.facebook))
//        lvwsample.adapter = MyAdapter(this, R.layout.row, list)
            var list = mutableListOf<StudentModel>()
            list.add(StudentModel("01", "Marvin", "Marquez"))
            list.add(StudentModel("02", "Glen", "Viola"))
            lvwstudent.adapter = StudentAdapter(this, R.layout.studentrow, list)


 

    }


}

