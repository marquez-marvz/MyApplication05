package com.example.myapplication05


import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication05.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.util_list.*
import kotlinx.android.synthetic.main.util_list.view.*


class ChartList : AppCompatActivity() {

    companion object {
        var scoreList = arrayListOf<ScoreModel>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.util_list)
        txtRemark.setText(Util.CHART_REMARK)
        txtTitle.setText(Util.CHART_TITLE)
        val db: DatabaseHandler = DatabaseHandler(this)
        var section =db.GetCurrentSection()

        val dbactivity: TableActivity = TableActivity(this)
        val activity: List<ScoreModel>
        var scoreAdapter: ScoreAdapter? = null;
        //        var section = ScoreMain.txtsection!!.text.toString()
        //        var activityCode = ScoreMain.txtactivitycode!!.text.toString()
        activity = dbactivity.GetScoreList(section, Util.CHART_ACTCODE, "REMARK", Util.CHART_REMARK)
     Log.e("ChartList65",activity.count().toString())
        scoreList.clear()
        for (e in activity) {
            scoreList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }

        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listScore.layoutManager = layoutmanager
        scoreAdapter = ScoreAdapter(this, scoreList)
        listScore.adapter = scoreAdapter



    }
}