package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.student_row.view.*
import kotlinx.android.synthetic.main.util_confirm.view.*


class StudentAdapter (val context:Context, val person:List<StudentModel>):RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.student_row, parent, false)
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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentStudent: StudentModel? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, currentStudent!!.firstname + "", Toast.LENGTH_SHORT).show();
                var A: StudentMain = StudentMain()
                A.ShowDialog("VIEW", context, currentStudent, position)
            }

            itemView.setOnLongClickListener {
                val studentNumber = currentStudent!!.studentno
                val firstName =  currentStudent!!.firstname
                val lastName = currentStudent!!.lastname

                // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();

                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgconfirm)
                    .setTitle("Do you like to delete $firstName  $lastName  ?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {
                    val db: DatabaseHandler = DatabaseHandler(context)
                    var status = db.ManageStudent("DELETE", studentNumber)
                    StudentMain.UpdateListContent(context, "SECTION")

                    notifyDataSetChanged()
                    confirmDialog.dismiss()
                }

                dlgconfirm.btnNo.setOnClickListener {
                    confirmDialog.dismiss()
                }



                true
            }

//            itemView.rowBtnDelete.setOnClickListener {
//                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
//            }
        }//init

        fun setData(pp: StudentModel?, pos: Int) {
            itemView.txtStudentNo.text = pp!!.studentno
            itemView.txtFirstName.text = pp!!.firstname
            itemView.txtLastName.text =pp!!.lastname
            itemView.txtSectionCode.text =pp!!.sectioncode
            itemView.txtGroup.text =pp!!.grp
            itemView.txtGender.text =pp!!.gender
            this.currentStudent= pp;
            this.currentPosition = pos
        }
    }
}