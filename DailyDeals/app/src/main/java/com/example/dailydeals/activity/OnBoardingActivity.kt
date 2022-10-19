package com.example.dailydeals.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.dailydeals.R
import com.example.dailydeals.adapter.OnBoardingViewPagerAdapter
import com.example.dailydeals.base.BaseActivity
import com.example.dailydeals.model.OnBoardingData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class OnBoardingActivity : BaseActivity() {

    lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    lateinit var tabLayout: TabLayout
    private lateinit var onBoardingviewPager: ViewPager
    lateinit var buttonNext: Button
    lateinit var buttonGetStarted: Button
    lateinit var tvSkip: TextView
    lateinit var list: ArrayList<OnBoardingData>
    var btnAnim: Animation? = null
    var position: Int = 0
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun getLayoutResId(): Int {
        return R.layout.activity_onboarding
    }

    @SuppressLint("CommitPrefEdits")
    override fun initViews() {

        onBoardingviewPager = findViewById(R.id.screnViewPager)
        tabLayout = findViewById(R.id.tab_indicator)
        buttonNext = findViewById(R.id.btnNext)
        buttonGetStarted = findViewById(R.id.btnGetStarted)
        tvSkip = findViewById(R.id.tvSkip)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sharedPreferences.edit()

        list = ArrayList()
        list.add(
            OnBoardingData(
                "Order Your Items...",
                "Description Of First Title",
                R.drawable.ic_checklist
            )
        )
        list.add(
            OnBoardingData(
                "Choose Your Itmes",
                "Description Of Second Title",
                R.drawable.ic_choose_product
            )
        )
        list.add(
            OnBoardingData(
                "Choose Your Payment Options",
                "Description Of Third Title",
                R.drawable.ic_payment
            )
        )
        list.add(
            OnBoardingData(
                "Locate your shipping Adress",
                "Description Of Fourth Title",
                R.drawable.ic_delivery
            )
        )
        list.add(
            OnBoardingData(
                "Enjoy with your products",
                "Description Of Fifth Title",
                R.drawable.ic_box_gift
            )
        )
        list.add(
            OnBoardingData(
                "Customer Support",
                "Description Of Sixth Title",
                R.drawable.ic_helpline
            )
        )

        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, list)
        onBoardingviewPager.adapter = onBoardingViewPagerAdapter

        tabLayout.setupWithViewPager(onBoardingviewPager)

    }

    override fun setListeners() {
        buttonNext.setOnClickListener {
            position = onBoardingviewPager.currentItem
            if (position < list.size) {
                position++
                onBoardingviewPager.setCurrentItem(position)
            }

            if (position == list.size - 1) {
                loadScreen()
            }

        }
        tvSkip.setOnClickListener {

            editor.putBoolean("onBoardingComplete", true)
            editor.apply()

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        buttonGetStarted.setOnClickListener {

            editor.putBoolean("onBoardingComplete", true)
            editor.apply()

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == list.size - 1) {
                    loadScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun loadScreen() {
        buttonNext.visibility = View.GONE
        tabLayout.visibility = View.GONE
        buttonGetStarted.visibility = View.VISIBLE
        buttonGetStarted.animation = btnAnim
    }

}