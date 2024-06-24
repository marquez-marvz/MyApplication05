package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.misc_main.*
import kotlinx.android.synthetic.main.score_main.*
import kotlinx.android.synthetic.main.task_dialog.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*


class MiscMain : AppCompatActivity() {
   var myContext = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.misc_main)
        SetSpinnerAdapter()
        currentSection= cboSectionMisc.getSelectedItem().toString();
        Log.e("XMISC", currentSection + " 22 ")
        MiscUpdateListContent(this, currentSection)
        MiscViewRecord()

        cboSectionMisc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)

                currentSection= cboSectionMisc.getSelectedItem().toString();
                MiscUpdateListContent(myContext, currentSection);
                miscAdapter!!.notifyDataSetChanged()
               // db.SetCurrentSection(currentSection)
            }
        }






        btnAdd.setOnClickListener {

            val dlginput = LayoutInflater.from(this).inflate(R.layout.util_inputbox, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlginput).setTitle("Input New Misc")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false);



            dlginput.btnOK.setOnClickListener {
                val db: TableRandom = TableRandom(this)
                var sectionCode = cboSectionMisc.getSelectedItem().toString();
                var miscCode = db.GetMiscCode(sectionCode)
                var description = dlginput.txtdata.text.toString()
                Log.e("MISC", "$sectionCode $miscCode,$description")

                db.ManageMisc("ADD", miscCode, description, miscCode + "-1", "-", sectionCode)
                db.SaveStudentMisc(sectionCode,miscCode + "-1" )

                inputDialog.dismiss()
                MiscUpdateListContent(this, sectionCode)
                miscAdapter!!.notifyDataSetChanged()

            }
            dlginput.btnCancel.setOnClickListener {
                inputDialog.dismiss()
            }
        }
    }

        companion object {
            var miscList = arrayListOf<MiscModel>()
            var miscAdapter: MiscAdapter? = null;
            var currentSection = ""

            fun MiscUpdateListContent(context: Context, section:String) {
                val dbglobal: TableRandom = TableRandom(context)
                val misc: List<MiscModel>

                miscList.clear()

                misc = dbglobal.GetMiscList(section)
                Util.Msgbox(context, misc.size.toString()+ section)
                for (e in misc) {
                    miscList.add(MiscModel(e.MiscCode, e.MiscDescription, e.OptionNumber, e.OptionDescription, e.SectionCode))
                }
            }
        }





    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionMisc.setAdapter(sectionAdapter);
    }

    fun MiscViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listMisc.layoutManager = layoutmanager
        miscAdapter = MiscAdapter(this, miscList)
        listMisc.adapter = miscAdapter
    }
}



