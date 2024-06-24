//package com.example.myapplication05
//
//class PaperGroupAdaopter {}


package com.example.myapplication05

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.enrolle_row.view.*
import kotlinx.android.synthetic.main.paperpic_row.view.*
import kotlinx.android.synthetic.main.random_row.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class PaperGroupAdaopter(val context: Context, val grp_picture: List<GrpModel>) :
    RecyclerView.Adapter<PaperGroupAdaopter.MyViewHolder>() {
    var previousSelectedButton: Button? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.paperpic_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return grp_picture.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = grp_picture[position]
        holder.setData(mySummary, position)
        //        if ( ( previousSelectedButton == null ) || ( previousSelectedButton == this.aButton ) ) {
        //            this.aButton!!.setBackgroundColor(Color.parseColor("#64B5F6"))
        //        } else {
        //
        //            previousSelectedButton!!.setBackgroundResource(android.R.drawable.btn_default);
        //            this.aButton!!.setBackgroundColor(Color.parseColor("#64B5F6"))
        //
        //            // !!.setBackgroundColor( ContextCompat.getColor( context, R.color.grey) )
        //            //this.aButton!!.setBackgroundColor( ContextCompat.getColor( context, R.color.orange) )
        //        }
        //

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentRandom: GrpModel? = null
        var currentPosition: Int = 0

        init {
            itemView.btnLastName.setOnClickListener { //  PaperPicture.txtStudent!!.setText(currentRandom!!.lastname + "," + currentRandom!!.firstname)
                PaperPicture.STUDENT_NAME =  currentRandom!!.grpnumber

                PaperPicture.btnPageNo!!.setText("P1")
                PaperPicture.txtStudent!!.setText( currentRandom!!.grpnumber)
                var keyword =   PaperPicture.myKeyword!!.text.toString()
                ShowPicture(PaperPicture.STUDENT_NAME , PaperPicture.SECTION, keyword)


                if (previousSelectedButton==null) {
                    previousSelectedButton = itemView.btnLastName
                }else {
                    previousSelectedButton!!.setBackgroundResource(android.R.drawable.btn_default);
                    previousSelectedButton = itemView.btnLastName
                }
                //                Log.e ("Helo",)
                //                Log.e ("Helo", PaperPicture.txtStudent!!.text.toString())
                //                val db: PaperPicture = PaperPicture()
                itemView.btnLastName!!.setBackgroundColor(Color.parseColor("#64B5F6"))

            }

        }

        //  dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default);
        fun setData(myatt: GrpModel?, pos: Int) {
//            itemView.btnLastName.setText(myatt!!.lastname.toString())
//            itemView.btnLastName.setBackgroundResource(android.R.drawable.btn_default);
//]]
            itemView.btnLastName.setText(myatt!!.grpnumber.toString())
            this.currentRandom = myatt;
            this.currentPosition = pos
        }

        //
        //        fun ColorDefault(){
        //            var x = 0;
        //            var count = getItemCount()
        //            while (x<=count){
        //            }
        //        }

    }


    fun ShowPicture(studName: String, section:String, keyword:String) {

        Log.e("section111", section)
        try {
            val path = "/storage/emulated/0/Quiz/" + section +"/" + studName
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")
            PaperPicture.myImage!!.setRotation(0f)
            val f: File = File(path, keyword  + ".jpg")
            if (f.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                PaperPicture.myImage!!.setImageBitmap(b)
            } else {
                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                PaperPicture.myImage!!.setImageBitmap(b)
            }
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")

            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

}