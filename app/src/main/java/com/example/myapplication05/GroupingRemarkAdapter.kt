//package com.example.myapplication05
//
//class  {}


package com.example.myapplication05



import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.group_main.*
import kotlinx.android.synthetic.main.group_row.view.*
import kotlinx.android.synthetic.main.group_student.view.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.student_dialog.view.txtContact
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*


class GroupingRemarkAdapter(val context: Context, val grp: List<GroupRemarkModel>) :
    RecyclerView.Adapter<GroupingRemarkAdapter.MyViewHolder>() {

    var countGroup = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.group_student, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        countGroup = grp.size
        return grp.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = grp[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentActivity: GroupRemarkModel? = null
        var currentPosition: Int = 0

        init {

        } //init

        fun setData(grp: GroupRemarkModel?, pos: Int) { //
            itemView.txtNewRemark.text = grp!!.Remark
            itemView.rowtxtName.setVisibility(View.INVISIBLE);
            itemView.btnScoreRemark.setVisibility(View.INVISIBLE);
            itemView.txtIndScore.setVisibility(View.INVISIBLE);
            itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
            itemView.rowBtnSave.setVisibility(View.INVISIBLE);
            itemView.txtNewRemark.setVisibility(View.VISIBLE);
            this.currentActivity = grp;
            this.currentPosition = pos

            Log.e("REM ADAPTER", "Pain")

        }
    }
}

