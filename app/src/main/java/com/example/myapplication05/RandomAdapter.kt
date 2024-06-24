package com.example.myapplication05

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.random_row.view.*


class RandomAdapter(val context: Context, val studentRandom: List<RandomModel>) :
    RecyclerView.Adapter<RandomAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.random_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return studentRandom.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = studentRandom[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRandom: RandomModel? = null
        var currentPosition: Int = 0

        init {

            itemView.rowBtnCorrect.setOnClickListener {
                RemarkUpdate("CORRECT")


            }

            itemView.rowbtnWrong.setOnClickListener {
                RemarkUpdate("WRONG")
            }

        } //init


        fun setData(myatt: RandomModel?, pos: Int) {
            val db: TableRandom= TableRandom(context)
            itemView.rowtxtSequenceNumber.text = myatt!!.sequenceNumber
            itemView.rowtxtName.text = myatt!!.completeName
            itemView.rowtxtRecitation.text =db.CounRecitation(myatt!!.studentNo).toString()

            if (myatt.remark == "CORRECT") {
                itemView.rowBtnCorrect.setBackgroundColor(Color.parseColor("#64B5F6"))
            } else if (myatt.remark == "WRONG") {
                itemView.rowbtnWrong.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else {
                itemView.rowBtnCorrect.setBackgroundResource(android.R.drawable.btn_default);
                itemView.rowbtnWrong.setBackgroundResource(android.R.drawable.btn_default);
            }


            //            itemView.rowtxtPrssentCount.text = myatt!!.prsentCount.toString()
            //            itemView.rowtxtLateCount.text = myatt!!.lateCount.toString()
            //            itemView.rowtxtAbsentCount.text = myatt!!.absentCount.toString()
            //            itemView.rowtxtExcuseCount.text = myatt!!.excuseCount.toString()
            //            //            itemView.rowtxtName.text = myatt!!.completeName
            //            //            itemView.rowtxtName.text = myatt!!.completeName
            //            //            itemView.rowtxtName.text = myatt!!.completeName
            this.currentRandom = myatt;
            this.currentPosition = pos
        }


        private fun RemarkUpdate(remark: String) {
            val e = currentRandom
            val db: TableRandom = TableRandom(context)
            itemView.rowBtnCorrect.setBackgroundResource(android.R.drawable.btn_default);
            itemView.rowbtnWrong.setBackgroundResource(android.R.drawable.btn_default);

            if (remark == "CORRECT") {
                itemView.rowBtnCorrect.setBackgroundColor(Color.parseColor("#64B5F6"))
                db.AddRecitation(e!!.sectioncode, e!!.studentNo,  e!!.randomCode)
            } else {
                itemView.rowbtnWrong.setBackgroundColor(Color.parseColor("#69F0AE"))
                db.DeleteRecitation(e!!.studentNo,  e!!.randomCode)

            }

            db.UpdateRandomRemark(e!!.studentNo, e!!.sectioncode, e!!.randomCode, remark)
            RandomMain.randomList[currentPosition].remark = remark
            itemView.rowtxtRecitation.text =db.CounRecitation(e!!.studentNo).toString()

        }
    }

    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
    //        TODO("Not yet implemented")
    //    }
}