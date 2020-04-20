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

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        recPerson.layoutManager = layoutmanager
        var list = arrayListOf<Person>()
//
        list.add(Person("MARVIN", "MARQUEZ"))
         list.add(Person("BING", "SANTOS"))
//        list.add(Person("BOY" , "AGTA")

     //      list.add(Person("MARVIN"))
////                list.add(Person("BING"))
////                list.add(Person("BOY"))

        val adapter = NewAdapter(this, list)
        recPerson.adapter = adapter


    }
}