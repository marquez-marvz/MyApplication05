//package com.example.myapplication05
//
//
//
//
//import android.content.Context
//import android.content.Intent
//import android.database.Cursor
//import android.graphics.Color
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.attendance_row.view.*
//import kotlinx.android.synthetic.main.section_row.view.btnStatus
//import kotlinx.android.synthetic.main.speak_answer_row.view.*
//import kotlinx.android.synthetic.main.speak_row.view.rowBtnEdit
//import kotlinx.android.synthetic.main.speak_row.view.rowBtnSave
//import java.util.ArrayList
//
//
//class SpeakAnswerAdapter(val context: Context, val theKey: List<AnswerModel>) :
//    RecyclerView.Adapter<SpeakAnswerAdapter.MyViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//
//        val myView = LayoutInflater.from(context).inflate(R.layout.speak_answer_row, parent, false)
//
//        return MyViewHolder((myView))
//
//    }
//
//    override fun getItemCount(): Int {
//        return theKey.size;
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        // holder.removeAllViews();
//        val mySummary = theKey[position]
//        holder.setData(mySummary, position)
//
//    }
//
//    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        var currentKey: AnswerModel? = null
//        var currentPosition: Int = 0
//
//        init {
//            itemView.rowBtnEdit.setOnClickListener {
//                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
//                itemView.rowBtnSave.setVisibility(View.VISIBLE);
//                itemView.txtAnswer.isEnabled = true
//            }
//
//            itemView.rowBtnSave.setOnClickListener {
//                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
//                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
//                itemView.txtAnswer.isEnabled = false
//                val db: DatabaseHandler = DatabaseHandler(context)
//                db.UpdateAnswer( currentKey!!.keyCode, currentKey!!.number,  itemView.txtAnswer.text.toString() )
//            }
//
//            itemView.rowBtnDelete.setOnClickListener {
//                val db: DatabaseHandler = DatabaseHandler(context)
//                db.DeleteAnswer( currentKey!!.keyCode, currentKey!!.number)
//                SpeakAnswerMain.AnswerUpdateListContent(context)
//                notifyDataSetChanged()
//            }
//
//
//
//
//
//
//            itemView.btnStatus.setOnClickListener {
//                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
//                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
//                itemView.txtAnswer.isEnabled = false
//                val db: DatabaseHandler = DatabaseHandler(context)
//                var stat = ""
//                if (itemView.btnStatus.text =="NO") {
//                    stat = "YES"
//                    itemView.btnStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
//                } else{
//                    stat = "NO"
//                    itemView.btnStatus.setBackgroundColor(Color.parseColor("#FFB74D"))
//
//            }
//                itemView.btnStatus.text = stat
//                db.UpdateAnswerStatus( currentKey!!.keyCode, currentKey!!.number,  stat)
//                SpeakAnswerMain.AnswerUpdateListContent(context)
//                notifyDataSetChanged()
//            }
//
//
//
//
//
//
//
//        } //init
//
//        fun DefaultColor() {
//            itemView.btnStatus.setBackgroundResource(android.R.drawable.btn_default);
//        }
//
//
//
//        fun setData(myatt: AnswerModel?, pos: Int) {
//            itemView.txtNumber.text = myatt!!.number.toString()
//            itemView.txtAnswer.setText(myatt!!.answer.toString())
//            itemView.btnStatus.setText(myatt!!.status.toString())
//            //  itemView.rowSchool.text = myatt!!.staus
//
//            when (myatt!!.status) {
//                "YES" -> itemView.btnStatus.setBackgroundColor(Color.parseColor("#69F0AE"))
//                "NO" -> itemView.btnStatus.setBackgroundColor(Color.parseColor("#FFB74D"))
//            }
//
//            this.currentKey= myatt;
//            this.currentPosition = pos
//        }
//
//
//    }
//
//    //    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
//    //        TODO("Not yet implemented")
//    //    }
//
//
//}
//
//
//
//fun DatabaseHandler.UpdateAnswer(keyCode:String,number:Int ,answer:String){
//    val db = this.writableDatabase
//
//    var sql = """
//             update tbkeyAnswer  set Answer='$answer'
//                          where keycode   ='$keyCode'
//                          and  number   =$number
//
//        """.trimIndent()
//
//
//
//    Log.e("KEY", sql)
//
//    db.execSQL(sql)
//
//
//}
//
//
//fun DatabaseHandler.DeleteAnswer(keyCode:String,number:Int){
//    val db = this.writableDatabase
//
//    var sql = """
//             delete from  tbkeyAnswer
//                          where keycode   ='$keyCode'
//                          and  number   =$number
//
//        """.trimIndent()
//
//    Log.e("KEY", sql)
//    db.execSQL(sql)
//}
//
//
//fun DatabaseHandler.UpdateAnswerStatus(keyCode:String,number:Int ,status:String){
//    val db = this.writableDatabase
//
//    var sql = """
//             update tbkeyAnswer  set Status='$status'
//                          where keycode   ='$keyCode'
//                          and  number   =$number
//
//        """.trimIndent()
//
//    Log.e("KEY", sql)
//    db.execSQL(sql)
//}
//
//
//
//
//
//
//
//
//
