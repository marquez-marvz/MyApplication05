package com.example.myapplication05/*
SHORCUT KEY:




   ctrl + - Fold all
 */
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.student_row.view.*
import java.lang.Exception

open class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, "dbstudentnew", null, 132) {

    companion object {
        private val TBINFO_CURRENTSECTION = "CurrentSection"

        //val DATABASE_VERSION = 1
        val TABLE_NAME = "tbstudent"
        val TBSTUDENT_STUDENTNO = "StudentNo"
        val TBSTUDENT_FIRST = "FirstName"
        val TBSTUDENT_LAST = "LastName"
        val TBSTUDENT_GRP = "GrpNumber"
        val TBSTUDENT_SECTION = "Section"
        val TBSTUDENT_GENDER = "Gender"
        val TBSTUDENT_CONTACT = "ContactNumber"

        val TBSECTION_CODE = "SectionCode"
        val TBSECTION_NAME = "SectionName"


    }


    var context: Context? = null

    init {
        this.context = context
    }

    fun StudentInfo(db: SQLiteDatabase?) {
        val sql = """
          CREATE TABLE  IF NOT EXISTS  TBSTUDENT  (
             StudentNo  INTEGER,
             FirstName  text,
             LastName  text,
             GrpNumber  text,
             Section  text,
             Gender  text,
             ContactNumber text, 
            PRIMARY KEY ( StudentNo )
          );
      """.trimIndent()
        db?.execSQL(sql);
    }

    fun SectionTableStructure(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE  IF NOT EXISTS TBSECTION (
              SectionCode text,
              SectionName text
            )
        """

        db?.execSQL(sql);
    }

    fun LogTableStructure(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE  IF NOT EXISTS  tblog  (
               LogID  TEXT,
               DateTime  text,
               Description  text,
              PRIMARY KEY ( LogID )
            );

        """
        db?.execSQL(sql);
    }


    fun TbinfoStructure(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE IF NOT EXISTS  tbinfo (
              CurrentSection text
            );
        """
        db?.execSQL(sql);

    }

    fun TbactivityStructure(db: SQLiteDatabase?) {
        val sql = """
          CREATE TABLE  tbactivity  (
           ActivityCode  TEXT,
           SectionCode  text,
           Description  text,
           Item  INTEGER,
           Status  text,
           Category text,
            PRIfMARY KEY ( ActivityCode ,  SectionCode )
         )

        """
        db?.execSQL(sql);
    }

    fun TbScoreStructure(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE  tbscore  (
               ActivityCode  TEXT,
               SectionCode  text,
               StudentNo  text,
               Score  INTEGER,
               Remark  text,
               SubmissionStatus  text,
              PRIMARY KEY ( ActivityCode ,  SectionCode ,  StudentNo )
        );
        """
        db?.execSQL(sql);
    }

    fun ScoreQuery(db: SQLiteDatabase?) {
        val sql = """

      CREATE VIEW IF NOT EXISTS tbscore_query AS
      SELECT
                tbscore.ActivityCode,
                tbscore.SectionCode,
                tbscore.StudentNo,
                tbscore.Score,
                tbscore.Remark,
                tbscore.SubmissionStatus,
                tbstudent.FirstName,
                tbstudent.LastName,
                tbstudent.GrpNumber,
                tbstudent.Gender
            FROM
                tbscore
                INNER JOIN tbstudent
            ON
                 tbscore.StudentNo = tbstudent.StudentNo            
        """.trimIndent()
        db?.execSQL(sql);
    }


    fun TbinfoRecord(db: SQLiteDatabase?) { //        val sql = """
        //           INSERT INTO tbinfo VALUES ('11-PROG-1, "FIRST");
        //        """
        //        db?.execSQL(sql);
    }

    fun TableSectionRecord(db: SQLiteDatabase?) {
        val sql = """
            INSERT INTO "TBSECTION" VALUES ('SEC-03', '11-PROG-1'),
        ('SEC-04', '11-PROG-2'),
        ('SEC-05', '12-PROG-1'),
        ('SEC-06', 'POLLUX')
        """.trimIndent()
        db?.execSQL(sql);

    }

    fun StudentRecord(db: SQLiteDatabase?) {
        val sql = """
        """.trimIndent()
        db?.execSQL(sql);
    }

    fun StudentData(db: SQLiteDatabase?) {

    }


    fun ShowField(tableName: String): String { //        var db: SQLiteDatabase;
        //        db = getReadableDatabase();
        //        var cursor: Cursor? = db.query(tableName, null, null, null, null, null, null);
        //        val col: Array<String> = cursor!!.getColumnNames()
        //        //var field: String = ""
        //        var field = ArrayList<String>()
        //        //adding String elements in the list
        //        //arraylist.add("Geeks")
        //
        //        for (c in col) {
        //            field.add(c)
        //        }
        //        val fields = arrayOfNulls<String>(field.size)
        //        field.toArray(fields)
        //        Toast.makeText(this.context, fields.joinToString(), Toast.LENGTH_LONG).show();
        //        return fields.joinToString()
        return "";
    }


    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


    }

    fun CreateStudentMiscTable(db: SQLiteDatabase?, stat: String = "") {

        val tbname = "TBMISC_STUDENT"
        var sql = ""
        if (stat == "DEL") {
            sql = "DROP TABLE IF EXISTS $tbname"
        } else {
            sql = """
             CREATE TABLE IF NOT EXISTS $tbname (  StudentNo text,    OptionNumber  text,  SectionCode text, 
             PRIMARY KEY (OptionNumber,StudentNo))   
             """
        }
        ExecuteSQL(sql, db, tbname, stat)
    }


    fun CreateMiscTable(db: SQLiteDatabase?, stat: String = "") {
        val tbname = "TBMISC"
        var sql = ""
        if (stat == "DEL") {
            sql = "DROP TABLE IF EXISTS $tbname"
        } else {
            sql = """
             CREATE TABLE IF NOT EXISTS $tbname (  MiscCode text, MiscDescription text,  OptionNumber text,  OptionDescription text,  SectionCode text, 
             PRIMARY KEY (OptionNumber, SectionCode))   
             """
        }
        ExecuteSQL(sql, db, tbname, stat)
    }

    fun CreateRecitationable(db: SQLiteDatabase?, stat: String = "") {
        val tbname = "TBRECITATION"
        var sql = ""
        if (stat == "DEL") {
            sql = "DROP TABLE IF EXISTS $tbname"
        } else {
            sql = """
             CREATE TABLE IF NOT EXISTS $tbname (RandomCode Text, StudentNo text, RecitationDate text,  Points Int,SectionCode text,  
             PRIMARY KEY (RandomCode, StudentNo))   
             """
        }
        ExecuteSQL(sql, db, tbname)
    }


    fun CreateSectionTable(db: SQLiteDatabase?, stat: String = "") {
        val tbname = "TBSECTION"
        var sql = ""
        if (stat == "DEL") {
            sql = "DROP TABLE IF EXISTS $tbname"
        } else {
            sql = """
             CREATE TABLE IF NOT EXISTS $tbname (SectionCode text, SectionName text)   
             """
        }
        ExecuteSQL(sql, db, tbname)
    }

    fun ExecuteSQL(sql: String, db: SQLiteDatabase?, entity: String, mode: String = "") {
        try {
            db?.execSQL(sql)
            if (mode == "DEL") {
                Util.Msgbox(context, "The $entity was deleted")
            } else {
                Util.Msgbox(context, "The $entity was created")
            }
        } catch (e: SQLiteException) {
            Util.Msgbox(context, "Error in UpGraded")
            Log.e("SQLER", sql + " " + e.toString())
        }
    }


    fun CreateRandomTable(db: SQLiteDatabase?) {
        var sql = """
             CREATE TABLE IF NOT EXISTS  tbrandom (RandomCode Text, SequenceNumber TEXT,StudentNo text, SectionCode text,  Remark text,  
             PRIMARY KEY (StudentNo, SectionCode))   
             """
        db?.execSQL(sql)


        //ShowField("tbscore")
    }

    fun CreateLogTable(db: SQLiteDatabase?) {
        var sql = """
             CREATE TABLE IF NOT EXISTS  tblog (LogID TEXT,DateTime text,
             Description text, 
             PRIMARY KEY (LogID))   
             """
        db?.execSQL(sql) //ShowField("tbscore")

    }


    fun CreateScoreTable(db: SQLiteDatabase?) {
        var sql = """
             CREATE TABLE IF NOT EXISTS  tbscore (ActivityCode TEXT,SectionCode text,
             StudentNo text,Score INTEGER, Remark text,  
             PRIMARY KEY (ActivityCode,SectionCode, StudentNo))   
             """
        db?.execSQL(sql) //ShowField("tbscore")
    }

    fun CreateStudentTable(db: SQLiteDatabase?) {
        var sql =
            ("CREATE TABLE tbstudent (StudentNo INTEGER PRIMARY KEY,FirstName text, LastName text,GrpNumber text, Section text, Gender Text )")
        db?.execSQL(sql)
        ShowField("tbscore")
    }

    fun CreateSchedTable(db: SQLiteDatabase?) {
        var sql =
            ("create table tbsched (SchedTime text,	SchedDate text,	SectionCode text, Remark text,   PRIMARY KEY (SchedTime, SchedDate, SectionCode))")
        db?.execSQL(sql)
        ShowField("tbscore")
    }

    fun CreateAttendanceTable(db: SQLiteDatabase?) {
        var sql =
            ("create table tbsched (SchedDate text,	SectionCode text,	StudentNumber text, Remark text, AttendanceStatus text  PRIMARY KEY (SchedTime, SchedDate, SectionCode, StudentNumber))")
        db?.execSQL(sql)
        ShowField("tbscore")
    }


    fun CreateInfoTable(db: SQLiteDatabase?) {
        var sql = ("create table  IF NOT EXISTS tbinfo (CurrentSection text)")
        db?.execSQL(sql)
    }

    fun CreateAttendanceView(db: SQLiteDatabase?) {
        var sql = """
            CREATE VIEW IF NOT EXISTS tbattendance_query AS SELECT tbattendance.SchedTime,
                tbattendance.SchedDate,
                tbattendance.SectionCode,
                tbattendance.StudentNumber,
                tbattendance.AttendanceStatus,
                tbattendance.Remark,
                tbstudent.FirstName,
                tbstudent.LastName,
                tbstudent.GrpNumber,
                tbstudent.Gender
            FROM
                tbattendance
                INNER JOIN tbstudent
            ON
                 tbattendance.StudentNumber = tbstudent.StudentNo
            """
        db?.execSQL(sql)
    }

    fun CreateScoreView(db: SQLiteDatabase?) {
        var sql = "DROP VIEW IF EXISTS tbscore_query"
        db?.execSQL(sql)
        sql = """
            CREATE VIEW IF NOT EXISTS tbscore_query AS SELECT
                tbscore.ActivityCode,
                tbscore.SectionCode,
                tbscore.StudentNo,
                tbscore.Score,
                tbscore.Remark,
                tbscore.SubmissionStatus,
                tbstudent.FirstName,
                tbstudent.LastName,
                tbstudent.GrpNumber,
                tbstudent.Gender
            FROM
                tbscore
                INNER JOIN tbstudent
            ON
                 tbscore.StudentNo = tbstudent.StudentNo
            """
        db?.execSQL(sql)
        Log.e("OKOK", "The tbscore_query is created ver 1")
    }

    fun CreateRandomView(db: SQLiteDatabase?) {
        var sql = "DROP VIEW IF EXISTS tbrandom_query"
        db?.execSQL(sql)
        sql = """
            CREATE VIEW IF NOT EXISTS tbrandom_query AS SELECT 
                tbrandom.RandomCode,
                tbrandom.SequenceNumber,
                tbrandom.Remark,
                tbrandom.StudentNo,
                tbrandom.SectionCode,
                tbstudent.FirstName,
                tbstudent.LastName,
                tbstudent.GrpNumber,
                tbstudent.Gender
            FROM
                tbrandom
                INNER JOIN tbstudent
            ON
                 tbrandom.StudentNo = tbstudent.StudentNo
            """
        db?.execSQL(sql)
        Util.Msgbox(context, "The tbscore_query is created")
    }

    fun CreateRecitationView(db: SQLiteDatabase?) {
        var sql = "DROP VIEW IF EXISTS tbrecitation_query"
        db?.execSQL(sql)
        sql = """
            CREATE VIEW IF NOT EXISTS tbrecitation_query AS SELECT 
                tbrecitation.RandomCode,
                tbrecitation.StudentNo,
                tbrecitation.SectionCode,
                tbstudent.FirstName,
                tbstudent.LastName
            FROM
                tbrecitation
                INNER JOIN tbstudent
            ON
                 tbrecitation.StudentNo = tbstudent.StudentNo
            """
        ExecuteSQL(sql, db, "tbrecitation_quety")

    }

    fun CreateMiscView(db: SQLiteDatabase?) {
        var sql = "DROP VIEW IF EXISTS tbmisc_query"
        db?.execSQL(sql)
        try {
            sql = """  CREATE VIEW IF NOT EXISTS tbmisc_query AS  SELECT
        tbmisc_student.StudentNo,
        tbmisc_student.OptionNumber,
        tbmisc_student.SectionCode,
        tbstudent.FirstName,
        tbstudent.LastName,
        tbstudent.GrpNumber,
        tbstudent.Gender,
        tbstudent.Section,
        tbmisc.OptionNumber,
        tbmisc.OptionDescription,
        tbmisc.MiscDescription,
        tbmisc.MiscCode
        FROM
        tbmisc_student
        INNER JOIN tbmisc ON tbmisc.OptionNumber = tbmisc_student.OptionNumber
        INNER JOIN tbstudent ON tbmisc_student.StudentNo = tbstudent.StudentNo """
            db?.execSQL(sql)
            Util.Msgbox(context, "tbmisc_query11 was created")
        } catch (e: SQLiteException) {
            Util.Msgbox(context, "SQL error")
        }


    }


    fun CountTableRecord(tableName: String) {
        var sql = "select * from  $tableName"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        Util.Msgbox(context, cursor.count.toString())
        cursor.close()
    }


    fun GetCurrentSection(): String {
        var sql = "select * from  tbinfo"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        var section = cursor.getString(cursor.getColumnIndex(TBINFO_CURRENTSECTION))
        return section
        cursor.close()
    }


    fun GetCurrentOriginalSection(): String {
        var sql = "select * from  tbinfo"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        var section = cursor.getString(cursor.getColumnIndex("CurrentOriginslSEction"))
        return section
        cursor.close()
    }

    fun DatabaseUtil(sql: String) {
        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun ManageStudent(crudStatus: String, studentno: String, fnanme: String = "", lastname: String = "", middle: String = "", section: String = "", gender: String = "", contact: String = "", parentContact: String = "", address: String = "", email: String = "", schoolStudentNo: String = ""): Boolean {
        Log.e("@@@536", "HEllooooo")

        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("StudentID", studentno)
            contentValues.put("FirstName", fnanme)
            contentValues.put("LastName", lastname)
            contentValues.put("MIddleName", middle)
            contentValues.put("OriginalSection", section)
            contentValues.put("Gender", gender)
            contentValues.put("ContactNumber", contact)
            contentValues.put("ParentContact", parentContact)
            contentValues.put("Address", address)
            contentValues.put("emailAddress", email)
            contentValues.put("SchoolStudentNumber", schoolStudentNo)


            var status: Boolean = false;
            when (crudStatus) {
                "ADD" -> {
                    val success = db.insert("tbstudent_info", null, contentValues)
                    if (success < 0) status = false
                    else status = true
                } //add

                "EDIT" -> {
                    var sql = """
                          update tbstudent_info 
                          set FirstName='$fnanme'
                          ,  LastName='$lastname'
                          ,  MIddleName='$middle'
                          ,  OriginalSection='$section'
                          ,  Gender='$gender'
                          ,  ContactNumber='$contact'
                          ,  ParentContact='$parentContact'
                          ,  Address='$address'
                          ,  emailAddress='$email'
                          ,  SchoolStudentNumber='$schoolStudentNo'
                          where  StudentID ='$studentno'
                         """.trimIndent()
                    db.execSQL(sql)
                    Log.e("@@@536", sql)
                } //edit

                "DELETE" -> {
                    db.delete("TBSCORE", TBSTUDENT_STUDENTNO + "=" + studentno, null) //   db.delete("TBATTENDANCE", "StudentNumber=" + studentno, null)
                    val success = db.delete(TABLE_NAME, TBSTUDENT_STUDENTNO + "=" + studentno, null)

                    if (success < 0) status = false
                    else status = true
                } //edit

            } //when
            return true;
            db.close()
        } catch (e: Exception) {
            Log.e("Err", e.toString())
            return true;
        }

    }

    fun UpdateDroppedStudent(section: String, studentNo: String) {
        val db = this.writableDatabase

        var sql = """
             update tbscore set SubmissionStatus='DR'
                          where SectionCode   ='$section'
                          and  StudentNo ='$studentNo'
        """.trimIndent()

        var sql2 = """
             update tbgrades set Remark='DROPPED'
                          where SectionCode   ='$section'
                          and  StudentNo ='$studentNo'
        """.trimIndent()


        Log.e("@@@", sql)
        Log.e("@@@", sql2)
        db.execSQL(sql)
        db.execSQL(sql2)
    }

    fun GetStudentList(category: String, section: String, searchString: String = ""): ArrayList<StudentInfoModel> {

        val studentList: ArrayList<StudentInfoModel> = ArrayList<StudentInfoModel>()

        var sql: String = """ SELECT  * FROM tbstudent_info
                                where OriginalSection='$section'  """

        Log.e("B210", category)

        if (category == "GROUP_SEARCH") {
            sql = sql + " and $TBSTUDENT_GRP like '$searchString%' order by lastName"
        } else if (category == "NAME_SEARCH") {
            sql = sql + " and $TBSTUDENT_LAST like '$searchString%' order by lastName"
        } else if (category == "GENDER_ORDER") {
            sql = sql + "ORDER BY $TBSTUDENT_GENDER DESC, $TBSTUDENT_LAST"
        } else if (category == "LAST_ORDER") {
            sql = sql + "order by lastName"
        } else if (category == "FIRST_ORDER") {
            sql = sql + "order by firstName"
        } else if (category == "ENROLL_ORDER") {
            sql = sql + "order by enrollmentstatus, lastname"
        } else if (category == "ID_ORDER") {
            sql = sql + "order by studentID"
        } else if (category == "BYLETTER") {
            sql =
                sql + "and LASTNAME LIKE '$searchString%' and status ='ACTIVE' order by lastname"
        } else if (category == "A-C") {
            sql =
                sql + "and (LASTNAME LIKE 'A%' OR LASTNAME LIKE 'B%' OR LASTNAME LIKE 'C%' ) order by lastname"
        } else if (category == "D-J") {
            sql =
                sql + "and (LASTNAME LIKE 'F%' OR LASTNAME LIKE 'G%' OR LASTNAME LIKE 'H%' OR LASTNAME LIKE 'I%'  OR LASTNAME LIKE 'J%' OR LASTNAME LIKE 'D%'  OR LASTNAME LIKE 'E%') order by lastname"
        } else if (category == "K-O") {
            sql =
                sql + "and (LASTNAME LIKE 'K%' OR LASTNAME LIKE 'L%' OR LASTNAME LIKE 'M%' OR LASTNAME LIKE 'N%'  OR LASTNAME LIKE 'O%') order by lastname"
        } else if (category == "P-R") {
            sql =
                sql + "and (LASTNAME LIKE 'P%' OR LASTNAME LIKE 'Q%' OR LASTNAME LIKE 'R%' ) order by lastname"
        } else if (category == "S-Z") {
            sql =
                sql + "and (LASTNAME LIKE 'U%' OR LASTNAME LIKE 'V%' OR LASTNAME LIKE 'W%' OR LASTNAME LIKE 'X%'  OR LASTNAME LIKE 'Y%' OR LASTNAME LIKE 'Z%' OR LASTNAME LIKE 'S%'  OR LASTNAME LIKE 'T%') order by lastname"
        }
        Log.e("SQL200", sql + category)

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("B21", cursor.count.toString())
        var num = 1;
        if (cursor.moveToFirst()) {
            do {
                Log.e("SQL", "hELLO124")
                var sn = cursor.getString(cursor.getColumnIndex("StudentID"))
                var fname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_FIRST))
                var lname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_LAST))

                var section = cursor.getString(cursor.getColumnIndex("OriginalSection"))
                var gender = cursor.getString(cursor.getColumnIndex(TBSTUDENT_GENDER))
                var extension = cursor.getString(cursor.getColumnIndex("Extension"))
                var contactNumber = cursor.getString(cursor.getColumnIndex("ContactNumber"))
                var parentContact = cursor.getString(cursor.getColumnIndex("ParentContact"))

                var address = cursor.getString(cursor.getColumnIndex("Address"))
                var emailAddress = cursor.getString(cursor.getColumnIndex("emailAddress"))
                var schoolStudentNumber =
                    cursor.getString(cursor.getColumnIndex("SchoolStudentNumber"))
                var middleName = cursor.getString(cursor.getColumnIndex("MIddleName"))

                val emp =
                    StudentInfoModel(sn, fname, lname, section, gender, extension, contactNumber, parentContact, address, emailAddress, schoolStudentNumber, middleName, num)

                //                var studentno:String ,
                //                var firstname:String,
                //                var lastname:String,
                //                var originalSection:String,
                //                var gender:String,
                //                var extension:String,
                //                var contactNumber:String,
                //                var parentcontact:String,
                //                var address:String,
                //                var emailAddress:String,
                //                var orderNumm: Int,
                //                var schoolStudentNumber:String,
                //                var expand:String,
                //                var middleName: String


                num++;
                Log.e("AAA", extension + "  " + contactNumber) //val emp = StudentModel(sn, fname, lname, grp, section, gender, extension, contactNumber)
                studentList.add(emp)
                Log.e("SQL", fname + " " + lname)
            } while (cursor.moveToNext())
        }
        return studentList
    }

    fun GetEnrolleList(category: String, section: String, searchString: String = ""): ArrayList<EnrolleModel> {

        val studentList: ArrayList<EnrolleModel> = ArrayList<EnrolleModel>()

        var sql: String = """ SELECT  * FROM tbstudent_query
                                where Section='$section'  """

        Log.e("B210", category)

        if (category == "GROUP_SEARCH") {
            sql = sql + " and $TBSTUDENT_GRP like '$searchString%' order by lastName"
        } else if (category == "NAME_SEARCH") {
            sql = sql + " and $TBSTUDENT_LAST like '$searchString%' order by lastName"
        } else if (category == "GENDER_ORDER") {
            sql = sql + "ORDER BY $TBSTUDENT_GENDER DESC, $TBSTUDENT_LAST"
        } else if (category == "LAST_ORDER") {
            sql = sql + "order by lastName"
        } else if (category == "FIRST_ORDER") {
            sql = sql + "order by firstName"
        } else if (category == "ENROLL_ORDER") {
            sql = sql + "order by enrollmentstatus, lastname"
        } else if (category == "GRP_ORDER") {
            sql = sql + "order by grpnumber"
        } else if (category == "RANDOM_ORDER") {
            sql = sql + "order by number"
        } else if (category == "A-C") {
            sql =
                sql + "and (LASTNAME LIKE 'A%' OR LASTNAME LIKE 'B%' OR LASTNAME LIKE 'C%' ) order by lastname"
        } else if (category == "D-J") {
            sql =
                sql + "and (LASTNAME LIKE 'F%' OR LASTNAME LIKE 'G%' OR LASTNAME LIKE 'H%' OR LASTNAME LIKE 'I%'  OR LASTNAME LIKE 'J%' OR LASTNAME LIKE 'D%'  OR LASTNAME LIKE 'E%') order by lastname"
        } else if (category == "K-O") {
            sql =
                sql + "and (LASTNAME LIKE 'K%' OR LASTNAME LIKE 'L%' OR LASTNAME LIKE 'M%' OR LASTNAME LIKE 'N%'  OR LASTNAME LIKE 'O%') order by lastname"
        } else if (category == "P-R") {
            sql =
                sql + "and (LASTNAME LIKE 'P%' OR LASTNAME LIKE 'Q%' OR LASTNAME LIKE 'R%' ) order by lastname"
        } else if (category == "S-Z") {
            sql =
                sql + "and (LASTNAME LIKE 'U%' OR LASTNAME LIKE 'V%' OR LASTNAME LIKE 'W%' OR LASTNAME LIKE 'X%'  OR LASTNAME LIKE 'Y%' OR LASTNAME LIKE 'Z%' OR LASTNAME LIKE 'S%'  OR LASTNAME LIKE 'T%') order by lastname"
        }
        Log.e("SQL200", sql + category)

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("B21", cursor.count.toString())
        var num = 1;
        if (cursor.moveToFirst()) {
            do {

                var sn = cursor.getString(cursor.getColumnIndex("StudentNo"))
                var fname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_FIRST))
                var lname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_LAST))
                var section = cursor.getString(cursor.getColumnIndex(TBSTUDENT_SECTION))
                var gender = cursor.getString(cursor.getColumnIndex(TBSTUDENT_GENDER))
                var enrollmentStatus = cursor.getString(cursor.getColumnIndex("EnrollmentStatus"))
                var studentID = cursor.getString(cursor.getColumnIndex("StudentID"))
                var grpNumber = cursor.getString(cursor.getColumnIndex("GrpNumber"))
                var link = cursor.getString(cursor.getColumnIndex("FolderLink"))
                var rnd = cursor.getString(cursor.getColumnIndex("Number")).toInt()
                var status = cursor.getString(cursor.getColumnIndex("Status"))
                val emp =
                    EnrolleModel(num, sn, fname, lname, section, gender, enrollmentStatus, studentID, grpNumber, link, rnd, status)

                //      var studentno:String ,
                //                var firstname:String,
                //                var lastname:String,
                //                var Section:String,
                //                var gender:String,
                //                var enrollmentStatus:String,
                //                var studentID:String,
                //                var grpNumber:String,
                num++; //val emp = StudentModel(sn, fname, lname, grp, section, gender, extension, contactNumber)
                studentList.add(emp)
                Log.e("SQL", fname + " " + link)
            } while (cursor.moveToNext())
        }
        return studentList
    }


    fun ShowAll(tableName: String, filter: String = "") {
        var sql = "select * from $tableName"
        if (filter != "") sql = sql + filter
        try {
            val db = this.readableDatabase
            var cursor: Cursor? = null
            cursor = db.rawQuery(sql, null)

            var tableField = ""
            for (x in 0..cursor.columnCount - 1) tableField =
                tableField + "\t" + cursor.columnNames[x].toString()
            Log.e("TBREC", tableField)
            Util.Msgbox(context, tableField)


            if (cursor.moveToFirst()) {
                do {
                    var str = ""
                    for (x in 0..cursor.columnCount - 1) {
                        var field = cursor.columnNames[x].toString()
                        var value = cursor.getString(cursor.getColumnIndex(field))
                        str = str + "\t" + value

                    }
                    Log.e("TBREC", str)
                } while (cursor.moveToNext())
            }
        } catch (e: SQLiteException) {
            Log.e("SQLER", sql + " " + e.toString())

        }


    }

    fun GetSubjectStudentNo(studentID: String, section: String): String {
        var sql = """
                SELECT * FROM tbenroll
                   where Section='$section'  
                   and  StudentID='$studentID'  
                  """
        Log.e("sss", sql)
        val db = this.writableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("StudentNo"))
        } else {
            return "NONE"
        }
    }


    //region 1
    fun GetNewSectionCode(): String {
        var sql = """
                SELECT * FROM TBsection
                ORDER BY $TBSECTION_CODE DESC
                  """
        val db = this.writableDatabase
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            var sectionCode = cursor.getString(cursor.getColumnIndex(TBSECTION_CODE))
            Log.e("SECTIONCODE", sectionCode)
            var num = sectionCode.takeLast(2).toInt() + 1 // Grade>>>
            Util.Msgbox(context, num.toString())
            return "SEC-" + Util.ZeroPad(num, 2)
        } else {
            return "SEC-10"
        }

    }

    fun SaveNewSection() {

    } //end region 1


    fun SearchStudent(lastname: String = ""): ArrayList<StudentModel> {

        val studentList: ArrayList<StudentModel> = ArrayList<StudentModel>()

        var sql: String = """ SELECT  * FROM TBSTUDENT
                          where LastName like '$lastname%' order by LastName, FirstName
                          """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLiteException) {
            db.execSQL(sql)
            return ArrayList()
        }
        var num = 1;

        if (cursor.moveToFirst()) {
            do {
                var studentID = cursor.getString(cursor.getColumnIndex(TBSTUDENT_STUDENTNO))
                var fname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_FIRST))
                var lname = cursor.getString(cursor.getColumnIndex(TBSTUDENT_LAST))
                var grp = cursor.getString(cursor.getColumnIndex(TBSTUDENT_GRP))
                var orig_section = cursor.getString(cursor.getColumnIndex("OriginslSection"))
                var gender = cursor.getString(cursor.getColumnIndex(TBSTUDENT_GENDER))
                var extension = cursor.getString(cursor.getColumnIndex("Extension"))
                var contactNumber = cursor.getString(cursor.getColumnIndex("ContactNumber"))
                var parentContcact = cursor.getString(cursor.getColumnIndex("ParentContact"))
                var enrollmentStatus = cursor.getString(cursor.getColumnIndex("EnrollmentStatus"))
                var address = cursor.getString(cursor.getColumnIndex("Address"))
                var emailAddress = cursor.getString(cursor.getColumnIndex("Address"))
                var schoolStudentNumber =
                    cursor.getString(cursor.getColumnIndex("SchoolStudentNumber"))
                var middleName = cursor.getString(cursor.getColumnIndex("MIddleName"))
                val emp =
                    StudentModel(studentID, fname, lname, grp, orig_section, gender, extension, contactNumber, parentContcact, enrollmentStatus, address, emailAddress, num, schoolStudentNumber, "NO", middleName)
                num++;
                studentList.add(emp)
            } while (cursor.moveToNext())
        }
        return studentList
    }


    fun SetCurrentSection(section: String) {
        var sql = "update tbinfo set CurrentSection ='$section'"
        Log.e("DDD", sql)
        val db = this.writableDatabase
        db.execSQL(sql)
    }


    fun SetOriginalSection(section: String) {
        var sql = "update tbinfo set CurrentOriginslSEction ='$section'"
        val db = this.writableDatabase
        db.execSQL(sql)
    }


    fun UpdateGroupNumber(studentNo: String, grpNumber: String) {
        var sql = "update tbstudent set GrpNumber   ='$grpNumber' where StudentNo  ='$studentNo'"
        Log.e("eeee", sql)
        val db = this.writableDatabase
        db.execSQL(sql)
    }


    fun CheckStudent(studnum: String, section: String): String {
        var sql: String = """ SELECT  * FROM TBSTUDENT_INFO
                          where StudentID   = '$studnum'
                          and OriginalSection   = '$section'
                          """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        Log.e("OLLLD", sql + "   " + cursor.count)
        if (cursor.moveToFirst()) return "OLD"
        else return "NEW"
    }

    fun CheckStudenNumber(studnum: String): String {
        var sql: String = """ SELECT  * FROM TBSTUDENT_INFO
                          where StudentID   = '$studnum'
                          """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) return "EXIST"
        else return "MISSING"
    }


    fun ManageStudentRecord(studnumm: String, firstName: String, lastName: String, middleName: String, extension: String, section: String, gender: String, contactNumber: String, parentContcact: String, address: String, emailAddress: String, schoolStudentNumber: String, mode: String) {

        var sql = ""
        var sql2 = ""
        var sql3 = ""
        val db = this.writableDatabase
        if (mode == "EDIT") {
            sql = """
             update tbstudent_info set FirstName ='$firstName'
                          ,  LastName ='$lastName'
                          ,  middleName = ''
                          ,  Gender ='$gender'
                          ,  Extension ='$extension'
                          ,  ContactNumber ='$contactNumber'
                          ,  ParentContact ='$parentContcact'
                            ,Address ='$address'
                            ,emailAddress ='$emailAddress'
                            ,SchoolStudentNumber ='$schoolStudentNumber',
                            OriginalSection ='$section'
                          WHERE  StudentID ='$studnumm'
        """.trimIndent()
            Log.e("SQL", sql)
            db.execSQL(sql)

        } else if (mode == "ADD") {
            sql = """
             insert into tbstudent_info (StudentID,FirstName,LastName,Extension,MIddleName, 
                       OriginalSection,Gender,ContactNumber,ParentContact,address, emailAddress,SchoolStudentNumber)
             values('$studnumm', '$firstName','$lastName','$extension' , '$middleName', '$section', '$gender', '$contactNumber',  
                    '$parentContcact', '$address', '$emailAddress', '$schoolStudentNumber') 
            """

            //            sql2= """
            //           insert into tbgrades values('$section', '$studnumm', 0.0, 0.0, 0.0, 0.0, 0.0, "-", 0.0, 0.0, 0.0, 1, "-", "-")
            //            """

            //            sql3= """
            //                INSERT INTO tbscore (ActivityCode, SectionCode, StudentNo,Score, Remark,SubmissionStatus, AdjustedScore, GradingPeriod )
            //                    SELECT ActivityCode, '$section', $studnumm, 0, "-", 'NO', 0, GradingPeriod
            //                    FROM tbactivity
            //                    WHERE sectionCode='$section';
            //                """
            Log.e("1234", sql)
            db.execSQL(sql) //            db.execSQL(sql2)
            //            db.execSQL(sql3)

        }
        Log.e("0", sql)
        Log.e("2", sql2)
        Log.e("3", sql3)


        db.close()
    }


    fun DeleteStudentRecord(studnumm: String, section: String) {
        val db = this.writableDatabase
        var sql = """
             delete from  tbgrades 
                          where  SectionCode ='$section'
                          and  StudentNo ='$studnumm'
                   """
        db.execSQL(sql)




        sql = """
             delete from  tbscore 
                          where  SectionCode ='$section'
                          and  StudentNo ='$studnumm'
                   """
        db.execSQL(sql)

        sql = """
             delete from  tbattendance
                          where  SectionCode ='$section'
                          and  StudentNumber ='$studnumm'
                   """
        db.execSQL(sql)

        sql = """
             delete from tbenroll
                          where  Section ='$section'
                          and  StudentNo ='$studnumm'
                   """
        db.execSQL(sql)

    }

    fun UpdateEnrollmentStatus(studnumm: String, section: String, enrollmentStatus: String) {
        val db = this.writableDatabase
        var submissionStatus = ""
        if (enrollmentStatus == "DROPPED") submissionStatus = "DRP"
        else submissionStatus = "NO"

        var sql = """
             update   tbstudent 
                          set   EnrollmentStatus ='$enrollmentStatus'
                          where  Section ='$section'
                          and  StudentNo ='$studnumm'
                   """
        db.execSQL(sql)

        sql = """
             update   tbscore
                          set   SubmissionStatus  ='$submissionStatus'
                          where  SectionCode ='$section'
                          and  StudentNo ='$studnumm'
                   """
        db.execSQL(sql)
    }


    fun CountStudentRecord(section: String, category: String): Int {

        var sql = ""
        if (category == "MALE") {
            sql = "select * from  tbstudent_query where gender ='MALE' and section ='$section'"
        } else if (category == "FEMALE") {
            sql = "select * from  tbstudent_query where gender ='FEMALE' and section ='$section'"
        } else if (category == "TOTAL") {
            sql = "select * from  tbstudent_query where section ='$section'"
        } else if (category == "ENROLLED") {
            sql =
                "select * from  tbstudent_query where enrollmentStatus ='ENROLLED' and section ='$section'"
        } else if (category == "DROPPED") {
            sql =
                "select * from  tbstudent_query where enrollmentStatus ='DROPPED' and section ='$section'"
        }

        Log.e("CCC", sql)
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        return cursor.count
    }

    fun GetStudentNumberViaEmail(section: String, email: String): Int {

        val sql = "select * from  tbstudent where  emailAddress ='$email' and section ='$section'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        Log.e("Emm", sql)
        if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex(TBSTUDENT_STUDENTNO))
            .toInt()
        else return 0
    }


}






