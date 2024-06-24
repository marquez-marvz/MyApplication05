package com.example.myapplication05.Student

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.example.myapplication05.DatabaseHandler
import com.example.myapplication05.SQLSelect
import com.example.myapplication05.StudentInfoModel
import com.example.myapplication05.StudentModel
import com.example.myapplication05.Util
import kotlinx.android.synthetic.main.attendance_row.view.*
import kotlinx.android.synthetic.main.student_main.*
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*


class StudentAdapter (val context:Context, val person:List<StudentInfoModel>): RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {
    val db: DatabaseHandler = DatabaseHandler(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.student_row, parent, false)
        return MyViewHolder((myView))
    }

    override fun getItemCount(): Int {

        return person.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val person = person[position]
        holder.setData(person, position)

    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentStudent: StudentInfoModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnIndividual.setOnClickListener {
                Toast.makeText(context, currentStudent!!.firstname + "", Toast.LENGTH_SHORT).show();
                var A: StudentMain = StudentMain()
                var sec = currentStudent!!.originalSection
                A.ShowDialog("VIEW", context,sec, currentStudent!!.studentID, position)
            }




            itemView.btnCopyStudent.setOnClickListener {
                Util.CopyText(context, itemView.txtContact.text.toString(), "Student Contact Copied")
            }

            itemView.btnPlus.setOnClickListener {
                itemView.txtContact.setVisibility(View.VISIBLE);
                itemView.txtParentContact.setVisibility(View.VISIBLE);
                itemView.txtAddress.setVisibility(View.VISIBLE);
                itemView.btnCopyParent.setVisibility(View.VISIBLE);
                itemView.btnCopyStudent.setVisibility(View.VISIBLE);
                itemView.btnPlus.setVisibility(View.INVISIBLE);
                itemView.btnMinus.setVisibility(View.VISIBLE); //                currentStudent!!.expand= "YES"
                //                notifyDataSetChanged()
            }

            itemView.btnMinus.setOnClickListener {
                itemView.txtContact.setVisibility(View.GONE);
                itemView.txtParentContact.setVisibility(View.GONE);
                itemView.txtAddress.setVisibility(View.GONE);
                itemView.btnCopyParent.setVisibility(View.GONE);
                itemView.btnCopyStudent.setVisibility(View.GONE);
                itemView.btnCopyStudent.setVisibility(View.GONE);
                itemView.btnPlus.setVisibility(View.VISIBLE);
                itemView.btnMinus.setVisibility(View.INVISIBLE); //                currentStudent!!.expand= "NO"
                //                notifyDataSetChanged()

            }




            itemView.btnCopyParent.setOnClickListener {
                Util.CopyText(context, itemView.txtParentContact.text.toString(), "Parent Contact Copied")
            }

            itemView.btnEnroll.setOnClickListener {
                val db: DatabaseHandler = DatabaseHandler(context)
                val e = currentStudent

                val subjectSection = db.GetCurrentSection()
                Log.e("1221", subjectSection)
                val studNo = db.GetNewStudentNumber(subjectSection)
                Log.e("1221", studNo.toString())
                db.AddEnrolle(studNo.toString(), e!!.studentID, e!!.firstname, e!!.lastname, subjectSection, "NONE", "ENROLLED") //(studnumm:String, studID:String, givenNamre:String ,surName:String, section:String, grpNumber:String, enrollmentStatus:String){
                itemView.btnEnroll.text = "OK"
                itemView.btnEnroll.isEnabled = false
                itemView.btnEnroll.setBackgroundColor(Color.parseColor("#64B5F6"))
                itemView.btnEnroll.setTextColor(Color.BLACK);
                StudentMain.UpdateEnrolleListContent(context, "LAST_ORDER")
                StudentMain.adapterEnrolle!!.notifyDataSetChanged()
            }



            itemView.setOnLongClickListener {
                val studentNumber = currentStudent!!.studentID
                val firstName = currentStudent!!.firstname
                val lastName = currentStudent!!.lastname
                val section = currentStudent!!.originalSection


                // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();

                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                    .setTitle("Do you like to delete $firstName  $lastName  ?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {

                    var status = db.DeleteStudentRecord(studentNumber, section)
                    StudentMain.UpdateListContent(context, "SECTION")

                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                }

                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }



                true
            }
        }

            //            itemView.rowBtnDelete.setOnClickListener {
            //                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
            //                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            //            }


            fun setData(pp: StudentInfoModel?, pos: Int) {

                itemView.txtStudentID.text = pp!!.studentID
                itemView.txtFirstName.text = pp!!.firstname
                itemView.txtLastName.text = pp!!.lastname.toUpperCase()

                itemView.txtGender.text = pp!!.gender

                itemView.txtContact.text = pp!!.contactNumber.toString()
                itemView.txtParentContact.text = pp!!.parentcontact.toString()
                itemView.txtAddress.text = pp!!.address.toString()
                itemView.txtOrderNum.text = pp!!.ctr.toString()






                itemView.txtContact.setVisibility(View.GONE);
                itemView.txtParentContact.setVisibility(View.GONE);
                itemView.txtAddress.setVisibility(View.GONE);
                itemView.btnCopyParent.setVisibility(View.GONE);
                itemView.btnCopyStudent.setVisibility(View.GONE);
                val db: DatabaseHandler = DatabaseHandler(context)
                val sectionName = db.GetCurrentSection()
                if (db.CheckEnrollment(pp!!.studentID, sectionName) == true) {
                    itemView.btnEnroll.text = "OK"
                    itemView.btnEnroll.isEnabled = false
                    itemView.btnEnroll.setBackgroundColor(Color.parseColor("#64B5F6"))
                    itemView.btnEnroll.setTextColor(Color.BLACK);
                } else {
                    itemView.btnEnroll.text = "ENROLL"
                    itemView.btnEnroll.setBackgroundResource(android.R.drawable.btn_default);
                    itemView.btnEnroll.isEnabled = true
                    itemView.btnEnroll.setTextColor(Color.BLACK);

                }


                //  itemView.rowBtnPresent.setBackgroundColor(Color.parseColor("#"))

                this.currentStudent = pp;
                this.currentPosition = pos
            }


//
//        fun showPopMenu(v: View) {
//            val popup = PopupMenu(context, v, Gravity.RIGHT)
//            popup.setOnMenuItemClickListener(this)
//            popup.inflate(R.menu.enrollment)
//            popup.show()
//
//        }


    }


    }






@SuppressLint("Range")
fun DatabaseHandler.GetOneStudentRecord (studentID:String) :StudentInfoModel {
    var stud: StudentInfoModel  = StudentInfoModel()

//    cursor2.getString(cursor2.getColumnIndex("StudentNo")
//
//    //StudentID	FirstName	LastName	OriginalSection	Gender	ContactNumber	Extension	ParentContact	Address	emailAddress	SchoolStudentNumber	MIddleName	Status
  var   sql = """
            SELECT *
            FROM tbstudent_info WHERE StudentID= '$studentID'
            AND  StudentID= '$studentID'
    """.trimIndent()
    var   cursor = SQLSelect(sql)
    if (cursor!!.moveToFirst()) {
        stud!!.studentID = studentID
        stud!!.firstname = cursor!!.getString(cursor!!.getColumnIndex("FirstName"))
        stud!!.lastname = cursor!!.getString(cursor!!.getColumnIndex("LastName"))
        stud!!.middleName = cursor!!.getString(cursor!!.getColumnIndex("MIddleName"))
        stud!!.gender= cursor!!.getString(cursor!!.getColumnIndex("Gender"))
        stud!!.contactNumber = cursor!!.getString(cursor!!.getColumnIndex("ContactNumber"))
        stud!!.parentcontact = cursor!!.getString(cursor!!.getColumnIndex("ParentContact"))
        stud!!.address = cursor!!.getString(cursor!!.getColumnIndex("Address"))
        stud!!.emailAddress = cursor!!.getString(cursor!!.getColumnIndex("emailAddress"))
        stud!!.schoolStudentNumber = cursor!!.getString(cursor!!.getColumnIndex("SchoolStudentNumber"))
    }
    return  stud
}

fun DatabaseHandler.CheckEnrollment (studentID:String, sectionName:String) :Boolean {
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

fun DatabaseHandler.AddEnrolle(studnumm:String, studID:String, givenNamre:String ,surName:String, section:String, grpNumber:String, enrollmentStatus:String){
   // StudentNo	StudentID	GivenNamre	SurName	GrpNumber	Section	EnrollmentStatus
    var sql = ""
    var sql2= ""
    var sql3= ""
    var sql4= ""
    val db = this.writableDatabase
        sql = """
             insert into tbenroll ( 
                      StudentNo,	StudentID,	GivenNamre,	SurName,	GrpNumber,	Section,	EnrollmentStatus, folderLink)
             values('$studnumm', '$studID','$givenNamre','$surName' , '$grpNumber', '$section', '$enrollmentStatus', '-') 
            """

                    sql2= """
                   insert into tbgrades values('$section', '$studnumm', 0.0, 0.0, 0.0, 0.0, 0.0, "-", 0.0, 0.0, 0.0, 1, "OK", "OK")
                    """

                    sql3= """
                        INSERT INTO tbscore (ActivityCode, SectionCode, StudentNo,Score, Remark,SubmissionStatus, AdjustedScore, GradingPeriod )
                            SELECT ActivityCode, '$section', $studnumm, 0, "-", 'NO', 0, GradingPeriod
                            FROM tbactivity
                            WHERE sectionCode='$section';
                        """


                    sql4= """
                        INSERT INTO tbattendance  (SchedDate,	SchedTime,	SectionCode,	StudentNumber,	Remark	, AttendanceStatus,	RandomNumber,	RecitationPoints,	TaskPoints)
                            SELECT SchedDate, SchedTime, SectionCode, $studnumm,"-",  '-', 0, 0, 0
                            FROM tbsched
                            WHERE sectionCode='$section';
                        """

    	//SchedID	SchedDate	SectionCode	Remark	SchedTerm	Day
        Log.e("1221", sql)
        db.execSQL(sql)
                    db.execSQL(sql2)
                    db.execSQL(sql3)
    db.execSQL(sql4)


    Log.e("1221", sql)
    Log.e("1221", sql2)
    Log.e("1221", sql3)
    db.close()
}



fun DatabaseHandler.GetNewStudentNumber(section:String):Int{


    // StudentNo	StudentID	GivenNamre	SurName	GrpNumber	Section	EnrollmentStatus
    var sql = ""
    var sql2= ""
    var sql3= ""

    sql = """
            SELECT * 
             FROM tbenroll
              WHERE section='$section'
              order by StudentNo desc
       """

    sql2 = """
            SELECT * from tbsection
              WHERE sectionName='$section'
       """

    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql2, null)

    var cursor2: Cursor? = null
    cursor2 = db.rawQuery(sql, null)




    if (cursor.moveToFirst()) {
        var sectionCode = cursor.getString(cursor.getColumnIndex("SectionCode"))
        Log.e("SECSEC", sectionCode)
        val substring = sectionCode.subSequence(4, 6)
        Log.e("SECSEC", substring.toString())
        var newNum = 0
        if (cursor2.moveToFirst()) {
            var num = cursor2.getString(cursor2.getColumnIndex("StudentNo"))
            newNum = num.toInt() + 1
            Log.e("SECSEC", newNum.toString())
            return  newNum
        } else {
            val newNum1 = (substring.toString() + "01")
            newNum = newNum1.toInt()
            Log.e("SECSEC", newNum.toString())
            return  newNum
        }
    }
    return 0;




}


