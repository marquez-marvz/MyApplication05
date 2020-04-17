package com.example.myapplication05



import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,"dbstudent",null, 1) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_NAME = "tbstudent"
        private val KEY_STUDENTNO = "studentno"
        private val KEY_FIRST = "firstname"
        private val KEY_LAST = "lastname"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val sql= ("CREATE TABLE tbstudent (studentno INTEGER PRIMARY KEY,firstname text, lastname text)")
        db?.execSQL(sql)
        Log.d("myTag", "This is my create");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("myTag", "This is my delete");

    }

    fun addStudent(studentno: String, fnanme: String, lastname: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_STUDENTNO, studentno)
        contentValues.put(KEY_FIRST, fnanme) // EmpModelClass Name
        contentValues.put(KEY_LAST, lastname) // EmpModelClass Phone

        val success = db.insert(TABLE_NAME, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
      //  var pp=""
     //  print("Hello")
        return success;
    }

    fun EditStudent(studentno: String, fnanme: String, lastname: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_STUDENTNO, studentno)
        contentValues.put(KEY_FIRST, fnanme) // EmpModelClass Name
        contentValues.put(KEY_LAST, lastname) // EmpModelClass Phone
        val success = db.update(TABLE_NAME, contentValues, KEY_STUDENTNO+ "=" +studentno,null)
        db.close() // Closing database connection
        return success;
    }

    fun DeleteStudent(studentno: String): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, KEY_STUDENTNO+ "=" +studentno,null)
        db.close()
        return success;
    }






    fun viewEmployee():List<StudentModel>{
        val studentList:ArrayList<StudentModel> = ArrayList<StudentModel>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var sn = ""
        var fname= ""
        var lname = ""
        if (cursor.moveToFirst()) {
            do {
                sn = cursor.getString(cursor.getColumnIndex(KEY_STUDENTNO))
                fname = cursor.getString(cursor.getColumnIndex(KEY_FIRST))
                lname= cursor.getString(cursor.getColumnIndex(KEY_LAST))
                val emp= StudentModel(sn, fname, lname)
                studentList.add(emp)
            } while (cursor.moveToNext())
        }
        return studentList
    }






}