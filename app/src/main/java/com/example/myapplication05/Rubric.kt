package com.example.myapplication05


//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.res.ColorStateList
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
////import android.text.method.ScrollingMovementMethod
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.WindowManager
//import android.widget.*
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.ActivityCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.volley.*
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.chart.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.group_main.*
import kotlinx.android.synthetic.main.rubrics.view.*
import kotlinx.android.synthetic.main.score_row.view.*
import kotlinx.android.synthetic.main.task_main.*
import kotlin.collections.ArrayList


class Rubric : AppCompatActivity() {
    var webView: WebView? = null
    var activityList = arrayListOf<ActivityModel>()
    var driveActivityCode = ""


    companion object {
        var alert: AlertDialog? = null
        var rubricList = arrayListOf<RubricModel>()
        var rubricAdapter: RubricAdapter? = null;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rubrics) //
    }

    fun DisplayRenark(v: View, context: Context, section: String, activityCode: String) {

        val db: DatabaseHandler = DatabaseHandler(context)

        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        v.listRubric.layoutManager = layoutmanager
        rubricAdapter = RubricAdapter(context, rubricList)
        v.listRubric.adapter = rubricAdapter

        var actRubricCode = db.GetSectionActivityRubric(section, activityCode);
        Log.e("82", actRubricCode)
        if (actRubricCode == "") {
            var rubricCode = NavigateRubric(context, "FIRST")
            ppp(context, rubricCode)
            v.txtRubrivCode.setText(rubricCode)
            Rubric.rubricAdapter!!.notifyDataSetChanged()
        } else {
            ppp(context, actRubricCode)
            v.txtRubrivCode.setText(actRubricCode)
            Rubric.rubricAdapter!!.notifyDataSetChanged()
        }

        v.btnRubricAdd.setOnClickListener {
            var code = db.AddNewRubric()
            ppp(context, code)
            v.txtRubrivCode.setText(code)
            Rubric.rubricAdapter!!.notifyDataSetChanged()
        }

        v.btnRubricDeLete.setOnClickListener {
            db.DeleteRubric("RUB-01")
        }

        v.btnRubricFirst.setOnClickListener {
            var rubricCode = NavigateRubric(context, "FIRST")
            Log.e("AAA", rubricCode)
            ppp(context, rubricCode)
            v.txtRubrivCode.setText(rubricCode)
            Rubric.rubricAdapter!!.notifyDataSetChanged()
        }

        v.btnRubricLast.setOnClickListener {
            var rubricCode = NavigateRubric(context, "LAST")
            ppp(context, rubricCode)
            v.txtRubrivCode.setText(rubricCode)
            Rubric.rubricAdapter!!.notifyDataSetChanged()
        }

        v.btnRubricNext.setOnClickListener {
            var code = v.txtRubrivCode.text

            var rubricCode = NavigateRubric(context, "NEXT", code.toString())
            if (rubricCode != "") {
                ppp(context, rubricCode)
                v.txtRubrivCode.setText(rubricCode)
                Rubric.rubricAdapter!!.notifyDataSetChanged()
            }
        }

        v.btnRubricPrev.setOnClickListener {
            var code = v.txtRubrivCode.text
            var rubricCode = NavigateRubric(context, "PREV", code.toString())
            if (rubricCode != "") {

                ppp(context, rubricCode)
                v.txtRubrivCode.setText(rubricCode)
                Rubric.rubricAdapter!!.notifyDataSetChanged()
            }
        }

        v.btnRubricAssign.setOnClickListener {
            var rubricCode = v.txtRubrivCode.text.toString()
            var actRubricCode = db.AssignActivityRubric (section, activityCode, rubricCode);
        }


    }


    fun NavigateRubric(c: Context, navigation: String, rubricCode: String = ""): String {
        val db: DatabaseHandler = DatabaseHandler(c)
        var rubricCodeList = arrayListOf<String>()
        rubricCodeList = db.NavigateRubric()
        var count = rubricCodeList.count()
        Log.e("AAA", count.toString())
        if (navigation == "FIRST") return rubricCodeList[0]
        else if (navigation == "LAST") return rubricCodeList[count - 1]
        else {
            var i = 0
            while (i < count) {
                Log.e("AA11", rubricCode + "   " + rubricCodeList[i] + "   " + navigation + "  " + i.toString() + " " + count)
                if (rubricCode == rubricCodeList[i] && navigation == "NEXT" && i < count - 1) {
                    return rubricCodeList[i + 1]

                } else if (rubricCode == rubricCodeList[i] && navigation == "PREV") {
                    if (i == 0) {
                        Log.e("Hello", "Hello")
                        return ""
                    } else {
                        return rubricCodeList[i - 1]
                    }
                }
                i++;
            }
        }
        return ""
    }


    fun ppp(context: Context, rubricCode: String) {
        val db: DatabaseHandler = DatabaseHandler(context)
        rubricList.clear()
        val criteria: List<RubricModel>

        criteria = db.GetRubricCrireria(rubricCode)


        for (e in criteria) {
            rubricList.add(RubricModel(e.RubricCode, e.RubricNUm, e.Description, e.Points))
        }
    }

}

fun DatabaseHandler.AddNewRubric(): String {
    var rubricCode = GetRubricCode()
    Log.e("RRR", rubricCode)
    var x = 1;
    while (x <= 5) {
        var sql = """
            insert into tbRubric(RubricCode, RubricNUm,Description,Points)
            values('$rubricCode', '$x', '-', 5)
    """
        Log.e("sss", sql)
        val db = this.writableDatabase
        db.execSQL(sql)
        x++

    }
    return rubricCode
}


fun DatabaseHandler.DeleteRubric(rubricCode: String) {

    val sql = """
                    DELETE  FROM tbrubric
                    WHERE RubricCode='$rubricCode' 
                    """
    Log.e("sss", sql)
    val db = this.writableDatabase
    db.execSQL(sql)


}


fun DatabaseHandler.GetRubricCode(): String {
    val db = this.readableDatabase
    val sql = """
                    SELECT DISTINCT(RubricCode) FROM tbrubric
                    ORDER BY RubricCode DESC
                    """


    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        var actCode = cursor.getString(cursor.getColumnIndex("RubricCode"))
        var num = actCode.takeLast(2).toInt() + 1 // Grade>>>
        Util.Msgbox(context, num.toString())
        return "RUB-" + Util.ZeroPad(num, 2)
    } else { // Util.Msgbox(context, "ACT-01" )
        return "RUB-01"
    } //  return  "helo"

}


fun DatabaseHandler.GetRubricCrireria(rubricCode: String): ArrayList<RubricModel> {
    val rubricList: ArrayList<RubricModel> = ArrayList<RubricModel>()

    val sql = """
                    SELECT * FROM tbrubric
                    WHERE RubricCode='$rubricCode' 
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
            var code = cursor.getString(cursor.getColumnIndex("RubricCode"))
            var num = cursor.getString(cursor.getColumnIndex("RubricNUm")).toInt()
            var description = cursor.getString(cursor.getColumnIndex("Description"))
            var point = cursor.getString(cursor.getColumnIndex("Points")).toInt()
            val att = RubricModel(code, num, description, point)
            rubricList.add(att)
        } while (cursor.moveToNext())
    }

    return rubricList
}


fun DatabaseHandler.NavigateRubric(): ArrayList<String> {
    val db = this.readableDatabase
    val sql = """
                    SELECT DISTINCT(RubricCode) FROM tbrubric
                    ORDER BY RubricCode 
                    """
    var rubricCodeList = arrayListOf<String>()
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        do {
            var code = cursor.getString(cursor.getColumnIndex("RubricCode"))
            rubricCodeList.add(code)
        } while (cursor.moveToNext())
    }
    return rubricCodeList
}


fun DatabaseHandler.GetSectionActivityRubric(section: String, activityCode: String): String {
    val db = this.readableDatabase
    val sql = """
                    SELECT *FROM tbactivity 
                    WHERE ActivityCode ='$activityCode'
                    AND SectionCode ='$section'
                    """
    Log.e("389", sql)
    var rubricCodeList = arrayListOf<String>()
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("RubricCode"))
    } else {
        return "-"
    }
}


fun DatabaseHandler.AssignActivityRubric(section: String, activityCode: String, rubricCode: String): String {
    val db = this.readableDatabase
    val sql = """
                    UPDATE tbactivity 
                    SET   RubricCode = '$rubricCode'                  
                    WHERE ActivityCode ='$activityCode'
                    AND SectionCode ='$section'
                    """
    Log.e("389", sql)
    var rubricCodeList = arrayListOf<String>()
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("RubricCode"))
    } else {
        return "-"
    }
}







