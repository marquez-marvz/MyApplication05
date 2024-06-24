package com.example.myapplication05


import android.R.attr.button
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler.Companion.TBSECTION_CODE
import kotlinx.android.synthetic.main.attendance_main.view.*
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.attendance_main.*
import kotlinx.android.synthetic.main.dialog_message.view.*
import kotlinx.android.synthetic.main.random_student.view.*
import kotlinx.android.synthetic.main.section_row.view.*
import kotlinx.android.synthetic.main.summary_main.*
import kotlinx.android.synthetic.main.summary_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.section_dialog.view.*
import kotlinx.android.synthetic.main.section_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.section_main.view.*
import kotlinx.android.synthetic.main.section_row.view.rowBtnDelete
import kotlinx.android.synthetic.main.speak_row.view.*
import kotlinx.android.synthetic.main.student_dialog.view.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import kotlin.random.Random
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord as btnSaveRecord1


class SectionAdapter(val context: Context, val section: List<SectionModel>) :
    RecyclerView.Adapter<SectionAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.section_row, parent, false)

        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return section.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = section[position]
        holder.setData(mySummary, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentSection: SectionModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnMessage.setOnClickListener { // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();
                val dlgSection = LayoutInflater.from(context).inflate(R.layout.dialog_message, null)
                val mBuilder =
                    AlertDialog.Builder(context).setView(dlgSection).setTitle("Manage Section")
                val confirmDialog = mBuilder.show()
                var p =
                    """"Are you sure? It doesn't mention that in the docs I link to in the above comment, although I just saw it is marked as such in the res docs. Strange they don't mention that on the TextView page . The trouble is that the workaro" +
                        und they suggest (multiline) stops the view being clickable as I put in my OP. """

                confirmDialog.setCanceledOnTouchOutside(false);
                dlgSection.txtMessage.setText(currentSection!!.Message)




                dlgSection.btnSaveRecord.setOnClickListener {
                    var txt = dlgSection.btnSaveRecord.text.toString()
                    if (txt == "EDIT RECORD") {
                        dlgSection.txtMessage.isEnabled = true
                        dlgSection.btnSaveRecord.text = "SAVE RECORD"
                    } else if (txt == "SAVE RECORD") {
                        dlgSection.txtMessage.isEnabled = false
                        dlgSection.btnSaveRecord.text = "EDIT  RECORD"
                        var msg = dlgSection.txtMessage.text.toString()
                        val db: DatabaseHandler = DatabaseHandler(context)
                        db.UpdateMessage(currentSection!!.sectionCode, msg)
                    }
                }

                dlgSection.btnClear.setOnClickListener {
                    dlgSection.txtMessage.setText("")
                }


            }
            itemView.setOnLongClickListener {
                val section = currentSection!!.sectionName
                val school = currentSection!!.school
                val status = currentSection!!.status

                // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();
                val dlgSection = LayoutInflater.from(context).inflate(R.layout.section_dialog, null)
                val mBuilder =
                    AlertDialog.Builder(context).setView(dlgSection).setTitle("Manage Section")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);
                val db: DatabaseHandler = DatabaseHandler(context)
                val arrSection: ArrayList<String> = db.GetSubjectDescription()
                var sectionAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
                sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dlgSection.cboSubjectDescription.setAdapter(sectionAdapter); //dlgSection.cboSubjectDescription = findViewById(R.id.cboSectionActivity) as Spinner

                dlgSection.txtSection.setText(currentSection!!.sectionName)
                dlgSection.txtFolderLink.setText(currentSection!!.folderLink)
                dlgSection.txtSectionCode.setText(currentSection!!.sectionCode) //                dlgSection.txtOriginalSection.seText(currentSection!!.originalSection)
                dlgSection.cboSchool.setSelection(GetSchoolIndex(currentSection!!.school, context))
                Log.e("4562", currentSection!!.schhoolYear)
                Log.e("4562", SectionMain.GetSchoolYearIndex(currentSection!!.schhoolYear)
                    .toString())
                dlgSection.cboRealSectionName.setSelection(SectionMain.GetOriginalSectionIndex(currentSection!!.originalSection))
                dlgSection.cboAddSchoolYear.setSelection(SectionMain.GetSchoolYearIndex(currentSection!!.schhoolYear))
                dlgSection.cboAddSemester.setSelection(SectionMain.GetSemesterIndex(currentSection!!.semester))
                dlgSection.cboSubjectDescription.setSelection(SectionMain.GetSubjectDescriptionIndex(context, currentSection!!.subjectDescription)) //                dlgSection.cboVisibility.setSelection(GetVisibilityIndex(currentSection!!.status, context))
                dlgSection.btnSaveRecord.setText("EDIT RECORD")
                dlgSection.txtSection.isEnabled = false
                dlgSection.txtSection.isEnabled = false
                dlgSection.cboSchool.isEnabled =
                    false //                dlgSection.cboVisibility.isEnabled = false

                dlgSection.btnSaveRecord.setOnClickListener {
                    if (dlgSection.btnSaveRecord.text == "EDIT RECORD") {
                        dlgSection.cboSchool.isEnabled =
                            true //                        dlgSection.cboVisibility.isEnabled = true
                        dlgSection.btnSaveRecord.setText("SAVE CHANGES")
                    } else {
                        val db: DatabaseHandler = DatabaseHandler(context)
                        val sectionName = dlgSection.txtSection.text.toString()
                        val sectionCode =
                            dlgSection.txtSectionCode.text.toString() //                        val originalSection = dlgSection.txtOriginalSection.text.toString()
                        val folderLink = dlgSection.txtFolderLink.text.toString()
                        val desc = dlgSection.cboSubjectDescription.getSelectedItem().toString();
                        val school = dlgSection.cboSchool.getSelectedItem().toString()
                        val origSection = dlgSection.cboRealSectionName.getSelectedItem().toString(); //                        val status =    dlgSection.cboVisibility.getSelectedItem().toString();
                        db.EditSection(sectionName, sectionCode, school, status, origSection, desc, folderLink)
                    }

                }



                true
            }


            itemView.btnOpenFolder.setOnClickListener{


            Util.FOLDER_LINK = currentSection!!.folderLink
            Util.FOLDER_NAME = ""
            Util.FOLDER_SECTION = currentSection!!.sectionName
            Util.FOLDER_STUDNUM = ""


            val intent = Intent(context, Gdrive::class.java)
            context.startActivity(intent)
        }

        itemView.rowBtnDelete.setOnClickListener {


            val e = currentSection


            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you like to delete " + e!!.sectionName)
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false)

            dlgconfirm.btnYes.setOnClickListener {
                val db: DatabaseHandler = DatabaseHandler(context)

                db.DeleteSection(e!!.sectionCode)
                val section =
                    SectionMain.SectionUpdateListContent(context, Util.CURRENT_SCHOOLYEAR, Util.CURRENT_SEMESTER)
                notifyDataSetChanged()
                confirmDialog.dismiss()
            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }

        }

    } //init


    fun setData(myatt: SectionModel?, pos: Int) {
        itemView.rowSectionName.text = myatt!!.sectionName
        itemView.rowSectionCode.text = myatt!!.sectionCode
        itemView.rowSchool.text = myatt!!.school
        itemView.rowOriginalSection.text = myatt!!.originalSection
        itemView.rowDescription.text = myatt!!.subjectDescription.toString()
        this.currentSection = myatt;
        this.currentPosition = pos
    }


}

//    override fun onBindViewHolder(holder: NewAdapter.MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
}



private fun DatabaseHandler.EditSection(sectionName: String, sectioncode: String, school: String, status: String) {
    var sql = """
        UPDATE TBSECTION 
         SET  SectionName='$sectionName'        
         ,  Status='$status'        
         ,  School='$school'        
         WHERE  SectionCode='$sectioncode'        
    """.trimIndent()
    Log.e("ppp", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}


private fun DatabaseHandler.UpdateMessage(sectioncode: String, msg: String) {
    var sql = """
        UPDATE TBSECTION 
         SET  Message='$msg'        
         WHERE  SectionCode='$sectioncode'        
    """.trimIndent()
    val db = this.writableDatabase
    db.execSQL(sql)
}


private fun DatabaseHandler.UpdateSectionStatus(sectioncode: String, status: String) {
    var sql = """
        UPDATE TBSECTION 
         SET  Status='$status'        
         WHERE  SectionCode='$sectioncode'        
    """.trimIndent()
    val db = this.writableDatabase
    db.execSQL(sql)
}


private fun DatabaseHandler.DeleteSection(sectioncode: String) {

    var sql = "DELETE FROM TBSECTION " + " where  $TBSECTION_CODE='$sectioncode'"
    val db = this.writableDatabase
    db.execSQL(sql)
}


fun GetSchoolIndex(search: String, context: Context): Int {
    val arrGroup: Array<String> = context.getResources().getStringArray(R.array.school)
    val index = arrGroup.indexOf(search)
    Log.e("xx", index.toString() + "   " + search)
    return index
}

//
//fun GetVisibilityIndex(search: String, context: Context): Int {
//    val arrGroup: Array<String> = context.getResources().getStringArray(R.array.visibilityMode)
//    val index = arrGroup.indexOf(search)
//    Log.e("uyy", index.toString() + "   " + search)
//
//    return index
//}








