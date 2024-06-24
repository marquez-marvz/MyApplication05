package com.example.myapplication05

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class TableRandom(context: Context) : DatabaseHandler(context) {

    companion object {
        private val TBRANDOM_SEQUENCE = "SequenceNumber"
        private val TBRANDOM_STUDENTNO = "StudentNo"
        private val TBRANDOM_SECTION = "SectionCode"
        private val TBRANDOM_REMARK = "Remark"
        private val TBRANDOM_CODE = "RandomCode"

        private val QRRANDOM_SEQUENCE = "SequenceNumber"
        private val QRRANDOM_STUDENTNO = "StudentNo"
        private val QRRANDOM_SECTION = "SectionCode"
        private val QRRANDOM_REMARK = "Remark"
        private val QRRANDOM_FIRST = "FirstName"
        private val QRRANDOM_LAST = "LastName"
        private val QRRANDOM_CODE = "RandomCode"

        private val TBRECITATION_CODE = "RandomCode"
        private val TBRECITATION_STUDENTNO = "StudentNo"
        private val TBRECITATION_DATE = "RecitationDate"
        private val TBRECITATION_POINTS = "Points"
        private val TBRECITATION_SECTION = "SectionCode"

        val TBMISC_CODE = "MiscCode"
        val TBMISC_DESC = "MiscDescription"
        val TBMISC_OPTIONCODE = "OptionNumber"
        val TBMISC_OPTIONDESC = "OptionDescription"
        val TBMISC_SECTIONCODE = "SectionCode"

        val QRMISC_STUDENTNO = "StudentNo"
        val QRMISC_FIRST = "FirstName"
        val QRMISC_LAST = "LastName"
        val QRMISC_GRP = "GrpNumber"
        val QRMISC_GENDER = "MiscCode"
        val QRMISC_SECTIONCODE = "SectionCode"
        val QRMISC_OPTION_NUMBER = "OptionNumber"
        val QRMISC_OPTION_DESC= "OptionDescription"
        val QRMISC_DESC= "MiscDescription"
        val QRMISC_CODE= "MiscCode"






    }

    fun CountSectionRandom(section: String): Int {
        var sql = """   SELECT * FROM TBRANDOM 
                        WHERE $QRRANDOM_SECTION='$section'  """

        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        Log.e("CTR", sql + "  " + cursor.count.toString())
        return cursor.count
        cursor.close()
    }

    fun ManageRandom(sequenceno: String, section: String, studentno: String, remark: String, randomCode: String): Boolean {
        val contentValues = ContentValues()
        val db = this.writableDatabase
        contentValues.put(TBRANDOM_SEQUENCE, sequenceno)
        contentValues.put(TBRANDOM_STUDENTNO, studentno)
        contentValues.put(TBRANDOM_SECTION, section)
        contentValues.put(TBRANDOM_REMARK, remark)
        contentValues.put(TBRANDOM_CODE, randomCode)

        val success = db.insert("tbrandom", null, contentValues)
        Log.e("RND", contentValues.toString() + "   " + success)
        if (success < 0) return false
        else return true
        db.close()
    }


    fun GetRandomList(sectioncode: String, search_string:String=""): List<RandomModel> {

        val randomList: ArrayList<RandomModel> = ArrayList<RandomModel>()
        var sql: String = """    SELECT  * FROM TBRANDOM_QUERY 
                                where $QRRANDOM_SECTION ='$sectioncode'
                                and $QRRANDOM_LAST like '$search_string%'
                                order by $TBRANDOM_SEQUENCE """

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

                var sequenceNumber = GetFiedValue(cursor, QRRANDOM_SEQUENCE)
                var sectioncode = GetFiedValue(cursor, QRRANDOM_SECTION)
                var studentNo = GetFiedValue(cursor, QRRANDOM_STUDENTNO)
                var randomCode = GetFiedValue(cursor, QRRANDOM_CODE)
                var fname = GetFiedValue(cursor, QRRANDOM_FIRST)
                var lname = GetFiedValue(cursor, QRRANDOM_LAST)
                var completeName = lname + "," + fname
                var recitationCount = 0
                var remark = GetFiedValue(cursor, QRRANDOM_REMARK)
                val rnd =
                    RandomModel(sequenceNumber, sectioncode, studentNo, completeName, 0, remark, randomCode)

                randomList.add(rnd)
            } while (cursor.moveToNext())
        }

        return randomList
    }

    fun GetFiedValue(c: Cursor, field: String): String {
        return c.getString(c.getColumnIndex(field)).toString()
    }

    fun DeleteRandom(section: String) {
        val db = this.writableDatabase
        val sql = "delete  from tbrandom where $TBRANDOM_SECTION='$section'"
        db.execSQL(sql)
    }

    fun DeleteSectionRecitation(section: String) {
        val db = this.writableDatabase
        val sql = "delete  from tbrecitation where $TBRECITATION_SECTION='$section'"
        db.execSQL(sql)
    }

    fun UpdateRandomRemark(studentno: String, section: String, randomCode: String, remark: String) {
        var sql = """  update tbrandom 
                       set $TBRANDOM_REMARK = '$remark'
                       where $TBRANDOM_STUDENTNO = '$studentno'
                       and  $TBRANDOM_SECTION= '$section'
                       and $TBRANDOM_CODE = '$randomCode' """
        var db = this.writableDatabase

        try {
            db.execSQL(sql)
        } catch (e: SQLiteException) {
            Log.e("SQLER", sql + " " + e.toString())
        }
    }

    fun DeleteRecitation(studentno: String, randomCode: String) {
        var sql = """  delete from tbrecitation 
                       where $TBRECITATION_CODE = '$randomCode'
                       and  $TBRANDOM_STUDENTNO= '$studentno' """
        var db = this.writableDatabase

        try {
            db.execSQL(sql)
        } catch (e: SQLiteException) {
            Log.e("SQLER", sql + " " + e.toString())
        }
    }

    fun CounRecitation(studentno: String): Int {
        var sql = """  select * from tbrecitation 
                        where  $TBRANDOM_STUDENTNO= '$studentno' """

        var cursor: Cursor? = GetCursor(sql)
        return cursor!!.count
    }


    private fun GetCursor(sql: String): Cursor? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
            return cursor;
        } catch (e: SQLiteException) {
           Log.e("SQLER", sql + "\n" + e.toString())
            return cursor
        }
    }


    fun AddRecitation(section: String, studentno: String, randomCode: String) {
        val contentValues = ContentValues()
        val cal = Calendar.getInstance()
        var myMonth = cal.get(Calendar.MONTH)
        var myDay = Util.ZeroPad(cal.get(Calendar.DAY_OF_MONTH), 2)
        val monthName =
            arrayOf<String>("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

        var mydate = monthName[myMonth] + " " + myDay

        val db = this.writableDatabase
        contentValues.put(TBRECITATION_CODE, randomCode)
        contentValues.put(TBRECITATION_STUDENTNO, studentno)
        contentValues.put(TBRECITATION_DATE, mydate)
        contentValues.put(TBRECITATION_POINTS, 0)
        contentValues.put(TBRECITATION_SECTION, section)
        val success = db.insert("tbrecitation", null, contentValues)
    }

    fun GetMiscCode(section: String):String {
        var sql = """  select * from   tbmisc
                       where $TBMISC_SECTIONCODE = '$section' 
                       order by $TBMISC_CODE DESC"""
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            var actCode = cursor.getString(cursor.getColumnIndex(TBMISC_CODE))
            var num = actCode.takeLast(2).toInt() + 1 // Grade>>>
            Util.Msgbox(context, num.toString())
            return "MISC" + Util.ZeroPad(num, 2)
        } else {
            // Util.Msgbox(context, "ACT-01" )
            return "MISC01"
        }

    }

    fun GetNewOptionNumber(section: String, miscCode:String):String {
        var sql = """  select * from   tbmisc
                       where $TBMISC_SECTIONCODE = '$section' 
                       and $TBMISC_CODE = '$miscCode' 
                       order by $TBMISC_OPTIONCODE  DESC"""
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            var actCode = cursor.getString(cursor.getColumnIndex(TBMISC_OPTIONCODE))
            var num = actCode.takeLast(1).toInt() + 1 // Grade>>>
            Util.Msgbox(context, num.toString())
            return  miscCode + "-" + num
        } else {
            // Util.Msgbox(context, "ACT-01" )
            return miscCode
        }

    }





    fun ManageMisc(crudStatus: String, MiscCode: String, MiscDescription: String = "", OptionNumber: String = "", OptionDescription: String = "0", SectionCode: String = ""): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TBMISC_CODE, MiscCode)
        contentValues.put(TBMISC_DESC, MiscDescription)
        contentValues.put(TBMISC_OPTIONCODE, OptionNumber)
        contentValues.put(TBMISC_OPTIONDESC, OptionDescription)
        contentValues.put(TBMISC_SECTIONCODE, SectionCode)

        var status: Boolean = false;
        val arr = arrayOf (MiscCode,  MiscDescription, OptionNumber, OptionDescription, SectionCode)
        Log.e("MISCX", arr.joinToString(" "))

        if (crudStatus == "ADD") {

            val success = db.insert("tbmisc", null, contentValues)
            Log.e("MISCX", success.toString())
            if (success < 0) status = false
            else status = true

        } else if (crudStatus == "EDIT") {
//            var where = "${TableActivity.TBACTIVITY_CODE}='$activityCode' and ${TableActivity.TBACTIVITY_SECTION}='$sectionCode'"
//            val editstat = db.update("TBACTIVITY", contentValues, where, null)
//            if (editstat < 0) status = false
//            else status = true
//            Log.e("XXX", editstat.toString() + "$actStatus   $where")
        } else if (crudStatus == "DELETE") {
//            var where = "${TableActivity.TBACTIVITY_CODE}='$activityCode' and ${TableActivity.TBACTIVITY_SECTION}='$sectionCode'"
//            var success = db.delete("TBACTIVITY", where, null)
//            db.delete("TBSCORE", where, null)


//            if (success < 0) status = false
//            else status = true
        }
        return true;
        db.close()
    }

    fun EditMiscDescription(miscCode: String, miscDescription: String) {
        var sql = """ update tbmisc
                          set $TBMISC_DESC = '$miscDescription'
                            where $TBMISC_CODE ='$miscCode' """

        SqlCrudExecution(sql)
    }


    fun EditOptionDescription(miscCode: String,  optionCode:String, optionDescription: String) {
        var sql = """     update tbmisc
                          set $TBMISC_OPTIONDESC = '$optionDescription'
                          where $TBMISC_CODE ='$miscCode' 
                           and  $TBMISC_OPTIONCODE ='$optionCode' """

        SqlCrudExecution(sql)
    }


    fun DeleteMisc(miscCode: String) {
        var sql = """   Delete from tbmisc
                        where $TBMISC_CODE ='$miscCode' """
        SqlCrudExecution(sql)
    }


    fun DeleteMiscOption(miscCode: String, optionNumber:String) {
        var sql = """   Delete from tbmisc
                        where $TBMISC_CODE ='$miscCode'
                        and $TBMISC_OPTIONCODE ='$optionNumber' """
        SqlCrudExecution(sql)
    }


    fun SqlCrudExecution(sql:String){
        val db = this.writableDatabase
        try {
            db.execSQL(sql)
            Log.e("SQLER", sql)
        } catch (e: SQLiteException) {
            Log.e("SQLER", sql + " " + e.toString())
        }
    }



    fun GetMiscList(sectioncode: String): List<MiscModel> {

        val miscList: ArrayList<MiscModel> = ArrayList<MiscModel>()
        var sql: String = """   SELECT DISTINCT $TBMISC_SECTIONCODE, $TBMISC_CODE, $TBMISC_DESC FROM TBMISC
                               WHERE $TBMISC_SECTIONCODE='$sectioncode'
                                order by $TBMISC_OPTIONCODE """


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

                var miscCode= GetFiedValue(cursor, TBMISC_CODE)
                var miscDescription = GetFiedValue(cursor, TBMISC_DESC)
                var optionNumber= "-"
                var optionDescription = "-"
                var sectioncode = GetFiedValue(cursor, TBMISC_SECTIONCODE)
                val myMisc = MiscModel(miscCode, miscDescription, optionNumber, optionDescription, sectioncode)

                miscList.add(myMisc)
            } while (cursor.moveToNext())
        }

        return miscList
    }


    fun GetMiscStudentList(sectioncode: String, miscCode:String): List<MiscStudentModel> {

        val miscStudentList: ArrayList<MiscStudentModel> = ArrayList<MiscStudentModel>()
        var sql: String = """   SELECT * FROM  MISC_QUERY 
                              WHERE  $QRMISC_SECTIONCODE='$sectioncode'
                               AND $QRMISC_CODE='$sectioncode'
                                order by $QRMISC_GENDER DESC, QRMISC_LAST """

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

                var studentNo= GetFiedValue(cursor, QRMISC_STUDENTNO)
                var firstName = GetFiedValue(cursor, QRMISC_FIRST)
                var lastName = GetFiedValue(cursor, QRMISC_FIRST)
                var  grpNumber= GetFiedValue(cursor, QRMISC_GRP)
                var gender = GetFiedValue(cursor, QRMISC_GENDER)
                var sectionCode = GetFiedValue(cursor, QRMISC_SECTIONCODE)
                var optionNumber = GetFiedValue(cursor,QRMISC_OPTION_NUMBER )
                var optionDesc = GetFiedValue(cursor,QRMISC_OPTION_DESC )
                var miscCode = GetFiedValue(cursor,QRMISC_CODE )
                var miscDesc = GetFiedValue(cursor,QRMISC_DESC )
                var completeName = lastName + " "  +  firstName

                val myMisc = MiscStudentModel(studentNo,optionNumber,sectionCode,completeName, grpNumber, gender, optionDesc, miscDesc )

                miscStudentList.add(myMisc)
            } while (cursor.moveToNext())
        }

        return miscStudentList
    }


    fun GetOptionList(sectionCode: String, miscCode:String): List<MiscModel> {

        val miscList: ArrayList<MiscModel> = ArrayList<MiscModel>()
        var sql: String = """    SELECT  * FROM TBMISC 
                               WHERE $TBMISC_SECTIONCODE='$sectionCode'
                               AND $TBMISC_CODE='$miscCode'
                                order by $TBMISC_OPTIONCODE """

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

                var miscCode= GetFiedValue(cursor, TBMISC_CODE)
                var miscDescription = GetFiedValue(cursor, TBMISC_DESC)
                var optionNumber= GetFiedValue(cursor, TBMISC_OPTIONCODE)
                var optionDescription = GetFiedValue(cursor, TBMISC_OPTIONDESC)
                var sectioncode = GetFiedValue(cursor, TBMISC_SECTIONCODE)
                val myMisc =
                    MiscModel(miscCode, miscDescription, optionNumber, optionDescription, sectioncode)

                miscList.add(myMisc)
            } while (cursor.moveToNext())
        }

        return miscList
    }

    fun SaveStudentMisc(sectionCode: String, optionNumber: String) {
        val db = this.writableDatabase

      //  CREATE TABLE IF NOT EXISTS $tbname (  StudentNo text,      text,  SectionCode text,
        var sql = """
            INSERT INTO TBMISC_STUDENT (StudentNo, OptionNumber ,SectionCode)
                SELECT studentno, '$optionNumber', '$sectionCode'
            FROM   tbstudent where section = '$sectionCode'
            """
        // Util.Msgbox(context, sql)
        db.execSQL(sql);
    }




}

