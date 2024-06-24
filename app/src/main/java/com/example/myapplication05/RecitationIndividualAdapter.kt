//package com.example.myapplication05
//
//class RecitationIndividualAdapter {}


package com.example.myapplication05

//class RecitationAdapter {}


//package com.example.myapplication05
//
//class EnrolleAdapter {}
//
//package com.example.myapplication05

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_individual.view.btnDateIndividual
import kotlinx.android.synthetic.main.attendance_row.view.btnAttendandeStatus
import kotlinx.android.synthetic.main.attendance_row.view.btnName
import kotlinx.android.synthetic.main.attendance_row.view.txtAttendanceStud
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.rowBtnMenu
import kotlinx.android.synthetic.main.recitation_row.view.btnRecitationName
import kotlinx.android.synthetic.main.recitation_row.view.btnRecitationStatus
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.student_row.view.btnIndividual
import kotlinx.android.synthetic.main.student_row.view.txtFirstName
import kotlinx.android.synthetic.main.student_row.view.txtGender
import kotlinx.android.synthetic.main.student_row.view.txtLastName
import kotlinx.android.synthetic.main.student_row.view.txtOrderNum
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class RecitationIndividualAdapter(val context: Context, val recitation: List<RecitationIndividualModel>) :
    RecyclerView.Adapter<RecitationIndividualAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context)
    var NUM = 1;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.attendance_individual, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {
        return recitation.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val person = recitation[position]
        holder.setData(person, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentStudent: RecitationIndividualModel? = null
        var currentPosition: Int = 0

        init {


        } ////

        fun setData(pp: RecitationIndividualModel?, pos: Int) {
            itemView.btnDateIndividual.text =pp!!.date
            var rem = pp!!.renmark
            if (rem == "CORRECT") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#64B5F6"))) // itemView.btnRecitationName.setBackgroundColor(Color.parseColor("#FFB74D"))
            } else if (rem == "EFFORT") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB74D")))

            } else if (rem == "NO ANS") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#69F0AE")))

            } else if (rem == "ABSENT") {
                itemView.btnDateIndividual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FCF55F")))

            }
        }

    }
}

