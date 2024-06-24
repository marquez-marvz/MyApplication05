package com.example.myapplication05.fragment

//class PdfRecordAdapter {}
//
//package com.example.myapplication05

//class OpenPdf_Adapter {}


import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.example.myapplication05.*
import com.example.myapplication05.fragment.GroupFragment
import com.example.myapplication05.fragment.ScoreFragment //import com.example.myapplication05.fragment.PictureFragment
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.fragment_folder_group.view.*
import kotlinx.android.synthetic.main.fragment_picture.view.*
import kotlinx.android.synthetic.main.fragment_picture.view.txtGroupNumber
import kotlinx.android.synthetic.main.fragment_score.view.*
import kotlinx.android.synthetic.main.gdrive.view.*
import kotlinx.android.synthetic.main.group_main.*
import kotlinx.android.synthetic.main.group_row.view.*
import kotlinx.android.synthetic.main.group_student.view.*
import kotlinx.android.synthetic.main.score_main.view.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_folder_shared.view.*
import java.util.HashMap
import kotlin.collections.Grouping


class GroupAdapter(val context: Context, val grp: List<GrpModel>) :
    RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {

    var countGroup = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.group_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        countGroup = grp.size
        return grp.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = grp[position]
        holder.setData(myActivity, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentActivity: GrpModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnGroupNumber.setOnLongClickListener {
                itemView.btnGroupMakeFolder.isVisible =
                    true //                val grp = itemView.btnGroupNumber.text.toString()
                //                val dlgsection = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                //                val mBuilder = AlertDialog.Builder(context).setView(dlgsection)
                //                    .setTitle("Do you want to delete the group " + grp)
                //                val inputDialog = mBuilder.show()
                //                inputDialog.setCanceledOnTouchOutside(false); //                dlgsection.txtSectionCode.setText(db.GetNewSectionCode())
                //                //                dlgsection.txtSectionCode.isEnabled= false
                //
                //                dlgsection.btnYes.setOnClickListener { //
                //                    val db2: DatabaseHandler = DatabaseHandler(context)
                //                    Log.e("SEE", GroupFragment.currentSection)
                //                    db2.DeleteGroup(GroupFragment.currentSection, grp)
                //                    GroupFragment.ShowGroupList(context, GroupFragment.currentSection)
                //                    GroupFragment.grpAdapter!!.notifyDataSetChanged();
                //                    inputDialog.dismiss()
                //                }
                //
                //                dlgsection.btnNo.setOnClickListener { //
                //                    inputDialog.dismiss()
                //                }
                true
            }



            itemView.btnGroupMakeFolder.setOnClickListener {
                val section = currentActivity!!.sectioncode
                val grp = currentActivity!!.grpnumber


                val dlgconfirm =
                    LayoutInflater.from(context).inflate(R.layout.util_folder_shared, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Input Link for  $grp  ?")
                val confirmDialog = mBuilder.show()

                confirmDialog.setCanceledOnTouchOutside(false);
                dlgconfirm.txtFolderLink.setText(currentActivity!!.folderLink) //                var email = db.GetEmail(currentStudent!!.studentID)
                //                dlgconfirm.txtEmail.setText(email.toString())


                dlgconfirm.btnShareFolder.setOnClickListener {
                    val db2: DatabaseHandler = DatabaseHandler(context)
                    var member: List<GrpMemberModel>
                    var emailList = ""
                    member = db2.GetGroupMember(GroupFragment.currentSection, grp)

                    for (e in member) {
                        var email = db2.GetEmailViaStudentNo(e.studentNo)
                        Log.e("email", email)
                        emailList = emailList + email + "\n"
                    }

                    ShareFolder(emailList, dlgconfirm, grp)
                }
            }


            itemView.btnGroupNumber.setOnClickListener {
                Log.e("5621", itemView.btnGroupNumber.text.toString())
                ScoreMain.ScoreUpdateListContent(context, "GROUP", itemView.btnGroupNumber.text.toString())
                ScoreMain.scoreAdapter!!.notifyDataSetChanged()
            }


        } //init

        fun setData(grp: GrpModel?, pos: Int) { //
            itemView.btnGroupNumber.text = grp!!.grpnumber
            Log.e("FFF", grp!!.grpnumber + "   " + grp!!.folderLink)

            Log.e("Hello", Util.TAB_GRP_POSITION.toString()) //            itemView.txtNewRemark.text = "---"
            //            itemView.txtNewRemark.setVisibility(View.INVISIBLE);
            if (grp!!.flag == "YES") {
                itemView.btnGroupNumber.setBackgroundColor(Color.parseColor("#64B5F6"))
            } else {
                itemView.btnGroupNumber.setBackgroundResource(android.R.drawable.btn_default)
            }


            //            itemView.btnGroupMakeFolder.isVisible = false

            this.currentActivity = grp;
            this.currentPosition = pos

        }

        fun ShareFolder(email: String, dlgconfirm: View, groupNumber: String) {
            val db: DatabaseHandler = DatabaseHandler(context)
            Log.e("@@@", "1222")
            Log.e("@@@", "Hwllo po")
            val loading = ProgressDialog.show(context, "", "Please wait")
            var url =
                "https://script.google.com/a/macros/deped.gov.ph/s/AKfycbzNcjfCSnBycZnxI-beLiKzZkmDdkRQzepPyL74t_BlwRDdMJrdvP6wgkublZymj8xP/exec";

            val stringRequest: StringRequest = object :

                StringRequest(Method.POST, url, Response.Listener { response ->
                    loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
                    //                    val intent = Intent(applicationContext, MainActivity::class.java)
                    //                    startActivity(intent)

                    Log.e("@@@", response)
                    Log.e("@@@", "Hwllo po")
                    dlgconfirm.txtFolderLink.setText(response)
                    if (response != "Invalid Email") {
                        db.UpdateGrouFolderLink(GroupFragment.currentSection, groupNumber, response)
                        dlgconfirm.txtFolderLink.setText(response)
                    }


                }, Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    val parmas: MutableMap<String, String> = HashMap()
                    var folderName = groupNumber
                    parmas["action"] = "SharedGroupFolder"
                    parmas["folderName"] = folderName;

                    parmas["sectionFolderLink"] =
                        db.GetSectionFolderLink(GroupFragment.currentSection)
                    parmas["emailAddress"] = email
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
}

fun DatabaseHandler.DeleteGroup(sectionName: String, grpnumber: String) {
    val grpList: ArrayList<GrpModel> = ArrayList<GrpModel>()
    var sql = """
            DELETE  FROM tbgroup 
            WHERE SectionCode= '$sectionName'
            AND  GroupNumber= '$grpnumber'
    """.trimIndent()
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}


fun DatabaseHandler.UpdateGrouFolderLink(sectionName: String, grpnumber: String, link: String) {
    val grpList: ArrayList<GrpModel> = ArrayList<GrpModel>()
    var sql = """
            UPDATE  tbgroup 
            SET FolderLink = "$link"
            WHERE SectionCode= '$sectionName'
            AND  GroupNumber= '$grpnumber'
    """.trimIndent()
    val db2 = this.writableDatabase
    db2.execSQL(sql)
}


/*###################################################################################################
                        GROUP MEMBER ADAPTER
##################################################################################################*/
class GroupMemberAdapter(val context: Context, val grp: List<GrpMemberModel>) :
    RecyclerView.Adapter<GroupMemberAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.group_student, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return grp.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myActivity = grp[position]
        holder.setData(myActivity, position)
        Log.e("Invoke", "Invoke")
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentActivity: GrpMemberModel? = null
        var currentPosition: Int = 0

        init {
            itemView.rowBtnEdit.setOnClickListener {
                itemView.rowBtnSave.setVisibility(View.VISIBLE);
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
            }

            itemView.rowBtnSave.setOnClickListener { //    itemView.rowBtnSave.setVisibility(View.VISIBLE);
                var e = currentActivity
                val db: TableActivity = TableActivity(context)
                val score = itemView.txtIndScore.text.toString()
                    .toInt() //  db.UpdateStudentRecord(ScoreFragment.GRP_ACT_CODE, GroupFragment.currentSection, e!!.studentNo, score, e!!.remark, "YES", e!!.AdjustedScore)
                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                itemView.rowBtnEdit.setVisibility(View.VISIBLE);
            }

            itemView.btnScoreRemark.setOnClickListener { //    itemView.rowBtnSave.setVisibility(View.VISIBLE);
                //                val dlgremark = LayoutInflater.from(context).inflate(R.layout.remark_main, null)
                //                val mBuilder = AlertDialog.Builder(context).setView(dlgremark).setTitle("")
                //                val inputDialog = mBuilder.show()
                //                inputDialog.setCanceledOnTouchOutside(false); //
                //                RemarkMain.btnRemark = itemView.btnScoreRemark
                //                RemarkMain.alert = inputDialog
                //                RemarkMain.remark_section = com.example.myapplication05.Grouping.GRP_SECTION
                //                var ppp: RemarkMain = RemarkMain()
                //                ppp.DisplayRenark(dlgremark, context, currentActivity!!.studentNo, com.example.myapplication05.Grouping.GRP_ACT_CODE)

            }


        } //init

        fun setData(grp: GrpMemberModel?, pos: Int) { //
            Log.e("HI", grp!!.firstname)
            itemView.rowtxtName.text = grp!!.lastnanme + "," + grp!!.firstname
            itemView.btnScoreRemark.text = grp!!.remark
            itemView.txtIndScore.setText(grp!!.score.toString())

            itemView.btnScoreRemark.setVisibility(View.VISIBLE);
            itemView.txtIndScore.setVisibility(View.VISIBLE);
            itemView.rowBtnEdit.setVisibility(View.VISIBLE);
            itemView.rowBtnSave.setVisibility(View.VISIBLE);
            itemView.txtNewRemark.setVisibility(View.INVISIBLE);
            Log.e("HelloMemnder", Util.TAB_GRP_POSITION.toString())

            if (Util.TAB_GRP_POSITION == 0) {
                itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                itemView.txtIndScore.setVisibility(View.INVISIBLE);
                itemView.btnScoreRemark.setVisibility(View.INVISIBLE);
            }
            if (Util.TAB_GRP_POSITION == 2) {
                var act = ScoreFragment.viewgroup!!.cboScoreActivity.getSelectedItem().toString();
                if (act == "Select") {
                    itemView.rowBtnEdit.setVisibility(View.INVISIBLE);
                    itemView.rowBtnSave.setVisibility(View.INVISIBLE);
                    itemView.txtIndScore.setVisibility(View.INVISIBLE);
                    itemView.btnScoreRemark.setVisibility(View.INVISIBLE);
                } else { //                    itemView.rowBtnEdit.setVisibility(View.VISIBLE);
                }



                this.currentActivity = grp
                this.currentPosition = pos
            }
        }


    }
}