package com.example.myapplication05

import android.app.ProgressDialog
import android.content.ClipData
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.EditText
import android.widget.Toast
import android.content.ClipboardManager
import android.content.Context.*
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.graphics.Color.red
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.Student.GetOriginalSection
import kotlinx.android.synthetic.main.student_main.*
import java.time.Period
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.getSystemService as getSystemService1
import java.util.Calendar as Calendar1


class Util(context: Context) : DatabaseHandler(context) {
    companion object {
        var CURRENT_DATE: String = "";
        var CURRENT_SECTION: String = "";
        var ATT_CURRENT_DATE: String = "";
        var ATT_CURRENT_SECTION: String = ""
        var ATT_CURRENT_DAY: String = ""
        var ATT_CURRENT_REMARK: String = ""
        var ATT_CURRENT_MONTH: String = ""
        var ATT_FLAG: String = ""



        var CURRENT_SCHOOLYEAR: String = ""
        var CURRENT_SEMESTER: String = ""



        var ATT_CURRENT_AMPM: String = ""
        var ACT_CODE: String = "";
        var ACT_CURRENT_SECTION: String = ""
        var ACT_DESCRIPTION: String = ""
        var ACT_CATEGORY: String = ""
        var ACT_ITEM: Int= 0
        var HVIEW: Boolean = false;

        var KEY_CODE: String = ""
        var KEY_DESCRIPTION: String = ""


        var MISC_CURRENT_SECTION: String = ""
        var MISC_DESCRIPTION: String = ""
        var MISC_CODE: String = ""
        var txtgrade: EditText? = null;

        var GRADE_INDIVIDUAL: String = ""
        var GRADE_STUD_NO: String = ""
        var GRADE_NAME: String = ""


        var FOLDER_LINK = ""
        var FOLDER_NAME= ""
        var FOLDER_SECTION= ""
        var FOLDER_STUDNUM= ""
        var FOLDER_ACT_DESCRIPTION =""


        var CHART_TITLE=""
        var CHART_REMARK=""
        var CHART_ACTCODE=""
        var CHART_STATUS = true;
        var ACT_OPEN_FOLDER = false;
        var ACT_RUBRIC= false;
        var CHART_CHOICE = ""


        var GRADE_SECTION = ""
        var GRADE_SEARCH = ""
        var GRADE_CHART = false

        var GENERALMODE = ""

        var TAB_GRP_POSITION = 0;
        var TAB_TEST_POSITION = 0;

        var ENROLLE_FLAG = ""

        fun Msgbox(context: Context?, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();


        }





        fun DatabaseUpGrade(context: Context) {
            Log.e("xxx", "Hello200")
            val db: DatabaseHandler = DatabaseHandler(context)

            // db.getWritableDatabase()
        }





        fun ZeroPad(num: Int, digit: Int): String {
            var str = num.toString().padStart(digit, '0')
            return str;
        }

        fun ShowTableField(context: Context, tableName: String) {
            val db: DatabaseHandler = DatabaseHandler(context)
            db.ShowField(tableName)
        }

        fun GetSectionIndex(search: String, context: Context): Int {
            val db: DatabaseHandler = DatabaseHandler(context)
            val arrSection: Array<String> =Util.GetSectionList(context)
            val index = arrSection.indexOf(search)
            return index
        }

        fun GetOriginalSectionIndex(search: String, context: Context): Int {
            val db: DatabaseHandler = DatabaseHandler(context)
            val arrSection: ArrayList<String> = db.GetOriginalSection()
            val index = arrSection.indexOf(search)
            return index
        }

        fun GetSectionList(context: Context): Array<String> {
            val db: DatabaseHandler = DatabaseHandler(context)
            val myList: List<SectionModel> = db.GetSectionList(Util.CURRENT_SCHOOLYEAR, Util.CURRENT_SEMESTER)

            var sectionArray = Array(myList.size) { "" }
            for (x in 0..myList.size - 1) {
                Log.e("SSS", myList[x].sectionName)
                sectionArray[x] = myList[x].sectionName
            }

            return sectionArray
        }



        fun GetSectionListNoSubject(context: Context): Array<String> {
            val db: DatabaseHandler = DatabaseHandler(context)
            val myList: List<SectionModel> = db.GetSectionList("SHOW", "")

            var sectionArray = Array(myList.size) { "" }
            for (x in 0..myList.size - 1) {
                sectionArray[x] = myList[x].sectionName
            }

            return sectionArray
        }


        fun GetSectionListActivity(context: Context, section:String) :Array<String> {
            val db: DatabaseHandler = DatabaseHandler(context)
            val gradingPeriod = GetCurrentGradingPeriod(context);
            val myList: List<ActivityModel> = db.GetSectionActivityList(section, gradingPeriod)

            var activityArray = Array(myList.size) { "" }
            for (x in 0..myList.size - 1) {
                activityArray[x] = myList[x].description
            }

            return activityArray
        }

        fun CopyText(c:Context,  copyString: String, copymsg:String="") {
            val clipboardManager = c.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", copyString)
            clipboardManager.setPrimaryClip(clipData)
           
        }

        fun GetArrayGroup(): Array<String> {
            val arrGroup =
                arrayOf("ALL","NONE","G-A", "G-B", "G-C", "G-D", "G-E", "G-F")
            return arrGroup
        }

        fun GetCurrentGradingPeriod(context: Context): String {
            val db: TableActivity = TableActivity(context)
            return db.GetDefaultGradingPeriod();
        }


        fun ExportToGoogleSheet(context:Context,  sheetName: String,data:String,heading:String, action:String ) {
            val loading = ProgressDialog.show(context, heading, "Please wait")




            var url =
                "https://script.google.com/macros/s/AKfycbwzu_JQAorVx8H59GS8AUceYvR4zITE42FFejiohyoOPe9aG0-7ofxqdqUhGIpTryz9Hg/exec"


            val stringRequest: StringRequest = object :

                StringRequest(Method.POST, url, Response.Listener { response ->
                    loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
                    //                    val intent = Intent(applicationContext, MainActivity::class.java)
                    //                    startActivity(intent)

                    Log.e("@@@", response)
                    Log.e("@@@", "Hwllo po")

                    var t = "\"" + "Section Not Found" + "\""
                    var q = "\"" + "Sheet is Missing" + "\""


                    Log.e("123", response + "  " + t)

                    if (response.toString() == t.toString()) {
                        Util.Msgbox(context, "Section Not Found");
                    } else if (response.toString() == q.toString()) {
                        Util.Msgbox(context, "Sheet is Missing");
                    } else {
                        Util.Msgbox(context, "OK");
                    }


                }, Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    val parmas: MutableMap<String, String> = HashMap()
                    Log.e("@@@20", action)
                    Log.e("@@@20", sheetName)
                    //here we pass params
                    parmas["action"] = action
                    parmas["data"] = data
                    parmas["sheetName"] = sheetName
                    return parmas
                }
            }

            val socketTimeOut = 50000 // u can change this .. here it is 50 seconds


            val retryPolicy: RetryPolicy =
                DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringRequest.retryPolicy = retryPolicy

            val queue = Volley.newRequestQueue(context)

            queue.add(stringRequest)
        }
    }




    //    fun SetCurrentSection(section: String) {
//        var sql = "update tbinfo set CurrentSection ='$section'"
//        val db = this.writableDatabase
//        db.execSQL(sql)
//


    }
