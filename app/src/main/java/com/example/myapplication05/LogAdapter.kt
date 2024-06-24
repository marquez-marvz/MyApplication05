package com.example.myapplication05

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.log_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*


class LogAdapter(val context: Context, val logAct: List<LogModel>) :
    RecyclerView.Adapter<LogAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.log_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return logAct.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = logAct[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRandom: LogModel? = null
        var currentPosition: Int = 0

        init {



        } //init


        fun setData(myatt: LogModel?, pos: Int) {
            val db: TableRandom= TableRandom(context)
            itemView.rowtxtLogID.text = myatt!!.LogID
            itemView.rowtxtLogDescription.text = myatt!!.Description
            this.currentRandom = myatt;
            this.currentPosition = pos
        }



    }


}