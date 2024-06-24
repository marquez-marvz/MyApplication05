//package com.example.myapplication05
//
//class MainSectionAdapter {}




package com.example.myapplication05

import android.R.attr.button
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.activiy_main_row.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.misc_option_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random


class MainSectionAdapter(val context: Context, val section: List<SectionModel>) :
    RecyclerView.Adapter<MainSectionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.activiy_main_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return section.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myMisc = section[position]
        holder.setData(myMisc, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentOption: MiscModel? = null
        var currentPosition: Int = 0


        init {
            itemView.btnSectionMain.setOnClickListener {
                val mydb: DatabaseHandler = DatabaseHandler(context)
                mydb.SetCurrentSection( itemView.btnSectionMain.text.toString())
                notifyDataSetChanged()
            }

        }


        fun setData(mysec: SectionModel?, pos: Int) {
            itemView.btnSectionMain.text = mysec!!.sectionName

            val db2: DatabaseHandler = DatabaseHandler(context)
            var defaultSection = db2.GetCurrentSection()

            if (mysec!!.sectionName == defaultSection)
               itemView.btnSectionMain.setBackgroundColor(Color.parseColor("#64B5F6"))
            else
                itemView.btnSectionMain.setBackgroundResource(android.R.drawable.btn_default); //

        }






    }
}
