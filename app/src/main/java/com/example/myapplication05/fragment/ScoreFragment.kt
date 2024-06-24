package com.example.myapplication05.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import kotlinx.android.synthetic.main.group_main.*


class ScoreFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("SCORE200", "GROUP 222")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        Log.e("PIC200", "HEloo12222222")
        viewgroup = inflater.inflate(R.layout.fragment_score, container, false)
        btnScoreSection = viewgroup!!.findViewById(R.id.btnScoreSection) as Button
        varcboActivity = viewgroup!!.findViewById(R.id.cboScoreActivity) as Spinner
        containerGlobal = container
        var ccc = container!!.context
         currentSection = GroupFragment.currentSection
        Log.e("1234", currentSection)
        ShowGroupList(ccc, currentSection)
        SetUpGroupAdapter()
        ShowMemberScore(ccc, currentSection, "ALL")
        SetUpMemberAdapter()
        LoadActivity()




        viewgroup!!.cboScoreActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var context = containerGlobal!!.context
                GRP_DESCRIPTION = viewgroup!!.cboScoreActivity.getSelectedItem().toString();
                val db2: DatabaseHandler = DatabaseHandler(context)
                ShowMemberScore(context, GroupFragment.currentSection, "ALL")
                Log.e("xx53", "OKOK")
                grpMemberAdapter!!.notifyDataSetChanged()
            }
        }

        viewgroup!!.btnSetGrade.setOnClickListener {

            for (i in 0..grpMemberList.size - 1) {
                grpMemberList[i].score = txtGroupScore.text.toString().toInt()
                grpMemberList[i].saveVisibility = "YES"
            }
            grpMemberAdapter!!.notifyDataSetChanged()
        }


        return viewgroup
    }



    fun SetUpGroupAdapter() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewgroup!!.listGroup.layoutManager = layoutmanager
        grpScoreAdapter = GroupAdapter(context, grpScoreList)
        viewgroup!!.listGroup.adapter = grpScoreAdapter
    }
    fun SetUpMemberAdapter() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewgroup!!.listMemberScore.layoutManager = layoutmanager
        grpMemberAdapter = GroupMemberAdapter(context, grpMemberList)
        viewgroup!!.listMemberScore.adapter = grpMemberAdapter
    }




    companion object {
        var btnScoreSection: Button? = null
        var varcboActivity: Spinner? = null
        var grpScoreList = arrayListOf<GrpModel>()
        var grpScoreAdapter: GroupAdapter? = null;
        var containerGlobal: ViewGroup? = null
        var grpMemberAdapter: GroupMemberAdapter? = null;
        var grpMemberList = arrayListOf<GrpMemberModel>()
        var activityList = arrayListOf<ActivityModel>()
        var currentSection = ""
        var currentGroup =""
        var viewgroup: View? = null
        var GRP_DESCRIPTION = ""
        var GRP_ACT_CODE = ""

        fun ShowGroupList(context: Context, section: String) {
            var context = containerGlobal!!.context
            val db2: DatabaseHandler = DatabaseHandler(context)
            grpScoreList.clear()
            Log.e("TTT", section)
            var grp: List<GrpModel>
            grp = db2.GetGroupList(section)
            Log.e("T100", grp.size.toString())
            for (e in grp) {
                Log.e("119", e.folderLink)
                grpScoreList.add(GrpModel(e.grpnumber , GroupFragment.currentGroup, e.num, "NO" , e.folderLink))
            }
        }


        fun ShowMemberScore(context: Context, section: String, grpNumber: String) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            grpMemberList.clear()

            var member: List<GrpMemberModel>
            member = db2.GetGroupMember(section, grpNumber)

            var activityCode = ""
            for (e in activityList) {
                if (GRP_DESCRIPTION == e.description) {
                    activityCode = e.activityCode
                    GRP_ACT_CODE = e.activityCode
                    break;
                }
            }


            for (e in member) {
                var stud= db2.GetActivityRecord(activityCode, section, e.studentNo)
                grpMemberList.add(GrpMemberModel(e.firstname, e.lastnanme, e.studentNo, e.grp, stud.score, stud.remark, stud.adjusted))
            }
        }

        fun LoadActivity() {
            var context = containerGlobal!!.context
            val section = GroupFragment.currentSection
            Log.e("@@150", section)
            val db: TableActivity = TableActivity(context)
            activityList = db.GetActivityList(section, db.GetDefaultGradingPeriod())

            var activityArray = Array(activityList.size + 1) { "" }
            var x = 1;
            activityArray[0] ="Select"
            for (e in activityList) {
                activityArray[x] = e.description
                x++;
            }
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.util_spinner, activityArray)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewgroup!!.cboScoreActivity.setAdapter(sectionAdapter);

        }

    }
}
