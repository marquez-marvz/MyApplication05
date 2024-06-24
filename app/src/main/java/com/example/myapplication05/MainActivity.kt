package com.example.myapplication05

//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.Student.StudentMain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.section_main.*
import kotlinx.android.synthetic.main.util_confirm.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

//import com.google.android.gms.drive.Drive
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential


class MainActivity : AppCompatActivity() {
    val db: DatabaseHandler = DatabaseHandler(this)
    var sectionList = arrayListOf<SectionModel>()
    var sectionAdapter: MainSectionAdapter? = null;

    @RequiresApi(Build.VERSION_CODES.KITKAT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db: DatabaseHandler = DatabaseHandler(this)
        db.GeturrentSchoolYearSemester()
        val root = Environment.getExternalStorageDirectory()
        SetDefaultSchoolYea(Util.CURRENT_SCHOOLYEAR)
        btnSemester.text = Util.CURRENT_SEMESTER
        Log.e("xxx20", root.toString()) //        if(!Environment.isExternalStorageManager().toString()) {
        //            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        //                  startActivity(intent)
        //        }
        db.ShowAll("tbinfo") //        Util.ShowTableField(this, "tbmisc_query")
        Log.e("xxx", "Hello200") //
        var t1: TextToSpeech? = null
        Log.e("9965", android.os.Build.VERSION.SDK_INT.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         BackUpDateComputation()
        }





        FillSectionList()
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listSectionControl.layoutManager = layoutmanager
        sectionAdapter = MainSectionAdapter(this, sectionList)
        listSectionControl.adapter = sectionAdapter


        btnSemester.setOnClickListener {
            if (btnSemester.text.toString() == "FIRST") {
                btnSemester.text = "SECOND"

            } else if (btnSemester.text.toString() == "SECOND") {
                btnSemester.text = "SUMMER"
            } else if (btnSemester.text.toString() == "SUMMER") {
                btnSemester.text = "FIRST"

            }
            db.SetSemester(btnSemester.text.toString())
            FillSectionList()
            sectionAdapter!!.notifyDataSetChanged()
        }


        cboSchoolYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val schoolyear = cboSchoolYear.getSelectedItem().toString();
                db.SetSchoolYear(schoolyear)
                FillSectionList()
                sectionAdapter!!.notifyDataSetChanged()

            }
        }


        btnSection.setOnClickListener {
            val intent = Intent(this, SectionMain::class.java)
            startActivity(intent)
        }

        btnSummary2.setOnClickListener { //            val PICKFILE_RESULT_CODE = 1
            //            val intent = Intent(Intent.ACTION_GET_CONTENT)
            //
            //
            //            intent.setType("file/*")
            //            startActivityForResult(intent, PICKFILE_RESULT_CODE)

            //            val intent = Intent(Intent.ACTION_GET_CONTENT)
            //            val uri =
            //                Uri.parse(Environment.getExternalStorageDirectory().path + File.separator + "Download" + File.separator)
            //            intent.setDataAndType(uri, "text/csv")
            //            startActivity(Intent.createChooser(intent, "Open folder"))
            //            val location = "/storage/emulated/0/Download/";
            //            val intent = Intent(Intent.ACTION_VIEW)
            //            val myDir: Uri = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", File(location))
            //            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            //            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            //                intent.setDataAndType(myDir,  DocumentsContract.Document.MIME_TYPE_DIR)
            //            else  intent.setDataAndType(myDir,  "*/*")
            //
            //            if (intent.resolveActivityInfo(this.packageManager, 0) != null)
            //            {
            //                this.startActivity(intent)
            //            }
            //            else
            //            {
            ////                // if you reach this place, it means there is no any file
            ////                // explorer app installed on your device
            ////                CustomToast.toastIt(context,context.getString(R.string.there_is_no_file_explorer_app_present_text))
            //            }

            val folderPath = Environment.getExternalStorageDirectory().toString() + "/11-PROG-2/"

            //            val intent = Intent()
            //            intent.setAction(Intent.ACTION_GET_CONTENT)
            //            val myUri = Uri.parse(folderPath)
            //            intent.setDataAndType(myUri, "file/*")
            //            startActivity(intent)

            val dir = File(Environment.getExternalStorageDirectory().toString() + "/11-PROG-2/")
            if (dir.exists() && dir.isDirectory) {
                Log.e("3456", "The file '$folderPath' exists.200")
            } else {
                Log.e("5678", "The file '$folderPath'not  exists.")

            }


//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            val uri = Uri.parse(folderPath) // a directory
//            intent.setDataAndType(uri, "*/*")
//            startActivity(Intent.createChooser(intent, "Open folder"))

////
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
//            startActivityForResult(Intent.createChooser(intent, "Select a file"),

            val selectedUri =
                Uri.parse(Environment.getExternalStorageDirectory().toString() + "/11-PROG-2/")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(selectedUri, "resource/folder")
var p = intent.resolveActivityInfo(packageManager, 0)
            if (Build.VERSION.SDK_INT < 30) {
                if (intent.resolveActivityInfo(packageManager, 0) != null) {
                    Log.e("1181", "NULL")

                    startActivity(intent)
                } else { // if you reach this place, it means there is no any file
                    // explorer app installed on your device

                    Log.e("1181", "NULL")
                }
            }

        }




        btnSubject.setOnClickListener {
            val intent = Intent(this, SubjectMain::class.java)
            startActivity(intent)
        }


        btnStudent.setOnClickListener {
            val intent = Intent(this, com.example.myapplication05.Student.StudentMain::class.java)
            startActivity(intent)
        }


        btnScore.setOnClickListener {
            val intent = Intent(this, com.example.myapplication05.Student.StudentMain::class.java)
            startActivity(intent)
        }

        btnEnrolled.setOnClickListener {
            val intent = Intent(this, EnrolleMain::class.java)
            startActivity(intent)
        }


        btnSchedule.setOnClickListener {
            val intent = Intent(this, SchedMain::class.java)
            startActivity(intent)
        }


        btnAttendance.setOnClickListener {
            val intent = Intent(this, AttendanceMain::class.java)
            startActivity(intent)
        }


        btnSummary.setOnClickListener {
            val intent = Intent(this, ScoreSummary::class.java)
            startActivity(intent)
        }

        btnActivity.setOnClickListener {
            val intent = Intent(this, ActivityMain::class.java)
            startActivity(intent)
        }

        btnRecitation.setOnClickListener {
            val intent = Intent(this, RecitationMain::class.java)
            startActivity(intent)
        }

        btnGrouping.setOnClickListener {
            val intent = Intent(this, Grouping::class.java)
            startActivity(intent)
        }

        btnNotes.setOnClickListener { //            val intent = Intent(this,  Notes::class.java)
            ////            startActivity(intent)
            //            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //                .requestScopes(Drive.SCOPE_FILE)
            //                .build()
            //
            //            val googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
            //            val signInIntent = googleSignInClient.signInIntent
            //            startActivityForResult(signInIntent, 1)
            //        viewModelScope.launch(Dispatchers.IO) {
            //                // Define a Folder
            //                val gFolder = com.google.api.services.drive.model.File()
            //                // Set file name and MIME
            //                gFolder.name = "My Cool Folder Name"
            //                gFolder.mimeType = "application/vnd.google-apps.folder"
            //
            //                // You can also specify where to create the new Google folder
            //                // passing a parent Folder Id
            //                val parents: MutableList<String> = ArrayList(1)
            //                parents.add("your_parent_folder_id_here")
            //                gFolder.parents = parents
            //                drive.Files().create(gFolder).setFields("id").execute()
            //            }


        }


        btnGrade.setOnClickListener {
            val intent = Intent(this, GradeMain::class.java)
            Util.GRADE_SECTION = db.GetCurrentSection();
            startActivity(intent)
        }

        btnMissing.setOnClickListener {
            val intent = Intent(this, Chart::class.java)
            startActivity(intent)
        }


        btnMisc.setOnClickListener {
            val intent = Intent(this, Gdrive::class.java)
            startActivity(intent)
        }

        btnIndividual.setOnClickListener {
            Util.GRADE_INDIVIDUAL = "INDIVIDUAL"
            val intent = Intent(this, IndividualStudenht::class.java)
            startActivity(intent)
        }

        btnCheckQuiz.setOnClickListener {
            val intent = Intent(this, MainCheckQuiz::class.java)
            startActivity(intent)
        }

        //        btnImportAnswer.setOnClickListener {
        //            val intent = Intent(this, Answer_Main::class.java)
        //            startActivity(intent)
        //        }


        btnBackUp.setOnClickListener {
            exportDB()
            Util.Msgbox(this, "Database was succdessfilly backup")

        }

        //btnCheckTestPaper
        btnCheckTestPaper.setOnClickListener {
            val intent = Intent(this, CheckMain::class.java)
            startActivity(intent)
        }

        btnGeneral.setOnClickListener {
            val intent = Intent(this, GeneralPic::class.java)
            startActivity(intent)
        }
        btnOpenPDF.setOnClickListener {
            val intent = Intent(this, AttendanceTab::class.java)
            startActivity(intent)

            //            val intent = Intent(this,  CheckMain::class.java)
            //            startActivity(intent)
            //            val intent = Intent(this,  OpenNewPDF::class.java)
            //            startActivity(intent)
            //            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            //
            //            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE)
        }

        //
        //            val i = Intent(Intent.ACTION_SEND)
        //            i.type = "message/rfc822"
        //            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("marquez.marvz@gmail.com"))
        //            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email")
        //            i.putExtra(Intent.EXTRA_TEXT, "body of email")
        //            try {
        //                startActivity(Intent.createChooser(i, "Send mail..."))
        //            } catch (ex: ActivityNotFoundException) {
        //                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT)
        //                    .show()
        //            }//            val intent = Intent(this,  OpenNewPDF::class.java)
        //           // exportDB()
        //            try {
        //
        //                val stringSenderEmail = "marquez.marvz@gmail.com";
        //                val stringReceiverEmail = "marvin.marquez2018@deped.gov.ph";
        //                val stringPasswordSenderEmail = "SirMarvz2023@@";
        //
        //                val  stringHost = "smtp.gmail.com";
        //
        //                 val properties:Properties = System.getProperties();
        //
        //                properties.put("mail.smtp.host", stringHost);
        //                properties.put("mail.smtp.port", "465");
        //                properties.put("mail.smtp.ssl.enable", "true");
        //                properties.put("mail.smtp.auth", "true");
        //
        ////                protected PasswordAuthentication getPasswordAuthentication() {
        ////                    return new PasswordAuthentication("username", "password")
        ////                }
        //
        //                val session: javax.mail.Session =
        //                    Session.getInstance(properties, object : Authenticator() {
        //
        //                            override fun getPasswordAuthentication() : PasswordAuthentication {
        //                                return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
        //                            }
        //                        //}
        //                    })
        //
        //
        //                val mimeMessage:MimeMessage =  MimeMessage(session);
        //                mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(stringReceiverEmail));
        //
        //                mimeMessage.setSubject("Subject: Android App email");
        //                mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. \n\n Cheers!\nProgrammer World");
        //
        //                Log.e("xxx", "Hrllo")
        //                val  thread:Thread = Thread(Runnable() {
        //                  //  @Override
        //                     fun  run() {
        //                        try {
        //                            Transport.send(mimeMessage);
        //                            Log.e("uuuu", "Hrllo")
        //
        //                        } catch ( e:MessagingException) {
        //                            Log.e("zzzz", "Hrllo")
        //                            e.printStackTrace();
        //                        }
        //                    }
        //                });
        //                thread.start();
        //                Log.e("yyy", "Hrllo")
        //
        //
        //            } catch ( e:AddressException) {
        //                Log.e("ADD", "Hrllo")
        //                e.printStackTrace();
        //            } catch ( e:MessagingException) {
        //                Log.e("MSG", "Hrllo")
        //                e.printStackTrace();
        //            }
        //  }
    } //oncreate

    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //        super.onActivityResult(requestCode, resultCode, data)
    //        if (requestCode == 1) {
    //            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
    //            try {
    //                val account = task.getResult(ApiException::class.java)
    //                val accessToken = account?.serverAuthCode ?: ""
    //                initializeDriveService(accessToken)
    //            } catch (e: ApiException) {
    //                // Handle sign-in error
    //            }
    //        }
    //    }

    //    private fun initializeDriveService(accessToken: String) {
    //        val httpTransport = AndroidHttp.newCompatibleTransport()
    //        val jsonFactory = GsonFactory.getDefaultInstance()
    //
    //        val credentials = GoogleAccountCredential.usingOAuth2(this, setOf(DriveScopes.DRIVE_FILE))
    //        credentials.selectedAccountName = GoogleSignIn.getLastSignedInAccount(this)?.account?.name
    //        credentials.tokenServerEncodedUrl = "https://accounts.google.com/o/oauth2/token"
    //        credentials.refreshToken = accessToken
    //
    //        val drive = Drive.Builder(httpTransport, jsonFactory, credentials)
    //            .setApplicationName("Your App Name").build()
    //    }
    //        // Call the method to create and share a folder
    //    private fun createAndShareFolder(drive: Drive) {
    //        val folderMetadata = File()
    //        folderMetadata.name = "Shared Folder Name"
    //        folderMetadata.mimeType = "application/vnd.google-apps.folder"
    //
    //        val folder = drive.files().create(folderMetadata)
    //            .setFields("id")
    //            .execute()
    //
    //        val permission = Permission()
    //        permission.type = "anyone"
    //        permission.role = "writer"
    //
    //        drive.permissions().create(folder.id, permission).execute()
    //
    //        // Handle success or error
    //    }


    fun SetDefaultSchoolYea(search: String) {
        val db: DatabaseHandler = DatabaseHandler(this)
        val arrSchoolYear = arrayOf("2022-2023", "2023-2024", "2024-2025")
        val index = arrSchoolYear.indexOf(search)
        cboSchoolYear.setSelection(index)
    }


    fun FillSectionList() {
        var sectionListLocal: List<SectionModel> =
            db.GetSectionList(Util.CURRENT_SCHOOLYEAR, Util.CURRENT_SEMESTER)
        sectionList.clear()

        for (e in sectionListLocal) {
            sectionList.add(SectionModel(e.sectionCode, e.sectionName, e.school, e.status, e.Message, e.originalSection, e.subjectDescription, e.folderLink))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)

    fun exportDB() {
        Log.e("aaa", "Hi")
        val DATABASE_NAME = "dbstudentnew";


        val sd: File = Environment.getExternalStorageDirectory()
        val data: File = Environment.getDataDirectory();
        Log.e("aaa", sd.toString())
        Log.e("bbb", data.toString())
        Log.e("aaa", sd.canWrite().toString())
        Log.e("aaa", sd.canWrite().toString())
        requestPermission()

        val now = Date()
        val sdf = SimpleDateFormat("MMM_d-k_mm")
        val formattedTime: String = sdf.format(now)
        Log.e("aaa10", formattedTime)

        val sdf2 = SimpleDateFormat("yyyy-MM-dd")
        var formattedTime2: String = sdf2.format(now)
        Log.e("aaa20", formattedTime2)






            val db: DatabaseHandler = DatabaseHandler(this)
            //formattedTime2 =   "2024-02-18"
            db.UpdateBackUpdate(formattedTime2) ////            val dateTime2: LocalDate =
            ////                LocalDate.parse(stringToTest2, formatter)
            //                        Log.e("T1",dateTime.toString())
            //                        Log.e("T2",dateTime2.toString())
            //                        var period = Period.between(dateTime, dateTime2)
            //                        Log.e("t3", period.getDays().toString())



        //val diff: Long = pp.toDate().getTime() - qq.getTime()

        //        var t1= LocalDate.parse(pp)
        //val diff: Long = pp.toDate().getTime() - qq.getTime()
        //  val diff: Duration = Duration.between(qq, t1)
        //  val minutes = diff.toMinutes()
        //    Log.e("mmm", minutes.toString())

        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val file1 =
            File(this.getExternalFilesDir(null), "/DriveSyncFiles/SSS") //     val  dir = "/storage/emulated/0/DriveSyncFiles/SSS" //     val dir2 = File(dir) //     dir2.mkdir()
        Log.e("", "creating folder" + extStorageDirectory) //data/data/com.example.myapplication05/databases/<your-database-name>
        if (sd.canWrite()) {
            Log.d("TAG", "DatabaseHandler: can write in sd"); //Replace with YOUR_PACKAGE_NAME and YOUR_DB_NAME
            val currentDBPath =
                "//data/data/com.example.myapplication05/databases/" + DATABASE_NAME; //Replace with YOUR_FOLDER_PATH and TARGET_DB_NAME in the SD card
            val copieDBPath = "/DriveSyncFiles/Database Android Backup/" + formattedTime
            val currentDB: File = File(data, currentDBPath);
            val copieDB = File(sd, copieDBPath);
            Log.e("aaa", currentDB.exists().toString())
            val f = File("/data/data/com.example.myapplication05/databases/dbstudentnew")
            Log.e("aaa10", f.exists().toString())
            if (f.exists()) {
                Log.e("TAG", "DatabaseHandler: DB exist");
                @SuppressWarnings("resource") val src: FileChannel =
                    FileInputStream(f).getChannel();
                @SuppressWarnings("resource") val dst: FileChannel =
                    FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            }
        }


    }


    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (Environment.isExternalStorageManager()) {
            } else {
                val intent = Intent()
                intent.action =
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION //intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun BackUpDateComputation() {

        val db: DatabaseHandler = DatabaseHandler(this)
        val backupStr = db.GetBackUpdate();
        if (backupStr != "") {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            val backupDate: LocalDate = LocalDate.parse(backupStr, formatter)

            val now = Date()
            val sdf2 = SimpleDateFormat("yyyy-MM-dd")
            val dateNowStr: String = sdf2.format(now)
            val dateNowDate: LocalDate = LocalDate.parse(dateNowStr, formatter)

            var period = Period.between(backupDate, dateNowDate)
            Log.e("PPP20", period.days.toString())
            if (period.days > 0) {
                val dlgquiz = LayoutInflater.from(this).inflate(R.layout.util_confirm, null)
                val mBuilder = AlertDialog.Builder(this).setView(dlgquiz)
                    .setTitle("Do you want to backup the database ?")
                val inputDialog = mBuilder.show()
                inputDialog.setCanceledOnTouchOutside(false); //

                dlgquiz.btnYes.setOnClickListener {
                    exportDB()
                    inputDialog.dismiss()
                }

                dlgquiz.btnNo.setOnClickListener {
                    inputDialog.dismiss()
                }

            } //                        Log.e("t3", period.getDays().toString())


        }
    }
}


//
//
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//
//fun vhin (){
//        val myPdfDocument = PdfDocument()
//        val myPaint = Paint()
//
//        val myPageInfo1 = PdfDocument.PageInfo.Builder(400, 600, 1).create()
//        val myPage1 = myPdfDocument.startPage(myPageInfo1)
//        val canvas = myPage1.canvas
//
//        canvas.drawText("Welcome to Sarthi Technology", 40F, 50F, myPaint);
//        canvas.drawText("Some Text", 10F, 25F,myPaint);
//        myPdfDocument.finishPage(myPage1);
//    //   val m =  getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//        // = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//       // val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Hello22.pdf")
//     //   val file = File(this.getExternalFilesDir("/storage/emulated/0/"), "Hello22.pdf")
//        //val file = File("/storage/emulated/0/" , "Hello.pdf")
//        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23)
//
//        val folder =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val myFile = File(folder, "Hello23.pdf")
//
//      //  try {
//            myPdfDocument.writeTo(FileOutputStream(myFile))
//      //  } catch (e: IOException) {
//     //       e.printStackTrace()
//     //   }
//
//        myPdfDocument.close()
//        Log.e("kkk", this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString())
//
//}
//


fun DatabaseHandler.UpdateBackUpdate(thedate: String) {
    var sql = """
                          update  tbinfo 
                          set  BackupDate='$thedate'
                          """
Log.e("1645", sql)
    SQLManage(sql)
}


fun DatabaseHandler.GetBackUpdate(): String {
    var sql = "select * from   tbinfo"
    var cursor = SQLSelect(sql)
    cursor!!.moveToFirst()
    return cursor!!.getString(cursor!!.getColumnIndex("BackupDate"))
}


@SuppressLint("Range")
fun DatabaseHandler.GeturrentSchoolYearSemester() {
    var sql = "select * from   tbinfo"
    var cursor = SQLSelect(sql)
    cursor!!.moveToFirst()
    Util.CURRENT_SCHOOLYEAR = cursor!!.getString(cursor!!.getColumnIndex("SchoolYear"))
    Util.CURRENT_SEMESTER = cursor!!.getString(cursor!!.getColumnIndex("Semester"))
}

@SuppressLint("Range")
fun DatabaseHandler.SetSemester(semester: String) {
    Util.CURRENT_SEMESTER = semester
    var sql = "update   tbinfo set semester = '$semester'"
    var cursor = SQLManage(sql)
}

fun DatabaseHandler.SetSchoolYear(schoolyear: String) {
    Util.CURRENT_SCHOOLYEAR = schoolyear
    var sql = "update   tbinfo set SchoolYear = '$schoolyear'"
    var cursor = SQLManage(sql)
}





