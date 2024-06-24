package com.example.myapplication05

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.misc_option.view.*
//import kotlinx.android.synthetic.main.misc_row.view.*
//import kotlinx.android.synthetic.main.misc_row.view.rowMiscCode
//import kotlinx.android.synthetic.main.misc_row.view.rowMiscDescription
import kotlinx.android.synthetic.main.util_confirm.view.*
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
import kotlinx.android.synthetic.main.misc_student.*
import kotlinx.android.synthetic.main.misc_student_row.view.*


class MiscStudent : AppCompatActivity() {
    var myContext = this;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.misc_student)
        SetSpinnerAdapter()
        txtMiscDescription.text =Util.MISC_DESCRIPTION

    }

    companion object{
        var miscStudentList = arrayListOf<MiscStudentModel>()
        var miscStudentAdapter: MiscAdapter? = null;

        fun MiscUpdateListContent(context: Context, section:String) {
            val db: TableRandom = TableRandom(context)
            val misc: List<MiscStudentModel>

            miscStudentList.clear()

            misc = db.GetMiscStudentList(Util.MISC_CURRENT_SECTION, Util.MISC_CODE)
            Util.Msgbox(context, misc.size.toString()+ section)
            for (e in misc) {
                miscStudentList.add(MiscStudentModel(e.studentNo, e.optionNumber, e.sectionCode, e.completeName, e.grpNumber, e.gender, e.optionDescription, e.miscDescription))
            }
        }


    }

    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionMiscStudent.setAdapter(sectionAdapter);
    }
}




class MiscStudentAdapter(val context: Context, val miscStudent: List<MiscStudentModel>) :
    RecyclerView.Adapter<MiscStudentAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.misc_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return miscStudent.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myMiscStudent = miscStudent[position]
        holder.setData(myMiscStudent, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentMiscStudent: MiscStudentModel? = null
        var currentPosition: Int = 0



        init {

        }


        fun setData(miscStud: MiscStudentModel?, pos: Int) {
            itemView.txtCompleteNane.setText(miscStud!!.completeName)
            this.currentMiscStudent = miscStud;
            this.currentPosition = pos
        }


    }
}





