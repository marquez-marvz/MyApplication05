
package com.example.myapplication05

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication05.fragment.FolderGroup
import com.example.myapplication05.fragment.GroupFragment
import com.example.myapplication05.fragment.GroupTabAdapter
import com.example.myapplication05.fragment.PictureFragment
import com.example.myapplication05.fragment.ScoreFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.sampletab.theTab
import kotlinx.android.synthetic.main.sampletab.viewPager


class Grouping : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sampletab)
        var context = this
        setUpTabs()
        Util.TAB_GRP_POSITION = 0

        theTab.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                Log.e("POSITION", position.toString())
                Util.TAB_GRP_POSITION = position
                if (position == 0) {
                    GroupFragment.grpAdapter!!.notifyDataSetChanged()
                    GroupFragment.grpMemberAdapter!!.notifyDataSetChanged()
                } else if (position == 1) {

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var x = ""
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                var y = ""
            }
        })

    }

    private fun setUpTabs() {
        viewPager.setOffscreenPageLimit(4)
        val adapter = GroupTabAdapter(supportFragmentManager)
        adapter.addFragment(GroupFragment(), "Group")
        adapter.addFragment(PictureFragment(), "Picture")
        adapter.addFragment(ScoreFragment(), "Score")
        adapter.addFragment(FolderGroup(), "Folder")
        //        adapter.addFragment((), "Folder")

        viewPager.adapter = adapter

        theTab.setupWithViewPager(viewPager)
    }
}


fun DatabaseHandler.AddGroup(sectionName: String) {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql = ""
    sql = """
            SELECT *
            FROM tbgroup WHERE SectionCode= '$sectionName' order by GroupNumber DESC
    """.trimIndent()
    Log.e("SQLSS", sql)

    val db = this.readableDatabase
    var cursor = db.rawQuery(sql, null)
    var grp = ""
    if (cursor.moveToFirst()) {
        var actCode = cursor.getString(cursor.getColumnIndex("GroupNumber"))
        var num = actCode.takeLast(2).toInt() + 1
        grp = "GRP-" + Util.ZeroPad(num, 2)

        Log.e("SQL200", "111")


    } else {
        grp = "GRP-01";


    }

    sql = """
             insert into tbgroup  (GroupNumber, 	SectionCode, FolderLink)
             values('$grp', '$sectionName', "-")
            """

    var sq2 = "delete from tbgroup"
    val db2 = this.writableDatabase
    db.execSQL(sql)


}


fun DatabaseHandler.GetGroupList(sectionName: String): ArrayList<GrpModel> {
    val grpList: ArrayList<GrpModel> = ArrayList<GrpModel>()
    var sql = """
            SELECT *
            FROM tbgroup WHERE SectionCode= '$sectionName' order by GroupNumber 
    """.trimIndent()
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    var x = 0;
    val att = GrpModel("NONE", sectionName, 0, "-", "")
    grpList.add(att)
    if (cursor.moveToFirst()) {
        do { //            , 	SectionCode
            var grpnumber = cursor.getString(cursor.getColumnIndex("GroupNumber"))
            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
             var folderLink = cursor.getString(cursor.getColumnIndex("FolderLink"))
            Log.e("LINK", grpnumber + "    " + sectionCode + "   " + folderLink)
            val att = GrpModel(grpnumber, sectionCode, x, "-", folderLink)
            grpList.add(att)
            x++;
        } while (cursor.moveToNext())
    }
    return grpList
}


fun DatabaseHandler.UpdateStudentGroup(studentNo: String, currentGroup: String, section: String) {

    var sql = """
             update  tbenroll set GrpNumber= '$currentGroup'	
             where section = '$section' 
             and StudentNo = '$studentNo'
            """ //    StudentNo	StudentID	GivenNamre	SurName	GrpNumber	Section	EnrollmentStatus

    val db2 = this.writableDatabase
    db2.execSQL(sql)
    Log.e("ADD GRP", sql)

}


fun DatabaseHandler.GetStudentScore(studentNo: String, activityCode: String, section: String) {

    //    var sql = """
    //             select tbenroll set GrpNumber= '$currentGroup'
    //             where section = '$section'
    //             and StudentNo = '$studentNo'
    //            """ //    StudentNo	StudentID	GivenNamre	SurName	GrpNumber	Section	EnrollmentStatus
    //
    //    val db2 = this.writableDatabase
    //    db2.execSQL(sql)
    //    Log.e("ADD GRP", sql)

}


fun DatabaseHandler.GetGroupMember(sectionName: String, grpNumber: String): ArrayList<GrpMemberModel> {
    val grpMember: ArrayList<GrpMemberModel> = ArrayList<GrpMemberModel>()
    var sql = ""
    if (grpNumber == "ALL") {
        sql = """     select * from  tbstudent_query	
             where section = '$sectionName' 
            """
    } else {
        sql = """     select * from  tbstudent_query	
             where section = '$sectionName' 
             and GrpNumber = '$grpNumber'
            """
    }
    Log.e("AAA", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        do {
            var fname = cursor.getString(cursor.getColumnIndex("FirstName"))
            var lname = cursor.getString(cursor.getColumnIndex("LastName"))
            var studentNo = cursor.getString(cursor.getColumnIndex("StudentNo"))
            var grpnumber = cursor.getString(cursor.getColumnIndex("GrpNumber"))
            fname = CapitalizeBeginName(fname.toLowerCase())
            lname =
                CapitalizeBeginName(lname.toLowerCase()) //            var sectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
            val att = GrpMemberModel(fname, lname, studentNo, grpnumber, 0, " ", 0)
            grpMember.add(att)
        } while (cursor.moveToNext())
    }
    return grpMember

}

fun DatabaseHandler.GetActivityRecord(activityCode: String, section: String, studentNo: String): ScoreDrive {
    val grpMember: ArrayList<ScoreModel> = ArrayList<ScoreModel>()
    var sql = ""
    var stud: ScoreDrive = ScoreDrive()

    sql = """     select * from  TBSCORE_QUERY	
             where sectionCode = '$section' 
             and  ActivityCode = '$activityCode' 
             and StudentNo = '$studentNo' 
            """

    Log.e("AAA", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        stud.remark = cursor.getString(cursor.getColumnIndex("Remark"))
        stud.score = cursor.getString(cursor.getColumnIndex("Score")).toInt()
        stud.first = cursor.getString(cursor.getColumnIndex("Criteria1")).toInt()
        stud.second = cursor.getString(cursor.getColumnIndex("Criteria2")).toInt()
        stud.third = cursor.getString(cursor.getColumnIndex("Criteria3")).toInt()
        stud.fourth = cursor.getString(cursor.getColumnIndex("Criteria4")).toInt()
        stud.fifth = cursor.getString(cursor.getColumnIndex("Criteria5")).toInt()
        stud.firstName = cursor.getString(cursor.getColumnIndex("FirstName"))
        stud.lastName = cursor.getString(cursor.getColumnIndex("LastName"))
    }

    //ActivityCode	SectionCode	StudentNo	Score	Remark	SubmissionStatus	AdjustedScore	GradingPeriod

    return stud;


}

fun CapitalizeBeginName(str: String): String {
    val str = str.split(" ").joinToString(separator = " ", transform = String::capitalize)
    return str
}


fun DatabaseHandler.SaveGroupRemark(grp: String, remark: String, sectionName: String) {

    val remarkID = GetNewRemarkID(sectionName)


    var sql = """
             insert into tbgroup_remark  (RemarkID,  GroupNUmber,	SectionCode	,RemarkDate,	RemarkTime,	Remark, RemarkSTatus)
             values('$remarkID','$grp', '$sectionName', '-', '-', '$remark', 'ACTIVE')
            """
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}


fun DatabaseHandler.SaveChangesRemark(grp: String, section: String, remark: String, remarkID: String) {

    var sql = """
             select * from  tbgroup_remark 
             where remarkID= '$remarkID'
            """

    Log.e("sqlREM", sql)
    val db2 = this.readableDatabase
    var cursor2: Cursor? = null
    cursor2 = db2.rawQuery(sql, null)

    if (cursor2.count == 0) {
        sql = """
             insert into tbgroup_remark  (RemarkID,  GroupNUmber,	SectionCode	,RemarkDate,	RemarkTime,	Remark, RemarkSTatus)
             values('$remarkID','$grp', '$section', '-', '-', '$remark', 'ACTIVE')
            """

    } else {

        sql = """
             update tbgroup_remark 
             set Remark = '$remark'
             where remarkID= '$remarkID'
             and  GroupNUmber= '$grp'
            """
    }
    Log.e("123", sql)
    var cursor: Cursor? = null
    val db = this.writableDatabase
    db2.execSQL(sql)
}


fun DatabaseHandler.UpdateRemarkStatus(remarkID: String, stat: String) {

    var sql = """
             update tbgroup_remark 
             set RemarkSTatus = '$stat'
             where RemarkID= '$remarkID'
            """
    Log.e("EDIT", sql)
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}

fun DatabaseHandler.GetGroupRemarkList(sectionName: String, grp: String, stat: String = "ACTIVE ONLY"): ArrayList<GroupRemarkModel> {
    val grpList: ArrayList<GroupRemarkModel> = ArrayList<GroupRemarkModel>()

    var t: GroupRemarkModel =
        GroupRemarkModel() //    RemarkID	GroupNUmber	SectionCode	RemarkDate	RemarkTime	Remark	RemarkSTatus
    var sql = """
            SELECT *
            FROM  tbgroup_remark 
            WHERE SectionCode = '$sectionName' 
            AND  GroupNUmber= '$grp' 
            """
    if (stat == "ACTIVE ONLY") {
        sql = sql + " AND  RemarkSTatus= 'ACTIVE'  order by  RemarkID"
    } else if (stat == "SHOW ALL") {
        sql = sql + " AND  (RemarkSTatus= 'ACTIVE' OR RemarkSTatus= 'HIDE'  order by  RemarkID"
    }

    Log.e("sqlREM", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    var x = 0;
    Log.e("sqlREM", cursor.count.toString())
    if (cursor.moveToFirst()) {
        do { //
            var remark: GroupRemarkModel = GroupRemarkModel()
            remark.SectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
            Log.e("777", cursor.getString(cursor.getColumnIndex("Remark")))
            remark.Remark = cursor.getString(cursor.getColumnIndex("Remark"))
            remark.RemarkDate = cursor.getString(cursor.getColumnIndex("RemarkDate"))
            remark.RemarkTime = cursor.getString(cursor.getColumnIndex("RemarkTime"))
            remark.GroupNUmber = cursor.getString(cursor.getColumnIndex("GroupNUmber"))
            remark.RemarkStatus = cursor.getString(cursor.getColumnIndex("RemarkSTatus"))
            remark.RemarkID = cursor.getString(cursor.getColumnIndex("RemarkID"))
            Log.e("888", remark.Remark)
            grpList.add(remark) //  RemarkID	GroupNUmber	SectionCode	RemarkDate	RemarkTime	Remark	RemarkSTatus
            x++;
        } while (cursor.moveToNext())
    }
    return grpList
}

fun DatabaseHandler.GetNewRemarkID(sectioncode: String): String {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    val sql: String //RemarkID	GroupNUmber	SectionCode	RemarkDate	RemarkTime	Remark	RemarkSTatus
    sql = """
                SELECT * FROM TBGROUP_REMARK 
                WHERE SectionCode='$sectioncode' 
                ORDER BY  RemarkID DESC
                """
    val db = this.readableDatabase

    var sql2 = """
                SELECT * FROM TBSECTION
                WHERE SectionName='$sectioncode' 
                """
    val cursor2 = db.rawQuery(sql2, null)

    var sectioncode = ""
    Log.e("211", sql2)
    Log.e("211", cursor2.count.toString())

    if (cursor2.moveToFirst()) {
        sectioncode = cursor2.getString(cursor2.getColumnIndex("SectionCode"))
    }
    Log.e("211", sectioncode)


    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        var actCode = cursor.getString(cursor.getColumnIndex("RemarkID"))
        var num = actCode.takeLast(3).toInt() + 1 // Grade>>>
        Util.Msgbox(context, num.toString())

        Log.e("211", sectioncode + "-" + Util.ZeroPad(num, 3))
        return sectioncode + "-" + Util.ZeroPad(num, 3)
    } else { // Util.Msgbox(context, "ACT-01" )
        return sectioncode + "-" + "001"
    } //  return  "helo"
}


fun DatabaseHandler.DeleteALlRemark() {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    val sql: String //RemarkID	GroupNUmber	SectionCode	RemarkDate	RemarkTime	Remark	RemarkSTatus
    sql = """
                DELETE FROM TBGROUP_REMARK 
                """
    val db = this.writableDatabase
    db.execSQL(sql)
}


fun DatabaseHandler.UpdateFolderLink() {
    val activityList: ArrayList<ActivityModel> = ArrayList<ActivityModel>()
    val sql: String //RemarkID	GroupNUmber	SectionCode	RemarkDate	RemarkTime	Remark	RemarkSTatus
    sql = """
                UPDATE TBGROUP 
                SET FOLDERLINK='-'
                """
    val db = this.writableDatabase
    db.execSQL(sql)
}


internal class OnTouch : OnTouchListener {
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        Log.e("8907", Util.HVIEW.toString())
        return Util.HVIEW

    }
}








