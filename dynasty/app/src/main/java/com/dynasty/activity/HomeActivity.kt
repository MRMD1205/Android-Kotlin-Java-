package com.dynasty.activity

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.dynasty.R
import com.dynasty.base.BaseActivity
import com.dynasty.fragment.BusinessFragment
import com.dynasty.fragment.CategoryFragment
import com.dynasty.util.BUSINESS_FRAGMENT
import com.dynasty.util.CATEGORY_FRAGMENT
import com.dynasty.util.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_bottom_tabs.view.*


class HomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var categoryFragment: CategoryFragment
    private lateinit var businessFragment: BusinessFragment
    var doubleBackToExitPressedOnce = false

    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initViews() {
        categoryFragment = CategoryFragment()
        businessFragment = BusinessFragment("0")
        setNavigationItem()
        Utils.showNoInternet(this)
    }

    override fun setListeners() {
    }

    private fun setNavigationItem() {
        bottomTabs.ivCategory.setImageResource(R.drawable.ic_home_selected)
        bottomTabs.ivBusiness.setImageResource(R.drawable.ic_business)
        navigateToFragment(categoryFragment, false, CATEGORY_FRAGMENT)
    }

    override fun onClick(view: View?) {
        super.onClick(view)
        when (view!!.id) {
            R.id.ivCategory -> {
                Log.e("NAv item", "menu_category Clicked")
                bottomTabs.ivCategory.setImageResource(R.drawable.ic_home_selected)
                bottomTabs.ivBusiness.setImageResource(R.drawable.ic_business)
                navigateToFragment(categoryFragment, false, CATEGORY_FRAGMENT)
            }
            R.id.ivHome -> {
                Log.e("NAv item", "menu_home Clicked")
                bottomTabs.ivCategory.setImageResource(R.drawable.ic_home_selected)
                bottomTabs.ivBusiness.setImageResource(R.drawable.ic_business)
                navigateToFragment(categoryFragment, false, CATEGORY_FRAGMENT)
            }
            R.id.ivBusiness -> {
                Log.e("NAv item", "menu_business Clicked")
                bottomTabs.ivCategory.setImageResource(R.drawable.ic_home)
                bottomTabs.ivBusiness.setImageResource(R.drawable.ic_business_selected)
                navigateToFragment(businessFragment, false, BUSINESS_FRAGMENT)
            }
        }
    }

    override fun onBackPressed() {
        try {
            if(supportFragmentManager.backStackEntryCount == 0) {
                if(businessFragment.isVisible) {
                    bottomTabs.ivCategory.setImageResource(R.drawable.ic_home_selected)
                    bottomTabs.ivBusiness.setImageResource(R.drawable.ic_business)
                    navigateToFragment(categoryFragment, false, CATEGORY_FRAGMENT)
                } else if(categoryFragment.isVisible) {
                    if(doubleBackToExitPressedOnce) {
                        super.onBackPressed()
                        return
                    }
                    this.doubleBackToExitPressedOnce = true
                    Snackbar.make(findViewById(android.R.id.content), "Press once again to exit", Snackbar.LENGTH_LONG).show()
                    Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
                }
            }
            else
            {
                super.onBackPressed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}