package com.example.myapplication05

import android.content.Context
import android.widget.Toast

class Util {
    companion object {
        fun Msgbox(context: Context, msg: String) {
                Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();

        }

        fun DatabaseUpGrade(context: Context) {
            val db: DatabaseHandler = DatabaseHandler(context)
            db.getWritableDatabase()

        }


        fun ZeroPad(num:Int, digit: Int):String{
            var str = num.toString().padStart(digit, '0')
            return str;
        }

        fun ShowTableField(context: Context, tableName:String) {
            val db: DatabaseHandler = DatabaseHandler(context)
            db.getWritableDatabase()
            db.ShowField(tableName)
        }

    }
}
