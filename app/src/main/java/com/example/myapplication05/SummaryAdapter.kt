package com.example.myapplication05

import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.attendance_main.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.attendance_row.view.rowtxtName
import kotlinx.android.synthetic.main.individual.view.*
import kotlinx.android.synthetic.main.summary_main.*
import kotlinx.android.synthetic.main.summary_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random


class SummaryAdapter(val context: Context, val summary: List<SummaryModel>) :
    RecyclerView.Adapter<SummaryAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.summary_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return summary.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = summary[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSummary: SummaryModel? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnLongClickListener {
                val completeName = currentSummary!!.completeName
                val studentNo = currentSummary!!.studentNo
                val dlgindividual = LayoutInflater.from(context).inflate(R.layout.individual, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgindividual).setTitle("$completeName")
                val individualDialog = mBuilder.show()
                individualDialog.setCanceledOnTouchOutside(false);

                val db: DatabaseHandler = DatabaseHandler(context)
                var mymonth = SummaryMain.GetMonth()
                var individualList: List<IndividualModel> = db.GetIndividuaList(mymonth, studentNo)
                Util.Msgbox(context, "${individualList.size.toString()}")
                val layoutmanager = LinearLayoutManager(context)
                layoutmanager.orientation = LinearLayoutManager.VERTICAL;
                dlgindividual.listIndividual.layoutManager = layoutmanager
                var adapter = IndividualAdapter(context, individualList)
                dlgindividual.listIndividual.adapter = adapter

                true
            } //long
        } //init


        fun setData(myatt: SummaryModel?, pos: Int) {
            itemView.rowtxtName.text = myatt!!.completeName
            itemView.rowtxtPrssentCount.text = myatt!!.prsentCount.toString()
            itemView.rowtxtLateCount.text = myatt!!.lateCount.toString()
            itemView.rowtxtAbsentCount.text = myatt!!.absentCount.toString()
            itemView.rowtxtExcuseCount.text = myatt!!.excuseCount.toString()
            //            itemView.rowtxtName.text = myatt!!.completeName
            //            itemView.rowtxtName.text = myatt!!.completeName
            //            itemView.rowtxtName.text = myatt!!.completeName
            this.currentSummary = myatt;
            this.currentPosition = pos
        }


    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}