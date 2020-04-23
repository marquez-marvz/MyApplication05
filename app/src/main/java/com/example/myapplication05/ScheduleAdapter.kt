package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.confirm.view.*
import kotlinx.android.synthetic.main.sched_main.view.*
import kotlinx.android.synthetic.main.sched_main.view.cboSectionSched
import kotlinx.android.synthetic.main.sched_row.view.*

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
            val mymain = LayoutInflater.from(context).inflate(R.layout.sched_main, null)
            val myDate = currentSched!!.myDate
            val ampm = currentSched!!.ampm
            val section = mymain.cboSectionSched.getSelectedItem().toString();
//            itemView.setOnClickListener {
//                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
//                // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();
//                var A: MyRecycle = MyRecycle()
//                A.ShowDialog("VIEW", context, currentSched, position)
//            }
//
//            itemView.setOnLongClickListener {
//
//
//
//                true
//            }
//
            itemView.rowBtnDelete.setOnClickListener {

                val txtDate = mymain.txtDate.text.toString()
                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.confirm, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgconfirm)
                    .setTitle("Do you like to delete $myDate  $ampm  ?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {
                    val db: DatabaseHandler = DatabaseHandler(context)
                    var status = db.ManageSched("DELETE", ampm, txtDate, section)
                    SchedMain.UpdateListContent(section, myDate,  context)
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

                val dlgInputBox = LayoutInflater.from(context).inflate(R.layout.inputbox, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgInputBox)
                    .setTitle("Input remark for  $myDate $ampm?")
                val inputBoxDialog = mBuilder.show()
                inputBoxDialog.setCanceledOnTouchOutside(false);

                dlgInputBox.btnOk.setOnClickListener {
                    val db: DatabaseHandler = DatabaseHandler(context)
                    var status = db.ManageSched("EDIT",ampm, myDate,section, remsrk)
                    MyRecycle.list.removeAt(currentPosition)
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
            this.currentSched = mysched;
            this.currentPosition = pos
        }
    }

//    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
}