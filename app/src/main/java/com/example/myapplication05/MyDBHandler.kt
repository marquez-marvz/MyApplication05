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
import java.time.Month


class DatabaseHandler(context: Context): SQLiteOpenHelper(context,"dbstudent",null, 15) {
    companion object {
        private val DATABASE_VERSION = 1
        private val TABLE_NAME = "tbstudent"
        private val TBSTUDENT_STUDENTNO = "StudentNo"
        private val TBSTUDENT_FIRST = "FirstName"
        private val TBSTUDENT_LAST = "LastName"
        private val TBSTUDENT_GRP = "GrpNumber"
        private val TBSTUDENT_SECTION = "Section"

        private val TBSCHED_TIME= "SchedTime"
        private val TBSCHED_DATE= "SchedDate"
        private val TBSCHED_SECTION= "SectionCode"
        private val TBSCHED_REMARK= "Remark"

    }




    var context: Context? = null

        init {
            this.context = context

        }

    fun ShowField(tableName:String){
        var db:SQLiteDatabase;
        db = getReadableDatabase();
        var cursor: Cursor? =  db.query(tableName, null, null, null, null, null, null);
        val col:Array<String> = cursor!!.getColumnNames()
        var s: String = ""
        for (c in col) {
            s = s + " " + c
        }
       Toast.makeText(this.context,  s,  Toast.LENGTH_LONG).show();
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val sql= ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, Lastname text,GrpNumber text, Section text))")
        db?.execSQL(sql)
        Log.d("myTag", "This is my delete");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

   var  sql = ("create table tbsched (SchedTime text,	SchedDate text,	SectionCode text, Remark text,   PRIMARY KEY (SchedTime, SchedDate, SectionCode))")
        //var  sql = ("drop table tbsched")
        db?.execSQL(sql)
//

//      var    sql =  "DROP TABLE tbstudent";
//                sql= ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, LastName text,GrpNumber text, Section text )")
//
//       db?.execSQL(sql)
//        Toast.makeText(this.context, " database is upgraded", Toast.LENGTH_LONG).show()

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

    fun ManageSched(crudStatus:String,  ampm: String, myDate: String, sectionCode: String, remark: String="-"):Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBSCHED_TIME, ampm)
        contentValues.put(TBSCHED_DATE, myDate) // EmpModelClass Name
        contentValues.put(TBSCHED_SECTION, sectionCode) // EmpModelClass Phone
        contentValues.put(TBSCHED_REMARK, remark) // EmpModelClass Phone
        var status:Boolean=false;
        when (crudStatus) {
            "ADD" -> {
                val success = db.insert("tbsched", null, contentValues)
                if (success<0)
                    status = false
                else
                    status =  true
            }//add

            "EDIT" -> {
                var where = "$TBSCHED_DATE='$myDate' and $TBSCHED_SECTION='$sectionCode' and $TBSCHED_TIME='$ampm'"
                val editstat = db.update(TABLE_NAME, contentValues, where , null)
                if (editstat<0)
                    status = false
                else
                    status = true
            }//edit



            "DELETE" -> {

                var where = "$TBSCHED_DATE='$myDate' and $TBSCHED_SECTION='$sectionCode' and $TBSCHED_TIME='$ampm'"
                 val success = db.delete("TBSCHED",  where,null)
//                if (success<0)
//                    status = false
//                else
//                    status = true
            }//edit

        }//when
        return status
        db.close()
    }

    fun  GetStudentList(category:String,section:String="",grp:String="", lastname:String="" ):List<Person> {
        val studentList: ArrayList<Person> = ArrayList<Person>()
        var sql: String=""
        when (category) {
            "ALL" -> sql = "SELECT  * FROM $TABLE_NAME"
            "SECTION" -> sql = "SELECT  * FROM $TABLE_NAME where $TBSTUDENT_SECTION='$section' and $TBSTUDENT_GRP like '$grp%'"
            "NAME" -> sql = "SELECT  * FROM $TABLE_NAME where $TBSTUDENT_SECTION='$section' and $TBSTUDENT_LAST like '$lastname%'"
        }
       // Toast.makeText(this.context,  sql,  Toast.LENGTH_LONG).show();



        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(sql, null)
        }catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var sn = cursor.getString(cursor.getColumnIndex(TBSTUDENT_STUDENTNO))
                var fname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_FIRST))
                var lname= cursor.getString(cursor.getColumnIndex(TBSTUDENT_LAST))
                var grp= cursor.getString(cursor.getColumnIndex(TBSTUDENT_GRP))
                var section= cursor.getString(cursor.getColumnIndex(TBSTUDENT_SECTION))
                val emp= Person(sn, fname, lname, grp, section)
                studentList.add(emp)
            } while (cursor.moveToNext())
        }
        return studentList
    }

    fun  GetScheduleList(sectioncode:String, monthname: String): List<ScheduleModel> {

        val schedList: ArrayList<ScheduleModel> = ArrayList<ScheduleModel>()
        var sql: String= "SELECT  * FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
        sql = sql + " and $TBSCHED_DATE like '$monthname%' order by $TBSCHED_DATE DESC"


        Toast.makeText(this.context,  sql,  Toast.LENGTH_LONG).show();
        Log.e("SQL",sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(sql, null)
        }catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var ampm= cursor.getString(cursor.getColumnIndex(TBSCHED_TIME))
                var myDate = cursor.getString(cursor.getColumnIndex(TBSCHED_DATE))
                var sectioncode = cursor.getString(cursor.getColumnIndex(TBSCHED_SECTION))
                var remark= cursor.getString(cursor.getColumnIndex(TBSCHED_REMARK))
                val sched= ScheduleModel(ampm, myDate, sectioncode, remark)
                schedList.add(sched)
            } while (cursor.moveToNext())
        }

        return schedList
    }

}





class ScheduleModel (var ampm:String ,  var myDate:String, var sectioncode:String, var renark:String){
    // class Person (var firstname:String){
}
