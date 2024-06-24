

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AttendanceTabAdapter(supportFragmentManager: FragmentManager) :
    FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTit1eList = ArrayList<String>()

    override fun getCount(): Int {
        return mFragmentList.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
        Log.e("PPP", mFragmentTit1eList[position])
        return mFragmentTit1eList[position]
    }


    override fun getItem(position: Int): Fragment {
        Log.e("PPP20",position.toString())
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTit1eList.add(title)
    }

}

