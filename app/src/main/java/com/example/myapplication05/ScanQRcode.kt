package com.example.myapplication05


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication05.R
import com.ortiz.touchview.BuildConfig
import kotlinx.android.synthetic.main.attendance_new_dialog.view.*
import kotlinx.android.synthetic.main.enrolle_main.*
import kotlinx.android.synthetic.main.individusl_student.*
import kotlinx.android.synthetic.main.qrcode.*
import kotlinx.android.synthetic.main.qrcode.cboStudent
import java.io.*
import java.nio.channels.FileChannel


class ScanQRcode : AppCompatActivity() {

    var studentAdapter: ArrayAdapter<String>? = null;
    var list = arrayListOf<EnrolleModel>()
    val myContext: Context = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paperpic)
        val db: DatabaseHandler = DatabaseHandler(this)
        var currentSection = db.GetCurrentSection();
        txtPaperSection.setText(currentSection)
        LoadStudents("LAST_ORDER")
        var CONTENT_REQUEST = 1337;
        var output: File? = null;
        imgStudent22.setMaxZoom(10.0f)

        btnAdd.setOnClickListener {
            val sdCard = Environment.getExternalStorageDirectory()
            val section = txtPaperSection.text.toString()
            val dir = File(sdCard.absolutePath + "/Quiz/" + section)
            dir.mkdirs()

            val studName = cboStudent.getSelectedItem().toString()
            val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + studName)
            dir2.mkdirs()
//
            var out = Environment.getExternalStorageDirectory()

//

            val file1 = File(out, "temp.jpg")
            val file2 = File(dir2, "Quiz1.jpg")

            val src = FileInputStream(file1).channel
            val dst = FileOutputStream(file2).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close() //

            //        else if (requestCode == AttendanceMain.PICK_FILE && resultcode == Activity.RESULT_OK && data != null) { //            STUD_NAME= attendanceList[INDEX].lastName + ","  + attendanceList[INDEX].firstName//ShowRemark(dlgRecite)
            //            //            SECTION_NAME = "12-PROG1-2023"
            //
            //            val uri: Uri? = data.data
            //            val myFile = File(uri.toString());
            //
            //            val selectedFile = data.data
            //            val path = selectedFile!!.path
            //            Log.e("AAA", uri.toString())
            //            Log.e("AAA", path.toString())
            //            var p = path!!.split(":")
            //            Log.e("AAA", p[1]) //                try {
            //            SECTION_NAME = dbhandler.GetRealSection(Util.ATT_CURRENT_SECTION)
            //            val folderDest = "/storage/emulated/0/PICTURE/" + SECTION_NAME
            //            val fileDest = File(folderDest)
            //            if (!fileDest.exists()) fileDest.mkdir()
            //
            //            /*   val data1 = Environment.getDataDirectory()
            //           Log.e("AAA", data1.toString())*/
            //            val file = File("/storage/emulated/0/", p[1])
            //            val file2 =
            //                File("/storage/emulated/0/Picture/" + SECTION_NAME, STUD_NAME + ".jpg") //                        val sourceImagePath = "/path/to/source/file.jpg" //                        val destinationImagePath = "/path/to/destination/file.jpg"
            //            //                        val source = File(data, sourceImagePath)
            //            //                        val destination = File(sd, destinationImagePath)
            //            /*if (source.exists()) {*/
            //            val src = FileInputStream(file).channel
            //            val dst = FileOutputStream(file2).channel
            //            dst.transferFrom(src, 0, src.size())
            //            src.close()
            //            dst.close() //                        }
            //            //                    }
            //            //                } catch (e: Exception) {
            //            //                }
            //            //               val mSelectedImagePath = getPath(uri);
            //            //                Log.e("AAA", mSelectedImagePath.toString())
            //            //                val f = File("/storage/emulated/0/Picture/Sample2.jpg")
            //            //                if (!f.exists()) {
            //            //                    try {
            //            //                        f.createNewFile()
            //            //                        copyFile(File(data.data?.let { getRealPathFromURI(it) }), f)
            //            //                    } catch (e: IOException) { // TODO Auto-generated catch block
            //            //                        e.printStackTrace()
            //            //                    }
            //            //                }
            //
            //
            /*//o get the image from the ImageView (say iv)
            //            to get the image from the ImageView (say iv)
            //            val bitmap = imgStudent.getDrawable().bitmap

            val draw = imgStudent22.drawable as BitmapDrawable
            val bitmap = draw.bitmap
            Log.e("DDD", "123")
            var outStream: FileOutputStream? = null
            val sdCard = Environment.getExternalStorageDirectory()
            val dir = File(sdCard.absolutePath + "/Hello20")
            dir.mkdirs()
            Log.e("DDD", dir.toString())
            val fileName = String.format("%d.jpg", System.currentTimeMillis())
            val outFile = File(dir, fileName)
            outStream = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
*/
        }


        btnShow.setOnClickListener {
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
            startActivityForResult(intent, 100) //'Util.Msgbox(myContext, "Hello")
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
    }


    fun LoadStudents(category: String) {
        Log.e("B201", category)


        UpdateListContent(myContext, category)

        val section = txtPaperSection.text
        var scoreListArray = Array(list.size + 1) { "" }

        var x = 1
        scoreListArray[0] = "Select"
        for (e in list) {
            scoreListArray[x] = e.lastname + "," + e.firstname
            x++;
        }

        studentAdapter = ArrayAdapter<String>(myContext, R.layout.util_spinner, scoreListArray)
        studentAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboStudent.setAdapter(studentAdapter);

    }

    fun UpdateListContent(context: Context, category: String = "ALL") {
        val db2: DatabaseHandler = DatabaseHandler(context)
        val student: List<EnrolleModel>
        var section = txtPaperSection.text.toString()

        student = db2.GetEnrolleList(category, section)
        list.clear()
        for (e in student) {
            list.add(EnrolleModel(e.ctr, e.studentno, e.firstname, e.lastname, e.Section, e.gender, e.enrollmentStatus, e.studentID, e.grpNumber, e.folderLink))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) { //        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        val path = "/storage/emulated/0"

        val f: File = File(path, "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        imgStudent22.setImageBitmap(b)

        val sdCard = Environment.getExternalStorageDirectory()
        val section = txtPaperSection.text.toString()
        val dir = File(sdCard.absolutePath + "/Quiz/" + section)
        dir.mkdirs()

        val studName = cboStudent.getSelectedItem().toString()
        val dir2 = File(sdCard.absolutePath + "/Quiz/" + section + "/" + studName)
        dir2.mkdirs()
        //
//        Log.e("Hello", )
        Log.e("Hello", sdCard.absolutePath)
        var out = Environment.getExternalStorageDirectory()

        //

        val file1 = File(out, "temp.jpg")
        val file2 = File(dir2, "Quiz1.jpg")

        val src = FileInputStream(file1).channel
        val dst = FileOutputStream(file2).channel
        dst.transferFrom(src, 0, src.size())
        src.close()
        dst.close() //


    //        if (requestCode == 100) {
        //            val bitmap: Bitmap = imageReturnedIntent!!.extras?.get("data") as Bitmap
        //            imgStudent22.setImageBitmap(bitmap)
        //        } //        else if (requestCode == AttendanceMain.PICK_FILE &&
        //    //
        //
        //        Log.e("234", "@@@@") //       // val bitmap: Bitmap = imageReturnedIntent!!.extras?.get("data") as Bitmap
        //        //        val thumbnail = imageReturnedIntent!!.extras?.get("data") as Bitmap
        //        //        val result1 = WeakReference(Bitmap.createScaledBitmap(thumbnail, thumbnail.width, thumbnail.height, false).copy(Bitmap.Config.RGB_565, true))
        //        //        val bm = result1.get()
        //        //        Log.e("234" , "@@@@")
        //        val extras: Bundle? = imageReturnedIntent!!.getExtras()
        //        val imageBitmap = extras?.get("data") as Bitmap?
        //        val bytes = ByteArrayOutputStream()
        //        imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        //        imgStudent22.setImageBitmap(imageBitmap)

        //        imgStudent22.setImageBitmap(bitmap) //
        //        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        //        WeakReference<Bitmap> result1 = new WeakReference<Bitmap>(Bitmap.createScaledBitmap(thumbnail,
        //                                                                                            thumbnail.getWidth(), thumbnail.getHeight(), false).copy(
        //                Bitmap.Config.RGB_565, true));
        //        Bitmap bm=result1.get();
        //        imageView.setImageBitmap(bm);}

        //        val selectedImage = bitmap!!.data
        //        //        val path = selectedImage!!.path
        //        //        Log.e("AAA", path.toString())
        ////        if (imageReturnedIntent == null)
        //            Log.e("BBB", requestCode.toString())
        //        Log.e("AAA", requestCode.toString())
        //        when (requestCode) {
        //            1 -> if (resultCode == RESULT_OK) {
        //                val selectedImage = imageReturnedIntent!!.data
        //                Log.e("AAA", imageReturnedIntent.toString())
        //                val path = selectedImage!!.path
        //             //   Log.e("AAA", uri.toString())
        //                Log.e("AAA", path.toString())
        ////                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        ////                val cursor: Cursor? =
        ////                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
        //                cursor!!.moveToFirst()
        //                val columnIndex: Int =
        //                    cursor.getColumnIndex(filePathColumn[0]) //file path of captured image
        //                val filePath = cursor.getString(columnIndex) //file path of captured image
        //                val f: File = File(filePath)
        //                val filename = f.name

        //   Toast.makeText(applicationContext, "Your Path:$filePath", 2000).show()
        //    Toast.makeText(applicationContext, "Your Filename:$filename", 2000).show()
        //cursor.close()

        //Convert file path into bitmap image using below line.
        // yourSelectedImage = BitmapFactory.decodeFile(filePath);
        //Toast.makeText(applicationContext, "Your image$yourSelectedImage", 2000).show()

        //put bitmapimage in your imageview
        //yourimgView.setImageBitmap(yourSelectedImage);
        //Savefile(filename, filePath)


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
}


//    private fun scanCode() {
//        val options = ScanOptions()
//        options.setPrompt("Volume up to flash on")
//        options.setBeepEnabled(true)
//        options.setOrientationLocked(true)
//        options.captureActivity = CaptureAct::class.java
//        for (i in 1..5) {
//            barLaucher.launch(options)
//        }
//    }


//    var barLaucher: ActivityResultLauncher<ScanOptions> =
//        registerForActivityResult(ScanContract()) { result ->
//            if (result.getContents() != null) {
////                val builder = AlertDialog.Builder(this)
////                builder.setTitle("Result")
////                builder.setMessage(result.getContents())
////                builder.setPositiveButton("OK") { dialogInterface, i -> dialogInterface.dismiss() }
////                    .show()
//                Util.Msgbox(this, result.getContents())
//                scanCode()
//            }
