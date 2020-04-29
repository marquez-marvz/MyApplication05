package com.example.myapplication05


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.student_dialog.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.task_dialog.*
import kotlinx.android.synthetic.main.task_dialog.view.*

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_main)

        //        val db: DatabaseHandler = DatabaseHandler(this)
        //        db.CountTableRecord("tbactivity");


        val arrSection: Array<String> = this.getResources().getStringArray(R.array.section_choice)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionActivity.setAdapter(sectionAdapter);
        cboSectionAct = findViewById(R.id.cboSectionActivity) as Spinner
        ActivityUpdateListContent(this)
        ActivityViewRecord()



        btnAddActivity.setOnClickListener {

            ShowDialog("ADD", this)
        }
    }


    companion object {
        var activityList = arrayListOf<ActivityModel>()
        var cboSectionAct: Spinner? = null;
        var activityAdapter: ActivityAdapter? = null;

        fun ActivityUpdateListContent(context: Context) {
            val dbglobal: TableActivity = TableActivity(context)
            val activity: List<ActivityModel>

            activityList.clear()
            val section = cboSectionAct!!.getSelectedItem().toString();
            activity = dbglobal.GetActivityList(section)

            for (e in activity) {
                activityList.add(ActivityModel(e.activityCode, e.sectionCode, e.description, e.item.toInt(), e.status))
            }
        }

        fun GetSubject(): String{
           return  cboSectionAct!!.getSelectedItem().toString();
        }

        fun ShowDialog(status: String, context: Context, activity: ActivityModel? = null, position: Int = -1) {
            val db: TableActivity = TableActivity(context)
            val dlgactivity = LayoutInflater.from(context).inflate(R.layout.task_dialog, null)
            val mBuilder =
                AlertDialog.Builder(context).setView(dlgactivity).setTitle("Manage Student")
            val activityDialog = mBuilder.show()
            activityDialog.setCanceledOnTouchOutside(false);

            if (status == "ADD") {
                ClearTextBox(dlgactivity)
                StatusTextBox(true, dlgactivity)
                dlgactivity.txtActivityCode.setText( db.GetNewActivityCode(GetSubject()))
            } else if (status == "EDIT") {
                dlgactivity.txtActivityCode.setText(activity!!.activityCode)
                dlgactivity.txtDescription.setText(activity!!.description)
                dlgactivity.txtItem.setText(activity.item.toString())
                dlgactivity.btnSaveRecord.setText("EDIT")
                StatusTextBox(false, dlgactivity)
            }


            dlgactivity.btnSaveRecord.setOnClickListener {
                val buttonText: String = dlgactivity.btnSaveRecord.getText().toString()
                val activityCode = dlgactivity.txtActivityCode.text.toString()
                val description = dlgactivity.txtDescription.text.toString()
                val item = dlgactivity.txtItem.text.toString()
                val sectionCode = cboSectionAct!!.getSelectedItem().toString();

                if (buttonText == "SAVE RECORD") {
                    var status = db.ManageActivity("ADD", activityCode, sectionCode, description, item, "ACTIVE")
                    ActivityUpdateListContent(context)
                    activityAdapter!!.notifyDataSetChanged()
                    activityDialog.dismiss()

                } else if (buttonText == "EDIT") {
                    StatusTextBox(true, dlgactivity)
                    dlgactivity.btnSaveRecord.setText("SAVE CHANGES")
                    dlgactivity.txtActivityCode.isEnabled = false;
                } else if (buttonText == "SAVE CHANGES") {
                    db.ManageActivity("EDIT", activityCode, sectionCode, description,item, "ACTIVE")
                    ActivityUpdateListContent(context)
                    activityAdapter!!.notifyDataSetChanged()
                    activityDialog.dismiss()
                }

            }

            dlgactivity.btnClose.setOnClickListener {
                activityDialog.dismiss()
            }

        } //ShowDialog

        private fun ClearTextBox(dlgactivity: View) {
            dlgactivity.txtActivityCode.setText("")
            dlgactivity.txtDescription.setText("")
            dlgactivity.txtItem.setText("")
        }

        fun StatusTextBox(stat: Boolean, dlgactivity: View) {

            dlgactivity.txtDescription.isEnabled = stat
            dlgactivity.txtItem.isEnabled = stat
        }


    }


    fun ActivityViewRecord() {

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listActivity.layoutManager = layoutmanager
        activityAdapter = ActivityAdapter(this, activityList)
        listActivity.adapter = activityAdapter

    }


}