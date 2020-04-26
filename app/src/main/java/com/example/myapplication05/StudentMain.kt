package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_main.*

import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.io.*


class StudentMain : AppCompatActivity() {
    val c:Context = this;
    companion object {
        var adapter: StudentAdapter? = null;
        var list = arrayListOf<StudentModel>()
        var cbosection: Spinner?= null ;
        var cbogroup: Spinner?= null ;
        var txtsearch: EditText?= null ;

        fun UpdateListContent(context: Context , category:String= "ALL") {
            val databaseHandler: DatabaseHandler = DatabaseHandler(context)
            val student: List<StudentModel>

            list.clear()
            when (category) {
                "SECTION" -> {
                    var section = cbosection!!.getSelectedItem().toString();
                    var group = cbogroup!!.getSelectedItem().toString();
                    if (group== "NONE")
                        student = databaseHandler.GetStudentList("SECTION", section)
                    else
                        student = databaseHandler.GetStudentList("SECTION", section,group)
                }

                "NAME" -> {
                    var section = cbosection!!.getSelectedItem().toString();
                    var lastname = txtsearch!!.text.toString();
                    student = databaseHandler.GetStudentList("NAME", section, "", lastname)
                }
                else -> student = databaseHandler.GetStudentList(category)
            }


            for (e in student) {
                list.add(StudentModel(e.studentno,  e.firstname, e.lastname,  e.grp, e.sectioncode))
            }

        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_main)
        UpdateListContent(this);
        ViewRecord()


//        cboGroupSearch.setSelection(0)
//        cboGroupSearcval //
      val arrGroup:Array<String> = this.getResources().getStringArray(R.array.grpNumber)
       val arrSection:Array<String> =  this.getResources().getStringArray(R.array.section_choice)
        var  groupAdapter:ArrayAdapter<String>  = ArrayAdapter<String>(this,R.layout.util_spinner,arrGroup)
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboGroupSearch.setAdapter(groupAdapter);

        var  sectionAdapter:ArrayAdapter<String>  = ArrayAdapter<String>(this,R.layout.util_spinner,arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionSearch.setAdapter(sectionAdapter);

        cbosection= findViewById(R.id.cboSectionSearch) as Spinner
        cbogroup = findViewById(R.id.cboGroupSearch) as Spinner
        txtsearch = findViewById(R.id.txtSearch) as EditText
//        h.setSelection(spinnerArrayAdapter.getCount()); //set the hint the default selection so it appears on launch.






        btnAdd.setOnClickListener {
            ShowDialog("ADD",this )
        }


        btnExport.setOnClickListener {
            ImportExportFile("exported")
        }
        btnImport.setOnClickListener {
            ImportExportFile("imported")
        }

        cboSectionSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                UpdateListContent(c, "SECTION")
                adapter!!.notifyDataSetChanged()
            }

        }


        cboGroupSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                UpdateListContent(c, "SECTION")
                adapter!!.notifyDataSetChanged()
            }
        }

        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                UpdateListContent(c, "NAME")
                adapter!!.notifyDataSetChanged()
            }

    })
        txtSearch.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                var group = cboGroupSearch.getSelectedItem().toString();
                if (group != "NONE")
                    cboGroupSearch.setSelection(0)

            } else { }
        })
    }  //OnCreate


    fun ImportExportFile(type:String){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.util_inputbox, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Filename to be $type")
        val mAlertDialog = mBuilder.show()

        mDialogView.btnOK.setOnClickListener {
            val filename = mDialogView.txtdata.text.toString() + ".csv"
            mAlertDialog.dismiss()
            if (type=="exported")
                WriteCSV(filename)
           else if(type=="imported")
                ReadCSV(filename)
        }

        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
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
                    list.add(StudentModel(token[0], token[1], token[2], token[3],  token[4]))


            }

//            //ViewRecord()
        }
        fileInputStream.close()
        adapter!!.notifyDataSetChanged()
        Toast.makeText(this, "SUCESS",   Toast.LENGTH_SHORT).show();

    }


    fun ViewRecord(){
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listStudent.layoutManager = layoutmanager

        adapter = StudentAdapter(this, list)
        listStudent.adapter = adapter
    }

    fun ShowDialog(status:String, context: Context, student:StudentModel?=null, position:Int=-1) {
        val dlgstudent = LayoutInflater.from(context).inflate(R.layout.student_dialog, null)
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
            dlgstudent.txtstudentnumber.setText(student!!.studentno)
            dlgstudent.txtfirstname.setText(student!!.firstname)
            dlgstudent.txtlastname.setText(student!!.lastname)
            dlgstudent.cbogroup.setSelection(GetGroupIndex(student!!.grp, context))
            dlgstudent.cbosection.setSelection(GetSectionIndex(student!!.sectioncode, context))
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
                        list.add(StudentModel(studentNumber, firstName, lastName, grpNumber,  section))
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

    fun Msgbox(msg: String ){
        Toast.makeText(this, "$msg",   Toast.LENGTH_SHORT).show();

    }



} //class