package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication05.R
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.general_pic.*
import kotlinx.android.synthetic.main.general_pic.btnCapture
import kotlinx.android.synthetic.main.general_pic.btnDeletePic
import kotlinx.android.synthetic.main.general_pic.imgStudent200
import kotlinx.android.synthetic.main.general_pic.view.*
import kotlinx.android.synthetic.main.general_row.view.*
import kotlinx.android.synthetic.main.keyword_row.view.*
import kotlinx.android.synthetic.main.paperpic_row.view.*
import kotlinx.android.synthetic.main.qrcode.view.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream


class GeneralPic : AppCompatActivity() {
    var adapterGeneral: GeneralAdapter? = null;
    val db2: DatabaseHandler = DatabaseHandler(this)

    var studentAdapter: ArrayAdapter<String>? = null;

    val myContext: Context = this;


    companion object {
        var varTxtFileName: TextView? = null;
        var section = ""
        var myImage: TouchImageView? = null;
        var vartxtFileName: TextView? = null
        var fileList = arrayListOf<GeneralModel>()

        fun ListFiles(section:String) {
            val path = Environment.getExternalStorageDirectory().toString() + "/General/" + section
            val dir2 = File(path)
            dir2.mkdirs() //
            Log.d("Files", "Path: $path")
            val directory = File(path)
            val files = directory.listFiles()
            fileList.clear()
            for (i in files.indices) {
                var filename = files[i].getName();
                var fileNameNoExt = filename.split(".")
                fileList.add(GeneralModel(fileNameNoExt[0]))
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.general_pic)


        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        lstGeneralFile.layoutManager = layoutmanager
        adapterGeneral = GeneralAdapter(this, fileList)
        lstGeneralFile.adapter = adapterGeneral
        section = db2.GetCurrentSection();
        myImage = findViewById(R.id.imgStudent200) as TouchImageView
        vartxtFileName = findViewById(R.id.txtGeneralKeyword) as TextView
        imgStudent200.setImageDrawable(null);

        Util.GENERALMODE = "GENERAL"



        ListFiles(section)
        SetSpinnerAdapter()
        SetDefaultSection()



        btnDeletePic.setOnClickListener {
            val sdCard = Environment.getExternalStorageDirectory()
            var filename = txtGeneralKeyword.text.toString() + ".jpg"
            val f = File(sdCard.absolutePath + "/General/" + section + "/" + filename)


            val dlgconfirm = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgconfirm).setTitle("Do you like to overwrite")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {
                f.delete()
                confirmDialog.dismiss()
                imgStudent200.setImageDrawable(null);
                txtGeneralKeyword.setText("")
                ListFiles(section)
                adapterGeneral!!.notifyDataSetChanged()
            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }

        }

//        btnCapture.setOnClickListener {
//            if (txtGeneralKeyword.text.toString() == "") {
//                Util.Msgbox(this, "Enter filename")
//            } else {
//                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                var out = Environment.getExternalStorageDirectory()
//                out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
//                val photoURI =
//                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", out)
//                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                startActivityForResult(i, 100)
//            }
//        }



        btnRotate.setOnClickListener {
            if (txtGeneralKeyword.text.toString() == "") {
                Util.Msgbox(this, "Enter filename")
            }
            else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 130) ////            //                startActivityForResult(intent, AttendanceMain.PICK_FILE)*/
            }

        }


        cboGeneralActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                section = cboGeneralActivity.getSelectedItem().toString();
                val mydb: DatabaseHandler = DatabaseHandler(myContext)
                mydb.SetCurrentSection(section)
                ListFiles(section)
                adapterGeneral!!.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) { //        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == 100) {
            ImageCapture()
        } else if (requestCode == 130) {
            val uri: Uri? = imageReturnedIntent!!.data
            val myFile = File(uri.toString());

            val selectedFile = imageReturnedIntent.data
            val path = selectedFile!!.path
            Log.e("AAA", uri.toString())
            Log.e("AAA", path.toString())
            var p = path!!.split(":")
            Log.e("AAA", p[1])


            val folderDest = "/storage/emulated/0/GENERAL/" + section
            val fileDest = File(folderDest)
            if (!fileDest.exists()) fileDest.mkdir()

            val keyword = txtGeneralKeyword.text.toString()
            val file = File("/storage/emulated/0/", p[1])
            val file2 =
                File("/storage/emulated/0/General/" + section, keyword + ".jpg") //                        val sourceImagePath = "/path/to/source/file.jpg" //                        val destinationImagePath = "/path/to/destination/file.jpg"


            val src = FileInputStream(file).channel
            val dst = FileOutputStream(file2).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
            ListFiles(section)
            adapterGeneral!!.notifyDataSetChanged()


        }


    }

    fun ImageCapture() {

        val path = "/storage/emulated/0"

        val f: File = File(path, "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        imgStudent200.setImageBitmap(b)

        val sdCard = Environment.getExternalStorageDirectory()

        val dir = File(sdCard.absolutePath + "/General/" + section)
        dir.mkdirs()


        val dir2 = File(sdCard.absolutePath + "/General/" + section)
        dir2.mkdirs() //
        //        Log.e("Hello", )
        Log.e("Hello", sdCard.absolutePath)
        var out = Environment.getExternalStorageDirectory()

        //

        var keyword = txtGeneralKeyword.text.toString()

        val file1 = File(out, "temp.jpg")
        val file2 = File(dir2, keyword + ".jpg")

        if (!file2.exists()) {
            val src = FileInputStream(file1).channel
            val dst = FileOutputStream(file2).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close() //
            ListFiles(section)
            adapterGeneral!!.notifyDataSetChanged()


        }

    }



    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboGeneralActivity.setAdapter(sectionAdapter);
    }

    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboGeneralActivity.setSelection(index)
    }

}


class GeneralAdapter(val context: Context, val general: List<GeneralModel>) :
    RecyclerView.Adapter<GeneralAdapter.MyViewHolder>() {
    val db2: DatabaseHandler = DatabaseHandler(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.general_row, parent, false)
        return MyViewHolder((myView))

    }

    override fun getItemCount(): Int {
        return general.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.removeAllViews();
        val myGeneral = general[position]
        holder!!.setData(myGeneral, position)

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentGeneral: GeneralModel? = null
        var currentPosition: Int = 0

        init {

            itemView.btnFileName.setOnClickListener {
                if (Util.GENERALMODE == "GENERAL"){
                    ShowPicture(GeneralPic.section, currentGeneral!!.fileName)
                    GeneralPic.vartxtFileName!!.setText(currentGeneral!!.fileName)
                }
                else{
//                    ShowPicture(PaperPicture.SECTION, currentGeneral!!.fileName)
//                    PaperPicture.dlgPicture!!.txtGeneralKeyword.setText(currentGeneral!!.fileName)
                }


//
            }

        }


        //  dlgAttendance!!.btnPresentDialog.setBackgroundResource(android.R.drawable.btn_default);
        fun setData(myatt: GeneralModel, pos: Int) { // itemView.btnLastName.setText(myatt!!.lastname.toString())
            itemView.btnFileName.setText(myatt!!.fileName)
            this.currentGeneral = myatt;
            this.currentPosition = pos



        }
    }


    fun ShowPicture(section: String, fileName: String) {

        Log.e("section111", section)
        Log.e("section111", fileName)
        try {
            val path = "/storage/emulated/0/General/" + section
            val f: File = File(path, fileName + ".jpg")
            if (f.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                if (Util.GENERALMODE == "GENERAL"){
                    GeneralPic.myImage!!.setImageBitmap(b)
                }
                else{
//                    /PaperPicture.dlgPicture!!.imgStudent200.setImageBitmap(b)
                }
            } else {
                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                if (Util.GENERALMODE == "GENERAL"){
                    GeneralPic.myImage!!.setImageBitmap(b)
                }
                else{
//                    PaperPicture.dlgPicture!!.imgStudent200.setImageBitmap(b)
                }
            }


            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}

