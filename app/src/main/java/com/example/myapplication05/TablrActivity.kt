package com.example.myapplication05

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class TableActivity(context: Context) : DatabaseHandler(context) {

    companion object {
        private val TBACTIVITY_CODE = "ActivityCode"
        private val TBACTIVITY_SECTION = "SectionCode"
        private val TBACTIVITY_DESCRIPTION = "Description"
        private val TBACTIVITY_ITEM = "Item"
        private val TBACTIVITY_GRADINGPERIOD = "GradingPeriod"
        private val TBACTIVITY_CATEGORY = "Category"

        private val TBSCORE_SECTION = "ActivityCode"
        private val TBSCORE_CODE = "SectionCode"
        private val TBSCORE_STUDENTNO = "StudentNo"
        private val TBSCORE_REMARK = "Remark"
        private val TBSCORE_SCORE = "Score"
        private val TBSCORE_SUBMISSION = "SubmissionStatus"

        private val QRSCORE_CODE = "ActivityCode"
        private val QRSCORE_SECTION = "SectionCode"
        private val QRSCORE_STUDENTNO = "StudentNo"
        private val QRSCORE_REMARK = "Remark"
        private val QRSCORE_SUBMISSION = "SubmissionStatus"
        private val QRSCORE_SCORE = "Score"
        private val QRSCORE_FIRST = "FirstName"
        private val QRSCORE_LAST = "LastName"
        private val QRSCORE_GROUP = "GrpNumber"
        private val QRSCORE_GENDER =
            "Gender" //ActivityCode	SectionCode	StudentNo	Score	Remark	SubmissionStatus

    }


    fun ManageActivity(crudStatus: String, activityCode: String, sectionCode: String = "", description: String = "", item: String = "0", actCategory: String = "", gradingPeriod: String = ""): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBACTIVITY_CODE, activityCode)
        contentValues.put(TBACTIVITY_SECTION, sectionCode)
        contentValues.put(TBACTIVITY_DESCRIPTION, description)
        contentValues.put(TBACTIVITY_ITEM, item.toInt())
        contentValues.put(TBACTIVITY_GRADINGPERIOD, gradingPeriod)
        contentValues.put(TBACTIVITY_CATEGORY, actCategory)
        var status: Boolean = false;

        if (crudStatus == "ADD") {
            val success = db.insert("tbactivity", null, contentValues)
            Util.Msgbox(context, success.toString())
            if (success < 0) status = false
            else status = true

        } else if (crudStatus == "EDIT") {
            var where = "$TBACTIVITY_CODE='$activityCode' and $TBACTIVITY_SECTION='$sectionCode'"
            val editstat = db.update("TBACTIVITY", contentValues, where, null)
            if (editstat < 0) status = false
            else status = true

        } else if (crudStatus == "DELETE") {
            var where = "$TBACTIVITY_CODE='$activityCode' and $TBACTIVITY_SECTION='$sectionCode'"
            var success = db.delete("TBACTIVITY", where, null)
            db.delete("TBSCORE", where, null)


            if (success < 0) status = false
            else status = true
        }
        return true;
        db.close()
    }

    fun GetActivityList(sectioncode: String, gradingPeriod: String = "", category: String = ""): ArrayList<ActivityModel> {
        val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
        val sql: String

        sql = """
                SELECT * FROM TBACTIVITY 
                WHERE $TBACTIVITY_SECTION='$sectioncode'    
                AND $TBACTIVITY_GRADINGPERIOD='$gradingPeriod' 
                ORDER BY $TBACTIVITY_CODE DESC
                """

        Log.e("PPP", sql)
        Log.e("PPP", gradingPeriod)
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
                var activityCode = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var sectionCode = cursor.getString(cursor.getColumnIndex(TBACTIVITY_SECTION))
                var description = cursor.getString(cursor.getColumnIndex(TBACTIVITY_DESCRIPTION))
                var item = cursor.getString(cursor.getColumnIndex(TBACTIVITY_ITEM)).toInt()
                var status = cursor.getString(cursor.getColumnIndex(TBACTIVITY_GRADINGPERIOD))
                var category = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CATEGORY))
                Log.e("XXX", status)
                val att =
                    ActivityModel(activityCode, sectionCode, description, item, status, category, "HIDE")
                activityList.add(att)
            } while (cursor.moveToNext())
        }
        return activityList
    }

    fun GetActivitYInfo(sectioncode: String, activityCode: String): Cursor {

        val sql: String

        sql = """
                SELECT * FROM TBACTIVITY 
                WHERE $TBACTIVITY_SECTION='$sectioncode'    
                AND $TBACTIVITY_CODE='$activityCode' 
                """


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)


        if (cursor.moveToFirst()) {
            return cursor
        }
        return cursor
    }

    fun UpdateSheetData(sectioncode: String, activityCode: String, sheetName: String) {

        val sql: String

        sql = """
               update tbactivity set   SheetName='$sheetName' 
               where activitycode= "$activityCode" 
               and sectionCode = '$sectioncode'
                """
        val db = this.writableDatabase
        db.execSQL(sql)

    }


    fun GetNewActivityCode(sectioncode: String): String {
        val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
        val sql: String
        sql = """
                SELECT * FROM TBACTIVITY 
                WHERE $TBACTIVITY_SECTION='$sectioncode' 
                ORDER BY $TBACTIVITY_CODE DESC
                """


        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            var actCode = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
            var num = actCode.takeLast(2).toInt() + 1 // Grade>>>
            Util.Msgbox(context, num.toString())
            return "ACT-" + Util.ZeroPad(num, 2)
        } else { // Util.Msgbox(context, "ACT-01" )
            return "ACT-01"
        } //  return  "helo"
    }


    fun GetNewSchedCode(sectioncode: String): String {
        val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
        val sql: String
        sql = """
                SELECT * FROM TBSCHED 
                WHERE SectionCode='$sectioncode' 
                ORDER BY SchedID DESC
                """

        Log.e("AAA", sql)
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            var actCode = cursor.getString(cursor.getColumnIndex("SchedID"))
            var num = actCode.takeLast(2).toInt() + 1 // Grade>>>
            Util.Msgbox(context, num.toString())
            return Util.ZeroPad(num, 2)
        } else { // Util.Msgbox(context, "ACT-01" )
            return "01"
        } //  return  "helo"
    }


    fun AddStudeScore(sectionCode: String, activityCode: String) {
        val db = this.writableDatabase
        var sql = """
            INSERT INTO TBSCORE (ActivityCode, SectionCode,StudentNo,Score, Remark, SubmissionStatus, AdjustedScore)
            SELECT '$activityCode', '$sectionCode', studentno, '0' , '-', 
            CASE
            WHEN EnrollmentStatus  = "ENROLLED" THEN 'NO'
            WHEN EnrollmentStatus  = "DROPPED"  THEN 'DRP'
            END, '0'
            FROM   tbstudent_query  where section = '$sectionCode'
            """ // Util.Msgbox(context, sql)
        db.execSQL(sql);
    }

    fun DuplicateActivityWithScore(sectionCode: String, activityCode: String, oldActivityCode: String) {
        val db = this.writableDatabase
        var sql = """
            INSERT INTO TBSCORE (ActivityCode, SectionCode,StudentNo,Score, Remark, SubmissionStatus, AdjustedScore)
            SELECT '$activityCode',SectionCode, studentno, score , '-', SubmissionStatus, 0
            FROM   TBSCORE
            where SectionCode = '$sectionCode'
            AND ActivityCode = '$oldActivityCode'
            """
        Log.e("SQL", sql)
        db.execSQL(sql);
    }


    fun UpdateCategory(sectionCode: String, activityCode: String, newCategory: String) {
        val db = this.writableDatabase
        var sql = """
             UPDATE tbActivity
                    SET Category = '$newCategory' 
                    WHERE ActivityCode='$activityCode' 
                    AND  SectionCode='$sectionCode' 
                    
            """

        db.execSQL(sql);
    }


    fun SaveLogDatabase(logID: String, dateTime: String, desc: String) {
        val db = this.writableDatabase //LogID	DateTime	Description
        var sql = """
            INSERT INTO TBLOG (LogID, DateTime,Description)
            VALUES('$logID', '$dateTime', '$desc')
            """ // Util.Msgbox(context, sql)\
        Log.e("yyy", sql);
        db.execSQL(sql);
    }

    fun GetScoreList(sectioncode: String, activityCode: String, category: String = "", search_string: String = "", zeroOnly: Boolean = false): ArrayList<ScoreModel> {
        val scoreList: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
        var sql: String
        sql = """   SELECT * FROM TBSCORE_QUERY 
                    WHERE $QRSCORE_SECTION='$sectioncode' 
                    AND $QRSCORE_CODE='$activityCode' """

        val sorted = " ORDER BY $QRSCORE_GENDER DESC, $QRSCORE_LAST"

        if (zeroOnly == true) {
            sql = sql + " and score = 0 "
        }


        if (category == "REMARK") {
            sql =
                sql + " and REMARK = '$search_string' order by LASTNAME"
        } else if (category == "SEARCHLETTER" && search_string == "A-C") {
            sql =
                sql + "and (LASTNAME LIKE 'A%' OR LASTNAME LIKE 'B%' OR LASTNAME LIKE 'C%' ) order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "D-J") {
            sql =
                sql + "and (LASTNAME LIKE 'F%' OR LASTNAME LIKE 'G%' OR LASTNAME LIKE 'H%' OR LASTNAME LIKE 'I%'  OR LASTNAME LIKE 'J%' OR LASTNAME LIKE 'D%'  OR LASTNAME LIKE 'E%') order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "K-O") {
            sql =
                sql + "and (LASTNAME LIKE 'K%' OR LASTNAME LIKE 'L%' OR LASTNAME LIKE 'M%' OR LASTNAME LIKE 'N%'  OR LASTNAME LIKE 'O%') order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "P-R") {
            sql =
                sql + "and (LASTNAME LIKE 'P%' OR LASTNAME LIKE 'Q%' OR LASTNAME LIKE 'R%' ) order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "S-Z") {
            sql =
                sql + "and (LASTNAME LIKE 'U%' OR LASTNAME LIKE 'V%' OR LASTNAME LIKE 'W%' OR LASTNAME LIKE 'X%'  OR LASTNAME LIKE 'Y%' OR LASTNAME LIKE 'Z%' OR LASTNAME LIKE 'S%'  OR LASTNAME LIKE 'T%') order by lastname"
        } else if (category == "SEARCHLETTER" && search_string == "ALL") {
            sql = sql + " order by lastname"
        } else if (category == "STATUS" && search_string == "ALL") {
            sql = sql + " ORDER BY LASTNAME ASC"
        } else if (category == "STATUS") {
            sql = sql + " AND SubmissionStatus  ='$search_string' "
            sql = sql + " ORDER BY LASTNAME ASC"
        } else if (category == "GROUP") {
            sql = sql + " AND $QRSCORE_GROUP='$search_string' $sorted"
        } else if (category == "NAME") {
            sql = sql + " AND $QRSCORE_LAST like'$search_string%' $sorted"
        } else if (category == "SORT" && search_string == "FIRSTNAME") {
            sql = sql + " order by $TBSTUDENT_FIRST "
        } else if (category == "SORT" && search_string == "LASTNAME") {
            sql = sql + " order by $TBSTUDENT_LAST, $TBSTUDENT_FIRST "
        } else if (category == "SORT" && search_string == "GROUP") {
            sql = sql + " order by GrpNumber, LastName "
        } else if (category == "SORT" && search_string == "SUBMIT") {
            sql = sql + " order by $QRSCORE_SUBMISSION , $TBSTUDENT_FIRST "
        } else if (category == "SORT" && search_string == "GENDER") {
            sql = sql + sorted
        } else if (category == "SORT") {
            sql = sql + " order by $TBSCORE_SCORE $search_string, $TBSTUDENT_LAST "
        } else {
            sql = sql + sorted
        }

        Log.e("SQL2057", sql)
        Log.e("SQL2057", "$category $search_string")

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        Log.e("YYY", cursor.count.toString())
        var completeName = ""
        if (cursor.moveToFirst()) {
            do { //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var studentNo = cursor.getString(cursor.getColumnIndex(QRSCORE_STUDENTNO))
                var firstName = cursor.getString(cursor.getColumnIndex(QRSCORE_FIRST))
                var lastName = cursor.getString(cursor.getColumnIndex(QRSCORE_LAST))
                var score = cursor.getString(cursor.getColumnIndex(QRSCORE_SCORE)).toInt()
                var adjustedScore = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
                var remark = cursor.getString(cursor.getColumnIndex(QRSCORE_REMARK))
                var submissionStatus = cursor.getString(cursor.getColumnIndex(QRSCORE_SUBMISSION))
                var grp = cursor.getString(cursor.getColumnIndex("GrpNumber"))
                var desc = cursor.getString(cursor.getColumnIndex("Description"))

                if (search_string == "FIRSTNAME") completeName = firstName + "," + lastName
                else completeName = lastName + "," + firstName
                Log.e("222", completeName + "  " + submissionStatus)
                val att =

                    ScoreModel(activityCode, sectioncode, firstName, lastName, studentNo, completeName, score, remark, "CLOSED", "OFF", submissionStatus, adjustedScore, grp, desc)
                scoreList.add(att)
            } while (cursor.moveToNext())
        }

        Log.e("1222", "DONE")
        return scoreList
    }

    fun GetSubjectTask(section: String): ArrayList<TaskInfoModel> {
        val taskInfo: ArrayList<TaskInfoModel> = ArrayList<TaskInfoModel>()
        var sql: String
        sql = """   SELECT * FROM tbTaskQuery
                    WHERE sectionName='$section' 
                     """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var completeName = ""
        if (cursor.moveToFirst()) {
            do { //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var taskCode = cursor.getString(cursor.getColumnIndex("TaskCode"))
                var taskTitle = cursor.getString(cursor.getColumnIndex("TaskTitle"))
                var subject = cursor.getString(cursor.getColumnIndex("Subject"))
                var sectionName = cursor.getString(cursor.getColumnIndex("SectionName"))
                val att = TaskInfoModel(taskCode, taskTitle, subject, sectionName)
                taskInfo.add(att)
            } while (cursor.moveToNext())
        }
        return taskInfo
    }

    fun GetActivityTaskCode(section: String, activityCode: String): String {

        var sql: String
        sql = """   SELECT * FROM tbActivity
                    WHERE ActivityCode='$activityCode' 
                    AND  SectionCode='$section' 
                     """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)


        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("TaskCodeNew"))
        }
        return ""
    }

    fun UpdateActivityTaskCode(taskcode: String, activityCode: String, sectionCode: String) {
        var sql: String
        sql = """   UPDATE tbActivity
                    SET TaskCodeNew='$taskcode' 
                    WHERE ActivityCode='$activityCode' 
                    AND  SectionCode='$sectionCode' 
                     """
        Log.e("XXX", taskcode)
        Log.e("SQKLAA", "$sql    $taskcode $activityCode    $sectionCode  ")
        val db2 = this.writableDatabase
        db2.execSQL(sql);
    }

    fun GetTaskAnswer(taskCode: String): ArrayList<TaskModel> {
        val taskList: ArrayList<TaskModel> = ArrayList<TaskModel>()
        var sql: String
        sql = "select * from tbTaskAnswer where taskCode = '$taskCode' order by ItemCode "


        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        Log.e("YYY", cursor.count.toString())
        if (cursor.moveToFirst()) {
            do { //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var taskCode = cursor.getString(cursor.getColumnIndex("TaskCode"))
                var iemCode = cursor.getString(cursor.getColumnIndex("ItemCode"))
                var answer = cursor.getString(cursor.getColumnIndex("Answer"))
                var points = cursor.getString(cursor.getColumnIndex("Points"))

                val att = TaskModel(taskCode, iemCode, answer, points)
                taskList.add(att)
            } while (cursor.moveToNext())
        }
        return taskList
    }


    fun GetTaskScoreRecord(taskCode: String, studentNumber: String): ArrayList<TaskScoreModel> {
        val taskList: ArrayList<TaskScoreModel> = ArrayList<TaskScoreModel>()
        var sql: String
        sql = """ 
            select * from tbTaskScoreQuery 
            where taskCode = '$taskCode'
            and StudentNumber = '$studentNumber'
        """.trimIndent()


        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        Log.e("YYY", cursor.count.toString())
        if (cursor.moveToFirst()) {
            do { //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var taskCode = cursor.getString(cursor.getColumnIndex("TaskCode"))
                var iemCode = cursor.getString(cursor.getColumnIndex("ItemCode"))
                var studentNumber = cursor.getString(cursor.getColumnIndex("StudentNumber"))
                var answer = cursor.getString(cursor.getColumnIndex("Answer"))
                var points = cursor.getString(cursor.getColumnIndex("Points")).toInt()
                var score = cursor.getString(cursor.getColumnIndex("Score")).toInt()
                val att = TaskScoreModel(taskCode, iemCode, studentNumber, score, answer, points)
                taskList.add(att)
            } while (cursor.moveToNext())
        }
        return taskList
    }

    fun GetTaskRecordList(taskCode: String, section: String, activityCode: String): ArrayList<TaskRecordModel> {
        val taskList: ArrayList<TaskRecordModel> = ArrayList<TaskRecordModel>()
        var sql: String
        sql = """ 
            select * from tbTaskStudentsQuery 
            where taskCode = '$taskCode'
            and section  = '$section'
        """.trimIndent()


        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        Log.e("YYY", cursor.count.toString())
        if (cursor.moveToFirst()) {
            do { //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var taskCode = cursor.getString(cursor.getColumnIndex("TaskCode"))
                var studentNumber = cursor.getString(cursor.getColumnIndex("StudentNumber"))
                var section = cursor.getString(cursor.getColumnIndex("Section"))
                var firstName = cursor.getString(cursor.getColumnIndex("FirstName"))
                var lastName = cursor.getString(cursor.getColumnIndex("LastName"))
                var score = GetTotalTaskScore(taskCode, studentNumber)
                var activitityScore = GetActivityScore(section, studentNumber, activityCode)
                val att =
                    TaskRecordModel(taskCode, studentNumber, firstName, lastName, section, score, activitityScore, activityCode)
                taskList.add(att)
            } while (cursor.moveToNext())
        }
        return taskList
    }


    fun GetLogList(): ArrayList<LogModel> {
        val logList: ArrayList<LogModel> = ArrayList<LogModel>()
        var sql: String
        sql = """   SELECT * FROM TBLOG 
                    ORDER BY LOGID DESC  """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do { //  LogID	DateTime	Description
                //var activityCode  = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CODE))
                var logID = cursor.getString(cursor.getColumnIndex("LogID"))
                var dateTime = cursor.getString(cursor.getColumnIndex("DateTime"))
                var description = cursor.getString(cursor.getColumnIndex("Description"))


                var logData = LogModel(logID, dateTime, description)
                logList.add(logData)
            } while (cursor.moveToNext())
        }
        return logList
    }

    fun UpdatePdfFiles(section: String, activityCode: String, path: String?) {
        val sql = """ UPDATE tbactivity
                    SET  PdfFilePath='$path'
                    WHERE activityCode='$activityCode'
                    AND SectionCode  ='$section' 
"""
        Log.e("", sql)
        val db = this.writableDatabase
        db.execSQL(sql)

    }

    fun GetPDFPath(section: String, activityCode: String): String {
        val sql = """ select * from tbactivity
                    WHERE activityCode='$activityCode'
                    AND SectionCode  ='$section'  """
        Log.e("PDFSQL", sql)
        var cursor: Cursor? = null
        val db = this.readableDatabase
        cursor = db.rawQuery(sql, null)
        Log.e("PDFSQL", cursor.count.toString())
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("PdfFilePath"))
        } else return ""
    }


    fun UpdateStudentRecord(sectionCode: String, activityCode: String, studentNo: String, score: Int, remark: String, submissionStatus: String, adjusted: Int = 0) {
        var sql = ""
        if (score > 0) {
            sql = """ UPDATE TBSCORE
                    SET  $TBSCORE_SCORE='$score'
                    , $TBSCORE_REMARK='OK'
                    , $TBSCORE_SUBMISSION='$submissionStatus'
                     , AdjustedScore='$adjusted'
                    WHERE $TBSCORE_SECTION='$sectionCode'
                    AND $TBSCORE_CODE='$activityCode' 
                    AND $TBSCORE_STUDENTNO='$studentNo'  """
        }
        else{
            sql = """ UPDATE TBSCORE
                    SET  $TBSCORE_SCORE='$score'
                    , $TBSCORE_SUBMISSION='$submissionStatus'
                     , AdjustedScore='$adjusted'
                    WHERE $TBSCORE_SECTION='$sectionCode'
                    AND $TBSCORE_CODE='$activityCode' 
                    AND $TBSCORE_STUDENTNO='$studentNo'  """
        }
        Log.e("56178", sql)
        val db = this.writableDatabase
        db.execSQL(sql)

        sql = """select * from  tbactivity
                    WHERE SectionCode='$activityCode'
                    AND ActivityCode='$sectionCode' 
                   
                    """
        val db2 = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db2.rawQuery(sql, null)
        var actTitle = ""
        if (cursor.moveToFirst()) {
            actTitle = cursor.getString(cursor.getColumnIndex("Description"))
        }
        IncremeentalBackUp(sectionCode, activityCode, studentNo, score, remark, actTitle, adjusted)
        WriteScoreCSV(sectionCode, activityCode, actTitle);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun IncremeentalBackUp(activityCode: String, sectionCode: String, studentNo: String, score: Int, remark: String, actTitle: String, adjusted: Int = 0) {
        val folderDest = "/storage/emulated/0/ANDROID BACKUP/" + sectionCode
        val fileDest = File(folderDest)
        if (!fileDest.exists()) fileDest.mkdir()
        val filename = activityCode + "-" + actTitle + "-INC.csv"

        val heading = "SN, CompleteName,Score,Adj,Remark, Day, Timw\n"
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
        val str = "$studentNo, $name, $score, $adjusted, $remark,$date1, $time\n"
        fstream.write(str.toByteArray())
        fstream.close()

    }


    fun WriteScoreCSV(activityCode: String, sectionCode: String, actTitle: String) {
        try {
            val folderSource = "/storage/emulated/0/DriveSyncFiles/ANDROID BACKUP/" + sectionCode

            val file = File(folderSource)
            if (!file.exists()) file.mkdir()

            val filename = activityCode + "-" + actTitle + ".csv"


            val heading = "SN, CompleteName,Score,Adj,Remark\n"
            val myFile = File(folderSource, filename)
            val fstream = FileOutputStream(myFile)
            fstream.write(heading.toByteArray())
            Log.e("235", folderSource + "   " + filename)


            var sql = """select * from  tbscore_query
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode'
                    ORDER BY lastname
                    """
            val db = this.readableDatabase
            Log.e("678", actTitle + sql)
            var cursor1: Cursor? = null
            cursor1 = db.rawQuery(sql, null)
            val columnNames: Array<String> = cursor1.getColumnNames()
            Log.e("678", columnNames.joinToString())
            if (cursor1.moveToFirst()) {
                do {
                    var str = cursor1.getString(cursor1.getColumnIndex("StudentNo")) + ","
                    str = str + cursor1.getString(cursor1.getColumnIndex("LastName")) + " "
                    str = str + cursor1.getString(cursor1.getColumnIndex("FirstName")) + ","
                    str = str + cursor1.getString(cursor1.getColumnIndex("Score")) + ","
                    str = str + cursor1.getString(cursor1.getColumnIndex("AdjustedScore")) + ","
                    str = str + cursor1.getString(cursor1.getColumnIndex("Remark")) + "\n"
                    fstream.write(str.toByteArray())
                    Log.e("678", str)
                } while (cursor1.moveToNext())
            }
            fstream.close()
        } catch (e: Exception) {
            Log.e("err", e.toString())
        }
    }

    fun GetStudentScore(sectionCode: String, activityCode: String, studentNo: String, stat: String = ""): ArrayList<ScoreModel> {
        val scoreList: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
        val db = this.readableDatabase
        var sql = """select * from  TBSCORE_QUERY
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND StudentNo='$studentNo' 
                    """
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            var studentNo = cursor.getString(cursor.getColumnIndex(QRSCORE_STUDENTNO))
            var firstName = cursor.getString(cursor.getColumnIndex(QRSCORE_FIRST))
            var lastName = cursor.getString(cursor.getColumnIndex(QRSCORE_LAST))
            var score = cursor.getString(cursor.getColumnIndex(QRSCORE_SCORE)).toInt()
            var remark = cursor.getString(cursor.getColumnIndex(QRSCORE_REMARK))
            var submissionStatus = cursor.getString(cursor.getColumnIndex(QRSCORE_SUBMISSION))
            var adjustedScore = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
            var grp = cursor.getString(cursor.getColumnIndex("GrpNumber"))
            var desc = cursor.getString(cursor.getColumnIndex("Description"))

            val att =
                ScoreModel(activityCode, sectionCode, firstName, lastName, studentNo, "", score, remark, "CLOSED", "OFF", submissionStatus, adjustedScore, grp, desc)
            scoreList.add(att)
        }
        return scoreList
    }

    fun GetNoSubmission(sectionCode: String, activityCode: String): String {
        val scoreList: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
        val db = this.readableDatabase
        var sql = """select * from  TBSCORE_QUERY
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND SubmissionStatus='NO' 
                    """
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var studentData = ""
        if (cursor.moveToFirst()) {
            do {
                var firstName = cursor.getString(cursor.getColumnIndex(QRSCORE_FIRST))
                var lastName = cursor.getString(cursor.getColumnIndex(QRSCORE_LAST))
                studentData = studentData + lastName + " " + firstName.substring(0, 1) + "\n"
            } while (cursor.moveToNext())
        } else {
            studentData = "NONE"
        }
        return studentData
    }

    fun GetDefaultGradingPeriod(): String {
        val db2 = this.readableDatabase
        var sql = "select * from  tbinfo"

        var cursor: Cursor? = null
        cursor = db2.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("CurrentQuarter"))
        }
        return "";
        db2.close()
    }

    fun SetDefaultGradingPeriod(quarter: String) {
        val db = this.readableDatabase
        val sql = "update  tbinfo set  CurrentQuarter='$quarter'"
        db.execSQL(sql)
        db.close()

    }

    fun SetAverageStatus(avg: String = "TRUE") {
        val db = this.readableDatabase
        val sql = "update  tbinfo set   AverageStatus='$avg'"
        db.execSQL(sql)
        db.close()
    }


    fun GetAverageStatus(): String {
        val db = this.readableDatabase
        var sql = "select * from  tbinfo"

        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("AverageStatus"))
        }
        return "";
        db.close()
    }


    fun GetIndividualNoSubmission(sectionCode: String, studentNo: String, completeName: String): String {
        val scoreList: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
        val db = this.readableDatabase
        val gradingPeriod = GetDefaultGradingPeriod();
        var sql = """select * from  TBSCORE_QUERY
                    WHERE SectionCode='$sectionCode'
                    AND StudentNo='$studentNo' 
                    AND SubmissionStatus='NO' 
                    AND GradingPeriod='$gradingPeriod' 
                    """
        Log.e("###", sectionCode + "" + studentNo + " " + completeName)
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var studentData = ""
        Log.e("###", cursor.count.toString())
        if (cursor.moveToFirst()) {
            studentData = completeName + "(" + cursor.count + ")" + "\n"
            do {
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var description = GetActivityDescription(sectionCode, activityCode)
                studentData = studentData + description + "\n"
            } while (cursor.moveToNext())
        } else {

        }
        return studentData
    }

    fun GetIndividualNoSubmissionCount(sectionCode: String, studentNo: String): Int {
        val scoreList: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
        val db = this.readableDatabase
        var sql = """select * from  TBSCORE_QUERY
                    WHERE SectionCode='$sectionCode'
                    AND StudentNo='$studentNo' 
                    AND SubmissionStatus='NO' 
                    """

        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var studentData = ""
        return cursor.count
    }

    fun GetActivityDescription(sectionCode: String, activityCode: String): String {
        val db = this.readableDatabase
        var sql = """select * from  TBACTIVITY
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode ='$activityCode' 
                    """

        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var studentData = ""
        var activity = ""
        if (cursor.moveToFirst()) {
            activity = cursor.getString(cursor.getColumnIndex("Description"))

        }
        return activity
    }


    fun GetPagePdf(taskcode: String, studentNumber: String, section: String): Int {
        val db2 = this.writableDatabase
        var sql = """select * from tbTaskStudents where TaskCode='$taskcode' 
                     and StudentNumber='$studentNumber'; 
                     where  section ='$section'; 
                    """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("Page")).toInt()
        } else return 1

    }


    fun AdddNewTaskAnswer(taskcode: String, itemCode: String, answer: String, point: Int) {
        val db2 = this.writableDatabase
        var sql = "INSERT INTO tbTaskAnswer VALUES('$taskcode','$itemCode','$answer',$point);"
        db2.execSQL(sql);
    }


    fun AdddNewTask(taskcode: String, title: String, subject: String) {
        val db2 = this.writableDatabase
        var sql = "INSERT INTO tbTask VALUES('$taskcode','$title','$subject');"
        db2.execSQL(sql);
    }


    fun AddNewTaskStudent(taskcode: String, studentNumber: String, section: String, page: Int) {
        val db2 = this.writableDatabase
        var sql = """select * from tbTaskStudents where TaskCode='$taskcode' and
                           StudentNumber='$studentNumber'; 
                    """
        Log.e("ADD", "$sql  $section")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("ADD100", cursor.count.toString())

        if (cursor.count == 0) {
            Log.e("ADD200", "$sql 000 $section")
            sql =
                "INSERT INTO tbTaskStudents VALUES('$taskcode','$studentNumber','$section',0, $page);"
            db2.execSQL(sql);

            sql =
                "insert into tbTaskScore select TaskCode, ItemCode, '$studentNumber', -1 from tbTaskAnswer where TaskCode='$taskcode'"
            db2.execSQL(sql);
        }
    }

    //        fun AddNewTaskStudent(taskcode:String, studentNumber:String, section:String, page:Int){
    //       val db2 = this.writableDatabase
    //             var sql = """select * from tbTaskStudents where TaskCode='$taskcode' and
    //                           StudentNumber='$studentNumber';
    //                    """
    //       Log.e("ADD", "$sql  $section")
    //       val db = this.readableDatabase
    //       var cursor: Cursor? = null
    //       cursor = db.rawQuery(sql, null)
    //       Log.e("ADD100", cursor.count.toString())
    //
    //       if (cursor.count ==0){
    //           Log.e("ADD200", "$sql 000 $section")
    //            sql ="INSERT INTO tbTaskStudents VALUES('$taskcode','$studentNumber','$section',0, $page);"
    //           db2.execSQL(sql);
    //
    //           sql ="insert into tbTaskScore select TaskCode, ItemCode, '$studentNumber', -1 from tbTaskAnswer"
    //           db2.execSQL(sql);
    //       }
    //       else{
    //          sql ="""
    //              insert into tbTaskScore select TaskCode, ItemCode, '$studentNumber', 0
    //              from tbTaskAnswer where TaskCode='$taskcode' and ItemCode Not In
    //              (select ItemCode from  tbTaskScore where StudentNumber='$studentNumber'
    //              and TaskCode='$taskcode');
    //          """.trimIndent()
    //           db2.execSQL(sql);
    //           Log.e("BBB", sql)
    //           sql ="""
    //           delete from tbTaskScore where TaskCode = '$taskcode' and
    //           StudentNumber='$studentNumber' and ItemCode not in
    //           (select ItemCode from tbTaskAnswer where TaskCode = '$taskcode');
    //          """.trimIndent()
    //           db2.execSQL(sql);
    //           Log.e("BBB", sql)
    //
    //       }
    //   }

    fun CheckTaskRecord(taskcode: String, studentNumber: String): String {
        val db2 = this.writableDatabase
        var sql = """select * from tbTaskStudents where TaskCode='$taskcode' and
                           StudentNumber='$studentNumber'; 
                    """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            return "FOUND"
        } else {
            return "NOT FOUND"
        }

    }

    fun SetTaskScore(taskcode: String, studentNumber: String, itemCode: String, score: Int) {
        val db2 = this.writableDatabase
        var sql = """
            update tbTaskScore set score = $score where StudentNumber='$studentNumber' 
             and TaskCode='$taskcode' and ItemCode='$itemCode';
                    """
        Log.e("AAA", sql)
        db2.execSQL(sql);
    }

    fun GetTaskScore(taskcode: String, studentNumber: String, itemCode: String): Int {
        val db2 = this.readableDatabase
        var sql = """
            select * from  tbTaskScore where StudentNumber='$studentNumber' 
            and TaskCode='$taskcode' and ItemCode='$itemCode'; 
                    """

        var cursor: Cursor? = null
        cursor = db2.rawQuery(sql, null)


        return 0;
    }

    fun GetTotalTaskScore(taskcode: String, studentNumber: String): Int {
        val db2 = this.readableDatabase
        var sql = """
            select sum(Score) as  total  from  tbTaskScore where StudentNumber='$studentNumber' 
            and TaskCode='$taskcode' and Score >=0
                    """
        Log.e("SUM", sql)
        if (CheckTaskRecord(taskcode, studentNumber) == "NOT FOUND") return 0;

        var cursor: Cursor? = null
        cursor =
            db2.rawQuery(sql, null) //        var p =  cursor.getString(cursor.getColumnIndex("total"))
        //        if(p == null)  {
        //            return 0;
        //        }


        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst()
            val total = cursor.getInt(0)
            return total

        } else {
            return 0
        }
    }

    fun GetActivityScore(section: String, studentNumber: String, activityCode: String): Int {
        val db2 = this.readableDatabase
        var sql = """
            select *from  tbscore 
              where StudentNo='$studentNumber' 
            and ActivityCode ='$activityCode'
            and SectionCode  ='$section'
                    """


        var cursor: Cursor? = null
        cursor = db2.rawQuery(sql, null)


        if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex("Score")).toInt()
        else return 0;
    }


    fun GetActivityDetail(sectionCode: String, activityCode: String, field: String): String {
        var sql = """select * from  TBACTIVITY
                    WHERE $TBACTIVITY_SECTION='$sectionCode'
                    AND $TBACTIVITY_CODE='$activityCode' """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        Log.e("111", sql)
        Log.e("111", cursor.count.toString())
        Log.e("111", cursor.getColumnName(0))

        if (field == "ITEM") {
            var item = cursor.getString(cursor.getColumnIndex(TBACTIVITY_ITEM))
            return item
        } else if (field == "DESCRIPTION") {
            var desc = cursor.getString(cursor.getColumnIndex(TBACTIVITY_DESCRIPTION))
            return desc
        } else if (field == "GRADINGPERIOD") {
            var status = cursor.getString(cursor.getColumnIndex(TBACTIVITY_GRADINGPERIOD))
            return status
        } else if (field == "CATEGORY") {
            var status = cursor.getString(cursor.getColumnIndex(TBACTIVITY_CATEGORY))
            return status
        } else {
            return ""
        }
    }

    fun GetIndividualActivity(sectionCode: String, studentNo: String, type: String = "CODE"): ArrayList<ScoreIndividualModel> {
        val individualList: ArrayList<ScoreIndividualModel> = ArrayList<ScoreIndividualModel>()
        val currentGradingPeriod = GetDefaultGradingPeriod()
        var sql = """   SELECT * FROM TBSCORE_QUERY 
                        WHERE $QRSCORE_SECTION='$sectionCode' 
                        AND $QRSCORE_STUDENTNO='$studentNo'
                        AND GradingPeriod='$currentGradingPeriod'
       """
        if (type == "MISSING") {
            sql = sql + "  AND SubmissionStatus='NO'"
            sql = sql + "  ORDER BY $QRSCORE_CODE "
        } else if (type == "CODE") sql = sql + "  ORDER BY $QRSCORE_CODE "
        else sql = sql + "  ORDER BY CATEGORY DESC"


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("Helo1233", sql)
        if (cursor.moveToFirst()) {
            do {
                Log.e("Helo", "ssss")
                var activityCode = cursor.getString(cursor.getColumnIndex(QRSCORE_CODE))
                var description = GetActivityDetail(sectionCode, activityCode, "DESCRIPTION")
                var category = GetActivityDetail(sectionCode, activityCode, "CATEGORY")
                var gradingPeriod = GetActivityDetail(sectionCode, activityCode, "GRADINGPERIOD")
                var item = GetActivityDetail(sectionCode, activityCode, "ITEM").toInt()

                var score = cursor.getString(cursor.getColumnIndex(QRSCORE_SCORE)).toInt()
                var adjustedScore = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
                var remark = cursor.getString(cursor.getColumnIndex(QRSCORE_REMARK))
                var submissionStatus = cursor.getString(cursor.getColumnIndex("SubmissionStatus"))

                val att =
                    ScoreIndividualModel(activityCode, sectionCode, studentNo, description, score, remark, "CLOSED", item, gradingPeriod, category, adjustedScore, submissionStatus)
                individualList.add(att)

            } while (cursor.moveToNext())
        }
        return individualList
    }


    fun GetActivityRecord(sectionCode: String, activityCode: String, search_string: String = "ALL", scoreStatus:String ="ZERO"): ArrayList<ScoreIndividualModel> {
        val individualList: ArrayList<ScoreIndividualModel> = ArrayList<ScoreIndividualModel>()
        val currentGradingPeriod = GetDefaultGradingPeriod()
        var sql = """   SELECT * FROM TBSCORE_QUERY 
                        WHERE sectionCode='$sectionCode' 
                        AND ActivityCode='$activityCode'
                        AND GradingPeriod='$currentGradingPeriod'
      """

        if (scoreStatus== "ZERO")
         sql = sql + " AND score =0 "

        if (search_string == "A-C") {
            sql =
                sql + "and (LASTNAME LIKE 'A%' OR LASTNAME LIKE 'B%' OR LASTNAME LIKE 'C%' ) order by lastname"
        } else if (search_string == "D-J") {
            sql =
                sql + "and (LASTNAME LIKE 'F%' OR LASTNAME LIKE 'G%' OR LASTNAME LIKE 'H%' OR LASTNAME LIKE 'I%'  OR LASTNAME LIKE 'J%' OR LASTNAME LIKE 'D%'  OR LASTNAME LIKE 'E%') order by lastname"
        } else if (search_string == "K-O") {
            sql =
                sql + "and (LASTNAME LIKE 'K%' OR LASTNAME LIKE 'L%' OR LASTNAME LIKE 'M%' OR LASTNAME LIKE 'N%'  OR LASTNAME LIKE 'O%') order by lastname"
        } else if (search_string == "P-R") {
            sql =
                sql + "and (LASTNAME LIKE 'P%' OR LASTNAME LIKE 'Q%' OR LASTNAME LIKE 'R%' ) order by lastname"
        } else if (search_string == "S-Z") {
            sql =
                sql + "and (LASTNAME LIKE 'U%' OR LASTNAME LIKE 'V%' OR LASTNAME LIKE 'W%' OR LASTNAME LIKE 'X%'  OR LASTNAME LIKE 'Y%' OR LASTNAME LIKE 'Z%' OR LASTNAME LIKE 'S%'  OR LASTNAME LIKE 'T%') order by lastname"
        } else {
            sql = sql + " order by lastname"

        }


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("Helo", sql)
        if (cursor.moveToFirst()) {
            do {

                //                GrpNumber	EnrollmentStatus	FirstName	LastName	Gender	ActivityCode	SectionCode	StudentNo	Score	Remark
                //                        SubmissionStatus	AdjustedScore	GradingPeriod	Description	Item	Category
                Log.e("Helo", "ssss")
                var activityCode = cursor.getString(cursor.getColumnIndex(QRSCORE_CODE))
                var studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))
                var description = GetActivityDetail(sectionCode, activityCode, "DESCRIPTION")
                var category = GetActivityDetail(sectionCode, activityCode, "CATEGORY")
                var gradingPeriod = GetActivityDetail(sectionCode, activityCode, "GRADINGPERIOD")
                var item = GetActivityDetail(sectionCode, activityCode, "ITEM").toInt()

                var score = cursor.getString(cursor.getColumnIndex(QRSCORE_SCORE)).toInt()
                var adjustedScore = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
                var remark = cursor.getString(cursor.getColumnIndex(QRSCORE_REMARK))
                var submissionStatus = cursor.getString(cursor.getColumnIndex("SubmissionStatus"))
                var lastName = cursor.getString(cursor.getColumnIndex("LastName"))

                val att =
                    ScoreIndividualModel(activityCode, sectionCode, studentNo, description, score, remark, "CLOSED", item, gradingPeriod, category, adjustedScore, submissionStatus, lastName)
                individualList.add(att)

            } while (cursor.moveToNext())
        }
        return individualList
    }


    fun GetNewLogID(): String {
        val sql: String
        sql = """
                SELECT * FROM TBLOG
                ORDER BY LogID DESC
                """


        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            var actCode = cursor.getString(cursor.getColumnIndex("LogID"))
            var num = actCode.takeLast(4).toInt() + 1 // Grade>>>
            Util.Msgbox(context, num.toString())
            return Util.ZeroPad(num, 4)
        } else {
            return "0001"
        } // LogID	DateTime	Description
        //  return  "helo"


    }

    fun GetStudentName(studentNumber: String): String {


        var sql = """   SELECT * FROM TBSTUDENT 
                        WHERE StudentNo='$studentNumber'  """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var firstname = ""
        var lastname = ""
        if (cursor.moveToFirst()) {
            firstname = cursor.getString(cursor.getColumnIndex("FirstName"))
            lastname = cursor.getString(cursor.getColumnIndex("LastName"))

        }

        return lastname + "," + firstname.substring(0, 1) //StudentNo	FirstName	LastName	GrpNumber	Section	Gender

    }

    fun SaveLog(studentNumber: String, sectioncode: String, activityCode: String, oldScore: Int, newScore: Int) {
        var studentName = GetStudentName(studentNumber);
        var sectionCode = sectioncode
        var actDescription = activityCode
        var logID = GetNewLogID();
        var dateTime = "JAN 15"
        var desc =
            "Update " + studentName + "( " + sectionCode + ") -" + actDescription + " from  " + oldScore + " to " + newScore
        SaveLogDatabase(logID, dateTime, desc);
    }


}

