package com.example.myapplication05.testpaper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.*
import kotlinx.android.synthetic.main.test_check.view.*
import kotlinx.android.synthetic.main.test_dialog_assign.*
import kotlinx.android.synthetic.main.test_dialog_assign.view.*
import kotlinx.android.synthetic.main.test_dialog_assign.view.btnSet
import kotlinx.android.synthetic.main.test_dialog_assign.view.cboQuizCode
import kotlinx.android.synthetic.main.test_score.*
import kotlinx.android.synthetic.main.test_score.view.*


class TestScore : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        viewscore = inflater.inflate(R.layout.test_score, container, false)
        containerGlobal = container
        var context = container!!.context
        val db: DatabaseHandler = DatabaseHandler(context);
        currentSectionScore = db.GetCurrentSection();
        currentSubject = db.GetSectionSubject(currentSectionScore)
        viewscore!!.txtScoreSection.setText(currentSectionScore)
        viewscore!!.btnScoreSubject.setText(currentSubject)
        LoadActivity(currentSectionScore)
        var act = db.GetDefaultActivity(currentSectionScore).toString()
        var i = GetComboSelection(viewscore!!.cboScoreActivity, act)
        viewscore!!.cboScoreActivity.setSelection(i)
        viewscore!!.imgStudentScore.setMaxZoom(10.0f)


        viewscore!!.btnListViewStatus.setOnClickListener {
            if ( viewscore!!.btnListViewStatus.text == "HIDE PICTURE") {
                viewscore!!.btnListViewStatus.setText( "SHOW PICTURE")
                viewscore!!.imgStudentScore.isVisible = false

                val params: ViewGroup.LayoutParams = viewscore!!.listQuizScore.getLayoutParams()
                params.width = 800
                viewscore!!.listQuizScore.setLayoutParams(params)
            } else {
                viewscore!!.imgStudentScore.isVisible = true
                viewscore!!.btnListViewStatus.setText( "HIDE PICTURE")

                val params: ViewGroup.LayoutParams = viewscore!!.listQuizScore.getLayoutParams()
                params.width = 180
                viewscore!!.listQuizScore.setLayoutParams(params)
            }
        }
        viewscore!!.btnSettingNew.setOnClickListener {
            /*val dlgquiz = LayoutInflater.from(context).inflate(R.layout.test_dialog_assign, null)
            val mBuilder = AlertDialog.Builder(context).setView(dlgquiz)
                .setTitle("Do you want to import answer?")
            val inputDialog = mBuilder.show()
            inputDialog.setCanceledOnTouchOutside(false); //
            val activity = viewscore!!.cboScoreActivity.getSelectedItem().toString();
            dlgquiz.txtQuizScoreSubject.setText(currentSubject)
            dlgquiz.txtScoreActivityDesc.setText(activity)


            //            val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
            //            val adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, items)
            //            dlgquiz.cboKeyWord.setAdapter(adapter)
            //            dlgquiz.cboKeyWord.setThreshold(2)

            val db2: DatabaseHandler = DatabaseHandler(context)
            val keyword: List<KeywordModel>
            var keywordArray = arrayListOf<String>()
            keyword =
                db2.GetKeywordList("IND", currentSectionScore, Util.GetCurrentGradingPeriod(context))

            for (e in keyword) {
                keywordArray.add(e.Keyword)
            }

            val adapter =
                ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, keywordArray)

            dlgquiz.cboKeyWord.setAdapter(adapter)
            dlgquiz.cboKeyWord.setThreshold(2)


            var quizList = db.GetSubjectQuizlist(currentSubject)
            val adapterQuiz =
                ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, quizList)
            dlgquiz.cboQuizCode.setAdapter(adapterQuiz)
            dlgquiz.cboQuizCode.setThreshold(2)


            dlgquiz.cboKeyWord.setOnTouchListener(OnTouchListener { v, event ->
                dlgquiz.cboKeyWord.showDropDown()
                false
            })

            dlgquiz.cboQuizCode.setOnTouchListener(OnTouchListener { v, event ->
                dlgquiz.cboQuizCode.showDropDown()
                false
            })

            dlgquiz.btnSet.setOnClickListener { //                .SetQuizKeywordQuiz(section: String, actCode:String,keyword: String, quizcode:String, subject:String ) {
                //                var sql = ""
                var quizcode = dlgquiz.cboQuizCode.text.toString()
                var keyword = dlgquiz.cboKeyWord.text.toString()
                var actCode = GetActivityCode(activity);
                db.SetQuizKeywordQuiz(currentSectionScore, actCode, keyword, quizcode, currentSubject)
            }*/
        }




        viewscore!!.cboScoreActivity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    activityDesc = viewscore!!.cboScoreActivity.getSelectedItem().toString();
                    GetScoreRecord()
                    SetUpActivityAdapter()
                    var actCode = GetActivityCode(activityDesc)
                    Log.e("HE114", actCode)
                    currentActivityCode = actCode
                    TestCheck.viewcheck!!.txtCheckSubject.setText(currentSubject)
                    db.GetQuizKeywordQuiz(currentSectionScore, actCode)
                    if (actCode !="") {
                        db.SetDefaultActivity(currentSectionScore, actCode)
                    }
                }
            }

        return viewscore
    }

    fun GetScoreRecord() {
        val context = containerGlobal!!.context
        val dbactivity: TableActivity = TableActivity(context)

        val db2: DatabaseHandler = DatabaseHandler(context)
        val activity: List<ScoreModel>
        val section = currentSectionScore
        var activityDesc = viewscore!!.cboScoreActivity.getSelectedItem().toString();
        val activityCode = GetActivityCode(activityDesc)

        activity = dbactivity.GetScoreList(section, activityCode, "SORT", "LASTNAME")
        scorelist.clear()

        for (e in activity) {
            scorelist.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, e.checkBoxStatus, e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }
    }

    fun SetUpActivityAdapter() {
        val context = containerGlobal!!.context
        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        viewscore!!.listQuizScore.layoutManager = layoutmanager
        adapterRecordScore = CheckRecordAdapter(context, scorelist)
        viewscore!!.listQuizScore.adapter = adapterRecordScore
    }

    fun GetComboSelection(cbo: Spinner, value: String): Int {
        var adapter = cbo.adapter
        val n = adapter.count
        var i = 0
        while (i < n) {
            Log.e("SSS", adapter.getItem(i).toString() + "   " + value)
            if (adapter.getItem(i).toString() == value) {
                Log.e("SSS100", adapter.getItem(i).toString() + "   " + value)
                return i
                break
            }
            i++;

        }
        return 0;
    }


    fun LoadActivity(section: String) {
        val context = containerGlobal!!.context
        val db: TableActivity = TableActivity(context)
        activityList = db.GetActivityList(section, db.GetDefaultGradingPeriod())
        var arrayActivityList = arrayListOf<String>()

        arrayActivityList.add("Select")
        for (e in activityList) {

            arrayActivityList.add(e.description)

        }
        var sectionAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context, R.layout.util_spinner, arrayActivityList)
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewscore!!.cboScoreActivity.setAdapter(sectionAdapter);

    }

    companion object {
        var viewscore: View? = null
        var containerGlobal: ViewGroup? = null
        var currentSectionScore = ""
        var currentKeywordScore = ""
        var currentActivityCode = ""
        var currentSubject = ""
        var activityList = arrayListOf<ActivityModel>()
        var activityDesc = ""
        var scorelist = arrayListOf<ScoreModel>()
        var adapterRecordScore: CheckRecordAdapter? = null;


        fun GetActivityCode(activityDesc: String): String {

            var activityCode = ""
            for (e in activityList) {
                Log.e("@@", e.description + "  " + activityDesc)
                if (activityDesc == e.description) {
                    activityCode = e.activityCode
                    break;
                }
            }
            return activityCode
        }

    }
}


fun DatabaseHandler.GetSectionSubject(section: String): String {
    var sql = ""
    sql = """ SELECT * from  tbsection_query
                where SectionName='$section'  
              """
    var cursor = SQLSelect(sql, 5)

    if (cursor!!.moveToFirst()) {
        return cursor!!.getString(cursor.getColumnIndex("QuizSubject"))
    } else {
        return "-"
    }
}


fun DatabaseHandler.SetQuizKeywordQuiz(section: String, actCode: String, keyword: String, quizcode: String, subject: String) {
    var sql = ""
    sql = """ SELECT * from  tbquiz
                where Subject='$subject'  
                and  QuizCode='$quizcode'  
              """
    var cursor = SQLSelect(sql, 5) //QuizID	Subject	QuizCode	Path	DefaultQuiz	Keyword
    var quizID = ""
    if (cursor!!.moveToFirst()) {
        quizID = cursor!!.getString(cursor.getColumnIndex("QuizID"))
    }
    Log.e("@@@", quizID) //ActivityCode	SectionCode	Description	Item	Status	Category	GradingPeriod	QuizID	RubricCode	KeyWord

    sql = """ UPDATE tbactivity
                set QuizID = '$quizID'
                ,  KeyWord = '$keyword'
                where SectionCode='$section'  
                and  ActivityCode='$actCode'  
              """
    SQLManage(sql)
    Log.e("@@@", sql)
}


fun DatabaseHandler.GetQuizKeywordQuiz(section: String, actCode: String) {

    var sql = """ select * from  tbactivity
                where SectionCode='$section'  
                and  ActivityCode='$actCode'  
              """
    SQLSelect(sql)

    var cursor = SQLSelect(sql, 5) //QuizID	Subject	QuizCode	Path	DefaultQuiz	Keyword
    var quizID = ""
    if (cursor!!.moveToFirst()) {
        val keyword = cursor!!.getString(cursor!!.getColumnIndex("KeyWord"))
        quizID = cursor!!.getString(cursor!!.getColumnIndex("QuizID"))
        TestScore.viewscore!!.btnKeyWordScore.setText(keyword)
        TestCheck.viewcheck!!.btnKeyWord.setText(keyword)
        Log.e("PPP", keyword + "   " + quizID)
        sql = """ SELECT * from  tbquiz
                where QuizID='$quizID'  
              """
        var cursor1 = SQLSelect(sql, 5)
        Log.e("PPP", cursor1!!.count.toString())
        if (cursor1!!.moveToFirst()) {
            val quizcode = cursor1!!.getString(cursor1!!.getColumnIndex("QuizCode"))
            TestScore.viewscore!!.btnScoreQuizCode.setText(quizcode)
            TestCheck.viewcheck!!.txtCheQuizCode.setText(quizcode)
            TestCheck.ShowListView()
            TestCheck.SetupQuizCodeAdapter()
        }

    }
    Log.e("@@@", quizID)
}


fun DatabaseHandler.SetDefaultActivity(section: String, actCode: String) {


    var sql = """ update  tbactivity
                set  DefaultActivity='NO'  
                where SectionCode='$section'  
              """
    SQLManage(sql)

    sql = """ update  tbactivity
                set  DefaultActivity='YES'  
                where SectionCode='$section'  
                and  ActivityCode='$actCode'  
              """
    SQLManage(sql)
}


fun DatabaseHandler.GetDefaultActivity(section: String):String {

    var desc = ""

    var sql = """ select * from  tbactivity
                where  DefaultActivity='YES'  
                and SectionCode='$section'  
              """
    var cursor=  SQLSelect(sql, 200)
    if (cursor!!.moveToFirst()) {
        desc =  cursor!!.getString(cursor!!.getColumnIndex("Description"))
    }
    return desc
}