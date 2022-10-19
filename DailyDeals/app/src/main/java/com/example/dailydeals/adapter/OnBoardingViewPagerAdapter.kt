package com.example.dailydeals.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.dailydeals.model.OnBoardingData
import com.example.dailydeals.R

class OnBoardingViewPagerAdapter(private var context:Context, private var onBoardingDataList: List<OnBoardingData>): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view == `object`
    }

    override fun getCount(): Int {
        return onBoardingDataList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_screen_layout,null)
        val imageView:ImageView
        val title:TextView
        val desc:TextView

        imageView = view.findViewById(R.id.introImg)
        title = view.findViewById(R.id.introTitle)
        desc = view.findViewById(R.id.introDesc)

        imageView.setImageResource(onBoardingDataList[position].screenImg)
        title.setText(onBoardingDataList[position].title)
        desc.setText(onBoardingDataList[position].desc)

        container.addView(view)
        return view

    }
}