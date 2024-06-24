//package com.example.myapplication05
//import android.content.ContentValues
//import android.content.Context
//import android.content.Intent
//import android.database.Cursor
//import android.os.Bundle
//import android.speech.tts.TextToSpeech
//import android.util.Log
//import android.view.LayoutInflater
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//
//import kotlinx.android.synthetic.main.speak.*
//
//import java.util.*
//
////import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
////import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_NAME
////import com.example.myapplication05.R.layout.section_dialog
////import com.example.myapplication05.R.layout.util_inputbox
////import kotlinx.android.synthetic.main.section_dialog.view.*
////import kotlinx.android.synthetic.main.section_main.*
////import kotlinx.android.synthetic.main.student_dialog.view.*
////import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
////import kotlinx.android.synthetic.main.task_dialog.view.*
////import kotlinx.android.synthetic.main.util_inputbox.view.*
////import kotlinx.android.synthetic.main.task_dialog.view.btnSaveRecord as btnSaveRecord1
//
//class Speak : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.speak)
//        val db: TableActivity = TableActivity(this)
//        KeyUpdateListContent(this)
//        KeyViewRecord()
//
//        btnAdd.setOnClickListener {
//            val db: DatabaseHandler = DatabaseHandler(this)
//            val keyCode = db.GetNewKeyCode()
//            db.SaveNewKey(keyCode, "-", "SHOW")
//            KeyUpdateListContent(this)
//            KeyViewRecord()
//            KeyAdapter!!.notifyDataSetChanged()
//
//        }
//
//    }
//
//
//    companion object {
//        var KeyAdapter: SpeakAdapter? = null;
//        var keyList = arrayListOf<KeyModel>()
//
//        fun KeyUpdateListContent(context: Context) {
//            val db: DatabaseHandler = DatabaseHandler(context)
//            val myList: List<KeyModel> = db.GetKeyList()
//            keyList.clear()
//
//            for (e in myList) {
//                keyList.add(KeyModel(e.keyCode, e.description, e.staus))
//
//            }
//
//        }
//    }
//
//
//    private fun KeyViewRecord() {
//        val layoutmanager = LinearLayoutManager(this)
//        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
//        listKey.layoutManager = layoutmanager
//        KeyAdapter = SpeakAdapter(this, keyList)
//        listKey.adapter = KeyAdapter
//    }
//}
//
//
//fun DatabaseHandler.GetKeyList() :List<KeyModel> {
//    val keyList: ArrayList<KeyModel> = ArrayList<KeyModel>()
//    var sql =""
//    //    if (Visibility =="SHOW")
//    sql = "SELECT * FROM TBKEY  ORDER  BY KEYCODE"
//    //    else if (Visibility =="HIDE")
//    //        sql = "SELECT * FROM TBSECTION where status='HIDE' ORDER BY ${DatabaseHandler.TBSECTION_NAME} "
//    //    else
//    //        sql = "SELECT * FROM TBSECTION ORDER BY ${DatabaseHandler.TBSECTION_NAME} "
//
//
//    val db = this.readableDatabase
//    var cursor: Cursor? = null
//    cursor = db.rawQuery(sql, null)
//
//    if (cursor.moveToFirst()) {
//        do {
//
//            var keyCode = cursor.getString(cursor.getColumnIndex("KeyCode"))
//            var description = cursor.getString(cursor.getColumnIndex("Description"))
//            var status = cursor.getString(cursor.getColumnIndex("Status"))
//
//            val myList = KeyModel(keyCode, description, status)
//            keyList.add(myList)
//        } while (cursor.moveToNext())
//    }
//    return keyList
//}
//
//
//fun DatabaseHandler.GetNewKeyCode():String {
//    var sql = """
//                SELECT * FROM TBKEY
//                ORDER BY KEYCODE DESC
//                  """
//    val db = this.readableDatabase
//    val cursor = db.rawQuery(sql, null)
//    if (cursor.moveToFirst()) {
//        var keyCode  = cursor.getString(cursor.getColumnIndex("KeyCode"))
//
//        var num = keyCode.takeLast(2).toInt() + 1 // Grade>>>
//        Util.Msgbox(context, num.toString())
//        return "KEY-" + Util.ZeroPad(num, 2)
//    } else {
//        return "KEY-10"
//    }
//}
//
//
//
//fun DatabaseHandler.SaveNewKey(keycode:String , description:String, status:String) {
//    var sql = """
//                insert into tbkey  (KeyCode,	Description,	Status) values('$keycode', '$description', '$status')
//                  """
//    val db = this.writableDatabase
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
//
//
