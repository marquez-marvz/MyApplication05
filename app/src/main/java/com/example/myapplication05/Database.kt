package com.example.myapplication05
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.database.*
import kotlinx.android.synthetic.main.filename.view.*
import java.io.*


class Database : AppCompatActivity() {
    var list = arrayListOf<StudentModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database)
        ViewRecord()

        btnExport.setOnClickListener {
            WriteCSV()
        }


        btnImport.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.filename, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Filename to be imported")
            val  mAlertDialog = mBuilder.show()

            mDialogView.btnDialogOK.setOnClickListener {
                     val filename = mDialogView.txtDialogFileName.text.toString() + ".csv"
                     mAlertDialog.dismiss()
                    // Msgbox(filename)
                ReadCSV(filename)
            }
         //   }
        }

        btnEdit.setOnClickListener {
            txtstudentnumber.isEnabled = false
            txtfirstname.isEnabled = true;
            txtlastname.isEnabled = true;
            btnAdd.setText("SAVE CHANGES")
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
            btnDelete.setText("CANCEL")
        }

        btnDelete.setOnClickListener {
            val buttonText: String = btnDelete.getText().toString()
            if (buttonText == "CANCEL") {
                TextBoxClear()
                TextBoxStatus(false)
                btnAdd.setText("ADD")
                btnDelete.setVisibility(View.INVISIBLE);
                btnDelete.setText("DELETE")
            } else if (buttonText == "DELETE") {
                btnAdd.setText("ADD")
                btnDelete.setVisibility(View.INVISIBLE);
                btnEdit.setVisibility(View.INVISIBLE);
                btnDelete.setText("DELETE")
                val studentNumber = txtstudentnumber.text.toString()
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                var status = databaseHandler.DeleteStudent(studentNumber)
                TextBoxClear()
                TextBoxStatus(false)
                ViewRecord();
            }

        }

        btnAdd.setOnClickListener {
            val buttonText: String = btnAdd.getText().toString()
            //  Msgbox(buttonText)
            if (buttonText == "ADD") {
                TextBoxStatus(true)
                TextBoxClear()
                btnAdd.setText("SAVE RECORD")
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setText("CANCEL")
            } else {
                val studentNumber = txtstudentnumber.text.toString()
                val firstName = txtfirstname.text.toString()
                val lastName = txtlastname.text.toString()
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                var myStat: Long = 0;
                if (buttonText == "SAVE RECORD") {
                    Msgbox("Hello")
                    var status = databaseHandler.addStudent(studentNumber, firstName, lastName)
                    myStat = status;
                } else if (buttonText == "SAVE CHANGES") {
                    var status = databaseHandler.EditStudent(studentNumber, firstName, lastName)
                    myStat = status.toLong();
                }

                if (myStat > -1) {
                    btnAdd.setText("ADD")
                    btnDelete.setVisibility(View.INVISIBLE);
                    btnDelete.setText("DELETE")
                    ViewRecord();
                    TextBoxStatus(true)
                    TextBoxClear()
                }

            }
        } //btnadd


        lvwstudent.setOnItemClickListener { adapterView: AdapterView<*>?,
                                            view: View?, position: Int, l: Long ->
            var studentnum = list[position].sn
            txtstudentnumber.setText(list[position].sn)
            txtfirstname.setText(list[position].fname)
            txtlastname.setText(list[position].lname)
            TextBoxStatus(false)

            btnAdd.setText("ADD")
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setText("DELETE")

        }
    }

    fun ViewRecord() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val student: List<StudentModel> = databaseHandler.viewEmployee()
        var index = 0
        list.clear()
        for (e in student) {
            list.add(StudentModel(e.sn, e.fname, e.lname))
        }
        lvwstudent.adapter = StudentAdapter(this, R.layout.studentrow, list)
    }


    fun Msgbox(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    fun TextBoxStatus(stat: Boolean) {
        txtstudentnumber.isEnabled = stat
        txtfirstname.isEnabled = stat
        txtlastname.isEnabled = stat
    }

    fun TextBoxClear() {
        txtstudentnumber.setText("")
        txtfirstname.setText("")
        txtlastname.setText("")
    }


    fun WriteCSV() {
        val FILENAME = "user_details.csv"
        val heading = "SN,FirstNane,LastName"
        val data = "001,Marvin,Marquez"

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            23
        )

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, FILENAME)
        val fstream = FileOutputStream(myFile)
        fstream.write(heading.toByteArray())
        fstream.write("\n".toByteArray())
        for (mylist in list) {
            var myData = mylist.sn + "," + mylist.fname + "," + mylist.lname
            fstream.write(myData.toByteArray())
            fstream.write("\n".toByteArray())
        }
        fstream.close()
    }

    fun ReadCSV(filename:String) {
        val FILENAME = filename
       // Msgbox("200")

//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//            23
//        )



        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, FILENAME)


        var fileInputStream = FileInputStream(myFile)


        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        var text: String = ""
        var line = bufferedReader.readLine()
        while(line != null) {
            text = line.toString()
            line = bufferedReader.readLine()
            var token= text.split(",").toTypedArray()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            var status = databaseHandler.addStudent(token[0],token[1], token[2])
            }
        fileInputStream.close()
        ViewRecord()
     }






}

