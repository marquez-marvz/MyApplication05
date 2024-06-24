package com.example.myapplication05

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.media.audiofx.AudioEffect.Descriptor
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer
import java.lang.Exception


class Grades(context: Context) : DatabaseHandler(context) {


    fun GetStudentGrades(sectionCode: String, studentNo: String, gradingPeriod: String): Int {
        val totalQuizScore =
            GetStudentTotal(sectionCode, studentNo, "Quiz", gradingPeriod).toFloat()
        val totalPerformaceScore =
            GetStudentTotal(sectionCode, studentNo, "PT", gradingPeriod).toFloat()
        val totalQuizItem = GetItemTotal(sectionCode, "Quiz", gradingPeriod)
        val totalPerformanceItenm = GetItemTotal(sectionCode, "PT", gradingPeriod)
        Log.e("GR", "TSQ" + totalQuizScore)
        Log.e("GR", "TSP" + totalPerformaceScore)
        Log.e("GR", "TIQ" + totalQuizItem)
        Log.e("GR", "TIP" + totalPerformanceItenm)
        Log.e("GR", "TIP" + (totalQuizScore / totalQuizItem))
        Log.e("GR", "TIP" + (totalPerformaceScore / totalPerformanceItenm))


        var tempGrade =
            ((totalQuizScore / totalQuizItem) * 100) * 0.3 + ((totalPerformaceScore / totalPerformanceItenm) * 100) * 0.7
        tempGrade = tempGrade
        Log.e("GR", "GRADES " + tempGrade.toString())
        Log.e("DEPED", "GRADES " + ConvertDepedGrade(tempGrade))

        return 1 //eturn ConvertDepedGrade(tempGrade)
    }

    fun ComputeGrades(sectionCode: String, studentNo: String, gradingPeriod: String) {
        val totalQuizScore =
            GetStudentTotal(sectionCode, studentNo, "Quiz", gradingPeriod).toFloat()
        val totalPerformaceScore =
            GetStudentTotal(sectionCode, studentNo, "PT", gradingPeriod).toFloat()
        val totalQuizItem = GetItemTotal(sectionCode, "Quiz", gradingPeriod)
        val totalPerformanceItenm = GetItemTotal(sectionCode, "PT", gradingPeriod)
        var tempGrade =
            ((totalQuizScore / totalQuizItem) * 100) * 0.3 + ((totalPerformaceScore / totalPerformanceItenm) * 100) * 0.7
        var realGrade =
            ConvertDepedGrade(tempGrade) //pdateGrade(sectionCode, studentNo, gradingPeriod, realGrade)
    }


    fun CollegeHeading(sectionCode: String, gradingPeriod: String): String {
        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var strItem = "\t\t"
        var strActCode = "\t\t"


        var exam = 0;
        Log.e("@@@ ", cursor.count.toString())

        var first = true
        var activityString = "\t\t"
        var descString = "\t\t"
        var itemString = "\t\t"
        var categoryString = "\t" + "Version " + GetVersion(sectionCode) + "\t"


        if (cursor.moveToFirst()) {
            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var desc = cursor.getString(cursor.getColumnIndex("Description"))
                activityString = activityString + activityCode + "\t"
                descString = descString + desc + "\t"
                itemString = itemString + item + "\t"
                categoryString = categoryString + category + "\t"
            } while (cursor.moveToNext())

        }
        return activityString + "\n" + descString + "\n" + categoryString + "\n" + itemString + "\n"
    }


    fun DEPEDHeading(sectionCode: String, gradingPeriod: String): String {
        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var strItem = "\t\t"
        var strActCode = "\t\t"


        var exam = 0;
        Log.e("DEPEDHeading", cursor.count.toString())

        var activityString = ""
        var activityDesc = ""
        var activityItem = ""
        var activityCategory = ""
        var activityCount = 0;
        var activityTotal = 0


        var quizString = "\t\t"
        var quizDesc = "\t\t"
        var quizItem = "\t\t"
        var quizCategory = "\t" + "Version " + GetVersion(sectionCode) + "\t"

        var quizTotal = 0;
        var quizCount = 0;




        if (cursor.moveToFirst()) {
            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var desc = cursor.getString(cursor.getColumnIndex("Description"))
                Log.e("DEPEDHeading", category + "  " + activityCode + "  " + desc)

                if (category == "PT") {
                    activityString = activityString + activityCode + "\t"
                    activityDesc = activityDesc + desc + "\t"
                    activityItem = activityItem + item + "\t"
                    activityCategory = activityCategory + category + "\t"
                    activityCount++
                    activityTotal = activityTotal + item
                } else if (category == "Quiz") {
                    quizString = quizString + activityCode + "\t"
                    quizDesc = quizDesc + desc + "\t"
                    quizItem = quizItem + item + "\t"
                    quizCategory = quizCategory + category + "\t"
                    quizCount++
                    quizTotal = quizTotal + item
                }


            } while (cursor.moveToNext())


        }

        Log.e("DEPEDHeading", activityString + "  " + activityDesc + "  " + activityCategory)
        Log.e("DEPEDHeading", quizString + "  " + quizDesc + "  " + quizCategory)


        for (i in activityCount + 1..10) {
            activityString = activityString + "ACT" + i + "\t"
            activityDesc = activityDesc + "\t"
            activityItem = activityItem + "\t"
            activityCategory = activityCategory + "\t"
        }

        activityString = activityString + "Total" + "\t" + "PS" + "\t" + "WS"
        activityDesc = activityDesc + "\t" + "" + "\t" + ""
        activityItem = activityItem + activityTotal + "\t"
        activityCategory = activityCategory + "\t" + "" + "\t"

        for (i in quizCount + 1..10) {
            quizString = quizString + "QUIZ" + i + "\t"
            quizDesc = quizDesc + "\t"
            quizItem = quizItem + "\t"
            quizCategory = quizCategory + "\t"
        }

        quizString = quizString + "Total" + "\t" + "PS" + "\t" + "WS"
        quizDesc = quizDesc + "\t" + "" + "\t" + "" + "\t" + ""
        quizItem = quizItem + quizTotal + "\t" + "" + "\t" + ""
        quizCategory = quizCategory + "\t" + "" + "\t" + ""


        var str = quizString + "\t\t" + activityString + "\t\t\t" + "IG" + "\t" + "QG" + "\n"
        str = str + quizDesc + "\t" + activityDesc + "\n"
        str = str + quizItem + "\t\t" + activityItem + "\n"
        str = str + quizCategory + "\t\t" + activityCategory + "\n"
        Log.e("DEPEDHeading", str)

        return "\n" + str
    }


    fun GradeComputation(sectionCode: String, studentNo: String, gradingPeriod: String): String {
        val school = GetSchool(sectionCode)
        var gr = ""
        if (school == "CSPC") {
            gr =
                CSPCComputeGrades(sectionCode, studentNo, gradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
        } else if (school == "BISCAST") {
            gr =
                BISCASATComputeGrades(sectionCode, studentNo, gradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
        } else {
            gr =
                DEPEDComputeGrades(sectionCode, studentNo, gradingPeriod) //myGrades.DisplayLogGrade(sectionCode, "FIRST")
        }
        return gr
    }

    fun CSPCComputeGrades(sectionCode: String, studentNo: String, gradingPeriod: String, ret: String = ""): String {

        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """


        var sql2: String = """ SELECT  * FROM tbsection_query
                                where SectionName  ='$sectionCode'  
                          """
        Log.e("SQL", sql2) //        SectionCode	SectionName	Status	School	Subject
        //        Version	Message	SubjectCode	SubjectDescription
        //        PT	Quiz	Exam	Project		ClassStandingCover

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var cursor2: Cursor? = null
        cursor2 = db.rawQuery(sql2, null)
        cursor2.moveToFirst()
        var classStandingPercentage =
            cursor2.getString(cursor2.getColumnIndex("ClassStanding")).toInt() / 100.0
        var projectPercentage = cursor2.getString(cursor2.getColumnIndex("Project")).toInt() / 100.0
        var examPercentage = cursor2.getString(cursor2.getColumnIndex("Exam")).toInt() / 100.0
        var quizPercentage = cursor2.getString(cursor2.getColumnIndex("Quiz")).toInt() / 100.0
        var ptPercentage = cursor2.getString(cursor2.getColumnIndex("PT")).toInt() / 100.0
        var participationPercentage =
            cursor2.getString(cursor2.getColumnIndex("Participation")).toInt() / 100.0
        var classStandingCoverage =
            cursor2.getString(cursor2.getColumnIndex("ClassStandingCoverage")).toUpperCase()

        var activityOriginal = 0.0;
        var activityAdjusted = 0.0;
        var quizOriginal = 0.0;
        var quizAdjusted = 0.0;
        var quizCount = 0.0;
        var activityCount = 0
        var examCount = 0
        var examOriginal = -0.0
        var examAdjusted = -0.0
        var projectOriginal = 0.0;
        var projectAdjusted = 0.0;
        var participationOriginal = 0.0
        var participationAdjusted = 0.0


        var strItem = "\t\t"
        var strActCode = "\t\t"


        var exam = 0;
        Log.e("@@@ ", cursor.count.toString())
        var studentRecord = ""

        var first = true
        var activityString = ""

        var activityComputation = ""
        var quizComputation = ""
        var examComputatiion = ""
        var partComputatiion = ""
        var projectComputatiion = ""
        if (cursor.moveToFirst()) {

            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var description = cursor.getString(cursor.getColumnIndex("Description"))
                var score = GetStudentScore(activityCode, studentNo, "SCORE")
                var enrollmentStatus = GetEnrollmentStatus(sectionCode, studentNo, gradingPeriod)
                var adjusted = GetStudentScore(activityCode, studentNo, "ADJUSTED")
                strActCode = strActCode + activityCode + "\t\t"
                strItem = strItem + item + "\t\t"

                if (category == "PT") {
                    var activityGrade = (score.toDouble() / item.toDouble()) * 50 + 50
                    activityOriginal = activityOriginal + activityGrade
                    activityGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    activityAdjusted = activityAdjusted + activityGrade
                    activityCount++;
                    var grades = String.format("%.2f", activityGrade).toDouble()
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    Log.e("", category + " " + item + "  " + "   " + activityCode + "  " + score + " " + activityGrade)
                    activityComputation =
                        activityComputation + description + ": " + adjusted + "/" + item + "=" + grades + "\n"
                } else if (category == "QUIZ") {
                    var quizGrade = (score.toDouble() / item.toDouble()) * 50 + 50
                    quizOriginal = quizOriginal + quizGrade
                    quizGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    quizAdjusted = quizAdjusted + quizGrade
                    quizCount++;
                    Log.e("", category + " " + item + "  " + "   " + activityCode + "  " + score + "  " + quizGrade)
                    var grades = String.format("%.2f", quizGrade).toDouble()
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    quizComputation =
                        quizComputation + description + ": " + adjusted + "/" + item + "=" + grades + "\n"

                } else if (category == "EXAM") {
                    var examOriginalGrade = (score.toDouble() / item.toDouble()) * 50 + 50
                    examOriginal = examOriginal + examOriginalGrade

                    var examAdjustedGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    examAdjusted = examAdjusted + examAdjustedGrade
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    examCount++
                    examComputatiion =
                        examComputatiion + adjusted + "/" + item + "=" + examAdjustedGrade + "\n" //    studentRecord = studentRecord + "\t" + adjusted + "\t" + grades

                } else if (category == "PROJECT") {
                    projectOriginal = (score.toDouble() / item.toDouble()) * 50 + 50
                    projectAdjusted = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    projectComputatiion =
                        projectComputatiion + adjusted + "/" + item + "=" + projectAdjusted + "\n"                       //studentRecord = studentRecord + "\t" + adjusted + "\t" + grades

                } else if (category == "PARTICIPATION") {
                    participationOriginal = (score.toDouble() / item.toDouble()) * 50 + 50
                    participationAdjusted = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    partComputatiion =
                        partComputatiion + adjusted + "/" + item + "=" + participationAdjusted + "\n"                      // studentRecord = studentRecord + "\t" + adjusted + "\t" + grades
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                }


            } while (cursor.moveToNext())
        }

        var averageOriginalActivity = activityOriginal / activityCount
        var averageOriginalQuiz = quizOriginal / quizCount
        var averageOriginalExam = examOriginal / examCount

        Log.e("ORIGINAL ACTIVity", averageOriginalActivity.toString())
        Log.e("ORIGINAL QUIZ", averageOriginalQuiz.toString())
        Log.e("ORIGINAL EXAM", averageOriginalExam.toString())
        Log.e("ORIGINAL PROJECT", projectOriginal.toString())
        Log.e("ORIGINAL PARTICIPATION", participationOriginal.toString())
        var originalClassStanding = 0.0
        if (classStandingCoverage.contains("QUIZ")) originalClassStanding =
            originalClassStanding + (averageOriginalQuiz * quizPercentage)
        if (classStandingCoverage.contains("PT")) originalClassStanding =
            originalClassStanding + (averageOriginalActivity * ptPercentage)
        if (classStandingCoverage.contains("PARTICIPATION")) originalClassStanding =
            originalClassStanding + (participationOriginal * participationPercentage)


        originalClassStanding = String.format("%.2f", originalClassStanding).toDouble()
        averageOriginalExam = String.format("%.2f", averageOriginalExam).toDouble()
        projectOriginal = String.format("%.2f", projectOriginal).toDouble()

        var originalGrade =
            originalClassStanding * classStandingPercentage + averageOriginalExam * examPercentage + projectPercentage * projectOriginal

        var averageAdjustedActivity = activityAdjusted / activityCount
        var averageAdjustedQuiz = quizAdjusted / quizCount
        var averageAdjustedExam = examAdjusted / examCount
        var adjustedClassStanding = 0.0
        if (classStandingCoverage.contains("QUIZ")) adjustedClassStanding =
            adjustedClassStanding + String.format("%.2f", averageAdjustedQuiz * quizPercentage)
                .toDouble() //(averageAdjustedQuiz * )
        if (classStandingCoverage.contains("PT")) adjustedClassStanding =
            adjustedClassStanding + String.format("%.2f", averageAdjustedActivity * ptPercentage)
                .toDouble() //   (averageAdjustedActivity * )
        if (classStandingCoverage.contains("PARTICIPATION")) adjustedClassStanding =
            adjustedClassStanding + String.format("%.2f", participationAdjusted * participationPercentage)
                .toDouble() // (participationOriginal * participationPercentage)


        adjustedClassStanding = String.format("%.2f", adjustedClassStanding).toDouble()
        averageAdjustedExam = String.format("%.2f", averageAdjustedExam).toDouble()
        projectAdjusted = String.format("%.2f", projectAdjusted).toDouble()

        Log.e("PERPER", (adjustedClassStanding * classStandingPercentage).toString())
        Log.e("PERPER", (averageAdjustedExam * examPercentage).toString())
        Log.e("PERPER", (projectPercentage * projectAdjusted).toString())

        var adjustedGrade =
            String.format("%.2f", adjustedClassStanding * classStandingPercentage).toDouble()
        adjustedGrade =
            adjustedGrade + String.format("%.2f", averageAdjustedExam * examPercentage).toDouble()
        adjustedGrade =
            adjustedGrade + String.format("%.2f", projectPercentage * projectAdjusted).toDouble()

        // var adjustedGrade = adjustedClassStanding * classStandingPercentage + averageAdjustedExam * examPercentage + projectPercentage * projectAdjusted


        var computationString = ""
        computationString =
            computationString + "ACTIVITY\n" + activityComputation + "\n" + "ACT AVG:" + averageAdjustedActivity + "\n"
        computationString =
            computationString + "QUIZ\n" + quizComputation + "\n" + "QUIZ AVG:" + averageAdjustedQuiz + "\n"
        computationString = computationString + "PARTICIPATION\n" + partComputatiion + "\n"
        computationString = computationString + "PROJECT\n" + projectComputatiion + "\n"
        computationString = computationString + "EXAM\n" + examComputatiion + "\n"

        Log.e("COMPUTATION", computationString)
        Log.e("EXAM", examAdjusted.toString())
        Log.e("PROJ", projectAdjusted.toString())
        Log.e("CS", adjustedClassStanding.toString())
        var equivalentOriginalGrade = GetCSPCEquivalentGrades(originalGrade);
        var equivalentAdjustedGrade = GetCSPCEquivalentGrades(adjustedGrade);
        UpdateGradeStudent(sectionCode, studentNo, gradingPeriod, adjustedGrade, equivalentOriginalGrade, equivalentAdjustedGrade)

        studentRecord =
            studentRecord + "\t" + "" + "\t" + examAdjusted + "\t" + "" + "\t" + equivalentOriginalGrade

        if (ret == "COMPUTATION") return computationString
        else return studentRecord
    }


    fun BISCASATComputeGrades(sectionCode: String, studentNo: String, gradingPeriod: String, ret: String = ""): String {

        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var sql2: String = """ SELECT  * FROM tbsection_query
                                where SectionName  ='$sectionCode'  
                          """
        Log.e("SQL123", sql2) //        SectionCode	SectionName	Status	School	Subject
        //        Version	Message	SubjectCode	SubjectDescription
        //        PT	Quiz	Exam	Project		ClassStandingCover


        var cursor2: Cursor? = null
        cursor2 = db.rawQuery(sql2, null)
        cursor2.moveToFirst()
        var classStandingPercentage =
            cursor2.getString(cursor2.getColumnIndex("ClassStanding")).toInt() / 100.0
        var projectPercentage = cursor2.getString(cursor2.getColumnIndex("Project")).toInt() / 100.0
        var examPercentage = cursor2.getString(cursor2.getColumnIndex("Exam")).toInt() / 100.0
        var quizPercentage = cursor2.getString(cursor2.getColumnIndex("Quiz")).toInt() / 100.0
        var ptPercentage = cursor2.getString(cursor2.getColumnIndex("PT")).toInt() / 100.0
        var participationPercentage =
            cursor2.getString(cursor2.getColumnIndex("Participation")).toInt() / 100.0
        var classStandingCoverage =
            cursor2.getString(cursor2.getColumnIndex("ClassStandingCoverage")).toUpperCase()

        Log.e("SQL123", quizPercentage.toString())
        Log.e("SQL123", ptPercentage.toString())
        Log.e("SQL123", examPercentage.toString())

        var activityOriginal = 0.0;
        var activityAdjusted = 0.0;
        var quizOriginal = 0.0;
        var quizAdjusted = 0.0;
        var quizCount = 0.0;
        var activityCount = 0
        var examOriginal = -0.0
        var examAdjusted = -0.0
        var projectOriginal = 0.0;
        var projectAdjusted = 0.0;
        var participationOriginal = 0.0
        var participationAdjusted = 0.0


        var strItem = "\t\t"
        var strActCode = "\t\t"


        var exam = 0;
        Log.e("@@@ ", cursor.count.toString())
        var studentRecord = ""

        var first = true
        var activityString = ""

        var activityComputation = ""
        var quizComputation = ""
        var examComputatiion = ""
        var partComputatiion = ""
        var projectComputatiion = ""
        if (cursor.moveToFirst()) {

            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var description = cursor.getString(cursor.getColumnIndex("Description"))
                var score = GetStudentScore(activityCode, studentNo, "SCORE")
                var enrollmentStatus = GetEnrollmentStatus(sectionCode, studentNo, gradingPeriod)
                var adjusted = GetStudentScore(activityCode, studentNo, "ADJUSTED")
                strActCode = strActCode + activityCode + "\t\t"
                strItem = strItem + item + "\t\t"

                if (category == "PT") {
                    var activityGrade = (score.toDouble() / item.toDouble()) * 50 + 50
                    activityOriginal = activityOriginal + activityGrade
                    activityGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    activityAdjusted = activityAdjusted + activityGrade
                    activityCount++;
                    var grades = String.format("%.2f", activityGrade).toDouble()
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    Log.e("", category + " " + item + "  " + "   " + activityCode + "  " + score + " " + activityGrade)
                    activityComputation =
                        activityComputation + description + ": " + adjusted + "/" + item + "=\t" + grades + "\n"
                } else if (category == "QUIZ") {
                    var quizGrade = (score.toDouble() / item.toDouble()) * 50 + 50
                    quizOriginal = quizOriginal + quizGrade
                    quizGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    quizAdjusted = quizAdjusted + quizGrade
                    quizCount++;
                    Log.e("", category + " " + item + "  " + "   " + activityCode + "  " + score + "  " + quizGrade)
                    var grades = String.format("%.2f", quizGrade).toDouble()
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    quizComputation =
                        quizComputation + description + ": " + adjusted + "/" + item + "=\t" + grades + "\n"

                } else if (category == "EXAM") {
                    examOriginal = (score.toDouble() / item.toDouble()) * 50 + 50
                    examAdjusted = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)

                    examComputatiion =
                        examComputatiion + adjusted + "/" + item + "=\t" + examAdjusted + "\n" //    studentRecord = studentRecord + "\t" + adjusted + "\t" + grades

                } else if (category == "PROJECT") {
                    projectOriginal = (score.toDouble() / item.toDouble()) * 50 + 50
                    projectAdjusted = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                    projectComputatiion =
                        projectComputatiion + adjusted + "/" + item + "=\t" + projectAdjusted + "\n"                       //studentRecord = studentRecord + "\t" + adjusted + "\t" + grades

                } else if (category == "PARTICIPATION") {
                    participationOriginal = (score.toDouble() / item.toDouble()) * 50 + 50
                    participationAdjusted = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                    partComputatiion =
                        partComputatiion + adjusted + "/" + item + "=\t" + participationAdjusted + "\n"                      // studentRecord = studentRecord + "\t" + adjusted + "\t" + grades
                    studentRecord =
                        studentRecord + "\t" + CheckDropped(sectionCode, studentNo, adjusted, gradingPeriod)
                }


            } while (cursor.moveToNext())
        }

        var averageActivity = activityOriginal / activityCount
        var averageQuiz = quizOriginal / quizCount
        var originalGrade =


            (averageActivity * ptPercentage) + (averageQuiz * quizPercentage) + (examOriginal * examPercentage)
        Log.e("Midgrade", originalGrade.toString())



        averageActivity = activityAdjusted / activityCount
        averageQuiz = quizAdjusted / quizCount

        val adjustedGrade =
            (averageActivity * ptPercentage) + (averageQuiz * quizPercentage) + (examAdjusted * examPercentage)
        var computationString = ""
        computationString =
            computationString + "ACTIVITY\n" + activityComputation + "\n" + "ACT AVG:\t" + averageActivity + "\n\n"
        computationString =
            computationString + "QUIZ\n" + quizComputation + "\n" + "QUIZ AVG:\t" + averageQuiz + "\n\n"
        computationString = computationString + "PARTICIPATION\t" + partComputatiion + "\n\n"
        computationString = computationString + "PROJECT\t" + projectComputatiion + "\n\n"
        computationString = computationString + "EXAM: " + examComputatiion + "\n\n"
        computationString = computationString + "GRADE: " + adjustedGrade + "\n\n"


        Log.e("COMPUTATION", computationString)
        Log.e("PART", examAdjusted.toString())
        Log.e("PROJ", projectAdjusted.toString())
        Log.e("EXAM", participationAdjusted.toString())
        var equivalentOriginalGrade = GetCSPCEquivalentGrades(originalGrade);
        var equivalentAdjustedGrade = GetCSPCEquivalentGrades(adjustedGrade);
        UpdateGradeStudent(sectionCode, studentNo, gradingPeriod, adjustedGrade, equivalentOriginalGrade, equivalentAdjustedGrade)

        studentRecord =
            studentRecord + "\t" + "" + "\t" + examAdjusted + "\t" + "" + "\t" + equivalentOriginalGrade
        ComputeAverage(sectionCode, studentNo)
        if (ret == "COMPUTATION") return computationString
        else return studentRecord


    }


    fun CheckDropped(sectionCode: String, studentNo: String, adjudted: Int, gradingPeriod: String): String {
        var enrollmentStatus = GetEnrollmentStatus(sectionCode, studentNo, gradingPeriod)
        if (enrollmentStatus == "OK") return adjudted.toString()
        else return "DRP"
    }

    fun ComputeAverage(section: String, studentNo: String) {;
        var first = 0.0
        var second = 0.0
        var average = 0.0
        val school = GetSchool(section)
        if (school == "DEPED") {
            first = GetStudentTermGrade(section, studentNo, "FIRST", "ADJUSTED")
            second = GetStudentTermGrade(section, studentNo, "SECOND", "ADJUSTED")
            var average1 =
                (first + second) / 2 //    Log.e("AVG100", first.toString()  + "  " + second.toString() )
            average = String.format("%.0f", average1).toDouble()
        } else if (school == "BISCAST") {
            first = GetStudentTermGrade(section, studentNo, "FIRST", "DECIMAL")
            second = GetStudentTermGrade(section, studentNo, "SECOND", "DECIMAL")
            average = first * (1 / 3.0) + second * (2 / 3.0)
        } else {
            first = GetStudentTermGrade(section, studentNo, "FIRST", "DECIMAL")
            second = GetStudentTermGrade(section, studentNo, "SECOND", "DECIMAL")
            val first_grade = String.format("%.2f", first * 0.5).toDouble()
            val second_grade = String.format("%.2f", second * 0.5).toDouble()
            average = (first_grade + second_grade)
        }
        UpdateAverage(section, studentNo, average)

    }

    fun DEPEDComputeGrades(sectionCode: String, studentNo: String, gradingPeriod: String, ret: String = ""): String {

        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """
        Log.e("ddd", "100")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        var activityOriginal = 0.0;
        var activityAdjusted = 0.0;
        var activityTotalItem = 0

        var quizOriginal = 0.0;
        var quizAdjusted = 0.0;
        var quizTotalItem = 0

        var examOriginal = 0.0
        var examAdjusted = 0.0
        var examItem = 0.0


        var activityString = ""
        var quizString = "\t"

        var strItem = "\t\t"
        var strActCode = "\t\t"


        var exam = 0;
        Log.e("@@@ ", cursor.count.toString())
        var studentRecord = ""

        var first = true

        var actCount = 0;
        var quizCount = 0
        Log.e("ddd", "120")


        if (cursor.moveToFirst()) {

            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var score = GetStudentScore(activityCode, studentNo, "SCORE")
                var adjusted = GetStudentScore(activityCode, studentNo, "ADJUSTED")

                Log.e("ddd", "155")

                strActCode = strActCode + activityCode + "\t\t"
                strItem = strItem + item + "\t\t"

                if (category == "PT") {
                    activityOriginal = activityOriginal + score
                    activityAdjusted = activityAdjusted + adjusted
                    activityTotalItem = activityTotalItem + item
                    Log.e("PT", score.toString() + "  " + item + "" + activityTotalItem)
                    activityString = activityString + adjusted + "\t"
                    actCount++;
                } else if (category == "QUIZ") {
                    quizOriginal = quizOriginal + score
                    quizAdjusted = quizAdjusted + adjusted
                    quizTotalItem = quizTotalItem + item
                    Log.e("QUIZ", score.toString() + "  " + item + "  " + quizTotalItem)
                    quizString = quizString + adjusted + "\t"
                    quizCount++;


                } else if (category == "EXAM") {
                    examOriginal = score.toDouble()
                    examAdjusted = adjusted.toDouble()
                    examItem = item.toDouble()


                    Log.e("EXAM", score.toString() + "  " + item + "  " + quizTotalItem) //    quizString = quizString + adjusted + "\t"


                }


            } while (cursor.moveToNext())
        }
        Log.e("ddd", "121")


        for (i in actCount + 1..10) {
            activityString = activityString + "\t"
        }

        for (i in quizCount + 1..10) {
            quizString = quizString + "\t"
        }


        var averageOriginalActivity = activityOriginal / activityTotalItem * 100
        var averageOriginalQuiz = quizOriginal / quizTotalItem * 100
        var originalExam = examOriginal / examItem * 100
        var originalGrade =
            (originalExam * 0.2) + (averageOriginalActivity * 0.6) + (averageOriginalQuiz * 0.2)

        var averageAdjustedActivity = activityAdjusted / activityTotalItem * 100
        var averageAdjustedQuiz = quizAdjusted / quizTotalItem * 100
        var adjustedExam = examAdjusted / examItem * 100

        val adjustedGrade =
            (averageAdjustedActivity * 0.6) + (averageAdjustedQuiz * 0.2) + (adjustedExam * 0.2)

        Log.e("QUIZ", studentNo + " " + quizTotalItem + " " + quizOriginal + "  " + quizAdjusted + "  " + averageOriginalQuiz + " " + averageAdjustedQuiz)
        Log.e("PT", studentNo + " " + activityTotalItem + " " + activityOriginal + "  " + activityAdjusted + "  " + averageOriginalActivity + "  " + averageAdjustedActivity)
        Log.e("EXAM", studentNo + " " + examItem + " " + examOriginal + "  " + examAdjusted + "  " + originalExam + "  " + adjustedExam)
        var equivalentOriginalGrade = ConvertDepedGrade(TwoDecimalPlaces(originalGrade))
        var equivalentadjustedGrade = ConvertDepedGrade(TwoDecimalPlaces(adjustedGrade))

        Log.e("ddd", quizCount.toString() + "   " + actCount)
        var str =
            quizString + quizAdjusted + "\t" + TwoDecimalPlaces(averageAdjustedQuiz) + "\t" + TwoDecimalPlaces(averageAdjustedQuiz * .2) + "\t\t"
        str =
            str + activityString + activityAdjusted + "\t" + TwoDecimalPlaces(averageAdjustedActivity) + "\t" + TwoDecimalPlaces(averageAdjustedActivity * .6)
        str = str + "\t\t\t\t" + TwoDecimalPlaces(adjustedGrade) + "\t" + equivalentadjustedGrade
        UpdateGradeStudent(sectionCode, studentNo, gradingPeriod, adjustedGrade, equivalentOriginalGrade, equivalentadjustedGrade)
        ComputeAverage(sectionCode, studentNo)

        //        studentRecord =
        //            studentRecord + "\t" + classStandingDec + "\t" + examAdjusted + "\t" + adjustedGradeDec + "\t" + equivalentOriginalGrade
        //        return  studentRecord;

        Log.e("sss", str);
        return str
    }

    fun TwoDecimalPlaces(num: Double): Double {
        return String.format("%.2f", num).toDouble()
    }

    //        ++++++++++++++++++++++++++++++++++++
    //            var averageActivity = activityOriginal / activityCount
    //            var averageQuiz = quizOriginal / quizCount
    //            var originalGrade = (averageActivity * 0.25) + (averageQuiz * 0.25) + (examOriginal * 0.5)
    //
    //            averageActivity = activityAdjusted / activityCount
    //            averageQuiz = quizAdjusted / quizCount
    //            val adjustedGrade =
    //                (averageActivity * 0.25) + (averageQuiz * 0.25) + (examAdjusted * 0.5)
    //
    //            Log.e("Midgrade", originalGrade.toString())
    //            UpdateGradeStudent(sectionCode, studentNo, gradingPeriod, originalGrade.toDouble(), adjustedGrade)
    // } //  }

    //}


    fun UpdateAdjustedScore(sectionCode: String, studentNo: String, gradingPeriod: String, adjustedScoe: Int) {

        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """
        Log.e("SSS", sql)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)


        var exam = 0;
        Log.e("@@@ ", cursor.count.toString())
        var studentRecord = ""

        var first = true
        var activityString = ""


        if (cursor.moveToFirst()) {

            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var score = GetStudentScore(activityCode, studentNo, "SCORE")
                Log.e("XXX", score.toString() + "  " + adjustedScoe + "  " + item)
                if (score + adjustedScoe <= item) {
                    sql = """update tbScore 
                            set AdjustedScore  ='$adjustedScoe'
                            where SectionCode  ='$sectionCode'
                            and StudentNo   ='$studentNo'
                            and  ActivityCode   ='$activityCode'
                            """

                } else {
                    val temp = item - score
                    sql = """update tbScore
                            set AdjustedScore  ='$temp'
                            where SectionCode  ='$sectionCode'
                            and StudentNo   ='$studentNo'
                            and  ActivityCode   ='$activityCode'
                            """
                }
                Log.e("UPDATE", sql)

                val db = this.writableDatabase
                db.execSQL(sql)


            } while (cursor.moveToNext())
        }
    }


    fun FileTransfer(sectionCode: String, gradingPeriod: String): String {

        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC                          """
        var sql2 = ""
        if (GetSchool(sectionCode) == "DEPED") sql2 =
            "SELECT  * FROM tbstudent_query where Section ='$sectionCode'  order by gender DESC,  lastName"
        else sql2 =
            "SELECT  * FROM tbstudent_query where Section ='$sectionCode'  order by lastName"


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        val db2 = this.readableDatabase
        var cursor2: Cursor? = null
        cursor2 = db2.rawQuery(sql2, null)

        var activityOriginal = 0.0;
        var activityAdjusted = 0.0;
        var quizOriginal = 0.0;
        var quizAdjusted = 0.0;
        var quizCount = 0.0;
        var activityCount = 0
        var examOriginal = -0.0
        var examAdjusted = -0.0


        var strItem = ""
        var strActCode = ""
        var strRecord = ""
        strRecord = "\t \n"
        if (GetSchool(sectionCode) == "DEPED") strRecord =
            strRecord + DEPEDHeading(sectionCode, gradingPeriod)
        else strRecord = strRecord + CollegeHeading(sectionCode, gradingPeriod)
        Log.e("xxxx", strRecord)
        var prevGender = "MALE"
        if (cursor2.moveToFirst()) {
            do {
                var firstName = cursor2.getString(cursor2.getColumnIndex("FirstName"))
                var lasttName = cursor2.getString(cursor2.getColumnIndex("LastName"))
                var studentNo = cursor2.getString(cursor2.getColumnIndex("StudentNo"))
                var gender = cursor2.getString(cursor2.getColumnIndex("Gender"))
                strItem = "\t\t"
                strActCode = "\t\t"
                var studentRecord = studentNo + "\t" + lasttName + "," + firstName
                val str = GradeComputation(sectionCode, studentNo, gradingPeriod)

                Log.e("D100", "$prevGender,  $gender " + GetSchool(sectionCode))
                if (prevGender != gender && GetSchool(sectionCode) == "DEPED") {
                    strRecord = strRecord + "\t \t \n\n"
                    Log.e("D100", "YESSSS")
                    Log.e("D100", strRecord)

                }

                strRecord = strRecord + studentRecord + str + "\n"


                prevGender = gender //                if (cursor.moveToFirst()) {
                //                    do {
                //
                //                        var category = cursor.getString(cursor.getColumnIndex("Category"))
                //                        var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                //                        var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                //                        var score = GetStudentScore(activityCode, studentNo, "SCORE")
                //                        var adjusted = GetStudentScore(activityCode, studentNo, "ADJUSTED")
                //
                //                        strActCode = strActCode + activityCode + "\t\t"
                //                        strItem = strItem + item + "\t\t"
                //
                //
                //
                //                        if (category == "PT") {
                //                            var activityGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                //                            activityAdjusted = activityAdjusted + activityGrade
                //                            activityCount++;
                //                            var grades = String.format("%.2f", activityGrade).toDouble()
                //                            studentRecord = studentRecord + "\t" + adjusted + "\t" + grades
                //
                //                        } else if (category == "Quiz") {
                //                            var quizGrade = (adjusted.toDouble() / item.toDouble()) * 50 + 50
                //                            quizAdjusted = quizAdjusted + quizGrade
                //                            var grades = String.format("%.2f", quizGrade).toDouble()
                //                            quizCount++;
                //                            studentRecord = studentRecord + "\t" + adjusted + "\t" + grades
                //                        } else {
                //                            examAdjusted =
                //                                (adjusted.toDouble() / item.toDouble()) * 50 + 50 // Log.e("", category + " " + item + "  "  + "   "+ activityCode + "  "+ score+ " " + examGrade)
                //                            studentRecord = studentRecord + "\t" +  adjusted + "\t" + examAdjusted
                //                        } //
                //                        //                    var grade= cursor.getString(cursor.getColumnIndex("First")).toInt()
                //
                //
                //                    } while (cursor.moveToNext())

                //                    var averageActivity = activityOriginal / activityCount
                //                    var averageQuiz = quizOriginal / quizCount
                //                    var originalGrade =
                //                        (averageActivity * 0.25) + (averageQuiz * 0.25) + (examOriginal * 0.5)
                //
                //                    averageActivity = activityAdjusted / activityCount
                //                    averageQuiz = quizAdjusted / quizCount
                //                    val classStanding = (averageActivity * 0.50) + (averageQuiz * 0.50)
                //                    val adjustedGrade = (averageActivity * 0.25) + (averageQuiz * 0.25) + (examAdjusted * 0.5)
                //                    Log.e("Midgrade", originalGrade.toString())
                //                    UpdateGradeStudent(sectionCode, studentNo, gradingPeriod, originalGrade.toDouble(), adjustedGrade)
                //                    var equivalentOriginalGrade = GetCSPCEquivalentGrades(adjustedGrade);
                //                    var classStandingDec = String.format("%.2f", classStanding).toDouble()
                //                    var adjustedGradeDec = String.format("%.2f", adjustedGrade).toDouble()
                //                    studentRecord =
                //                        studentRecord + "\t" + classStandingDec + "\t" + examAdjusted + "\t" + adjustedGradeDec + "\t" + equivalentOriginalGrade
                //                    strRecord = strRecord + studentRecord + "\n"

                // } //  }
            } while (cursor2.moveToNext())
            strRecord = strActCode + "\n" + strItem + "\n" + strRecord
            Log.e("ALL", strRecord)
        }
        return strRecord
    }


    //1111

    fun ActivityExported(sectionCode: String, gradingPeriod: String): String {
        var sql: String = """ SELECT  * FROM tbactivity
                                where SectionCode  ='$sectionCode'  
                                and GradingPeriod   ='$gradingPeriod'
                                order by category DESC
                          """
        val oneData = JSONObject()
        val twoData = JSONObject()
        val threeData = JSONObject()

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var actCount = 1;
        var quizCount = 1;


        oneData.put("Name", "Description")
        oneData.put("section", sectionCode)
        oneData.put("term", gradingPeriod)
        twoData.put("Name", "Item")
        threeData.put("Name", "ActivityCode")
        var heading = ""
        if (cursor.moveToFirst()) {
            do {
                var category = cursor.getString(cursor.getColumnIndex("Category"))
                var item = cursor.getString(cursor.getColumnIndex("Item")).toInt();
                var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                var desc = cursor.getString(cursor.getColumnIndex("Description"))
                Log.e("DEPEDHeading", category + "  " + activityCode + "  " + desc)

                if (category == "PT") {
                    if (actCount == 1) {
                        heading = "act1"
                    }
                    if (actCount == 2) {
                        heading = "act2"
                    }
                    if (actCount == 3) {
                        heading = "act3"
                    }
                    if (actCount == 4) {
                        heading = "act4"
                    }

                    if (actCount == 5) {
                        heading = "act5"
                    }
                    actCount++

                } else if (category == "QUIZ") {
                    if (quizCount == 1) {
                        heading = "quiz1"
                    }
                    if (quizCount == 2) {
                        heading = "quiz2"
                    }
                    if (quizCount == 3) {
                        heading = "quiz3"
                    }

                    if (quizCount == 4) {
                        heading = "quiz3"
                    }

                    if (quizCount == 4) {
                        heading = "quiz4"
                    }
                    if (quizCount == 5) {
                        heading = "quiz5"
                    }


                    quizCount++;


                } else if (category == "EXAM") {
                    heading = "exam"
                }


                Log.e("34567", heading + "  " + quizCount + "    " + actCount)

                oneData.put(heading, desc)
                twoData.put(heading, item)
                threeData.put(heading, activityCode)


            } while (cursor.moveToNext())


        }
        var activityData = JSONArray()
        activityData.put(oneData)
        activityData.put(twoData)
        activityData.put(threeData)

        sql = """ SELECT  * FROM tbstudent_query
                                where Section  ='$sectionCode'  
                                order by LastName
                          """
        var cursor2: Cursor? = null
        cursor2 = db.rawQuery(sql, null)

        if (cursor2.moveToFirst()) {
            do {
                var firstName = cursor2.getString(cursor2.getColumnIndex("FirstName"))
                var lastName = cursor2.getString(cursor2.getColumnIndex("LastName"))
                var studentNo = cursor2.getString(cursor2.getColumnIndex("StudentNo"))
                val studData = JSONObject()
                studData.put("Name", lastName + "," + firstName)

                actCount = 1;
                quizCount = 1;


                if (cursor.moveToFirst()) {
                    do {

                        var activityCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
                        var category = cursor.getString(cursor.getColumnIndex("Category"))
                        var score = GetNewStudentScore(studentNo, activityCode)

                        if (category == "PT") {
                            if (actCount == 1) {
                                heading = "act1"

                            }
                            if (actCount == 2) {
                                heading = "act2"

                            }
                            if (actCount == 3) {
                                heading = "act3"

                            }

                              if (actCount == 4) {
                            heading = "act4"
                        }

                            if (actCount == 5) {
                                heading = "act5"
                            }



                            actCount++

                        } else if (category == "QUIZ") {
                            if (quizCount == 1) {
                                heading = "quiz1"

                            }
                            if (quizCount == 2) {
                                heading = "quiz2"

                            }
                            if (quizCount == 3) {
                                heading = "quiz3"

                            }

                            if (quizCount == 4) {
                                heading = "quiz4"

                            }

                            if (quizCount == 5) {
                                heading = "quiz5"

                            }
                            quizCount++;


                        } else if (category == "EXAM") {
                            heading = "exam"

                        }

                        studData.put(heading, score)

                        Log.e("36705", category + "   " + heading + "   " + quizCount + "  " + actCount)
                        Log.e("36705", studData.toString())
                    } while (cursor.moveToNext())
                    activityData.put(studData)
                }
            } while (cursor2.moveToNext())
        }

        Log.e("36709", activityData.toString())
        return activityData.toString()
    }

    fun GetNewStudentScore(studentNo: String, activityCode: String): String {
        var sql = """ SELECT  * FROM tbscore
                                where ActivityCode  ='$activityCode'  
                                and StudentNo  ='$studentNo'  
                             
                          """


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {

            var score = cursor.getString(cursor.getColumnIndex("Score")).toInt()
            var adj = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
            return (score + adj).toString()
        }
        return ""

    }


    @SuppressLint("Range")
    fun GradeExported(sectionCode: String): String {

        var sql: String = """ SELECT  * FROM tbgrade_query
                                where SectionCode  ='$sectionCode' 
                                 order by LastName"""
        val db2 = this.readableDatabase
        var cursor2: Cursor? = null
        cursor2 = db2.rawQuery(sql, null)

        var gradeList = JSONArray()


        //    playerIds.put(notification)
        //    Log.e("4589-1", notification.toString())
        //
        //
        //
        //    playerIds.put(notification1)
        //    Log.e("4589-2", notification1.toString())
        //
        //
        //    Log.e("4589-3", playerIds.toString())

        if (cursor2.moveToFirst()) {
            do {
                val school = GetSchool(sectionCode)
                val grade = JSONObject()

                var firstname = cursor2.getString(cursor2.getColumnIndex("FirstName"))
                var lastname = cursor2.getString(cursor2.getColumnIndex("LastName"))
                var studentNo =
                    cursor2.getString(cursor2.getColumnIndex("StudentNo")) //studentRecord =studentRecord +  studentNo + "\t" + lasttName + "," + firstName+ "\t"

                var firstGrade = 0.0
                var secondGrade = 0.0
                var cumulativeGrade = 0.0


                if (school == "DEPED") {
                    firstGrade =
                        cursor2.getString(cursor2.getColumnIndex("FirstEquivalent")).toDouble()
                    secondGrade =
                        cursor2.getString(cursor2.getColumnIndex("SecondEquivalent")).toDouble()
                    cumulativeGrade =
                        cursor2.getString(cursor2.getColumnIndex("CumulativeGrade")).toDouble()
                } else {
                    firstGrade = cursor2.getString(cursor2.getColumnIndex("FirstGrade")).toDouble()
                    secondGrade =
                        cursor2.getString(cursor2.getColumnIndex("SecondGrade")).toDouble()
                    cumulativeGrade =
                        cursor2.getString(cursor2.getColumnIndex("CumulativeGrade")).toDouble()

                }

                grade.put("firstName", firstname)
                grade.put("lastName", lastname)
                grade.put("studentNo", studentNo)
                grade.put("firstGrade", firstGrade)
                grade.put("secondGrade", secondGrade)
                grade.put("cumulativeGrade", cumulativeGrade)
                gradeList.put(grade)
            } while (cursor2.moveToNext())


        }
        Log.e("4589", gradeList.toString())
        return gradeList.toString()
    }


    fun GetGradeListforCopy(sectionCode: String): String {
        val school = GetSchool(sectionCode)
        Log.e("123", "345")
        var sql2 = ""
        if (school == "DEPED") sql2 =
            "SELECT  * FROM tbgrade_query  where SectionCode ='$sectionCode'  order  by gender DESC,  lastName"
        else sql2 =
            "SELECT  * FROM tbgrade_query  where SectionCode ='$sectionCode'  order  by lastName"

        Log.e("123", sql2)

        val db2 = this.readableDatabase
        var cursor2: Cursor? = null
        cursor2 = db2.rawQuery(sql2, null)
        var studentRecord = ""
        Log.e("xxx", "@@@")
        if (cursor2.moveToFirst()) {
            do {
                var firstName = cursor2.getString(cursor2.getColumnIndex("FirstName"))
                var lasttName = cursor2.getString(cursor2.getColumnIndex("LastName"))
                var studentNo = cursor2.getString(cursor2.getColumnIndex("StudentNo"))
                studentRecord =
                    studentRecord + studentNo + "\t" + lasttName + "," + firstName + "\t"
                if (school == "DEPED") {
                    studentRecord =
                        studentRecord + cursor2.getString(cursor2.getColumnIndex("FirstEquivalent")) + "\t"
                    studentRecord =
                        studentRecord + cursor2.getString(cursor2.getColumnIndex("SecondEquivalent")) + "\t"
                } else {
                    studentRecord =
                        studentRecord + cursor2.getString(cursor2.getColumnIndex("FirstGrade")) + "\t"
                    studentRecord =
                        studentRecord + cursor2.getString(cursor2.getColumnIndex("SecondGrade,")) + "\t"
                }


                studentRecord =
                    studentRecord + cursor2.getString(cursor2.getColumnIndex("CumulativeGrade")) + "\t"
                studentRecord =
                    studentRecord + cursor2.getString(cursor2.getColumnIndex("CumulativeGradeEquivalent")) + "\t"

                studentRecord = studentRecord + "\n"
                Log.e("xxx", studentRecord)
            } while (cursor2.moveToNext())


        }
        Log.e("xxx290", studentRecord)
        return studentRecord
    }


    fun DataToGoogleSheet(data: String) {
        val url =
            "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec"
        val stringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener {

        }, Response.ErrorListener {

        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["section"] = "BSIT-2B"
                params["data"] = "Hello"
                return params;
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }


    //CREATE TABLE tbgrades (SectionCode  text, StudentNo  text, FirstGrade Integer, FirstEquivalent,SecondGrade Integer, SecondEquivalent text, CumulativeGrade text, Remark text, PRIMARY KEY (SectionCode ,  StudentNo ));

    fun UpdateGradeStudent(sectionCode: String, studentNo: String, gradingPeriod: String, adjustedGrade: Double, equivalentOriginalGrade: Double, equivalentAdjustedGrade: Double) {
        var grades = String.format("%.2f", adjustedGrade).toDouble()

        Log.e("ROUND", adjustedGrade.toString() + "  " + grades)
        var sql = ""
        if (gradingPeriod == "FIRST") {
            sql = """update tbgrades
              set FirstGrade  ='$grades',
                  FirstEquivalent  ='$equivalentAdjustedGrade',
                  FirstOriginalGrade  ='$equivalentOriginalGrade'
              where SectionCode  ='$sectionCode'
              and StudentNo   ='$studentNo'
              """
        } else if (gradingPeriod == "SECOND") {
            sql = """update tbgrades
              set SecondGrade  ='$grades',
                  SecondEquivalent  ='$equivalentAdjustedGrade',
                  SecondOriginalGrade  ='$equivalentOriginalGrade'
              where SectionCode  ='$sectionCode'
              and StudentNo   ='$studentNo'
              """
        }
        Log.e("UPdate", sql)
        val db = this.writableDatabase
        db.execSQL(sql)

    }

    fun UpdateAverage(sectionCode: String, studentNo: String, averageGrade: Double) {
        var equivalentAverageGrade = GetCSPCEquivalentGrades(averageGrade);
        var grades = String.format("%.2f", equivalentAverageGrade).toDouble()

        var sql = """update tbgrades
              set CumulativeGrade  ='$averageGrade',
                  CumulativeGradeEquivalent  ='$equivalentAverageGrade'
              where SectionCode  ='$sectionCode'
              and StudentNo   ='$studentNo'
              """

        val db = this.writableDatabase
        db.execSQL(sql)

    }

    fun UpdateVersion(sectionCode: String) {

        var sql = """update tbsection
              set version   = version +1
              where SectionName  ='$sectionCode'
              """
        Log.e("qqq", sql)
        val db = this.writableDatabase
        db.execSQL(sql)
    }

    fun UpdateEnrollmentStatus(sectionCode: String, studentNo: String, status: String, gradingPeriod: String) {
        var sql = ""
        if (gradingPeriod == "FIRST") {
            sql = """update tbgrades
                            set MidtermStatus = "$status"
                            where SectionCode  ='$sectionCode'
                            and StudentNo  ='$studentNo'
                            """
        } else if (gradingPeriod == "SECOND") {
            sql = """update tbgrades
                            set FinalStatus = "$status"
                            where SectionCode ='$sectionCode'
                            and StudentNo  ='$studentNo'
                            """
        }
        val db = this.writableDatabase
        Log.e("SQL2323", sql)
        db.execSQL(sql)
    }

    fun GetEnrollmentStatus(sectionCode: String, studentNo: String, gradingPeriod: String): String {
        var sql = ""
        sql = """select * from tbgrades
                            where SectionCode  ='$sectionCode'
                            and StudentNo  ='$studentNo'
                            """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            if (gradingPeriod == "FIRST") return cursor.getString(cursor.getColumnIndex("MidtermStatus"))
            else if (gradingPeriod == "SECOND") return cursor.getString(cursor.getColumnIndex("FinalStatus"))
        }
        return ""
    }

    fun GetVersion(sectionCode: String): Int {

        var sql = """select * from  tbsection
              where SectionName  ='$sectionCode'
              """

        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("Version")).toInt()

        }
        return 0;
    }


    fun GetCSPCEquivalentGrades(grade: Double): Double {
        if (grade >= 99) return 1.0;
        else if (grade >= 98 && grade < 99) return 1.1;
        else if (grade >= 97 && grade < 98) return 1.2;
        else if (grade >= 96 && grade < 97) return 1.25;
        else if (grade >= 95 && grade < 96) return 1.3;
        else if (grade >= 94 && grade < 95) return 1.4;
        else if (grade >= 93 && grade < 94) return 1.5;
        else if (grade >= 92 && grade < 93) return 1.6;
        else if (grade >= 91 && grade < 92) return 1.7;
        else if (grade >= 90 && grade < 91) return 1.75;
        else if (grade >= 89 && grade < 90) return 1.8;
        else if (grade >= 88 && grade < 89) return 1.9;
        else if (grade >= 87 && grade < 88) return 2.0;
        else if (grade >= 86 && grade < 87) return 2.1;
        else if (grade >= 85 && grade < 86) return 2.2;
        else if (grade >= 84 && grade < 85) return 2.25;
        else if (grade >= 83 && grade < 84) return 2.3;
        else if (grade >= 82 && grade < 83) return 2.4;
        else if (grade >= 81 && grade < 82) return 2.5;
        else if (grade >= 80 && grade < 81) return 2.6;
        else if (grade >= 79 && grade < 80) return 2.7;
        else if (grade >= 78 && grade < 79) return 2.75;
        else if (grade >= 77 && grade < 78) return 2.8;
        else if (grade >= 76 && grade < 77) return 2.9;
        else if (grade >= 75 && grade < 76) return 3.0;
        else return 4.0;


    }

    fun GetStudentScore(activityCode: String, studentNo: String, stat: String): Int {
        var sql: String = """ SELECT  * FROM tbscore
                                where ActivityCode   ='$activityCode'  
                                and StudentNo     ='$studentNo' 
                          """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var score = 0
        var adjusted = 0
        if (cursor.moveToFirst()) {
            score = cursor.getString(cursor.getColumnIndex("Score")).toInt()
            adjusted = cursor.getString(cursor.getColumnIndex("AdjustedScore")).toInt()
        }

        if (stat == "SCORE") return score.toInt();
        else return score + adjusted;
    }


    fun ImportGradeData() {
        var sql: String = """ SELECT  * FROM tbstudent
                          """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var score = ""
        if (cursor.moveToFirst()) {
            do {
                var studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))
                var sectionCode = cursor.getString(cursor.getColumnIndex("Section"))
                val db2 = this.writableDatabase
                var sql2 = """
						INSERT INTO TBGRADES VALUES ('$sectionCode', '$studentNo','0',0,'0',0,0,'-')"""
                db2.execSQL(sql2);
            } while (cursor.moveToNext())

        }
    }


    fun UpdateGrade(sectionCode: String, studentNo: String, gradingPeriod: String, realGrade: Int) {

        var sql = """
            update tbgrades set  first ='$realGrade'
            where   SectionCode ='$sectionCode'
            and  StudentNo ='$studentNo'
        """.trimIndent()
        val db = this.writableDatabase
        Log.e("sql", sql)
        db.execSQL(sql)
    }

    fun GetStudentTermGrade(sectionCode: String, studentNo: String, gradingPeriod: String, type: String): Double {
        var sql = """
            select * from  tbgrades
            where   SectionCode ='$sectionCode'
            and  StudentNo ='$studentNo'
        """.trimIndent()
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var score = ""
        if (cursor.moveToFirst()) {
            if (gradingPeriod == "FIRST" && type == "ORIGINAL") return cursor.getString(cursor.getColumnIndex("FirstOriginalGrade"))
                .toDouble()
            else if (gradingPeriod == "FIRST" && type == "ADJUSTED") return cursor.getString(cursor.getColumnIndex("FirstEquivalent"))
                .toDouble()
            else if (gradingPeriod == "FIRST" && type == "DECIMAL") return cursor.getString(cursor.getColumnIndex("FirstGrade"))
                .toDouble()
            else if (gradingPeriod == "SECOND" && type == "ORIGINAL") return cursor.getString(cursor.getColumnIndex("SecondOriginalGrade"))
                .toDouble()
            else if (gradingPeriod == "SECOND" && type == "ADJUSTED") return cursor.getString(cursor.getColumnIndex("SecondEquivalent"))
                .toDouble()
            else if (gradingPeriod == "SECOND" && type == "DECIMAL") return cursor.getString(cursor.getColumnIndex("SecondGrade"))
                .toDouble()
            else if (gradingPeriod == "CUMULATIVE") return cursor.getString(cursor.getColumnIndex("CumulativeGrade"))
                .toDouble()
        }
        return 0.0
    }


    fun DisplayLogGrade(sectionCode: String, gradingPeriod: String) {

        var sql: String = """ SELECT  * FROM tbgrades
                                where SectionCode  ='$sectionCode'  """
        Log.e("sql2", sql)
        try {

            val db = this.readableDatabase
            var cursor: Cursor? = null
            cursor = db.rawQuery(sql, null)

            // StudentNo	SectionCode	First	Second	Third	Fourth	Average	Remark
            if (cursor.moveToFirst()) {
                do {
                    var sn = cursor.getString(cursor.getColumnIndex("StudentNo"))
                    var grade = cursor.getString(cursor.getColumnIndex("First")).toInt()
                    Log.e("grade", sn + " " + grade)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("err", e.toString())

        }
    }


    fun GetStudentTotal(sectionCode: String, studentNo: String, category: String, gradingPeriod: String): Int {
        val sql = """
               select sum(score) as totalScore from tbscore 
               where studentno ='$studentNo' 
               and activitycode in (select activitycode 
               from tbactivity 
                where category ='$category' and 
               sectioncode='$sectionCode' 
               and gradingperiod='$gradingPeriod')              
          """.trimIndent()
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        var totalScore = 0
        if (cursor.moveToFirst()) totalScore =
            cursor.getString(cursor.getColumnIndex("totalScore")).toInt()
        return totalScore
    }

    fun GetItemTotal(sectionCode: String, category: String, gradingPeriod: String): Int {
        try {
            val sql = """
               select sum(item) as totalItem from tbactivity 
                where category ='$category' and 
               sectioncode='$sectionCode' 
               and gradingperiod='$gradingPeriod'            
          """.trimIndent()


            val db = this.readableDatabase
            var cursor: Cursor? = null
            cursor = db.rawQuery(sql, null)
            var totalItem = 0
            if (cursor.moveToFirst()) totalItem =
                cursor.getString(cursor.getColumnIndex("totalItem")).toInt()
            return totalItem
        } catch (e: Exception) {
            Log.e("err", e.toString())
            return 0;
        }
    }


    fun ConvertDepedGrade(grade: Double): Double {
        if (grade == 100.0) return 100.0;
        else if (grade >= 98.40 && grade <= 99.99) return 99.0
        else if (grade >= 96.80 && grade <= 98.39) return 98.0;
        else if (grade >= 95.20 && grade <= 96.79) return 97.0
        else if (grade >= 93.60 && grade <= 95.19) return 96.0
        else if (grade >= 92.00 && grade <= 93.59) return 95.0
        else if (grade >= 90.40 && grade <= 91.99) return 94.0
        else if (grade >= 88.80 && grade <= 90.39) return 93.0
        else if (grade >= 87.20 && grade <= 88.79) return 92.0
        else if (grade >= 85.60 && grade <= 87.19) return 91.0
        else if (grade >= 84.00 && grade <= 85.59) return 90.0
        else if (grade >= 82.40 && grade <= 83.99) return 89.0
        else if (grade >= 80.80 && grade <= 82.39) return 88.0
        else if (grade >= 79.20 && grade <= 80.79) return 87.0
        else if (grade >= 77.60 && grade <= 79.19) return 86.0
        else if (grade >= 76.00 && grade <= 77.599) return 85.0
        else if (grade >= 74.40 && grade <= 75.999) return 84.0
        else if (grade >= 72.80 && grade <= 74.399) return 83.0
        else if (grade >= 71.20 && grade <= 72.799) return 82.0
        else if (grade >= 69.60 && grade <= 71.199) return 81.0
        else if (grade >= 68.00 && grade <= 69.599) return 80.0
        else if (grade >= 66.40 && grade <= 67.999) return 79.0
        else if (grade >= 64.80 && grade <= 66.399) return 78.0
        else if (grade >= 63.20 && grade <= 64.799) return 77.0
        else if (grade >= 61.60 && grade <= 63.199) return 76.0
        else if (grade >= 60.00 && grade <= 61.599) return 75.0
        else if (grade >= 56.00 && grade <= 59.99) return 74.0;
        else if (grade >= 52.00 && grade <= 55.99) return 73.0;
        else if (grade >= 48.00 && grade <= 51.99) return 72.0;
        else if (grade >= 44.00 && grade <= 47.99) return 71.0;
        else if (grade >= 40.00 && grade <= 43.99) return 70.0;
        else if (grade >= 36.00 && grade <= 39.99) return 69.0;
        else if (grade >= 32.00 && grade <= 35.99) return 68.0;
        else if (grade >= 28.00 && grade <= 31.99) return 67.0;
        else if (grade >= 24.00 && grade <= 27.99) return 66.0;
        else if (grade >= 20.00 && grade <= 23.99) return 65.0;
        else if (grade >= 16.00 && grade <= 19.99) return 64.0;
        else if (grade >= 12.00 && grade <= 15.99) return 63.0;
        else if (grade >= 8.00 && grade <= 11.99) return 62.0;
        else if (grade >= 4.00 && grade <= 7.99) return 61.0
        else if (grade >= 0.00 && grade <= 3.99) return 61.0
        else return 60.0
    }

    fun GetSchool(section: String): String {

        val sql = """
               select * from tbSection
                where  sectionName ='$section'  
          """.trimIndent()


        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) return cursor.getString(cursor.getColumnIndex("School"))
        else return ""
    }

    fun GetGradeList(sql: String): ArrayList<GradeModel> {
        val gradeList: ArrayList<GradeModel> = ArrayList<GradeModel>()


        val db = this.readableDatabase
        var cursor: Cursor? = null

        cursor = db.rawQuery(sql, null)

        // StudentNo	FirstName	LastName	GrpNumber	Section	Gender
        if (cursor.moveToFirst()) {
            do {
                var studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))
                var firstName = cursor.getString(cursor.getColumnIndex("FirstName"))
                var lastName = cursor.getString(cursor.getColumnIndex("LastName"))
                var section = cursor.getString(cursor.getColumnIndex("SectionCode"))
                var firstGrade = cursor.getString(cursor.getColumnIndex("FirstGrade")).toDouble()
                var firstEquivalent =
                    cursor.getString(cursor.getColumnIndex("FirstEquivalent")).toDouble()
                var firstOriginalGrade =
                    cursor.getString(cursor.getColumnIndex("FirstOriginalGrade")).toDouble()
                var secondGrade = cursor.getString(cursor.getColumnIndex("SecondGrade")).toDouble()
                var secondEquivalent =
                    cursor.getString(cursor.getColumnIndex("SecondEquivalent")).toDouble()
                var secondOriginalGrade =
                    cursor.getString(cursor.getColumnIndex("SecondOriginalGrade")).toDouble()
                var cumulativeGrade =
                    cursor.getString(cursor.getColumnIndex("CumulativeGrade")).toDouble()
                var cumulativeGradeEquivalent =
                    cursor.getString(cursor.getColumnIndex("CumulativeGradeEquivalent")).toDouble()
                var remark = cursor.getString(cursor.getColumnIndex("Remark"))
                var midtermStatus = cursor.getString(cursor.getColumnIndex("MidtermStatus"))
                var finalStatus = cursor.getString(cursor.getColumnIndex("FinalStatus"))
                var gender = cursor.getString(cursor.getColumnIndex("Gender"))
                val record =
                    GradeModel(section, studentNo, firstName, lastName, firstGrade, firstEquivalent, firstOriginalGrade, secondGrade, secondEquivalent, secondOriginalGrade, cumulativeGrade, cumulativeGradeEquivalent, remark, midtermStatus, finalStatus, gender)
                gradeList.add(record)

                //

            } while (cursor.moveToNext())
        }
        return gradeList
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

    fun GradeCount(gradingPeriod: String, min: Int, max: Double, sectionCode: String): Int {

        var gradingPeriodField = gradingPeriod + "Grade"
        var gradingPeriodEquivalent = gradingPeriod + "Equivalent"
        var averageStatus = GetAverageStatus()
        var sql = ""

        if (gradingPeriod == "AVG") averageStatus = "TRUE"

        if (averageStatus == "TRUE") {
            sql = """ select count(*) as scorecount from tbgrades
            where CumulativeGrade >= $min
            and CumulativeGrade <= $max
            and sectioncode = '$sectionCode'
            """.trimIndent()
        } else {
            val school = GetSchool(sectionCode)
            if (school == "DEPED") {
                sql = """
            select count(*) as scorecount from tbgrades
            where $gradingPeriodEquivalent >= $min
            and $gradingPeriodEquivalent <= $max
            and sectioncode = '$sectionCode'
        """.trimIndent()

            } else {
                sql = """
            select count(*) as scorecount from tbgrades
            where $gradingPeriodField >= $min
            and $gradingPeriodField <= $max
            and sectioncode = '$sectionCode'
        """.trimIndent()
            }
        }
        var cursor: Cursor? = null

        Log.e("Grades1582", sql)
        val db = this.readableDatabase
        cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        var grCount = cursor.getString(cursor.getColumnIndex("scorecount")).toInt()
        return grCount


    }

    fun GeneralGradeCount(gradingPeriod: String, min: Int, max: Double, school: String): Int {

        var gradingPeriodField = gradingPeriod + "Grade"
        var gradingPeriodEquivalent = gradingPeriod + "Equivalent"
        var averageStatus = GetAverageStatus()
        var sql = ""

        if (gradingPeriod == "AVG") averageStatus = "TRUE"

        if (averageStatus == "TRUE") {
            sql = """ select count(*) as scorecount from tbgrades
            where CumulativeGrade >= $min
            and CumulativeGrade <= $max
            and sectioncode IN (select sectionName from tbsection_query where school='$school' and status ='SHOW')
            """.trimIndent()
        } else {

            if (school == "DEPED") {
                sql = """
            select count(*) as scorecount from tbgrades
            where $gradingPeriodEquivalent >= $min
            and $gradingPeriodEquivalent <= $max
           and sectioncode IN (select sectionName from tbsection_query where school='$school' and status ='SHOW')

        """.trimIndent()

            } else {
                sql = """
            select count(*) as scorecount from tbgrades
            where $gradingPeriodField >= $min
            and $gradingPeriodField <= $max
            and sectioncode IN (select sectionName from tbsection_query where school='$school' and status ='SHOW')

        """.trimIndent()
            }
        }
        var cursor: Cursor? = null

        Log.e("Grades1582", sql)
        val db = this.readableDatabase
        cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        var grCount = cursor.getString(cursor.getColumnIndex("scorecount")).toInt()
        return grCount


    }


    fun DeleteQuery() {
        val sql = "DROP  VIEW tbgrade_query"
        val db = this.writableDatabase
        db.execSQL(sql)
    }


}