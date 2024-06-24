package com.example.myapplication05

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.file_chooser.*


import java.util.regex.Pattern

class FileChooser: AppCompatActivity() {
    var fileIntent:Intent?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_chooser)




        btnImportNew.setOnClickListener {
          // override fun onClick(v: View) {

                fileIntent = Intent(Intent.ACTION_GET_CONTENT)
                fileIntent!!.setType("*/*")
                startActivityForResult(fileIntent, 10)

            //}
        }



    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 10 && resultCode == RESULT_OK) {
//            var path = data!!.getData()!!.getFile()
//            Util.Msgbox(this, path.toString())
//        }
//    }

}


