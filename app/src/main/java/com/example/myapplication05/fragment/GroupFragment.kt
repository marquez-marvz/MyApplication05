package com.example.myapplication05.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.*
import kotlinx.android.synthetic.main.fragment_folder_group.view.*
import kotlinx.android.synthetic.main.fragment_group.view.*
import kotlinx.android.synthetic.main.fragment_group.view.listGroup
import kotlinx.android.synthetic.main.fragment_picture.view.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import kotlinx.android.synthetic.main.group_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*

class GroupFragment : Fragment() {

    var viewgroup: View? = null
    var studentList = arrayListOf<EnrolleModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        viewgroup = inflater.inflate(R.layout.fragment_group, container, false)
        var ccc = container!!.context

        containerGlobal = container
        SetSpinnerAdapter()
        SetDefaultSection()
        currentSection = viewgroup!!.cboGroupSection.getSelectedItem().toString();


        ShowGroupList(ccc, currentSection)
        SetUpGroupAdapter()
        LoadStudents("GROUP_SEARCH", "NONE")

        ShowMember1(ccc, currentSection, "ALL")
        SetUpMemberAdapter()

        Log.e("GROUP", "GROUP 1111")

        viewgroup!!.btnLoadAll.setOnClickListener {
            LoadStudents("LAST_ORDER", "")
        }

        viewgroup!!.btnGroupMode.setOnClickListener {
            if (viewgroup!!.btnGroupMode.text == "MEMBER DETAIL") {
                viewgroup!!.btnGroupMode.setText("GROUP DETAIL")
                viewgroup!!.listMember.isVisible = false
                val params: ViewGroup.LayoutParams = viewgroup!!.listGroup.getLayoutParams()
                params.width = 1500
                viewgroup!!.listGroup.setLayoutParams(params)

            } else {
                viewgroup!!.btnGroupMode.setText("MEMBER DETAIL")
                viewgroup!!.listMember.isVisible = true
                val params: ViewGroup.LayoutParams = viewgroup!!.listGroup.getLayoutParams()
                params.width = 300
                viewgroup!!.listGroup.setLayoutParams(params)

            }
        }


        viewgroup!!.btnAddNewMember.setOnClickListener {
            Log.e("CG", currentGroup)

            var currentStudent = viewgroup!!.cboStudent.getSelectedItem().toString()
            for (e in studentList) {
                var stud = e.lastname + "," + e.firstname
                if (currentStudent == stud) {
                    val db2: DatabaseHandler = DatabaseHandler(ccc)
                    db2.UpdateStudentGroup(e.studentno, currentGroup, db2.GetCurrentSection());
                    ShowMember1(ccc, currentSection, currentGroup)
                    grpMemberAdapter!!.notifyDataSetChanged()
                    LoadStudents("GROUP_SEARCH", "NONE")
                    break;
                }
            }
        }



        viewgroup!!.btnAddGroupNew.setOnClickListener {
            val dlgsection =
                LayoutInflater.from(requireContext()).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(dlgsection)
                .setTitle("Do you want to add group d ")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //                dlgsection.txtSectionCode.setText(db.GetNewSectionCode())
            //                dlgsection.txtSectionCode.isEnabled= false

            dlgsection.btnYes.setOnClickListener { //
                val db2: DatabaseHandler = DatabaseHandler(ccc)
                db2.AddGroup(db2.GetCurrentSection())
                ShowGroupList(ccc, currentSection)
                grpAdapter!!.notifyDataSetChanged()
                inputDialog.dismiss()
            }

            dlgsection.btnNo.setOnClickListener { //
                inputDialog.dismiss()
            }

        }


        viewgroup!!.cboGroupSection.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { //
                    val section = viewgroup!!.cboGroupSection.getSelectedItem().toString();
                    Util.CURRENT_SECTION = section;
                    val mydb: DatabaseHandler = DatabaseHandler(ccc)
                    mydb.SetCurrentSection(section)
                    currentSection = mydb.GetCurrentSection()
                    ShowGroupList(ccc, currentSection)
                    grpAdapter!!.notifyDataSetChanged()
                    ShowMember1(ccc, mydb.GetCurrentSection(), "ALL")
                    grpMemberAdapter!!.notifyDataSetChanged() //                ScoreFragment.LoadActivity()
                    ScoreFragment.btnScoreSection!!.setText(GroupFragment.currentSection)
                    ScoreFragment.ShowGroupList(ccc, GroupFragment.currentSection)
                    ScoreFragment.grpScoreAdapter!!.notifyDataSetChanged()
                    ScoreFragment.grpMemberAdapter!!.notifyDataSetChanged()


                    ScoreFragment.viewgroup!!.txtGroupScore.setText("")
                    PictureFragment.viewpicture!!.btnSectionGroupPic.setText(currentSection)
                    FolderGroup.viewfolder!!.btnScoreFolder.setText(currentSection)

                }
            }

        return viewgroup;

    }

    private fun SetSpinnerAdapter() {
        var context = containerGlobal!!.context
        val arrSection: Array<String> = Util.GetSectionList(context)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewgroup!!.cboGroupSection.setAdapter(sectionAdapter);

    }

    private fun SetDefaultSection() {
        var context = containerGlobal!!.context
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(context)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, context)
        viewgroup!!.cboGroupSection.setSelection(index)

    }

    fun SetUpGroupAdapter() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewgroup!!.listGroup.layoutManager = layoutmanager
        grpAdapter = GroupAdapter(context, grpList)
        viewgroup!!.listGroup.adapter = grpAdapter
    }

    fun SetUpMemberAdapter() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewgroup!!.listMember.layoutManager = layoutmanager
        grpMemberAdapter = GroupMemberAdapter(context, grpMemberList)
        viewgroup!!.listMember.adapter = grpMemberAdapter
    }

    fun ShowGroupList(section: String) {
        var context = containerGlobal!!.context
        val db2: DatabaseHandler = DatabaseHandler(context)
        grpList.clear()
        Log.e("TTT", section)
        var grp: List<GrpModel>
        grp = db2.GetGroupList(section)
        Log.e("TTT", grp.size.toString())
        for (e in grp) {
            grpList.add(GrpModel(e.grpnumber, e.grpnumber, e.num))
        }
    }

    fun LoadStudents(category: String, search: String) {
        var context = containerGlobal!!.context
        val section = viewgroup!!.cboGroupSection.getSelectedItem().toString();
        val db2: DatabaseHandler = DatabaseHandler(context)
        studentList = db2.GetEnrolleList(category, section, search) //
        var studentArray = Array(studentList.size + 1) { "" }
        var x = 1;
        studentArray[0] = "Select"
        for (e in studentList) {
            studentArray[x] = e.lastname + "," + e.firstname
            x++;
        }
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, studentArray)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewgroup!!.cboStudent.setAdapter(sectionAdapter);
    }



    companion object {
        var containerGlobal: ViewGroup? = null
        var grpAdapter: GroupAdapter? = null;
        var grpList = arrayListOf<GrpModel>()
        var currentSection = ""
        var grpMemberList = arrayListOf<GrpMemberModel>()
        var currentGroup: String = ""
        var grpMemberAdapter: GroupMemberAdapter? = null;

        fun ShowGroupList(context: Context, section: String) {
            var context = containerGlobal!!.context
            val db2: DatabaseHandler = DatabaseHandler(context)
            grpList.clear()
            Log.e("TTT", section)
            var grp: List<GrpModel>
            grp = db2.GetGroupList(section)
            Log.e("TTT", grp.size.toString())
            for (e in grp) {
                grpList.add(GrpModel(e.grpnumber, e.grpnumber, e.num))
            }

        }

        fun ShowMember1(context: Context, section: String, grpNumber: String) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            grpMemberList.clear()

            var member: List<GrpMemberModel>
            member = db2.GetGroupMember(section, grpNumber)

            Log.e("member", member.size.toString())

            for (e in member) {
                grpMemberList.add(GrpMemberModel(e.firstname, e.lastnanme, e.studentNo, e.grp, 0, "", 0))
            }
        } // //        fun ShowGroupList(section: String) { //            val db2: DatabaseHandler = DatabaseHandler(this)

    }
}





