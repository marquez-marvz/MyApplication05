//package com.example.myapplication05
//
//class  {}

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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.chart.*
import kotlinx.android.synthetic.main.task_main.*
import kotlinx.android.synthetic.main.util_list.view.*


class Chart : AppCompatActivity() {
    companion object {
        var gradeAdapter: GradeAdapter? = null;
        var gradeList = arrayListOf<GradeModel>()
        var scoreList = arrayListOf<ScoreModel>()
        var scoreAdapter: ScoreAdapter? = null;


        fun SetUpPie(pie: PieChart?, array: ArrayList<PieEntry>, title: String) {
            var pieDataSet: PieDataSet = PieDataSet(array, "Visita")
            pieDataSet.setColors(Color.parseColor("#64e986"), Color.parseColor("#89cff0"), Color.parseColor("#FFCCCB"), Color.parseColor("#ffaf7a"), Color.MAGENTA, Color.parseColor("#d3d3d3"))

            pieDataSet.setValueTextSize(15f);
            pieDataSet.valueTextColor = Color.BLACK

            pie!!.setMinAngleForSlices(40F);
            var pieData: PieData = PieData(pieDataSet);
            pie!!.setData(pieData);
            pie!!.getDescription().setEnabled(false);
            pie!!.setCenterText(title); //


            val legend: Legend = pie.getLegend()
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)

            pie!!.getLegend().setEnabled(false);
        }


        fun SetUpPieAttendance(pie: PieChart?, array: ArrayList<PieEntry>, title: String) {
            var pieDataSet: PieDataSet = PieDataSet(array, "Visita")

            val list = arrayListOf<Int>()


            for (e in array) {
                if (e.label == "NONE") {
                    list.add(Color.parseColor("#d3d3d3"))
                }

                if (e.label == "PRESENT") {
                    list.add(Color.parseColor("#64B5F6"))
                }

                if (e.label == "LATE") {
                    list.add(Color.parseColor("#69F0AE"))
                }

                if (e.label == "ABSENT") {
                    list.add(Color.parseColor("#FFB74D"))
                }

                if (e.label == "EXCUSE") {
                    list.add(Color.parseColor("#D2B55B"))
                }

            }




            pieDataSet.setColors(list)

            pieDataSet.setValueTextSize(15f);
            pieDataSet.valueTextColor = Color.BLACK

            pie!!.setMinAngleForSlices(40F);
            var pieData: PieData = PieData(pieDataSet);
            pie!!.setData(pieData);
            pie!!.getDescription().setEnabled(false);
            pie!!.setCenterText(title); //


            val legend: Legend = pie.getLegend()
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)

            val colorBlack = Color.parseColor("#000000")
            pie.setEntryLabelColor(colorBlack)

            pie!!.getLegend().setEnabled(false);
            pie.notifyDataSetChanged();
            pie.invalidate();
        }

        fun SetUpPieRecitation(pie: PieChart?, array: ArrayList<PieEntry>, title: String) {
            var pieDataSet: PieDataSet = PieDataSet(array, "Visita")

            val list = arrayListOf<Int>()

            // no ans = green
            // vorrect  = blue
            // ABSENT = green
            // no ans = green

            for (e in array) {
                if (e.label == "NONE") {
                    list.add(Color.parseColor("#d3d3d3"))
                }

                if (e.label == "CORRECT") {
                    list.add(Color.parseColor("#64B5F6"))
                }

                if (e.label == "EFFORT") {
                    list.add(Color.parseColor("#FFB74D"))
                }


                if (e.label == "NO ANS") {
                    list.add(Color.parseColor("#69F0AE"))
                }

                if (e.label == "ABSENT") {
                    list.add(Color.parseColor("#FCF55F"))
                }

            }




            pieDataSet.setColors(list)

            pieDataSet.setValueTextSize(15f);
            pieDataSet.valueTextColor = Color.BLACK

            pie!!.setMinAngleForSlices(40F);
            var pieData: PieData = PieData(pieDataSet);
            pie!!.setData(pieData);
            pie!!.getDescription().setEnabled(false);
            pie!!.setCenterText(title); //


            val legend: Legend = pie.getLegend()
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.setDrawInside(false)

            val colorBlack = Color.parseColor("#000000")
            pie.setEntryLabelColor(colorBlack)

            pie!!.getLegend().setEnabled(false);
            pie.notifyDataSetChanged();
            pie.invalidate();
        }



        fun SetUpGradePie(pie: PieChart?, array: ArrayList<PieEntry>, title: String) {
            var pieDataSet: PieDataSet = PieDataSet(array, "Visita")
            pieDataSet.setColors(Color.parseColor("#64e986"), Color.parseColor("#89cff0"), Color.parseColor("#FFCCCB"), Color.parseColor("#ffaf7a"), Color.MAGENTA, Color.parseColor("#d3d3d3"))

            pieDataSet.setValueTextSize(15f);
            pieDataSet.valueTextColor = Color.BLACK

            pie!!.setMinAngleForSlices(40F);
            var pieData: PieData = PieData(pieDataSet);
            pie!!.setData(pieData);
            pie!!.getDescription().setEnabled(false);
            pie!!.setCenterText(title); //


            pie!!.getLegend().setEnabled(false); //          pie.setValueTextColor(Color.BLACK)
        }


        fun SetUpAttendancePie(pie: PieChart?, array: ArrayList<PieEntry>, title: String) {
            var x = 0
            val colorko =
                arrayListOf(Color.parseColor("#64e986"), Color.parseColor("#89cff0"), Color.parseColor("#FFCCCB"), Color.MAGENTA)

            ////        for (pp in array)
            //            if (pp)
            //            var pieDataSet: PieDataSet = PieDataSet(array, "Visita")
            //            pieDataSet.setColors(colorko)
            //                    pieDataSet.setValueTextSize(15f);
            //            pieDataSet.valueTextColor = Color.BLACK
            //
            //            pie!!.setMinAngleForSlices(40F);
            //            var pieData: PieData = PieData(pieDataSet);
            //            pie!!.setData(pieData);
            //            pie!!.getDescription().setEnabled(false);
            //            pie!!.setCenterText(title); //
            //
            //
            //            val legend: Legend = pie.getLegend()
            //            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            //            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            //            legend.orientation = Legend.LegendOrientation.VERTICAL
            //            legend.setDrawInside(false)
            //            legend.setTextColor(Color.RED);
        }

    }


    var pieChart1: PieChart? = null
    var pieChart2: PieChart? = null
    var pieChart3: PieChart? = null
    var pieChart4: PieChart? = null
    var pieChart5: PieChart? = null
    var pieChart6: PieChart? = null
    var pieChart7: PieChart? = null
    var pieChart8: PieChart? = null
    var pieChart9: PieChart? = null
    var pieChart10: PieChart? = null
    var mode = ""
    var school = ""

    var ct = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart)

        pieChart1 = findViewById(R.id.piechart) as PieChart
        pieChart2 = findViewById(R.id.piechart2) as PieChart
        pieChart3 = findViewById(R.id.piechart3) as PieChart
        pieChart4 = findViewById(R.id.piechart4) as PieChart
        pieChart5 = findViewById(R.id.piechart5) as PieChart
        pieChart6 = findViewById(R.id.piechart6) as PieChart
        pieChart7 = findViewById(R.id.piechart7) as PieChart
        pieChart8 = findViewById(R.id.piechart8) as PieChart
        pieChart9 = findViewById(R.id.piechart9) as PieChart
        pieChart10 = findViewById(R.id.piechart10) as PieChart
        var currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
        btnChartTerm.setText(currentGradingPeriod)
        HideAllChart()
        SetUpGradeAdapter()
        SetUpScoreAdapter()


        btnChartActivity.setOnClickListener {
            btnChartActivity()
        }

        btnCSPCGrade.setOnClickListener {
            school = "CSPC"
            mode = "GRADE"
            ShowGradesInChart(school)
        }

        btnDepedGrade.setOnClickListener {
            school = "DEPED"
            mode = "GRADE"
            ShowGradesInChart(school)

        }

        btnBiscastGrade.setOnClickListener {
            Log.e("Hello", "@@@@123")
            school = "BISCAST"
            mode = "GRADE"
            ShowGradesInChart(school)
        }

        btnChartTerm.setOnClickListener() {
            var term = btnChartTerm.text.toString()
            if (term == "FIRST") {
                btnChartTerm.setText("SECOND")
                val db: TableActivity = TableActivity(this)
                db.SetDefaultGradingPeriod("SECOND")
                db.SetAverageStatus("FALSE")
            } else if (term == "SECOND") {
                btnChartTerm.setText("AVG")
                val db: TableActivity = TableActivity(this)
                db.SetAverageStatus("TRUE")
            } else if (term == "AVG") {
                btnChartTerm.setText("FIRST")
                btnChartTerm.setText("FIRST")
                val db: TableActivity = TableActivity(this)
                db.SetDefaultGradingPeriod("FIRST")
                db.SetAverageStatus("FALSE")
            }
            Log.e("XXX", mode)
            if (mode == "ACTIVITY") {
                HideAllChart()
                Activity()
            } else if (mode == "GRADE") {
                Log.e("XXX", "Hello 223")
                HideAllChart()
                ShowGradesInChart(school)
            }


        }







        pieChart1!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart1, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart2!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart2, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart3!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart3, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart4!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart4, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart5!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart5, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart6!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart6, e)

            }

            override fun onNothingSelected() {}
        })

        pieChart7!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart7, e)

            }

            override fun onNothingSelected() {}
        })

        pieChart8!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart8, e)

            }

            override fun onNothingSelected() {}
        })

        pieChart9!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart9, e)
            }

            override fun onNothingSelected() {}
        })

        pieChart10!!.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            fun onValueSelected(e: Map.Entry<*, *>?, dataSetIndex: Int, h: Highlight?) { //fire up event
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                PieClick(pieChart10, e)
            }

            override fun onNothingSelected() {}
        })

    }

    fun btnChartActivity() {
        HideAllChart()
        Activity()
        Util.CHART_CHOICE = "ACTIVITY"
        mode = "ACTIVITY"
        listGradebracket.isVisible = false
        listActivityChart.isVisible = true
        val db2: DatabaseHandler = DatabaseHandler(this)
        txtSection.text = db2.GetCurrentSection()
    }

    fun HideAllChart() {

        pieChart1!!.isVisible = false
        pieChart2!!.isVisible = false
        pieChart3!!.isVisible = false
        pieChart4!!.isVisible = false
        pieChart5!!.isVisible = false
        pieChart6!!.isVisible = false
        pieChart7!!.isVisible = false
        pieChart8!!.isVisible = false
        pieChart9!!.isVisible = false
        pieChart10!!.isVisible = false

    }

    fun PieClick(pie: PieChart?, e: Entry?) {
        Log.e("2233", mode)
        if (mode == "ACTIVITY") {
            var title = pie!!.getCenterText().toString()
            var ppp = title.split("\n")
            Util.CHART_TITLE = ppp[0]
            var actCode = ppp[1]
            var remark = (e as PieEntry).label
            txtInfo.text = Util.CHART_TITLE
            txtRemarkChart.text = remark
            ScoreUpdateListContent(this, "REMARK", remark, actCode)
            scoreAdapter!!.notifyDataSetChanged()

            //            val intent = Intent(ct, ChartList::class.java)
            //            startActivity(intent)
        } else if (mode == "GRADE") {
            var section = pie!!.getCenterText().toString()
            var gradeSearch =
                (e as PieEntry).label //            Util.GRADE_CHART = true //            val intent = Intent(ct, GradeMain::class.jNHava) //            startActivity(intent)
            txtSection.text = section
            txtInfo.text = gradeSearch
            listActivityChart.isVisible = false
            listGradebracket.isVisible = true
            var gr = gradeSearch.split("-")
            if (section == "GENERAL") {
                ShowBracketGrades(gr[0].toInt(), gr[1].toInt() + 0.99, "", "GENERAL")
            } else {
                ShowBracketGrades(gr[0].toInt(), gr[1].toInt() + 0.99, section, "SECTION")
            }


        }
    }


    fun ScoreUpdateListContent(context: Context, category: String = "", remark: String = "", actCode: String) {
        val dbactivity: TableActivity = TableActivity(context)
        val db2: DatabaseHandler = DatabaseHandler(context)
        val activity: List<ScoreModel>
        scoreList.clear()
        var section = db2.GetCurrentSection()

        activity = dbactivity.GetScoreList(section, actCode, category, remark)

        for (e in activity) {
            scoreList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }

        Log.e("4396", scoreList.size.toString())

    }

    fun SectionGradeRecord(sectionCode: String): ArrayList<PieEntry> {
        var gradeCount = arrayListOf<PieEntry>()
        val myGrades: Grades = Grades(this)
        val currentGradingPeriod = btnChartTerm.text.toString()


        var grade6069 = myGrades.GradeCount(currentGradingPeriod, 50, 69.99, sectionCode)
        if (grade6069 > 0) gradeCount.add(PieEntry(grade6069.toFloat(), "60-69"))

        var grade7074 = myGrades.GradeCount(currentGradingPeriod, 70, 74.99, sectionCode)
        if (grade7074 > 0) gradeCount.add(PieEntry(grade7074.toFloat(), "70-74"))

        var grade7579 = myGrades.GradeCount(currentGradingPeriod, 75, 79.99, sectionCode)
        if (grade7579 > 0) gradeCount.add(PieEntry(grade7579.toFloat(), "75-79"))

        var grade8089 = myGrades.GradeCount(currentGradingPeriod, 80, 89.99, sectionCode)
        if (grade8089 > 0) gradeCount.add(PieEntry(grade8089.toFloat(), "80-89"))

        var grade9094 = myGrades.GradeCount(currentGradingPeriod, 90, 94.99, sectionCode)
        if (grade9094 > 0) gradeCount.add(PieEntry(grade9094.toFloat(), "90-94"))

        var grade9699 = myGrades.GradeCount(currentGradingPeriod, 95, 99.99, sectionCode)
        if (grade9699 > 0) gradeCount.add(PieEntry(grade9699.toFloat(), "95-99"))
        return gradeCount
    }

    fun GeneralGradeRecord(school: String): ArrayList<PieEntry> {
        var gradeCount = arrayListOf<PieEntry>()
        val myGrades: Grades = Grades(this)
        val currentGradingPeriod = btnChartTerm.text.toString()


        var grade6069 = myGrades.GeneralGradeCount(currentGradingPeriod, 50, 69.99, school)
        if (grade6069 > 0) gradeCount.add(PieEntry(grade6069.toFloat(), "50-69"))

        var grade7074 = myGrades.GeneralGradeCount(currentGradingPeriod, 70, 74.99, school)
        if (grade7074 > 0) gradeCount.add(PieEntry(grade7074.toFloat(), "70-74"))

        var grade7579 = myGrades.GeneralGradeCount(currentGradingPeriod, 75, 79.99, school)
        if (grade7579 > 0) gradeCount.add(PieEntry(grade7579.toFloat(), "75-79"))

        var grade8089 = myGrades.GeneralGradeCount(currentGradingPeriod, 80, 89.99, school)
        if (grade8089 > 0) gradeCount.add(PieEntry(grade8089.toFloat(), "80-89"))

        var grade9094 = myGrades.GeneralGradeCount(currentGradingPeriod, 90, 94.99, school)
        if (grade9094 > 0) gradeCount.add(PieEntry(grade9094.toFloat(), "90-94"))

        var grade9699 = myGrades.GeneralGradeCount(currentGradingPeriod, 95, 99.99, school)
        if (grade9699 > 0) gradeCount.add(PieEntry(grade9699.toFloat(), "95-99"))
        return gradeCount
    }


    fun Activity() {
        val db3: TableActivity = TableActivity(this)
        val db: DatabaseHandler = DatabaseHandler(this)
        val activity: List<ActivityModel>
        activity = db3.GetActivityList(db.GetCurrentSection(), btnChartTerm.text.toString())

        var ctr = 1;
        for (e in activity) {
            var remarkList = arrayListOf<PieEntry>()
            remarkList = db.GetActivityRecordRemark(db.GetCurrentSection(), e.activityCode)
            if (ctr == 1) {
                pieChart1!!.isVisible = true
                SetUpPie(pieChart1, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 2) {
                pieChart2!!.isVisible = true
                SetUpPie(pieChart2, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 3) {
                pieChart3!!.isVisible = true
                SetUpPie(pieChart3, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 4) {
                pieChart4!!.isVisible = true
                SetUpPie(pieChart4, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 5) {
                pieChart5!!.isVisible = true
                SetUpPie(pieChart5, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 6) {
                pieChart6!!.isVisible = true
                SetUpPie(pieChart6, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 7) {
                pieChart7!!.isVisible = true
                SetUpPie(pieChart7, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 8) {
                pieChart8!!.isVisible = true
                SetUpPie(pieChart8, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 9) {
                pieChart9!!.isVisible = true
                SetUpPie(pieChart9, remarkList, e.description + "\n" + e.activityCode)
            }

            if (ctr == 10) {
                pieChart10!!.isVisible = true
                SetUpPie(pieChart10, remarkList, e.description + "\n" + e.activityCode)
            }
            ctr++
        }

    }

    fun SetUpGradeAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listGradebracket.layoutManager = layoutmanager
        gradeAdapter = GradeAdapter(this, gradeList)
        listGradebracket.adapter = gradeAdapter

    }


    fun SetUpScoreAdapter() {
        val layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        listActivityChart.layoutManager = layoutmanager
        scoreAdapter = ScoreAdapter(this, scoreList)
        listActivityChart.adapter = scoreAdapter
    }


    fun ShowBracketGrades(min: Int, max: Double, section: String, category: String) {
        Log.e("2233", min.toString())
        Log.e("2233", max.toString())
        Log.e("2233", section)
        Log.e("2233cc", category)
        try {
            val db: TableActivity = TableActivity(this)
            val db1: Grades = Grades(this)

            val currentGradingPeriod = Util.GetCurrentGradingPeriod(this)
            var gradingPeriodEquivalent = currentGradingPeriod + "Equivalent"

            var gradingPeriodField = currentGradingPeriod + "Grade"
            val averageStatus = db.GetAverageStatus()
            var sql = ""
            if (averageStatus == "TRUE" && category == "SECTION") {
                sql = """ select * from tbgrade_query
                    where CumulativeGrade >= $min
                    and CumulativeGrade <= $max
                    and sectioncode = '$section'
               """.trimIndent()
            } else if (averageStatus == "TRUE" && category == "GENERAL") {
                sql = """ select * from tbgrade_query
                    where CumulativeGrade >= $min
                    and CumulativeGrade <= $max
                     and sectioncode IN (select sectionName from tbsection_query where school='$school' and status ='SHOW')
                    """.trimIndent()
            } else if (category == "SECTION" && school == "DEPED") {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    and   $gradingPeriodEquivalent >= $min
                    and   $gradingPeriodEquivalent <=$max
                    order by $gradingPeriodField
                    """.trimIndent()
            } else if (category == "GENERAL" && school == "DEPED") {
                sql = """select * from tbgrade_query
                    where sectioncode IN (select sectionName from tbsection_query where school='$school' and status ='SHOW')
                    and   $gradingPeriodEquivalent >= $min
                    and   $gradingPeriodEquivalent <=$max
                    order by $gradingPeriodField
                    """.trimIndent()
            } else if (category == "SECTION" && school == "BISCAST") {
                sql = """select * from tbgrade_query
                    where sectioncode = '$section'
                    and   $gradingPeriodField >= $min
                    and   $gradingPeriodField <=$max
                    order by $gradingPeriodField
                    """.trimIndent()


            } else if (category == "GENERAL" && school == "BISCAST") {

                sql = """select * from tbgrade_query
                    where sectioncode IN (select sectionName from tbsection_query where school='$school' and status ='SHOW')
                    and   $gradingPeriodField >= $min
                    and   $gradingPeriodField <=$max
                    order by $gradingPeriodField
                    """.trimIndent()
            }

            ////            if (school == "DEPED") {
            ////
            ////            } else {
            //                sql = """select * from tbgrade_query
            //                    where sectioncode = '$section'
            //                    and   $gradingPeriodField >= $min
            //                    and   $gradingPeriodField <=$max
            //                    order by $gradingPeriodField
            //                    """.trimIndent()
            ////            }


            Log.e("123432", sql)
            UpdateListContent(sql)

        } catch (e: Exception) {
            Log.e("helloerr", e.toString())
        }

    }

    public fun UpdateListContent(sql: String) {
        try {
            val dbGrade: Grades = Grades(this)
            val theGrade: List<GradeModel>
            gradeList.clear()

            theGrade = dbGrade.GetGradeList(sql)
            for (e in theGrade) {
                gradeList.add(GradeModel(e.sectioncode, e.studentNo, e.firstname, e.lastname, e.firstGrade, e.firstEquivalent, e.firstOriginalGrade, e.secondGrade, e.secondEquivalent, e.secondOriginalGrade, e.CumulativeGrade, e.CumulativeGradeEquivalent, e.remark, e.MidtermStatus, e.FinalStatus, e.gender))
            }
            Log.e("2233", gradeList.count().toString())
            gradeAdapter!!.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("err", e.toString())
        }
    }


    fun ShowGradesInChart(school: String) {
        HideAllChart()
        val db: DatabaseHandler = DatabaseHandler(this)
        var sectionList = arrayListOf<String>()
        sectionList = db.GetSectionListPerSchool(school)

        var ctr = 2
        var gradeCount = arrayListOf<PieEntry>()
        gradeCount = GeneralGradeRecord(school)
        pieChart1!!.isVisible = true
        SetUpPie(pieChart1, gradeCount, "GENERAL")

        for (section in sectionList) {


            if (ctr == 2) {
                gradeCount = SectionGradeRecord(section)
                pieChart2!!.isVisible = true
                SetUpPie(pieChart2, gradeCount, section)
            }

            if (ctr == 3) {
                gradeCount = SectionGradeRecord(section)
                pieChart3!!.isVisible = true
                SetUpPie(pieChart3, gradeCount, section)
            }

            if (ctr == 4) {
                gradeCount = SectionGradeRecord(section)
                pieChart4!!.isVisible = true
                SetUpPie(pieChart4, gradeCount, section)
            }

            if (ctr == 5) {
                gradeCount = SectionGradeRecord(section)
                pieChart5!!.isVisible = true
                SetUpPie(pieChart5, gradeCount, section)
            }
            ctr++;
        }

    }


    fun ShowDialog(context: Context, title: String, remark: String, stud: String, section: String = "", activityCode: String = "") {
        val dlgactivity = LayoutInflater.from(context).inflate(R.layout.util_list, null)
        val mBuilder = AlertDialog.Builder(context).setView(dlgactivity).setTitle(title)

        val activityDialog =
            mBuilder.show() //        val imm = //            getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager //        imm.toggleSoftInput(android.view.inputmethod.InputMethodManager.SHOW_FORCED, android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY)

        activityDialog.setCanceledOnTouchOutside(false);
        dlgactivity.txtRemark.setText(remark)


        val dbactivity: TableActivity = TableActivity(context)
        val activity: List<ScoreModel>
        var scoreList = arrayListOf<ScoreModel>()
        var scoreAdapter: ScoreAdapter? =
            null; //        var section = ScoreMain.txtsection!!.text.toString()
        //        var activityCode = ScoreMain.txtactivitycode!!.text.toString()
        activity = dbactivity.GetScoreList(section, activityCode, "REMARK", remark)
        for (e in activity) {
            scoreList.add(ScoreModel(e.activitycode, e.sectioncode, e.firstname, e.lastnanme, e.studentNo, e.completeName, e.score, e.remark, e.status, "OFF", e.SubmissionStatus, e.AdjustedScore, e.grp, e.description))
        }

        val layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL;
        dlgactivity.listScore.layoutManager = layoutmanager
        scoreAdapter = ScoreAdapter(context, scoreList)
        dlgactivity.listScore.adapter = scoreAdapter


    } //ShowDialog
}


// fun DatabaseHandler.GetActivityRecord(sectionName: String, grpNumber: String): ArrayList<GrpMemberModel> {
fun DatabaseHandler.GetActivityRecordRemark(sectionName: String, activitycode: String): ArrayList<PieEntry> {
    var remarkList = arrayListOf<PieEntry>()


    val grpMember: ArrayList<GrpMemberModel> = ArrayList<GrpMemberModel>()
    var sql = ""
    sql = """ select distinct (remark) as remarkList  from tbscore_query	
             where sectionCode = '$sectionName' 
            and activityCode = '$activitycode' 
            """
    Log.e("AAA", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        do {
            var rem = cursor.getString(cursor.getColumnIndex("remarkList"))
            Log.e("12345", rem)
            var sql2 = """ select *   from tbscore_query	
                         where sectionCode = '$sectionName' 
                        and activityCode = '$activitycode' 
                        and remark = '$rem' 
                        """
            var cursor2: Cursor? = null
            cursor2 = db.rawQuery(sql2, null)
            Log.e("12345", sql2)
            Log.e("1122", rem + "    " + cursor2.count.toString())
            remarkList.add(PieEntry(cursor2.count.toFloat(), rem))
        } while (cursor.moveToNext())
    }
    return remarkList
}


// fun DatabaseHandler.GetActivityRecord(sectionName: String, grpNumber: String): ArrayList<GrpMemberModel> {
fun DatabaseHandler.GetRecitationCount(sectionName: String, date: String): ArrayList<PieEntry> {
    var remarkList = arrayListOf<PieEntry>()
    var sql = """ select *   from tbrecitation	
                      where sectionCode = '$sectionName' 
                      and Date = '$date' 
                      and Remark = 'NO ANS' 
                      and StudentNo <> '' 
                       """

    var cursor = SQLSelect(sql)
    if (cursor!!.count > 0) remarkList.add(PieEntry(cursor!!.count.toFloat(), "NO ANS"))


    sql = """ select *   from tbrecitation	
                      where sectionCode = '$sectionName' 
                      and Date = '$date' 
                      and Remark = 'EFFORT' 
                         and StudentNo <> '' 
                       """
    var cursor2 = SQLSelect(sql)
    if (cursor2!!.count > 0) remarkList.add(PieEntry(cursor2!!.count.toFloat(), "EFFORT"))

    sql = """ select *   from tbrecitation	
                      where sectionCode = '$sectionName' 
                      and Date = '$date' 
                      and Remark = 'CORRECT' 
                         and StudentNo <> '' 
                       """
    var cursor3 = SQLSelect(sql)
    if (cursor3!!.count > 0) remarkList.add(PieEntry(cursor3!!.count.toFloat(), "CORRECT"))


    sql = """ select *   from tbrecitation	
                      where sectionCode = '$sectionName' 
                      and Date = '$date' 
                      and Remark = 'ABSENT' 
                         and StudentNo <> '' 
                       """
    var cursor4 = SQLSelect(sql)
    if (cursor4!!.count > 0) remarkList.add(PieEntry(cursor4!!.count.toFloat(), "ABSENT"))


    sql = """ select *   from tbenroll	
                      where section = '$sectionName' 
                       """
    var cursor5 = SQLSelect(sql)
//    Log.e("1235", cursor5!!.count.toString())
    Log.e("1235", cursor!!.count.toString())
    Log.e("1235", cursor2!!.count.toString())
    Log.e("1235", cursor3!!.count.toString())
    Log.e("1235", cursor4!!.count.toString())
    var num = cursor5!!.count - (cursor!!.count + cursor2!!.count + cursor3!!.count + cursor4!!.count)
    if (num > 0) remarkList.add(PieEntry(num.toFloat(), "NONE"))

    //    val grpMember: ArrayList<GrpMemberModel> = ArrayList<GrpMemberModel>()
    //    var sql = ""
    //    sql = """ select distinct (remark) as remarkList  from tbscore_query
    //             where sectionCode = '$sectionName'
    //            and activityCode = '$activitycode'
    //            """
    //    Log.e("AAA", sql)
    //    val db = this.readableDatabase
    //    var cursor: Cursor? = null
    //    cursor = db.rawQuery(sql, null)
    //    if (cursor.moveToFirst()) {
    //        do {
    //            var rem = cursor.getString(cursor.getColumnIndex("remarkList"))
    //            Log.e("12345", rem)
    //            var sql2 = """ select *   from tbscore_query
    //                         where sectionCode = '$sectionName'
    //                        and activityCode = '$activitycode'
    //                        and remark = '$rem'
    //                        """
    //            var cursor2: Cursor? = null
    //            cursor2 = db.rawQuery(sql2, null)
    //            Log.e("12345", sql2)
    //            Log.e("1122", rem + "    " + cursor2.count.toString())
    //            remarkList.add(PieEntry(cursor2.count.toFloat(), rem))
    //        } while (cursor.moveToNext())
    //    }
    return remarkList
}

fun DatabaseHandler.GetStudentListRemark(sectionName: String, activitycode: String, status: String): String {


    var sql = ""
    sql = """ select * from  tbscore_query	
             where sectionCode = '$sectionName' 
            and activityCode = '$activitycode' 
            and remark = '$status' 
            """
    Log.e("AAA", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    var stud = ""
    if (cursor.moveToFirst()) {
        do {
            var fname = cursor.getString(cursor.getColumnIndex("FirstName"))
            var lname = cursor.getString(cursor.getColumnIndex("LastName"))
            stud = stud + lname + " " + fname + "\n"
        } while (cursor.moveToNext())
    }
    return stud

}


fun DatabaseHandler.GetActivityCode(sectionName: String, title: String): String {
    var sql = """ select  * from tbactivity	
             where sectionCode = '$sectionName' 
            and description  = '$title' 
            """
    Log.e("AAA", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        val actCode = cursor.getString(cursor.getColumnIndex("ActivityCode"))
        Log.e("Chart@673", actCode)
        return actCode
    }
    return ""
}

fun DatabaseHandler.GetSectionListPerSchool(school: String): ArrayList<String> {
    var sectionList =
        arrayListOf<String>() //SectionCode	SectionName	Status	School	Subject	Version	Message	SubjectCode	RealSectionName

    var sql = ""
    sql = """ select * from  tbsection_query	
             where School = '$school' 
            and Status = 'SHOW' 
            """
    Log.e("@@@", sql)
    val db = this.readableDatabase
    var cursor: Cursor? = null
    cursor = db.rawQuery(sql, null)
    if (cursor.moveToFirst()) {
        do {

            var rem = cursor.getString(cursor.getColumnIndex("SectionName"))
            Log.e("XXX", rem)
            sectionList.add(rem)
        } while (cursor.moveToNext())
    }
    return sectionList
}




