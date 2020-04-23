package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.confirm.view.*
import kotlinx.android.synthetic.main.dialog_student.view.*
import kotlinx.android.synthetic.main.recycle_row.view.*

class NewAdapter (val context:Context, val person:List<Person>):RecyclerView.Adapter<NewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val myView = LayoutInflater.from(context).inflate(R.layout.recycle_row, parent, false)
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

        var currentPerson: Person? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                // Toast.makeText(context, currentPerson!!.firstname + "", Toast.LENGTH_SHORT).show();
                var A: MyRecycle = MyRecycle()
                A.ShowDialog("VIEW", context, currentPerson, position)
            }

            itemView.setOnLongClickListener {
                val studentNumber = currentPerson!!.studentno
                val firstName =  currentPerson!!.firstname
                val lastName = currentPerson!!.lastname

               // Toast.makeText(context, position.toString() + "", Toast.LENGTH_SHORT).show();

                val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.inputbox, null)
                val mBuilder = AlertDialog.Builder(context)
                    .setView(dlgconfirm)
                    .setTitle("Do you like to delete $firstName  $lastName  ?")
                val confirmDialog = mBuilder.show()
                confirmDialog.setCanceledOnTouchOutside(false);

                dlgconfirm.btnYes.setOnClickListener {
//                    val db: DatabaseHandler = DatabaseHandler(context)
//                    var status = db.ManageStudent("DELETE", studentNumber)
//                    MyRecycle.list.removeAt(currentPosition)
//                    notifyDataSetChanged()
//                    confirmDialog.dismiss()
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

        fun setData(pp: Person?, pos: Int) {
                itemView.txtStudentNo.text = pp!!.studentno
                itemView.txtFirstName.text = pp!!.firstname
                itemView.txtLastName.text =pp!!.lastname
                itemView.txtSectionCode.text =pp!!.sectioncode
            itemView.txtGroup.text =pp!!.grp
                this.currentPerson= pp;
                this.currentPosition = pos
        }
    }
}