//package com.example.myapplication05
//
//class EnrolleAdapter {}

package com.example.myapplication05

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication05.R
import com.example.myapplication05.Student.GetNewStudentID
import com.example.myapplication05.Student.GetOriginalSection
import com.example.myapplication05.Student.StudentMain
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.rowBtnMenu
import kotlinx.android.synthetic.main.student_dialog.view.btnSaveRecord
import kotlinx.android.synthetic.main.student_dialog.view.cboSection
import kotlinx.android.synthetic.main.student_dialog.view.txtStudentData
import kotlinx.android.synthetic.main.student_dialog.view.txtstudentnumber
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.student_row.view.btnIndividual
import kotlinx.android.synthetic.main.student_row.view.txtFirstName
import kotlinx.android.synthetic.main.student_row.view.txtGender
import kotlinx.android.synthetic.main.student_row.view.txtLastName
import kotlinx.android.synthetic.main.student_row.view.txtOrderNum
import kotlinx.android.synthetic.main.task_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import kotlinx.android.synthetic.main.util_folder_shared.view.*
import kotlinx.android.synthetic.main.util_inputbox.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.HashMap


class EnrolleAdapter (val context:Context, val person:List<EnrolleModel>):RecyclerView.Adapter<EnrolleAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.enrolle_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {

        return person.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val person= person[position]
        holder.setData(person, position)

    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {

        var currentStudent: EnrolleModel? = null
        var currentPosition: Int = 0

        init { //            itemView.btnIndividual.setOnClickListener {
            //                Toast.makeText(context, currentStudent!!.firstname + "", Toast.LENGTH_SHORT).show();
            //                var A: StudentMain = StudentMain()
            //               // A.ShowDialog("VIEW", context, currentStudent, position)
            //            }


            itemView.setOnLongClickListener {
                val studentNumber = currentStudent!!.studentno
                val firstName = currentStudent!!.firstname
                val lastName =
                    currentStudent!!.lastname //   val section = currentStudent!!.originalSection


                // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();

                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $firstName  $lastName  ?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {

                    //    var status = db.DeleteStudentRecord(studentNumber, section )
                    StudentMain.UpdateListContent(context, "SECTION")

                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                }

                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }



                true
            }

            itemView.rowBtnMenu.setOnClickListener {
                showPopMenuEnrolle(itemView)
            }


            itemView.btnIndividual.setOnClickListener {
                Toast.makeText(context, currentStudent!!.firstname + "", Toast.LENGTH_SHORT).show();
                var A: StudentMain = StudentMain()
                //var sec = currentStudent!!.originalSection
                A.ShowDialog("VIEW", context,"", currentStudent!!.studentID, position)
            }

//            itemView.btnSharedFolder.setOnClickListener {
//                //                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                //                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                //                intent.setType("image/*"); // startActivitinyForResult(intent, 100)
//                //                startActivityForResult(intent, AttendanceMain.PICK_FILE)
//            }

            itemView.btnDeteleteEnrolle.setOnClickListener {
            // LayoutInflater layoutInflater = getLayoutInflater(null); //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);
                Log.e("1335", currentStudent!!.Section)
                val section = currentStudent!!.Section
                val firstName = currentStudent!!.firstname
                val lastName = currentStudent!!.lastname


                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $lastName $firstName in $section?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false)

                dlgconfirm.btnYes.setOnClickListener {
                    db.DeleteStudentRecord(currentStudent!!.studentno, currentStudent!!.Section)
                    StudentMain.UpdateEnrolleListContent(context, "LAST_ORDER");
                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                } //
                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }
            }




            itemView.btnOpenFolder.setOnClickListener {
                Util.FOLDER_LINK = currentStudent!!.folderLink
                val intent = Intent(context, Gdrive::class.java)
                context.startActivity(intent) //
                //                val intent = Intent(context, IndividualStudenht::class.java)
                //                Util.GRADE_INDIVIDUAL ="GRADES"
                //                Util.GRADE_STUD_NO = currentGrade!!.studentNo
                //                Util.GRADE_NAME = currentGrade!!.lastname +  "," + currentGrade!!.firstname
                //                context.startActivity(intent)

            }

            itemView.btnStudentShareFolder.setOnClickListener {
//                val section = currentStudent!!.Section
//                val firstName = currentStudent!!.firstname
//                val lastName =
//                    currentStudent!!.lastname //   val section = currentStudent!!.originalSection
//
//                Log.e("AAA", currentStudent!!.firstname + "  " + currentStudent!!.folderLink) // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();
//
//                val dlgconfirm =
//                    LayoutInflater.from(context).inflate(R.layout.util_folder_shared, null)
//                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
//                    .setTitle("Input Link for  $firstName  $lastName  ?")
//                val confirmDialog = mBuilder.show()
//                val studentNumber = currentStudent!!.studentno
//                confirmDialog.setCanceledOnTouchOutside(false);
//                dlgconfirm.txtFolderLink.setText(currentStudent!!.folderLink)
               var email = db.GetEmail(currentStudent!!.studentID)
//                dlgconfirm.txtEmail.setText(email.toString())
//
//
//                dlgconfirm.btnShareFolder.setOnClickListener {
                    ShareFolder(email, currentStudent)

                }








            //            itemView.rowBtnDelete.setOnClickListener {
            //                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
            //                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            //            }
        } //init

        fun setData(pp: EnrolleModel?, pos: Int) {

            itemView.txtStudentNo.text = pp!!.studentno
            itemView.txtFirstName.text = pp!!.firstname
            itemView.txtLastName.text = pp!!.lastname.toUpperCase()

            itemView.txtGender.text = pp!!.gender

            itemView.btnStudentStatus.text = pp!!.studentStatus
            Log.e("@@@", pp!!.lastname + "   " + pp!!.folderLink)


            if (pp!!.folderLink == "-"){
                itemView.btnFolderStatus.text = "NO LINK"
            }
            else {
                itemView.btnFolderStatus.text = "WITH LINK"
            }







            itemView.txtOrderNum.text = pp!!.ctr.toString()


                if(Util.ENROLLE_FLAG == "STUDENT"){
                    itemView.btnOpenFolder.isVisible = false

                    itemView.txtGender.isVisible = false
                }
            else {
                    itemView.btnOpenFolder.isVisible = true

                    itemView.txtGender.isVisible = true
                }

            this.currentStudent = pp;56
            this.currentPosition = pos

        }


        fun showPopMenuEnrolle(v: View) {
            val popup = PopupMenu(context, v, Gravity.RIGHT)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.enrollment)
            popup.show()
        }

        fun ShareFolder(email: String, e: EnrolleModel?) {

            val loading = ProgressDialog.show(context, "", "Please wait")
            var url =
                "https://script.google.com/macros/s/AKfycbzwIAC9lZorW10u_ea-7brbbjdHF5J1AWyrNENl8SanNiZHoxDU0Cko_MAaBWxSCKHc/exec";
Log.e("4563", url)
            val stringRequest: StringRequest = object :

                StringRequest(Method.POST, url, Response.Listener { response ->
                    loading.dismiss() //Toast.makeText(this@AddItem, response, Toast.LENGTH_LONG).show()
                    //                    val intent = Intent(applicationContext, MainActivity::class.java)
                    //                    startActivity(intent)

                    Log.e("@@@", response)
                    Log.e("@@@", "Hwllo po")
                  //  dlgconfirm.txtFolderLink.setText(response)
                    if (response != "Invalid Email") {
                        db.UpdateLink(e!!.studentno, e!!.Section, response)
                        e!!.folderLink = response
                        notifyDataSetChanged()
                    }


                }, Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    val parmas: MutableMap<String, String> = HashMap()
                    var folderName = e!!.lastname + "," + e!!.firstname

                    parmas["action"] = "SharedFolder"
                    parmas["folderName"] = folderName
                    parmas["sectionFolder"] =
                        db.GetSectionFolderLink(e!!.Section) //   parmas["sectionFolder"] ="https://drive.google.com/drive/folders/1s0NmdPGCHeF2pKNrB9GaB95RzvHDpVq8"
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


        fun CheckSharedFolder(email: String, e: EnrolleModel?) {

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
                    //  dlgconfirm.txtFolderLink.setText(response)
                    if (response != "Invalid Email") {
                        db.UpdateLink(e!!.studentno, e!!.Section, response)
                        e!!.folderLink = response


                    }


                }, Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    val parmas: MutableMap<String, String> = HashMap()
                    var folderName = e!!.lastname + "," + e!!.firstname

                    parmas["action"] = "CheckedShareddFolder"
                    parmas["folderName"] = folderName
                    parmas["sectionFolder"] =
                        db.GetSectionFolderLink(e!!.Section) //   parmas["sectionFolder"] ="https://drive.google.com/drive/folders/1s0NmdPGCHeF2pKNrB9GaB95RzvHDpVq8"
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



        override fun onMenuItemClick(item: MenuItem): Boolean {
            val selected = item.toString() //
            Log.e("SEL", selected)
            if (selected == "Delete Student") {
                val mymain = LayoutInflater.from(context)
                    .inflate(R.layout.sched_main, null) // LayoutInflater layoutInflater = getLayoutInflater(null); //  View total=layoutInflater.inflate(fragment_shopping_cart, parent, false);
                val section = currentStudent!!.Section
                val firstName = currentStudent!!.firstname
                val lastName = currentStudent!!.lastname


                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $lastName $firstName in $section?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false)

                dlgconfirm.btnYes.setOnClickListener {
                    db.DeleteStudentRecord(currentStudent!!.studentno, currentStudent!!.Section)
                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                } //
                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }
            }

            if (selected == "Copy Email") {
                var email =
                    db.GetEmail(currentStudent!!.studentID) //                Util.Msgbox(context, email)
                //                Util.CopyText(context, email, email)
                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_inputbox, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false)
                dlgconfirm.txtdata.setText(email)
                dlgconfirm.btnOK.setText("Update Email")
                dlgconfirm.btnOK.setOnClickListener {
                    var newEmail = dlgconfirm.txtdata.text.toString()
                    db.UpdateEmail(currentStudent!!.studentID, newEmail)
                    confirmDialog.dismiss()

                } //
                //                dlgconfirm.btnNo.setOnClickListener {
                //                    confirmDialog.dismiss()
                //                }


            }




            return true
        }
    }

//        override fun onActivityResult(requestCode: Int, resultcode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultcode, data)
//            if (requestCode == AttendanceMain.PICK_FILE && resultcode == Activity.RESULT_OK && data != null) {
//
//                val uri: Uri? = data.data
//                val myFile = File(uri.toString());
//
//                val selectedFile = data.data
//                val path = selectedFile!!.path
//                Log.e("AAA", uri.toString())
//                Log.e("AAA", path.toString())
//                var p = path!!.split(":")
//                Log.e("AAA",p[1])
//                //                try {
//                /*   val data1 = Environment.getDataDirectory()
//               Log.e("AAA", data1.toString())*/
//                val file = File("/storage/emulated/0/" , p[1])
//                val file2 = File("/storage/emulated/0/Picture" , "one2.jpg")
//                //                        val sourceImagePath = "/path/to/source/file.jpg"
//                //                        val destinationImagePath = "/path/to/destination/file.jpg"
//                //                        val source = File(data, sourceImagePath)
//                //                        val destination = File(sd, destinationImagePath)
//                /*if (source.exists()) {*/
//                val src = FileInputStream(file).channel
//                val dst = FileOutputStream(file2).channel
//                dst.transferFrom(src, 0, src.size())
//                src.close()
//                dst.close()
//                //                        }
//                //                    }
//                //                } catch (e: Exception) {
//                //                }
//                //               val mSelectedImagePath = getPath(uri);
//                //                Log.e("AAA", mSelectedImagePath.toString())
//                //                val f = File("/storage/emulated/0/Picture/Sample2.jpg")
//                //                if (!f.exists()) {
//                //                    try {
//                //                        f.createNewFile()
//                //                        copyFile(File(data.data?.let { getRealPathFromURI(it) }), f)
//                //                    } catch (e: IOException) { // TODO Auto-generated catch block
//                //                        e.printStackTrace()
//                //                    }
//                //                }
//
//
//
//
//
//
//            }
//
//        }
//
//
//
//    }
}


fun DatabaseHandler.CheckEnrollment22 (studentID:String, sectionName:String) :Boolean {
    val sectionList: ArrayList<String> = ArrayList<String>()
    var sql =""
    sql = """
            SELECT *
            FROM tbenroll WHERE Section= '$sectionName'
            AND  StudentID= '$studentID'
    """.trimIndent()
    Log.e("SQLSS", sql)

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)

    if (cursor.moveToFirst()) {
        return true
    }
    else
        return false

}



fun DatabaseHandler.UpdateLink (studentID:String, sectionName:String, link:String){
   var  sql = """
            Update tbenroll
            SET  FolderLink= '$link'
            WHERE Section= '$sectionName'
            AND  StudentNo= '$studentID'
    """.trimIndent()

    Log.e("SQLSS", sql)
//    sql = "Update tbenroll SET  FolderLink= '-'"
//    Log.e("SQLSS200", sql)
    val db = this.writableDatabase
    db.execSQL(sql)

}



fun DatabaseHandler.GetEmail (studentID:String):String {
    var  sql = """
            select * from  tbstudent_info 
            where StudentID= '$studentID'
    """.trimIndent()

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return  cursor.getString(cursor.getColumnIndex("emailAddress"))
    }
    else {
        return ""
    }

}


fun DatabaseHandler.GetEmailViaStudentNo (studentNo:String):String {
    var  sql = """
            select * from  tbstudent_query
            where StudentNO= '$studentNo'
    """.trimIndent()

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return  cursor.getString(cursor.getColumnIndex("emailAddress"))
    }
    else {
        return ""
    }

}


fun DatabaseHandler.UpdateEmail (studentID:String, email:String){
    var  sql = """
            update  tbstudent_info 
            set  emailAddress= '$email'
            where StudentID= '$studentID'
    """.trimIndent()
Log.e("EM", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}




fun DatabaseHandler.GetSectionFolderLink (section:String):String{
    var  sql = """
            select * from  tbsection 
            where SectionName= '$section'
    """.trimIndent()

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return  cursor.getString(cursor.getColumnIndex("SectionFolderLInk"))
    }
    else {
        return ""
    }
}




    

