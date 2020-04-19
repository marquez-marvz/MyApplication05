package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class StudentAdapter (var myContext: Context, var resources:Int, var myItems:List<StudentModel>):
    ArrayAdapter<StudentModel>(myContext, resources, myItems) {

    var lcontext: Context? = null

    init {
        this.lcontext = myContext

    }
    //   override fun getView(position: Int, convertView: View?, parent: ViewGroup) {
     override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{

        val layoutInflater: LayoutInflater = LayoutInflater.from(myContext)
        val view: View = layoutInflater.inflate(resources, null)

        val sn: TextView = view.findViewById(R.id.rowStudentNo)
        val fname: TextView = view.findViewById(R.id.rowFirstName)
        val lname: TextView = view.findViewById(R.id.rowLastName)
        //Button  = (Button) row.findViewById(R.id.DeleteImageView);
        val delete: Button = view.findViewById(R.id.rowBtnDelete)

//        delete.setOnClickListener {
//            Toast.makeText(this.context, position.toString(),  Toast.LENGTH_LONG).show();
//            var db:Database = Database();
//           db.ShowDialog("VIEW", position, myContext)
//        //    db.ViewRecord(myContext)
//           // db.Sample("VIEW", position)
//
//        }


            var theItem = myItems[position]
        sn.text = theItem.sn
        fname.text = theItem.fname
        lname.text = theItem.lname
        return view
    }

    }




