package com.example.myapplication05

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.widget.Toast

class Util {
    companion object {
        var CURRENT_DATE: String = "";
        var CURRENT_SECTION: String = "";
        var ATT_CURRENT_DATE: String = "";
        var ATT_CURRENT_SECTION: String = ""
        var ATT_CURRENT_AMPM: String = ""

        fun Msgbox(context: Context?, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        }

        fun DatabaseUpGrade(context: Context) {
            val db: DatabaseHandler = DatabaseHandler(context)
            db.getWritableDatabase()
        }


        fun ZeroPad(num: Int, digit: Int): String {
            var str = num.toString().padStart(digit, '0')
            return str;
        }

        fun ShowTableField(context: Context, tableName: String) {
            val db: DatabaseHandler = DatabaseHandler(context)
            db.ShowField(tableName)
        }

    }
}