package com.example.myapplication05

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import kotlinx.android.synthetic.main.grade_main.*
import kotlinx.android.synthetic.main.score_individual.*
import kotlinx.android.synthetic.main.score_individual.btnCopyText
import kotlinx.android.synthetic.main.score_individual.view.*
import kotlinx.android.synthetic.main.score_individual.view.listIndividualScore
import kotlinx.android.synthetic.main.score_main.*

class ScoreIndividualMain : AppCompatActivity() {

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var  itemView: View

        setContentView(R.layout.score_individual)

        val db: TableActivity = TableActivity(this)
        var individualList: List<ScoreIndividualModel>
        individualList = db.GetIndividualActivity("11-PROG-1",  "1681")
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listIndividualScore.layoutManager = layoutmanager
        var adapter = ScoreIndividualAdapter(this, individualList)
        listIndividualScore.adapter = adapter




    }

    fun SetGrade(context: Context, studentGrade:Int) {
        //txtGrade.setText(studentGrade.toString())
    }

    fun SetGrades(){
        txtAdjustedGrade.setText("100")
    }
}