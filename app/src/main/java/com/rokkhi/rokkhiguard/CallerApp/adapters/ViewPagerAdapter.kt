package com.rokkhi.rokkhiguard.CallerApp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.rokkhi.rokkhiguard.R
import com.rokkhi.rokkhiguard.CallerApp.activities.SimpleActivity
import com.rokkhi.rokkhiguard.CallerApp.fragments.MyViewPagerFragment

class ViewPagerAdapter(val activity: SimpleActivity) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = getFragment(position)
        val view = activity.layoutInflater.inflate(layout, container, false)
        container.addView(view)

        (view as MyViewPagerFragment).apply {
            setupFragment(activity)
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun getCount() = 2

    override fun isViewFromObject(view: View, item: Any) = view == item

    private fun getFragment(position: Int): Int {
        return when (position) {
            0 -> R.layout.fragment_contacts
//            1 -> R.layout.fragment_favorites
            else -> R.layout.fragment_recents
        }
    }
}
