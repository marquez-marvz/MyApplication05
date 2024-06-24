package com.example.myapplication05.testpaper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
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
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.*
import com.ortiz.touchview.BuildConfig
import com.ortiz.touchview.TouchImageView
import kotlinx.android.synthetic.main.keyword_main.view.*
import kotlinx.android.synthetic.main.paperpic_row.view.btnLastName
import kotlinx.android.synthetic.main.test_capture.view.*
import kotlinx.android.synthetic.main.test_capture.view.btnCapture
import kotlinx.android.synthetic.main.test_capture.view.btnDeletePic
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.io.*
import java.nio.channels.FileChannel

class TestCapture : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        viewcapture = inflater.inflate(R.layout.test_capture, container, false)
        containerGlobal = container
        var context = container!!.context
        val db: DatabaseHandler = DatabaseHandler(context)
        currentCaptureSection = db.GetCurrentSection();

        SetSpinnerAdapter()
        SetDefaultSection()
        var CONTENT_REQUEST = 1337;
        var output: File? = null;
        viewcapture!!.imgStudent.setMaxZoom(10.0f)

        varbtnPage1 = viewcapture!!.findViewById(R.id.btnPage1) as Button
        varbtnPage2 = viewcapture!!.findViewById(R.id.btnPage2) as Button
        varbtnPage3 = viewcapture!!.findViewById(R.id.btnPage3) as Button
        varbtnPage4 = viewcapture!!.findViewById(R.id.btnPage4) as Button
        varbtnPage5 = viewcapture!!.findViewById(R.id.btnPage5) as Button
        varbtnPage6 = viewcapture!!.findViewById(R.id.btnPage6) as Button
        varbtnPage7 = viewcapture!!.findViewById(R.id.btnPage7) as Button
        varbtnPage8 = viewcapture!!.findViewById(R.id.btnPage8) as Button
        varbtnPage9 = viewcapture!!.findViewById(R.id.btnPage9) as Button
        varbtnPage10 = viewcapture!!.findViewById(R.id.btnPage10) as Button



        ViewRecord()
        var key = db.GetNewDefaultKeyWord("IND", currentCaptureSection)
        viewcapture!!.btnPaperKeyWord.setText(key)
        viewcapture!!.imgStudent.setImageDrawable(null);

        viewcapture!!.btnDeletePic.setOnClickListener {
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()

            if (viewcapture!!.btnPage1.text.toString() == "P2") {
                keyword = keyword + "-2"
            } else if (viewcapture!!.btnPage1.text.toString() == "P3") {
                keyword = keyword + "-3"
            } else if (viewcapture!!.btnPage1.text.toString() == "P4") {
                keyword = keyword + "-4"
            } else if (viewcapture!!.btnPage1.text.toString() == "P5") {
                keyword = keyword + "-5"
            }
            val fileName = keyword + ".jpg"
            val sdCard = Environment.getExternalStorageDirectory()
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            val f =
                File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME + "/" + fileName)

            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you like to overwrite")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {
                f.delete()
                confirmDialog.dismiss()
            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }

        }

        fun btnNoName() {}
        viewcapture!!.btnNoName.setOnClickListener {

            viewcapture!!.txtStudentName.text = "NoName"
            PAPER_FILENAME = "_NONAME"
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME)
            ShowButtonPages(TestCapture.PAPER_FILENAME, TestCapture.currentCaptureSection, keyword)
            CURRENT_PAGE= "PAGE 1"
            ChangeColorPageButton("PAGE 1")

        }

        fun btnCapture() {}

        viewcapture!!.btnCapture.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var out = Environment.getExternalStorageDirectory()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
            CURRENT_PAGE = GetCurrentPage(PAPER_FILENAME, section, keyword) // val photoURI =
            val photoURI =
                FileProvider.getUriForFile(context, "com.ortiz.touchview.provider", out) // FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", out)
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(i, 100)
        }

        viewcapture!!.btnSearchSecond.setOnClickListener() {
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            UpdateListContent(context, "D-J")
            adapterPaperPic!!.notifyDataSetChanged()
        }
        viewcapture!!.btnSearchThird.setOnClickListener() {
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            UpdateListContent(context, "K-O")
            adapterPaperPic!!.notifyDataSetChanged()
        }
        viewcapture!!.btnSearchFourth.setOnClickListener() {
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            UpdateListContent(context, "P-R")
            adapterPaperPic!!.notifyDataSetChanged()
        }
        viewcapture!!.btnSearchFifth.setOnClickListener() {
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            UpdateListContent(context, "S-Z")
            adapterPaperPic!!.notifyDataSetChanged()
        }

        viewcapture!!.btnSearchFirst.setOnClickListener() {
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            UpdateListContent(context, "A-C")
            adapterPaperPic!!.notifyDataSetChanged()
        }

        viewcapture!!.btnAbsent.setOnClickListener() {
            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz)
                .setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {
                val f: File = File("/storage/emulated/0/Picture/", "absent.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                viewcapture!!.imgStudent!!.setImageBitmap(b)
                val draw = viewcapture!!.imgStudent.drawable as BitmapDrawable
                var bitmap = draw.bitmap

                var outStream: FileOutputStream? = null
                val sdCard = Environment.getExternalStorageDirectory()
                val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
                val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
                dir2.mkdirs() //
                //        Log.e("Hello", )
                // Log.e("Hello", sdCard.absolutePath)
                var out = Environment.getExternalStorageDirectory()

                //

                var keyword = viewcapture!!.btnPaperKeyWord.text.toString()

                if (viewcapture!!.btnPage1.text.toString() == "P2") {
                    keyword = keyword + "-2"
                } else if (viewcapture!!.btnPage1.text.toString() == "P3") {
                    keyword = keyword + "-3"
                } else if (viewcapture!!.btnPage1.text.toString() == "P4") {
                    keyword = keyword + "-4"
                } else if (viewcapture!!.btnPage1.text.toString() == "P5") {
                    keyword = keyword + "-5"
                }

                // Log.e("DDD", dir.toString())
                val fileName = keyword + ".jpg"
                val outFile = File(dir2, fileName)
                outStream = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.flush()
                outStream.close()
                inputDialog.dismiss()
            }
            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }
        }

        viewcapture!!.btnNoPage2.setOnClickListener() {
            val dlgquiz = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz)
                .setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {
                val f: File = File("/storage/emulated/0/Picture/", "nopage2.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                viewcapture!!.imgStudent!!.setImageBitmap(b)
                val draw = viewcapture!!.imgStudent.drawable as BitmapDrawable
                var bitmap = draw.bitmap

                var outStream: FileOutputStream? = null
                val sdCard = Environment.getExternalStorageDirectory()
                val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
                val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
                dir2.mkdirs() //
                //        Log.e("Hello", )
                // Log.e("Hello", sdCard.absolutePath)
                var out = Environment.getExternalStorageDirectory()

                var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
                keyword = keyword + "-2"

                val fileName = keyword + ".jpg"
                val outFile = File(dir2, fileName)
                outStream = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream!!)
                outStream!!.flush()
                outStream!!.close()
                inputDialog.dismiss()
            }
            dlgquiz.btnNo.setOnClickListener {
                inputDialog.dismiss()
            }
        }

        viewcapture!!.btnImport.setOnClickListener() { //            dlgPicture = LayoutInflater.from(context).inflate(R.layout.general_pic, null)
            //            val mBuilder = AlertDialog.Builder(context).setView(dlgPicture)
            //                .setTitle("")
            //            val inputDialog = mBuilder.show()
            //            inputDialog.setCanceledOnTouchOutside(false); //
            //            GeneralPic.ListFiles(currentCaptureSection) //            GeneralPic.section = SECTION
            //            val layoutmanager = LinearLayoutManager(context)
            //            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            //            dlgPicture!!.lstGeneralFile.layoutManager = layoutmanager
            //            adapterGeneral = GeneralAdapter(context, GeneralPic.fileList)
            //            dlgPicture!!.lstGeneralFile.adapter = adapterGeneral
            //            dlgPicture!!.btnDeletePic.isVisible= false
            //
            //            dlgPicture!!.btnCapture.setText("USE")
            //
            //            dlgPicture!!.cboGeneralActivity.isVisible= false
            //
            //            dlgPicture!!.btnCapture.setOnClickListener {
            //                val path = "/storage/emulated/0/General/" + currentCaptureSection
            //                var fileName =dlgPicture!!.txtGeneralKeyword.text.toString() + ".jpg"
            //                var f = File(path, fileName)
            //
            //                if (f!!.exists()) {
            //                    val b = BitmapFactory.decodeStream(FileInputStream(f))
            //                    viewcapture!!.imgStudent.setImageBitmap(b)
            //                } else {
            //                    val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
            //                    val b = BitmapFactory.decodeStream(FileInputStream(f))
            //                    viewcapture!!.imgStudent!!.setImageBitmap(b)
            //                }
            //
            //                val page = viewcapture!!.btnPage.text.toString()
            //                var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            //
            //                if (page == "") {
            //                    keyword = keyword + ".jpg"
            //                } else if (page == "P1") {
            //                    keyword =  keyword + ".jpg"
            //                } else if (page == "P2") {
            //                    keyword = keyword + "-2" + ".jpg"
            //                } else if (page == "P3") {
            //                    keyword = keyword + "-3" + ".jpg"
            //                } else if (page == "P4") {
            //                    keyword = keyword + "-4" + ".jpg"
            //                } else if (page == "P5") {
            //                    keyword = keyword + "-5" + ".jpg"
            //                }
            //
            //                var studName = viewcapture!!.txtStudentName.text.toString()
            //                val file2 = File("/storage/emulated/0/Quiz/" + currentCaptureSection + "/" + studName, keyword)
            //                val file1 =  File ("/storage/emulated/0/General/" + currentCaptureSection, fileName)
            //
            //
            //                val src = FileInputStream(file1).channel
            //                val dst = FileOutputStream(file2).channel
            //                dst.transferFrom(src, 0, src.size())
            //                src.close()
            //                dst.close()
            //
            //
            //            }
        }

        //        viewcapture!!.btnPage1.setOnLongClickListener() {
        ////            viewcapture!!.btnPage.setText("P1")
        ////            var studName = viewcapture!!.txtStudentName.text.toString()
        ////            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
        ////            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
        ////            ShowPicture(studName, section, keyword, viewcapture!!.btnPage.text.toString())
        ////            true
        //        }
        fun btnWithPicture() {}
        viewcapture!!.btnWithPicture.setOnClickListener() {


            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<EnrolleModel>
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            student = db2.GetEnrolleList("LAST_ORDER", section)
            list.clear()
            for (e in student) {
                var fileName = e!!.lastname + "_" + e!!.studentno
                var stat = PictureCheck(fileName, section, keyword)
                if (stat == true) {
                    list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
                }

            }
            adapterPaperPic!!.notifyDataSetChanged()
        }


        fun btnNoPicture() {}
        viewcapture!!.btnNoPicture.setOnClickListener() {


            val db2: DatabaseHandler = DatabaseHandler(context)
            val student: List<EnrolleModel>
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            student = db2.GetEnrolleList("LAST_ORDER", section)
            list.clear()
            for (e in student) {
                var fileName = e!!.lastname + "_" + e!!.studentno
                var stat = PictureCheck(fileName, section, keyword)
                if (stat == false) {
                    list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
                }

            }
            adapterPaperPic!!.notifyDataSetChanged()
        }

//        fun btnNoNamePage() {}
//        viewcapture!!.btnNoNamePage.setOnClickListener() {
//           if  btnNoNamePage.text == "PAGE 1 OF 1"
//        }


        fun btnPage1() {}
        viewcapture!!.btnPage1.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME)
            ChangeColorPageButton("PAGE 1")
        }

        viewcapture!!.btnPage2.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-2")
            ChangeColorPageButton("PAGE 2")
        }

        viewcapture!!.btnPage3.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-3")
            ChangeColorPageButton("PAGE 3")
        }

        viewcapture!!.btnPage4.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-4")
            ChangeColorPageButton("PAGE 4")
        }

        viewcapture!!.btnPage5.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-5")
            ChangeColorPageButton("PAGE 5")
        }

        viewcapture!!.btnPage6.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-6")
            ChangeColorPageButton("PAGE 6")
        }


        viewcapture!!.btnPage7.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-7")
            ChangeColorPageButton("PAGE 7")
        }



        viewcapture!!.btnPage8.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-8")
            ChangeColorPageButton("PAGE 8")
        }



        viewcapture!!.btnPage9.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-9")
            ChangeColorPageButton("PAGE 9")
        }

        viewcapture!!.btnPage10.setOnClickListener() {
            var studName = viewcapture!!.txtStudentName.text.toString()
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
            ShowPicture(section, keyword, PAPER_FILENAME + "-10")
            ChangeColorPageButton("PAGE 10")
        }











        fun btnKeyword() {}
        viewcapture!!.btnPaperKeyWord.setOnClickListener() {
            var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
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
            var category = "IND"
            UpdateListKeyWord(category, period, section, context)

            val arrSection: ArrayList<String> = db.GetAllKeyWord()
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dlgquiz.cboKeyWord.setAdapter(sectionAdapter);
            dlgquiz.txtKeyWord.setText("")



            dlgquiz.btnSavekeyWord.setOnClickListener {
                var keyword = dlgquiz.txtKeyWord.text.toString()
                val newPeriod = dlgquiz.btnPeriod.text.toString()
                var category = "IND"
                db.SaveNewKeyword(keyword, currentCaptureSection, category, newPeriod)
                UpdateListKeyWord("IND", newPeriod, section, context)
                keyWordAdapter!!.notifyDataSetChanged()
                val sdCard = Environment.getExternalStorageDirectory()
                val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + keyword)
                dir2.mkdirs() //

            }

            dlgquiz.btnPeriod.setOnClickListener {
                var newPeriod = dlgquiz.btnPeriod.text.toString()
                if (newPeriod == "FIRST") {
                    dlgquiz.btnPeriod.setText("SECOND")
                } else {
                    dlgquiz.btnPeriod.setText("FIRST")
                }
                newPeriod = dlgquiz.btnPeriod.text.toString()
                var category = "IND"
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
                            dlgquiz.txtKeyWord.setText("sss")
                        }
                    }
                }

            //db.UpdateDefaultKeyword(viewcapture!!.btnPaperKeyWord.text.toString())
        }

        viewcapture!!.cboSectionPic.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val db: TableActivity = TableActivity(context)
                    currentCaptureSection =
                        viewcapture!!.cboSectionPic.getSelectedItem().toString();
                    val mydb: DatabaseHandler = DatabaseHandler(context)
                    mydb.SetCurrentSection(currentCaptureSection)
                    UpdateListContent(context, "LAST_ORDER")
                    adapterPaperPic!!.notifyDataSetChanged()
                    viewcapture!!.imgStudent.setImageDrawable(null);
                    var key = db.GetNewDefaultKeyWord("IND", currentCaptureSection)

                    viewcapture!!.btnPaperKeyWord.setText(key)
                    viewcapture!!.txtStudentName.setText("")
                }
            }
        fun btnRotate() {}
        viewcapture!!.btnRotate.setOnClickListener { //            var angle = imgStudent22.getRotation() + 90
            //            imgStudent22.setRotation(angle);


            val draw = viewcapture!!.imgStudent.drawable as BitmapDrawable
            var bitmap = draw.bitmap

            bitmap = rotateBitmap(bitmap, 90f)

            var outStream: FileOutputStream? = null
            val sdCard = Environment.getExternalStorageDirectory()
            val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
            val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
            dir2.mkdirs() //
            //        Log.e("Hello", )
            // Log.e("Hello", sdCard.absolutePath)
            var out = Environment.getExternalStorageDirectory()

            //

            var keyword = viewcapture!!.btnPaperKeyWord.text.toString()

            if (viewcapture!!.btnPage1.text.toString() == "P2") {
                keyword = keyword + "-2"
            } else if (viewcapture!!.btnPage1.text.toString() == "P3") {
                keyword = keyword + "-3"
            } else if (viewcapture!!.btnPage1.text.toString() == "P4") {
                keyword = keyword + "-4"
            } else if (viewcapture!!.btnPage1.text.toString() == "P5") {
                keyword = keyword + "-5"
            }


            //            val dir = File(sdCard.absolutePath + "/Picture/" )
            //            dir.mkdirs()
            // Log.e("DDD", dir.toString())
            val fileName = keyword + ".jpg"
            val outFile = File(dir2, fileName)
            outStream = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream!!)
            outStream!!.flush()
            outStream!!.close()
            val b = BitmapFactory.decodeStream(FileInputStream(outFile))
            viewcapture!!.imgStudent.setImageBitmap(b)

            //            imgStudent22.setRotation(0f);

        }

        return viewcapture
    }

    private fun SetSpinnerAdapter() {
        var context = containerGlobal!!.context
        val arrSection: Array<String> = Util.GetSectionList(context)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewcapture!!.cboSectionPic.setAdapter(sectionAdapter);
    }

    private fun SetDefaultSection() {
        var context = containerGlobal!!.context
        val db: DatabaseHandler = DatabaseHandler(context)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, context)
        viewcapture!!.cboSectionPic.setSelection(index)
    }


    fun ShowPicture(section: String, keyword: String, fileName: String) {

        Log.e("section111", section)
        try {
            val path = "/storage/emulated/0/Quiz/" + section + "/" + keyword

            var f: File? = null
            f = File(path, fileName + ".jpg")

            //            if (page == "") {
            //                f = File(path, keyword + ".jpg")
            //            } else if (page == "P1") {
            //                f = File(path, keyword + ".jpg")
            //            } else if (page == "P2") {
            //                f = File(path, keyword + "-2" + ".jpg")
            //            } else if (page == "P3") {
            //                f = File(path, keyword + "-3" + ".jpg")
            //            } else if (page == "P4") {
            //                f = File(path, keyword + "-4" + ".jpg")
            //            } else if (page == "P5") {
            //                f = File(path, keyword + "-5" + ".jpg")
            //            }

            if (f!!.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                viewcapture!!.imgStudent.setImageBitmap(b)
            } else {
                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                viewcapture!!.imgStudent!!.setImageBitmap(b)
            }


            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


    }

    fun UpdateListContent(context: Context, category: String = "ALL") {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = viewcapture!!.cboSectionPic.getSelectedItem().toString();

        student = db2.GetEnrolleList(category, section)
        list.clear()
        for (e in student) {
            list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }
    }


    fun ViewRecord() {
        var context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewcapture!!.listPaperPic.layoutManager = layoutmanager
        adapterPaperPic = PaperPicAdapter(context, list)
        viewcapture!!.listPaperPic.adapter = adapterPaperPic
    }


    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


    fun Savefile(name: String?, path: String?) {
        val direct =
            File(Environment.getExternalStorageDirectory().toString() + "/MyAppFolder/MyApp/")
        val file = File("/storage/emulated/0/Picture/", "one.png")
        if (!direct.exists()) {
            direct.mkdir()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
                val src: FileChannel = FileInputStream(path).getChannel()
                val dst: FileChannel = FileOutputStream(file).getChannel()
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) { //        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        var context = containerGlobal!!.context
        val path = "/storage/emulated/0"
        STUDENT_NAME = viewcapture!!.txtStudentName.text.toString()
        val f: File = File(path, "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        viewcapture!!.imgStudent.setImageBitmap(b)
        var keyword = viewcapture!!.btnPaperKeyWord.text.toString()
        val sdCard = Environment.getExternalStorageDirectory()
        val section = viewcapture!!.cboSectionPic.getSelectedItem().toString();
        val dir = File(sdCard.absolutePath + "/Quiz/" + section)
        dir.mkdirs()


        val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + keyword)
        dir2.mkdirs() //
        //        Log.e("Hello", )

        Log.e("Hello444", sdCard.absolutePath)

        Log.e("Hello444", sdCard.absolutePath + "/Quiz/" + section + "/" + TestCapture.STUDENT_NAME)
        var out = Environment.getExternalStorageDirectory()

        //

        var newFileName = ""
        if (CURRENT_PAGE == "PAGE 1") {
            newFileName = PAPER_FILENAME
        } else if (CURRENT_PAGE == "PAGE 2") {
            newFileName = PAPER_FILENAME + "-2"
        } else if (CURRENT_PAGE == "PAGE 3") {
            newFileName = PAPER_FILENAME + "-3"
        } else if (CURRENT_PAGE == "PAGE 4") {
            newFileName = PAPER_FILENAME + "-4"
        } else if (CURRENT_PAGE == "PAGE 5") {
            newFileName = PAPER_FILENAME + "-5"
        }


        val file1 = File(out, "temp.jpg")
        val file2 = File(dir2, newFileName + ".jpg")

        if (!file2.exists()) {
            val src = FileInputStream(file1).channel
            val dst = FileOutputStream(file2).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close() //
            var page = ""
            if (CURRENT_PAGE == "PAGE 1") {
                page = "PAGE 2"
            } else if (CURRENT_PAGE == "PAGE 2") {
                page = "PAGE 3"
            } else if (CURRENT_PAGE == "PAGE 3") {
                page = "PAGE 4"
            } else if (CURRENT_PAGE == "PAGE 4") {
                page = "PAGE 5"
            }


            val dlgconfirm = LayoutInflater.from(context).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgconfirm)
                .setTitle("Do you add " + page + " for " + TestCapture.STUDENT_NAME)
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {


                CURRENT_PAGE = page
                ChangeColorPageButton(page)

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
        adapterPaperPic!!.notifyDataSetChanged()
        ShowButtonPages(PAPER_FILENAME, section, keyword)
    }


    companion object {
        var viewcapture: View? = null
        var containerGlobal: ViewGroup? = null
        var currentCaptureSection = ""
        var currentKeyword = ""
        var adapterPaperPic: PaperPicAdapter? = null;
        var adapterGrpPic: PaperGroupAdaopter? = null;
        var keyWordAdapter: KeyWordAdapter? = null;
        var adapterGeneral: GeneralAdapter? = null;

        var studentAdapter: ArrayAdapter<String>? = null;
        var list = arrayListOf<EnrolleModel>()
        var grpList = arrayListOf<GrpModel>()
        var txtStudent: TextView? = null;
        var myKeyword: Button? = null;
        var btnPageNo: Button? = null;
        var myImage: TouchImageView? = null;
        var STUDENT_NAME = ""
        var KEYWORD = ""
        var SECTION = ""
        var PAPER_FILENAME = ""
        var CURRENT_PAGE = ""

        var inputDialogKeyword: AlertDialog? = null
        var varbtnPaperKeyWord: Button? = null;
        var listKeyWord = arrayListOf<KeywordModel>()
        var dlgPicture: View? = null


        var varbtnPage1: Button? = null;
        var varbtnPage2: Button? = null;
        var varbtnPage3: Button? = null;
        var varbtnPage4: Button? = null;
        var varbtnPage5: Button? = null;
        var varbtnPage6: Button? = null;
        var varbtnPage7: Button? = null;
        var varbtnPage8: Button? = null;
        var varbtnPage9: Button? = null;
        var varbtnPage10: Button? = null;


        fun UpdateListKeyWord(category: String, period: String, section: String, context: Context) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val keyword: List<KeywordModel>


            keyword = db2.GetKeywordList(category, section, period)
            listKeyWord.clear()
            for (e in keyword) {
                listKeyWord.add(KeywordModel(e.Section, e.Keyword, e.GradingPeriod, e.Category, e.DefautKeyword))
            }
        }


        fun ShowButtonPages(fileName: String, section: String, keyword: String) {
            varbtnPage1!!.isVisible = false
            varbtnPage2!!.isVisible = false
            varbtnPage3!!.isVisible = false
            varbtnPage4!!.isVisible = false
            varbtnPage5!!.isVisible = false
            varbtnPage6!!.isVisible = false
            varbtnPage7!!.isVisible = false
            varbtnPage8!!.isVisible = false
            varbtnPage9!!.isVisible = false
            varbtnPage10!!.isVisible = false


            val path = "/storage/emulated/0/Quiz/" + section + "/" + keyword

            TestCapture.viewcapture!!.imgStudent.setRotation(0f)
            val f: File = File(path, fileName + ".jpg")
            if (f.exists()) {
                varbtnPage1!!.isVisible = true
            }


            val f2: File = File(path, fileName + "-2" + ".jpg")
            if (f2.exists()) {
                varbtnPage2!!.isVisible = true
            }


            val f3: File = File(path, fileName + "-3" + ".jpg")
            if (f3.exists()) {
                varbtnPage3!!.isVisible = true
            }


            val f4: File = File(path, fileName + "-4" + ".jpg")
            if (f4.exists()) {
                varbtnPage4!!.isVisible = true
            }

            val f5: File = File(path, fileName + "-5" + ".jpg")
            if (f5.exists()) {
                varbtnPage5!!.isVisible = true
            }

            val f6: File = File(path, fileName + "-6" + ".jpg")
            if (f6.exists()) {
                varbtnPage6!!.isVisible = true
            }

            val f7: File = File(path, fileName + "-7" + ".jpg")
            if (f7.exists()) {
                varbtnPage7!!.isVisible = true
            }

            val f8: File = File(path, fileName + "-8" + ".jpg")
            if (f8.exists()) {
                varbtnPage8!!.isVisible = true
            }

            val f9: File = File(path, fileName + "-9" + ".jpg")
            if (f9.exists()) {
                varbtnPage9!!.isVisible = true
            }

            val f10: File = File(path, fileName + "-10" + ".jpg")
            if (f10.exists()) {
                varbtnPage10!!.isVisible = true
            }


        }


        fun GetCurrentPage(fileName: String, section: String, keyword: String): String {

            val path = "/storage/emulated/0/Quiz/" + section + "/" + keyword
            var currentPage = "PAGE 1"
            val f: File = File(path, fileName + ".jpg")
            if (f.exists()) {
                currentPage = "PAGE 2"
            }


            val f2: File = File(path, fileName + "-2" + ".jpg")
            if (f2.exists()) {
                currentPage = "PAGE 3"
            }


            val f3: File = File(path, fileName + "-3" + ".jpg")
            if (f3.exists()) {
                currentPage = "PAGE 4"

            }


            val f4: File = File(path, fileName + "-4" + ".jpg")
            if (f4.exists()) {
                currentPage = "PAGE 5"
            }

            val f5: File = File(path, fileName + "-5" + ".jpg")
            if (f5.exists()) {
                currentPage = "PAGE 6"
            }
            return currentPage
        }


        fun ChangeColorPageButton(page: String) {
CURRENT_PAGE = page
            varbtnPage1!!.setBackgroundColor(Color.parseColor("#69F0AE"))
            varbtnPage1!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage2!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage3!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage4!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage5!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage6!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage7!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage8!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage9!!.setBackgroundResource(android.R.drawable.btn_default);
            varbtnPage10!!.setBackgroundResource(android.R.drawable.btn_default);

            if (page == "PAGE 1") {
                varbtnPage1!!.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else if (page == "PAGE 2") {
                varbtnPage2!!.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else if (page == "PAGE 3") {
                varbtnPage3!!.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else if (page == "PAGE 4") {
                varbtnPage4!!.setBackgroundColor(Color.parseColor("#69F0AE"))
            } else if (page == "PAGE 5") {
                varbtnPage5!!.setBackgroundColor(Color.parseColor("#69F0AE"))

        } else if (page == "PAGE 6") {
            varbtnPage6!!.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else if (page == "PAGE 7") {
            varbtnPage7!!.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else if (page == "PAGE 8") {
            varbtnPage8!!.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else if (page == "PAGE 9") {
            varbtnPage9!!.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else if (page == "PAGE 10") {
            varbtnPage10!!.setBackgroundColor(Color.parseColor("#69F0AE"))
        }


    }

        fun PictureCheck(fileName: String, section: String, keyword: String): Boolean {
            val path = "/storage/emulated/0/Quiz/" + section + "/" + keyword

            TestCapture.viewcapture!!.imgStudent.setRotation(0f)
            val f: File = File(path, fileName + ".jpg")
            if (f.exists()) {
                return true
            } else {
                return false
            }

        }


        //

    }
}