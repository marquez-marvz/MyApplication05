package com.example.myapplication05

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.reading.*
import java.io.*

class Reading : AppCompatActivity() {
    val myContext: Context = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reading)

        btnsave.setOnClickListener {
            //            var fos: FileOutputStream? = null
            //            val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            //            var  FILE_NAME = folder.toString() + "/" + "example3.txt"
            //            try {
            //                fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            //                fos.write("Hello".toByteArray())
            //
            //                Toast.makeText(this, "Saved to $filesDir/$FILE_NAME", Toast.LENGTH_LONG).show()
            //            } catch (e: FileNotFoundException) {
            //                e.printStackTrace()
            //            } catch (e: IOException) {
            //                e.printStackTrace()
            //            } finally {
            //                if (fos != null) {
            //                    try {
            //                        fos.close()
            //                    } catch (e: IOException) {
            //                        e.printStackTrace()
            //                    }
            //                }
            //            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You have already granted this write permission!", Toast.LENGTH_SHORT)
                    .show();
            } else {
                //requestStoragePermission();
                Toast.makeText(this, "You have not  already granted this write permission!", Toast.LENGTH_SHORT)
                    .show();
            }
            Log.e("Yes", "reading10");
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 23)

            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myFile = File(folder, "qw.csv")
            val fstream = FileOutputStream(myFile)
            fstream.write("567890".toByteArray())
            fstream.write("\n".toByteArray())
            fstream.close()
        }

        btnload.setOnClickListener {
            //            val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            //            val myFile = File(mypath, filename)
            //            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)
            //
            //
            //            try {
            //                Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
            //
            //                var fileInputStream = FileInputStream(myFile)
            //
            //
            //                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            //                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            //                var text: String = ""
            //                var line = bufferedReader.readLine()
            //                var errMessage = ""
            //                while (line != null) {
            //                    text = line.toString()
            //                    line = bufferedReader.readLine()
            //                    var token = text.split(",").toTypedArray()
            //                    var saveStatus = true
            //
            //
            //
            //                    if (token.size < 6) {
            //                        saveStatus = false;
            //                        errMessage = errMessage + "$line-Missing Token\n"
            //                    } else if (token[0] == "") {
            //                        saveStatus = false
            //                        errMessage = errMessage + "$line- Student No is Blank\n"
            //                    } else if (token[1] == "") {
            //                        saveStatus = false
            //                        errMessage = errMessage + "$line- Firstnname is Blank\n"
            //                    } else if (token[2] == "") {
            //                        saveStatus = false
            //                        errMessage = errMessage + "$line- Lastnname is Blank\n"
            //                    } else if (GetGroupIndex(token[3], this) < 0) {
            //                        saveStatus = false
            //                        errMessage = errMessage + "$line-Grp  is Invaid"
            //
            //                    } else if (GetSectionIndex(token[4], this) < 0) {
            //                        saveStatus = false
            //                        errMessage = errMessage + "$line-Section  is Invaid"
            //                    } else if (GetGenderIndex(token[5], this) < 0) {
            //                        saveStatus = false
            //                        errMessage = errMessage + "$line-Gender  is Invaid"
            //                    }
            //                    if (saveStatus == true) {
            //                        var status =
            //                            db.ManageStudent("ADD", token[0], token[1], token[2], token[3], token[4], token[5])
            //                        StudentMain.UpdateListContent(this)
            //                        StudentMain.adapter!!.notifyDataSetChanged()
            //
            //                        //if (status == true)
            //                        //  list.add(StudentModel(token[0], token[1], token[2], token[3], token[4]))
            //
            //
            //                    }
            //
            //                    //            //ViewRecord()
            //                }
            //                fileInputStream.close()
            //                StudentMain.adapter!!.notifyDataSetChanged()
            //
            //
            //
            //            }
        }
    }
}


