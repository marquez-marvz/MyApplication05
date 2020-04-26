package com.example.myapplication05

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


import kotlinx.android.synthetic.main.sched_main.view.*
import kotlinx.android.synthetic.main.sched_main.view.cboSectionSched
import kotlinx.android.synthetic.main.sched_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*

class ScheduleAdapter(val context: Context, val sched: List<ScheduleModel>) :
    RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.sched_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return sched.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val schedule = sched[position]
        holder.setData(schedule, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSched: ScheduleModel? = null
        var currentPosition: Int = 0

        init {

            itemView.setOnClickListener {
                val intent = Intent(context, AttendanceMain::class.java)
                context.startActivity(intent)
                 Util.ATT_CURRENT_DATE = currentSched!!.myDate
                 Util.ATT_CURRENT_SECTION = Util.CURRENT_SECTION
                 Util.ATT_CURRENT_AMPM=currentSched!!.ampm
            }
//            val intent = Intent(this, NextActivity::class.java)
//            (this@CurrentClassNam

            itemView.rowBtnDelete.setOnClickListener {
                val mymain = LayoutInflater.from(context).inflate(R.layout.sched_main, null)
                // LayoutInflater layoutInflater = getLayoutInflater(null);
                //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);

                val myDate = currentSched!!.myDate
                val ampm = currentSched!!.ampm
                val section = Util.CURRENT_SECTION

                // val thedate  = txtDate.text;

                val txtDate = mymain.txtDate.text.toString()
                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgconfirm)
                    .setTitle("Do you like to delete $myDate  $ampm  in $section ")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {


                    val db: DatabaseHandler = DatabaseHandler(context)
                    var status = db.ManageSched("DELETE", ampm, myDate, section)
                    SchedMain.UpdateListContent(context)
                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                }
//
                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }
//
            }


            itemView.setOnLongClickListener {
                val myDate = currentSched!!.myDate
                val ampm = currentSched!!.ampm

                val section = Util.CURRENT_SECTION

                val dlgInputBox = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgInputBox)
                    .setTitle("Input remark for  $myDate $ampm?")
                val inputBoxDialog = mBuilder.show()
                inputBoxDialog.setCanceledOnTouchOutside(false);
                dlgInputBox.txtdata.setText(currentSched!!.renark)


                dlgInputBox.btnOK.setOnClickListener {
                    val remark = dlgInputBox.txtdata.text.toString()
                    val db: DatabaseHandler = DatabaseHandler(context)
                    var status = db.ManageSched("EDIT", ampm, myDate, section, remark)
                    SchedMain.UpdateListContent(context)
                    notifyDataSetChanged()
                    inputBoxDialog.dismiss()

                }

                dlgInputBox.btnCancel.setOnClickListener {
                    inputBoxDialog.dismiss()
                }



                true
            }

        }//init

        fun setData(mysched: ScheduleModel?, pos: Int) {
            itemView.rowDate.text = mysched!!.ampm
            itemView.rowAmPm.text = mysched!!.myDate
            itemView.rowremark.text = mysched!!.renark
            this.currentSched = mysched;
            this.currentPosition = pos
        }
    }

//    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
}