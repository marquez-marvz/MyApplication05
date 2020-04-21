package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.database.*
import kotlinx.android.synthetic.main.dialog_student.view.*
import kotlinx.android.synthetic.main.myrecycle.*
import kotlinx.android.synthetic.main.myrecycle.btnAdd

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



    }

    fun UpdateListContent() {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val student: List<Person> = databaseHandler.GetStudentList()


        for (e in student) {
            list.add(Person(e.studentno,  e.firstname, e.lastname, e.sectioncode, e.grp))
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



        dlgstudent.btnDelete.setOnLongClickListener {
            val studentNumber = dlgstudent.txtstudentnumber.text.toString()
            val firstName = dlgstudent.txtfirstname.text.toString()
                val lastName = dlgstudent.txtlastname.text.toString()
                val db: DatabaseHandler = DatabaseHandler(context)
                var status = db.ManageStudent("DELETE", studentNumber)
            //list.add(Person(studentNumber, firstName, lastName, grpNumber,  section))
            list.removeAt(position)
            adapter!!.notifyDataSetChanged()
            studentDialog.dismiss()
            true
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