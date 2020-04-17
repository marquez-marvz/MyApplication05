package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView



class StudentAdapter (var myContext: Context, var resources:Int, var myItems:List<StudentModel>):
    ArrayAdapter<StudentModel>(myContext, resources, myItems) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(myContext)
        val view: View = layoutInflater.inflate(resources, null)

       val sn: TextView = view.findViewById(R.id.rowStudentNo)
        val fname: TextView = view.findViewById(R.id.rowFirstName)
        val lname: TextView = view.findViewById(R.id.rowLastName)


        var theItem = myItems[position]
        sn.text = theItem.sn
        fname.text = theItem.fname
        lname.text = theItem.lname
        return view
    }




}