//package com.example.myapplication05
//
//import android.content.Context
//import android.content.Intent
//import android.database.Cursor
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AlertDialog
//import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.dialog_message.view.*
//import kotlinx.android.synthetic.main.score_row.view.*
//import kotlinx.android.synthetic.main.section_dialog.view.*
//import kotlinx.android.synthetic.main.section_row.view.*
//import kotlinx.android.synthetic.main.section_row.view.btnStatus
//import kotlinx.android.synthetic.main.speak_row.view.*
//import kotlinx.android.synthetic.main.speak_row.view.rowBtnEdit
//import kotlinx.android.synthetic.main.speak_row.view.rowBtnSave
//import kotlinx.android.synthetic.main.util_confirm.view.*
//import java.util.ArrayList
//
//
//class SpeakAdapter(val context: Context, val theKey: List<KeyModel>) :
//    RecyclerView.Adapter<SpeakAdapter.MyViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//
//        val myView = LayoutInflater.from(context).inflate(R.layout.speak_row, parent, false)
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
//        var currentKey: KeyModel? = null
//        var currentPosition: Int = 0
//
//        init {
//            itemView.rowBtnEdit.setOnClickListener {
//                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
//                itemView.rowBtnSave.setVisibility(View.VISIBLE);
//                itemView.txtDescription.isEnabled = true
//            }
//
//            itemView.rowBtnSave.setOnClickListener {
//                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
//                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
//                itemView.txtDescription.isEnabled = false
//                val db: DatabaseHandler = DatabaseHandler(context)
//               db.UpdateDescriptionDatabase( itemView.txtDescription.text.toString(), currentKey!!.keyCode)
//               // UpdateDescription(currentKey!!.keyCode, currentKey!!.description)
//            }
//
//            itemView.setOnClickListener {
//                val intent = Intent(context, SpeakAnswerMain::class.java)
//                context.startActivity(intent)
//                Util.KEY_CODE = currentKey!!.keyCode
//                Util.KEY_DESCRIPTION = currentKey!!.description
//            }
//
//
//
//
//
//        } //init
//
//
//        fun setData(myatt: KeyModel?, pos: Int) {
//            itemView.txtKeyCode.text = myatt!!.keyCode
//            itemView.txtDescription.setText(myatt!!.description.toString())
//          //  itemView.rowSchool.text = myatt!!.staus
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
//}
//
//
//
//fun DatabaseHandler.UpdateDescriptionDatabase(description:String, keycode:String, ss:String=""){
//    val db = this.writableDatabase
//
//    var sql = """
//             update tbkey  set Description='$description'
//                          where Keycode   ='$keycode'
//
//        """.trimIndent()
//
//
//
// Log.e("KEY", sql)
//
//    db.execSQL(sql)
//
//
//}
//
//
//
//
//
//
//
//
