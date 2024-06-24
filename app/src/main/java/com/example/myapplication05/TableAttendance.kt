package com.example.myapplication05

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import com.example.myapplication05.Util.Companion.ATT_CURRENT_DATE as ATT_CURRENT_DATE1

class TableAttendance(context: Context) : DatabaseHandler(context) {
    companion object {
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
        private val QRATTENDANCE_GENDER = "Gender"
    }

    fun ManageSched(crudStatus: String, ampm: String, myDate: String, sectionCode: String, schedID: String, day:String, remark: String = "-"
    ): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBSCHED_TIME, ampm)
        contentValues.put(TBSCHED_DATE, myDate) // EmpModelClass Name
        contentValues.put(TBSCHED_SECTION, sectionCode) // EmpModelClass Phone
        contentValues.put(TBSCHED_REMARK, remark) // EmpModelClass Phone
        contentValues.put("SchedID", schedID) // EmpModelClass Phone
        contentValues.put("Day", day) // EmpModelClass Phone
        var term = context?.let { Util.GetCurrentGradingPeriod(it) }
        contentValues.put("SchedTerm", term) // EmpModelClass Phone
        var status: Boolean = false;
        when (crudStatus) {
            "ADD" -> {
                val success = db.insert("tbsched", null, contentValues)
                if (success < 0) status = false
                else status = true
            } //add

            "EDIT" -> {
                var where =
                    "$TBSCHED_DATE='$myDate' and $TBSCHED_SECTION='$sectionCode' and $TBSCHED_TIME='$ampm'"
                val editstat = db.update("TBSCHED", contentValues, where, null)
                Util.Msgbox(context, where)
                if (editstat < 0) status = false
                else status = true
            } //edit


            "DELETE" -> {

                var where =
                    "$TBSCHED_DATE='$myDate' and $TBSCHED_SECTION='$sectionCode' and $TBSCHED_TIME='$ampm'"
                val success = db.delete("TBSCHED", where, null)

                where =
                    "$TBATTENDANCE_DATE='$myDate' and $TBATTENDANCE_SECTION='$sectionCode' and $TBATTENDANCE_TIME='$ampm'"
                db.delete("TBATTENDANCE", where, null)

                Toast.makeText(this.context, where, Toast.LENGTH_LONG)
                    .show(); //                if (success<0)
                //                    status = false
                //                else
                //                    status = true
            } //edit

        } //when
        return status
        db.close()
    }


    fun GetScheduleList(sectioncode: String, category: String, monthname: String = "", orderby:String =""): List<ScheduleModel> {

        val schedList: ArrayList<ScheduleModel> = ArrayList<ScheduleModel>()
        var sql = ""
        Log.e("MMM", category + "  " + monthname)
        if (category == "ALL") {
            sql = "SELECT  *  FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
            sql = sql + " order by schedID"
        } else if (category == "FIRST") {
            sql = "SELECT  * FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
            sql = sql + " and SchedTerm = 'FIRST' order by $TBSCHED_DATE DESC"
        } else if (category == "SECOND") {
            sql = "SELECT  * FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
            sql = sql + " and  SchedTerm = 'SECOND' order by $TBSCHED_DATE DESC"
        } else if (category == "MONTH" && orderby=="ASC") {
            sql = "SELECT  * FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
            sql = sql + " and $TBSCHED_DATE like '$monthname%' order by $TBSCHED_DATE"

    } else if (category == "MONTH") {
        sql = "SELECT  * FROM TBSCHED where $TBSCHED_SECTION='$sectioncode'"
        sql = sql + " and $TBSCHED_DATE like '$monthname%' order by $TBSCHED_DATE DESC"
    }


        Log.e("3453", sql)
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
                var term = cursor.getString(cursor.getColumnIndex("SchedTerm"))
                var schedID = cursor.getString(cursor.getColumnIndex("SchedID"))
                var day = cursor.getString(cursor.getColumnIndex("Day"))
                val sched = ScheduleModel(ampm, myDate, sectioncode, remark, term, schedID, day)
                schedList.add(sched)
            } while (cursor.moveToNext())
        } //        SchedTime	SchedDat	SectionCode	Remark	SchedTerm
        return schedList
    }

    fun CountAttendance(attStatus: String, mydate: String, ampm: String, section: String): String {

        var sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                and $TBATTENDANCE_STATUS='$attStatus'
            """

        val db = this.readableDatabase
        Log.e("157", sql )
        val cursor = db.rawQuery(sql, null)
        val count = cursor.count
        cursor.close()
        Util.Msgbox(context, count.toString())
        return count.toString()

    }

    fun GetAttendanceList(mydate:String, ampm:String ,     category: String = "ALL", search_string: String=""): ArrayList<AttendanceModel> {
//        var mydate: String = ATT_CURRENT_DATE1
//        var ampm: String = Util.ATT_CURRENT_AMPM
   var section: String = Util.ATT_CURRENT_SECTION
//        Log.e("3612-4", ATT_CURRENT_DATE1)
        val attendanceList: ArrayList<AttendanceModel> = ArrayList<AttendanceModel>()
        var sql: String = ""
Log.e("3489", Util.CURRENT_DATE)
        var sql1 = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                """
        Log.e("ATTX", category)
        if (category == "ATT_STATUS") {
            sql = sql1 + "and AttendanceStatus = '$search_string' order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "A-C") {
            sql1 =
                sql1 + "and (LASTNAME LIKE 'A%' OR LASTNAME LIKE 'B%' OR LASTNAME LIKE 'C%' )  order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "D-J") {
            sql1 =
                sql1 + "and (LASTNAME LIKE 'F%' OR LASTNAME LIKE 'G%' OR LASTNAME LIKE 'H%' OR LASTNAME LIKE 'I%'  OR LASTNAME LIKE 'J%' OR LASTNAME LIKE 'D%'  OR LASTNAME LIKE 'E%') order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "K-O") {
            sql1 =
                sql1 + "and (LASTNAME LIKE 'K%' OR LASTNAME LIKE 'L%' OR LASTNAME LIKE 'M%' OR LASTNAME LIKE 'N%'  OR LASTNAME LIKE 'O%') order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "P-R") {
            sql1 =
                sql1 + "and (LASTNAME LIKE 'P%' OR LASTNAME LIKE 'Q%' OR LASTNAME LIKE 'R%' ) order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "S-Z") {
            sql1 =
                sql1 + "and (LASTNAME LIKE 'U%' OR LASTNAME LIKE 'V%' OR LASTNAME LIKE 'W%' OR LASTNAME LIKE 'X%'  OR LASTNAME LIKE 'Y%' OR LASTNAME LIKE 'Z%' OR LASTNAME LIKE 'S%'  OR LASTNAME LIKE 'T%') order by lastname"
        } else {
            when (category) {
                "GROUP" -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                and $QRATTENDANCE_GRP like'$search_string%'
                ORDER BY $QRATTENDANCE_GENDER DESC, $QRATTENDANCE_LAST
                """

                } //group


                "INDIVIDUAL" -> {
                    val currentGradingPeriod = context?.let { Util.GetCurrentGradingPeriod(it) }
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE  $QRATTENDANCE_SECTION='$section'
                AND  SchedTerm = '$currentGradingPeriod'
                AND   StudentNumber = '$search_string'
                ORDER BY SchedID desc
                """

                } //group


                "NAME" -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                and $QRATTENDANCE_LAST like'$search_string%'
                ORDER BY $QRATTENDANCE_GENDER DESC, $QRATTENDANCE_LAST
                """
                } //group

                "ATTENDANCE" -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE='$mydate' 
                and $QRATTENDANCE_TIME='$ampm' 
                and $QRATTENDANCE_SECTION='$section'
                and $QRATTENDANCE_STATUS ='$search_string'
                ORDER BY $QRATTENDANCE_GENDER DESC, $QRATTENDANCE_LAST
                """
                } //group

                "LASTNAME" -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                ORDER BY  $QRATTENDANCE_LAST , $QRATTENDANCE_FIRST
                """
                } //group

                "FIRSTNAME" -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                ORDER BY  $QRATTENDANCE_FIRST, $QRATTENDANCE_LAST
                """
                } //group

                "RANDOM" -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                ORDER BY RandomNumber
                """
                } //group


                else -> {
                    sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE='$mydate' 
                and $TBATTENDANCE_TIME='$ampm' 
                and $TBATTENDANCE_SECTION='$section'
                ORDER BY $QRATTENDANCE_GENDER DESC, $QRATTENDANCE_LAST
                """
                } //else
            } //when
        } //Util.Msgbox(context, sql)
        Log.e("4655", sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            if (category == "SEARCHLETTER") {
                Log.e("ATTSQ20L", sql1)
                cursor = db.rawQuery(sql1, null)
            } else cursor = db.rawQuery(sql, null)

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
                var completeName = lname.toUpperCase() + "," + fname
                var taskpoint = cursor.getString(cursor.getColumnIndex("TaskPoints")).toInt()
                var recitationPoint =
                    cursor.getString(cursor.getColumnIndex("RecitationPoints")).toInt()
                var randomNumber = cursor.getString(cursor.getColumnIndex("RandomNumber")).toInt()
                val att =
                    AttendanceModel(ampm, myDate, sectionCode, studentNo, completeName, grp, attendanceStatus, remark, recitationPoint, taskpoint, randomNumber, fname, lname)
                Log.e("stud", "$completeName $attendanceStatus $category")
                attendanceList.add(att)
            } while (cursor.moveToNext())
        } //        Util.Msgbox(context, sql)
        Log.e("RECORD", sql)
        Log.e("RECORD", attendanceList.size.toString())

        return attendanceList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun UpdateStudentAttendance(attStatus: String, studentNo: String = "", myDate: String, ampm: String, remark: String = "-") {
        var section: String = Util.ATT_CURRENT_SECTION
        val db = this.writableDatabase
        var sql: String
        if (studentNo == "ALL") {
            sql = """
              update tbattendance set $TBATTENDANCE_STATUS = '$attStatus'
              where $TBATTENDANCE_DATE='$myDate' 
              and $TBATTENDANCE_TIME='$ampm' 
              and $TBATTENDANCE_SECTION ='$section'
              """

            db.execSQL(sql)
            WriteAttendancveCSV(section, myDate, ampm, "YES")
        } else {
            sql = """
              update tbattendance set $TBATTENDANCE_STATUS = '$attStatus',
              $TBATTENDANCE_REMARK = '$remark'
              where $TBATTENDANCE_DATE='$myDate' 
              and $TBATTENDANCE_TIME='$ampm' 
              and $TBATTENDANCE_SECTION ='$section'
              and $TBATTENDANCE_STUDENTNO ='$studentNo'
              """
            db.execSQL(sql)
            IncremeentalBackUpAttendance(section, studentNo, myDate, ampm, attStatus, 0, 0)
            WriteAttendancveCSV(section, myDate, ampm)
        }
        Log.e("ATTSQL", sql)

    }

    fun UpdateRecitation(score: Int, studentNo: String, theDate: String = "", theAmpm: String = "", theSection: String = "") {

        var myDate = ""
        var ampm = ""
        var section = ""
        Log.e("RRR20", theAmpm)
        if (theDate == "") myDate = ATT_CURRENT_DATE1
        else myDate = theDate

        if (theAmpm == "") ampm = Util.ATT_CURRENT_AMPM
        else ampm = theAmpm

        if (section == "") section = Util.ATT_CURRENT_SECTION
        else section = theSection

        var sql: String

        sql = """
            update tbattendance set RecitationPoints = '$score'
            where $TBATTENDANCE_DATE='$myDate' 
            and $TBATTENDANCE_TIME='$ampm' 
            and $TBATTENDANCE_SECTION ='$section'
            and $TBATTENDANCE_STUDENTNO ='$studentNo'
            """
        Log.e("RRR", sql)
        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun UpdateTaskScore(score: Int, studentNo: String, theDate: String = "", theAmpm: String = "", theSection: String = "") {


        var sql: String

        sql = """
            update tbattendance set TaskPoints = '$score'
            where $TBATTENDANCE_DATE='$theDate' 
            and $TBATTENDANCE_TIME='$theAmpm' 
            and $TBATTENDANCE_SECTION ='$theSection'
            and $TBATTENDANCE_STUDENTNO ='$studentNo'
            """
        Log.e("RRR", sql)
        val db = this.writableDatabase
        db.execSQL(sql)
    }


    @RequiresApi(Build.VERSION_CODES.O)

    fun IncremeentalBackUpAttendance(sectionCode: String, studentNo: String, myDate: String, myTime: String, attStatus: String, taskScore: Int = 0, recPoint: Int = 0) {
        val folderDest = "/storage/emulated/0/ANDROID BACKUP/" + sectionCode
        val fileDest = File(folderDest)
        if (!fileDest.exists()) fileDest.mkdir()
        val filename = myDate + "-" + myTime + ".csv"

        val heading = "SN, CompleteName,Status,TaakPoint,Recitation, Day, Timw\n"
        val myFile = File(folderDest, filename)
        var headStatus = true;
        if (myFile.exists()) {
            headStatus = false
        }

        val fstream = FileOutputStream(myFile, true)

        if (headStatus == true) {
            fstream.write(heading.toByteArray())
        }

        var sql = """select * from  tbstudent_query
                    WHERE StudentNo='$studentNo'
                    AND Section='$sectionCode'
                    """

        Log.e("444", sql)
        val formatter = SimpleDateFormat("MM-dd")
        val date = Date()
        val date1 = formatter.format(date)

        val formatter2 = SimpleDateFormat("HH:mm")

        val time = formatter2.format(date)


        val db2 = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db2.rawQuery(sql, null)
        var name = ""
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex("LastName")) + "  "
            name = name + cursor.getString(cursor.getColumnIndex("FirstName"))
        }
        Log.e("Hello", sql)
        val str = "$studentNo, $name, $attStatus, $taskScore,$recPoint, $date1, $time\n"
        Log.e("444", str)
        fstream.write(str.toByteArray())
        fstream.close()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun WriteAttendancveCSV(sectionCode: String, myDate: String, myTime: String, presentAll: String = "NO") {
        try {
            val folderSource = "/storage/emulated/0/DriveSyncFiles/ANDROID BACKUP/" + sectionCode

            val file = File(folderSource)
            if (!file.exists()) file.mkdir()

            val filename = myDate + "-" + myTime + ".csv"


            val heading = "SN, CompleteName,Status,TaakPoint,Recitation\n"
            val myFile = File(folderSource, filename)
            val fstream = FileOutputStream(myFile)
            fstream.write(heading.toByteArray())
            Log.e("235", folderSource + "   " + filename)

            //            SchedDate	SchedTime	SectionCode	StudentNumber	Remark	AttendanceStatus	RandomNumber
            //            RecitationPoints	SchedTerm	SchedID	TaskPoints	GrpNumber	FirstName	LastName	Gender

            var sql = """select * from  tbattendance_query
                    WHERE SectionCode='$sectionCode'
                    AND SchedDate='$myDate'
                    AND SchedTime='$myTime'
                    ORDER BY lastname
                    """
            val db = this.readableDatabase
            var cursor1: Cursor? = null
            cursor1 = db.rawQuery(sql, null)
            val columnNames: Array<String> = cursor1.getColumnNames()
            Log.e("678", columnNames.joinToString())
            if (cursor1.moveToFirst()) {
                do {
                    val studnum = cursor1.getString(cursor1.getColumnIndex("StudentNumber"))
                    var str = studnum + ","
                    str = str + cursor1.getString(cursor1.getColumnIndex("LastName")) + " "
                    str = str + cursor1.getString(cursor1.getColumnIndex("FirstName")) + ","
                    var attStatus = cursor1.getString(cursor1.getColumnIndex("AttendanceStatus"))
                    var taskScore = cursor1.getString(cursor1.getColumnIndex("TaskPoints")).toInt()
                    var recPoint =
                        cursor1.getString(cursor1.getColumnIndex("RecitationPoints")).toInt()
                    str = str + attStatus + "," + taskScore + "," + recPoint + "\n"
                    fstream.write(str.toByteArray())
                    Log.e("678", str)
                    if (presentAll == "YES") {
                        IncremeentalBackUpAttendance(sectionCode, studnum, myDate, myTime, attStatus, taskScore, recPoint)
                    }
                } while (cursor1.moveToNext())
            }
            fstream.close()
        } catch (e: Exception) {
            Log.e("err", e.toString())
        }
    }


    fun UpdateStudentActivityStatus(statusd: String, studentNo: String, section: String, activityCode: String) {

        // CREATE TABLE tbscore  (ActivityCode  TEXT,SectionCode  text,StudentNo  text,Score  INTEGER,Remark  text, SubmissionStatus  text,PRIMARY KEY ( ActivityCode ,  SectionCode ,  StudentNo
        var sql: String

        sql = """
              update tbscore set SubmissionStatus = '$statusd'
              where SectionCode='$section'
              and ActivityCode='$activityCode'
              and StudentNo ='$studentNo'
              """


        val db = this.writableDatabase
        db.execSQL(sql)
        Log.e("123", sql)

    }


    fun AddStudetAttendance(mydate: String, mytime: String, section: String) {
        val db = this.writableDatabase
        var sql = """
            INSERT INTO TBATTENDANCE (SchedTime, SchedDate, SectionCode , StudentNumber , AttendanceStatus ,Remark)
            SELECT '$mytime', '$mydate', '$section', studentno, '-' , '-'
            FROM   tbstudent_query where section = '$section'
            """
        Util.Msgbox(context, sql)
        db.execSQL(sql);


    }


    fun RandomnNum(section: String, schedDate: String) {
        val db = this.readableDatabase
        var sql = """
            select * 
            FROM   tbstudent where section = '$section' order by lastname
            """
        Log.e("E", sql)

        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        val numberArray = IntArray(cursor.count)
        val statusArray = BooleanArray(cursor.count)
        val studentNoArray = Array<String>(cursor.count) { "" }
        var randomNumber = 0

        Log.e("E", cursor.count.toString())




        for (i in 0..cursor.count - 1) {
            do {
                randomNumber = Random.nextInt(0, cursor.count)
            } while (statusArray[randomNumber] == true)
            statusArray[randomNumber] = true
            numberArray[i] = randomNumber

        }

        for (i in 0..cursor.count - 1) {
            Log.e("RND", i.toString() + "    " + numberArray[i].toString())
        }

        var j = 0;
        if (cursor.moveToFirst()) {
            do {
                studentNoArray[j] = cursor.getString(cursor.getColumnIndex("StudentNo"))
                j++;
            } while (cursor.moveToNext())
        }

        for (i in 0..cursor.count - 1) {
            var rndNumber = numberArray[i]
            var studentNo = studentNoArray[i]


            sql = """
              update tbattendance set 
              RandomNumber = '$rndNumber'
              where SectionCode='$section'
              and StudentNumber='$studentNo'
              and SchedDate ='$schedDate'
              """

            val db = this.writableDatabase
            db.execSQL(sql)


        }


    }

    // fun  DeleteStudetAttendance(mydate:String, ampm:String, section:String){
    fun DeleteStudetAttendance() {
        val db = this.writableDatabase
        var sql = "DELETE FROM  TBATTENDANCE"
        db.execSQL(sql)
    }

    fun GetIndividuaList(monthname: String, studentno: String): List<IndividualModel> {

        val individualList: ArrayList<IndividualModel> = ArrayList<IndividualModel>()
        val sql: String
        sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $QRATTENDANCE_DATE like '$monthname%' 
                and $QRATTENDANCE_STUDENTNO='$studentno' 
                order by  $QRATTENDANCE_DATE 
                """
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
                var remark = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_REMARK))
                var attendanceStatus = cursor.getString(cursor.getColumnIndex(QRATTENDANCE_STATUS))
                val individual = IndividualModel(ampm, myDate, attendanceStatus, remark)
                individualList.add(individual)
            } while (cursor.moveToNext())
        } //        Util.Msgbox(context, sql)
        //              Log.e("RECORD", sql)
        //        Log.e("RECORD", attendanceList.size.toString())

        return individualList
    }


    fun GetCountAttendanceList(myMonth: String, section: String, category: String = "SECTION", search_string: String = ""): List<SummaryModel> {

        val studentList: List<EnrolleModel>

        // Util.Msgbox(context, "$myMonth" )
        val attendanceCount: ArrayList<SummaryModel> = ArrayList<SummaryModel>()
        when (category) {
            "GROUP" -> studentList = GetEnrolleList(category, section, search_string)
            "NAME" -> studentList = GetEnrolleList(category, section, search_string)
            else -> studentList = GetEnrolleList("SECTION", section)
        }


        //  Util.Msgbox(context, "$myMonth" )
        for (student in studentList) {
            val studentname = student.lastname + "," + student.firstname
            var studentNo = student.studentno
            var presentCount = GetIndividualCouunt(studentNo, myMonth, "P")
            var lateCount = GetIndividualCouunt(studentNo, myMonth, "L")
            var absentCount = GetIndividualCouunt(studentNo, myMonth, "A")
            var excuseCount = GetIndividualCouunt(studentNo, myMonth, "E")
            val sum =
                SummaryModel(studentNo, studentname, presentCount, lateCount, absentCount, excuseCount, student.firstname, student.lastname)
            attendanceCount.add(sum)

            Log.e("ATT", "$studentname  $presentCount $lateCount, $absentCount, $excuseCount")
        }

        return attendanceCount
    }

    fun GetIndividualCouunt(studentNo: String, monthName: String, attendanceStatus: String, category: String = ""): Int { //        var mydate: String = Util.ATT_CURRENT_DATE
        //        var ampm: String = Util.ATT_CURRENT_AMPM
        //        var section: String = Util.ATT_CURRENT_SECTION
        val db = this.readableDatabase
        var sql = ""
        if (category == "RECITATION") {
            sql = """
                SELECT SUM(RecitationPoints) AS totalRec FROM TBATTENDANCE_QUERY 
                 WHERE SchedTerm = '$monthName'
                and $TBATTENDANCE_STUDENTNO='$studentNo'
                and RecitationPoints >=0
            """
            Log.e("SUSU", sql)
            val cursor = db.rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex("totalRec")) == null) return 0;
                else return cursor.getString(cursor.getColumnIndex("totalRec")).toInt()
            }

        } else if (category == "TASK") {
            sql = """
                SELECT SUM(TaskPoints) AS totalTask FROM TBATTENDANCE_QUERY 
                 WHERE SchedTerm = '$monthName'
                and $TBATTENDANCE_STUDENTNO='$studentNo'
            """
            Log.e("SUSU", sql)
            val cursor = db.rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex("totalTask")) == null) return 0
                else return cursor.getString(cursor.getColumnIndex("totalTask")).toInt()
            }
            return 0;
        } else if (monthName == "FIRST" || monthName == "SECOND") {
            sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE SchedTerm = '$monthName'
                and $TBATTENDANCE_STUDENTNO='$studentNo'
                and $TBATTENDANCE_STATUS='$attendanceStatus'
            """
        } else if (monthName == "ALL") {
            sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                where $TBATTENDANCE_STUDENTNO='$studentNo'
                and $TBATTENDANCE_STATUS='$attendanceStatus'
            """
        } else {
            sql = """
                SELECT * FROM TBATTENDANCE_QUERY 
                WHERE $TBATTENDANCE_DATE LIKE'$monthName%' 
                and $TBATTENDANCE_STUDENTNO='$studentNo'
                and $TBATTENDANCE_STATUS='$attendanceStatus'
            """
        }

        Log.e("ATT", "$sql")

        val cursor = db.rawQuery(sql, null)

        val count = cursor.count
        cursor.close() //Util.Msgbox(context, count.toString())
        return count
    }

    fun GetStudentNumbet(lastName: String, firstName: String = ""): String {
        val sql: String
        sql = """
                SELECT * FROM TBSTUDENT 
                WHERE firstName='$firstName' 
                and lastName='$lastName' 
                """ //Log.e("!!!!", sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var studentNo = ""
        if (cursor.moveToFirst()) {
            studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))

        }
        return studentNo;
    }


    fun GetYesSubmissionCount(section: String, activityCode: String): String {

        var sql: String
        sql = """
                SELECT * FROM tbstudent_query 
                WHERE section='$section' 
                """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        // CREATE TABLE tbscore  (ActivityCode  TEXT,SectionCode  text,StudentNo  text,Score  INTEGER,Remark  text, SubmissionStatus  text,PRIMARY KEY ( ActivityCode ,  SectionCode ,  StudentNo ));


        sql = """
                SELECT * FROM TBSCORE
                WHERE ActivityCode='$activityCode' 
                AND SectionCode='$section' 
                AND (SubmissionStatus='YES' OR  SubmissionStatus='OK') 
                """
        val db2 = this.readableDatabase
        var cursor2: Cursor? = null
        cursor2 = db.rawQuery(sql, null)
        var num = cursor2.count
        var num2 = cursor.count
        Log.e("DIV", num.toString() + "  " + num2 + " " + num / num2)
        return (cursor2.count / cursor.count.toDouble() * 100).toInt().toString() + "%"
    }

    fun GetSubmissionCount(section: String, activityCode: String, submissionStatus: String): Int {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var sql = """
                SELECT * FROM TBSCORE
                WHERE ActivityCode='$activityCode' 
                AND SectionCode='$section' 
                AND SubmissionStatus='$submissionStatus' 
                """
        cursor = db.rawQuery(sql, null)
        return cursor.count
    }


    fun GetStatusStudent(studentNo: String, section: String, activityCode: String): String {

        var sql: String
        sql = """
              select * from  tbscore 
              where SectionCode='$section'
              and ActivityCode='$activityCode'
              and StudentNo ='$studentNo'
              """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("2121", sql)
        var submissionStatus = ""
        if (cursor.moveToFirst()) {
            submissionStatus = cursor.getString(cursor.getColumnIndex("SubmissionStatus"))
        }

        return submissionStatus

    }

    fun GetStudentNumber(name: String): String {
        val arr = name.split(",").toTypedArray()
        val firstname = arr[1]
        val lastname = arr[0]
        Log.e("search", name + arr[0] + arr.get(0))
        var sql: String
        sql = """
              select * from  tbstudent 
              where firstName='$firstname'
              and lastName='$lastname'
              """
        var studentNo = ""
        Log.e("search", sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))
        }

        return studentNo

    }


    fun UpdateStudentActivityScore(statusd: String, score: Int, studentNo: String, section: String, activityCode: String) {

        // CREATE TABLE tbscore  (ActivityCode  TEXT,SectionCode  text,StudentNo  text,Score  INTEGER,Remark  text, SubmissionStatus  text,PRIMARY KEY ( ActivityCode ,  SectionCode ,  StudentNo
        var sql: String

        sql = """
              update tbscore set SubmissionStatus = '$statusd',
              score = '$score'
              where SectionCode='$section'
              and ActivityCode='$activityCode'
              and StudentNo ='$studentNo'
              """


        val db = this.writableDatabase
        db.execSQL(sql)
        Log.e("123", sql)

    }


    fun UpdateStudentActivityStatusNew(status: String, studentNo: String, section: String, activityCode: String) {
        var sql: String
        sql = """
              update tbscore set SubmissionStatus = '$status'
              where SectionCode='$section'
              and ActivityCode='$activityCode'
              and StudentNo ='$studentNo'
              """

        Log.e("", sql)
        val db = this.writableDatabase
        db.execSQL(sql)


    }


}


