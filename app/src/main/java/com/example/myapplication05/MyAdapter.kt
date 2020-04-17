package com.example.myapplication05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter (var myContext:Context, var resources:Int, var myItems:List<Model>):ArrayAdapter<Model>(myContext, resources, myItems) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(myContext)
        val view: View = layoutInflater.inflate(resources, null)
        val imageView: ImageView = view.findViewById(R.id.myimg)
        val name: TextView = view.findViewById(R.id.txtname)
        val description: TextView = view.findViewById(R.id.txtdesc)


        var theItem = myItems[position]
        imageView.setImageDrawable(myContext.resources.getDrawable(theItem.img))
        name.text = theItem.title
        description.text = theItem.desc
        return view
    }




}