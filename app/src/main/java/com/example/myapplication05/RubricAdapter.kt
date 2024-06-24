//package com.example.myapplication05
//
//class RubricAdapter {}


package com.example.myapplication05


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.rubric_row.view.*


class RubricAdapter(val context: Context, val rubric: List<RubricModel>) :
    RecyclerView.Adapter<RubricAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.rubric_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return rubric.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val myrubric = rubric[position]
        holder.setData(myrubric, position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRubric: RubricModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnRubricEdit.setOnClickListener {
                itemView.btnRubricEdit.setVisibility(View.INVISIBLE);
                itemView.btnRubricSave.setVisibility(View.VISIBLE);
                itemView.txtRubricDescription.isEnabled = true
                itemView.btnPointCheck.isEnabled = true
                true
            } //init

            itemView.btnRubricSave.setOnClickListener {
                itemView.btnRubricEdit.setVisibility(View.VISIBLE);
                itemView.btnRubricSave.setVisibility(View.INVISIBLE);
                itemView.txtRubricDescription.isEnabled = false
                itemView.btnPointCheck.isEnabled = false
                var e = currentRubric

                var desc = itemView.txtRubricDescription.text.toString()
                var points = itemView.btnPointCheck.text.toString().toInt()
                db.UpdateRubricCriteria(e!!.RubricCode, e!!.RubricNUm, desc, points)
            } //init

            itemView.btnPointCheck.setOnClickListener {
                var points = itemView.btnPointCheck.text.toString().toInt()
                points++;
                var e = currentRubric
                itemView.btnPointCheck.setText(points.toString())
                var desc = itemView.txtRubricDescription.text.toString()
                db.UpdateRubricCriteria(e!!.RubricCode, e!!.RubricNUm, desc, points)
            } //init

            itemView.btnPointCheck.setOnLongClickListener {
                var points = 0
                var e = currentRubric
                itemView.btnPointCheck.setText(points.toString())
                var desc = itemView.txtRubricDescription.text.toString()
                db.UpdateRubricCriteria(e!!.RubricCode, e!!.RubricNUm, desc, points)
                true
            } //init


        }


        fun setData(myatt: RubricModel?, pos: Int) {
            itemView.txtRubricDescription.setText(myatt!!.Description.toString())
            itemView.btnPointCheck.text = myatt!!.Points.toString()
            itemView.txtRubricDescription.isEnabled = false
            itemView.btnPointCheck.isEnabled = false
            this.currentRubric = myatt
            this.currentPosition = pos
        }
    }


}


fun DatabaseHandler.UpdateRubricCriteria(rubricCode: String, rubricNum: Int, desc: String, point: Int) {

    var sql = """
            update tbRubric
            set Description = '$desc'
            , Points = '$point'
            where  RubricCode = '$rubricCode'
            and  RubricNUm = '$rubricNum'
"""

    Log.e("sss", sql)
    val db = this.writableDatabase
    db.execSQL(sql)

}

