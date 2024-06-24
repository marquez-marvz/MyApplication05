package com.example.myapplication05.testpaper

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.key
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.ActivityMain
import com.example.myapplication05.EnrolleModel
import com.example.myapplication05.R
import com.example.myapplication05.TableActivity
import kotlinx.android.synthetic.main.paperpic_row.view.btnLastName
import kotlinx.android.synthetic.main.test_capture.view.btnPage1
import kotlinx.android.synthetic.main.test_capture.view.btnPaperKeyWord
import kotlinx.android.synthetic.main.test_capture.view.cboSectionPic
import kotlinx.android.synthetic.main.test_capture.view.imgStudent
import kotlinx.android.synthetic.main.test_capture.view.txtStudentName
import kotlinx.android.synthetic.main.util_confirm.view.btnNo
import kotlinx.android.synthetic.main.util_confirm.view.btnYes
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class PaperPicAdapter(val context: Context, val picture: List<EnrolleModel>) :
    RecyclerView.Adapter<PaperPicAdapter.MyViewHolder>() {
    var previousSelectedButton: Button? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.paperpic_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return picture.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val mySummary = picture[position]
        holder.setData(mySummary, position) //        if ( ( previousSelectedButton == null ) || ( previousSelectedButton == this.aButton ) ) {
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

        var currentRandom: EnrolleModel? = null
        var currentPosition: Int = 0

        init {

            fun btnLastnane() {}
            itemView.btnLastName.setOnClickListener { //  PaperPicture.txtStudent!!.setText(currentRandom!!.lastname + "," + currentRandom!!.firstname)
                var name = currentRandom!!.lastname + "," + currentRandom!!.firstname

                TestCapture.viewcapture!!.btnPage1.setText("P1")
                TestCapture.viewcapture!!.txtStudentName!!.setText(name)
                TestCapture.PAPER_FILENAME =
                    currentRandom!!.lastname + "_" + currentRandom!!.studentno

                var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
                Log.e("465", keyword)
                ShowPicture(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
                TestCapture.ShowButtonPages(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
                TestCapture.CURRENT_PAGE = "PAGE 1"
                TestCapture.ChangeColorPageButton("PAGE 1")
            }

            fun btnLastnane_Long() {}
            itemView.btnLastName.setOnLongClickListener { //  PaperPicture.txtStudent!!.setText(currentRandom!!.lastname + "," + currentRandom!!.firstname)

                if (TestCapture.viewcapture!!.txtStudentName!!.text.toString() == "NoName") {

                    var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
                    var section = TestCapture.currentCaptureSection
                    var fromFileName = ""
                    var toFileName = ""
                    if (TestCapture.CURRENT_PAGE == "PAGE 1") {
                        fromFileName = "_NoName.jpg"
                    } else if (TestCapture.CURRENT_PAGE == "PAGE 2") {
                        fromFileName = "_NoName-2.jpg"
                    } else if (TestCapture.CURRENT_PAGE == "PAGE 3") {
                        fromFileName = "_NoName-3.jpg"
                    } else if (TestCapture.CURRENT_PAGE == "PAGE 4") {
                        fromFileName = "_NoName-4.jpg"
                    }
                    else if (TestCapture.CURRENT_PAGE == "PAGE 5") {
                        fromFileName = "_NoName-5.jpg"
                    }
                    else if (TestCapture.CURRENT_PAGE == "PAGE 6") {
                        fromFileName = "_NoName-6.jpg"
                    }
                    else if (TestCapture.CURRENT_PAGE == "PAGE 7") {
                        fromFileName = "_NoName-7.jpg"
                    }




                    var name = currentRandom!!.lastname + "_"   + currentRandom!!.studentno
                    var name1= currentRandom!!.lastname + "," + currentRandom!!.firstname
                    var currentpage = TestCapture.GetCurrentPage(name, section, keyword)
//                    Log.e("1667", currentpage)
//                    Log.e("1667", name)
//                    Log.e("1667", fileName)

                    if (currentpage == "PAGE 1") {
                        toFileName = "$name.jpg"
                    } else if (currentpage == "PAGE 2") {
                        toFileName = "$name-2.jpg"
                    } else if (currentpage == "PAGE 3") {
                        toFileName = "$name-3.jpg"
                    }


                    val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
                    val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                        .setTitle("Do you like to rename  for $name1 ?")
                    val confirmDialog = mBuilder.show()
                    confirmDialog.setCanceledOnTouchOutside(false)

                    dlgconfirm.btnYes.setOnClickListener {
                        val sdCard = Environment.getExternalStorageDirectory()
                        val path = sdCard.absolutePath + "/Quiz/" + section + "/" + keyword
                        var  fromDir   =  File(path, fromFileName)
                        var  toDir   =  File(path, toFileName)
                        if (fromDir.exists())
                            fromDir.renameTo(toDir)

                        TestCapture.viewcapture!!.txtStudentName!!.setText(name1)
                        TestCapture.PAPER_FILENAME =
                            currentRandom!!.lastname + "_" + currentRandom!!.studentno

                        var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
                        ShowPicture(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
                        TestCapture.ShowButtonPages(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
                        TestCapture.CURRENT_PAGE = "PAGE 1"
                        TestCapture.ChangeColorPageButton("PAGE 1")
                        confirmDialog.dismiss()
                    } //
                    dlgconfirm.btnNo.setOnClickListener {
                        confirmDialog.dismiss()
                    }


                }

                true

                /* val dir = Environment.getExternalStorageDirectory()
                if (dir.exists()) {
                    val from = File(dir, "from.mp4")
                    val to = File(dir, "to.mp4")
                    if (from.exists()) from.renameTo(to)
                }
                    currentRandom!!.lastname + "_" + currentRandom!!.studentno

                var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
                Log.e("465", keyword)
                ShowPicture(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
                TestCapture.ShowButtonPages(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
                TestCapture.CURRENT_PAGE = "PAGE 1"
                TestCapture.ChangeColorPageButton("PAGE 1")*/ //  }

            }
        }

        //  dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default);
        fun setData(myatt: EnrolleModel?, pos: Int) {
            itemView.btnLastName.setText(myatt!!.lastname.toString())

            var keyword = TestCapture.viewcapture!!.btnPaperKeyWord.text.toString()
            var fileName = myatt!!.lastname + "_" + myatt!!.studentno
            var stat = TestCapture.PictureCheck(fileName, myatt!!.Section, keyword)
            if (stat == true) {
                itemView.btnLastName.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else {
                itemView.btnLastName.setBackgroundResource(android.R.drawable.btn_default);
            }
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


    fun ShowPicture(fileName: String, section: String, keyword: String) {

        try {
            val path = "/storage/emulated/0/Quiz/" + section + "/" + keyword

            TestCapture.viewcapture!!.imgStudent.setRotation(0f)
            val f: File = File(path, fileName + ".jpg")
            if (f.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                TestCapture.viewcapture!!.imgStudent.setImageBitmap(b)
            } else {
                TestCapture.viewcapture!!.imgStudent.setImageBitmap(null)
            }


            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}





