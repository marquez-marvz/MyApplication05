package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.database.*
import kotlinx.android.synthetic.main.dialog_student.view.*
import kotlinx.android.synthetic.main.filename.view.*
import kotlinx.android.synthetic.main.myrecycle.*
import kotlinx.android.synthetic.main.myrecycle.btnAdd
import java.io.*
import kotlinx.android.synthetic.main.myrecycle.btnExport as btnExport1
import kotlinx.android.synthetic.main.myrecycle.btnImport as btnImport1

class MyRecycle : AppCompatActivity() {
    companion object {
     var adapter: NewAdapter? = null;
     var list = arrayListOf<Person>()
 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myrecycle)
        UpdateListContent();
        ViewRecord()

        btnAdd.setOnClickListener {
            ShowDialog("ADD",this )
        }


        btnExport.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.filename, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Filename to be imported")
            val mAlertDialog = mBuilder.show()

            mDialogView.btnDialogOK.setOnClickListener {
                val filename = mDialogView.txtDialogFileName.text.toString() + ".csv"
                mAlertDialog.dismiss()
                WriteCSV(filename)
            }


        }

        btnImport.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.filename, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Filename to be imported")
            val mAlertDialog = mBuilder.show()

            mDialogView.btnDialogOK.setOnClickListener {
                val filename = mDialogView.txtDialogFileName.text.toString()
                mAlertDialog.dismiss()
                ReadCSV(filename)

            }
        }

        cboSectionSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                UpdateListContent("SECTION")
                adapter!!.notifyDataSetChanged()
            }

        }





    }


    fun WriteCSV(filename:String) {
        val FILENAME = filename
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
            var myData = mylist.studentno + "," + mylist.firstname + "," + mylist.lastname
            myData = myData + "," + mylist.sectioncode + "," + mylist.grp
            fstream.write(myData.toByteArray())
            fstream.write("\n".toByteArray())
        }
        fstream.close()
    }

    fun ReadCSV(filename:String) {
        val FILENAME = filename
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, FILENAME)

        Toast.makeText(this, filename,   Toast.LENGTH_SHORT).show();

        var fileInputStream = FileInputStream(myFile)


        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
       var text: String = ""
        var line = bufferedReader.readLine()
        while (line != null) {
            text = line.toString()
            line = bufferedReader.readLine()
            var token = text.split(",").toTypedArray()
            var saveStatus = true



            if (token.size <5)
                saveStatus = false;
            if (token[0] == "")
                saveStatus = false;
            if (token[1] == "")
                saveStatus = false;
            if (token[2] == "")
                saveStatus = false;
            if (GetGroupIndex(token[3],this) <0)
                saveStatus = false;
            if (GetSectionIndex(token[4],this) < 0 )
                saveStatus = false;

            if (saveStatus == true) {
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                var status = databaseHandler.ManageStudent(
                    "ADD",
                    token[0],
                    token[1],
                    token[2],
                    token[3],
                    token[4]
                )
                if (status == true)
                    list.add(Person(token[0], token[1], token[2], token[3],  token[4]))


            }

//            //ViewRecord()
        }
        fileInputStream.close()
        adapter!!.notifyDataSetChanged()
        Toast.makeText(this, "SUCESS",   Toast.LENGTH_SHORT).show();

      }





    fun UpdateListContent(category:String= "ALL") {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val student: List<Person>

        list.clear()
        when (category) {
            "SECTION" -> {
                var section = cboSectionSearch.getSelectedItem().toString();
                student = databaseHandler.GetStudentList("SECTION", section)
                Toast.makeText(getBaseContext(),"Hello World123",   Toast.LENGTH_SHORT).show();
            }
             else -> student = databaseHandler.GetStudentList(category)
        }


        for (e in student) {
            list.add(Person(e.studentno,  e.firstname, e.lastname,  e.grp, e.sectioncode))
        }

    }

    fun ViewRecord(){
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        recPerson.layoutManager = layoutmanager

        adapter = NewAdapter(this, list)
        recPerson.adapter = adapter
    }




    fun ShowDialog(status:String, context: Context,  person:Person?=null, position:Int=-1) {
        val dlgstudent = LayoutInflater.from(context).inflate(R.layout.dialog_student, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(dlgstudent)
            .setTitle("Manage Student")
        val studentDialog = mBuilder.show()
        studentDialog.setCanceledOnTouchOutside(false);


        if (status == "ADD") {
            dlgstudent.txtstudentnumber.setText("")
            dlgstudent.txtfirstname.setText("")
            dlgstudent.txtlastname.setText("")
            StatusTextBox(true, dlgstudent)
        } else if (status == "VIEW") {
            dlgstudent.txtstudentnumber.setText(person!!.studentno)
          dlgstudent.txtfirstname.setText(person!!.firstname)
            dlgstudent.txtlastname.setText(person!!.lastname)
            dlgstudent.cbogroup.setSelection(GetGroupIndex(person!!.grp, context))
            dlgstudent.cbosection.setSelection(GetSectionIndex(person!!.sectioncode, context))
            dlgstudent.btnSaveRecord.setText("EDIT")
        }


        dlgstudent.btnSaveRecord.setOnClickListener {
            val buttonText: String = dlgstudent.btnSaveRecord.getText().toString()
            val studentNumber = dlgstudent.txtstudentnumber.text.toString()
            val firstName = dlgstudent.txtfirstname.text.toString()
            val lastName = dlgstudent.txtlastname.text.toString()
            val grpNumber = dlgstudent.cbogroup.getSelectedItem().toString();
            val section = dlgstudent.cbosection.getSelectedItem().toString();
            val db: DatabaseHandler = DatabaseHandler(context)
            when (buttonText) {
                "SAVE RECORD" -> {
                    var status = db.ManageStudent("ADD", studentNumber, firstName, lastName, grpNumber,  section)
                    studentDialog.dismiss()
                    if (status == true) {
                        list.add(Person(studentNumber, firstName, lastName, grpNumber,  section))
                        adapter!!.notifyDataSetChanged()
                    }
                }

                "EDIT" -> {
                    StatusTextBox(true, dlgstudent)
                    dlgstudent.btnSaveRecord.setText("SAVE CHANGES")
                    dlgstudent.txtstudentnumber.isEnabled = false;
                }

                "SAVE CHANGES" -> {
                    var status = db.ManageStudent(
                        "EDIT",
                        studentNumber,
                        firstName,
                        lastName,
                        grpNumber,
                        section
                    )
                    Toast.makeText(context, position.toString() + list.size.toString() + "", Toast.LENGTH_SHORT).show();
                    list[position].studentno = studentNumber
                    list[position].firstname = firstName
                    list[position].lastname = lastName
                    list[position].grp = grpNumber
                    list[position].sectioncode = section
                    adapter!!.notifyItemChanged(position)
                    studentDialog.dismiss()

                    // ViewRecord(context)

                }
            }//end when
        }



        dlgstudent.btnClose.setOnClickListener {
            studentDialog.dismiss()
        }


    }//ShowDialog


    fun StatusTextBox(stat:Boolean, dlgstudent: View) {
        dlgstudent.txtstudentnumber.isEnabled = stat
        dlgstudent.txtfirstname.isEnabled = stat
        dlgstudent.txtlastname.isEnabled = stat
        dlgstudent.cbogroup.isClickable = stat
        dlgstudent.cbosection.isClickable = stat
    }

    fun GetGroupIndex(search:String, context:Context):Int{
        // val res: Resources = resources
        val arrGroup:Array<String> = context.getResources().getStringArray(R.array.grpNumber)
        val index = arrGroup.indexOf(search)
        // Msgbox(p.toString() + "")
        //getResources().getStringArray(R.array.your_string_array)
        return index

    }

    fun GetSectionIndex(search:String, context:Context):Int{
        val arrSection:Array<String> =  context.getResources().getStringArray(R.array.section_choice)
        val index = arrSection.indexOf(search)
        return index

    }




} //class