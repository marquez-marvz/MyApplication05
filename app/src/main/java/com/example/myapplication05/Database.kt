
//git archive -o "D:\Google Drive\Android App Backup\April17-701pm.tgz"   dcb7f2fef8e618d13eee120a8997680920bb1f86
package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.database.*
import kotlinx.android.synthetic.main.dialog_student.view.*
import kotlinx.android.synthetic.main.filename.view.*
import java.io.*


class Database : AppCompatActivity() {
    companion object {
        var list = arrayListOf<StudentModel>()
    }


//
//    private var buttonaddticket: Button? = null
//    private var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database)
        Toast.makeText(this, this.toString(),  Toast.LENGTH_LONG).show();

        ViewRecord()

//        //  DatabaseUtility(this)
//
//        buttonaddticket =findViewById(R.id.btnDelete) as Button
//        buttonaddticket.setOnClickListener(this);
//
//        listView = (ListView)findViewById(R.id.lvwstudent);
//        listView.setOnItemClickListener(this);







        btnExport.setOnClickListener {
            WriteCSV()
        }


        btnImport.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.filename, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Filename to be imported")
            val mAlertDialog = mBuilder.show()

            mDialogView.btnDialogOK.setOnClickListener {
                val filename = mDialogView.txtDialogFileName.text.toString() + ".csv"
                mAlertDialog.dismiss()
                ReadCSV(filename)
            }
//
//                mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
//
//                val email = mDialogView.dialogEmailEt.text.toString()
//                val password = mDialogView.dialogPasswEt.text.toString()
//                //set the input text in TextView
            // mainInfoTv.setText("Name:"+ name +"\nEmail: "+ email +"\nPassword: "+ password)
            //   }
            //cancel button click of custom layout
//            mDialogView.dialogCancelBtn.setOnClickListener {
//                //dismiss dialog
//                mAlertDialog.dismiss()
//            }
        }

        //    btnEdit.setOnClickListener {
//            txtstudentnumber.isEnabled = false
//            txtfirstname.isEnabled = true;
//            txtlastname.isEnabled = true;
//            btnAdd.setText("SAVE CHANGES")
//            btnDelete.setVisibility(View.VISIBLE);
//            btnEdit.setVisibility(View.INVISIBLE);
//            btnDelete.setText("CANCEL")
        //   }

//     //   btnDelete.setOnClickListener {
//          //  val buttonText: String = btnDelete.getText().toString()
//            if (buttonText == "CANCEL") {
//                TextBoxClear()
//                TextBoxStatus(false)
//                btnAdd.setText("ADD")
////                btnDelete.setVisibility(View.INVISIBLE);
////                btnDelete.setText("DELETE")
//            } else if (buttonText == "DELETE") {
//                btnAdd.setText("ADD")
////                btnDelete.setVisibility(View.INVISIBLE);
////                btnEdit.setVisibility(View.INVISIBLE);
////                btnDelete.setText("DELETE")
//              // val studentNumber = txtstudentnumber.text.toString()
//                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
//                var status = databaseHandler.DeleteStudent(studentNumber)
//                TextBoxClear()
//                TextBoxStatus(false)
//                ViewRecord();
//            }
//
//        }


        lvwstudent.setOnItemClickListener { adapterView: AdapterView<*>?,
                                            view: View?, position: Int, l: Long ->
           //  ShowDialog("VIEW", position, this);
             Msgbox("Hello World")
        }
    }

    fun ViewRecord(context:Context = this) {
//        val databaseHandler: DatabaseHandler = DatabaseHandler(context)
//        val student: List<Person> = databaseHandler.GetStudentList()
//
//        list.clear()
//        for (e in student) {
//            list.add(Person( e.firstname, e.lastname))
//

//            }

//        Log.e("Hello11", context.toString())
//        Log.e("Hello11", list.toString())
//        Log.e("Hello11", R.layout.studentrow.toString())
//        lvwstudent.adapter = StudentAdapter(context, R.layout.studentrow, list)
    }


    fun Msgbox(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    fun Sample(status:String, position:Int) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }





    fun WriteCSV() {
        val FILENAME = "user_details.csv"
        val heading = "SN,FirstNane,LastName"

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
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, FILENAME)


        var fileInputStream = FileInputStream(myFile)


        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        var text: String = ""
        var line = bufferedReader.readLine()
        while (line != null) {
            text = line.toString()
            line = bufferedReader.readLine()
            var token = text.split(",").toTypedArray()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
       //     var status = databaseHandler.addStudent(token[0], token[1], token[2])
        }
        fileInputStream.close()
        ViewRecord()
    }



//            dlgstudent.btnDelete.setOnClickListener {
//                val studentNumber = dlgstudent.txtstudentnumber.text.toString()
//                val firstName = dlgstudent.txtfirstname.text.toString()
//                val lastName = dlgstudent.txtlastname.text.toString()
//                val db: DatabaseHandler = DatabaseHandler(context)
//                var status = db.ManageStudent("DELETE", studentNumber)
//                studentDialog.dismiss()
//                ViewRecord()
//            }//btndelete
//        }//btnadd

    fun DatabaseUtility(context: Context){
        val db: DatabaseHandler = DatabaseHandler(context)
        //db.getWritableDatabase()
        //db.ShowField()
    }














}

