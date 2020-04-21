package com.example.myapplication05



import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast






class DatabaseHandler(context: Context): SQLiteOpenHelper(context,"dbstudent",null, 11) {
    companion object {
        private val DATABASE_VERSION = 1
        private val TABLE_NAME = "tbstudent"
        private val TBSTUDENT_STUDENTNO = "StudentNo"
        private val TBSTUDENT_FIRST = "FirstName"
        private val TBSTUDENT_LAST = "LastName"
        private val TBSTUDENT_GRP = "GrpNumber"
        private val TBSTUDENT_SECTION = "Section"
    }




    var context: Context? = null

        init {
            this.context = context

        }

    fun ShowField(){
        var db:SQLiteDatabase;
        db = getReadableDatabase();
        var cursor: Cursor? =  db.query("tbstudent", null, null, null, null, null, null);
        val col:Array<String> = cursor!!.getColumnNames()
        var s: String = ""
        for (c in col) {
            s = s + " " + c
        }
        Toast.makeText(this.context,  s,  Toast.LENGTH_LONG).show();
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val sql= ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, Lastname text,GrpNumber text, Section text )")
        db?.execSQL(sql)
        Log.d("myTag", "This is my delete");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
      var    sql =  "DROP TABLE tbstudent";
        db?.execSQL(sql)
         sql= ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, LastName text,GrpNumber text, Section text )")

       db?.execSQL(sql)
        Toast.makeText(this.context, " database is upgraded", Toast.LENGTH_LONG).show()

    }




    fun ManageStudent(crudStatus:String,  studentno: String, fnanme: String="", lastname: String="", grpnumber: String="", section: String=""):Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBSTUDENT_STUDENTNO, studentno)
        contentValues.put(TBSTUDENT_FIRST, fnanme) // EmpModelClass Name
        contentValues.put(TBSTUDENT_LAST, lastname) // EmpModelClass Phone
        contentValues.put(TBSTUDENT_GRP, grpnumber) // EmpModelClass Phone
        contentValues.put(TBSTUDENT_SECTION, section) // EmpModelClass Phone
        var status:Boolean=false;
        when (crudStatus) {
            "ADD" -> {
                val success = db.insert("tbstudent", null, contentValues)
                if (success<0)
                      status = false
                else
                      status =  true
            }//add

            "EDIT" -> {
                val editstat = db.update(TABLE_NAME, contentValues, TBSTUDENT_STUDENTNO+ "=" + studentno, null)
                if (editstat<0)
                   status = false
                else
                    status = true
            }//edit

            "DELETE" -> {
                val success = db.delete(TABLE_NAME, TBSTUDENT_STUDENTNO+ "=" +studentno,null)
                if (success<0)
                    status = false
                else
                    status = true
            }//edit

        }//when
        return status
        db.close()
    }








    fun  GetStudentList():List<Person>{
        val studentList:ArrayList<Person> = ArrayList<Person>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var sn = cursor.getString(cursor.getColumnIndex(TBSTUDENT_STUDENTNO))
                var fname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_FIRST))
                var lname= cursor.getString(cursor.getColumnIndex(TBSTUDENT_LAST))
                var grp= cursor.getString(cursor.getColumnIndex(TBSTUDENT_GRP))
                var section= cursor.getString(cursor.getColumnIndex(TBSTUDENT_SECTION))
                val emp= Person(sn, fname, lname, section, grp)
                studentList.add(emp)
            } while (cursor.moveToNext())
        }
        return studentList
    }






}