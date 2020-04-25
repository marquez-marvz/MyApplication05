package com.example.myapplication05


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, "dbstudent", null, 19) {
    companion object {
        val DATABASE_VERSION = 1
        val TABLE_NAME = "tbstudent"
        val TBSTUDENT_STUDENTNO = "StudentNo"
        val TBSTUDENT_FIRST = "FirstName"
        val TBSTUDENT_LAST = "LastName"
        val TBSTUDENT_GRP = "GrpNumber"
        val TBSTUDENT_SECTION = "Section"

        private val TBSCHED_TIME = "SchedTime"
        private val TBSCHED_DATE = "SchedDate"
        private val TBSCHED_SECTION = "SectionCode"
        private val TBSCHED_REMARK = "Remark"

        private val TBATTENDANCE_TIME = "SchedTime"
        private val TBATTENDANCE_DATE = "SchedDate"
        private val TBATTENDANCE_SECTION = "SectionCode"
        private val TBATTENDANCE_STUDENTNO = "StudentNumber"
        private val TBATTENDANCE_STATUS = "AttendanceStatus"
        private val TBATTENDANCE_REMARK = "Remark"

        private val QRATTENDANCE_TIME = "SchedTime"
        private val QRATTENDANCE_DATE = "SchedDate"
        private val QRATTENDANCE_SECTION = "SectionCode"
        private val QRATTENDANCE_STUDENTNO = "StudentNumber"
        private val QRATTENDANCE_STATUS = "AttendanceStatus"
        private val QRATTENDANCE_REMARK = "Remark"
        private val QRATTENDANCE_FIRST = "FirstName"
        private val QRATTENDANCE_LAST = "LastName"
        private val QRATTENDANCE_GRP = "GrpNumber"


    }


    var context: Context? = null

    init {
        this.context = context

    }

    fun ShowField(tableName: String): String {
        var db: SQLiteDatabase;
        db = getReadableDatabase();
        var cursor: Cursor? = db.query(tableName, null, null, null, null, null, null);
        val col: Array<String> = cursor!!.getColumnNames()
        //var field: String = ""
        var field = ArrayList<String>()
        //adding String elements in the list
        //arraylist.add("Geeks")

        for (c in col) {
            field.add(c)
        }
        val fields = arrayOfNulls<String>(field.size)
        field.toArray(fields)
        Toast.makeText(this.context, fields.joinToString(), Toast.LENGTH_LONG).show();
        return fields.joinToString()
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val sql =
            ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, Lastname text,GrpNumber text, Section text))")
        db?.execSQL(sql)
        Log.d("myTag", "This is my delete");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var sql =
            """
            CREATE VIEW tbattendance_query AS SELECT 
                            tbattendance.SchedTime,
                            tbattendance.SchedDate,
                            tbattendance.SectionCode,
                            tbattendance.StudentNumber,
                            tbattendance.AttendanceStatus,
                            tbattendance.Remark,
                            tbstudent.FirstName,
                            tbstudent.LastName,
                            tbstudent.GrpNumber
                FROM
                tbattendance
                INNER JOIN
                        tbstudent
                ON
                tbattendance.StudentNumber = tbstudent.StudentNo
        """
        // var sql =
        //("create table tbsched (SchedTime text,	SchedDate text,	SectionCode text, Remark text,   PRIMARY KEY (SchedTime, SchedDate, SectionCode))")
        //var  sql = ("drop table tbsched")
        db?.execSQL(sql)
//

//      var    sql =  "DROP TABLE tbstudent";
//                sql= ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, LastName text,GrpNumber text, Section text )")
//
//       db?.execSQL(sql)
        Toast.makeText(this.context, " database is upgraded", Toast.LENGTH_LONG).show()
    }

    fun ManageStudent(
        crudStatus: String,
        studentno: String,
        fnanme: String = "",
        lastname: String = "",
        grpnumber: String = "",
        section: String = ""
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBSTUDENT_STUDENTNO, studentno)
        contentValues.put(TBSTUDENT_FIRST, fnanme) // EmpModelClass Name
        contentValues.put(TBSTUDENT_LAST, lastname) // EmpModelClass Phone
        contentValues.put(TBSTUDENT_GRP, grpnumber) // EmpModelClass Phone
        contentValues.put(TBSTUDENT_SECTION, section) // EmpModelClass Phone
        var status: Boolean = false;
        when (crudStatus) {
            "ADD" -> {
                val success = db.insert("tbstudent", null, contentValues)
                if (success < 0)
                    status = false
                else
                    status = true
            }//add

            "EDIT" -> {
                val editstat = db.update(
                    TABLE_NAME,
                    contentValues,
                    TBSTUDENT_STUDENTNO + "=" + studentno,
                    null
                )
                if (editstat < 0)
                    status = false
                else
                    status = true
            }//edit

            "DELETE" -> {
                val success = db.delete(TABLE_NAME, TBSTUDENT_STUDENTNO + "=" + studentno, null)
                if (success < 0)
                    status = false
                else
                    status = true
            }//edit

        }//when
        return status
        db.close()
    }

    fun ManageSched(
        crudStatus: String,
        ampm: String,
        myDate: String,
        sectionCode: String,
        remark: String = "-"
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBSCHED_TIME, ampm)
        contentValues.put(TBSCHED_DATE, myDate) // EmpModelClass Name
        contentValues.put(TBSCHED_SECTION, sectionCode) // EmpModelClass Phone
        contentValues.put(TBSCHED_REMARK, remark) // EmpModelClass Phone
        var status: Boolean = false;
        when (crudStatus) {
            "ADD" -> {
                val success = db.insert("tbsched", null, contentValues)
                if (success < 0)
                    status = false
                else
                    status = true
            }//add

            "EDIT" -> {
                var where =
                    "$TBSCHED_DATE='$myDate' and $TBSCHED_SECTION='$sectionCode' and $TBSCHED_TIME='$ampm'"
                val editstat = db.update("TBSCHED", contentValues, where, null)
                Util.Msgbox(context, where)
                if (editstat < 0)
                    status = false
                else
                    status = true
            }//edit


            "DELETE" -> {

                var where =
                    "$TBSCHED_DATE='$myDate' and $TBSCHED_SECTION='$sectionCode' and $TBSCHED_TIME='$ampm'"
                val success = db.delete("TBSCHED", where, null)
                Toast.makeText(this.context, where, Toast.LENGTH_LONG).show();
//                if (success<0)
//                    status = false
//                else
//                    status = true
            }//edit

        }//when
        return status
        db.close()
    }

    fun GetStudentList(
        category: String,
        section: String = "",
        grp: String = "",
        lastname: String = ""
    ): List<Person> {

        val studentList: ArrayList<Person> = ArrayList<Person>()
        var sql: String = ""
        when (category) {
            "ALL" -> sql = "SELECT  * FROM $TABLE_NAME"
            "SECTION" -> sql =
                """
                "SELECT  * FROM $TABLE_NAME 
                where $TBSTUDENT_SECTION='$section' 
                and $TBSTUDENT_GRP like '$grp%'"
                """
            "NAME" -> sql =
                "SELECT  * FROM $TABLE_NAME where $TBSTUDENT_SECTION='$section' and $TBSTUDENT_LAST like '$lastname%'"
        }
        // Toast.makeText(this.context,  sql,  Toast.LENGTH_LONG).show();


        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var sn = cursor.getString(cursor.getColumnIndex(TBSTUDENT_STUDENTNO))
                var fname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_FIRST))
                var lname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_LAST))
                var grp = cursor.getString(cursor.getColumnIndex(TBSTUDENT_GRP))
                var section = cursor.getString(cursor.getColumnIndex(TBSTUDENT_SECTION))
                val emp = Person(sn, fname, lname, grp, section)
                studentList.add(emp)
            } while (cursor.moveToNext())
        }
        return studentList
    }

    fun GetScheduleList(sectioncode: String, monthname: String): List<ScheduleModel> {

        val schedList: ArrayList<ScheduleModel> = ArrayList<ScheduleModel>()
        var sql: String = "SELECT  * FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
        sql = sql + " and $TBSCHED_DATE like '$monthname%' order by $TBSCHED_DATE DESC"



        Log.e("SQL", sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var ampm = cursor.getString(cursor.getColumnIndex(TBSCHED_TIME))
                var myDate = cursor.getString(cursor.getColumnIndex(TBSCHED_DATE))
                var sectioncode = cursor.getString(cursor.getColumnIndex(TBSCHED_SECTION))
                var remark = cursor.getString(cursor.getColumnIndex(TBSCHED_REMARK))
                val sched = ScheduleModel(ampm, myDate, sectioncode, remark)
                schedList.add(sched)
            } while (cursor.moveToNext())
        }

        return schedList
    }


    fun CountAttendance(attStatus: String): String {
        var mydate: String = Util.ATT_CURRENT_DATE
        var ampm: String = Util.ATT_CURRENT_AMPM
        var section: String = Util.ATT_CURRENT_SECTION
        var sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                and $TBATTENDANCE_STATUS='$attStatus'
            """

        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        val count = cursor.count
        cursor.close()
        Util.Msgbox(context,  count.toString())
        return count.toString()

    }

    fun GetAttendanceList(category: String = "ALL", str: String = ""): List<AttendanceModel> {
        var mydate: String = Util.ATT_CURRENT_DATE
        var ampm: String = Util.ATT_CURRENT_AMPM
        var section: String = Util.ATT_CURRENT_SECTION
        val attendanceList: ArrayList<AttendanceModel> = ArrayList<AttendanceModel>()
        val sql: String



        when (category) {
            "GROUP" -> {
                sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                and $QRATTENDANCE_GRP like'$str%'
                """
            }//group

            "NAME" -> {
                sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                and $QRATTENDANCE_LAST like'$str%'
                """
            }//group

            "ATTENDANCE" -> {
                sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                and $QRATTENDANCE_STATUS ='$str'
                """
            }//group


            else -> {
                sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                """
            } //else
        }//when

        //Util.Msgbox(context, sql)

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                var ampm = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_TIME))
                var myDate = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_DATE))
                var sectionCode = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_SECTION))
                var remark = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_REMARK))
                var studentNo = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_STUDENTNO))
                var attendanceStatus = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_STATUS))
                var fname = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_FIRST))
                var lname = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_LAST))
                var grp = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_GRP))
                var completeName = lname + "," + fname
                val att = AttendanceModel(
                    ampm,
                    myDate,
                    sectionCode,
                    studentNo,
                    completeName,
                    grp,
                    attendanceStatus,
                    remark
                )
                Log.e("stud", "$completeName $attendanceStatus $category")
                attendanceList.add(att)
            } while (cursor.moveToNext())
        }
//        Util.Msgbox(context, sql)
      Log.e("RECORD", sql)
      Log.e("RECORD", attendanceList.size.toString())

        return attendanceList
    }


    fun UpdateStudentAttendance(  attStatus: String, studentNo: String ="" )  {
        var myDate: String = Util.ATT_CURRENT_DATE
        var ampm: String = Util.ATT_CURRENT_AMPM
        var section: String = Util.ATT_CURRENT_SECTION

        var sql:String
             if (studentNo == "") {
                 sql = """
              update tbattendance set $TBATTENDANCE_STATUS = '$attStatus'
              where $TBATTENDANCE_DATE='$myDate' 
              and $TBATTENDANCE_TIME='$ampm' 
              and $TBATTENDANCE_SECTION ='$section'
              """
             }
            else{
                 sql = """
              update tbattendance set $TBATTENDANCE_STATUS = '$attStatus'
              where $TBATTENDANCE_DATE='$myDate' 
              and $TBATTENDANCE_TIME='$ampm' 
              and $TBATTENDANCE_SECTION ='$section'
              and $TBATTENDANCE_STUDENTNO ='$studentNo'
              """
             }
        val db = this.writableDatabase
        db.execSQL(sql)
    }


    fun AddStudetAttendance(mydate: String, mytime: String, section: String) {
        val db = this.writableDatabase
        var sql =
            "INSERT INTO TBATTENDANCE (SchedTime, SchedDate, SectionCode , StudentNumber , AttendanceStatus ,Remark)"
        sql = sql + "SELECT '$mytime', '$mydate', '$section', studentno, '-' , '-'"
        sql = sql + " FROM   tbstudent where section = '$section'"
        Util.Msgbox(context, sql)
        db.execSQL(sql)
        ;
    }

    // fun  DeleteStudetAttendance(mydate:String, ampm:String, section:String){
    fun DeleteStudetAttendance() {
        val db = this.writableDatabase

        var sql = "DELETE FROM  TBATTENDANCE"
        db.execSQL(sql)

//        var where = "$TBATTENDANCE_DATE='$mydate' and $TBATTENDANCE_TIME='$ampm' and $TBSCHED_SECTION='$section'"
//        val success = db.delete("TBATTENDANCR",  where,null)
    }


    fun ShowAllRecord(tableName: String) {
        var sql: String = "SELECT  * FROM $tableName"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        val FILENAME = tableName + ".csv"
        val heading = ShowField(tableName)


        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val myFile = File(folder, FILENAME)
        val fstream = FileOutputStream(myFile)
        fstream.write(heading.toByteArray())
        fstream.write("\n".toByteArray())
//        for (mylist in MyRecycle.list) {
//            var myData = mylist.studentno + "," + mylist.firstname + "," + mylist.lastname
//            myData = myData + "," + mylist.sectioncode + "," + mylist.grp
//        }


        var p: String = ""
        if (cursor.moveToFirst()) {
            do {
                when (tableName) {
                    "TBSTUDENT" -> {
                        p =
                            cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_STUDENTNO))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_FIRST))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_LAST))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_GRP))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSTUDENT_SECTION))
                    }

                    "TBSCHED" -> {
                        p = cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSCHED_TIME))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSCHED_DATE))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSCHED_SECTION))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBSCHED_REMARK))
                    }

                    "TBATTENDANCE" -> {
                        p =
                            cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBATTENDANCE_TIME))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBATTENDANCE_DATE))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBATTENDANCE_SECTION))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBATTENDANCE_STUDENTNO))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBATTENDANCE_STATUS))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(DatabaseHandler.TBATTENDANCE_REMARK))
                    }

                    "TBATTENDANCE_QUERY" -> {
                        p = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_TIME))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_DATE))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_SECTION))
                        p =
                            p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_STUDENTNO))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_STATUS))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_REMARK))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_FIRST))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_LAST))
                        p = p + "," + cursor.getString(cursor.getColumnIndex(QRATTENDANCE_GRP))
                    }
                }

                Util.Msgbox(context, p)    // val emp = Person(sn, fname, lname, grp, section)
                fstream.write(p.toByteArray())
                fstream.write("\n".toByteArray())
                Log.e("RECORD", p)
            } while (cursor.moveToNext())

        }
        fstream.close()
    }

}



class ScheduleModel(
    var ampm: String,
    var myDate: String,
    var sectioncode: String,
    var renark: String
)

class AttendanceModel(
    var ampm: String,
    var myDate: String,
    var sectionCode: String,
    var studentNo: String,
    var completeName:String,
    var groupNumber:String,
    var attendanceStatus: String,
    var remark: String
)


