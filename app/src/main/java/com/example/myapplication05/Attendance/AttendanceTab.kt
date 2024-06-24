

package com.example.myapplication05

import AttendanceTabAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication05.R
import com.example.myapplication05.fragment.*
import com.example.myapplication05.testpaper.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.att_tab.*
import kotlinx.android.synthetic.main.sampletab.*
import kotlinx.android.synthetic.main.sampletab.theTab


class AttendanceTab : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.att_tab)
        var context = this
        setUpTabs()
        Util.TAB_TEST_POSITION  = 0

        theTab.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }

    private fun setUpTabs() {
        viewPagerAttendace.setOffscreenPageLimit(2)
        val adapter = AttendanceTabAdapter(supportFragmentManager)
//       adapter.addFragment(SchedFragment(), "Sched")
       // adapter.addFragment(AttendanceFragment(), "Attendance")
        viewPagerAttendace.adapter = adapter
        theTab.setupWithViewPager(viewPagerAttendace)
    }
}