//package com.example.myapplication05
//
//class PaperPicture {}

package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.example.myapplication05.testpaper.PaperPicAdapter
import com.ortiz.touchview.BuildConfig
import com.ortiz.touchview.TouchImageView //import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.general_pic.view.*
import kotlinx.android.synthetic.main.keyword_main.view.*
import kotlinx.android.synthetic.main.paperpic.* //import kotlinx.android.synthetic.main.qrcode.* //import kotlinx.android.synthetic.main.qrcode.btnAdd //import kotlinx.android.synthetic.main.qrcode.btnShow
import kotlinx.android.synthetic.main.paperpic.btnCapture
import kotlinx.android.synthetic.main.paperpic.btnDeletePic
import kotlinx.android.synthetic.main.paperpic.btnRotate
import kotlinx.android.synthetic.main.paperpic.imgStudent22
import kotlinx.android.synthetic.main.util_confirm.view.* //import kotlinx.android.synthetic.main.qrcode.cboStudent //import kotlinx.android.synthetic.main.qrcode.imgStudent22 //import kotlinx.android.synthetic.main.qrcode.txtPaperSection
import java.io.*
import java.nio.channels.FileChannel


class PaperPicture : AppCompatActivity() {
    var adapterPaperPic: PaperPicAdapter? = null;
    var adapterGrpPic: PaperGroupAdaopter? = null;
    var keyWordAdapter: KeyWordAdapter? = null;
    var adapterGeneral: GeneralAdapter? = null;
    val db2: DatabaseHandler = DatabaseHandler(this)

    var studentAdapter: ArrayAdapter<String>? = null;
    var list = arrayListOf<EnrolleModel>()
    val myContext: Context = this;
    var grpList = arrayListOf<GrpModel>()

    companion object {
        var txtStudent: TextView? = null;
        var myKeyword: Button? = null;
        var btnPageNo: Button? = null;
        var myImage: TouchImageView? = null;
        var STUDENT_NAME = ""
        var KEYWORD = ""
        var SECTION = ""
        var inputDialogKeyword: AlertDialog? = null
        var varbtnPaperKeyWord: Button? = null;
        var listKeyWord = arrayListOf<KeywordModel>()
        var dlgPicture: View? = null

        fun UpdateListKeyWord(category: String, period: String, section: String, context: Context) {
            val db2: DatabaseHandler = DatabaseHandler(context)
            val keyword: List<KeywordModel>


            keyword = db2.GetKeywordList(category, section, period)
            listKeyWord.clear()
            for (e in keyword) {
                listKeyWord.add(KeywordModel(e.Section, e.Keyword, e.GradingPeriod, e.Category, e.DefautKeyword))
            }
        } //
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paperpic)
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        SetSpinnerAdapter()
        SetDefaultSection()
        SECTION = currentSection
        KEYWORD = btnPaperKeyWord.text.toString() //        LoadStudents("LAST_ORDER")
        //   UpdateListContent(myContext, "LAST_ORDER")
        var CONTENT_REQUEST = 1337;
        var output: File? = null;
        imgStudent22.setMaxZoom(10.0f)
        ShowGroupList(this, currentSection)
        ViewRecord()
        var key = db.GetNewDefaultKeyWord(btnCategory.text.toString(), currentSection)
        btnPaperKeyWord.setText(key)
        imgStudent22.setImageDrawable(null);


        txtStudent = findViewById(R.id.txtStudentName) as TextView
        btnPageNo = findViewById(R.id.btnPage) as Button
        myImage = findViewById(R.id.imgStudent22) as TouchImageView
        myKeyword = findViewById(R.id.btnPaperKeyWord) as Button
        varbtnPaperKeyWord = findViewById(R.id.btnPaperKeyWord) as Button
        Util.GENERALMODE = "PAPER"



        btnDeletePic.setOnClickListener {
            var keyword = btnPaperKeyWord.text.toString()

            if (btnPage.text.toString() == "P2") {
                keyword = keyword + "-2"
            } else if (btnPage.text.toString() == "P3") {
                keyword = keyword + "-3"
            } else if (btnPage.text.toString() == "P4") {
                keyword = keyword + "-4"
            } else if (btnPage.text.toString() == "P5") {
                keyword = keyword + "-5"
            }
            val fileName = keyword + ".jpg"
            val sdCard = Environment.getExternalStorageDirectory()
            val section = cboSectionPic.getSelectedItem().toString();
            val f =
                File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME + "/" + fileName)

            val dlgconfirm = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgconfirm).setTitle("Do you like to overwrite")
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {
                f.delete()
                confirmDialog.dismiss()
            }

            dlgconfirm.btnNo.setOnClickListener {
                confirmDialog.dismiss()
            }


            //            val dir = File(sdCard.absolutePath + "/Picture/" )
            //            dir.mkdirs()
            // Log.e("DDD", dir.toString())

            //            val outFile = File(dir2, fileName)
            //            outStream = FileOutputStream(outFile)
            //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            //            outStream.flush()
            //            outStream.close()
            //            true
        }
        btnCapture.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var out = Environment.getExternalStorageDirectory()
            out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
            val photoURI =
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", out)
            i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(i, 100)

            //            val builder = VmPolicy.Builder()
            //            StrictMode.setVmPolicy(builder.build())
            //            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //
            //            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
            //
            //            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

            // start the image capture Intent

            // start the image capture Intent

            //
            //            val REQUEST_ACTION_CAMERA = 9
            //
            //
            //            val cameraImgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //            cameraImgIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", File("your_file_name_with_dir")))
            //            startActivityForResult(cameraImgIntent, REQUEST_ACTION_CAMERA) //            val cameraImgIntent =
            //            //                Intent(MediaStore.ACTION_IMAGE_CAPTURE) //
            //            //            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            //
            //            output = File(dir, "CameraContentDemo.jpeg")
            //            cameraImgIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output))
            //
            //            startActivityForResult(cameraImgIntent, CONTENT_REQUEST)
            //                       val dirName ="/storage/emulated/0/Picture/one.png"
            //            //            cameraImgIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", File(dirName)))
            //            startActivityForResult(cameraImgIntent, 100) //            val scanner = IntentIntegrator(this)
            //            //            Log.e("SSS", "HE=l-LLO123")
            //            //            scanner.initiateScan()
            //
            //            // Creating folders for Image
            //            // Creating folders for Image
            //            //            val imageFolderPath =
            //            //                (Environment.getExternalStorageDirectory().toString() + "/AutoFare")
            //            //            val imagesFolder = File(imageFolderPath)
            //            imagesFolder.mkdirs()
            //
            //            // Generating file name
            //
            //            // Generating file name
            //          val   imageName = Date().toString() + ".png"

            // Creating image here

            // Creating image here

            //            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //
            //            //Folder is already created
            //
            //            //Folder is already created
            //            val dirName ="/storage/emulated/0/Picture/one.png"
            //           //     (Environment.getExternalStorageDirectory().path + "/MyAppFolder/MyApp" + n) + ".png"
            //
            //            val uriSavedImage: Uri = Uri.fromFile(File(dirName))
            //            camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
            //            startActivityForResult(camera, 1)
            ////            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            ////            startActivityForResult(camera, 1)


        }

        btnSearchSecond.setOnClickListener() {
            val section = cboSectionPic.getSelectedItem().toString();
            UpdateListContent(this, "D-J")
            adapterPaperPic!!.notifyDataSetChanged()
        }
        btnSearchThird.setOnClickListener() {
            val section = cboSectionPic.getSelectedItem().toString();
            UpdateListContent(this, "K-O")
            adapterPaperPic!!.notifyDataSetChanged()
        }
        btnSearchFourth.setOnClickListener() {
            val section = cboSectionPic.getSelectedItem().toString();
            UpdateListContent(this, "P-R")
            adapterPaperPic!!.notifyDataSetChanged()
        }

        btnSearchFifth.setOnClickListener() {
            val section = cboSectionPic.getSelectedItem().toString();
            UpdateListContent(this, "S-Z")
            adapterPaperPic!!.notifyDataSetChanged()
        }

        btnSearchFirst.setOnClickListener() {
            val section = cboSectionPic.getSelectedItem().toString();
            UpdateListContent(this, "A-C")
            adapterPaperPic!!.notifyDataSetChanged()
        }



        btnAbsent.setOnClickListener() {
            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgquiz).setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {
                val f: File = File("/storage/emulated/0/Picture/", "absent.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imgStudent22!!.setImageBitmap(b)
                val draw = imgStudent22.drawable as BitmapDrawable
                var bitmap = draw.bitmap

                var outStream: FileOutputStream? = null
                val sdCard = Environment.getExternalStorageDirectory()
                val section = cboSectionPic.getSelectedItem().toString();
                val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
                dir2.mkdirs() //
                //        Log.e("Hello", )
                // Log.e("Hello", sdCard.absolutePath)
                var out = Environment.getExternalStorageDirectory()

                //

                var keyword = btnPaperKeyWord.text.toString()

                if (btnPage.text.toString() == "P2") {
                    keyword = keyword + "-2"
                } else if (btnPage.text.toString() == "P3") {
                    keyword = keyword + "-3"
                } else if (btnPage.text.toString() == "P4") {
                    keyword = keyword + "-4"
                } else if (btnPage.text.toString() == "P5") {
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


        btnNoPage2.setOnClickListener() {
            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgquiz).setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            dlgquiz.btnYes.setOnClickListener {
                val f: File = File("/storage/emulated/0/Picture/", "nopage2.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imgStudent22!!.setImageBitmap(b)
                val draw = imgStudent22.drawable as BitmapDrawable
                var bitmap = draw.bitmap

                var outStream: FileOutputStream? = null
                val sdCard = Environment.getExternalStorageDirectory()
                val section = cboSectionPic.getSelectedItem().toString();
                val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
                dir2.mkdirs() //
                //        Log.e("Hello", )
                // Log.e("Hello", sdCard.absolutePath)
                var out = Environment.getExternalStorageDirectory()

                var keyword = btnPaperKeyWord.text.toString()
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

        btnCategory.setOnClickListener() {
            if (btnCategory.text.toString() == "IND") {
                btnCategory.setText("GRP")
                ViewRecordGroup()
            } else {
                btnCategory.setText("IND")
                ViewRecord()
            }

        }


        btnImport.setOnClickListener() {
            dlgPicture = LayoutInflater.from(this).inflate(R.layout.general_pic, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgPicture)
                .setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //
            GeneralPic.ListFiles(SECTION) //            GeneralPic.section = SECTION
            val layoutmanager = LinearLayoutManager(this)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlgPicture!!.lstGeneralFile.layoutManager = layoutmanager
            adapterGeneral = GeneralAdapter(this, GeneralPic.fileList)
            dlgPicture!!.lstGeneralFile.adapter = adapterGeneral
            dlgPicture!!.btnDeletePic.isVisible= false

            dlgPicture!!.btnCapture.setText("USE")

            dlgPicture!!.cboGeneralActivity.isVisible= false

            dlgPicture!!.btnCapture.setOnClickListener {
                val path = "/storage/emulated/0/General/" + SECTION
                var fileName = dlgPicture!!.txtGeneralKeyword.text.toString() + ".jpg"
                var f = File(path, fileName)

                if (f!!.exists()) {
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    imgStudent22.setImageBitmap(b)
                } else {
                    val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                    val b = BitmapFactory.decodeStream(FileInputStream(f))
                    imgStudent22!!.setImageBitmap(b)
                }

                val page = btnPage.text.toString()
                var keyword = btnPaperKeyWord.text.toString()

                if (page == "") {
                    keyword = keyword + ".jpg"
                } else if (page == "P1") {
                    keyword =  keyword + ".jpg"
                } else if (page == "P2") {
                    keyword = keyword + "-2" + ".jpg"
                } else if (page == "P3") {
                    keyword = keyword + "-3" + ".jpg"
                } else if (page == "P4") {
                    keyword = keyword + "-4" + ".jpg"
                } else if (page == "P5") {
                    keyword = keyword + "-5" + ".jpg"
                }

                var studName = txtStudentName.text.toString()
                val file2 = File("/storage/emulated/0/Quiz/" + SECTION + "/" + studName, keyword)
                val file1 =  File ("/storage/emulated/0/General/" + SECTION, fileName)


                val src = FileInputStream(file1).channel
                val dst = FileOutputStream(file2).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()


            }
        }



        btnPage.setOnLongClickListener() {
            btnPage.setText("P1")
            var studName = txtStudentName.text.toString()
            var section = cboSectionPic.getSelectedItem().toString();
            var keyword = btnPaperKeyWord.text.toString()
            ShowPicture(studName, section, keyword, btnPage.text.toString())
            true
        }
        btnPage.setOnClickListener() {
            if (btnPage.text.toString() == "P1") btnPage.setText("P2")
            else if (btnPage.text.toString() == "P2") btnPage.setText("P3")
            else if (btnPage.text.toString() == "P3") btnPage.setText("P4")
            else if (btnPage.text.toString() == "P4") btnPage.setText("P5")
            var studName = txtStudentName.text.toString()
            var section = cboSectionPic.getSelectedItem().toString();
            var keyword = btnPaperKeyWord.text.toString()
            ShowPicture(studName, section, keyword, btnPage.text.toString())
        }

        btnPaperKeyWord.setOnClickListener() {
            var section = cboSectionPic.getSelectedItem().toString();
            val dlgquiz = LayoutInflater.from(this).inflate(R.layout.keyword_main, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgquiz).setTitle("")
            inputDialogKeyword = mBuilder.show()
            inputDialogKeyword!!.setCanceledOnTouchOutside(false); //

            val layoutmanager = LinearLayoutManager(this)
            layoutmanager.orientation = LinearLayoutManager.VERTICAL;
            dlgquiz.lstKeyWord.layoutManager = layoutmanager
            keyWordAdapter = KeyWordAdapter(this, listKeyWord)
            dlgquiz.lstKeyWord.adapter = keyWordAdapter
            var period = Util.GetCurrentGradingPeriod(this)
            dlgquiz.btnPeriod.setText(period)
            var category = btnCategory.text.toString()
            UpdateListKeyWord(category, period, section, myContext)

            val arrSection: ArrayList<String> = db2.GetAllKeyWord()
            var sectionAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dlgquiz.cboKeyWord.setAdapter(sectionAdapter);
            dlgquiz.txtKeyWord.setText("")



            dlgquiz.btnSavekeyWord.setOnClickListener {
                var keyword = dlgquiz.txtKeyWord.text.toString()
                val newPeriod = dlgquiz.btnPeriod.text.toString()
                var category = btnCategory.text.toString()
                db2.SaveNewKeyword(keyword, SECTION, category, newPeriod)
                UpdateListKeyWord("IND", newPeriod, section, myContext)
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
                UpdateListKeyWord(category, newPeriod, section, myContext)
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


            //            dlgquiz.btnNo.setOnClickListener {
            //                inputDialog.dismiss()
            //            }

            /*  var keyword = btnPaperKeyWord.text.toString()
              if (keyword == "QUIZ1") {
                  btnPaperKeyWord.setText("QUIZ2")
              } else if (keyword == "QUIZ2") {
                  btnPaperKeyWord.setText("QUIZ3")
              } else if (keyword == "QUIZ3") {
                  btnPaperKeyWord.setText("QUIZ4")
              } else if (keyword == "QUIZ4") {
                  btnPaperKeyWord.setText("QUIZ5")
              }
              if (keyword == "QUIZ5") {
                  btnPaperKeyWord.setText("MIDTERM")
              }
              db.UpdateDefaultKeyword(btnPaperKeyWord.text.toString())*/
        }


        //        btnPaperKeyWord.setOnLongClickListener() {
        //            btnPaperKeyWord.setText("QUIZ1")
        //            true
        //        }
        //        btnPaperKeyWord.setOnClickListener() {
        //            var keyword = btnPaperKeyWord.text.toString()
        //            if (keyword == "QUIZ1") {
        //                btnPaperKeyWord.setText("QUIZ2")
        //            } else if (keyword == "QUIZ2") {
        //                btnPaperKeyWord.setText("QUIZ3")
        //            } else if (keyword == "QUIZ3") {
        //                btnPaperKeyWord.setText("QUIZ4")
        //            } else if (keyword == "QUIZ4") {
        //                btnPaperKeyWord.setText("QUIZ5")
        //            }
        //            if (keyword == "QUIZ5") {
        //                btnPaperKeyWord.setText("MIDTERM")
        //            }
        //        }


        btnRotate.setOnClickListener {
//            var angle = imgStudent22.getRotation() + 90
//            imgStudent22.setRotation(angle);


            val draw = imgStudent22.drawable as BitmapDrawable
            var bitmap = draw.bitmap

            bitmap = rotateBitmap(bitmap, 90f)

            var outStream: FileOutputStream? = null
            val sdCard = Environment.getExternalStorageDirectory()
            val section = cboSectionPic.getSelectedItem().toString();
            val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
            dir2.mkdirs() //
            //        Log.e("Hello", )
            // Log.e("Hello", sdCard.absolutePath)
            var out = Environment.getExternalStorageDirectory()

            //

            var keyword = btnPaperKeyWord.text.toString()

            if (btnPage.text.toString() == "P2") {
                keyword = keyword + "-2"
            } else if (btnPage.text.toString() == "P3") {
                keyword = keyword + "-3"
            } else if (btnPage.text.toString() == "P4") {
                keyword = keyword + "-4"
            } else if (btnPage.text.toString() == "P5") {
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
            imgStudent22.setImageBitmap(b)

//            imgStudent22.setRotation(0f);

        }

        cboSectionPic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db: TableActivity = TableActivity(myContext)
                SECTION = cboSectionPic.getSelectedItem().toString();
                val mydb: DatabaseHandler = DatabaseHandler(myContext)
                mydb.SetCurrentSection(SECTION)
                UpdateListContent(myContext, "LAST_ORDER")
                adapterPaperPic!!.notifyDataSetChanged()
                imgStudent22.setImageDrawable(null);
                var key = db.GetNewDefaultKeyWord(btnCategory.text.toString(), SECTION)

                btnPaperKeyWord.setText(key)
                txtStudentName.setText("")
            }
        }


    }


    // // //    fun LoadStudents(category: String) { //        Log.e("B201", category) // // //        UpdateListContent(myContext, category) // //        val section = txtPaperSection.text //        var scoreListArray = Array(list.size + 1) { "" } // //        var x = 1 //        scoreListArray[0] = "Select"
    //        for (e in list) {
    //            scoreListArray[x] = e.lastname + "," + e.firstname
    //            x++;
    //        }
    //
    //        studentAdapter = ArrayAdapter<String>(myContext, R.layout.util_spinner, scoreListArray)
    //        studentAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //        cboStudent.setAdapter(studentAdapter);
    //
    //    }

    fun UpdateListContent(context: Context, category: String = "ALL") {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = cboSectionPic.getSelectedItem().toString();

        student = db2.GetEnrolleList(category, section)
        list.clear()
        for (e in student) {
            list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }
    }


    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

//ok
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) { //        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        val path = "/storage/emulated/0"

        val f: File = File(path, "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        imgStudent22.setImageBitmap(b)

        val sdCard = Environment.getExternalStorageDirectory()
        val section = cboSectionPic.getSelectedItem().toString();
        val dir = File(sdCard.absolutePath + "/Quiz/" + section)
        dir.mkdirs()


        val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + STUDENT_NAME)
        dir2.mkdirs() //
        //        Log.e("Hello", )
        Log.e("Hello", sdCard.absolutePath)
        var out = Environment.getExternalStorageDirectory()

        //

        var keyword = btnPaperKeyWord.text.toString()

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


            val dlgconfirm = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgconfirm)
                .setTitle("Do you add " + page + " for " + STUDENT_NAME)
            val confirmDialog = mBuilder.show()
            confirmDialog.setCanceledOnTouchOutside(false);

            dlgconfirm.btnYes.setOnClickListener {

                btnPage.setText(page)


                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var out = Environment.getExternalStorageDirectory()
                out = File(out, "temp.jpg") //val photoURI = Uri.fromFile(out)
                val photoURI =
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", out)
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(i, 100)
                confirmDialog.dismiss()

            }



            dlgconfirm.btnNo.setOnClickListener {
                Log.e("NO", "AAA")
                confirmDialog.dismiss()
            }
        } else {

            val dlgconfirm = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
            val mBuilder =
                AlertDialog.Builder(this).setView(dlgconfirm).setTitle("Do you like to overwrite")
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


    fun ViewRecord() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listPaperPic.layoutManager = layoutmanager

        adapterPaperPic = PaperPicAdapter(this, list)
        listPaperPic.adapter = adapterPaperPic
    }


    fun ViewRecordGroup() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listPaperPic.layoutManager = layoutmanager

        adapterGrpPic = PaperGroupAdaopter(this, grpList)
        listPaperPic.adapter = adapterGrpPic
    }


    fun ShowPicture(studName: String, section: String, keyword: String, page: String = "") {

        Log.e("section111", section)
        try {
            val path = "/storage/emulated/0/Quiz/" + section + "/" + studName
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")
            var f: File? = null
            if (page == "") {
                f = File(path, keyword + ".jpg")
            } else if (page == "P1") {
                f = File(path, keyword + ".jpg")
            } else if (page == "P2") {
                f = File(path, keyword + "-2" + ".jpg")
            } else if (page == "P3") {
                f = File(path, keyword + "-3" + ".jpg")
            } else if (page == "P4") {
                f = File(path, keyword + "-4" + ".jpg")
            } else if (page == "P5") {
                f = File(path, keyword + "-5" + ".jpg")
            }

            if (f!!.exists()) {
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imgStudent22.setImageBitmap(b)
            } else {
                val f: File = File("/storage/emulated/0/Picture/", "person.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imgStudent22!!.setImageBitmap(b)
            }
            Log.e("sss", path)
            Log.e("sss", studName + ".jpg")

            // val img = findViewById<View>(R.id.imgStudent) as ImageView

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


    }

    fun ShowGroupList(context: Context, section: String) {
        val db2: DatabaseHandler = DatabaseHandler(context)
        grpList.clear()

        var grp: List<GrpModel>
        grp = db2.GetGroupList(section)

        for (e in grp) {
            grpList.add(GrpModel(e.grpnumber, e.grpnumber, e.num))
        }
    }


    private fun SetSpinnerAdapter() {
        val arrSection: Array<String> = Util.GetSectionList(this)
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, arrSection)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSectionPic.setAdapter(sectionAdapter);
    }

    private fun SetDefaultSection() {
        var mycontext = this;
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        var index = Util.GetSectionIndex(currentSection, this)
        cboSectionPic.setSelection(index)
    }


}


fun DatabaseHandler.UpdateDefaultKeyword(keyword: String) {
    var sql = """ UPDATE tbinfo
              set  Keyword='$keyword'
  """
    SQLManage(sql)
}


fun GetCursorFieldValue(cursor: Cursor?, field: String): String {
    Log.e("bbb", cursor!!.count.toString())
    return cursor!!.getString(cursor!!.getColumnIndex("Keyword"))
}

fun DatabaseHandler.SaveNewKeyword(keyword: String, section: String, category: String, period: String) {
    var sql = """ INSERT INTO tbkeyword (Section, Keyword, GradingPeriod, Category, DefautKeyword)
                  values('$section', '$keyword', '$period', '$category', 'NO')
              
  """
    SQLManage(sql)
}

fun DatabaseHandler.GetKeywordList(category: String, section: String, period: String = ""): ArrayList<KeywordModel> {

    val keywordList: ArrayList<KeywordModel> = ArrayList<KeywordModel>()

    var sql: String = """ SELECT  * FROM tbkeyword
                                where Section='$section' 
                                and Category='$category' 
                                and  GradingPeriod='$period' 
                """
    var cursor = SQLSelect(sql, 211)
    var num = 1;
    Log.e("743", cursor!!.count.toString())
    if (cursor!!.moveToFirst()) {
        do {
            var keyword = cursor!!.getString(cursor!!.getColumnIndex("Keyword"))
            var def = cursor!!.getString(cursor!!.getColumnIndex("DefautKeyword"))
            val emp = KeywordModel(section, keyword, period, category, def)
            keywordList.add(emp)
        } while (cursor!!.moveToNext())
    }
    return keywordList
}


fun DatabaseHandler.UpdateDefaultKeyWord(category: String, section: String, keyword: String = "") {

    val keywordList: ArrayList<KeywordModel> = ArrayList<KeywordModel>()

    var sql: String = """ UPDATE tbkeyword
                                set  DefautKeyword='NO' 
                                where Section='$section' 
                                and Category='$category' 
                """

    SQLManage(sql)
    sql = """ UPDATE tbkeyword
                                set  DefautKeyword='YES' 
                                where Section='$section' 
                                and Category='$category' 
                                and KeyWord='$keyword' 
                                """
    SQLManage(sql)
}


fun DatabaseHandler.GetNewDefaultKeyWord(category: String, section: String): String {

    val keywordList: ArrayList<KeywordModel> = ArrayList<KeywordModel>()

    var sql: String = """ SELECT * FROM  tbkeyword
                                where Section='$section' 
                                and Category='$category' 
                                and DefautKeyword='YES' 
                                
                """
    var cursor = SQLSelect(sql, 111)
    Log.e("CCC", cursor!!.count.toString())
    if (cursor!!.count == 0) {
        return "-"
    } else {
        cursor!!.moveToFirst()
        return cursor!!.getString(cursor.getColumnIndex("Keyword"))
    }
}


fun DatabaseHandler.GetAllKeyWord(): ArrayList<String> {

    var sql: String = """ SELECT DISTINCT(Keyword) FROM tbkeyword order by Keyword
                """
    var cursor = SQLSelect(sql, 200)
    var num = 1;
    val keywordList: ArrayList<String> = ArrayList<String>()
    keywordList.add("Select")
    if (cursor!!.moveToFirst()) {
        do {
            var keyword = cursor!!.getString(cursor!!.getColumnIndex("Keyword"))
            keywordList.add(keyword)
        } while (cursor!!.moveToNext())
    }
    return keywordList
}