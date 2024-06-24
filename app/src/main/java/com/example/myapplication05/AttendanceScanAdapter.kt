
package com.example.myapplication05


import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.att_individual_main.view.*
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnEdit
import kotlinx.android.synthetic.main.att_individual_main.view.rowBtnSave
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual_row.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlinx.android.synthetic.main.score_individual.view.btnNext as btnNext1


class AttendanceScanAdapter(val context: Context, val attendance: List<AttendanceScanModel>) :
    RecyclerView.Adapter<AttendanceScanAdapter.MyViewHolder>() {


    var attendanceIndividualList = arrayListOf<AttendanceModel>()
    var attendanceIndividualAdapter: AttendabceIndividualAdapter? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.attendance_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return attendance.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myattendance = attendance[position]
        holder.setData(myattendance, position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentAttendance: AttendanceScanModel? = null
        var currentPosition: Int = 0

        init{

        }












        fun setData(myatt: AttendanceScanModel?, pos: Int) {
            itemView.rowtxtLastName.setVisibility(View.INVISIBLE);
            itemView.rowtxtFirstName.setVisibility(View.INVISIBLE);
          //  itemView.rowBtnRemark.setVisibility(View.INVISIBLE);
//            itemView.btnAbsentCount.setVisibility(View.INVISIBLE);


            this.currentAttendance = myatt;
            this.currentPosition = pos
            //itemView.rowtxtNum.text = myatt!!.num
         //   itemView.rowtxtStudentNumber.text = myatt!!.studNumber
          //  itemView.rowtxtCompleteName.text = myatt!!.completeName


        }
    }


}











