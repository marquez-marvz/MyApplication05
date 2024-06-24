
package com.example.myapplication05

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication05.R
import com.example.myapplication05.fragment.*
import com.example.myapplication05.testpaper.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.sampletab.*


class MainCheckQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sampletab)
        var context = this
        setUpTabs()
        Util.TAB_TEST_POSITION  = 0

        theTab.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                Log.e("POSITION", position.toString())
                Util.TAB_TEST_POSITION = position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var x = ""
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                var y = ""
            }
        })

    }

    private fun setUpTabs() {
        viewPager.setOffscreenPageLimit(4)
        val adapter = TestAdapter(supportFragmentManager)
        adapter.addFragment(TestCapture(), "Capt")
        adapter.addFragment(TestAnswer(), "Ans")

        adapter.addFragment(TestScore(), "Score")
        adapter.addFragment(TestCheck(), "Check")
        adapter.addFragment(TestPicture(), "Pic")
        //        adapter.addFragment((), "Folder")

        viewPager.adapter = adapter
        theTab.setupWithViewPager(viewPager)
    }
}