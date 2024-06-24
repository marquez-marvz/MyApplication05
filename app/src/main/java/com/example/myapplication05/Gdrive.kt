package com.example.myapplication05


//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.res.ColorStateList
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
////import android.text.method.ScrollingMovementMethod
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.WindowManager
//import android.widget.*
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.ActivityCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.volley.*
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.check_main.*
import kotlinx.android.synthetic.main.gdrive.*
import kotlinx.android.synthetic.main.rubric_student.view.*


class Gdrive : AppCompatActivity() {
    var webView: WebView? = null
    var activityList = arrayListOf<ActivityModel>()
    var driveActivityCode = ""
    var scoreList = arrayListOf<ScoreModel>()
    var remarkList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gdrive)
        webView = findViewById<View>(R.id.webview) as WebView
        webView!!.webViewClient = WebViewClient()
        webView!!.loadUrl(Util.FOLDER_LINK);
        var c: Context = this
        btnEdit.isEnabled = false
        btnDriveRemark.isEnabled = false


        LoadActivity() //WebSettings webSettings = webView.getSettings();
        Log.e("AAA", Util.FOLDER_ACT_DESCRIPTION)
        LoadDefaultActivity(Util.FOLDER_SECTION, Util.FOLDER_ACT_DESCRIPTION);
        var webSettings: WebSettings = webView!!.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Log.e("PPP21", driveActivityCode)
        txtStudent.setText(Util.FOLDER_NAME.toUpperCase())
        ScoreUpdateListContent(this, "SORT", "LASTNAME")
        Log.e("PPP20", scoreList.size.toString())
        Log.e("PPP20", driveActivityCode)
        var db: DatabaseHandler = DatabaseHandler(this)
        driveActivityCode
        remarkList = db.GetActivityRemark(Util.FOLDER_SECTION, GetActivityCode())
        driveActivityCode = GetActivityCode()

        var rubricnum = 1;
        ShowCriteria(rubricnum)

        btnCriteria.setOnClickListener {
            if (rubricnum < 5) {
                rubricnum++;
                ShowCriteria(rubricnum)
            }
        }

        btnCriteriaScore.setOnClickListener {
            val db2: DatabaseHandler = DatabaseHandler(this)
            var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, rubricnum)
            var score = btnCriteriaScore.text.toString().toInt()
            if (score < point) {
                score++;
                btnCriteriaScore.setText(score.toString())
                if (rubricnum == 1) {
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria1", score)
                }
                if (rubricnum == 2) {
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria2", score)
                }
                if (rubricnum == 3) {
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria3", score)
                }
                if (rubricnum == 4) {
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria4", score)
                }
                if (rubricnum == 5) {
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria5", score)
                }

            }
        }

        btnCriteriaScore.setOnLongClickListener {
            val db2: DatabaseHandler = DatabaseHandler(this)
            btnCriteriaScore.setText("0")

            if (rubricnum == 1) {
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria1", 0)
            }
            if (rubricnum == 2) {
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria2", 0)
            }
            if (rubricnum == 3) {
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria3", 0)
            }
            if (rubricnum == 4) {
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria4", 0)
            }
            if (rubricnum == 5) {
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria5", 0)
            }

            true
        }



        btnCriteria.setOnLongClickListener {
            rubricnum = 1;
            ShowCriteria(rubricnum)
            true
        }
        btnShowRubric.setOnClickListener {
            val dlgremark = LayoutInflater.from(this).inflate(R.layout.rubric_student, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgremark).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            var actRubricCode = db.GetSectionActivityRubric(Util.FOLDER_SECTION, driveActivityCode);
            val db2: DatabaseHandler = DatabaseHandler(c)

            var stud =
                db2.GetActivityRecord(driveActivityCode, Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)

            var first = db2.GetActivityRubricCriteria(actRubricCode, driveActivityCode, 1)
            dlgremark.btnRubricfirst.setText(first)
            dlgremark.btnPointFirst.setText(stud.first.toString())
            dlgremark.txtRubricStudetName.setText(stud.lastName + "," + stud.firstName)
            dlgremark.txtRubricStudetName.setText(stud.lastName + "," + stud.firstName)
            dlgremark.btnRubricRemark.setText(stud.remark)

            var criteria = db2.GetActivityRubricCriteria(actRubricCode, driveActivityCode, 2)
            dlgremark.btnRubricSecond.setText(criteria)
            dlgremark.btnPointSecond.setText(stud.second.toString())

            criteria = db2.GetActivityRubricCriteria(actRubricCode, driveActivityCode, 3)
            dlgremark.btnRubricThird.setText(criteria)
            dlgremark.btnPointThird.setText(stud.third.toString())


            criteria = db2.GetActivityRubricCriteria(actRubricCode, driveActivityCode, 4)
            dlgremark.btnRubricFourth.setText(criteria)
            dlgremark.btnPointFourth.setText(stud.fourth.toString())


            criteria = db2.GetActivityRubricCriteria(actRubricCode, driveActivityCode, 5)
            dlgremark.btnRubricFifth.setText(criteria)
            dlgremark.btnPointFifth.setText(stud.fifth.toString())

            GetRubricTotalScore(dlgremark)


            dlgremark.btnPointFirst.setOnClickListener {
                var num = dlgremark.btnPointFirst.text.toString().toInt()
                var result = Updatecriteria(num, 1, "Criteria1", dlgremark)
                dlgremark.btnPointFirst.setText(result.toString())
                GetRubricTotalScore(dlgremark)
            }

            dlgremark.btnPointFirst.setOnLongClickListener {
                dlgremark.btnPointFirst.setText("0")
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria1", 0)
                GetRubricTotalScore(dlgremark)

                true
            }

            dlgremark.btnPointSecond.setOnClickListener {
                var num = dlgremark.btnPointSecond.text.toString().toInt()
                var result = Updatecriteria(num, 2, "Criteria2", dlgremark)
                dlgremark.btnPointSecond.setText(result.toString())
                GetRubricTotalScore(dlgremark)
            }

            dlgremark.btnPointSecond.setOnLongClickListener {
                dlgremark.btnPointSecond.setText("0")
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria2", 0)
                GetRubricTotalScore(dlgremark)

                true
            }


            dlgremark.btnPointThird.setOnClickListener {
                var num = dlgremark.btnPointThird.text.toString().toInt()
                var result = Updatecriteria(num, 3, "Criteria3", dlgremark)
                dlgremark.btnPointThird.setText(result.toString())
                GetRubricTotalScore(dlgremark)

            }

            dlgremark.btnPointThird.setOnLongClickListener {
                dlgremark.btnPointThird.setText("0")
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria3", 0)
                GetRubricTotalScore(dlgremark)

                true
            }

            dlgremark.btnPointFourth.setOnClickListener {
                var num = dlgremark.btnPointFourth.text.toString().toInt()
                var result = Updatecriteria(num, 4, "Criteria4", dlgremark)
                dlgremark.btnPointFourth.setText(result.toString())
                GetRubricTotalScore(dlgremark)
            }

            dlgremark.btnPointFourth.setOnLongClickListener {
                dlgremark.btnPointFourth.setText("0")
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria4", 0)
                GetRubricTotalScore(dlgremark)

                true
            }

            dlgremark.btnPointFifth.setOnClickListener {
                var num = dlgremark.btnPointFifth.text.toString().toInt()
                var result = Updatecriteria(num, 5, "Criteria5", dlgremark)
                dlgremark.btnPointFifth.setText(result.toString())
                GetRubricTotalScore(dlgremark)
                true
            }



            dlgremark.btnPointFifth.setOnLongClickListener {
                dlgremark.btnPointFifth.setText("0")
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria5", 0)
                GetRubricTotalScore(dlgremark)
                true

            }


            //RubricFirst
            dlgremark.btnRubricfirst.setOnLongClickListener {
                var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, 1)
                dlgremark.btnPointFirst.setText(point.toString())
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria1", point)
                GetRubricTotalScore(dlgremark)
                true
            }

            dlgremark.btnRubricfirst.setOnClickListener {
                var num = dlgremark.btnPointFirst.text.toString().toInt()
                if (num > 0) {
                    num--;
                    dlgremark.btnPointFirst.setText(num.toString())
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria1", num)
                    GetRubricTotalScore(dlgremark)
                }
            }

            //RubricSecond
            dlgremark.btnRubricSecond.setOnLongClickListener {
                var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, 2)
                dlgremark.btnPointSecond.setText(point.toString())
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria2", point)
                GetRubricTotalScore(dlgremark)
                true
            }

            dlgremark.btnRubricSecond.setOnClickListener {
                var num = dlgremark.btnPointSecond.text.toString().toInt()
                if (num > 0) {
                    num--;
                    dlgremark.btnPointSecond.setText(num.toString())
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria2", num)
                    GetRubricTotalScore(dlgremark)
                }
            }

            dlgremark.btnRubricThird.setOnLongClickListener {
                var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, 3)
                dlgremark.btnPointThird.setText(point.toString())
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria3", point)
                GetRubricTotalScore(dlgremark)
                true
            }

            dlgremark.btnRubricThird.setOnClickListener {
                var num = dlgremark.btnPointThird.text.toString().toInt()
                if (num > 0) {
                    num--;
                    dlgremark.btnPointThird.setText(num.toString())
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria3", num)
                    GetRubricTotalScore(dlgremark)
                }
            }

            dlgremark.btnRubricFourth.setOnLongClickListener {
                var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, 4)
                dlgremark.btnPointFourth.setText(point.toString())
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria4", point)
                GetRubricTotalScore(dlgremark)
                true
            }

            dlgremark.btnRubricFourth.setOnClickListener {
                var num = dlgremark.btnPointFourth.text.toString().toInt()
                if (num > 0) {
                    num--;
                    dlgremark.btnPointFourth.setText(num.toString())

                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria4", num)
                    GetRubricTotalScore(dlgremark)
                }
            }

            dlgremark.btnRubricFifth.setOnLongClickListener {
                var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, 5)
                dlgremark.btnPointFifth.setText(point.toString())
                db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria5", point)
                GetRubricTotalScore(dlgremark)
                true
            }

            dlgremark.btnRubricFifth.setOnClickListener {
                var num = dlgremark.btnPointFifth.text.toString().toInt()
                if (num > 0) {
                    num--;
                    dlgremark.btnPointFifth.setText(num.toString())
                    db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, "Criteria5", num)
                    GetRubricTotalScore(dlgremark)
                }
            }




            dlgremark.btnRubricNext.setOnClickListener {
                var x = 0;
                var ctr = scoreList.count() - 1
                while (x < ctr) {
                    if (scoreList[x].studentNo == Util.FOLDER_STUDNUM) {
                        if (x < ctr) {
                            Util.FOLDER_STUDNUM = scoreList[x + 1].studentNo
                            var stud =
                                db2.GetActivityRecord(driveActivityCode, Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)
                            dlgremark.btnPointFirst.setText(stud.first.toString())
                            dlgremark.txtRubricStudetName.setText(stud.lastName + "," + stud.firstName)
                            dlgremark.btnPointSecond.setText(stud.second.toString())
                            dlgremark.btnPointThird.setText(stud.third.toString())
                            dlgremark.btnPointFourth.setText(stud.fourth.toString())
                            dlgremark.btnPointFifth.setText(stud.fifth.toString())
                            dlgremark.btnRubricRemark.setText(stud.remark)
                            dlgremark.btnPointFirst.isEnabled = true
                            dlgremark.btnPointSecond.isEnabled = true
                            dlgremark.btnPointThird.isEnabled = true
                            dlgremark.btnPointFourth.isEnabled = true
                            dlgremark.btnPointFifth.isEnabled = true
                            GetRubricTotalScore(dlgremark)

                        }
                        break
                    }
                    x++;
                }
            }


            dlgremark.btnTotalScore.setOnClickListener {
                var score =  dlgremark.btnTotalScore.text.toString().toInt()
                val db2: DatabaseHandler = DatabaseHandler(c)
                db2.DriveUpdateStudentRecord(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, score)
                GetRubricTotalScore(dlgremark)
            }

            dlgremark.btnRubricPrev.setOnClickListener {
                var x = 0;
                var ctr = scoreList.count() - 1
                while (x < ctr) {
                    if (scoreList[x].studentNo == Util.FOLDER_STUDNUM) {
                        if (x > 0) {
                            Util.FOLDER_STUDNUM = scoreList[x - 1].studentNo
                            var stud =
                                db2.GetActivityRecord(driveActivityCode, Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)
                            dlgremark.btnPointFirst.setText(stud.first.toString())
                            dlgremark.txtRubricStudetName.setText(stud.lastName + "," + stud.firstName)
                            dlgremark.btnPointSecond.setText(stud.second.toString())
                            dlgremark.btnPointThird.setText(stud.third.toString())
                            dlgremark.btnPointFourth.setText(stud.fourth.toString())
                            dlgremark.btnPointFifth.setText(stud.fifth.toString())
                            dlgremark.btnRubricRemark.setText(stud.remark)
                            dlgremark.btnPointFirst.isEnabled = true
                            dlgremark.btnPointSecond.isEnabled = true
                            dlgremark.btnPointThird.isEnabled = true
                            dlgremark.btnPointFourth.isEnabled = true
                            dlgremark.btnPointFifth.isEnabled = true
                            GetRubricTotalScore(dlgremark)
                        }
                        break
                    }
                    x++;
                }
            }

            dlgremark.btnRubricRemark.setOnClickListener { //3456
                Log.e("Hwlo", "XXX")
                var remarkList: ArrayList<String> = ArrayList<String>()
                remarkList = db.GetRemark(Util.FOLDER_SECTION)
                var rem = dlgremark.btnRubricRemark.text.toString()
                if (rem == "-") {
                    dlgremark.btnRubricRemark.setText(remarkList[0])
                } else {
                    var x = 0
                    Log.e("Hwlo", remarkList.count().toString())
                    while (x < remarkList.size - 2) {
                        if (rem == remarkList[x]) {
                            dlgremark.btnRubricRemark.setText(remarkList[x + 1])
                            driveActivityCode = GetActivityCode()

                            db.UpdateStudentRemark(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, remarkList[x + 1])
                            break;
                        }
                        Log.e(x.toString(), remarkList[x])
                        x++
                    }
                }
                dlgremark.btnPointFirst.isEnabled = false
                dlgremark.btnPointSecond.isEnabled = false
                dlgremark.btnPointThird.isEnabled = false
                dlgremark.btnPointFourth.isEnabled = false
                dlgremark.btnPointFifth.isEnabled = false
            }

            dlgremark.btnRubricRemark.setOnLongClickListener {
                var remarkList: ArrayList<String> = ArrayList<String>()
                remarkList = db.GetRemark(Util.FOLDER_SECTION)
                dlgremark.btnRubricRemark.setText(remarkList[0])
                driveActivityCode = GetActivityCode()
                db.UpdateStudentRemark(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, remarkList[0])
                dlgremark.btnPointFirst.isEnabled = false
                dlgremark.btnPointSecond.isEnabled = false
                dlgremark.btnPointThird.isEnabled = false
                dlgremark.btnPointFourth.isEnabled = false
                dlgremark.btnPointFifth.isEnabled = false
                true
            }

        }


        btnDriveRemarkSearch.setOnLongClickListener {
            btnDriveRemarkSearch.text = "ALL"
            ScoreUpdateListContent(this, "SORT", "LASTNAME")
            DisplayRecord(scoreList[0])
            true
        }




        btnDriveRemarkSearch.setOnClickListener {
            if (btnDriveRemarkSearch.text == "ALL") {
                btnDriveRemarkSearch.text = ScoreMain.remarkList[0]
                ScoreUpdateListContent(this, "SORT", "LASTNAME")
                ScoreUpdateListContent(this, "REMARK", remarkList[0])
                DisplayRecord(scoreList[0])
            } else {
                for (i in 0..remarkList.size - 1) {
                    if (btnDriveRemarkSearch.text == remarkList[i] && i < remarkList.size - 1) {
                        btnDriveRemarkSearch.text = remarkList[i + 1]
                        ScoreUpdateListContent(this, "REMARK", remarkList[i + 1])
                        DisplayRecord(scoreList[0])
                        break
                    }
                }
            }

        }

        btnDriveNext.setOnClickListener {
            var x = 0;
            var ctr = scoreList.count() - 1
            while (x < ctr) {
                if (scoreList[x].studentNo == Util.FOLDER_STUDNUM) {
                    if (x < ctr) {
                        DisplayRecord(scoreList[x + 1])
                        rubricnum = 1;
                        ShowCriteria(rubricnum)
                    }
                    break
                }
                x++;
            }
        }

        btnDrivePrev.setOnClickListener { //            var num = btnFive.text.toString().toInt()
            var x = 0;
            var ctr = scoreList.count() - 1
            Log.e("1234", ctr.toString())
            while (x <= ctr) {
                Log.e("1234", x.toString())
                Log.e("1234", scoreList[x].studentNo)
                Log.e("1234", Util.FOLDER_STUDNUM)
                if (scoreList[x].studentNo == Util.FOLDER_STUDNUM) {
                    if (x > 0) {
                        DisplayRecord(scoreList[x - 1])
                        rubricnum = 1;
                        ShowCriteria(rubricnum)
                    }
                    break
                }
                x++
            }
        }

        btnGotoScore.setOnClickListener { //            var num = btnFive.text.toString().toInt()
            val intent = Intent(this,  ScoreMain::class.java)
            startActivity(intent)
        }

        btnDriveRubric.setOnClickListener {
            val dlgremark = LayoutInflater.from(this).inflate(R.layout.rubrics, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgremark).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //

            Rubric.alert = inputDialog
            var ppp: Rubric = Rubric()
            val section = Util.FOLDER_SECTION
            driveActivityCode = GetActivityCode()
            var actRubricCode = db.GetSectionActivityRubric(section, driveActivityCode);
            ppp.DisplayRenark(dlgremark, this, section, driveActivityCode)


        }



        btnEdit.setOnClickListener {
            btnEdit.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            txtDriveScore.isEnabled = true

            txtDriveScore.requestFocus()
            if (txtDriveScore.text.toString() == "0") txtDriveScore.setText("")
        } //init

        btnSave.setOnClickListener {
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.INVISIBLE);
            txtDriveScore.isEnabled = false
            val db2: DatabaseHandler = DatabaseHandler(c)
            val score = txtDriveScore.text.toString().toInt()
            Log.e("SSS121", driveActivityCode)
            db2.DriveUpdateStudentRecord(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, score)
        } //init'm

        btnDriveRemark.setOnClickListener {
            val dlgremark = LayoutInflater.from(this).inflate(R.layout.remark_main, null)
            val mBuilder = AlertDialog.Builder(this).setView(dlgremark).setTitle("")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //
            RemarkMain.btnRemark = btnDriveRemark
            RemarkMain.alert = inputDialog
            RemarkMain.remark_section = Util.FOLDER_SECTION
            var ppp: RemarkMain = RemarkMain()
            ppp.SetButtonArray(dlgremark)
            val score = txtDriveScore.text.toString()
            ppp.DisplayRenark(dlgremark, this, score, driveActivityCode) // ppp.DisplayRenark(dlgremark, context, currentScore!!.studentNo, currentScore!!.activitycode)

            for (i in 0..9) {
                RemarkMain.buttons[i].setOnClickListener {
                    val rem = RemarkMain.buttons[i].text.toString()
                    btnDriveRemark.text = rem
                    inputDialog.dismiss() //UpdateStudentRemark()
                    val db: DatabaseHandler = DatabaseHandler(this)
                    db.UpdateStudentRemark(RemarkMain.remark_section, driveActivityCode, Util.FOLDER_STUDNUM, rem)

                } //init
            }
        }
        cboDriveActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val db2: DatabaseHandler = DatabaseHandler(c)
                Log.e("@@@123", "Hello")
                val activity = cboDriveActivity.getSelectedItem().toString();
                driveActivityCode = ""
                Log.e("@@@123", activity)
                for (e in activityList) {
                    Log.e("@@@123", activity + "   " + e.description)
                    if (activity == e.description) {
                        driveActivityCode = e.activityCode

                        break;
                    }
                }

                if (driveActivityCode == "") {
                    return
                }
                btnEdit.isEnabled = true
                btnDriveRemark.isEnabled = true
                var stud =
                    db2.GetActivityRecord(driveActivityCode, Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)
                txtDriveScore.setText(stud.score.toString())
                btnDriveRemark.setText(stud.remark.toString())

                webView!!.webViewClient = WebViewClient()
                webView!!.loadUrl(Util.FOLDER_LINK); //                Log.e("REM", stud.remark + "     " + stud.score)
                //                grpMemberList.add(GrpMemberModel(e.firstname, e.lastnanme, e.studentNo, e.grp, stud.score, stud.remark, stud.adjusted))
            }
        }
    }

    fun GetTotalScore(dlgRemark: View): Int {
        var n1 = dlgRemark.btnPointFirst.text.toString().toInt();
        var n2 = dlgRemark.btnPointSecond.text.toString().toInt();
        var n3 = dlgRemark.btnPointThird.text.toString().toInt();
        var n4 = dlgRemark.btnPointFourth.text.toString().toInt();
        var n5 = dlgRemark.btnPointFifth.text.toString().toInt();
        var total = n1 + n2 + n3 + n4 + n5

        return total
    }

    fun LoadActivity() {
        val section = Util.FOLDER_SECTION
        val db: TableActivity = TableActivity(this)

        activityList = db.GetActivityList(section, db.GetDefaultGradingPeriod())
        var activityArray = Array(activityList.size + 1) { "" }
        var x = 0;

        for (e in activityList) {
            activityArray[x] = e.description
            x++;
        }
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.util_spinner, activityArray)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboDriveActivity.setAdapter(sectionAdapter);

    }

    fun ShowCriteria(rubricnum: Int) {
        var db: DatabaseHandler = DatabaseHandler(this)
        driveActivityCode = GetActivityCode()
        var actRubricCode = db.GetSectionActivityRubric(Util.FOLDER_SECTION, driveActivityCode);

        var criteria = db.GetActivityRubricCriteria(actRubricCode, driveActivityCode, rubricnum)
        btnCriteria.setText(criteria)
        var stud = db.GetActivityRecord(driveActivityCode, Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)
        if (rubricnum == 1) {
            btnCriteriaScore.setText(stud.first.toString())
        } else if (rubricnum == 2) {
            btnCriteriaScore.setText(stud.second.toString())
        } else if (rubricnum == 3) {
            btnCriteriaScore.setText(stud.third.toString())
        } else if (rubricnum == 4) {
            btnCriteriaScore.setText(stud.fourth.toString())
        } else if (rubricnum == 5) {
            btnCriteriaScore.setText(stud.fifth.toString())
        }
    }

    fun GetActivityCode(): String {
        val activity = cboDriveActivity.getSelectedItem().toString();
        for (e in activityList) {
            Log.e("@@@123", activity + "   " + e.description)
            if (activity == e.description) {
                return e.activityCode

                break;
            }
        }
        return ""
    }

    fun ScoreUpdateListContent(context: Context, category: String = "", sortOrder: String = "", zeroOnly: Boolean = false) {
        val dbactivity: TableActivity = TableActivity(context)
        val activity: List<ScoreModel>
        scoreList.clear()
        var section = Util.FOLDER_SECTION

        var activityCode = GetActivityCode()
        activity = dbactivity.GetScoreList(section, activityCode, category, sortOrder)
        Log.e("SSS12", activityCode)

        for (e in activity) {
            scoreList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }
        Log.e("SSS12", scoreList.size.toString())
        Util.Msgbox(context, ScoreMain.scoreList.size.toString()) //            var completeName: String,
    }

    fun Updatecriteria(num: Int, buttonNum: Int, criteria: String, dlgRemark: View): Int {
        driveActivityCode = GetActivityCode()
        val db2: DatabaseHandler = DatabaseHandler(this)
        var point = db2.GetActivityCriteria(Util.FOLDER_SECTION, driveActivityCode, buttonNum)
        var num1 = num
        if (num1 < point) {
            num1++;
            var total = GetTotalScore(dlgRemark) + 1
            txtDriveScore.setText(total.toString())
            db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, criteria, num1)
        }

        return num1;


    }

    fun ResetCriteria(criteria: String, dlgRemark: View) {
        var total = GetTotalScore(dlgRemark)
        txtDriveScore.setText(total.toString())
        val db2: DatabaseHandler = DatabaseHandler(this)
        db2.UpdateCriteriaScore(Util.FOLDER_SECTION, driveActivityCode, Util.FOLDER_STUDNUM, criteria, 0, total)
    }

    private fun LoadDefaultActivity(section: String, actDescription: String) {
        var mycontext = this;
        val db: TableActivity = TableActivity(this)
        var activityArray = Array(activityList.size + 1) { "" }
        var x = 0;
        for (e in activityList) {
            activityArray[x] = e.description
            Log.e("AAA", activityArray[x])
            x++;
        }


        val index = activityArray.indexOf(actDescription)
        Log.e("AAA", index.toString())
        cboDriveActivity.setSelection(index)
    }


    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

    fun DisplayRecord(e: ScoreModel) {
        val db: DatabaseHandler = DatabaseHandler(this)
        Util.FOLDER_LINK = db.GetLink(e.studentNo, e.sectioncode)
        Util.FOLDER_NAME = e.lastnanme
        Util.FOLDER_STUDNUM = e.studentNo
        val db2: DatabaseHandler = DatabaseHandler(this)
        txtStudent.setText(Util.FOLDER_NAME.toUpperCase())
        webView!!.webViewClient = WebViewClient()
        webView!!.loadUrl(Util.FOLDER_LINK);
        var stud =
            db2.GetActivityRecord(GetActivityCode(), Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)
        txtDriveScore.setText(stud.score.toString())
        btnDriveRemark.setText(stud.remark.toString())

    }

    fun GetRubricTotalScore(dlgremark:View){
        var actCode = GetActivityCode()
        val db2: DatabaseHandler = DatabaseHandler(this)
        val grades: Grades = Grades(this)

        var stud = db2.GetActivityRecord(actCode, Util.FOLDER_SECTION, Util.FOLDER_STUDNUM)
        var totalScore = stud.first + stud.second + stud.third + stud.fourth + stud.fifth
        var point = GetRubricTotalPoint()
        var dec = ((totalScore.toDouble() / point.toDouble()) * 100).toDouble()
        var grade = grades.ConvertDepedGrade(dec)
        dlgremark.btnTotalScore.setText(totalScore.toString())
        dlgremark.btnEquivalentGrade.setText(grade.toString())
        var recordScore = db2.GetActivutyStudentScore(Util.FOLDER_SECTION, actCode, Util.FOLDER_STUDNUM)
        Log.e("798SCORE", recordScore.toString())
        Log.e("798SCORE", totalScore.toString())
        if (recordScore == totalScore){
            dlgremark.btnTotalScore.setBackgroundColor(Color.parseColor("#69F0AE"))
        } else {

            dlgremark.btnTotalScore.setBackgroundResource(android.R.drawable.btn_default);
        }

    }


    fun GetRubricTotalPoint():Int{
        var point = 0
        var actCode = GetActivityCode()
        val db2: DatabaseHandler = DatabaseHandler(this)

        point = point +  db2.GetActivityCriteria(Util.FOLDER_SECTION, actCode, 1)
        point = point +  db2.GetActivityCriteria(Util.FOLDER_SECTION, actCode, 2)
        point = point +  db2.GetActivityCriteria(Util.FOLDER_SECTION, actCode, 3)
        point = point +  db2.GetActivityCriteria(Util.FOLDER_SECTION, actCode, 4)
        point = point +  db2.GetActivityCriteria(Util.FOLDER_SECTION, actCode, 5)
      return point
    }



}

fun DatabaseHandler.DriveUpdateStudentRecord(sectionCode: String, activityCode: String, studentNo: String, score: Int) {
    var myremark ="-"
     if (score>0){
         myremark ="OK"
     }


    var sql = ""
    sql = """ UPDATE TBSCORE
                    SET  Score='$score'
                    ,Remark='$myremark'
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND StudentNo='$studentNo'  """
    val db = this.writableDatabase
    db.execSQL(sql)
}


fun DatabaseHandler.UpdateCriteriaScore(sectionCode: String, activityCode: String, studentNo: String, criteria: String, criteriaScore: Int, totalScore: Int) {
    var sql = ""
    sql = """ UPDATE TBSCORE
                    SET  Score='$totalScore'
                    , $criteria='$criteriaScore'
                    ,Remark='OK'
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND StudentNo='$studentNo'  """
    Log.e("SSS", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}


fun DatabaseHandler.UpdateCriteriaScore(sectionCode: String, activityCode: String, studentNo: String, criteria: String, criteriaScore: Int) {
    var sql = ""
    sql = """ UPDATE TBSCORE
                    SET  
                     $criteria='$criteriaScore'
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND StudentNo='$studentNo'  """
    Log.e("SSS", sql)
    val db = this.writableDatabase
    db.execSQL(sql)
}


fun DatabaseHandler.GetActivityCriteria(sectionCode: String, activityCode: String, num: Int): Int {
    var rubricCode = GetSectionActivityRubric(sectionCode, activityCode)

    val db = this.readableDatabase
    val sql = """
                    SELECT *FROM tbRubric
                    WHERE RubricCode ='$rubricCode'
                    AND RubricNUm =$num
                    """
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        return cursor.getString(cursor.getColumnIndex("Points")).toInt()
    } else {
        return 0
    }
}


fun DatabaseHandler.GetActivityRubricCriteria(rubricCode: String, activityCode: String, num: Int): String {


    val db = this.readableDatabase
    val sql = """
                    SELECT *FROM tbRubric
                    WHERE RubricCode ='$rubricCode'
                    AND RubricNUm =$num
                    """
    val cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        var point = cursor.getString(cursor.getColumnIndex("Points"))
        var desc = cursor.getString(cursor.getColumnIndex("Description"))
        return desc + "(" + point + ")"
    } else {
        return "----"
    }
}


fun DatabaseHandler.GetActivutyStudentScore(sectionCode: String, activityCode: String, studentNo: String):Int {
    var sql = ""
    sql = """ SELECT * FROM TBSCORE
                    WHERE SectionCode='$sectionCode'
                    AND ActivityCode='$activityCode' 
                    AND StudentNo='$studentNo'  """
    val cursor = SQLSelect(sql, 400)
    cursor!!.moveToFirst()
    var point = cursor!!.getString(cursor!!.getColumnIndex("Score")).toInt()
    Log.e("PPP", point.toString())
    return point;
}




