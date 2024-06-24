package com.example.myapplication05.fragment

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.*
import com.ortiz.touchview.BuildConfig
import kotlinx.android.synthetic.main.fragment_picture.*
import kotlinx.android.synthetic.main.fragment_picture.view.*
import kotlinx.android.synthetic.main.fragment_score.view.*
import kotlinx.android.synthetic.main.keyword_main.view.*
import kotlinx.android.synthetic.main.paperpic.*
import kotlinx.android.synthetic.main.paperpic.btnPage
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class PictureFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("P5000", "GROUP 222111")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        Log.e("PIC5000", "HEloo12222222")

        viewpicture = inflater.inflate(R.layout.fragment_picture, container, false)
        containerGlobal = container
        var context = container!!.context
        val db2: DatabaseHandler = DatabaseHandler(context)
        ShowGroupList(context, GroupFragment.currentSection)
        SetUpGroupAdapter()
        viewpicture!!.imageGroup.setRotation(0f)
        var key = db2.GetNewDefaultKeyWord("GRP", GroupFragment.currentSection)
        viewpicture!!.btnPaperKeyWord.setText(key)

        viewpicture!!.btnCapture.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var out = Environment.getExternalStorageDirectory()
            out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
            val photoURI =
                FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", out)
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(i, 100)
        }

        viewpicture!!.btnPage.setOnLongClickListener() {
            viewpicture!!.btnPage.setText("P1")
            var studName = viewpicture!!.txtGroupNumber.text.toString()
            var section = GroupFragment.currentSection
            var keyword = viewpicture!!.btnPaperKeyWord.text.toString()
            ShowPicture(studName, section, keyword)
            true
        }
         viewpicture!!.btnPage.setOnClickListener() {
             var ext= ""
            if (btnPage.text.toString() == "P1"){
                btnPage.setText("P2")
                ext = "-2"
            }
            else if  (btnPage.text.toString() == "P2") {
                btnPage.setText("P3")
                ext = "-3"
            }
            else if (btnPage.text.toString() == "P3") {
                btnPage.setText("P4")
                ext = "-4"

            }
            else if (btnPage.text.toString() == "P4") {
                btnPage.setText("P5")
                ext = "-5"
            }
             var studName = viewpicture!!.txtGroupNumber.text.toString()
             var section = GroupFragment.currentSection
             var keyword = viewpicture!!.btnPaperKeyWord.text.toString() + ext
            ShowPicture(studName, section, keyword)
        }


        viewpicture!!.btnPaperKeyWord.setOnClickListener() {
            var section =GroupFragment.currentSection
            var context = containerGlobal!!.context
            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.keyword_main, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz).setTitle("")
            inputDialogKeyword = mBuilder.show()
            inputDialogKeyword!!.setCanceledOnTouchOutside(false); //

            val layoutmanager = LinearLayoutManager(context)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlgquiz.lstKeyWord.layoutManager = layoutmanager
            keyWordAdapter = KeyWordAdapter(context, listKeyWord)
            dlgquiz.lstKeyWord.adapter = keyWordAdapter
            var period = Util.GetCurrentGradingPeriod(context)
            dlgquiz.btnPeriod.setText(period)

            UpdateListKeyWord("GRP", period, section, context)




            val arrSection: ArrayList<String> = db2.GetAllKeyWord()
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dlgquiz.cboKeyWord.setAdapter(sectionAdapter);
            dlgquiz.txtKeyWord.setText("")



            dlgquiz.btnSavekeyWord.setOnClickListener {
                var keyword = dlgquiz.txtKeyWord.text.toString()
                val newPeriod = dlgquiz.btnPeriod.text.toString()
                var category ="GRP"
                db2.SaveNewKeyword(keyword, GroupFragment.currentSection, category, newPeriod)
                UpdateListKeyWord("IND", newPeriod, section, context)
                keyWordAdapter!!.notifyDataSetChanged()
            }

            dlgquiz.btnPeriod.setOnClickListener {
                var newPeriod = dlgquiz.btnPeriod.text.toString()
                if (newPeriod == "FIRST") {
                    dlgquiz.btnPeriod.setText("SECOND")
                } else {
                    dlgquiz.btnPeriod.setText("FIRST")
                }
                newPeriod = dlgquiz.btnPeriod.text.toString()
                var category = btnCategory.text.toString()
                UpdateListKeyWord(category, newPeriod, section, context)
                keyWordAdapter!!.notifyDataSetChanged()

            }

            dlgquiz.cboKeyWord.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        var sss = dlgquiz.cboKeyWord.getSelectedItem().toString();
                        if (sss == "Select") {
                            dlgquiz.txtKeyWord.setText("")
                        } else {
                            dlgquiz.txtKeyWord.setText(sss)
                        }
                    }
                }

        }


        return viewpicture
    }

    fun ShowGroupList(context: Context, section: String) {
        var context = containerGlobal!!.context
        val db2: DatabaseHandler = DatabaseHandler(context)
        grpList.clear()

        var grp: List<GrpModel>
        grp = db2.GetGroupList(section)
        Log.e("T100", grp.size.toString())
        for (e in grp) {
            grpList.add(GrpModel(e.grpnumber, e.grpnumber, e.num))
        }

    }

    fun UpdateListKeyWord(category: String, period: String, section: String, context: Context) {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val keyword: List<KeywordModel>


        keyword = db2.GetKeywordList(category, section, period)
        listKeyWord.clear()
        for (e in keyword) {
            listKeyWord.add(KeywordModel(e.Section, e.Keyword, e.GradingPeriod, e.Category, e.DefautKeyword))
        }
    } //

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) { //        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        val path = "/storage/emulated/0"
        var context = containerGlobal!!.context
        val f: File = File(path, "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        viewpicture!!.imageGroup.setImageBitmap(b)

        val sdCard = Environment.getExternalStorageDirectory()
        val section = GroupFragment.currentSection
        val dir = File(sdCard.absolutePath + "/Group/" + section)
        dir.mkdirs()


        val dir2 =
            File(sdCard.absolutePath + "/Group/" + section + "/" + viewpicture!!.txtGroupNumber.text.toString())
        dir2.mkdirs() //
        //        Log.e("Hello", )
        Log.e("Hello", sdCard.absolutePath)
        var out = Environment.getExternalStorageDirectory()

        //

        var keyword = viewpicture!!.btnPaperKeyWord.text.toString()

        if (btnPage.text.toString() == "P2") {
            keyword = keyword + "-2"
        } else if (btnPage.text.toString() == "P3") {
            keyword = keyword + "-3"
        } else if (btnPage.text.toString() == "P4") {
            keyword = keyword + "-4"
        } else if (btnPage.text.toString() == "P5") {
            keyword = keyword + "-5"
        }

        Log.e("key", btnPage.text.toString())
        Log.e("key", keyword)

        val file1 = File(out, "temp.jpg")
        val file2 = File(dir2, keyword + ".jpg")

        if (!file2.exists()) {
            val src = FileInputStream(file1).channel
            val dst = FileOutputStream(file2).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close() //
            var page = ""
            if (btnPage.text.toString() == "P1") {
                page = "P2"
            } else if (btnPage.text.toString() == "P2") {
                page = "P3"
            } else if (btnPage.text.toString() == "P3") {
                page = "P4"
            } else if (btnPage.text.toString() == "P4") {
                page = "P5"
            }


            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you add " + page + " for " + PaperPicture.STUDENT_NAME)
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {

                btnPage.setText(page)


                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var out = Environment.getExternalStorageDirectory()
                out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
                val photoURI =
                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", out)
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(i, 100)
                confirmDialog.dismiss()

            }



            dlgconfirm.btnNo.setOnClickListener {
                Log.e("NO", "AAA")
                confirmDialog.dismiss()
            }
        } else {

            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you like to overwrite")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {
                val src = FileInputStream(file1).channel
                val dst = FileOutputStream(file2).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close() //
                confirmDialog.dismiss()
            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }
        } //        if (btnPage.text.toString() == "P1") btnPage.setText("P2")
        //        else if (btnPage.text.toString() == "P2") btnPage.setText("P3")
        //        else if (btnPage.text.toString() == "P3") btnPage.setText("P4")
        //        else if (btnPage.text.toString() == "P4") btnPage.setText("P5")

    }


    fun SetUpGroupAdapter() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewpicture!!.listPaperPic.layoutManager = layoutmanager
        grpAdapter = GroupAdapter(context, grpList)
        viewpicture!!.listPaperPic.adapter = grpAdapter
    }

    companion object {
        var viewpicture: View? = null
        var containerGlobal: ViewGroup? = null
        var grpList = arrayListOf<GrpModel>()
        var grpAdapter: GroupAdapter? = null;
        var inputDialogKeyword: AlertDialog? = null
        var keyWordAdapter: KeyWordAdapter? = null;
        var listKeyWord = arrayListOf<KeywordModel>()

        fun ShowPicture(grpNumber: String, section: String, keyword: String) {

            Log.e("section111", section)
            try {
                val path = "/storage/emulated/0/Group/" + section + "/" + grpNumber
                viewpicture!!.imageGroup.setRotation(0f)
                val f: File = File(path, keyword + ".jpg")
                if (f.exists()) {
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    viewpicture!!.imageGroup.setImageBitmap(b)
                } else {
                    val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    viewpicture!!.imageGroup.setImageBitmap(b)
                }


                // val img = findViewById<View>(R.id.imgStudent) as ImageView

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }


        var x = 2;

    }


}
